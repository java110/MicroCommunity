package com.java110.order.rest;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.log.LoggerEngine;
import com.java110.common.util.ProtocolUtil;
import com.java110.core.base.controller.BaseController;
import com.java110.entity.order.OrderList;
import com.java110.order.smo.IOrderServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单服务 提供类
 *
 * 主要提供订单相关服务
 * Created by wuxw on 2017/4/11.
 */
@RestController
public class OrderServiceRest extends BaseController {


    @Autowired
    IOrderServiceSMO iOrderServiceSMO;


    /**
     * 查询订单信息
     * 接口协议：
     * 请求报文
     * {'olId':'71234567','extSystemId':'','custId':'','channelId':''}
     *
     * 可以根据 购物车ID 或者 外部系统ID 或者 custId 或者 channelId
     *
     * 返回报文:
     * {
     "RESULT_CODE": "0000",
     "RESULT_MSG": "成功",
     "RESULT_INFO": {
     "orderLists": [
     {
     "channelId": "700212896",
     "remarks": "",
     "olId": "123456",
     "custId": "701008023904",
     "statusCd": "S",
     "reqTime": "20170411163709",
     "extSystemId": "310013698777",
     "olTypeCd": "15",
     "orderListAttrs": [
     {
     "olId": "123456",
     "attrCd": "712345",
     "value": "XXX",
     "name": "系统来源"
     }
     ],
     "busiOrders": [
     {
     "boId": "123456",
     "olId": "123456",
     "actionTypeCd": "C1",
     "status_cd": "0",
     "create_dt": "2017-04-16 22:58:03",
     "start_dt": "2017-04-16 22:58:03",
     "end_dt": "2017-04-16 22:58:03",
     "remark": "",
     "busiOrders": [
     {
     "boId": "123456",
     "attrCd": "712345",
     "value": "XXX",
     "name": "系统来源"
     }
     ]
     }
     ]
     }
     ]
     }
     }
     *
     * @param orderInfo
     * @return
     */
    @RequestMapping("/orderService/queryOrder")
    public String queryOrder(@RequestParam("orderInfo") String orderInfo) {

        LoggerEngine.debug("soOrderService入参：" + orderInfo);
        String resultUserInfo = null;

        JSONObject reqOrderJSON = null;

        try{

            reqOrderJSON = this.simpleValidateJSON(orderInfo);

            //校验 购物车ID 或者 外部系统ID 或者 custId 或者 channelId 中的一个是
            Assert.notNull(reqOrderJSON);

            if(!reqOrderJSON.containsKey("olId") && !reqOrderJSON.containsKey("extSystemId")
                    && !reqOrderJSON.containsKey("custId") && !reqOrderJSON.containsKey("channelId")){
                throw new IllegalArgumentException("此接口只支持用olId，extSystemId，custId，channelId 来查询订单，当前请求报文为："+orderInfo);
            }
            //转化为 对象
            OrderList orderListTmp = JSONObject.toJavaObject(reqOrderJSON, OrderList.class);

            resultUserInfo = iOrderServiceSMO.queryOrderInfo(orderListTmp);

        }catch (Exception e){
            LoggerEngine.error("查询失败，orderInfo = "+orderInfo,e);
            resultUserInfo = ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"查询失败，orderInfo = "+orderInfo+"失败原因："+e,null);
        }finally {
            return resultUserInfo;
        }
    }

    /**
     * 订单统一处理接口
     *
     * orderListInfo 中字段 asyn 如果为 A 表示 异步处理订单，其他表同步处理订单
     * 接口协议
     * {
     "orderList": {
     "orderListAttrs": [
     {
     "attrCd": "40010026",
     "name": "购物车流水号",
     "value": "31201704110009114961"
     }
     ],"busiOrder": [
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
     "busiObj": {
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
     "olTypeCd": "15",
     "asyn":"A"
     }
     }
     }
     * @param orderInfo
     * @return
     */
    @RequestMapping("/orderService/soOrderService")
    public String soOrderService(@RequestParam("orderInfo") String orderInfo){

        LoggerEngine.debug("soOrderService入参：" + orderInfo);
        String resultUserInfo = null;

        JSONObject reqOrderJSON = null;

        try{

            reqOrderJSON = this.simpleValidateJSON(orderInfo);

            if(reqOrderJSON == null || !reqOrderJSON.containsKey("orderList")){
                throw new IllegalArgumentException("请求参数为空 reqOrderJSON ："+reqOrderJSON);
            }

            resultUserInfo = iOrderServiceSMO.orderDispatch(reqOrderJSON.getJSONObject("orderList"));

        }catch (Exception e){
            LoggerEngine.error("订单受理出现异常：", e);
            resultUserInfo = ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"订单受理出现异常，"+e,null);
        }finally {
            return resultUserInfo;
        }


    }

    /**
     * 作废订单时，order_list busi_order 和bo_开头数据不影响，只是作废实例数据，如作废客户信息，只作废 cust 和cust_attr 表中的数据
     * 作废订单依然会创建order_list 和busi_order 数据
     * 作废指定动作的信息
     * 请求协议：
     * {
     *     "orderList":{
     *     "transactionId": "1000000200201704113137002690",
             "channelId": "700212896",
             "remarks": "",
             "custId": "701008023904",
             "statusCd": "S",
             "reqTime": "20170411163709",
             "extSystemId": "310013698777",
             "olTypeCd": "15",
     *        "oldOlId":"123456789",
     *        "asyn":"S"
     *     },
     *     "busiOrder":[{
     *         "actionTypeCd":"ALL"
     *     }]
     * }
     *
     * 说明
     * oldOlId 购物车ID，对应表order_list
     * asyn 处理方式，S 同步 A 异步
     *
     * 注意这里busiOrder 的{} 只能有一个，有多个，只能会处理第一个
     *
     * actionTypeCd All标识 作废所有订单订单  C2 作废客户信息，。。。
     *
     * transactionId 交易流水
     *
     * channelId 渠道ID，对应channel表，可以自定义一个
     *
     * remarks 备注，不能超过200个，如果超过200个会截取200个字
     *
     * custId 操作者ID，可能客户本身，很有很可能商家，或管理员
     *
     * statusCd S表示这个购物车有效，
     *
     * reqTime 请求时间
     *
     * extSystemId 外部系统关联ID
     *
     * olTypeCd 购物车类型，如 微信创建的购物车 1 APP创建的2 PC网站创建 3
     *
     * asyn 处理方式，S 同步 A 异步
     *
     * 作废指定订单项的信息
     * 请求协议：
     * {
     *     "orderList":{
     *         "transactionId": "1000000200201704113137002690",
                 "channelId": "700212896",
                 "remarks": "",
                 "custId": "701008023904",
                 "statusCd": "S",
                 "reqTime": "20170411163709",
                 "extSystemId": "310013698777",
                 "olTypeCd": "15",
                 "asyn":"A"
     *     },
     *     "busiOrder":[{
     *          "oldBoId":"123456789"
     *     },
     *     {
     *          "oldBoId":"123456799"
     *     }]
     * }
     * oldBoId 为作废单个 要作废的订单项 对应busi_order 的 boId
     *
     * transactionId 交易流水
     *
     * channelId 渠道ID，对应channel表，可以自定义一个
     *
     * remarks 备注，不能超过200个，如果超过200个会截取200个字
     *
     * custId 操作者ID，可能客户本身，很有很可能商家，或管理员
     *
     * statusCd S表示这个购物车有效，
     *
     * reqTime 请求时间
     *
     * extSystemId 外部系统关联ID
     *
     * olTypeCd 购物车类型，如 微信创建的购物车 1 APP创建的2 PC网站创建 3
     *
     * asyn 处理方式，S 同步 A 异步
     *
     *
     * 统一返回协议：
     * {
     "RESULT_CODE": "0000",
     "RESULT_MSG": "成功",
     "RESULT_INFO": {"oldOrder":"1234567","oldBoId":"7000123,718881991"}
     }
     * RESULT_CODE 0000 成功，1999 失败， 失败原因为 RESULT_MSG
     *
     * RESULT_INFO 成功时返回作废 的购物车 olId 和 boId
     * @param orderInfo
     * @return
     */
    @RequestMapping("/orderService/deleteOrderInfo")
    public String deleteOrderInfo(@RequestParam("orderInfo") String orderInfo){

        LoggerEngine.debug("deleteOrderInfo入参：" + orderInfo);
        String resultUserInfo = null;

        JSONObject reqOrderJSON = null;

        try{

            reqOrderJSON = this.simpleValidateJSON(orderInfo);

            if(reqOrderJSON == null || !reqOrderJSON.containsKey("orderList")){
                throw new IllegalArgumentException("请求参数为空 reqOrderJSON ："+reqOrderJSON);
            }

            resultUserInfo = iOrderServiceSMO.orderDispatch(reqOrderJSON.getJSONObject("orderList"));

        }catch (Exception e){
            LoggerEngine.error("订单受理出现异常：", e);
            resultUserInfo = ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"订单受理出现异常，"+e,null);
        }finally {
            return resultUserInfo;
        }

    }


    public IOrderServiceSMO getiOrderServiceSMO() {
        return iOrderServiceSMO;
    }

    public void setiOrderServiceSMO(IOrderServiceSMO iOrderServiceSMO) {
        this.iOrderServiceSMO = iOrderServiceSMO;
    }
}
