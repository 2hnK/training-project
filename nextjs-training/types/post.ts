export interface Post {
  id: number;
  title: string;
  content: string;
  author: string;
  authorId: string;
  category: string;
  views: number;
  comments: number;
  likes: number;
  date: string;
  isHot?: boolean;
  tags?: string[];
  createdAt?: Date;
  updatedAt?: Date;
}

export interface Comment {
  id: number;
  postId: number;
  author: string;
  content: string;
  date: string;
  likes: number;
}

export type Category = '전체' | '공지사항' | '질문' | '정보' | '자유' | 'Tips';

export type SortBy = 'latest' | 'popular' | 'views' | 'comments';
