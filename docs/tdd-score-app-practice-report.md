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

本次小项目已完成 JUnit、MockK、Robolectric 和 Espresso 四类测试：

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
- `ScoreEntryServiceMockKTest`
  - 使用 MockK 模拟 `ScoreRepository`。
  - 验证业务服务调用依赖对象的 `save` 和 `findAll` 方法。
  - 验证有依赖对象的业务逻辑可以脱离真实仓库单独测试。
- `ScorePreferencesStoreRobolectricTest`
  - 使用 Robolectric 提供 Android `Context`。
  - 验证 `SharedPreferences` 中成绩记录的保存和读取。
  - 覆盖不需要真机也能测试的 Android 相关逻辑。
- `MainActivityEspressoTest`
  - 启动成绩录入页面。
  - 输入学生姓名和三类题目分数。
  - 点击“计算并保存”按钮。
  - 验证结果文本和历史记录文本显示正确。

## 5. 构建与验证结果

执行时使用本机 Java 21、Gradle、Android SDK 中的 `aapt2`。为了避免中文路径和 Gradle 临时目录导致测试子进程异常，项目根目录提供了 CMD 脚本统一设置运行环境。

本地单元测试、MockK 测试、Robolectric 测试和 APK 构建：

```powershell
run-tdd-score-app.cmd
```

该脚本会执行：

```text
:tdd-score-app:testDebugUnitTest
:tdd-score-app:assembleDebug
```

Espresso 界面测试需要先启动模拟器，再单独执行：

```powershell
run-tdd-score-app-espresso.cmd
```

该脚本会执行：

```text
:tdd-score-app:connectedDebugAndroidTest
```

生成 APK 位置：

```text
tdd-score-app/build/outputs/apk/debug/tdd-score-app-debug.apk
```

## 6. 后续可扩展内容

后续可以继续补充：

- 数据持久化：把当前页面中的内存仓库替换为 SharedPreferences 或数据库仓库。
- 更多业务规则：例如缺考、补考、成绩复核、不同试卷满分规则。
- 更多界面流程：例如成绩列表页、成绩详情页、删除或修改成绩记录。
- CI 配置：默认跑 `assembleDebug` 和 `testDebugUnitTest`，模拟器相关的 `connectedDebugAndroidTest` 单独执行。

## 7. 小结

本次实践完成了一个独立的成绩电子化 Android 示例应用，并以 TDD 思路优先覆盖核心业务逻辑。当前版本包含 JUnit、MockK、Robolectric 和 Espresso 测试，能够覆盖纯业务逻辑、有依赖对象的业务服务、Android 存储逻辑以及简单界面交互，适合作为“基于 TDD 的考试试卷成绩电子化示例应用”的课堂实践材料。
