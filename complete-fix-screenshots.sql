-- COMPLETE FIX: Create columns and add different screenshots
-- This will work even if columns don't exist yet

USE steam_store;

-- Step 1: Add columns (ignore errors if they already exist)
ALTER TABLE products ADD COLUMN screenshot1_url VARCHAR(500);
ALTER TABLE products ADD COLUMN screenshot2_url VARCHAR(500);
ALTER TABLE products ADD COLUMN screenshot3_url VARCHAR(500);
ALTER TABLE products ADD COLUMN screenshot4_url VARCHAR(500);
ALTER TABLE products ADD COLUMN screenshot5_url VARCHAR(500);
ALTER TABLE products ADD COLUMN trailer_video_url VARCHAR(500);
ALTER TABLE products ADD COLUMN capsule_image_url VARCHAR(500);
ALTER TABLE products ADD COLUMN about_the_game VARCHAR(1000);

-- Step 2: Add DIFFERENT screenshots using other games' images
-- Product 1 gets images from products 2,3,4,5,6
UPDATE products p1
INNER JOIN products p2 ON p2.id = 2
INNER JOIN products p3 ON p3.id = 3
INNER JOIN products p4 ON p4.id = 4
INNER JOIN products p5 ON p5.id = 5
INNER JOIN products p6 ON p6.id = 6
SET
  p1.screenshot1_url = p2.header_image_url,
  p1.screenshot2_url = p3.header_image_url,
  p1.screenshot3_url = p4.header_image_url,
  p1.screenshot4_url = p5.header_image_url,
  p1.screenshot5_url = p6.header_image_url,
  p1.capsule_image_url = p1.header_image_url,
  p1.about_the_game = p1.description
WHERE p1.id = 1;

-- Product 2 gets images from products 7,8,9,10,11
UPDATE products p1
INNER JOIN products p2 ON p2.id = 7
INNER JOIN products p3 ON p3.id = 8
INNER JOIN products p4 ON p4.id = 9
INNER JOIN products p5 ON p5.id = 10
INNER JOIN products p6 ON p6.id = 11
SET
  p1.screenshot1_url = p2.header_image_url,
  p1.screenshot2_url = p3.header_image_url,
  p1.screenshot3_url = p4.header_image_url,
  p1.screenshot4_url = p5.header_image_url,
  p1.screenshot5_url = p6.header_image_url,
  p1.capsule_image_url = p1.header_image_url,
  p1.about_the_game = p1.description
WHERE p1.id = 2;

-- Product 3 gets images from products 12,13,14,15,16
UPDATE products p1
INNER JOIN products p2 ON p2.id = 12
INNER JOIN products p3 ON p3.id = 13
INNER JOIN products p4 ON p4.id = 14
INNER JOIN products p5 ON p5.id = 15
INNER JOIN products p6 ON p6.id = 16
SET
  p1.screenshot1_url = p2.header_image_url,
  p1.screenshot2_url = p3.header_image_url,
  p1.screenshot3_url = p4.header_image_url,
  p1.screenshot4_url = p5.header_image_url,
  p1.screenshot5_url = p6.header_image_url,
  p1.capsule_image_url = p1.header_image_url,
  p1.about_the_game = p1.description
WHERE p1.id = 3;

-- Verify
SELECT
  id,
  name,
  CASE WHEN screenshot1_url IS NOT NULL THEN 'YES' ELSE 'NO' END as has_screenshots,
  CASE WHEN screenshot1_url = screenshot2_url THEN 'SAME' ELSE 'DIFFERENT' END as are_different
FROM products
WHERE id IN (1,2,3);

-- Expected output:
-- id | name | has_screenshots | are_different
-- 1  | ...  | YES             | DIFFERENT
-- 2  | ...  | YES             | DIFFERENT
-- 3  | ...  | YES             | DIFFERENT
