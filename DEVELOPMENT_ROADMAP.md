# Campus Connect - Full Development Roadmap
**Project**: Campus Management System  
**Duration**: 12 Weeks | 3 Phases  
**Start Date**: June 16, 2026  
**Status**: Draft

---

## 📋 Project Overview

Campus Connect is a full-stack campus management system built on:
- **Backend**: Spring Boot 3.2 + PostgreSQL 14+
- **Frontend**: React + Vite
- **Testing**: Selenium, Rest Assured, Cucumber (written separately after feature completion)
- **CI/CD**: Jenkins
- **Version Control**: GitHub

### Development Workflow
1. **Development Phase**: Build complete features (API + UI)
2. **GitHub Push**: Push completed sprint to repository
3. **Testing Phase**: Create Jira tickets for automation, write test scripts
4. **Repeat**: Move to next sprint

---

## 🚀 Phase 1: MVP - Core Application (Weeks 1-4)

### Sprint 1: Framework & Authentication (Week 1)
**Deliverable**: Complete authentication system (API + UI)  
**GitHub Push**: End of Week 1

#### Backend Development
- [x] **US-1.1.1**: Setup Spring Boot Backend Project
  - Initialize Spring Boot 3.2 project with Maven
  - Configure PostgreSQL 14+ connection
  - Setup project structure (config, entity, repository, service, controller)
  - Add dependencies (Spring Web, Data JPA, Security, JWT)
  - Create application.properties with DB config
  - Story Points: 3

- [x] **US-1.1.4**: Implement JWT Authentication API
  - Create User entity (JPA with password hashing using bcrypt)
  - Create UserRepository (JPA with custom queries)
  - Implement UserService with CRUD + authentication logic
  - Create AuthController endpoints:
    - `POST /api/v1/auth/register` (new user registration)
    - `POST /api/v1/auth/login` (user login with JWT)
    - `POST /api/v1/auth/logout` (logout/invalidate token)
  - Implement JWT token generation (24-hour expiry)
  - Configure Spring Security with JWT filter
  - Add password validation (min 8 chars, special chars required)
  - Add email uniqueness validation
  - Story Points: 8

- [x] **US-1.1.5**: Implement Forgot Password API
  - Create PasswordReset entity with token and expiry
  - Implement password reset token generation (30-min expiry)
  - Create endpoints:
    - `POST /api/v1/auth/forgot-password` (send reset email)
    - `POST /api/v1/auth/validate-token` (validate reset token)
    - `POST /api/v1/auth/reset-password` (update password)
  - Add email notification service integration (placeholder)
  - Story Points: 5

#### Frontend Development
- [x] **US-1.1.2**: Setup React Frontend Project
  - Create React app with Vite
  - Setup folder structure (components, pages, services, utilities, assets)
  - Install and configure Axios for API calls
  - Setup React Router for navigation
  - Configure environment variables for API endpoint
  - Story Points: 3

- [x] **US-1.1.6**: Implement Login UI
  - Create LoginPage component with email/password form
  - Add form validation (email format, password required)
  - Create API service for login call
  - Store JWT token in localStorage
  - Add error handling and display error messages
  - Redirect to dashboard on success
  - Add "Forgot Password" and "Register" links
  - Story Points: 5

- [x] **US-1.1.7**: Implement Registration UI
  - Create RegisterPage component
  - Add fields: Email, Full Name, Password, Confirm Password
  - Add form validation (all fields required, password strength check)
  - Create API service for registration
  - Show success message and redirect to login
  - Add error handling (duplicate email, weak password)
  - Add "Back to Login" link
  - Story Points: 5

#### Framework Setup
- [x] **US-1.1.3**: Setup Test Automation Framework
  - Create Maven project for automation
  - Add dependencies: Selenium, Rest Assured, Cucumber, TestNG, Extent Reports
  - Setup POM structure (pages, drivers, utilities, config)
  - Create ConfigReader utility
  - Create WebDriver Factory with thread safety
  - Setup Cucumber step definitions base class
  - Create test data folder structure
  - Story Points: 5

**Sprint 1 Summary**:
- Total Story Points: 34
- Total User Stories: 7
- All P0 (Critical) priority
- **Outcome**: Full authentication module (API + UI) ready, automation framework scaffold created, pushed to GitHub

---

