#!/bin/bash

echo "Starting Car Rental Platform Services..."
echo ""

# Function to start a service in the background
start_service() {
    local service_name=$1
    local service_dir=$2
    local port=$3
    
    echo "Starting $service_name (Port $port)..."
    cd "$service_dir" || exit
    mvn spring-boot:run > "../logs/${service_name}.log" 2>&1 &
    echo $! > "../logs/${service_name}.pid"
    cd ..
    sleep 5
}

# Create logs directory
mkdir -p logs

# Start services in order
start_service "Car Service" "car-service" "8081"
start_service "Payment Service" "payment-service" "8083"
start_service "Rental Service" "rental-service" "8082"
start_service "Analytics Service" "analytics-service" "8084"

echo ""
echo "All services are starting..."
echo ""
echo "Service URLs:"
echo "  Car Service:      http://localhost:8081/swagger-ui.html"
echo "  Rental Service:   http://localhost:8082/swagger-ui.html"
echo "  Payment Service:  http://localhost:8083/swagger-ui.html"
echo "  Analytics Service: http://localhost:8084/swagger-ui.html"
echo ""
echo "Logs are available in the 'logs' directory"
echo "PID files are stored in the 'logs' directory for stopping services"
echo ""
echo "To stop all services, run: ./stop-all-services.sh"

