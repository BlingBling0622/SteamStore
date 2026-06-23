# Bug Report - Steam Library Java Application

## Critical Bugs

### 1. **Missing Steam Link Section on Dashboard (Dashboard shows no Steam integration for users without linked accounts)**
**Location:** `src/main/resources/templates/dashboard.html`
**Issue:** When a user is logged in but hasn't linked their Steam account, the dashboard is completely empty except for the header. There's no UI to prompt them to link their Steam account.
**Impact:** Users have no way to discover or use the Steam linking feature.
**Fix Required:** Add a section that displays when `steamLinked == false` with a call-to-action button to link Steam account.

### 2. **N+1 Query Problem in Cart Operations**
**Location:** `src/main/java/com/steamlibrary/service/StoreService.java:28-30`
**Issue:** `getCartTotal()` calls `getCart()` which fetches all cart items, then iterates to calculate total. When called alongside `getCart()` in the same controller method (like in `StoreController.cart()`), it fetches cart items twice.
**Impact:** Performance degradation, unnecessary database queries.
**Fix Required:** Refactor to calculate total from already-fetched cart items or use a database aggregate query.

### 3. **Missing Eager Fetch Strategy Causes LazyInitializationException Risk**
**Location:** `src/main/java/com/steamlibrary/model/Order.java:25-29`
**Issue:** Order has `@ManyToMany` relationship with Products using `EAGER` fetch, but User uses `LAZY`. When accessing `order.getUser()` outside a transaction context, it can throw `LazyInitializationException`.
**Impact:** Potential runtime exceptions when displaying order history.
**Fix Required:** Either make User fetch EAGER in Order or ensure transactional context when accessing orders.

### 4. **Inconsistent Null Handling in Product Discount**
**Location:** `src/main/java/com/steamlibrary/model/Product.java:29`
**Issue:** `discountPercent` is initialized to `0` but is an `Integer` (nullable type). In templates, checks use `p.discountPercent > 0` which could fail if the value is null from external data sources.
**Impact:** Potential NullPointerException when rendering prices.
**Fix Required:** Change to `int` primitive type or add null-safe checks in templates.

## Major Bugs

### 5. **Steam API Error Handling Swallows Exceptions**
**Location:** `src/main/java/com/steamlibrary/service/SteamService.java:89-93`
**Issue:** When Steam API calls fail, generic RuntimeExceptions are thrown with just the message. No logging of the actual error, no retry logic, no circuit breaker.
**Impact:** Difficult to debug Steam integration issues; poor user experience.
**Fix Required:** Add proper logging, specific exception types, and user-friendly error messages.

### 6. **Security: Steam Account Hijacking Vulnerability**
**Location:** `src/main/java/com/steamlibrary/service/SteamService.java:44-47`
**Issue:** When linking a Steam account, the code checks if the Steam ID is already linked to another user and throws an exception. However, there's no check to prevent a user from unlinking and re-linking to a different account repeatedly, or from linking to an invalid/fake Steam ID.
**Impact:** Users could potentially link to Steam IDs they don't own (if OpenID verification is bypassed or fails).
**Fix Required:** Add additional verification, rate limiting, and audit logging.

### 7. **Missing Transaction Boundary**
**Location:** `src/main/java/com/steamlibrary/repository/CartRepository.java:12`
**Issue:** `deleteByUser(User user)` is a derived delete query but the calling method `StoreService.checkout()` is `@Transactional`. However, if an exception occurs after `deleteByUser` but before the transaction commits, the cart items might be deleted without creating the order.
**Impact:** Data loss - cart items deleted but order not saved.
**Fix Required:** Ensure delete happens after order save is confirmed, or use proper exception handling.

### 8. **Missing Index on Foreign Keys**
**Location:** All entity classes
**Issue:** JPA/Hibernate creates foreign key columns but doesn't automatically add indexes. Queries like `findByUser()`, `findBySteamId()`, etc. will do table scans as data grows.
**Impact:** Performance degradation at scale.
**Fix Required:** Add `@Table(indexes = ...)` annotations to entities.

## Minor Bugs

