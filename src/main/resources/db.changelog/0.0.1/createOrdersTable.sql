CREATE TABLE IF NOT EXISTS orders (
                        order_id  UUID DEFAULT gen_random_uuid() PRIMARY KEY,
                        order_price DECIMAL(19, 2) NOT NULL,
                        status VARCHAR(255) NOT NULL,
                        user_id UUID NOT NULL,
                        FOREIGN KEY (user_id) REFERENCES users(user_id)
);