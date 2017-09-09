package com.java110.merchant.rest;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.log.LoggerEngine;
import com.java110.common.util.Assert;
import com.java110.common.util.ProtocolUtil;
import com.java110.core.base.controller.BaseController;
import com.java110.entity.merchant.MerchantMember;
import com.java110.feign.merchant.IMerchantMemberService;
import com.java110.merchant.smo.IMerchantMemberServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商户成员操作
 * Created by wuxw on 2017/8/30.
 */
@RestController
public class MerchantMemberServiceRest extends BaseController implements IMerchantMemberService {

    @Autowired
    IMerchantMemberServiceSMO iMerchantMemberServiceSMO;



    /**
     * 通过Merchant对象中数据查询用户信息
     * 如,用户ID，名称
     * @param data
     * @return
     */
    @RequestMapping("/merchantMemberService/queryMerchantInfo")
    public String queryMerchantMember(@RequestParam("data") String data){
        LoggerEngine.debug("queryMerchantInfo入参：" + data);


        String resultMerchantInfo = null;

        JSONObject reqMerchantJSON = null;
        try {
            reqMerchantJSON = this.simpleValidateJSON(data);
            MerchantMember oldMerchantMember = new MerchantMember();
            oldMerchantMember.setMerchantId(reqMerchantJSON.getString("merchantId"));
            resultMerchantInfo = iMerchantMemberServiceSMO.queryMerchantMember(oldMerchantMember);

        } catch (Exception e) {
            LoggerEngine.error("服务处理出现异常：", e);
            resultMerchantInfo = ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"服务处理出现异常"+e,null);
        } finally {
            LoggerEngine.debug("商户成员操作出参：" + resultMerchantInfo);
            return resultMerchantInfo;
        }
    }


    /**
     * 商户成员信息受理
     * 协议：
     * {
     *     'boMerchantMember':[{}]
     * }
     * @param data
     * @return
     */
    @RequestMapping("/merchantMemberService/soMerchantMemberService")
    public String soMerchantMemberService(@RequestParam("data") String data){

        LoggerEngine.debug("soMerchantMemberService入参：" + data);

        String resultMerchantInfo = null;

        JSONObject reqMerchantJSON = null;
        try {
            reqMerchantJSON = this.simpleValidateJSON(data);
            //1.0规则校验，报文是否合法


            //2.0 受理商户成员信息
            resultMerchantInfo = iMerchantMemberServiceSMO.soMerchantMemberService(reqMerchantJSON);


        } catch (Exception e) {
            LoggerEngine.error("服务处理出现异常：", e);
            resultMerchantInfo = ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"服务处理出现异常"+e,null);
        } finally {
            LoggerEngine.debug("商户成员操作商户成员出参：" + resultMerchantInfo);
            return resultMerchantInfo;
        }
    }

    /**
     * 这个接口专门用于订单服务受理用，入参为 JSONObject
     *
     * 支持 多个 商户成员信息 受理
     *
     * 请求协议：
     *
     * {
     "data":
     {
     "actionTypeCd": "C1",
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
    @RequestMapping("/merchantMemberService/soMerchantMemberServiceForOrderService")
    public String soMerchantMemberServiceForOrderService(@RequestParam("data") String data) {
        LoggerEngine.debug("soMerchantMemberService入参：" + data);

        String resultMerchantInfo = null;

        JSONObject reqMerchantJSON = null;
        try {
            reqMerchantJSON = this.simpleValidateJSON(data);
            //1.0规则校验，报文是否合法


            //2.0 受理商户成员信息
            resultMerchantInfo = iMerchantMemberServiceSMO.soMerchantMemberServiceForOrderService(reqMerchantJSON);


        } catch (Exception e) {
            LoggerEngine.error("服务处理出现异常：", e);
            resultMerchantInfo = ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"服务处理出现异常"+e,null);
        } finally {
            LoggerEngine.debug("商户成员操作商户成员出参：" + resultMerchantInfo);
            return resultMerchantInfo;
        }
    }


    /**
     * 商户成员信息处理
     * 协议：
     *{
     *     boMerchant:[{},{}]
     * }
     * @param data
     * @return
     * @throws Exception
     */
    @RequestMapping("/merchantMemberService/soBoMerchantMember")
    public String soBoMerchantMember(@RequestParam("data") String data ) {
        LoggerEngine.debug("soBoMerchantMember入参：" + data);

        String resultMerchantInfo = null;

        JSONObject reqMerchantJSON = null;
        try {
            reqMerchantJSON = this.simpleValidateJSON(data);
            resultMerchantInfo = iMerchantMemberServiceSMO.soBoMerchantMember(data);
        }catch (Exception e){
            LoggerEngine.error("服务处理出现异常：", e);
            resultMerchantInfo = ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"服务处理出现异常:"+e,null);
        } finally {
            LoggerEngine.debug("商户成员操作商户成员出参：" + resultMerchantInfo);
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
    @RequestMapping("/merchantMemberService/soDeleteMerchantMemberService")
    public String soDeleteMerchantMemberService(@RequestParam("data") String data){

        LoggerEngine.debug("作废订单入参："+data);

        String resultMerchantInfo = null;

        JSONObject reqParam = null;

        try{
            reqParam = this.simpleValidateJSON(data);

            Assert.isNull(reqParam,"data","传入报文错误，没有包含data节点"+reqParam);

            //resultMerchantInfo = iMerchantMemberServiceSMO.soDeleteMerchantMember(reqParam.getJSONArray("data"));

        }catch (Exception e){
            LoggerEngine.error("服务处理出现异常：", e);
            resultMerchantInfo = ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"服务处理出现异常"+e+data,null);
        }finally {
            LoggerEngine.debug("作废订单出参：" + resultMerchantInfo);
            return resultMerchantInfo;
        }
    }

    /**
     * 根据ol_id 查询需要作废的数据 这里 ol_id 就是 versionId
     * @param data {'ol_id':'123456789'}
     * @return
     */
    @RequestMapping("/merchantMemberService/queryNeedDeleteData")
    public String queryNeedDeleteData(@RequestParam("data") String data){
        LoggerEngine.debug("-----------------[MerchantMemberServiceRest.queryNeedDeleteData]-------------------作废订单查询入参："+data);

        String resultMerchantInfo = null;

        JSONObject reqParam = null;

        try{
            reqParam = this.simpleValidateJSON(data);

            resultMerchantInfo = iMerchantMemberServiceSMO.queryNeedDeleteData(reqParam);

        }catch (Exception e){
            LoggerEngine.error("服务处理出现异常：", e);
            resultMerchantInfo = ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"服务处理出现异常"+e+data,null);
        }finally {
            LoggerEngine.debug("作废订单出参：" + resultMerchantInfo);
            return resultMerchantInfo;
        }

    }

}
