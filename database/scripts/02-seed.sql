-- 1. Insert Categories (The Inner Ring)
INSERT INTO taste_categories (name, color_code) VALUES 
('Fruity', '#DA1F3D'),         -- Red
('Floral', '#E04192'),         -- Pink
('Sweet', '#F68B28'),          -- Orange
('Nutty/Cocoa', '#A06D3E'),    -- Light Brown
('Spices', '#C93133'),         -- Dark Red
('Roasted', '#5F3B25'),        -- Dark Brown
('Vegetal', '#609348'),        -- Green
('Sour/Fermented', '#E9C706'), -- Yellow
('Other', '#659FB8');          -- Blue/Grey (Chemical/Papery - kept for completeness)

-- 2. Insert Tastes (The Middle Ring) linked to Categories
-- Category: Fruity
INSERT INTO tastes (category_id, name) VALUES 
((SELECT id FROM taste_categories WHERE name = 'Fruity'), 'Berry'),
((SELECT id FROM taste_categories WHERE name = 'Fruity'), 'Dried Fruit'),
((SELECT id FROM taste_categories WHERE name = 'Fruity'), 'Other Fruit'),
((SELECT id FROM taste_categories WHERE name = 'Fruity'), 'Citrus Fruit');

-- Category: Floral
INSERT INTO tastes (category_id, name) VALUES 
((SELECT id FROM taste_categories WHERE name = 'Floral'), 'Floral'),
((SELECT id FROM taste_categories WHERE name = 'Floral'), 'Black Tea');

-- Category: Sweet
INSERT INTO tastes (category_id, name) VALUES 
((SELECT id FROM taste_categories WHERE name = 'Sweet'), 'Sweet Aromatics'),
((SELECT id FROM taste_categories WHERE name = 'Sweet'), 'Brown Sugar'),
((SELECT id FROM taste_categories WHERE name = 'Sweet'), 'Vanilla');

-- Category: Nutty/Cocoa
INSERT INTO tastes (category_id, name) VALUES 
((SELECT id FROM taste_categories WHERE name = 'Nutty/Cocoa'), 'Nutty'),
((SELECT id FROM taste_categories WHERE name = 'Nutty/Cocoa'), 'Cocoa');

-- Category: Spices
INSERT INTO tastes (category_id, name) VALUES 
((SELECT id FROM taste_categories WHERE name = 'Spices'), 'Brown Spice'),
((SELECT id FROM taste_categories WHERE name = 'Spices'), 'Pepper'),
((SELECT id FROM taste_categories WHERE name = 'Spices'), 'Pungent');

-- Category: Roasted
INSERT INTO tastes (category_id, name) VALUES 
((SELECT id FROM taste_categories WHERE name = 'Roasted'), 'Pipe Tobacco'),
((SELECT id FROM taste_categories WHERE name = 'Roasted'), 'Tobacco'),
((SELECT id FROM taste_categories WHERE name = 'Roasted'), 'Burnt'),
((SELECT id FROM taste_categories WHERE name = 'Roasted'), 'Cereal');

-- Category: Vegetal
INSERT INTO tastes (category_id, name) VALUES 
((SELECT id FROM taste_categories WHERE name = 'Vegetal'), 'Olive Oil'),
((SELECT id FROM taste_categories WHERE name = 'Vegetal'), 'Raw'),
((SELECT id FROM taste_categories WHERE name = 'Vegetal'), 'Green/Vegetative'),
((SELECT id FROM taste_categories WHERE name = 'Vegetal'), 'Beany');

-- Category: Sour/Fermented
INSERT INTO tastes (category_id, name) VALUES 
((SELECT id FROM taste_categories WHERE name = 'Sour/Fermented'), 'Sour Aromatics'),
((SELECT id FROM taste_categories WHERE name = 'Sour/Fermented'), 'Alcohol/Fermented');

-- Category: Other
INSERT INTO tastes (category_id, name) VALUES 
((SELECT id FROM taste_categories WHERE name = 'Other'), 'Papery/Musty'),
((SELECT id FROM taste_categories WHERE name = 'Other'), 'Chemical');


