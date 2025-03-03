ALTER TABLE users
    ADD COLUMN IF NOT EXISTS failed_login_attempts BIGINT NOT NULL default 0;