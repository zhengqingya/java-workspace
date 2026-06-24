<?php

declare(strict_types=1);

final class Otel
{
    private const SPAN_KIND_SERVER = 2;
    private const SPAN_KIND_CLIENT = 3;
    private const STATUS_CODE_ERROR = 2;

    private string $endpoint;

    public function __construct(private readonly string $serviceName)
    {
        $this->endpoint = rtrim(getenv('OTEL_EXPORTER_OTLP_ENDPOINT') ?: 'http://127.0.0.1:4318', '/');
    }

    public function startServerSpan(string $method, string $path, string $query, string $traceparent): array
    {
        $parent = $this->parseTraceparent($traceparent);
        $traceId = $parent['trace_id'] ?? $this->randomHex(16);

        return [
            'trace_id' => $traceId,
            'span_id' => $this->randomHex(8),
            'parent_span_id' => $parent['span_id'] ?? null,
            'name' => sprintf('%s %s', $method, $path),
            'kind' => self::SPAN_KIND_SERVER,
            'start_time_unix_nano' => $this->nowNano(),
            'attributes' => [
                'http.request.method' => $method,
                'url.path' => $path,
                'url.query' => $query,
            ],
        ];
    }

    /**
     * 创建下游 HTTP 调用客户端 Span。
     */
    public function startClientSpan(string $name, array $parentSpan): array
    {
        return [
            'trace_id' => $parentSpan['trace_id'],
            'span_id' => $this->randomHex(8),
            'parent_span_id' => $parentSpan['span_id'],
            'name' => $name,
            'kind' => self::SPAN_KIND_CLIENT,
            'start_time_unix_nano' => $this->nowNano(),
            'attributes' => [],
        ];
    }

    /**
     * 给 Span 标记错误状态和异常属性。
     */
    public function markError(array $span, \Throwable $e): array
    {
        $span['status'] = [
            'code' => self::STATUS_CODE_ERROR,
            'message' => $e->getMessage(),
        ];
        $span['attributes']['exception.type'] = $e::class;
        $span['attributes']['exception.message'] = $e->getMessage();
        return $span;
    }

    public function exportSpan(array $span): void
    {
        $span['end_time_unix_nano'] = $this->nowNano();
        $spanPayload = [
            'traceId' => $span['trace_id'],
            'spanId' => $span['span_id'],
            'parentSpanId' => $span['parent_span_id'] ?? '',
            'name' => $span['name'],
            'kind' => $span['kind'],
            'startTimeUnixNano' => (string) $span['start_time_unix_nano'],
            'endTimeUnixNano' => (string) $span['end_time_unix_nano'],
            'attributes' => $this->attributes($span['attributes']),
        ];
        if (isset($span['status'])) {
            $spanPayload['status'] = $span['status'];
        }

        $payload = [
            'resourceSpans' => [[
                'resource' => ['attributes' => $this->resourceAttributes()],
                'scopeSpans' => [[
                    'scope' => ['name' => $this->serviceName],
                    'spans' => [$spanPayload],
                ]],
            ]],
        ];

        $this->post('/v1/traces', $payload);
    }

    public function exportLog(string $level, string $message, array $context, ?array $span): void
    {
        $payload = [
            'resourceLogs' => [[
                'resource' => ['attributes' => $this->resourceAttributes()],
                'scopeLogs' => [[
                    'scope' => ['name' => $this->serviceName],
                    'logRecords' => [[
                        'timeUnixNano' => (string) $this->nowNano(),
                        'severityText' => strtoupper($level),
                        'body' => ['stringValue' => $message],
                        'traceId' => $span['trace_id'] ?? '',
                        'spanId' => $span['span_id'] ?? '',
                        'attributes' => $this->attributes($context),
                    ]],
                ]],
            ]],
        ];

        $this->post('/v1/logs', $payload);
    }

    public function traceparent(array $span): string
    {
        return sprintf('00-%s-%s-01', $span['trace_id'], $span['span_id']);
    }

    private function parseTraceparent(string $traceparent): array
    {
        if (preg_match('/^00-([a-f0-9]{32})-([a-f0-9]{16})-[a-f0-9]{2}$/', $traceparent, $matches) !== 1) {
            return [];
        }
        return ['trace_id' => $matches[1], 'span_id' => $matches[2]];
    }

    private function post(string $path, array $payload): void
    {
        try {
            file_get_contents($this->endpoint . $path, false, stream_context_create([
                'http' => [
                    'method' => 'POST',
                    'header' => "Content-Type: application/json\r\n",
                    'content' => json_encode($payload, JSON_UNESCAPED_SLASHES | JSON_UNESCAPED_UNICODE),
                    'timeout' => 2,
                    'ignore_errors' => true,
                ],
            ]));
        } catch (\Throwable) {
        }
    }

    private function attributes(array $attributes): array
    {
        $result = [];
        foreach ($attributes as $key => $value) {
            $result[] = [
                'key' => (string) $key,
                'value' => $this->attributeValue($value),
            ];
        }
        return $result;
    }

    private function resourceAttributes(): array
    {
        return $this->attributes([
            'service.name' => $this->serviceName,
            'service.version' => '1.0.0',
            'service.namespace' => getenv('OTEL_SERVICE_NAMESPACE') ?: 'default',
            'deployment.environment' => getenv('DEPLOYMENT_ENVIRONMENT') ?: 'dev',
        ]);
    }

    private function attributeValue(mixed $value): array
    {
        return match (true) {
            is_bool($value) => ['boolValue' => $value],
            is_int($value) => ['intValue' => (string) $value],
            is_float($value) => ['doubleValue' => $value],
            default => ['stringValue' => (string) $value],
        };
    }

    private function randomHex(int $bytes): string
    {
        return bin2hex(random_bytes($bytes));
    }

    private function nowNano(): int
    {
        return (int) (microtime(true) * 1_000_000_000);
    }
}
