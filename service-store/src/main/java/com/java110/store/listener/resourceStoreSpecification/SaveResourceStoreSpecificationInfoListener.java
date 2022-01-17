package com.java110.store.listener.resourceStoreSpecification;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.resourceStoreSpecification.ResourceStoreSpecificationPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.store.dao.IResourceStoreSpecificationServiceDao;
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
 * 保存 物品规格信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveResourceStoreSpecificationInfoListener")
@Transactional
public class SaveResourceStoreSpecificationInfoListener extends AbstractResourceStoreSpecificationBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveResourceStoreSpecificationInfoListener.class);

    @Autowired
    private IResourceStoreSpecificationServiceDao resourceStoreSpecificationServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_RESOURCE_STORE_SPECIFICATION;
    }

    /**
     * 保存物品规格信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessResourceStoreSpecification 节点
        if (data.containsKey(ResourceStoreSpecificationPo.class.getSimpleName())) {
            Object bObj = data.get(ResourceStoreSpecificationPo.class.getSimpleName());
            JSONArray businessResourceStoreSpecifications = null;
            if (bObj instanceof JSONObject) {
                businessResourceStoreSpecifications = new JSONArray();
                businessResourceStoreSpecifications.add(bObj);
            } else {
                businessResourceStoreSpecifications = (JSONArray) bObj;
            }
            //JSONObject businessResourceStoreSpecification = data.getJSONObject(ResourceStoreSpecificationPo.class.getSimpleName());
            for (int bResourceStoreSpecificationIndex = 0; bResourceStoreSpecificationIndex < businessResourceStoreSpecifications.size(); bResourceStoreSpecificationIndex++) {
                JSONObject businessResourceStoreSpecification = businessResourceStoreSpecifications.getJSONObject(bResourceStoreSpecificationIndex);
                doBusinessResourceStoreSpecification(business, businessResourceStoreSpecification);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("rssId", businessResourceStoreSpecification.getString("rssId"));
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

        //物品规格信息
        List<Map> businessResourceStoreSpecificationInfo = resourceStoreSpecificationServiceDaoImpl.getBusinessResourceStoreSpecificationInfo(info);
        if (businessResourceStoreSpecificationInfo != null && businessResourceStoreSpecificationInfo.size() > 0) {
            reFreshShareColumn(info, businessResourceStoreSpecificationInfo.get(0));
            resourceStoreSpecificationServiceDaoImpl.saveResourceStoreSpecificationInfoInstance(info);
            if (businessResourceStoreSpecificationInfo.size() == 1) {
                dataFlowContext.addParamOut("rssId", businessResourceStoreSpecificationInfo.get(0).get("rss_id"));
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
        //物品规格信息
        List<Map> resourceResourceStoreSpecificationSpecificationInfo = resourceStoreSpecificationServiceDaoImpl.getResourceStoreSpecificationInfo(info);
        if (resourceResourceStoreSpecificationSpecificationInfo != null && resourceResourceStoreSpecificationSpecificationInfo.size() > 0) {
            reFreshShareColumn(paramIn, resourceResourceStoreSpecificationSpecificationInfo.get(0));
            resourceStoreSpecificationServiceDaoImpl.updateResourceStoreSpecificationInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessResourceStoreSpecification 节点
     *
     * @param business                           总的数据节点
     * @param businessResourceStoreSpecification 物品规格节点
     */
    private void doBusinessResourceStoreSpecification(Business business, JSONObject businessResourceStoreSpecification) {

        Assert.jsonObjectHaveKey(businessResourceStoreSpecification, "rssId", "businessResourceStoreSpecification 节点下没有包含 rssId 节点");

        if (businessResourceStoreSpecification.getString("rssId").startsWith("-")) {
            //刷新缓存
            //flushResourceStoreSpecificationId(business.getDatas());

            businessResourceStoreSpecification.put("rssId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_rssId));

        }

        businessResourceStoreSpecification.put("bId", business.getbId());
        businessResourceStoreSpecification.put("operate", StatusConstant.OPERATE_ADD);
        //保存物品规格信息
        resourceStoreSpecificationServiceDaoImpl.saveBusinessResourceStoreSpecificationInfo(businessResourceStoreSpecification);

    }

    @Override
    public IResourceStoreSpecificationServiceDao getResourceStoreSpecificationServiceDaoImpl() {
        return resourceStoreSpecificationServiceDaoImpl;
    }

    public void setResourceStoreSpecificationServiceDaoImpl(IResourceStoreSpecificationServiceDao resourceStoreSpecificationServiceDaoImpl) {
        this.resourceStoreSpecificationServiceDaoImpl = resourceStoreSpecificationServiceDaoImpl;
    }
}
