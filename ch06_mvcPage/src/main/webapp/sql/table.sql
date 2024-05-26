-- 회원 관리
-- 여기에 저장되어 있는 id는 사용할 수 없도록 한다.
create table zmember(
mem_num number not null,
id varchar2(12) unique not null,
auth number(1) default 2 not null, -- 회원 등급 : 0:탈퇴 회원, 1:정지 회원, 2:일반 회원, 9:관리자
constraint zmember_pk primary key (mem_num)
);

Create sequence zmember_seq;

-- 상세 페이지용 Join 해서 사용하기
-- 재가입을 못하게 하도록 테이블을 분리하여 처리함 -> 탈퇴할 때 이 테이블의 내용들을 다 없애야한다
create table zmember_detail(
mem_num number not null,
name varchar2(30) not null,
passwd varchar2(12) not null,
phone varchar2(15) not null,
email varchar2(50) not null,
zipcode varchar2(5) not null,
address1 varchar2(90) not null,
address2 varchar2(90) not null,
photo varchar2(400),
reg_date date default sysdate not null,
modify_date date,
constraint zmember_detail_pk primary key (mem_num),
constraint zmember_detail_fk foreign key (mem_num) references zmember (mem_num)
);

-- 게시판 
create table zboard(
board_num number not null,
title varchar2(150) not null,
content clob not null,
hit number(9) default 0 not null,
reg_date date default sysdate not null,
modify_date date,
filename varchar2(400),
ip varchar2(40) not null,
mem_num number not null,
constraint zboard_pk primary key (board_num),
constraint zboard_fk foreign key (mem_num) references zmember (mem_num)
);
create sequence zboard_seq;

-- 좋아요
create table zboard_fav(
board_num number NOT NULL,
mem_num  number not null,
constraint zboard_fav_fk1 foreign key (board_num) references zboard (board_num),
constraint zboard_fav_fk2 foreign key (mem_num) references zmember (mem_num)
);

-- 댓글
create table zboard_reply(
re_num number not null,
re_content varchar2(900) not null,
re_date date default sysdate not null,
re_modifydate date,
re_ip varchar2(40) not null,
board_num number not null,
mem_num number not null,
constraint zreply_pk primary key (re_num),
constraint zreply_fk1 foreign key (board_num) references zboard (board_num),
constraint zreply_fk2 foreign key (mem_num) references zmember (mem_num)
);
create sequence zreply_seq;

-- 상품 등록
create table zitem(
item_num number not null,
name varchar2(30) not null,
price number(9) not null, 
quantity number(7) not null, 
photo1 varchar2(400) not null, 
photo2 varchar2(400) not null, 
detail clob not null,
reg_date date default sysdate not null,
modify_date date,
status number(1) not null,		-- 표시 여부 (판매 가능 여부) 1: 미표시, 2:표시
constraint zitem_pk primary key (item_num)
);

create sequence zimte_seq;




