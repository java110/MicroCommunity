package com.java110.store.listener.allocationStorehouse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.allocationStorehouse.AllocationStorehousePo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.store.dao.IAllocationStorehouseServiceDao;
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
 * 保存 仓库调拨信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveAllocationStorehouseInfoListener")
@Transactional
public class SaveAllocationStorehouseInfoListener extends AbstractAllocationStorehouseBusinessServiceDataFlowListener{

    private static Logger logger = LoggerFactory.getLogger(SaveAllocationStorehouseInfoListener.class);

    @Autowired
    private IAllocationStorehouseServiceDao allocationAllocationStorehousehouseServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_ALLOCATION_STOREHOUSE;
    }

    /**
     * 保存仓库调拨信息 business 表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessAllocationStorehouse 节点
        if(data.containsKey(AllocationStorehousePo.class.getSimpleName())){
            Object bObj = data.get(AllocationStorehousePo.class.getSimpleName());
            JSONArray businessAllocationStorehouses = null;
            if(bObj instanceof JSONObject){
                businessAllocationStorehouses = new JSONArray();
                businessAllocationStorehouses.add(bObj);
            }else {
                businessAllocationStorehouses = (JSONArray)bObj;
            }
            //JSONObject businessAllocationStorehouse = data.getJSONObject(AllocationStorehousePo.class.getSimpleName());
            for (int bAllocationStorehouseIndex = 0; bAllocationStorehouseIndex < businessAllocationStorehouses.size();bAllocationStorehouseIndex++) {
                JSONObject businessAllocationStorehouse = businessAllocationStorehouses.getJSONObject(bAllocationStorehouseIndex);
                doBusinessAllocationStorehouse(business, businessAllocationStorehouse);
                if(bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("asId", businessAllocationStorehouse.getString("asId"));
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

        //仓库调拨信息
        List<Map> businessAllocationStorehouseInfo = allocationAllocationStorehousehouseServiceDaoImpl.getBusinessAllocationStorehouseInfo(info);
        if( businessAllocationStorehouseInfo != null && businessAllocationStorehouseInfo.size() >0) {
            reFreshShareColumn(info, businessAllocationStorehouseInfo.get(0));
            allocationAllocationStorehousehouseServiceDaoImpl.saveAllocationStorehouseInfoInstance(info);
            if(businessAllocationStorehouseInfo.size() == 1) {
                dataFlowContext.addParamOut("asId", businessAllocationStorehouseInfo.get(0).get("as_id"));
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
        //仓库调拨信息
        List<Map> allocationAllocationStorehousehouseInfo = allocationAllocationStorehousehouseServiceDaoImpl.getAllocationStorehouseInfo(info);
        if(allocationAllocationStorehousehouseInfo != null && allocationAllocationStorehousehouseInfo.size() > 0){
            reFreshShareColumn(paramIn, allocationAllocationStorehousehouseInfo.get(0));
            allocationAllocationStorehousehouseServiceDaoImpl.updateAllocationStorehouseInfoInstance(paramIn);
        }
    }



    /**
     * 处理 businessAllocationStorehouse 节点
     * @param business 总的数据节点
     * @param businessAllocationStorehouse 仓库调拨节点
     */
    private void doBusinessAllocationStorehouse(Business business,JSONObject businessAllocationStorehouse){

        Assert.jsonObjectHaveKey(businessAllocationStorehouse,"asId","asId 节点下没有包含 asId 节点");

        if(businessAllocationStorehouse.getString("asId").startsWith("-")){
            //刷新缓存
            //flushAllocationStorehouseId(business.getDatas());

            businessAllocationStorehouse.put("asId",GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_allocationStorehouseId));

        }

        businessAllocationStorehouse.put("bId",business.getbId());
        businessAllocationStorehouse.put("operate", StatusConstant.OPERATE_ADD);
        //保存仓库调拨信息
        allocationAllocationStorehousehouseServiceDaoImpl.saveBusinessAllocationStorehouseInfo(businessAllocationStorehouse);

    }
    @Override
    public IAllocationStorehouseServiceDao getAllocationStorehouseServiceDaoImpl() {
        return allocationAllocationStorehousehouseServiceDaoImpl;
    }

    public void setAllocationStorehouseServiceDaoImpl(IAllocationStorehouseServiceDao allocationAllocationStorehousehouseServiceDaoImpl) {
        this.allocationAllocationStorehousehouseServiceDaoImpl = allocationAllocationStorehousehouseServiceDaoImpl;
    }
}
