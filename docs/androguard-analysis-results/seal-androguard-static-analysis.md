# 基于 Androguard 的 APK 静态分析报告

## 一、分析对象

- APK 文件：`F:\study\Android\Seal-main\bundletool-output\Seal-from-aab-universal-debug.apk`
- 应用名称：`Seal Debug`
- 包名：`com.junkfood.seal.debug`
- 版本：`2.0.0-alpha.5-(F-Droid)-debug`
- versionCode：`200000150`
- minSdk / targetSdk：`24` / `35`
- 主 Activity：`com.junkfood.seal.MainActivity`

## 二、Manifest 与组件信息

- application 类：`com.junkfood.seal.App`
- debuggable：`True`
- allowBackup：`True`
- exported 组件数量：`4`

| 类型 | 数量 |
| --- | ---: |
| Activity | 4 |
| Service | 3 |
| Receiver | 2 |
| Provider | 2 |

### Activity 列表
- `com.junkfood.seal.CrashReportActivity`
- `com.junkfood.seal.MainActivity`
- `androidx.compose.ui.tooling.PreviewActivity`
- `com.junkfood.seal.QuickDownloadActivity`

### Service 列表
- `com.junkfood.seal.DownloadService`
- `androidx.appcompat.app.AppLocalesMetadataHolderService`
- `androidx.room.MultiInstanceInvalidationService`

### Receiver 列表
- `com.junkfood.seal.NotificationActionReceiver`
- `androidx.profileinstaller.ProfileInstallReceiver`

### Provider 列表
- `androidx.core.content.FileProvider`
- `androidx.startup.InitializationProvider`

### Exported 组件

| 类型 | 名称 | exported | intent-filter |
| --- | --- | --- | --- |
| `activity` | `com.junkfood.seal.QuickDownloadActivity` | `true` | `True` |
| `activity` | `com.junkfood.seal.MainActivity` | `true` | `True` |
| `activity` | `androidx.compose.ui.tooling.PreviewActivity` | `true` | `False` |
| `receiver` | `androidx.profileinstaller.ProfileInstallReceiver` | `true` | `True` |

### Intent Filter 明细

- 含 Intent Filter 的组件数量：`3`

| 类型 | 组件 | action | category | data |
| --- | --- | --- | --- | --- |
| `activity` | `com.junkfood.seal.MainActivity` | `android.intent.action.MAIN, android.intent.action.SEND, android.intent.action.VIEW` | `android.intent.category.LAUNCHER, android.intent.category.DEFAULT, android.intent.category.BROWSABLE` | `{'mimeType': 'text/plain'}, {'scheme': 'http'}, {'scheme': 'https'}, {'mimeType': 'video/*'}, {'mimeType': 'audio/*'}` |
| `activity` | `com.junkfood.seal.QuickDownloadActivity` | `android.intent.action.SEND, android.intent.action.VIEW` | `android.intent.category.DEFAULT, android.intent.category.BROWSABLE` | `{'mimeType': 'text/plain'}, {'scheme': 'http'}, {'scheme': 'https'}, {'mimeType': 'video/*'}, {'mimeType': 'audio/*'}` |
| `receiver` | `androidx.profileinstaller.ProfileInstallReceiver` | `androidx.profileinstaller.action.INSTALL_PROFILE, androidx.profileinstaller.action.SKIP_FILE, androidx.profileinstaller.action.SAVE_PROFILE, androidx.profileinstaller.action.BENCHMARK_OPERATION` | `` | `` |
## 三、权限信息

- 权限数量：`10`

- `android.permission.ACCESS_NETWORK_STATE`
- `android.permission.FOREGROUND_SERVICE`
- `android.permission.FOREGROUND_SERVICE_SPECIAL_USE`
- `android.permission.INTERNET`
- `android.permission.MANAGE_EXTERNAL_STORAGE`
- `android.permission.POST_NOTIFICATIONS`
- `android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS`
- `android.permission.REQUEST_INSTALL_PACKAGES`
- `android.permission.WRITE_EXTERNAL_STORAGE`
- `com.junkfood.seal.debug.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION`

