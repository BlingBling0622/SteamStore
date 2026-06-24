#!/bin/bash
echo "🧪 Testing Steam Library Application..."
echo ""

# Test 1: Check if application is running
echo "✅ Test 1: Application Running"
if netstat -ano | grep -q ":8080.*LISTENING"; then
    echo "   ✓ Port 8080 is listening"
else
    echo "   ✗ Port 8080 is NOT listening"
    exit 1
fi

# Test 2: Home page
echo ""
echo "✅ Test 2: Home Page"
if curl -s http://localhost:8080 | grep -q "Steam Store"; then
    echo "   ✓ Home page loads successfully"
else
    echo "   ✗ Home page failed to load"
    exit 1
fi

# Test 3: Store page
echo ""
echo "✅ Test 3: Store Page"
if curl -s http://localhost:8080/store | grep -q "Store"; then
    echo "   ✓ Store page loads successfully"
else
    echo "   ✗ Store page failed to load"
    exit 1
fi

# Test 4: Login page
echo ""
echo "✅ Test 4: Login Page"
if curl -s http://localhost:8080/login | grep -q "Sign In"; then
    echo "   ✓ Login page loads successfully"
else
    echo "   ✗ Login page failed to load"
    exit 1
fi

# Test 5: Register page
echo ""
echo "✅ Test 5: Register Page"
if curl -s http://localhost:8080/register | grep -q "Create Account"; then
    echo "   ✓ Register page loads successfully"
else
    echo "   ✗ Register page failed to load"
    exit 1
fi

# Test 6: Database file exists
echo ""
echo "✅ Test 6: Database Persistence"
if [ -f "./data/steamstore.mv.db" ]; then
    echo "   ✓ Database file exists"
    ls -lh ./data/steamstore.mv.db | awk '{print "   Database size: " $5}'
else
    echo "   ✗ Database file not found"
    exit 1
fi

echo ""
echo "🎉 All tests passed! Application is working perfectly!"
echo ""
echo "📊 Summary:"
echo "   • Application: Running on port 8080"
echo "   • Home page: OK"
echo "   • Store page: OK"
echo "   • Login page: OK"
echo "   • Register page: OK"
echo "   • Database: OK"
echo ""
echo "🌐 Access your application at: http://localhost:8080"
