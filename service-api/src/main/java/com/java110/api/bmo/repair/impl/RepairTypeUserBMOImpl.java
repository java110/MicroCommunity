package com.java110.api.bmo.repair.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.repair.IRepairTypeUserBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.repairTypeUser.IRepairTypeUserInnerServiceSMO;
import com.java110.po.repair.RepairTypeUserPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("repairTypeUserBMOImpl")
public class RepairTypeUserBMOImpl extends ApiBaseBMO implements IRepairTypeUserBMO {

    @Autowired
    private IRepairTypeUserInnerServiceSMO repairTypeUserInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addRepairTypeUser(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("typeUserId", "-1");
        RepairTypeUserPo repairTypeUserPo = BeanConvertUtil.covertBean(paramInJson, RepairTypeUserPo.class);
        super.insert(dataFlowContext, repairTypeUserPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_REPAIR_TYPE_USER);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateRepairTypeUser(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        RepairTypeUserPo repairTypeUserPo = BeanConvertUtil.covertBean(paramInJson, RepairTypeUserPo.class);
        super.update(dataFlowContext, repairTypeUserPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_REPAIR_TYPE_USER);
    }



    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteRepairTypeUser(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        RepairTypeUserPo repairTypeUserPo = BeanConvertUtil.covertBean(paramInJson, RepairTypeUserPo.class);
        super.update(dataFlowContext, repairTypeUserPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_REPAIR_TYPE_USER);
    }

}
