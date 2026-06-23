@echo off
echo Running SQL fix...
"C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -u root -p123456 steam_store < quick-fix-different-images.sql
if %errorlevel% equ 0 (
    echo.
    echo SUCCESS! Screenshots updated!
    echo.
    echo Now refresh: http://localhost:8080/store/game/2
    echo You should see 5 DIFFERENT images!
) else (
    echo.
    echo MySQL not found. Please:
    echo 1. Open MySQL Workbench
    echo 2. Run quick-fix-different-images.sql
)
pause
