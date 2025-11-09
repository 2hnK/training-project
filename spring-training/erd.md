// Use DBML to define your database structure
// Docs: https://dbml.dbdiagram.io/docs

// 사용자 테이블
Table USERS {
  USER_ID BIGINT [primary key, increment, note: '사용자 고유 ID']
  NAME VARCHAR(255) [not null, note: '계정 아이디']
  PASSWORD VARCHAR(255) [not null, note: '계정 비밀번호']
  NICKNAME VARCHAR(255) [not null, note: '닉네임']
  EMAIL VARCHAR(255) [not null, note: '이메일 주소']
  CREATED_AT TIMESTAMP [note: '생성 일시']
  UPDATED_AT TIMESTAMP [note: '최종 수정 일시']
}

// 강좌 테이블
Table COURSES {
  COURSE_ID BIGINT [primary key, increment, note: '강좌 고유 ID']
  NAME VARCHAR(255) [not null, note: '강좌명']
  DESCRIPTION VARCHAR(1000) [not null, note: '강좌 상세 설명']
  CATEGORY VARCHAR(255) [not null, note: '강좌 카테고리']
  RATING INTEGER [not null, note: '강좌 평점 (1-5)']
  CREATED_AT TIMESTAMP [note: '생성 일시']
  UPDATED_AT TIMESTAMP [note: '최종 수정 일시']
}

// 챕터 테이블
Table CHAPTERS {
  CHAPTER_ID BIGINT [primary key, increment, note: '챕터 고유 ID']
  COURSE_ID BIGINT [not null, ref: > COURSES.COURSE_ID, note: '소속 강좌 ID']
  TITLE VARCHAR(255) [not null, note: '챕터 제목']
  CONTENT VARCHAR(5000) [note: '챕터 내용']
  ORDER_INDEX INTEGER [not null, note: '챕터 정렬 순서']
  CREATED_AT TIMESTAMP [note: '생성 일시']
  UPDATED_AT TIMESTAMP [note: '최종 수정 일시']
}

// 북마크 테이블 (사용자-강좌 매핑)
Table BOOKMARKS {
  BOOKMARK_ID BIGINT [primary key, increment, note: '북마크 고유 ID']
  USER_ID BIGINT [not null, ref: > USERS.USER_ID, note: '사용자 ID']
  COURSE_ID BIGINT [not null, ref: > COURSES.COURSE_ID, note: '강좌 ID']
  CREATED_AT TIMESTAMP [note: '생성 일시']
  UPDATED_AT TIMESTAMP [note: '최종 수정 일시']
  
  indexes {
    (USER_ID, COURSE_ID) [unique, note: '사용자당 강좌 중복 북마크 방지']
  }
}