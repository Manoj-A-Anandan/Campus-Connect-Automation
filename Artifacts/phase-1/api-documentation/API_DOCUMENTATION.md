# Phase 1: Complete API Documentation

**Version**: 1.0  
**Date**: June 16, 2026  
**Base URL**: `http://localhost:8080/api/v1`

---

## 📋 API Endpoints Overview

| Endpoint | Method | Authentication | Description |
|----------|--------|-----------------|-------------|
| `/auth/register` | POST | None | Register new user |
| `/auth/login` | POST | None | User login |
| `/auth/forgot-password` | POST | None | Request password reset |
| `/auth/validate-token/{token}` | POST | None | Validate reset token |
| `/auth/reset-password` | POST | None | Reset password |
| `/auth/health` | GET | None | Health check |

---

## 🔐 Authentication

### JWT Token Usage

All authenticated endpoints require a Bearer token in the Authorization header:

```
Authorization: Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...
```

**Token Validity**: 24 hours

**Token Contents**:
```json
{
  "sub": "user@campus.edu",
  "role": "STUDENT",
  "userId": 1,
  "iat": 1686828800,
  "exp": 1686915200
}
```

---

## 📝 Endpoint Details

### 1. User Registration

**Endpoint**: `POST /auth/register`

**Authentication**: Not required

**Request Headers**:
```
Content-Type: application/json
```

**Request Body**:
```json
{
  "email": "string",           // Required, must be valid email, unique
  "fullName": "string",         // Required
  "password": "string",         // Required, min 8 chars with complexity
  "confirmPassword": "string",  // Required, must match password
  "role": "string"              // Required: ADMIN, FACULTY, STUDENT
}
```

**Success Response** (201 Created):
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "token": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": null,
    "email": "john@campus.edu",
    "fullName": "John Doe",
    "role": "STUDENT",
    "userId": 1,
    "message": "Registration successful"
  }
}
```

**Error Responses**:

- **400 Bad Request** - Invalid email format:
```json
{
  "success": false,
  "message": "Invalid email format"
}
```

- **400 Bad Request** - Email already exists:
```json
{
  "success": false,
  "message": "Email already registered"
}
```

- **400 Bad Request** - Weak password:
```json
{
  "success": false,
  "message": "Password must contain at least one special character"
}
```

- **400 Bad Request** - Passwords don't match:
```json
{
  "success": false,
  "message": "Passwords do not match"
}
```

**cURL Example**:
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

**JavaScript/Axios Example**:
```javascript
import axios from 'axios';

const registerUser = async (userData) => {
  try {
    const response = await axios.post(
      'http://localhost:8080/api/v1/auth/register',
      userData
    );
    console.log('Registration successful:', response.data);
    localStorage.setItem('token', response.data.data.token);
  } catch (error) {
    console.error('Registration failed:', error.response.data.message);
  }
};

registerUser({
  email: 'john@campus.edu',
  fullName: 'John Doe',
  password: 'SecurePass123!',
  confirmPassword: 'SecurePass123!',
  role: 'STUDENT'
});
```

---

### 2. User Login

**Endpoint**: `POST /auth/login`

**Authentication**: Not required

**Request Headers**:
```
Content-Type: application/json
```

**Request Body**:
```json
{
  "email": "string",      // Required
  "password": "string"    // Required
}
```

**Success Response** (200 OK):
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": null,
    "email": "john@campus.edu",
    "fullName": "John Doe",
    "role": "STUDENT",
    "userId": 1,
    "message": "Login successful"
  }
}
```

**Error Responses**:

- **400 Bad Request** - Invalid credentials:
```json
{
  "success": false,
  "message": "Invalid email or password"
}
```

- **400 Bad Request** - User not found:
```json
{
  "success": false,
  "message": "Invalid email or password"
}
```

**cURL Example**:
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@campus.edu",
    "password": "SecurePass123!"
  }'
```

**JavaScript/Axios Example**:
```javascript
const loginUser = async (email, password) => {
  try {
    const response = await axios.post(
      'http://localhost:8080/api/v1/auth/login',
      { email, password }
    );
    localStorage.setItem('token', response.data.data.token);
    localStorage.setItem('user', JSON.stringify(response.data.data));
    // Redirect to dashboard
    window.location.href = '/dashboard';
  } catch (error) {
    console.error('Login failed:', error.response.data.message);
  }
};
```

---

### 3. Forgot Password

**Endpoint**: `POST /auth/forgot-password`

**Authentication**: Not required

**Request Headers**:
```
Content-Type: application/json
```

**Request Body**:
```json
{
  "email": "string"  // Required, must exist in system
}
```

**Success Response** (200 OK):
```json
{
  "success": true,
  "message": "Password reset link sent to your email"
}
```

**Error Response** (404 Not Found):
```json
{
  "success": false,
  "message": "User not found with email: john@campus.edu"
}
```

**Email Sent**:
The system sends an email with reset link:
```
Subject: Campus Connect - Password Reset Request

