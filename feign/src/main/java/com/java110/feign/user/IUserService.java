package com.java110.feign.user;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.log.LoggerEngine;
import com.java110.common.util.ProtocolUtil;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * 用户服务接口
 * 用户查询，修改，删除 实现
 * Created by wuxw on 2017/4/5.
 */
@FeignClient(name = "user-service", fallback = UserServiceFallback.class)
public interface IUserService {

    /**
     * 通过User对象中数据查询用户信息
     * 如,用户ID，名称
     * @param data
     * @return
     */
    @RequestMapping("/userService/queryUserInfo")
    public String queryUserInfo(@RequestParam("data") String data);


    /**
     * 根据购物车信息查询
     * @param busiOrder
     * @return
     */
    public String queryUserInfoByOlId(@RequestParam("busiOrder") String busiOrder);


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
    public String soUserService(@RequestParam("data") String data);


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
    @RequestMapping("/userService/soUserServiceForOrderService")
    public String soUserServiceForOrderService(@RequestParam("data") String data);


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
    public String soBoCust(@RequestParam("data") String data ) ;

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
    public String soBoCustAttr(@RequestParam("data") String data) ;


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
    public String soDeleteCustService(@RequestParam("data") String data);

}
