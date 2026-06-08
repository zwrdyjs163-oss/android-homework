import argparse
import html
import json
import math
import re
from collections import Counter, defaultdict, deque
from pathlib import Path


ROOT = Path(__file__).resolve().parent
ANSI_RE = re.compile(r"\x1b\[[0-9;]*[A-Za-z]")
JSON_RE = re.compile(r"(\{.*\})")
TID_RE = re.compile(r"TID\s+(0x[0-9a-fA-F]+|\d+)")
JAVA_METHOD_RE = re.compile(
    r"(?P<method>(?:[A-Za-z_$][\w$]*\.)+[A-Za-z_$][\w$<>]*(?:\([^)]*\))?)"
)


def clean_line(line):
    return ANSI_RE.sub("", line.rstrip("\n"))


def read_json_events(lines):
    events = []
    for line in lines:
        match = JSON_RE.search(line)
        if not match:
            continue
        try:
            event = json.loads(match.group(1))
        except json.JSONDecodeError:
            continue
        if isinstance(event, dict) and event.get("type") in {"enter", "exit", "throw"}:
            events.append(event)
    return events


def edges_from_json_events(events):
    stacks = defaultdict(list)
    edges = Counter()
    methods = Counter()
    thrown = Counter()

    for event in events:
        event_type = event.get("type")
        signature = event.get("signature") or event.get("method") or event.get("name")
        thread = str(event.get("thread") or event.get("tid") or "main")
        if event_type == "enter" and signature:
            caller = stacks[thread][-1] if stacks[thread] else "<entry>"
            edges[(caller, signature)] += 1
            methods[signature] += 1
            stacks[thread].append(signature)
        elif event_type in {"exit", "throw"} and signature:
            if event_type == "throw":
                thrown[signature] += 1
            if stacks[thread]:
                if stacks[thread][-1] == signature:
                    stacks[thread].pop()
                elif signature in stacks[thread]:
                    stacks[thread].remove(signature)

    return edges, methods, thrown, len(events), "json"


def extract_trace_rows(lines):
    rows = []
    current_thread = "main"

    for index, raw_line in enumerate(lines):
        line = clean_line(raw_line)
        tid_match = TID_RE.search(line)
        if tid_match:
            current_thread = tid_match.group(1)

        if "Started tracing" in line or "Instrumenting" in line or "Resolving" in line:
            continue

        if line.strip().startswith("<="):
            rows.append({"type": "exit", "line": index + 1, "thread": current_thread, "raw": line})
            continue

        match = None
        for candidate in JAVA_METHOD_RE.finditer(line):
            method = candidate.group("method")
            if "." not in method:
                continue
            match = candidate
            break
        if not match:
            continue

        method = normalize_method(match.group("method"))
        if not looks_like_app_method(method):
            continue

        rows.append(
            {
                "type": "enter",
                "line": index + 1,
                "thread": current_thread,
                "column": match.start("method"),
                "method": method,
                "raw": line,
            }
        )

    return rows


def normalize_method(method):
    method = method.strip()
    method = method.replace("com.junkfood.seal.", "")
    method = re.sub(r"\(.*\)$", "()", method)
    method = re.sub(r"\s+", " ", method)
    return method


def looks_like_app_method(method):
    if method.startswith(("android.", "androidx.", "java.", "kotlin.", "kotlinx.")):
        return False
    return "." in method


def edges_from_trace_rows(rows):
    if not rows:
        return Counter(), Counter(), Counter(), 0, "frida-trace"

    if any(row.get("type") == "exit" for row in rows):
        stacks = defaultdict(list)
        edges = Counter()
        methods = Counter()
        enter_count = 0

        for row in rows:
            thread = row["thread"]
            stack = stacks[thread]
            if row.get("type") == "exit":
                if stack:
                    stack.pop()
                continue

            method = row["method"]
            caller = stack[-1] if stack else "<entry>"
            edges[(caller, method)] += 1
            methods[method] += 1
            stack.append(method)
            enter_count += 1

        return edges, methods, Counter(), enter_count, "frida-trace"

    min_column_by_thread = {}
    for row in rows:
        if row.get("type") != "enter":
            continue
        thread = row["thread"]
        min_column_by_thread[thread] = min(row["column"], min_column_by_thread.get(thread, row["column"]))

    stacks = defaultdict(list)
    edges = Counter()
    methods = Counter()

    for row in rows:
        if row.get("type") != "enter":
            continue
        thread = row["thread"]
        base = min_column_by_thread.get(thread, row["column"])
        depth = max(0, int(round((row["column"] - base) / 2.0)))
        stack = stacks[thread]
        while len(stack) > depth:
            stack.pop()
        caller = stack[-1] if stack else "<entry>"
        method = row["method"]
        edges[(caller, method)] += 1
        methods[method] += 1
        stack.append(method)

    return edges, methods, Counter(), len(rows), "frida-trace"


