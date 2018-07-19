DROP TABLE IF EXISTS `sip-notify`;
create database `sip-notify` default charset utf8 collate utf8_general_ci;

DROP TABLE IF EXISTS mail_template;
CREATE TABLE mail_template(
  id BIGINT AUTO_INCREMENT PRIMARY KEY ,
  app_id BIGINT NOT NULL DEFAULT 0 COMMENT '应用id',
  sender_id BIGINT NOT NULL DEFAULT 0 COMMENT '发送者id',
  code VARCHAR(128) NOT NULL DEFAULT '' COMMENT '邮件模板code',
  to_user VARCHAR(2000) NOT NULL DEFAULT '' COMMENT '收件人,多个收件人使用 , 分割',
  send_type VARCHAR(64) NOT NULL DEFAULT 'mass' COMMENT '收件人发送类型,alone:单独发送,mass:群发',
  copy_user VARCHAR(2000) NOT NULL DEFAULT '' COMMENT '抄送人',
  subject VARCHAR(256) NOT NULL DEFAULT '' COMMENT '邮件主题',
  content LONGTEXT NOT NULL COMMENT '邮件正文',
  description VARCHAR(512) NOT NULL DEFAULT '' COMMENT '邮件描述',
  enable TINYINT NOT NULL DEFAULT 0 COMMENT '是否启用,0:禁用,1:启用',
  cdate int not null default 0 comment '创建时间',
  udate int not null default 0 comment '更新时间',
  UNIQUE KEY (app_id,code)
)COMMENT '邮件模板' engine=InnoDB;

DROP TABLE IF EXISTS mail_sender;
CREATE TABLE mail_sender(
  id BIGINT AUTO_INCREMENT PRIMARY KEY ,
  host VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'host地址',
  port INT NOT NULL DEFAULT 0 COMMENT '端口',
  app_id BIGINT NOT NULL COMMENT '应用id',
  from_uname VARCHAR(32) NOT NULL DEFAULT '' COMMENT '发件人用户名',
  from_upwd VARCHAR(32) NOT NULL DEFAULT '' COMMENT '发件人密码',
  from_name VARCHAR(64) NOT NULL DEFAULT '' COMMENT '发件人名',
  description VARCHAR(512) NOT NULL DEFAULT '' COMMENT '描述',
  cdate int not null default 0 comment '创建时间',
  udate int not null default 0 comment '更新时间',
  UNIQUE KEY (app_id,from_uname)
)COMMENT '邮件发送人配置' engine=InnoDB;