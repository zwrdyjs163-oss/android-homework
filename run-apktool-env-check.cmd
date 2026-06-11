@echo off
setlocal

cd /d "%~dp0"

set "JAVA_HOME=F:\java\jdk1.8.0"
if exist "F:\java\jdk-21.0.8\bin\java.exe" (
    set "JAVA21_HOME=F:\java\jdk-21.0.8"
)
set "APKTOOL_DIR=%CD%\tools\apktool"
set "ANDROID_SDK_ROOT=F:\AS_SDK"
set "BUILD_TOOLS=%ANDROID_SDK_ROOT%\build-tools\34.0.0"
set "PATH=%APKTOOL_DIR%;%ANDROID_SDK_ROOT%\platform-tools;%BUILD_TOOLS%;%JAVA_HOME%\bin;%PATH%"

echo ===== Java =====
where java
java -version

echo.
echo ===== Apktool =====
if not exist "%APKTOOL_DIR%\apktool.jar" (
    echo Missing %APKTOOL_DIR%\apktool.jar
    exit /b 1
)
java -jar "%APKTOOL_DIR%\apktool.jar" --version

echo.
echo ===== Android SDK tools =====
where adb
adb version
where zipalign
zipalign -h >nul 2>nul
echo zipalign available
where apksigner
apksigner --version

echo.
echo Environment check finished.

endlocal
