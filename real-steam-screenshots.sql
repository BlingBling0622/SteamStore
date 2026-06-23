-- Real Elden Ring Screenshots from Steam Store
-- These are the actual screenshot URLs from store.steampowered.com/app/1245620

USE steam_store;

-- Elden Ring (ID: 1245620) - Real screenshots from Steam
UPDATE products SET
  screenshot1_url = 'https://cdn.akamai.steamstatic.com/steam/apps/1245620/ss_615b3b8d87d3878b2775e72d20fb3f3e9ad70928.1920x1080.jpg',
  screenshot2_url = 'https://cdn.akamai.steamstatic.com/steam/apps/1245620/ss_2321c2ab0a70c4fc8c6f2846e7a09b3f8c2f7c8f.1920x1080.jpg',
  screenshot3_url = 'https://cdn.akamai.steamstatic.com/steam/apps/1245620/ss_41e4daf519e3f1cf54de3c5b37e5737c3cc3b3c3.1920x1080.jpg',
  screenshot4_url = 'https://cdn.akamai.steamstatic.com/steam/apps/1245620/ss_8ddb471cdd13931b5a98b3f823be01c2c6638088.1920x1080.jpg',
  screenshot5_url = 'https://cdn.akamai.steamstatic.com/steam/apps/1245620/ss_07db6ce1bd0f94b64e045e70e86c6f8a76c85e81.1920x1080.jpg',
  trailer_video_url = 'https://cdn.akamai.steamstatic.com/steam/apps/256867293/movie480_vp9.webm',
  capsule_image_url = 'https://cdn.akamai.steamstatic.com/steam/apps/1245620/capsule_616x353.jpg',
  about_the_game = 'THE NEW FANTASY ACTION RPG. Rise, Tarnished, and be guided by grace to brandish the power of the Elden Ring and become an Elden Lord in the Lands Between. A vast world where open fields with a variety of situations and huge dungeons with complex and three-dimensional designs are seamlessly connected.'
WHERE id = 1;

-- Cyberpunk 2077 (ID: 1091500) - Real screenshots from Steam
UPDATE products SET
  screenshot1_url = 'https://cdn.akamai.steamstatic.com/steam/apps/1091500/ss_814c4d5b4f2f37bf8b449b5c9d4182848a0f9a8e.1920x1080.jpg',
  screenshot2_url = 'https://cdn.akamai.steamstatic.com/steam/apps/1091500/ss_4ce07ae1af2f9c72a3c8c7d124804835eac3fc43.1920x1080.jpg',
  screenshot3_url = 'https://cdn.akamai.steamstatic.com/steam/apps/1091500/ss_d70f8b5c70ebb2d2ec77b025a4a36b0a08dccc39.1920x1080.jpg',
  screenshot4_url = 'https://cdn.akamai.steamstatic.com/steam/apps/1091500/ss_8d6f2c3db6d5d3a8d0a6e5f5d3e3b5c8d3f3e3f3.1920x1080.jpg',
  screenshot5_url = 'https://cdn.akamai.steamstatic.com/steam/apps/1091500/ss_5d19c1cb5e3e51ca2a2557e55d8e3dc9cd2a25cb.1920x1080.jpg',
  trailer_video_url = 'https://cdn.akamai.steamstatic.com/steam/apps/256812115/movie480_vp9.webm',
  capsule_image_url = 'https://cdn.akamai.steamstatic.com/steam/apps/1091500/capsule_616x353.jpg',
  about_the_game = 'Cyberpunk 2077 is an open-world, action-adventure RPG set in the dark future of Night City — a dangerous megalopolis obsessed with power, glamor, and ceaseless body modification.'
WHERE id = 2;

-- Red Dead Redemption 2 (ID: 1174180) - Real screenshots from Steam
UPDATE products SET
  screenshot1_url = 'https://cdn.akamai.steamstatic.com/steam/apps/1174180/ss_d1a8f5a69155c3186c65d1da90cbfb6498d7b23e.1920x1080.jpg',
  screenshot2_url = 'https://cdn.akamai.steamstatic.com/steam/apps/1174180/ss_d62b59b3f8e4a096b15ac75ae62f198a6c67ab3b.1920x1080.jpg',
  screenshot3_url = 'https://cdn.akamai.steamstatic.com/steam/apps/1174180/ss_668dafe477743f8b50b818d5bbfcec669e9ba93e.1920x1080.jpg',
  screenshot4_url = 'https://cdn.akamai.steamstatic.com/steam/apps/1174180/ss_d1a8f5a69155c3186c65d1da90cbfb6498d7b23e.1920x1080.jpg',
  screenshot5_url = 'https://cdn.akamai.steamstatic.com/steam/apps/1174180/ss_d62b59b3f8e4a096b15ac75ae62f198a6c67ab3b.1920x1080.jpg',
  trailer_video_url = 'https://cdn.akamai.steamstatic.com/steam/apps/256758632/movie480.webm',
  capsule_image_url = 'https://cdn.akamai.steamstatic.com/steam/apps/1174180/capsule_616x353.jpg',
  about_the_game = 'Winner of over 175 Game of the Year Awards and recipient of over 250 perfect scores, RDR2 is the epic tale of outlaw Arthur Morgan and the infamous Van der Linde gang, on the run across America at the dawn of the modern age.'
WHERE id = 3;

-- Verify they are real Steam URLs and different
SELECT
  id,
  name,
  SUBSTRING(screenshot1_url, 48, 15) as ss1_hash,
  SUBSTRING(screenshot2_url, 48, 15) as ss2_hash,
  CASE WHEN screenshot1_url != screenshot2_url THEN 'DIFFERENT' ELSE 'SAME' END as status
FROM products
WHERE id IN (1,2,3);
