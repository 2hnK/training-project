import { Tabs, TabsList, TabsTrigger } from './ui/tabs';

export function CategoryTabs() {
  return (
    <Tabs defaultValue="all" className="w-full">
      <TabsList className="w-full justify-start bg-transparent border-b rounded-none h-auto p-0">
        <TabsTrigger 
          value="all"
          className="rounded-none border-b-2 border-transparent data-[state=active]:border-blue-600 data-[state=active]:bg-transparent px-6"
        >
          전체
        </TabsTrigger>
        <TabsTrigger 
          value="notice"
          className="rounded-none border-b-2 border-transparent data-[state=active]:border-blue-600 data-[state=active]:bg-transparent px-6"
        >
          공지사항
        </TabsTrigger>
        <TabsTrigger 
          value="question"
          className="rounded-none border-b-2 border-transparent data-[state=active]:border-blue-600 data-[state=active]:bg-transparent px-6"
        >
          질문
        </TabsTrigger>
        <TabsTrigger 
          value="info"
          className="rounded-none border-b-2 border-transparent data-[state=active]:border-blue-600 data-[state=active]:bg-transparent px-6"
        >
          정보
        </TabsTrigger>
        <TabsTrigger 
          value="free"
          className="rounded-none border-b-2 border-transparent data-[state=active]:border-blue-600 data-[state=active]:bg-transparent px-6"
        >
          자유
        </TabsTrigger>
      </TabsList>
    </Tabs>
  );
}
