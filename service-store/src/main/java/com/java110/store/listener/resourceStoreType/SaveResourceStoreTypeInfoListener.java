package com.java110.store.listener.resourceStoreType;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.resourceStoreType.ResourceStoreTypePo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.store.dao.IResourceStoreTypeServiceDao;
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
 * 保存 物品类型信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveResourceStoreTypeInfoListener")
@Transactional
public class SaveResourceStoreTypeInfoListener extends AbstractResourceStoreTypeBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveResourceStoreTypeInfoListener.class);

    @Autowired
    private IResourceStoreTypeServiceDao resourceResourceStoreTypeTypeServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_RESOURCE_STORE_TYPE;
    }

    /**
     * 保存物品类型信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessResourceStoreType 节点
        if (data.containsKey(ResourceStoreTypePo.class.getSimpleName())) {
            Object bObj = data.get(ResourceStoreTypePo.class.getSimpleName());
            JSONArray businessResourceStoreTypes = null;
            if (bObj instanceof JSONObject) {
                businessResourceStoreTypes = new JSONArray();
                businessResourceStoreTypes.add(bObj);
            } else {
                businessResourceStoreTypes = (JSONArray) bObj;
            }
            //JSONObject businessResourceStoreType = data.getJSONObject(ResourceStoreTypePo.class.getSimpleName());
            for (int bResourceStoreTypeIndex = 0; bResourceStoreTypeIndex < businessResourceStoreTypes.size(); bResourceStoreTypeIndex++) {
                JSONObject businessResourceStoreType = businessResourceStoreTypes.getJSONObject(bResourceStoreTypeIndex);
                doBusinessResourceStoreType(business, businessResourceStoreType);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("rstId", businessResourceStoreType.getString("rstId"));
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

        //物品类型信息
        List<Map> businessResourceStoreTypeInfo = resourceResourceStoreTypeTypeServiceDaoImpl.getBusinessResourceStoreTypeInfo(info);
        if (businessResourceStoreTypeInfo != null && businessResourceStoreTypeInfo.size() > 0) {
            reFreshShareColumn(info, businessResourceStoreTypeInfo.get(0));
            resourceResourceStoreTypeTypeServiceDaoImpl.saveResourceStoreTypeInfoInstance(info);
            if (businessResourceStoreTypeInfo.size() == 1) {
                dataFlowContext.addParamOut("rstId", businessResourceStoreTypeInfo.get(0).get("rstId"));
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
        //物品类型信息
        List<Map> resourceResourceStoreTypeTypeInfo = resourceResourceStoreTypeTypeServiceDaoImpl.getResourceStoreTypeInfo(info);
        if (resourceResourceStoreTypeTypeInfo != null && resourceResourceStoreTypeTypeInfo.size() > 0) {
            reFreshShareColumn(paramIn, resourceResourceStoreTypeTypeInfo.get(0));
            resourceResourceStoreTypeTypeServiceDaoImpl.updateResourceStoreTypeInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessResourceStoreType 节点
     *
     * @param business                  总的数据节点
     * @param businessResourceStoreType 物品类型节点
     */
    private void doBusinessResourceStoreType(Business business, JSONObject businessResourceStoreType) {

        Assert.jsonObjectHaveKey(businessResourceStoreType, "rstId", "businessResourceStoreType 节点下没有包含 rstId 节点");

        if (businessResourceStoreType.getString("rstId").startsWith("-")) {
            //刷新缓存
            //flushResourceStoreTypeId(business.getDatas());
            businessResourceStoreType.put("rstId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_rstId));

        }

        businessResourceStoreType.put("bId", business.getbId());
        businessResourceStoreType.put("operate", StatusConstant.OPERATE_ADD);
        //保存物品类型信息
        resourceResourceStoreTypeTypeServiceDaoImpl.saveBusinessResourceStoreTypeInfo(businessResourceStoreType);

    }

    @Override
    public IResourceStoreTypeServiceDao getResourceStoreTypeServiceDaoImpl() {
        return resourceResourceStoreTypeTypeServiceDaoImpl;
    }

    public void setResourceStoreTypeServiceDaoImpl(IResourceStoreTypeServiceDao resourceResourceStoreTypeTypeServiceDaoImpl) {
        this.resourceResourceStoreTypeTypeServiceDaoImpl = resourceResourceStoreTypeTypeServiceDaoImpl;
    }
}
