# 基于 Appium 的安卓应用性能测试实践简报

## 一、实践目标

本次实践选取开源安卓项目 Seal 作为被测应用，围绕安卓应用常见性能指标进行测试：启动耗时、内存占用、CPU 状态和界面渲染表现。测试目标不是做完整压力测试，而是完成一次可复现的课堂级 Appium 性能测试流程。

## 二、测试环境

| 项目 | 内容 |
| --- | --- |
| 被测应用 | Seal |
| 调试包 | Seal-2.0.0-alpha.5-fdroid-x86_64-debug.apk |
| 包名 | com.junkfood.seal.debug |
| 启动 Activity | com.junkfood.seal.MainActivity |
| 测试设备 | Android Emulator |
| 设备型号 | sdk_gphone16k_x86_64 |
| CPU ABI | x86_64 |
| Android 版本 | 17 |
| 自动化工具 | Appium 2 + UiAutomator2 |
| 辅助采集 | adb dumpsys / am start |

## 三、测试方案

1. 构建 debug APK，并安装到安卓模拟器。
2. 使用 Appium UiAutomator2 启动应用，保持自动化会话。
3. 通过 Appium Performance API 采集 `memoryinfo`、`cpuinfo`、`batteryinfo`、`networkinfo` 等指标。
4. 使用 `adb shell am start -W` 辅助采集冷启动耗时。
5. 使用 `dumpsys meminfo` 和 `dumpsys gfxinfo` 辅助记录内存与渲染状态，便于和 Appium 采集结果交叉验证。

仓库中已补充脚本：

| 文件 | 作用 |
| --- | --- |
| `scripts/appium_performance_test.py` | 连接 Appium Server，启动应用并采集性能数据 |
| `scripts/collect_adb_performance.ps1` | 采集启动、内存、渲染、CPU 原始数据 |

运行 Appium 脚本前需要先启动 Appium Server：

```powershell
$env:APPIUM_HOME = (Resolve-Path .\.appium-home).Path
.\.appium-tools\node_modules\.bin\appium.cmd --address 127.0.0.1 --port 4723
```

然后运行：

```powershell
python .\scripts\appium_performance_test.py
```

## 四、实践数据

本次在模拟器上完成安装和启动验证，应用可以正常启动。Appium 脚本输出文件为 `docs/appium-performance-results/appium-performance-20260530-134043.json`。一次冷启动采集结果如下：

| 指标 | 结果 |
| --- | ---: |
| LaunchState | COLD |
| TotalTime | 2887 ms |
| WaitTime | 2889 ms |

内存采集结果如下：

| 指标 | 结果 |
| --- | ---: |
| totalPss | 144906 KB |
| totalRss | 262036 KB |
| totalPrivateDirty | 116284 KB |
| nativePss | 13620 KB |
| dalvikPss | 6780 KB |
| nativeHeapAllocatedSize | 20973 KB |

Appium Performance API 返回支持的指标类型如下：

| 类型 | 说明 |
| --- | --- |
| cpuinfo | CPU 使用信息 |
| memoryinfo | 内存使用信息 |
| batteryinfo | 电量信息 |
| networkinfo | 网络收发信息 |

界面渲染采集结果如下：

| 指标 | 结果 |
| --- | ---: |
| Total frames rendered | 4 |
| Janky frames | 4 |
| Janky frames ratio | 100.00% |
| 50th percentile | 2050 ms |
| GPU 50th percentile | 12 ms |

CPU 采集时应用进入稳定页面后占用较低，系统总 CPU 记录为：

| 指标 | 结果 |
| --- | ---: |
| TOTAL CPU | 47% |
| user | 9.6% |
| kernel | 5.3% |
| iowait | 32% |

Appium `cpuinfo` 在稳定页面返回 `user=0`、`kernel=0`，说明采样瞬间应用 CPU 占用较低。

## 五、结果分析

本次 Appium 测试中应用冷启动耗时约 2.9 秒，属于可以接受但仍可继续优化的启动表现。由于测试包为 debug 版本，并且首次启动需要初始化数据库、资源和调试环境，因此启动耗时会高于正式 release 包。

内存方面，应用启动后 totalPss 约 145 MB，totalRss 约 262 MB。对于包含 Kotlin、Compose、Room、网络与下载相关能力的应用来说，该结果可以作为后续优化对比的基线。

渲染方面，`gfxinfo` 只记录到冷启动初期的 4 帧，并且 4 帧都被标记为 jank。这更像是启动阶段初始化造成的卡顿，不代表应用稳定使用过程中的持续帧率。后续可以在 Appium 中增加页面滚动、按钮点击、输入框输入等操作，再采集操作后的帧数据。

## 六、结论

本次实践完成了安卓模拟器启动、APK 安装、应用冷启动、内存占用、CPU 状态和渲染信息采集。仓库已提供 Appium 自动化性能测试脚本，可以在 Appium Server 启动后复现实验流程，并将 JSON 结果保存到 `docs/appium-performance-results` 目录。

后续如果需要进一步完善，可以增加 3 次以上重复采样，计算平均值；也可以将测试包换成 release 版本，用同一脚本对比 debug 和 release 的性能差异。
