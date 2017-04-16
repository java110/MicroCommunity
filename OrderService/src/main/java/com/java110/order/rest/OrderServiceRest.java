package com.java110.order.rest;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.log.LoggerEngine;
import com.java110.common.util.ProtocolUtil;
import com.java110.core.base.controller.BaseController;
import com.java110.order.smo.IOrderServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
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
     * @param orderInfo
     * @return
     */
    public String queryOrder(@RequestParam("orderInfo") String orderInfo) {

        try {

        }catch (Exception e){

        }finally {
            return "";
        }
    }

    /**
     * 订单统一处理接口
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
     "olTypeCd": "15"
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


    public IOrderServiceSMO getiOrderServiceSMO() {
        return iOrderServiceSMO;
    }

    public void setiOrderServiceSMO(IOrderServiceSMO iOrderServiceSMO) {
        this.iOrderServiceSMO = iOrderServiceSMO;
    }
}
