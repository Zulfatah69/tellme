@echo off
title TellMe - Spring Boot + Cloudflare Tunnel
color 0A

echo.
echo  ================================================
echo    TellMe - Starting Application
echo  ================================================
echo.

REM -------------------------------------------------------
REM  Path cloudflared (hardcoded karena tidak di PATH)
REM -------------------------------------------------------
set CLOUDFLARED=C:\Cloudflared\cloudflared.exe

REM -------------------------------------------------------
REM  Cek apakah cloudflared.exe ada
REM -------------------------------------------------------
if not exist "%CLOUDFLARED%" (
    echo  [ERROR] cloudflared tidak ditemukan di: %CLOUDFLARED%
    echo  Pastikan file ada di C:\Cloudflared\cloudflared.exe
    echo.
    pause
    exit /b 1
)
echo  [OK] cloudflared ditemukan: %CLOUDFLARED%
echo.

REM -------------------------------------------------------
REM  Cek dan bebaskan port 8082 jika sudah dipakai
REM -------------------------------------------------------
echo  Mengecek port 8082...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":8082 " ^| findstr "LISTENING"') do (
    echo  [!] Port 8082 dipakai oleh PID %%a - Mematikan...
    taskkill /PID %%a /F >nul 2>&1
)
echo  [OK] Port 8082 siap digunakan.
echo.

REM -------------------------------------------------------
REM  Start Spring Boot di port 8082
REM -------------------------------------------------------
echo  [1/2] Starting Spring Boot di port 8082...
start "TellMe - Spring Boot" cmd /k "cd /d "%~dp0" && mvnw.cmd spring-boot:run -Dspring-boot.run.arguments=--server.port=8082"

REM -------------------------------------------------------
REM  Tunggu Spring Boot siap (20 detik)
REM -------------------------------------------------------
echo.
echo  Menunggu Spring Boot siap (20 detik)...
timeout /t 20 /nobreak

REM -------------------------------------------------------
REM  Start Cloudflare Tunnel ke port 8082
REM -------------------------------------------------------
echo.
echo  [2/2] Menjalankan Cloudflare Tunnel...
echo  Tunnel ID : 6ad1f32d-3f75-421f-98d7-884ddb8187c9
echo  Local URL : http://localhost:8082
echo.
"%CLOUDFLARED%" tunnel run 6ad1f32d-3f75-421f-98d7-884ddb8187c9

echo.
echo  Cloudflare Tunnel berhenti.
pause
