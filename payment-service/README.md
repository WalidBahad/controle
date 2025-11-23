# Payment Service

Service for processing payments in the Car Rental Platform.

## Overview

The payment-service provides mock payment processing endpoints that simulate Stripe and PayPal payment gateways. This is a demonstration service and does not perform actual payment transactions.

## Port

**8083**

## Technology

- Spring Boot 3.2.0
- Spring WebFlux/WebClient (for reactive HTTP communication)
- No database (stateless service)

## Payment Request Attributes

- `paymentMethod` (String) - "stripe" or "paypal"
- `amount` (Double) - Payment amount (must be positive)
- `clientId` (String) - Client/customer identifier
- `description` (String) - Transaction description

## Payment Response Attributes

- `paymentId` (String) - Generated payment transaction ID
- `status` (String) - "SUCCESS" or "FAILED"
- `message` (String) - Status message
- `amount` (Double) - Payment amount
- `paymentMethod` (String) - Payment method used

## API Endpoints

### Base Path: `/api/payments`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/payments/process` | Process a payment (mock) |
| GET | `/api/payments/health` | Health check endpoint |

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
cd payment-service
mvn spring-boot:run
```

## Access Points

- **API Base**: http://localhost:8083/api
- **Swagger UI**: http://localhost:8083/swagger-ui.html
- **API Docs**: http://localhost:8083/api-docs

## Example Requests

### Process Payment (Stripe)

```bash
curl -X POST http://localhost:8083/api/payments/process \
  -H "Content-Type: application/json" \
  -d '{
    "paymentMethod": "stripe",
    "amount": 250.00,
    "clientId": "client-123",
    "description": "Rental for car 1 (Toyota Camry)"
  }'
```

**Success Response:**
```json
{
  "paymentId": "ch_abc123def456ghi789jkl012",
  "status": "SUCCESS",
  "message": "Payment processed successfully via STRIPE",
  "amount": 250.00,
  "paymentMethod": "stripe"
}
```

### Process Payment (PayPal)

```bash
curl -X POST http://localhost:8083/api/payments/process \
  -H "Content-Type: application/json" \
  -d '{
    "paymentMethod": "paypal",
    "amount": 150.00,
    "clientId": "client-456",
    "description": "Rental for car 2 (Honda Accord)"
  }'
```

**Success Response:**
```json
{
  "paymentId": "PP-xyz789uvw456rst123mno012",
  "status": "SUCCESS",
  "message": "Payment processed successfully via PAYPAL",
  "amount": 150.00,
  "paymentMethod": "paypal"
}
```

### Health Check

```bash
curl http://localhost:8083/api/payments/health
```

**Response:**
```
Payment service is running
```

## Payment ID Format

- **Stripe**: `ch_` prefix followed by 24 alphanumeric characters
  - Example: `ch_abc123def456ghi789jkl012`
- **PayPal**: `PP-` prefix followed by 24 alphanumeric characters
  - Example: `PP-xyz789uvw456rst123mno012`

## Mock Payment Behavior

### Success Rate
- **90% success rate** - Most payments succeed
- **10% failure rate** - Simulates real-world payment failures

### Processing Time
- Simulates network delay (~500ms)
- Real payment gateways typically take 1-3 seconds

### Failure Response
```json
{
  "paymentId": null,
  "status": "FAILED",
  "message": "Payment processing failed. Please try again or use a different payment method.",
  "amount": 250.00,
  "paymentMethod": "stripe"
}
```

## Error Responses

### Unsupported Payment Method
```json
{
  "status": 400,
  "error": "Unsupported payment method. Use 'stripe' or 'paypal'"
}
```

### Validation Error
```json
{
  "status": 400,
  "error": "Validation failed",
  "validationErrors": {
    "amount": "Amount must be positive",
    "paymentMethod": "Payment method is required"
  }
}
```

## Configuration

Key configuration in `application.properties`:
- Server port: `8083`
- No database configuration (stateless service)

## Service Integration

This service is consumed by:
- **rental-service** (via WebClient) - Processes payments before confirming rentals

## Integration Example

When rental-service creates a rental, it calls this service:

```java
PaymentRequest request = new PaymentRequest(
    "stripe",
    250.00,
    "client-123",
    "Rental for car 1 (Toyota Camry)"
);

PaymentResponse response = paymentServiceClient.processPayment(request).block();
```

## Notes

⚠️ **This is a mock service** - It does NOT perform actual payment transactions. For production:
- Integrate with real Stripe API
- Integrate with real PayPal API
- Implement secure payment token handling
- Store payment records in a database
- Implement payment webhooks
- Add payment retry logic
- Implement fraud detection

## Error Handling

Global exception handler provides consistent error responses:
- Validation errors (400)
- Unsupported payment method (400)
- Internal server errors (500)

## Testing

To test payment failures, retry the request multiple times. The 10% failure rate will occasionally return failed payments for testing error handling.

