winget install ffmpeg
winget install yt-dlp

@echo off

where java >nul 2>nul
if %errorlevel% equ 0 (
    echo Java is installed and available as a command-line tool.
    explorer "http://localhost"
    java -jar opensoundqueue.jar
) else (
    echo Java is not installed or not available as a command-line tool.
    echo Please install Java and ensure it is in your PATH.
    pause
)