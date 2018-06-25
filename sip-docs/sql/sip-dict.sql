drop database if exists `sip-dict`;
create database `sip-dict` default charset utf8 collate utf8_general_ci;
USE `sip-dict`;

drop table if exists dict;
create table dict (
  id bigint auto_increment comment '主键' primary key,
  name varchar(255) NOT NULL DEFAULT '' COMMENT '字典名',
  value varchar(255) NOT NULL DEFAULT '' COMMENT '字典值',
  lft bigint NOT NULL DEFAULT 0 COMMENT '左节点',
  rgt bigint NOT NULL DEFAULT 0 COMMENT '右节点',
  lvl int NOT NULL DEFAULT 0 COMMENT '节点层级',
  fixed tinyint NOT NULL DEFAULT 0 COMMENT '能否固定 0否,1是',
  isdel tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除 0否,1是',
  KEY key_value (value),
  KEY key_lft (lft),
  KEY key_rgt (rgt),
  KEY key_isdel (isdel)
)comment '字典表' engine=InnoDB;
-- 添加desc描述字段