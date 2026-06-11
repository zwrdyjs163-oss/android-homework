param(
    [Parameter(Mandatory = $true)]
    [string]$DecodedDir,

    [Parameter(Mandatory = $true)]
    [string]$EvidenceDir
)

$ErrorActionPreference = "Stop"

$smaliPath = Join-Path $DecodedDir "smali_classes15\com\junkfood\seal\MainActivity.smali"
$stringsPath = Join-Path $DecodedDir "res\values\strings.xml"

if (!(Test-Path $smaliPath)) {
    throw "MainActivity smali file not found: $smaliPath"
}

if (!(Test-Path $stringsPath)) {
    throw "strings.xml not found: $stringsPath"
}

New-Item -ItemType Directory -Force -Path $EvidenceDir | Out-Null

$utf8NoBom = New-Object System.Text.UTF8Encoding($false)

$smali = [System.IO.File]::ReadAllText($smaliPath, [System.Text.Encoding]::UTF8)
if ($smali -notmatch "APKTOOL_LAB") {
    $needle = "    invoke-super {p0, p1}, Landroidx/appcompat/app/AppCompatActivity;->onCreate(Landroid/os/Bundle;)V"
    $insert = @"
    invoke-super {p0, p1}, Landroidx/appcompat/app/AppCompatActivity;->onCreate(Landroid/os/Bundle;)V

    const-string v0, "APKTOOL_LAB"

    const-string v1, "Seal repackaged by apktool lab"

    invoke-static {v0, v1}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    move-result v0
"@
    if (!$smali.Contains($needle)) {
        throw "Patch point not found in MainActivity.onCreate"
    }
    $smali = $smali.Replace($needle, $insert)
    [System.IO.File]::WriteAllText($smaliPath, $smali, $utf8NoBom)
}

$strings = [System.IO.File]::ReadAllText($stringsPath, [System.Text.Encoding]::UTF8)
$strings = [regex]::Replace(
    $strings,
    '<string name="app_name">.*?</string>',
    '<string name="app_name">Seal APKTool Lab</string>',
    1
)
[System.IO.File]::WriteAllText($stringsPath, $strings, $utf8NoBom)

$summary = @(
    "APKTool lab patch summary",
    "DecodedDir=$DecodedDir",
    "SmaliPatch=$smaliPath",
    "InsertedLogTag=APKTOOL_LAB",
    "InsertedLogMessage=Seal repackaged by apktool lab",
    "ResourcePatch=$stringsPath",
    "AppName=Seal APKTool Lab"
)
[System.IO.File]::WriteAllLines((Join-Path $EvidenceDir "patch-summary.txt"), $summary, $utf8NoBom)

Write-Host "Patch completed."
