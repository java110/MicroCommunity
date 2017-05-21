package com.java110.feign.product;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * 用户服务接口
 * 用户查询，修改，删除 实现
 * Created by wuxw on 2017/4/5.
 */
@FeignClient(name = "product-service", fallback = ProductServiceFallback.class)
public interface IProductService {

    /**
     * 通过Product对象中数据查询用户信息
     * 如,用户ID，名称
     * @param data
     * @return
     */
    @RequestMapping("/productService/queryProductInfo")
    public String queryProductInfo(@RequestParam("data") String data);


    /**
     * 用户服务信息受理
     * 协议：
     * {
     *     'boProduct':[{}],
     *     'boProductAttr':[{}]
     * }
     * @param data
     * @return
     */
    @RequestMapping("/productService/soProductService")
    public String soProductService(@RequestParam("data") String data);


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
     "boProduct": [
     {
     "productId": "-1",
     "name": "S",
     "email": "-52",
     "cellphone": "17797173942",
     "realName": "wuxw",
     "sex": "1",
     "password": "123456",
     "lanId": "863010",
     "productAdress": "青海省西宁市城中区格兰小镇",
     "productType": "1",
     "openId": "",
     "state": "ADD"
     },
     {
     "productId": "123",
     "name": "S",
     "email": "-52",
     "cellphone": "17797173942",
     "realName": "wuxw",
     "sex": "1",
     "password": "123456",
     "lanId": "863010",
     "productAdress": "青海省西宁市城中区格兰小镇",
     "productType": "1",
     "openId": "",
     "state": "DEL"
     }
     ],
     "boProductAttr": [
     {
     "productId": "123",
     "prodId": "-1",
     "attrCd": "123344",
     "value": "1",
     "state": "ADD"
     },
     {
     "productId": "123",
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
    @RequestMapping("/productService/soProductServiceForOrderService")
    public String soProductServiceForOrderService(@RequestParam("data") String data);


    /**
     * 客户信息处理
     * 协议：
     *{
     *     boProduct:[{},{}]
     * }
     * @param data
     * @return
     * @throws Exception
     */
    @RequestMapping("/productService/soBoProduct")
    public String soBoProduct(@RequestParam("data") String data) ;

    /**
     * 客户信息属性处理
     * 协议：
     *{
     *     boProductAttr:[{},{}]
     * }
     * @param data
     * @return
     * @throws Exception
     */
    @RequestMapping("/productService/soBoProductAttr")
    public String soBoProductAttr(@RequestParam("data") String data) ;


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
    @RequestMapping("/productService/soDeleteProductService")
    public String soDeleteProductService(@RequestParam("data") String data);

}
