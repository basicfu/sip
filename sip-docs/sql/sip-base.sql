drop database if exists `sip-base`;
create database `sip-base` default charset utf8 collate utf8_general_ci;
USE `sip-base`;

drop table if exists tenant;
create table tenant(
	id bigint auto_increment primary key,
	name varchar(64) not null default '' comment '租户企业名',
	domain varchar(64) not null default '' comment '租户域名前缀'
)
comment '租户表' engine=InnoDB;

drop table if exists application;
create table application(
	id bigint auto_increment primary key,
	tenant_id bigint not null default 0 comment '租户ID',
	name varchar(64) not null default '' comment '应用名',
	path varchar(64) not null default '' comment '应用path',
	server_id varchar(64) not null default '' comment '应用注册名',
	url varchar(255) not null default '' comment '应用URL',
	strip_prefix tinyint not null default 0 comment '过滤前缀',
	retryable tinyint not null default 0 comment '重试',
	sensitive_headers varchar(5000) null comment '敏感头信息'
)
comment '应用表' engine=InnoDB;

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
  type varchar(64) not null default '' comment '字段类型(0Text,1Number,2Check,3Radio,4Date,5Time,6DateTime,7Dict)',
  extra varchar(64) not null default '' comment '字段长度(Text限制字段长度,Number限制字段范围大小(-100~300),字典存储关联的字典,其他不限)',
  sort int not null default 0 comment '字段顺序',
  cdate int not null default 0 comment '创建时间',
  udate int not null default 0 comment '更新时间',
  unique key (tenant_id,name)
)comment '用户模板' engine=InnoDB;
