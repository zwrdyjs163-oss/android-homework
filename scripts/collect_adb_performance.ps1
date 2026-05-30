param(
    [string]$Adb = "F:\AS_SDK\platform-tools\adb.exe",
    [string]$PackageName = "com.junkfood.seal.debug",
    [string]$Activity = "com.junkfood.seal.MainActivity"
)

$ErrorActionPreference = "Stop"
$root = Split-Path -Parent (Split-Path -Parent $MyInvocation.MyCommand.Path)
$outDir = Join-Path $root "docs\appium-performance-results"
New-Item -ItemType Directory -Force -Path $outDir | Out-Null

$stamp = Get-Date -Format "yyyyMMdd-HHmmss"
$summary = Join-Path $outDir "adb-performance-$stamp.txt"

function Add-Section([string]$Title, [scriptblock]$Command) {
    "===== $Title =====" | Add-Content -Path $summary -Encoding UTF8
    & $Command | Add-Content -Path $summary -Encoding UTF8
    "" | Add-Content -Path $summary -Encoding UTF8
}

& $Adb shell am force-stop $PackageName | Out-Null
Start-Sleep -Seconds 1

Add-Section "Cold start" {
    & $Adb shell am start -W -n "$PackageName/$Activity"
}

Start-Sleep -Seconds 3

Add-Section "Memory" {
    & $Adb shell dumpsys meminfo $PackageName
}

Add-Section "Graphics" {
    & $Adb shell dumpsys gfxinfo $PackageName
}

Add-Section "CPU" {
    & $Adb shell dumpsys cpuinfo
}

Write-Host "Saved ADB performance report: $summary"
