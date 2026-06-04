import json
import re
import sys
from collections import Counter
from datetime import datetime
from pathlib import Path

from loguru import logger

logger.remove()
logger.add(sys.stderr, level="ERROR")

from androguard.core.apk import APK
from androguard.misc import AnalyzeAPK


ROOT = Path(__file__).resolve().parents[1]
DEFAULT_APK = ROOT / "bundletool-output" / "Seal-from-aab-universal-debug.apk"
OUT_DIR = ROOT / "docs" / "androguard-analysis-results"
APP_CLASS_PREFIX = "Lcom/junkfood/seal/"
APP_CLASS_REGEX = r"^Lcom/junkfood/seal/.*"


def dex_name_to_java(name):
    if not name:
        return ""
    if name.startswith("L") and name.endswith(";"):
        name = name[1:-1]
    return name.replace("/", ".")


def package_name(class_name, depth=3):
    java_name = dex_name_to_java(class_name)
    parts = java_name.split(".")
    if len(parts) <= depth:
        return java_name
    return ".".join(parts[:depth])


def extension_name(path):
    name = Path(path).name
    if "." not in name:
        return "<no_ext>"
    return "." + name.rsplit(".", 1)[-1].lower()


def method_label(node):
    method = getattr(node, "method", node)
    class_name = ""
    method_name = ""
    descriptor = ""

    for attr, target in (
        ("get_class_name", "class_name"),
        ("get_name", "method_name"),
        ("get_descriptor", "descriptor"),
    ):
        try:
            value = getattr(method, attr)()
        except Exception:
            value = ""
        if target == "class_name":
            class_name = value
        elif target == "method_name":
            method_name = value
        else:
            descriptor = value

    if class_name or method_name:
        return f"{dex_name_to_java(class_name)}.{method_name}{descriptor}"
    return str(node)


def method_class_name(node):
    method = getattr(node, "method", node)
    try:
        return method.get_class_name()
    except Exception:
        return ""


def external_package_from_method(node):
    class_name = method_class_name(node)
    if not class_name or class_name.startswith(APP_CLASS_PREFIX):
        return None
    java_name = dex_name_to_java(class_name)
    parts = java_name.split(".")
    if java_name.startswith("java.") or java_name.startswith("kotlin."):
        return ".".join(parts[:2])
    if java_name.startswith("androidx.") or java_name.startswith("android."):
        return ".".join(parts[:3])
    if len(parts) >= 3:
        return ".".join(parts[:3])
    return java_name


def bool_attr(value):
    return str(value).lower() == "true"


def component_details(apk):
    android_ns = "{http://schemas.android.com/apk/res/android}"
    root = apk.get_android_manifest_xml()
    application = root.find("application")
    details = {"application": {}, "components": []}
    if application is None:
        return details

    details["application"] = {
        "debuggable": bool_attr(application.get(android_ns + "debuggable")),
        "allowBackup": bool_attr(application.get(android_ns + "allowBackup")),
        "name": application.get(android_ns + "name", ""),
    }

    tag_to_type = {
        "activity": "activity",
        "service": "service",
        "receiver": "receiver",
        "provider": "provider",
    }
    for child in application:
        tag = str(child.tag).split("}")[-1]
        if tag not in tag_to_type:
            continue
        name = child.get(android_ns + "name", "")
        exported_value = child.get(android_ns + "exported")
        has_intent_filter = child.find("intent-filter") is not None
        details["components"].append(
            {
                "type": tag_to_type[tag],
                "name": name,
                "exported": exported_value,
                "exported_bool": bool_attr(exported_value),
                "has_intent_filter": has_intent_filter,
            }
        )
    return details


def intent_filter_details(apk, components):
    details = []
    for component_type in ("activity", "service", "receiver"):
        for name in components.get(component_type + "s", []):
            filters = apk.get_intent_filters(component_type, name)
            if not filters:
                continue
            details.append(
                {
                    "type": component_type,
                    "name": name,
                    "actions": filters.get("action", []),
                    "categories": filters.get("category", []),
                    "data": filters.get("data", []),
                }
            )
    return details


