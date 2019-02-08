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
@Java110Listener("saveBuyShopListener")
@Transactional
public class SaveBuyShopListener extends AbstractShopBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(SaveBuyShopListener.class);

    @Autowired
    IShopServiceDao shopServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_BUY_SHOP_INFO;
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
        if(data.containsKey("businessBuyShop")){
            JSONObject businessBuyShop = data.getJSONObject("businessBuyShop");
            doBusinessBuyShop(business,businessBuyShop);
            dataFlowContext.addParamOut("buyId",businessBuyShop.getString("buyId"));
        }

    }

    /**
     * business 数据转移到 instance
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {
        //todo buy 没有business过程，所以这里不做处理
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
        Map buyShop = shopServiceDaoImpl.getBuyShop(info);
        if(buyShop != null && !buyShop.isEmpty()){
            paramIn.put("shopId",buyShop.get("shop_id").toString());
            shopServiceDaoImpl.updateBuyShopInstance(paramIn);
            dataFlowContext.addParamOut("buyId",buyShop.get("buy_id"));
        }
    }



    /**
     * 处理 businessShop 节点
     * @param business 总的数据节点
     * @param businessBuyShop 商品节点
     */
    private void doBusinessBuyShop(Business business,JSONObject businessBuyShop){

        Assert.jsonObjectHaveKey(businessBuyShop,"buyId","businessBuyShop 节点下没有包含 buyId 节点");

        Assert.jsonObjectHaveKey(businessBuyShop,"shopId","businessBuyShop 节点下没有包含 shopId 节点");

        businessBuyShop.put("bId",business.getbId());
        //businessBuyShop.put("operate", StatusConstant.OPERATE_ADD);

        //保存商户信息
        shopServiceDaoImpl.saveBuyShopInstance(businessBuyShop);

    }



    @Override
    public IShopServiceDao getShopServiceDaoImpl() {
        return shopServiceDaoImpl;
    }

    public void setShopServiceDaoImpl(IShopServiceDao shopServiceDaoImpl) {
        this.shopServiceDaoImpl = shopServiceDaoImpl;
    }
}
