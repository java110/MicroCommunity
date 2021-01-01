# HC小区管理系统对外设备相关接口

## 说明

文档说明设备相关接口协议

HC小区管理系统源码获取 https://gitee.com/wuxw7/MicroCommunity

## 业主获取对应设备

说明：查询业主可以开门设备

请求方式： GET

请求地址：/api/owner.listOwnerMachines

请求参数：

| 名称 | 类型 | 是否必填 | 示例值 | 描述 |
| :----:| :----: | :----: | :----: | :----: |
| memberId | String | 是 | 772020123183100051 | 业主ID |
| communityId | String | 是 | 2020122471920846 | 小区ID |

请求示例：
```url

http://ip:port/api/owner.listOwnerMachines?memberId=772020123183100051&communityId=2020122471920846

```

返回参数：

| 名称 | 类型 | 是否必填 | 示例值 | 描述 |
| :----:| :----: | :----: | :----: | :----: |
| direction | String | 是 | 3306 | 进场 |
| directionName | String | 是 | 进场 | 中文描述 |
| heartbeatTime | String | 是 | 2020-12-31 18:06:40 | 设备最近心跳时间 |
| locationObjId | String | 是 | 2020122471920846 | 位置ID |
| locationObjName | String | 是 | iot_小区 位置3 | 位置说明 |
| machineCode | String | 是 | 1368371 | 设备编码 |
| machineId | String | 是 | 892020122872730977 | 设备ID |
| machineIp | String | 否 | 192.168.1.1 | 设备IP |
| machineMac | String | 否 | 11:11:11:11 | 设备mac |
| machineName | String | 是 | 厦门集美2 | 设备名称 |
| machineTypeCd | String | 是 | 9999 | 表示门禁 |
| machineTypeCdName | String | 是 | 门禁 | 设备类型说明 |

返回示例：
```json
{
	"machines": [{
		"direction": "3306",
		"directionName": "进场",
		"heartbeatTime": "2020-12-31 18:06:40",
		"locationObjId": "2020122471920846",
		"locationObjName": "iot_小区 位置3",
		"machineCode": "1368371",
		"machineId": "892020122872730977",
		"machineIp": "",
		"machineMac": "",
		"machineName": "厦门集美2",
		"machineTypeCd": "9999",
		"machineTypeCdName": "门禁"
	}],
	"page": 1,
	"records": 1,
	"rows": 1,
	"total": 0
}
```

## 物业查询所有设备

说明：查询业主可以开门设备

请求方式： GET

请求地址：/api/machine.listMachines

请求参数：

| 名称 | 类型 | 是否必填 | 示例值 | 描述 |
| :----:| :----: | :----: | :----: | :----: |
| page | Integer | 是 | 1 | 页数 |
| row | Integer | 是 | 10 | 每页显示数量 |
| communityId | String | 是 | 2020122471920846 | 小区ID |

请求示例：
```url

http://ip:port/api/machine.listMachines?communityId=2020122471920846&page=1&row=10

```

返回参数：

| 名称 | 类型 | 是否必填 | 示例值 | 描述 |
| :----:| :----: | :----: | :----: | :----: |
| direction | String | 是 | 3306 | 进场 |
| directionName | String | 是 | 进场 | 中文描述 |
| heartbeatTime | String | 是 | 2020-12-31 18:06:40 | 设备最近心跳时间 |
| locationObjId | String | 是 | 2020122471920846 | 位置ID |
| locationObjName | String | 是 | iot_小区 位置3 | 位置说明 |
| machineCode | String | 是 | 1368371 | 设备编码 |
| machineId | String | 是 | 892020122872730977 | 设备ID |
| machineIp | String | 否 | 192.168.1.1 | 设备IP |
| machineMac | String | 否 | 11:11:11:11 | 设备mac |
| machineName | String | 是 | 厦门集美2 | 设备名称 |
| machineTypeCd | String | 是 | 9999 | 表示门禁 |
| machineTypeCdName | String | 是 | 门禁 | 设备类型说明 |

返回示例：
```json
{
	"machines": [{
		"direction": "3306",
		"directionName": "进场",
		"heartbeatTime": "2020-12-31 18:06:40",
		"locationObjId": "2020122471920846",
		"locationObjName": "iot_小区 位置3",
		"machineCode": "1368371",
		"machineId": "892020122872730977",
		"machineIp": "",
		"machineMac": "",
		"machineName": "厦门集美2",
		"machineTypeCd": "9999",
		"machineTypeCdName": "门禁"
	}],
	"page": 1,
	"records": 1,
	"rows": 1,
	"total": 0
}
```

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

