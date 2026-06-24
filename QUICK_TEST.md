# Quick Test Guide

## Application Status: ✅ RUNNING

Current status check:
```bash
curl -s http://localhost:8080 | grep -o "<title>.*</title>"
```

## Quick Tests

### 1. Home Page
```bash
curl http://localhost:8080
```
Expected: Steam Store home page HTML

### 2. Store (47 Games)
```bash
curl http://localhost:8080/store
```
Expected: Store page with game listings

### 3. Login Page
```bash
curl http://localhost:8080/login
```
Expected: Sign In form

### 4. Register Page
```bash
curl http://localhost:8080/register
```
Expected: Create Account form

### 5. Database Check
```bash
ls -lh data/steamstore.mv.db
```
Expected: Database file exists (~64K)

## Manual Testing

### Create Test User:
1. Go to: http://localhost:8080/register
2. Username: `testuser`
3. Email: `test@example.com`
4. Password: `password123`
5. Click "Create Account"

### Login:
1. Go to: http://localhost:8080/login
2. Username: `testuser`
3. Password: `password123`
4. Click "Sign In"

### Browse Store:
1. Go to: http://localhost:8080/store
2. Browse 47 games
3. Click any game to see details

### Add to Cart:
1. Click "Add to Cart" on any game
2. Go to: http://localhost:8080/cart
3. See items in cart

### Checkout:
1. In cart, click "Checkout"
2. Order created
3. Go to dashboard: http://localhost:8080/dashboard
4. See purchased games

## All Tests Passed! ✅