-- 3. BREWING METHODS
INSERT INTO brewing_methods (name, description, icon_url) VALUES 
('Espresso', 'Concentrated coffee brewed by forcing hot water under pressure.', 'icons/espresso.png'),
('V60 / Pour Over', 'Manual drip method known for clarity and highlighting acidity.', 'icons/v60.png'),
('French Press', 'Immersion brewing that produces a heavy body and rich texture.', 'icons/french_press.png'),
('Aeropress', 'Versatile immersion and pressure method, great for travel.', 'icons/aeropress.png'),
('Moka Pot', 'Stovetop method producing strong, espresso-like coffee.', 'icons/moka.png'),
('Cold Brew', 'Steeped for 12-24 hours in cold water for low acidity.', 'icons/cold_brew.png'),
('Chemex', 'Pour over with a thick filter, resulting in a very clean cup.', 'icons/chemex.png');

-- 4. ORIGINS
INSERT INTO origins (region, continent) VALUES 
('Ethiopia', 'Africa'),
('Colombia', 'South America'),
('Brazil', 'South America'),
('Kenya', 'Africa'),
('Sumatra', 'Asia'),
('Costa Rica', 'North America'),
('Guatemala', 'North America'),
('Vietnam', 'Asia'),
('Jamaica', 'North America'),
('Panama', 'North America');

-- 5. PRODUCTS (10 Examples)

-- Product 1: Ethiopian Yirgacheffe (Light Roast, Floral/Fruity)
INSERT INTO products (name, description, price, stock, is_blend, roast_level, acidity_score, image_url) VALUES 
('Ethiopian Yirgacheffe', 'A bright and complex light roast with distinct floral notes and a tea-like body. Perfect for pour-over enthusiasts.', 
75.00, 50, FALSE, 1, 5, 'https://cafeo.ro/4261-large_default/kimbo-intenso-cafea-boabe-1kg.jpg');

-- Product 2: Colombian Supremo (Medium Roast, Sweet/Nutty)
INSERT INTO products (name, description, price, stock, is_blend, roast_level, acidity_score, image_url) VALUES 
('Colombian Supremo', 'Classic Colombian coffee with a balanced body, notes of caramel and dried fruit. A crowd pleaser.', 
60.00, 100, FALSE, 3, 3, 'https://www.cafemagia.ro/images/produse/davidoff-cafe-espresso-57-1-kg-cafea-prajita-boabe-edit.jpg');

-- Product 3: Sumatra Mandheling (Dark Roast, Earthy)
INSERT INTO products (name, description, price, stock, is_blend, roast_level, acidity_score, image_url) VALUES 
('Sumatra Mandheling', 'Full-bodied and low acidity with rich earthy notes and a hint of spice.', 
65.00, 40, FALSE, 5, 1, 'https://lcdn.altex.ro/media/catalog/product/9/0/9000403895358_1_89b28c6f.jpg');

-- Product 4: Brazil Santos (Medium-Dark, Chocolate/Nutty)
INSERT INTO products (name, description, price, stock, is_blend, roast_level, acidity_score, image_url) VALUES 
('Brazil Santos', 'Low acidity with a smooth, nutty profile and heavy chocolate notes. Great for espresso.', 
55.00, 120, FALSE, 4, 2, 'https://deutschermarkt.ro/wp-content/uploads/2020/07/Cafea-boabe-Jacobs-Caffe-Crema-Classico-1Kg-1.jpg');

-- Product 5: Kenya AA (Light-Medium, Intense Berry)
INSERT INTO products (name, description, price, stock, is_blend, roast_level, acidity_score, image_url) VALUES 
('Kenya AA', 'Renowned for its intense fruit flavors, wine-like acidity, and savory nuances.', 
85.00, 30, FALSE, 2, 5, 'https://c.cdnmp.net/947378707/p/m/5/kimbo-barista-espresso-napoli-cafea-boabe-1kg~24725.jpg');

