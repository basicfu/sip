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
  app_id bigint not null default 0 comment '应用Id',
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
  icon varchar(5000) not null default '' comment '菜单图标',
  type varchar(64) not null default '' comment '菜单类型(页面,元素)',
  display tinyint not null default 0 comment '是否显示'
)
comment '菜单表' engine=InnoDB;

drop table if exists role_menu;
CREATE TABLE role_menu (
  id bigint auto_increment primary key,
  app_id bigint not null default 0 comment '应用Id',
  role_id bigint not null default 0 comment '角色ID',
  menu_id bigint not null default 0 comment '菜单ID'
)comment '角色菜单表' engine=InnoDB;

drop table if exists resource;
CREATE TABLE resource (
  id bigint auto_increment primary key,
  app_id bigint not null default 0 comment '应用Id',
  service_id bigint not null default 0 comment '服务Id',
  url varchar(255) not null default '' comment '资源URL',
  method varchar(10) not null default '' comment '请求方法(一个接口有多种请求方式强制拆成多个接口)',
  name varchar(100) not null default '' comment '资源名',
  unique key (service_id,url,method)
)comment '资源表' engine=InnoDB;

drop table if exists menu_resource;
CREATE TABLE menu_resource (
  id bigint auto_increment primary key,
  app_id bigint not null default 0 comment '应用Id',
  menu_id bigint not null default 0 comment '菜单Id',
  resource_id bigint not null default 0 comment '资源Id'
)comment '菜单资源表' engine=InnoDB;

drop table if exists role_permission;
CREATE TABLE role_permission (
  id bigint auto_increment primary key,
  app_id bigint not null default 0 comment '应用Id',
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
  app_id bigint not null default 0 comment '应用Id',
  permission_id bigint not null default 0 comment '权限Id',
  resource_id bigint not null default 0 comment '资源Id'
)comment '权限资源表' engine=InnoDB;

