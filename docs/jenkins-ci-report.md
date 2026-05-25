# Jenkins 安卓应用持续集成实践简报

## 1. 实践目标

参考 `MT2026-L09-Android应用持续集成-v1-r4-20260525.pdf`，为当前 Android 项目配置 Jenkins 持续集成流程，完成命令行构建、Jenkins 构建任务配置、APK 产物归档方案，并记录关键配置。

## 2. 项目情况

- 项目路径：`F:\study\Android\Mine-master`
- App 模块：`app`
- 构建变体：`officialDebug`
- Gradle Wrapper：`5.1.1`
- Android Gradle Plugin：`3.4.0`
- JDK：`F:\java\jdk1.8.0`
- Android SDK：`F:\AS_SDK`
- Gradle 本地缓存：`.gradle-local`
- 构建输出目录：`.espresso-build/app`

该项目较旧，Gradle 5.1.1 与 Android Gradle Plugin 3.4.0 在 JDK 8 下最稳定。因此 Jenkins 中不要使用 JDK 11/17/21 直接构建本项目。

## 3. 本次新增 CI 配置

新增文件：

- `Jenkinsfile`：用于 Jenkins Pipeline 项目。
- `ci/jenkins-build.bat`：用于 Jenkins freestyle project 的 Windows 批处理构建步骤，也可被 `Jenkinsfile` 调用。

默认 CI 任务：

```text
:app:assembleOfficialDebug
```

该任务用于生成 officialDebug APK，适合作为本次 Jenkins 实践中的“至少一次成功构建”目标。

## 4. Jenkins 全局配置

进入：

```text
Manage Jenkins > Tools
```

建议配置：

- JDK 名称：`JDK8`
- JDK 路径：`F:\java\jdk1.8.0`
- Gradle：使用项目 Gradle Wrapper，不需要额外安装新版 Gradle。

进入：

```text
Manage Jenkins > System
```

添加全局环境变量：

```text
ANDROID_HOME=F:\AS_SDK
ANDROID_SDK_ROOT=F:\AS_SDK
JAVA_HOME=F:\java\jdk1.8.0
```

## 5. Freestyle Project 配置方式

新建任务：

```text
New Item > Freestyle project
```

Source Code Management：

- 选择 Git。
- Repository URL 填写自己的 GitHub 或 Gitee 仓库地址。
- Credentials 按仓库权限配置。
- Branch 建议填写：`*/master` 或 `*/main`，以实际仓库分支为准。

Build Steps：

选择：

```text
Execute Windows batch command
```

填写：

```bat
ci\jenkins-build.bat :app:assembleOfficialDebug
```

Post-build Actions：

选择：

```text
Archive the artifacts
```

Files to archive 填写：

```text
.espresso-build/app/outputs/apk/official/debug/*.apk
```

## 6. Pipeline Project 配置方式

如果创建 Pipeline 项目，可选择：

```text
Pipeline script from SCM
```

Script Path 填写：

```text
Jenkinsfile
```

该 `Jenkinsfile` 会执行：

```text
ci\jenkins-build.bat :app:assembleOfficialDebug
```

并自动归档 APK：

```text
.espresso-build/app/outputs/apk/official/debug/*.apk
```

## 7. 本地构建验证

在项目根目录执行与 Jenkins 相同的核心构建脚本：

```bat
ci\jenkins-build.bat :app:assembleOfficialDebug
```

实际验证结果：

```text
BUILD SUCCESSFUL
```

本次生成的 APK：

```text
F:\study\Android\Mine-master\.espresso-build\app\outputs\apk\official\debug\mine-[official]-0.6.2-debug.apk
```

## 8. 截图建议

学习通简报建议包含以下截图：

- Jenkins 首页或 New Item 创建任务页面。
- Jenkins 全局工具配置中的 JDK 8 配置。
- Jenkins 任务中的 Git 仓库配置。
- Jenkins Build Step 中的 `ci\jenkins-build.bat :app:assembleOfficialDebug`。
- Jenkins Console Output 中的 `BUILD SUCCESSFUL`。
- Jenkins 构建页面中的 Archived Artifacts / APK 归档结果。

## 9. 说明

本项目已有 Espresso 设备测试配置，但该测试依赖已启动的 Android 模拟器，适合作为扩展 CI 步骤，不建议作为第一次 Jenkins 成功构建的默认目标。

如果 Jenkins 机器已经启动了 Android Studio 官方 API 32 模拟器，可以手动执行：

```bat
ci\jenkins-build.bat :app:connectedOfficialDebugAndroidTest
```

如果只需要完成 L09 中“至少进行一次项目的成功构建”和“归档 APK”的要求，默认的 `:app:assembleOfficialDebug` 已经足够。