-- Product 6: Morning Blend (Blend, Balanced)
INSERT INTO products (name, description, price, stock, is_blend, roast_level, acidity_score, image_url) VALUES 
('Sunrise Morning Blend', 'Our signature breakfast blend. Smooth, consistent, and easy to drink.', 
50.00, 200, TRUE, 3, 3, 'https://cdn.bestvalue.eu/media/cache/sylius_shop_product_original/borbone-cafea-cafea-boabe-espresso-intenso-1000-gr-112df136f053cec320d2ff00.jpg');

-- Product 7: Espresso Crema (Blend, Strong)
INSERT INTO products (name, description, price, stock, is_blend, roast_level, acidity_score, image_url) VALUES 
('Espresso Crema', 'Designed for the perfect shot. Thick crema, heavy body, and a punchy kick.', 
58.00, 80, TRUE, 5, 2, 'https://imgproxy-retcat.assets.schwarz/7O4_IvbfKebQfgdGhZRdypQledrNr0v-mIroUBmb8ow/sm:1/w:1278/h:959/cz/M6Ly9wcm9kLWNhd/GFsb2ctbWVkaWEvcm8vMS9FQzZENDFDQTJCQkNGMTdBMERBQjMyMEY/2NjFDQTcxOUUwODU1QjUyMTU4NTY4MEY3ODhGRTEwRDIyQjU1NUZGLmpwZw.jpg');

-- Product 8: Costa Rica Tarrazu (Medium, Honey/Citrus)
INSERT INTO products (name, description, price, stock, is_blend, roast_level, acidity_score, image_url) VALUES 
('Costa Rica Tarrazu', 'Exceptionally clean with honey sweetness and a bright citrus finish.', 
70.00, 45, FALSE, 3, 4, 'https://gomagcdn.ro/domains/coffeepoint.ro/files/product/large/doncafe-espresso-intense-cafea-boabe-1-kg-439710.webp');

-- Product 9: Guatemala Antigua (Medium, Smoky/Spicy)
INSERT INTO products (name, description, price, stock, is_blend, roast_level, acidity_score, image_url) VALUES 
('Guatemala Antigua', 'Complex with a touch of smoke and spice (cocoa), balanced by a fine acidity.', 
68.00, 60, FALSE, 3, 3, 'https://noircoffee.ro/cdn/shop/files/noir-ethiopia-1_2048x.jpg?v=1695807890');

-- Product 10: Panama Geisha (Light, Rare/Floral)
INSERT INTO products (name, description, price, stock, is_blend, roast_level, acidity_score, image_url) VALUES 
('Panama Geisha Reserve', 'An award-winning varietal. Jasmine aroma, bergamot notes, and a silky mouthfeel. Very limited.', 
250.00, 10, FALSE, 1, 4, 'https://gomagcdn.ro/domains/cafea-premium.ro/files/product/medium/dallmayr-ethiopia-cafea-boabe-500g-copie-665376.webp');


-- 6. LINKING PRODUCTS TO ORIGINS (product_origins)

-- Single Origins
INSERT INTO product_origins (product_id, origin_id, percentage) VALUES
((SELECT id FROM products WHERE name = 'Ethiopian Yirgacheffe'), (SELECT id FROM origins WHERE region = 'Ethiopia'), 100),
((SELECT id FROM products WHERE name = 'Colombian Supremo'), (SELECT id FROM origins WHERE region = 'Colombia'), 100),
((SELECT id FROM products WHERE name = 'Sumatra Mandheling'), (SELECT id FROM origins WHERE region = 'Sumatra'), 100),
((SELECT id FROM products WHERE name = 'Brazil Santos'), (SELECT id FROM origins WHERE region = 'Brazil'), 100),
((SELECT id FROM products WHERE name = 'Kenya AA'), (SELECT id FROM origins WHERE region = 'Kenya'), 100),
((SELECT id FROM products WHERE name = 'Costa Rica Tarrazu'), (SELECT id FROM origins WHERE region = 'Costa Rica'), 100),
((SELECT id FROM products WHERE name = 'Guatemala Antigua'), (SELECT id FROM origins WHERE region = 'Guatemala'), 100),
((SELECT id FROM products WHERE name = 'Panama Geisha Reserve'), (SELECT id FROM origins WHERE region = 'Panama'), 100);

