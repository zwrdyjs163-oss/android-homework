import json
import os
import re
import subprocess
import sys
import time
from datetime import datetime
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
LOCAL_APPIUM = ROOT / ".appium-python"
if LOCAL_APPIUM.exists():
    sys.path.insert(0, str(LOCAL_APPIUM))

from appium import webdriver
from appium.options.android import UiAutomator2Options


PACKAGE_NAME = os.getenv("APP_PACKAGE", "com.junkfood.seal.debug")
MAIN_ACTIVITY = os.getenv("APP_ACTIVITY", "com.junkfood.seal.MainActivity")
DEVICE_NAME = os.getenv("APPIUM_DEVICE_NAME", "emulator-5554")
SERVER_URL = os.getenv("APPIUM_SERVER_URL", "http://127.0.0.1:4723")
ADB = os.getenv("ADB", r"F:\AS_SDK\platform-tools\adb.exe")
OUT_DIR = ROOT / "docs" / "appium-performance-results"


def run_adb(*args):
    completed = subprocess.run(
        [ADB, *args],
        check=False,
        text=True,
        encoding="utf-8",
        errors="replace",
        stdout=subprocess.PIPE,
        stderr=subprocess.STDOUT,
    )
    return completed.stdout.strip()


def collect_cold_start():
    run_adb("shell", "am", "force-stop", PACKAGE_NAME)
    time.sleep(1)
    output = run_adb(
        "shell",
        "am",
        "start",
        "-W",
        "-n",
        f"{PACKAGE_NAME}/{MAIN_ACTIVITY}",
    )
    metrics = {}
    for key in ("ThisTime", "TotalTime", "WaitTime"):
        match = re.search(rf"{key}:\s*(\d+)", output)
        if match:
            metrics[key] = int(match.group(1))
    return {"raw": output, "metrics": metrics}


def create_driver():
    options = UiAutomator2Options()
    options.platform_name = "Android"
    options.automation_name = "UiAutomator2"
    options.device_name = DEVICE_NAME
    options.app_package = PACKAGE_NAME
    options.app_activity = MAIN_ACTIVITY
    options.no_reset = True
    options.auto_grant_permissions = True
    options.new_command_timeout = 120
    return webdriver.Remote(SERVER_URL, options=options)


def collect_appium_performance(driver):
    result = {}
    try:
        supported_types = driver.get_performance_data_types()
    except Exception as exc:
        return {"error": f"get_performance_data_types failed: {exc}"}

    result["supported_types"] = supported_types
    for perf_type in ("memoryinfo", "cpuinfo", "batteryinfo", "networkinfo"):
        if perf_type not in supported_types:
            continue
        try:
            result[perf_type] = driver.get_performance_data(PACKAGE_NAME, perf_type, 10)
        except Exception as exc:
            result[perf_type] = {"error": str(exc)}
    return result


def main():
    OUT_DIR.mkdir(parents=True, exist_ok=True)
    timestamp = datetime.now().strftime("%Y%m%d-%H%M%S")

    report = {
        "timestamp": timestamp,
        "device": DEVICE_NAME,
        "package": PACKAGE_NAME,
        "activity": MAIN_ACTIVITY,
        "cold_start": collect_cold_start(),
    }

    driver = create_driver()
    try:
        time.sleep(3)
        report["page_source_length"] = len(driver.page_source or "")
        report["window_size"] = driver.get_window_size()
        report["appium_performance"] = collect_appium_performance(driver)
    finally:
        driver.quit()

    output_path = OUT_DIR / f"appium-performance-{timestamp}.json"
    output_path.write_text(json.dumps(report, ensure_ascii=False, indent=2), encoding="utf-8")
    print(f"Saved performance report: {output_path}")


if __name__ == "__main__":
    main()
