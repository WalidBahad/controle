# Analytics Service

Service for calculating and exposing car occupancy analytics.

## Overview

The analytics-service calculates car occupancy rates (percentage of days each car is rented) by aggregating data from car-service and rental-service. It provides insights into how efficiently the car fleet is being utilized.

## Port

**8084**

## Technology

- Spring Boot 3.2.0
- Spring Cloud OpenFeign (for service-to-service communication)
- No database (stateless service that aggregates data from other services)

## Occupancy Rate Attributes

- `carId` (Long) - Car identifier
- `brand` (String) - Car brand
- `model` (String) - Car model
- `year` (Integer) - Manufacturing year
- `totalDaysInPeriod` (Long) - Total days in the analysis period
- `rentedDays` (Long) - Number of days the car was rented
- `occupancyPercentage` (Double) - Percentage of days rented (0-100)
- `numberOfRentals` (Integer) - Total number of rentals for this car

## API Endpoints

### Base Path: `/api/analytics`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/analytics/occupancy` | Get occupancy rates for all cars |
| GET | `/api/analytics/occupancy/car/{carId}` | Get occupancy rate for a specific car |

### Query Parameters

Both endpoints support optional query parameters:
- `startDate` (optional) - Start date of analysis period (format: yyyy-MM-dd)
  - Default: 30 days ago
- `endDate` (optional) - End date of analysis period (format: yyyy-MM-dd)
  - Default: today

## Running the Service

### Prerequisites
- Java 17+
- Maven 3.6+
- **car-service** must be running on port 8081
- **rental-service** must be running on port 8082

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
cd analytics-service
mvn spring-boot:run
```

## Access Points

- **API Base**: http://localhost:8084/api
- **Swagger UI**: http://localhost:8084/swagger-ui.html
- **API Docs**: http://localhost:8084/api-docs

## Example Requests

### Get Occupancy Rates for All Cars (Last 30 Days)

```bash
curl http://localhost:8084/api/analytics/occupancy
```

**Response:**
```json
[
  {
    "carId": 1,
    "brand": "Toyota",
    "model": "Camry",
    "year": 2023,
    "totalDaysInPeriod": 30,
    "rentedDays": 15,
    "occupancyPercentage": 50.0,
    "numberOfRentals": 3
  },
  {
    "carId": 2,
    "brand": "Honda",
    "model": "Accord",
    "year": 2022,
    "totalDaysInPeriod": 30,
    "rentedDays": 10,
    "occupancyPercentage": 33.33,
    "numberOfRentals": 2
  }
]
```

### Get Occupancy Rates for Custom Date Range

```bash
curl "http://localhost:8084/api/analytics/occupancy?startDate=2024-01-01&endDate=2024-01-31"
```

### Get Occupancy Rate for a Specific Car

```bash
curl http://localhost:8084/api/analytics/occupancy/car/1
```

**Response:**
```json
{
  "carId": 1,
  "brand": "Toyota",
  "model": "Camry",
  "year": 2023,
  "totalDaysInPeriod": 30,
  "rentedDays": 15,
  "occupancyPercentage": 50.0,
  "numberOfRentals": 3
}
```

### Get Occupancy Rate for Specific Car with Custom Date Range

```bash
curl "http://localhost:8084/api/analytics/occupancy/car/1?startDate=2024-01-01&endDate=2024-01-31"
```

## How Occupancy is Calculated

1. **Fetches all cars** from car-service
2. **Fetches all rentals** from rental-service
3. **Filters rentals**:
   - Only ACTIVE or COMPLETED rentals
   - Only rentals for the specified car(s)
4. **Calculates rented days**:
   - Counts days within the analysis period
   - Handles overlapping rentals (capped at total days)
   - Includes partial days
5. **Calculates occupancy percentage**:
   - `(rentedDays / totalDaysInPeriod) × 100`
   - Rounded to 2 decimal places

### Example Calculation

For a 30-day period:
- Car rented from Jan 1-10 (10 days)
- Car rented from Jan 15-25 (11 days)
- Total rented days: 21 days
- Occupancy: (21 / 30) × 100 = 70%

## Service Dependencies

This service depends on:
- **car-service** (port 8081) - Fetches car information
- **rental-service** (port 8082) - Fetches rental data

Both services must be running for analytics to work.

## Error Responses

### Car Not Found
```json
{
  "status": 404,
  "error": "Car not found with ID: 999"
}
```

### Service Unavailable
```json
{
  "status": 503,
  "error": "Unable to fetch cars from car-service"
}
```

### Invalid Date Range
```json
{
  "status": 400,
  "error": "Start date must be before or equal to end date"
}
```

## Configuration

Key configuration in `application.properties`:
- Server port: `8084`
- Car service URL: `http://localhost:8081`
- Rental service URL: `http://localhost:8082`

## Use Cases

### Fleet Management
- Identify underutilized cars
- Optimize car inventory
- Plan maintenance schedules based on usage

### Business Intelligence
- Calculate revenue per car
- Identify popular car models
- Forecast demand based on historical occupancy

### Reporting
- Monthly occupancy reports
- Quarterly performance reviews
- Annual utilization analysis

## Notes

- Only ACTIVE and COMPLETED rentals are counted
- CANCELLED rentals are excluded from calculations
- Overlapping rentals are handled correctly (days are not double-counted)
- Default analysis period is last 30 days
- Occupancy percentage is capped at 100%

## Error Handling

Global exception handler provides consistent error responses:
- Validation errors (400)
- Not found errors (404)
- Service unavailable errors (503)
- Internal server errors (500)

## Performance Considerations

- All data is fetched on-demand (no caching)
- For large datasets, consider implementing caching
- Service calls are synchronous (could be optimized with reactive streams)
- Calculations are done in-memory

## Future Enhancements

Potential improvements:
- Caching for frequently requested analytics
- Batch processing for historical data
- Real-time occupancy updates
- Export analytics to CSV/PDF
- Advanced metrics (revenue per car, average rental duration)
- Predictive analytics (demand forecasting)

