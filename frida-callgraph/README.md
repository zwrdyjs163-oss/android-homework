# Frida Server 动态调用关系实践

## 1. 实践目标

本实践使用 Frida 对 Seal Android 应用进行动态插桩，记录应用运行时的方法调用轨迹，并将 trace 日志转换为调用关系图。该任务与 Androguard 静态分析互补：Androguard 看到的是 APK 中“可能存在”的类和调用关系，Frida 记录的是实际操作过程中“真实发生”的方法调用。

## 2. 环境要求

建议模拟器配置：

| 项目 | 要求 |
| --- | --- |
| AVD | Pixel 8 |
| API | Android 14 / API 34 |
| System Image | Google APIs |
| ABI | x86_64 |
| Root | 需要 `adb shell su` 可用 |

说明：高版本模拟器可能遇到 16K page size 兼容问题，API 34 更稳。Google Play 镜像通常没有 root，建议使用 Google APIs 镜像。

## 3. 本地 Frida 工具

本机 Python 包版本已经确认：

```powershell
python -c "import frida; print(frida.__version__)"
```

输出：

```text
17.10.0
```

项目本地安装了 `frida-tools`，使用包装脚本查看版本：

```powershell
.\frida-callgraph\run_frida.ps1 --version
```

输出应为：

```text
17.10.0
```

## 4. 下载 frida-server

根据 Frida 版本和模拟器 ABI，下载：

```text
frida-server-17.10.0-android-x86_64.xz
```

下载地址：

```text
https://github.com/frida/frida/releases/download/17.10.0/frida-server-17.10.0-android-x86_64.xz
```

将下载的 `.xz` 文件放到：

```text
F:\study\Android\Seal-main\frida-callgraph\
```

然后执行：

```powershell
.\frida-callgraph\setup_frida_server.ps1 .\frida-callgraph\frida-server-17.10.0-android-x86_64.xz
```

脚本会完成：

1. 解压 `.xz`
2. 推送到 `/data/local/tmp/frida-server`
3. 设置执行权限
4. 使用 root 启动 frida-server

## 5. 安装并启动被测应用

安装 APK：

```powershell
F:\AS_SDK\platform-tools\adb.exe install -r -d .\bundletool-output\Seal-from-aab-universal-debug.apk
```

启动应用后查看 PID：

```powershell
F:\AS_SDK\platform-tools\adb.exe shell pidof com.junkfood.seal.debug
```

## 6. 运行 Frida Hook

假设 PID 为 `5305`，执行：

```powershell
.\frida-callgraph\run_frida.ps1 -U -p 5305 -l .\frida-callgraph\hook.js -o .\frida-callgraph\trace_output.json
```

运行后在模拟器里操作 Seal，例如打开页面、输入下载链接、点击下载按钮。操作结束后按 `Ctrl+C` 停止 Frida。

## 7. 生成调用图

```powershell
python .\frida-callgraph\generate_callgraph.py .\frida-callgraph\trace_output.json
```

输出文件：

| 文件 | 说明 |
| --- | --- |
| `callgraph.dot` | Graphviz 调用图 |
| `callgraph.html` | 可直接打开查看的 HTML 调用图 |
| `callgraph-summary.md` | 调用统计摘要 |

## 8. 提交内容建议

学习通截图可以截以下内容：

1. `frida --version` 输出 `17.10.0`
2. `adb shell pidof com.junkfood.seal.debug` 输出 PID
3. Frida 运行时产生 trace 日志
4. `generate_callgraph.py` 生成 `callgraph.html` 和摘要

仓库中建议提交：

- `frida-callgraph/README.md`
- `frida-callgraph/hook.js`
- `frida-callgraph/generate_callgraph.py`
- `frida-callgraph/setup_frida_server.ps1`
- `frida-callgraph/run_frida.ps1`

不建议提交 `frida-server` 二进制、`.xz` 压缩包和大体积 trace 文件。
