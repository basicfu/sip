drop database if exists `sip-logs`;
create database `sip-logs` default charset utf8 collate utf8_general_ci;
USE `sip-logs`;

drop table if exists template;
CREATE TABLE template (
  id bigint auto_increment primary key,
  app_id bigint not null default 0 comment '应用ID',
  name varchar(32) not null default '' comment '模板名',
  cdate int not null default 0 comment '创建时间',
  udate int not null default 0 comment '更新时间'
)comment '日志模板' engine=InnoDB;

drop table if exists template_field;
CREATE TABLE template_field (
  id bigint auto_increment primary key,
  template_id bigint not null default 0 comment '模板ID',
  name varchar(32) not null default '' comment '字段名',
  en_name varchar(32) not null default '' comment '字段英文名',
  sort int not null default 0 comment '字段顺序',
  cdate int not null default 0 comment '创建时间',
  udate int not null default 0 comment '更新时间',
  unique key (template_id,en_name)
)comment '模板字段' engine=InnoDB;

drop table if exists warn;
CREATE TABLE warn (
  id bigint auto_increment primary key,
  name varchar(32) not null default '' comment '规则名',
  template_id bigint not null default 0 comment '模板ID',
  cdate int not null default 0 comment '创建时间',
  udate int not null default 0 comment '更新时间'
)comment '报警项' engine=InnoDB;

drop table if exists warn_mode;
CREATE TABLE warn_mode (
  id bigint auto_increment primary key,
  warn_id bigint not null default 0 comment '报警项ID',
  mode varchar(32) not null default '' comment '报警模式(notify模块的所有类型(邮件、短信、webhook、微信/QQ))',
  template_id bigint not null default 0 comment '通知模式的模板ID',
  cdate int not null default 0 comment '创建时间',
  udate int not null default 0 comment '更新时间'
)comment '报警模式' engine=InnoDB;

drop table if exists warn_rule;
CREATE TABLE warn_rule (
  id bigint auto_increment primary key,
  name varchar(32) not null default '' comment '规则名',
  en_name varchar(32) not null default '' comment '模板下的字段英文名',
  `condition` varchar(32) not null default '' comment '条件字典(>=,>,<=,<,=,!=,equals,not equals)',
  value varchar(512) not null default '' comment '条件值',
  cdate int not null default 0 comment '创建时间',
  udate int not null default 0 comment '更新时间'
)comment '报警规则' engine=InnoDB;

drop table if exists logs;
CREATE TABLE logs (
  id bigint auto_increment primary key,
  template_id bigint not null default 0 comment '模板ID',
  content text not null comment '日志json内容',
  cdate bigint not null default 0 comment '创建时间(ms)'
)comment '日志表' engine=InnoDB;

