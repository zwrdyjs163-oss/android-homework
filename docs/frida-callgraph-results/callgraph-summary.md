# Frida 动态调用关系摘要

- trace 事件数量：`665`
- 方法节点数量：`94`
- 调用边数量：`104`
- 异常事件方法数量：`0`

## 高频方法 Top 30

| 方法 | 次数 |
| --- | ---: |
| `util.PreferenceUtil.getBoolean(Ljava/lang/String;,Z)` | 31 |
| `ui.common.CompositionLocalsKt.getLocalFixedColorRoles()` | 14 |
| `util.PreferenceUtil.getString(Ljava/lang/String;,Ljava/lang/String;)` | 8 |
| `util.PreferenceUtil.getInt(Ljava/lang/String;,I)` | 7 |
| `ui.page.settings.directory.DownloadDirectoryPreferencesKt.isValidDirectory(Ljava/lang/String;)` | 6 |
| `ui.page.settings.directory.DownloadDirectoryPreferencesKt$DownloadDirectoryPreferences$launcher$1.createIntent(Landroid/content/Context;,Ljava/lang/Object;)` | 5 |
| `ui.page.settings.directory.DownloadDirectoryPreferencesKt$DownloadDirectoryPreferences$launcher$1.createIntent(Landroid/content/Context;,Landroid/net/Uri;)` | 5 |
| `ui.common.CompositionLocalsKt.getLocalWindowWidthState()` | 5 |
| `ui.component.SelectionGroupKt.SelectionGroupItem(Lcom/junkfood/seal/ui/component/SelectionGroupScope;,Z,Lkotlin/jvm/functions/Function0;,Landroidx/compose/ui/Modifier;,Z,Landroidx/compose/ui/graphics/Shape;,Lcom/junkfood/seal/ui/component/SelectionGroupItemColors;,Landroidx/compose/foundation/interaction/MutableInteractionSource;,Landroidx/compose/foundation/layout/PaddingValues;,Lkotlin/jvm/functions/Function3;,Landroidx/compose/runtime/Composer;,I,I)` | 4 |
| `ui.component.SelectionGroupKt$SelectionGroupItem$2$1$1.invoke(Ljava/lang/Object;,Ljava/lang/Object;)` | 4 |
| `ui.component.SelectionGroupKt$SelectionGroupItem$2$1$1.invoke(Landroidx/compose/runtime/Composer;,I)` | 4 |
| `ui.page.downloadv2.configure.DownloadDialogViewModel.getSheetStateFlow()` | 3 |
| `ui.common.motion.AnimationSpecsKt.getEmphasizedDecelerate()` | 3 |
| `ui.common.motion.AnimationSpecsKt.getEmphasizedAccelerate()` | 3 |
| `ui.page.downloadv2.DownloadPageV2Kt.DownloadPageV2(Landroidx/compose/ui/Modifier;,Lkotlin/jvm/functions/Function0;,Lcom/junkfood/seal/ui/page/downloadv2/configure/DownloadDialogViewModel;,Lcom/junkfood/seal/download/DownloaderV2;,Landroidx/compose/runtime/Composer;,I,I)` | 3 |
| `ui.page.AppEntryKt.AppEntry(Lcom/junkfood/seal/ui/page/downloadv2/configure/DownloadDialogViewModel;,Landroidx/compose/runtime/Composer;,I)` | 2 |
| `ui.page.NavigationDrawerKt.NavigationDrawer-G6M8kWE(Landroidx/compose/ui/Modifier;,Landroidx/compose/material3/DrawerState;,I,Ljava/lang/String;,Ljava/lang/String;,Z,Lkotlin/jvm/functions/Function1;,Lkotlin/jvm/functions/Function1;,Z,Lkotlin/jvm/functions/Function2;,Lkotlin/jvm/functions/Function2;,Landroidx/compose/runtime/Composer;,I,I,I)` | 2 |
| `ui.page.AppEntryKt$settingsGraph$1$13.invoke(Ljava/lang/Object;,Ljava/lang/Object;,Ljava/lang/Object;,Ljava/lang/Object;)` | 2 |
| `ui.page.AppEntryKt$settingsGraph$1$13.invoke(Landroidx/compose/animation/AnimatedVisibilityScope;,Landroidx/navigation/NavBackStackEntry;,Landroidx/compose/runtime/Composer;,I)` | 2 |
| `ui.page.settings.directory.DownloadDirectoryPreferencesKt.DownloadDirectoryPreferences(Lkotlin/jvm/functions/Function0;,Landroidx/compose/runtime/Composer;,I)` | 2 |
| `ui.page.AppUpdaterKt.AppUpdater(Landroidx/compose/runtime/Composer;,I)` | 2 |
| `ui.page.YtdlpUpdaterKt.YtdlpUpdater(Landroidx/compose/runtime/Composer;,I)` | 2 |
| `ui.page.NavigationDrawerKt$NavigationDrawer$1$1.invoke(Ljava/lang/Object;,Ljava/lang/Object;,Ljava/lang/Object;)` | 2 |
| `ui.page.NavigationDrawerKt$NavigationDrawer$1$1.invoke(Landroidx/compose/foundation/layout/ColumnScope;,Landroidx/compose/runtime/Composer;,I)` | 2 |
| `ui.page.NavigationDrawerKt.NavigationDrawerSheetContent(Landroidx/compose/ui/Modifier;,Ljava/lang/String;,Z,Lkotlin/jvm/functions/Function1;,Lkotlin/jvm/functions/Function1;,Lkotlin/jvm/functions/Function2;,Landroidx/compose/runtime/Composer;,I,I)` | 2 |
| `ui.page.ComposableSingletons$NavigationDrawerKt$lambda-3$1.invoke(Ljava/lang/Object;,Ljava/lang/Object;)` | 2 |
| `ui.page.ComposableSingletons$NavigationDrawerKt$lambda-3$1.invoke(Landroidx/compose/runtime/Composer;,I)` | 2 |
| `ui.page.ComposableSingletons$NavigationDrawerKt$lambda-2$1.invoke(Ljava/lang/Object;,Ljava/lang/Object;)` | 2 |
| `ui.page.ComposableSingletons$NavigationDrawerKt$lambda-2$1.invoke(Landroidx/compose/runtime/Composer;,I)` | 2 |
| `ui.page.ComposableSingletons$NavigationDrawerKt$lambda-15$1.invoke(Ljava/lang/Object;,Ljava/lang/Object;)` | 2 |

