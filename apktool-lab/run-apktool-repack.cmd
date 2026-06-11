@echo off
setlocal

set "LAB_DIR=%~dp0"
set "ROOT_DIR=%LAB_DIR%.."
set "JAVA_HOME=F:\java\jdk1.8.0"
set "ANDROID_SDK_ROOT=F:\AS_SDK"
set "BUILD_TOOLS=%ANDROID_SDK_ROOT%\build-tools\34.0.0"
set "PLATFORM_TOOLS=%ANDROID_SDK_ROOT%\platform-tools"
set "PATH=%JAVA_HOME%\bin;%BUILD_TOOLS%;%PLATFORM_TOOLS%;%PATH%"

set "APKTOOL_JAR=%ROOT_DIR%\tools\apktool\apktool.jar"
set "INPUT_APK=%LAB_DIR%input\seal-generic-universal-debug.apk"
set "DECODED_DIR=%LAB_DIR%work\decoded"
set "OUTPUT_DIR=%LAB_DIR%output"
set "EVIDENCE_DIR=%LAB_DIR%evidence"
set "KEYSTORE_DIR=%LAB_DIR%keystore"
set "UNALIGNED_APK=%OUTPUT_DIR%\seal-repacked-unaligned.apk"
set "ALIGNED_APK=%OUTPUT_DIR%\seal-repacked-aligned.apk"
set "SIGNED_APK=%OUTPUT_DIR%\seal-repacked-signed.apk"
set "KEYSTORE_FILE=%KEYSTORE_DIR%\apktool-lab-debug.keystore"

if not exist "%APKTOOL_JAR%" (
    echo Missing apktool jar: "%APKTOOL_JAR%"
    exit /b 1
)

if not exist "%INPUT_APK%" (
    echo Missing input APK: "%INPUT_APK%"
    echo Copy the tested app APK to apktool-lab\input\seal-generic-universal-debug.apk
    exit /b 1
)

if not exist "%OUTPUT_DIR%" mkdir "%OUTPUT_DIR%"
if not exist "%EVIDENCE_DIR%" mkdir "%EVIDENCE_DIR%"
if not exist "%KEYSTORE_DIR%" mkdir "%KEYSTORE_DIR%"

echo ===== Step 1: decode APK with apktool =====
java -jar "%APKTOOL_JAR%" d -f "%INPUT_APK%" -o "%DECODED_DIR%"
if errorlevel 1 exit /b %errorlevel%

echo.
echo ===== Step 2: patch decoded smali/resources =====
powershell -NoProfile -ExecutionPolicy Bypass -File "%LAB_DIR%scripts\patch-decoded.ps1" -DecodedDir "%DECODED_DIR%" -EvidenceDir "%EVIDENCE_DIR%"
if errorlevel 1 exit /b %errorlevel%

echo.
echo ===== Step 3: rebuild APK with apktool =====
java -jar "%APKTOOL_JAR%" b "%DECODED_DIR%" -o "%UNALIGNED_APK%"
if errorlevel 1 exit /b %errorlevel%

echo.
echo ===== Step 4: zipalign rebuilt APK =====
zipalign -f -p 4 "%UNALIGNED_APK%" "%ALIGNED_APK%"
if errorlevel 1 exit /b %errorlevel%

echo.
echo ===== Step 5: create debug keystore if needed =====
if not exist "%KEYSTORE_FILE%" (
    keytool -genkeypair -v -keystore "%KEYSTORE_FILE%" -storepass android -keypass android -alias apktool-lab -keyalg RSA -keysize 2048 -validity 10000 -dname "CN=APKTool Lab, OU=Mobile Testing, O=DLUT, L=Dalian, S=Liaoning, C=CN"
    if errorlevel 1 exit /b %errorlevel%
)

echo.
echo ===== Step 6: sign rebuilt APK =====
call apksigner sign --ks "%KEYSTORE_FILE%" --ks-key-alias apktool-lab --ks-pass pass:android --key-pass pass:android --out "%SIGNED_APK%" "%ALIGNED_APK%"
if errorlevel 1 exit /b %errorlevel%

echo.
echo ===== Step 7: verify signature =====
call apksigner verify --verbose "%SIGNED_APK%" > "%EVIDENCE_DIR%\apksigner-verify.txt"
if errorlevel 1 exit /b %errorlevel%

echo.
echo ===== Step 8: collect result info =====
java -jar "%APKTOOL_JAR%" --version > "%EVIDENCE_DIR%\apktool-version.txt"
dir "%OUTPUT_DIR%" > "%EVIDENCE_DIR%\output-files.txt"

echo.
echo Repacked APK:
echo %SIGNED_APK%
echo.
echo Evidence:
echo %EVIDENCE_DIR%

endlocal
