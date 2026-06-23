#!/bin/bash

# Steam Library Application Stop Script

echo "========================================"
echo "Steam Library Application Stop"
echo "========================================"
echo ""

# Find process using port 8080
PORT_IN_USE=$(netstat -ano | grep :8080 | grep LISTENING)

if [ -z "$PORT_IN_USE" ]; then
    echo "ℹ️  No application running on port 8080"
    exit 0
fi

echo "Found running application:"
echo "$PORT_IN_USE"
echo ""

# Extract PID
PID=$(echo "$PORT_IN_USE" | awk '{print $5}' | head -1)

echo "Stopping process $PID..."
taskkill //PID $PID //F

sleep 2

# Verify it stopped
PORT_CHECK=$(netstat -ano | grep :8080 | grep LISTENING)
if [ -z "$PORT_CHECK" ]; then
    echo ""
    echo "✅ Application stopped successfully!"
else
    echo ""
    echo "⚠️  Application may still be running. Try manually:"
    echo "   taskkill //PID $PID //F"
fi
