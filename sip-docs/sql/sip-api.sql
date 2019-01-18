drop database if exists `sip-api`;
create database `sip-api` default charset utf8 collate utf8_general_ci;
USE `sip-api`;

drop table if exists project;
create table project(
	id bigint auto_increment primary key,
	name varchar(32) not null default '' comment '项目名',
	base_path varchar(32) not null default '' comment '基础路径',
	project_type varchar(32) not null default '' comment '项目类型，公开、私有',
	members varchar(32) not null default '' comment '成员数组，uid',
	env varchar(32) not null default '' comment '环境数组，name、url、header、cookie、global',
	cdate int not null default 0 comment '创建时间',
  udate int not null default 0 comment '更新时间',
  cuid bigint not null default 0 comment '创建人ID',
)
comment '项目表' engine=InnoDB;

drop table if exists project_user;
create table project_user(
	id bigint auto_increment primary key,
	uid bigint not null default 0 comment '用户id',
	project_id bigint not null default 0 comment '项目id',
	cdate int not null default 0 comment '创建时间',
  cuid bigint not null default 0 comment '创建人ID',
)
comment '项目用户表' engine=InnoDB;

drop table if exists project_category;
CREATE TABLE project_category (
  id bigint auto_increment primary key,
  project_id bigint not null default 0 comment '项目Id',
  pid bigint not null default 0 comment '父级Id',
  name varchar(32) not null default '' comment '分类名',
  sort int not null default 0 comment '分类顺序',
  cdate int not null default 0 comment '创建时间',
  udate int not null default 0 comment '更新时间',
  cuid bigint not null default 0 comment '创建人ID',
)
comment '项目分类表' engine=InnoDB;

drop table if exists interface;
create table interface(
	id bigint auto_increment primary key,
	name varchar(32) not null default '' comment '接口名',
	description varchar(255) not null default '' comment '接口描述',
	status varchar(32) not null default '' comment '接口状态（完成、未完成）',
	project_id bigint not null default 0 comment '项目ID',
	category_id bigint not null default 0 comment '接口分类ID',
	method varchar(32) not null default '' comment '请求方式，GET/POST/PUT等',
	host varchar(255) not null default '' comment '主机地址',
	path varchar(255) not null default '' comment 'path',
	path_params varchar(32) not null default '' comment 'path参数数组key/value/desc/enable',
	query_params varchar(32) not null default '' comment 'URL请求参数数组----------跟在path之后,key/value/desc/enable',
	req_headers varchar(32) not null default '' comment '请求头数组----------,key/value/desc/enable',
	req_body_type varchar(32) not null default '' comment '[form,json,file,raw]',
	req_body varchar(32) not null default '' comment '请求体，form(根据是否选择file自动改变请求头类型)(key/type(text/file)/value/desc/enable),json,file(一个路径path),raw(text)',
	res_body_type varchar(32) not null default '' comment '[json,file,raw]',
	res_body varchar(32) not null default '' comment '请求体，form(根据是否选择file自动改变请求头类型)(key/type(text/file)/value/desc/enable),json,file(一个路径path),raw(text)',
	cdate int not null default 0 comment '创建时间',
  udate int not null default 0 comment '更新时间',
  cuid bigint not null default 0 comment '创建人ID',
	unique key (project_id,method,path)
)
comment '接口表' engine=InnoDB;

drop table if exists interface_history;
create table interface_history(
	id bigint auto_increment primary key,
	interface_id bigint not null default 0 comment '接口ID',
	type varchar(32) not null default '' comment 'SAVE（保存历史）,REQUEST(请求历史)',
	name varchar(32) not null default '' comment '接口名',
	description varchar(255) not null default '' comment '接口描述',
	status varchar(32) not null default '' comment '接口状态（完成、未完成）',
	project_id bigint not null default 0 comment '项目ID',
	category_id bigint not null default 0 comment '接口分类ID',
	method varchar(32) not null default '' comment '请求方式，GET/POST/PUT等',
	path varchar(255) not null default '' comment 'path',
	path_params varchar(32) not null default '' comment 'path参数数组key/value/desc/enable',
	query_params varchar(32) not null default '' comment 'URL请求参数数组----------跟在path之后,key/value/desc/enable',
	req_headers varchar(32) not null default '' comment '请求头数组----------,key/value/desc/enable',
	req_body_type varchar(32) not null default '' comment '[form,json,file,raw]',
	req_body varchar(32) not null default '' comment '请求体，form(根据是否选择file自动改变请求头类型)(key/type(text/file)/value/desc/enable),json,file(一个路径path),raw(text)',
	res_body_type varchar(32) not null default '' comment '[json,file,raw]',
	res_body varchar(32) not null default '' comment '请求体，form(根据是否选择file自动改变请求头类型)(key/type(text/file)/value/desc/enable),json,file(一个路径path),raw(text)',
	cdate int not null default 0 comment '创建时间',
  cuid bigint not null default 0 comment '创建人ID'
)
comment '接口历史表' engine=InnoDB;
