@echo off
echo Starting Car Rental Platform Services...
echo.

echo Starting Car Service (Port 8081)...
start "Car Service" cmd /k "cd car-service && mvn spring-boot:run"

timeout /t 5 /nobreak >nul

echo Starting Payment Service (Port 8083)...
start "Payment Service" cmd /k "cd payment-service && mvn spring-boot:run"

timeout /t 5 /nobreak >nul

echo Starting Rental Service (Port 8082)...
start "Rental Service" cmd /k "cd rental-service && mvn spring-boot:run"

timeout /t 5 /nobreak >nul

echo Starting Analytics Service (Port 8084)...
start "Analytics Service" cmd /k "cd analytics-service && mvn spring-boot:run"

echo.
echo All services are starting...
echo.
echo Service URLs:
echo   Car Service:      http://localhost:8081/swagger-ui.html
echo   Rental Service:   http://localhost:8082/swagger-ui.html
echo   Payment Service:  http://localhost:8083/swagger-ui.html
echo   Analytics Service: http://localhost:8084/swagger-ui.html
echo.
echo Press any key to exit...
pause >nul

