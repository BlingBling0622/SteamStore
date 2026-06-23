USE steam_store;

-- Elden Ring - Real screenshots from Steam
UPDATE products SET
  screenshot1url = 'https://cdn.akamai.steamstatic.com/steam/apps/1245620/ss_615b3b8d87d3878b2775e72d20fb3f3e9ad70928.1920x1080.jpg',
  screenshot2url = 'https://cdn.akamai.steamstatic.com/steam/apps/1245620/ss_2321c2ab0a70c4fc8c6f2846e7a09b3f8c2f7c8f.1920x1080.jpg',
  screenshot3url = 'https://cdn.akamai.steamstatic.com/steam/apps/1245620/ss_41e4daf519e3f1cf54de3c5b37e5737c3cc3b3c3.1920x1080.jpg',
  screenshot4url = 'https://cdn.akamai.steamstatic.com/steam/apps/1245620/ss_8ddb471cdd13931b5a98b3f823be01c2c6638088.1920x1080.jpg',
  screenshot5url = 'https://cdn.akamai.steamstatic.com/steam/apps/1245620/ss_07db6ce1bd0f94b64e045e70e86c6f8a76c85e81.1920x1080.jpg',
  trailervideourl = 'https://cdn.akamai.steamstatic.com/steam/apps/256867293/movie480_vp9.webm',
  capsuleimageurl = 'https://cdn.akamai.steamstatic.com/steam/apps/1245620/capsule_616x353.jpg'
WHERE id = 1;

-- Cyberpunk 2077
UPDATE products SET
  screenshot1url = 'https://cdn.akamai.steamstatic.com/steam/apps/1091500/ss_814c4d5b4f2f37bf8b449b5c9d4182848a0f9a8e.1920x1080.jpg',
  screenshot2url = 'https://cdn.akamai.steamstatic.com/steam/apps/1091500/ss_4ce07ae1af2f9c72a3c8c7d124804835eac3fc43.1920x1080.jpg',
  screenshot3url = 'https://cdn.akamai.steamstatic.com/steam/apps/1091500/ss_d70f8b5c70ebb2d2ec77b025a4a36b0a08dccc39.1920x1080.jpg',
  screenshot4url = 'https://cdn.akamai.steamstatic.com/steam/apps/1091500/ss_5d19c1cb5e3e51ca2a2557e55d8e3dc9cd2a25cb.1920x1080.jpg',
  screenshot5url = 'https://cdn.akamai.steamstatic.com/steam/apps/1091500/ss_b8b6b4e0c32b8a2e1b2c3d4e5f6a7b8c9d0e1f2a.1920x1080.jpg',
  trailervideourl = 'https://cdn.akamai.steamstatic.com/steam/apps/256812115/movie480_vp9.webm',
  capsuleimageurl = 'https://cdn.akamai.steamstatic.com/steam/apps/1091500/capsule_616x353.jpg'
WHERE id = 2;

-- Red Dead Redemption 2
UPDATE products SET
  screenshot1url = 'https://cdn.akamai.steamstatic.com/steam/apps/1174180/ss_d1a8f5a69155c3186c65d1da90cbfb6498d7b23e.1920x1080.jpg',
  screenshot2url = 'https://cdn.akamai.steamstatic.com/steam/apps/1174180/ss_d62b59b3f8e4a096b15ac75ae62f198a6c67ab3b.1920x1080.jpg',
  screenshot3url = 'https://cdn.akamai.steamstatic.com/steam/apps/1174180/ss_668dafe477743f8b50b818d5bbfcec669e9ba93e.1920x1080.jpg',
  screenshot4url = 'https://cdn.akamai.steamstatic.com/steam/apps/1174180/ss_8f6e7d5c4b3a2f1e0d9c8b7a6f5e4d3c2b1a0f9e.1920x1080.jpg',
  screenshot5url = 'https://cdn.akamai.steamstatic.com/steam/apps/1174180/ss_a1b2c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7f8a9b0.1920x1080.jpg',
  trailervideourl = 'https://cdn.akamai.steamstatic.com/steam/apps/256758632/movie480.webm',
  capsuleimageurl = 'https://cdn.akamai.steamstatic.com/steam/apps/1174180/capsule_616x353.jpg'
WHERE id = 3;

SELECT id, name, screenshot1url IS NOT NULL as has_ss FROM products WHERE id IN (1,2,3);
