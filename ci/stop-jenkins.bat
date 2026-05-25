@echo off
setlocal

for /f "tokens=5" %%P in ('netstat -ano ^| findstr ":8080" ^| findstr "LISTENING"') do (
    echo Stopping Jenkins process %%P
    taskkill /PID %%P /F
    exit /b %ERRORLEVEL%
)

echo No Jenkins process was found on port 8080.
exit /b 0
