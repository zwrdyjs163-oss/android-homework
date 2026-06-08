# 基于 frida-trace 的方法调用图实践报告

## 一、实践目标

本次实践基于 `frida-trace` 对 Seal Android 应用进行动态方法追踪，记录应用运行时真实触发的方法调用，并通过 Python 脚本构建方法调用图。生成结果为 HTML 文件，可以直接用浏览器打开查看。

## 二、实现内容

| 文件 | 作用 |
| --- | --- |
| `frida-callgraph/run_frida_trace.ps1` | 封装 `frida-trace` 运行命令，默认追踪 Seal 中稳定的 Java 方法范围 |
| `frida-callgraph/frida_trace_callgraph.py` | 解析 `frida-trace` 日志，统计方法节点和调用边，生成 HTML 调用图 |
| `frida-callgraph/sample_frida_trace_output.log` | 样例 trace 日志，用于在没有模拟器时验证脚本 |
| `docs/frida-trace-callgraph-results/frida-trace-callgraph.html` | 可直接用浏览器打开的调用图结果 |
| `docs/frida-trace-callgraph-results/frida-trace-callgraph-summary.md` | 调用图摘要 |
| `docs/frida-trace-callgraph-results/frida-trace-callgraph.json` | 结构化统计结果 |

## 三、frida-trace 追踪范围

默认追踪以下范围：

```text
com.junkfood.seal.util.PreferenceUtil!*/u
com.junkfood.seal.ui.page.downloadv2.*!*/u
com.junkfood.seal.ui.page.settings.*!*/u
```

这些范围覆盖了偏好配置读取、下载页面逻辑、设置页面逻辑，比较稳定，不依赖真机复杂操作，适合作为课堂实践的动态分析对象。

## 四、运行命令

启动应用后运行：

```powershell
.\frida-callgraph\run_frida_trace.ps1
```

生成浏览器调用图：

```powershell
python .\frida-callgraph\frida_trace_callgraph.py `
  .\frida-callgraph\frida-trace-output.log `
  --out-dir .\docs\frida-trace-callgraph-results
```

如果没有模拟器，可用样例日志验证：

```powershell
python .\frida-callgraph\frida_trace_callgraph.py `
  .\frida-callgraph\sample_frida_trace_output.log `
  --out-dir .\docs\frida-trace-callgraph-results
```

## 五、样例验证结果

当前已使用样例 `frida-trace` 日志完成脚本验证：

| 指标 | 数量 |
| --- | ---: |
| 日志事件 | 10 |
| 方法节点 | 9 |
| 调用边 | 10 |
| 异常方法 | 0 |

HTML 结果包含：

- SVG 方法调用图
- 高频方法表
- 高频调用关系表
- 方法搜索框
- 调用关系搜索框

## 六、结论

本实践完成了从 `frida-trace` 动态日志到浏览器调用图的完整流程。该方法可以观察应用在真实操作过程中触发的方法链路，适合与 Androguard 静态分析结果对照：Androguard 说明“代码中有哪些类和可能关系”，`frida-trace` 说明“实际运行时哪些方法真的被调用”。
