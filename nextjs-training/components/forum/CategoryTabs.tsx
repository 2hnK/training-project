'use client';

import { Tabs, TabsList, TabsTrigger } from '@/components/ui';
import { Category } from '@/types/post';

interface CategoryTabsProps {
  defaultValue?: Category;
  onValueChange?: (value: string) => void;
}

export function CategoryTabs({
  defaultValue = '전체',
  onValueChange,
}: CategoryTabsProps) {
  return (
    <Tabs
      defaultValue={defaultValue}
      onValueChange={onValueChange}
      className="w-full"
    >
      <TabsList className="w-full justify-start bg-transparent border-b rounded-none h-auto p-0">
        <TabsTrigger
          value="전체"
          className="rounded-none border-b-2 border-transparent data-[state=active]:border-blue-600 data-[state=active]:bg-transparent px-6"
        >
          전체
        </TabsTrigger>
        <TabsTrigger
          value="공지사항"
          className="rounded-none border-b-2 border-transparent data-[state=active]:border-blue-600 data-[state=active]:bg-transparent px-6"
        >
          공지사항
        </TabsTrigger>
        <TabsTrigger
          value="질문"
          className="rounded-none border-b-2 border-transparent data-[state=active]:border-blue-600 data-[state=active]:bg-transparent px-6"
        >
          질문
        </TabsTrigger>
        <TabsTrigger
          value="정보"
          className="rounded-none border-b-2 border-transparent data-[state=active]:border-blue-600 data-[state=active]:bg-transparent px-6"
        >
          정보
        </TabsTrigger>
        <TabsTrigger
          value="자유"
          className="rounded-none border-b-2 border-transparent data-[state=active]:border-blue-600 data-[state=active]:bg-transparent px-6"
        >
          자유
        </TabsTrigger>
        <TabsTrigger
          value="Tips"
          className="rounded-none border-b-2 border-transparent data-[state=active]:border-blue-600 data-[state=active]:bg-transparent px-6"
        >
          Tips
        </TabsTrigger>
      </TabsList>
    </Tabs>
  );
}
