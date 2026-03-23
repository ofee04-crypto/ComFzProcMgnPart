@echo off
echo ===========================================
echo Starting Patent Management System...
echo ===========================================

if not exist target\patent-mgn-app.jar goto error_missing

echo Please keep this window open. 
echo To stop the system, close this window.
echo.
echo Starting server...

java -jar target\patent-mgn-app.jar

echo.
echo System stopped.
pause
exit /b 0

:error_missing
echo [ERROR] target\patent-mgn-app.jar not found!
echo Please run build.bat first.
pause
exit /b 1
