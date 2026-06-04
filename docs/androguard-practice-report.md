# Androguard 安装与 APK 静态分析实践简报

## 一、实践目标

本次实践完成 Androguard 的安装验证，并使用 Androguard 对安卓 APK 进行静态分析，查看 APK 的包名、版本、Manifest、权限和签名信息。

## 二、实践环境

| 项目 | 内容 |
| --- | --- |
| 工具 | Androguard |
| 版本 | 4.1.3 |
| 安装位置 | F:\anaconda\Scripts\androguard.exe |
| 被分析 APK | F:\study\Android\Seal-main\bundletool-output\Seal-from-aab-universal-debug.apk |
| APK 包名 | com.junkfood.seal.debug |

## 三、安装验证

验证命令：

```powershell
androguard --version
```

输出结果：

```text
androguard, version 4.1.3
```

同时也验证了 Python 包可以导入：

```powershell
python -c "import androguard; print(androguard.__version__)"
```

输出结果：

```text
4.1.3
```

## 四、命令行使用

查看 Androguard 支持的命令：

```powershell
androguard --help
```

常用命令如下：

| 命令 | 作用 |
| --- | --- |
| `androguard apkid <apk>` | 查看 APK 包名、versionCode、versionName |
| `androguard axml <apk>` | 解析 AndroidManifest.xml |
| `androguard sign <apk>` | 查看 APK 签名证书信息 |
| `androguard arsc <apk>` | 解析资源表 resources.arsc |
| `androguard analyze <apk>` | 进入交互式分析环境 |

## 五、实际分析结果

执行包信息分析：

```powershell
androguard apkid .\bundletool-output\Seal-from-aab-universal-debug.apk
```

输出结果：

```text
{
  ".\bundletool-output\Seal-from-aab-universal-debug.apk": [
    "com.junkfood.seal.debug",
    "200000150",
    "2.0.0-alpha.5-(F-Droid)-debug"
  ]
}
```

执行签名分析：

```powershell
androguard sign .\bundletool-output\Seal-from-aab-universal-debug.apk
```

输出结果：

```text
Seal-from-aab-universal-debug.apk, package: 'com.junkfood.seal.debug'
Is signed v1: False
Is signed v2: True
Is signed v3: True
Found 1 unique certificates
sha1 0885c67b07de094f3195913e674e3853d623ce80
```

执行 Manifest 分析：

```powershell
androguard axml .\bundletool-output\Seal-from-aab-universal-debug.apk
```

部分结果：

```text
package="com.junkfood.seal.debug"
android:versionCode="200000150"
android:versionName="2.0.0-alpha.5-(F-Droid)-debug"
android:minSdkVersion="24"
android:targetSdkVersion="35"
android:name="com.junkfood.seal.MainActivity"
```

## 六、Python API 使用

仓库中补充了一个简单脚本：

```text
F:\study\Android\Seal-main\scripts\androguard_apk_summary.py
```

运行命令：

```powershell
python .\scripts\androguard_apk_summary.py
```

脚本会输出 APK 包名、版本、主 Activity、SDK 版本和权限列表，适合截图作为实践结果。

## 七、实践结论

本次实践完成了 Androguard 安装验证，并成功使用命令行和 Python API 对 APK 进行了静态分析。通过分析可以获得 APK 的基础身份信息、Manifest 内容、权限声明和签名方式，说明 Androguard 可以正常用于安卓应用逆向与安全分析入门实践。

