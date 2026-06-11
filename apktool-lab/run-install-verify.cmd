@echo off
setlocal

set "LAB_DIR=%~dp0"
set "ANDROID_SDK_ROOT=F:\AS_SDK"
set "PATH=%ANDROID_SDK_ROOT%\platform-tools;%PATH%"
set "SIGNED_APK=%LAB_DIR%output\seal-repacked-signed.apk"
set "EVIDENCE_DIR=%LAB_DIR%evidence"
set "PACKAGE_NAME=com.junkfood.seal.debug"
set "MAIN_ACTIVITY=com.junkfood.seal.MainActivity"

if not exist "%SIGNED_APK%" (
    echo Missing signed APK: "%SIGNED_APK%"
    echo Run apktool-lab\run-apktool-repack.cmd first.
    exit /b 1
)

if not exist "%EVIDENCE_DIR%" mkdir "%EVIDENCE_DIR%"

echo ===== Connected devices =====
adb devices

echo.
echo ===== Install repacked APK =====
adb uninstall %PACKAGE_NAME% >nul 2>nul
adb install -r "%SIGNED_APK%"
if errorlevel 1 exit /b %errorlevel%

echo.
echo ===== Launch app and capture logcat marker =====
adb logcat -c
adb shell am start -n %PACKAGE_NAME%/%MAIN_ACTIVITY%
ping 127.0.0.1 -n 6 >nul
adb logcat -d | findstr APKTOOL_LAB > "%EVIDENCE_DIR%\logcat-apktool-lab.txt"

echo.
echo Logcat evidence:
type "%EVIDENCE_DIR%\logcat-apktool-lab.txt"

endlocal
