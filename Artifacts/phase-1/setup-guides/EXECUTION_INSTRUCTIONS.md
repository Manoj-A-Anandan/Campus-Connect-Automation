# Phase 1: Execution Instructions

**Version**: 1.0  
**Date**: June 16, 2026  

---

## 🎯 Quick Reference

**Start Everything** (Docker):
```bash
cd "D:\Projects\Campus Connect"
docker-compose up -d
```

**Access Points**:
- Frontend: http://localhost:3000
- Backend: http://localhost:8080/api/v1
- API Docs: http://localhost:8080/api/swagger-ui.html
- Database: localhost:5432 (postgres:postgres)

---

## 🚀 Step-by-Step Execution

### Step 1: Environment Preparation

**Check Java Installation**:
```bash
java -version
# Expected: openjdk version "17" or higher
```

**Check Maven Installation**:
```bash
mvn -version
# Expected: Apache Maven 3.8 or higher
```

**Check Node.js Installation**:
```bash
node -v
# Expected: v18 or higher
npm -v
# Expected: 8 or higher
```

**Check PostgreSQL** (if local):
```bash
psql --version
# Expected: psql (PostgreSQL) 14 or higher
```

---

### Step 2: Database Setup

**Option A: Using Docker Compose** (Automatic)
```bash
# Docker Compose handles database creation
docker-compose up -d postgres
# Wait 30 seconds for database to be ready
```

**Option B: Manual PostgreSQL Setup**
```bash
# Connect to PostgreSQL
psql -U postgres

# Create database
CREATE DATABASE campus_connect;

# Create user with password
CREATE USER campususer WITH PASSWORD 'campuspass';

# Grant privileges
GRANT ALL PRIVILEGES ON DATABASE campus_connect TO campususer;

# Exit
\q
```

**Verify Database Creation**:
```bash
psql -U postgres -d campus_connect -c "SELECT 1;"
# Should return: 1
```

---

### Step 3: Backend Setup & Execution

**Navigate to Backend Directory**:
```bash
cd "D:\Projects\Campus Connect\backend"
```

**Build Backend**:
```bash
mvn clean install
```

**Expected Output**:
```
[INFO] BUILD SUCCESS
[INFO] Total time: 45s
```

**Run Backend**:
```bash
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
[main] Started CampusConnectBackendApplication in 8.5 seconds
```

**Backend Ready on**: http://localhost:8080/api/v1

**Test Backend**:
```bash
curl http://localhost:8080/api/v1/auth/health

# Expected Response:
# {"success":true,"message":"API is running"}
```

---

### Step 4: Frontend Setup & Execution

**Open New Terminal / Navigate to Frontend**:
```bash
cd "D:\Projects\Campus Connect\frontend"
```

**Install Dependencies**:
```bash
npm install
```

**Expected Output**:
```
added 150 packages in 25s
```

**Start Frontend**:
```bash
npm run dev
```

**Expected Output**:
```
VITE v5.0.0 ready in 2346 ms

➜ Local: http://localhost:3000/
➜ Network: http://192.168.x.x:3000/
```

**Frontend Ready on**: http://localhost:3000

---

### Step 5: Manual Testing

#### Test 1: API Health Check

```bash
# Terminal 3 - Test API
curl http://localhost:8080/api/v1/auth/health
```

**Expected Response**:
```json
{
  "success": true,
  "message": "API is running"
}
```

#### Test 2: User Registration via API

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@campus.edu",
    "fullName": "Test User",
    "password": "TestPass123!",
    "confirmPassword": "TestPass123!",
    "role": "STUDENT"
  }'
```

**Expected Response**:
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "token": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...",
    "email": "test@campus.edu",
    "fullName": "Test User",
    "role": "STUDENT",
    "userId": 1
  }
}
```

#### Test 3: User Login via API

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@campus.edu",
    "password": "TestPass123!"
  }'
```

**Expected Response**:
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...",
    "email": "test@campus.edu",
    "fullName": "Test User",
    "role": "STUDENT",
    "userId": 1
  }
}
```

