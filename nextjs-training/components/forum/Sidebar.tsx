'use client';

import { TrendingUp, Calendar, Tag } from 'lucide-react';
import { Card } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import Link from 'next/link';

export function Sidebar() {
  const popularPosts = [
    { id: 1, title: 'React 19 새로운 기능 정리', views: 1234 },
    { id: 2, title: 'TypeScript 5.0 마이그레이션 가이드', views: 987 },
    { id: 3, title: 'Next.js 앱 라우터 완벽 가이드', views: 856 },
    { id: 4, title: 'Tailwind CSS 실전 팁 모음', views: 743 },
    { id: 5, title: '프론트엔드 성능 최적화 방법', views: 621 },
  ];

  const categories = [
    { name: '공지사항', count: 12, color: 'bg-blue-100 text-blue-700' },
    { name: '질문', count: 234, color: 'bg-green-100 text-green-700' },
    { name: '정보', count: 156, color: 'bg-purple-100 text-purple-700' },
    { name: '자유', count: 89, color: 'bg-orange-100 text-orange-700' },
    { name: 'Tips', count: 67, color: 'bg-pink-100 text-pink-700' },
  ];

  const tags = [
    'React',
    'TypeScript',
    'Next.js',
    'Vue',
    'Node.js',
    'CSS',
    'JavaScript',
    'UI/UX',
  ];

  return (
    <aside className="space-y-6">
      <Card className="p-5">
        <div className="flex items-center gap-2 mb-4">
          <TrendingUp className="h-5 w-5 text-red-500" />
          <h3 className="font-semibold">인기 게시글</h3>
        </div>
        <div className="space-y-3">
          {popularPosts.map((post, index) => (
            <Link key={post.id} href={`/posts/${post.id}`}>
              <div className="flex items-start gap-3 cursor-pointer hover:text-blue-600 transition-colors group">
                <span className="text-gray-400 mt-1 font-medium">
                  {index + 1}
                </span>
                <div className="flex-1 min-w-0">
                  <p className="text-sm line-clamp-2 group-hover:text-blue-600">
                    {post.title}
                  </p>
                  <p className="text-xs text-gray-500 mt-1">
                    조회 {post.views}
                  </p>
                </div>
              </div>
            </Link>
          ))}
        </div>
      </Card>

      <Card className="p-5">
        <div className="flex items-center gap-2 mb-4">
          <Calendar className="h-5 w-5 text-blue-500" />
          <h3 className="font-semibold">카테고리</h3>
        </div>
        <div className="space-y-2">
          {categories.map((cat, index) => (
            <div
              key={index}
              className="flex items-center justify-between p-2 rounded-lg hover:bg-gray-50 cursor-pointer transition-colors"
            >
              <span className="text-sm">{cat.name}</span>
              <Badge className={cat.color} variant="secondary">
                {cat.count}
              </Badge>
            </div>
          ))}
        </div>
      </Card>

      <Card className="p-5">
        <div className="flex items-center gap-2 mb-4">
          <Tag className="h-5 w-5 text-purple-500" />
          <h3 className="font-semibold">인기 태그</h3>
        </div>
        <div className="flex flex-wrap gap-2">
          {tags.map((tag, index) => (
            <Badge
              key={index}
              variant="outline"
              className="cursor-pointer hover:bg-gray-100 transition-colors"
            >
              #{tag}
            </Badge>
          ))}
        </div>
      </Card>
    </aside>
  );
}
