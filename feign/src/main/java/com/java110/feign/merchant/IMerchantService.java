package com.java110.feign.merchant;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * 用户服务接口
 * 用户查询，修改，删除 实现
 * Created by wuxw on 2017/4/5.
 */
@FeignClient(name = "merchant-service", fallback = MerchantServiceFallback.class)
public interface IMerchantService {

    /**
     * 通过Merchant对象中数据查询用户信息
     * 如,用户ID，名称
     * @param data
     * @return
     */
    @RequestMapping("/merchantService/queryMerchantInfo")
    public String queryMerchantInfo(@RequestParam("data") String data);


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
    public String soMerchantService(@RequestParam("data") String data);


    /**
     * 这个接口专门用于订单服务受理用，入参为 JSONArray
     *
     * 支持 多个 客户信息 受理
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
    @RequestMapping("/merchantService/soMerchantServiceForOrderService")
    public String soMerchantServiceForOrderService(@RequestParam("data") String data);


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
    public String soBoMerchant(@RequestParam("data") String data) ;

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
    public String soBoMerchantAttr(@RequestParam("data") String data) ;


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
    public String soDeleteMerchantService(@RequestParam("data") String data);

}
