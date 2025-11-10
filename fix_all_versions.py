import os
import re
import glob

def remove_version_from_file(filepath):
    """파일에서 버전 하드코딩을 제거"""
    try:
        with open(filepath, 'r', encoding='utf-8') as f:
            content = f.read()

        # @버전 패턴을 제거 (예: "package@1.2.3" -> "package")
        original_content = content
        content = re.sub(r'"([^"]+)@\d+\.\d+\.\d+"', r'"\1"', content)

        if content != original_content:
            with open(filepath, 'w', encoding='utf-8') as f:
                f.write(content)
            return True
        return False
    except Exception as e:
        print(f"Error processing {filepath}: {e}")
        return False

def main():
    ui_dir = "nextjs-training/components/ui"
    updated_files = []

    # 모든 .tsx 파일 찾기
    tsx_files = glob.glob(os.path.join(ui_dir, "*.tsx"))

    for tsx_file in tsx_files:
        if remove_version_from_file(tsx_file):
            updated_files.append(os.path.basename(tsx_file))

    print(f"Updated {len(updated_files)} files:")
    for file in sorted(updated_files):
        print(f"  - {file}")

if __name__ == "__main__":
    main()
