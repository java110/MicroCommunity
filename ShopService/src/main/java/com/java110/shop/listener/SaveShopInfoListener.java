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
@Java110Listener("saveShopInfoListener")
@Transactional
public class SaveShopInfoListener extends AbstractShopBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(SaveShopInfoListener.class);

    @Autowired
    IShopServiceDao shopServiceDaoImpl;

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_SHOP_INFO;
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
        if(data.containsKey("businessShop")){
            JSONObject businessShop = data.getJSONObject("businessShop");
            doBusinessShop(business,businessShop);
            dataFlowContext.addParamOut("shopId",businessShop.getString("shopId"));
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
        Map businessShopInfo = shopServiceDaoImpl.getBusinessShopInfo(info);
        if( businessShopInfo != null && !businessShopInfo.isEmpty()) {
            shopServiceDaoImpl.saveShopInfoInstance(info);
            dataFlowContext.addParamOut("storeId",businessShopInfo.get("store_id"));
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
        Map storeInfo = shopServiceDaoImpl.getShopInfo(info);
        if(storeInfo != null && !storeInfo.isEmpty()){
            paramIn.put("shopId",storeInfo.get("shop_id").toString());
            shopServiceDaoImpl.updateShopInfoInstance(paramIn);
            dataFlowContext.addParamOut("storeId",storeInfo.get("store_id"));
        }
    }



    /**
     * 处理 businessShop 节点
     * @param business 总的数据节点
     * @param businessShop 商品节点
     */
    private void doBusinessShop(Business business,JSONObject businessShop){

        Assert.jsonObjectHaveKey(businessShop,"shopId","businessShop 节点下没有包含 storeId 节点");

        Assert.jsonObjectHaveKey(businessShop,"startDate","businessShop 节点下没有包含 startDate 节点");
        Assert.jsonObjectHaveKey(businessShop,"endDate","businessShop 节点下没有包含 endDate 节点");

        businessShop.put("bId",business.getbId());
        businessShop.put("operate", StatusConstant.OPERATE_ADD);
        //对日期处理
        Date startDate = null;
        Date entDate = null;
        try {
            startDate = DateUtil.getDateFromString(businessShop.getString("startDate"), DateUtil.DATE_FORMATE_STRING_A);
            entDate = DateUtil.getDateFromString(businessShop.getString("endDate"), DateUtil.DATE_FORMATE_STRING_A);
            businessShop.put("startDate",startDate);
            businessShop.put("endDate",entDate);
        } catch (Exception e) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"传入参数 startDate endDate 格式不正确，请填写 "
                    +DateUtil.DATE_FORMATE_STRING_A +" 格式，"+businessShop);
        }
        //保存商户信息
        shopServiceDaoImpl.saveBusinessShopInfo(businessShop);

    }



    @Override
    public IShopServiceDao getShopServiceDaoImpl() {
        return shopServiceDaoImpl;
    }

    public void setShopServiceDaoImpl(IShopServiceDao shopServiceDaoImpl) {
        this.shopServiceDaoImpl = shopServiceDaoImpl;
    }
}
