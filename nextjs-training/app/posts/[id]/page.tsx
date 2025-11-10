'use client';

import { use } from 'react';
import { Header } from '@/components/forum/Header';
import { Button } from '@/components/ui/button';
import { Card } from '@/components/ui/card';
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui';
import { Badge } from '@/components/ui/badge';
import { Separator } from '@/components/ui/separator';
import { Textarea } from '@/components/ui/textarea';
import {
  ArrowLeft,
  Eye,
  MessageSquare,
  Heart,
  Share2,
  Bookmark,
} from 'lucide-react';
import Link from 'next/link';
import { mockPosts, mockComments } from '@/lib/mock-data';
import { motion } from 'framer-motion';

export default function PostDetailPage({
  params,
}: {
  params: Promise<{ id: string }>;
}) {
  const { id } = use(params);
  const post = mockPosts.find((p) => p.id === parseInt(id));
  const comments = mockComments.filter((c) => c.postId === parseInt(id));

  if (!post) {
    return (
      <div className="min-h-screen bg-gray-50">
        <Header />
        <main className="container mx-auto px-4 py-8">
          <div className="text-center">
            <h1 className="text-2xl font-bold mb-4">게시글을 찾을 수 없습니다</h1>
            <Link href="/posts">
              <Button>목록으로 돌아가기</Button>
            </Link>
          </div>
        </main>
      </div>
    );
  }

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
              <div className="flex items-center gap-2 mb-4">
                <Badge variant="secondary">{post.category}</Badge>
                {post.isHot && (
                  <Badge className="bg-red-500 hover:bg-red-600">HOT</Badge>
                )}
              </div>

              <h1 className="text-3xl font-bold mb-6">{post.title}</h1>

              <div className="flex items-center justify-between mb-6 pb-6 border-b">
                <div className="flex items-center gap-3">
                  <Avatar className="h-12 w-12">
                    <AvatarImage src="" />
                    <AvatarFallback>{post.author[0]}</AvatarFallback>
                  </Avatar>
                  <div>
                    <p className="font-semibold">{post.author}</p>
                    <p className="text-sm text-gray-500">{post.date}</p>
                  </div>
                </div>

                <div className="flex items-center gap-6 text-sm text-gray-500">
                  <div className="flex items-center gap-1">
                    <Eye className="h-4 w-4" />
                    <span>{post.views}</span>
                  </div>
                  <div className="flex items-center gap-1">
                    <MessageSquare className="h-4 w-4" />
                    <span>{post.comments}</span>
                  </div>
                  <div className="flex items-center gap-1 text-red-500">
                    <Heart className="h-4 w-4" />
                    <span>{post.likes}</span>
                  </div>
                </div>
              </div>

              <div className="prose max-w-none mb-8">
                <p className="text-gray-700 leading-relaxed whitespace-pre-wrap">
                  {post.content}
                </p>
              </div>

              {post.tags && post.tags.length > 0 && (
                <div className="flex flex-wrap gap-2 mb-6 pb-6 border-b">
                  {post.tags.map((tag, index) => (
                    <Badge key={index} variant="outline">
                      #{tag}
                    </Badge>
                  ))}
                </div>
              )}

              <div className="flex items-center justify-between">
                <div className="flex gap-2">
                  <Button variant="outline" className="gap-2">
                    <Heart className="h-4 w-4" />
                    좋아요 {post.likes}
                  </Button>
                  <Button variant="outline" className="gap-2">
                    <Bookmark className="h-4 w-4" />
                    북마크
                  </Button>
                </div>
                <Button variant="outline" className="gap-2">
                  <Share2 className="h-4 w-4" />
                  공유
                </Button>
              </div>
            </Card>
          </motion.div>

          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.3, delay: 0.1 }}
          >
            <Card className="p-6">
              <h2 className="text-xl font-bold mb-4">
                댓글 {comments.length}
              </h2>

              <div className="space-y-4 mb-6">
                {comments.map((comment) => (
                  <div key={comment.id} className="flex gap-3">
                    <Avatar className="h-10 w-10">
                      <AvatarImage src="" />
                      <AvatarFallback>{comment.author[0]}</AvatarFallback>
                    </Avatar>
                    <div className="flex-1">
                      <div className="flex items-center gap-2 mb-1">
                        <span className="font-semibold">
                          {comment.author}
                        </span>
                        <span className="text-sm text-gray-500">
                          {comment.date}
                        </span>
                      </div>
                      <p className="text-gray-700 mb-2">{comment.content}</p>
                      <Button
                        variant="ghost"
                        size="sm"
                        className="gap-1 h-8 text-gray-500"
                      >
                        <Heart className="h-3 w-3" />
                        {comment.likes}
                      </Button>
                    </div>
                  </div>
                ))}
              </div>

              <Separator className="my-6" />

              <div className="space-y-3">
                <h3 className="font-semibold">댓글 작성</h3>
                <Textarea
                  placeholder="댓글을 입력하세요..."
                  className="min-h-24"
                />
                <div className="flex justify-end">
                  <Button>댓글 등록</Button>
                </div>
              </div>
            </Card>
          </motion.div>
        </div>
      </main>
    </div>
  );
}
