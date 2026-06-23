-- SQL Script to Add Steam Media URLs to Your Games
-- This uses Steam's public CDN URLs (100% legal)

-- Example 1: Elden Ring (App ID: 1245620)
-- Find actual screenshot hashes from: https://store.steampowered.com/app/1245620/
UPDATE products SET
  trailer_video_url = 'https://cdn.akamai.steamstatic.com/steam/apps/1245620/movie480.webm',
  background_image_url = 'https://cdn.akamai.steamstatic.com/steam/apps/1245620/library_hero.jpg',
  screenshot1_url = 'https://cdn.akamai.steamstatic.com/steam/apps/1245620/ss_615b3b8d87d3878b2775e72d20fb3f3e9ad70928.1920x1080.jpg',
  screenshot2_url = 'https://cdn.akamai.steamstatic.com/steam/apps/1245620/ss_2321c2ab0a70c4fc8c6f2846e7a09b3f8c2f7c8f.1920x1080.jpg',
  screenshot3_url = 'https://cdn.akamai.steamstatic.com/steam/apps/1245620/ss_41e4daf519e3f1cf54de3c5b37e5737c3cc3b3c3.1920x1080.jpg',
  screenshot4_url = 'https://cdn.akamai.steamstatic.com/steam/apps/1245620/ss_8ddb471cdd13931b5a98b3f823be01c2c6638088.1920x1080.jpg',
  screenshot5_url = 'https://cdn.akamai.steamstatic.com/steam/apps/1245620/ss_07db6ce1bd0f94b64e045e70e86c6f8a76c85e81.1920x1080.jpg',
  about_the_game = 'THE NEW FANTASY ACTION RPG. Rise, Tarnished, and be guided by grace to brandish the power of the Elden Ring and become an Elden Lord in the Lands Between. A vast world where open fields with a variety of situations and huge dungeons with complex and three-dimensional designs are seamlessly connected.'
WHERE name = 'Elden Ring';

-- Example 2: Cyberpunk 2077 (App ID: 1091500)
UPDATE products SET
  trailer_video_url = 'https://cdn.akamai.steamstatic.com/steam/apps/1091500/movie480.webm',
  background_image_url = 'https://cdn.akamai.steamstatic.com/steam/apps/1091500/library_hero.jpg',
  screenshot1_url = 'https://cdn.akamai.steamstatic.com/steam/apps/1091500/ss_1.jpg',
  screenshot2_url = 'https://cdn.akamai.steamstatic.com/steam/apps/1091500/ss_2.jpg',
  screenshot3_url = 'https://cdn.akamai.steamstatic.com/steam/apps/1091500/ss_3.jpg',
  about_the_game = 'Cyberpunk 2077 is an open-world, action-adventure RPG set in the dark future of Night City — a dangerous megalopolis obsessed with power, glamor, and ceaseless body modification.'
WHERE name = 'Cyberpunk 2077';

-- Example 3: Dark Souls III (App ID: 374320)
UPDATE products SET
  trailer_video_url = 'https://cdn.akamai.steamstatic.com/steam/apps/374320/movie480.webm',
  background_image_url = 'https://cdn.akamai.steamstatic.com/steam/apps/374320/library_hero.jpg',
  screenshot1_url = 'https://cdn.akamai.steamstatic.com/steam/apps/374320/ss_1.jpg',
  screenshot2_url = 'https://cdn.akamai.steamstatic.com/steam/apps/374320/ss_2.jpg',
  about_the_game = 'Dark Souls continues to push the boundaries with the latest, ambitious chapter in the critically-acclaimed and genre-defining series. Prepare yourself and Embrace The Darkness!'
WHERE name = 'Dark Souls III';

-- Example 4: GTA V (App ID: 271590)
UPDATE products SET
  trailer_video_url = 'https://cdn.akamai.steamstatic.com/steam/apps/271590/movie480.webm',
  background_image_url = 'https://cdn.akamai.steamstatic.com/steam/apps/271590/library_hero.jpg',
  screenshot1_url = 'https://cdn.akamai.steamstatic.com/steam/apps/271590/ss_1.jpg',
  screenshot2_url = 'https://cdn.akamai.steamstatic.com/steam/apps/271590/ss_2.jpg',
  about_the_game = 'Grand Theft Auto V for PC offers players the option to explore the award-winning world of Los Santos and Blaine County in resolutions of up to 4k and beyond, as well as the chance to experience the game running at 60 frames per second.'
WHERE name = 'Grand Theft Auto V';

-- Example 5: The Witcher 3 (App ID: 292030)
UPDATE products SET
  trailer_video_url = 'https://cdn.akamai.steamstatic.com/steam/apps/292030/movie480.webm',
  background_image_url = 'https://cdn.akamai.steamstatic.com/steam/apps/292030/library_hero.jpg',
  screenshot1_url = 'https://cdn.akamai.steamstatic.com/steam/apps/292030/ss_1.jpg',
  screenshot2_url = 'https://cdn.akamai.steamstatic.com/steam/apps/292030/ss_2.jpg',
  about_the_game = 'As war rages on throughout the Northern Realms, you take on the greatest contract of your life — tracking down the Child of Prophecy, a living weapon that can alter the shape of the world.'
WHERE name = 'The Witcher 3';

-- ============================================
-- HOW TO FIND SCREENSHOT URLs FOR ANY GAME:
-- ============================================
-- 1. Visit the Steam store page: https://store.steampowered.com/app/{APP_ID}/
-- 2. Right-click on a screenshot → "Open image in new tab"
-- 3. Copy the URL, it will look like:
--    https://cdn.akamai.steamstatic.com/steam/apps/{APP_ID}/ss_{HASH}.1920x1080.jpg
-- 4. Use that exact URL in your SQL update

-- ============================================
-- TEMPLATE FOR ADDING MORE GAMES:
-- ============================================
/*
UPDATE products SET
  trailer_video_url = 'https://cdn.akamai.steamstatic.com/steam/apps/{APP_ID}/movie480.webm',
  background_image_url = 'https://cdn.akamai.steamstatic.com/steam/apps/{APP_ID}/library_hero.jpg',
  screenshot1_url = 'https://cdn.akamai.steamstatic.com/steam/apps/{APP_ID}/ss_{HASH1}.1920x1080.jpg',
  screenshot2_url = 'https://cdn.akamai.steamstatic.com/steam/apps/{APP_ID}/ss_{HASH2}.1920x1080.jpg',
  screenshot3_url = 'https://cdn.akamai.steamstatic.com/steam/apps/{APP_ID}/ss_{HASH3}.1920x1080.jpg',
  screenshot4_url = 'https://cdn.akamai.steamstatic.com/steam/apps/{APP_ID}/ss_{HASH4}.1920x1080.jpg',
  screenshot5_url = 'https://cdn.akamai.steamstatic.com/steam/apps/{APP_ID}/ss_{HASH5}.1920x1080.jpg',
  about_the_game = 'Extended game description here...'
WHERE name = 'Game Name';
*/

-- ============================================
-- COMMON STEAM APP IDs REFERENCE:
-- ============================================
-- Counter-Strike 2: 730
-- Dota 2: 570
-- Apex Legends: 1172470
-- Red Dead Redemption 2: 1174180
-- Sekiro: 814380
-- Resident Evil 2: 883710
-- Monster Hunter World: 582010
-- Hades: 1145360
-- Stardew Valley: 413150
-- Terraria: 105600

-- ============================================
-- VERIFY YOUR UPDATES:
-- ============================================
-- SELECT id, name, trailer_video_url, screenshot1_url
-- FROM products
-- WHERE trailer_video_url IS NOT NULL;
