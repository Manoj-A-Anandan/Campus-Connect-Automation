# Phase 1: Setup & Execution Guide

**Version**: 1.0  
**Date**: June 16, 2026  

---

## 🚀 Quick Start (5 Minutes)

### Prerequisites
- Docker & Docker Compose installed, OR
- Java 17+, Maven 3.8+, Node.js 18+, PostgreSQL 14+

### Step 1: Run with Docker Compose

```bash
cd "D:\Projects\Campus Connect"
docker-compose up -d
```

### Step 2: Access the Application

- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8080/api/v1
- **API Documentation**: http://localhost:8080/api/swagger-ui.html
- **Database**: localhost:5432 (postgres:postgres)

### Step 3: Test Registration

1. Go to http://localhost:3000
2. Click "Don't have an account? Register"
3. Fill the form:
   - Email: `test@campus.edu`
   - Full Name: `Test User`
   - Password: `SecurePass123!`
   - Confirm: `SecurePass123!`
   - Role: `Student`
4. Click Register
5. You'll be redirected to Dashboard

### Step 4: Test Login

1. Click Logout
2. Go back to http://localhost:3000
3. Enter credentials:
   - Email: `test@campus.edu`
   - Password: `SecurePass123!`
4. Click Login
5. You'll see the Dashboard with your user info

---

## 📦 Local Development Setup

### Backend Setup

#### 1. Prerequisites
```bash
java -version        # Should be 17 or higher
mvn -version        # Should be 3.8 or higher
```

#### 2. PostgreSQL Setup (Windows)

**Option A: Using pgAdmin**
```bash
# Create database
CREATE DATABASE campus_connect;

# Create user (if not exists)
CREATE USER postgres WITH PASSWORD 'postgres';
GRANT ALL PRIVILEGES ON DATABASE campus_connect TO postgres;
```

**Option B: Using Command Line**
```bash
psql -U postgres -c "CREATE DATABASE campus_connect;"
```

#### 3. Backend Configuration

Edit `backend/src/main/resources/application.properties`:

```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/campus_connect
spring.datasource.username=postgres
spring.datasource.password=postgres

# JWT
jwt.secret=campus-connect-secret-key-very-secure-and-long-for-hs512-algorithm-2024
jwt.expiration=86400000

# Server
server.port=8080
```

#### 4. Build Backend

```bash
cd backend

# Install dependencies and build
mvn clean install

# Run the application
mvn spring-boot:run
```

**Expected Output**:
```
. ____ _ __ _ _
/\\ / ___'_ __ _ _(_)_ __ __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
\\/ ___)| |_)| | | | | || (_| | ) ) ) )
' |____| .__|_| |_|_| |_\__, | / / / /
=========|_|==============|___/=/_/_/_/
[main] o.s.boot.StartupInfoLogger : Started CampusConnectBackendApplication
```

**Backend is ready**: http://localhost:8080/api/v1

---

### Frontend Setup

#### 1. Prerequisites
```bash
node -v    # Should be 18 or higher
npm -v     # Should be 8 or higher
```

#### 2. Install Dependencies

```bash
cd frontend

npm install
```

#### 3. Configuration (Optional)

Edit `frontend/vite.config.js` if backend is on different port:

```javascript
server: {
  port: 3000,
  proxy: {
    '/api': {
      target: 'http://localhost:8080/api',  // Change this if needed
      changeOrigin: true,
      rewrite: (path) => path.replace(/^\/api/, '')
    }
  }
}
```

#### 4. Start Development Server

```bash
npm run dev
```

**Expected Output**:
```
VITE v5.0.0 ready in 2346 ms

➜ Local: http://localhost:3000/
➜ Network: http://192.168.x.x:3000/
```

**Frontend is ready**: http://localhost:3000

---

## 🐳 Docker Compose Setup (Recommended)

### 1. Prerequisites
```bash
docker --version      # Should be 20.10 or higher
docker-compose --version  # Should be 1.29 or higher
```

### 2. Run All Services

```bash
cd "D:\Projects\Campus Connect"

# Start all services (PostgreSQL, Backend, Frontend)
docker-compose up -d

# View logs
docker-compose logs -f backend

# View frontend logs
docker-compose logs -f frontend

# View database logs
docker-compose logs -f postgres
```

### 3. Verify Services

```bash
docker-compose ps

# Expected output:
# NAME                    STATUS              PORTS
# campus_connect_db       Up 2 minutes        5432
# campus_connect_backend  Up 1 minute         8080
# campus_connect_frontend Up 30 seconds       3000
```

### 4. Stop Services

```bash
docker-compose down

# Remove volumes (delete database)
docker-compose down -v
```

### 5. Rebuild Images

```bash
docker-compose build

# Build specific service
docker-compose build backend
docker-compose build frontend
```

---

## 🧪 Manual Testing

### Test 1: API Health Check

```bash
curl http://localhost:8080/api/v1/auth/health
```

**Expected Response**:
```json
{
  "success": true,
  "message": "API is running"
}
```

### Test 2: User Registration (cURL)

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@campus.edu",
    "fullName": "John Doe",
    "password": "SecurePass123!",
    "confirmPassword": "SecurePass123!",
    "role": "STUDENT"
  }'
```

**Expected Response** (201 Created):
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "token": "eyJhbGciOiJIUzUxMiJ9...",
    "email": "john@campus.edu",
    "fullName": "John Doe",
    "role": "STUDENT",
    "userId": 1
  }
}
```