## 四、文件与 DEX 信息

| 指标 | 数值 |
| --- | ---: |
| APK 内文件数量 | 999 |
| DEX 文件数量 | 19 |
| 总类数量 | 36716 |
| 项目自定义类数量 | 3497 |
| 第三方或系统类数量 | 33219 |
| 总方法数量 | 222906 |
| 项目自定义方法数量 | 16340 |
| native so 文件数量 | 36 |

### DEX 分布

| DEX 序号 | 类数量 | 方法数量 | 字段数量 | 字符串数量 |
| ---: | ---: | ---: | ---: | ---: |
| 1 | 17086 | 64245 | 29785 | 120226 |
| 2 | 326 | 1659 | 775 | 2559 |
| 3 | 150 | 887 | 395 | 1733 |
| 4 | 314 | 1911 | 867 | 3468 |
| 5 | 506 | 2866 | 1358 | 4997 |
| 6 | 204 | 1205 | 461 | 2080 |
| 7 | 412 | 2523 | 1186 | 4058 |
| 8 | 232 | 1440 | 639 | 2619 |
| 9 | 90 | 456 | 35 | 603 |
| 10 | 3 | 49 | 10 | 153 |
| 11 | 7 | 125 | 41 | 364 |
| 12 | 8063 | 65173 | 32573 | 88442 |
| 13 | 6706 | 64611 | 20523 | 76106 |
| 14 | 857 | 6552 | 2382 | 8734 |
| 15 | 416 | 445 | 24145 | 7769 |
| 16 | 26 | 201 | 78 | 508 |
| 17 | 270 | 1543 | 601 | 2554 |
| 18 | 887 | 5908 | 2846 | 9537 |
| 19 | 161 | 1107 | 360 | 2030 |

### 项目自定义包分布 Top 20

| 包名 | 类数量 |
| --- | ---: |
| `com.junkfood.seal.ui` | 3130 |
| `com.junkfood.seal.util` | 123 |
| `com.junkfood.seal.download` | 78 |
| `com.junkfood.seal.database` | 76 |
| `com.junkfood.seal.App$$ExternalSyntheticLambda0` | 1 |
| `com.junkfood.seal.App$$ExternalSyntheticLambda1` | 1 |
| `com.junkfood.seal.App$$ExternalSyntheticLambda2` | 1 |
| `com.junkfood.seal.App$$ExternalSyntheticLambda3` | 1 |
| `com.junkfood.seal.App$$ExternalSyntheticLambda4` | 1 |
| `com.junkfood.seal.App$$ExternalSyntheticLambda5` | 1 |
| `com.junkfood.seal.App$$ExternalSyntheticLambda6` | 1 |
| `com.junkfood.seal.App$$ExternalSyntheticLambda7` | 1 |
| `com.junkfood.seal.App$Companion$WhenMappings` | 1 |
| `com.junkfood.seal.App$Companion$connection$1` | 1 |
| `com.junkfood.seal.App$Companion` | 1 |
| `com.junkfood.seal.App$onCreate$3$2` | 1 |
| `com.junkfood.seal.App$onCreate$3` | 1 |
| `com.junkfood.seal.App` | 1 |
| `com.junkfood.seal.BuildConfig` | 1 |
| `com.junkfood.seal.CrashReportActivity$onCreate$1$1$1$$ExternalSyntheticLambda0` | 1 |

### 自定义类样例

