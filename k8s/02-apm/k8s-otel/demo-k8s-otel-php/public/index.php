<?php

declare(strict_types=1);

require dirname(__DIR__) . '/src/Otel.php';

// 1、初始化 OTel 上报器，并为当前 HTTP 请求创建服务端 Span。
$serviceName = getenv('OTEL_SERVICE_NAME') ?: 'demo-k8s-otel-php';
$otel = new Otel($serviceName);
$span = $otel->startServerSpan($_SERVER['REQUEST_METHOD'] ?? 'GET', requestPath(), $_SERVER['QUERY_STRING'] ?? '', $_SERVER['HTTP_TRACEPARENT'] ?? '');

try {
    // 2、执行业务路由，结束时补充响应状态并导出服务端 Span。
    route($otel, $span, $serviceName);
    $span['attributes']['http.response.status_code'] = http_response_code();
    $otel->exportSpan($span);
} catch (Throwable $e) {
    http_response_code(500);
    $span['attributes']['http.response.status_code'] = 500;
    $span = $otel->markError($span, $e);
    $otel->exportSpan($span);
    logMessage($otel, 'error', 'request failed', ['exception' => $e::class, 'message' => $e->getMessage()], $span, $serviceName);
    jsonResponse(['error' => 'internal server error']);
}

function route(Otel $otel, array $span, string $serviceName): void
{
    $path = requestPath();

    if ($path === '/health') {
        logMessage($otel, 'info', 'health request', [], $span, $serviceName);
        jsonResponse(['status' => 'ok', 'service' => $serviceName]);
        return;
    }

    if ($path === '/hello') {
        $name = (string) ($_GET['name'] ?? 'php');
        logMessage($otel, 'info', sprintf('hello request, name=%s', $name), ['name' => $name], $span, $serviceName);
        jsonResponse([
            'message' => sprintf('hello, %s', $name),
            'service' => $serviceName,
            'trace_id' => $span['trace_id'],
            'span_id' => $span['span_id'],
        ]);
        return;
    }

    if ($path === '/chain') {
        $targetName = trim((string) ($_GET['targetName'] ?? 'target')) ?: 'target';
        $targetUrl = trim((string) ($_GET['targetUrl'] ?? ''));
        logMessage($otel, 'info', sprintf('chain request, targetName=%s, targetUrl=%s', $targetName, $targetUrl), [
            'target_name' => $targetName,
            'target_url' => $targetUrl,
        ], $span, $serviceName);

        $result = [
            'service' => $serviceName,
            'message' => sprintf('php -> %s', $targetName),
            'target_name' => $targetName,
            'target_url' => $targetUrl,
            'trace_id' => $span['trace_id'],
            'span_id' => $span['span_id'],
        ];
        if ($targetUrl === '') {
            jsonResponse($result);
            return;
        }

        // 3、调用下游时创建客户端 Span，并通过 traceparent 继续透传链路上下文。
        $clientSpan = $otel->startClientSpan(sprintf('HTTP GET %s', $targetName), $span);
        $clientSpan['attributes']['http.request.method'] = 'GET';
        $clientSpan['attributes']['url.full'] = $targetUrl;

        try {
            $result['downstream'] = httpGetJson($targetUrl, [
                'traceparent: ' . $otel->traceparent($clientSpan),
            ]);
            $clientSpan['attributes']['http.response.status_code'] = 200;
            $otel->exportSpan($clientSpan);
        } catch (Throwable $e) {
            $clientSpan['attributes']['http.response.status_code'] = 502;
            $clientSpan = $otel->markError($clientSpan, $e);
            $otel->exportSpan($clientSpan);
            throw $e;
        }
        jsonResponse($result);
        return;
    }

    http_response_code(404);
    jsonResponse(['error' => 'not found']);
}

function requestPath(): string
{
    return parse_url($_SERVER['REQUEST_URI'] ?? '/', PHP_URL_PATH) ?: '/';
}

function jsonResponse(array $payload): void
{
    header('Content-Type: application/json; charset=utf-8');
    echo json_encode($payload, JSON_UNESCAPED_SLASHES | JSON_UNESCAPED_UNICODE);
}

function logMessage(Otel $otel, string $level, string $message, array $context, ?array $span, string $serviceName): void
{
    $traceId = $span['trace_id'] ?? '0';
    $spanId = $span['span_id'] ?? '0';
    error_log(sprintf('[service=%s trace_id=%s span_id=%s] %s %s', $serviceName, $traceId, $spanId, $level, $message));
    $otel->exportLog($level, $message, $context, $span);
}

function httpGetJson(string $url, array $headers): mixed
{
    $response = file_get_contents($url, false, stream_context_create([
        'http' => [
            'method' => 'GET',
            'timeout' => 5,
            'header' => implode("\r\n", $headers),
        ],
    ]));

    if ($response === false) {
        throw new RuntimeException(sprintf('GET %s failed', $url));
    }

    return json_decode($response, true);
}
