package com.java110.store.listener.purchaseApplyDetail;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.purchase.PurchaseApplyDetailPo;
import com.java110.store.dao.IPurchaseApplyDetailServiceDao;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存 订单明细信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("savePurchaseApplyDetailInfoListener")
@Transactional
public class SavePurchaseApplyDetailInfoListener extends AbstractPurchaseApplyDetailBusinessServiceDataFlowListener{

    private static Logger logger = LoggerFactory.getLogger(SavePurchaseApplyDetailInfoListener.class);

    @Autowired
    private IPurchaseApplyDetailServiceDao purchaseApplyDetailServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_PURCHASE_APPLY_DETAIL;
    }

    /**
     * 保存订单明细信息 business 表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessPurchaseApplyDetail 节点
        if(data.containsKey(PurchaseApplyDetailPo.class.getSimpleName())){
            Object bObj = data.get(PurchaseApplyDetailPo.class.getSimpleName());
            JSONArray businessPurchaseApplyDetails = null;
            if(bObj instanceof JSONObject){
                businessPurchaseApplyDetails = new JSONArray();
                businessPurchaseApplyDetails.add(bObj);
            }else {
                businessPurchaseApplyDetails = (JSONArray)bObj;
            }
            //JSONObject businessPurchaseApplyDetail = data.getJSONObject("businessPurchaseApplyDetail");
            for (int bPurchaseApplyDetailIndex = 0; bPurchaseApplyDetailIndex < businessPurchaseApplyDetails.size();bPurchaseApplyDetailIndex++) {
                JSONObject businessPurchaseApplyDetail = businessPurchaseApplyDetails.getJSONObject(bPurchaseApplyDetailIndex);
                doBusinessPurchaseApplyDetail(business, businessPurchaseApplyDetail);
                if(bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("id", businessPurchaseApplyDetail.getString("id"));
                }
            }
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

        //订单明细信息
        List<Map> businessPurchaseApplyDetailInfo = purchaseApplyDetailServiceDaoImpl.getBusinessPurchaseApplyDetailInfo(info);
        if( businessPurchaseApplyDetailInfo != null && businessPurchaseApplyDetailInfo.size() >0) {
            reFreshShareColumn(info, businessPurchaseApplyDetailInfo.get(0));
            purchaseApplyDetailServiceDaoImpl.savePurchaseApplyDetailInfoInstance(info);
            if(businessPurchaseApplyDetailInfo.size() == 1) {
                dataFlowContext.addParamOut("id", businessPurchaseApplyDetailInfo.get(0).get("id"));
            }
        }
    }


    /**
     * 刷 分片字段
     *
     * @param info         查询对象
     * @param businessInfo 小区ID
     */
    private void reFreshShareColumn(Map info, Map businessInfo) {

        if (info.containsKey("storeId")) {
            return;
        }

        if (!businessInfo.containsKey("store_id")) {
            return;
        }

        info.put("storeId", businessInfo.get("store_id"));
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
        //订单明细信息
        List<Map> purchaseApplyDetailInfo = purchaseApplyDetailServiceDaoImpl.getPurchaseApplyDetailInfo(info);
        if(purchaseApplyDetailInfo != null && purchaseApplyDetailInfo.size() > 0){
            reFreshShareColumn(paramIn, purchaseApplyDetailInfo.get(0));
            purchaseApplyDetailServiceDaoImpl.updatePurchaseApplyDetailInfoInstance(paramIn);
        }
    }



    /**
     * 处理 businessPurchaseApplyDetail 节点
     * @param business 总的数据节点
     * @param businessPurchaseApplyDetail 订单明细节点
     */
    private void doBusinessPurchaseApplyDetail(Business business,JSONObject businessPurchaseApplyDetail){

        Assert.jsonObjectHaveKey(businessPurchaseApplyDetail,"id","businessPurchaseApplyDetail 节点下没有包含 id 节点");

        if(businessPurchaseApplyDetail.getString("id").startsWith("-")){
            //刷新缓存
            //flushPurchaseApplyDetailId(business.getDatas());

            businessPurchaseApplyDetail.put("id",GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_id));

        }

        businessPurchaseApplyDetail.put("bId",business.getbId());
        businessPurchaseApplyDetail.put("operate", StatusConstant.OPERATE_ADD);
        //保存订单明细信息
        purchaseApplyDetailServiceDaoImpl.saveBusinessPurchaseApplyDetailInfo(businessPurchaseApplyDetail);

    }

    public IPurchaseApplyDetailServiceDao getPurchaseApplyDetailServiceDaoImpl() {
        return purchaseApplyDetailServiceDaoImpl;
    }

    public void setPurchaseApplyDetailServiceDaoImpl(IPurchaseApplyDetailServiceDao purchaseApplyDetailServiceDaoImpl) {
        this.purchaseApplyDetailServiceDaoImpl = purchaseApplyDetailServiceDaoImpl;
    }
}
