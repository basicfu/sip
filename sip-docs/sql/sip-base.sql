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

drop table if exists app_service;
create table app_service(
	id bigint auto_increment primary key,
	app_id bigint not null default 0 comment '应用ID',
	name varchar(32) not null default '' comment '应用名',
	path varchar(32) not null default '' comment '应用path',
	server_id varchar(32) not null default '' comment '应用注册名',
	url varchar(255) not null default '' comment '应用URL',
	strip_prefix tinyint not null default 0 comment '过滤前缀',
	retryable tinyint not null default 0 comment '重试',
	sensitive_headers varchar(512) null comment '敏感头信息',
	cdate int not null default 0 comment '创建时间',
  udate int not null default 0 comment '更新时间',
  unique key (app_id,path),
  unique key (app_id,server_id,url)
)
comment '服务表' engine=InnoDB;

drop table if exists app_secret;
create table app_secret(
	id bigint auto_increment primary key,
	app_id bigint not null default 0 comment '应用ID',
	secret varchar(32) not null default '' comment 'secret',
	description varchar(32) not null default '' comment '描述',
	cdate int not null default 0 comment '创建时间',
  udate int not null default 0 comment '更新时间',
	unique key (app_id,secret)
)
comment '应用secret表' engine=InnoDB;

drop table if exists user;
create table user(
  id bigint auto_increment primary key,
  app_id bigint not null default 0 comment '租户ID',
  username varchar(32) not null default '' comment '用户名',
  nickname varchar(32) not null default '' comment '昵称',
  content varchar(20000) null default '' comment '用户json信息(mysql8)',
  cdate int not null default 0 comment '创建时间',
  udate int not null default 0 comment '更新时间',
  cuid bigint not null default 0 comment '创建人ID',
  type varchar(64) not null default '' comment '用户类型字典',
  status tinyint not null default 0 comment '0正常,1删除,2黑名单'
)comment '用户表' engine=InnoDB;

drop table if exists user_auth;
CREATE TABLE user_auth (
  id bigint auto_increment primary key,
  app_id bigint not null default 0 comment '应用ID',
  uid bigint not null comment '用户ID',
  type varchar(64) not null default '' comment 'auth类型字典',
  username varchar(100) not null default '' comment '登录标识',
  password varchar(100) not null default '' comment '密码凭证',
  salt varchar(64) not null default '' comment '盐值',
  cdate int not null default 0 comment '创建时间',
  udate int not null default 0 comment '更新时间',
  ldate int not null default 0 comment '最后一次登录时间',
  status tinyint not null default 0 comment '0正常,1删除,2黑名单',
  unique key (uid,type)
)comment '用户授权表' engine=InnoDB;

drop table if exists user_template;
CREATE TABLE user_template (
  id bigint auto_increment primary key,
  app_id bigint not null default 0 comment '租户ID',
  name varchar(64) not null default '' comment '字段名',
  en_name varchar(64) not null default '' comment '字段英文名',
  type varchar(64) not null default '' comment '字段类型(0TEXT,1NUMBER,2CHECK,3RADIO,4SELECT,5DATE)',
  extra varchar(64) not null default '' comment '字段长度(TEXT限制字段长度范围,NUMBER限制字段长度且范围大小10,2(0.15~255.25),CHECK/RADIO/SELECT存储关联的字典,DATA自定义格式化时间yyyy-MM-dd HH:mm:ss)',
  default_value varchar(2000) not null default '' comment '字段默认值',
  required tinyint not null default 0 comment '是否必填',
  sort int not null default 0 comment '字段顺序',
  cdate int not null default 0 comment '创建时间',
  udate int not null default 0 comment '更新时间',
  unique key (app_id,name)
)comment '用户模板' engine=InnoDB;


INSERT INTO app (id,name, code, cdate, udate) VALUES (1,'SIP', 'sip', 1531389485, 1531389485);
INSERT INTO user (id,app_id, username, nickname, content, cdate, udate, cuid, type, status) VALUES (1,1, 'test', 'test', '{}', 1533019466, 1539054488, 0, 'SYSTEM_SUPER_ADMIN', 0);
INSERT INTO user_auth (id,app_id,uid, type, username, password, salt, cdate, udate, ldate,status) VALUES (1,1,1, 0, 'test', '$2a$10$3d7ykD7OxOTglE6DcLdhWerSpViDhIAyz6CylkBg.QkRlTgtduQCa', '', 1533019466, 1539002195, 1538995227,0);
