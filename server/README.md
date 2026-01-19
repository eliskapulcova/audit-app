# Audit API (Backend)

Spring Boot backend application written in **Kotlin**, responsible for:

- Parsing audit issues from a **CSV file**
- Loading additional **analysis/statistics JSON**
- Exposing a REST API endpoint returning a combined **audit report**
- Running locally or inside **Docker**

---

## Tech Stack

- **Kotlin**
- **Spring Boot 3**
- **Java 17**
- **Gradle (Kotlin DSL)**
- **Docker**
- Embedded **Tomcat**

---

## API Overview

### Get Audit Report

Returns parsed CSV issues together with analysis/statistics JSON.

**Endpoint**

GET /v1/report

**Response Example**
```json
{
  "issuesReport": {
    "issues": [
      {
        "updateDate": "2025-12-29T00:34:47+0000",
        "line": 90,
        "rule": "php:S1066",
        "project": "age-verification",
        "severity": "MAJOR",
        "status": "OPEN"
      }
    ]
  },
  "analysisReport": {
    "totalIssues": 123,
    "bySeverity": {
      "MAJOR": 45,
      "MINOR": 78
    }
  }
}
```

### Running locally

./gradlew bootRun

**Application will be available at**
http://localhost:8080/v1/report


