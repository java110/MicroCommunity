package com.java110.store.listener.resourceSupplier;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.resourceSupplier.ResourceSupplierPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.store.dao.IResourceSupplierServiceDao;
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
@Java110Listener("saveResourceSupplierInfoListener")
@Transactional
public class SaveResourceSupplierInfoListener extends AbstractResourceSupplierBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveResourceSupplierInfoListener.class);

    @Autowired
    private IResourceSupplierServiceDao resourceSupplierServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_RESOURCE_SUPPLIER;
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

        //处理 businessResourceSupplier 节点
        if (data.containsKey(ResourceSupplierPo.class.getSimpleName())) {
            Object bObj = data.get(ResourceSupplierPo.class.getSimpleName());
            JSONArray businessResourceSuppliers = null;
            if (bObj instanceof JSONObject) {
                businessResourceSuppliers = new JSONArray();
                businessResourceSuppliers.add(bObj);
            } else {
                businessResourceSuppliers = (JSONArray) bObj;
            }
            //JSONObject businessResourceSupplier = data.getJSONObject(ResourceSupplierPo.class.getSimpleName());
            for (int bResourceSupplierIndex = 0; bResourceSupplierIndex < businessResourceSuppliers.size(); bResourceSupplierIndex++) {
                JSONObject businessResourceSupplier = businessResourceSuppliers.getJSONObject(bResourceSupplierIndex);
                doBusinessResourceSupplier(business, businessResourceSupplier);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("rsId", businessResourceSupplier.getString("rsId"));
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
        List<Map> businessResourceSupplierInfo = resourceSupplierServiceDaoImpl.getBusinessResourceSupplierInfo(info);
        if (businessResourceSupplierInfo != null && businessResourceSupplierInfo.size() > 0) {
            reFreshShareColumn(info, businessResourceSupplierInfo.get(0));
            resourceSupplierServiceDaoImpl.saveResourceSupplierInfoInstance(info);
            if (businessResourceSupplierInfo.size() == 1) {
                dataFlowContext.addParamOut("rsId", businessResourceSupplierInfo.get(0).get("rs_id"));
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
        //物品供应商信息
        List<Map> resourceSupplierInfo = resourceSupplierServiceDaoImpl.getResourceSupplierInfo(info);
        if (resourceSupplierInfo != null && resourceSupplierInfo.size() > 0) {
            reFreshShareColumn(paramIn, resourceSupplierInfo.get(0));
            resourceSupplierServiceDaoImpl.updateResourceSupplierInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessResourceSupplier 节点
     *
     * @param business                 总的数据节点
     * @param businessResourceSupplier 物品供应商节点
     */
    private void doBusinessResourceSupplier(Business business, JSONObject businessResourceSupplier) {

        Assert.jsonObjectHaveKey(businessResourceSupplier, "rsId", "businessResourceSupplier 节点下没有包含 rsId 节点");

        if (businessResourceSupplier.getString("rsId").startsWith("-")) {
            //刷新缓存
            //flushResourceSupplierId(business.getDatas());

            businessResourceSupplier.put("rsId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_rsId));

        }

        businessResourceSupplier.put("bId", business.getbId());
        businessResourceSupplier.put("operate", StatusConstant.OPERATE_ADD);
        //保存物品供应商信息
        resourceSupplierServiceDaoImpl.saveBusinessResourceSupplierInfo(businessResourceSupplier);

    }

    @Override
    public IResourceSupplierServiceDao getResourceSupplierServiceDaoImpl() {
        return resourceSupplierServiceDaoImpl;
    }

    public void setResourceSupplierServiceDaoImpl(IResourceSupplierServiceDao resourceSupplierServiceDaoImpl) {
        this.resourceSupplierServiceDaoImpl = resourceSupplierServiceDaoImpl;
    }
}
