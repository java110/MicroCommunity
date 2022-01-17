package com.java110.store.listener.resourceStore;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.purchase.ResourceStorePo;
import com.java110.store.dao.IResourceStoreServiceDao;
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
 * 保存 资源信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveResourceStoreInfoListener")
@Transactional
public class SaveResourceStoreInfoListener extends AbstractResourceStoreBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveResourceStoreInfoListener.class);

    @Autowired
    private IResourceStoreServiceDao resourceResourceStoreServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_RESOURCE_STORE;
    }

    /**
     * 保存资源信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessResourceStore 节点
        if (data.containsKey(ResourceStorePo.class.getSimpleName())) {
            Object bObj = data.get(ResourceStorePo.class.getSimpleName());
            JSONArray businessResourceStores = null;
            if (bObj instanceof JSONObject) {
                businessResourceStores = new JSONArray();
                businessResourceStores.add(bObj);
            } else {
                businessResourceStores = (JSONArray) bObj;
            }
            //JSONObject businessResourceStore = data.getJSONObject("businessResourceStore");
            for (int bResourceStoreIndex = 0; bResourceStoreIndex < businessResourceStores.size(); bResourceStoreIndex++) {
                JSONObject businessResourceStore = businessResourceStores.getJSONObject(bResourceStoreIndex);
                doBusinessResourceStore(business, businessResourceStore);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("resId", businessResourceStore.getString("resId"));
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

        //资源信息
        List<Map> businessResourceStoreInfo = resourceResourceStoreServiceDaoImpl.getBusinessResourceStoreInfo(info);
        if (businessResourceStoreInfo != null && businessResourceStoreInfo.size() > 0) {
            reFreshShareColumn(info, businessResourceStoreInfo.get(0));
            resourceResourceStoreServiceDaoImpl.saveResourceStoreInfoInstance(info);
            if (businessResourceStoreInfo.size() == 1) {
                dataFlowContext.addParamOut("resId", businessResourceStoreInfo.get(0).get("resId"));
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
        //资源信息
        List<Map> resourceResourceStoreInfo = resourceResourceStoreServiceDaoImpl.getResourceStoreInfo(info);
        if (resourceResourceStoreInfo != null && resourceResourceStoreInfo.size() > 0) {
            reFreshShareColumn(paramIn, resourceResourceStoreInfo.get(0));
            resourceResourceStoreServiceDaoImpl.updateResourceStoreInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessResourceStore 节点
     *
     * @param business              总的数据节点
     * @param businessResourceStore 资源节点
     */
    private void doBusinessResourceStore(Business business, JSONObject businessResourceStore) {

        Assert.jsonObjectHaveKey(businessResourceStore, "resId", "businessResourceStore 节点下没有包含 resId 节点");

        if (businessResourceStore.getString("resId").startsWith("-")) {
            //刷新缓存
            //flushResourceStoreId(business.getDatas());

            businessResourceStore.put("resId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_resId));

        }

        businessResourceStore.put("bId", business.getbId());
        businessResourceStore.put("operate", StatusConstant.OPERATE_ADD);
        //保存资源信息
        resourceResourceStoreServiceDaoImpl.saveBusinessResourceStoreInfo(businessResourceStore);

    }

    public IResourceStoreServiceDao getResourceStoreServiceDaoImpl() {
        return resourceResourceStoreServiceDaoImpl;
    }

    public void setResourceStoreServiceDaoImpl(IResourceStoreServiceDao resourceResourceStoreServiceDaoImpl) {
        this.resourceResourceStoreServiceDaoImpl = resourceResourceStoreServiceDaoImpl;
    }
}
