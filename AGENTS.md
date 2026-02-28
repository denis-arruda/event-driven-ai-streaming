# AGENTS.md

## Project Overview

Project with 3 modules. Each module is an event processor. Each module uses Quarkus MicroProfile and follows Boundary-Control-Entity (BCE) architectural pattern. The project should follow clean separation of concerns with Kafka Producer/Consumer, MicroProfile Config, CDI, and Health checks.

## Architecture

### BCE Pattern

- **Boundary**: Kafka consumer, Kafka producers and health checks - coarse-grained components exposing functionality
- **Control**: Business logic and procedural code - stateless processing
- **Entity**: Domain objects, data classes, and entities

[BCE pattern]https://bce.design

### Package Structure

```
dev.denisarruda.[app-name].[component-name].[boundary|control|entity]
```

Example: `dev.denisarruda.qmp.greetings.boundary.GreetingResource`

## Build & Test

### Development Mode

```bash
mvn quarkus:dev
```

### Build

```bash
mvn clean package
```

### Package

```bash
mvn package
```

## Code Style

### Java Version

Java 25 with modern syntax:
- Use `var` for local variables
- Pattern matching
- Text blocks for multiline strings
- Records for immutable data

## Security Considerations

- Avoid command injection, XSS, SQL injection
- Do not commit secrets (.env, credentials files)
- Use proper input validation in resources

## Dependencies

**IMPORTANT**: Always ask before adding new dependencies to `pom.xml`. This project minimizes external dependencies and relies on Java SE APIs and MicroProfile standards.
