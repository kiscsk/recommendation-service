# Recommendation Service

A scalable  RESTful service for cryptocurrency price recommendations, statistics, and volatility analysis.  
Built with Java, Spring Boot and Maven.

---

## Features

- **CSV Data Ingestion:** Loads crypto price data from CSV files at startup.
- **Statistics Endpoints:** Get oldest, newest, min, max prices for any supported crypto.
- **Normalized Range Calculation:** Compare cryptos by normalized price range ((max-min)/min).
- **Daily Volatility:** Find the crypto with the highest normalized range for a specific day.
- **Rate Limiting:** IP-based request throttling using Bucket4j.
- **Error Handling:** Standardized error responses and custom exceptions.
- **OpenAPI Documentation:** Interactive Swagger UI for all endpoints.
- **In-Memory Repository:** Fast, simple storage for development and testing.
- **Automated Tests:** Unit and integration tests with high coverage.

---

## Technology Stack

- **Java 21**
- **Spring Boot 3.4.5**
- **Maven**
- **OpenCSV** (CSV parsing)
- **Bucket4j** (rate limiting)
- **JUnit 5 & Mockito** (testing)
- **Springdoc OpenAPI** (Swagger UI)

---

## Project Structure
```
recommendation-service/
├── src/
│   ├── main/
│   │   ├── java/com/epam/xmtesttask/
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   ├── repository/
│   │   │   ├── domain/
│   │   ├── resources/
│   ├── test/
├── pom.xml
├── mvnw, mvnw.cmd
```

## How to Build and Run

### Prerequisites

- Java 21
- (Optional) Docker

### Build

```bash
./mvnw clean package
```

### Run Locally

```bash
./mvnw spring-boot:run
```
or after build
```bash
java -jar target/recommendation-service-1.0-SNAPSHOT-spring-boot.jar
```

### Run with Docker
Under construction

## API Documentation & Usage
Swagger UI is available at:  
http://localhost:8080/swagger-ui.html

### Endpoints
* Get normalized ranges for all cryptos
```bash
GET /cryptos/normalized-range
```
* Get stats for a specific crypto
```bash
GET /cryptos/{symbol}/stats
```
* Get crypto with highest normalized range for a day
```bash
GET /cryptos/highest-normalized-range?date=2023-01-15
```

## Testing
Unit and integration tests are included.
* unit tests run on maven test phase by maven-surefire-plugin
* integration tests run on maven integration-test phase by maven-failsafe-plugin

Note: run integration-tests
```bash
./mvnw clean verify
```

## Contribution Guidelines
* Fork the repository and create your feature branch. 
* Write clear, tested code and add JavaDoc. 
* Ensure all tests pass and coverage is above 80%. 
* Submit a pull request with a description of your changes.

## Special notes, potential enhancements
* The service uses an in-memory repository. For production, consider a persistent database. 
* Maven wrapper included for least requirements.
* Rate limiting is IP-based and can be configured in RateLimitFilter.java.
* CSV files should be placed in the resources/prices directory and follow the format: timestamp,symbol,price.
* Containerization is a requirement (wasn't able to create a local docker up and running on a windows machine)