- `com.junkfood.seal.App$$ExternalSyntheticLambda0`
- `com.junkfood.seal.App$$ExternalSyntheticLambda1`
- `com.junkfood.seal.App$$ExternalSyntheticLambda2`
- `com.junkfood.seal.App$$ExternalSyntheticLambda3`
- `com.junkfood.seal.App$$ExternalSyntheticLambda4`
- `com.junkfood.seal.App$$ExternalSyntheticLambda5`
- `com.junkfood.seal.App$$ExternalSyntheticLambda6`
- `com.junkfood.seal.App$$ExternalSyntheticLambda7`
- `com.junkfood.seal.App$Companion$WhenMappings`
- `com.junkfood.seal.App$Companion$connection$1`
- `com.junkfood.seal.App$Companion`
- `com.junkfood.seal.App$onCreate$3$2`
- `com.junkfood.seal.App$onCreate$3`
- `com.junkfood.seal.App`
- `com.junkfood.seal.BuildConfig`
- `com.junkfood.seal.CrashReportActivity$onCreate$1$1$1$$ExternalSyntheticLambda0`
- `com.junkfood.seal.CrashReportActivity$onCreate$1$1$1`
- `com.junkfood.seal.CrashReportActivity$onCreate$1$1`
- `com.junkfood.seal.CrashReportActivity$onCreate$1`
- `com.junkfood.seal.CrashReportActivity`
- `com.junkfood.seal.CrashReportActivityKt$$ExternalSyntheticLambda0`
- `com.junkfood.seal.CrashReportActivityKt$$ExternalSyntheticLambda1`
- `com.junkfood.seal.CrashReportActivityKt$CrashReportPage$2`
- `com.junkfood.seal.CrashReportActivityKt$CrashReportPage$3`
- `com.junkfood.seal.CrashReportActivityKt`
- `com.junkfood.seal.DownloadService$DownloadServiceBinder`
- `com.junkfood.seal.DownloadService`
- `com.junkfood.seal.DownloadServiceKt`
- `com.junkfood.seal.Downloader$$ExternalSyntheticLambda0`
- `com.junkfood.seal.Downloader$$ExternalSyntheticLambda1`

## 五、调用关系摘要

- 调用图范围：`classes under com.junkfood.seal`
- 调用图节点数：`17276`
- 调用图边数：`53967`

### 连接度最高的方法 Top 20

| 方法 | 入度 | 出度 | 总度 |
| --- | ---: | ---: | ---: |
| `java.lang.Object.<init>()V` | 3072 | 0 | 3072 |
| `androidx.compose.runtime.ComposerKt.traceEventEnd()V` | 1486 | 0 | 1486 |
| `androidx.compose.runtime.ComposerKt.isTraceInProgress()Z` | 1486 | 0 | 1486 |
| `androidx.compose.runtime.ComposerKt.traceEventStart(I I I Ljava/lang/String;)V` | 1486 | 0 | 1486 |
| `androidx.compose.runtime.ComposerKt.sourceInformation(Landroidx/compose/runtime/Composer; Ljava/lang/String;)V` | 1484 | 0 | 1484 |
| `androidx.compose.runtime.Composer.skipToGroupEnd()V` | 1378 | 0 | 1378 |
| `androidx.compose.runtime.Composer.getSkipping()Z` | 1359 | 0 | 1359 |
| `kotlin.jvm.internal.Intrinsics.checkNotNullParameter(Ljava/lang/Object; Ljava/lang/String;)V` | 1258 | 0 | 1258 |
| `java.lang.Number.intValue()I` | 1213 | 0 | 1213 |
| `androidx.compose.runtime.Composer.startReplaceGroup(I)V` | 749 | 0 | 749 |
| `androidx.compose.runtime.Composer.endReplaceGroup()V` | 749 | 0 | 749 |
| `androidx.compose.runtime.Composer.rememberedValue()Ljava/lang/Object;` | 742 | 0 | 742 |
| `androidx.compose.runtime.Composer.updateRememberedValue(Ljava/lang/Object;)V` | 742 | 0 | 742 |
| `androidx.compose.runtime.Composer$Companion.getEmpty()Ljava/lang/Object;` | 668 | 0 | 668 |
| `androidx.compose.runtime.Composer.changed(Ljava/lang/Object;)Z` | 513 | 0 | 513 |
| `androidx.compose.ui.res.StringResources_androidKt.stringResource(I Landroidx/compose/runtime/Composer; I)Ljava/lang/String;` | 497 | 0 | 497 |
| `androidx.compose.runtime.Composer.changedInstance(Ljava/lang/Object;)Z` | 373 | 0 | 373 |
| `java.lang.Integer.intValue()I` | 361 | 0 | 361 |
| `androidx.compose.material3.TextKt.Text--4IGK_g(Ljava/lang/String; Landroidx/compose/ui/Modifier; J J Landroidx/compose/ui/text/font/FontStyle; Landroidx/compose/ui/text/font/FontWeight; Landroidx/compose/ui/text/font/FontFamily; J Landroidx/compose/ui/text/style/TextDecoration; Landroidx/compose/ui/text/style/TextAlign; J I Z I I Lkotlin/jvm/functions/Function1; Landroidx/compose/ui/text/TextStyle; Landroidx/compose/runtime/Composer; I I I)V` | 347 | 0 | 347 |
| `kotlin.jvm.internal.Intrinsics.areEqual(Ljava/lang/Object; Ljava/lang/Object;)Z` | 322 | 0 | 322 |

