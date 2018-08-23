服务集成平台SIP（Service Integration Platform）
===============================
[![Apache License 2](https://img.shields.io/badge/license-ASF2-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.txt)
[![Build Status](https://travis-ci.org/codecentric/spring-boot-admin.svg?branch=master)](https://travis-ci.org/codecentric/spring-boot-admin)
[![Coverage Status](https://coveralls.io/repos/github/codecentric/spring-boot-admin/badge.svg)](https://coveralls.io/github/codecentric/spring-boot-admin)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/de.codecentric/spring-boot-admin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/de.codecentric/spring-boot-admin/)

SIP服务集成平台描述
### 技术
- kotlin
- gradle
- tkmapper
### 项目结构
- 租户、应用
```
├── sip-docs               // 文档中心
│   ├── docs               // 文档说明
│   │   ├── api            // API文档
│   │   ├── server         // 服务端文档
│   │   └── client         // 客户端文档
│   └── sql                // 初始化sql
├── sip-core               // SIP核心依赖
├── sip-eureka             // 注册中心  
├── sip-getway             // 网关
├── sip-base               // SIP基础(租户、应用、用户)
├── sip-dict               // 字典服务
├── sip-permission         // 权限管理
├── sip-notify             // 通知服务
├── sip-logs               // Logs服务
├── sip-tools              // 常用工具服务(IP查询、Kubernetes Webhook)
├── sip-schedule           // 调度服务
├── sip-api                // API服务(TODO)

TODO
- notify接入短信、webhook
- 邮件、短信发送量统计
- 日志报警项在调用发送EMAIL、SMS时该条日志数据所有字段当做变量传入
