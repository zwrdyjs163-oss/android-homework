# frida-trace 方法调用图摘要

- 日志来源：`frida-trace`
- 日志事件数量：`98`
- 方法节点数量：`12`
- 调用边数量：`17`
- 异常方法数量：`0`

## 高频方法 Top 30

| 方法 | 次数 |
| --- | ---: |
| `PreferenceUtil.getBoolean$default()` | 31 |
| `PreferenceUtil.getBoolean()` | 31 |
| `PreferenceUtil.getString$default()` | 8 |
| `PreferenceUtil.getString()` | 8 |
| `PreferenceUtil.getInt$default()` | 7 |
| `PreferenceUtil.getInt()` | 7 |
| `PreferenceUtil.getSponsorBlockCategories()` | 1 |
| `PreferenceUtil.getAudioConvertFormat()` | 1 |
| `PreferenceUtil.getVideoFormat()` | 1 |
| `PreferenceUtil.getVideoResolution()` | 1 |
| `PreferenceUtil.getMaxDownloadRate()` | 1 |
| `PreferenceUtil.containsKey()` | 1 |

## 高频调用关系 Top 30

| 调用方 | 被调用方 | 次数 |
| --- | --- | ---: |
| `<entry>` | `PreferenceUtil.getBoolean$default()` | 31 |
| `PreferenceUtil.getBoolean$default()` | `PreferenceUtil.getBoolean()` | 31 |
| `PreferenceUtil.getString$default()` | `PreferenceUtil.getString()` | 8 |
| `PreferenceUtil.getInt$default()` | `PreferenceUtil.getInt()` | 7 |
| `<entry>` | `PreferenceUtil.getString$default()` | 6 |
| `<entry>` | `PreferenceUtil.getInt$default()` | 4 |
| `<entry>` | `PreferenceUtil.getSponsorBlockCategories()` | 1 |
| `PreferenceUtil.getSponsorBlockCategories()` | `PreferenceUtil.getString$default()` | 1 |
| `<entry>` | `PreferenceUtil.getAudioConvertFormat()` | 1 |
| `PreferenceUtil.getAudioConvertFormat()` | `PreferenceUtil.getInt$default()` | 1 |
| `<entry>` | `PreferenceUtil.getVideoFormat()` | 1 |
| `PreferenceUtil.getVideoFormat()` | `PreferenceUtil.getInt$default()` | 1 |
| `<entry>` | `PreferenceUtil.getVideoResolution()` | 1 |
| `PreferenceUtil.getVideoResolution()` | `PreferenceUtil.getInt$default()` | 1 |
| `<entry>` | `PreferenceUtil.getMaxDownloadRate()` | 1 |
| `PreferenceUtil.getMaxDownloadRate()` | `PreferenceUtil.getString$default()` | 1 |
| `<entry>` | `PreferenceUtil.containsKey()` | 1 |