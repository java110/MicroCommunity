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

|父元素名称|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: |
|machineCode|1|String|10|系统ID|由中心服务提供|
|devGroup|1|String|30|交互流水|appId+'00'+YYYYMMDD+10位序列|
|name|1|String|30|用户ID|已有用户ID|
|authCode|1|String|4|订单类型|查看订单类型说明|
|ip|1|String|14|请求时间|YYYYMMDDhhmmss|
|mac|1|String|200|备注|备注|
|remarks|?|String|64|签名|查看加密说明|
|faceNum|?|Array|-|订单属性|-|
|lastOnTime|1|String|12|规格编码|由中心服务提供|
|statCode|1|String|50|属性值|-|
|deviceType|1|Object|-|返回结果节点|-|
|versionCode|1|String|4|返回状态|查看状态说明|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，





###### 举例
> 地址：[http://api.demo.winqi.cn/api/machineTranslate.machineHeartbeart?app_id=992019111002270001&communityId=7020181217000001&transaction_id=-1&req_time=20181113225612&user_id=-1](http://api.demo.winqi.cn/api/machineTranslate.machineHeartbeart?app_id=992019111002270001&communityId=7020181217000001&transaction_id=-1&req_time=20181113225612&user_id=-1)

``` javascript
请求头信息：
Content-Type:application/json

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
 {"code":-1,"message":"该设备【AC_20191110001】未在该小区【7020181217000001】注册"}

```