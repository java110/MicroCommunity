package com.java110.merchant.smo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.entity.merchant.Merchant;

/**
 *
 * 用户信息管理，服务
 * Created by wuxw on 2017/4/5.
 */
public interface IMerchantServiceSMO {

    /**
     * 新建用户
     * @param merchantInfoJson
     * @return
     */
    public String saveMerchant(String merchantInfoJson) throws Exception;

    /**
     * 所有服务类（增删改查用户）
     * @param merchantInfoJson
     * @return
     */
    public String soMerchantService(JSONObject merchantInfoJson) throws Exception;

    /**
     * 所有服务类（增删改查用户）
     * @param merchantInfoJson
     * @return
     */
    public String soMerchantServiceForOrderService(JSONObject merchantInfoJson) throws Exception;

    /**
     * 客户信息处理
     * 协议：
     *{
     *     boMerchant:[{},{}]
     * }
     * @param boMerchants
     * @return
     * @throws Exception
     */
    public String soBoMerchant(String boMerchants) throws Exception;

    /**
     * 客户信息属性处理
     * 协议：
     *{
     *     boMerchantAttr:[{},{}]
     * }
     * @param boMerchantAttrs
     * @return
     * @throws Exception
     */
    public String soBoMerchantAttr(String boMerchantAttrs) throws Exception;

    /**
     * 作废客户信息
     * [{},{},{}]
     * @param datas
     * @return
     * @throws Exception
     */
    public String soDeleteMerchantInfo(JSONArray datas) throws Exception;


    /**
     * 查询客户信息
     * 包括 基本信息merchant 和 属性信息 merchantAttr
     * @param merchant
     * @return
     * @throws Exception
     */
    public String queryMerchant(Merchant merchant) throws Exception;
}
