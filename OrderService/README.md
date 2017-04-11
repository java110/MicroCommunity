OrderService模块

1.0 订单管理

1.1.1 订单查询

1.1.2 订单提交

请求报文格式为：
{
    "orderList": {
        "orderListAttrs": [
            {
                "attrCd": "40010026",
                "name": "购物车流水号",
                "value": "31201704110009114961"
            }
        ],
        "busiOrder": [
            {
                "data": {
                    "boCust": [
                        {
                            "custId": "-1",
                            "name": "S",
                            "email": "-52",
                            "cellphone": "17797173942",
                            "realName": "wuxw",
                            "sex": "1",
                            "password": "123456",
                            "lanId": "863010",
                            "custAdress": "青海省西宁市城中区格兰小镇",
                            "custType": "1",
                            "openId": "",
                            "state": "ADD"
                        },
                        {
                            "custId": "123",
                            "name": "S",
                            "email": "-52",
                            "cellphone": "17797173942",
                            "realName": "wuxw",
                            "sex": "1",
                            "password": "123456",
                            "lanId": "863010",
                            "custAdress": "青海省西宁市城中区格兰小镇",
                            "custType": "1",
                            "openId": "",
                            "state": "DEL"
                        }
                    ],
                    "boCustAttr": [
                        {
                            "custId": "123",
                            "prodId": "-1",
                            "attrCd": "123344",
                            "value": "1",
                            "state": "ADD"
                        },
                        {
                            "custId": "123",
                            "prodId": "-1",
                            "attrCd": "123345",
                            "value": "1",
                            "state": "DEL"
                        }
                    ]
                },
                "busiOrder": {
                    "name": "新建客户",
                    "actionTypeCd": "C1",
                    "actionClassCd": "1",
                    "status_cd": "S",
                    "start_dt": "2017-04-11",
                    "end_dt": "2017-04-12",
                    "remark": ""
                },
                "busiOrderAttrs": [
                    {
                        "attrCd": "40010026",
                        "name": "购物车流水号",
                        "value": "31201704110009114961"
                    }
                ]
            }
        ],
        "orderListInfo": {
            "transactionId": "1000000200201704113137002690",
            "channelId": "700212896",
            "remarks": "",
            "olId": "-1",
            "custId": "701008023904",
            "statusCd": "S",
            "reqTime": "20170411163709",
            "extSystemId": "310013698777",
            "olTypeCd": "15"
        }
    }
}

1.1.3 订单取消