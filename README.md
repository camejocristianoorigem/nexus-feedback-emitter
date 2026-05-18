# nexus-feedback-emitter

Asynchronous high-throughput event emitter built with Java 21 and Spring Boot 4 designed to simulate massive HTTP feedback ingestion workloads against distributed processing services.

---

# Overview

`nexus-feedback-emitter` is a load simulation microservice responsible for generating and dispatching large volumes of asynchronous HTTP requests to the Nexus Feedback Processor ecosystem.

The project was designed to stress-test event-driven architectures, validate concurrent processing strategies, benchmark API throughput, and simulate real-world traffic bursts using modern JVM concurrency features such as Virtual Threads.

The service acts as a synthetic traffic generator capable of dispatching thousands of parallel feedback events with minimal platform overhead.

---

# Architecture Goals

- Massive asynchronous request emission
- Lightweight concurrency using Virtual Threads
- HTTP-based workload simulation
- Realistic randomized payload generation
- Processor stress and throughput benchmarking
- Event-driven ecosystem validation
- High scalability with low thread cost
- Simple horizontal execution strategy

---

# Tech Stack

| Technology | Purpose |
|---|---|
| Java 21 | Modern JVM runtime |
| Spring Boot 4 | Application framework |
| Virtual Threads | High-concurrency execution |
| Java HttpClient | Non-blocking HTTP communication |
| Maven | Dependency management |
| Spring Web MVC | REST API exposure |
| Redis Starter | Future distributed buffering/caching support |
| Bean Validation | Request validation infrastructure |

---

# Project Structure

```text
src/main/java/nexus/feedback/emitter
├── controller
│   └── SimulationController.java
│
├── service
│   └── FeedbackEmitterService.java
│
src/main/resources
└── application.yaml
```

---

# Core Features

## Massive Parallel Request Dispatch

The emitter can generate and dispatch large quantities of HTTP POST requests concurrently using Java Virtual Threads.

```java
Executors.newVirtualThreadPerTaskExecutor()
```

This allows the application to scale to extremely high concurrency levels without the traditional memory cost associated with platform threads.

---

## Dynamic Payload Generation

Each emitted request contains randomized content including:

- Unique UUID-based titles
- Randomized descriptions
- Synthetic customer emails

Example payload:

```json
{
  "title": "Erro detectado no lote automático - ID 2f54...",
  "description": "aBcDeFgHiJkLmNoPqRsT",
  "customerEmail": "usuario_15@nexus.com"
}
```

---

## Background Execution

Simulation jobs are executed asynchronously so HTTP trigger endpoints remain responsive.

```java
Thread.ofVirtual().start(() -> emitterService.emitMassiveFeedbacks(amount));
```

---

## Success and Failure Metrics

The service tracks:

- Successful requests
- Failed requests
- Non-201 HTTP responses

```java
AtomicInteger successCount
AtomicInteger errorCount
```

---

# REST API

## Start Simulation

### Endpoint

```http
POST /api/v1/simulation/start
```

### Query Parameters

| Parameter | Type | Default | Description |
|---|---|---|---|
| amount | int | 20 | Number of requests to emit |

---

## Example Request

```bash
curl -X POST "http://localhost:8082/api/v1/simulation/start?amount=1000"
```

---

## Example Response

```text
Simulação de 1000 requisições iniciada em background!
```

---

# Configuration

## application.yaml

```yaml
spring:
  application:
    name: nexus.feedback.emitter

  threads:
    virtual:
      enabled: true

server:
  port: 8082

  tomcat:
    threads:
      max: 200

    keep-alive-timeout: 60s
    max-keep-alive-requests: 10000
```

---

# Concurrency Strategy

The application uses two layers of concurrency:

## 1. Virtual Thread Executor

Responsible for handling each outgoing HTTP request independently.

```java
Executors.newVirtualThreadPerTaskExecutor()
```

---

## 2. Non-Blocking HTTP Client

Java HttpClient handles asynchronous network communication efficiently.

```java
HttpClient.newBuilder()
```

---

# Processor Integration

The emitter currently targets:

```text
http://localhost:8081/api/v1/feedbacks
```

This endpoint is expected to belong to the `nexus-feedback-processor` microservice.

---

# Scalability Characteristics

| Characteristic | Result |
|---|---|
| Thread overhead | Extremely low |
| Context switching | Minimal |
| Parallelism | High |
| JVM memory usage | Efficient |
| HTTP throughput | High |
| Blocking tolerance | Excellent |

---

# Running the Project

## Requirements

- Java 21+
- Maven 3.9+

---

## Clone Repository

```bash
git clone https://github.com/camejocristianoorigem/nexus-feedback-emitter.git
```

---

## Build Project

```bash
mvn clean install
```

---

## Run Application

```bash
mvn spring-boot:run
```

---

# Load Testing Examples

## 100 Requests

```bash
curl -X POST "http://localhost:8082/api/v1/simulation/start?amount=100"
```

## 10,000 Requests

```bash
curl -X POST "http://localhost:8082/api/v1/simulation/start?amount=10000"
```

## 100,000 Requests

```bash
curl -X POST "http://localhost:8082/api/v1/simulation/start?amount=100000"
```

---

# Possible Future Improvements

- Reactive HTTP client migration
- Kafka/RabbitMQ event emission
- Distributed load generation
- Metrics via Micrometer + Prometheus
- Grafana dashboards
- Request batching
- Adaptive throttling
- Retry and backoff strategies
- Chaos engineering support
- Docker/Kubernetes deployment
- OpenTelemetry tracing
- Structured logging
- Rate limiting simulation
- Redis-backed buffering

---

# Example Execution Flow

```text
Client Trigger
      │
      ▼
SimulationController
      │
      ▼
FeedbackEmitterService
      │
      ▼
Virtual Thread Executor
      │
      ▼
HTTP Request Generation
      │
      ▼
Feedback Processor API
```

---

# Performance Philosophy

This project embraces modern JVM concurrency patterns introduced by Project Loom, enabling highly scalable workload generation while preserving imperative programming simplicity.

Instead of relying on complex reactive pipelines, the emitter leverages Virtual Threads to achieve:

- Simpler code
- Better readability
- Massive concurrency
- Lower operational complexity

---

# License

This project is intended for educational, benchmarking, and distributed systems experimentation purposes.