## 高频调用边 Top 30

| 调用方 | 被调用方 | 次数 |
| --- | --- | ---: |
| `ui.page.downloadv2.DownloadPageV2Kt.DownloadPageV2(Landroidx/compose/ui/Modifier;,Lkotlin/jvm/functions/Function0;,Lcom/junkfood/seal/ui/page/downloadv2/configure/DownloadDialogViewModel;,Lcom/junkfood/seal/download/DownloaderV2;,Landroidx/compose/runtime/Composer;,I,I)` | `util.PreferenceUtil.getBoolean(Ljava/lang/String;,Z)` | 31 |
| `ui.page.downloadv2.DownloadPageV2Kt$DownloadPageImplV2$5$3$1$1$2.invoke(Lcom/junkfood/seal/ui/component/SelectionGroupScope;,Landroidx/compose/runtime/Composer;,I)` | `ui.common.CompositionLocalsKt.getLocalFixedColorRoles()` | 12 |
| `ui.page.settings.directory.DownloadDirectoryPreferencesKt.DownloadDirectoryPreferences(Lkotlin/jvm/functions/Function0;,Landroidx/compose/runtime/Composer;,I)` | `ui.page.settings.directory.DownloadDirectoryPreferencesKt.isValidDirectory(Ljava/lang/String;)` | 6 |
| `ui.page.downloadv2.DownloadPageV2Kt.DownloadPageV2(Landroidx/compose/ui/Modifier;,Lkotlin/jvm/functions/Function0;,Lcom/junkfood/seal/ui/page/downloadv2/configure/DownloadDialogViewModel;,Lcom/junkfood/seal/download/DownloaderV2;,Landroidx/compose/runtime/Composer;,I,I)` | `util.PreferenceUtil.getString(Ljava/lang/String;,Ljava/lang/String;)` | 6 |
| `<entry>` | `ui.page.settings.directory.DownloadDirectoryPreferencesKt$DownloadDirectoryPreferences$launcher$1.createIntent(Landroid/content/Context;,Ljava/lang/Object;)` | 5 |
| `ui.page.settings.directory.DownloadDirectoryPreferencesKt$DownloadDirectoryPreferences$launcher$1.createIntent(Landroid/content/Context;,Ljava/lang/Object;)` | `ui.page.settings.directory.DownloadDirectoryPreferencesKt$DownloadDirectoryPreferences$launcher$1.createIntent(Landroid/content/Context;,Landroid/net/Uri;)` | 5 |
| `ui.page.downloadv2.DownloadPageV2Kt.DownloadPageV2(Landroidx/compose/ui/Modifier;,Lkotlin/jvm/functions/Function0;,Lcom/junkfood/seal/ui/page/downloadv2/configure/DownloadDialogViewModel;,Lcom/junkfood/seal/download/DownloaderV2;,Landroidx/compose/runtime/Composer;,I,I)` | `util.PreferenceUtil.getInt(Ljava/lang/String;,I)` | 4 |
| `ui.page.downloadv2.DownloadPageV2Kt$DownloadPageImplV2$5$3$1$1$2.invoke(Lcom/junkfood/seal/ui/component/SelectionGroupScope;,Landroidx/compose/runtime/Composer;,I)` | `ui.component.SelectionGroupKt.SelectionGroupItem(Lcom/junkfood/seal/ui/component/SelectionGroupScope;,Z,Lkotlin/jvm/functions/Function0;,Landroidx/compose/ui/Modifier;,Z,Landroidx/compose/ui/graphics/Shape;,Lcom/junkfood/seal/ui/component/SelectionGroupItemColors;,Landroidx/compose/foundation/interaction/MutableInteractionSource;,Landroidx/compose/foundation/layout/PaddingValues;,Lkotlin/jvm/functions/Function3;,Landroidx/compose/runtime/Composer;,I,I)` | 4 |
| `ui.component.SelectionGroupKt.SelectionGroupItem(Lcom/junkfood/seal/ui/component/SelectionGroupScope;,Z,Lkotlin/jvm/functions/Function0;,Landroidx/compose/ui/Modifier;,Z,Landroidx/compose/ui/graphics/Shape;,Lcom/junkfood/seal/ui/component/SelectionGroupItemColors;,Landroidx/compose/foundation/interaction/MutableInteractionSource;,Landroidx/compose/foundation/layout/PaddingValues;,Lkotlin/jvm/functions/Function3;,Landroidx/compose/runtime/Composer;,I,I)` | `ui.component.SelectionGroupKt$SelectionGroupItem$2$1$1.invoke(Ljava/lang/Object;,Ljava/lang/Object;)` | 4 |
| `ui.component.SelectionGroupKt$SelectionGroupItem$2$1$1.invoke(Ljava/lang/Object;,Ljava/lang/Object;)` | `ui.component.SelectionGroupKt$SelectionGroupItem$2$1$1.invoke(Landroidx/compose/runtime/Composer;,I)` | 4 |
| `ui.page.NavigationDrawerKt.NavigationDrawer-G6M8kWE(Landroidx/compose/ui/Modifier;,Landroidx/compose/material3/DrawerState;,I,Ljava/lang/String;,Ljava/lang/String;,Z,Lkotlin/jvm/functions/Function1;,Lkotlin/jvm/functions/Function1;,Z,Lkotlin/jvm/functions/Function2;,Lkotlin/jvm/functions/Function2;,Landroidx/compose/runtime/Composer;,I,I,I)` | `ui.common.motion.AnimationSpecsKt.getEmphasizedDecelerate()` | 3 |
| `ui.page.NavigationDrawerKt.NavigationDrawer-G6M8kWE(Landroidx/compose/ui/Modifier;,Landroidx/compose/material3/DrawerState;,I,Ljava/lang/String;,Ljava/lang/String;,Z,Lkotlin/jvm/functions/Function1;,Lkotlin/jvm/functions/Function1;,Z,Lkotlin/jvm/functions/Function2;,Lkotlin/jvm/functions/Function2;,Landroidx/compose/runtime/Composer;,I,I,I)` | `ui.common.motion.AnimationSpecsKt.getEmphasizedAccelerate()` | 3 |
| `<entry>` | `ui.page.downloadv2.DownloadPageV2Kt.DownloadPageV2(Landroidx/compose/ui/Modifier;,Lkotlin/jvm/functions/Function0;,Lcom/junkfood/seal/ui/page/downloadv2/configure/DownloadDialogViewModel;,Lcom/junkfood/seal/download/DownloaderV2;,Landroidx/compose/runtime/Composer;,I,I)` | 3 |
| `<entry>` | `ui.page.AppEntryKt.AppEntry(Lcom/junkfood/seal/ui/page/downloadv2/configure/DownloadDialogViewModel;,Landroidx/compose/runtime/Composer;,I)` | 2 |
| `ui.page.AppEntryKt.AppEntry(Lcom/junkfood/seal/ui/page/downloadv2/configure/DownloadDialogViewModel;,Landroidx/compose/runtime/Composer;,I)` | `ui.common.CompositionLocalsKt.getLocalWindowWidthState()` | 2 |
| `ui.page.AppEntryKt.AppEntry(Lcom/junkfood/seal/ui/page/downloadv2/configure/DownloadDialogViewModel;,Landroidx/compose/runtime/Composer;,I)` | `ui.page.downloadv2.configure.DownloadDialogViewModel.getSheetStateFlow()` | 2 |
| `ui.page.AppEntryKt.AppEntry(Lcom/junkfood/seal/ui/page/downloadv2/configure/DownloadDialogViewModel;,Landroidx/compose/runtime/Composer;,I)` | `ui.page.NavigationDrawerKt.NavigationDrawer-G6M8kWE(Landroidx/compose/ui/Modifier;,Landroidx/compose/material3/DrawerState;,I,Ljava/lang/String;,Ljava/lang/String;,Z,Lkotlin/jvm/functions/Function1;,Lkotlin/jvm/functions/Function1;,Z,Lkotlin/jvm/functions/Function2;,Lkotlin/jvm/functions/Function2;,Landroidx/compose/runtime/Composer;,I,I,I)` | 2 |
| `ui.page.NavigationDrawerKt.NavigationDrawer-G6M8kWE(Landroidx/compose/ui/Modifier;,Landroidx/compose/material3/DrawerState;,I,Ljava/lang/String;,Ljava/lang/String;,Z,Lkotlin/jvm/functions/Function1;,Lkotlin/jvm/functions/Function1;,Z,Lkotlin/jvm/functions/Function2;,Lkotlin/jvm/functions/Function2;,Landroidx/compose/runtime/Composer;,I,I,I)` | `ui.page.AppEntryKt$settingsGraph$1$13.invoke(Ljava/lang/Object;,Ljava/lang/Object;,Ljava/lang/Object;,Ljava/lang/Object;)` | 2 |
| `ui.page.AppEntryKt$settingsGraph$1$13.invoke(Ljava/lang/Object;,Ljava/lang/Object;,Ljava/lang/Object;,Ljava/lang/Object;)` | `ui.page.AppEntryKt$settingsGraph$1$13.invoke(Landroidx/compose/animation/AnimatedVisibilityScope;,Landroidx/navigation/NavBackStackEntry;,Landroidx/compose/runtime/Composer;,I)` | 2 |
| `ui.page.AppEntryKt$settingsGraph$1$13.invoke(Landroidx/compose/animation/AnimatedVisibilityScope;,Landroidx/navigation/NavBackStackEntry;,Landroidx/compose/runtime/Composer;,I)` | `ui.page.settings.directory.DownloadDirectoryPreferencesKt.DownloadDirectoryPreferences(Lkotlin/jvm/functions/Function0;,Landroidx/compose/runtime/Composer;,I)` | 2 |
| `ui.page.NavigationDrawerKt.NavigationDrawer-G6M8kWE(Landroidx/compose/ui/Modifier;,Landroidx/compose/material3/DrawerState;,I,Ljava/lang/String;,Ljava/lang/String;,Z,Lkotlin/jvm/functions/Function1;,Lkotlin/jvm/functions/Function1;,Z,Lkotlin/jvm/functions/Function2;,Lkotlin/jvm/functions/Function2;,Landroidx/compose/runtime/Composer;,I,I,I)` | `ui.page.AppUpdaterKt.AppUpdater(Landroidx/compose/runtime/Composer;,I)` | 2 |
| `ui.page.NavigationDrawerKt.NavigationDrawer-G6M8kWE(Landroidx/compose/ui/Modifier;,Landroidx/compose/material3/DrawerState;,I,Ljava/lang/String;,Ljava/lang/String;,Z,Lkotlin/jvm/functions/Function1;,Lkotlin/jvm/functions/Function1;,Z,Lkotlin/jvm/functions/Function2;,Lkotlin/jvm/functions/Function2;,Landroidx/compose/runtime/Composer;,I,I,I)` | `ui.page.YtdlpUpdaterKt.YtdlpUpdater(Landroidx/compose/runtime/Composer;,I)` | 2 |
| `ui.page.NavigationDrawerKt.NavigationDrawer-G6M8kWE(Landroidx/compose/ui/Modifier;,Landroidx/compose/material3/DrawerState;,I,Ljava/lang/String;,Ljava/lang/String;,Z,Lkotlin/jvm/functions/Function1;,Lkotlin/jvm/functions/Function1;,Z,Lkotlin/jvm/functions/Function2;,Lkotlin/jvm/functions/Function2;,Landroidx/compose/runtime/Composer;,I,I,I)` | `ui.page.NavigationDrawerKt$NavigationDrawer$1$1.invoke(Ljava/lang/Object;,Ljava/lang/Object;,Ljava/lang/Object;)` | 2 |
| `ui.page.NavigationDrawerKt$NavigationDrawer$1$1.invoke(Ljava/lang/Object;,Ljava/lang/Object;,Ljava/lang/Object;)` | `ui.page.NavigationDrawerKt$NavigationDrawer$1$1.invoke(Landroidx/compose/foundation/layout/ColumnScope;,Landroidx/compose/runtime/Composer;,I)` | 2 |
| `ui.page.NavigationDrawerKt$NavigationDrawer$1$1.invoke(Landroidx/compose/foundation/layout/ColumnScope;,Landroidx/compose/runtime/Composer;,I)` | `ui.page.NavigationDrawerKt.NavigationDrawerSheetContent(Landroidx/compose/ui/Modifier;,Ljava/lang/String;,Z,Lkotlin/jvm/functions/Function1;,Lkotlin/jvm/functions/Function1;,Lkotlin/jvm/functions/Function2;,Landroidx/compose/runtime/Composer;,I,I)` | 2 |
| `ui.page.NavigationDrawerKt.NavigationDrawerSheetContent(Landroidx/compose/ui/Modifier;,Ljava/lang/String;,Z,Lkotlin/jvm/functions/Function1;,Lkotlin/jvm/functions/Function1;,Lkotlin/jvm/functions/Function2;,Landroidx/compose/runtime/Composer;,I,I)` | `ui.page.ComposableSingletons$NavigationDrawerKt$lambda-3$1.invoke(Ljava/lang/Object;,Ljava/lang/Object;)` | 2 |
| `ui.page.ComposableSingletons$NavigationDrawerKt$lambda-3$1.invoke(Ljava/lang/Object;,Ljava/lang/Object;)` | `ui.page.ComposableSingletons$NavigationDrawerKt$lambda-3$1.invoke(Landroidx/compose/runtime/Composer;,I)` | 2 |
| `ui.page.NavigationDrawerKt.NavigationDrawerSheetContent(Landroidx/compose/ui/Modifier;,Ljava/lang/String;,Z,Lkotlin/jvm/functions/Function1;,Lkotlin/jvm/functions/Function1;,Lkotlin/jvm/functions/Function2;,Landroidx/compose/runtime/Composer;,I,I)` | `ui.page.ComposableSingletons$NavigationDrawerKt$lambda-2$1.invoke(Ljava/lang/Object;,Ljava/lang/Object;)` | 2 |
| `ui.page.ComposableSingletons$NavigationDrawerKt$lambda-2$1.invoke(Ljava/lang/Object;,Ljava/lang/Object;)` | `ui.page.ComposableSingletons$NavigationDrawerKt$lambda-2$1.invoke(Landroidx/compose/runtime/Composer;,I)` | 2 |
| `ui.page.NavigationDrawerKt.NavigationDrawerSheetContent(Landroidx/compose/ui/Modifier;,Ljava/lang/String;,Z,Lkotlin/jvm/functions/Function1;,Lkotlin/jvm/functions/Function1;,Lkotlin/jvm/functions/Function2;,Landroidx/compose/runtime/Composer;,I,I)` | `ui.page.ComposableSingletons$NavigationDrawerKt$lambda-15$1.invoke(Ljava/lang/Object;,Ljava/lang/Object;)` | 2 |