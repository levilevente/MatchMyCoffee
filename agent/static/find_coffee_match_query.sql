SELECT DISTINCT products.id, products.name, products.description, products.price, products.roast_level, products.acidity_score, bm.name AS brewing_method, pbm.is_optimal AS brewing_method_is_optimal, STRING_AGG(DISTINCT tc.name || ' - ' || t.name, ', ') AS tastes, products.is_blend, STRING_AGG(DISTINCT o.region || ' - ' || o.continent || ' ' || po.percentage, ' + ') AS blend FROM products
JOIN product_brewing_methods pbm ON products.id = pbm.product_id
JOIN brewing_methods bm ON pbm.brewing_method_id = bm.id
JOIN product_tastes pt ON products.id = pt.product_id
JOIN tastes t ON pt.taste_id = t.id
JOIN taste_categories tc ON t.category_id = tc.id
JOIN product_origins po ON products.id = po.product_id
JOIN origins o ON po.origin_id = o.id
WHERE bm.name LIKE %s AND
        products.roast_level BETWEEN %s AND %s AND
        products.acidity_score BETWEEN %s AND %s AND
        tc.name = ANY(%s) AND
        products.is_blend = %s AND
        products.stock > 0 AND
        products.is_active = TRUE
GROUP BY products.id, products.name, products.description, products.price, products.roast_level, products.acidity_score, bm.name, pbm.is_optimal
LIMIT 5;