drop database if exists `sip-base`;
create database `sip-base` default charset utf8 collate utf8_general_ci;
USE `sip-base`;

drop table if exists role;
create table role(
	id bigint auto_increment primary key,
	sensitive_headers varchar(5000) null comment '敏感头信息'
)
comment '角色表' engine=InnoDB;

drop table if exists user_role;
create table user_role(
  id bigint auto_increment primary key,
  tenant_id bigint not null default 0 comment '租户ID',
)comment '用户角色表' engine=InnoDB;

drop table if exists menu;
CREATE TABLE menu (
  id bigint auto_increment primary key,
  unique key (uid,type)
)comment '菜单表' engine=InnoDB;

drop table if exists role_menu;
CREATE TABLE role_menu (
  id bigint auto_increment primary key,
  unique key (tenant_id,name)
)comment '角色菜单表' engine=InnoDB;

drop table if exists role_menu;
CREATE TABLE role_menu (
  id bigint auto_increment primary key,
  unique key (tenant_id,name)
)comment '资源表' engine=InnoDB;

drop table if exists role_menu;
CREATE TABLE role_menu (
  id bigint auto_increment primary key,
  unique key (tenant_id,name)
)comment '菜单资源表' engine=InnoDB;

drop table if exists role_menu;
CREATE TABLE role_menu (
  id bigint auto_increment primary key,
  unique key (tenant_id,name)
)comment '角色权限表' engine=InnoDB;

drop table if exists role_menu;
CREATE TABLE role_menu (
  id bigint auto_increment primary key,
  unique key (tenant_id,name)
)comment '权限表' engine=InnoDB;

drop table if exists role_menu;
CREATE TABLE role_menu (
  id bigint auto_increment primary key,
  unique key (tenant_id,name)
)comment '权限资源表' engine=InnoDB;
