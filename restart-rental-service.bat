@echo off
echo Stopping Rental Service...

REM Find and kill the rental service process on port 8082
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8082 ^| findstr LISTENING') do (
    taskkill /F /PID %%a
    echo Killed process %%a
)

timeout /t 3 /nobreak >nul

echo.
echo Starting Rental Service with updated code...
cd rental-service
start "Rental Service" cmd /k "mvn spring-boot:run"

echo.
echo Rental Service is restarting...
echo Check http://localhost:8082/swagger-ui.html when ready
echo.
pause
