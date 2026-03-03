/**
 * Content Enrichment processor — consumes {@code content-published} Kafka events,
 * invokes an LLM agent to extract themes, emotional tone, audience profile and keywords,
 * then publishes the accumulated result to {@code content-enriched}.
 *
 * <p>Follows the Boundary–Control–Entity pattern; fault tolerance (retry, circuit breaker,
 * timeout) is applied at the control layer. Dead-letter routing is handled by the Kafka connector.
 */
package dev.denisarruda.contentenrichment;
