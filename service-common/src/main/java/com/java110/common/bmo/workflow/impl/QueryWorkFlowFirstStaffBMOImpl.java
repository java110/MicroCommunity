package com.java110.common.bmo.workflow.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java110.common.bmo.workflow.IQueryWorkFlowFirstStaffBMO;
import com.java110.common.dao.IWorkflowServiceDao;
import com.java110.common.dao.IWorkflowStepServiceDao;
import com.java110.common.dao.IWorkflowStepStaffServiceDao;
import com.java110.dto.oaWorkflow.OaWorkflowDto;
import com.java110.dto.org.OrgDto;
import com.java110.dto.workflow.WorkflowDto;
import com.java110.dto.workflow.WorkflowModelDto;
import com.java110.intf.oa.IOaWorkflowInnerServiceSMO;
import com.java110.intf.user.IOrgInnerServiceSMO;
import com.java110.po.oaWorkflow.OaWorkflowPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.Base64Convert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
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

    @Autowired
    private IOaWorkflowInnerServiceSMO oaWorkflowInnerServiceSMOImpl;

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
        param.put("storeId", tmpWorkflowDto.getStoreId());
        //查询步骤
        List<Map> workflowSteps = workflowStepServiceDaoImpl.getWorkflowStepInfo(param);

        if (workflowSteps == null || workflowSteps.size() < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "未查询到流程定义");
        }

        param = new HashMap();
        param.put("statusCd", "0");
        param.put("communityId", tmpWorkflowDto.getCommunityId());
        param.put("stepId", workflowSteps.get(0).get("stepId"));
        param.put("storeId", tmpWorkflowDto.getStoreId());

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

    @Override
    public ResponseEntity<String> deployModel(WorkflowModelDto workflowModelDto) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();

        OaWorkflowDto oaWorkflowDto = new OaWorkflowDto();
        oaWorkflowDto.setModelId(workflowModelDto.getModelId());
        List<OaWorkflowDto> oaWorkflowDtos = oaWorkflowInnerServiceSMOImpl.queryOaWorkflows(oaWorkflowDto);

        Assert.listOnlyOne(oaWorkflowDtos, "未包含流程");

        String deploymentid = "";
        try {
            Model modelData = repositoryService.getModel(workflowModelDto.getModelId());
            byte[] bpmnBytes = null;
            JsonNode editorNode = new ObjectMapper().readTree(repositoryService.getModelEditorSource(workflowModelDto.getModelId()));
            BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
            BpmnModel model = jsonConverter.convertToBpmnModel(editorNode);
            bpmnBytes = new BpmnXMLConverter().convertToXML(model);
            String encoded = Base64Convert.byteToBase64(bpmnBytes);
            byte[] decoded = Base64Convert.base64ToByte(encoded);
            String xml = new String(decoded);
            String processName = modelData.getName() + ".bpmn20.xml";
            ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);
            Deployment deployment = repositoryService.createDeployment().name(oaWorkflowDtos.get(0).getFlowName())
                    .addInputStream(processName, in).deploy();
            deploymentid = deployment.getId();
        } catch (Exception e) {
            System.out.println(e);
        }
        Assert.isTrue(!StringUtil.isEmpty(deploymentid), "流程部署出错");
        ProcessDefinition processDefinition = repositoryService
                .createProcessDefinitionQuery()
                .deploymentId(deploymentid)
                .singleResult();
        //更新部门流程关系表
        workflowModelDto.setDeploymentId(deploymentid);

        OaWorkflowPo oaWorkflowPo = new OaWorkflowPo();
        oaWorkflowPo.setFlowId(oaWorkflowDtos.get(0).getFlowId());
        oaWorkflowPo.setStoreId(oaWorkflowDtos.get(0).getStoreId());
        oaWorkflowPo.setProcessDefinitionKey(deploymentid);
        oaWorkflowInnerServiceSMOImpl.updateOaWorkflow(oaWorkflowPo);
//        //部署历史表
//        List<DeployHistoryEntity> deployHistoryEntities = deployHistoryRepository.getDeployHistoryByDeptWithProcessKeyId(deptWithProcessKeyId);
//        for (DeployHistoryEntity deployHistoryEntity : deployHistoryEntities) {
//            if (deployHistoryEntity.getModelKeyid().equals(modelId)) {
//                deployHistoryEntity.setDeploy(true);
//            } else {
//                deployHistoryEntity.setDeploy(false);
//            }
//            deployHistoryRepository.update(deployHistoryEntity);
//        }
        return ResultVo.success();
    }

}
