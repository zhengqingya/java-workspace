<?php

declare(strict_types=1);

use Monolog\Handler\StreamHandler;
use Monolog\Level;
use Monolog\Logger;
use Psr\Log\LoggerInterface;

require_once __DIR__ . '/../vendor/autoload.php';

$serviceName = getenv('SW_AGENT_NAME') ?: 'demo-k8s-agent-php';
$logger = createLogger($serviceName);

try {
    route($serviceName, $logger);
} catch (Throwable $e) {
    http_response_code(500);
    $logger->error('request failed', [
        'service' => $serviceName,
        'error' => $e->getMessage(),
    ]);
    jsonResponse(['error' => 'internal server error']);
}

function route(string $serviceName, LoggerInterface $logger): void
{
    $path = requestPath();

    if ($path === '/health') {
        $logger->info('health request', [
            'service' => $serviceName,
            'trace_id' => currentTraceId(),
        ]);
        jsonResponse(['status' => 'ok', 'service' => $serviceName]);
        return;
    }

    if ($path === '/hello') {
        $name = (string) ($_GET['name'] ?? 'php');
        $traceId = currentTraceId();
        $logger->info('hello request', [
            'service' => $serviceName,
            'trace_id' => $traceId,
            'name' => $name,
        ]);
        jsonResponse([
            'message' => sprintf('hello, %s', $name),
            'service' => $serviceName,
            'trace_id' => $traceId,
        ]);
        return;
    }

    if ($path === '/chain') {
        $targetName = trim((string) ($_GET['targetName'] ?? 'target')) ?: 'target';
        $targetUrl = trim((string) ($_GET['targetUrl'] ?? ''));
        $traceId = currentTraceId();
        $logger->info('chain request', [
            'service' => $serviceName,
            'trace_id' => $traceId,
            'target_name' => $targetName,
            'target_url' => $targetUrl,
        ]);

        $result = [
            'service' => $serviceName,
            'message' => sprintf('php -> %s', $targetName),
            'target_name' => $targetName,
            'target_url' => $targetUrl,
            'trace_id' => $traceId,
        ];
        if ($targetUrl === '') {
            jsonResponse($result);
            return;
        }

        $result['downstream'] = httpGetJson($targetUrl);
        jsonResponse($result);
        return;
    }

    http_response_code(404);
    jsonResponse(['error' => 'not found']);
}

function createLogger(string $serviceName): LoggerInterface
{
    // 1、使用 Monolog 的 PSR-3 LoggerInterface，便于 skywalking_agent 按 psr_logging_level hook 日志。
    $logger = new Logger($serviceName);

    // 2、日志仍输出到 stderr，方便容器本地排查；SkyWalking 原生上报由 PHP Agent 接管。
    $logger->pushHandler(new StreamHandler('php://stderr', Level::Info));

    // 3、返回统一 logger，业务入口只通过 PSR-3 写日志。
    return $logger;
}

function requestPath(): string
{
    return parse_url($_SERVER['REQUEST_URI'] ?? '/', PHP_URL_PATH) ?: '/';
}

function currentTraceId(): string
{
    // 1、skywalking_agent 会把当前请求的 trace id 注入到 $_SERVER['SW_TRACE_ID']。
    // 2、本地未加载扩展时返回 N/A，接口仍可用于基础连通性验证。
    // 3、接口响应和日志统一使用 trace_id，方便和 Java/Python 对齐。
    return $_SERVER['SW_TRACE_ID'] ?? 'N/A';
}

function jsonResponse(array $payload): void
{
    header('Content-Type: application/json; charset=utf-8');
    echo json_encode($payload, JSON_UNESCAPED_SLASHES | JSON_UNESCAPED_UNICODE);
}

function httpGetJson(string $url): mixed
{
    $response = file_get_contents($url, false, stream_context_create([
        'http' => [
            'method' => 'GET',
            'timeout' => 5,
        ],
    ]));

    if ($response === false) {
        throw new RuntimeException(sprintf('GET %s failed', $url));
    }

    return json_decode($response, true);
}
