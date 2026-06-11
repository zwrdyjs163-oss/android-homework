# APKTool reverse and repack lab

This small project performs APKTool-based reverse analysis and repackaging for the tested Seal APK.

## Input

Put the tested APK here:

```text
apktool-lab/input/seal-generic-universal-debug.apk
```

The current tested APK was exported from the Seal project:

```text
app/build/outputs/apk/generic/debug/Seal-2.0.0-alpha.5-generic-universal-debug.apk
```

## Scripts

Run the full reverse, patch, rebuild, align, sign, and signature-verify process:

```bat
cd /d F:\study\Android\Seal-main
apktool-lab\run-apktool-repack.cmd
```

Optional emulator install and runtime log verification:

```bat
apktool-lab\run-install-verify.cmd
```

## What is modified

The script modifies two decoded APK files:

1. `smali_classes15/com/junkfood/seal/MainActivity.smali`
   - Inserts `Log.d("APKTOOL_LAB", "Seal repackaged by apktool lab")` into `MainActivity.onCreate`.
2. `res/values/strings.xml`
   - Changes `app_name` from `Seal Debug` to `Seal APKTool Lab`.

## Outputs

Generated outputs are written to:

```text
apktool-lab/output/
apktool-lab/evidence/
```

Important files:

```text
apktool-lab/output/seal-repacked-signed.apk
apktool-lab/evidence/patch-summary.txt
apktool-lab/evidence/apksigner-verify.txt
apktool-lab/evidence/logcat-apktool-lab.txt
```
