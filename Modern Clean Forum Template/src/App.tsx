import { useState } from "react";
import { Header } from "./components/Header";
import { PostCard } from "./components/PostCard";
import { Sidebar } from "./components/Sidebar";
import { CategoryTabs } from "./components/CategoryTabs";
import { Button } from "./components/ui/button";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "./components/ui/select";
import { Plus } from "lucide-react";
import { motion } from "motion/react";

const mockPosts = [
  {
    id: 1,
    title: "React 19의 새로운 기능들이 정식 출시되었습니다",
    content:
      "React 19가 정식으로 출시되었습니다. Server Components, Actions, 그리고 여러 새로운 훅들이 포함되어 있습니다. 이번 업데이트는 개발자 경험을 크게 향상시킬 것으로 기대됩니다.",
    author: "김개발",
    category: "공지사항",
    views: 1234,
    comments: 45,
    likes: 89,
    date: "2시간 전",
    isHot: true,
  },
  {
    id: 2,
    title: "TypeScript에서 Generic 타입 잘 사용하는 방법",
    content:
      "TypeScript의 Generic은 재사용 가능한 컴포넌트를 만들 때 매우 유용합니다. 이 글에서는 실전에서 자주 사용되는 Generic 패턴들을 소개합니다.",
    author: "이타입",
    category: "Tips",
    views: 987,
    comments: 32,
    likes: 67,
    date: "5시간 전",
    isHot: true,
  },
  {
    id: 3,
    title: "Next.js 15 App Router 마이그레이션 후기",
    content:
      "Pages Router에서 App Router로 마이그레이션한 경험을 공유합니다. 장점과 단점, 그리고 주의해야 할 점들을 정리했습니다.",
    author: "박프론트",
    category: "정보",
    views: 856,
    comments: 28,
    likes: 54,
    date: "1일 전",
    isHot: false,
  },
  {
    id: 4,
    title: "CSS Grid vs Flexbox 언제 무엇을 써야 할까요?",
    content:
      "CSS 레이아웃을 구성할 때 Grid와 Flexbox 중 어떤 것을 선택해야 할지 고민이 되시나요? 각각의 사용 사례를 예제와 함께 설명합니다.",
    author: "최스타일",
    category: "질문",
    views: 743,
    comments: 19,
    likes: 43,
    date: "1일 전",
    isHot: false,
  },
  {
    id: 5,
    title: "React Query로 서버 상태 관리하기",
    content:
      "React Query(TanStack Query)를 사용하면 서버 상태 관리가 매우 간편해집니다. 캐싱, 리페칭, 동기화 등의 기능을 자세히 알아봅니다.",
    author: "정쿼리",
    category: "Tips",
    views: 621,
    comments: 15,
    likes: 38,
    date: "2일 전",
    isHot: false,
  },
  {
    id: 6,
    title: "웹 접근성 개선하기 - 실전 가이드",
    content:
      "웹 접근성은 모든 사용자가 웹사이트를 사용할 수 있도록 하는 중요한 요소입니다. ARIA 속성, 키보드 네비게이션 등을 알아봅니다.",
    author: "안웹앱",
    category: "정보",
    views: 534,
    comments: 12,
    likes: 29,
    date: "3일 전",
    isHot: false,
  },
  {
    id: 7,
    title: "Vite vs Webpack 2024년 번들러 비교",
    content:
      "최근 많은 프로젝트가 Webpack에서 Vite로 전환하고 있습니다. 두 번들러의 차이점과 각각의 장단점을 비교 분석합니다.",
    author: "강빌드",
    category: "정보",
    views: 489,
    comments: 23,
    likes: 31,
    date: "4일 전",
    isHot: false,
  },
  {
    id: 8,
    title: "프론트엔드 개발자 로드맵 2024",
    content:
      "2024년 프론트엔드 개발자가 되기 위해 학습해야 할 기술 스택과 로드맵을 정리했습니다. 초보자부터 중급자까지 참고하세요.",
    author: "윤커리어",
    category: "자유",
    views: 412,
    comments: 18,
    likes: 26,
    date: "5일 전",
    isHot: false,
  },
];

export default function App() {
  const [sortBy, setSortBy] = useState("latest");

  return (
    <div className="min-h-screen bg-gray-50">
      <Header />

      <main className="container mx-auto px-4 py-8">
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          <div className="lg:col-span-2 space-y-6">
            <div className="bg-white rounded-lg shadow-sm border border-gray-200">
              <CategoryTabs />
            </div>

            <div className="flex items-center justify-between">
              <div className="flex items-center gap-2">
                <span className="text-gray-600">총</span>
                <span>{mockPosts.length}</span>
                <span className="text-gray-600">
                  개의 게시글
                </span>
              </div>

              <div className="flex items-center gap-3">
                <Select
                  value={sortBy}
                  onValueChange={setSortBy}
                >
                  <SelectTrigger className="w-32">
                    <SelectValue />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="latest">
                      최신순
                    </SelectItem>
                    <SelectItem value="popular">
                      인기순
                    </SelectItem>
                    <SelectItem value="views">
                      조회순
                    </SelectItem>
                    <SelectItem value="comments">
                      댓글순
                    </SelectItem>
                  </SelectContent>
                </Select>

                <Button className="gap-2">
                  <Plus className="h-4 w-4" />
                  글쓰기
                </Button>
              </div>
            </div>

            <div className="space-y-4">
              {mockPosts.map((post, index) => (
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
                    variant={page === 1 ? "default" : "outline"}
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
          type: "spring",
          stiffness: 260,
          damping: 20,
        }}
        className="fixed bottom-8 right-8 lg:hidden"
      >
        <Button
          size="lg"
          className="h-14 w-14 rounded-full shadow-lg"
        >
          <Plus className="h-6 w-6" />
        </Button>
      </motion.div>
    </div>
  );
}