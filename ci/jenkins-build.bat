@echo off
setlocal

rem Jenkins/Android CI bootstrap for this legacy Gradle project.
rem Default task builds the official debug APK. Pass Gradle tasks as arguments to override.

if "%JAVA_HOME%"=="" set "JAVA_HOME=F:\java\jdk1.8.0"
if "%ANDROID_HOME%"=="" set "ANDROID_HOME=F:\AS_SDK"
if "%ANDROID_SDK_ROOT%"=="" set "ANDROID_SDK_ROOT=%ANDROID_HOME%"
if "%CI_GRADLE_USER_HOME%"=="" (
    set "GRADLE_USER_HOME=%CD%\.gradle-local"
) else (
    set "GRADLE_USER_HOME=%CI_GRADLE_USER_HOME%"
)

set "PATH=%JAVA_HOME%\bin;%ANDROID_HOME%\platform-tools;%ANDROID_HOME%\tools;%PATH%"

set "TASKS=%*"
if "%TASKS%"=="" set "TASKS=:app:assembleOfficialDebug"

echo JAVA_HOME=%JAVA_HOME%
echo ANDROID_HOME=%ANDROID_HOME%
echo GRADLE_USER_HOME=%GRADLE_USER_HOME%
echo Gradle tasks: %TASKS%

call gradlew.bat %TASKS% --offline --stacktrace
set "BUILD_RESULT=%ERRORLEVEL%"

if not "%BUILD_RESULT%"=="0" exit /b %BUILD_RESULT%

if not "%WORKSPACE%"=="" (
    if not exist "%WORKSPACE%\ci-artifacts" mkdir "%WORKSPACE%\ci-artifacts"
    copy /Y ".espresso-build\app\outputs\apk\official\debug\*.apk" "%WORKSPACE%\ci-artifacts\" > nul 2> nul
    if "%ERRORLEVEL%"=="0" (
        echo Copied APK artifacts to %WORKSPACE%\ci-artifacts
    ) else (
        echo No APK artifacts were copied from .espresso-build\app\outputs\apk\official\debug
    )
)

exit /b 0
