# Bundletool 安装与 AAB 生成 APK 实践简报

## 一、实践目标

本次实践在本地完成 bundletool 工具安装，并使用 Android 项目构建出的 `.aab` 文件，通过 bundletool 实际生成 APK 文件，最后在模拟器上验证生成的 APK 可以安装。

## 二、实践环境

| 项目 | 内容 |
| --- | --- |
| 项目 | Seal |
| 操作系统 | Windows |
| Java | JDK 21 |
| Android SDK | F:\AS_SDK |
| bundletool | 1.18.3 |
| 模拟器 | emulator-5554 |

## 三、工具安装

本地安装的 bundletool 文件位置：

```text
F:\study\Android\Seal-main\tools\bundletool-all-1.18.3.jar
```

版本验证命令：

```powershell
F:\Java\jdk-21.0.8\bin\java.exe -jar .\tools\bundletool-all-1.18.3.jar version
```

验证结果：

```text
1.18.3
```

## 四、构建 AAB 文件

执行 Gradle bundle 构建任务后，生成的 AAB 文件为：

```text
F:\study\Android\Seal-main\app\build\outputs\bundle\fdroidDebug\app-fdroid-debug.aab
```

文件大小：

```text
162,112,948 bytes
```

## 五、使用 bundletool 生成 APK

使用 bundletool 从 AAB 生成 APKS 文件：

```powershell
F:\Java\jdk-21.0.8\bin\java.exe -jar .\tools\bundletool-all-1.18.3.jar build-apks --bundle=.\app\build\outputs\bundle\fdroidDebug\app-fdroid-debug.aab --output=.\bundletool-output\seal-fdroid-debug-20260601.apks --mode=universal --overwrite
```

生成的 APKS 文件：

```text
F:\study\Android\Seal-main\bundletool-output\seal-fdroid-debug-20260601.apks
```

从 APKS 中解出的 universal APK 文件：

```text
F:\study\Android\Seal-main\bundletool-output\Seal-from-aab-universal-debug.apk
```

文件大小：

```text
163,801,571 bytes
```

## 六、APK 信息验证

使用 `aapt dump badging` 查看 APK 信息，结果如下：

```text
package: name='com.junkfood.seal.debug' versionCode='200000150' versionName='2.0.0-alpha.5-(F-Droid)-debug'
launchable-activity: name='com.junkfood.seal.MainActivity'
native-code: 'arm64-v8a' 'armeabi-v7a' 'x86' 'x86_64'
```

## 七、安装验证

模拟器中已有更高版本应用，因此第一次普通安装提示版本降级。随后使用允许降级参数安装：

```powershell
adb install -r -d .\bundletool-output\Seal-from-aab-universal-debug.apk
```

安装结果：

```text
Success
```

## 八、实践结论

本次实践完成了 bundletool 的本地安装、版本验证、AAB 文件构建、通过 bundletool 生成 APKS 文件、解出 APK 文件，并在安卓模拟器上完成安装验证。说明 bundletool 工具可以正常用于将 Android App Bundle 转换为可安装 APK。

