# Campus Connect - Test Automation Framework & CI/CD Pipeline

This repository contains the **automated test suite** and CI/CD pipeline for **Campus Connect**, an enterprise campus management system. 

The focus of this test suite is the **Sprint 1 Delivery: Authentication Module MVP**. The automation framework validates API integrations, web UI flows, security boundaries, and session management.

---

## 🛠️ Test Automation Stack

The framework is a **BDD-driven hybrid automation framework** built with Java and Maven:
- **Language**: Java 17
- **Test Runner**: TestNG (v7.10.2)
- **BDD Framework**: Cucumber (v7.18.0)
- **API Testing**: REST Assured (v5.5.6)
- **UI Testing**: Selenium WebDriver (v4.44.0) with WebDriverManager (v5.9.1)
- **Reporting**: Extent Reports (v5.1.2), Allure Reports (v2.29.0)
- **Logging**: Log4j2 (v2.23.1)
- **CI/CD**: Jenkins Pipeline (`Jenkinsfile`)

---

## 📋 Directory Structure

The automation source is located in the `campus-automation` subdirectory:

```text
campus-automation/
├── src/test/java/
│   ├── config/            # Thread-safe config properties loader
│   ├── context/           # TestContext for cross-step data sharing
│   ├── driver/            # Thread-safe Web Driver manager (ThreadLocal)
│   ├── dto/               # REST Request/Response Data Transfer Objects
│   ├── hooks/             # Cucumber hooks (browser lifecycle, screenshots)
│   ├── pages/             # Page Object Model (POM) classes
│   ├── reports/           # Allure / Extent report setup and listeners
│   ├── runner/            # TestNG Cucumber Runner classes
│   ├── stepdefinitions/   # Cucumber step definitions
│   │   ├── api/           # REST Assured step definitions
│   │   └── ui/            # Selenium step definitions
│   └── utilities/         # Custom loggers, DB query utilities, listeners
├── src/test/resources/
│   ├── features/          # Cucumber Gherkin feature files
│   │   ├── api/           # API Scenarios (ForgotPassword, Login, Register, ResetPassword)
│   │   └── ui/            # UI Scenarios (Login, Register)
│   ├── schemas/           # JSON schema files for contract validation
│   ├── config.properties  # Test run properties (browser, URLs, headless flag)
│   └── log4j2.xml         # Logging configuration
├── pom.xml                # Maven project structure & dependencies
└── testng.xml             # Test suite runner suites xml
```

---

## 🚀 Quick Start & Local Execution

### 1. Run the Application Under Test (AUT)
The application services must be running before executing the automation tests. Use Docker Compose to spin them up:

```bash
# Start all services (PostgreSQL, Backend API, React Frontend)
docker-compose up -d --build
```
Verify the application is up:
- Frontend UI: `http://localhost:3000`
- Backend API: `http://localhost:8080/api/v1`
- Swagger UI: `http://localhost:8080/api/swagger-ui.html`

### 2. Configure Execution Settings
Edit `campus-automation/src/test/resources/config.properties` if needed:
```properties
browser = chrome
base.url = http://localhost:3000
headless = true
api.base.url = http://localhost:8080/api/v1
```

### 3. Run Automation Suites via Maven
Run the commands from the `campus-automation` directory:

```bash
cd campus-automation

# Run the complete test suite (UI + API)
mvn clean test

# Run API Tests only
mvn clean test -Dcucumber.filter.tags="@api"

# Run UI Tests only
mvn clean test -Dcucumber.filter.tags="@ui"

# Run Smoke Tests
mvn clean test -Dcucumber.filter.tags="@smoke"

# Run Regression Tests
mvn clean test -Dcucumber.filter.tags="@regression"
```

---

## 📊 Test Reports & Execution Logs

Once tests are executed, reports and logs are generated automatically:

### 1. Extent HTML Report
- **Path**: `campus-automation/reports/extent-report/ExtentReport.html`
- A single, self-contained interactive dashboard showing step execution, duration, and failure screenshots.

### 2. Failure Screenshots
- **Path**: `campus-automation/reports/extent-report/screenshots/`
- For UI tests, a screenshot is automatically captured upon test step failure and embedded directly inside the Extent report.

### 3. Allure Reports
- Allure results are written to `campus-automation/target/allure-results`.
- To generate and view the report in your web browser:
  ```bash
  allure serve target/allure-results
  ```

### 4. Log4j2 Logs
- Running tests writes real-time steps to the console and to files under `campus-automation/logs/`.
- Use this log file to debug request payloads, response validations, and browser element interactions.

---

## 🔗 Jenkins CI/CD Pipeline

The repository includes a Jenkins pipeline defined in the [Jenkinsfile](./Jenkinsfile) at the root directory:
- **Clean up**: Deletes existing containers.
- **Build**: Compiles, starts the AUT services via Docker.
- **Health Checks**: Polls API and UI endpoints until responsive.
- **Run Suite**: Triggers TestNG execution in headless Chrome mode.
- **Post-Build Actions**: 
  - Archives Extent Reports and failure screenshots as build artifacts.
  - Publishes TestNG results inside the Jenkins UI.
  - Sends email reports to the configured recipient.
  - Tears down the Docker stack.

*Note: The automatic schedule and polling triggers are currently commented out inside the `Jenkinsfile` triggers block to allow manual runs during project updates.*
