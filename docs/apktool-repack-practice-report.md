# APKTool 安卓逆向分析与重打包实践简报

## 1. 实践目标

本次实践针对大作业报告中的被测应用 Seal，使用 APKTool 完成 APK 解包、smali 代码修改、资源修改、重打包、对齐、签名和安装验证。实践过程参考 `MT2026-L14-Android应用逆向分析技术-v1-r3-20260611.pdf` 中关于 APKTool、smali 和重打包流程的内容。

被测 APK：

```text
apktool-lab/input/seal-generic-universal-debug.apk
```

来源：

```text
app/build/outputs/apk/generic/debug/Seal-2.0.0-alpha.5-generic-universal-debug.apk
```

## 2. 环境与工具

| 工具 | 用途 | 本次使用情况 |
|---|---|---|
| Java | 运行 APKTool、keytool | `F:\java\jdk1.8.0` |
| APKTool | APK 解包和重打包 | `3.0.2` |
| zipalign | APK 对齐 | Android SDK build-tools 34.0.0 |
| apksigner | APK 签名与签名验证 | Android SDK build-tools 34.0.0 |
| adb | 安装和启动重打包 APK | `F:\AS_SDK\platform-tools\adb.exe` |

环境检查脚本：

```text
run-apktool-env-check.cmd
```

## 3. 脚本化实现

本次练习整理为一个小项目：

```text
apktool-lab/
```

主要脚本：

| 脚本 | 功能 |
|---|---|
| `apktool-lab/run-apktool-repack.cmd` | 自动完成解包、修改、重打包、对齐、签名、签名验证 |
| `apktool-lab/scripts/patch-decoded.ps1` | 修改 smali 代码和资源字符串 |
| `apktool-lab/run-install-verify.cmd` | 将重打包 APK 安装到模拟器并抓取 logcat 验证 |

脚本执行命令：

```bat
cd /d F:\study\Android\Seal-main
apktool-lab\run-apktool-repack.cmd
```

模拟器安装验证命令：

```bat
apktool-lab\run-install-verify.cmd
```

## 4. 解包过程

脚本使用 APKTool 执行：

```bat
java -jar tools\apktool\apktool.jar d -f apktool-lab\input\seal-generic-universal-debug.apk -o apktool-lab\work\decoded
```

解包后重点查看：

```text
apktool-lab/work/decoded/AndroidManifest.xml
apktool-lab/work/decoded/res/values/strings.xml
apktool-lab/work/decoded/smali_classes15/com/junkfood/seal/MainActivity.smali
```

Manifest 中确认被测包名和入口 Activity：

```text
package="com.junkfood.seal.debug"
MainActivity="com.junkfood.seal.MainActivity"
```

## 5. 修改内容

### 5.1 smali 代码修改

修改文件：

```text
apktool-lab/work/decoded/smali_classes15/com/junkfood/seal/MainActivity.smali
```

在 `MainActivity.onCreate` 中插入日志代码：

```smali
const-string v0, "APKTOOL_LAB"

const-string v1, "Seal repackaged by apktool lab"

invoke-static {v0, v1}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

move-result v0
```

该修改对应 PPT 中的 smali 修改示例，运行后可通过 logcat 验证。

### 5.2 资源修改

修改文件：

```text
apktool-lab/work/decoded/res/values/strings.xml
```

修改前：

```xml
<string name="app_name">Seal Debug</string>
```

修改后：

```xml
<string name="app_name">Seal APKTool Lab</string>
```

该修改用于从应用名称层面辅助确认重打包 APK 确实来自修改后的解包目录。

## 6. 重打包、对齐和签名

重打包：

```bat
java -jar tools\apktool\apktool.jar b apktool-lab\work\decoded -o apktool-lab\output\seal-repacked-unaligned.apk
```

对齐：

```bat
zipalign -f -p 4 apktool-lab\output\seal-repacked-unaligned.apk apktool-lab\output\seal-repacked-aligned.apk
```

签名：

```bat
apksigner sign --ks apktool-lab\keystore\apktool-lab-debug.keystore --out apktool-lab\output\seal-repacked-signed.apk apktool-lab\output\seal-repacked-aligned.apk
```

输出 APK：

```text
apktool-lab/output/seal-repacked-signed.apk
```

## 7. 验证结果

### 7.1 签名验证

签名验证文件：

```text
apktool-lab/evidence/apksigner-verify.txt
```

验证结果：

```text
Verifies
Verified using v2 scheme (APK Signature Scheme v2): true
Verified using v3 scheme (APK Signature Scheme v3): true
Number of signers: 1
```

说明重打包 APK 已完成签名，并通过 apksigner 验证。

### 7.2 安装与运行验证

当前模拟器：

```text
emulator-5554
```

安装结果：

```text
Success
```

启动命令：

```bat
adb shell am start -n com.junkfood.seal.debug/com.junkfood.seal.MainActivity
```

logcat 验证文件：

```text
apktool-lab/evidence/logcat-apktool-lab.txt
```

验证到的日志：

```text
D APKTOOL_LAB: Seal repackaged by apktool lab
```

说明 smali 修改已经在重打包后的 APK 中生效。

## 8. 结论

本次实践完成了 APKTool 逆向分析与重打包的完整流程：从 Seal APK 解包，定位 Manifest、资源和 smali 文件，修改 `MainActivity.onCreate` 的 smali 代码并修改应用名称资源，再通过 APKTool 重新打包，使用 zipalign 对齐，使用 apksigner 签名，最后安装到模拟器并通过 logcat 验证修改生效。

该流程已经脚本化，相关脚本和证据文件位于 `apktool-lab` 目录，可作为独立小项目提交到 GitHub。
