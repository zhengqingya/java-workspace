package main

import (
	"context"
	"encoding/json"
	"errors"
	"fmt"
	"log/slog"
	"net/http"
	"os"
	"os/signal"
	"strconv"
	"strings"
	"syscall"
	"time"

	"go.opentelemetry.io/contrib/bridges/otelslog"
	"go.opentelemetry.io/contrib/instrumentation/net/http/otelhttp"
	"go.opentelemetry.io/otel"
	"go.opentelemetry.io/otel/attribute"
	"go.opentelemetry.io/otel/exporters/otlp/otlplog/otlploggrpc"
	"go.opentelemetry.io/otel/exporters/otlp/otlpmetric/otlpmetricgrpc"
	"go.opentelemetry.io/otel/exporters/otlp/otlptrace/otlptracegrpc"
	"go.opentelemetry.io/otel/log/global"
	"go.opentelemetry.io/otel/propagation"
	sdklog "go.opentelemetry.io/otel/sdk/log"
	sdkmetric "go.opentelemetry.io/otel/sdk/metric"
	"go.opentelemetry.io/otel/sdk/resource"
	sdktrace "go.opentelemetry.io/otel/sdk/trace"
	"go.opentelemetry.io/otel/trace"
)

const (
	serviceName    = "demo-k8s-otel-go"
	serviceVersion = "1.0.0"
	defaultAddr    = ":18082"
	defaultOTLPEnd = "http://127.0.0.1:4317"
)

type telemetryShutdown func(context.Context) error

func main() {
	ctx := context.Background()
	logger, shutdown, err := initTelemetry(ctx)
	if err != nil {
		fmt.Fprintf(os.Stderr, "init telemetry: %v\n", err)
		os.Exit(1)
	}
	defer func() {
		shutdownCtx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
		defer cancel()
		if err := shutdown(shutdownCtx); err != nil {
			fmt.Fprintf(os.Stderr, "shutdown telemetry: %v\n", err)
		}
	}()

	addr := getenv("HTTP_ADDR", defaultAddr)
	server := &http.Server{
		Addr:              addr,
		Handler:           newHandler(logger),
		ReadHeaderTimeout: 5 * time.Second,
	}

	errCh := make(chan error, 1)
	go func() {
		logger.InfoContext(ctx, "http server starting", slog.String("addr", addr))
		errCh <- server.ListenAndServe()
	}()

	stopCh := make(chan os.Signal, 1)
	signal.Notify(stopCh, syscall.SIGINT, syscall.SIGTERM)

	select {
	case sig := <-stopCh:
		logger.InfoContext(ctx, "shutdown signal received", slog.String("signal", sig.String()))
	case err := <-errCh:
		if !errors.Is(err, http.ErrServerClosed) {
			logger.ErrorContext(ctx, "http server stopped", slog.Any("error", err))
			os.Exit(1)
		}
	}

	shutdownCtx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()
	if err := server.Shutdown(shutdownCtx); err != nil {
		logger.ErrorContext(ctx, "http server shutdown failed", slog.Any("error", err))
	}
}

