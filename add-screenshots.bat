@echo off
echo ================================================
echo Adding Screenshots to Steam Store Database
echo ================================================
echo.

cd /d "%~dp0"

echo Connecting to MySQL and adding screenshots...
mysql -u root -p123456 steam_store < add-screenshots-now.sql

if %errorlevel% equ 0 (
    echo.
    echo ================================================
    echo SUCCESS! Screenshots added to database
    echo ================================================
    echo.
    echo Now refresh your browser at:
    echo http://localhost:8080/store/game/1
    echo.
    echo You should see 5 thumbnails in the carousel!
    echo ================================================
) else (
    echo.
    echo ================================================
    echo ERROR: Could not connect to MySQL
    echo ================================================
    echo.
    echo Please do it manually:
    echo 1. Open MySQL Workbench
    echo 2. Connect to steam_store database
    echo 3. Run the SQL from: add-screenshots-now.sql
    echo ================================================
)

echo.
pause
