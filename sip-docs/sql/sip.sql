create table tenant(
	id bigint auto_increment comment '主键' primary key,
	icon varchar(255) null comment '菜单图标'
)
comment '租户表' engine=InnoDB;

create table application(
	id bigint auto_increment comment '主键' primary key,
	name varchar(100) not null comment '应用名',
	code varchar(32) null comment '应用code',
	enable
)
comment '应用表' engine=InnoDB;