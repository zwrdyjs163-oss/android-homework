# 基于 TDD 的试卷成绩电子化示例应用实践简报

## 1. 实践目标

本次实践独立构建了一个 Android 示例应用 `tdd-score-app`，主题为“考试试卷成绩电子化”。应用用于录入学生姓名、选择题分数、填空题分数和简答题分数，自动计算总分、判断等级，并在页面中展示本次录入结果和历史记录。

实践重点不是先做复杂界面，而是按照 TDD 思路先拆出可测试的核心逻辑，再让 Android 页面调用这些逻辑。这样可以先保证成绩计算、边界校验、业务保存流程正确，再逐步扩展界面测试或持久化能力。

## 2. 项目结构

新增模块：

```text
tdd-score-app/
  src/main/java/com/example/tddscore/
    MainActivity.kt
    domain/
      ScoreInput.kt
      ScoreRecord.kt
      GradeLevel.kt
      ScoreCalculator.kt
    data/
      ScoreRepository.kt
      InMemoryScoreRepository.kt
      ScorePreferencesStore.kt
    service/
      ScoreEntryService.kt
  src/test/java/com/example/tddscore/
    domain/ScoreCalculatorTest.kt
    data/InMemoryScoreRepositoryTest.kt
    service/ScoreEntryServiceTest.kt
```

核心类说明：

- `ScoreCalculator`：负责分数范围校验、总分计算、等级判断。
- `ScoreEntryService`：负责处理一次成绩录入，修剪姓名、调用计算器、生成记录并保存。
- `ScoreRepository`：定义成绩保存和查询接口。
- `InMemoryScoreRepository`：提供内存版仓库，方便页面和测试使用。
- `MainActivity`：提供简单的成绩录入页面。

## 3. TDD 实践过程

第一步先写 `ScoreCalculatorTest`，明确选择题、填空题、简答题的分值上限分别为 40、30、30，并验证总分和等级映射。随后实现 `ScoreCalculator`，让测试通过。

第二步补 `InMemoryScoreRepositoryTest`，验证多条成绩记录能够按保存顺序查询。随后实现仓库类。

第三步补 `ScoreEntryServiceTest`，用手写测试替身 `RecordingScoreRepository` 模拟依赖对象，验证业务服务会计算等级、去掉姓名前后空格，并把结果保存到仓库。

第四步实现 `MainActivity`，页面包含学生姓名、三类题目分数输入框、计算保存按钮、结果文本和历史记录文本。界面只负责输入输出，核心判断仍在可测试的业务类中完成。

## 4. 已完成测试

本次小项目已完成本地 JUnit 单元测试：

- `ScoreCalculatorTest`
  - 验证三类题目分数汇总为总分。
  - 验证超过单项上限时抛出异常。
  - 验证优秀、良好、及格、不及格等级映射。
- `InMemoryScoreRepositoryTest`
  - 验证成绩记录保存后可以按顺序读取。
- `ScoreEntryServiceTest`
  - 验证提交成绩时会计算总分和等级。
  - 验证学生姓名会去除前后空格。
  - 验证成绩记录会保存到依赖的仓库对象。

## 5. 构建与验证结果

由于当前环境不能联网下载新的 Gradle 依赖，本模块第一版采用 Android 原生控件和 JUnit 测试，减少外部依赖。执行时使用本机已有 Gradle、Java 21、Android SDK 中的 `aapt2`。

已通过的验证：

```powershell
gradle :tdd-score-app:testDebugUnitTest
```

结果：`BUILD SUCCESSFUL`。

```powershell
gradle :tdd-score-app:assembleDebug
```

结果：`BUILD SUCCESSFUL`。

生成 APK 位置：

```text
tdd-score-app/build/outputs/apk/debug/tdd-score-app-debug.apk
```

## 6. 后续可扩展内容

后续如果网络或 Gradle 缓存恢复，可以继续补充：

- MockK 测试：对 `ScoreRepository` 使用 MockK 验证 `ScoreEntryService` 的依赖调用。
- Robolectric 测试：测试 `ScorePreferencesStore` 的 SharedPreferences 持久化逻辑。
- Espresso 测试：测试页面启动、输入成绩、点击按钮、结果文本显示。
- 数据持久化：把当前页面中的内存仓库替换为 SharedPreferences 或数据库仓库。

## 7. 小结

本次实践完成了一个独立的成绩电子化 Android 示例应用，并以 TDD 思路优先覆盖核心业务逻辑。当前版本已经能够本地构建、运行 JUnit 测试并生成 Debug APK，适合作为“基于 TDD 的考试试卷成绩电子化示例应用”的课堂实践材料。
