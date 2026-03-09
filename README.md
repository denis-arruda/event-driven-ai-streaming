# Event-Driven AI Streaming

Quarkus processors that enrich, analyze, and generate marketing assets for published content using LangChain4j (OpenAI gpt-4o) over Kafka. A web UI lets users submit content and view AI-processed results.

**Pipeline:** `content-published` → `content-enriched` → `content-sensitivity-analyzed` → `ai-content-finalized`

## Modules

- [content-enrichment](content-enrichment/) — extracts themes, emotional tone, audience profile, and keywords
- [sensitivity-compliance](sensitivity-compliance/) — evaluates age ratings, regional restrictions, and risk flags
- [marketing-narrative](marketing-narrative/) — generates headlines, taglines, and promotional descriptions
- [frontend-web](frontend-web/) — web UI to publish content and view finalized results

## Running

Requires `LLM_API_KEY`. Kafka is provided by Quarkus Dev Services — no local setup needed.

```bash
export LLM_API_KEY=<your-openai-api-key>
cd content-enrichment && ../mvnw quarkus:dev
```

Replace `content-enrichment` with any module name.

## Dev UI

With a module running in dev mode, open its Dev UI in the browser:

| Module | Port | Dev UI |
|---|---|---|
| `content-enrichment` | 9090 | http://localhost:9090/q/dev |
| `sensitivity-compliance` | 9091 | http://localhost:9091/q/dev |
| `marketing-narrative` | 9093 | http://localhost:9093/q/dev |
| `frontend-web` | 9095 | http://localhost:9095/q/dev |

## Tests

```bash
./mvnw test      # unit tests
./mvnw verify    # unit + integration tests
```

## Build

```bash
./mvnw clean package
```

## Container Images

Container images are built using [Quarkus JIB](https://quarkus.io/guides/container-image#jib) (`quarkus-container-image-jib`).

JIB was chosen over the Buildpack extension because this is a multi-module Maven project: the Buildpack extension rebuilds from source inside an isolated container where the parent POM (`event-driven-ai-streaming:pom`) is not resolvable, causing a fatal build failure. JIB avoids this entirely by packaging the pre-built JAR directly into the image — no Docker daemon or source rebuild required.

Build all images:

```bash
./mvnw package -Dquarkus.container-image.build=true
```

Images produced:

| Module | Image |
|---|---|
| `content-enrichment` | `denisarruda/content-enrichment:1.0-SNAPSHOT` |
| `sensitivity-compliance` | `denisarruda/sensitivity-compliance:1.0-SNAPSHOT` |
| `marketing-narrative` | `denisarruda/marketing-narrative:1.0-SNAPSHOT` |
| `frontend-web` | `denisarruda/frontend-web:1.0-SNAPSHOT` |

## Docker Compose

Start the full stack (Kafka + all modules):

```bash
export LLM_API_KEY=<your-openai-api-key>
docker compose up
```

| Service | Host port |
|---|---|
| Kafka (internal) | `9092` |
| Kafka (external / host access) | `9094` |
| `content-enrichment` | `9090` |
| `sensitivity-compliance` | `9091` |
| `marketing-narrative` | `9093` |
| `frontend-web` | `9095` |
| `kafka-ui` | `8090` |

Connect to Kafka from the host at `localhost:9094`. Open the web UI at http://localhost:9095 and Kafka UI at http://localhost:8090.

## Specification

See [docs/specification.md](docs/specification.md).
