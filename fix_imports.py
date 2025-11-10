#!/usr/bin/env python3
import os
import re

def fix_import_paths(root_dir):
    """Fix import paths in all .tsx files within the UI components directory."""
    for root, dirs, files in os.walk(root_dir):
        for file in files:
            if file.endswith('.tsx'):
                filepath = os.path.join(root, file)

                # Skip utils directory itself
                if 'utils' in filepath and filepath.endswith('utils/index.ts'):
                    continue

                with open(filepath, 'r', encoding='utf-8') as f:
                    content = f.read()

                # Replace './utils' with '../utils' in all files that are not in the root ui directory
                if './utils' in content:
                    # Check if this file is in a subdirectory (not directly in ui/)
                    relative_path = os.path.relpath(filepath, root_dir)
                    if os.sep in relative_path:  # It's in a subdirectory
                        content = content.replace('from "./utils"', 'from "../utils"')

                        with open(filepath, 'w', encoding='utf-8') as f:
                            f.write(content)
                        print(f"Fixed: {filepath}")

if __name__ == "__main__":
    ui_dir = r"C:\Users\kimgh\Documents\Document2025\training-project\nextjs-training\components\ui"
    fix_import_paths(ui_dir)
    print("Import path fixing completed!")

