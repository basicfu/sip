# 用户管理

## 登录

> 登录以便请求后续需要权限的接口

请求方式：`POST`

请求地址：`/user/login`

请求类型：`application/json`

请求参数：

| 名称       | 类型     | 必需(Y/N) | 默认值  | 描述   |
| :------- | :----- | :------ | ---- | :--- |
| username | String | Y       | -    | 用户名  |
| password | String | Y       | -    | 密码   |

响应参数：

| 名称       | 类型      | 描述                |
| :------- | :------ | :---------------- |
| success  | Boolean | 登录成功              |
| time     | Number  | 登录成功的时间戳          |
| token    | String  | 登录成功后的Token后续请求使用 |
| username | String  | 用户名               |

请求示例：

```json
{
  "username": "test",
  "password": "test"
}
```

响应示例：

```json
{
  "success": true,
  "time":1531799162118,
  "token": "796e87533d16454b86cc7e9e6dc9aa84",
  "username": "test"
}
```

## 注销

> 退出当前登录的用户

请求方式：`GET`

请求地址：`/user/logout`

请求类型：无

请求参数：无

响应参数：无

请求示例：无

响应示例：无
