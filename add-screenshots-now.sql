-- Copy and paste this entire block into your MySQL client or Workbench
-- This will add 5 screenshots to your first few products

USE steam_store;

-- Add screenshots to Product ID 1 (uses header image temporarily for testing)
UPDATE products SET
  screenshot1_url = header_image_url,
  screenshot2_url = header_image_url,
  screenshot3_url = header_image_url,
  screenshot4_url = header_image_url,
  screenshot5_url = header_image_url
WHERE id = 1;

-- Add screenshots to Product ID 2
UPDATE products SET
  screenshot1_url = header_image_url,
  screenshot2_url = header_image_url,
  screenshot3_url = header_image_url,
  screenshot4_url = header_image_url,
  screenshot5_url = header_image_url
WHERE id = 2;

-- Add screenshots to Product ID 3
UPDATE products SET
  screenshot1_url = header_image_url,
  screenshot2_url = header_image_url,
  screenshot3_url = header_image_url,
  screenshot4_url = header_image_url,
  screenshot5_url = header_image_url
WHERE id = 3;

-- Verify it worked
SELECT id, name,
  CASE WHEN screenshot1_url IS NOT NULL THEN '✓ YES' ELSE '✗ NO' END as has_screenshots
FROM products
WHERE id IN (1,2,3);

-- Expected output:
-- +----+------------------+-----------------+
-- | id | name             | has_screenshots |
-- +----+------------------+-----------------+
-- |  1 | Elden Ring       | ✓ YES           |
-- |  2 | Dark Souls III   | ✓ YES           |
-- |  3 | Cyberpunk 2077   | ✓ YES           |
-- +----+------------------+-----------------+