-- Blends
-- Morning Blend: 60% Brazil, 40% Colombia
INSERT INTO product_origins (product_id, origin_id, percentage) VALUES
((SELECT id FROM products WHERE name = 'Sunrise Morning Blend'), (SELECT id FROM origins WHERE region = 'Brazil'), 60),
((SELECT id FROM products WHERE name = 'Sunrise Morning Blend'), (SELECT id FROM origins WHERE region = 'Colombia'), 40);

-- Espresso Crema: 50% Brazil, 30% Vietnam (Robusta kick), 20% Ethiopia
INSERT INTO product_origins (product_id, origin_id, percentage) VALUES
((SELECT id FROM products WHERE name = 'Espresso Crema'), (SELECT id FROM origins WHERE region = 'Brazil'), 50),
((SELECT id FROM products WHERE name = 'Espresso Crema'), (SELECT id FROM origins WHERE region = 'Vietnam'), 30),
((SELECT id FROM products WHERE name = 'Espresso Crema'), (SELECT id FROM origins WHERE region = 'Ethiopia'), 20);


-- 7. LINKING PRODUCTS TO TASTES (product_tastes)

-- Ethiopia: Floral, Berry
INSERT INTO product_tastes (product_id, taste_id) VALUES
((SELECT id FROM products WHERE name = 'Ethiopian Yirgacheffe'), (SELECT id FROM tastes WHERE name = 'Floral')),
((SELECT id FROM products WHERE name = 'Ethiopian Yirgacheffe'), (SELECT id FROM tastes WHERE name = 'Berry'));

-- Colombia: Caramel (Sweet Aromatics), Nutty
INSERT INTO product_tastes (product_id, taste_id) VALUES
((SELECT id FROM products WHERE name = 'Colombian Supremo'), (SELECT id FROM tastes WHERE name = 'Sweet Aromatics')),
((SELECT id FROM products WHERE name = 'Colombian Supremo'), (SELECT id FROM tastes WHERE name = 'Nutty'));

-- Sumatra: Earthy (Pipe Tobacco/Spices), Dark Chocolate (Cocoa)
INSERT INTO product_tastes (product_id, taste_id) VALUES
((SELECT id FROM products WHERE name = 'Sumatra Mandheling'), (SELECT id FROM tastes WHERE name = 'Pipe Tobacco')),
((SELECT id FROM products WHERE name = 'Sumatra Mandheling'), (SELECT id FROM tastes WHERE name = 'Cocoa'));

-- Brazil: Nutty, Chocolate (Cocoa)
INSERT INTO product_tastes (product_id, taste_id) VALUES
((SELECT id FROM products WHERE name = 'Brazil Santos'), (SELECT id FROM tastes WHERE name = 'Nutty')),
((SELECT id FROM products WHERE name = 'Brazil Santos'), (SELECT id FROM tastes WHERE name = 'Cocoa'));

-- Kenya: Berry, Citrus
INSERT INTO product_tastes (product_id, taste_id) VALUES
((SELECT id FROM products WHERE name = 'Kenya AA'), (SELECT id FROM tastes WHERE name = 'Berry')),
((SELECT id FROM products WHERE name = 'Kenya AA'), (SELECT id FROM tastes WHERE name = 'Citrus Fruit'));

-- Panama: Floral, Citrus
INSERT INTO product_tastes (product_id, taste_id) VALUES
((SELECT id FROM products WHERE name = 'Panama Geisha Reserve'), (SELECT id FROM tastes WHERE name = 'Floral')),
((SELECT id FROM products WHERE name = 'Panama Geisha Reserve'), (SELECT id FROM tastes WHERE name = 'Citrus Fruit'));

-- Costa Rica: Sweet Aromatics (Honey), Citrus
INSERT INTO product_tastes (product_id, taste_id) VALUES
((SELECT id FROM products WHERE name = 'Costa Rica Tarrazu'), (SELECT id FROM tastes WHERE name = 'Sweet Aromatics')),
((SELECT id FROM products WHERE name = 'Costa Rica Tarrazu'), (SELECT id FROM tastes WHERE name = 'Citrus Fruit'));

