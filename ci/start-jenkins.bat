@echo off
setlocal

set "ROOT=%~dp0.."
pushd "%ROOT%"

if not exist "jenkins.war" (
    echo jenkins.war was not found in %CD%
    exit /b 1
)

if not exist ".jenkins-home" mkdir ".jenkins-home"

set "JENKINS_HOME=%CD%\.jenkins-home"
set "JENKINS_LOG=%JENKINS_HOME%\jenkins.log"

echo Starting Jenkins on http://localhost:8080/
echo JENKINS_HOME=%JENKINS_HOME%

start "Jenkins" /min cmd /c ""F:\java\jdk-21.0.8\bin\java.exe" -jar "%CD%\jenkins.war" --httpPort=8080 > "%JENKINS_LOG%" 2>&1"

popd
