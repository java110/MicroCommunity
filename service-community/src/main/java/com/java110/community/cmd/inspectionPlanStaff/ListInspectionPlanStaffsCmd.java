package com.java110.community.cmd.inspectionPlanStaff;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.inspection.InspectionPlanStaffDto;
import com.java110.intf.community.IInspectionPlanStaffInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.inspectionPlanStaff.ApiInspectionPlanStaffDataVo;
import com.java110.vo.api.inspectionPlanStaff.ApiInspectionPlanStaffVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "inspectionPlanStaff.listInspectionPlanStaffs")
public class ListInspectionPlanStaffsCmd extends Cmd {


    @Autowired
    private IInspectionPlanStaffInnerServiceSMO inspectionPlanStaffInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

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
