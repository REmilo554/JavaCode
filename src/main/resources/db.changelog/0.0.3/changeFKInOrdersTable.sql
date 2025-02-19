ALTER TABLE orders
    DROP CONSTRAINT orders_user_id_fkey;

ALTER TABLE orders
    ADD CONSTRAINT orders_user_id_fkey
        FOREIGN KEY (user_id)
            REFERENCES users (user_id)
            ON DELETE SET NULL;