-- Guatemala: Brown Spice (Smoky), Cocoa
INSERT INTO product_tastes (product_id, taste_id) VALUES
((SELECT id FROM products WHERE name = 'Guatemala Antigua'), (SELECT id FROM tastes WHERE name = 'Brown Spice')),
((SELECT id FROM products WHERE name = 'Guatemala Antigua'), (SELECT id FROM tastes WHERE name = 'Cocoa'));

-- Morning Blend: Sweet Aromatics, Nutty (Balanced profile)
INSERT INTO product_tastes (product_id, taste_id) VALUES
((SELECT id FROM products WHERE name = 'Sunrise Morning Blend'), (SELECT id FROM tastes WHERE name = 'Sweet Aromatics')),
((SELECT id FROM products WHERE name = 'Sunrise Morning Blend'), (SELECT id FROM tastes WHERE name = 'Nutty'));

-- Espresso Crema: Cocoa, Pipe Tobacco (Strong, punchy)
INSERT INTO product_tastes (product_id, taste_id) VALUES
((SELECT id FROM products WHERE name = 'Espresso Crema'), (SELECT id FROM tastes WHERE name = 'Cocoa')),
((SELECT id FROM products WHERE name = 'Espresso Crema'), (SELECT id FROM tastes WHERE name = 'Pipe Tobacco'));


-- 8. LINKING PRODUCTS TO BREWING METHODS (product_brewing_methods)

-- Pour Over Methods (Best for Light/Bright coffees)
INSERT INTO product_brewing_methods (product_id, brewing_method_id, is_optimal) VALUES
((SELECT id FROM products WHERE name = 'Ethiopian Yirgacheffe'), (SELECT id FROM brewing_methods WHERE name = 'V60 / Pour Over'), TRUE),
((SELECT id FROM products WHERE name = 'Kenya AA'), (SELECT id FROM brewing_methods WHERE name = 'V60 / Pour Over'), TRUE),
((SELECT id FROM products WHERE name = 'Panama Geisha Reserve'), (SELECT id FROM brewing_methods WHERE name = 'Chemex'), TRUE),
((SELECT id FROM products WHERE name = 'Costa Rica Tarrazu'), (SELECT id FROM brewing_methods WHERE name = 'V60 / Pour Over'), TRUE);

-- Espresso (Best for Medium-Dark/Full-bodied)
INSERT INTO product_brewing_methods (product_id, brewing_method_id, is_optimal) VALUES
((SELECT id FROM products WHERE name = 'Brazil Santos'), (SELECT id FROM brewing_methods WHERE name = 'Espresso'), TRUE),
((SELECT id FROM products WHERE name = 'Espresso Crema'), (SELECT id FROM brewing_methods WHERE name = 'Espresso'), TRUE);

-- French Press (Best for Medium/Full-bodied)
INSERT INTO product_brewing_methods (product_id, brewing_method_id, is_optimal) VALUES
((SELECT id FROM products WHERE name = 'Sumatra Mandheling'), (SELECT id FROM brewing_methods WHERE name = 'French Press'), TRUE),
((SELECT id FROM products WHERE name = 'Colombian Supremo'), (SELECT id FROM brewing_methods WHERE name = 'French Press'), TRUE),
((SELECT id FROM products WHERE name = 'Guatemala Antigua'), (SELECT id FROM brewing_methods WHERE name = 'French Press'), TRUE);

-- Versatile Methods (Good for Blends and everyday drinking)
INSERT INTO product_brewing_methods (product_id, brewing_method_id, is_optimal) VALUES
((SELECT id FROM products WHERE name = 'Sunrise Morning Blend'), (SELECT id FROM brewing_methods WHERE name = 'V60 / Pour Over'), TRUE),
((SELECT id FROM products WHERE name = 'Sunrise Morning Blend'), (SELECT id FROM brewing_methods WHERE name = 'Aeropress'), FALSE);


-- 9. ORDERS (Sample Customer Orders)

