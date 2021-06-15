package com.java110.api.bmo.communityLocation.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.communityLocation.ICommunityLocationBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.community.ICommunityLocationInnerServiceSMO;
import com.java110.po.community.CommunityLocationPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("communityLocationBMOImpl")
public class CommunityLocationBMOImpl extends ApiBaseBMO implements ICommunityLocationBMO {

    @Autowired
    private ICommunityLocationInnerServiceSMO communityLocationInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addCommunityLocation(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("locationId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_locationId));
        CommunityLocationPo communityLocationPo = BeanConvertUtil.covertBean(paramInJson, CommunityLocationPo.class);
        super.insert(dataFlowContext, communityLocationPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_LOCATION);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateCommunityLocation(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        CommunityLocationPo communityLocationPo = BeanConvertUtil.covertBean(paramInJson, CommunityLocationPo.class);
        super.update(dataFlowContext, communityLocationPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_LOCATION);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteCommunityLocation(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        CommunityLocationPo communityLocationPo = BeanConvertUtil.covertBean(paramInJson, CommunityLocationPo.class);
        super.update(dataFlowContext, communityLocationPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_LOCATION);
    }

}
