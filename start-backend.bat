@echo off
echo Starting UMS Backend Server...
echo.
echo Make sure you have Java 17+ and Maven installed
echo.
echo The server will start on http://localhost:8080
echo.
echo Press Ctrl+C to stop the server
echo.

cd /d "%~dp0"

mvn spring-boot:run

pause
