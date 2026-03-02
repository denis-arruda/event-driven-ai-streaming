# Event-Driven AI Streaming

Three independent Quarkus processors that enrich, analyze, and generate marketing assets for published content using LangChain4j (OpenAI gpt-4o) over Kafka.

**Pipeline:** `content-published` → `content-enriched` → `content-sensitivity-analyzed` → `ai-content-finalized`

## Modules

- [content-enrichment](content-enrichment/) — extracts themes, emotional tone, audience profile, and keywords
- [sensitivity-compliance](sensitivity-compliance/) — evaluates age ratings, regional restrictions, and risk flags
- [marketing-narrative](marketing-narrative/) — generates headlines, taglines, and promotional descriptions

## Running

Requires `LLM_API_KEY`. Kafka is provided by Quarkus Dev Services — no local setup needed.

```bash
export LLM_API_KEY=<your-openai-api-key>
cd content-enrichment && ../mvnw quarkus:dev
```

Replace `content-enrichment` with any module name.

## Tests

```bash
./mvnw test      # unit tests
./mvnw verify    # unit + integration tests
```

## Build

```bash
./mvnw clean package
```

## Specification

See [docs/specification.md](docs/specification.md).