func newHandler(logger *slog.Logger) http.Handler {
	mux := http.NewServeMux()
	httpClient := &http.Client{
		Timeout:   5 * time.Second,
		Transport: otelhttp.NewTransport(http.DefaultTransport),
	}

	mux.HandleFunc("GET /health", func(w http.ResponseWriter, r *http.Request) {
		logger.InfoContext(r.Context(), "health request")
		writeJSON(w, http.StatusOK, map[string]string{
			"status":  "ok",
			"service": serviceName,
		})
	})

	mux.HandleFunc("GET /hello", func(w http.ResponseWriter, r *http.Request) {
		name := r.URL.Query().Get("name")
		if name == "" {
			name = "go"
		}

		span := trace.SpanFromContext(r.Context())
		span.SetAttributes(attribute.String("app.hello.name", name))
		logger.InfoContext(r.Context(), "hello request", slog.String("name", name))
		writeJSON(w, http.StatusOK, map[string]string{
			"message":  "hello, " + name,
			"service":  serviceName,
			"trace_id": span.SpanContext().TraceID().String(),
			"span_id":  span.SpanContext().SpanID().String(),
		})
	})

	mux.HandleFunc("GET /trace", func(w http.ResponseWriter, r *http.Request) {
		ctx, span := otel.Tracer(serviceName).Start(r.Context(), "build-trace-response")
		defer span.End()

		step := r.URL.Query().Get("step")
		if step == "" {
			step = "k8s"
		}
		span.SetAttributes(attribute.String("app.trace.step", step))
		logger.InfoContext(ctx, "trace request handled", slog.String("step", step))

		writeJSON(w, http.StatusOK, map[string]string{
			"message":  "trace generated",
			"service":  serviceName,
			"step":     step,
			"trace_id": span.SpanContext().TraceID().String(),
			"span_id":  span.SpanContext().SpanID().String(),
		})
	})

	mux.HandleFunc("GET /chain", func(w http.ResponseWriter, r *http.Request) {
		targetName := strings.TrimSpace(r.URL.Query().Get("targetName"))
		if targetName == "" {
			targetName = "target"
		}
		targetURL := strings.TrimSpace(r.URL.Query().Get("targetUrl"))
		logger.InfoContext(r.Context(), "chain request", slog.String("target_name", targetName), slog.String("target_url", targetURL))

		span := trace.SpanFromContext(r.Context())
		result := map[string]any{
			"service":     serviceName,
			"message":     "go -> " + targetName,
			"target_name": targetName,
			"target_url":  targetURL,
			"trace_id":    span.SpanContext().TraceID().String(),
			"span_id":     span.SpanContext().SpanID().String(),
		}
		if targetURL == "" {
			writeJSON(w, http.StatusOK, result)
			return
		}

		req, err := http.NewRequestWithContext(r.Context(), http.MethodGet, targetURL, nil)
		if err != nil {
			http.Error(w, err.Error(), http.StatusInternalServerError)
			return
		}

		resp, err := httpClient.Do(req)
		if err != nil {
			http.Error(w, err.Error(), http.StatusBadGateway)
			return
		}
		defer resp.Body.Close()

		var downstream map[string]any
		if err := json.NewDecoder(resp.Body).Decode(&downstream); err != nil {
			http.Error(w, err.Error(), http.StatusBadGateway)
			return
		}

		result["downstream"] = downstream
		writeJSON(w, http.StatusOK, result)
	})

	return otelhttp.NewHandler(mux, serviceName)
}

func initTelemetry(ctx context.Context) (*slog.Logger, telemetryShutdown, error) {
	setDefaultOTLPEnv()

	res, err := resource.New(
		ctx,
		resource.WithFromEnv(),
		resource.WithTelemetrySDK(),
		resource.WithAttributes(
			attribute.String("service.name", getenv("OTEL_SERVICE_NAME", serviceName)),
			attribute.String("service.version", serviceVersion),
			attribute.String("service.namespace", getenv("OTEL_SERVICE_NAMESPACE", "default")),
			attribute.String("deployment.environment", getenv("DEPLOYMENT_ENVIRONMENT", "dev")),
		),
	)
	if err != nil {
		return nil, nil, err
	}

	traceExporter, err := otlptracegrpc.New(ctx)
	if err != nil {
		return nil, nil, err
	}
	tracerProvider := sdktrace.NewTracerProvider(
		sdktrace.WithBatcher(traceExporter),
		sdktrace.WithResource(res),
	)
	otel.SetTracerProvider(tracerProvider)
	otel.SetTextMapPropagator(propagation.NewCompositeTextMapPropagator(
		propagation.TraceContext{},
		propagation.Baggage{},
	))

	metricExporter, err := otlpmetricgrpc.New(ctx)
	if err != nil {
		return nil, nil, err
	}
	meterProvider := sdkmetric.NewMeterProvider(
		sdkmetric.WithReader(sdkmetric.NewPeriodicReader(
			metricExporter,
			sdkmetric.WithInterval(metricExportInterval()),
		)),
		sdkmetric.WithResource(res),
	)
	otel.SetMeterProvider(meterProvider)

	logExporter, err := otlploggrpc.New(ctx)
	if err != nil {
		return nil, nil, err
	}
	loggerProvider := sdklog.NewLoggerProvider(
		sdklog.WithProcessor(sdklog.NewBatchProcessor(logExporter)),
		sdklog.WithResource(res),
	)
	global.SetLoggerProvider(loggerProvider)

	consoleHandler := &traceLogHandler{
		next: slog.NewJSONHandler(os.Stdout, &slog.HandlerOptions{
			Level: logLevelFromEnv(),
		}),
	}
	otelHandler := otelslog.NewHandler(
		serviceName,
		otelslog.WithLoggerProvider(loggerProvider),
		otelslog.WithSource(true),
	)
	logger := slog.New(multiHandler{consoleHandler, otelHandler})
	slog.SetDefault(logger)

	return logger, func(ctx context.Context) error {
		return errors.Join(
			loggerProvider.Shutdown(ctx),
			meterProvider.Shutdown(ctx),
			tracerProvider.Shutdown(ctx),
		)
	}, nil
}

