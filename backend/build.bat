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
mkdir dist\PatentApp

echo.
echo ===================================================
echo [4/4] Creating Portable Deployment Package...
echo ===================================================
set JDK_URL=https://api.adoptium.net/v3/binary/latest/21/ga/windows/x64/jre/hotspot/normal/eclipse

if not exist ".jdk-local" mkdir .jdk-local
if not exist ".jdk-local\jdk21.zip" (
    echo Downloading Portable Java Runtime Environment from Adoptium...
    curl -L "%JDK_URL%" -o .jdk-local\jdk21.zip
    if %ERRORLEVEL% neq 0 goto error_jdk
)

echo Extracting Java Runtime (using PowerShell core. This may take 1-2 minutes)...
powershell -NoProfile -Command "Expand-Archive -Path '%CD%\.jdk-local\jdk21.zip' -DestinationPath '%CD%\dist\PatentApp' -Force"
if %ERRORLEVEL% neq 0 (
    echo [ERROR] PowerShell extraction failed!
    goto error_extract_jdk
)

:: Bulletproof rename of the extracted JRE directory
for /d %%I in (dist\PatentApp\jdk*) do ren "%%I" jre 2>nul
for /d %%I in (dist\PatentApp\amazon-corretto*) do ren "%%I" jre 2>nul
if not exist dist\PatentApp\jre (
    for /d %%I in (dist\PatentApp\*) do ren "%%I" jre 2>nul
)

echo Copying Application File...
copy target\patent-mgn-app.jar dist\PatentApp\app.jar

echo Creating Portable Startup Script...
echo @echo off > dist\PatentApp\Run_Server.bat
echo title Patent Case Management Server >> dist\PatentApp\Run_Server.bat
echo :: Support running directly from UNC Network Shares >> dist\PatentApp\Run_Server.bat
echo pushd "%%~dp0" 2^>nul >> dist\PatentApp\Run_Server.bat
echo echo =================================================== >> dist\PatentApp\Run_Server.bat
echo echo Starting Patent Case Management System... >> dist\PatentApp\Run_Server.bat
echo echo Server is running on Port 8080 >> dist\PatentApp\Run_Server.bat
echo echo Do not close this window while using the system. >> dist\PatentApp\Run_Server.bat
echo echo =================================================== >> dist\PatentApp\Run_Server.bat
echo if not exist "%%~dp0jre\bin\java.exe" ( >> dist\PatentApp\Run_Server.bat
echo     echo FATAL ERROR: jre\bin\java.exe not found! >> dist\PatentApp\Run_Server.bat
echo     echo Check if the extraction failed or folder was moved incorrectly. >> dist\PatentApp\Run_Server.bat
echo     pause >> dist\PatentApp\Run_Server.bat
echo     exit /b 1 >> dist\PatentApp\Run_Server.bat
echo ) >> dist\PatentApp\Run_Server.bat
echo "%%~dp0jre\bin\java.exe" -jar "%%~dp0app.jar" >> dist\PatentApp\Run_Server.bat
echo pause >> dist\PatentApp\Run_Server.bat

echo.
echo ===================================================
echo [SUCCESS] Packaging complete!
echo ===================================================
echo Your fully portable application is ready at:
echo %CD%\dist\PatentApp\
echo.
echo You can copy this ENTIRE FOLDER to a USB or ANY Windows computer.
echo Just double-click 'Run_Server.bat' inside it to start the system!
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

:error_jdk
echo.
echo [ERROR] Failed to download portable JDK.
pause
exit /b 1

:error_extract_jdk
echo.
echo [ERROR] Failed to extract JDK.
pause
exit /b 1
