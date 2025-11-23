#!/bin/bash

echo "Stopping Car Rental Platform Services..."
echo ""

# Function to stop a service
stop_service() {
    local service_name=$1
    
    if [ -f "logs/${service_name}.pid" ]; then
        local pid=$(cat "logs/${service_name}.pid")
        if ps -p $pid > /dev/null 2>&1; then
            echo "Stopping $service_name (PID: $pid)..."
            kill $pid
            rm "logs/${service_name}.pid"
        else
            echo "$service_name was not running"
            rm "logs/${service_name}.pid"
        fi
    else
        echo "$service_name PID file not found"
    fi
}

stop_service "Car Service"
stop_service "Payment Service"
stop_service "Rental Service"
stop_service "Analytics Service"

echo ""
echo "All services stopped"

