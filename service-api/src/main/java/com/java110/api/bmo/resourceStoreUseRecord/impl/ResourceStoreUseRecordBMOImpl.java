package com.java110.api.bmo.resourceStoreUseRecord.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.resourceStoreUseRecord.IResourceStoreUseRecordBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.intf.store.IResourceStoreUseRecordInnerServiceSMO;
import com.java110.po.resourceStoreUseRecord.ResourceStoreUseRecordPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("resourceStoreUseRecordBMOImpl")
public class ResourceStoreUseRecordBMOImpl extends ApiBaseBMO implements IResourceStoreUseRecordBMO {

    @Autowired
    private IResourceStoreUseRecordInnerServiceSMO resourceStoreUseRecordInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addResourceStoreUseRecord(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("rsurId", "-1");
        ResourceStoreUseRecordPo resourceStoreUseRecordPo = BeanConvertUtil.covertBean(paramInJson, ResourceStoreUseRecordPo.class);
        super.insert(dataFlowContext, resourceStoreUseRecordPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_RESOURCE_STORE_USE_RECORD);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateResourceStoreUseRecord(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        ResourceStoreUseRecordPo resourceStoreUseRecordPo = BeanConvertUtil.covertBean(paramInJson, ResourceStoreUseRecordPo.class);
        super.update(dataFlowContext, resourceStoreUseRecordPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_RESOURCE_STORE_USE_RECORD);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteResourceStoreUseRecord(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        ResourceStoreUseRecordPo resourceStoreUseRecordPo = BeanConvertUtil.covertBean(paramInJson, ResourceStoreUseRecordPo.class);
        super.update(dataFlowContext, resourceStoreUseRecordPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_RESOURCE_STORE_USE_RECORD);
    }

}
