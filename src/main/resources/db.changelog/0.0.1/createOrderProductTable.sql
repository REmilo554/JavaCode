CREATE TABLE IF NOT EXISTS order_product (
                               order_product_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
                               order_id UUID NOT NULL,
                               product_id UUID NOT NULL,
                               quantity INTEGER NOT NULL,
                               FOREIGN KEY (order_id) REFERENCES orders(order_id),
                               FOREIGN KEY (product_id) REFERENCES products(product_id)
);