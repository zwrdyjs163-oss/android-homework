# Frida Server 动态调用关系实践说明

## 一、实践目标

本次实践基于 Frida Server 对 Seal 应用进行动态插桩，记录应用实际运行过程中的方法调用，并使用脚本将 trace 日志转换为调用关系摘要和调用图。

该实践与 Androguard 静态分析形成互补：

- Androguard：不运行应用，分析 APK 内声明的类、组件、权限和静态调用结构。
- Frida：运行应用，在真实操作过程中记录实际发生的方法调用。

## 二、当前完成情况

| 项目 | 状态 |
| --- | --- |
| Python frida 包 | 已安装，版本 `17.10.0` |
| frida-tools | 已安装到项目本地 `.frida-tools-python` |
| frida 命令包装脚本 | 已完成 |
| frida-server x86_64 | 已下载到本地，未提交进仓库 |
| hook.js | 已完成 |
| trace 转调用图脚本 | 已完成 |
| 样例 trace 验证 | 已完成 |
| 真模拟器运行 | 待 Pixel 8 / API 34 / Google APIs root 模拟器启动后执行 |

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
| `frida-callgraph/README.md` | 完整操作步骤 |
| `frida-callgraph/hook.js` | Frida Hook 脚本，用于记录方法进入/退出 |
| `frida-callgraph/generate_callgraph.py` | 将 trace 日志转换为调用图和摘要 |
| `frida-callgraph/run_frida.ps1` | Windows 下运行 Frida 的包装脚本 |
| `frida-callgraph/setup_frida_server.ps1` | 解压、推送并启动 frida-server |
| `frida-callgraph/sample_trace_output.json` | 用于验证调用图脚本的样例 trace |

## 四、模拟器要求

建议使用：

```text
Pixel 8
API 34
Google APIs
x86_64
```

原因：

1. Google APIs 镜像可以使用 root，更适合启动 frida-server。
2. API 34 对当前项目更稳定。
3. 更高版本可能遇到 16K page size 兼容问题。

## 五、操作流程

启动模拟器后，确认设备在线：

```powershell
F:\AS_SDK\platform-tools\adb.exe devices -l
```

推送并启动 frida-server：

```powershell
.\frida-callgraph\setup_frida_server.ps1 .\frida-callgraph\frida-server-17.10.0-android-x86_64.xz
```

安装被测 APK：

```powershell
F:\AS_SDK\platform-tools\adb.exe install -r -d .\bundletool-output\Seal-from-aab-universal-debug.apk
```

查看应用 PID：

```powershell
F:\AS_SDK\platform-tools\adb.exe shell pidof com.junkfood.seal.debug
```

运行 Frida Hook：

```powershell
.\frida-callgraph\run_frida.ps1 -U -p <PID> -l .\frida-callgraph\hook.js -o .\frida-callgraph\trace_output.json
```

在模拟器中操作应用，例如进入主页、输入链接、点击下载。操作结束后按 `Ctrl+C` 停止 Frida。

生成调用图：

```powershell
python .\frida-callgraph\generate_callgraph.py .\frida-callgraph\trace_output.json
```

生成结果：

| 文件 | 说明 |
| --- | --- |
| `frida-callgraph/callgraph.dot` | Graphviz 调用图 |
| `frida-callgraph/callgraph.html` | HTML 调用图摘要 |
| `frida-callgraph/callgraph-summary.md` | Markdown 调用统计 |

## 六、样例验证结果

已使用 `sample_trace_output.json` 验证调用图脚本：

```text
events=7 methods=3 edges=3
```

说明 `generate_callgraph.py` 可以正常解析 Frida trace，并生成调用图相关文件。

## 七、提交说明

仓库中提交脚本和说明文件，不提交以下运行产物：

- `frida-server`
- `frida-server-*.xz`
- `trace_output.json`
- `callgraph.dot`
- `callgraph.html`
- `callgraph-summary.md`

这些文件会根据实际模拟器运行结果重新生成。
