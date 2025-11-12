-- Authorities 테이블 초기 데이터
-- data-user-h2.sql 의 사용자와 연계된 권한 데이터
-- 관리자: admin (member_id = 1) -> ROLE_ADMIN, ROLE_USER
-- 일반 사용자: 나머지 모두 ROLE_USER

-- 관리자 권한 (member_id = 1: admin)
INSERT INTO AUTHORITIES (MEMBER_ID, AUTHORITY) VALUES
(1, 'ROLE_ADMIN'),
(1, 'ROLE_USER');

-- 일반 사용자 권한
-- member_id = 2: user
-- member_id = 3: kimjihoon
-- member_id = 4~23: 나머지 사용자들
INSERT INTO AUTHORITIES (MEMBER_ID, AUTHORITY) VALUES
(2, 'ROLE_USER'),
(3, 'ROLE_USER'),
(4, 'ROLE_USER'),
(5, 'ROLE_USER'),
(6, 'ROLE_USER'),
(7, 'ROLE_USER'),
(8, 'ROLE_USER'),
(9, 'ROLE_USER'),
(10, 'ROLE_USER'),
(11, 'ROLE_USER'),
(12, 'ROLE_USER'),
(13, 'ROLE_USER'),
(14, 'ROLE_USER'),
(15, 'ROLE_USER'),
(16, 'ROLE_USER'),
(17, 'ROLE_USER'),
(18, 'ROLE_USER'),
(19, 'ROLE_USER'),
(20, 'ROLE_USER'),
(21, 'ROLE_USER'),
(22, 'ROLE_USER'),
(23, 'ROLE_USER');