Hi John Doe,

You requested to reset your password. Click the link below to proceed:

http://localhost:3000/reset-password?token=uuid-token-here

This link will expire in 30 minutes.

If you did not request this, please ignore this email.

Best regards,
Campus Connect Team
```

**cURL Example**:
```bash
curl -X POST http://localhost:8080/api/v1/auth/forgot-password \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@campus.edu"
  }'
```

---

### 4. Validate Reset Token

**Endpoint**: `POST /auth/validate-token/{token}`

**Authentication**: Not required

**Path Parameters**:
- `token` (string, required) - Password reset token from email

**Success Response** (200 OK):
```json
{
  "success": true,
  "message": "Token is valid"
}
```

**Error Responses**:

- **400 Bad Request** - Invalid token:
```json
{
  "success": false,
  "message": "Invalid or expired reset token"
}
```

- **400 Bad Request** - Token expired:
```json
{
  "success": false,
  "message": "Reset token has expired or already used"
}
```

**cURL Example**:
```bash
curl -X POST http://localhost:8080/api/v1/auth/validate-token/f47ac10b-58cc-4372-a567-0e02b2c3d479 \
  -H "Content-Type: application/json"
```

---

### 5. Reset Password

**Endpoint**: `POST /auth/reset-password`

**Authentication**: Not required

**Request Headers**:
```
Content-Type: application/json
```

**Request Body**:
```json
{
  "token": "string",              // Required, from email link
  "newPassword": "string",        // Required, min 8 chars with complexity
  "confirmPassword": "string"     // Required, must match newPassword
}
```

**Success Response** (200 OK):
```json
{
  "success": true,
  "message": "Password reset successfully"
}
```

**Error Responses**:

- **400 Bad Request** - Invalid token:
```json
{
  "success": false,
  "message": "Invalid or expired reset token"
}
```

- **400 Bad Request** - Passwords don't match:
```json
{
  "success": false,
  "message": "Passwords do not match"
}
```

- **400 Bad Request** - Weak password:
```json
{
  "success": false,
  "message": "Password must be at least 8 characters long"
}
```

**cURL Example**:
```bash
curl -X POST http://localhost:8080/api/v1/auth/reset-password \
  -H "Content-Type: application/json" \
  -d '{
    "token": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
    "newPassword": "NewSecurePass456!",
    "confirmPassword": "NewSecurePass456!"
  }'
```

---

### 6. Health Check

**Endpoint**: `GET /auth/health`

**Authentication**: Not required

**Success Response** (200 OK):
```json
{
  "success": true,
  "message": "API is running"
}
```

**cURL Example**:
```bash
curl http://localhost:8080/api/v1/auth/health
```

---

## 🔍 HTTP Status Codes

| Status | Meaning | Scenario |
|--------|---------|----------|
| 200 | OK | Successful GET request |
| 201 | Created | Successful user registration |
| 400 | Bad Request | Invalid input, validation error |
| 401 | Unauthorized | Missing or invalid JWT token |
| 404 | Not Found | Resource not found |
| 500 | Internal Server Error | Server error |

---

## ✅ Password Validation Rules

Passwords must meet ALL these requirements:

| Rule | Example Valid | Example Invalid |
|------|---|---|
| Length | 8+ characters | `Pass123!` (8 chars) ✓ | `Pass12!` (7 chars) ✗ |
| Uppercase | Contains A-Z | `SecurePass123!` ✓ | `securepass123!` ✗ |
| Lowercase | Contains a-z | `SecurePass123!` ✓ | `SECUREPASS123!` ✗ |
| Digit | Contains 0-9 | `SecurePass123!` ✓ | `SecurePass!` ✗ |
| Special Char | Contains !@#$%^&* | `SecurePass123!` ✓ | `SecurePass123` ✗ |

**Valid Examples**:
```
SecurePass123!
Test@Pass2024
MyPassword#2024
Admin@12345!
Campus123!Connect
```

---

## 🛡️ CORS Configuration

The API is configured to accept requests from:
- `http://localhost:3000` (Frontend dev)
- `http://localhost:5173` (Vite default)

**Allowed Methods**: GET, POST, PUT, DELETE, OPTIONS  
**Allowed Headers**: * (all headers)  
**Credentials**: Enabled

To add more origins, edit `SecurityConfig.java`:
```java
configuration.setAllowedOrigins(Arrays.asList(
    "http://localhost:3000",
    "http://localhost:5173",
    "https://yourproductiondomain.com"
));
```

---

## 🔗 API Flow Diagram

