-- MatchMyCoffee Database Initialization Script

-- 1. CLEANUP
DROP TABLE IF EXISTS reviews CASCADE;
DROP TABLE IF EXISTS order_items CASCADE;
DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS product_brewing_methods CASCADE;
DROP TABLE IF EXISTS product_tastes CASCADE;
DROP TABLE IF EXISTS product_origins CASCADE;
DROP TABLE IF EXISTS products CASCADE;
DROP TABLE IF EXISTS origins CASCADE;
DROP TABLE IF EXISTS brewing_methods CASCADE;
DROP TABLE IF EXISTS tastes CASCADE;
DROP TABLE IF EXISTS taste_categories CASCADE;
DROP TABLE IF EXISTS blog_posts CASCADE;

-- 2. LOOKUP TABLES
CREATE TABLE taste_categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    color_code VARCHAR(7) NOT NULL
);

CREATE TABLE tastes (
    id SERIAL PRIMARY KEY,
    category_id INTEGER NOT NULL REFERENCES taste_categories(id) ON DELETE CASCADE,
    name VARCHAR(50) NOT NULL,
    UNIQUE (category_id, name)
);

CREATE INDEX idx_tastes_category ON tastes(category_id);

CREATE TABLE brewing_methods (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    icon_url VARCHAR(255)
);

CREATE TABLE origins (
    id SERIAL PRIMARY KEY,
    region VARCHAR(100) NOT NULL UNIQUE,
    continent VARCHAR(50)
);

-- 3. CORE PRODUCTS
CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description TEXT NOT NULL,
    
    -- Inventory
    price NUMERIC(10, 2) NOT NULL CHECK (price >= 0),
    stock INTEGER NOT NULL DEFAULT 0 CHECK (stock >= 0),
    is_active BOOLEAN DEFAULT TRUE, 
    
    image_url VARCHAR(255),
    
    -- Coffee Specifics
    is_blend BOOLEAN DEFAULT FALSE,
    
    -- Using Integer 1-5 allows for easy AI comparison logic
    roast_level INTEGER NOT NULL CHECK (roast_level BETWEEN 1 AND 5), 
    acidity_score INTEGER CHECK (acidity_score BETWEEN 1 AND 5),
    
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- Junction Table: Connects Products to Origins (Handles Blends)
CREATE TABLE product_origins (
    product_id BIGINT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    origin_id INTEGER NOT NULL REFERENCES origins(id) ON DELETE CASCADE,
    
    percentage INTEGER CHECK (percentage BETWEEN 1 AND 100), 
    
    PRIMARY KEY (product_id, origin_id)
);

-- Junction Table: Connects Products to Tastes
CREATE TABLE product_tastes (
    product_id BIGINT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    taste_id INTEGER NOT NULL REFERENCES tastes(id) ON DELETE CASCADE,
    PRIMARY KEY (product_id, taste_id)
);

-- Junction Table: Connects Products to Brewing Methods
CREATE TABLE product_brewing_methods (
    product_id BIGINT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    brewing_method_id INTEGER NOT NULL REFERENCES brewing_methods(id) ON DELETE CASCADE,
    is_optimal BOOLEAN DEFAULT FALSE, 
    PRIMARY KEY (product_id, brewing_method_id)
);

-- 4. SALES & ORDERS
CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    order_reference VARCHAR(50) NOT NULL UNIQUE,
    
    total_amount NUMERIC(10, 2) NOT NULL CHECK (total_amount >= 0),
    currency VARCHAR(3) DEFAULT 'RON',
    
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING' 
        CHECK (status IN ('PENDING', 'PAID', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED')),
    
    -- Customer details
    customer_email VARCHAR(150) NOT NULL,
    customer_first_name VARCHAR(100) NOT NULL,
    customer_last_name VARCHAR(100) NOT NULL,
    
    -- Shipping Details
    shipping_address_line1 VARCHAR(255) NOT NULL,
    shipping_address_line2 VARCHAR(255),
    shipping_city VARCHAR(100) NOT NULL,
    shipping_state VARCHAR(100),
    shipping_zip VARCHAR(20) NOT NULL,
    shipping_country VARCHAR(100) NOT NULL,
    
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_orders_email ON orders(customer_email);
CREATE INDEX idx_orders_reference ON orders(order_reference);

-- Junction table: Connects Orders to Products
CREATE TABLE order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    product_id BIGINT REFERENCES products(id) ON DELETE SET NULL,
    
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    price_at_purchase NUMERIC(10, 2) NOT NULL
);

-- 5. CONTENT
CREATE TABLE reviews (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    
    author_name VARCHAR(100) NOT NULL,
    rating INTEGER NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment TEXT,
    
    is_approved BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE blog_posts (
    id SERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    author_role VARCHAR(50) DEFAULT 'Marketing',
    published_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    is_published BOOLEAN DEFAULT TRUE
);

-- 6. TRIGGERS
-- Function to update the 'updated_at' column automatically
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Apply triggers ONLY to tables with 'updated_at'
CREATE TRIGGER update_products_modtime BEFORE UPDATE ON products FOR EACH ROW EXECUTE PROCEDURE update_updated_at_column();
CREATE TRIGGER update_orders_modtime BEFORE UPDATE ON orders FOR EACH ROW EXECUTE PROCEDURE update_updated_at_column();