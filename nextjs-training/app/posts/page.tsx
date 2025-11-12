'use client';

import { useState, useEffect } from 'react';
import { Header } from '@/components/forum/Header';
import { PostCard } from '@/components/forum/PostCard';
import { Sidebar } from '@/components/forum/Sidebar';
import { CategoryTabs } from '@/components/forum/CategoryTabs';
import { Button, Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui';
import { Plus } from 'lucide-react';
import { motion } from 'framer-motion';
import { mockPosts } from '@/lib/mock-data';
import Link from 'next/link';
import { Category, SortBy } from '@/types/post';
import { getPosts, Post as ApiPost } from '@/lib/api/posts';

// 백엔드 Post를 프론트엔드 Post로 변환하는 함수
function transformApiPost(apiPost: ApiPost, index: number) {
  return {
    id: apiPost.id,
    title: apiPost.title,
    content: apiPost.content,
    author: apiPost.author,
    authorId: apiPost.authorId?.toString() || 'unknown',
    category: '자유', // 기본 카테고리 (백엔드에 카테고리 필드가 없으므로)
    views: index * 10 + 100, // 임시 조회수
    comments: Math.floor(Math.random() * 50), // 임시 댓글수
    likes: Math.floor(Math.random() * 100), // 임시 좋아요수
    date: new Date(apiPost.createdAt).toLocaleString('ko-KR', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    }),
    isHot: false,
    tags: [],
    createdAt: new Date(apiPost.createdAt),
    updatedAt: new Date(apiPost.updatedAt),
  };
}

export default function PostsPage() {
  const [sortBy, setSortBy] = useState<SortBy>('latest');
  const [selectedCategory, setSelectedCategory] = useState<Category>('전체');
  const [posts, setPosts] = useState<any[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // API에서 게시글 목록 가져오기
  useEffect(() => {
    async function fetchPosts() {
      try {
        setIsLoading(true);
        setError(null);
        const apiPosts = await getPosts();
        const transformedPosts = apiPosts.map((post, index) => transformApiPost(post, index));
        setPosts(transformedPosts);
      } catch (err) {
        console.error('게시글 목록 로드 실패:', err);
        setError('게시글을 불러오는데 실패했습니다. 백엔드 서버가 실행 중인지 확인해주세요.');
        // 에러 시 목업 데이터 사용
        setPosts(mockPosts);
      } finally {
        setIsLoading(false);
      }
    }

    fetchPosts();
  }, []);

  // 카테고리별 필터링
  const filteredPosts =
    selectedCategory === '전체'
      ? posts
      : posts.filter((post) => post.category === selectedCategory);

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

            {error && (
              <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-4">
                <p className="text-yellow-800 text-sm">{error}</p>
              </div>
            )}

            <div className="flex items-center justify-between">
              <div className="flex items-center gap-2">
                <span className="text-gray-600">총</span>
                <span className="font-semibold">{sortedPosts.length}</span>
                <span className="text-gray-600">개의 게시글</span>
                {isLoading && <span className="text-sm text-gray-500">(로딩 중...)</span>}
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
