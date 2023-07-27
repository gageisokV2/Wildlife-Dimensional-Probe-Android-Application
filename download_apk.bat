@echo off
setlocal

set "vbscript=%temp%\folderdialog.vbs"
set "download_path="

:: Create a temporary VBScript file
echo Set objShell = WScript.CreateObject("Shell.Application") > "%vbscript%"
echo Set objFolder = objShell.BrowseForFolder(0, "Select the path to download the Wildlife Dimension Probe Android Application APK:", 0, 0) >> "%vbscript%"
echo If objFolder Is Nothing Then >> "%vbscript%"
echo     WScript.Quit(1) >> "%vbscript%"
echo End If >> "%vbscript%"
echo WScript.Echo objFolder.Self.Path >> "%vbscript%"

:: Execute the VBScript and capture the selected directory path
for /f "delims=" %%I in ('cscript //nologo "%vbscript%"') do set "download_path=%%I"

:: Remove the temporary VBScript file
del "%vbscript%"

if "%download_path%"=="" (
    echo No directory path selected. Exiting script.
    pause
    exit /b
)

echo Downloading Python files...
curl -L -o "%download_path%\app-debug.apk" "https://raw.githubusercontent.com/gageisokV2/Wildlife-Dimensional-Probe-Android-Application/main/app-debug.apk"

echo APK downloaded successfully to %download_path%.

pause
