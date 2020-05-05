package com.java110.api.listener.inspectionPlanStaff;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.inspectionPlanStaff.IInspectionPlanStaffInnerServiceSMO;
import com.java110.dto.inspectionPlanStaff.InspectionPlanStaffDto;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeInspectionPlanStaffConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.inspectionPlanStaff.ApiInspectionPlanStaffDataVo;
import com.java110.vo.api.inspectionPlanStaff.ApiInspectionPlanStaffVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询小区侦听类
 */
@Java110Listener("listInspectionPlanStaffsListener")
public class ListInspectionPlanStaffsListener extends AbstractServiceApiListener {

    @Autowired
    private IInspectionPlanStaffInnerServiceSMO inspectionPlanStaffInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeInspectionPlanStaffConstant.LIST_INSPECTIONPLANSTAFFS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IInspectionPlanStaffInnerServiceSMO getInspectionPlanStaffInnerServiceSMOImpl() {
        return inspectionPlanStaffInnerServiceSMOImpl;
    }

    public void setInspectionPlanStaffInnerServiceSMOImpl(IInspectionPlanStaffInnerServiceSMO inspectionPlanStaffInnerServiceSMOImpl) {
        this.inspectionPlanStaffInnerServiceSMOImpl = inspectionPlanStaffInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        InspectionPlanStaffDto inspectionPlanStaffDto = BeanConvertUtil.covertBean(reqJson, InspectionPlanStaffDto.class);

        int count = inspectionPlanStaffInnerServiceSMOImpl.queryInspectionPlanStaffsCount(inspectionPlanStaffDto);

        List<ApiInspectionPlanStaffDataVo> inspectionPlanStaffs = null;

        if (count > 0) {
            inspectionPlanStaffs = BeanConvertUtil.covertBeanList(inspectionPlanStaffInnerServiceSMOImpl.queryInspectionPlanStaffs(inspectionPlanStaffDto), ApiInspectionPlanStaffDataVo.class);
        } else {
            inspectionPlanStaffs = new ArrayList<>();
        }

        ApiInspectionPlanStaffVo apiInspectionPlanStaffVo = new ApiInspectionPlanStaffVo();

        apiInspectionPlanStaffVo.setTotal(count);
        apiInspectionPlanStaffVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiInspectionPlanStaffVo.setInspectionPlanStaffs(inspectionPlanStaffs);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiInspectionPlanStaffVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
