@echo off
setlocal
echo ===================================================
echo [1/4] Preparing Maven Environment...
echo ===================================================

set MAVEN_VERSION=3.9.6
set MAVEN_DIR=%CD%\.mvn-local\apache-maven-%MAVEN_VERSION%
set MVN_CMD=%MAVEN_DIR%\bin\mvn.cmd

if exist "%MVN_CMD%" goto run_maven

echo Maven not found locally. Downloading Apache Maven %MAVEN_VERSION%...
if not exist ".mvn-local" mkdir .mvn-local
curl -fsSL https://archive.apache.org/dist/maven/maven-3/%MAVEN_VERSION%/binaries/apache-maven-%MAVEN_VERSION%-bin.zip -o .mvn-local\maven.zip
if %ERRORLEVEL% neq 0 goto error_download

echo Extracting Maven...
tar -xf .mvn-local\maven.zip -C .mvn-local
if %ERRORLEVEL% neq 0 goto error_extract

:run_maven
echo.
echo ===================================================
echo [2/4] Building Spring Boot Application (Fat JAR)...
echo ===================================================
call "%MVN_CMD%" clean package -DskipTests
if %ERRORLEVEL% neq 0 goto error_mvn

echo.
echo ===================================================
echo [3/4] Preparing output directory...
echo ===================================================
if exist dist rmdir /s /q dist
mkdir dist

echo.
echo ===================================================
echo [4/4] Packaging Application into Windows EXE...
echo ===================================================
jpackage --type app-image --name PatentApp --input target\ --main-jar patent-mgn-app.jar --dest dist\
if %ERRORLEVEL% neq 0 goto error_jpackage

echo.
echo ===================================================
echo [SUCCESS] Packaging complete!
echo ===================================================
echo The executable is located at:
echo %CD%\dist\PatentApp\PatentApp.exe
echo.
pause
exit /b 0

:error_download
echo.
echo [ERROR] Failed to download Maven. Please check your internet connection.
pause
exit /b 1

:error_extract
echo.
echo [ERROR] Failed to extract Maven.
pause
exit /b 1

:error_mvn
echo.
echo [ERROR] Maven build failed.
pause
exit /b 1

:error_jpackage
echo.
echo [ERROR] JPackage failed.
echo Please make sure you are using JDK 14+ and jpackage is in your PATH.
pause
exit /b 1
