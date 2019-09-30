package com.java110.shop.listener;

import com.alibaba.fastjson.JSONArray;
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
import java.util.List;
import java.util.Map;

/**
 * 处理节点 businessBuyShopAttr
 * Created by wuxw on 2018/7/7.
 */
@Java110Listener(name = "saveBuyShopAttrListener")
@Transactional
public class SaveBuyShopAttrListener extends AbstractShopBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(SaveBuyShopAttrListener.class);

    @Autowired
    IShopServiceDao shopServiceDaoImpl;


    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_BUY_SHOP_INFO;
    }



    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");
        //处理商品属性
        if(data.containsKey("businessBuyShopAttr")){
            JSONArray businessBuyShopAttrs = data.getJSONArray("businessBuyShopAttr");
            doBusinessBuyShopAttr(business,businessBuyShopAttrs);
        }

    }

    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {
        //todo buy 没有business过程，所以这里不做处理
    }

    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        Map info = new HashMap();
        info.put("bId",bId);
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        Map paramIn = new HashMap();
        paramIn.put("bId",bId);
        paramIn.put("statusCd",StatusConstant.STATUS_CD_INVALID);

        //商品属性
        List<Map> buyShopAttrs = shopServiceDaoImpl.getBuyShopAttrs(info);
        if(buyShopAttrs != null && buyShopAttrs.size()>0){
            shopServiceDaoImpl.updateBuyShopAttrInstance(paramIn);
        }
        
    }


    /**
     * 处理商品 属性
     * @param business 当前业务
     * @param businessBuyShopAttrs 商品属性
     */
    private void doBusinessBuyShopAttr(Business business, JSONArray businessBuyShopAttrs) {

        for(int shopAttrIndex = 0 ; shopAttrIndex < businessBuyShopAttrs.size();shopAttrIndex ++){
            JSONObject shopAttr = businessBuyShopAttrs.getJSONObject(shopAttrIndex);
            Assert.jsonObjectHaveKey(shopAttr,"attrId","businessBuyShopAttr 节点下没有包含 attrId 节点");
            shopAttr.put("bId",business.getbId());
            //shopAttr.put("operate", StatusConstant.OPERATE_ADD);
            shopServiceDaoImpl.saveBuyShopAttrInstance(shopAttr);
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
