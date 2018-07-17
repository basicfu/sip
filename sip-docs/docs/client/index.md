# SIP客户端

## 引入依赖(maven)

```xml
<dependency>
    <groupId>com.basicfu.sip</groupId>
    <artifactId>sip-client</artifactId>
    <version>0.0.1</version>
</dependency>
```

## 引入依赖(gradle)

```
compile 'com.basicfu.sip:sip-client:0.0.1'
```

## 启用服务

> 在启动类上添加注解`@EnableSipClient`默认启用`sip-client`所有提供的服务

```java
@EnableSipClient
@SpringCloudApplication
class Application{
}
```

> 如果只想使用部分服务

```java
@EnableSipClient(enable = {Function.User,Function.Dict})
```

> 如果只想禁用部分服务

```
@EnableSipClient(disable = {Function.User,Function.Dict})
```

> enable和disable不可同时存在，完整的服务列表参见[服务列表](#服务列表)

## 服务列表

- 用户模块(User)
- 字典模块(Dict)
- 获取所有api接口(Interface)