```
User Registration Flow:
┌─────────────────┐
│  Frontend Form  │
└────────┬────────┘
         │
         ├─ POST /auth/register (name, email, password, role)
         ├─ Validate password strength
         ├─ Check email uniqueness
         ├─ Hash password with BCrypt
         ├─ Save user to database
         ├─ Generate JWT token (24 hours)
         │
         ▼
┌─────────────────┐
│  JWT Token in   │
│  localStorage   │
└────────┬────────┘
         │
         ├─ Redirect to Dashboard
         ├─ Include token in future requests
         │
         ▼
┌─────────────────┐
│  Protected API  │
│  Endpoints      │
└─────────────────┘

Login Flow:
┌──────────────────┐
│  Frontend Form   │
└────────┬─────────┘
         │
         ├─ POST /auth/login (email, password)
         ├─ Find user by email
         ├─ Compare password with hashed value
         ├─ Generate JWT token (24 hours)
         │
         ▼
┌──────────────────┐
│  JWT Token in    │
│  localStorage    │
└────────┬─────────┘
         │
         ├─ Redirect to Dashboard
         │
         ▼
┌──────────────────┐
│  Access Protected│
│  Resources       │
└──────────────────┘

Forgot Password Flow:
┌──────────────────┐
│  Enter Email     │
└────────┬─────────┘
         │
         ├─ POST /auth/forgot-password (email)
         ├─ Find user by email
         ├─ Generate reset token (30 min expiry)
         ├─ Send email with reset link
         │
         ▼
┌──────────────────┐
│  User Clicks     │
│  Email Link      │
└────────┬─────────┘
         │
         ├─ Frontend shows Reset Password page
         ├─ POST /auth/validate-token/{token}
         ├─ Token verified in database
         │
         ▼
┌──────────────────┐
│  Enter New       │
│  Password        │
└────────┬─────────┘
         │
         ├─ POST /auth/reset-password
         ├─ Validate token not expired/used
         ├─ Hash new password
         ├─ Update user password
         ├─ Mark token as used
         │
         ▼
┌──────────────────┐
│  Password Changed│
│  Can Login       │
└──────────────────┘
```

---

## 📊 Request/Response Examples by Role

### Example: Student Registration & Login

```json
REGISTER REQUEST:
{
  "email": "student@campus.edu",
  "fullName": "Alex Student",
  "password": "StudentPass123!",
  "confirmPassword": "StudentPass123!",
  "role": "STUDENT"
}

LOGIN REQUEST:
{
  "email": "student@campus.edu",
  "password": "StudentPass123!"
}

LOGIN RESPONSE:
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...",
    "email": "student@campus.edu",
    "fullName": "Alex Student",
    "role": "STUDENT",
    "userId": 5
  }
}
```

### Example: Faculty Registration & Login

```json
REGISTER REQUEST:
{
  "email": "faculty@campus.edu",
  "fullName": "Dr. Smith",
  "password": "FacultyPass123!",
  "confirmPassword": "FacultyPass123!",
  "role": "FACULTY"
}

LOGIN RESPONSE:
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...",
    "email": "faculty@campus.edu",
    "fullName": "Dr. Smith",
    "role": "FACULTY",
    "userId": 3
  }
}
```

### Example: Admin Registration & Login

```json
REGISTER REQUEST:
{
  "email": "admin@campus.edu",
  "fullName": "Admin User",
  "password": "AdminPass123!",
  "confirmPassword": "AdminPass123!",
  "role": "ADMIN"
}

LOGIN RESPONSE:
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...",
    "email": "admin@campus.edu",
    "fullName": "Admin User",
    "role": "ADMIN",
    "userId": 1
  }
}
```

---

## 🧪 API Testing Commands

### Using Postman Collection

1. Create a new Postman collection
2. Add these requests:
   - POST Register
   - POST Login
   - POST Forgot Password
   - POST Validate Token
   - POST Reset Password
   - GET Health

3. Add pre-request script to save token:
```javascript
if (pm.response.code === 200 || pm.response.code === 201) {
    var jsonData = pm.response.json();
    pm.environment.set("token", jsonData.data.token);
}
```

4. Add token to Authorization header:
```
Authorization: Bearer {{token}}
```

---

## 📝 Common Integration Patterns

### React Component Integration

```javascript
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/v1';

// Create axios instance with defaults
const apiClient = axios.create({
  baseURL: API_BASE_URL
});

// Add token to requests
apiClient.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Auth service
export const authService = {
  register: (data) => apiClient.post('/auth/register', data),
  login: (data) => apiClient.post('/auth/login', data),
  forgotPassword: (data) => apiClient.post('/auth/forgot-password', data),
  resetPassword: (data) => apiClient.post('/auth/reset-password', data),
  validateToken: (token) => apiClient.post(`/auth/validate-token/${token}`)
};
```

---

**API Documentation Version**: 1.0  
**Last Updated**: June 16, 2026
