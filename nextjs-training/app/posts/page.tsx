'use client';

import { useState } from 'react';
import { Header } from '@/components/forum/Header';
import { PostCard } from '@/components/forum/PostCard';
import { Sidebar } from '@/components/forum/Sidebar';
import { CategoryTabs } from '@/components/forum/CategoryTabs';
import { Button } from '@/components/ui/button';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import { Plus } from 'lucide-react';
import { motion } from 'framer-motion';
import { mockPosts } from '@/lib/mock-data';
import Link from 'next/link';
import { Category, SortBy } from '@/types/post';

export default function PostsPage() {
  const [sortBy, setSortBy] = useState<SortBy>('latest');
  const [selectedCategory, setSelectedCategory] = useState<Category>('전체');

  // 카테고리별 필터링
  const filteredPosts =
    selectedCategory === '전체'
      ? mockPosts
      : mockPosts.filter((post) => post.category === selectedCategory);

  // 정렬
  const sortedPosts = [...filteredPosts].sort((a, b) => {
    switch (sortBy) {
      case 'popular':
        return b.likes - a.likes;
      case 'views':
        return b.views - a.views;
      case 'comments':
        return b.comments - a.comments;
      default: // latest
        return b.id - a.id;
    }
  });

  return (
    <div className="min-h-screen bg-gray-50">
      <Header />

      <main className="container mx-auto px-4 py-8">
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          <div className="lg:col-span-2 space-y-6">
            <div className="bg-white rounded-lg shadow-sm border border-gray-200">
              <CategoryTabs
                defaultValue={selectedCategory}
                onValueChange={(value) =>
                  setSelectedCategory(value as Category)
                }
              />
            </div>

            <div className="flex items-center justify-between">
              <div className="flex items-center gap-2">
                <span className="text-gray-600">총</span>
                <span className="font-semibold">{sortedPosts.length}</span>
                <span className="text-gray-600">개의 게시글</span>
              </div>

              <div className="flex items-center gap-3">
                <Select
                  value={sortBy}
                  onValueChange={(value) => setSortBy(value as SortBy)}
                >
                  <SelectTrigger className="w-32">
                    <SelectValue />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="latest">최신순</SelectItem>
                    <SelectItem value="popular">인기순</SelectItem>
                    <SelectItem value="views">조회순</SelectItem>
                    <SelectItem value="comments">댓글순</SelectItem>
                  </SelectContent>
                </Select>

                <Link href="/posts/new">
                  <Button className="gap-2">
                    <Plus className="h-4 w-4" />
                    글쓰기
                  </Button>
                </Link>
              </div>
            </div>

            <div className="space-y-4">
              {sortedPosts.map((post, index) => (
                <motion.div
                  key={post.id}
                  initial={{ opacity: 0, y: 20 }}
                  animate={{ opacity: 1, y: 0 }}
                  transition={{
                    duration: 0.3,
                    delay: index * 0.05,
                  }}
                >
                  <PostCard {...post} />
                </motion.div>
              ))}
            </div>

            <div className="flex justify-center pt-4">
              <div className="flex gap-2">
                {[1, 2, 3, 4, 5].map((page) => (
                  <Button
                    key={page}
                    variant={page === 1 ? 'default' : 'outline'}
                    size="sm"
                    className="w-10"
                  >
                    {page}
                  </Button>
                ))}
              </div>
            </div>
          </div>

          <div className="hidden lg:block">
            <div className="sticky top-24">
              <Sidebar />
            </div>
          </div>
        </div>
      </main>

      <motion.div
        initial={{ scale: 0 }}
        animate={{ scale: 1 }}
        transition={{
          delay: 0.5,
          type: 'spring',
          stiffness: 260,
          damping: 20,
        }}
        className="fixed bottom-8 right-8 lg:hidden"
      >
        <Link href="/posts/new">
          <Button size="lg" className="h-14 w-14 rounded-full shadow-lg">
            <Plus className="h-6 w-6" />
          </Button>
        </Link>
      </motion.div>
    </div>
  );
}
