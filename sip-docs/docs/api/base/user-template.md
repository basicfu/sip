# 用户模板

## 查询

> 查询用户模板

请求方式：`GET`

请求地址：`/user-template/list`

请求类型：`-`

支持分页: `Y`

请求参数：

| 名称   | 类型     | 必需(Y/N) | 默认值  | 描述      |
| :--- | :----- | :------ | ---- | :------ |
| name | String | N       | -    | 字段名(模糊) |

响应参数：

| 名称           | 类型      | 描述                                       |
| :----------- | :------ | :--------------------------------------- |
| id           | Number  | 唯一标识                                     |
| name         | String  | 字段名                                      |
| enName       | String  | 字段英文名                                    |
| type         | String  | 字段类型(FIELD_TYPE)                         |
| extra        | String  | 扩展信息:<br />- TEXT限制字段长度范围<br />- NUMBER限制字段长度且范围大小10,2(0.15~255.25)<br />- CHECK/RADIO/SELECT存储关联的字典<br />- DATA自定义格式化时间yyyy-MM-dd HH:mm:ss |
| defaultValue | String  | 默认值                                      |
| required     | Boolean | 是否必填                                     |
| sort         | Number  | 顺序                                       |
| cdate        | Number  | 创建时间戳秒                                   |
| udate        | Number  | 更新时间戳秒                                   |

请求示例：

```json
{
  "username": "test"
}
```

响应示例：

```json
[{
  "id": 1,
  "appId": 1,
  "name": "昵称",
  "enName": "nickname",
  "type": "TEXT",
  "extra": "2~32",
  "defaultValue": "",
  "required": true,
  "sort": 1,
  "cdate": 1531817055,
  "udate": 1531817055
}]
```

## 查询所有

> 查询所有用户模板

请求方式：`GET`

请求地址：`/user-template/all`

请求类型：`-`

请求参数：`-`

响应参数：

| 名称           | 类型      | 描述                                       |
| :----------- | :------ | :--------------------------------------- |
| id           | Number  | 唯一标识                                     |
| name         | String  | 字段名                                      |
| enName       | String  | 字段英文名                                    |
| type         | String  | 字段类型(FIELD_TYPE)                         |
| extra        | String  | 扩展信息:<br />- TEXT限制字段长度范围<br />- NUMBER限制字段长度且范围大小10,2(0.15~255.25)<br />- CHECK/RADIO/SELECT存储关联的字典<br />- DATA自定义格式化时间yyyy-MM-dd HH:mm:ss |
| defaultValue | String  | 默认值                                      |
| required     | Boolean | 是否必填                                     |
| sort         | Number  | 顺序                                       |
| cdate        | Number  | 创建时间戳秒                                   |
| udate        | Number  | 更新时间戳秒                                   |

请求示例：`-`

响应示例：

```json
[{
  "id": 1,
  "appId": 1,
  "name": "昵称",
  "enName": "nickname",
  "type": "TEXT",
  "extra": "2~32",
  "defaultValue": "",
  "required": true,
  "sort": 1,
  "cdate": 1531817055,
  "udate": 1531817055
}]
```

## 添加

> 添加用户模板

请求方式：`POST`

请求地址：`/user-template/insert`

请求类型：`application/json`

请求参数：

| 名称           | 类型      | 必需(Y/N) | 默认值   | 描述                                       |
| ------------ | ------- | ------- | ----- | ---------------------------------------- |
| name         | String  | Y       | -     | 字段名                                      |
| enName       | String  | Y       | -     | 英文字段名                                    |
| type         | String  | Y       | -     | 字段类型(见字典FIELD_TYPE)                      |
| extra        | String  | Y       | -     | 扩展信息:<br />- TEXT限制字段长度范围<br />- NUMBER限制字段长度且范围大小10,2(0.15~255.25)<br />- CHECK/RADIO/SELECT存储关联的字典<br />- DATA自定义格式化时间yyyy-MM-dd HH:mm:ss |
| defaultValue | String  | N       | -     | 默认值                                      |
| required     | Boolean | N       | false | 是否必选                                     |
| sort         | Number  | N       | 0     | 顺序                                       |

响应参数：见通用

请求示例：

```json
{
  "name": "昵称",
  "enName": "nickname",
  "type": "TEXT",
  "extra": "2~32",
  "defaultValue": "",
  "required": true,
  "sort": 1
}
```

响应示例：见通用

## 更新

> 更新用户模板

请求方式：`POST`

请求地址：`/user-template/update`

请求类型：`application/json`

请求参数：

| 名称           | 类型      | 必需(Y/N) | 默认值   | 描述                                       |
| ------------ | ------- | ------- | ----- | ---------------------------------------- |
| id           | Number  | Y       | -     | 唯一标识                                     |
| name         | String  | Y       | -     | 字段名                                      |
| enName       | String  | Y       | -     | 英文字段名                                    |
| type         | String  | Y       | -     | 字段类型(见字典FIELD_TYPE)                      |
| extra        | String  | Y       | -     | 扩展信息:<br />- TEXT限制字段长度范围<br />- NUMBER限制字段长度且范围大小10,2(0.15~255.25)<br />- CHECK/RADIO/SELECT存储关联的字典<br />- DATA自定义格式化时间yyyy-MM-dd HH:mm:ss |
| defaultValue | String  | N       | -     | 默认值                                      |
| required     | Boolean | N       | false | 是否必选                                     |
| sort         | Number  | N       | 0     | 顺序                                       |

响应参数：见通用

请求示例：

```json
{
  "id": 1,
  "name": "昵称",
  "enName": "nickname",
  "type": "TEXT",
  "extra": "2~32",
  "defaultValue": "",
  "required": true,
  "sort": 1
}
```

响应示例：见通用

## 删除

> 删除用户模板

请求方式：`DELETE`

请求地址：`/user-template/delete`

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
