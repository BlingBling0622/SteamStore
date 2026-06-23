@echo off
echo Adding different screenshots to database...
"C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -u root -p123456 steam_store < add-different-screenshots.sql
if %errorlevel% equ 0 (
    echo.
    echo SUCCESS! Different screenshots added!
    echo.
    echo Now visit: http://localhost:8080/store/game/1
    echo You should see 5 DIFFERENT images in the carousel!
    echo.
) else (
    echo.
    echo Error: Could not connect to MySQL
    echo Please run add-different-screenshots.sql manually in MySQL Workbench
)
pause
