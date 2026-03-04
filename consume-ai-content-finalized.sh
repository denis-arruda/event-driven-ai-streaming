#!/usr/bin/env bash
# Consumes messages from the ai-content-finalized topic (final pipeline output).
# Usage: ./consume-ai-content-finalized.sh [--latest]
set -euo pipefail
KAFKA_BOOTSTRAP="${KAFKA_BOOTSTRAP:-localhost:9094}"
OFFSET="--from-beginning"
[[ "${1:-}" == "--latest" ]] && OFFSET=""
echo "Consuming from ai-content-finalized on ${KAFKA_BOOTSTRAP} (Ctrl+C to stop)..."
docker run --rm -it --network host apache/kafka:latest \
  /opt/kafka/bin/kafka-console-consumer.sh \
  --bootstrap-server "${KAFKA_BOOTSTRAP}" \
  --topic ai-content-finalized \
  ${OFFSET}