INSERT INTO menu (id,app_id, pid, name, path, sort, icon, type, display) VALUES
CREATE TABLE `sip-permission`.menu
(
    id bigint(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    app_id bigint(20) DEFAULT '0' NOT NULL COMMENT '应用Id',
    pid bigint(20) DEFAULT '0' NOT NULL COMMENT '父级Id',
    name varchar(32) DEFAULT '' NOT NULL COMMENT '菜单名',
    path varchar(255) DEFAULT '' NOT NULL COMMENT '菜单路径',
    sort int(11) DEFAULT '0' NOT NULL COMMENT '菜单顺序',
    icon varchar(5000) DEFAULT '' NOT NULL COMMENT '菜单图标',
    type varchar(64) DEFAULT '' NOT NULL COMMENT '菜单类型(页面,元素)',
    display tinyint(4) DEFAULT '0' NOT NULL COMMENT '是否显示'
);
INSERT INTO `sip-permission`.menu (app_id, pid, name, path, sort, icon, type, display) VALUES (1, 0, '基础服务', '/base', 0, '<svg viewBox="0 0 1024 1024" width="24px" height="24px" fill="currentColor">
  <path d="M464 144H160c-8.8 0-16 7.2-16 16v304c0 8.8 7.2 16 16 16h304c8.8 0 16-7.2 16-16V160c0-8.8-7.2-16-16-16zm-52 268H212V212h200v200zm452-268H560c-8.8 0-16 7.2-16 16v304c0 8.8 7.2 16 16 16h304c8.8 0 16-7.2 16-16V160c0-8.8-7.2-16-16-16zm-52 268H612V212h200v200zM464 544H160c-8.8 0-16 7.2-16 16v304c0 8.8 7.2 16 16 16h304c8.8 0 16-7.2 16-16V560c0-8.8-7.2-16-16-16zm-52 268H212V612h200v200zm452-268H560c-8.8 0-16 7.2-16 16v304c0 8.8 7.2 16 16 16h304c8.8 0 16-7.2 16-16V560c0-8.8-7.2-16-16-16zm-52 268H612V612h200v200z"></path>
</svg>', 'PAGE', 1);
INSERT INTO `sip-permission`.menu (app_id, pid, name, path, sort, icon, type, display) VALUES (1, 1, '应用管理', '/base/app', 0, '<svg viewBox="0 0 24 24" width="24px" height="24px" fill="currentColor">
  <path d="M17.27,6.73l-4.24,10.13l-1.32-3.42l-0.32-0.83l-0.82-0.32l-3.43-1.33L17.27,6.73 M21,3L3,10.53v0.98l6.84,2.65L12.48,21h0.98L21,3L21,3z"></path>
</svg>', 'PAGE', 1);
INSERT INTO `sip-permission`.menu (app_id, pid, name, path, sort, icon, type, display) VALUES (1, 1, '应用服务', '/base/app-service', 0, '<svg viewBox="0 0 24 24" width="24px" height="24px" fill="currentColor">
  <path d="M17.63,5.84C17.27,5.33,16.67,5,16,5L5,5.01C3.9,5.01,3,5.9,3,7v10c0,1.1,0.9,1.99,2,1.99L16,19c0.67,0,1.27-0.33,1.63-0.84L22,12L17.63,5.84z M16,17H5V7h11l3.55,5L16,17z"/>
</svg>', 'PAGE', 1);
INSERT INTO `sip-permission`.menu (app_id, pid, name, path, sort, icon, type, display) VALUES (1, 1, 'Secrets', '/base/app-secret', 0, '<svg viewBox="0 0 24 24" width="24px" height="24px" fill="currentColor">
  <path d="M20,4v12H8V4H20 M20,2H8C6.9,2,6,2.9,6,4v12c0,1.1,0.9,2,2,2h12c1.1,0,2-0.9,2-2V4C22,2.9,21.1,2,20,2L20,2z"/>
		<polygon points="11.5,11.67 13.19,13.93 15.67,10.83 19,15 9,15 		"/>
		<path d="M2,6v14c0,1.1,0.9,2,2,2h14v-2H4V6H2z"/>
</svg>', 'PAGE', 1);
INSERT INTO `sip-permission`.menu (app_id, pid, name, path, sort, icon, type, display) VALUES (1, 1, '用户模板', '/base/user-template', 0, '<svg viewBox="0 0 24 24" width="24px" height="24px" fill="currentColor">
  <path d="M19,2H5C3.9,2,3,2.9,3,4v14c0,1.1,0.9,2,2,2h4l3,3l3-3h4c1.1,0,2-0.9,2-2V4C21,2.9,20.1,2,19,2z M19,18h-4h-0.83l-0.59,0.59L12,20.17l-1.59-1.59L9.83,18H9H5V4h14V18z"/>
  <polygon points="12,17 13.88,12.88 18,11 13.88,9.12 12,5 10.12,9.12 6,11 10.12,12.88"/>
</svg>', 'PAGE', 1);
INSERT INTO `sip-permission`.menu (app_id, pid, name, path, sort, icon, type, display) VALUES (1, 1, '用户管理', '/base/user', 0, '<svg viewBox="0 0 1024 1024" width="24px" height="24px" fill="currentColor">
  <path d="M858.5 763.6a374 374 0 0 0-80.6-119.5 375.63 375.63 0 0 0-119.5-80.6c-.4-.2-.8-.3-1.2-.5C719.5 518 760 444.7 760 362c0-137-111-248-248-248S264 225 264 362c0 82.7 40.5 156 102.8 201.1-.4.2-.8.3-1.2.5-44.8 18.9-85 46-119.5 80.6a375.63 375.63 0 0 0-80.6 119.5A371.7 371.7 0 0 0 136 901.8a8 8 0 0 0 8 8.2h60c4.4 0 7.9-3.5 8-7.8 2-77.2 33-149.5 87.8-204.3 56.7-56.7 132-87.9 212.2-87.9s155.5 31.2 212.2 87.9C779 752.7 810 825 812 902.2c.1 4.4 3.6 7.8 8 7.8h60a8 8 0 0 0 8-8.2c-1-47.8-10.9-94.3-29.5-138.2zM512 534c-45.9 0-89.1-17.9-121.6-50.4S340 407.9 340 362c0-45.9 17.9-89.1 50.4-121.6S466.1 190 512 190s89.1 17.9 121.6 50.4S684 316.1 684 362c0 45.9-17.9 89.1-50.4 121.6S557.9 534 512 534z"></path>
</svg>', 'PAGE', 1);
INSERT INTO `sip-permission`.menu (app_id, pid, name, path, sort, icon, type, display) VALUES (1, 0, '字典服务', '/dict', 0, '<svg viewBox="0 0 24 24" width="24px" height="24px" fill="currentColor">
  <path d="M17.63,5.84C17.27,5.33,16.67,5,16,5L5,5.01C3.9,5.01,3,5.9,3,7v10c0,1.1,0.9,1.99,2,1.99L16,19
		c0.67,0,1.27-0.33,1.63-0.84L22,12L17.63,5.84z M16,17H5V7h11l3.55,5L16,17z"/>
</svg>', 'PAGE', 1);
INSERT INTO `sip-permission`.menu (app_id, pid, name, path, sort, icon, type, display) VALUES (1, 7, '字典管理', '/dict/dict', 0, '<svg viewBox="0 0 24 24" width="24px" height="24px" fill="currentColor">
  <path d="M18,2h-8L4,8v12c0,1.1,0.9,2,2,2h12c1.1,0,2-0.9,2-2V4C20,2.9,19.1,2,18,2z M18,20L6,20V8.83L10.83,4H18V20z"/>
		<rect x="9" y="7" width="2" height="4"/>
		<rect x="12" y="7" width="2" height="4"/>
		<rect x="15" y="7" width="2" height="4"/>
</svg>', 'PAGE', 1);
INSERT INTO `sip-permission`.menu (app_id, pid, name, path, sort, icon, type, display) VALUES (1, 0, '权限服务', '/permission', 0, '<svg viewBox="0 0 24 24" width="24px" height="24px" fill="currentColor">
  <path d="M18,8h-1V6c0-2.76-2.24-5-5-5S7,3.24,7,6v2H6c-1.1,0-2,0.9-2,2v10c0,1.1,0.9,2,2,2h12c1.1,0,2-0.9,2-2V10
		C20,8.9,19.1,8,18,8z M9,6c0-1.66,1.34-3,3-3s3,1.34,3,3v2H9V6z M18,20H6V10h12V20z M12,17c1.1,0,2-0.9,2-2c0-1.1-0.9-2-2-2
		c-1.1,0-2,0.9-2,2C10,16.1,10.9,17,12,17z"/>
</svg>', 'PAGE', 1);
INSERT INTO `sip-permission`.menu (app_id, pid, name, path, sort, icon, type, display) VALUES (1, 9, '角色管理', '/permission/role', 0, '<svg viewBox="0 0 24 24" width="24px" height="24px" fill="currentColor">
  <path d="M11,12c2.21,0,4-1.79,4-4c0-2.21-1.79-4-4-4S7,5.79,7,8C7,10.21,8.79,12,11,12z M11,6c1.1,0,2,0.9,2,2c0,1.1-0.9,2-2,2
			S9,9.1,9,8C9,6.9,9.9,6,11,6z"/>
		<path d="M5,18c0.2-0.63,2.57-1.68,4.96-1.94l2.04-2C11.61,14.02,11.32,14,11,14c-2.67,0-8,1.34-8,4v2h9l-2-2H5z"/>
		<polygon points="20.6,12.5 15.47,17.67 13.4,15.59 12,17 15.47,20.5 22,13.91 		"/>
</svg>', 'PAGE', 1);
INSERT INTO `sip-permission`.menu (app_id, pid, name, path, sort, icon, type, display) VALUES (1, 9, '菜单管理', '/permission/menu', 0, '<svg viewBox="0 0 24 24" width="24px" height="24px" fill="currentColor">
  <path d="M3,18h18v-2H3V18z M3,13h18v-2H3V13z M3,6v2h18V6H3z"></path>
</svg>', 'PAGE', 1);
INSERT INTO `sip-permission`.menu (app_id, pid, name, path, sort, icon, type, display) VALUES (1, 9, '权限管理', '/permission/permission', 0, '<svg viewBox="0 0 24 24" width="24px" height="24px" fill="currentColor">
  <path d="M18,8h-1V6c0-2.76-2.24-5-5-5S7,3.24,7,6v2H6c-1.1,0-2,0.9-2,2v10c0,1.1,0.9,2,2,2h12c1.1,0,2-0.9,2-2V10
		C20,8.9,19.1,8,18,8z M9,6c0-1.66,1.34-3,3-3s3,1.34,3,3v2H9V6z M18,20H6V10h12V20z M12,17c1.1,0,2-0.9,2-2c0-1.1-0.9-2-2-2
		c-1.1,0-2,0.9-2,2C10,16.1,10.9,17,12,17z"/>
</svg>', 'PAGE', 1);
INSERT INTO `sip-permission`.menu (app_id, pid, name, path, sort, icon, type, display) VALUES (1, 9, '资源管理', '/permission/resource', 0, '<svg viewBox="0 0 24 24" width="24px" height="24px" fill="currentColor">
  <path d="M19,19H5V5h7V3H5C3.89,3,3,3.9,3,5v14c0,1.1,0.89,2,2,2h14c1.1,0,2-0.9,2-2v-7h-2V19z M14,3v2h3.59l-9.83,9.83l1.41,1.41
		L19,6.41V10h2V3H14z"/>
</svg>', 'PAGE', 1);
INSERT INTO `sip-permission`.menu (app_id, pid, name, path, sort, icon, type, display) VALUES (1, 0, '日志服务', '/logs', 0, '<svg viewBox="0 0 24 24" width="24px" height="24px" fill="currentColor">
  <path d="M18,2H6C4.9,2,4,2.9,4,4v16c0,1.1,0.9,2,2,2h12c1.1,0,2-0.9,2-2V4C20,2.9,19.1,2,18,2z M9,4h2v5l-1-0.75L9,9V4z M18,20H6V4
		h1v9l3-2.25L13,13V4h5V20z"/>
</svg>', 'PAGE', 1);
INSERT INTO `sip-permission`.menu (app_id, pid, name, path, sort, icon, type, display) VALUES (1, 14, '日志模板', '/logs/template', 0, '<svg viewBox="0 0 24 24" width="24px" height="24px" fill="currentColor">
  <path d="M19,2H5C3.9,2,3,2.9,3,4v14c0,1.1,0.9,2,2,2h4l3,3l3-3h4c1.1,0,2-0.9,2-2V4C21,2.9,20.1,2,19,2z M19,18h-4h-0.83l-0.59,0.59L12,20.17l-1.59-1.59L9.83,18H9H5V4h14V18z"/>
  <polygon points="12,17 13.88,12.88 18,11 13.88,9.12 12,5 10.12,9.12 6,11 10.12,12.88"/>
</svg>', 'PAGE', 1);
INSERT INTO `sip-permission`.menu (app_id, pid, name, path, sort, icon, type, display) VALUES (1, 14, '日志报警', '/logs/warn', 0, '<svg viewBox="0 0 1024 1024" width="24px" height="24px" fill="currentColor">
  <path d="M849.12 928.704 174.88 928.704c-45.216 0-81.536-17.728-99.68-48.64-18.144-30.912-15.936-71.296 6.08-110.752L421.472 159.648c22.144-39.744 55.072-62.528 90.304-62.528s68.128 22.752 90.336 62.464l340.544 609.792c22.016 39.456 24.288 79.808 6.112 110.72C930.656 911.008 894.304 928.704 849.12 928.704zM511.808 161.12c-11.2 0-24.032 11.104-34.432 29.696L137.184 800.544c-10.656 19.136-13.152 36.32-6.784 47.168 6.368 10.816 22.592 17.024 44.48 17.024l674.24 0c21.92 0 38.112-6.176 44.48-17.024 6.336-10.816 3.872-28-6.816-47.136L546.24 190.816C535.872 172.224 522.976 161.12 511.808 161.12zM512 640c-17.664 0-32-14.304-32-32l0-288c0-17.664 14.336-32 32-32s32 14.336 32 32l0 288C544 625.696 529.664 640 512 640zM512 752.128m-48 0a1.5 1.5 0 1 0 96 0 1.5 1.5 0 1 0-96 0Z"></path>
</svg>', 'PAGE', 1);
INSERT INTO `sip-permission`.menu (app_id, pid, name, path, sort, icon, type, display) VALUES (1, 0, '通知服务', '/notify', 0, '<svg viewBox="0 0 24 24" width="24px" height="24px" fill="currentColor">
  <path d="M19,3H5C3.9,3,3,3.9,3,5v14c0,1.1,0.9,2,2,2h14c1.1,0,2-0.9,2-2V5C21,3.9,20.1,3,19,3z M19,19H5V5h14V19z"/>
		<polygon points="15,15 11,15 11,13 15,13 15,11 11,11 11,9 15,9 15,7 9,7 9,17 15,17 		"/>
</svg>', 'PAGE', 1);
INSERT INTO `sip-permission`.menu (app_id, pid, name, path, sort, icon, type, display) VALUES (1, 17, '邮件配置', '/notify/mail-sender', 0, '<svg viewBox="0 0 24 24" width="24px" height="24px" fill="currentColor">
  <path d="M19.43,12.98c0.04-0.32,0.07-0.64,0.07-0.98c0-0.34-0.03-0.66-0.07-0.98l2.11-1.65c0.19-0.15,0.24-0.42,0.12-0.64l-2-3.46
			c-0.09-0.16-0.26-0.25-0.44-0.25c-0.06,0-0.12,0.01-0.17,0.03l-2.49,1c-0.52-0.4-1.08-0.73-1.69-0.98l-0.38-2.65
			C14.46,2.18,14.25,2,14,2h-4C9.75,2,9.54,2.18,9.51,2.42L9.13,5.07C8.52,5.32,7.96,5.66,7.44,6.05l-2.49-1
			C4.89,5.03,4.83,5.02,4.77,5.02c-0.17,0-0.34,0.09-0.43,0.25l-2,3.46C2.21,8.95,2.27,9.22,2.46,9.37l2.11,1.65
			C4.53,11.34,4.5,11.67,4.5,12c0,0.33,0.03,0.66,0.07,0.98l-2.11,1.65c-0.19,0.15-0.24,0.42-0.12,0.64l2,3.46
			c0.09,0.16,0.26,0.25,0.44,0.25c0.06,0,0.12-0.01,0.17-0.03l2.49-1c0.52,0.4,1.08,0.73,1.69,0.98l0.38,2.65
			C9.54,21.82,9.75,22,10,22h4c0.25,0,0.46-0.18,0.49-0.42l0.38-2.65c0.61-0.25,1.17-0.59,1.69-0.98l2.49,1
			c0.06,0.02,0.12,0.03,0.18,0.03c0.17,0,0.34-0.09,0.43-0.25l2-3.46c0.12-0.22,0.07-0.49-0.12-0.64L19.43,12.98z M17.45,11.27
			c0.04,0.31,0.05,0.52,0.05,0.73c0,0.21-0.02,0.43-0.05,0.73l-0.14,1.13l0.89,0.7l1.08,0.84l-0.7,1.21l-1.27-0.51l-1.04-0.42
			l-0.9,0.68c-0.43,0.32-0.84,0.56-1.25,0.73l-1.06,0.43l-0.16,1.13L12.7,20H11.3l-0.19-1.35l-0.16-1.13l-1.06-0.43
			c-0.43-0.18-0.83-0.41-1.23-0.71l-0.91-0.7l-1.06,0.43l-1.27,0.51l-0.7-1.21l1.08-0.84l0.89-0.7l-0.14-1.13
			C6.52,12.43,6.5,12.2,6.5,12s0.02-0.43,0.05-0.73l0.14-1.13L5.8,9.44L4.72,8.6l0.7-1.21l1.27,0.51l1.04,0.42l0.9-0.68
			c0.43-0.32,0.84-0.56,1.25-0.73l1.06-0.43l0.16-1.13L11.3,4h1.39l0.19,1.35l0.16,1.13l1.06,0.43c0.43,0.18,0.83,0.41,1.23,0.71
			l0.91,0.7l1.06-0.43l1.27-0.51l0.7,1.21L18.2,9.44l-0.89,0.7L17.45,11.27z"/>
		<path d="M12,8c-2.21,0-4,1.79-4,4s1.79,4,4,4s4-1.79,4-4S14.21,8,12,8z M12,14c-1.1,0-2-0.9-2-2s0.9-2,2-2s2,0.9,2,2
			S13.1,14,12,14z"/>
</svg>', 'PAGE', 1);
INSERT INTO `sip-permission`.menu (app_id, pid, name, path, sort, icon, type, display) VALUES (1, 17, '邮件模板', '/notify/mail-template', 0, '<svg viewBox="0 0 24 24" width="24px" height="24px" fill="currentColor">
  <path d="M22,6c0-1.1-0.9-2-2-2H4C2.9,4,2,4.9,2,6v12c0,1.1,0.9,2,2,2h16c1.1,0,2-0.9,2-2V6z M20,6l-8,4.99L4,6H20zM20,18L4,18V8l8,5l8-5V18z"></path>
</svg>', 'PAGE', 1);
INSERT INTO `sip-permission`.menu (app_id, pid, name, path, sort, icon, type, display) VALUES (1, 17, '短信模板', '/notify/sms', 0, '<svg viewBox="0 0 24 24" width="24px" height="24px" fill="currentColor">
  <path d="M18,8h-1V6c0-2.76-2.24-5-5-5S7,3.24,7,6v2H6c-1.1,0-2,0.9-2,2v10c0,1.1,0.9,2,2,2h12c1.1,0,2-0.9,2-2V10
		C20,8.9,19.1,8,18,8z M9,6c0-1.66,1.34-3,3-3s3,1.34,3,3v2H9V6z M18,20H6V10h12V20z M12,17c1.1,0,2-0.9,2-2c0-1.1-0.9-2-2-2
		c-1.1,0-2,0.9-2,2C10,16.1,10.9,17,12,17z"/>
</svg>', 'PAGE', 1);
INSERT INTO `sip-permission`.menu (app_id, pid, name, path, sort, icon, type, display) VALUES (1, 17, '短信配置', '/notify/sms-config', 0, '<svg viewBox="0 0 24 24" width="24px" height="24px" fill="currentColor">
  <path d="M19,19H5V5h7V3H5C3.89,3,3,3.9,3,5v14c0,1.1,0.89,2,2,2h14c1.1,0,2-0.9,2-2v-7h-2V19z M14,3v2h3.59l-9.83,9.83l1.41,1.41
		L19,6.41V10h2V3H14z"/>
</svg>', 'PAGE', 1);
INSERT INTO `sip-permission`.menu (app_id, pid, name, path, sort, icon, type, display) VALUES (1, 0, '工具服务', '/tools', 0, '<svg viewBox="0 0 24 24" width="24px" height="24px" fill="currentColor">
  <path d="M22,9.24l-7.19-0.62L12,2L9.19,8.63L2,9.24l5.46,4.73L5.82,21L12,17.27L18.18,21l-1.63-7.03L22,9.24z M12,15.4l-3.76,2.27
		l1-4.28l-3.32-2.88l4.38-0.38L12,6.1l1.71,4.04l4.38,0.38l-3.32,2.88l1,4.28L12,15.4z"/>
</svg>', 'PAGE', 1);
INSERT INTO `sip-permission`.menu (app_id, pid, name, path, sort, icon, type, display) VALUES (1, 22, 'kubeCharts', '/tools/kube-charts', 0, '<svg viewBox="0 0 24 24" width="24px" height="24px" fill="currentColor">
  <path d="M18,2H6C4.9,2,4,2.9,4,4v16c0,1.1,0.9,2,2,2h12c1.1,0,2-0.9,2-2V4C20,2.9,19.1,2,18,2z M9,4h2v5l-1-0.75L9,9V4z M18,20H6V4
		h1v9l3-2.25L13,13V4h5V20z"/>
</svg>', 'PAGE', 1);
INSERT INTO `sip-permission`.menu (app_id, pid, name, path, sort, icon, type, display) VALUES (1, 0, '首页', '/', -1, '<svg viewBox="0 0 24 24" width="24px" height="24px" fill="currentColor">
  <path d="M12,5.69l5,4.5V12v6h-2v-4v-2h-2h-2H9v2v4H7v-6v-1.81L12,5.69 M12,3L2,12h3v8h6v-6h2v6h6v-8h3L12,3L12,3z"></path>
</svg>', 'PAGE', 1);
