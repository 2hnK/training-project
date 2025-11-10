import os
import re

def remove_version_from_imports(file_path):
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()

    # @숫자.숫자.숫자 패턴을 찾아서 제거
    new_content = re.sub(r'"([^"]*@)\d+\.\d+\.\d+"', r'"\1"', content)

    if content != new_content:
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(new_content)
        print(f"Updated {file_path}")
        return True
    return False

def process_directory(dir_path):
    updated_count = 0
    for root, dirs, files in os.walk(dir_path):
        for file in files:
            if file.endswith('.tsx'):
                file_path = os.path.join(root, file)
                if remove_version_from_imports(file_path):
                    updated_count += 1
    return updated_count

updated = process_directory('nextjs-training/components/ui')
print(f"Total files updated: {updated}")