### 项目自定义方法连接度 Top 20

| 方法 | 入度 | 出度 | 总度 |
| --- | ---: | ---: | ---: |
| `com.junkfood.seal.ui.page.download.DownloadPageKt.DownloadPage(Lkotlin/jvm/functions/Function0; Lkotlin/jvm/functions/Function0; Lkotlin/jvm/functions/Function0; Lkotlin/jvm/functions/Function0; Lkotlin/jvm/functions/Function0; Lkotlin/jvm/functions/Function1; Lcom/junkfood/seal/download/DownloaderV2; Lcom/junkfood/seal/ui/page/download/HomePageViewModel; Lcom/junkfood/seal/ui/page/downloadv2/configure/DownloadDialogViewModel; Landroidx/compose/runtime/Composer; I I)V` | 1 | 150 | 151 |
| `com.junkfood.seal.ui.page.videolist.VideoListPageKt.VideoListPage(Lcom/junkfood/seal/ui/page/videolist/VideoListViewModel; Lkotlin/jvm/functions/Function0; Landroidx/compose/runtime/Composer; I I)V` | 2 | 136 | 138 |
| `com.junkfood.seal.ui.page.download.DownloadSettingsDialogKt.DownloadSettingDialog(Z Z Z Lkotlin/jvm/functions/Function1; Lkotlin/jvm/functions/Function0; Lkotlin/jvm/functions/Function0; Landroidx/compose/runtime/Composer; I I)V` | 2 | 106 | 108 |
| `com.junkfood.seal.ui.page.downloadv2.configure.FormatPageKt.FormatPageImpl(Landroidx/compose/ui/Modifier; Lcom/junkfood/seal/util/VideoInfo; Z Z Z Ljava/util/Set; Lkotlin/jvm/functions/Function0; Lkotlin/jvm/functions/Function1; Landroidx/compose/runtime/Composer; I I)V` | 4 | 99 | 103 |
| `com.junkfood.seal.ui.page.settings.appearance.AppearancePreferencesKt$AppearancePreferences$2.invoke(Landroidx/compose/foundation/layout/PaddingValues; Landroidx/compose/runtime/Composer; I)V` | 1 | 99 | 100 |
| `com.junkfood.seal.ui.component.VideoListItemKt.MediaListItem(Landroidx/compose/ui/Modifier; Ljava/lang/String; Ljava/lang/String; Ljava/lang/String; Ljava/lang/String; Ljava/lang/String; J Lkotlin/jvm/functions/Function0; Lkotlin/jvm/functions/Function0; Lkotlin/jvm/functions/Function0; Lkotlin/jvm/functions/Function0; Lkotlin/jvm/functions/Function0; Lkotlin/jvm/functions/Function0; Landroidx/compose/runtime/Composer; I I I)V` | 3 | 96 | 99 |
| `com.junkfood.seal.ui.component.FormatItemKt.FormatItem-uPCbpMU(Landroidx/compose/ui/Modifier; Ljava/lang/String; Z Z Ljava/lang/String; Ljava/lang/String; Z J J Lkotlin/jvm/functions/Function0; Lkotlin/jvm/functions/Function0; Landroidx/compose/runtime/Composer; I I I)V` | 8 | 90 | 98 |
| `com.junkfood.seal.ui.page.downloadv2.configure.DownloadDialogV2Kt.ConfigurePage(Landroidx/compose/ui/Modifier; Ljava/lang/String; Lcom/junkfood/seal/ui/page/downloadv2/configure/Config; Lcom/junkfood/seal/util/DownloadUtil$DownloadPreferences; Lkotlin/jvm/functions/Function2; Lkotlin/jvm/functions/Function1; Lkotlin/jvm/functions/Function1; Lkotlin/jvm/functions/Function1; Landroidx/compose/runtime/Composer; I I)V` | 2 | 94 | 96 |
| `com.junkfood.seal.ui.component.FormatItemKt.FormatVideoPreview(Landroidx/compose/ui/Modifier; Ljava/lang/String; Ljava/lang/String; Ljava/lang/String; I Z Z Z Z Lkotlin/jvm/functions/Function0; Lkotlin/jvm/functions/Function0; Lkotlin/jvm/functions/Function0; Lkotlin/jvm/functions/Function0; Landroidx/compose/runtime/Composer; I I I)V` | 3 | 90 | 93 |
| `com.junkfood.seal.ui.page.downloadv2.ActionSheetKt.ActionSheetInfo(Landroidx/compose/ui/Modifier; Lcom/junkfood/seal/download/Task; Lcom/junkfood/seal/download/Task$ViewState; Landroidx/compose/runtime/Composer; I I)V` | 2 | 90 | 92 |
| `com.junkfood.seal.ui.component.DownloadQueueItemKt$CustomCommandTaskItem$6$1.invoke(Landroidx/compose/runtime/Composer; I)V` | 1 | 90 | 91 |
| `com.junkfood.seal.ui.page.AppEntryKt.AppEntry(Lcom/junkfood/seal/ui/page/downloadv2/configure/DownloadDialogViewModel; Landroidx/compose/runtime/Composer; I)V` | 2 | 88 | 90 |
| `com.junkfood.seal.util.DownloadUtil.downloadVideo-hUnOzRk(Lcom/junkfood/seal/util/VideoInfo; Ljava/lang/String; I Ljava/lang/String; Lcom/junkfood/seal/util/DownloadUtil$DownloadPreferences; Lkotlin/jvm/functions/Function3;)Ljava/lang/Object;` | 2 | 87 | 89 |
| `com.junkfood.seal.ui.component.ActionSheetItemsKt.ActionSheetPrimaryButton-dH6d5Fo(Landroidx/compose/ui/Modifier; J J J Landroidx/compose/ui/graphics/vector/ImageVector; Ljava/lang/String; Lkotlin/jvm/functions/Function0; Landroidx/compose/runtime/Composer; I I)V` | 11 | 77 | 88 |
| `com.junkfood.seal.ui.page.download.VideoSectionSliderKt.VideoSelectionSlider(Landroidx/compose/ui/Modifier; Landroidx/compose/material3/RangeSliderState; Lkotlin/jvm/functions/Function0; Lkotlin/jvm/functions/Function0; Landroidx/compose/runtime/Composer; I I)V` | 3 | 82 | 85 |
| `com.junkfood.seal.ui.page.downloadv2.configure.InputUrlDialogKt.InputUrlPageImpl(Landroidx/compose/ui/Modifier; Ljava/util/List; Ljava/util/List; Lkotlin/jvm/functions/Function1; Lkotlin/jvm/functions/Function1; Lkotlin/jvm/functions/Function1; Landroidx/compose/runtime/Composer; I I)V` | 3 | 82 | 85 |
| `com.junkfood.seal.ui.page.downloadv2.configure.PlaylistSelectionPageKt.PlaylistSelectionPage(Lcom/junkfood/seal/ui/page/downloadv2/configure/DownloadDialogViewModel$SelectionState$PlaylistSelection; Lcom/junkfood/seal/download/DownloaderV2; Lkotlin/jvm/functions/Function0; Landroidx/compose/runtime/Composer; I I)V` | 3 | 81 | 84 |
| `com.junkfood.seal.ui.page.downloadv2.configure.DownloadDialogV2Kt.ErrorPage(Landroidx/compose/ui/Modifier; Lcom/junkfood/seal/ui/page/downloadv2/configure/DownloadDialogViewModel$SheetState$Error; Lkotlin/jvm/functions/Function1; Landroidx/compose/runtime/Composer; I I)V` | 2 | 82 | 84 |
| `com.junkfood.seal.ui.page.settings.directory.DownloadDirectoryPreferencesKt.DownloadDirectoryPreferences(Lkotlin/jvm/functions/Function0; Landroidx/compose/runtime/Composer; I)V` | 3 | 80 | 83 |
| `com.junkfood.seal.ui.page.downloadv2.DownloadPageV2Kt.SubHeader-oC9nPe0(Landroidx/compose/ui/Modifier; J I I Z Lkotlin/jvm/functions/Function0; Lkotlin/jvm/functions/Function0; Landroidx/compose/runtime/Composer; I I)V` | 2 | 79 | 81 |

