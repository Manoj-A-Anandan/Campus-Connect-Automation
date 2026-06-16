# Phase 1: Database Schema & Data Models

**Version**: 1.0  
**Date**: June 16, 2026  
**Database**: PostgreSQL 14+  

---

## 📊 Database Overview

**Database Name**: `campus_connect`  
**Tables**: 2  
**Primary Keys**: 2  
**Foreign Keys**: 1  
**Indexes**: 2  
**Constraints**: Multiple

---

## 📋 Table 1: Users

### Purpose
Stores all user accounts (Students, Faculty, Admins) with authentication credentials.

### SQL Definition

```sql
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('ADMIN', 'FACULTY', 'STUDENT')),
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create index on email for faster lookups
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);
```

### Column Details

| Column | Type | Constraints | Description |
|--------|------|-----------|-------------|
| id | SERIAL | PRIMARY KEY | Unique user identifier |
| email | VARCHAR(255) | NOT NULL, UNIQUE | User email (login credential) |
| password | VARCHAR(255) | NOT NULL | BCrypt hashed password |
| full_name | VARCHAR(255) | NOT NULL | User's full name |
| role | VARCHAR(20) | NOT NULL, CHECK | User role: ADMIN, FACULTY, STUDENT |
| active | BOOLEAN | DEFAULT true | Account status (soft delete) |
| created_at | TIMESTAMP | DEFAULT NOW() | Account creation timestamp |
| updated_at | TIMESTAMP | DEFAULT NOW() | Last update timestamp |

### Indexes

```sql
-- Email lookup optimization
CREATE INDEX idx_users_email ON users(email);

-- Role-based queries
CREATE INDEX idx_users_role ON users(role);
```

### Example Data

```sql
INSERT INTO users (email, full_name, password, role, active)
VALUES (
    'student@campus.edu',
    'Alex Student',
    '$2a$10$slYQmyNdGzin7olVV9YO2OPST9/PgBkqquzi.Ss8MCUgdXVjYzHce',
    'STUDENT',
    true
);

INSERT INTO users (email, full_name, password, role, active)
VALUES (
    'faculty@campus.edu',
    'Dr. Smith',
    '$2a$10$YXeEhJa1n8F6dHPQKhxJe.W7ZHhCEfN8RhX2D0K7vYD8K5L8L9WFi',
    'FACULTY',
    true
);

INSERT INTO users (email, full_name, password, role, active)
VALUES (
    'admin@campus.edu',
    'Admin User',
    '$2a$10$slYQmyNdGzin7olVV9YO2OPST9/PgBkqquzi.Ss8MCUgdXVjYzHce',
    'ADMIN',
    true
);
```

### Queries

**Find User by Email**:
```sql
SELECT * FROM users WHERE email = 'student@campus.edu';
```

**Check Email Uniqueness**:
```sql
SELECT COUNT(*) FROM users WHERE email = 'student@campus.edu';
```

**Get All Active Users**:
```sql
SELECT * FROM users WHERE active = true ORDER BY created_at DESC;
```

**Count Users by Role**:
```sql
SELECT role, COUNT(*) as count FROM users GROUP BY role;
```

**Get Recent Users**:
```sql
SELECT * FROM users ORDER BY created_at DESC LIMIT 10;
```

**Search Users**:
```sql
SELECT * FROM users 
WHERE full_name ILIKE '%Alex%' 
   OR email ILIKE '%campus%' 
ORDER BY created_at DESC;
```

---

## 📋 Table 2: Password Resets

### Purpose
Stores password reset tokens with expiry information for secure password recovery.

### SQL Definition

```sql
CREATE TABLE password_resets (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    token VARCHAR(255) NOT NULL UNIQUE,
    expiry_time TIMESTAMP NOT NULL,
    used BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create index for token lookup
CREATE INDEX idx_password_resets_token ON password_resets(token);
CREATE INDEX idx_password_resets_user_id ON password_resets(user_id);
```

### Column Details

| Column | Type | Constraints | Description |
|--------|------|-----------|-------------|
| id | SERIAL | PRIMARY KEY | Token record identifier |
| user_id | INTEGER | NOT NULL, FK | References users(id) |
| token | VARCHAR(255) | NOT NULL, UNIQUE | UUID reset token |
| expiry_time | TIMESTAMP | NOT NULL | Token expiration time |
| used | BOOLEAN | DEFAULT false | One-time use flag |
| created_at | TIMESTAMP | DEFAULT NOW() | Token creation timestamp |

### Indexes

