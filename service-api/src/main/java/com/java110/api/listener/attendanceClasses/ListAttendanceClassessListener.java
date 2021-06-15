package com.java110.api.listener.attendanceClasses;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.attendanceClasses.AttendanceClassesDto;
import com.java110.intf.common.IAttendanceClassesInnerServiceSMO;
import com.java110.utils.constant.ServiceCodeAttendanceClassesConstant;
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
@Java110Listener("listAttendanceClassessListener")
public class ListAttendanceClassessListener extends AbstractServiceApiListener {

    @Autowired
    private IAttendanceClassesInnerServiceSMO attendanceClassesInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeAttendanceClassesConstant.LIST_ATTENDANCECLASSESS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IAttendanceClassesInnerServiceSMO getAttendanceClassesInnerServiceSMOImpl() {
        return attendanceClassesInnerServiceSMOImpl;
    }

    public void setAttendanceClassesInnerServiceSMOImpl(IAttendanceClassesInnerServiceSMO attendanceClassesInnerServiceSMOImpl) {
        this.attendanceClassesInnerServiceSMOImpl = attendanceClassesInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        AttendanceClassesDto attendanceClassesDto = BeanConvertUtil.covertBean(reqJson, AttendanceClassesDto.class);

        int count = attendanceClassesInnerServiceSMOImpl.queryAttendanceClassessCount(attendanceClassesDto);

        List<AttendanceClassesDto> attendanceClassesDtos = null;

        if (count > 0) {
            attendanceClassesDtos = attendanceClassesInnerServiceSMOImpl.queryAttendanceClassess(attendanceClassesDto);
        } else {
            attendanceClassesDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, attendanceClassesDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