def parse_trace(path):
    lines = Path(path).read_text(encoding="utf-8", errors="replace").splitlines()
    json_events = read_json_events(lines)
    if json_events:
        return edges_from_json_events(json_events)
    rows = extract_trace_rows(lines)
    return edges_from_trace_rows(rows)


def short_name(name, max_len=88):
    if not name:
        return "<unknown>"
    name = name.replace("com.junkfood.seal.", "")
    if len(name) > max_len:
        return name[: max_len - 3] + "..."
    return name


def graph_layout(edges, methods, max_edges=80):
    selected_edges = edges.most_common(max_edges)
    nodes = {"<entry>"}
    adjacency = defaultdict(list)
    indegree = Counter()

    for (source, target), count in selected_edges:
        nodes.add(source)
        nodes.add(target)
        adjacency[source].append(target)
        indegree[target] += 1

    levels = {"<entry>": 0}
    queue = deque(["<entry>"])
    while queue:
        node = queue.popleft()
        for child in adjacency.get(node, []):
            next_level = min(levels[node] + 1, 5)
            if child not in levels or next_level < levels[child]:
                levels[child] = next_level
                queue.append(child)

    for node in nodes:
        if node not in levels:
            levels[node] = 1 if indegree[node] == 0 else 2

    buckets = defaultdict(list)
    for node in nodes:
        buckets[levels[node]].append(node)
    for level in buckets:
        buckets[level].sort(key=lambda n: (-methods.get(n, 0), n))

    width = max(960, (max(buckets.keys(), default=0) + 1) * 260)
    height = max(520, max((len(v) for v in buckets.values()), default=1) * 82 + 80)
    positions = {}
    for level, bucket in buckets.items():
        x = 40 + level * 250
        gap = max(68, (height - 80) / max(len(bucket), 1))
        for index, node in enumerate(bucket):
            y = 50 + index * gap
            positions[node] = (x, y)

    return selected_edges, positions, width, height


