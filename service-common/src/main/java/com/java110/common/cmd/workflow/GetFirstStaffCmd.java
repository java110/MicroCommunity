package com.java110.common.cmd.workflow;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IWorkflowServiceDao;
import com.java110.common.dao.IWorkflowStepServiceDao;
import com.java110.common.dao.IWorkflowStepStaffServiceDao;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.org.OrgStaffRelDto;
import com.java110.dto.workflow.WorkflowDto;
import com.java110.intf.store.IOrgStaffRelV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Java110Cmd(serviceCode = "/workflow/getFirstStaff")
public class GetFirstStaffCmd extends Cmd {

    @Autowired
    private IWorkflowServiceDao workflowServiceDaoImpl;

    @Autowired
    private IWorkflowStepServiceDao workflowStepServiceDaoImpl;

    @Autowired
    private IWorkflowStepStaffServiceDao workflowStepStaffServiceDaoImpl;

    @Autowired
    private IOrgStaffRelV1InnerServiceSMO orgStaffRelV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson,"flowType","未包含类型");
        Assert.hasKeyAndValue(reqJson,"communityId","未包含小区");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        String storeId = context.getReqHeaders().get("store-id");
        WorkflowDto workflowDto = new WorkflowDto();
        workflowDto.setCommunityId(reqJson.getString("communityId"));
        workflowDto.setFlowType(reqJson.getString("flowType"));
        workflowDto.setStoreId(storeId);

        List<Map> workflows = workflowServiceDaoImpl.getWorkflowInfo(BeanConvertUtil.beanCovertMap(workflowDto));

        if (workflows == null || workflows.size() < 1) {
            context.setResponseEntity(ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "未查询到流程"));
            return ;
        }

        WorkflowDto tmpWorkflowDto = BeanConvertUtil.covertBean(workflows.get(0), WorkflowDto.class);

        Map param = new HashMap();
        param.put("statusCd", "0");
        param.put("flowId", tmpWorkflowDto.getFlowId());
        param.put("seq", "1");
        param.put("communityId", tmpWorkflowDto.getCommunityId());
        param.put("storeId", tmpWorkflowDto.getStoreId());
        //查询步骤
        List<Map> workflowSteps = workflowStepServiceDaoImpl.getWorkflowStepInfo(param);

        if (workflowSteps == null || workflowSteps.size() < 1) {
            context.setResponseEntity(ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "未查询到流程定义"));
            return ;
        }

        param = new HashMap();
        param.put("statusCd", "0");
        param.put("communityId", tmpWorkflowDto.getCommunityId());
        param.put("stepId", workflowSteps.get(0).get("stepId"));
        param.put("storeId", tmpWorkflowDto.getStoreId());

        List<Map> workflowStepStaffs = workflowStepStaffServiceDaoImpl.getWorkflowStepStaffInfo(param);

        if (workflowStepStaffs == null || workflowStepStaffs.size() < 1) {
            context.setResponseEntity(ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "未查询到流程定义"));
            return ;
        }

        Map staffInfo = workflowStepStaffs.get(0);
        String staffId = staffInfo.get("staffId") + "";
        OrgStaffRelDto orgDto = new OrgStaffRelDto();
        if (staffId.startsWith("${")) {
            context.setResponseEntity(ResultVo.createResponseEntity(orgDto));
            return ;
        }
        orgDto.setStaffId(staffId);

        OrgStaffRelDto orgStaffRelDto = new OrgStaffRelDto();
        orgStaffRelDto.setStaffId(staffId);
        List<OrgStaffRelDto> orgStaffRelDtos = orgStaffRelV1InnerServiceSMOImpl.queryStaffOrgNames(orgStaffRelDto);
        if (orgStaffRelDtos == null || orgStaffRelDtos.size() < 1) {
            context.setResponseEntity(ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "未查询到员工组织信息"));
            return ;
        }
        orgDto = orgStaffRelDtos.get(0);
        orgDto.setStaffName(staffInfo.get("staffName") + "");

        context.setResponseEntity(ResultVo.createResponseEntity(orgDto));
    }
}