def top_counter(counter, limit=20):
    return [{"name": name, "count": count} for name, count in counter.most_common(limit)]


def analyze(apk_path):
    apk_path = Path(apk_path)
    OUT_DIR.mkdir(parents=True, exist_ok=True)

    quick_apk = APK(str(apk_path), skip_analysis=True)
    apk, dex_files, analysis = AnalyzeAPK(str(apk_path))
    meta_apk = apk
    manifest_details = component_details(meta_apk)

    all_classes = []
    all_methods_count = 0
    dex_summaries = []
    for index, dex in enumerate(dex_files, start=1):
        classes = dex.get_classes()
        methods_count = dex.get_len_methods()
        all_classes.extend(classes)
        all_methods_count += methods_count
        dex_summaries.append(
            {
                "index": index,
                "classes": len(classes),
                "methods": methods_count,
                "fields": dex.get_len_fields(),
                "strings": dex.get_len_strings(),
            }
        )

    class_names = [cls.get_name() for cls in all_classes]
    app_classes = [name for name in class_names if name.startswith(APP_CLASS_PREFIX)]
    app_class_set = set(app_classes)

    app_methods_count = 0
    app_class_method_samples = []
    for cls in all_classes:
        class_name = cls.get_name()
        if not class_name.startswith(APP_CLASS_PREFIX):
            continue
        methods = cls.get_methods()
        app_methods_count += len(methods)
        if len(app_class_method_samples) < 15:
            app_class_method_samples.append(
                {
                    "class": dex_name_to_java(class_name),
                    "methods": [m.get_name() for m in methods[:8]],
                    "method_count": len(methods),
                }
            )

    activities = list(meta_apk.get_activities())
    services = list(meta_apk.get_services())
    receivers = list(meta_apk.get_receivers())
    providers = list(meta_apk.get_providers())
    component_lists = {
        "activitys": activities,
        "services": services,
        "receivers": receivers,
    }
    intent_filters = intent_filter_details(meta_apk, component_lists)
    permissions = list(meta_apk.get_permissions())
    files = list(meta_apk.get_files())
    native_libs = sorted([f for f in files if f.startswith("lib/") and f.endswith(".so")])

    call_graph = analysis.get_call_graph(classname=APP_CLASS_REGEX, no_isolated=True)
    degree_items = []
    app_degree_items = []
    external_api_counter = Counter()
    internal_dependency_counter = Counter()
    for node in call_graph.nodes:
        item = {
            "method": method_label(node),
            "in_degree": call_graph.in_degree(node),
            "out_degree": call_graph.out_degree(node),
            "total_degree": call_graph.degree(node),
        }
        degree_items.append(item)
        if method_class_name(node).startswith(APP_CLASS_PREFIX):
            app_degree_items.append(item)
    degree_items.sort(key=lambda item: item["total_degree"], reverse=True)
    app_degree_items.sort(key=lambda item: item["total_degree"], reverse=True)

    edge_samples = []
    for index, (source, target) in enumerate(call_graph.edges()):
        if index < 30:
            edge_samples.append({"from": method_label(source), "to": method_label(target)})
        target_package = external_package_from_method(target)
        if method_class_name(source).startswith(APP_CLASS_PREFIX) and target_package:
            external_api_counter[target_package] += 1
        source_class = method_class_name(source)
        target_class = method_class_name(target)
        if source_class.startswith(APP_CLASS_PREFIX) and target_class.startswith(APP_CLASS_PREFIX):
            source_pkg = package_name(source_class, 5)
            target_pkg = package_name(target_class, 5)
            if source_pkg != target_pkg:
                internal_dependency_counter[f"{source_pkg} -> {target_pkg}"] += 1

    exported_components = [
        item
        for item in manifest_details["components"]
        if item["exported_bool"] or (item["exported"] is None and item["has_intent_filter"])
    ]

    report = {
        "generated_at": datetime.now().isoformat(timespec="seconds"),
        "apk_path": str(apk_path.resolve()),
        "basic": {
            "is_valid_apk": meta_apk.is_valid_APK(),
            "app_name": meta_apk.get_app_name(),
            "package": meta_apk.get_package(),
            "version_code": meta_apk.get_androidversion_code(),
            "version_name": meta_apk.get_androidversion_name(),
            "min_sdk": meta_apk.get_min_sdk_version(),
            "target_sdk": meta_apk.get_target_sdk_version(),
            "main_activity": meta_apk.get_main_activity(),
        },
        "components": {
            "activities_count": len(activities),
            "services_count": len(services),
            "receivers_count": len(receivers),
            "providers_count": len(providers),
            "activities": activities,
            "services": services,
            "receivers": receivers,
            "providers": providers,
            "application": manifest_details["application"],
            "exported_components_count": len(exported_components),
            "exported_components": exported_components,
            "intent_filters_count": len(intent_filters),
            "intent_filters": intent_filters,
        },
        "permissions": {
            "count": len(permissions),
            "items": sorted(permissions),
        },
        "files": {
            "count": len(files),
            "dex_names": list(meta_apk.get_dex_names()),
            "native_libs_count": len(native_libs),
            "native_libs_sample": native_libs[:30],
            "extension_top": top_counter(Counter(extension_name(f) for f in files), 15),
        },
        "dex": {
            "dex_count": len(dex_files),
            "total_classes": len(class_names),
            "total_methods": all_methods_count,
            "app_classes": len(app_classes),
            "app_methods": app_methods_count,
            "external_or_library_classes": len(class_names) - len(app_classes),
            "dex_summaries": dex_summaries,
            "top_packages": top_counter(Counter(package_name(name, 3) for name in class_names), 20),
            "top_app_packages": top_counter(Counter(package_name(name, 4) for name in app_classes), 20),
            "app_class_samples": [dex_name_to_java(name) for name in sorted(app_classes)[:50]],
            "app_class_method_samples": app_class_method_samples,
        },
        "call_graph": {
            "scope": "classes under com.junkfood.seal",
            "nodes": call_graph.number_of_nodes(),
            "edges": call_graph.number_of_edges(),
            "top_degree_methods": degree_items[:20],
            "top_app_degree_methods": app_degree_items[:20],
            "top_external_api_packages": top_counter(external_api_counter, 20),
            "top_internal_package_dependencies": top_counter(internal_dependency_counter, 20),
            "edge_samples": edge_samples,
        },
        "signatures": {
            "is_signed_v1": meta_apk.is_signed_v1(),
            "is_signed_v2": meta_apk.is_signed_v2(),
            "is_signed_v3": meta_apk.is_signed_v3(),
            "signature_names": list(meta_apk.get_signature_names()),
        },
    }

    json_path = OUT_DIR / "seal-androguard-static-analysis.json"
    json_path.write_text(json.dumps(report, ensure_ascii=False, indent=2), encoding="utf-8")
    write_markdown(report, OUT_DIR / "seal-androguard-static-analysis.md")

    print("Androguard deep static analysis complete")
    print(f"JSON: {json_path}")
    print(f"Markdown: {OUT_DIR / 'seal-androguard-static-analysis.md'}")
    print(f"Package: {report['basic']['package']}")
    print(f"Components: activities={report['components']['activities_count']}, services={report['components']['services_count']}, receivers={report['components']['receivers_count']}, providers={report['components']['providers_count']}")
    print(f"Classes: total={report['dex']['total_classes']}, app={report['dex']['app_classes']}")
    print(f"Methods: total={report['dex']['total_methods']}, app={report['dex']['app_methods']}")
    print(f"Call graph: nodes={report['call_graph']['nodes']}, edges={report['call_graph']['edges']}")


