$ErrorActionPreference = "Stop"

$root = Split-Path -Parent (Split-Path -Parent $MyInvocation.MyCommand.Path)
$sitePackages = Join-Path $root ".frida-tools-python\Lib\site-packages"
$fridaExe = Join-Path $root ".frida-tools-python\Scripts\frida.exe"

if (!(Test-Path $fridaExe)) {
    throw "frida.exe not found. Install with: python -m pip install frida-tools --prefix .\.frida-tools-python"
}

$env:PYTHONPATH = $sitePackages
& $fridaExe @args
