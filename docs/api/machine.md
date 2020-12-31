# HC小区管理系统对外设备相关接口

## 说明

文档说明设备相关接口协议

## 业主获取对应设备

## 物业查询所有设备

## 远程开门

说明：物业系统web端，业主手机端或者物业手机版远程开门场景

请求方式： POST

请求地址：/api/machine/openDoor

请求参数：

| 名称 | 类型 | 是否必填 | 示例值 | 描述 |
| :----:| :----: | :----: | :----: | :----: |
| machineCode | String | 是 | 1368371 | 设备编号 |
| userRole | String | owner | 0 | 用户角色，owner 是业主，staff 为员工 |
| userId | String | 否 | 123123 | 当用户角色为业主时必填，员工时不填 |
| communityId | String | 是 | 2020122471920846 | 小区ID |

请求示例：
```json
{
	"machineCode": "1368371",
	"userRole": "owner",
	"userId":"123123",
	"communityId": "2020122471920846"
}
```

返回示例：
```json
{
    "code":0,
    "msg":"成功",
    "data":""
}
```

## 设备重启

说明：物业系统web端 重启设备场景

请求方式： POST

请求地址：/api/machine/restartMachine

请求参数：

| 名称 | 类型 | 是否必填 | 示例值 | 描述 |
| :----:| :----: | :----: | :----: | :----: |
| machineCode | String | 是 | 1368371 | 设备编号 |
| communityId | String | 是 | 2020122471920846 | 小区ID |

请求示例：
```json
{
	"machineCode": "1368371",
	"communityId": "2020122471920846"
}
```

返回示例：
```json
{
    "code":0,
    "msg":"成功",
    "data":""
}
```

## 人脸开门记录上报

说明：HC物联网系统将开门记录上报物业系统

请求方式： POST

请求地址：/api/machine/openDoorLog

请求参数：

| 名称 | 类型 | 是否必填 | 示例值 | 描述 |
| :----:| :----: | :----: | :----: | :----: |
| userId | String | 是 | 702020042194860037 | 用户ID |
| userName | String | 是 | 张三 | 用户名称 |
| machineCode | String | 是 | 101010 | 设备编码 |
| openTypeCd | String | 是 | 1000 | 开门方式 1000 人脸开门 2000 钥匙开门 |
| similar | String | 是 | 100 | 开门相似度 |
| photo | String | 是 | base64,xxx | 抓拍照片 |
| dateTime | String | 是 | 2020-12-27 00:00:00 | 开门时间 |
| extCommunityId | String | 是 | 702020042194860039 | 小区ID |
| recordTypeCd | String | 是 | 8888 | 记录类型，8888 开门记录 6666 访客留影 |


请求示例：
```json
{
    "userId": "702020042194860037",
    "userName": "张三",
    "machineCode": "101010",
    "openTypeCd": "1000",
    "similar": "100",
    "photo": "base64,xxx",
    "dateTime": "2020-12-27 00:00:00",
    "extCommunityId": "702020042194860039",
    "recordTypeCd": "8888"
}
```

返回示例：
```json
{
    "code":0,
    "msg":"成功",
    "data":""
}
```

## 物联网指令执行结果反馈（当和物联网系统异步交互时有效）

说明：当和物联网系统异步交互时 或者物联网系统采用mqtt 协议时，执行结果 需要异步方式上报，需要物联网系统配置地址

请求方式： POST

请求地址：/api/machine/cmdResult

请求参数：

| 名称 | 类型 | 是否必填 | 示例值 | 描述 |
| :----:| :----: | :----: | :----: | :----: |
| taskId | String | 是 | 702020042194860037 | 任务ID，第三方系统调用结果时传递 |
| code | Integer | 是 | 0 | 指令执行状态码 0 成功 其他失败 |
| msg | String | 是 | 成功 | 指令执行说明 |

请求示例：
```json
{
    "taskId": "702020042194860037",
    "code": 0,
    "msg": "成功"
}
```

返回示例：
```json
{
    "code":0,
    "msg":"成功",
    "data":""
}
```

