# 用户管理

## 获取当前用户信息

> 获取当前登录的用户信息

请求方式：`GET`

请求地址：`/user`

请求类型：`-`

请求参数：`-`

响应参数：

| 名称          | 类型     | 描述                |
| :---------- | :----- | :---------------- |
| id          | Number | 唯一标识              |
| appId       | Number | 应用唯一标识            |
| appCode     | String | 应用Code            |
| username    | String | 用户名               |
| mobile      | String | 手机号               |
| email       | String | 邮箱                |
| cdate       | Number | 创建时间              |
| udate       | Number | 更新时间              |
| ldate       | Number | 最后一次登录时间          |
| type        | String | 用户类型字典(USER_TYPE) |
| status      | Number | 用户状态              |
| roles       | Array  | 用户拥有的角色           |
| menus       | Array  | 用户拥有的菜单           |
| permissions | Array  | 用户拥有的权限           |
| resources   | Object | 用户拥有的资源           |

请求示例：`-`

响应示例：

```Json
{
  "roles": [
    {
      "enalbe": true,
      "code": "GUEST",
      "appId": 1,
      "name": "访客",
      "id": 5
    }
  ],
  "mobile": null,
  "resources": {
    "1": [
      "/POST/user/login",
      "/GET/user",
      "/GET/user/logout",
      "/GET/user-template/list"
    ]
  },
  "appCode": "sip",
  "type": "SYSTEM_SUPER_ADMIN",
  "content": null,
  "cdate": 1533019466,
  "permissions": null,
  "appId": 1,
  "ldate": 1539571930,
  "id": 1,
  "menus": [],
  "udate": 1539139074,
  "email": null,
  "status": 0,
  "username": "test"
}
```

## 根据用户ID获取用户详情

> 根据用户ID获取用户详情

请求方式：`GET`

请求地址：`/user/get/{id}`

请求类型：`-`

请求参数：

| 名称   | 类型     | 必需(Y/N) | 默认值  | 描述   |
| :--- | :----- | :------ | ---- | :--- |
| id   | Number | Y       | -    | 用户ID |

响应参数：

| 名称       | 类型     | 描述                |
| :------- | :----- | :---------------- |
| id       | Number | 唯一标识              |
| appId    | Number | 应用唯一标识            |
| appCode  | String | 应用Code            |
| username | String | 用户名               |
| mobile   | String | 手机号               |
| email    | String | 邮箱                |
| cdate    | Number | 创建时间              |
| udate    | Number | 更新时间              |
| ldate    | Number | 最后一次登录时间          |
| type     | String | 用户类型字典(USER_TYPE) |
| status   | Number | 用户状态              |

请求示例：`-`

响应示例：

```json
{
  "type": "SYSTEM_SUPER_ADMIN",
  "branch": "测试部门",
  "content": null,
  "cdate": 1533019466,
  "permissions": null,
  "appId": 1,
  "nickname": "付亮",
  "company": "北京简游网络北京简游网络",
  "ldate": 1539571930,
  "id": 1,
  "menus": null,
  "udate": 1539139074,
  "email": null,
  "status": 0,
  "username": "test"
}
```

## 查询

> 查询用户列表

请求方式：`GET`

请求地址：`/user/list`

请求类型：`-`

支持分页: `Y`

请求参数：

| 名称   | 类型     | 必需(Y/N) | 默认值  | 描述      |
| :--- | :----- | :------ | ---- | :------ |
| name | String | N       | -    | 字段名(模糊) |

响应参数：

| 名称       | 类型     | 描述                |
| :------- | :----- | :---------------- |
| id       | Number | 唯一标识              |
| appId    | Number | 应用唯一标识            |
| appCode  | String | 应用Code            |
| username | String | 用户名               |
| mobile   | String | 手机号               |
| email    | String | 邮箱                |
| cdate    | Number | 创建时间              |
| udate    | Number | 更新时间              |
| ldate    | Number | 最后一次登录时间          |
| type     | String | 用户类型字典(USER_TYPE) |
| status   | Number | 用户状态              |

请求示例：

```json
{
  "username": "test"
}
```

响应示例：

```json
[
  {
    "roles": null,
    "mobile": null,
    "resources": null,
    "appCode": null,
    "type": "APP_ADMIN",
    "branch": "123",
    "cdate": 1539331521,
    "permissions": null,
    "appId": 1,
    "nickname": "123",
    "company": "123",
    "ldate": 0,
    "id": 6,
    "menus": null,
    "udate": 1539332888,
    "email": null,
    "status": 0,
    "username": "basicfu1"
  }
]
```

## 批量根据用户ID获取用户详情

> 批量根据用户ID获取用户详情

请求方式：`GET`

请求地址：`/user/list/{ids}`

请求类型：`-`

请求参数：

| 名称   | 类型    | 必需(Y/N) | 默认值  | 描述     |
| :--- | :---- | :------ | ---- | :----- |
| ids  | Array | Y       | -    | 用户ID数组 |

响应参数：

| 名称       | 类型     | 描述                |
| :------- | :----- | :---------------- |
| id       | Number | 唯一标识              |
| appId    | Number | 应用唯一标识            |
| appCode  | String | 应用Code            |
| username | String | 用户名               |
| mobile   | String | 手机号               |
| email    | String | 邮箱                |
| cdate    | Number | 创建时间              |
| udate    | Number | 更新时间              |
| ldate    | Number | 最后一次登录时间          |
| type     | String | 用户类型字典(USER_TYPE) |
| status   | Number | 用户状态              |

请求示例：`-`

响应示例：

