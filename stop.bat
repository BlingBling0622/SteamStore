@echo off
REM Steam Library Application Stop Script (Windows)

echo ========================================
echo Steam Library Application Stop
echo ========================================
echo.

REM Find process using port 8080
netstat -ano | findstr :8080 | findstr LISTENING > nul
if %errorlevel% neq 0 (
    echo No application running on port 8080
    pause
    exit /b 0
)

echo Found running application:
netstat -ano | findstr :8080 | findstr LISTENING
echo.

REM Extract PID and kill
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8080 ^| findstr LISTENING') do (
    echo Stopping process %%a...
    taskkill /PID %%a /F
    timeout /t 2 > nul
    goto :check
)

:check
REM Verify it stopped
netstat -ano | findstr :8080 | findstr LISTENING > nul
if %errorlevel% neq 0 (
    echo.
    echo Application stopped successfully!
) else (
    echo.
    echo WARNING: Application may still be running.
)

pause
