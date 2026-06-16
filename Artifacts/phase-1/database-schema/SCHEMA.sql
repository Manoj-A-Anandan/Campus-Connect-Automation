-- ==========================================
-- Campus Connect - Phase 1 Database Schema
-- Version: 1.0
-- Date: 2026-06-16
-- Database: PostgreSQL 14+
-- ==========================================

-- Create database
-- Note: Run this first if database doesn't exist
-- CREATE DATABASE campus_connect;

-- Connect to database
-- \c campus_connect;

-- ==========================================
-- Table 1: Users
-- ==========================================

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT check_valid_role CHECK (role IN ('ADMIN', 'FACULTY', 'STUDENT'))
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_role ON users(role);

-- ==========================================
-- Table 2: Password Resets
-- ==========================================

CREATE TABLE IF NOT EXISTS password_resets (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    token VARCHAR(255) NOT NULL UNIQUE,
    expiry_time TIMESTAMP NOT NULL,
    used BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_password_resets_user_id FOREIGN KEY (user_id) 
        REFERENCES users(id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_password_resets_token ON password_resets(token);
CREATE INDEX IF NOT EXISTS idx_password_resets_user_id ON password_resets(user_id);

-- ==========================================
-- Sample Data (Optional - for testing)
-- ==========================================

-- Insert sample student user
-- Password: TestStudent123!
-- Hashed: $2a$10$slYQmyNdGzin7olVV9YO2OPST9/PgBkqquzi.Ss8MCUgdXVjYzHce
INSERT INTO users (email, full_name, password, role, active)
VALUES (
    'student@campus.edu',
    'Alex Student',
    '$2a$10$slYQmyNdGzin7olVV9YO2OPST9/PgBkqquzi.Ss8MCUgdXVjYzHce',
    'STUDENT',
    true
) ON CONFLICT (email) DO NOTHING;

-- Insert sample faculty user
-- Password: TestFaculty123!
-- Hashed: $2a$10$YXeEhJa1n8F6dHPQKhxJe.W7ZHhCEfN8RhX2D0K7vYD8K5L8L9WFi
INSERT INTO users (email, full_name, password, role, active)
VALUES (
    'faculty@campus.edu',
    'Dr. Smith',
    '$2a$10$YXeEhJa1n8F6dHPQKhxJe.W7ZHhCEfN8RhX2D0K7vYD8K5L8L9WFi',
    'FACULTY',
    true
) ON CONFLICT (email) DO NOTHING;

-- Insert sample admin user
-- Password: TestAdmin123!
-- Hashed: $2a$10$slYQmyNdGzin7olVV9YO2OPST9/PgBkqquzi.Ss8MCUgdXVjYzHce
INSERT INTO users (email, full_name, password, role, active)
VALUES (
    'admin@campus.edu',
    'Admin User',
    '$2a$10$slYQmyNdGzin7olVV9YO2OPST9/PgBkqquzi.Ss8MCUgdXVjYzHce',
    'ADMIN',
    true
) ON CONFLICT (email) DO NOTHING;

-- ==========================================
-- Verification Queries
-- ==========================================

-- Verify users table
-- SELECT * FROM users;

-- Verify password_resets table
-- SELECT * FROM password_resets;

-- Count users
-- SELECT COUNT(*) as total_users FROM users;

-- Count by role
-- SELECT role, COUNT(*) as count FROM users GROUP BY role;

-- ==========================================
-- Useful Queries for Operations
-- ==========================================

-- Get all active users
-- SELECT id, email, full_name, role, created_at FROM users WHERE active = true ORDER BY created_at DESC;

-- Find user by email
-- SELECT * FROM users WHERE email = 'student@campus.edu';

-- Check if email exists
-- SELECT EXISTS(SELECT 1 FROM users WHERE email = 'student@campus.edu');

-- Get password reset tokens for a user
-- SELECT * FROM password_resets WHERE user_id = 1 AND used = false AND expiry_time > NOW();

-- Update user role
-- UPDATE users SET role = 'FACULTY', updated_at = NOW() WHERE email = 'student@campus.edu';

-- Deactivate user
-- UPDATE users SET active = false, updated_at = NOW() WHERE email = 'student@campus.edu';

-- Delete unused expired tokens
-- DELETE FROM password_resets WHERE used = false AND expiry_time < NOW();

-- Get database size
-- SELECT pg_size_pretty(pg_database_size('campus_connect'));

-- Get table sizes
-- SELECT schemaname, tablename, pg_size_pretty(pg_total_relation_size(schemaname||'.'||tablename)) FROM pg_tables WHERE schemaname NOT IN ('pg_catalog', 'information_schema') ORDER BY pg_total_relation_size(schemaname||'.'||tablename) DESC;

-- ==========================================
-- Cleanup (Optional - use with caution)
-- ==========================================

-- Delete all users (DELETE THIS DATA)
-- DELETE FROM users WHERE id > 0;

-- Delete all password reset tokens
-- DELETE FROM password_resets WHERE id > 0;

-- Drop tables (Careful!)
-- DROP TABLE IF EXISTS password_resets CASCADE;
-- DROP TABLE IF EXISTS users CASCADE;

-- Drop database (Extremely dangerous!)
-- DROP DATABASE IF EXISTS campus_connect;
