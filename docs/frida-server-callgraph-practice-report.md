# Frida Server 动态调用关系实践报告

## 一、实践目标

本次实践使用 Frida Server 对 Seal Android 应用进行动态插桩，在模拟器中运行应用并记录实际发生的方法调用，再使用脚本生成调用关系摘要和调用图。

该实践与 Androguard 静态分析形成互补：

- Androguard：不运行应用，分析 APK 中声明的类、组件、权限、调用关系和依赖结构。
- Frida：运行应用，在真实页面启动和操作过程中记录运行时方法调用。

## 二、环境与版本

| 项目 | 结果 |
| --- | --- |
| 模拟器 | Pixel 8 |
| Android 版本 | Android 14 |
| API | 34 |
| ABI | x86_64 |
| root 权限 | 已确认可用 |
| Frida Python 包 | 17.10.0 |
| frida-tools | 已安装到项目本地 `.frida-tools-python` |
| frida-server | `frida-server-17.10.0-android-x86_64` |
| 被测应用 | `com.junkfood.seal.debug` |

Frida 版本验证：

```powershell
.\frida-callgraph\run_frida.ps1 --version
```

输出：

```text
17.10.0
```

## 三、仓库文件说明

| 文件 | 作用 |
| --- | --- |
| `frida-callgraph/README.md` | Frida Server 实践步骤说明 |
| `frida-callgraph/hook.js` | Frida Hook 脚本，用于记录 Seal 包内方法调用 |
| `frida-callgraph/generate_callgraph.py` | 将 Frida trace 日志转换为调用图和统计摘要 |
| `frida-callgraph/run_frida.ps1` | Windows 下运行本地 Frida 的包装脚本 |
| `frida-callgraph/setup_frida_server.ps1` | 解压、推送并启动 frida-server |
| `frida-callgraph/sample_trace_output.json` | 用于验证调用图脚本的样例 trace |
| `docs/frida-callgraph-results/callgraph-summary.md` | 本次真机模拟器运行生成的调用关系摘要 |
| `docs/frida-callgraph-results/callgraph.html` | 本次真机模拟器运行生成的 HTML 调用图结果 |
| `docs/frida-callgraph-results/frida-run-evidence.txt` | 本次运行过程和关键输出记录 |

## 四、操作流程

启动 Pixel 8 / API 34 模拟器后，确认设备在线：

```powershell
F:\AS_SDK\platform-tools\adb.exe devices -l
```

确认 root 权限可用：

```powershell
F:\AS_SDK\platform-tools\adb.exe root
F:\AS_SDK\platform-tools\adb.exe shell id
```

推送并启动 frida-server：

```powershell
.\frida-callgraph\setup_frida_server.ps1 .\frida-callgraph\frida-server-17.10.0-android-x86_64.xz
```

安装被测 APK：

```powershell
F:\AS_SDK\platform-tools\adb.exe install -r -d .\bundletool-output\Seal-from-aab-universal-debug.apk
```

启动应用：

```powershell
F:\AS_SDK\platform-tools\adb.exe shell am start -n com.junkfood.seal.debug/com.junkfood.seal.MainActivity
```

查看应用 PID：

```powershell
F:\AS_SDK\platform-tools\adb.exe shell pidof com.junkfood.seal.debug
```

运行 Frida Hook：

```powershell
.\frida-callgraph\run_frida.ps1 -U -p <PID> -l .\frida-callgraph\hook.js -o .\frida-callgraph\trace_output.json
```

操作应用页面后，停止 Frida，并生成调用图：

```powershell
python .\frida-callgraph\generate_callgraph.py .\frida-callgraph\trace_output.json
```

## 五、实测结果

本次已在 Pixel 8 / API 34 / x86_64 模拟器上完成实测。Frida 成功附加到 Seal 应用进程，并记录到应用运行时调用事件。

生成结果：

| 指标 | 数量 |
| --- | ---: |
| trace 事件数量 | 665 |
| 方法节点数量 | 94 |
| 调用边数量 | 104 |
| 异常事件方法数量 | 0 |

高频方法示例：

| 方法 | 次数 |
| --- | ---: |
| `util.PreferenceUtil.getBoolean(Ljava/lang/String;,Z)` | 31 |
| `ui.common.CompositionLocalsKt.getLocalFixedColorRoles()` | 14 |
| `util.PreferenceUtil.getString(Ljava/lang/String;,Ljava/lang/String;)` | 8 |
| `util.PreferenceUtil.getInt(Ljava/lang/String;,I)` | 7 |
| `ui.page.settings.directory.DownloadDirectoryPreferencesKt.isValidDirectory(Ljava/lang/String;)` | 6 |

高频调用关系示例：

| 调用方 | 被调用方 | 次数 |
| --- | --- | ---: |
| `ui.page.downloadv2.DownloadPageV2Kt.DownloadPageV2(...)` | `util.PreferenceUtil.getBoolean(Ljava/lang/String;,Z)` | 31 |
| `ui.page.downloadv2.DownloadPageV2Kt$DownloadPageImplV2$5$3$1$1$2.invoke(...)` | `ui.common.CompositionLocalsKt.getLocalFixedColorRoles()` | 12 |
| `ui.page.settings.directory.DownloadDirectoryPreferencesKt.DownloadDirectoryPreferences(...)` | `ui.page.settings.directory.DownloadDirectoryPreferencesKt.isValidDirectory(Ljava/lang/String;)` | 6 |

## 六、结论

通过本次 Frida Server 动态插桩，可以看到 Seal 应用在页面启动和设置页面相关操作中，会频繁读取偏好配置、Compose 主题状态、下载目录配置等逻辑。相比 Androguard 的静态调用关系，Frida 结果更能反映实际运行路径和用户操作触发的调用情况。

仓库中不提交 `frida-server` 二进制、压缩包和原始 trace 日志，只提交脚本、说明文档和本次运行摘要，避免仓库体积过大。
