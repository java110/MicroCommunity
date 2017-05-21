package com.java110.merchant.smo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.entity.product.Product;

/**
 *
 * 用户信息管理，服务
 * Created by wuxw on 2017/4/5.
 */
public interface IProductServiceSMO {

    /**
     * 新建用户
     * @param productInfoJson
     * @return
     */
    public String saveProduct(String productInfoJson) throws Exception;

    /**
     * 所有服务类（增删改查用户）
     * @param productInfoJson
     * @return
     */
    public String soProductService(JSONObject productInfoJson) throws Exception;

    /**
     * 所有服务类（增删改查用户）
     * @param productInfoJson
     * @return
     */
    public String soProductServiceForOrderService(JSONObject productInfoJson) throws Exception;

    /**
     * 客户信息处理
     * 协议：
     *{
     *     boProduct:[{},{}]
     * }
     * @param boProducts
     * @return
     * @throws Exception
     */
    public String soBoProduct(String boProducts) throws Exception;

    /**
     * 客户信息属性处理
     * 协议：
     *{
     *     boProductAttr:[{},{}]
     * }
     * @param boProductAttrs
     * @return
     * @throws Exception
     */
    public String soBoProductAttr(String boProductAttrs) throws Exception;

    /**
     * 作废客户信息
     * [{},{},{}]
     * @param datas
     * @return
     * @throws Exception
     */
    public String soDeleteProductInfo(JSONArray datas) throws Exception;


    /**
     * 查询客户信息
     * 包括 基本信息product 和 属性信息 productAttr
     * @param product
     * @return
     * @throws Exception
     */
    public String queryProduct(Product product) throws Exception;
}
