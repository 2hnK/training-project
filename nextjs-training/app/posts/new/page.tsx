'use client';

import { useState } from 'react';
import { Header } from '@/components/forum/Header';
import { Button } from '@/components/ui/button';
import { Card } from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import { Textarea } from '@/components/ui/textarea';
import { Label } from '@/components/ui/label';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import { Badge } from '@/components/ui/badge';
import { ArrowLeft, X } from 'lucide-react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { motion } from 'framer-motion';

export default function NewPostPage() {
  const router = useRouter();
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [category, setCategory] = useState('');
  const [tags, setTags] = useState<string[]>([]);
  const [tagInput, setTagInput] = useState('');

  const handleAddTag = () => {
    if (tagInput.trim() && !tags.includes(tagInput.trim())) {
      setTags([...tags, tagInput.trim()]);
      setTagInput('');
    }
  };

  const handleRemoveTag = (tagToRemove: string) => {
    setTags(tags.filter((tag) => tag !== tagToRemove));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    // TODO: API 연동 시 실제 제출 로직 구현
    console.log({
      title,
      content,
      category,
      tags,
    });

    // 임시로 목록 페이지로 이동
    alert('게시글이 작성되었습니다! (목업 모드)');
    router.push('/posts');
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <Header />

      <main className="container mx-auto px-4 py-8">
        <div className="max-w-4xl mx-auto space-y-6">
          <Link href="/posts">
            <Button variant="ghost" className="gap-2">
              <ArrowLeft className="h-4 w-4" />
              목록으로
            </Button>
          </Link>

          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.3 }}
          >
            <Card className="p-8">
              <h1 className="text-3xl font-bold mb-8">새 게시글 작성</h1>

              <form onSubmit={handleSubmit} className="space-y-6">
                <div className="space-y-2">
                  <Label htmlFor="category">카테고리</Label>
                  <Select value={category} onValueChange={setCategory} required>
                    <SelectTrigger id="category">
                      <SelectValue placeholder="카테고리를 선택하세요" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="공지사항">공지사항</SelectItem>
                      <SelectItem value="질문">질문</SelectItem>
                      <SelectItem value="정보">정보</SelectItem>
                      <SelectItem value="자유">자유</SelectItem>
                      <SelectItem value="Tips">Tips</SelectItem>
                    </SelectContent>
                  </Select>
                </div>

                <div className="space-y-2">
                  <Label htmlFor="title">제목</Label>
                  <Input
                    id="title"
                    placeholder="제목을 입력하세요"
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                    required
                    className="text-lg"
                  />
                </div>

                <div className="space-y-2">
                  <Label htmlFor="content">내용</Label>
                  <Textarea
                    id="content"
                    placeholder="내용을 입력하세요"
                    value={content}
                    onChange={(e) => setContent(e.target.value)}
                    required
                    className="min-h-80 text-base"
                  />
                  <p className="text-sm text-gray-500">
                    {content.length} 자
                  </p>
                </div>

                <div className="space-y-2">
                  <Label htmlFor="tags">태그</Label>
                  <div className="flex gap-2">
                    <Input
                      id="tags"
                      placeholder="태그를 입력하고 Enter 또는 추가 버튼을 클릭하세요"
                      value={tagInput}
                      onChange={(e) => setTagInput(e.target.value)}
                      onKeyDown={(e) => {
                        if (e.key === 'Enter') {
                          e.preventDefault();
                          handleAddTag();
                        }
                      }}
                    />
                    <Button
                      type="button"
                      variant="outline"
                      onClick={handleAddTag}
                    >
                      추가
                    </Button>
                  </div>
                  {tags.length > 0 && (
                    <div className="flex flex-wrap gap-2 mt-3">
                      {tags.map((tag) => (
                        <Badge
                          key={tag}
                          variant="secondary"
                          className="gap-1 pr-1"
                        >
                          #{tag}
                          <button
                            type="button"
                            onClick={() => handleRemoveTag(tag)}
                            className="ml-1 hover:bg-gray-300 rounded-full p-0.5"
                          >
                            <X className="h-3 w-3" />
                          </button>
                        </Badge>
                      ))}
                    </div>
                  )}
                </div>

                <div className="flex justify-end gap-3 pt-6">
                  <Link href="/posts">
                    <Button type="button" variant="outline">
                      취소
                    </Button>
                  </Link>
                  <Button type="submit" disabled={!title || !content || !category}>
                    게시글 작성
                  </Button>
                </div>
              </form>
            </Card>
          </motion.div>

          <Card className="p-6 bg-blue-50 border-blue-200">
            <h3 className="font-semibold mb-2 text-blue-900">
              작성 가이드
            </h3>
            <ul className="text-sm text-blue-800 space-y-1">
              <li>• 명확하고 구체적인 제목을 작성해주세요.</li>
              <li>
                • 다른 사용자들이 이해하기 쉽도록 내용을 자세히
                작성해주세요.
              </li>
              <li>• 적절한 카테고리를 선택해주세요.</li>
              <li>• 태그를 추가하면 게시글을 더 쉽게 찾을 수 있습니다.</li>
            </ul>
          </Card>
        </div>
      </main>
    </div>
  );
}
