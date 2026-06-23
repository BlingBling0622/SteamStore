#!/bin/bash

echo "=========================================="
echo "Steam Library App - System Test"
echo "=========================================="
echo ""

BASE_URL="http://localhost:8080"
PASS=0
FAIL=0

# Function to test endpoint
test_endpoint() {
    local name=$1
    local url=$2
    local expected=$3
    
    echo -n "Testing $name... "
    
    response=$(curl -s -o /dev/null -w "%{http_code}" "$url")
    
    if [ "$response" = "$expected" ]; then
        echo "✅ PASS (HTTP $response)"
        ((PASS++))
    else
        echo "❌ FAIL (Expected $expected, got $response)"
        ((FAIL++))
    fi
}

# Run tests
test_endpoint "Home Page" "$BASE_URL/" "200"
test_endpoint "Login Page" "$BASE_URL/login" "200"
test_endpoint "Register Page" "$BASE_URL/register" "200"
test_endpoint "H2 Console" "$BASE_URL/h2-console" "200"
test_endpoint "Dashboard (should redirect)" "$BASE_URL/dashboard" "302"

echo ""
echo "=========================================="
echo "Test Results"
echo "=========================================="
echo "Passed: $PASS"
echo "Failed: $FAIL"
echo ""

if [ $FAIL -eq 0 ]; then
    echo "✅ All tests passed! Application is working correctly."
    exit 0
else
    echo "⚠️  Some tests failed. Check application logs."
    exit 1
fi
