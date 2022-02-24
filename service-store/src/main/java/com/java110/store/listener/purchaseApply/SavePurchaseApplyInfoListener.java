package com.java110.store.listener.purchaseApply;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.po.purchase.PurchaseApplyDetailPo;
import com.java110.po.purchase.PurchaseApplyPo;
import com.java110.store.dao.IPurchaseApplyServiceDao;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.purchaseApply.PurchaseApplyDetailVo;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存 采购申请信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("savePurchaseApplyInfoListener")
@Transactional
public class SavePurchaseApplyInfoListener extends AbstractPurchaseApplyBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SavePurchaseApplyInfoListener.class);

    @Autowired
    private IPurchaseApplyServiceDao purchaseApplyServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_PURCHASE_APPLY;
    }

    /**
     * 保存采购申请信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessPurchaseApply 节点
        if (data.containsKey(PurchaseApplyPo.class.getSimpleName())) {
            Object bObj = data.get(PurchaseApplyPo.class.getSimpleName());
            JSONArray businessPurchaseApplys = null;
            if (bObj instanceof JSONObject) {
                businessPurchaseApplys = new JSONArray();
                businessPurchaseApplys.add(bObj);
            } else {
                businessPurchaseApplys = (JSONArray) bObj;
            }
            //JSONObject businessPurchaseApply = data.getJSONObject("businessPurchaseApply");
            for (int bPurchaseApplyIndex = 0; bPurchaseApplyIndex < businessPurchaseApplys.size(); bPurchaseApplyIndex++) {
                JSONObject businessPurchaseApply = businessPurchaseApplys.getJSONObject(bPurchaseApplyIndex);
                doBusinessPurchaseApply(business, businessPurchaseApply);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("applyOrderId", businessPurchaseApply.getString("applyOrderId"));
                }
            }
        }
    }

    /**
     * business 数据转移到 instance
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();

        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_ADD);

        //采购申请信息
        List<Map> businessPurchaseApplyInfo = purchaseApplyServiceDaoImpl.getBusinessPurchaseApplyInfo(info);
        List<Map> businessPurchaseApplyDetailInfo = purchaseApplyServiceDaoImpl.getBusinessPurchaseApplyDetailInfo(info);

        List<PurchaseApplyDetailVo> purchaseApplyDetailVos = new ArrayList<>();
        for (int i = 0; i < businessPurchaseApplyDetailInfo.size(); i++) {
            PurchaseApplyDetailVo purchaseApplyDetailVo = JSON.parseObject(JSON.toJSONString(businessPurchaseApplyDetailInfo.get(i)), PurchaseApplyDetailVo.class);
            purchaseApplyDetailVos.add(purchaseApplyDetailVo);
        }

        if (businessPurchaseApplyInfo != null && businessPurchaseApplyInfo.size() > 0) {
            reFreshShareColumn(info, businessPurchaseApplyInfo.get(0));
            purchaseApplyServiceDaoImpl.savePurchaseApplyInfoInstance(info);
            purchaseApplyServiceDaoImpl.savePurchaseApplyDetailInfo(BeanConvertUtil.covertBeanList(purchaseApplyDetailVos, PurchaseApplyDetailPo.class));
            if (businessPurchaseApplyInfo.size() == 1) {
                dataFlowContext.addParamOut("applyOrderId", businessPurchaseApplyInfo.get(0).get("apply_order_id"));
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
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");
        Map info = new HashMap();
        info.put("bId", bId);
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        Map paramIn = new HashMap();
        paramIn.put("bId", bId);
        paramIn.put("statusCd", StatusConstant.STATUS_CD_INVALID);
        //采购申请信息
        List<Map> purchaseApplyInfo = purchaseApplyServiceDaoImpl.getPurchaseApplyInfo(info);
        if (purchaseApplyInfo != null && purchaseApplyInfo.size() > 0) {
            reFreshShareColumn(paramIn, purchaseApplyInfo.get(0));
            purchaseApplyServiceDaoImpl.updatePurchaseApplyInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessPurchaseApply 节点
     *
     * @param business              总的数据节点
     * @param businessPurchaseApply 采购申请节点
     */
    private void doBusinessPurchaseApply(Business business, JSONObject businessPurchaseApply) {

        Assert.jsonObjectHaveKey(businessPurchaseApply, "applyOrderId", "businessPurchaseApply 节点下没有包含 applyOrderId 节点");

        //状态
        //businessPurchaseApply.put("state","1000");
        businessPurchaseApply.put("bId", business.getbId());
        businessPurchaseApply.put("operate", StatusConstant.OPERATE_ADD);
        Object jsonArray = businessPurchaseApply.get("resourceStores");
        List<PurchaseApplyDetailVo> list = JSONObject.parseArray(jsonArray.toString(), PurchaseApplyDetailVo.class);
        for (PurchaseApplyDetailVo purchaseApplyDetailVo : list) {
            purchaseApplyDetailVo.setApplyOrderId(businessPurchaseApply.get("applyOrderId").toString());
            purchaseApplyDetailVo.setbId(business.getbId());
            purchaseApplyDetailVo.setStatusCd("0");
            purchaseApplyDetailVo.setOperate(StatusConstant.OPERATE_ADD);
        }
        //保存采购申请信息
        purchaseApplyServiceDaoImpl.saveBusinessPurchaseApplyInfo(businessPurchaseApply);
        //保存订单明细
        purchaseApplyServiceDaoImpl.saveBusinessPurchaseApplyDetailInfo(list);

    }

    public IPurchaseApplyServiceDao getPurchaseApplyServiceDaoImpl() {
        return purchaseApplyServiceDaoImpl;
    }

    public void setPurchaseApplyServiceDaoImpl(IPurchaseApplyServiceDao purchaseApplyServiceDaoImpl) {
        this.purchaseApplyServiceDaoImpl = purchaseApplyServiceDaoImpl;
    }
}
