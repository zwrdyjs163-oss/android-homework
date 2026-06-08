# frida-trace 方法调用图摘要

- 日志来源：`frida-trace`
- 日志事件数量：`10`
- 方法节点数量：`9`
- 调用边数量：`10`
- 异常方法数量：`0`

## 高频方法 Top 30

| 方法 | 次数 |
| --- | ---: |
| `util.PreferenceUtil.getString()` | 2 |
| `ui.page.downloadv2.DownloadPageV2Kt.DownloadPageV2()` | 1 |
| `util.PreferenceUtil.getBoolean()` | 1 |
| `util.PreferenceUtil.getInt()` | 1 |
| `ui.page.AppEntryKt.AppEntry()` | 1 |
| `ui.page.NavigationDrawerKt.NavigationDrawer()` | 1 |
| `ui.common.CompositionLocalsKt.getLocalWindowWidthState()` | 1 |
| `ui.page.settings.directory.DownloadDirectoryPreferencesKt.DownloadDirectoryPreferences()` | 1 |
| `ui.page.settings.directory.DownloadDirectoryPreferencesKt.isValidDirectory()` | 1 |

## 高频调用关系 Top 30

| 调用方 | 被调用方 | 次数 |
| --- | --- | ---: |
| `<entry>` | `ui.page.downloadv2.DownloadPageV2Kt.DownloadPageV2()` | 1 |
| `ui.page.downloadv2.DownloadPageV2Kt.DownloadPageV2()` | `util.PreferenceUtil.getBoolean()` | 1 |
| `ui.page.downloadv2.DownloadPageV2Kt.DownloadPageV2()` | `util.PreferenceUtil.getString()` | 1 |
| `ui.page.downloadv2.DownloadPageV2Kt.DownloadPageV2()` | `util.PreferenceUtil.getInt()` | 1 |
| `<entry>` | `ui.page.AppEntryKt.AppEntry()` | 1 |
| `ui.page.AppEntryKt.AppEntry()` | `ui.page.NavigationDrawerKt.NavigationDrawer()` | 1 |
| `ui.page.NavigationDrawerKt.NavigationDrawer()` | `ui.common.CompositionLocalsKt.getLocalWindowWidthState()` | 1 |
| `<entry>` | `ui.page.settings.directory.DownloadDirectoryPreferencesKt.DownloadDirectoryPreferences()` | 1 |
| `ui.page.settings.directory.DownloadDirectoryPreferencesKt.DownloadDirectoryPreferences()` | `ui.page.settings.directory.DownloadDirectoryPreferencesKt.isValidDirectory()` | 1 |
| `ui.page.settings.directory.DownloadDirectoryPreferencesKt.DownloadDirectoryPreferences()` | `util.PreferenceUtil.getString()` | 1 |