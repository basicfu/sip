# 服务集成平台SIP（Service Integration Platform）

SIP是基于Spring Cloud微服务化的后台管理系统，集成应用、服务、用户、字典、权限、调度、通知等多个常用服务，SIP致力于开发集成所有常用的后台管理系统依赖的常用服务

## 技术

- kotlin
- gradle
- tkmapper

## [TODO](https://github.com/basicfu/sip/projects/1)

## 运行

- 初始化`sip-docs/sql`下所有sql


- 启动服务
  - docker环境执行`docker-compose up`
  - 开发环境依次启动`eureka`、`getway`、`base`三个必须服务
- 访问`localhost:7100`

## [运行UI](https://github.com/basicfu/sip-web)

## 简化封装写法

[TkMapper](https://github.com/basicfu/sip/wiki/TkMapper-kotlin)

[快速创建kotlin类](https://github.com/basicfu/sip/wiki/%E5%BF%AB%E9%80%9F%E5%88%9B%E5%BB%BAkotlin%E7%B1%BB)

## MybatisGenerator生成使用

- 修改`tool/generator`中的参数
- 执行`./gradlew :sip-core:mybatisGenerator`即可生成
- 自动生成的`model`和`mapper`为`java`类，请使用`idea`一键转换为`kotlin`文件

## 项目结构

```
#已确定模块
├── sip-docs               // 文档中心
├── sip-core               // JAVA核心依赖(通用类,不含任何业务)
├── sip-common             // SIP核心依赖(通用类,含业务)
├── sip-eureka             // 注册中心  
├── sip-getway             // 网关
├── sip-base               // SIP基础(应用、服务、用户、字典、权限)
├── sip-notify             // 通知服务
#不确定模块
├── sip-schedule           // 调度服务
├── sip-approval           // 审批流
├── sip-api                // API服务(应用)
├── sip-ci                // CI服务(应用)
├── sip-tools              // 常用工具服务(IP查询、Kubernetes Webhook)
```
