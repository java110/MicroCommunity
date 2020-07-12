package com.java110.api.bmo.user.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.user.IUserAttrBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.intf.user.IUserAttrInnerServiceSMO;
import com.java110.po.userAttr.UserAttrPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userAttrBMOImpl")
public class UserAttrBMOImpl extends ApiBaseBMO implements IUserAttrBMO {

    @Autowired
    private IUserAttrInnerServiceSMO userAttrInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addUserAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("attrId", "-1");
        UserAttrPo userAttrPo = BeanConvertUtil.covertBean(paramInJson, UserAttrPo.class);
        super.insert(dataFlowContext, userAttrPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_USER_ATTR_INFO);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateUserAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        UserAttrPo userAttrPo = BeanConvertUtil.covertBean(paramInJson, UserAttrPo.class);
        super.update(dataFlowContext, userAttrPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_USER_ATTR_INFO);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteUserAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        UserAttrPo userAttrPo = BeanConvertUtil.covertBean(paramInJson, UserAttrPo.class);
        super.update(dataFlowContext, userAttrPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_USER_ATTR_INFO);
    }

}
