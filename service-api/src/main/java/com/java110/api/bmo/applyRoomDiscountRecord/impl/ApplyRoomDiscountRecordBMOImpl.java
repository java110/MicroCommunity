package com.java110.api.bmo.applyRoomDiscountRecord.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.applyRoomDiscountRecord.IApplyRoomDiscountRecordBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.intf.community.IApplyRoomDiscountRecordInnerServiceSMO;
import com.java110.po.applyRoomDiscountRecord.ApplyRoomDiscountRecordPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("applyRoomDiscountRecordBMOImpl")
public class ApplyRoomDiscountRecordBMOImpl extends ApiBaseBMO implements IApplyRoomDiscountRecordBMO {

    @Autowired
    private IApplyRoomDiscountRecordInnerServiceSMO applyRoomDiscountRecordInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addApplyRoomDiscountRecord(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("ardrId", "-1");
        ApplyRoomDiscountRecordPo applyRoomDiscountRecordPo = BeanConvertUtil.covertBean(paramInJson, ApplyRoomDiscountRecordPo.class);
        super.insert(dataFlowContext, applyRoomDiscountRecordPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_APPLY_ROOM_DISCOUNT_RECORD);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateApplyRoomDiscountRecord(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        ApplyRoomDiscountRecordPo applyRoomDiscountRecordPo = BeanConvertUtil.covertBean(paramInJson, ApplyRoomDiscountRecordPo.class);
        super.update(dataFlowContext, applyRoomDiscountRecordPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_APPLY_ROOM_DISCOUNT_RECORD);
    }



    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteApplyRoomDiscountRecord(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        ApplyRoomDiscountRecordPo applyRoomDiscountRecordPo = BeanConvertUtil.covertBean(paramInJson, ApplyRoomDiscountRecordPo.class);
        super.update(dataFlowContext, applyRoomDiscountRecordPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_APPLY_ROOM_DISCOUNT_RECORD);
    }

}
