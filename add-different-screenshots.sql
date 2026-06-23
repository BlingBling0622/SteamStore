-- Add DIFFERENT screenshot URLs to your products
-- These use actual game header images as placeholders for screenshots
-- Run this in MySQL Workbench or command line

USE steam_store;

-- First, let's add different images to the first 10 products
-- We'll use other games' header images as "screenshots" so they're actually different

-- Product 1: Gets screenshots from products 2,3,4,5,6
UPDATE products SET
  screenshot1_url = (SELECT header_image_url FROM products WHERE id = 2),
  screenshot2_url = (SELECT header_image_url FROM products WHERE id = 3),
  screenshot3_url = (SELECT header_image_url FROM products WHERE id = 4),
  screenshot4_url = (SELECT header_image_url FROM products WHERE id = 5),
  screenshot5_url = (SELECT header_image_url FROM products WHERE id = 6)
WHERE id = 1;

-- Product 2: Gets screenshots from products 1,3,5,7,9
UPDATE products SET
  screenshot1_url = (SELECT header_image_url FROM products WHERE id = 1),
  screenshot2_url = (SELECT header_image_url FROM products WHERE id = 3),
  screenshot3_url = (SELECT header_image_url FROM products WHERE id = 5),
  screenshot4_url = (SELECT header_image_url FROM products WHERE id = 7),
  screenshot5_url = (SELECT header_image_url FROM products WHERE id = 9)
WHERE id = 2;

-- Product 3: Gets screenshots from products 1,2,4,6,8
UPDATE products SET
  screenshot1_url = (SELECT header_image_url FROM products WHERE id = 1),
  screenshot2_url = (SELECT header_image_url FROM products WHERE id = 2),
  screenshot3_url = (SELECT header_image_url FROM products WHERE id = 4),
  screenshot4_url = (SELECT header_image_url FROM products WHERE id = 6),
  screenshot5_url = (SELECT header_image_url FROM products WHERE id = 8)
WHERE id = 3;

-- Add to ALL remaining products (using same pattern)
UPDATE products SET
  screenshot1_url = header_image_url,
  screenshot2_url = (SELECT header_image_url FROM products WHERE id = (id % 100) + 1),
  screenshot3_url = (SELECT header_image_url FROM products WHERE id = (id % 100) + 2),
  screenshot4_url = (SELECT header_image_url FROM products WHERE id = (id % 100) + 3),
  screenshot5_url = (SELECT header_image_url FROM products WHERE id = (id % 100) + 4)
WHERE id > 3;

-- Verify the results
SELECT
  id,
  name,
  SUBSTRING(screenshot1_url, 50, 20) as ss1,
  SUBSTRING(screenshot2_url, 50, 20) as ss2,
  SUBSTRING(screenshot3_url, 50, 20) as ss3,
  SUBSTRING(screenshot4_url, 50, 20) as ss4,
  SUBSTRING(screenshot5_url, 50, 20) as ss5
FROM products
WHERE id <= 5;

-- You should see DIFFERENT URLs for each screenshot!
-- Now refresh http://localhost:8080/store/game/1 and you'll see 5 different images!
