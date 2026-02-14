-- PostgreSQL DDL for Invoice Generator Application
-- Drop tables if they exist (in correct order due to foreign key constraints)
DROP TABLE IF EXISTS invoice_items CASCADE;
DROP TABLE IF EXISTS invoices CASCADE;
DROP TABLE IF EXISTS addresses CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS clients CASCADE;
DROP SEQUENCE IF EXISTS user_id_seq CASCADE;
DROP SEQUENCE IF EXISTS client_id_seq CASCADE;
DROP SEQUENCE IF EXISTS invoice_id_seq CASCADE;
DROP SEQUENCE IF EXISTS invoice_item_id_seq CASCADE;
DROP SEQUENCE IF EXISTS address_id_seq CASCADE;


CREATE SEQUENCE user_id_seq START WITH 1000 INCREMENT BY 1;
CREATE SEQUENCE client_id_seq START WITH 2000 INCREMENT BY 1;
CREATE SEQUENCE invoice_id_seq START WITH 3000 INCREMENT BY 1;
CREATE SEQUENCE invoice_item_id_seq START WITH 7000 INCREMENT BY 1;
CREATE SEQUENCE address_id_seq START WITH 5000 INCREMENT BY 1;

-- Create invoices table
CREATE TABLE invoices (
     id BIGINT PRIMARY KEY DEFAULT nextval('invoice_id_seq'),
    invoice_number VARCHAR(255),
    invoice_date DATE,
    from_name VARCHAR(255),
    from_address TEXT,
    from_gst VARCHAR(50),
    to_name VARCHAR(255),
    to_address TEXT,
    to_gst VARCHAR(50),
    state_code VARCHAR(50),
    vehicle_number VARCHAR(50),
    total_value DOUBLE PRECISION,
    total_cgst DOUBLE PRECISION,
    total_sgst DOUBLE PRECISION,
    grand_total DOUBLE PRECISION
);

-- Create invoice_items table
CREATE TABLE invoice_items (
     id BIGINT PRIMARY KEY DEFAULT nextval('invoice_item_id_seq'),
    invoice_id BIGINT NOT NULL,
    serial_no INTEGER,
    description VARCHAR(500),
    hsn_code VARCHAR(50),
    quantity DOUBLE PRECISION,
    rate_per_kg DOUBLE PRECISION,
    total_value DOUBLE PRECISION,
    cgst_percent DOUBLE PRECISION,
    cgst_amount DOUBLE PRECISION,
    sgst_percent DOUBLE PRECISION,
    sgst_amount DOUBLE PRECISION,
    CONSTRAINT fk_invoice FOREIGN KEY (invoice_id) REFERENCES invoices(id) ON DELETE CASCADE
);

-- Create users table
CREATE TABLE users (
     id BIGINT PRIMARY KEY DEFAULT nextval('user_id_seq'),
    username VARCHAR(255) NOT NULL UNIQUE,
    email_id VARCHAR(255),
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    gstin VARCHAR(50) UNIQUE,
    user_type VARCHAR(50) NOT NULL DEFAULT 'user',
    fail_count INTEGER NOT NULL DEFAULT 0,
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    insert_tms TIMESTAMP NOT NULL,
    updated_tms TIMESTAMP
);

-- Create clients table
CREATE TABLE clients (
     id BIGINT PRIMARY KEY DEFAULT nextval('client_id_seq'),
    client_name VARCHAR(255) NOT NULL,
    email_id VARCHAR(255),
    phone_number VARCHAR(50),
    mobile_number VARCHAR(50),
    gstin VARCHAR(50) UNIQUE,
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    insert_tms TIMESTAMP NOT NULL,
    updated_tms TIMESTAMP
);

-- Create addresses table
CREATE TABLE addresses (
    id BIGINT PRIMARY KEY DEFAULT nextval('address_id_seq'),
    address_type VARCHAR(50) NOT NULL DEFAULT 'home',
    entity_type VARCHAR(50) NOT NULL,
    entity_id BIGINT NOT NULL,
    house_no VARCHAR(100),
    address1 VARCHAR(500),
    address2 VARCHAR(500),
    city VARCHAR(100),
    state VARCHAR(100),
    country VARCHAR(100),
    pincode VARCHAR(20),
    created_by BIGINT,
    updated_by BIGINT,
    created_tms TIMESTAMP NOT NULL,
    updated_tms TIMESTAMP
);

-- Create indexes for better query performance
CREATE INDEX idx_invoice_number ON invoices(invoice_number);
CREATE INDEX idx_invoice_date ON invoices(invoice_date);
CREATE INDEX idx_invoice_items_invoice_id ON invoice_items(invoice_id);
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_deleted ON users(is_deleted);
CREATE INDEX idx_clients_name ON clients(client_name);
CREATE INDEX idx_clients_deleted ON clients(is_deleted);
CREATE INDEX idx_addresses_entity ON addresses(entity_type, entity_id);
CREATE INDEX idx_addresses_created_by ON addresses(created_by);
CREATE INDEX idx_addresses_updated_by ON addresses(updated_by);
