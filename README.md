# Campus Connect - Campus Management System

A full-stack campus management system built with **Spring Boot 3.2** and **React + Vite**.

## 🎯 Sprint 1: Authentication Module - Complete MVP

This is the **fully functional Sprint 1 delivery** with:
- ✅ JWT-based authentication API
- ✅ User registration with password validation
- ✅ User login with token generation
- ✅ Forgot password and reset functionality
- ✅ React frontend with login, register, password reset pages
- ✅ Protected routes and dashboard
- ✅ Fully styled responsive UI
- ✅ Docker Compose setup for local development

---

## 📋 Project Structure

```
campus-connect/
├── backend/                    # Spring Boot 3.2 Backend
│   ├── src/main/java/
│   │   └── com/campusconnect/
│   │       ├── config/         # Security & Spring Config
│   │       ├── controller/     # REST Controllers
│   │       ├── service/        # Business Logic
│   │       ├── repository/     # JPA Repositories
│   │       ├── entity/         # JPA Entities
│   │       ├── dto/            # Request/Response DTOs
│   │       ├── security/       # JWT & Security
│   │       └── exception/      # Exception Handling
│   ├── src/main/resources/
│   │   └── application.properties
│   ├── pom.xml
│   └── Dockerfile
├── frontend/                   # React + Vite Frontend
│   ├── src/
│   │   ├── pages/              # LoginPage, RegisterPage, etc.
│   │   ├── components/         # ProtectedRoute
│   │   ├── services/           # API Services
│   │   ├── styles/             # CSS Styles
│   │   ├── App.jsx
│   │   └── main.jsx
│   ├── package.json
│   ├── vite.config.js
│   ├── index.html
│   └── Dockerfile
├── docker-compose.yml         # PostgreSQL + Backend + Frontend
├── .gitignore
└── README.md
```

---

## 🚀 Quick Start

### Option 1: Docker Compose (Recommended)

```bash
# Start all services (PostgreSQL, Backend, Frontend)
docker-compose up -d

# Access the application:
# Frontend: http://localhost:3000
# Backend API: http://localhost:8080/api
# Swagger UI: http://localhost:8080/api/swagger-ui.html
```

### Option 2: Local Development

#### Prerequisites
- Java 17+
- Maven 3.8+
- Node.js 18+
- PostgreSQL 14+

#### Backend Setup

```bash
cd backend

# Install dependencies
mvn clean install

# Start the application
mvn spring-boot:run

# Application will start on http://localhost:8080
# API Base URL: http://localhost:8080/api/v1
```

#### Frontend Setup

```bash
cd frontend

# Install dependencies
npm install

# Start development server
npm run dev

# Frontend will start on http://localhost:3000
```

---

## 🔐 API Endpoints

### Authentication

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/v1/auth/register` | Register new user |
| POST | `/v1/auth/login` | Login user |
| POST | `/v1/auth/forgot-password` | Request password reset |
| POST | `/v1/auth/validate-token/{token}` | Validate reset token |
| POST | `/v1/auth/reset-password` | Reset password |
| GET | `/v1/auth/health` | API health check |

### Request/Response Examples

#### Register
```json
POST /v1/auth/register
{
  "email": "student@campus.edu",
  "fullName": "John Doe",
  "password": "SecurePass123!",
  "confirmPassword": "SecurePass123!",
  "role": "STUDENT"
}

Response 201:
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "token": "eyJhbGciOiJIUzUxMiJ9...",
    "email": "student@campus.edu",
    "fullName": "John Doe",
    "role": "STUDENT",
    "userId": 1
  }
}
```

#### Login
```json
POST /v1/auth/login
{
  "email": "student@campus.edu",
  "password": "SecurePass123!"
}

