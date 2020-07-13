package com.java110.common.bmo.workflow.impl;

import com.java110.common.bmo.workflow.IQueryWorkFlowFirstStaffBMO;
import com.java110.common.dao.IWorkflowServiceDao;
import com.java110.common.dao.IWorkflowStepServiceDao;
import com.java110.common.dao.IWorkflowStepStaffServiceDao;
import com.java110.dto.org.OrgDto;
import com.java110.dto.workflow.WorkflowDto;
import com.java110.intf.user.IOrgInnerServiceSMO;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("queryWorkFlowFirstStaffServiceImpl")
public class QueryWorkFlowFirstStaffBMOImpl implements IQueryWorkFlowFirstStaffBMO {

    @Autowired
    private IWorkflowServiceDao workflowServiceDaoImpl;

    @Autowired
    private IWorkflowStepServiceDao workflowStepServiceDaoImpl;

    @Autowired
    private IWorkflowStepStaffServiceDao workflowStepStaffServiceDaoImpl;

    @Autowired
    private IOrgInnerServiceSMO orgInnerServiceSMOImpl;

    @Override
    public ResponseEntity<String> query(WorkflowDto workflowDto) {

        List<Map> workflows = workflowServiceDaoImpl.getWorkflowInfo(BeanConvertUtil.beanCovertMap(workflowDto));

        if (workflows == null || workflows.size() < 1) {
            return null;
        }

        WorkflowDto tmpWorkflowDto = BeanConvertUtil.covertBean(workflows.get(0), WorkflowDto.class);

        Map param = new HashMap();
        param.put("statusCd", "0");
        param.put("flowId", tmpWorkflowDto.getFlowId());
        param.put("seq", "1");
        param.put("communityId", tmpWorkflowDto.getCommunityId());
        //查询步骤
        List<Map> workflowSteps = workflowStepServiceDaoImpl.getWorkflowStepInfo(param);

        if (workflowSteps == null || workflowSteps.size() < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "未查询到流程定义");
        }

        param = new HashMap();
        param.put("statusCd", "0");
        param.put("communityId", tmpWorkflowDto.getCommunityId());
        param.put("stepId", workflowSteps.get(0).get("stepId"));


        List<Map> workflowStepStaffs = workflowStepStaffServiceDaoImpl.getWorkflowStepStaffInfo(param);

        if (workflowStepStaffs == null || workflowStepStaffs.size() < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "未查询到流程定义");
        }

        Map staffInfo = workflowStepStaffs.get(0);

        OrgDto orgDto = new OrgDto();
        orgDto.setStaffId(staffInfo.get("staffId") + "");

        List<OrgDto> orgDtos = orgInnerServiceSMOImpl.queryOrgs(orgDto);

        if (orgDtos == null || orgDtos.size() < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "未查询到员工组织信息");
        }

        orgDto = orgDtos.get(0);
        orgDto.setStaffName(staffInfo.get("staffName") + "");

        return ResultVo.createResponseEntity(orgDto);
    }
}
