pipeline {
    agent any

    stages {
        stage('Build and unit test') {
            steps {
                bat 'gradlew.bat assembleDebug testDebugUnitTest'
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'app/build/outputs/apk/**/*.apk', fingerprint: true, allowEmptyArchive: true
            junit allowEmptyResults: true, testResults: 'app/build/test-results/testDebugUnitTest/**/*.xml'
        }
    }
}
