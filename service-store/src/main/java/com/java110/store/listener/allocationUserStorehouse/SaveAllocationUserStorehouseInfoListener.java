package com.java110.store.listener.allocationUserStorehouse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.allocationUserStorehouse.AllocationUserStorehousePo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.store.dao.IAllocationUserStorehouseServiceDao;
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
 * 保存 物品供应商信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveAllocationUserStorehouseInfoListener")
@Transactional
public class SaveAllocationUserStorehouseInfoListener extends AbstractAllocationUserStorehouseBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveAllocationUserStorehouseInfoListener.class);

    @Autowired
    private IAllocationUserStorehouseServiceDao allocationUserStorehouseServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_ALLOCATION_USER_STOREHOUSE;
    }

    /**
     * 保存物品供应商信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessAllocationUserStorehouse 节点
        if (data.containsKey(AllocationUserStorehousePo.class.getSimpleName())) {
            Object bObj = data.get(AllocationUserStorehousePo.class.getSimpleName());
            JSONArray businessAllocationUserStorehouses = null;
            if (bObj instanceof JSONObject) {
                businessAllocationUserStorehouses = new JSONArray();
                businessAllocationUserStorehouses.add(bObj);
            } else {
                businessAllocationUserStorehouses = (JSONArray) bObj;
            }
            //JSONObject businessAllocationUserStorehouse = data.getJSONObject(AllocationUserStorehousePo.class.getSimpleName());
            for (int bAllocationUserStorehouseIndex = 0; bAllocationUserStorehouseIndex < businessAllocationUserStorehouses.size(); bAllocationUserStorehouseIndex++) {
                JSONObject businessAllocationUserStorehouse = businessAllocationUserStorehouses.getJSONObject(bAllocationUserStorehouseIndex);
                doBusinessAllocationUserStorehouse(business, businessAllocationUserStorehouse);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("ausId", businessAllocationUserStorehouse.getString("ausId"));
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

        //物品供应商信息
        List<Map> businessAllocationUserStorehouseInfo = allocationUserStorehouseServiceDaoImpl.getBusinessAllocationUserStorehouseInfo(info);
        if (businessAllocationUserStorehouseInfo != null && businessAllocationUserStorehouseInfo.size() > 0) {
            reFreshShareColumn(info, businessAllocationUserStorehouseInfo.get(0));
            allocationUserStorehouseServiceDaoImpl.saveAllocationUserStorehouseInfoInstance(info);
            if (businessAllocationUserStorehouseInfo.size() == 1) {
                dataFlowContext.addParamOut("ausId", businessAllocationUserStorehouseInfo.get(0).get("aus_id"));
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

        if (info.containsKey("objId")) {
            return;
        }

        if (!businessInfo.containsKey("obj_id")) {
            return;
        }

        info.put("objId", businessInfo.get("obj_id"));
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
        //物品供应商信息
        List<Map> allocationUserAllocationUserStorehouseInfo = allocationUserStorehouseServiceDaoImpl.getAllocationUserStorehouseInfo(info);
        if (allocationUserAllocationUserStorehouseInfo != null && allocationUserAllocationUserStorehouseInfo.size() > 0) {
            reFreshShareColumn(paramIn, allocationUserAllocationUserStorehouseInfo.get(0));
            allocationUserStorehouseServiceDaoImpl.updateAllocationUserStorehouseInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessAllocationUserStorehouse 节点
     *
     * @param business                         总的数据节点
     * @param businessAllocationUserStorehouse 物品供应商节点
     */
    private void doBusinessAllocationUserStorehouse(Business business, JSONObject businessAllocationUserStorehouse) {

        Assert.jsonObjectHaveKey(businessAllocationUserStorehouse, "ausId", "businessAllocationUserStorehouse 节点下没有包含 ausId 节点");

        if (businessAllocationUserStorehouse.getString("ausId").startsWith("-")) {
            //刷新缓存
            //flushAllocationUserStorehouseId(business.getDatas());

            businessAllocationUserStorehouse.put("ausId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ausId));

        }

        businessAllocationUserStorehouse.put("bId", business.getbId());
        businessAllocationUserStorehouse.put("operate", StatusConstant.OPERATE_ADD);
        //保存物品供应商信息
        allocationUserStorehouseServiceDaoImpl.saveBusinessAllocationUserStorehouseInfo(businessAllocationUserStorehouse);

    }

    @Override
    public IAllocationUserStorehouseServiceDao getAllocationUserStorehouseServiceDaoImpl() {
        return allocationUserStorehouseServiceDaoImpl;
    }

    public void setAllocationUserStorehouseServiceDaoImpl(IAllocationUserStorehouseServiceDao allocationUserStorehouseServiceDaoImpl) {
        this.allocationUserStorehouseServiceDaoImpl = allocationUserStorehouseServiceDaoImpl;
    }
}
