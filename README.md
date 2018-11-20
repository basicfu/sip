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
├── sip-core               // JAVA核心依赖(通用类,不含任何业务)
├── sip-common             // SIP核心依赖(通用类,含业务)
├── sip-eureka             // 注册中心  
├── sip-getway             // 网关
├── sip-base               // SIP基础(租户、应用、用户)
├── sip-dict               // 字典服务
├── sip-permission         // 权限管理
├── sip-notify             // 通知服务
├── sip-logs               // Logs服务
├── sip-tools              // 常用工具服务(IP查询、Kubernetes Webhook)
├── sip-schedule           // 调度服务
├── sip-api                // API服务
├── sip-approval           // 审批流


├── sip-eureka             // 注册中心  
├── sip-getway             // 网关
├── sip-base               // SIP基础(应用、服务、用户、字典、权限)
├── sip-notify             // 通知服务
├── sip-logs               // Logs服务
├── sip-schedule           // 调度服务
├── sip-api                // API服务
├── sip-approval           // 审批流




TODO
- 用户添加登录次数、创建人

- 新建应用时初始化一些数据
	- 根字典
	- 未登录用户token
	
	
- 所有的表必须有cdate和udate

- notify接入短信、webhook
- 邮件、短信发送量统计
- 日志报警项在调用发送EMAIL、SMS时该条日志数据所有字段当做变量传入
- 删除字典时需要判断用户模板

#提供两种应用方式
- 每个表增加应用ID
- 每个应用一个独立数据库，采用docker动态

url中app参数为系统应用，不能占用


feat: 添加了一个新功能
fix: 修复了一个bug
docs: 文档发生修改
style: 代码格式变化
refactor: 重构代码且不引进新的功能或修复bug
test: 添加或修改测试用例
ci: 持续集成
sql: SQL变动
update: 参数发生变化






1.sip调用sip应用中的url
/base/list?app=sip            =>   /sip/base/list
2.wutong调用wutong应用中的url
/base/list?app=wutong   =>   /wutong/base/list
3.wutong调用sip中的url
/base/list?app=wutong&system=0   =>   /sip/base/list
当存在system参数时则调用系统应用

#mybatisGenerator
./gradlew :sip-core:mybatisGenerator

val len=keys test_*
//1.如果有登录删除所有已登录
if(len>0){
clear*

//2.允许单个账号同时2台设备登录
if(len>2){
clear*


#关于后台参数说明
查询条件 过滤null/underfind/""

必填   过滤null/underfind/""
非必填  过滤null/underfind



#### 用户类型

- 系统用户(账号属于sip的应用，类型为0)
  - 只能登录sip应用，无法登录其他应用
  - 默认分配一个超管admin/admin的账号，不能删除，权限过滤器针对超管做权限跳过
  - 属于sip应用的用户拥有可以切换任意一个应用后台的权限
- 应用管理员(账号属于自己的应用/sip，类型为1)
  - 可以登录sip应用管理自己的应用，（也可以登录自己的应用，待思考）
  - 新建应用后默认分配一个应用超管，账号归属于自己的应用，可以新建普通管理员和普通用户，由自定义角色的权限决定
- 应用普通管理员
- 普通用户(账号属于自己的应用，类型为2)
  - 只能登录自己的应用，不能登录其他应用

sip   0  系统超级管理员 只有一个                       （0，1，2这三类应用无视应用唯一，2，3这两类还要保证应用下唯一）
sip   1  系统普通管理员 可以多个 可以切换多套用户权限

sip 2  应用超级管理员 只有一个 应用超管用户名需要唯一(默认应用名，后期再设置自定义)
sip 3  应用普通管理员 可以多个 登录时以 应用名+@+用户名                        

other 4  普通用户      无限个                          （应用下唯一）

用户类型为0，1，2的用户名不能重复




  2.1关于资源（唯一一个跨应用关联的功能）
  应用(权限/菜单)可以添加自己应用中的资源也可以添加系统应用中的资源
  3.1一/多套用户体系问题
  后期考虑怎么实现一套用户和多套用户功能相互切换，一套用户可以登录任意系统，多套系统就是目前使用的方式
2.一套用户体系（以后考虑怎么实现可以每个应用每个应用体系，或者共用一套用户体系），可以共用用户字段信息，每个应用可以拥有独立的用户字段



系统设置
通用
最大附件大小（单位M或KB）
默认每页数
每页对象选项（20，100）最大100

身份验证
自助注册（禁用，电子邮件激活，手机号激活，自动激活，手动激活(注册后不可用，需要从后台改为可用)）
允许使用邮箱、手机登录
密码最小长度（默认8）
数天之后，用以强制更改密码（0禁用）
（重置密码允许和上次相同，考虑是否实用）
登录错误次数超过多少次（默认5）
被禁用登录时长（默认30分钟）
（多久内被禁用多少次永久禁止，需管理员解除，考虑是否实用）
登录超时时长（考虑多久刷新一次登录，以及活动是否刷新）
登录模式（1，2，3）
强制在下次登录时更改密码
分配随机密码邮件通知

修改密码后，清空已在线用户


账户
允许用户账户注销
用户注册后默认角色（）

邮件通知


系统状态信息