def write_html(edges, methods, thrown, event_count, source_type, output_path):
    selected_edges, positions, width, height = graph_layout(edges, methods)

    edge_lines = []
    for (source, target), count in selected_edges:
        if source not in positions or target not in positions:
            continue
        x1, y1 = positions[source]
        x2, y2 = positions[target]
        stroke_width = 1.2 + min(5, math.log(count + 1))
        edge_lines.append(
            f'<line x1="{x1 + 180}" y1="{y1 + 18}" x2="{x2}" y2="{y2 + 18}" '
            f'stroke="#6b7280" stroke-width="{stroke_width:.2f}" marker-end="url(#arrow)" />'
        )
        mid_x = (x1 + x2 + 180) / 2
        mid_y = (y1 + y2 + 36) / 2
        edge_lines.append(
            f'<text x="{mid_x:.1f}" y="{mid_y:.1f}" class="edge-label">{count}</text>'
        )

    node_rects = []
    for node, (x, y) in positions.items():
        label = html.escape(short_name(node, 42))
        calls = methods.get(node, 0)
        css_class = "entry-node" if node == "<entry>" else "method-node"
        node_rects.append(
            f'<g class="{css_class}">'
            f'<rect x="{x}" y="{y}" width="180" height="38" rx="6"></rect>'
            f'<title>{html.escape(node)} calls={calls}</title>'
            f'<text x="{x + 8}" y="{y + 23}">{label}</text>'
            f"</g>"
        )

    method_rows = []
    for method, count in methods.most_common(80):
        method_rows.append(
            f"<tr><td><code>{html.escape(short_name(method, 180))}</code></td><td>{count}</td></tr>"
        )

    edge_rows = []
    for (source, target), count in edges.most_common(160):
        edge_rows.append(
            "<tr>"
            f"<td><code>{html.escape(short_name(source, 140))}</code></td>"
            f"<td><code>{html.escape(short_name(target, 140))}</code></td>"
            f"<td>{count}</td>"
            "</tr>"
        )

    thrown_rows = []
    for method, count in thrown.most_common(40):
        thrown_rows.append(
            f"<tr><td><code>{html.escape(short_name(method, 180))}</code></td><td>{count}</td></tr>"
        )
    if not thrown_rows:
        thrown_rows.append('<tr><td colspan="2">未记录到异常事件</td></tr>')

    document = f"""<!doctype html>
<html lang="zh-CN">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>frida-trace 方法调用图</title>
  <style>
    body {{ margin: 0; font-family: Arial, "Microsoft YaHei", sans-serif; color: #172033; background: #f7f8fb; }}
    header {{ padding: 22px 28px; background: #172033; color: #fff; }}
    h1 {{ margin: 0 0 8px; font-size: 24px; }}
    main {{ padding: 22px 28px 40px; }}
    .stats {{ display: grid; grid-template-columns: repeat(auto-fit, minmax(150px, 1fr)); gap: 10px; margin: 0 0 18px; }}
    .stat {{ background: #fff; border: 1px solid #d9dee8; border-radius: 8px; padding: 12px; }}
    .stat strong {{ display: block; font-size: 22px; margin-top: 5px; }}
    .panel {{ background: #fff; border: 1px solid #d9dee8; border-radius: 8px; margin-top: 16px; overflow: hidden; }}
    .panel h2 {{ font-size: 18px; margin: 0; padding: 14px 16px; border-bottom: 1px solid #e6e9f0; }}
    .graph-wrap {{ overflow: auto; padding: 12px; }}
    svg {{ background: #fcfdff; border: 1px solid #e6e9f0; border-radius: 6px; }}
    .method-node rect {{ fill: #e8f2ff; stroke: #4d82c4; }}
    .entry-node rect {{ fill: #fff4d8; stroke: #c28a12; }}
    text {{ font-size: 11px; fill: #172033; }}
    .edge-label {{ font-size: 10px; fill: #374151; paint-order: stroke; stroke: #fff; stroke-width: 3px; }}
    table {{ width: 100%; border-collapse: collapse; }}
    th, td {{ border-top: 1px solid #e6e9f0; padding: 8px 10px; text-align: left; font-size: 13px; vertical-align: top; }}
    th {{ background: #f1f4f8; }}
    code {{ white-space: normal; word-break: break-all; }}
    input {{ width: calc(100% - 32px); margin: 12px 16px; padding: 9px 10px; border: 1px solid #cfd6e3; border-radius: 6px; }}
  </style>
</head>
<body>
  <header>
    <h1>frida-trace 方法调用图</h1>
    <div>来源：{html.escape(source_type)} 日志，结果可直接用浏览器打开。</div>
  </header>
  <main>
    <section class="stats">
      <div class="stat">日志事件<strong>{event_count}</strong></div>
      <div class="stat">方法节点<strong>{len(methods)}</strong></div>
      <div class="stat">调用边<strong>{len(edges)}</strong></div>
      <div class="stat">异常方法<strong>{len(thrown)}</strong></div>
    </section>
    <section class="panel">
      <h2>调用图</h2>
      <div class="graph-wrap">
        <svg width="{width}" height="{height}" role="img" aria-label="frida-trace call graph">
          <defs>
            <marker id="arrow" markerWidth="10" markerHeight="10" refX="8" refY="3" orient="auto" markerUnits="strokeWidth">
              <path d="M0,0 L0,6 L9,3 z" fill="#6b7280"></path>
            </marker>
          </defs>
          {''.join(edge_lines)}
          {''.join(node_rects)}
        </svg>
      </div>
    </section>
    <section class="panel">
      <h2>高频方法</h2>
      <input id="methodSearch" placeholder="搜索方法名">
      <table id="methodTable"><thead><tr><th>方法</th><th>次数</th></tr></thead><tbody>{''.join(method_rows)}</tbody></table>
    </section>
    <section class="panel">
      <h2>高频调用关系</h2>
      <input id="edgeSearch" placeholder="搜索调用方或被调用方">
      <table id="edgeTable"><thead><tr><th>调用方</th><th>被调用方</th><th>次数</th></tr></thead><tbody>{''.join(edge_rows)}</tbody></table>
    </section>
    <section class="panel">
      <h2>异常事件</h2>
      <table><thead><tr><th>方法</th><th>次数</th></tr></thead><tbody>{''.join(thrown_rows)}</tbody></table>
    </section>
  </main>
  <script>
    function bindSearch(inputId, tableId) {{
      const input = document.getElementById(inputId);
      const rows = Array.from(document.querySelectorAll(`#${{tableId}} tbody tr`));
      input.addEventListener('input', () => {{
        const text = input.value.toLowerCase();
        for (const row of rows) {{
          row.style.display = row.textContent.toLowerCase().includes(text) ? '' : 'none';
        }}
      }});
    }}
    bindSearch('methodSearch', 'methodTable');
    bindSearch('edgeSearch', 'edgeTable');
  </script>
</body>
</html>
"""
    output_path.write_text(document, encoding="utf-8")


