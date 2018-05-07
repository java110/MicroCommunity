微小区平台

分支说明：

分支管理说明，test为最新代码未测试代码（主要为了防止本地代码遗失），master 为最新测试过代码（待产品化），product 为产品化代码

开发工具：

java1.8 + idea/eclipse + mysql 

技术架构：

Java + spring cloud + mybatis + mysql + activemq + redis



1.0 小区商家 1
美食
外卖
生鲜
超市
家政
其他
2.0 小区物业 2
小区公告
物业缴费（先不实现，只是用于通知作用，如电费，水费，气费）
房屋登记（先不实现）
停车位登记（先不实现）
意见保修
3.0 小区论坛 5
4.0 小区二手交易服务（后期实现）
5.0 小区拼车服务（附近小区显示5公里之内） 4
6.0 小区运动排名 6

目前提供服务：

1、上下班顺风车服务，用户发布服务，车主联系提供服务。

2、超市，便利店，饭馆外卖，家政商家入驻，提供送货上门服务，主要优势为，商家可以不需要花昂贵的钱租店铺，只需在家提供服务，为小区内无工作者提供就业机会。

3、为物业管理者提供免费的小区公告发布服务，方便及时查看公告，如 停电，停水 公告。

4、免费意见报修服务，用户可以向客户反馈自己的意见，和需要维修的东西，以至于物业管理者及时处理

5、小区论坛，方便小区内交流（正在规划中，欢迎合作）。

订单调度流程：

这里以wechat 服务 order 服务 user服务 为例说明

![image](https://github.com/java110/MicroCommunity/blob/master/OrderService/orderDispatch.png)


1>、受理过程 调用方发起订单受理，订单服务根据订单中每个（busiOrder节点下的actionTypeCd）业务动作，将订单信息预处理后转发相应模块受理数据。

2>、撤单过程 通用方发起撤单，订单服务根据撤单的boId 从数据库中查询信息，分别调相应服务撤单处理。

3>、事物补偿 如果以上有失败的情况下，作废当前订单信息，订单服务发起消息广播（activemq 消息广播方式），让各模块监听广播监听相应数据，恢复以前数据。

重要说明：

订单调度 外围系统 如用户服务系统，商户服务系统，支付服务系统，必须要实现以下接口：

1、受理接口

2、作废订单接口

请求协议为：

{ 
    'data':
[
    
    {
        'olId':'123456',
        'newBoId': '123456',
        'boId': '222222',
        'actionTypeCd': 'C1'
    },
    {
    'olId':'123456',
        'newBoId': '123456',
        'boId': '222222',
        'actionTypeCd': 'C1'
    },
    {
    'olId':'123456',
        'newBoId': '123456',
        'boId': '222222',
        'actionTypeCd': 'C1'
    }
]
}


3、补偿事物（目前是activemq）

请求报文 这里data下为所有要作废的订单boId 

{ 
    'data':
[
    
    {
        'boId': '222222',
        'actionTypeCd': 'C1'
    },
    {
        'boId': '222222',
        'actionTypeCd': 'M1'
    },
    {
        'boId': '222222',
        'actionTypeCd': 'C1'
    }
]
}

统一返回报文模板为：

{


 "RESULT_CODE": "0000",
 
 "RESULT_MSG": "成功",
 
 "RESULT_INFO":{}
}

RESULT_CODE 结果编码

0000 表示成功

1999 表示失败

RESULT_MSG 描述信息

RESULT_INFO 需要附带信息时，可以添加在这里

加入钉钉java110 工作群随时了解项目进度，和java110开发者零距离沟通

![image](https://github.com/java110/MicroCommunity/blob/test/dingding_java110.jpg)


