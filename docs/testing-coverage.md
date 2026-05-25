# Android 单元测试与覆盖率记录

## 任务目标

- 编译大作业目标 App。
- 编写/生成 JUnit 本地单元测试。
- 增加基于 MockK 的测试用例。
- 增加基于 Robolectric 的 Android 本地单元测试。
- 记录测试覆盖率变化。

## 已完成配置

- 在根目录 `build.gradle` 中加入 Kotlin Gradle 插件依赖，用于编译 Kotlin 测试代码。
- 在 `app/build.gradle` 中加入 `kotlin-android`、`jacoco` 插件。
- 在 `app/build.gradle` 中加入 JUnit、MockK、Robolectric 测试依赖。
- 在 `app/build.gradle` 中开启本地单元测试 Android 资源支持。
- 新增 `jacocoOfficialDebugUnitTestReport` 任务，用于生成 `officialDebug` 变体的本地单元测试覆盖率报告。
- 新增本机 `local.properties` 和 debug keystore 配置，方便 Android Studio/Gradle 本地编译。

## 新增测试用例

| 类型 | 文件 | 说明 |
| --- | --- | --- |
| JUnit | `app/src/test/java/com/coderpage/mine/app/tally/utils/TimeUtilsTest.java` | 测试月份天数计算，包括 30 天、31 天和闰年 2 月。 |
| JUnit | `app/src/test/java/com/coderpage/mine/app/tally/module/chart/data/CategoryDataTest.java` | 测试分类金额占比和格式化百分比。 |
| MockK | `app/src/test/kotlin/com/coderpage/mine/utils/SelectionBuilderMockKTest.kt` | Mock `SQLiteDatabase`，验证 `SelectionBuilder` 生成的表名、where 条件和参数会正确传入数据库更新方法。 |
| Robolectric | `app/src/test/java/com/coderpage/mine/utils/PreferencesUtilsRobolectricTest.java` | 使用 Robolectric 提供的 Android 运行环境，测试 `SharedPreferences` 中 UUID 和搜索历史的读写。 |

## 推荐运行命令

```powershell
$env:JAVA_HOME='F:\java\jdk1.8.0'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
.\gradlew.bat :app:assembleOfficialDebug
.\gradlew.bat :app:testOfficialDebugUnitTest
.\gradlew.bat :app:jacocoOfficialDebugUnitTestReport
```

## 覆盖率报告位置

运行覆盖率任务后可查看：

- HTML：`app/build/reports/jacoco/jacocoOfficialDebugUnitTestReport/html/index.html`
- XML：`app/build/reports/jacoco/jacocoOfficialDebugUnitTestReport/jacocoOfficialDebugUnitTestReport.xml`

## 覆盖率变化记录

| 阶段 | 测试情况 | 覆盖率记录 |
| --- | --- | --- |
| 初始状态 | `app` 模块只有模板 `ExampleUnitTest` 和空的 `TallyChartViewModelTest`，基本没有覆盖真实业务逻辑。 | 未生成有效业务覆盖率报告。 |
| 新增测试后 | 新增 9 个有效测试方法，覆盖时间工具、分类占比、数据库选择条件构造、SharedPreferences 读写。 | 等执行 `jacocoOfficialDebugUnitTestReport` 后，以报告中的 line/branch/class coverage 为准。 |

## 当前执行状态

本次在 Codex 环境中，Gradle 已经开始下载 `gradle-5.1.1-all.zip`，但会话网络仍被系统拦截并返回 `Permission denied: connect`，所以没有拿到实际测试结果和覆盖率数字。项目文件已经补齐，后续在 Android Studio 的 Terminal 或本机 PowerShell 中执行上面的命令即可生成报告。
