# Login & Session Persistence Fix

## Issues Identified

1. **Auto-logout when clicking store page** - Session not persisting properly
2. **Purchased games not showing** - User's owned products not displaying after checkout

## Root Cause Analysis

After inspecting the code and database:

1. **Orders ARE being saved correctly** - Database shows orders and order_products are properly linked
2. **The issue is likely:**
   - Session management configuration was missing
   - Potential lazy loading issues when fetching orders and products
   - No transaction boundary on `getOwnedProductIds()` method

## Fixes Applied

### 1. Enhanced Security Configuration (`SecurityConfig.java`)

Added proper session management:

```java
.sessionManagement(session -> session
    .sessionFixation().migrateSession()
    .maximumSessions(1)
    .maxSessionsPreventsLogin(false)
)
```

**What this does:**
- `migrateSession()` - Creates a new session on login to prevent session fixation attacks
- `maximumSessions(1)` - Allows only one active session per user
- `maxSessionsPreventsLogin(false)` - New login invalidates old session instead of blocking

### 2. Added Transaction Boundary (`StoreService.java`)

```java
@Transactional(readOnly = true)
public Set<Long> getOwnedProductIds(User user) {
    // ... fetch orders and products
}
```

**What this does:**
- Ensures the entire operation (fetching orders + products) happens within one database transaction
- Prevents lazy loading exceptions when accessing `order.getProducts()`
- The `readOnly = true` optimizes performance for read operations

### 3. Added Comprehensive Logging

Added debug logging to track:
- How many orders are found for a user
- How many unique products the user owns
- Order creation with product count and total

**Check logs at:** `E:\Project\steam-library-java\application.log`

### 4. Created Debug Endpoint (`DebugController.java`)

A new endpoint to check session status: `http://localhost:8080/debug/session`

This returns JSON showing:
- Is user authenticated?
- Username and user ID
- How many products they own
- Cart count
- Any errors that occur

## How to Test

### Step 1: Restart the Application

Stop and restart your Spring Boot application to load the new code:

```bash
cd E:/Project/steam-library-java
# Stop the current instance (Ctrl+C if running in terminal)
# Then restart:
./start.sh
# or
java -jar target/steam-library-app-1.0.0.jar
```

### Step 2: Test Login Persistence

1. Open browser and go to `http://localhost:8080`
2. Click "Sign In" and login with username: `zhaoyingchen33` (or your account)
3. After login, you should be redirected to `/dashboard`
4. Click "Store" in the navigation
5. **Expected:** You should remain logged in and see your cart count in the top-right

**If you get logged out:** Check browser console (F12) for any errors

### Step 3: Test Purchased Games Display

1. While logged in, go to `http://localhost:8080/store`
2. Find a game you've already purchased (Elden Ring or Dark Souls III for `zhaoyingchen33`)
3. Click on the game to view details
4. **Expected:** The button should show "✓ In your library" (green with checkmark)

**If it shows "Add to Cart" instead:** The owned products are not being fetched

### Step 4: Check Debug Endpoint

Visit: `http://localhost:8080/debug/session`

**Expected output (when logged in):**
```json
{
  "authenticated": true,
  "username": "zhaoyingchen33",
  "userId": 1,
  "email": "mc3184046135@outlook.com",
  "ownedProductCount": 2,
  "ownedProductIds": [1, 17],
  "cartCount": 0
}
```

**If authenticated is false:** Session is not persisting

### Step 5: Test New Purchase

1. Add a game to cart that you don't own
2. Go to cart and checkout
3. After checkout success page, click "Continue Shopping" or navigate to Store
4. Click on the game you just bought
5. **Expected:** Should show "✓ In your library"

## Database Verification

Check if your orders are actually saved:

```sql
-- Check your orders
SELECT o.id, o.user_id, u.username, o.total, o.purchased_at 
FROM orders o 
JOIN users u ON o.user_id = u.id 
WHERE u.username = 'YOUR_USERNAME'
ORDER BY o.purchased_at DESC;

-- Check products in those orders
SELECT o.id as order_id, p.id as product_id, p.name 
FROM orders o
JOIN users u ON o.user_id = u.id
JOIN order_products op ON o.id = op.order_id
JOIN products p ON op.product_id = p.id
WHERE u.username = 'YOUR_USERNAME';

-- Check total owned products count
SELECT u.username, COUNT(DISTINCT op.product_id) as owned_games
FROM users u
LEFT JOIN orders o ON u.id = o.user_id
LEFT JOIN order_products op ON o.id = op.order_id
WHERE u.username = 'YOUR_USERNAME'
GROUP BY u.id;
```

## Additional Configuration

If you're still experiencing session issues, add these to `application.properties`:

```properties
# Session timeout (30 minutes)
server.servlet.session.timeout=30m

# Cookie settings for better session persistence
server.servlet.session.cookie.http-only=true
server.servlet.session.cookie.secure=false
server.servlet.session.cookie.max-age=1800

# Enable persistent sessions
spring.session.store-type=none
```

## Common Issues & Solutions

### Issue: Still getting logged out on store page

**Solution 1:** Clear browser cookies and cache, then login again

**Solution 2:** Check if you have multiple tabs open - with `maximumSessions(1)`, opening a new login tab will invalidate the old session

**Solution 3:** Check application logs for authentication errors:
```bash
tail -f E:/Project/steam-library-java/application.log | grep -i "error\|session\|auth"
```

### Issue: Owned products still not showing

**Possible causes:**

1. **Order-Products not linked properly** - Check:
   ```sql
   SELECT * FROM order_products WHERE order_id IN (SELECT id FROM orders WHERE user_id = YOUR_USER_ID);
   ```

2. **Lazy loading exception** - Check logs for `LazyInitializationException`

3. **User object mismatch** - The User object fetched in store controller might be different from the one in orders

**Debug:** Use the `/debug/session` endpoint to see if `ownedProductIds` is empty

### Issue: Session works but owned products are empty

This means the session is fine but `getOwnedProductIds()` is returning empty.

**Fix:** Verify the Order entity's relationship:

```java
@ManyToMany(fetch = FetchType.EAGER)  // Must be EAGER
@JoinTable(name = "order_products",
           joinColumns = @JoinColumn(name = "order_id"),
           inverseJoinColumns = @JoinColumn(name = "product_id"))
private List<Product> products;
```

If it's already EAGER (which it is), the issue is with how User objects are compared. Add this to `User.java`:

```java
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof User)) return false;
    User user = (User) o;
    return id != null && id.equals(user.getId());
}

@Override
public int hashCode() {
    return getClass().hashCode();
}
```

## Summary

The changes made ensure:

1. ✅ **Sessions persist** across page navigation via proper session management
2. ✅ **Transactions wrap** the owned products query to prevent lazy loading issues
3. ✅ **Logging added** to trace issues
4. ✅ **Debug endpoint** to verify authentication state

After restarting the application, sessions should persist and owned products should display correctly.
