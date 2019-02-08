package com.java110.shop.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.BusinessTypeConstant;
import com.java110.common.constant.StatusConstant;
import com.java110.common.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.shop.dao.IShopServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理节点 businessShopAttr
 * 和 businessShopAttrParam
 * Created by wuxw on 2018/7/7.
 */
@Java110Listener(name = "saveShopAttrAndAttrParamListener")
@Transactional
public class SaveShopAttrAndAttrParamListener extends AbstractShopBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(SaveShopAttrAndAttrParamListener.class);

    @Autowired
    IShopServiceDao shopServiceDaoImpl;


    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_SHOP_INFO;
    }



    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");
        //处理商品属性
        if(data.containsKey("businessShopAttr")){
            JSONArray businessShopAttrs = data.getJSONArray("businessShopAttr");
            doBusinessShopAttr(business,businessShopAttrs);
        }
        //处理商品属性参数
        if(data.containsKey("businessShopAttrParam")){
            JSONArray businessShopAttrParams = data.getJSONArray("businessShopAttrParam");
            doBusinessShopAttrParams(business,businessShopAttrParams);
        }
    }

    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {
        Map info = new HashMap();
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_ADD);
        //商品属性
        List<Map> businessShopAttrs = shopServiceDaoImpl.getBusinessShopAttrs(info);
        if(businessShopAttrs != null && businessShopAttrs.size() > 0) {
            shopServiceDaoImpl.saveShopAttrsInstance(info);
        }

        //商品属性参数
        List<Map> buinessShopAttrParams = shopServiceDaoImpl.getBusinessShopAttrParams(info);
        if(buinessShopAttrParams != null && buinessShopAttrParams.size() > 0){
            shopServiceDaoImpl.saveShopAttrParamsInstance(info);
        }
    }

    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        Map info = new HashMap();
        info.put("bId",bId);
        Map paramIn = new HashMap();
        paramIn.put("bId",bId);
        paramIn.put("statusCd",StatusConstant.STATUS_CD_INVALID);
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        //商品属性
        List<Map> shopAttrs = shopServiceDaoImpl.getShopAttrs(info);
        if(shopAttrs != null && shopAttrs.size()>0){
            shopServiceDaoImpl.updateShopAttrInstance(paramIn);
        }

        //商品属性参数
        List<Map> shopAttrParams = shopServiceDaoImpl.getShopAttrParams(info);
        if(shopAttrParams != null && shopAttrParams.size() >0){
            shopServiceDaoImpl.updateShopAttrParamInstance(info);
        }
    }


    /**
     * 处理商品 属性
     * @param business 当前业务
     * @param businessShopAttrs 商品属性
     */
    private void doBusinessShopAttr(Business business, JSONArray businessShopAttrs) {

        for(int shopAttrIndex = 0 ; shopAttrIndex < businessShopAttrs.size();shopAttrIndex ++){
            JSONObject shopAttr = businessShopAttrs.getJSONObject(shopAttrIndex);
            Assert.jsonObjectHaveKey(shopAttr,"attrId","businessShopAttr 节点下没有包含 attrId 节点");
            shopAttr.put("bId",business.getbId());
            shopAttr.put("operate", StatusConstant.OPERATE_ADD);
            shopServiceDaoImpl.saveBusinessShopAttr(shopAttr);
        }
    }

    /**
     * 商品属性参数
     * @param business 当前业务
     * @param businessShopAttrParams 商品属性参数
     */
    private void doBusinessShopAttrParams(Business business, JSONArray businessShopAttrParams) {

        for(int shopAttrParamIndex = 0 ; shopAttrParamIndex < businessShopAttrParams.size();shopAttrParamIndex ++){
            JSONObject shopAttrParam = businessShopAttrParams.getJSONObject(shopAttrParamIndex);
            Assert.jsonObjectHaveKey(shopAttrParam,"attrParamId","businessShopAttr 节点下没有包含 attrParamId 节点");
            shopAttrParam.put("bId",business.getbId());
            shopAttrParam.put("operate", StatusConstant.OPERATE_ADD);
            shopServiceDaoImpl.saveBusinessShopAttrParam(shopAttrParam);
        }
    }



    @Override
    public IShopServiceDao getShopServiceDaoImpl() {
        return shopServiceDaoImpl;
    }

    public void setShopServiceDaoImpl(IShopServiceDao shopServiceDaoImpl) {
        this.shopServiceDaoImpl = shopServiceDaoImpl;
    }
}
