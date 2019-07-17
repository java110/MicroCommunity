### 变更历史
版本|变更内容|变更时间|变更人员
:-: | :-: | :-: | :-:
v0.01|初稿|2018-07-24|wuxw

### 本页内容

1. [配置相关表](#配置相关表)
2. [外部应用表（c_app）配置](#外部应用表c_app配置)
3. [服务提供表（c_service）配置](#服务提供表c_service配置)
4. [路由表配置（c_route）](#路由表配置c_route)
5. [查询类服务实现表配置（c_service_sql）](#查询类服务实现表配置c_service_sql)

### 配置相关表

```
-- 外部应用表
select a.* from c_app a ;
-- 服务提供表
select a.* from c_service a;
-- 外部应用 和 服务关联表
select a.* from c_route a;

-- 查询类 服务实现表
select a.* from c_service_sql a;
```

### 外部应用表（c_app）配置

表字段|类型|约束|长度|可空|配置说明
:-:|:-:|:-:|:-:|:-:|:-:
id|int|1|-|否|自动增长
app_id|String|1|10|否|开始于8000418001，新增加1
name|String|1|50|否|外部应用名称，如 微信小程序
security_code|String|?|64|是|加密掩码，如123456
while_list_ip|String|?|200|是|白名单ip 多个之间用;隔开
black_list_ip|String|?|200|是|黑名单ip 多个之间用;隔开
remark|String|?|200|是|备注信息
status_cd|String|1|2|否|数据状态，0在用，1失效

security_code 不为空则sign签名校验，为空则不做校验

while_list_ip 不为空则只有配置了ip才能访问，为空不做校验

black_list_ip 不为空则配置了ip不能访问，为空不做校验

相关insert语句，请参考：

```
insert into c_app(app_id,`name`,security_code,remark,status_cd)
values('8000418001','内部测试应用','WEBURFPKIFJUHNCJUEIKMKJUJHULSMNCHDY89KMC','记得删除','0');
```


### 服务提供表（c_service）配置

表字段|类型|约束|长度|可空|配置说明
:-:|:-:|:-:|:-:|:-:|:-:
id|int|1|-|否|自动增长
service_id|String|1|12|否|服务编码ID，开始于1003180001，新增加1
service_code|String|1|50|否|自定义，命名方式查询类query.+目标系统+.+业务名称 保存类 save.+目标系统+.+业务名称 修改类 modify.+目标系统+.+业务名称 删除类 remove.+目标系统+.+业务名称 例如：query.user.userinfo save.user.adduserinfo
business_type_cd|String|1|4|否|业务项类型，参考c_business_type表
name|String|1|50|否|服务名称
seq|int|1|-|否|顺序 只有同步方式下根据seq从小到大调用接口
messageQueueName|String|?|50|是|消息队里名称，只有异步时有用
is_instance|String|1|2|否|是否Instance过程 Y 需要，N不需要
url|String|?|200|是|目标地址
method|String|?|50|是|方法 空 为http post LOCAL_SERVICE 为调用本地服务 其他为webservice方式调用
timeout|int|?|-|是|超时时间，单位为秒
retry_count|int|?|-|是|重试次数
provide_app_id|String|1|30|否|服务提供appId
status_cd|String|1|2|否|数据状态，0在用，1失效

messageQueueName 在异步的情况下使用，为落地方系统kafka topic 如 commentServiceTopic

```
/**
 * kafka侦听
 * Created by wuxw on 2018/4/15.
 */
public class CommentServiceKafka extends BaseController {

    @KafkaListener(topics = {"commentServiceTopic"})
    public void listen(ConsumerRecord<?, ?> record) {
    }
}
```
相关insert语句，请参考

```
insert into c_service(service_id,service_code,business_type_cd,`name`,seq,messageQueueName,url,provide_app_id,status_cd)
values('1003180001','query.order.orderInfo','Q','订单信息',1,'','http://center-service/businessApi/query','8000418001','0');
```

### 路由表配置（c_route）

表字段|类型|约束|长度|可空|配置说明
:-:|:-:|:-:|:-:|:-:|:-:
id|int|1|-|否|自动增长
app_id|String|1|10|否|关联c_app表
service_id|String|1|12|否|关联c_service表
order_type_cd|String|1|4|否|查看订单类型说明
invoke_limit_times|int|?|-|是|调用次数限制
invoke_model|String|1|1|否|调用方式S同步方式，A异步方式
status_cd|String|1|2|否|数据状态，0在用，1失效


invoke_model 为同步 S时，表c_service 表中的url字段不能为空；为A时 c_service 表中的messageQueueName字段不能为空

相关insert语句，请参考
```
insert into c_route(app_id,service_id,invoke_model,order_type_cd,status_cd) values(
'8000418001','1003180001','S','Q','0'
);
```

### 查询类服务实现表配置（c_service_sql）

表字段|类型|约束|长度|可空|配置说明
:-:|:-:|:-:|:-:|:-:|:-:
id|int|1|-|否|自动增长
service_code|String|1|50|否|关联c_service表
name|String|1|50|否|名称
params|String|1|500|否|查询参数
query_model|String|1|1|否|查询方式 1、sql,2、存储过程，3、执行java代码
sql|String|?|-|是|在查询方式为1 的情况下 执行sql
proc|String|?|200|是|在查询方式为2时执行，存储过程，不推荐
java_script|String|?|-|是|在查询方式为3时执行java脚本代码
template|String|?|-|是|在查询方式为1 时，输出模板
remark|String|?|200|是|描述
status_cd|String|1|2|否|数据状态，0在用，1失效

sql 配置方式 如下格式：

```
{
	"param1": "SELECT co.app_id appId,co.create_time createTime,co.ext_transaction_id extTransactionId,co.finish_time finishTime,co.order_type_cd orderTypeCd,co.o_id oId,co.remark remark,co.request_time requestTime,co.user_id userId,co.status_cd statusCd FROM c_orders co WHERE co.o_id = #oId# ",
	"param2": "SELECT cb.b_id bId, cb.business_type_cd businessTypeCd,cb.create_time createTime,cb.finish_time finishTime ,cb.o_id oId,cb.remark remark,cb.status_cd statusCd FROM c_business cb WHERE cb.o_id = #oId# "
}
```

template 配置格式如下：

```
{
	"PARAM": {
		"param1": "$.#order#Object",
		"param2": "$.#business#Array"
	},
	"TEMPLATE": {
		"response": {
			"code": "0000",
			"message": "成功"
		}
	}
}
```

template 的param1 和 sql 的param1  对应 ，模板是TEMPLATE，最后生成的格式为：

```
{
	"response": {
		"code": "0000",
		"message": "成功"
	},
	"order": {
		"appId": "",
		"createTime": "",
		"下面节点对应sql的查询出来字段": ""
	},
	"business": [{
		"bId": "",
		"businessTypeCd": "",
		"下面节点对应sql的查询出来字段": ""
	}]
}
```

请求报文格式为：

```
{
  "orders": {
    "transactionId": "100000000020180409224736000001",
    "requestTime": "20180409224736",
    "orderTypeCd":"订单类型,查询,受理",
    "dataFlowId":"20020180000001",
    "businessType":"Q"//B business 过程 I Instance过程
  },
  "business": {
    "bId":"12345678",
    "serviceCode": "querycustinfo",
    "serviceName": "查询客户",
    "remark": "备注",
    "datas": {
      "params": {
        //这个做查询时的参数
      }
    }
  }
}
```

相关insert 语句，请参考：

```
INSERT INTO c_service_sql(service_code,`name`,params,query_model,`sql`,proc,template,remark,status_cd)
VALUES('query.order.orderInfo','订单信息','oId','1','{
                                                 	"param1":"SELECT co.app_id appId,co.create_time createTime,co.ext_transaction_id extTransactionId,co.finish_time finishTime
                                                 ,co.order_type_cd orderTypeCd,co.o_id oId ,co.remark remark ,co.request_time requestTime ,co.user_id userId,co.status_cd statusCd
                                                  FROM c_orders co WHERE co.o_id = #oId#",
                                                  "param2":"SELECT cb.b_id bId, cb.business_type_cd businessTypeCd,cb.create_time createTime,cb.finish_time finishTime ,cb.o_id oId,
                                                 cb.remark remark,cb.status_cd statusCd FROM c_business cb WHERE cb.o_id = #oId#"
                                                 }','','{"PARAM":{
                                                            "param1": "$.#order#Object",
                                                            "param2": "$.#business#Array"
                                                            },"TEMPLATE":{
                                                         "response": {
                                                           "code": "0000",
                                                           "message": "成功"
                                                         }
                                                       }}','','0');
```

[>回到首页](home)