func setDefaultOTLPEnv() {
	if os.Getenv("OTEL_SERVICE_NAME") == "" {
		_ = os.Setenv("OTEL_SERVICE_NAME", serviceName)
	}
	if os.Getenv("OTEL_EXPORTER_OTLP_ENDPOINT") == "" &&
		os.Getenv("OTEL_EXPORTER_OTLP_TRACES_ENDPOINT") == "" &&
		os.Getenv("OTEL_EXPORTER_OTLP_METRICS_ENDPOINT") == "" &&
		os.Getenv("OTEL_EXPORTER_OTLP_LOGS_ENDPOINT") == "" {
		_ = os.Setenv("OTEL_EXPORTER_OTLP_ENDPOINT", defaultOTLPEnd)
	}
	if os.Getenv("OTEL_EXPORTER_OTLP_PROTOCOL") == "" {
		_ = os.Setenv("OTEL_EXPORTER_OTLP_PROTOCOL", "grpc")
	}
}

type multiHandler []slog.Handler

func (h multiHandler) Enabled(ctx context.Context, level slog.Level) bool {
	for _, handler := range h {
		if handler.Enabled(ctx, level) {
			return true
		}
	}
	return false
}

func (h multiHandler) Handle(ctx context.Context, record slog.Record) error {
	var err error
	for _, handler := range h {
		if handler.Enabled(ctx, record.Level) {
			err = errors.Join(err, handler.Handle(ctx, record.Clone()))
		}
	}
	return err
}

func (h multiHandler) WithAttrs(attrs []slog.Attr) slog.Handler {
	handlers := make(multiHandler, len(h))
	for i, handler := range h {
		handlers[i] = handler.WithAttrs(attrs)
	}
	return handlers
}

func (h multiHandler) WithGroup(name string) slog.Handler {
	handlers := make(multiHandler, len(h))
	for i, handler := range h {
		handlers[i] = handler.WithGroup(name)
	}
	return handlers
}

type traceLogHandler struct {
	next slog.Handler
}

func (h *traceLogHandler) Enabled(ctx context.Context, level slog.Level) bool {
	return h.next.Enabled(ctx, level)
}

func (h *traceLogHandler) Handle(ctx context.Context, record slog.Record) error {
	spanContext := trace.SpanContextFromContext(ctx)
	if spanContext.IsValid() {
		record.AddAttrs(
			slog.String("trace_id", spanContext.TraceID().String()),
			slog.String("span_id", spanContext.SpanID().String()),
			slog.String("trace_flags", spanContext.TraceFlags().String()),
		)
	}
	return h.next.Handle(ctx, record)
}

func (h *traceLogHandler) WithAttrs(attrs []slog.Attr) slog.Handler {
	return &traceLogHandler{next: h.next.WithAttrs(attrs)}
}

func (h *traceLogHandler) WithGroup(name string) slog.Handler {
	return &traceLogHandler{next: h.next.WithGroup(name)}
}

func writeJSON(w http.ResponseWriter, status int, payload any) {
	w.Header().Set("Content-Type", "application/json; charset=utf-8")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(payload); err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
	}
}

func getenv(key string, fallback string) string {
	value := strings.TrimSpace(os.Getenv(key))
	if value == "" {
		return fallback
	}
	return value
}

func logLevelFromEnv() slog.Leveler {
	switch strings.ToLower(getenv("LOG_LEVEL", "info")) {
	case "debug":
		return slog.LevelDebug
	case "warn":
		return slog.LevelWarn
	case "error":
		return slog.LevelError
	default:
		if level, err := strconv.Atoi(os.Getenv("LOG_LEVEL")); err == nil {
			return slog.Level(level)
		}
		return slog.LevelInfo
	}
}

func metricExportInterval() time.Duration {
	interval, err := strconv.Atoi(getenv("OTEL_METRIC_EXPORT_INTERVAL", "10000"))
	if err != nil || interval <= 0 {
		return 10 * time.Second
	}
	return time.Duration(interval) * time.Millisecond
}
