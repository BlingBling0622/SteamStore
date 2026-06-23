# Steam Media Enhancement - Implementation Guide

## ✅ What's Been Implemented

### 1. **Enhanced Product Model**
Added new fields to `Product.java`:
- `screenshot1Url` through `screenshot5Url` - Five screenshot URLs
- `trailerVideoUrl` - Game trailer video (WebM format)
- `backgroundImageUrl` - Large background image
- `aboutTheGame` - Extended description (1000 chars)

### 2. **Enhanced Game Detail Page**
New features:
- **Video player** with play button overlay
- **Screenshot carousel** with 5 thumbnail slots
- **Click to switch** between video and images
- **Screenshots gallery tab** showing all images in grid
- **Responsive video controls**

---

## 📝 Steam CDN URL Patterns (Public & Legal)

Steam uses predictable URL patterns for their public CDN:

### **App ID Structure**
Each Steam game has an App ID. For example:
- Elden Ring: `1245620`
- Cyberpunk 2077: `1091500`
- Dark Souls III: `374320`

### **URL Patterns**

#### 1. Header Image (460x215)
```
https://cdn.akamai.steamstatic.com/steam/apps/{APP_ID}/header.jpg
```
Example: `https://cdn.akamai.steamstatic.com/steam/apps/1245620/header.jpg`

#### 2. Capsule Image (231x87)
```
https://cdn.akamai.steamstatic.com/steam/apps/{APP_ID}/capsule_231x87.jpg
```

#### 3. Screenshots (1920x1080)
```
https://cdn.akamai.steamstatic.com/steam/apps/{APP_ID}/ss_[HASH].1920x1080.jpg
```

**Problem**: Screenshot hashes are unique per game and can't be predicted.

**Solution**: Use Steam Web API or manual collection.

#### 4. Trailer Video (480p WebM)
```
https://cdn.akamai.steamstatic.com/steam/apps/{APP_ID}/movie480.webm
```

#### 5. Trailer Video (Max quality)
```
https://cdn.akamai.steamstatic.com/steam/apps/{APP_ID}/movie_max.webm
```

#### 6. Hero/Background Image
```
https://cdn.akamai.steamstatic.com/steam/apps/{APP_ID}/library_hero.jpg
```

---

## 🔧 How to Populate Your Database

### Option 1: Manual Entry (Recommended for Educational Project)

Update your `DataInitializer.java` to include media URLs for featured games:

```java
// Example for Elden Ring (App ID: 1245620)
Product eldenRing = new Product();
eldenRing.setName("Elden Ring");
eldenRing.setHeaderImageUrl("https://cdn.akamai.steamstatic.com/steam/apps/1245620/header.jpg");
eldenRing.setCapsuleImageUrl("https://cdn.akamai.steamstatic.com/steam/apps/1245620/capsule_231x87.jpg");
eldenRing.setTrailerVideoUrl("https://cdn.akamai.steamstatic.com/steam/apps/1245620/movie480.webm");
eldenRing.setBackgroundImageUrl("https://cdn.akamai.steamstatic.com/steam/apps/1245620/library_hero.jpg");

// Screenshots - you need to find these manually from Steam store page
eldenRing.setScreenshot1Url("https://cdn.akamai.steamstatic.com/steam/apps/1245620/ss_[HASH1].1920x1080.jpg");
eldenRing.setScreenshot2Url("https://cdn.akamai.steamstatic.com/steam/apps/1245620/ss_[HASH2].1920x1080.jpg");
// ... etc
```

### Option 2: Use Steam Web API (Legal, Requires API Key)

Steam provides a free Web API: https://partner.steamgames.com/doc/webapi

**Steps:**
1. Get a Steam Web API key (free): https://steamcommunity.com/dev/apikey
2. Use the API endpoint:
   ```
   https://store.steampowered.com/api/appdetails?appids={APP_ID}
   ```
3. This returns JSON with screenshots, videos, etc.

**Example Response:**
```json
{
  "1245620": {
    "success": true,
    "data": {
      "name": "Elden Ring",
      "screenshots": [
        {
          "id": 0,
          "path_thumbnail": "...",
          "path_full": "https://cdn.akamai.steamstatic.com/steam/apps/1245620/ss_abc123.jpg"
        }
      ],
      "movies": [
        {
          "id": 256867584,
          "webm": {
            "480": "https://cdn.akamai.steamstatic.com/steam/apps/256867584/movie480.webm",
            "max": "https://cdn.akamai.steamstatic.com/steam/apps/256867584/movie_max.webm"
          }
        }
      ]
    }
  }
}
```

