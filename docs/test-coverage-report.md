# 单元测试覆盖率报告

## 项目信息

- 项目：Mine 记账本 (Mine-master)
- 构建工具：Gradle 5.1.1 + AGP 3.4.0
- 测试框架：JUnit 4 + MockK 1.9.3 + Robolectric 3.8
- 覆盖率工具：JaCoCo 0.8.3
- 测试变体：officialDebug
- 报告日期：2026-05-18

## 构建命令

```bash
# 需先设置 JAVA_HOME 为 JDK 8（Gradle 5.1.1 不兼容 JDK 16+）
$env:JAVA_HOME = "F:\java\jdk1.8.0"

# 编译
.\gradlew.bat :app:assembleOfficialDebug

# 运行单元测试
.\gradlew.bat :app:testOfficialDebugUnitTest

# 生成覆盖率报告
.\gradlew.bat :app:jacocoOfficialDebugUnitTestReport
```

覆盖率报告路径：`app/build/reports/jacoco/jacocoOfficialDebugUnitTestReport/html/index.html`

## 覆盖率变化对比

| 指标 | 增加前 | 增加后 | 变化 |
|------|--------|--------|------|
| **指令覆盖率** | 1% (741/45,444) | 2% (1,081/45,444) | +340 指令 |
| **分支覆盖率** | 1% (35/3,195) | 1% (43/3,195) | +8 分支 |
| **行覆盖** | 194/11,312 | 272/11,312 | +78 行 |
| **方法覆盖** | 51/2,159 | 65/2,159 | +14 方法 |
| **类覆盖** | 15/319 | 17/319 | +2 类 |

## 测试用例统计

### 增加前（原有测试）

| 文件 | 类型 | 测试数 | 说明 |
|------|------|--------|------|
| ExampleUnitTest.java | JUnit | 1 | 示例测试 |
| CategoryDataTest.java | JUnit | 2 | 百分比计算 |
| TimeUtilsTest.java | JUnit | 3 | 月份天数计算 |
| PreferencesUtilsRobolectricTest.java | Robolectric | 2 | SharedPreferences 读写 |
| SelectionBuilderMockKTest.kt | MockK+Robolectric | 2 | SelectionBuilder update/where |
| **合计** | | **10** | |

### 新增测试

| 文件 | 类型 | 测试数 | 说明 |
|------|------|--------|------|
| SelectionBuilderTest.java | JUnit+Robolectric | 15 | reset/where/table/map/getSelection/query/delete/toString |
| TallyUtilsTest.java | JUnit | 8 | formatDisplayMoney 各种边界 |
| DateUtilsRobolectricTest.java | Robolectric | 8 | monthDateRange/dayDateRange/yearDateRange |
| AsyncTaskExecutorMockKTest.kt | MockK | 6 | executor/submit/corePoolSize/timeout |
| DefaultVersionComparatorMockKTest.kt | MockK+Robolectric | 4 | 版本比较/包未找到 |
| **合计** | | **41** | |

### 新增后总计

原有 10 + 新增 41 = **51 个测试用例**，全部通过。

## 新增测试说明

### 1. JUnit 纯逻辑测试

**TallyUtilsTest.java** — 测试 `TallyUtils.formatDisplayMoney()` 方法：
- 正数、零、负数格式化
- 大额、极小值处理
- 小数截断与四舍五入行为
- 整数值的格式化

### 2. JUnit + Robolectric 测试

**SelectionBuilderTest.java** — 全面测试 `SelectionBuilder` 类：
- `reset()` 清理状态
- `where()` 单条件/多条件组合/空条件/null条件/异常场景
- `table()` / `table(params)` 设置表名
- `map()` / `mapToTable()` 列映射
- `getSelection()` / `getSelectionArgs()` 获取构建结果
- `query()`/`delete()`/`update()` 无表名时抛异常
- `toString()` 输出格式

**DateUtilsRobolectricTest.java** — 测试 `DateUtils` 日期范围方法：
- `monthDateRange()` 一月/闰年/非闰年/十二月
- `dayDateRange()` 起止时间/时长验证
- `yearDateRange()` 年度范围/起止顺序
- 常量值验证

### 3. MockK 测试

**AsyncTaskExecutorMockKTest.kt** — 测试 `AsyncTaskExecutor` 线程池：
- `executor()` 返回类型与配置
- `submit(Callable/Runnable)` 返回 Future
- 核心线程数 >= 2
- 核心线程超时回收

**DefaultVersionComparatorMockKTest.kt** — 测试 `DefaultVersionComparator` 版本比较：
- 当前版本低于最新 → 返回 true
- 当前版本等于/高于最新 → 返回 false
- 包名未找到 → 返回 false（异常处理）

### 4. MockK + Robolectric 混合测试

原有的 `SelectionBuilderMockKTest.kt` 和新增的 `DefaultVersionComparatorMockKTest.kt` 使用了 MockK + Robolectric 组合：
- MockK 负责 mock 依赖（SQLiteDatabase、PackageManager、ApkModel）
- Robolectric 提供 Android 框架支持（TextUtils.isEmpty、ContentValues 等）

## 环境注意事项

- 本项目 Gradle 5.1.1 **不兼容 JDK 16+**，执行命令前需临时设置 `JAVA_HOME` 为 JDK 8
- 命令行中临时设置 `$env:JAVA_HOME` 仅对当前会话有效，不会修改系统环境变量
- MockK 1.9.3 中 `eq`/`match`/`same` 是 `MockKMatcherScope` 的扩展函数，仅在 `every{}`/`verify{}` 块内可用，不可作为顶层 import
- `local.properties` 中 `keyStore` 路径已从 `debug.keystore` 修正为 `../debug.keystore`（相对于 app 模块）
