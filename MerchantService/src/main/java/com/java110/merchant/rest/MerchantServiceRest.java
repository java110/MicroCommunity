package com.java110.merchant.rest;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.log.LoggerEngine;
import com.java110.common.util.Assert;
import com.java110.common.util.ProtocolUtil;
import com.java110.core.base.controller.BaseController;
import com.java110.entity.merchant.Merchant;
import com.java110.feign.merchant.IMerchantService;
import com.java110.merchant.smo.IMerchantServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户服务提供类
 * Created by wuxw on 2017/4/5.
 */
@RestController
public class MerchantServiceRest extends BaseController implements IMerchantService {

    @Autowired
    IMerchantServiceSMO iMerchantServiceSMO;

    /**
     * 通过Merchant对象中数据查询用户信息
     * 如,用户ID，名称
     * @param data
     * @return
     */
    @RequestMapping("/merchantService/queryMerchantInfo")
    public String queryMerchantInfo(@RequestParam("data") String data){
        LoggerEngine.debug("queryMerchantInfo入参：" + data);


        String resultMerchantInfo = null;

        JSONObject reqMerchantJSON = null;
        try {
            reqMerchantJSON = this.simpleValidateJSON(data);
            Merchant oldMerchant = new Merchant();
            oldMerchant.setMerchantId(reqMerchantJSON.getString("merchantId"));
            resultMerchantInfo = iMerchantServiceSMO.queryMerchant(oldMerchant);

        } catch (Exception e) {
            LoggerEngine.error("服务处理出现异常：", e);
            resultMerchantInfo = ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"服务处理出现异常"+e,null);
        } finally {
            LoggerEngine.debug("用户服务操作客户出参：" + resultMerchantInfo);
            return resultMerchantInfo;
        }
    }


    /**
     * 用户服务信息受理
     * 协议：
     * {
     *     'boMerchant':[{}],
     *     'boMerchantAttr':[{}]
     * }
     * @param data
     * @return
     */
    @RequestMapping("/merchantService/soMerchantService")
    public String soMerchantService(@RequestParam("data") String data){

        LoggerEngine.debug("soMerchantService入参：" + data);

        String resultMerchantInfo = null;

        JSONObject reqMerchantJSON = null;
        try {
            reqMerchantJSON = this.simpleValidateJSON(data);
            //1.0规则校验，报文是否合法


            //2.0 受理客户信息
            resultMerchantInfo = iMerchantServiceSMO.soMerchantService(reqMerchantJSON);


        } catch (Exception e) {
            LoggerEngine.error("服务处理出现异常：", e);

            resultMerchantInfo = ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"服务处理出现异常"+e,null);
        } finally {
            LoggerEngine.debug("用户服务操作客户出参：" + resultMerchantInfo);
            return resultMerchantInfo;
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
     "data":
     {
     "boMerchant": [
     {
     "merchantId": "-1",
     "name": "S",
     "email": "-52",
     "cellphone": "17797173942",
     "realName": "wuxw",
     "sex": "1",
     "password": "123456",
     "lanId": "863010",
     "merchantAdress": "青海省西宁市城中区格兰小镇",
     "merchantType": "1",
     "openId": "",
     "state": "ADD"
     },
     {
     "merchantId": "123",
     "name": "S",
     "email": "-52",
     "cellphone": "17797173942",
     "realName": "wuxw",
     "sex": "1",
     "password": "123456",
     "lanId": "863010",
     "merchantAdress": "青海省西宁市城中区格兰小镇",
     "merchantType": "1",
     "openId": "",
     "state": "DEL"
     }
     ],
     "boMerchantAttr": [
     {
     "merchantId": "123",
     "prodId": "-1",
     "attrCd": "123344",
     "value": "1",
     "state": "ADD"
     },
     {
     "merchantId": "123",
     "prodId": "-1",
     "attrCd": "123345",
     "value": "1",
     "state": "DEL"
     }
     ]
     }

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
    @RequestMapping("/merchantService/soMerchantServiceForOrderService")
    public String soMerchantServiceForOrderService(@RequestParam("data") String data) {
        LoggerEngine.debug("soMerchantService入参：" + data);

        String resultMerchantInfo = null;

        JSONObject reqMerchantJSON = null;
        try {
            reqMerchantJSON = this.simpleValidateJSON(data);
            //1.0规则校验，报文是否合法


            //2.0 受理客户信息
            resultMerchantInfo = iMerchantServiceSMO.soMerchantServiceForOrderService(reqMerchantJSON);


        } catch (Exception e) {
            LoggerEngine.error("服务处理出现异常：", e);
            resultMerchantInfo = ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"服务处理出现异常"+e,null);
        } finally {
            LoggerEngine.debug("用户服务操作客户出参：" + resultMerchantInfo);
            return resultMerchantInfo;
        }
    }


    /**
     * 客户信息处理
     * 协议：
     *{
     *     boMerchant:[{},{}]
     * }
     * @param data
     * @return
     * @throws Exception
     */
    @RequestMapping("/merchantService/soBoMerchant")
    public String soBoMerchant(@RequestParam("data") String data ) {
        LoggerEngine.debug("soBoMerchant入参：" + data);

        String resultMerchantInfo = null;

        JSONObject reqMerchantJSON = null;
        try {
            reqMerchantJSON = this.simpleValidateJSON(data);
            resultMerchantInfo = iMerchantServiceSMO.soBoMerchant(data);
        }catch (Exception e){
            LoggerEngine.error("服务处理出现异常：", e);
            resultMerchantInfo = ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"服务处理出现异常:"+e,null);
        } finally {
            LoggerEngine.debug("用户服务操作客户出参：" + resultMerchantInfo);
            return resultMerchantInfo;
        }
    }

    /**
     * 客户信息属性处理
     * 协议：
     *{
     *     boMerchantAttr:[{},{}]
     * }
     * @param data
     * @return
     * @throws Exception
     */
    @RequestMapping("/merchantService/soBoMerchantAttr")
    public String soBoMerchantAttr(@RequestParam("data") String data) {
        LoggerEngine.debug("soBoMerchantAttr入参：" + data);

        String resultMerchantInfo = null;

        JSONObject reqMerchantJSON = null;
        try {
            reqMerchantJSON = this.simpleValidateJSON(data);
            resultMerchantInfo = iMerchantServiceSMO.soBoMerchantAttr(data);
        }catch (Exception e){
            LoggerEngine.error("服务处理出现异常：", e);
            resultMerchantInfo = ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"服务处理出现异常"+e,null);
        } finally {
            LoggerEngine.debug("用户服务操作客户出参：" + resultMerchantInfo);
            return resultMerchantInfo;
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
    @RequestMapping("/merchantService/soDeleteMerchantService")
    public String soDeleteMerchantService(@RequestParam("data") String data){

        LoggerEngine.debug("作废订单入参："+data);

        String resultMerchantInfo = null;

        JSONObject reqParam = null;

        try{
            reqParam = this.simpleValidateJSON(data);

            Assert.isNotNull(reqParam,"data","传入报文错误，没有包含data节点"+reqParam);

            resultMerchantInfo = iMerchantServiceSMO.soDeleteMerchantInfo(reqParam.getJSONArray("data"));

        }catch (Exception e){
            LoggerEngine.error("服务处理出现异常：", e);
            resultMerchantInfo = ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"服务处理出现异常"+e+data,null);
        }finally {
            LoggerEngine.debug("作废订单出参：" + resultMerchantInfo);
            return resultMerchantInfo;
        }
    }


    @RequestMapping("/merchantService/queryMerchantInfoByOlId")
    public String queryMerchantInfoByOlId(@RequestParam("busiOrder") String busiOrder){


        LoggerEngine.debug("queryMerchantInfoByOlId入参：" + busiOrder);


        String resultMerchantInfo = null;

        try {
            this.simpleValidateJSON(busiOrder);
            resultMerchantInfo = iMerchantServiceSMO.queryMerchantInfoByOlId(busiOrder);

        } catch (Exception e) {
            LoggerEngine.error("服务处理出现异常：", e);
            resultMerchantInfo = ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"服务处理出现异常"+e,null);
        } finally {
            LoggerEngine.debug("用户服务操作客户出参：" + resultMerchantInfo);
            return resultMerchantInfo;
        }
    }

    @RequestMapping("/merchantService/queryNeedDeleteMerchantInfoByOlId")
    public String queryNeedDeleteMerchantInfoByOlId(@RequestParam("busiOrder") String busiOrder){


        LoggerEngine.debug("queryMerchantInfoByOlId入参：" + busiOrder);


        String resultMerchantInfo = null;

        try {
            this.simpleValidateJSON(busiOrder);
            resultMerchantInfo = iMerchantServiceSMO.queryNeedDeleteMerchantInfoByOlId(busiOrder);

        } catch (Exception e) {
            LoggerEngine.error("服务处理出现异常：", e);
            resultMerchantInfo = ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"服务处理出现异常"+e,null);
        } finally {
            LoggerEngine.debug("用户服务操作客户出参：" + resultMerchantInfo);
            return resultMerchantInfo;
        }
    }



    public IMerchantServiceSMO getiMerchantServiceSMO() {
        return iMerchantServiceSMO;
    }

    public void setiMerchantServiceSMO(IMerchantServiceSMO iMerchantServiceSMO) {
        this.iMerchantServiceSMO = iMerchantServiceSMO;
    }
}
