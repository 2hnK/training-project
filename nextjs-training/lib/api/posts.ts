/**
 * Post API 서비스 계층
 * 백엔드 Spring Boot API와 통신하는 함수들을 정의합니다.
 */

// API 기본 URL (환경 변수에서 가져옴)
const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

/**
 * API Response 공통 타입
 */
export interface ApiResponse<T> {
  success: boolean;
  message?: string;
  data: T;
}

/**
 * Post 타입 (백엔드 PostResponse와 일치)
 */
export interface Post {
  id: number;
  title: string;
  content: string;
  author: string;
  authorId: number;
  createdAt: string;
  updatedAt: string;
}

/**
 * Post 생성 요청 타입 (백엔드 PostCreateRequest와 일치)
 */
export interface CreatePostRequest {
  title: string;
  content: string;
}

/**
 * 페이지네이션 타입 (Spring Data Page)
 */
export interface Page<T> {
  content: T[];
  pageable: {
    sort: {
      sorted: boolean;
      unsorted: boolean;
      empty: boolean;
    };
    offset: number;
    pageNumber: number;
    pageSize: number;
    paged: boolean;
    unpaged: boolean;
  };
  totalElements: number;
  totalPages: number;
  last: boolean;
  size: number;
  number: number;
  sort: {
    sorted: boolean;
    unsorted: boolean;
    empty: boolean;
  };
  numberOfElements: number;
  first: boolean;
  empty: boolean;
}

/**
 * 게시글 목록 조회
 * GET /api/posts/list
 */
export async function getPosts(): Promise<Post[]> {
  try {
    const res = await fetch(`${API_BASE_URL}/api/posts/list`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
      cache: 'no-store', // 항상 최신 데이터 가져오기
    });

    if (!res.ok) {
      throw new Error(`게시글 목록 조회 실패: ${res.status}`);
    }

    const response: ApiResponse<Page<Post>> = await res.json();
    
    if (response.success && response.data) {
      return response.data.content;
    }
    
    throw new Error('게시글 목록을 불러오는데 실패했습니다');
  } catch (error) {
    console.error('게시글 목록 조회 에러:', error);
    throw error;
  }
}

/**
 * 게시글 상세 조회
 * GET /api/posts/{id}
 */
export async function getPost(id: number): Promise<Post> {
  try {
    const res = await fetch(`${API_BASE_URL}/api/posts/${id}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
      cache: 'no-store',
    });

    if (!res.ok) {
      if (res.status === 404) {
        throw new Error('게시글을 찾을 수 없습니다');
      }
      throw new Error(`게시글 조회 실패: ${res.status}`);
    }

    const response: ApiResponse<Post> = await res.json();
    
    if (response.success && response.data) {
      return response.data;
    }
    
    throw new Error('게시글을 불러오는데 실패했습니다');
  } catch (error) {
    console.error('게시글 상세 조회 에러:', error);
    throw error;
  }
}

/**
 * 게시글 생성
 * POST /api/posts/create
 */
export async function createPost(data: CreatePostRequest): Promise<Post> {
  try {
    const res = await fetch(`${API_BASE_URL}/api/posts/create`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    });

    if (!res.ok) {
      const errorData = await res.json().catch(() => ({}));
      throw new Error(errorData.message || '게시글 생성에 실패했습니다');
    }

    const response: ApiResponse<Post> = await res.json();
    
    if (response.success && response.data) {
      return response.data;
    }
    
    throw new Error('게시글 생성에 실패했습니다');
  } catch (error) {
    console.error('게시글 생성 에러:', error);
    throw error;
  }
}

