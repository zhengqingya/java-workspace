#!/usr/bin/env bash

set -euo pipefail

export HTTP_HOST="${HTTP_HOST:-0.0.0.0}"
export HTTP_PORT="${HTTP_PORT:-18081}"
export LOG_LEVEL="${LOG_LEVEL:-INFO}"

exec python app.py