### Sprint 2: Student & Attendance Management (Week 2)
**Deliverable**: Complete student management and attendance APIs + UI  
**GitHub Push**: End of Week 2

#### Backend Development
- [x] **US-1.2.1**: Create Student Management APIs (CRUD)
  - Create Student entity (JPA)
  - Create StudentRepository with custom queries
  - Create StudentService with CRUD operations
  - Create StudentController endpoints:
    - `POST /api/v1/students` (create new student)
    - `GET /api/v1/students` (list students with pagination)
    - `GET /api/v1/students/{id}` (get student by ID)
    - `PUT /api/v1/students/{id}` (update student)
    - `DELETE /api/v1/students/{id}` (soft delete)
  - Add validation for roll number uniqueness
  - Add input validation (email, phone format)
  - Story Points: 8

- [x] **US-1.2.2**: Create Attendance Management APIs
  - Create Attendance entity (JPA)
  - Create AttendanceRepository with custom queries
  - Create AttendanceService with business logic
  - Create AttendanceController endpoints:
    - `POST /api/v1/attendance` (mark attendance)
    - `GET /api/v1/attendance/student/{studentId}` (view student attendance)
    - `GET /api/v1/attendance/course/{courseId}` (view course attendance)
    - `PUT /api/v1/attendance/{id}` (update attendance record)
  - Add validation (prevent duplicate marks for same student-course-date)
  - Add attendance status enum (Present, Absent, Late)
  - Story Points: 8

- [x] **US-1.2.3**: Create Course Management APIs
  - Create Course entity (JPA)
  - Create CourseRepository
  - Create CourseService with CRUD
  - Create CourseController endpoints:
    - `POST /api/v1/courses` (create course)
    - `GET /api/v1/courses` (list courses)
    - `GET /api/v1/courses/{id}` (get course)
    - `PUT /api/v1/courses/{id}` (update course)
    - `DELETE /api/v1/courses/{id}` (delete course)
  - Link course to faculty
  - Story Points: 5

#### Frontend Development
- [x] **US-1.2.4**: Implement Student Management UI
  - Create StudentListPage component
  - Create StudentFormPage (add/edit)
  - Add student search and filter
  - Create API services for CRUD operations
  - Add success/error toast notifications
  - Story Points: 8

- [x] **US-1.2.5**: Implement Attendance UI
  - Create AttendanceMarkPage component
  - Create AttendanceReportPage component
  - Add date picker for attendance
  - Create API services for attendance operations
  - Add bulk attendance marking (optional)
  - Story Points: 8

- [x] **US-1.2.6**: Create Dashboard Layout
  - Create main Dashboard shell
  - Add sidebar navigation
  - Add header with user profile and logout
  - Create protected routes (with JWT verification)
  - Story Points: 5

**Sprint 2 Summary**:
- Total Story Points: 42
- Total User Stories: 6
- All P0 (Critical) priority
- **Outcome**: Student and Attendance modules complete, dashboard layout, pushed to GitHub

---

### Sprint 3: Event Management & CI/CD (Week 3)
**Deliverable**: Complete event management system + GitHub integration  
**GitHub Push**: End of Week 3

#### Backend Development
- [x] **US-1.3.1**: Create Event Management APIs
  - Create Event entity (JPA)
  - Create EventRepository
  - Create EventService with CRUD + business logic
  - Create EventController endpoints:
    - `POST /api/v1/events` (create event)
    - `GET /api/v1/events` (list with filters: upcoming, past)
    - `GET /api/v1/events/{id}` (get event details)
    - `PUT /api/v1/events/{id}` (update event)
    - `DELETE /api/v1/events/{id}` (delete event)
  - Add event status (Active, Cancelled, Completed)
  - Story Points: 8

- [x] **US-1.3.2**: Create Event Registration APIs
  - Create EventRegistration entity (many-to-many with Event and Student)
  - Create EventRegistrationService
  - Create EventRegistrationController endpoints:
    - `POST /api/v1/event-registrations` (register for event)
    - `GET /api/v1/event-registrations/student/{studentId}` (my events)
    - `GET /api/v1/event-registrations/event/{eventId}` (registered students)
    - `DELETE /api/v1/event-registrations/{id}` (unregister)
  - Add capacity validation
  - Story Points: 5

#### Frontend Development
- [x] **US-1.3.3**: Implement Event Management UI
  - Create EventListPage component
  - Create EventDetailPage component
  - Create EventFormPage (add/edit)
  - Add event filters (date range, category)
  - Add event registration flow
  - Story Points: 8

