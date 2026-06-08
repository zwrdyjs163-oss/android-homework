# Androguard 安装与 APK 静态分析实践简报

## 一、实践目标

本次实践完成 Androguard 的安装验证，并使用 Androguard 对 Seal APK 进行静态分析。分析内容包括 APK 基础信息、Manifest、组件、权限、签名、DEX 类信息、方法数量、调用关系和依赖关系。

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

同时补充了深入静态分析脚本：

```text
F:\study\Android\Seal-main\scripts\androguard_deep_static_analysis.py
```

该脚本会输出更完整的类、调用图和依赖关系分析结果，结果文件位于：

```text
F:\study\Android\Seal-main\docs\androguard-analysis-results\
```

## 七、深入静态分析结果

### 组件与权限

| 项目 | 数量 |
| --- | ---: |
| Activity | 4 |
| Service | 3 |
| Receiver | 2 |
| Provider | 2 |
| Exported 组件 | 4 |
| Intent Filter 组件 | 3 |
| 权限 | 10 |

主要组件包括：

- `com.junkfood.seal.MainActivity`
- `com.junkfood.seal.QuickDownloadActivity`
- `com.junkfood.seal.DownloadService`
- `com.junkfood.seal.NotificationActionReceiver`

### DEX、类与方法

| 指标 | 数量 |
| --- | ---: |
| DEX 文件 | 19 |
| 总类数量 | 36716 |
| 项目自定义类数量 | 3497 |
| 总方法数量 | 222906 |
| 项目自定义方法数量 | 16340 |
| native so 文件数量 | 36 |

项目自定义类主要集中在：

- `com.junkfood.seal.ui`
- `com.junkfood.seal.util`
- `com.junkfood.seal.download`
- `com.junkfood.seal.database`

### 调用关系

| 指标 | 数量 |
| --- | ---: |
| 调用图节点 | 17276 |
| 调用图边 | 53967 |

连接度较高的方法包括：

- `com.junkfood.seal.ui.page.download.DownloadPageKt.DownloadPage(...)`
- `com.junkfood.seal.ui.page.videolist.VideoListPageKt.VideoListPage(...)`
- `com.junkfood.seal.ui.page.download.DownloadSettingsDialogKt.DownloadSettingDialog(...)`
- `com.junkfood.seal.ui.page.downloadv2.configure.FormatPageKt.FormatPageImpl(...)`

这些方法多位于页面、下载配置和视频列表相关逻辑中，说明 Seal 的业务逻辑主要集中在 UI 页面组合、下载参数配置和偏好设置读取上。

### 依赖关系

项目代码调用较多的外部 API 包包括：

| 外部包 | 调用次数 |
| --- | ---: |
| `androidx.compose.runtime` | 19434 |
| `java.lang` | 6724 |
| `androidx.compose.ui` | 3086 |
| `kotlin.jvm` | 2196 |
| `androidx.compose.material3` | 1890 |

项目内部依赖关系较集中的方向包括：

| 调用方 | 被调用方 | 次数 |
| --- | --- | ---: |
| `com.junkfood.seal.ui.page` | `com.junkfood.seal.ui.component` | 389 |
| `com.junkfood.seal.ui.page` | `com.junkfood.seal.util.PreferenceUtil` | 191 |
| `com.junkfood.seal.util.DownloadUtil` | `com.junkfood.seal.util.DownloadUtil$DownloadPreferences` | 89 |
| `com.junkfood.seal.ui.page` | `com.junkfood.seal.database.objects` | 84 |
| `com.junkfood.seal.ui.page` | `com.junkfood.seal.ui.common` | 73 |

## 八、结果文件

| 文件 | 作用 |
| --- | --- |
| `docs/androguard-practice-report.md` | Androguard 实践简报 |
| `docs/androguard练习.docx` | 可提交的 Word 简报 |
| `docs/androguard-analysis-results/seal-androguard-static-analysis.md` | 完整静态分析报告 |
| `docs/androguard-analysis-results/seal-androguard-static-analysis.json` | 结构化分析结果 |
| `docs/androguard-analysis-results/androguard-screenshot-evidence.txt` | 截图/运行证据文字 |
| `docs/androguard-analysis-results/androguard-analysis-results.rar` | Androguard 分析结果压缩包 |

## 九、实践结论

本次实践完成了 Androguard 安装验证，并成功使用命令行和 Python API 对 APK 进行了静态分析。通过分析可以获得 APK 的基础身份信息、Manifest 内容、权限声明、签名方式、类数量、方法数量、调用图和依赖关系。静态分析结果显示，Seal 的代码规模较大，包含 19 个 DEX 文件，项目自定义类主要集中在 UI、工具类、下载和数据库模块；调用关系中页面逻辑、偏好配置和下载配置是主要分析重点。
