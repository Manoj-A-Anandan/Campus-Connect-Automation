pipeline {
    agent any

    stages {
        stage('Clean Environment') {
            steps {
                echo 'Cleaning up existing containers...'
                bat 'docker rm -f campus_connect_db campus_connect_backend campus_connect_frontend || exit 0'
                bat 'docker compose down --volumes --remove-orphans || exit 0'
            }
        }

        stage('Build & Start Services') {
            steps {
                echo 'Starting application database, API, and frontend...'
                bat 'docker compose up -d --build'
            }
        }

        stage('Health Check') {
            steps {
                echo 'Waiting for Backend API to be responsive...'
                bat '''
                @echo off
                set timeout=90
                :loop_backend
                curl -s http://localhost:8080/api/swagger-ui.html >nul 2>&1
                if %errorlevel% equ 0 (
                    echo Backend API is responsive!
                    goto backend_up
                )
                ping 127.0.0.1 -n 6 >nul
                set /a timeout=timeout-5
                if %timeout% gtr 0 goto loop_backend
                echo Error: Backend API failed to start.
                exit /b 1
                :backend_up
                echo Backend is ready!
                '''

                echo 'Waiting for Frontend Web App to be responsive...'
                bat '''
                @echo off
                set timeout=45
                :loop_frontend
                curl -sI http://localhost:3000/login | findstr "200" >nul 2>&1
                if %errorlevel% equ 0 (
                    echo Frontend is responsive!
                    goto frontend_up
                )
                ping 127.0.0.1 -n 6 >nul
                set /a timeout=timeout-5
                if %timeout% gtr 0 goto loop_frontend
                echo Error: Frontend failed to start.
                exit /b 1
                :frontend_up
                echo Frontend is ready!
                '''
            }
        }

        stage('Run Automation Tests') {
            steps {
                echo 'Running automation test suite...'
                dir('campus-automation') {
                    bat 'mvn clean test "-Dsurefire.suiteXmlFiles=testng.xml"'
                }
            }
        }
    }

    post {
        always {
            echo 'Archiving test execution reports...'
            dir('campus-automation') {
                // Archive Extent reports and screenshots
                archiveArtifacts artifacts: 'reports/extent-report/**/*', allowEmptyArchive: true
                // Archive Log4j2 logs
                archiveArtifacts artifacts: 'target/logs/**/*', allowEmptyArchive: true
            }

            // Publish TestNG results inside Jenkins UI
            junit testResults: 'campus-automation/target/surefire-reports/*.xml', allowEmptyResults: true

            echo 'Tearing down Docker environment...'
            bat 'docker compose down --volumes || exit 0'
        }

        success {
            echo 'Build Successful!'
        }

        failure {
            echo 'Build Failed!'
        }
    }
}