drop database if exists `sip-base`;
create database `sip-base` default charset utf8 collate utf8_general_ci;
USE `sip-base`;

drop table if exists app;
create table app(
	id bigint auto_increment primary key,
	name varchar(32) not null default '' comment '应用名',
	code varchar(32) not null default '' comment '应用code',
	cdate int not null default 0 comment '创建时间',
  udate int not null default 0 comment '更新时间',
	unique key (name),
	unique key (code)
)
comment '应用表' engine=InnoDB;

drop table if exists app_secret;
create table app_secret(
	id bigint auto_increment primary key,
	app_id bigint not null default 0 comment '应用ID',
	secret varchar(32) not null default '' comment 'secret',
	description varchar(64) not null default '' comment '描述',
	cdate int not null default 0 comment '创建时间',
  udate int not null default 0 comment '更新时间',
	unique key (app_id,secret)
)
comment '应用secret表' engine=InnoDB;

drop table if exists service;
create table service(
	id bigint auto_increment primary key,
	app_id bigint not null default 0 comment '应用ID',
	name varchar(64) not null default '' comment '应用名',
	path varchar(64) not null default '' comment '应用path',
	server_id varchar(64) not null default '' comment '应用注册名',
	url varchar(255) not null default '' comment '应用URL',
	strip_prefix tinyint not null default 0 comment '过滤前缀',
	retryable tinyint not null default 0 comment '重试',
	sensitive_headers varchar(500) null comment '敏感头信息',
	cdate int not null default 0 comment '创建时间',
  udate int not null default 0 comment '更新时间',
  unique key (app_id,path)
)
comment '服务表' engine=InnoDB;

drop table if exists user;
create table user(
  id bigint auto_increment primary key,
  tenant_id bigint not null default 0 comment '租户ID',
  username varchar(32) not null default '' comment '用户名',
  content varchar(20000) null default '' comment '用户json信息(mysql8)',
  cdate int not null default 0 comment '创建时间',
  udate int not null default 0 comment '更新时间',
  type tinyint not null default 0 comment '用户类型0系统用户,1租户,2普通用户',
  status tinyint not null default 0 comment '0正常,1删除,2黑名单'
)comment '用户表' engine=InnoDB;

drop table if exists user_auth;
CREATE TABLE user_auth (
  id bigint auto_increment primary key,
  uid bigint not null comment '用户ID',
  type tinyint not null default 0 comment 'auth类型0用户名,1手机号,2字典(读取字典)',
  username varchar(100) not null default '' comment '登录标识',
  password varchar(100) not null default '' comment '密码凭证',
  salt varchar(64) not null default '' comment '盐值',
  cdate int not null default 0 comment '创建时间',
  udate int not null default 0 comment '更新时间',
  ldate int not null default 0 comment '最后一次登录时间',
  unique key (uid,type)
)comment '用户授权表' engine=InnoDB;

drop table if exists user_template;
CREATE TABLE user_template (
  id bigint auto_increment primary key,
  tenant_id bigint not null default 0 comment '租户ID',
  name varchar(64) not null default '' comment '字段名',
  en_name varchar(64) not null default '' comment '字段英文名',
  type varchar(64) not null default '' comment '字段类型(0TEXT,1NUMBER,2CHECK,3RADIO,4SELECT,5DATE)',
  extra varchar(64) not null default '' comment '字段长度(TEXT限制字段长度范围,NUMBER限制字段长度且范围大小10,2(0.15~255.25),CHECK/RADIO/SELECT存储关联的字典,DATA自定义格式化时间yyyy-MM-dd HH:mm:ss)',
  default_value varchar(2000) not null default '' comment '字段默认值',
  required tinyint not null default 0 comment '是否必填',
  sort int not null default 0 comment '字段顺序',
  cdate int not null default 0 comment '创建时间',
  udate int not null default 0 comment '更新时间',
  unique key (tenant_id,name)
)comment '用户模板' engine=InnoDB;


INSERT INTO app (id,name, code, cdate, udate) VALUES (1,'SIP', 'sip', 1531389485, 1531389485);
