package com.java110.feign.merchant;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.log.LoggerEngine;
import com.java110.common.util.Assert;
import com.java110.common.util.ProtocolUtil;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by wuxw on 2017/8/30.
 */

@FeignClient(name = "merchant-service", fallback = MerchantMemberServiceFallBack.class)
public interface IMerchantMemberService {

    /**
     * 通过Merchant对象中数据查询用户信息
     * 如,用户ID，名称
     * @param data
     * @return
     */
    @RequestMapping("/merchantMemberService/queryMerchantMember")
    public String queryMerchantMember(@RequestParam("data") String data);


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
    public String soMerchantMemberService(@RequestParam("data") String data);

    /**
     * 这个接口专门用于订单服务受理用，入参为 JSONObject
     *
     * 支持 多个 商户成员信息 受理
     *
     * 请求协议：
     *
     * {
     "data": [
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
    @RequestMapping("/merchantMemberService/soMerchantMemberServiceForOrderService")
    public String soMerchantMemberServiceForOrderService(@RequestParam("data") String data);

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
    public String soBoMerchantMember(@RequestParam("data") String data ) ;


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
    public String soDeleteMerchantMemberService(@RequestParam("data") String data);


    /**
     * 根据ol_id 查询需要作废的数据 这里 ol_id 就是 versionId
     * @param data {'ol_id':'123456789'}
     * @return
     */
    @RequestMapping("/merchantMemberService/queryNeedDeleteData")
    public String queryNeedDeleteData(@RequestParam("data") String data);

}
