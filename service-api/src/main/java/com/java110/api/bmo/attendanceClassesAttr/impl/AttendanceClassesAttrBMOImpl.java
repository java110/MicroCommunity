package com.java110.api.bmo.attendanceClassesAttr.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.attendanceClassesAttr.IAttendanceClassesAttrBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.intf.common.IAttendanceClassesAttrInnerServiceSMO;
import com.java110.po.attendanceClassesAttr.AttendanceClassesAttrPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("attendanceClassesAttrBMOImpl")
public class AttendanceClassesAttrBMOImpl extends ApiBaseBMO implements IAttendanceClassesAttrBMO {

    @Autowired
    private IAttendanceClassesAttrInnerServiceSMO attendanceClassesAttrInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addAttendanceClassesAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("attrId", "-1");
        AttendanceClassesAttrPo attendanceClassesAttrPo = BeanConvertUtil.covertBean(paramInJson, AttendanceClassesAttrPo.class);
        super.insert(dataFlowContext, attendanceClassesAttrPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_ATTENDANCE_CLASSES_ATTR);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateAttendanceClassesAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        AttendanceClassesAttrPo attendanceClassesAttrPo = BeanConvertUtil.covertBean(paramInJson, AttendanceClassesAttrPo.class);
        super.update(dataFlowContext, attendanceClassesAttrPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_ATTENDANCE_CLASSES_ATTR);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteAttendanceClassesAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        AttendanceClassesAttrPo attendanceClassesAttrPo = BeanConvertUtil.covertBean(paramInJson, AttendanceClassesAttrPo.class);
        super.update(dataFlowContext, attendanceClassesAttrPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_ATTENDANCE_CLASSES_ATTR);
    }

}
