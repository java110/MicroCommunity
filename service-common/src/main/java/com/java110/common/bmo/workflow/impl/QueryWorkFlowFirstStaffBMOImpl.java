package com.java110.common.bmo.workflow.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.java110.common.bmo.workflow.IQueryWorkFlowFirstStaffBMO;
import com.java110.common.dao.IWorkflowServiceDao;
import com.java110.common.dao.IWorkflowStepServiceDao;
import com.java110.common.dao.IWorkflowStepStaffServiceDao;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.oaWorkflow.OaWorkflowDto;
import com.java110.dto.oaWorkflowForm.OaWorkflowFormDto;
import com.java110.dto.oaWorkflowXml.OaWorkflowXmlDto;
import com.java110.dto.org.OrgDto;
import com.java110.dto.workflow.WorkflowDto;
import com.java110.dto.workflow.WorkflowModelDto;
import com.java110.intf.oa.IOaWorkflowFormInnerServiceSMO;
import com.java110.intf.oa.IOaWorkflowInnerServiceSMO;
import com.java110.intf.oa.IOaWorkflowXmlInnerServiceSMO;
import com.java110.intf.user.IOrgInnerServiceSMO;
import com.java110.po.oaWorkflow.OaWorkflowPo;
import com.java110.po.oaWorkflowXml.OaWorkflowXmlPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.Base64Convert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("queryWorkFlowFirstStaffServiceImpl")
public class QueryWorkFlowFirstStaffBMOImpl implements IQueryWorkFlowFirstStaffBMO {
    private static final Logger logger = LoggerFactory.getLogger(QueryWorkFlowFirstStaffBMOImpl.class);
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

    @Autowired
    private IOaWorkflowXmlInnerServiceSMO oaWorkflowXmlInnerServiceSMOImpl;

    @Autowired
    private IOaWorkflowFormInnerServiceSMO oaWorkflowFormInnerServiceSMOImpl;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ObjectMapper objectMapper;


    String MODEL_ID = "modelId";
    String MODEL_NAME = "name";
    String MODEL_REVISION = "revision";
    String MODEL_DESCRIPTION = "description";


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

        //表单 部署
        deployForm(oaWorkflowDtos.get(0));

        String deploymentid = "";
        try {
            Model modelData = repositoryService.getModel(workflowModelDto.getModelId());
            byte[] bpmnBytes = null;
            bpmnBytes = repositoryService.getModelEditorSource(workflowModelDto.getModelId());
            String encoded = Base64Convert.byteToBase64(bpmnBytes);
            byte[] decoded = Base64Convert.base64ToByte(encoded);
            String xml = new String(decoded);
            String processName = modelData.getName() + ".bpmn20.xml";
            ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);
            Deployment deployment = repositoryService.createDeployment().name(oaWorkflowDtos.get(0).getFlowName())
                    .addInputStream(processName, in).deploy();
            deploymentid = deployment.getId();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
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
        oaWorkflowPo.setState(OaWorkflowDto.STATE_COMPLAINT);
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

    /**
     * 部署表单
     *
     * @param oaWorkflowDto
     */
    private void deployForm(OaWorkflowDto oaWorkflowDto) {
        if (StringUtil.isEmpty(oaWorkflowDto.getCurFormId())) {
            throw new IllegalArgumentException("未设置表单");
        }
        OaWorkflowFormDto oaWorkflowFormDto = new OaWorkflowFormDto();
        oaWorkflowFormDto.setFormId(oaWorkflowDto.getCurFormId());
        oaWorkflowFormDto.setStoreId(oaWorkflowDto.getStoreId());
        List<OaWorkflowFormDto> oaWorkflowFormDtos = oaWorkflowFormInnerServiceSMOImpl.queryOaWorkflowForms(oaWorkflowFormDto);
        Assert.listOnlyOne(oaWorkflowFormDtos, "未设置表单");
        //查询表是否存在

//        int count = oaWorkflowFormInnerServiceSMOImpl.hasTable(oaWorkflowFormDtos.get(0).getTableName());
//        if (count > 0) { // 已经部署过不用再部署
//            return;
//        }

        String formJson = oaWorkflowFormDtos.get(0).getFormJson();

        Assert.isJsonObject(formJson, "表单设计出错，请重新设计");

        JSONObject form = JSONObject.parseObject(formJson);

        JSONArray components = form.getJSONArray("components");
        JSONObject component = null;
        StringBuffer sql = new StringBuffer("create table if not exists ");
        sql.append(oaWorkflowFormDtos.get(0).getTableName());
        sql.append(" (");
        boolean isVarchar = false;
        JSONObject validate = null;
        for (int componentIndex = 0; componentIndex < components.size(); componentIndex++) {
            component = components.getJSONObject(componentIndex);
            if ("text".equals(component.getString("type"))
                    || "button".equals(component.getString("type"))) {
                continue;
            }
            isVarchar = false;
            sql.append(component.getString("key"));
            sql.append(" ");
            if ("number".equals(component.getString("type"))) {
                sql.append(" bigint");
            } else if ("textfield".equals(component.getString("type"))) {
                sql.append(" varchar");
                isVarchar = true;
            } else if ("checkbox".equals(component.getString("type"))) {
                sql.append(" varchar");
                isVarchar = true;
            } else if ("radio".equals(component.getString("type"))) {
                sql.append(" varchar");
                isVarchar = true;
            } else if ("select".equals(component.getString("type"))) {
                sql.append(" varchar");
                isVarchar = true;
            } else if ("textarea".equals(component.getString("type"))) {
                sql.append(" longtext");
            } else if ("textdate".equals(component.getString("type"))) {
                sql.append(" date");
            } else if ("textdatetime".equals(component.getString("type"))) {
                sql.append(" time");
            } else {
                throw new IllegalArgumentException("不支持的类型");
            }

            if (component.containsKey("validate")) {
                validate = component.getJSONObject("validate");
                if (isVarchar && validate.containsKey("maxLength")) {
                    sql.append("(");
                    sql.append(validate.getIntValue("maxLength"));
                    sql.append(") ");
                } else if (isVarchar) {
                    sql.append("(64) ");
                }
                if (validate.containsKey("required") && validate.getBoolean("required")) {
                    sql.append(" not null ");
                }
            }

            sql.append(" comment '");
            sql.append(component.getString("label"));
            sql.append("'");

            if (componentIndex != components.size() - 1) {
                sql.append(",");
            }
        }
        sql.append(" )");
        logger.debug("部署表单sql" + sql.toString());
        int count = oaWorkflowFormInnerServiceSMOImpl.createTable(sql.toString());
        if (count < 1) { // 已经部署过不用再部署
            throw new IllegalArgumentException("部署表单失败");
        }
    }