#### CI/CD & Version Control
- [x] **US-1.3.4**: Setup GitHub Repository & CI/CD Pipeline
  - Create GitHub repository
  - Setup .gitignore for Spring Boot and React projects
  - Create docker-compose.yml (PostgreSQL, Spring Boot, React)
  - Setup Dockerfile for backend and frontend
  - Create Jenkinsfile with build, test, deploy stages
  - Configure GitHub webhooks for Jenkins
  - Setup branch protection rules (main branch)
  - Story Points: 8

**Sprint 3 Summary**:
- Total Story Points: 29
- Total User Stories: 4
- All P0 (Critical) priority
- **Outcome**: Event management complete, CI/CD pipeline ready, GitHub setup, pushed to GitHub

---

### Sprint 4: Testing & Optimization (Week 4)
**Deliverable**: Automation scripts for all Sprint 1-3 features  
**GitHub Push**: End of Week 4

#### Automation Framework
- [x] **US-1.4.1**: Create Jira Testing Tickets & Automation Scripts
  - **TESTING-1**: Automate Authentication APIs
    - Scenarios: Successful login, Invalid credentials, Successful registration, Duplicate email, Weak password
    - Tool: Rest Assured + Cucumber
    - Coverage: All auth endpoints
  
  - **TESTING-2**: Automate Login/Registration UI
    - Scenarios: Valid login, Invalid credentials, Successful registration, Forgot password flow
    - Tool: Selenium + POM + Cucumber
    - Coverage: Login and Registration pages
  
  - **TESTING-3**: Automate Student Management APIs
    - Scenarios: Create, Read, Update, Delete students, Validation errors
    - Tool: Rest Assured + Cucumber
  
  - **TESTING-4**: Automate Student Management UI
    - Scenarios: View list, Create new, Update, Delete, Search/Filter
    - Tool: Selenium + POM
  
  - **TESTING-5**: Automate Attendance APIs
    - Scenarios: Mark attendance, Update, Retrieve by student/course, Validations
    - Tool: Rest Assured + Cucumber
  
  - **TESTING-6**: Automate Attendance UI
    - Scenarios: Mark attendance, View reports, Apply filters
    - Tool: Selenium + POM
  
  - **TESTING-7**: Automate Event Management APIs
    - Scenarios: Create, Update, Delete events, Register/Unregister
    - Tool: Rest Assured + Cucumber
  
  - **TESTING-8**: Automate Event Management UI
    - Scenarios: View events, Create, Edit, Register, Filter
    - Tool: Selenium + POM
  
  - **TESTING-9**: Integration & End-to-End Tests
    - Scenarios: Complete user journeys (Register → Login → Mark Attendance → View Reports)
    - Tool: Selenium + Cucumber
  
  - **TESTING-10**: Database Validation Tests
    - Verify data integrity in PostgreSQL after API operations
    - Tool: JDBC + Rest Assured
  
  - **TESTING-11**: Performance & Load Tests
    - API response times, concurrent user handling
    - Tool: JMeter (optional Phase 2)

**Sprint 4 Summary**:
- Total Story Points: 50+
- Jira Tickets Created: 11+ testing tickets
- **Outcome**: Complete automation coverage for Phase 1, all tests passing, pushed to GitHub with Extent Reports

---

## 🔧 Phase 2: Enhancement Features (Weeks 5-7)

### Sprint 5: File Management & Notifications (Week 5)
**Features to Develop**:
- File upload (profile pictures, documents)
- File validation and storage
- Notification system (email/in-app)
- Email service integration
- **Testing**: Automation scripts for file upload and notifications

### Sprint 6: Attendance Alerts & Bulk Operations (Week 6)
**Features to Develop**:
- Attendance threshold alerts (< 75%)
- Bulk student import (CSV)
- Bulk attendance marking
- Leave approval workflow
- **Testing**: Automation for bulk operations and alerts

### Sprint 7: Advanced Filtering & Export (Week 7)
**Features to Develop**:
- Advanced search and filtering (all modules)
- Data export (Excel/PDF)
- Report generation
- **Testing**: Automation for export and report features

---

## 📊 Phase 3: Dashboard & Scalability (Weeks 8-12)