---

## 🎯 Quick Implementation Guide

### Step 1: Find App IDs for Your Games

Visit: https://store.steampowered.com/

Search for a game and look at the URL:
```
https://store.steampowered.com/app/1245620/Elden_Ring/
                                    ^^^^^^^^
                                    App ID
```

### Step 2: Test URLs

Before adding to database, test if the URLs work:

```bash
# Test header image
curl -I https://cdn.akamai.steamstatic.com/steam/apps/1245620/header.jpg

# Test video
curl -I https://cdn.akamai.steamstatic.com/steam/apps/1245620/movie480.webm
```

### Step 3: Update Database

You can update existing products via SQL:

```sql
-- Update Elden Ring (assuming it's product ID 1)
UPDATE products SET
  trailer_video_url = 'https://cdn.akamai.steamstatic.com/steam/apps/1245620/movie480.webm',
  background_image_url = 'https://cdn.akamai.steamstatic.com/steam/apps/1245620/library_hero.jpg',
  screenshot1_url = 'https://cdn.akamai.steamstatic.com/steam/apps/1245620/ss_1.jpg',
  screenshot2_url = 'https://cdn.akamai.steamstatic.com/steam/apps/1245620/ss_2.jpg',
  about_the_game = 'THE NEW FANTASY ACTION RPG. Rise, Tarnished, and be guided by grace to brandish the power of the Elden Ring and become an Elden Lord in the Lands Between.'
WHERE id = 1;
```

---

## 📋 Common Steam App IDs

Here are some popular games with their App IDs:

| Game | App ID | URL Template |
|------|--------|--------------|
| Elden Ring | 1245620 | `steam/apps/1245620/...` |
| Cyberpunk 2077 | 1091500 | `steam/apps/1091500/...` |
| Dark Souls III | 374320 | `steam/apps/374320/...` |
| GTA V | 271590 | `steam/apps/271590/...` |
| Witcher 3 | 292030 | `steam/apps/292030/...` |
| Red Dead 2 | 1174180 | `steam/apps/1174180/...` |
| Counter-Strike 2 | 730 | `steam/apps/730/...` |
| Dota 2 | 570 | `steam/apps/570/...` |

---

## 🚀 What Your Users Will See

### With Media Populated:
1. **Hero section** with clickable video thumbnail
2. **Play button** overlay on main image
3. **5 screenshot thumbnails** below
4. **Click thumbnails** to switch between video/images
5. **Video plays** in the hero area with controls
6. **Screenshots tab** with full gallery grid

### Without Media (Fallback):
- Shows `headerImageUrl` as fallback
- No video player (graceful degradation)
- Works perfectly with existing data

---

## ⚠️ Important Notes

### Legal Compliance
✅ **Using Steam's public CDN URLs** - Legal and common practice
✅ **Referencing publicly available content** - Similar to embedding YouTube
✅ **Educational/portfolio project** - Fair use

❌ **Don't scrape Steam's website automatically**
❌ **Don't redistribute downloaded Steam content**
❌ **Don't claim content as your own**

### Performance
- Steam's CDN is fast and reliable
- Videos are lazy-loaded (preload="none")
- Images use Steam's optimized sizes

---

## 🎓 Example: Adding Media for One Game

```java
// In DataInitializer.java
Product eldenRing = productRepository.findById(1L).orElse(new Product());
eldenRing.setTrailerVideoUrl("https://cdn.akamai.steamstatic.com/steam/apps/1245620/movie480.webm");
eldenRing.setScreenshot1Url("https://cdn.akamai.steamstatic.com/steam/apps/1245620/ss_615b3b8d87d3878b2775e72d20fb3f3e9ad70928.1920x1080.jpg");
eldenRing.setScreenshot2Url("https://cdn.akamai.steamstatic.com/steam/apps/1245620/ss_2321c2ab0a70c4fc8c6f2846e7a09b3f8c2f7c8f.1920x1080.jpg");
// ... add more
eldenRing.setAboutTheGame("THE NEW FANTASY ACTION RPG. Rise, Tarnished, and be guided by grace...");
productRepository.save(eldenRing);
```

---

Your game detail pages are now ready to display rich media content! Just populate the database with Steam CDN URLs and everything will work automatically. 🎮✨
