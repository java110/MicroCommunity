package com.java110.shop.listener;

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
import java.util.Map;

/**
 * 保存 商品信息 侦听
 * 处理 businessShop 节点
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveShopDescListener")
@Transactional
public class SaveShopDescListener extends AbstractShopBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(SaveShopDescListener.class);

    @Autowired
    IShopServiceDao shopServiceDaoImpl;

    @Override
    public int getOrder() {
        return 5;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_SHOP_INFO;
    }

    /**
     * 保存商品信息 business 表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 shopDescId 节点
        if(data.containsKey("businessShopDesc")){
            JSONObject businessShopDesc = data.getJSONObject("businessShopDesc");
            doBusinessShopDesc(business,businessShopDesc);
            dataFlowContext.addParamOut("shopDescId",businessShopDesc.getString("shop_desc_Id"));
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
        Map businessShopDesc = shopServiceDaoImpl.getBusinessShopDesc(info);
        if( businessShopDesc != null && !businessShopDesc.isEmpty()) {
            shopServiceDaoImpl.saveShopDescInstance(info);
            dataFlowContext.addParamOut("shopDescId",businessShopDesc.get("shop_desc_id"));
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
        Map storeDesc = shopServiceDaoImpl.getShopDesc(info);
        if(storeDesc != null && !storeDesc.isEmpty()){
            paramIn.put("shopId",storeDesc.get("shop_id").toString());
            shopServiceDaoImpl.updateShopDescInstance(paramIn);
            dataFlowContext.addParamOut("shopDescId",storeDesc.get("shop_desc_id"));
        }
    }



    /**
     * 处理 businessShopDesc 节点
     * @param business 总的数据节点
     * @param businessShopDesc 商品节点
     */
    private void doBusinessShopDesc(Business business, JSONObject businessShopDesc){

        Assert.jsonObjectHaveKey(businessShopDesc,"shopDescId","businessShop 节点下没有包含 shopDescId 节点");

        businessShopDesc.put("bId",business.getbId());
        businessShopDesc.put("operate", StatusConstant.OPERATE_ADD);

        //保存商户信息
        shopServiceDaoImpl.saveBusinessShopDesc(businessShopDesc);

    }



    @Override
    public IShopServiceDao getShopServiceDaoImpl() {
        return shopServiceDaoImpl;
    }

    public void setShopServiceDaoImpl(IShopServiceDao shopServiceDaoImpl) {
        this.shopServiceDaoImpl = shopServiceDaoImpl;
    }
}
