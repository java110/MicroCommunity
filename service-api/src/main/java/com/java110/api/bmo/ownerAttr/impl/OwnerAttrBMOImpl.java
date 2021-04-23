package com.java110.api.bmo.ownerAttr.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.ownerAttr.IOwnerAttrBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.user.IOwnerAttrInnerServiceSMO;
import com.java110.po.owner.OwnerAttrPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ownerAttrBMOImpl")
public class OwnerAttrBMOImpl extends ApiBaseBMO implements IOwnerAttrBMO {

    @Autowired
    private IOwnerAttrInnerServiceSMO ownerAttrInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addOwnerAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("attrId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
        OwnerAttrPo ownerAttrPo = BeanConvertUtil.covertBean(paramInJson, OwnerAttrPo.class);
        super.insert(dataFlowContext, ownerAttrPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_OWNER_ATTR_INFO);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateOwnerAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        OwnerAttrPo ownerAttrPo = BeanConvertUtil.covertBean(paramInJson, OwnerAttrPo.class);
        super.update(dataFlowContext, ownerAttrPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_OWNER_ATTR_INFO);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteOwnerAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        OwnerAttrPo ownerAttrPo = BeanConvertUtil.covertBean(paramInJson, OwnerAttrPo.class);
        super.update(dataFlowContext, ownerAttrPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_OWNER_ATTR_INFO);
    }

}
