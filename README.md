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
### 功能特点
- a
- b
- c
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
├── sip-sso                // 单点登录
├── sip-permission         // 权限管理
├── sip-notify             // 通知服务
├── sip-schedule           // 调度服务
├── sip-api                // API服务
├── sip-log                // LOG服务
### 环境变量
kubernetes集群地址 KUBE_URL
