package com.java110.api.bmo.attendanceClasses.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.attendanceClasses.IAttendanceClassesBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.common.IAttendanceClassesInnerServiceSMO;
import com.java110.po.attendanceClasses.AttendanceClassesPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("attendanceClassesBMOImpl")
public class AttendanceClassesBMOImpl extends ApiBaseBMO implements IAttendanceClassesBMO {

    @Autowired
    private IAttendanceClassesInnerServiceSMO attendanceClassesInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addAttendanceClasses(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("classesId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_classesId));
        AttendanceClassesPo attendanceClassesPo = BeanConvertUtil.covertBean(paramInJson, AttendanceClassesPo.class);
        super.insert(dataFlowContext, attendanceClassesPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_ATTENDANCE_CLASSES);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateAttendanceClasses(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        AttendanceClassesPo attendanceClassesPo = BeanConvertUtil.covertBean(paramInJson, AttendanceClassesPo.class);
        super.update(dataFlowContext, attendanceClassesPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_ATTENDANCE_CLASSES);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteAttendanceClasses(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        AttendanceClassesPo attendanceClassesPo = BeanConvertUtil.covertBean(paramInJson, AttendanceClassesPo.class);
        super.update(dataFlowContext, attendanceClassesPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_ATTENDANCE_CLASSES);
    }

}
