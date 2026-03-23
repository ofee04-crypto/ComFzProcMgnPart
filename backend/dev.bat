@echo off
echo ===========================================
echo Starting Hot Reload Development Mode
echo ===========================================
echo Note:
echo 1. Modify HTML/CSS files and simply refresh the browser to see changes!
echo 2. If you modify Java files, close this window and run dev.bat again.
echo.

if not exist ".mvn-local\apache-maven-3.9.6\bin\mvn.cmd" (
    echo [ERROR] Maven not found. Please run build.bat first to initialize.
    pause
    exit /b 1
)

call ".mvn-local\apache-maven-3.9.6\bin\mvn.cmd" spring-boot:run
pause
