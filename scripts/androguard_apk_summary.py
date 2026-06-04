import sys
from pathlib import Path

from loguru import logger

logger.remove()
logger.add(sys.stderr, level="ERROR")

from androguard.core.apk import APK


DEFAULT_APK = (
    Path(__file__).resolve().parents[1]
    / "bundletool-output"
    / "Seal-from-aab-universal-debug.apk"
)


def main():
    apk_path = Path(sys.argv[1]) if len(sys.argv) > 1 else DEFAULT_APK
    apk = APK(str(apk_path))

    print("Androguard APK Analysis")
    print(f"APK: {apk_path}")
    print(f"Package: {apk.get_package()}")
    print(f"Version code: {apk.get_androidversion_code()}")
    print(f"Version name: {apk.get_androidversion_name()}")
    print(f"Main activity: {apk.get_main_activity()}")
    print(f"Min SDK: {apk.get_min_sdk_version()}")
    print(f"Target SDK: {apk.get_target_sdk_version()}")
    print(f"Permissions count: {len(apk.get_permissions())}")
    print("Permissions:")
    for permission in sorted(apk.get_permissions()):
        print(f" - {permission}")


if __name__ == "__main__":
    main()