    @Override
    @Java110Transactional
    public ResponseEntity<String> saveModel(WorkflowModelDto workflowModelDto) {
        //根据
        OaWorkflowDto oaWorkflowDto = new OaWorkflowDto();
        oaWorkflowDto.setModelId(workflowModelDto.getModelId());
        List<OaWorkflowDto> oaWorkflowDtos = oaWorkflowInnerServiceSMOImpl.queryOaWorkflows(oaWorkflowDto);

        Assert.listOnlyOne(oaWorkflowDtos, "未包含流程");

        OaWorkflowXmlPo oaWorkflowXmlPo = new OaWorkflowXmlPo();
        oaWorkflowXmlPo.setStoreId(oaWorkflowDtos.get(0).getStoreId());
        oaWorkflowXmlPo.setBpmnXml(workflowModelDto.getJson_xml());
        oaWorkflowXmlPo.setFlowId(oaWorkflowDtos.get(0).getFlowId());
        oaWorkflowXmlPo.setSvgXml(workflowModelDto.getSvg_xml());
        //查询部署

        OaWorkflowXmlDto oaWorkflowXmlDto = new OaWorkflowXmlDto();
        oaWorkflowXmlDto.setFlowId(oaWorkflowDtos.get(0).getFlowId());
        oaWorkflowXmlDto.setStoreId(oaWorkflowDtos.get(0).getStoreId());

        List<OaWorkflowXmlDto> oaWorkflowXmlDtos = oaWorkflowXmlInnerServiceSMOImpl.queryOaWorkflowXmls(oaWorkflowXmlDto);
        int flag = 0;
        if (oaWorkflowXmlDtos == null || oaWorkflowXmlDtos.size() < 1) {
            oaWorkflowXmlPo.setXmlId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_xmlId));
            flag = oaWorkflowXmlInnerServiceSMOImpl.saveOaWorkflowXml(oaWorkflowXmlPo);
        } else {
            oaWorkflowXmlPo.setXmlId(oaWorkflowXmlDtos.get(0).getXmlId());
            flag = oaWorkflowXmlInnerServiceSMOImpl.updateOaWorkflowXml(oaWorkflowXmlPo);
        }
        if (flag < 1) {
            throw new IllegalArgumentException("流程图处理失败");
        }

        try {
            Model model = repositoryService.getModel(workflowModelDto.getModelId());
            ObjectNode modelJson = (ObjectNode) objectMapper.readTree(model.getMetaInfo());
            modelJson.put(MODEL_NAME, oaWorkflowDtos.get(0).getFlowName());
            modelJson.put(MODEL_DESCRIPTION, oaWorkflowDtos.get(0).getDescrible());
            modelJson.put(ModelDataJsonConstants.MODEL_REVISION, model.getVersion() + 1);
            model.setMetaInfo(modelJson.toString());
            model.setName(oaWorkflowDtos.get(0).getFlowName());
            repositoryService.saveModel(model);
            repositoryService.addModelEditorSource(model.getId(), workflowModelDto.getJson_xml().getBytes("utf-8"));

            InputStream svgStream = new ByteArrayInputStream(workflowModelDto.getSvg_xml().getBytes("utf-8"));
            TranscoderInput input = new TranscoderInput(svgStream);

            PNGTranscoder transcoder = new PNGTranscoder();
            // Setup output
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            TranscoderOutput output = new TranscoderOutput(outStream);

            // Do the transformation
            transcoder.transcode(input, output);
            final byte[] result = outStream.toByteArray();
            repositoryService.addModelEditorSourceExtra(model.getId(), result);
            outStream.close();

        } catch (Exception e) {
            logger.error("Error saving model", e);
            throw new ActivitiException("Error saving model", e);
        }

        OaWorkflowPo oaWorkflowPo = new OaWorkflowPo();
        oaWorkflowPo.setFlowId(oaWorkflowDtos.get(0).getFlowId());
        oaWorkflowPo.setState(OaWorkflowDto.STATE_WAIT);
        flag = oaWorkflowInnerServiceSMOImpl.updateOaWorkflow(oaWorkflowPo);
        if (flag < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
        }

        return ResultVo.success();
    }

}
