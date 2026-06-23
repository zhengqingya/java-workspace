#!/usr/bin/env bash

set -euo pipefail

export HTTP_HOST="${HTTP_HOST:-0.0.0.0}"
export HTTP_PORT="${HTTP_PORT:-18083}"

if ! command -v php >/dev/null 2>&1; then
  echo "php command not found. Run: conda install -c conda-forge php" >&2
  exit 1
fi

exec php -S "${HTTP_HOST}:${HTTP_PORT}" -t public public/index.php
