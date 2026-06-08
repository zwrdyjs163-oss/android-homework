@echo off
setlocal

cd /d "%~dp0"

set "JAVA_HOME=F:\java\jdk-21.0.8"
set "PATH=%JAVA_HOME%\bin;%PATH%"
set "GRADLE_USER_HOME=%CD%\.gradle-user-home"
if exist "F:\AS_Grandle\caches" (
    set "GRADLE_RO_DEP_CACHE=F:\AS_Grandle\caches"
)
set "TEMP=%CD%\.tmp"
set "TMP=%CD%\.tmp"

set "CLASSPATH="
set "JAVA_TOOL_OPTIONS="
set "_JAVA_OPTIONS="
set "JDK_JAVA_OPTIONS="

if not exist "%TEMP%" mkdir "%TEMP%"

set "AAPT2_ARG="
if exist "F:\AS_SDK\build-tools\34.0.0\aapt2.exe" (
    set "AAPT2_ARG=-Pandroid.aapt2FromMavenOverride=F:\AS_SDK\build-tools\34.0.0\aapt2.exe"
)

set "GRADLE_CMD=gradlew.bat"
if exist "F:\AS_Grandle\wrapper\dists\gradle-8.10.2-bin\a04bxjujx95o3nb99gddekhwo\gradle-8.10.2\bin\gradle.bat" (
    set "GRADLE_CMD=F:\AS_Grandle\wrapper\dists\gradle-8.10.2-bin\a04bxjujx95o3nb99gddekhwo\gradle-8.10.2\bin\gradle.bat"
)

echo JAVA_HOME=%JAVA_HOME%
"%JAVA_HOME%\bin\java.exe" -version

call "%GRADLE_CMD%" --stop
call "%GRADLE_CMD%" :tdd-score-app:testDebugUnitTest :tdd-score-app:assembleDebug --no-daemon --no-configuration-cache --max-workers=1 -Dorg.gradle.java.installations.paths=F:\java\jdk-21.0.8 -Dfile.encoding=UTF-8 %AAPT2_ARG%
if errorlevel 1 exit /b %errorlevel%

endlocal
