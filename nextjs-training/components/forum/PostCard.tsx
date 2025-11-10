'use client';

import { MessageSquare, Heart, Eye, TrendingUp } from 'lucide-react';
import { Card } from '@/components/ui';
import { Badge } from '@/components/ui';
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui';
import { motion } from 'framer-motion';
import Link from 'next/link';
import { Post } from '@/types/post';

interface PostCardProps extends Post {}

export function PostCard({
  id,
  title,
  content,
  author,
  category,
  views,
  comments,
  likes,
  date,
  isHot = false,
}: PostCardProps) {
  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.3 }}
      whileHover={{ y: -4 }}
    >
      <Link href={`/posts/${id}`}>
        <Card className="p-5 hover:shadow-lg transition-all duration-300 cursor-pointer border border-gray-200 hover:border-gray-300">
          <div className="flex items-start gap-4">
            <Avatar className="h-10 w-10">
              <AvatarImage src="" />
              <AvatarFallback>{author[0]}</AvatarFallback>
            </Avatar>

            <div className="flex-1 min-w-0">
              <div className="flex items-center gap-2 mb-2">
                <Badge variant="secondary" className="text-xs">
                  {category}
                </Badge>
                {isHot && (
                  <Badge className="text-xs bg-red-500 hover:bg-red-600">
                    <TrendingUp className="h-3 w-3 mr-1" />
                    HOT
                  </Badge>
                )}
              </div>

              <h3 className="text-lg font-semibold mb-2 line-clamp-1 hover:text-blue-600 transition-colors">
                {title}
              </h3>

              <p className="text-gray-600 text-sm mb-3 line-clamp-2">
                {content}
              </p>

              <div className="flex items-center justify-between text-sm text-gray-500">
                <div className="flex items-center gap-4">
                  <span>{author}</span>
                  <span>{date}</span>
                </div>

                <div className="flex items-center gap-4">
                  <div className="flex items-center gap-1">
                    <Eye className="h-4 w-4" />
                    <span>{views}</span>
                  </div>
                  <div className="flex items-center gap-1">
                    <MessageSquare className="h-4 w-4" />
                    <span>{comments}</span>
                  </div>
                  <div className="flex items-center gap-1 text-red-500">
                    <Heart className="h-4 w-4" />
                    <span>{likes}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </Card>
      </Link>
    </motion.div>
  );
}
