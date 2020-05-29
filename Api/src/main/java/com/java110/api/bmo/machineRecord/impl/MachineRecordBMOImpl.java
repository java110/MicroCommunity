package com.java110.api.bmo.machineRecord.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.machineRecord.IMachineRecordBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.po.machine.MachineRecordPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.stereotype.Service;

/**
 * @ClassName MachineRecordBMOImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/9 22:50
 * @Version 1.0
 * add by wuxw 2020/3/9
 **/
@Service("machineRecordBMOImpl")
public class MachineRecordBMOImpl extends ApiBaseBMO implements IMachineRecordBMO {

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteMachineRecord(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        MachineRecordPo machineRecordPo = BeanConvertUtil.covertBean(paramInJson, MachineRecordPo.class);
        super.delete(dataFlowContext, machineRecordPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_MACHINE_RECORD);
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addMachineRecord(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        String machineRecordId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_machineRecordId);
        paramInJson.put("machineRecordId", machineRecordId);
        JSONObject businessMachineRecord = new JSONObject();
        businessMachineRecord.putAll(paramInJson);
        businessMachineRecord.put("machineRecordId", machineRecordId);
        MachineRecordPo machineRecordPo = BeanConvertUtil.covertBean(businessMachineRecord, MachineRecordPo.class);
        super.insert(dataFlowContext, machineRecordPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_MACHINE_RECORD);

    }

    /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addPhoto(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        JSONObject businessUnit = new JSONObject();
        businessUnit.put("fileRelId", "-1");
        businessUnit.put("relTypeCd", "60000");
        businessUnit.put("saveWay", "table");
        businessUnit.put("objId", paramInJson.getString("machineRecordId"));
        businessUnit.put("fileRealName", paramInJson.getString("photoId"));
        businessUnit.put("fileSaveName", paramInJson.getString("fileSaveName"));
        MachineRecordPo machineRecordPo = BeanConvertUtil.covertBean(businessUnit, MachineRecordPo.class);
        super.insert(dataFlowContext, machineRecordPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FILE_REL);

    }
}
