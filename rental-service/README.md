# Rental Service

Service for managing car rental reservations and bookings.

## Overview

The rental-service handles the creation and management of car rentals. It integrates with:
- **car-service** (via FeignClient) - to check car availability
- **payment-service** (via WebClient) - to process payments before confirming bookings

## Port

**8082**

## Technology

- Spring Boot 3.2.0
- Spring Data JPA
- Spring Cloud OpenFeign (for car-service communication)
- Spring WebFlux/WebClient (for payment-service communication)
- H2 Database (in-memory)

## Rental Entity Attributes

- `id` (Long) - Auto-generated unique identifier
- `carId` (Long) - Reference to car from car-service
- `clientId` (String) - Client/customer identifier
- `startDate` (LocalDate) - Rental start date
- `endDate` (LocalDate) - Rental end date
- `status` (Enum) - ACTIVE, COMPLETED, or CANCELLED
- `paymentId` (String) - Payment transaction ID from payment-service
- `totalAmount` (Double) - Total rental amount

## API Endpoints

### Base Path: `/api/rentals`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/rentals` | Create a new rental (requires payment) |
| GET | `/api/rentals` | Get all rentals |
| GET | `/api/rentals/{id}` | Get rental by ID |
| GET | `/api/rentals/client/{clientId}` | Get rentals by client ID |
| GET | `/api/rentals/car/{carId}` | Get rentals by car ID |

## Running the Service

### Prerequisites
- Java 17+
- Maven 3.6+
- **car-service** must be running on port 8081
- **payment-service** must be running on port 8083

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
cd rental-service
mvn spring-boot:run
```

## Access Points

- **API Base**: http://localhost:8082/api
- **Swagger UI**: http://localhost:8082/swagger-ui.html
- **API Docs**: http://localhost:8082/api-docs
- **H2 Console**: http://localhost:8082/h2-console
  - JDBC URL: `jdbc:h2:mem:rentaldb`
  - Username: `sa`
  - Password: (empty)

## Example Requests

### Create a Rental

When creating a rental, the service will:
1. Validate the request
2. Check car availability via car-service
3. Check for overlapping rentals
4. Calculate total amount
5. Process payment via payment-service
6. Create rental record
7. Update car status to RENTED

```bash
curl -X POST http://localhost:8082/api/rentals \
  -H "Content-Type: application/json" \
  -d '{
    "carId": 1,
    "clientId": "client-123",
    "startDate": "2024-01-15",
    "endDate": "2024-01-20"
  }'
```

**Response:**
```json
{
  "id": 1,
  "carId": 1,
  "clientId": "client-123",
  "startDate": "2024-01-15",
  "endDate": "2024-01-20",
  "status": "ACTIVE",
  "paymentId": "ch_abc123...",
  "totalAmount": 250.00
}
```

### Get All Rentals
```bash
curl http://localhost:8082/api/rentals
```

### Get Rental by ID
```bash
curl http://localhost:8082/api/rentals/1
```

### Get Rentals by Client ID
```bash
curl http://localhost:8082/api/rentals/client/client-123
```

### Get Rentals by Car ID
```bash
curl http://localhost:8082/api/rentals/car/1
```

## Business Logic

### Rental Creation Flow

1. **Validation**:
   - End date must be after start date
   - Start date cannot be in the past
   - All required fields must be provided

2. **Car Availability Check** (via FeignClient):
   - Fetches car details from car-service
   - Verifies car exists
   - Checks if car status is AVAILABLE

3. **Overlap Check**:
   - Queries database for active rentals in the date range
   - Prevents double-booking

4. **Payment Processing** (via WebClient):
   - Calculates total amount (price per day × number of days)
   - Calls payment-service to process payment
   - Payment must be successful to proceed

5. **Rental Creation**:
   - Creates rental record with ACTIVE status
   - Stores payment ID and total amount
   - Updates car status to RENTED in car-service

## Error Responses

### Car Not Available
```json
{
  "status": 400,
  "error": "Car is not available for rental"
}
```

### Payment Failed
```json
{
  "status": 402,
  "error": "Payment processing failed: [error message]"
}
```

### Car Not Found
```json
{
  "status": 404,
  "error": "Car not found with ID: 1"
}
```

### Date Validation Error
```json
{
  "status": 400,
  "error": "End date must be after start date"
}
```

## Configuration

Key configuration in `application.properties`:
- Server port: `8082`
- Database: H2 in-memory (`jdbc:h2:mem:rentaldb`)
- Car service URL: `http://localhost:8081`
- Payment service URL: `http://localhost:8083`

## Service Dependencies

This service depends on:
- **car-service** (port 8081) - Must be running
- **payment-service** (port 8083) - Must be running

## Error Handling

Global exception handler provides consistent error responses:
- Validation errors (400)
- Not found errors (404)
- Payment failures (402)
- Service unavailable errors (503)
- Internal server errors (500)

## Notes

- Rentals cannot overlap for the same car
- Payment is required before rental confirmation
- Car status is automatically updated when rental is created
- Total amount is calculated based on price per day and number of rental days

