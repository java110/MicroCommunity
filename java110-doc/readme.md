## java110 api 文档使用说明

### @Java110ApiDoc 的使用

样例

```
@Java110ApiDoc(
        title = "HC小区管理系统api接口文档",
        description = "HC小区管理系统api接口文档",
        company="Java110工作室",
        version = "v1.4"
)
```
title 表示 接口文档题目

description 表示 接口文档说明

company 文档公司

version 文档版本

该注解用在入口 类 比如 Hc api 的restApi.java 中

### @Java110RequestMappingsDoc 和 @Java110RequestMappingDoc 的使用

```
@Java110RequestMappingsDoc(
        mappingsDocs = {
                @Java110RequestMappingDoc(name="用户中心",resource = "user",url="http://service-user/userDoc",seq = 1),
                @Java110RequestMappingDoc(name="账户中心",resource = "acct",url="http://service-acct/acctDoc",seq = 2),
                @Java110RequestMappingDoc(name="通用中心",resource = "common",url="http://service-common/commonDoc",seq = 3),
                @Java110RequestMappingDoc(name="小区中心",resource = "community",url="http://service-community/communityDoc",seq = 4),
                @Java110RequestMappingDoc(name="开发中心",resource = "dev",url="http://service-dev/devDoc",seq = 5),
                @Java110RequestMappingDoc(name="费用中心",resource = "fee",url="http://service-fee/feeDoc",seq = 6),
                @Java110RequestMappingDoc(name="定时任务",resource = "job",url="http://service-job/jobDoc",seq = 7),
                @Java110RequestMappingDoc(name="oa",resource = "oa",url="http://service-oa/oaDoc",seq = 8),
                @Java110RequestMappingDoc(name="订单中心",resource = "order",url="http://service-order/orderDoc",seq = 9),
                @Java110RequestMappingDoc(name="报表中心",resource = "report",url="http://service-report/reportDoc",seq = 10),
                @Java110RequestMappingDoc(name="商户中心",resource = "store",url="http://service-store/storeDoc",seq = 11),
        }
)
```

资源目录映射 注解

name 为 名称 文档中的菜单

resource 为资源目录

url 为微服务地址

seq 文档中的排序

### Java110CmdDoc

接口注解

```
@Java110CmdDoc(title = "用户登录",
        description="登录功能 主要用于 员工 或者管理员登录使用",
        httpMethod="post",
        url="/app/login.pcUserLogin",
        resource = "user",
        author = "吴学文"
)
```

作用在cmd文件上注解 描述 接口信息

title 接口名称

description 接口描述

httpMethod 接口类型

url 接口请求地址

resource 资源路径，要写 @Java110RequestMappingDoc 下resource

author 作者信息

### @Java110ParamsDoc 
请求参数注解

```
@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "username", length = 30, remark = "用户名，物业系统分配"),
        @Java110ParamDoc(name = "passwd", length = 30, remark = "密码，物业系统分配"),
})
```
描述请求参数信息

parentNodeName 上级节点名称

name 当前节点名称

type 类型 String  int  Object Array 

length 类型为 string时 的长度 

defaultValue 默认值;

remark 说明;

### @Java110ResponseDoc

返回参数注解
```
@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
                @Java110ParamDoc(name = "data", type = "Object", remark = "有效数据"),
                @Java110ParamDoc(parentNodeName = "data",name = "userId", type = "String", remark = "用户ID"),
                @Java110ParamDoc(parentNodeName = "data",name = "token", type = "String", remark = "临时票据"),
        }
)

```

描述返回参数信息

parentNodeName 上级节点名称

name 当前节点名称

type 类型 String  int  Object Array

length 类型为 string时 的长度

defaultValue 默认值;

remark 说明;

### @Java110ExampleDoc

举例节点

```
@Java110ExampleDoc(
        reqBody="{'username':'wuxw','passwd':'admin'}",
        resBody="{'code':0,'msg':'成功','data':{'userId':'123123','token':'123213'}}"
)
```

reqBody 请求报文

resBody 返回报文


### demo

/**
 * 用户登录 功能
 * 请求地址为/app/login.pcUserLogin
 */

@Java110CmdDoc(title = "用户登录",
        description = "登录功能 主要用于 员工 或者管理员登录使用",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/login.pcUserLogin",
        resource = "userDoc",
        author = "吴学文",
        serviceCode = "login.pcUserLogin"
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "username", length = 30, remark = "用户名，物业系统分配"),
        @Java110ParamDoc(name = "passwd", length = 30, remark = "密码，物业系统分配"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
                @Java110ParamDoc(name = "data", type = "Object", remark = "有效数据"),
                @Java110ParamDoc(parentNodeName = "data",name = "userId", type = "String", remark = "用户ID"),
                @Java110ParamDoc(parentNodeName = "data",name = "token", type = "String", remark = "临时票据"),
        }
)

@Java110ExampleDoc(
        reqBody="{'username':'wuxw','passwd':'admin'}",
        resBody="{'code':0,'msg':'成功','data':{'userId':'123123','token':'123213'}}"
)

