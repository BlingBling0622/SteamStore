-- PASTE THIS INTO MYSQL WORKBENCH AND CLICK EXECUTE!
-- This will add 5 screenshots to ALL your products RIGHT NOW

USE steam_store;

-- Add screenshots to all products (uses header_image_url as temporary placeholder)
UPDATE products
SET
  screenshot1_url = header_image_url,
  screenshot2_url = header_image_url,
  screenshot3_url = header_image_url,
  screenshot4_url = header_image_url,
  screenshot5_url = header_image_url,
  capsule_image_url = header_image_url,
  about_the_game = description
WHERE screenshot1_url IS NULL;

-- Check how many were updated
SELECT COUNT(*) as 'Products Updated' FROM products WHERE screenshot1_url IS NOT NULL;

-- Now refresh your browser at http://localhost:8080/store/game/1
-- You should see 5 thumbnails in the carousel!
