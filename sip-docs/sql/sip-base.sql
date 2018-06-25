create table tenant(
	id bigint auto_increment comment '主键' primary key,
	icon varchar(255) null comment '菜单图标'
)
comment '租户表' engine=InnoDB;

create table application(
	id bigint auto_increment comment '主键' primary key,
	name varchar(100) not null comment '应用名',
	path varchar(64) not null comment '应用path',
	server_id varchar(64) null comment '应用注册名',
	url varchar(255) null comment '应用URL',
	strip_prefix tinyint(1) not null default 0 comment '过滤前缀',
	retryable tinyint(1) not null default 0 comment '重试',
	sensitive_headers varchar(5000) null comment '敏感头信息'
)
comment '应用表' engine=InnoDB;