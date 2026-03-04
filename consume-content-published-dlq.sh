#!/usr/bin/env bash
# Consumes messages from the content-published-dlq topic.
# Usage: ./consume-content-published-dlq.sh [--latest]
set -euo pipefail
KAFKA_BOOTSTRAP="${KAFKA_BOOTSTRAP:-localhost:9094}"
OFFSET="--from-beginning"
[[ "${1:-}" == "--latest" ]] && OFFSET=""
echo "Consuming from content-published-dlq on ${KAFKA_BOOTSTRAP} (Ctrl+C to stop)..."
docker run --rm -it --network host apache/kafka:latest \
  /opt/kafka/bin/kafka-console-consumer.sh \
  --bootstrap-server "${KAFKA_BOOTSTRAP}" \
  --topic content-published-dlq \
  ${OFFSET}
