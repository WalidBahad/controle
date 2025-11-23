# Car Service

Service for managing car entities in the Car Rental Platform.

## Overview

The car-service provides a REST API for managing car inventory using Spring Data REST. It exposes CRUD operations automatically through REST endpoints.

## Port

**8081**

## Technology

- Spring Boot 3.2.0
- Spring Data REST
- Spring Data JPA
- H2 Database (in-memory)

## Car Entity Attributes

- `id` (Long) - Auto-generated unique identifier
- `brand` (String) - Car brand (e.g., Toyota, Honda)
- `model` (String) - Car model (e.g., Camry, Accord)
- `year` (Integer) - Manufacturing year
- `status` (Enum) - AVAILABLE or RENTED
- `pricePerDay` (Double) - Rental price per day

## API Endpoints

Spring Data REST automatically exposes the following endpoints:

### Base Path: `/api/cars`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/cars` | Get all cars (paginated) |
| GET | `/api/cars/{id}` | Get car by ID |
| POST | `/api/cars` | Create a new car |
| PUT | `/api/cars/{id}` | Update a car |
| PATCH | `/api/cars/{id}` | Partially update a car |
| DELETE | `/api/cars/{id}` | Delete a car |

### Search Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/cars/search/findByStatus?status=AVAILABLE` | Find cars by status |
| GET | `/api/cars/search/findByBrand?brand=Toyota` | Find cars by brand |

## Running the Service

### Prerequisites
- Java 17+
- Maven 3.6+

### Build
```bash
mvn clean install
```

### Run
```bash
mvn spring-boot:run
```

Or from the root directory:
```bash
cd car-service
mvn spring-boot:run
```

## Access Points

- **API Base**: http://localhost:8081/api
- **Swagger UI**: http://localhost:8081/swagger-ui.html
- **API Docs**: http://localhost:8081/api-docs
- **H2 Console**: http://localhost:8081/h2-console
  - JDBC URL: `jdbc:h2:mem:cardb`
  - Username: `sa`
  - Password: (empty)

## Example Requests

### Get All Cars
```bash
curl http://localhost:8081/api/cars
```

### Get Car by ID
```bash
curl http://localhost:8081/api/cars/1
```

### Create a Car
```bash
curl -X POST http://localhost:8081/api/cars \
  -H "Content-Type: application/json" \
  -d '{
    "brand": "Toyota",
    "model": "Camry",
    "year": 2023,
    "status": "AVAILABLE",
    "pricePerDay": 50.00
  }'
```

### Update Car Status
```bash
curl -X PUT http://localhost:8081/api/cars/1 \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "brand": "Toyota",
    "model": "Camry",
    "year": 2023,
    "status": "RENTED",
    "pricePerDay": 50.00
  }'
```

### Find Available Cars
```bash
curl http://localhost:8081/api/cars/search/findByStatus?status=AVAILABLE
```

### Find Cars by Brand
```bash
curl http://localhost:8081/api/cars/search/findByBrand?brand=Toyota
```

## Sample Data

On startup, the service automatically initializes with the following sample cars:
- Toyota Camry 2023 - $50/day (AVAILABLE)
- Honda Accord 2022 - $45/day (AVAILABLE)
- Ford Mustang 2023 - $75/day (RENTED)
- BMW X5 2023 - $120/day (AVAILABLE)
- Mercedes-Benz C-Class 2022 - $110/day (AVAILABLE)
- Tesla Model 3 2023 - $95/day (AVAILABLE)

## Configuration

Key configuration in `application.properties`:
- Server port: `8081`
- Database: H2 in-memory (`jdbc:h2:mem:cardb`)
- REST API base path: `/api`
- Spring Data REST pagination: 20 items per page

## Integration

This service is consumed by:
- **rental-service** - Checks car availability before creating rentals
- **analytics-service** - Fetches car information for occupancy calculations

## Error Handling

The service includes validation for:
- Required fields (brand, model, year, pricePerDay)
- Year range (1900-2100)
- Positive price values

Validation errors return HTTP 400 with detailed error messages.