### 项目代码调用的外部 API 包 Top 20

| 外部包 | 调用次数 |
| --- | ---: |
| `androidx.compose.runtime` | 19434 |
| `java.lang` | 6724 |
| `androidx.compose.ui` | 3086 |
| `kotlin.jvm` | 2196 |
| `androidx.compose.material3` | 1890 |
| `androidx.compose.foundation` | 1834 |
| `java.util` | 612 |
| `kotlin.coroutines` | 387 |
| `androidx.compose.material` | 373 |
| `kotlinx.serialization.encoding` | 357 |
| `kotlin.ResultKt` | 210 |
| `kotlin.collections` | 197 |
| `androidx.compose.animation` | 175 |
| `kotlinx.serialization.internal` | 159 |
| `kotlinx.coroutines.flow` | 140 |
| `kotlinx.coroutines.BuildersKt` | 111 |
| `android.content.Context` | 96 |
| `kotlin.Result` | 95 |
| `android.content.Intent` | 63 |
| `kotlin.text` | 58 |

### 项目内部包依赖关系 Top 20

| 依赖关系 | 调用次数 |
| --- | ---: |
| `com.junkfood.seal.ui.page -> com.junkfood.seal.ui.component` | 389 |
| `com.junkfood.seal.ui.page -> com.junkfood.seal.util.PreferenceUtil` | 191 |
| `com.junkfood.seal.util.DownloadUtil -> com.junkfood.seal.util.DownloadUtil$DownloadPreferences` | 89 |
| `com.junkfood.seal.ui.page -> com.junkfood.seal.database.objects` | 84 |
| `com.junkfood.seal.ui.page -> com.junkfood.seal.ui.common` | 73 |
| `com.junkfood.seal.ui.page -> com.junkfood.seal.util.PreferenceStrings` | 32 |
| `com.junkfood.seal.ui.page -> com.junkfood.seal.ui.theme` | 31 |
| `com.junkfood.seal.ui.component -> com.junkfood.seal.ui.theme` | 31 |
| `com.junkfood.seal.ui.page -> com.junkfood.seal.util.DownloadUtil$DownloadPreferences` | 30 |
| `com.junkfood.seal.ui.page -> com.junkfood.seal.util.VideoInfo` | 27 |
| `com.junkfood.seal.ui.page -> com.junkfood.seal.download.Task$ViewState` | 26 |
| `com.junkfood.seal.ui.page -> com.junkfood.seal.util.TextUtilKt` | 25 |
| `com.junkfood.seal.ui.page -> com.junkfood.seal.Downloader` | 24 |
| `com.junkfood.seal.util.DatabaseUtil -> com.junkfood.seal.database.VideoInfoDao` | 23 |
| `com.junkfood.seal.ui.page -> com.junkfood.seal.util.DatabaseUtil` | 20 |
| `com.junkfood.seal.util.DownloadUtil -> com.junkfood.seal.util.VideoInfo` | 19 |
| `com.junkfood.seal.ui.page -> com.junkfood.seal.App$Companion` | 18 |
| `com.junkfood.seal.ui.page -> com.junkfood.seal.util.FileUtil` | 18 |
| `com.junkfood.seal.ui.page -> com.junkfood.seal.Downloader$CustomCommandTask` | 18 |
| `com.junkfood.seal.ui.page -> com.junkfood.seal.util.ToastUtil` | 17 |

