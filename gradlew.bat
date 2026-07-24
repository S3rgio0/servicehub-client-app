@echo off
setlocal enabledelayedexpansion

set "SCRIPT_DIR=%~dp0"
if "%SCRIPT_DIR:~-1%"=="\" set "SCRIPT_DIR=%SCRIPT_DIR:~0,-1%"
set GRADLE_VERSION=8.8
set CACHE_DIR=%USERPROFILE%\.gradle\servicehub-gradle\%GRADLE_VERSION%
set GRADLE_HOME=%CACHE_DIR%\gradle-%GRADLE_VERSION%
set GRADLE_EXE=%GRADLE_HOME%\bin\gradle.bat
set ZIP_FILE=%CACHE_DIR%\gradle-%GRADLE_VERSION%-bin.zip
set GRADLE_URL=https://services.gradle.org/distributions/gradle-%GRADLE_VERSION%-bin.zip

if not exist "%GRADLE_EXE%" (
	if not exist "%CACHE_DIR%" mkdir "%CACHE_DIR%"
	if not exist "%ZIP_FILE%" (
		powershell -NoProfile -ExecutionPolicy Bypass -Command "$ProgressPreference='SilentlyContinue'; Invoke-WebRequest -Uri '%GRADLE_URL%' -OutFile '%ZIP_FILE%'"
		if errorlevel 1 exit /b 1
	)
	if exist "%GRADLE_HOME%" rmdir /s /q "%GRADLE_HOME%"
	powershell -NoProfile -ExecutionPolicy Bypass -Command "Expand-Archive -Path '%ZIP_FILE%' -DestinationPath '%CACHE_DIR%' -Force"
	if errorlevel 1 exit /b 1
)

call "%GRADLE_EXE%" -p "%SCRIPT_DIR%" %*
exit /b %ERRORLEVEL%
