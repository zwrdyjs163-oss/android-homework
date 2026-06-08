param(
    [string]$PackageName = "com.junkfood.seal.debug",
    [string[]]$JavaMethod = @(
        "com.junkfood.seal.util.PreferenceUtil!*/u",
        "com.junkfood.seal.ui.page.downloadv2.*!*/u",
        "com.junkfood.seal.ui.page.settings.*!*/u"
    ),
    [string]$Output = "frida-callgraph/frida-trace-output.log",
    [string]$Adb = "F:\AS_SDK\platform-tools\adb.exe",
    [switch]$Spawn
)

$ErrorActionPreference = "Stop"

$root = Split-Path -Parent (Split-Path -Parent $MyInvocation.MyCommand.Path)
$sitePackages = Join-Path $root ".frida-tools-python\Lib\site-packages"
$fridaTrace = Join-Path $root ".frida-tools-python\Scripts\frida-trace.exe"

if (!(Test-Path $fridaTrace)) {
    throw "frida-trace.exe not found. Install frida-tools into .frida-tools-python first."
}

$env:PYTHONPATH = $sitePackages

New-Item -ItemType Directory -Force -Path (Split-Path -Parent $Output) | Out-Null

$traceArgs = @("-U")
if ($Spawn) {
    $traceArgs += @("-f", $PackageName)
} else {
    $pidText = (& $Adb shell pidof $PackageName) -join ""
    $pidText = $pidText.Trim()
    if (!$pidText) {
        throw "App process not found. Start $PackageName first, or run with -Spawn."
    }
    $traceArgs += @("-p", $pidText)
}

foreach ($pattern in $JavaMethod) {
    $traceArgs += @("-j", $pattern)
}

$traceArgs += @("-o", $Output)

Write-Host "Starting frida-trace. Press Ctrl+C when app operations are finished."
Write-Host "Output: $Output"
& $fridaTrace @traceArgs
