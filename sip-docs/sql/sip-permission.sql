drop database if exists `sip-permission`;
create database `sip-permission` default charset utf8 collate utf8_general_ci;
USE `sip-permission`;

drop table if exists role;
create table role(
  id bigint auto_increment primary key,
  app_id bigint not null default 0 comment '应用Id',
  code varchar(32) not null default '' comment '角色code',
  name varchar(32) not null default '' comment '角色名',
  enalbe tinyint not null default 0 comment '是否启用'
)
comment '角色表' engine=InnoDB;

drop table if exists user_role;
create table user_role(
  id bigint auto_increment primary key,
  user_id bigint not null default 0 comment '用户Id',
  role_id bigint not null default 0 comment '角色Id'
)
comment '用户角色表' engine=InnoDB;

drop table if exists menu;
CREATE TABLE menu (
  id bigint auto_increment primary key,
  app_id bigint not null default 0 comment '应用Id',
  pid bigint not null default 0 comment '父级Id',
  name varchar(32) not null default '' comment '菜单名',
  path varchar(255) not null default '' comment '菜单路径',
  sort int not null default 0 comment '菜单顺序',
  icon varchar(255) not null default '' comment '菜单图标',
  type varchar(64) not null default '' comment '菜单类型(页面,元素)',
  display tinyint not null default 0 comment '是否显示'
)
comment '菜单表' engine=InnoDB;

drop table if exists role_menu;
CREATE TABLE role_menu (
  id bigint auto_increment primary key,
  role_id bigint not null default 0 comment '角色ID',
  menu_id bigint not null default 0 comment '菜单ID'
)comment '角色菜单表' engine=InnoDB;

drop table if exists resource;
CREATE TABLE resource (
  id bigint auto_increment primary key,
  service_id bigint not null default 0 comment '服务Id',
  url varchar(255) not null default '' comment '资源URL',
  name varchar(100) not null default '' comment '资源名'
)comment '资源表' engine=InnoDB;

drop table if exists menu_resource;
CREATE TABLE menu_resource (
  id bigint auto_increment primary key,
  menu_id bigint not null default 0 comment '菜单Id',
  resource_id bigint not null default 0 comment '资源Id'
)comment '菜单资源表' engine=InnoDB;

drop table if exists role_permission;
CREATE TABLE role_permission (
  id bigint auto_increment primary key,
  role_id bigint not null default 0 comment '角色Id',
  permission_id bigint not null default 0 comment '权限Id'
)comment '角色权限表' engine=InnoDB;

create table permission(
  id bigint auto_increment primary key,
  app_id bigint not null default 0 comment '应用Id',
  name varchar(32) not null default '' comment '权限名'
)comment '权限表' engine=InnoDB;

drop table if exists permission_resource;
CREATE TABLE permission_resource (
  id bigint auto_increment primary key,
  permission_id bigint not null default 0 comment '权限Id',
  resource_id bigint not null default 0 comment '资源Id'
)comment '权限资源表' engine=InnoDB;
