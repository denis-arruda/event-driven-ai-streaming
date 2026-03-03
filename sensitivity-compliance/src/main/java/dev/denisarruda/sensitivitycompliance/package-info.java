/**
 * Sensitivity &amp; Compliance processor — consumes {@code content-enriched} Kafka events,
 * invokes an LLM agent to evaluate age ratings, regional restrictions and risk flags,
 * then publishes the accumulated result to {@code content-sensitivity-analyzed}.
 *
 * <p>Follows the Boundary–Control–Entity pattern; fault tolerance (retry, circuit breaker,
 * timeout) is applied at the control layer. Dead-letter routing is handled by the Kafka connector.
 */
package dev.denisarruda.sensitivitycompliance;
