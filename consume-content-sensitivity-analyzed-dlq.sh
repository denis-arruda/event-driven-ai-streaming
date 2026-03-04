#!/usr/bin/env bash
# Consumes messages from the content-sensitivity-analyzed-dlq topic.
# Usage: ./consume-content-sensitivity-analyzed-dlq.sh [--latest]
set -euo pipefail
KAFKA_BOOTSTRAP="${KAFKA_BOOTSTRAP:-localhost:9094}"
OFFSET="--from-beginning"
[[ "${1:-}" == "--latest" ]] && OFFSET=""
echo "Consuming from content-sensitivity-analyzed-dlq on ${KAFKA_BOOTSTRAP} (Ctrl+C to stop)..."
docker run --rm -it --network host apache/kafka:latest \
  /opt/kafka/bin/kafka-console-consumer.sh \
  --bootstrap-server "${KAFKA_BOOTSTRAP}" \
  --topic content-sensitivity-analyzed-dlq \
  ${OFFSET}