### 调用边样例

| 调用方 | 被调用方 |
| --- | --- |
| `com.junkfood.seal.ui.page.settings.format.ComposableSingletons$DownloadFormatPreferencesKt$lambda-1$1.<clinit>()V` | `com.junkfood.seal.ui.page.settings.format.ComposableSingletons$DownloadFormatPreferencesKt$lambda-1$1.<init>()V` |
| `com.junkfood.seal.ui.page.settings.format.ComposableSingletons$DownloadFormatPreferencesKt$lambda-1$1.<init>()V` | `java.lang.Object.<init>()V` |
| `com.junkfood.seal.ui.page.settings.format.ComposableSingletons$DownloadFormatPreferencesKt$lambda-1$1.invoke(Ljava/lang/Object; Ljava/lang/Object;)Ljava/lang/Object;` | `java.lang.Number.intValue()I` |
| `com.junkfood.seal.ui.page.settings.format.ComposableSingletons$DownloadFormatPreferencesKt$lambda-1$1.invoke(Ljava/lang/Object; Ljava/lang/Object;)Ljava/lang/Object;` | `com.junkfood.seal.ui.page.settings.format.ComposableSingletons$DownloadFormatPreferencesKt$lambda-1$1.invoke(Landroidx/compose/runtime/Composer; I)V` |
| `com.junkfood.seal.ui.page.settings.format.ComposableSingletons$DownloadFormatPreferencesKt$lambda-1$1.invoke(Landroidx/compose/runtime/Composer; I)V` | `androidx.compose.material3.TextKt.Text--4IGK_g(Ljava/lang/String; Landroidx/compose/ui/Modifier; J J Landroidx/compose/ui/text/font/FontStyle; Landroidx/compose/ui/text/font/FontWeight; Landroidx/compose/ui/text/font/FontFamily; J Landroidx/compose/ui/text/style/TextDecoration; Landroidx/compose/ui/text/style/TextAlign; J I Z I I Lkotlin/jvm/functions/Function1; Landroidx/compose/ui/text/TextStyle; Landroidx/compose/runtime/Composer; I I I)V` |
| `com.junkfood.seal.ui.page.settings.format.ComposableSingletons$DownloadFormatPreferencesKt$lambda-1$1.invoke(Landroidx/compose/runtime/Composer; I)V` | `androidx.compose.runtime.ComposerKt.traceEventEnd()V` |
| `com.junkfood.seal.ui.page.settings.format.ComposableSingletons$DownloadFormatPreferencesKt$lambda-1$1.invoke(Landroidx/compose/runtime/Composer; I)V` | `androidx.compose.runtime.ComposerKt.sourceInformation(Landroidx/compose/runtime/Composer; Ljava/lang/String;)V` |
| `com.junkfood.seal.ui.page.settings.format.ComposableSingletons$DownloadFormatPreferencesKt$lambda-1$1.invoke(Landroidx/compose/runtime/Composer; I)V` | `androidx.compose.runtime.ComposerKt.isTraceInProgress()Z` |
| `com.junkfood.seal.ui.page.settings.format.ComposableSingletons$DownloadFormatPreferencesKt$lambda-1$1.invoke(Landroidx/compose/runtime/Composer; I)V` | `androidx.compose.ui.res.StringResources_androidKt.stringResource(I Landroidx/compose/runtime/Composer; I)Ljava/lang/String;` |
| `com.junkfood.seal.ui.page.settings.format.ComposableSingletons$DownloadFormatPreferencesKt$lambda-1$1.invoke(Landroidx/compose/runtime/Composer; I)V` | `androidx.compose.runtime.ComposerKt.traceEventStart(I I I Ljava/lang/String;)V` |
| `com.junkfood.seal.ui.page.settings.format.ComposableSingletons$DownloadFormatPreferencesKt$lambda-1$1.invoke(Landroidx/compose/runtime/Composer; I)V` | `androidx.compose.runtime.Composer.skipToGroupEnd()V` |
| `com.junkfood.seal.ui.page.settings.format.ComposableSingletons$DownloadFormatPreferencesKt$lambda-1$1.invoke(Landroidx/compose/runtime/Composer; I)V` | `androidx.compose.runtime.Composer.getSkipping()Z` |
| `com.junkfood.seal.ui.page.settings.format.ComposableSingletons$DownloadFormatPreferencesKt$lambda-10$1.<clinit>()V` | `com.junkfood.seal.ui.page.settings.format.ComposableSingletons$DownloadFormatPreferencesKt$lambda-10$1.<init>()V` |
| `com.junkfood.seal.ui.page.settings.format.ComposableSingletons$DownloadFormatPreferencesKt$lambda-10$1.<init>()V` | `java.lang.Object.<init>()V` |
| `com.junkfood.seal.ui.page.settings.format.ComposableSingletons$DownloadFormatPreferencesKt$lambda-10$1.invoke(Ljava/lang/Object; Ljava/lang/Object;)Ljava/lang/Object;` | `java.lang.Number.intValue()I` |
| `com.junkfood.seal.ui.page.settings.format.ComposableSingletons$DownloadFormatPreferencesKt$lambda-10$1.invoke(Ljava/lang/Object; Ljava/lang/Object;)Ljava/lang/Object;` | `com.junkfood.seal.ui.page.settings.format.ComposableSingletons$DownloadFormatPreferencesKt$lambda-10$1.invoke(Landroidx/compose/runtime/Composer; I)V` |
| `com.junkfood.seal.ui.page.settings.format.ComposableSingletons$DownloadFormatPreferencesKt$lambda-10$1.invoke(Landroidx/compose/runtime/Composer; I)V` | `androidx.compose.material3.IconKt.Icon-ww6aTOc(Landroidx/compose/ui/graphics/vector/ImageVector; Ljava/lang/String; Landroidx/compose/ui/Modifier; J Landroidx/compose/runtime/Composer; I I)V` |
| `com.junkfood.seal.ui.page.settings.format.ComposableSingletons$DownloadFormatPreferencesKt$lambda-10$1.invoke(Landroidx/compose/runtime/Composer; I)V` | `androidx.compose.runtime.Composer.skipToGroupEnd()V` |
| `com.junkfood.seal.ui.page.settings.format.ComposableSingletons$DownloadFormatPreferencesKt$lambda-10$1.invoke(Landroidx/compose/runtime/Composer; I)V` | `androidx.compose.runtime.Composer.getSkipping()Z` |
| `com.junkfood.seal.ui.page.settings.format.ComposableSingletons$DownloadFormatPreferencesKt$lambda-10$1.invoke(Landroidx/compose/runtime/Composer; I)V` | `androidx.compose.runtime.ComposerKt.isTraceInProgress()Z` |

## 六、签名信息

- v1 签名：`False`
- v2 签名：`True`
- v3 签名：`True`
- 签名文件：``

## 七、结论

本次分析使用 Androguard 对 Seal APK 进行了静态分析，覆盖 APK 基础信息、Manifest 组件、权限、文件结构、DEX 类与方法统计，以及项目自定义类范围内的调用关系摘要。结果表明，该 APK 包含多个 DEX 文件，项目自定义类集中在 `com.junkfood.seal` 包下；应用声明了网络、外部存储、通知、安装包请求、忽略电池优化等权限；主入口为 `com.junkfood.seal.MainActivity`。该 debug 包 `debuggable=true`，适合课程测试，但正式发布包应关闭调试属性。调用图摘要可用于后续定位核心业务类、高连接度方法和项目代码依赖的外部 API。
