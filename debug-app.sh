#!/bin/bash
# Debug script - Run this to test your application

echo "=== STEAM STORE DEBUG SCRIPT ==="
echo ""

# Check MySQL connection
echo "1. Testing MySQL connection..."
mysql -u root -p123456 steam_store -e "SELECT COUNT(*) as total_games FROM products;" 2>&1 | grep -v Warning
echo ""

# Check Product model compilation
echo "2. Checking if Product.java compiled..."
if [ -f "target/classes/com/steamlibrary/model/Product.class" ]; then
    echo "✓ Product.class exists"
else
    echo "✗ Product.class NOT found - Need to compile!"
fi
echo ""

# Check if app is running
echo "3. Testing if app is responding..."
curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/store 2>/dev/null
APP_STATUS=$?
if [ $APP_STATUS -eq 0 ]; then
    echo "✓ App is running"
else
    echo "✗ App is NOT running or not responding"
fi
echo ""

# Test database data
echo "4. Checking database data quality..."
mysql -u root -p123456 steam_store << 'SQL' 2>&1 | grep -v Warning
SELECT
    COUNT(*) as total,
    SUM(CASE WHEN capsule_image_url IS NOT NULL THEN 1 ELSE 0 END) as has_capsule,
    SUM(CASE WHEN header_image_url IS NOT NULL THEN 1 ELSE 0 END) as has_header,
    SUM(CASE WHEN name IS NOT NULL THEN 1 ELSE 0 END) as has_name,
    SUM(CASE WHEN price IS NOT NULL THEN 1 ELSE 0 END) as has_price
FROM products;
SQL
echo ""

echo "=== INSTRUCTIONS ==="
echo "If all checks pass, restart your Spring Boot app:"
echo "1. Stop app (Ctrl+C)"
echo "2. mvn spring-boot:run"
echo "3. Visit http://localhost:8080/store"
echo ""
echo "If you still get errors, paste the FIRST 10 LINES of the error here!"
