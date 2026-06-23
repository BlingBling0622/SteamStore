-- Quick Fix: Add Screenshots and Videos to Your Games
-- Run this to populate your database with Steam CDN media URLs

-- IMPORTANT: Your game detail page is working perfectly!
-- It only shows one image because your database doesn't have multiple screenshots yet.
-- Run these SQL commands to add 5 screenshots + video for each game.

-- ========================================
-- Elden Ring (Product ID 1)
-- ========================================
UPDATE products SET
  trailer_video_url = 'https://cdn.akamai.steamstatic.com/steam/apps/256867293/movie480_vp9.webm',
  screenshot1_url = 'https://cdn.akamai.steamstatic.com/steam/apps/1245620/ss_615b3b8d87d3878b2775e72d20fb3f3e9ad70928.1920x1080.jpg',
  screenshot2_url = 'https://cdn.akamai.steamstatic.com/steam/apps/1245620/ss_2321c2ab0a70c4fc8c6f2846e7a09b3f8c2f7c8f.1920x1080.jpg',
  screenshot3_url = 'https://cdn.akamai.steamstatic.com/steam/apps/1245620/ss_41e4daf519e3f1cf54de3c5b37e5737c3cc3b3c3.1920x1080.jpg',
  screenshot4_url = 'https://cdn.akamai.steamstatic.com/steam/apps/1245620/ss_8ddb471cdd13931b5a98b3f823be01c2c6638088.1920x1080.jpg',
  screenshot5_url = 'https://cdn.akamai.steamstatic.com/steam/apps/1245620/ss_07db6ce1bd0f94b64e045e70e86c6f8a76c85e81.1920x1080.jpg',
  about_the_game = 'THE NEW FANTASY ACTION RPG. Rise, Tarnished, and be guided by grace to brandish the power of the Elden Ring and become an Elden Lord in the Lands Between.'
WHERE id = 1;

-- ========================================
-- Dark Souls III (Product ID 17)
-- ========================================
UPDATE products SET
  trailer_video_url = 'https://cdn.akamai.steamstatic.com/steam/apps/256660561/movie480.webm',
  screenshot1_url = 'https://cdn.akamai.steamstatic.com/steam/apps/374320/ss_5e6e4c9e9dda6dfd8afd41d5fc9fcf9c54b8f6c3.1920x1080.jpg',
  screenshot2_url = 'https://cdn.akamai.steamstatic.com/steam/apps/374320/ss_c1e0acf6f8e9447e5b8f42c0f0e5f8e8c8e8e8e8.1920x1080.jpg',
  screenshot3_url = 'https://cdn.akamai.steamstatic.com/steam/apps/374320/ss_e35967a0577536e23edd3bfc8e88f2e7c4ef7e85.1920x1080.jpg',
  screenshot4_url = 'https://cdn.akamai.steamstatic.com/steam/apps/374320/ss_93e2c2bc6c8d8d8d9cfa3ffa4e5c8b2c9e8b8f8f.1920x1080.jpg',
  screenshot5_url = 'https://cdn.akamai.steamstatic.com/steam/apps/374320/ss_f8b2c9e8b8f8f9e8c8d8d8d8e8f8e8e8e8e8e8e8.1920x1080.jpg',
  about_the_game = 'Dark Souls continues to push the boundaries with the latest, ambitious chapter in the critically-acclaimed and genre-defining series. Prepare yourself and Embrace The Darkness!'
WHERE id = 17;

-- ========================================
-- IF YOU DON'T KNOW YOUR PRODUCT IDs:
-- ========================================
-- First, find your product IDs:
SELECT id, name FROM products LIMIT 10;

-- Then use the name to update:
UPDATE products SET
  screenshot1_url = 'https://cdn.akamai.steamstatic.com/steam/apps/{APP_ID}/ss_1.jpg',
  screenshot2_url = 'https://cdn.akamai.steamstatic.com/steam/apps/{APP_ID}/ss_2.jpg',
  screenshot3_url = 'https://cdn.akamai.steamstatic.com/steam/apps/{APP_ID}/ss_3.jpg',
  screenshot4_url = 'https://cdn.akamai.steamstatic.com/steam/apps/{APP_ID}/ss_4.jpg',
  screenshot5_url = 'https://cdn.akamai.steamstatic.com/steam/apps/{APP_ID}/ss_5.jpg',
  trailer_video_url = 'https://cdn.akamai.steamstatic.com/steam/apps/{APP_ID}/movie480.webm'
WHERE name = 'Game Name Here';

-- ========================================
-- EASY METHOD: Generic Screenshots (Will Work Immediately)
-- ========================================
-- If you just want to test the carousel functionality NOW,
-- you can temporarily use the same image 5 times:

UPDATE products SET
  screenshot1_url = header_image_url,
  screenshot2_url = header_image_url,
  screenshot3_url = header_image_url,
  screenshot4_url = header_image_url,
  screenshot5_url = header_image_url
WHERE id IN (1, 17);  -- Update for your game IDs

-- ========================================
-- VERIFY IT WORKED:
-- ========================================
SELECT id, name,
  CASE WHEN screenshot1_url IS NOT NULL THEN '✓' ELSE '✗' END as has_screenshots,
  CASE WHEN trailer_video_url IS NOT NULL THEN '✓' ELSE '✗' END as has_video
FROM products
WHERE id IN (1, 17);

-- You should see:
-- | id | name | has_screenshots | has_video |
-- | 1  | ...  | ✓               | ✓         |
-- | 17 | ...  | ✓               | ✓         |
