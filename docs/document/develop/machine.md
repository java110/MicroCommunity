### 变更历史
版本|变更内容|变更时间|变更人员
:-: | :-: | :-: | :-:
v0.01|初稿|2019-11-12|wuxw

### 简介

对接门禁系统，主要采用锐目公司提供的思路来提供解决方案，主要包括

1、硬件轮训请求HC小区系统，获取下一步操作指令（必须对接接口）

2、请求HC小区系统获取用户信息（在第一步下发获取指定用户信息时)

3、人脸识别数据上报（非必须接口）

4、硬件命令执行反馈（非必须接口）

### 流程图

![流程图](images/machineFlow.png)

## 1、轮训接口

###### 接口功能
> API服务做保存车辆管理时调用该接口

###### URL
> [http://api.demo.winqi.cn/api/machineTranslate.machineHeartbeart](http://api.demo.winqi.cn/api/machineTranslate.machineHeartbeart)

###### 支持格式
> JSON

###### HTTP请求方式
> POST

###### 协议接口

header 信息：

|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: |
|MachineCode|1|String|30|设备编码|门禁编码|

body 信息：

|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: |
|machineCode|1|String|30|设备编码|门禁编码|
|devGroup|1|String|30|分组|-|
|name|1|String|30|设备名称|-|
|authCode|1|String|30|授权码|-|
|ip|?|String|30|设备IP|-|
|mac|1|String|30|设备mac|-|
|remarks|?|String|200|设备备注|-|
|faceNum|?|int|-|当前人脸数|-|
|lastOnTime|?|int|-|最后请求时间|由中心服务提供|
|statCode|?|String|50|设备授权状态|-|
|deviceType|?|String|12|设备类型|-|
|versionCode|?|String|4|设备版本号|-|

###### 返回协议

|父元素名称|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: | :-: |
|-|code|1|String|30|状态编码|0成功，-1 失败|
|-|msg|1|String|200|描述|失败或成功时的描述|
|-|data|1|Array|-|数据节点|-|
|data|taskcmd|1|String|30|任务指令| 101： 增加/更新人脸， 102： 删除人脸， 103： 清空人脸库|
|data|taskid|1|String|30|任务id|一般为uuid|
|data|taskinfo|1|String|30|附带信息|101 时业主ID|


###### 举例
> 地址：[http://api.demo.winqi.cn/api/machineTranslate.machineHeartbeart?app_id=992019111002270001&communityId=7020181217000001&transaction_id=-1&req_time=20181113225612&user_id=-1](http://api.demo.winqi.cn/api/machineTranslate.machineHeartbeart?app_id=992019111002270001&communityId=7020181217000001&transaction_id=-1&req_time=20181113225612&user_id=-1)

``` javascript
请求头信息：
Content-Type:application/json
machineCode:AC_20191110001

请求报文：
{
"machineCode":"AC_20191110001",
"devGroup":"default",
"name":"dev1",
"authCode":"ab2324f12ca2312b213133bfac",
"ip":"192.168.100.33",
"mac":"00:00:00:00","remarks":"test",
"faceNum":0,
"lastOnTime":15328329,
"statCode":1,
"deviceType":1,
"versionCode":114
}

返回报文：
 {"code":0,"data":[{"taskinfo":"772019092507000013","taskcmd":101,"taskId":"74ec26a4c3a94c579050c0651c7f6929"}],"message":"success"}

```

## 2、查询用户信息

###### 接口功能
> 门禁查询用户信息，其中包含照片信息

###### URL
> [http://api.demo.winqi.cn/api/machineTranslate.machineQueryUserInfo](http://api.demo.winqi.cn/api/machineTranslate.machineQueryUserInfo)

###### 支持格式
> JSON

###### HTTP请求方式
> POST

###### 协议接口

header 信息：

|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: |
|MachineCode|1|String|30|设备编码|门禁编码|

body信息：

|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: |
|faceid|1|String|30|用户ID|业主ID|


###### 返回协议

|父元素名称|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: | :-: |
|-|code|1|String|30|状态编码|0成功，-1 失败|
|-|msg|1|String|200|描述|失败或成功时的描述|
|-|data|1|Object|-|数据节点|-|
|data|userid|1|String|30|用户ID| 业主ID |
|data|groupid|1|String|30|分组ID|返回小区ID|
|data|group|1|String|30|分组|返回小区名称|
|data|name|1|String|30|用户名称|业主名称|
|data|faceBase64|1|String|-|用户base64照片|业主base64照片|
|data|idcNumber|1|String|30|用户身份证|-|
|data|startTime|1|int|-|开始时间|-|
|data|endTime|1|int|-|结束时间|-|
|data|remarks|1|String|200|备注|-|
|data|reserved|1|String|200|备注|-|


###### 举例
> 地址：[http://api.demo.winqi.cn/api/machineTranslate.machineQueryUserInfo?app_id=992019111002270001&communityId=7020181217000001&transaction_id=-1&req_time=20181113225612&user_id=-1](http://api.demo.winqi.cn/api/machineTranslate.machineQueryUserInfo?app_id=992019111002270001&communityId=7020181217000001&transaction_id=-1&req_time=20181113225612&user_id=-1)

``` javascript
请求头信息：
Content-Type:application/json
MachineCode:AC_20191110002

请求报文：
{
"faceid":"772019092507000013"
}

返回报文：
 {
     "code": 0,
     "data": {
         "reserved": "772019092507000013",
         "groupid": "7020181217000001",
         "name": "吴学文",
         "startTime": 1569373402000,
         "faceBase64": "照片base64 太多删除了",
         "endTime": 32503651200000,
         "idNumber": "772019092507000013",
         "userid": "772019092507000013",
         "remarks": "HC小区管理系统",
         "group": "万博家博园（城西区）"
     },
     "message": "success"
 }
```

## 3 设备执行命令上报

###### 接口功能
> 设备执行命令上报

###### URL
> [http://api.demo.winqi.cn/api/machineTranslate.machineCmdResult](http://api.demo.winqi.cn/api/machineTranslate.machineCmdResult)

###### 支持格式
> JSON

###### HTTP请求方式
> POST

###### 协议接口

header 信息：

|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: |
|MachineCode|1|String|30|设备编码|门禁编码|

body信息：

|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: |
|commandid|1|String|30|命令ID|-|
|taskid|1|String|30|任务执行后的当前值|-|
|msg|1|String|200|附带信息|-|
|errorcode|1|String|30|错误码（ 0： 无错误， -1:出错）|-|


###### 返回协议

|父元素名称|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: | :-: |
|-|code|1|String|30|状态编码|0成功，-1 失败|
|-|msg|1|String|200|描述|失败或成功时的描述|
|-|data|1|Object|-|数据节点|-|


###### 举例
> 地址：[http://api.demo.winqi.cn/api/machineTranslate.machineCmdResult?app_id=992019111002270001&communityId=7020181217000001&transaction_id=-1&req_time=20181113225612&user_id=-1](http://api.demo.winqi.cn/api/machineTranslate.machineCmdResult?app_id=992019111002270001&communityId=7020181217000001&transaction_id=-1&req_time=20181113225612&user_id=-1)

``` javascript
请求头信息：
Content-Type:application/json
MachineCode:AC_20191110002

请求报文：
{
"commandid":"xxxxxxxxxx",
"taskid":"xxxxxxxxxx",
"taskinfo":"error",
"msg":"error",
"errorcode":-1
}

返回报文：
 {
     "code": 0,
     "data": {
         "$ref": "@"
     },
     "message": "success"
 }
```

## 3 刷脸记录上报

###### 接口功能
> 刷脸记录上报

###### URL
> [http://api.demo.winqi.cn/api/machineTranslate.machineUploadFaceLog](http://api.demo.winqi.cn/api/machineTranslate.machineUploadFaceLog)

###### 支持格式
> JSON

###### HTTP请求方式
> POST

###### 协议接口

header 信息：

|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: |
|MachineCode|1|String|30|设备编码|门禁编码|

body信息：

|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: |
|userId|1|String|30|命令ID|-|
|machineCode|1|String|30|设备编码|-|
|openTypeCd|1|String|12|开门方式|开门方式，1000 人脸开门 2000 钥匙开门|
|recordTypeCd|1|String|12|记录类型|记录类型，8888 开门记录 6666 访客留影|
|similar|1|String|200|相似度|-|
|screenId|1|String|30|屏幕ID|-|
|photo|1|String|30|base64照片|-|
|dateTime|1|String|30|日期|YYYY-MM-DD hh:mm:ss|


###### 返回协议

|父元素名称|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: | :-: |
|-|code|1|String|30|状态编码|0成功，-1 失败|
|-|msg|1|String|200|描述|失败或成功时的描述|
|-|data|1|Object|-|数据节点|-|


###### 举例
> 地址：[http://api.demo.winqi.cn/api/machineTranslate.machineUploadFaceLog?app_id=992019111002270001&communityId=7020181217000001&transaction_id=-1&req_time=20181113225612&user_id=-1](http://api.demo.winqi.cn/api/machineTranslate.machineUploadFaceLog?app_id=992019111002270001&communityId=7020181217000001&transaction_id=-1&req_time=20181113225612&user_id=-1)

``` javascript
请求头信息：
Content-Type:application/json
MachineCode:AC_20191110002

请求报文：
{
"userId":"772019121580420009",
"machineCode":"test001",
"openTypeCd":"1000",
"recordTypeCd":"8888",
"screenId":1,
"similar":0.77,
"photo":"data:image/jpeg;base64,/9j/4AAQSLf//Z234234",
"dateTime":"2017-11-30 16:37:00"
}

返回报文：
 {
     "code": 0,
     "message": "success"
 }
```