Response 200:
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzUxMiJ9...",
    "email": "student@campus.edu",
    "fullName": "John Doe",
    "role": "STUDENT",
    "userId": 1
  }
}
```

---

## 🛡️ Features

### Authentication
- ✅ User registration with email validation
- ✅ Password strength validation (8+ chars, uppercase, lowercase, digit, special char)
- ✅ Email uniqueness check
- ✅ Secure password hashing (BCrypt)
- ✅ JWT token generation (24-hour expiry)
- ✅ Forgot password with email link (30-min expiry)
- ✅ Password reset functionality

### Frontend
- ✅ Login page with form validation
- ✅ Registration page with password strength feedback
- ✅ Forgot password page
- ✅ Password reset page with token validation
- ✅ Protected dashboard (requires authentication)
- ✅ JWT token persistence in localStorage
- ✅ Automatic redirect to login on token expiry
- ✅ Responsive design for mobile/tablet
- ✅ Error and success messaging

### Backend
- ✅ Spring Security with JWT filter
- ✅ CORS configuration for frontend domain
- ✅ Global exception handling
- ✅ Request/Response validation
- ✅ Database integration with PostgreSQL
- ✅ JPA Hibernate ORM
- ✅ Swagger/OpenAPI documentation
- ✅ Request logging

---

## 📦 Technology Stack

### Backend
- **Framework**: Spring Boot 3.2
- **Language**: Java 17
- **Database**: PostgreSQL 14+
- **Security**: Spring Security + JWT
- **ORM**: JPA Hibernate
- **API Docs**: Springdoc OpenAPI (Swagger)
- **Build**: Maven

### Frontend
- **Framework**: React 18.2
- **Build Tool**: Vite
- **Routing**: React Router v6
- **HTTP Client**: Axios
- **Styling**: CSS3

### DevOps
- **Containerization**: Docker
- **Orchestration**: Docker Compose

---

## 🔧 Configuration

### Backend Configuration (application.properties)

```properties
# Server
server.port=8080

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/campus_connect
spring.datasource.username=postgres
spring.datasource.password=postgres

# JWT
jwt.secret=your-secret-key
jwt.expiration=86400000  # 24 hours

# Mail (for password reset emails)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
```

### Frontend Configuration

Edit `vite.config.js` to change API endpoint:
```javascript
proxy: {
  '/api': {
    target: 'http://localhost:8080/api',
    changeOrigin: true,
    rewrite: (path) => path.replace(/^\/api/, '')
  }
}
```

---

## 🧪 Testing the APIs

### Using Postman

1. **Register** → POST `http://localhost:8080/api/v1/auth/register`
2. **Login** → POST `http://localhost:8080/api/v1/auth/login`
3. **Copy the token** from response
4. **Add Authorization header**: `Bearer {token}`
5. Access protected endpoints

### Using cURL

```bash
# Register
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@campus.edu",
    "fullName": "Test User",
    "password": "SecurePass123!",
    "confirmPassword": "SecurePass123!",
    "role": "STUDENT"
  }'

# Login
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@campus.edu",
    "password": "SecurePass123!"
  }'
```

---

## 📝 Password Requirements

- **Minimum 8 characters**
- **At least one uppercase letter** (A-Z)
- **At least one lowercase letter** (a-z)
- **At least one digit** (0-9)
- **At least one special character** (!@#$%^&*()_+\-=\[\]{};':\"\\|,.<>\/?)

---

## 🚀 Deployment

### Docker Deployment

```bash
# Build images
docker-compose build

# Run containers
docker-compose up -d

# View logs
docker-compose logs -f backend
docker-compose logs -f frontend

# Stop containers
docker-compose down
```

### Database Migrations

Hibernate auto-creates tables based on JPA entities. Configure in `application.properties`:
```properties
spring.jpa.hibernate.ddl-auto=update
```

---

## 🔄 Next Steps (Sprint 2)

- Student Management APIs (CRUD)
- Attendance Management APIs
- Course Management APIs
- Student and Attendance UI pages
- Dashboard layout

---

## 📄 License

This project is part of Campus Connect - Full Stack Portfolio Project (June 2026)

---

## 👨‍💻 Contributing

For Sprint 1 testing automation, create Jira tickets for:
1. **TESTING-1**: Automate Authentication APIs
2. **TESTING-2**: Automate Login/Registration UI

---

## 📧 Support

For issues or questions, please refer to the project documentation or contact the development team.

---

**Sprint 1 Status**: ✅ Complete - Ready for testing automation
**Last Updated**: June 16, 2026
