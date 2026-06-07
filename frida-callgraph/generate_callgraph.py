import html
import json
import re
import sys
from collections import Counter, defaultdict
from pathlib import Path


ROOT = Path(__file__).resolve().parent


def parse_events(path):
    events = []
    for raw_line in Path(path).read_text(encoding="utf-8", errors="replace").splitlines():
        match = re.search(r"(\{.*\})", raw_line)
        if not match:
            continue
        try:
            event = json.loads(match.group(1))
        except json.JSONDecodeError:
            continue
        if isinstance(event, dict):
            events.append(event)
    return events


def short_name(signature):
    if not signature:
        return "<unknown>"
    signature = signature.replace("com.junkfood.seal.", "")
    return signature


def build_edges(events):
    stacks = defaultdict(list)
    edges = Counter()
    methods = Counter()
    thrown = Counter()

    for event in events:
        event_type = event.get("type")
        signature = event.get("signature")
        thread = event.get("thread", "main")
        if event_type == "enter" and signature:
            caller = stacks[thread][-1] if stacks[thread] else "<entry>"
            edges[(caller, signature)] += 1
            methods[signature] += 1
            stacks[thread].append(signature)
        elif event_type in ("exit", "throw") and signature:
            if event_type == "throw":
                thrown[signature] += 1
            if stacks[thread]:
                if stacks[thread][-1] == signature:
                    stacks[thread].pop()
                elif signature in stacks[thread]:
                    stacks[thread].remove(signature)
    return edges, methods, thrown


def write_dot(edges, output_path):
    lines = ["digraph FridaCallGraph {", "  rankdir=LR;", "  node [shape=box, fontsize=10];"]
    for (source, target), count in edges.most_common(400):
        lines.append(
            f'  "{short_name(source)}" -> "{short_name(target)}" [label="{count}"];'
        )
    lines.append("}")
    output_path.write_text("\n".join(lines), encoding="utf-8")


def write_html(edges, methods, output_path):
    rows = []
    for (source, target), count in edges.most_common(200):
        rows.append(
            "<tr>"
            f"<td>{html.escape(short_name(source))}</td>"
            f"<td>{html.escape(short_name(target))}</td>"
            f"<td>{count}</td>"
            "</tr>"
        )
    method_rows = []
    for method, count in methods.most_common(50):
        method_rows.append(
            f"<tr><td>{html.escape(short_name(method))}</td><td>{count}</td></tr>"
        )
    document = f"""<!doctype html>
<html lang="zh-CN">
<head>
  <meta charset="utf-8">
  <title>Frida Call Graph Summary</title>
  <style>
    body {{ font-family: Arial, sans-serif; margin: 24px; }}
    table {{ border-collapse: collapse; width: 100%; margin-bottom: 28px; }}
    th, td {{ border: 1px solid #ccc; padding: 6px 8px; text-align: left; font-size: 13px; }}
    th {{ background: #f3f3f3; }}
    code {{ background: #f5f5f5; padding: 2px 4px; }}
  </style>
</head>
<body>
  <h1>Frida 动态调用关系摘要</h1>
  <p>节点和边来自 <code>hook.js</code> 记录的运行时方法调用。</p>
  <h2>高频方法 Top 50</h2>
  <table><thead><tr><th>方法</th><th>调用次数</th></tr></thead><tbody>{''.join(method_rows)}</tbody></table>
  <h2>调用边 Top 200</h2>
  <table><thead><tr><th>调用方</th><th>被调用方</th><th>次数</th></tr></thead><tbody>{''.join(rows)}</tbody></table>
</body>
</html>
"""
    output_path.write_text(document, encoding="utf-8")


def write_summary(events, edges, methods, thrown, output_path):
    lines = [
        "# Frida 动态调用关系摘要",
        "",
        f"- trace 事件数量：`{len(events)}`",
        f"- 方法节点数量：`{len(methods)}`",
        f"- 调用边数量：`{len(edges)}`",
        f"- 异常事件方法数量：`{len(thrown)}`",
        "",
        "## 高频方法 Top 30",
        "",
        "| 方法 | 次数 |",
        "| --- | ---: |",
    ]
    for method, count in methods.most_common(30):
        lines.append(f"| `{short_name(method)}` | {count} |")
    lines.extend(["", "## 高频调用边 Top 30", "", "| 调用方 | 被调用方 | 次数 |", "| --- | --- | ---: |"])
    for (source, target), count in edges.most_common(30):
        lines.append(f"| `{short_name(source)}` | `{short_name(target)}` | {count} |")
    output_path.write_text("\n".join(lines), encoding="utf-8")


def main():
    if len(sys.argv) < 2:
        print("Usage: python generate_callgraph.py trace_output.json")
        raise SystemExit(2)

    trace_path = Path(sys.argv[1])
    events = parse_events(trace_path)
    edges, methods, thrown = build_edges(events)

    dot_path = ROOT / "callgraph.dot"
    html_path = ROOT / "callgraph.html"
    summary_path = ROOT / "callgraph-summary.md"

    write_dot(edges, dot_path)
    write_html(edges, methods, html_path)
    write_summary(events, edges, methods, thrown, summary_path)

    print(f"events={len(events)} methods={len(methods)} edges={len(edges)}")
    print(f"written: {dot_path}")
    print(f"written: {html_path}")
    print(f"written: {summary_path}")


if __name__ == "__main__":
    main()
