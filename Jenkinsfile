pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    environment {
        JAVA_HOME = 'F:\\java\\jdk1.8.0'
        ANDROID_HOME = 'F:\\AS_SDK'
        ANDROID_SDK_ROOT = 'F:\\AS_SDK'
        GRADLE_USER_HOME = "${WORKSPACE}\\.gradle-local"
    }

    stages {
        stage('Build APK') {
            steps {
                bat 'ci\\jenkins-build.bat :app:assembleOfficialDebug'
            }
        }

        stage('Archive artifacts') {
            steps {
                archiveArtifacts artifacts: '.espresso-build/app/outputs/apk/official/debug/*.apk', fingerprint: true
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true, testResults: '.espresso-build/app/test-results/**/*.xml, .espresso-build/app/outputs/androidTest-results/**/*.xml'
            archiveArtifacts allowEmptyArchive: true, artifacts: '.espresso-build/app/reports/**/*'
        }
    }
}
