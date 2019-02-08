package com.java110.shop.listener;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.BusinessTypeConstant;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.constant.StatusConstant;
import com.java110.common.exception.ListenerExecuteException;
import com.java110.common.util.Assert;
import com.java110.common.util.DateUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.shop.dao.IShopServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 保存 商品信息 侦听
 * 处理 businessShop 节点
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveShopPreferentialListener")
@Transactional
public class SaveShopPreferentialListener extends AbstractShopBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(SaveShopPreferentialListener.class);

    @Autowired
    IShopServiceDao shopServiceDaoImpl;

    @Override
    public int getOrder() {
        return 4;
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

        //处理 businessShopPreferential 节点
        if(data.containsKey("businessShopPreferential")){
            JSONObject businessShopPreferential = data.getJSONObject("businessShopPreferential");
            doBusinessShopPreferential(business,businessShopPreferential);
            dataFlowContext.addParamOut("shopPreferentialId",businessShopPreferential.getString("shopPreferentialId"));
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
        Map businessShopPreferential = shopServiceDaoImpl.getBusinessShopPreferential(info);
        if( businessShopPreferential != null && !businessShopPreferential.isEmpty()) {
            shopServiceDaoImpl.saveShopPreferentialInstance(info);
            dataFlowContext.addParamOut("shopPreferentialId",businessShopPreferential.get("shop_preferential_id"));
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
        Map storePreferential = shopServiceDaoImpl.getShopPreferential(info);
        if(storePreferential != null && !storePreferential.isEmpty()){
            paramIn.put("shopId",storePreferential.get("shop_id").toString());
            shopServiceDaoImpl.updateShopPreferentialInstance(paramIn);
            dataFlowContext.addParamOut("shopPreferentialId",storePreferential.get("shop_preferential_id"));
        }
    }



    /**
     * 处理 businessShopPreferential 节点
     * @param business 总的数据节点
     * @param businessShopPreferential 商品节点
     */
    private void doBusinessShopPreferential(Business business, JSONObject businessShopPreferential){

        Assert.jsonObjectHaveKey(businessShopPreferential,"shopPreferentialId","businessShop 节点下没有包含 shopPreferentialId 节点");
        Assert.jsonObjectHaveKey(businessShopPreferential,"preferentialStartDate","businessShop 节点下没有包含 preferentialStartDate 节点");
        Assert.jsonObjectHaveKey(businessShopPreferential,"preferentialEndDate","businessShop 节点下没有包含 preferentialEndDate 节点");

        businessShopPreferential.put("bId",business.getbId());
        businessShopPreferential.put("operate", StatusConstant.OPERATE_ADD);
        //对日期处理
        Date preferentialStartDate = null;
        Date preferentialEndDate = null;
        try {
            preferentialStartDate = DateUtil.getDateFromString(businessShopPreferential.getString("preferentialStartDate"), DateUtil.DATE_FORMATE_STRING_A);
            preferentialEndDate = DateUtil.getDateFromString(businessShopPreferential.getString("preferentialEndDate"), DateUtil.DATE_FORMATE_STRING_A);
            businessShopPreferential.put("preferentialStartDate",preferentialStartDate);
            businessShopPreferential.put("preferentialEndDate",preferentialEndDate);
        } catch (Exception e) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"传入参数 preferentialStartDate preferentialEndDate 格式不正确，请填写 "
                    +DateUtil.DATE_FORMATE_STRING_A +" 格式，"+businessShopPreferential);
        }
        //保存商户信息
        shopServiceDaoImpl.saveBusinessShopPreferential(businessShopPreferential);

    }



    @Override
    public IShopServiceDao getShopServiceDaoImpl() {
        return shopServiceDaoImpl;
    }

    public void setShopServiceDaoImpl(IShopServiceDao shopServiceDaoImpl) {
        this.shopServiceDaoImpl = shopServiceDaoImpl;
    }
}
