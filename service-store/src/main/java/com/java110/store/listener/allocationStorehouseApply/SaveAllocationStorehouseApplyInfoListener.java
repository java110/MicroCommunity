package com.java110.store.listener.allocationStorehouseApply;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.allocationStorehouseApply.AllocationStorehouseApplyPo;
import com.java110.store.listener.allocationStorehouseApply.AbstractAllocationStorehouseApplyBusinessServiceDataFlowListener;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.store.dao.IAllocationStorehouseApplyServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存 调拨申请信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveAllocationStorehouseApplyInfoListener")
@Transactional
public class SaveAllocationStorehouseApplyInfoListener extends AbstractAllocationStorehouseApplyBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveAllocationStorehouseApplyInfoListener.class);

    @Autowired
    private IAllocationStorehouseApplyServiceDao allocationAllocationStorehouseApplyhouseApplyServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_ALLOCATION_STOREHOUSE_APPLY;
    }

    /**
     * 保存调拨申请信息 business 表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessAllocationStorehouseApply 节点
        if(data.containsKey(AllocationStorehouseApplyPo.class.getSimpleName())){
            Object bObj = data.get(AllocationStorehouseApplyPo.class.getSimpleName());
            JSONArray businessAllocationStorehouseApplys = null;
            if(bObj instanceof JSONObject){
                businessAllocationStorehouseApplys = new JSONArray();
                businessAllocationStorehouseApplys.add(bObj);
            }else {
                businessAllocationStorehouseApplys = (JSONArray)bObj;
            }
            //JSONObject businessAllocationStorehouseApply = data.getJSONObject(AllocationStorehouseApplyPo.class.getSimpleName());
            for (int bAllocationStorehouseApplyIndex = 0; bAllocationStorehouseApplyIndex < businessAllocationStorehouseApplys.size();bAllocationStorehouseApplyIndex++) {
                JSONObject businessAllocationStorehouseApply = businessAllocationStorehouseApplys.getJSONObject(bAllocationStorehouseApplyIndex);
                doBusinessAllocationStorehouseApply(business, businessAllocationStorehouseApply);
                if(bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("applyId", businessAllocationStorehouseApply.getString("applyId"));
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

        //调拨申请信息
        List<Map> businessAllocationStorehouseApplyInfo = allocationAllocationStorehouseApplyhouseApplyServiceDaoImpl.getBusinessAllocationStorehouseApplyInfo(info);
        if( businessAllocationStorehouseApplyInfo != null && businessAllocationStorehouseApplyInfo.size() >0) {
            reFreshShareColumn(info, businessAllocationStorehouseApplyInfo.get(0));
            allocationAllocationStorehouseApplyhouseApplyServiceDaoImpl.saveAllocationStorehouseApplyInfoInstance(info);
            if(businessAllocationStorehouseApplyInfo.size() == 1) {
                dataFlowContext.addParamOut("applyId", businessAllocationStorehouseApplyInfo.get(0).get("allocationAllocationStorehouseApplyhouseApply_id"));
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
        //调拨申请信息
        List<Map> allocationAllocationStorehouseApplyhouseApplyInfo = allocationAllocationStorehouseApplyhouseApplyServiceDaoImpl.getAllocationStorehouseApplyInfo(info);
        if(allocationAllocationStorehouseApplyhouseApplyInfo != null && allocationAllocationStorehouseApplyhouseApplyInfo.size() > 0){
            reFreshShareColumn(paramIn, allocationAllocationStorehouseApplyhouseApplyInfo.get(0));
            allocationAllocationStorehouseApplyhouseApplyServiceDaoImpl.updateAllocationStorehouseApplyInfoInstance(paramIn);
        }
    }



    /**
     * 处理 businessAllocationStorehouseApply 节点
     * @param business 总的数据节点
     * @param businessAllocationStorehouseApply 调拨申请节点
     */
    private void doBusinessAllocationStorehouseApply(Business business,JSONObject businessAllocationStorehouseApply){

        Assert.jsonObjectHaveKey(businessAllocationStorehouseApply,"applyId","businessAllocationStorehouseApply 节点下没有包含 applyId 节点");

        if(businessAllocationStorehouseApply.getString("applyId").startsWith("-")){
            //刷新缓存
            //flushAllocationStorehouseApplyId(business.getDatas());

            businessAllocationStorehouseApply.put("applyId",GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_applyId));

        }

        businessAllocationStorehouseApply.put("bId",business.getbId());
        businessAllocationStorehouseApply.put("operate", StatusConstant.OPERATE_ADD);
        //保存调拨申请信息
        allocationAllocationStorehouseApplyhouseApplyServiceDaoImpl.saveBusinessAllocationStorehouseApplyInfo(businessAllocationStorehouseApply);

    }
    @Override
    public IAllocationStorehouseApplyServiceDao getAllocationStorehouseApplyServiceDaoImpl() {
        return allocationAllocationStorehouseApplyhouseApplyServiceDaoImpl;
    }

    public void setAllocationStorehouseApplyServiceDaoImpl(IAllocationStorehouseApplyServiceDao allocationAllocationStorehouseApplyhouseApplyServiceDaoImpl) {
        this.allocationAllocationStorehouseApplyhouseApplyServiceDaoImpl = allocationAllocationStorehouseApplyhouseApplyServiceDaoImpl;
    }
}
