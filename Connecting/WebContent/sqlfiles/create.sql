DROP TABLE MEMBER CASCADE CONSTRAINTS;

CREATE TABLE MEMBER (

	ID VARCHAR2(20) PRIMARY KEY,
	PASSWORD VARCHAR2(20) NOT NULL,
	EMAIL VARCHAR2(20) NOT NULL,
	NAME VARCHAR2(10) NOT NULL,
	REG_DATE DATE DEFAULT SYSDATE,
	PROFILE_IMG VARCHAR2(20) 
);

insert into member (id, password, email,name,profile_img) 
values('hh','1234','hhyy@naver.com','hy',null);

select * from MEMBER;


CREATE TABLE HEART ( -- ����ڰ� ��Ʈ ��ư Ŭ���� �Խù��� ����ִ� ���̺� --
	ID VARCHAR2(20),
	HEART VARCHAR2(30), -- ��Ʈ ��ư ���� �Խù��� BOARD_ID (�ĸ��� ����) 100,200,300 --
	FOREIGN KEY(ID) REFERENCES MEMBER(ID)
);


CREATE TABLE BOARD (
	BOARD_ID     NUMBER PRIMARY KEY, -- MAX�� ��������, ���� NULL�̸� 0������ ���� --
	CATEGORY     NUMBER(5) CHECK(CATEGORY IN (0, 1, 2, 3)), --0:����ȸ, 1:�ڶ�ȸ, 2:����ŷ, 3:����/���� -- 
	LOC          NUMBER(5) CHECK(LOC IN (0, 1, 2, 3, 4)), --0:����, 1:���/��õ, 2:����/��û/����, 3:�λ�/�뱸/��� 4:����/����/���� --
	ID           VARCHAR2(20), --MEMBER���̺��� �ۼ��� ID--
	TITLE        VARCHAR2(50),
	HOST_NAME    VARCHAR2(10), -- �������� �̸� --
	ADDRESS      VARCHAR2(30), -- ���ּ� --
	START_DATE   VARCHAR2(20),
	END_DATE     VARCHAR2(20),
	START_TIME   VARCHAR2(20),
	END_TIME     VARCHAR2(20),
	WRITE_DATE   DATE DEFAULT SYSDATE,
	CONTENT      VARCHAR2(399),
	BOARD_IMG    VARCHAR2(30),
	FOREIGN KEY(ID) REFERENCES MEMBER(ID) ON DELETE CASCADE -- ȸ�� Ż�� �� �ۼ� �۵� ���ư� --
);

insert into board (board_id,category,loc,id,title,
start_date,end_date) 
values(3,1,0,'hh','2���ڶ�ȸ','2022-02-01','2022-02-31');

select * from board;


CREATE TABLE NOTICE (
	NOTICE_ID NUMBER(20) PRIMARY KEY,
	TITLE VARCHAR2(50),
	CONTENT VARCHAR2(399),
	WRITE_DATE DATE DEFAULT SYSDATE
);