```sql
-- Token lookup optimization
CREATE INDEX idx_password_resets_token ON password_resets(token);

-- User lookup optimization
CREATE INDEX idx_password_resets_user_id ON password_resets(user_id);
```

### Example Data

```sql
INSERT INTO password_resets (user_id, token, expiry_time, used)
VALUES (
    1,
    'f47ac10b-58cc-4372-a567-0e02b2c3d479',
    NOW() + INTERVAL '30 minutes',
    false
);

-- Used token
INSERT INTO password_resets (user_id, token, expiry_time, used)
VALUES (
    2,
    'a1b2c3d4-e5f6-7890-abcd-ef1234567890',
    NOW() - INTERVAL '1 hour',
    true
);
```

### Queries

**Find Token by Value**:
```sql
SELECT * FROM password_resets WHERE token = 'f47ac10b-58cc-4372-a567-0e02b2c3d479';
```

**Check Token Validity**:
```sql
SELECT * FROM password_resets 
WHERE token = 'f47ac10b-58cc-4372-a567-0e02b2c3d479'
  AND used = false
  AND expiry_time > NOW();
```

**Get Unused Tokens for User**:
```sql
SELECT * FROM password_resets 
WHERE user_id = 1 
  AND used = false
  AND expiry_time > NOW();
```

**Get Active Tokens**:
```sql
SELECT pr.*, u.email 
FROM password_resets pr
JOIN users u ON pr.user_id = u.id
WHERE pr.used = false 
  AND pr.expiry_time > NOW()
ORDER BY pr.created_at DESC;
```

**Clean Up Expired Tokens**:
```sql
DELETE FROM password_resets WHERE expiry_time < NOW();
```

**Mark Token as Used**:
```sql
UPDATE password_resets SET used = true WHERE id = 1;
```

---

## 🔗 Relationships

### Foreign Key: password_resets → users

```sql
ALTER TABLE password_resets
ADD CONSTRAINT fk_password_resets_user_id
FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;
```

**Behavior**:
- When a user is deleted, associated reset tokens are automatically deleted (CASCADE)
- Prevents orphaned records
- Maintains referential integrity

---

## 📊 Entity Relationship Diagram

```
┌─────────────────────┐
│       USERS         │
├─────────────────────┤
│ id (PK)             │
│ email (UNIQUE)      │
│ password            │
│ full_name           │
│ role                │
│ active              │
│ created_at          │
│ updated_at          │
└────────┬────────────┘
         │
         │ 1:N
         │ (user has many reset tokens)
         │
┌────────▼────────────────────┐
│  PASSWORD_RESETS            │
├─────────────────────────────┤
│ id (PK)                     │
│ user_id (FK → users.id)     │
│ token (UNIQUE)              │
│ expiry_time                 │
│ used                        │
│ created_at                  │
└─────────────────────────────┘
```

---

## 🔐 Data Security

### Password Hashing

Passwords are hashed using **BCrypt** with default cost factor:
- **Algorithm**: BCrypt with SHA-512
- **Cost Factor**: 10 (default)
- **Hash Length**: 60 characters
- **Example**: `$2a$10$slYQmyNdGzin7olVV9YO2OPST9/PgBkqquzi.Ss8MCUgdXVjYzHce`

**Format**: `$2a$cost$salt(22chars)hash(31chars)`

### Token Generation

Reset tokens are generated as **UUID v4**:
- **Format**: `f47ac10b-58cc-4372-a567-0e02b2c3d479`
- **Generation**: Java UUID.randomUUID()
- **Length**: 36 characters (with hyphens)
- **Uniqueness**: Guaranteed

---

## 📈 Database Growth Projection

### Year 1 Estimates

| Table | Est. Records | Storage |
|-------|--------------|---------|
| users | 10,000 | ~5 MB |
| password_resets | 50,000* | ~10 MB |
| Total | | ~15 MB |

*Assumes 5 reset attempts per user per year

### Growth Capacity

Current schema supports:
- **Users**: Up to 2 billion (SERIAL limit)
- **Storage**: Essentially unlimited with PostgreSQL
- **Performance**: Indexed lookups < 100ms with proper maintenance

---

## 🔧 Database Maintenance

### Backup

```bash
# Full database backup
pg_dump -h localhost -U postgres -d campus_connect > backup.sql

# Compressed backup
pg_dump -h localhost -U postgres -d campus_connect | gzip > backup.sql.gz
```

### Restore

```bash
# From SQL file
psql -h localhost -U postgres -d campus_connect < backup.sql

# From compressed file
gunzip < backup.sql.gz | psql -h localhost -U postgres -d campus_connect
```

