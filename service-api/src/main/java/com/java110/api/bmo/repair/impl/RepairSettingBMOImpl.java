package com.java110.api.bmo.repair.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.repair.IRepairSettingBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.repairSetting.IRepairSettingInnerServiceSMO;
import com.java110.po.repair.RepairSettingPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("repairSettingBMOImpl")
public class RepairSettingBMOImpl extends ApiBaseBMO implements IRepairSettingBMO {

    @Autowired
    private IRepairSettingInnerServiceSMO repairSettingInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addRepairSetting(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("settingId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_settingId));

        RepairSettingPo repairSettingPo = BeanConvertUtil.covertBean(paramInJson, RepairSettingPo.class);
        repairSettingPo.setRepairType(repairSettingPo.getSettingId());
        super.insert(dataFlowContext, repairSettingPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_REPAIR_SETTING);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateRepairSetting(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        RepairSettingPo repairSettingPo = BeanConvertUtil.covertBean(paramInJson, RepairSettingPo.class);
        super.update(dataFlowContext, repairSettingPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_REPAIR_SETTING);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteRepairSetting(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        RepairSettingPo repairSettingPo = BeanConvertUtil.covertBean(paramInJson, RepairSettingPo.class);
        super.update(dataFlowContext, repairSettingPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_REPAIR_SETTING);
    }

}
