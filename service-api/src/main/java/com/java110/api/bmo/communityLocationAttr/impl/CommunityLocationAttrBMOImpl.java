package com.java110.api.bmo.communityLocationAttr.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.communityLocationAttr.ICommunityLocationAttrBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.intf.community.ICommunityLocationAttrInnerServiceSMO;
import com.java110.po.communityLocationAttr.CommunityLocationAttrPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("communityLocationAttrBMOImpl")
public class CommunityLocationAttrBMOImpl extends ApiBaseBMO implements ICommunityLocationAttrBMO {

    @Autowired
    private ICommunityLocationAttrInnerServiceSMO communityLocationAttrInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addCommunityLocationAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("attrId", "-1");
        CommunityLocationAttrPo communityLocationAttrPo = BeanConvertUtil.covertBean(paramInJson, CommunityLocationAttrPo.class);
        super.insert(dataFlowContext, communityLocationAttrPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_LOCATION_ATTR);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateCommunityLocationAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        CommunityLocationAttrPo communityLocationAttrPo = BeanConvertUtil.covertBean(paramInJson, CommunityLocationAttrPo.class);
        super.update(dataFlowContext, communityLocationAttrPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_LOCATION_ATTR);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteCommunityLocationAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        CommunityLocationAttrPo communityLocationAttrPo = BeanConvertUtil.covertBean(paramInJson, CommunityLocationAttrPo.class);
        super.update(dataFlowContext, communityLocationAttrPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_LOCATION_ATTR);
    }

}
