# AGENTS.md

## Project Overview

Three-module AI-powered event processing pipeline built with Quarkus MicroProfile, following the Boundary-Control-Entity (BCE) pattern. Events flow through Kafka topics with each module appending its results (event-carried state transfer).

**Pipeline:** `content-published` → `content-enriched` → `content-sensitivity-analyzed` → `ai-content-finalized`

## Modules

| Module | Consumes | Produces |
|---|---|---|
| `content-enrichment` | `content-published` | `content-enriched` |
| `sensitivity-compliance` | `content-enriched` | `content-sensitivity-analyzed` |
| `marketing-narrative` | `content-sensitivity-analyzed` | `ai-content-finalized` |

Each module has a corresponding DLQ topic (`<input-topic>-dlq`).

## Architecture

### BCE Pattern

- **Boundary**: Kafka consumers, Kafka producers, health checks
- **Control**: Stateless business logic and AI integration
- **Entity**: Domain objects and immutable event records

See [BCE pattern](https://bce.design).

### Package Structure

```
dev.denisarruda.[module-name].[component-name].[boundary|control|entity]
```

## Configuration

Each module has its own `src/main/resources/application.properties`.

| Variable | Required | Default | Description |
|---|---|---|---|
| `LLM_API_KEY` | yes | — | OpenAI API key (`gpt-4o`) |
| `OTEL_EXPORTER_OTLP_ENDPOINT` | no | `http://localhost:4317` | OTEL collector endpoint |
| `kafka.bootstrap.servers` | no | `localhost:9092` | Kafka broker (overridden by Dev Services in dev mode) |

Do not commit secrets or credentials — always use environment variables.

## Build & Test

### Development Mode

Kafka is provided automatically via Quarkus Dev Services (no local Kafka required). Run per module:

```bash
cd content-enrichment && ../mvnw quarkus:dev
```

### Build & Test (all modules)

Run from the project root:

```bash
./mvnw clean package   # build all modules
./mvnw test            # unit tests
./mvnw verify          # unit + integration tests
```

## Dependencies

**IMPORTANT**: Always ask before adding new dependencies to `pom.xml`. This project minimizes external dependencies and relies on Java SE, MicroProfile, and Jakarta EE APIs.