### Vacuum & Analyze

```sql
-- Clean up space
VACUUM ANALYZE;

-- For specific table
VACUUM ANALYZE users;
VACUUM ANALYZE password_resets;
```

### Reindex

```sql
-- Rebuild all indexes
REINDEX DATABASE campus_connect;

-- Rebuild specific table indexes
REINDEX TABLE users;
REINDEX TABLE password_resets;
```

---

## 📊 Statistics & Monitoring

### Table Statistics

```sql
-- User count by role
SELECT role, COUNT(*) as count 
FROM users 
GROUP BY role;

-- Active vs inactive users
SELECT 
  active, 
  COUNT(*) as count 
FROM users 
GROUP BY active;

-- Most recent users
SELECT id, email, full_name, created_at
FROM users
ORDER BY created_at DESC
LIMIT 10;
```

### Password Reset Statistics

```sql
-- Valid (unused) tokens
SELECT COUNT(*) FROM password_resets 
WHERE used = false AND expiry_time > NOW();

-- Expired tokens
SELECT COUNT(*) FROM password_resets 
WHERE expiry_time < NOW();

-- Tokens by user
SELECT user_id, COUNT(*) as token_count
FROM password_resets
GROUP BY user_id
ORDER BY token_count DESC;
```

### Database Size

```sql
-- Database size
SELECT pg_size_pretty(pg_database_size('campus_connect'));

-- Table sizes
SELECT 
  schemaname,
  tablename,
  pg_size_pretty(pg_total_relation_size(schemaname||'.'||tablename))
FROM pg_tables
WHERE schemaname NOT IN ('pg_catalog', 'information_schema')
ORDER BY pg_total_relation_size(schemaname||'.'||tablename) DESC;
```

---

## 🚨 Constraints & Validation

### Database-Level Constraints

```sql
-- Role validation
ALTER TABLE users 
ADD CONSTRAINT check_valid_role 
CHECK (role IN ('ADMIN', 'FACULTY', 'STUDENT'));

-- Email not null and not empty
ALTER TABLE users 
ADD CONSTRAINT check_email_not_empty 
CHECK (email != '');

-- Password not empty
ALTER TABLE users 
ADD CONSTRAINT check_password_not_empty 
CHECK (password != '');
```

---

## 🔄 Data Migration Examples

### Add New Column

```sql
-- Add phone column
ALTER TABLE users ADD COLUMN phone VARCHAR(20);

-- Set default value
ALTER TABLE users ALTER COLUMN phone SET DEFAULT '';

-- Make non-nullable
ALTER TABLE users ALTER COLUMN phone SET NOT NULL;
```

### Add New Role

```sql
-- Update role constraint
ALTER TABLE users DROP CONSTRAINT check_valid_role;

ALTER TABLE users 
ADD CONSTRAINT check_valid_role 
CHECK (role IN ('ADMIN', 'FACULTY', 'STUDENT', 'GUEST'));
```

### Archive Old Data

```sql
-- Create archive table
CREATE TABLE users_archive AS 
SELECT * FROM users 
WHERE created_at < NOW() - INTERVAL '1 year';

-- Delete archived data
DELETE FROM users 
WHERE id IN (SELECT id FROM users_archive);
```

---

## 📝 Schema Version History

| Version | Date | Changes |
|---------|------|---------|
| 1.0 | 2026-06-16 | Initial schema with users and password_resets tables |
| - | - | - |

---

## 🎯 Future Schema Additions (Phase 2+)

```sql
-- Students table (Phase 2)
CREATE TABLE students (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id),
    roll_number VARCHAR(50) UNIQUE,
    enrollment_date DATE,
    created_at TIMESTAMP DEFAULT NOW()
);

-- Courses table (Phase 2)
CREATE TABLE courses (
    id SERIAL PRIMARY KEY,
    code VARCHAR(20) UNIQUE,
    name VARCHAR(255),
    credits INTEGER,
    faculty_id INTEGER REFERENCES users(id),
    created_at TIMESTAMP DEFAULT NOW()
);

-- Attendance table (Phase 2)
CREATE TABLE attendance (
    id SERIAL PRIMARY KEY,
    student_id INTEGER REFERENCES students(id),
    course_id INTEGER REFERENCES courses(id),
    attendance_date DATE,
    status VARCHAR(20),
    created_at TIMESTAMP DEFAULT NOW()
);
```

---

**Database Schema Version**: 1.0  
**Last Updated**: June 16, 2026
