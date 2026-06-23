USE steam_store;

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
  p1.screenshot5_url = p6.header_image_url
WHERE p1.id = 1;

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
  p1.screenshot5_url = p6.header_image_url
WHERE p1.id = 2;

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
  p1.screenshot5_url = p6.header_image_url
WHERE p1.id = 3;

SELECT id, name, 'Screenshots Updated' as status FROM products WHERE id IN (1,2,3);