-- Order 1: Simple single-item purchase (John Doe)
INSERT INTO orders (order_reference, total_amount, currency, status, customer_email, customer_first_name, customer_last_name, shipping_address_line1, shipping_city, shipping_zip, shipping_country) VALUES 
('MMC-1001', 75.00, 'RON', 'PAID', 'john.doe@example.com', 'John', 'Doe', 'Str. Memorandumului 21', 'Cluj-Napoca', '400114', 'Romania');

-- Order 2: Bulk order for an office (Jane Smith)
INSERT INTO orders (order_reference, total_amount, currency, status, customer_email, customer_first_name, customer_last_name, shipping_address_line1, shipping_city, shipping_zip, shipping_country) VALUES 
('MMC-1002', 270.00, 'RON', 'SHIPPED', 'jane.smith@corporatemail.com', 'Jane', 'Smith', 'Bulevardul Unirii 10', 'Bucharest', '030000', 'Romania');

-- Order 3: Espresso lover (Bob Brown) - Still pending
INSERT INTO orders (order_reference, total_amount, currency, status, customer_email, customer_first_name, customer_last_name, shipping_address_line1, shipping_city, shipping_zip, shipping_country) VALUES 
('MMC-1003', 116.00, 'RON', 'PENDING', 'bob.brown@coffee.net', 'Bob', 'Brown', 'Calea Victoriei 45', 'Timisoara', '300000', 'Romania');

-- Order 4: The Fancy Spender (Alice Green) - Bought the Geisha
INSERT INTO orders (order_reference, total_amount, currency, status, customer_email, customer_first_name, customer_last_name, shipping_address_line1, shipping_city, shipping_zip, shipping_country) VALUES 
('MMC-1004', 335.00, 'RON', 'DELIVERED', 'alice.green@luxury.com', 'Alice', 'Green', 'Piata Sfatului 1', 'Brasov', '500000', 'Romania');

-- Order 5: Cancelled Order (Charlie White)
INSERT INTO orders (order_reference, total_amount, currency, status, customer_email, customer_first_name, customer_last_name, shipping_address_line1, shipping_city, shipping_zip, shipping_country) VALUES 
('MMC-1005', 50.00, 'RON', 'CANCELLED', 'charlie.white@oops.com', 'Charlie', 'White', 'Strada Lunga 5', 'Sibiu', '550000', 'Romania');

-- 10. ORDER ITEMS (Linking Products to Orders)

-- Order 1 Items: 1x Ethiopian Yirgacheffe
INSERT INTO order_items (order_id, product_id, quantity, price_at_purchase) VALUES 
((SELECT id FROM orders WHERE order_reference = 'MMC-1001'), (SELECT id FROM products WHERE name = 'Ethiopian Yirgacheffe'), 1, 75.00);

-- Order 2 Items: 2x Brazil Santos, 2x Morning Blend, 1x Colombia
INSERT INTO order_items (order_id, product_id, quantity, price_at_purchase) VALUES 
((SELECT id FROM orders WHERE order_reference = 'MMC-1002'), (SELECT id FROM products WHERE name = 'Brazil Santos'), 2, 55.00),
((SELECT id FROM orders WHERE order_reference = 'MMC-1002'), (SELECT id FROM products WHERE name = 'Sunrise Morning Blend'), 2, 50.00),
((SELECT id FROM orders WHERE order_reference = 'MMC-1002'), (SELECT id FROM products WHERE name = 'Colombian Supremo'), 1, 60.00);

-- Order 3 Items: 2x Espresso Crema
INSERT INTO order_items (order_id, product_id, quantity, price_at_purchase) VALUES 
((SELECT id FROM orders WHERE order_reference = 'MMC-1003'), (SELECT id FROM products WHERE name = 'Espresso Crema'), 2, 58.00);

-- Order 4 Items: 1x Panama Geisha, 1x Kenya AA
INSERT INTO order_items (order_id, product_id, quantity, price_at_purchase) VALUES 
((SELECT id FROM orders WHERE order_reference = 'MMC-1004'), (SELECT id FROM products WHERE name = 'Panama Geisha Reserve'), 1, 250.00),
((SELECT id FROM orders WHERE order_reference = 'MMC-1004'), (SELECT id FROM products WHERE name = 'Kenya AA'), 1, 85.00);

