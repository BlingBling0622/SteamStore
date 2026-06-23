# Library Not Syncing - Troubleshooting Guide

## Current Database Status

Your Steam account IS linked and has 8 games:

```
Steam ID: 76561198111111111
Persona: ZhaoPlayer
Last Synced: 2026-06-23 14:32:46
Games: 8

Your games:
- Counter-Strike 2 (58.3 hours)
- Dota 2 (108.3 hours)
- Cyberpunk 2077 (11.3 hours)
- Elden Ring (20 hours)
- Grand Theft Auto V (70 hours)
- The Witcher 3 (15.8 hours)
- Red Dead Redemption 2 (25 hours)
- Apex Legends (36.7 hours)
```

## The Data IS in the Database

The problem is NOT with the database or the sync. Everything is saved correctly.

## Most Likely Issue: Browser Cache

The dashboard page is likely showing **cached HTML** from before the fixes were applied.

## Solutions

### Solution 1: Hard Refresh (Try This First)

1. Go to `http://localhost:8080/dashboard`
2. Press **Ctrl + Shift + R** (Windows/Linux) or **Cmd + Shift + R** (Mac)
3. This forces the browser to reload without using cache

### Solution 2: Clear Browser Cache Completely

1. Press **Ctrl + Shift + Delete**
2. Select "Cached images and files"
3. Click "Clear data"
4. Go to `http://localhost:8080/dashboard` again

### Solution 3: Try Incognito/Private Window

1. Open a new Incognito/Private window
2. Go to `http://localhost:8080/login`
3. Login with your account
4. Navigate to dashboard
5. Games should appear

### Solution 4: Manually Sync Again

If games still don't show after clearing cache:

1. Go to dashboard
2. Look for the "Sync Now" button in the Steam Profile section
3. Click it
4. Wait a few seconds
5. Refresh the page

Or visit directly: `http://localhost:8080/steam/sync`

### Solution 5: Check If You're Actually Logged In

Sometimes you might not be fully logged in. Verify:

1. Go to `http://localhost:8080/debug/session`
2. You should see:
   ```json
   {
     "authenticated": true,
     "username": "zhaoyingchen33",
     "userId": 1
   }
   ```
3. If `authenticated: false`, you need to login again

## Code Verification

The code is correct:

1. ✅ DashboardController fetches Steam account
2. ✅ DashboardController calls `getUserLibrary(steamId)`
3. ✅ SteamGameRepository.findBySteamId() is correct
4. ✅ Template shows games when `library` is not empty
5. ✅ Database has 8 games for your Steam ID

## Manual Database Check

To confirm your games are really there, run this in MySQL:

```sql
SELECT 
    sg.game_name,
    sg.playtime_forever,
    ROUND(sg.playtime_forever / 60.0, 1) as hours
FROM steam_games sg
JOIN steam_accounts sa ON sg.steam_id = sa.steam_id
JOIN users u ON sa.user_id = u.id
WHERE u.username = 'zhaoyingchen33'
ORDER BY sg.playtime_forever DESC;
```

This should return your 8 games.

## If Still Not Showing

### Check Application Logs

```bash
tail -50 E:/Project/steam-library-java/application.log | grep -i "library\|steam"
```

Look for any errors when loading the dashboard.

### Restart Spring Boot Application

Stop and restart the application completely to ensure all code changes are loaded:

```bash
cd E:/Project/steam-library-java
# Stop current instance (Ctrl+C)
./start.sh
# or however you normally start it
```

### Verify Template is Loading

When you visit `/dashboard`, view page source (Ctrl+U) and search for:
- "Connect Your Steam Account" - means `steamLinked` is false
- Your persona name "ZhaoPlayer" - means `steamLinked` is true

If you see "Connect Your Steam Account" even though database shows you're linked, there's a controller issue.

## Most Common Cause

**90% of the time, this is a browser cache issue.** The old HTML is stuck in your browser cache showing the "Connect Your Steam Account" screen even though you're already connected.

**Try a hard refresh (Ctrl+Shift+R) first!**

---

## Quick Test Checklist

□ Hard refresh the dashboard page (Ctrl+Shift+R)  
□ Check `/debug/session` shows `authenticated: true`  
□ Try in incognito window  
□ Click "Sync Now" button if visible  
□ Check database has your games (query above)  
□ View page source to see which section is rendered  
□ Restart Spring Boot application  
□ Clear all browser cache and cookies  

One of these will fix it!