### Sprint 8: Admin Dashboard (Week 8)
**Features to Develop**:
- Statistics and KPIs dashboard
- Charts and graphs (attendance trends, event registrations)
- User management panel
- System logs and audit trail
- **Testing**: Dashboard UI automation

### Sprint 9: Analytics & Reports (Week 9)
**Features to Develop**:
- Advanced reporting engine
- Custom report builder
- Export to PDF/Excel
- Schedule reports (email delivery)
- **Testing**: Report generation and delivery automation

### Sprint 10: Performance & Optimization (Week 10)
**Features to Develop**:
- Database query optimization
- Caching layer (Redis optional)
- API response time optimization
- Frontend optimization (lazy loading, code splitting)
- **Testing**: Performance and load testing

### Sprint 11: Two-Factor Authentication & Security (Week 11)
**Features to Develop**:
- 2FA implementation (OTP/authenticator)
- Account lockout after failed attempts
- Password strength enforcement
- Session management
- **Testing**: Security automation tests

### Sprint 12: Production Hardening & Documentation (Week 12)
**Features to Develop**:
- API documentation (Swagger/OpenAPI)
- User guide and admin guide
- Deployment runbook
- Final testing and bug fixes
- Production release
- **Testing**: Final regression testing, documentation

---

## 📌 Development Workflow Summary

### For Each Sprint:
1. **Development Phase**:
   - Build Backend APIs (entities, repositories, services, controllers)
   - Build Frontend UI (components, pages, services)
   - Test manually with Postman/browser
   - Commit to feature branch

2. **GitHub Push**:
   - Create pull request (code review)
   - Merge to `develop` branch
   - Push to `main` branch (tagged release)

3. **Testing Phase**:
   - Create Jira testing tickets
   - Write Cucumber scenarios
   - Implement Rest Assured API tests
   - Implement Selenium UI tests
   - Execute tests and generate Extent Reports

4. **Repeat**: Move to next sprint

---

## 🎯 Success Metrics

| Metric | Target |
|--------|--------|
| Code Coverage | ≥ 85% |
| Test Pass Rate | ≥ 95% |
| API Response Time | < 500ms (95th percentile) |
| Automation Coverage | ≥ 80% |
| On-time Sprint Completion | 100% |
| Critical Bugs in Production | 0 |
| Deployment Success Rate | 100% |
| Documentation Completeness | 100% |

---

## 📦 Deliverables by Phase

### Phase 1 Deliverables (End of Week 4):
- ✅ Spring Boot backend with all core APIs
- ✅ React frontend with all UI pages
- ✅ PostgreSQL database schema
- ✅ GitHub repository with CI/CD pipeline
- ✅ Complete automation test suite (80+ test cases)
- ✅ Extent Reports
- ✅ Docker Compose setup

### Phase 2 Deliverables (End of Week 7):
- ✅ File management and notifications
- ✅ Bulk operations and alerts
- ✅ Advanced filtering and export
- ✅ Extended automation coverage

### Phase 3 Deliverables (End of Week 12):
- ✅ Admin dashboard and analytics
- ✅ Performance optimization
- ✅ Enhanced security (2FA, session management)
- ✅ Production-ready application
- ✅ Complete documentation
- ✅ 100+ automated test cases
- ✅ Jenkins CI/CD fully operational

---

## 🔗 GitHub Repository Structure

```
campus-connect/
├── backend/                    # Spring Boot project
│   ├── src/
│   ├── pom.xml
│   ├── Dockerfile
│   └── application.properties
├── frontend/                   # React project
│   ├── src/
│   ├── package.json
│   ├── vite.config.js
│   └── Dockerfile
├── automation/                 # Test automation project
│   ├── src/
│   ├── pom.xml
│   ├── features/              # Cucumber feature files
│   ├── reports/               # Extent Reports
│   └── Dockerfile
├── docker-compose.yml         # Local development setup
├── Jenkinsfile                # CI/CD pipeline
└── README.md
```

---

## ✅ Next Steps

1. **Week 1**: Start Sprint 1 development (Backend auth API + Frontend login/register)
2. **End of Week 1**: Push to GitHub
3. **Week 2**: Create testing Jira tickets, write automation scripts
4. **Week 2+**: Parallel development (Sprint 2) + testing (Sprint 1 automation)
5. **Continue**: Repeat for Sprints 3 and 4

---

**Status**: Ready to begin Sprint 1 development  
**Last Updated**: June 16, 2026
