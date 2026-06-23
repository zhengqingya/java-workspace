#!/usr/bin/env bash

set -euo pipefail

export HTTP_HOST="${HTTP_HOST:-0.0.0.0}"
export HTTP_PORT="${HTTP_PORT:-18083}"
export SW_AGENT_NAME="${SW_AGENT_NAME:-demo-k8s-agent-php}"
export SW_AGENT_COLLECTOR_BACKEND_SERVICES="${SW_AGENT_COLLECTOR_BACKEND_SERVICES:-127.0.0.1:11800}"

if ! command -v php >/dev/null 2>&1; then
  echo "php command not found. Run: conda install -c conda-forge php" >&2
  exit 1
fi

if [ ! -f vendor/autoload.php ]; then
  if ! command -v composer >/dev/null 2>&1; then
    echo "vendor/autoload.php not found. Run: composer install" >&2
    exit 1
  fi

  composer install --no-dev --optimize-autoloader --no-interaction
fi

exec php \
  -d skywalking_agent.enable=On \
  -d skywalking_agent.reporter_type=grpc \
  -d "skywalking_agent.server_addr=${SW_AGENT_COLLECTOR_BACKEND_SERVICES}" \
  -d "skywalking_agent.service_name=${SW_AGENT_NAME}" \
  -S "${HTTP_HOST}:${HTTP_PORT}" \
  -t public public/index.php
