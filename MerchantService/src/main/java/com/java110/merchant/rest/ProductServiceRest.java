package com.java110.merchant.rest;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.log.LoggerEngine;
import com.java110.common.util.Assert;
import com.java110.common.util.ProtocolUtil;
import com.java110.core.base.controller.BaseController;
import com.java110.entity.product.Product;
import com.java110.feign.product.IProductService;
import com.java110.merchant.smo.IProductServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户服务提供类
 * Created by wuxw on 2017/4/5.
 */
@RestController
public class ProductServiceRest extends BaseController implements IProductService {

    @Autowired
    IProductServiceSMO iProductServiceSMO;

    /**
     * 通过Product对象中数据查询用户信息
     * 如,用户ID，名称
     * @param data
     * @return
     */
    @RequestMapping("/productService/queryProductInfo")
    public String queryProductInfo(@RequestParam("data") String data){
        LoggerEngine.debug("queryProductInfo入参：" + data);


        String resultProductInfo = null;

        JSONObject reqProductJSON = null;
        try {
            reqProductJSON = this.simpleValidateJSON(data);
            Product oldProduct = new Product();
            oldProduct.setProductId(reqProductJSON.getString("productId"));
            resultProductInfo = iProductServiceSMO.queryProduct(oldProduct);

        } catch (Exception e) {
            LoggerEngine.error("服务处理出现异常：", e);
            resultProductInfo = ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"服务处理出现异常"+e,null);
        } finally {
            LoggerEngine.debug("用户服务操作客户出参：" + resultProductInfo);
            return resultProductInfo;
        }
    }


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
    public String soProductService(@RequestParam("data") String data){

        LoggerEngine.debug("soProductService入参：" + data);

        String resultProductInfo = null;

        JSONObject reqProductJSON = null;
        try {
            reqProductJSON = this.simpleValidateJSON(data);
            //1.0规则校验，报文是否合法


            //2.0 受理产品信息
            resultProductInfo = iProductServiceSMO.soProductService(reqProductJSON);


        } catch (Exception e) {
            LoggerEngine.error("服务处理出现异常：", e);
            resultProductInfo = ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"服务处理出现异常"+e,null);
        } finally {
            LoggerEngine.debug("用户服务操作产品出参：" + resultProductInfo);
            return resultProductInfo;
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
    @Override
    @RequestMapping("/productService/soProductServiceForOrderService")
    public String soProductServiceForOrderService(@RequestParam("data") String data) {
        LoggerEngine.debug("soProductService入参：" + data);

        String resultProductInfo = null;

        JSONObject reqProductJSON = null;
        try {
            reqProductJSON = this.simpleValidateJSON(data);
            //1.0规则校验，报文是否合法


            //2.0 受理客户信息
            resultProductInfo = iProductServiceSMO.soProductServiceForOrderService(reqProductJSON);


        } catch (Exception e) {
            LoggerEngine.error("服务处理出现异常：", e);
            resultProductInfo = ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"服务处理出现异常"+e,null);
        } finally {
            LoggerEngine.debug("用户服务操作客户出参：" + resultProductInfo);
            return resultProductInfo;
        }
    }


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
    public String soBoProduct(@RequestParam("data") String data ) {
        LoggerEngine.debug("soBoProduct入参：" + data);

        String resultProductInfo = null;

        JSONObject reqProductJSON = null;
        try {
            reqProductJSON = this.simpleValidateJSON(data);
            resultProductInfo = iProductServiceSMO.soBoProduct(data);
        }catch (Exception e){
            LoggerEngine.error("服务处理出现异常：", e);
            resultProductInfo = ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"服务处理出现异常:"+e,null);
        } finally {
            LoggerEngine.debug("用户服务操作客户出参：" + resultProductInfo);
            return resultProductInfo;
        }
    }

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
    public String soBoProductAttr(@RequestParam("data") String data) {
        LoggerEngine.debug("soBoProductAttr入参：" + data);

        String resultProductInfo = null;

        JSONObject reqProductJSON = null;
        try {
            reqProductJSON = this.simpleValidateJSON(data);
            resultProductInfo = iProductServiceSMO.soBoProductAttr(data);
        }catch (Exception e){
            LoggerEngine.error("服务处理出现异常：", e);
            resultProductInfo = ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"服务处理出现异常"+e,null);
        } finally {
            LoggerEngine.debug("用户服务操作客户出参：" + resultProductInfo);
            return resultProductInfo;
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
    @RequestMapping("/productService/soDeleteProductService")
    public String soDeleteProductService(@RequestParam("data") String data){

        LoggerEngine.debug("作废订单入参："+data);

        String resultProductInfo = null;

        JSONObject reqParam = null;

        try{
            reqParam = this.simpleValidateJSON(data);

            Assert.isNull(reqParam,"data","传入报文错误，没有包含data节点"+reqParam);

            resultProductInfo = iProductServiceSMO.soDeleteProductInfo(reqParam.getJSONArray("data"));

        }catch (Exception e){
            LoggerEngine.error("服务处理出现异常：", e);
            resultProductInfo = ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"服务处理出现异常"+e+data,null);
        }finally {
            LoggerEngine.debug("作废订单出参：" + resultProductInfo);
            return resultProductInfo;
        }
    }


    public IProductServiceSMO getiProductServiceSMO() {
        return iProductServiceSMO;
    }

    public void setiProductServiceSMO(IProductServiceSMO iProductServiceSMO) {
        this.iProductServiceSMO = iProductServiceSMO;
    }
}
