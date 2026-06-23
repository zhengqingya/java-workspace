package main

import (
	"context"
	"encoding/json"
	"errors"
	"net/http"
	"os"
	"os/signal"
	"strconv"
	"strings"
	"syscall"
	"time"

	swtrace "github.com/apache/skywalking-go/toolkit/trace"
	"go.uber.org/zap"
	"go.uber.org/zap/zapcore"
)

const (
	defaultAddr = ":18082"
)

func main() {
	logger := newLogger()
	defer func() {
		_ = logger.Sync()
	}()

	addr := getenv("HTTP_ADDR", defaultAddr)
	server := &http.Server{
		Addr:              addr,
		Handler:           newHandler(logger),
		ReadHeaderTimeout: 5 * time.Second,
	}

	errCh := make(chan error, 1)
	go func() {
		logger.Info("http server starting", zap.String("addr", addr))
		errCh <- server.ListenAndServe()
	}()

	stopCh := make(chan os.Signal, 1)
	signal.Notify(stopCh, syscall.SIGINT, syscall.SIGTERM)

	select {
	case sig := <-stopCh:
		logger.Info("shutdown signal received", zap.String("signal", sig.String()))
	case err := <-errCh:
		if !errors.Is(err, http.ErrServerClosed) {
			logger.Error("http server stopped", zap.Error(err))
			os.Exit(1)
		}
	}

	shutdownCtx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()
	if err := server.Shutdown(shutdownCtx); err != nil {
		logger.Error("http server shutdown failed", zap.Error(err))
	}
}

func newHandler(logger *zap.Logger) http.Handler {
	mux := http.NewServeMux()
	httpClient := &http.Client{
		Timeout: 5 * time.Second,
	}
	serviceName := getenv("SW_AGENT_NAME", "demo-k8s-agent-go")

	mux.HandleFunc("GET /hello", func(w http.ResponseWriter, r *http.Request) {
		name := r.URL.Query().Get("name")
		if name == "" {
			name = "go"
		}

		traceContext := currentTraceContext()
		logger.Info("hello request", append(traceContext.zapFields(), zap.String("name", name))...)
		writeJSON(w, http.StatusOK, map[string]string{
			"message":  "hello, " + name,
			"service":  serviceName,
			"trace_id": traceContext.TraceID,
		})
	})

	mux.HandleFunc("GET /chain", func(w http.ResponseWriter, r *http.Request) {
		targetName := strings.TrimSpace(r.URL.Query().Get("targetName"))
		if targetName == "" {
			targetName = "target"
		}
		targetURL := strings.TrimSpace(r.URL.Query().Get("targetUrl"))
		traceContext := currentTraceContext()
		logger.Info("chain request", append(traceContext.zapFields(), zap.String("target_name", targetName), zap.String("target_url", targetURL))...)

		result := map[string]any{
			"service":     serviceName,
			"message":     "go -> " + targetName,
			"target_name": targetName,
			"target_url":  targetURL,
			"trace_id":    traceContext.TraceID,
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

	return mux
}

type traceContext struct {
	TraceID   string
	SegmentID string
	SpanID    string
}

func currentTraceContext() traceContext {
	// 1、SkyWalking Go Agent 增强后，toolkit 会返回真实 trace 上下文。
	traceID := strings.TrimSpace(swtrace.GetTraceID())
	segmentID := strings.TrimSpace(swtrace.GetSegmentID())
	spanID := strconv.FormatInt(int64(swtrace.GetSpanID()), 10)

	// 2、未通过 go-agent 增强直接运行时，返回 N/A，接口仍可用于基础验证。
	if traceID == "" {
		traceID = "N/A"
	}
	if segmentID == "" {
		segmentID = "N/A"
	}
	if spanID == "-1" {
		spanID = "N/A"
	}

	// 3、响应和日志统一使用 trace_id，方便和 Java/Python/PHP 对齐。
	return traceContext{
		TraceID:   traceID,
		SegmentID: segmentID,
		SpanID:    spanID,
	}
}

func (ctx traceContext) zapFields() []zap.Field {
	return []zap.Field{
		zap.String("trace_id", ctx.TraceID),
		zap.String("segment_id", ctx.SegmentID),
		zap.String("span_id", ctx.SpanID),
	}
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

func newLogger() *zap.Logger {
	// 1、使用 zap 作为应用日志框架，便于 SkyWalking Go Agent 日志插件识别并接管。
	config := zap.NewProductionConfig()
	config.EncoderConfig.TimeKey = "time"
	config.EncoderConfig.LevelKey = "level"
	config.EncoderConfig.MessageKey = "msg"
	config.EncoderConfig.EncodeTime = zapcore.ISO8601TimeEncoder

	// 2、从 LOG_LEVEL 环境变量读取日志级别，保持本地和 K8s 配置一致。
	config.Level = logLevelFromEnv()

	// 3、构建失败时回退到 production logger，避免日志初始化影响 demo 服务启动。
	logger, err := config.Build()
	if err != nil {
		fallback, _ := zap.NewProduction()
		return fallback
	}
	return logger
}

func logLevelFromEnv() zap.AtomicLevel {
	switch strings.ToLower(getenv("LOG_LEVEL", "info")) {
	case "debug":
		return zap.NewAtomicLevelAt(zap.DebugLevel)
	case "warn":
		return zap.NewAtomicLevelAt(zap.WarnLevel)
	case "error":
		return zap.NewAtomicLevelAt(zap.ErrorLevel)
	default:
		return zap.NewAtomicLevelAt(zap.InfoLevel)
	}
}