def write_summary(edges, methods, thrown, event_count, source_type, output_path):
    lines = [
        "# frida-trace 方法调用图摘要",
        "",
        f"- 日志来源：`{source_type}`",
        f"- 日志事件数量：`{event_count}`",
        f"- 方法节点数量：`{len(methods)}`",
        f"- 调用边数量：`{len(edges)}`",
        f"- 异常方法数量：`{len(thrown)}`",
        "",
        "## 高频方法 Top 30",
        "",
        "| 方法 | 次数 |",
        "| --- | ---: |",
    ]
    for method, count in methods.most_common(30):
        lines.append(f"| `{short_name(method, 160)}` | {count} |")

    lines.extend(["", "## 高频调用关系 Top 30", "", "| 调用方 | 被调用方 | 次数 |", "| --- | --- | ---: |"])
    for (source, target), count in edges.most_common(30):
        lines.append(f"| `{short_name(source, 100)}` | `{short_name(target, 100)}` | {count} |")

    output_path.write_text("\n".join(lines), encoding="utf-8")


def write_json(edges, methods, thrown, event_count, source_type, output_path):
    data = {
        "source_type": source_type,
        "event_count": event_count,
        "method_count": len(methods),
        "edge_count": len(edges),
        "exception_method_count": len(thrown),
        "methods": [{"method": method, "count": count} for method, count in methods.most_common()],
        "edges": [
            {"source": source, "target": target, "count": count}
            for (source, target), count in edges.most_common()
        ],
        "exceptions": [{"method": method, "count": count} for method, count in thrown.most_common()],
    }
    output_path.write_text(json.dumps(data, ensure_ascii=False, indent=2), encoding="utf-8")


def main():
    parser = argparse.ArgumentParser(description="Build a browsable method call graph from frida-trace output.")
    parser.add_argument("trace_log", help="frida-trace output log path")
    parser.add_argument("--out-dir", default=str(ROOT / "frida-trace-callgraph"), help="output directory")
    args = parser.parse_args()

    trace_path = Path(args.trace_log)
    out_dir = Path(args.out_dir)
    out_dir.mkdir(parents=True, exist_ok=True)

    edges, methods, thrown, event_count, source_type = parse_trace(trace_path)

    html_path = out_dir / "frida-trace-callgraph.html"
    summary_path = out_dir / "frida-trace-callgraph-summary.md"
    json_path = out_dir / "frida-trace-callgraph.json"

    write_html(edges, methods, thrown, event_count, source_type, html_path)
    write_summary(edges, methods, thrown, event_count, source_type, summary_path)
    write_json(edges, methods, thrown, event_count, source_type, json_path)

    print(f"source={source_type} events={event_count} methods={len(methods)} edges={len(edges)}")
    print(f"written: {html_path}")
    print(f"written: {summary_path}")
    print(f"written: {json_path}")


if __name__ == "__main__":
    main()
