package com.java110.user.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.log.LoggerEngine;
import com.java110.common.util.Assert;
import com.java110.common.util.ProtocolUtil;
import com.java110.core.base.controller.BaseController;
import com.java110.entity.user.Cust;
import com.java110.feign.user.IUserService;
import com.java110.user.smo.IUserServiceSMO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户服务提供类
 * Created by wuxw on 2017/4/5.
 */
//@RestController
public class UserServiceRest extends BaseController implements IUserService {

    private final static Logger logger = LoggerFactory.getLogger(UserServiceRest.class);

    @Autowired
    IUserServiceSMO iUserServiceSMO;

    /**
     * 通过User对象中数据查询用户信息
     * 如,用户ID，名称
     * @param data
     * @return
     */
    @RequestMapping("/userService/queryUserInfo")
    public String queryUserInfo(@RequestParam("data") String data){
        LoggerEngine.debug("queryUserInfo入参：" + data);


        String resultUserInfo = null;

        JSONObject reqUserJSON = null;
        try {
            reqUserJSON = this.simpleValidateJSON(data);
            Cust oldCust = new Cust();
            oldCust.setCustId(reqUserJSON.getString("custId"));
            resultUserInfo = iUserServiceSMO.queryCust(oldCust);

        } catch (Exception e) {
            LoggerEngine.error("服务处理出现异常：", e);
            resultUserInfo = ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"服务处理出现异常"+e,null);
        } finally {
            LoggerEngine.debug("用户服务操作客户出参：" + resultUserInfo);
            return resultUserInfo;
        }
    }

    /**
     * 根据购物车信息查询
     *
     * 这里返回data信息
     * @param busiOrder
     * @return
     */
    @Override
    @RequestMapping("/userService/queryCustInfoByOlId")
    public String queryCustInfoByOlId(@RequestParam("busiOrder") String busiOrder) {


        LoggerEngine.debug("queryUserInfo入参：" + busiOrder);


        String resultUserInfo = null;

        try {
             this.simpleValidateJSON(busiOrder);
            resultUserInfo = iUserServiceSMO.queryCustInfoByOlId(busiOrder);

        } catch (Exception e) {
            LoggerEngine.error("服务处理出现异常：", e);
            resultUserInfo = ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"服务处理出现异常"+e,null);
        } finally {
            LoggerEngine.debug("用户服务操作客户出参：" + resultUserInfo);
            return resultUserInfo;
        }
    }

    /**
     * 根据购物车信息查询 需要作废的发起的报文
     *
     * 这里返回data信息
     * @param busiOrder
     * @return
     */
    @Override
    @RequestMapping("/userService/queryNeedDeleteCustInfoByOlId")
    public String queryNeedDeleteCustInfoByOlId(@RequestParam("busiOrder")  String busiOrder) {


        LoggerEngine.debug("queryUserInfo入参：" + busiOrder);


        String resultUserInfo = null;

        try {
            this.simpleValidateJSON(busiOrder);
            resultUserInfo = iUserServiceSMO.queryNeedDeleteCustInfoByOlId(busiOrder);

        } catch (Exception e) {
            LoggerEngine.error("服务处理出现异常：", e);
            resultUserInfo = ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"服务处理出现异常"+e,null);
        } finally {
            LoggerEngine.debug("用户服务操作客户出参：" + resultUserInfo);
            return resultUserInfo;
        }
    }


    /**
     * 用户服务信息受理
     * 协议：
     * {
     *     'boCust':[{}],
     *     'boCustAttr':[{}]
     * }
     * @param data
     * @return
     */
    @RequestMapping("/userService/soUserService")
    public String soUserService(@RequestParam("data") String data){

        LoggerEngine.debug("soUserService入参：" + data);

        String resultUserInfo = null;

        JSONObject reqUserJSON = null;
        try {
            reqUserJSON = this.simpleValidateJSON(data);
            //1.0规则校验，报文是否合法

            //2.0 受理客户信息
            resultUserInfo = iUserServiceSMO.soUserService(reqUserJSON);

        } catch (Exception e) {
            LoggerEngine.error("服务处理出现异常：", e);
            resultUserInfo = ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"服务处理出现异常"+e,null);
        } finally {
            LoggerEngine.debug("用户服务操作客户出参：" + resultUserInfo);
            return resultUserInfo;
        }
    }


    /**
     * 这个接口专门用于订单服务受理用，入参为 JSONObject
     *
     * 支持 多个 客户信息 受理
     *
     * 请求协议：
     *
     * {
     "data": [
     {
     "actionTypeCd": "C1",
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
     }
     ]
     }

     *
     * 返回协议：
     *
     * {
     'RESULT_CODE': '0000',
     'RESULT_MSG': '成功',
     'RESULT_INFO': {}
     }
     * @param data
     * @return
     */
    @Override
    @RequestMapping("/userService/soUserServiceForOrderService")
    public String soUserServiceForOrderService(@RequestParam("data") String data) {
        LoggerEngine.debug("soUserService入参：" + data);

        String resultUserInfo = null;

        JSONObject reqUserJSON = null;
        try {
            reqUserJSON = this.simpleValidateJSON(data);
            //1.0规则校验，报文是否合法


            //2.0 受理客户信息
            resultUserInfo = iUserServiceSMO.soUserServiceForOrderService(reqUserJSON);


        } catch (Exception e) {
            LoggerEngine.error("服务处理出现异常：", e);
            resultUserInfo = ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"服务处理出现异常"+e,null);
        } finally {
            LoggerEngine.debug("用户服务操作客户出参：" + resultUserInfo);
            return resultUserInfo;
        }
    }


    /**
     * 客户信息处理
     * 协议：
     *{
     *     boCust:[{},{}]
     * }
     * @param data
     * @return
     * @throws Exception
     */
    @RequestMapping("/userService/soBoCust")
    public String soBoCust(@RequestParam("data") String data ) {
        LoggerEngine.debug("soBoCust入参：" + data);

        String resultUserInfo = null;

        JSONObject reqUserJSON = null;
        try {
            reqUserJSON = this.simpleValidateJSON(data);
            resultUserInfo = iUserServiceSMO.soBoCust(data);
        }catch (Exception e){
            LoggerEngine.error("服务处理出现异常：", e);
            resultUserInfo = ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"服务处理出现异常:"+e,null);
        } finally {
            LoggerEngine.debug("用户服务操作客户出参：" + resultUserInfo);
            return resultUserInfo;
        }
    }

    /**
     * 客户信息属性处理
     * 协议：
     *{
     *     boCustAttr:[{},{}]
     * }
     * @param data
     * @return
     * @throws Exception
     */
    @RequestMapping("/userService/soBoCustAttr")
    public String soBoCustAttr(@RequestParam("data") String data) {
        LoggerEngine.debug("soBoCustAttr入参：" + data);

        String resultUserInfo = null;

        JSONObject reqUserJSON = null;
        try {
            reqUserJSON = this.simpleValidateJSON(data);
            resultUserInfo = iUserServiceSMO.soBoCustAttr(data);
        }catch (Exception e){
            LoggerEngine.error("服务处理出现异常：", e);
            resultUserInfo = ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"服务处理出现异常"+e,null);
        } finally {
            LoggerEngine.debug("用户服务操作客户出参：" + resultUserInfo);
            return resultUserInfo;
        }
    }


    /**
     * 作废订单，根据boId作废订单
     *
     * 接口协议：
     *
     * { 'data': [

     {
     'olId': '123456',
     'boId': '222222',
     'actionTypeCd': 'C1'
     },
     {
     'olId': '123456',
     'boId': '222222',
     'actionTypeCd': 'C1'
     },
     {
     'olId': '123456',
     'boId': '222222',
     'actionTypeCd': 'C1'
     }
     ] }
     * @param data
     * @return
     */
    @RequestMapping("/userService/soDeleteCustService")
    public String soDeleteCustService(@RequestParam("data") String data){

        LoggerEngine.debug("作废订单入参："+data);

        String resultUserInfo = null;

        JSONObject reqParam = null;

        try{
            reqParam = this.simpleValidateJSON(data);

            Assert.isNotNull(reqParam,"data","传入报文错误，没有包含data节点"+reqParam);

            resultUserInfo = iUserServiceSMO.soDeleteCustInfo(reqParam.getJSONArray("data"));

        }catch (Exception e){
            LoggerEngine.error("服务处理出现异常：", e);
            resultUserInfo = ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"服务处理出现异常"+e+data,null);
        }finally {
            LoggerEngine.debug("作废订单出参：" + resultUserInfo);
            return resultUserInfo;
        }
    }


    public IUserServiceSMO getiUserServiceSMO() {
        return iUserServiceSMO;
    }

    public void setiUserServiceSMO(IUserServiceSMO iUserServiceSMO) {
        this.iUserServiceSMO = iUserServiceSMO;
    }
}
