insert into user_tb(username, password, email, created_at)
values ('ssar', '1234', 'ssar@nate.com', now());
insert into user_tb(username, password, email, created_at)
values ('cos', '1234', 'cos@nate.com', now());

insert into board_tb(title, content, user_id, created_at)
values ('제목1', '내용1', 1, now());
insert into board_tb(title, content, user_id, created_at)
values ('제목2', '내용2', 1, now());
insert into board_tb(title, content, user_id, created_at)
values ('제목3', '내용3', 1, now());
insert into board_tb(title, content, user_id, created_at)
values ('제목4', '내용4', 2, now());
insert into board_tb(title, content, user_id, created_at)
values ('제목5', '내용5', 2, now());
insert into board_tb(title, content, user_id, created_at)
values ('제목6', '내용6', 2, now());
insert into board_tb(title, content, user_id, created_at)
values ('제목7', '내용7', 2, now());
insert into board_tb(title, content, user_id, created_at)
values ('제목8', '내용8', 2, now());
insert into board_tb(title, content, user_id, created_at)
values ('제목9', '내용9', 2, now());

insert into reply_tb(comment, board_id, user_id, created_at)
values ('댓글1', 1, 1, now());
insert into reply_tb(comment, board_id, user_id, created_at)
values ('댓글2', 4, 1, now());
insert into reply_tb(comment, board_id, user_id, created_at)
values ('댓글3', 4, 1, now());
insert into reply_tb(comment, board_id, user_id, created_at)
values ('댓글4', 4, 2, now());

insert into love_tb(board_id, user_id, created_at)
values (9, 1, now());
insert into love_tb(board_id, user_id, created_at)
values (9, 2, now());
insert into love_tb(board_id, user_id, created_at)
values (8, 1, now());