### Test 3: User Login (cURL)

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@campus.edu",
    "password": "SecurePass123!"
  }'
```

**Expected Response** (200 OK):
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzUxMiJ9...",
    "email": "john@campus.edu",
    "fullName": "John Doe",
    "role": "STUDENT",
    "userId": 1
  }
}
```

### Test 4: Invalid Login

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@campus.edu",
    "password": "WrongPassword123!"
  }'
```

**Expected Response** (400 Bad Request):
```json
{
  "success": false,
  "message": "Invalid email or password"
}
```

---

## 📊 Database Verification

### Connect to PostgreSQL

```bash
# Using psql
psql -h localhost -U postgres -d campus_connect

# View users table
SELECT * FROM users;

# View password resets table
SELECT * FROM password_resets;

# Check table structure
\d users
\d password_resets
```

### Sample Data

```sql
-- Insert test user (password will be hashed)
INSERT INTO users (email, password, full_name, role, active)
VALUES ('test@campus.edu', '$2a$10$...', 'Test User', 'STUDENT', true);

-- View all users
SELECT id, email, full_name, role, active, created_at FROM users;
```

---

## 🔐 Password Requirements for Testing

When testing, ensure passwords meet these requirements:
- **Minimum 8 characters**
- **At least one uppercase letter** (A-Z)
- **At least one lowercase letter** (a-z)
- **At least one digit** (0-9)
- **At least one special character** (!@#$%^&* etc.)

**Valid Examples**:
- `SecurePass123!`
- `Test@Pass2024`
- `MyPassword#2024`
- `Admin@12345!`

**Invalid Examples**:
- `password123` (no uppercase, no special char)
- `Pass123` (7 chars, too short)
- `PASSWORD123!` (no lowercase)

---

## 📝 Common Operations

### Add New User via psql

```bash
psql -U postgres -d campus_connect

INSERT INTO users (email, password, full_name, role, active, created_at, updated_at)
VALUES (
  'faculty@campus.edu',
  '$2a$10$slYQmyNdGzin7olVV9YO2OPST9/PgBkqquzi.Ss8MCUgdXVjYzHce', -- hashed password
  'Faculty Member',
  'FACULTY',
  true,
  NOW(),
  NOW()
);
```

### List All Users

```bash
SELECT 
  id, 
  email, 
  full_name, 
  role, 
  active, 
  created_at 
FROM users 
ORDER BY created_at DESC;
```

### Delete Test User

```bash
DELETE FROM users WHERE email = 'test@campus.edu';
```

### Reset Password (Admin Operation)

```bash
-- Generate new hashed password: SecurePass123!
UPDATE users 
SET password = '$2a$10$YXeEhJa1n8F6dHPQKhxJe.W7ZHhCEfN8RhX2D0K7vYD8K5L8L9WFi',
    updated_at = NOW()
WHERE email = 'john@campus.edu';
```

---

## 🔧 Troubleshooting

### Issue: Port 8080 Already in Use

```bash
# Find process using port 8080
netstat -ano | findstr :8080

# Kill process (Windows)
taskkill /PID {PID} /F

# Or change port in application.properties
server.port=8081
```

### Issue: Port 3000 Already in Use

```bash
# On Windows
netstat -ano | findstr :3000
taskkill /PID {PID} /F

# On macOS/Linux
lsof -i :3000
kill -9 {PID}
```

### Issue: PostgreSQL Connection Error

```bash
# Check if PostgreSQL is running
pg_isready -h localhost -p 5432

# Verify credentials
psql -h localhost -U postgres -d campus_connect

# Check database exists
psql -U postgres -l | grep campus_connect
```

### Issue: Frontend Can't Connect to Backend

1. Verify backend is running: `curl http://localhost:8080/api/v1/auth/health`
2. Check CORS configuration in `SecurityConfig.java`
3. Verify proxy in `vite.config.js`
4. Check browser console for errors (F12)

### Issue: JWT Token Expired

- Tokens are valid for 24 hours
- To test expiry, change `jwt.expiration=60000` (1 minute) in `application.properties`
- Login again to get new token

---

## 📋 Pre-Deployment Checklist

- [ ] Java 17+ installed
- [ ] PostgreSQL 14+ running
- [ ] Maven 3.8+ installed
- [ ] Node.js 18+ installed
- [ ] Backend builds successfully (`mvn clean install`)
- [ ] Frontend dependencies installed (`npm install`)
- [ ] Database created (`campus_connect`)
- [ ] Backend starts without errors (`mvn spring-boot:run`)
- [ ] Frontend starts without errors (`npm run dev`)
- [ ] Can register new user
- [ ] Can login with credentials
- [ ] Can see dashboard after login
- [ ] Can logout successfully
- [ ] Swagger API docs accessible

---

## 🚀 Deployment Commands Summary

```bash
# Docker Compose (All in One)
cd "D:\Projects\Campus Connect"
docker-compose up -d

# Local - Terminal 1 (Backend)
cd backend
mvn spring-boot:run

# Local - Terminal 2 (Frontend)
cd frontend
npm run dev

# Local - Terminal 3 (Database)
psql -U postgres -d campus_connect
```

---

**Setup Guide Version**: 1.0  
**Last Updated**: June 16, 2026
