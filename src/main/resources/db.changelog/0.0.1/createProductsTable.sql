CREATE TABLE IF NOT EXISTS products (
                          product_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          price DECIMAL(19, 2) NOT NULL,
                          amount INTEGER NOT NULL
);