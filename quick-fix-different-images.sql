-- SIMPLE SOLUTION: Use different game headers as screenshots temporarily
-- This will give you 5 DIFFERENT images in the carousel RIGHT NOW
-- Then you can manually replace with real screenshots later

USE steam_store;

-- Update Cyberpunk 2077 with different images
UPDATE products SET
  screenshot1_url = (SELECT header_image_url FROM products WHERE name = 'Cyberpunk 2077'),
  screenshot2_url = (SELECT header_image_url FROM products WHERE name = 'Elden Ring'),
  screenshot3_url = (SELECT header_image_url FROM products WHERE name = 'The Witcher 3: Wild Hunt'),
  screenshot4_url = (SELECT header_image_url FROM products WHERE name = 'Dark Souls III'),
  screenshot5_url = (SELECT header_image_url FROM products WHERE name = 'Grand Theft Auto V')
WHERE name = 'Cyberpunk 2077';

-- Update Elden Ring with different images
UPDATE products SET
  screenshot1_url = (SELECT header_image_url FROM products WHERE name = 'Elden Ring'),
  screenshot2_url = (SELECT header_image_url FROM products WHERE name = 'Dark Souls III'),
  screenshot3_url = (SELECT header_image_url FROM products WHERE name = 'Cyberpunk 2077'),
  screenshot4_url = (SELECT header_image_url FROM products WHERE name = 'The Witcher 3: Wild Hunt'),
  screenshot5_url = (SELECT header_image_url FROM products WHERE name = 'Red Dead Redemption 2')
WHERE name = 'Elden Ring';

-- Update Dark Souls III with different images
UPDATE products SET
  screenshot1_url = (SELECT header_image_url FROM products WHERE name = 'Dark Souls III'),
  screenshot2_url = (SELECT header_image_url FROM products WHERE name = 'Elden Ring'),
  screenshot3_url = (SELECT header_image_url FROM products WHERE name = 'Skyrim Special Edition'),
  screenshot4_url = (SELECT header_image_url FROM products WHERE name = 'Monster Hunter: World'),
  screenshot5_url = (SELECT header_image_url FROM products WHERE name = 'Baldur\'s Gate 3')
WHERE name = 'Dark Souls III';

-- Verify - should see 5 DIFFERENT screenshots now
SELECT name,
  SUBSTRING(screenshot1_url, 50, 30) as ss1,
  SUBSTRING(screenshot2_url, 50, 30) as ss2,
  SUBSTRING(screenshot3_url, 50, 30) as ss3
FROM products
WHERE name IN ('Cyberpunk 2077', 'Elden Ring', 'Dark Souls III');

-- NOW REFRESH YOUR BROWSER!
-- You should see 5 DIFFERENT images in the carousel!