---

### Step 6: Browser Testing

**Open Browser**:
```
http://localhost:3000
```

#### Test Registration Flow

1. **Click "Don't have an account? Register"**
2. **Fill Registration Form**:
   - Email: `john@campus.edu`
   - Full Name: `John Doe`
   - Password: `SecurePass123!`
   - Confirm: `SecurePass123!`
   - Role: Select "Student"
3. **Click "Register" Button**
4. **Expected**: Redirect to Dashboard with user info displayed

#### Test Login Flow

1. **From Dashboard, Click "Logout"**
2. **Redirected to Login Page**
3. **Fill Login Form**:
   - Email: `john@campus.edu`
   - Password: `SecurePass123!`
4. **Click "Login" Button**
5. **Expected**: Redirect to Dashboard with user info

#### Test Forgot Password Flow

1. **Click "Forgot Password?" link on Login page**
2. **Enter email**: `john@campus.edu`
3. **Click "Send Reset Link"**
4. **Expected**: Success message showing email sent
5. **Copy reset token** from backend logs (or email in production)
6. **Navigate to reset URL**: `http://localhost:3000/reset-password?token={token}`
7. **Enter new password**: `NewSecurePass456!`
8. **Click "Reset Password"**
9. **Expected**: Success message and redirect to login
10. **Login with new password**: `NewSecurePass456!`

---

## 🐳 Docker Compose Execution

### Start All Services

```bash
cd "D:\Projects\Campus Connect"

docker-compose up -d
```

**Expected Output**:
```
Creating campus_connect_db ... done
Creating campus_connect_backend ... done
Creating campus_connect_frontend ... done
```

### Monitor Services

```bash
# View all running containers
docker-compose ps

# View backend logs
docker-compose logs -f backend

# View frontend logs
docker-compose logs -f frontend

# View database logs
docker-compose logs -f postgres
```

### Stop Services

```bash
# Stop all services
docker-compose stop

# Stop specific service
docker-compose stop backend

# Remove all containers
docker-compose down

# Remove containers and volumes (delete database)
docker-compose down -v
```

### Rebuild Images

```bash
# Rebuild all images
docker-compose build

# Rebuild specific service
docker-compose build backend
docker-compose build frontend

# Rebuild and start
docker-compose up -d --build
```

---

## 📊 Database Operations

### Connect to PostgreSQL

```bash
# Using Docker
docker-compose exec postgres psql -U postgres -d campus_connect

# Or locally
psql -U postgres -d campus_connect
```

### View All Users

```sql
SELECT id, email, full_name, role, active, created_at FROM users ORDER BY id;
```

**Expected Output**:
```
 id |      email      | full_name |  role   | active |          created_at
----+-----------------+-----------+---------+--------+-------------------------------
  1 | john@campus.edu | John Doe  | STUDENT | t      | 2026-06-16 10:30:45.123456
  2 | jane@campus.edu | Jane Doe  | FACULTY | t      | 2026-06-16 10:31:20.654321
```

### View Password Reset Tokens

```sql
SELECT id, user_id, token, expiry_time, used, created_at 
FROM password_resets 
ORDER BY created_at DESC;
```

### Delete Test User

```sql
DELETE FROM users WHERE email = 'john@campus.edu';
```

### Count Users by Role

```sql
SELECT role, COUNT(*) as count FROM users GROUP BY role;
```

**Expected Output**:
```
  role   | count
---------+-------
 STUDENT |     5
 FACULTY |     2
 ADMIN   |     1
```

---

## 🔧 Common Operations

### Generate New Test User

```sql
-- Password: TestPass123!
-- Hashed with BCrypt: $2a$10$...
INSERT INTO users (email, full_name, password, role, active)
VALUES (
  'admin@campus.edu',
  'Admin User',
  '$2a$10$slYQmyNdGzin7olVV9YO2OPST9/PgBkqquzi.Ss8MCUgdXVjYzHce',
  'ADMIN',
  true
);
```

