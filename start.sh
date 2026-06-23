#!/bin/bash

echo "===================================="
echo "Steam Library App - Quick Start"
echo "===================================="
echo ""

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "[ERROR] Java is not installed or not in PATH"
    echo "Please install Java 17 or higher"
    exit 1
fi

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "[ERROR] Maven is not installed or not in PATH"
    echo "Please install Maven 3.6 or higher"
    exit 1
fi

echo "[INFO] Java and Maven detected"
echo ""

# Check if application.properties has Steam API key configured
if grep -q "YOUR_STEAM_API_KEY_HERE" src/main/resources/application.properties; then
    echo "[WARNING] Steam API key not configured!"
    echo "Please update src/main/resources/application.properties"
    echo "Get your API key from: https://steamcommunity.com/dev/apikey"
    echo ""
    read -p "Press Enter to continue..."
fi

echo "[INFO] Building the application..."
mvn clean package -DskipTests
if [ $? -ne 0 ]; then
    echo "[ERROR] Build failed"
    exit 1
fi

echo ""
echo "[INFO] Starting the application..."
echo "[INFO] Access the app at: http://localhost:8080"
echo "[INFO] Press Ctrl+C to stop the application"
echo ""

java -jar target/steam-library-app-1.0.0.jar
