# Purchased Games Now Show in Dashboard - FIXED

## Problem
When you purchased games from the store, they didn't appear in your library/dashboard. Only Steam-synced games were showing.

## Root Cause
The dashboard only displayed games from your **Steam library** (from the Steam API sync). Games purchased from the **store** are saved in a completely separate table (`orders` and `order_products`) and were never being fetched or displayed.

## What I Fixed

### 1. ✅ `DashboardController.java`
**Added:**
- Injected `StoreService` dependency
- Now calls `storeService.getPurchasedProducts(user)` to fetch purchased games
- Passes `purchasedGames` and `purchasedCount` to the template

### 2. ✅ `StoreService.java`
**Added new method:**
```java
public List<Product> getPurchasedProducts(User user)
```
- Fetches all orders for the user
- Extracts all products from those orders
- Returns distinct products (no duplicates)
- Includes logging for debugging

### 3. ✅ `dashboard.html`
**Added new section:**
- "Purchased Games" section that displays store purchases separately from Steam library
- Shows games with their header image and "Store Purchase" label
- Appears in TWO places:
  1. When Steam IS linked - shows below Steam library
  2. When Steam NOT linked - shows as standalone section

## How It Works Now

### Scenario 1: Steam Account Linked + Store Purchases
Your dashboard shows:
```
[Steam Profile Stats]
├─ Games Owned: 8
├─ Hours Played: 368.3
└─ Last Synced: 2026-06-23

[Steam Library (8)]
├─ Counter-Strike 2 (58.3 hrs)
├─ Dota 2 (108.3 hrs)
└─ ... (6 more Steam games)

[Purchased Games (2)]  ← NEW!
├─ Elden Ring (Store Purchase)
└─ Dark Souls III (Store Purchase)
```

### Scenario 2: No Steam Account + Store Purchases
Your dashboard shows:
```
[Connect Your Steam Account]
(Link button here)

[Purchased Games (2)]  ← Shows even without Steam!
├─ Elden Ring (Store Purchase)
└─ Dark Souls III (Store Purchase)
```

## Your Current Data

According to the database:

**Steam Library (8 games):**
- Counter-Strike 2
- Dota 2
- Cyberpunk 2077
- Elden Ring
- Grand Theft Auto V
- The Witcher 3
- Red Dead Redemption 2
- Apex Legends

**Store Purchases (2 games):**
- Elden Ring (Order #1)
- Dark Souls III (Order #2)

**Note:** Elden Ring appears in BOTH lists because:
1. You have it in your actual Steam library (synced from Steam API)
2. You also purchased it from the store

## Testing

1. **Login** at `http://localhost:8080/login`
2. **Go to dashboard** at `http://localhost:8080/dashboard`
3. **Scroll down** - you should now see a new section called "Purchased Games (2)"
4. It will show **Elden Ring** and **Dark Souls III** with "Store Purchase" label

## If Still Not Showing

### Refresh the Page
Press **Ctrl + Shift + R** (hard refresh) to clear cached HTML

### Check Database
Verify your purchases are actually in the database:
```sql
SELECT p.name, o.purchased_at 
FROM order_products op
JOIN orders o ON op.order_id = o.id
JOIN products p ON op.product_id = p.id
JOIN users u ON o.user_id = u.id
WHERE u.username = 'zhaoyingchen33'
ORDER BY o.purchased_at DESC;
```

Should return:
```
name                | purchased_at
Dark Souls III      | 2026-06-23 15:50:17
Elden Ring          | 2026-06-23 15:41:31
```

### Check Logs
Look for this in application logs:
```
User zhaoyingchen33 has purchased 2 unique products
```

## Summary

✅ **DashboardController** - Now fetches purchased games  
✅ **StoreService** - New method `getPurchasedProducts()`  
✅ **dashboard.html** - New "Purchased Games" section  
✅ **Works with or without Steam** - Shows purchases regardless of Steam link status  
✅ **No duplicates** - Each product shown once in the purchased section  

Your purchased games from the store (Elden Ring, Dark Souls III) will now appear in a separate "Purchased Games" section on your dashboard! 🎮
