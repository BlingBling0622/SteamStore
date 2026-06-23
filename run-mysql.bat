@echo off
"C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -u root -p123456 steam_store < add-screenshots-now.sql
if %errorlevel% equ 0 (
    echo SUCCESS! Screenshots added!
    echo Refresh http://localhost:8080/store/game/1
) else (
    echo MySQL not found at default location
    echo Please run add-screenshots-now.sql manually in MySQL Workbench
)
pause
