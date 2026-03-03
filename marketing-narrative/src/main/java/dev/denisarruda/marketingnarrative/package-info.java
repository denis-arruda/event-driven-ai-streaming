/**
 * Marketing Narrative processor — consumes {@code content-sensitivity-analyzed} Kafka events,
 * invokes an LLM agent to generate headlines, taglines, and promotional descriptions,
 * then publishes the fully accumulated result to {@code ai-content-finalized}.
 *
 * <p>Follows the Boundary–Control–Entity pattern; fault tolerance (retry, circuit breaker,
 * timeout) is applied at the control layer. Dead-letter routing is handled by the Kafka connector.
 */
package dev.denisarruda.marketingnarrative;
