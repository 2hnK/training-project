const fs = require('fs');
const path = require('path');

// 버전 패턴을 제거하는 함수
function removeVersionFromImport(line) {
  // @숫자.숫자.숫자 패턴을 찾아서 제거
  return line.replace(/"([^"]*@)\d+\.\d+\.\d+"/g, '"$1"');
}

// components/ui 폴더 내 모든 .tsx 파일 처리
function processDirectory(dirPath) {
  const files = fs.readdirSync(dirPath);

  files.forEach(file => {
    const filePath = path.join(dirPath, file);
    const stat = fs.statSync(filePath);

    if (stat.isDirectory()) {
      processDirectory(filePath);
    } else if (file.endsWith('.tsx')) {
      console.log(`Processing ${filePath}`);

      const content = fs.readFileSync(filePath, 'utf8');
      const lines = content.split('\n');
      const processedLines = lines.map(removeVersionFromImport);
      const newContent = processedLines.join('\n');

      if (content !== newContent) {
        fs.writeFileSync(filePath, newContent);
        console.log(`Updated ${filePath}`);
      }
    }
  });
}

processDirectory('nextjs-training/components/ui');
console.log('All files processed!');
