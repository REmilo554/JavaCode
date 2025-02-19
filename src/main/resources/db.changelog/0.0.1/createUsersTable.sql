CREATE TABLE IF NOT EXISTS users (
                       user_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
                       full_name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL
);