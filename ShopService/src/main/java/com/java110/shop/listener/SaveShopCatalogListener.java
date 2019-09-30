package com.java110.shop.listener;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.shop.dao.IShopServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 保存 商品信息 侦听
 * 处理 businessShop 节点
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveShopCatalogListener")
@Transactional
public class SaveShopCatalogListener extends AbstractShopBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(SaveShopCatalogListener.class);

    @Autowired
    IShopServiceDao shopServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_SHOP_CATALOG;
    }

    /**
     * 保存商户信息 business 表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessShop 节点
        if(data.containsKey("businessShopCatalog")){
            JSONObject businessShopCatalog = data.getJSONObject("businessShopCatalog");
            doBusinessShopCatalog(business,businessShopCatalog);
            dataFlowContext.addParamOut("catalogId",businessShopCatalog.getString("catalogId"));
        }

    }

    /**
     * business 数据转移到 instance
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();

        Map info = new HashMap();
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_ADD);

        //商户信息
        Map businessShopCatalog = shopServiceDaoImpl.getBusinessShopCatalog(info);
        if( businessShopCatalog != null && !businessShopCatalog.isEmpty()) {
            shopServiceDaoImpl.saveShopCatalogInstance(info);
            dataFlowContext.addParamOut("catalogId",businessShopCatalog.get("catalog_id"));
        }

    }

    /**
     * 撤单
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");
        Map info = new HashMap();
        info.put("bId",bId);
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        Map paramIn = new HashMap();
        paramIn.put("bId",bId);
        paramIn.put("statusCd",StatusConstant.STATUS_CD_INVALID);
        //商户信息
        Map storeCatalog = shopServiceDaoImpl.getShopCatalog(info);
        if(storeCatalog != null && !storeCatalog.isEmpty()){
            paramIn.put("storeId",storeCatalog.get("store_id").toString());
            shopServiceDaoImpl.updateShopCatalogInstance(paramIn);
            dataFlowContext.addParamOut("catalogId",storeCatalog.get("catalog_id"));
        }
    }



    /**
     * 处理 businessShop 节点
     * @param business 总的数据节点
     * @param businessShopCatalog 商品节点
     */
    private void doBusinessShopCatalog(Business business,JSONObject businessShopCatalog){

        Assert.jsonObjectHaveKey(businessShopCatalog,"storeId","businessShopCatalog 节点下没有包含 storeId 节点");

        businessShopCatalog.put("bId",business.getbId());
        businessShopCatalog.put("operate", StatusConstant.OPERATE_ADD);

        //保存商户信息
        shopServiceDaoImpl.saveBusinessShopCatalog(businessShopCatalog);

    }



    @Override
    public IShopServiceDao getShopServiceDaoImpl() {
        return shopServiceDaoImpl;
    }

    public void setShopServiceDaoImpl(IShopServiceDao shopServiceDaoImpl) {
        this.shopServiceDaoImpl = shopServiceDaoImpl;
    }
}
