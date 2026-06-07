param(
    [Parameter(Mandatory = $true)]
    [string]$FridaServerXz,
    [string]$Adb = "F:\AS_SDK\platform-tools\adb.exe"
)

$ErrorActionPreference = "Stop"
$root = Split-Path -Parent (Split-Path -Parent $MyInvocation.MyCommand.Path)
$targetDir = Join-Path $root "frida-callgraph"
$serverPath = Join-Path $targetDir "frida-server"

if (!(Test-Path $FridaServerXz)) {
    throw "frida-server xz file not found: $FridaServerXz"
}

@"
import lzma
from pathlib import Path
source = Path(r"$FridaServerXz")
target = Path(r"$serverPath")
target.write_bytes(lzma.decompress(source.read_bytes()))
print(target)
"@ | python -

& $Adb push $serverPath /data/local/tmp/frida-server
& $Adb shell chmod 755 /data/local/tmp/frida-server

& $Adb root | Out-Host
Start-Sleep -Seconds 2

$deviceId = (& $Adb shell id) -join "`n"
if ($deviceId -match "uid=0") {
    & $Adb shell "/data/local/tmp/frida-server >/data/local/tmp/frida.log 2>&1 &"
} else {
    & $Adb shell su -c "/data/local/tmp/frida-server >/data/local/tmp/frida.log 2>&1 &"
}

Write-Host "frida-server pushed and started."
