drop database if exists `sip-tools`;
create database `sip-tools` default charset utf8 collate utf8_general_ci;
USE `sip-tools`;

drop table if exists kube_charts;
create table kube_charts(
	id bigint auto_increment primary key,
	name varchar(64) not null default '' comment 'chart名',
	namespace varchar(64) not null default '' comment '命名空间',
  `values` text not null comment 'chart values',
	cdate int not null default 0 comment '创建时间',
  udate int not null default 0 comment '更新时间',
	unique key (name)
)
comment 'charts表' engine=InnoDB;
