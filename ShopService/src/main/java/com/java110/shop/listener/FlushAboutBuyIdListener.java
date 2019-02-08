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
 * 如果buyId  buyAttrId 填写的值为-1,则重新生成
 *
 * Created by wuxw on 2018/7/7.
 */
@Java110Listener(name="flushAboutBuyIdListener")
public class FlushAboutBuyIdListener extends AbstractShopBusinessServiceDataFlowListener {
    private final static Logger logger = LoggerFactory.getLogger(FlushAboutBuyIdListener.class);

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_BUY_SHOP_INFO;
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
        if(data.containsKey("businessBuyShop")){
            JSONObject businessBuyShop = data.getJSONObject("businessBuyShop");
            if(!businessBuyShop.containsKey("buyId") || businessBuyShop.getString("buyId").startsWith("-")){
                flushShopBuyId(data);
            }
        }

        //刷新 attrId
        if(data.containsKey("businessBuyShopAttr")){
            JSONArray businessBuyShopAttrs = data.getJSONArray("businessBuyShopAttr");
            for(int businessBuyShopAttrIndex = 0 ; businessBuyShopAttrIndex < businessBuyShopAttrs.size();businessBuyShopAttrIndex++){
                JSONObject attrObj = businessBuyShopAttrs.getJSONObject(businessBuyShopAttrIndex);
                if(attrObj.containsKey("attrId") && !attrObj.getString("attrId").startsWith("-")){
                    continue;
                }
                attrObj.put("attrId",GenerateCodeFactory.getShopBuyAttrId());
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
     * 刷新 商品目录ID
     * @param data
     */
    private void flushShopBuyId(JSONObject data) {

        String buyId = GenerateCodeFactory.getShopBuyId();
        JSONObject businessBuyShop = data.getJSONObject("businessBuyShop");
        businessBuyShop.put("buyId",buyId);

        //刷商品属性
        if(data.containsKey("businessBuyShopAttr")) {
            JSONArray businessBuyShopAttrs = data.getJSONArray("businessBuyShopAttr");
            for(int businessBuyShopAttrIndex = 0;businessBuyShopAttrIndex < businessBuyShopAttrs.size();businessBuyShopAttrIndex++) {
                JSONObject businessBuyShopAttr = businessBuyShopAttrs.getJSONObject(businessBuyShopAttrIndex);
                businessBuyShopAttr.put("buyId", buyId);
            }
        }
    }
}
