@echo off
echo ====================================
echo Steam Library App - Quick Start
echo ====================================
echo.

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Java is not installed or not in PATH
    echo Please install Java 17 or higher
    pause
    exit /b 1
)

REM Check if Maven is installed
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Maven is not installed or not in PATH
    echo Please install Maven 3.6 or higher
    pause
    exit /b 1
)

echo [INFO] Java and Maven detected
echo.

REM Check if application.properties has Steam API key configured
findstr /C:"YOUR_STEAM_API_KEY_HERE" src\main\resources\application.properties >nul
if %errorlevel% equ 0 (
    echo [WARNING] Steam API key not configured!
    echo Please update src\main\resources\application.properties
    echo Get your API key from: https://steamcommunity.com/dev/apikey
    echo.
    pause
)

echo [INFO] Building the application...
call mvn clean package -DskipTests
if %errorlevel% neq 0 (
    echo [ERROR] Build failed
    pause
    exit /b 1
)

echo.
echo [INFO] Starting the application...
echo [INFO] Access the app at: http://localhost:8080
echo [INFO] Press Ctrl+C to stop the application
echo.

java -jar target\steam-library-app-1.0.0.jar
