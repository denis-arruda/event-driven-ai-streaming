# Event-Driven Architecture Powered by AI Agents

## Proof of Concept – Technical Specification

---

# 1. Purpose

This document defines the specification for a Proof of Concept (PoC) of an AI-driven semantic processing pipeline for a streaming platform.

The system processes newly published content through three LLM-based event processors. Each processor consumes a Kafka topic, enriches the event using an LLM agent implemented with LangChain4j, and republishes the accumulated message to the next topic.

All components:

* Use Java 25
* Use Quarkus
* Follow BCE (Boundary–Control–Entity) architecture
* Communicate exclusively via Kafka
* Accumulate previous agent results in the event payload

The final output is published to a Kafka topic for downstream systems.

---

# 2. High-Level Architecture

Flow of events:

content-published
→ Content Enrichment Processor
→ content-enriched
→ Sensitivity & Compliance Processor
→ content-sensitivity-analyzed
→ Marketing Narrative Processor
→ ai-content-finalized

All communication between components is asynchronous and topic-based. No synchronous HTTP calls are allowed between processors.

---

# 3. Architectural Principles

## 3.1 Event-Driven Choreography

* No central orchestrator.
* Each processor consumes exactly one topic and produces exactly one topic.
* Processors are independently deployable.

## 3.2 Event-Carried State Accumulation

* Each processor appends its result to the existing payload.
* Previous fields must not be modified.
* Events must be immutable.
* Correlation is based on contentId.

## 3.3 Immutability

Java 25 records must be used for domain entities and event models.

---

# 4. Kafka Topics

| Topic                        | Producer              | Consumer              |
| ---------------------------- | --------------------- | --------------------- |
| content-published            | CMS                   | Enrichment Processor  |
| content-enriched             | Enrichment Processor  | Sensitivity Processor |
| content-sensitivity-analyzed | Sensitivity Processor | Marketing Processor   |
| ai-content-finalized         | Marketing Processor   | Downstream Systems    |

Each processor must define a Dead Letter Topic (DLQ).

---

# 5. Event Model

## 5.1 Initial Event

Topic: content-published

Example payload:

```json
{
  "contentId": "S123",
  "title": "Shadow District",
  "description": "A political thriller set in a dystopian city.",
  "genre": "Thriller",
  "region": "GLOBAL",
  "timestamp": "2026-03-01T10:00:00Z"
}
```

---

# 6. Agent Processors

---

# 6.1 Content Enrichment Processor

## Input Topic

content-published

## Output Topic

content-enriched

## Responsibilities

* Analyze description and metadata
* Identify:

  * Narrative themes
  * Emotional tone
  * Audience profile
  * Advanced keywords
* Append enrichment data to payload

## Output Structure

```json
{
  "contentId": "S123",
  "title": "...",
  "description": "...",
  "genre": "...",
  "region": "...",
  "timestamp": "...",
  "enrichment": {
    "themes": ["corruption", "moral conflict"],
    "emotionalTone": "dark suspense",
    "audienceProfile": "adults 25-45",
    "keywords": ["political intrigue", "power struggle"]
  }
}
```

---

# 6.2 Sensitivity & Compliance Processor

## Input Topic

content-enriched

## Output Topic

content-sensitivity-analyzed

## Responsibilities

* Evaluate regulatory and cultural sensitivity risks
* Suggest:

  * Age rating
  * Region-specific restrictions
  * Sensitive content flags
* Use enrichment context as input

## Output Structure

```json
{
  "...": "previous fields",
  "sensitivity": {
    "ageRatingSuggested": "16+",
    "sensitiveRegions": ["DE", "IN"],
    "riskFlags": ["political corruption theme"]
  }
}
```

---

# 6.3 Marketing Narrative Processor

## Input Topic

content-sensitivity-analyzed

## Output Topic

ai-content-finalized

## Responsibilities

* Generate:

  * Global marketing headline
  * Region-adapted headlines
  * Tagline
  * Short promotional description
* Consider:

  * Enrichment metadata
  * Sensitivity constraints

## Output Structure

```json
{
  "...": "previous fields",
  "marketing": {
    "headlineGlobal": "In a city of shadows, trust is the ultimate risk.",
    "headlineDE": "A gripping tale of secrets and survival.",
    "tagline": "Power hides in the dark.",
    "shortDescription": "A suspense thriller about corruption and survival."
  }
}
```

The final topic ai-content-finalized contains the fully accumulated event.

---

# 7. BCE Architecture Requirements

Each processor must follow BCE.

## 7.1 Boundary Layer

* Kafka consumer (Reactive Messaging)
* Kafka producer
* Message validation
* Serialization and deserialization
* Exception handling mapping to DLQ

## 7.2 Control Layer

* Orchestrates:

  * Input validation
  * Invocation of LangChain4j agent
  * Mapping between entity models and prompt models
  * Fault tolerance handling
* Contains workflow logic

## 7.3 Entity Layer

* Immutable domain records
* Event payload models
* Agent result models
* No infrastructure dependencies

---

# 8. LangChain4j Requirements

Each processor must:

* Use the Quarkus LangChain4j extension
* Define a typed AI interface
* Use structured JSON output
* Configure:

  * Model name
  * Temperature
  * Timeout
  * Max tokens
* Validate structured response before publishing

Example conceptual interface:

```java
@AiService
public interface ContentEnrichmentAgent {
    EnrichmentResult analyze(ContentInput input);
}
```

---

# 9. Fault Tolerance

Each processor must implement:

* Retry with exponential backoff
* Circuit breaker
* Timeout configuration
* Dead Letter Topic for unrecoverable failures

Idempotency must be guaranteed by:

* Using contentId as correlation key
* Avoiding mutation of previous payload fields

---

# 10. Non-Functional Requirements

* Asynchronous processing only
* Independent deployability
* Simple logging
* Health endpoints
* Metrics exposure
* Observability integration
* Configurable LLM provider
* Token usage monitoring per processor

---

# 11. PoC Scope Limitations

For this Proof of Concept:

* No schema registry required
* No vector database integration
* No multi-region routing logic
* No external orchestration service
* Single Kafka cluster

---

# 12. Expected Outcome

The final topic ai-content-finalized must contain:

* Original content metadata
* Narrative enrichment
* Sensitivity analysis
* Marketing assets

This topic can be consumed by:

* Content management dashboards
* UI services
* Notification systems
* Analytics pipelines

---

# 13. Success Criteria for PoC

The PoC is considered successful if:

* All three processors can be deployed independently.
* Events flow correctly across topics.
* Each processor appends structured output.
* The final event contains accumulated results from all agents.
* The pipeline demonstrates cooperative LLM processing through Kafka.

---