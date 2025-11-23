# Car Rental Platform - Multi-Service Spring Boot Backend

A microservices-based backend system for a car rental platform, built with Spring Boot 3 and Java 17.

## Architecture Overview

This platform consists of four independent microservices:

1. **car-service** (Port 8081) - Manages car entities and availability
2. **rental-service** (Port 8082) - Handles rental reservations and bookings
3. **payment-service** (Port 8083) - Processes payments (mock implementation)
4. **analytics-service** (Port 8084) - Calculates car occupancy analytics

## Technology Stack

- **Spring Boot 3.2.0**
- **Java 17**
- **H2 Database** (in-memory, can be switched to PostgreSQL)
- **Maven** (multi-module project)
- **Spring Data REST** (for car-service)
- **Spring Cloud OpenFeign** (for service-to-service communication)
- **Spring WebFlux/WebClient** (for reactive HTTP communication)
- **SpringDoc OpenAPI** (Swagger documentation)
- **Spring Boot Validation**

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- IDE (IntelliJ IDEA, Eclipse, or VS Code recommended)

## Quick Start

### 1. Build All Services

From the root directory:

```bash
mvn clean install
```

### 2. Run Services Individually

Each service should be started in separate terminal windows:

```bash
# Terminal 1 - Car Service
cd car-service
mvn spring-boot:run

# Terminal 2 - Rental Service
cd rental-service
mvn spring-boot:run

# Terminal 3 - Payment Service
cd payment-service
mvn spring-boot:run

# Terminal 4 - Analytics Service (Optional)
cd analytics-service
mvn spring-boot:run
```

### 3. Access Services

- **Car Service**: http://localhost:8081
  - API Base: http://localhost:8081/api
  - Swagger UI: http://localhost:8081/swagger-ui.html
  - H2 Console: http://localhost:8081/h2-console

- **Rental Service**: http://localhost:8082
  - API Base: http://localhost:8082/api
  - Swagger UI: http://localhost:8082/swagger-ui.html

- **Payment Service**: http://localhost:8083
  - API Base: http://localhost:8083/api
  - Swagger UI: http://localhost:8083/swagger-ui.html

- **Analytics Service**: http://localhost:8084
  - API Base: http://localhost:8084/api
  - Swagger UI: http://localhost:8084/swagger-ui.html

## Service Documentation

Each service has its own detailed README:
- [Car Service README](car-service/README.md)
- [Rental Service README](rental-service/README.md)
- [Payment Service README](payment-service/README.md)
- [Analytics Service README](analytics-service/README.md)

## Service Communication Flow

```
┌─────────────┐
│ car-service │ (Manages car inventory)
└──────┬──────┘
       │ FeignClient
       ▼
┌─────────────┐      ┌──────────────┐
│rental-service│◄────►│payment-service│ (Processes payments)
└──────┬──────┘ WebClient └──────────────┘
       │
       │ FeignClient
       ▼
┌─────────────┐
│analytics-   │ (Calculates occupancy rates)
│service      │
└─────────────┘
```

## Example API Workflow

1. **Create a Rental**:
   ```bash
   POST http://localhost:8082/api/rentals
   {
     "carId": 1,
     "clientId": "client-123",
     "startDate": "2024-01-15",
     "endDate": "2024-01-20"
   }
   ```

2. **The rental-service will**:
   - Check car availability via car-service (FeignClient)
   - Process payment via payment-service (WebClient)
   - Create rental record
   - Update car status to RENTED

3. **View Analytics**:
   ```bash
   GET http://localhost:8084/api/analytics/occupancy?startDate=2024-01-01&endDate=2024-01-31
   ```

## Error Handling

All services include global exception handlers that return consistent error responses:
- Validation errors (400)
- Not found errors (404)
- Payment failures (402)
- Service unavailable errors (503)
- Internal server errors (500)

## Database

All services use H2 in-memory databases for local testing. To switch to PostgreSQL:
1. Update `application.properties` in each service
2. Add PostgreSQL driver dependency
3. Configure connection details

## Testing

Run tests for all services:
```bash
mvn test
```

## Project Structure

```
car-rental-platform/
├── pom.xml (parent POM)
├── car-service/
│   ├── src/main/java/com/carrental/carservice/
│   └── src/main/resources/
├── rental-service/
│   ├── src/main/java/com/carrental/rentalservice/
│   └── src/main/resources/
├── payment-service/
│   ├── src/main/java/com/carrental/paymentservice/
│   └── src/main/resources/
└── analytics-service/
    ├── src/main/java/com/carrental/analyticsservice/
    └── src/main/resources/
```

## Notes

- All services are stateless and can be scaled independently
- Payment service uses mock payment processing (10% failure rate for testing)
- Sample data is automatically initialized on startup
- All services expose Swagger/OpenAPI documentation

## License

This project is for educational/demonstration purposes.

