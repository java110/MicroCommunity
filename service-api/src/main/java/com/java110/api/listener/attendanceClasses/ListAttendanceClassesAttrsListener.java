package com.java110.api.listener.attendanceClasses;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.attendanceClasses.AttendanceClassesAttrDto;
import com.java110.intf.common.IAttendanceClassesAttrInnerServiceSMO;
import com.java110.utils.constant.ServiceCodeAttendanceClassesAttrConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询小区侦听类
 */
@Java110Listener("listAttendanceClassesAttrsListener")
public class ListAttendanceClassesAttrsListener extends AbstractServiceApiListener {

    @Autowired
    private IAttendanceClassesAttrInnerServiceSMO attendanceClassesAttrInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeAttendanceClassesAttrConstant.LIST_ATTENDANCECLASSESATTRS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IAttendanceClassesAttrInnerServiceSMO getAttendanceClassesAttrInnerServiceSMOImpl() {
        return attendanceClassesAttrInnerServiceSMOImpl;
    }

    public void setAttendanceClassesAttrInnerServiceSMOImpl(IAttendanceClassesAttrInnerServiceSMO attendanceClassesAttrInnerServiceSMOImpl) {
        this.attendanceClassesAttrInnerServiceSMOImpl = attendanceClassesAttrInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        AttendanceClassesAttrDto attendanceClassesAttrDto = BeanConvertUtil.covertBean(reqJson, AttendanceClassesAttrDto.class);

        int count = attendanceClassesAttrInnerServiceSMOImpl.queryAttendanceClassesAttrsCount(attendanceClassesAttrDto);

        List<AttendanceClassesAttrDto> attendanceClassesAttrDtos = null;

        if (count > 0) {
            attendanceClassesAttrDtos = attendanceClassesAttrInnerServiceSMOImpl.queryAttendanceClassesAttrs(attendanceClassesAttrDto);
        } else {
            attendanceClassesAttrDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, attendanceClassesAttrDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