### Update User Role

```sql
UPDATE users SET role = 'FACULTY' WHERE email = 'john@campus.edu';
```

### Deactivate User

```sql
UPDATE users SET active = false WHERE email = 'john@campus.edu';
```

### Check Token Validity

```javascript
// In JavaScript/Node.js console
const token = "eyJhbGciOiJIUzUxMiJ9...";
const decoded = JSON.parse(atob(token.split('.')[1]));
console.log('Expires at:', new Date(decoded.exp * 1000));
console.log('Is Expired:', Date.now() > decoded.exp * 1000);
```

---

## 📋 Verification Checklist

After execution, verify:

- [ ] PostgreSQL container running (`docker-compose ps`)
- [ ] Backend API responding (`curl http://localhost:8080/api/v1/auth/health`)
- [ ] Frontend loading (`http://localhost:3000` loads page)
- [ ] Swagger docs accessible (`http://localhost:8080/api/swagger-ui.html`)
- [ ] Can register new user via UI
- [ ] Can login with created credentials
- [ ] JWT token stored in localStorage (F12 > Application > Local Storage)
- [ ] Dashboard displays user information
- [ ] Logout redirects to login
- [ ] Protected route redirects to login if unauthorized
- [ ] Error messages display for invalid credentials
- [ ] Password validation works (rejects weak passwords)

---

## 🚨 Troubleshooting During Execution

### Issue: "Port 8080 already in use"

**Solution**:
```bash
# Find process using port 8080
netstat -ano | findstr :8080

# Kill process
taskkill /PID {PID} /F

# Or change port in application.properties
server.port=8081
```

### Issue: "Connection refused" for backend

**Solution**:
```bash
# Check if backend is running
curl http://localhost:8080/api/v1/auth/health

# Check logs
docker-compose logs backend

# Restart backend
docker-compose restart backend
```

### Issue: "Cannot connect to database"

**Solution**:
```bash
# Check if PostgreSQL is running
docker-compose ps | grep postgres

# Check database logs
docker-compose logs postgres

# Restart database
docker-compose restart postgres
```

### Issue: "CORS error in frontend"

**Solution**:
1. Verify backend URL in frontend config
2. Check CORS configuration in SecurityConfig.java
3. Verify backend is running on correct port
4. Clear browser cache and restart

### Issue: "JWT token invalid or expired"

**Solution**:
```bash
# Check token expiry in browser console
const token = localStorage.getItem('token');
const decoded = JSON.parse(atob(token.split('.')[1]));
console.log('Expires:', new Date(decoded.exp * 1000));

# Login again to get new token
```

---

## 🧪 Performance Verification

### Check API Response Time

```bash
# Linux/macOS
time curl http://localhost:8080/api/v1/auth/health

# Windows PowerShell
Measure-Command { Invoke-WebRequest http://localhost:8080/api/v1/auth/health }
```

**Expected**: < 500ms for 95th percentile

### Check Database Performance

```sql
-- Test query performance
EXPLAIN ANALYZE SELECT * FROM users WHERE email = 'john@campus.edu';
```

**Expected**: < 100ms

---

## 📝 Execution Log Example

```
[INFO] Starting CampusConnectBackendApplication
[INFO] Tomcat initialized with port(s): 8080 (http)
[INFO] Tomcat started on port(s): 8080 (http) with context path '/api'
[INFO] Started CampusConnectBackendApplication in 8.5 seconds
[DEBUG] CORS configuration enabled for localhost:3000
[DEBUG] JWT filter configured and active
[INFO] User registered: john@campus.edu
[INFO] JWT token generated for user: john@campus.edu (userId: 1)
[INFO] User login successful: john@campus.edu
[INFO] Password reset requested for: jane@campus.edu
[INFO] Password reset email sent to: jane@campus.edu
[INFO] Password reset completed for: jane@campus.edu
```

---

**Execution Instructions Version**: 1.0  
**Last Updated**: June 16, 2026