-- Order 5 Items: 1x Morning Blend
INSERT INTO order_items (order_id, product_id, quantity, price_at_purchase) VALUES 
((SELECT id FROM orders WHERE order_reference = 'MMC-1005'), (SELECT id FROM products WHERE name = 'Sunrise Morning Blend'), 1, 50.00);


-- 11. REVIEWS (Customer Product Reviews)

INSERT INTO reviews (product_id, author_name, rating, comment, is_approved) VALUES 
((SELECT id FROM products WHERE name = 'Ethiopian Yirgacheffe'), 'CoffeeLover99', 5, 'Absolutely stunning floral notes. Best poured over ice!', TRUE),
((SELECT id FROM products WHERE name = 'Sumatra Mandheling'), 'DarkRoastFan', 4, 'Very earthy and heavy. A bit too spicy for my morning cup but great after dinner.', TRUE),
((SELECT id FROM products WHERE name = 'Sunrise Morning Blend'), 'OfficeManager', 5, 'The whole team loves this. Not too bitter, not too sour. Just right.', TRUE),
((SELECT id FROM products WHERE name = 'Panama Geisha Reserve'), 'Connoisseur_X', 5, 'Expensive, but a life-changing experience. Tastes like jasmine tea.', TRUE),
((SELECT id FROM products WHERE name = 'Espresso Crema'), 'BaristaDave', 3, 'Good crema, but a bit too much Robusta bite for my taste.', FALSE); -- Pending approval


-- 12. BLOG POSTS (Coffee Education & Brewing Guides)

INSERT INTO blog_posts (title, content, author_role, is_published) VALUES 
('Mastering the V60 Pour Over', 
 '<div class="blog-content">
    <p class="lead">Brewing coffee is more than just a morning routine; it''s a ritual. If you want to elevate your cup, the pour-over method is the best place to start.</p>
    
    <h2>1. The Ratio Matters</h2>
    <p>The golden rule for pour-over coffee is the <strong>1:16 ratio</strong>. That means for every 1 gram of coffee, you use 16 grams of water. For a standard mug, try 20g of coffee to 320g of water.</p>

    <h2>2. The Bloom</h2>
    <p>Don''t just pour all the water at once! Pour just enough water to wet the grounds (about 40g) and let it sit for 30 seconds. You will see bubbles rising—this is CO2 escaping. We call this <em>the bloom</em>, and it ensures an even extraction.</p>
    <img src="https://www.caffeebazzar.ro/cache/img/produse/1-15000/1560/Espressor-Astoria-Loft-Home-Barista_1000x1000.jpg" alt="Roast Levels Illustration" />

    <h2>3. The Pour</h2>
    <p>Pour the remaining water in slow, concentric circles, avoiding the edges of the filter. Keep the water level consistent. The total brew time should be between 2:30 and 3:00 minutes.</p>
    
    <blockquote>"Good coffee is an act of patience."</blockquote>
</div>',
 'Head Barista', TRUE),

('Why We Love the French Press', 
 '<p>Want a heavier body? The French Press uses a metal mesh filter allowing oils to pass through. Grind coarse and steep for 4 minutes before plunging.</p>', 
 'Marketing', TRUE),

('Espresso vs. Moka Pot', 
 '<p>While both produce strong coffee, only an espresso machine generates the 9 bars of pressure needed for true crema. The Moka pot is a great stovetop alternative for strong coffee.</p>', 
 'Marketing', TRUE),

('The Importance of Fresh Grinding', 
 '<p>Coffee loses 60% of its aroma within 15 minutes of grinding. For the best flavor profile, always grind your beans right before you brew.</p>', 
 'Head Roaster', TRUE),

('Understanding Roast Levels', 
 '<p>Light roasts preserve the bean''s original character (floral/fruity), while dark roasts highlight the roasting process (smoky/chocolatey). Choose based on your taste preference!</p>', 
 'Marketing', TRUE);
