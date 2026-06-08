# Frida 动态调用图实践

本目录包含两套动态分析方式：

1. `frida + hook.js`：自定义 Hook，输出 JSON 事件，再生成调用图。
2. `frida-trace`：使用 Frida 官方 trace 工具追踪 Java 方法，再生成可浏览 HTML 调用图。

本次“基于 frida-trace，实现代码，构建方法调用图”的重点使用第二套方式。

## 环境要求

建议模拟器配置：

| 项目 | 建议 |
| --- | --- |
| AVD | Pixel 8 |
| Android | Android 14 / API 34 |
| System Image | Google APIs |
| ABI | x86_64 |
| root | 需要可用 |

高版本模拟器可能遇到 16K page size 兼容问题，API 34 更稳定。

## 启动 frida-server

将下载好的 `frida-server-17.10.0-android-x86_64.xz` 放到本目录，然后执行：

```powershell
.\frida-callgraph\setup_frida_server.ps1 .\frida-callgraph\frida-server-17.10.0-android-x86_64.xz
```

脚本会完成解压、推送、授权和启动。

## 安装并启动被测应用

```powershell
F:\AS_SDK\platform-tools\adb.exe install -r -d .\bundletool-output\Seal-from-aab-universal-debug.apk
F:\AS_SDK\platform-tools\adb.exe shell am start -n com.junkfood.seal.debug/com.junkfood.seal.MainActivity
```

确认进程存在：

```powershell
F:\AS_SDK\platform-tools\adb.exe shell pidof com.junkfood.seal.debug
```

## 使用 frida-trace 记录方法调用

运行：

```powershell
.\frida-callgraph\run_frida_trace.ps1
```

默认追踪以下 Java 方法范围：

```text
com.junkfood.seal.util.PreferenceUtil!*/u
com.junkfood.seal.ui.page.downloadv2.*!*/u
com.junkfood.seal.ui.page.settings.*!*/u
```

含义：

- `类名!方法名` 是 `frida-trace -j` 的 Java 方法查询格式。
- `*` 表示匹配多个类或多个方法。
- `/u` 表示跳过系统类，减少噪声。

运行后在模拟器里操作 Seal，例如启动页面、进入设置页、切换选项、输入下载链接。操作结束后按 `Ctrl+C` 停止。

默认日志输出：

```text
frida-callgraph/frida-trace-output.log
```

也可以手动指定方法范围：

```powershell
.\frida-callgraph\run_frida_trace.ps1 `
  -JavaMethod "com.junkfood.seal.util.PreferenceUtil!*/u","com.junkfood.seal.download.*!*/u"
```

## 生成浏览器调用图

```powershell
python .\frida-callgraph\frida_trace_callgraph.py `
  .\frida-callgraph\frida-trace-output.log `
  --out-dir .\docs\frida-trace-callgraph-results
```

输出文件：

| 文件 | 说明 |
| --- | --- |
| `frida-trace-callgraph.html` | 可直接用浏览器打开的调用图 |
| `frida-trace-callgraph-summary.md` | Markdown 摘要 |
| `frida-trace-callgraph.json` | 结构化统计结果 |

如果模拟器暂时不在线，可以先用样例日志验证：

```powershell
python .\frida-callgraph\frida_trace_callgraph.py `
  .\frida-callgraph\sample_frida_trace_output.log `
  --out-dir .\docs\frida-trace-callgraph-results
```

## 另一种 JSON Hook 方式

如果需要使用自定义 Hook：

```powershell
.\frida-callgraph\run_frida.ps1 -U -p <PID> -l .\frida-callgraph\hook.js -o .\frida-callgraph\trace_output.json
python .\frida-callgraph\generate_callgraph.py .\frida-callgraph\trace_output.json
```

该方式生成：

| 文件 | 说明 |
| --- | --- |
| `callgraph.dot` | Graphviz 调用图 |
| `callgraph.html` | HTML 调用结果 |
| `callgraph-summary.md` | Markdown 摘要 |

## 提交建议

建议提交脚本、README、报告和结果摘要，不提交以下文件：

- `frida-server`
- `frida-server-*.xz`
- `frida-trace-output.log`
- 大体积原始 trace 文件
