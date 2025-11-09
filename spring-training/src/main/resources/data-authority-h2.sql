-- Authorities 테이블 초기 데이터
-- data-user-h2.sql 의 사용자와 연계된 권한 데이터
-- 관리자: admin (ROLE_ADMIN, ROLE_USER)
-- 일반 사용자: 나머지 모두 ROLE_USER

-- 관리자 권한
INSERT INTO AUTHORITIES (USERNAME, AUTHORITY) VALUES
('admin', 'ROLE_ADMIN'),
('admin', 'ROLE_USER');

-- 일반 사용자 권한
INSERT INTO AUTHORITIES (USERNAME, AUTHORITY) VALUES
('user', 'ROLE_USER'),
('kimjihoon', 'ROLE_USER'),
('kimminjun', 'ROLE_USER'),
('leeseoyeon', 'ROLE_USER'),
('parkdoyoon', 'ROLE_USER'),
('choihaeun', 'ROLE_USER'),
('jungsiwoo', 'ROLE_USER'),
('kangjian', 'ROLE_USER'),
('choseojun', 'ROLE_USER'),
('yoonarin', 'ROLE_USER'),
('jangyejun', 'ROLE_USER'),
('limhayoon', 'ROLE_USER'),
('hanyuju', 'ROLE_USER'),
('oheunwoo', 'ROLE_USER'),
('seoian', 'ROLE_USER'),
('hwangjiho', 'ROLE_USER'),
('songyuna', 'ROLE_USER'),
('kwonjuwon', 'ROLE_USER'),
('hongdaeun', 'ROLE_USER'),
('moonjihoo', 'ROLE_USER'),
('baesua', 'ROLE_USER'),
('ryujunseo', 'ROLE_USER');
