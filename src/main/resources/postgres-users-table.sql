-- Add users table to existing schema

-- Create sequence starting at 100
CREATE SEQUENCE id_seq START WITH 100 INCREMENT BY 1;

CREATE TABLE users (
    id BIGINT PRIMARY KEY DEFAULT nextval('id_seq'),
    username VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    fail_count INTEGER NOT NULL DEFAULT 0,
    insert_tms TIMESTAMP NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    updated_tms TIMESTAMP
);

-- Create index for username lookups
CREATE INDEX idx_users_username ON users(username);