```json
[
  {
    "type": "SYSTEM_SUPER_ADMIN",
    "branch": "测试部门",
    "content": null,
    "cdate": 1533019466,
    "permissions": null,
    "appId": 1,
    "nickname": "付亮",
    "company": "北京简游网络北京简游网络",
    "ldate": 1539571930,
    "id": 1,
    "menus": null,
    "udate": 1539139074,
    "email": null,
    "status": 0,
    "username": "test"
  }
]
```

## 批量根据用ID获取用户名

> 批量根据用ID获取用户名

请求方式：`GET`

请求地址：`/user/list/username/{ids}`

请求类型：`-`

请求参数：

| 名称   | 类型    | 必需(Y/N) | 默认值  | 描述     |
| :--- | :---- | :------ | ---- | :----- |
| ids  | Array | Y       | -    | 用户ID数组 |

响应参数：

| 名称       | 类型     | 描述   |
| :------- | :----- | :--- |
| id       | Number | 唯一标识 |
| username | String | 用户名  |

请求示例：`-`

响应示例：

```json
[
  {
    "id": 1,
    "username": "test"
  }
]
```

## 用户suggest

> 用户列表模糊查询

请求方式：`GET`

请求地址：`/user/suggest/{name}`

请求类型：`-`

请求参数：

| 名称       | 类型     | 必需(Y/N) | 默认值  | 描述                     |
| :------- | :----- | :------ | ---- | :--------------------- |
| name     | String | Y       | -    | 用户名、手机号或邮箱             |
| roleCode | String | N       | -    | 如果有角色code只模糊角色code中的用户 |
| limit    | Number | N       | 20   | 需要得到的结果条数,最大100        |

响应参数：

| 名称       | 类型     | 描述                |
| :------- | :----- | :---------------- |
| id       | Number | 唯一标识              |
| appId    | Number | 应用唯一标识            |
| appCode  | String | 应用Code            |
| username | String | 用户名               |
| mobile   | String | 手机号               |
| email    | String | 邮箱                |
| cdate    | Number | 创建时间              |
| udate    | Number | 更新时间              |
| ldate    | Number | 最后一次登录时间          |
| type     | String | 用户类型字典(USER_TYPE) |
| status   | Number | 用户状态              |

请求示例：`-`

响应示例：

```json
[
  {
    "type": "SYSTEM_SUPER_ADMIN",
    "branch": "测试部门",
    "content": null,
    "cdate": 1533019466,
    "permissions": null,
    "appId": 1,
    "nickname": "付亮",
    "company": "北京简游网络北京简游网络",
    "ldate": 1539571930,
    "id": 1,
    "menus": null,
    "udate": 1539139074,
    "email": null,
    "status": 0,
    "username": "test"
  }
]
```

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

| 名称       | 类型     | 描述                |
| :------- | :----- | :---------------- |
| roles    | Array  | 用户拥有的角色           |
| time     | Number | 登录成功的时间戳          |
| token    | String | 登录成功后的Token后续请求使用 |
| username | String | 用户名               |

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
  "roles": [
    {
      "enalbe": true,
      "code": "GUEST",
      "appId": 1,
      "name": "访客",
      "id": 5
    }
  ],
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

## 添加

> 添加用户

请求方式：`POST`

请求地址：`/user/insert`

请求类型：`application/json`

请求参数：

| 名称       | 类型     | 必需(Y/N) | 默认值  | 描述                |
| -------- | ------ | ------- | ---- | ----------------- |
| username | String | Y       | -    | 用户名               |
| type     | String | Y       | -    | 用户类型(字典USER_TYPE) |
| mobile   | String | N       | -    | 手机号               |
| email    | String | N       | -    | 邮箱                |
| password | String | Y       | -    | 密码                |
| roleIds  | Array  | N       | -    | 用户拥有的角色           |
| ...      | ...    | ...     | ...  | 附加用户字段见用户模板描述     |

响应参数：见通用

请求示例：

```json
{
  "username": "test",
  "type": "NORMAL",
  "roleIds": [6],
  "mobile": "18611331160",
  "email": "basicfu@gmail.com",
  "password": "1"
}
```

响应示例：见通用

## 更新

> 更新用户

请求方式：`POST`

请求地址：`/user/update`

请求类型：`application/json`

请求参数：

| 名称       | 类型     | 必需(Y/N) | 默认值  | 描述                |
| -------- | ------ | ------- | ---- | ----------------- |
| id       | Number | Y       | -    | 用户唯一标识            |
| username | String | Y       | -    | 用户名               |
| type     | String | Y       | -    | 用户类型(字典USER_TYPE) |
| mobile   | String | N       | -    | 手机号               |
| email    | String | N       | -    | 邮箱                |
| password | String | N       | -    | 密码                |
| roleIds  | Array  | N       | -    | 用户拥有的角色           |
| ...      | ...    | ...     | ...  | 附加用户字段见用户模板描述     |

响应参数：见通用

请求示例：

```json
{
  "id": 1,
  "username": "test",
  "type": "NORMAL",
  "roleIds": [6],
  "mobile": "18611331160",
  "email": "basicfu@gmail.com",
  "password": "1"
}
```

响应示例：见通用

## 删除

> 删除用户

请求方式：`DELETE`

请求地址：`/user/delete`

请求类型：`application/json`

请求参数：

| 名称   | 类型    | 必需(Y/N) | 默认值  | 描述     |
| ---- | ----- | ------- | ---- | ------ |
| ids  | Array | Y       | -    | 唯一标识数组 |

响应参数：见通用

请求示例：

```json
{
  "ids": [1,2]
}
```

响应示例：见通用
