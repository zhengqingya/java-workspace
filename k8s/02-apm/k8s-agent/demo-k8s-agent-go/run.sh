#!/usr/bin/env bash

set -euo pipefail

export HTTP_ADDR="${HTTP_ADDR:-:18082}"
export LOG_LEVEL="${LOG_LEVEL:-info}"

exec go run .
