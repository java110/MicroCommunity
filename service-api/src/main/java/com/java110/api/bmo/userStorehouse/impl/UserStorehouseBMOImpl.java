package com.java110.api.bmo.userStorehouse.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.userStorehouse.IUserStorehouseBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.intf.store.IUserStorehouseInnerServiceSMO;
import com.java110.po.userStorehouse.UserStorehousePo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userStorehouseBMOImpl")
public class UserStorehouseBMOImpl extends ApiBaseBMO implements IUserStorehouseBMO {

    @Autowired
    private IUserStorehouseInnerServiceSMO userStorehouseInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addUserStorehouse(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("usId", "-1");
        UserStorehousePo userStorehousePo = BeanConvertUtil.covertBean(paramInJson, UserStorehousePo.class);
        super.insert(dataFlowContext, userStorehousePo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_USER_STOREHOUSE);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateUserStorehouse(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        UserStorehousePo userStorehousePo = BeanConvertUtil.covertBean(paramInJson, UserStorehousePo.class);
        super.update(dataFlowContext, userStorehousePo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_USER_STOREHOUSE);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteUserStorehouse(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        UserStorehousePo userStorehousePo = BeanConvertUtil.covertBean(paramInJson, UserStorehousePo.class);
        super.update(dataFlowContext, userStorehousePo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_USER_STOREHOUSE);
    }

}
