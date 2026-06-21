pipeline {
    agent any

    parameters {
        choice(name: 'TEST_TYPE', choices: ['smoke', 'regression'], description: 'Select test suite to run')
        string(name: 'EMAIL_RECIPIENT', defaultValue: 'manoj55802@gmail.com', description: 'Recipient email address for the test report')
    }

    triggers {
        cron('*/30 * * * *')
    }

    environment {
        RESOLVED_TEST_TYPE = 'smoke'
    }

    tools {
        maven 'Maven3'
        jdk 'JDK17'
    }

    stages {
        stage('Determine Test Type') {
            steps {
                script {
                    def isTimer = currentBuild.getBuildCauses().toString().contains('TimerTrigger')
                    if (isTimer) {
                        def calendar = Calendar.getInstance()
                        int hour = calendar.get(Calendar.HOUR_OF_DAY)
                        int minute = calendar.get(Calendar.MINUTE)
                        if (hour % 3 == 0 && minute < 30) {
                            env.RESOLVED_TEST_TYPE = 'regression'
                            echo "Timer triggered: Running REGRESSION tests."
                        } else {
                            env.RESOLVED_TEST_TYPE = 'smoke'
                            echo "Timer triggered: Running SMOKE tests."
                        }
                    } else {
                        env.RESOLVED_TEST_TYPE = params.TEST_TYPE ?: 'smoke'
                        echo "Manual/Non-timer trigger: Running ${env.RESOLVED_TEST_TYPE} tests."
                    }
                }
            }
        }

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
                echo "Running ${env.RESOLVED_TEST_TYPE} automation test suite..."
                dir('campus-automation') {
                    bat "mvn clean test -Dsurefire.suiteXmlFiles=testng.xml -Dcucumber.filter.tags=@${env.RESOLVED_TEST_TYPE}"
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

            echo 'Sending email report...'
            script {
                def recipient = params.EMAIL_RECIPIENT ?: 'manoj55802@gmail.com'
                emailext (
                    subject: "Campus Connect Build #${env.BUILD_NUMBER} - ${env.RESOLVED_TEST_TYPE.toUpperCase()} - ${currentBuild.currentResult}",
                    body: """
                        <h3>Campus Connect Build #${env.BUILD_NUMBER}</h3>
                        <p><b>Status:</b> ${currentBuild.currentResult}</p>
                        <p><b>Test Suite Run:</b> ${env.RESOLVED_TEST_TYPE}</p>
                        <p><b>Pipeline Job:</b> ${env.JOB_NAME}</p>
                        <p><b>Jenkins Build URL:</b> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
                        <br/>
                        <p><i>The Extent HTML test execution report has been attached to this email.</i></p>
                    """,
                    to: recipient,
                    attachmentsPattern: 'campus-automation/reports/extent-report/ExtentReport.html',
                    mimeType: 'text/html'
                )
            }
        }

        success {
            echo 'Build Successful!'
        }

        failure {
            echo 'Build Failed!'
        }
    }
}