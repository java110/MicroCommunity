package com.java110.shop.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.BusinessTypeConstant;
import com.java110.common.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.shop.dao.IShopServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 如果shopId填写的值为-1,则重新生成
 * Created by wuxw on 2018/7/7.
 */
@Java110Listener(name="flushAboutShopIdListener")
public class FlushAboutShopIdListener extends AbstractShopBusinessServiceDataFlowListener {
    private final static Logger logger = LoggerFactory.getLogger(FlushAboutShopIdListener.class);

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_SHOP_INFO;
    }

    @Override
    public IShopServiceDao getShopServiceDaoImpl() {
        return null;
    }

    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");
        //刷新shopId
        if(data.containsKey("businessShop")){
            JSONObject businessShop = data.getJSONObject("businessShop");
            if(!businessShop.containsKey("shopId") || businessShop.getString("shopId").startsWith("-")){
                flushShopId(data);
            }
        }
        //刷新 attrId
        if(data.containsKey("businessShopAttr")){
            JSONArray businessShopAttrs = data.getJSONArray("businessShopAttr");
            for(int businessShopAttrIndex = 0 ; businessShopAttrIndex < businessShopAttrs.size();businessShopAttrIndex++){
                JSONObject attrObj = businessShopAttrs.getJSONObject(businessShopAttrIndex);
                if(attrObj.containsKey("attrId") && !attrObj.getString("attrId").startsWith("-")){
                    continue;
                }
                attrObj.put("attrId",GenerateCodeFactory.getShopAttrId());
            }
        }
        //刷新 shopPhotoId
        if(data.containsKey("businessShopPhoto")){
            JSONArray businessShopPhotos = data.getJSONArray("businessShopPhoto");
            for (int businessShopPhotoIndex = 0;businessShopPhotoIndex < businessShopPhotos.size();businessShopPhotoIndex ++){
                JSONObject photoObj = businessShopPhotos.getJSONObject(businessShopPhotoIndex);
                if(photoObj.containsKey("shopPhotoId") && !photoObj.getString("shopPhotoId").startsWith("-")){
                    continue;
                }
                photoObj.put("shopPhotoId",GenerateCodeFactory.getShopPhotoId());
            }
        }
        //刷新 attrParamId
        if(data.containsKey("businessShopAttrParam")){
            JSONArray businessShopAttrParams = data.getJSONArray("businessShopAttrParam");
            for (int businessShopAttrParamIndex = 0;businessShopAttrParamIndex < businessShopAttrParams.size();businessShopAttrParamIndex ++){
                JSONObject attrParamObj = businessShopAttrParams.getJSONObject(businessShopAttrParamIndex);
                if(attrParamObj.containsKey("attrParamId") && !attrParamObj.getString("attrParamId").startsWith("-")){
                    continue;
                }
                attrParamObj.put("attrParamId",GenerateCodeFactory.getShopAttrParamId());
            }
        }
        //刷新 shopPreferentialId
        if(data.containsKey("businessShopPreferential")){
            JSONObject businessShopPreferential = data.getJSONObject("businessShopPreferential");
            if(!businessShopPreferential.containsKey("shopPreferentialId") || businessShopPreferential.getString("shopPreferentialId").startsWith("-")){
                businessShopPreferential.put("shopPreferentialId",GenerateCodeFactory.getShopPreferentialId());
            }
        }
        //刷新 商品描述Id
        if(data.containsKey("businessShopDesc")){
            JSONObject businessShopDesc = data.getJSONObject("businessShopDesc");
            if(!businessShopDesc.containsKey("shopDescId") || businessShopDesc.getString("shopDescId").startsWith("-")){
                businessShopDesc.put("shopDescId",GenerateCodeFactory.getShopDescId());
            }
        }
    }

    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {
        // nothing to do
    }

    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {
        // nothing to do
    }

    /**
     * 刷新 商品ID
     * @param data
     */
    private void flushShopId(JSONObject data) {

        String shopId = GenerateCodeFactory.getShopId();
        JSONObject businessShop = data.getJSONObject("businessShop");
        businessShop.put("shopId",shopId);
        //刷商品属性
        if(data.containsKey("businessShopAttr")) {
            JSONArray businessShopAttrs = data.getJSONArray("businessShopAttr");
            for(int businessShopAttrIndex = 0;businessShopAttrIndex < businessShopAttrs.size();businessShopAttrIndex++) {
                JSONObject businessStoreAttr = businessShopAttrs.getJSONObject(businessShopAttrIndex);
                businessStoreAttr.put("shopId", shopId);
            }
        }
        //刷商品属性
        if(data.containsKey("businessShopAttrParam")) {
            JSONArray businessShopAttrParams = data.getJSONArray("businessShopAttrParam");
            for(int businessShopAttrParamIndex = 0;businessShopAttrParamIndex < businessShopAttrParams.size();businessShopAttrParamIndex++) {
                JSONObject businessStoreAttrParam = businessShopAttrParams.getJSONObject(businessShopAttrParamIndex);
                businessStoreAttrParam.put("shopId", shopId);
            }
        }
        //刷 是商品照片 的 shopId
        if(data.containsKey("businessShopPhoto")) {
            JSONArray businessShopPhotos = data.getJSONArray("businessShopPhoto");
            for(int businessShopPhotoIndex = 0;businessShopPhotoIndex < businessShopPhotos.size();businessShopPhotoIndex++) {
                JSONObject businessStorePhoto = businessShopPhotos.getJSONObject(businessShopPhotoIndex);
                businessStorePhoto.put("shopId", shopId);
            }
        }
        //刷 商品优惠 的 shopId
        if(data.containsKey("businessShopPreferential")) {
            JSONObject businessShopPreferential = data.getJSONObject("businessShopPreferential");
            businessShopPreferential.put("shopId", shopId);
        }
        //商品描述
        if(data.containsKey("businessShopDesc")){
            JSONObject businessShopDesc = data.getJSONObject("businessShopDesc");
            businessShopDesc.put("shopId", shopId);
        }
    }
}