### 9. **Inconsistent Date Formatting in Dashboard**
**Location:** `src/main/resources/templates/dashboard.html:104`
**Issue:** Uses `#temporals.format(steamAccount.lastSynced, 'MMM dd HH:mm')` which doesn't show the year and uses 24-hour format without timezone.
**Impact:** User confusion about sync times, especially for old syncs.
**Fix Required:** Add year and use locale-aware date formatting.

### 10. **Missing CSRF Token in Logout Form**
**Location:** `src/main/resources/templates/dashboard.html:68-70`
**Issue:** Logout form submits via POST but Thymeleaf should auto-add CSRF token. However, the form uses inline style which might break in some Thymeleaf versions.
**Impact:** Potential logout failure if CSRF token is not included.
**Fix Required:** Verify CSRF token is included; use Thymeleaf form tag features properly.

### 11. **No Validation on RegisterRequest**
**Location:** `src/main/java/com/steamlibrary/dto/RegisterRequest.java` (not read but inferred from usage)
**Issue:** The controller uses `@Valid` but the DTO class likely has minimal validation annotations.
**Impact:** Weak input validation allowing malformed data.
**Fix Required:** Add `@NotBlank`, `@Email`, `@Size`, and custom password strength validators.

### 12. **Race Condition in Product Seeding**
**Location:** `src/main/java/com/steamlibrary/config/DataInitializer.java:20`
**Issue:** Uses `productRepository.count() > 0` to check if data exists, but in a multi-instance deployment, two instances could both see count=0 and try to insert, causing duplicate key errors.
**Impact:** Application startup failure in clustered environments.
**Fix Required:** Use database-level unique constraints and handle duplicate key exceptions gracefully.

### 13. **Image URL Error Handling Missing**
**Location:** `src/main/resources/templates/dashboard.html:132`
**Issue:** Uses `onerror="this.src='...'"` for fallback image, but hardcodes a specific game's header (appid 220 = Half-Life 2).
**Impact:** All broken images show Half-Life 2, which is confusing.
**Fix Required:** Use a generic placeholder image hosted locally.

### 14. **Missing Cart Count in Store Page for Unauthenticated Users**
**Location:** `src/main/java/com/steamlibrary/controller/StoreController.java:22-30`
**Issue:** When `auth == null`, cart count is not set, causing potential NPE in template if template references `${cartCount}`.
**Impact:** Potential error for anonymous users (though template uses null-safe `cartCount != null` check).
**Fix Required:** Always set cartCount, use 0 for anonymous users.

### 15. **Steam Library Sync is Not Atomic**
**Location:** `src/main/java/com/steamlibrary/service/SteamService.java:99-129`
**Issue:** `syncSteamLibrary()` deletes all games, then saves new ones. If the Steam API call fails after delete, user loses all library data.
**Impact:** Data loss on transient API failures.
**Fix Required:** Use a temporary table or defer delete until after successful fetch and save.

## Recommendations

### Performance
- Add database connection pooling configuration (HikariCP settings)
- Implement Redis caching for Steam API responses (15-minute TTL)
- Add pagination to `/store` endpoint for large product catalogs

### Security
- Add rate limiting on Steam link/sync endpoints
- Implement CSRF protection verification in all forms
- Add SQL injection protection verification (JPA handles this, but audit native queries if any)
- Sanitize user inputs in registration

### UX
- Add loading indicators for Steam sync operations
- Add confirmation dialog before unlinking Steam account
- Show error details in a user-friendly way
- Add "last updated" timestamp to product catalog

### Code Quality
- Add unit tests for service layer
- Add integration tests for controllers
- Add custom exception classes instead of generic RuntimeException
- Add API documentation (Swagger/OpenAPI)
- Extract magic strings to constants or configuration

## Database Schema Issues

### Missing Constraints
1. No unique constraint on `cart_items(user_id, product_id)` - users could add same product multiple times
2. No check constraint on `products.price >= 0`
3. No check constraint on `products.discount_percent BETWEEN 0 AND 100`

### Missing Columns
1. `products` table missing `created_at`, `updated_at` timestamps
2. `orders` table missing `status` enum (pending, completed, refunded)
3. `cart_items` missing `created_at` timestamp
