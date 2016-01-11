
create table user (
	id bigint generated by default as identity,
	alias_name varchar(64) not null,
	login_name varchar(64) not null,
	salt varchar(64) not null,
	email varchar(128),
	hash_password varchar(255) not null,
	primary key (id)
);


insert into user (id,email,alias_name,login_name,salt,hash_password) values(1,'linchao@111.com.cn','系统管理员','system','b6682f6ff51ca51e','eea645f601a395e800163c89d18241a060a2d826');
insert into user (id,email,alias_name,login_name,salt,hash_password) values(2,'c.lins@qq.com','超级管理员','admin','b6446157e322ef03','1f0914cf5a3e2cdb9e0b9252e8d607deddafcb50');