def md_list(items):
    if not items:
        return "- 无"
    return "\n".join(f"- `{item}`" for item in items)


def write_markdown(report, path):
    basic = report["basic"]
    comp = report["components"]
    dex = report["dex"]
    call_graph = report["call_graph"]

    lines = [
        "# 基于 Androguard 的 APK 静态分析报告",
        "",
        "## 一、分析对象",
        "",
        f"- APK 文件：`{report['apk_path']}`",
        f"- 应用名称：`{basic['app_name']}`",
        f"- 包名：`{basic['package']}`",
        f"- 版本：`{basic['version_name']}`",
        f"- versionCode：`{basic['version_code']}`",
        f"- minSdk / targetSdk：`{basic['min_sdk']}` / `{basic['target_sdk']}`",
        f"- 主 Activity：`{basic['main_activity']}`",
        "",
        "## 二、Manifest 与组件信息",
        "",
        f"- application 类：`{comp['application'].get('name', '')}`",
        f"- debuggable：`{comp['application'].get('debuggable')}`",
        f"- allowBackup：`{comp['application'].get('allowBackup')}`",
        f"- exported 组件数量：`{comp['exported_components_count']}`",
        "",
        "| 类型 | 数量 |",
        "| --- | ---: |",
        f"| Activity | {comp['activities_count']} |",
        f"| Service | {comp['services_count']} |",
        f"| Receiver | {comp['receivers_count']} |",
        f"| Provider | {comp['providers_count']} |",
        "",
        "### Activity 列表",
        md_list(comp["activities"]),
        "",
        "### Service 列表",
        md_list(comp["services"]),
        "",
        "### Receiver 列表",
        md_list(comp["receivers"]),
        "",
        "### Provider 列表",
        md_list(comp["providers"]),
        "",
        "### Exported 组件",
        "",
        "| 类型 | 名称 | exported | intent-filter |",
        "| --- | --- | --- | --- |",
    ]
    for item in comp["exported_components"]:
        lines.append(
            f"| `{item['type']}` | `{item['name']}` | `{item['exported']}` | `{item['has_intent_filter']}` |"
        )

    lines.extend(
        [
            "",
            "### Intent Filter 明细",
            "",
            f"- 含 Intent Filter 的组件数量：`{comp['intent_filters_count']}`",
            "",
            "| 类型 | 组件 | action | category | data |",
            "| --- | --- | --- | --- | --- |",
        ]
    )
    for item in comp["intent_filters"]:
        actions = ", ".join(item["actions"])
        categories = ", ".join(item["categories"])
        data_items = ", ".join(str(data) for data in item["data"])
        lines.append(
            f"| `{item['type']}` | `{item['name']}` | `{actions}` | `{categories}` | `{data_items}` |"
        )

    lines.extend(
        [
        "## 三、权限信息",
        "",
        f"- 权限数量：`{report['permissions']['count']}`",
        "",
        md_list(report["permissions"]["items"]),
        "",
        "## 四、文件与 DEX 信息",
        "",
        "| 指标 | 数值 |",
        "| --- | ---: |",
        f"| APK 内文件数量 | {report['files']['count']} |",
        f"| DEX 文件数量 | {dex['dex_count']} |",
        f"| 总类数量 | {dex['total_classes']} |",
        f"| 项目自定义类数量 | {dex['app_classes']} |",
        f"| 第三方或系统类数量 | {dex['external_or_library_classes']} |",
        f"| 总方法数量 | {dex['total_methods']} |",
        f"| 项目自定义方法数量 | {dex['app_methods']} |",
        f"| native so 文件数量 | {report['files']['native_libs_count']} |",
        "",
        "### DEX 分布",
        "",
        "| DEX 序号 | 类数量 | 方法数量 | 字段数量 | 字符串数量 |",
        "| ---: | ---: | ---: | ---: | ---: |",
    ]
    )
    for item in dex["dex_summaries"]:
        lines.append(
            f"| {item['index']} | {item['classes']} | {item['methods']} | {item['fields']} | {item['strings']} |"
        )

    lines.extend(
        [
            "",
            "### 项目自定义包分布 Top 20",
            "",
            "| 包名 | 类数量 |",
            "| --- | ---: |",
        ]
    )
    for item in dex["top_app_packages"]:
        lines.append(f"| `{item['name']}` | {item['count']} |")

    lines.extend(
        [
            "",
            "### 自定义类样例",
            "",
            md_list(dex["app_class_samples"][:30]),
            "",
            "## 五、调用关系摘要",
            "",
            f"- 调用图范围：`{call_graph['scope']}`",
            f"- 调用图节点数：`{call_graph['nodes']}`",
            f"- 调用图边数：`{call_graph['edges']}`",
            "",
        "### 连接度最高的方法 Top 20",
        "",
        "| 方法 | 入度 | 出度 | 总度 |",
        "| --- | ---: | ---: | ---: |",
        ]
    )
    for item in call_graph["top_degree_methods"]:
        lines.append(
            f"| `{item['method']}` | {item['in_degree']} | {item['out_degree']} | {item['total_degree']} |"
        )

    lines.extend(
        [
            "",
            "### 项目自定义方法连接度 Top 20",
            "",
            "| 方法 | 入度 | 出度 | 总度 |",
            "| --- | ---: | ---: | ---: |",
        ]
    )
    for item in call_graph["top_app_degree_methods"]:
        lines.append(
            f"| `{item['method']}` | {item['in_degree']} | {item['out_degree']} | {item['total_degree']} |"
        )

    lines.extend(
        [
            "",
            "### 项目代码调用的外部 API 包 Top 20",
            "",
            "| 外部包 | 调用次数 |",
            "| --- | ---: |",
        ]
    )
    for item in call_graph["top_external_api_packages"]:
        lines.append(f"| `{item['name']}` | {item['count']} |")

    lines.extend(
        [
            "",
            "### 项目内部包依赖关系 Top 20",
            "",
            "| 依赖关系 | 调用次数 |",
            "| --- | ---: |",
        ]
    )
    for item in call_graph["top_internal_package_dependencies"]:
        lines.append(f"| `{item['name']}` | {item['count']} |")

    lines.extend(
        [
            "",
            "### 调用边样例",
            "",
            "| 调用方 | 被调用方 |",
            "| --- | --- |",
        ]
    )
    for edge in call_graph["edge_samples"][:20]:
        lines.append(f"| `{edge['from']}` | `{edge['to']}` |")

    lines.extend(
        [
            "",
            "## 六、签名信息",
            "",
            f"- v1 签名：`{report['signatures']['is_signed_v1']}`",
            f"- v2 签名：`{report['signatures']['is_signed_v2']}`",
            f"- v3 签名：`{report['signatures']['is_signed_v3']}`",
            f"- 签名文件：`{', '.join(report['signatures']['signature_names'])}`",
            "",
            "## 七、结论",
            "",
            "本次分析使用 Androguard 对 Seal APK 进行了静态分析，覆盖 APK 基础信息、Manifest 组件、权限、文件结构、DEX 类与方法统计，以及项目自定义类范围内的调用关系摘要。结果表明，该 APK 包含多个 DEX 文件，项目自定义类集中在 `com.junkfood.seal` 包下；应用声明了网络、外部存储、通知、安装包请求、忽略电池优化等权限；主入口为 `com.junkfood.seal.MainActivity`。该 debug 包 `debuggable=true`，适合课程测试，但正式发布包应关闭调试属性。调用图摘要可用于后续定位核心业务类、高连接度方法和项目代码依赖的外部 API。",
            "",
        ]
    )

    path.write_text("\n".join(lines), encoding="utf-8")


if __name__ == "__main__":
    apk_arg = sys.argv[1] if len(sys.argv) > 1 else DEFAULT_APK
    analyze(apk_arg)
