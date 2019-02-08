package com.java110.shop.listener;

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
 * 如果shopCatalogId填写的值为-1,则重新生成
 * Created by wuxw on 2018/7/7.
 */
@Java110Listener(name="flushShopCatalogIdListener")
public class FlushShopCatalogIdListener extends AbstractShopBusinessServiceDataFlowListener {
    private final static Logger logger = LoggerFactory.getLogger(FlushShopCatalogIdListener.class);

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_SHOP_CATALOG;
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
        if(data.containsKey("businessShopCatalog")){
            JSONObject businessShopCatalog = data.getJSONObject("businessShopCatalog");
            if(!businessShopCatalog.containsKey("catalogId") || businessShopCatalog.getString("catalogId").startsWith("-")){
                flushShopCatalogId(data);
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
    private void flushShopCatalogId(JSONObject data) {

        String catalogId = GenerateCodeFactory.getShopCatalogId();
        JSONObject businessShopCatalog = data.getJSONObject("businessShopCatalog");
        businessShopCatalog.put("catalogId",catalogId);
    }
}
