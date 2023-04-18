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
import com.java110.core.log.LoggerFactory;
import com.java110.dto.oaWorkflow.OaWorkflowDto;
import com.java110.dto.oaWorkflow.OaWorkflowFormDto;
import com.java110.dto.oaWorkflow.OaWorkflowXmlDto;
import com.java110.dto.org.OrgStaffRelDto;
import com.java110.dto.workflow.WorkflowDto;
import com.java110.dto.workflow.WorkflowModelDto;
import com.java110.intf.common.IOaWorkflowActivitiInnerServiceSMO;
import com.java110.intf.oa.IOaWorkflowFormInnerServiceSMO;
import com.java110.intf.oa.IOaWorkflowInnerServiceSMO;
import com.java110.intf.oa.IOaWorkflowXmlInnerServiceSMO;
import com.java110.intf.store.IOrgStaffRelV1InnerServiceSMO;
import com.java110.intf.user.IOrgInnerServiceSMO;
import com.java110.po.oaWorkflow.OaWorkflowPo;
import com.java110.po.oaWorkflowXml.OaWorkflowXmlPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
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
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
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
    private IOrgStaffRelV1InnerServiceSMO orgStaffRelV1InnerServiceSMOImpl;

    @Autowired
    private IOaWorkflowActivitiInnerServiceSMO oaWorkflowActivitiInnerServiceSMOImpl;


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
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "未查询到流程");
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
        String staffId = staffInfo.get("staffId") + "";
        OrgStaffRelDto orgDto = new OrgStaffRelDto();
        if (staffId.startsWith("${")) {
            return ResultVo.createResponseEntity(orgDto);
        }
        orgDto.setStaffId(staffId);

        OrgStaffRelDto orgStaffRelDto = new OrgStaffRelDto();
        orgStaffRelDto.setStaffId(staffId);
        List<OrgStaffRelDto> orgStaffRelDtos = orgStaffRelV1InnerServiceSMOImpl.queryStaffOrgNames(orgStaffRelDto);
        if (orgStaffRelDtos == null || orgStaffRelDtos.size() < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "未查询到员工组织信息");
        }
        orgDto = orgStaffRelDtos.get(0);
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

        //普通流程需要部署 表单 其他类型不需要部署
        if (OaWorkflowDto.FLOW_TYPE_PUBLIC.equals(oaWorkflowDtos.get(0).getFlowType())) {
            //表单 部署
            deployForm(oaWorkflowDtos.get(0));
        }

        String deploymentid = "";
        try {
            Model modelData = repositoryService.getModel(workflowModelDto.getModelId());
            byte[] bpmnBytes = null;
            bpmnBytes = repositoryService.getModelEditorSource(workflowModelDto.getModelId());
            if (bpmnBytes == null || bpmnBytes.length < 1) {
                throw new CmdException("未设置流程");
            }
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

        String formJson = oaWorkflowFormDtos.get(0).getFormJson();

        Assert.isJsonObject(formJson, "表单设计出错，请重新设计");

        JSONObject form = JSONObject.parseObject(formJson);

        JSONArray components = form.getJSONArray("components");
        JSONObject component = null;
        StringBuffer sql = new StringBuffer("create table if not exists ");
        sql.append(oaWorkflowFormDtos.get(0).getTableName());
        sql.append(" (");
        sql.append("id varchar(30) NOT NULL PRIMARY KEY COMMENT '主键ID',");
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
                sql.append(" longtext CHARACTER SET utf8");
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
                }
                if (isVarchar && !validate.containsKey("maxLength")) {
                    sql.append("(64) ");
                }
                if (validate.containsKey("required") && validate.getBoolean("required")) {
                    sql.append(" not null ");
                }
            }
            if (!component.containsKey("validate") && isVarchar) {
                sql.append("(64) ");
            }

            sql.append(" comment '");
            sql.append(component.getString("label"));
            sql.append("',");
        }
        sql.append("store_id varchar(30) not null COMMENT '商户ID',");
        sql.append("create_user_id varchar(30) not null COMMENT '创建人ID',");
        sql.append("create_user_name varchar(64) not null COMMENT '创建人',");
        sql.append("state varchar(12) not null COMMENT '状态 1001 申请 1002 待审核 1003 退回 1004 委托 1005 办结',");
        sql.append("create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',");
        sql.append("status_cd varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效'");
        sql.append(") DEFAULT CHARSET=utf8");
        logger.debug("部署表单sql" + sql.toString());
        oaWorkflowFormInnerServiceSMOImpl.createTable(sql.toString());

//       int count = oaWorkflowFormInnerServiceSMOImpl.createTable(sqlStr);
//        if (count < 1) { // 已经部署过不用再部署
//            throw new IllegalArgumentException("部署表单失败");
//        }
    }

    /**
     * 创建model
     * 微信 17797173942
     *
     * @param workflowModelDto
     * @return
     */
    @Override
    @Java110Transactional
    public ResponseEntity<String> saveModel(WorkflowModelDto workflowModelDto) {
        //根据
        OaWorkflowDto oaWorkflowDto = new OaWorkflowDto();
        oaWorkflowDto.setModelId(workflowModelDto.getModelId());
        List<OaWorkflowDto> oaWorkflowDtos = oaWorkflowInnerServiceSMOImpl.queryOaWorkflows(oaWorkflowDto);

        Assert.listOnlyOne(oaWorkflowDtos, "未包含流程");
        workflowModelDto.setFlowId(oaWorkflowDtos.get(0).getFlowId());

        //这里决定对bpmn xml 文件做过滤处理
        dealBpmnXml(workflowModelDto);

        //业务校验
        businessCheck(oaWorkflowDtos.get(0), workflowModelDto);

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
            model.setKey("java110_" + oaWorkflowDtos.get(0).getFlowId());
            repositoryService.saveModel(model);
            String jsonXml = workflowModelDto.getJson_xml();
            jsonXml = jsonXml.replaceAll("camunda:assignee", "activiti:assignee");
            repositoryService.addModelEditorSource(model.getId(), jsonXml.getBytes("utf-8"));

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


    //业务校验
    private void businessCheck(OaWorkflowDto oaWorkflowDto, WorkflowModelDto workflowModelDto) {
        if (OaWorkflowDto.FLOW_TYPE_VISIT.equals(oaWorkflowDto.getFlowType())) {
            businessFirstAuditCheck(oaWorkflowDto, workflowModelDto);
        }

        if(OaWorkflowDto.FLOW_TYPE_OWNER_SETTLED.equals(oaWorkflowDto.getFlowType())){
            businessFirstAuditCheck(oaWorkflowDto, workflowModelDto);
        }
    }

    /**
     * 第一审批人必须要固定
     *
     * @param oaWorkflowDto
     * @param workflowModelDto
     */
    private void businessFirstAuditCheck(OaWorkflowDto oaWorkflowDto, WorkflowModelDto workflowModelDto) {
        OaWorkflowXmlDto oaWorkflowXmlDto = new OaWorkflowXmlDto();
        oaWorkflowXmlDto.setBpmnXml(workflowModelDto.getJson_xml());
        List<JSONObject> outs = oaWorkflowActivitiInnerServiceSMOImpl.queryFirstAuditStaff(oaWorkflowXmlDto);

        if (outs == null || outs.size() < 1) {
            throw new IllegalArgumentException("提交者之后，第一审批人必须要指定具体审批人");
        }

        String assignee = outs.get(0).getString("assignee");

        if (StringUtil.isEmpty(assignee)) {
            throw new IllegalArgumentException("提交者之后，第一审批人必须要指定具体审批人");
        }

        if (assignee.startsWith("-")) {
            throw new IllegalArgumentException("提交者之后，第一审批人必须要指定具体审批人");
        }

    }

    /**
     * 过滤BpmnXml
     *
     * @param workflowModelDto
     */
    private void dealBpmnXml(WorkflowModelDto workflowModelDto) {
        String bpmnXml = workflowModelDto.getJson_xml();
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(bpmnXml);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
        Element rootElement = doc.getRootElement();
        Element process = rootElement.element("process");
        List<Element> userTasks = process.elements("userTask");
        for (Element userTask : userTasks) {
            Attribute assignee = userTask.attribute("assignee");
            if (assignee == null) {
                userTask.addAttribute("camunda:assignee", "${nextUserId}");
            }
        }

        Attribute activiti = rootElement.attribute("activiti");
        if (activiti == null) {
            rootElement.addAttribute("xmlns:activiti", "http://activiti.org/bpmn");
        }

        Attribute processId = process.attribute("id");
        if (processId == null) {
            workflowModelDto.setJson_xml(rootElement.asXML());
            return;
        }
        String processIdValue = processId.getValue();
        String newXml = rootElement.asXML();
        newXml = newXml.replaceAll(processIdValue, "java110_" + workflowModelDto.getFlowId());


        workflowModelDto.setJson_xml(newXml);
    }

    public static void main(String[] args) {
        String xml = getXml();
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(xml);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }

        Element rootElement = doc.getRootElement();
        Element process = rootElement.element("process");
        List<Element> userTasks = process.elements("userTask");
        for (Element userTask : userTasks) {
            Attribute assignee = userTask.attribute("assignee");
            if (assignee == null) {
                userTask.addAttribute("activiti:assignee", "${createUserId}");
            }
        }
        Attribute activiti = rootElement.attribute("activiti");
        if (activiti == null) {
            rootElement.addAttribute("xmlns:activiti", "http://activiti.org/bpmn");
        }

        Attribute processId = process.attribute("id");
        if (processId == null) {
            return;
        }
        String processIdValue = processId.getValue();
        String newXml = rootElement.asXML();
        newXml = newXml.replaceAll(processIdValue, "你好兄弟");


        System.out.printf("xml=\n" + newXml);


    }

    private static String getXml() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<bpmn2:definitions xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:bpmn2=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\" xmlns:dc=\"http://www.omg.org/spec/DD/20100524/DC\" xmlns:di=\"http://www.omg.org/spec/DD/20100524/DI\" id=\"sample-diagram\" targetNamespace=\"http://bpmn.io/schema/bpmn\" xsi:schemaLocation=\"http://www.omg.org/spec/BPMN/20100524/MODEL BPMN20.xsd\">\n" +
                "  <bpmn2:process id=\"java110_752021081769600001\" name=\"请假流程\" isExecutable=\"true\">\n" +
                "    <bpmn2:sequenceFlow id=\"Flow_1gyk5nu\" sourceRef=\"StartEvent_1\" targetRef=\"Activity_0d63gnp\" />\n" +
                "    <bpmn2:userTask id=\"Activity_0d63gnp\" name=\"测试审核\">\n" +
                "      <bpmn2:incoming>Flow_1gyk5nu</bpmn2:incoming>\n" +
                "      <bpmn2:outgoing>Flow_0vajlrg</bpmn2:outgoing>\n" +
                "    </bpmn2:userTask>\n" +
                "    <bpmn2:endEvent id=\"Event_1wbhsi9\" name=\"结束节点\">\n" +
                "      <bpmn2:incoming>Flow_1pn6kje</bpmn2:incoming>\n" +
                "      <bpmn2:terminateEventDefinition id=\"TerminateEventDefinition_05gt98d\" />\n" +
                "    </bpmn2:endEvent>\n" +
                "    <bpmn2:startEvent id=\"StartEvent_1\" name=\"开始\">\n" +
                "      <bpmn2:outgoing>Flow_1gyk5nu</bpmn2:outgoing>\n" +
                "    </bpmn2:startEvent>\n" +
                "    <bpmn2:task id=\"Activity_0zilrzy\" name=\"优化\">\n" +
                "      <bpmn2:incoming>Flow_0vajlrg</bpmn2:incoming>\n" +
                "      <bpmn2:outgoing>Flow_1pn6kje</bpmn2:outgoing>\n" +
                "    </bpmn2:task>\n" +
                "    <bpmn2:sequenceFlow id=\"Flow_0vajlrg\" sourceRef=\"Activity_0d63gnp\" targetRef=\"Activity_0zilrzy\" />\n" +
                "    <bpmn2:sequenceFlow id=\"Flow_1pn6kje\" sourceRef=\"Activity_0zilrzy\" targetRef=\"Event_1wbhsi9\" />\n" +
                "  </bpmn2:process>\n" +
                "  <bpmndi:BPMNDiagram id=\"BPMNDiagram_1\">\n" +
                "    <bpmndi:BPMNPlane id=\"BPMNPlane_1\" bpmnElement=\"java110_752021081769600001\">\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_1pn6kje_di\" bpmnElement=\"Flow_1pn6kje\">\n" +
                "        <di:waypoint x=\"760\" y=\"370\" />\n" +
                "        <di:waypoint x=\"922\" y=\"370\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_0vajlrg_di\" bpmnElement=\"Flow_0vajlrg\">\n" +
                "        <di:waypoint x=\"600\" y=\"258\" />\n" +
                "        <di:waypoint x=\"630\" y=\"258\" />\n" +
                "        <di:waypoint x=\"630\" y=\"370\" />\n" +
                "        <di:waypoint x=\"660\" y=\"370\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_1gyk5nu_di\" bpmnElement=\"Flow_1gyk5nu\">\n" +
                "        <di:waypoint x=\"448\" y=\"258\" />\n" +
                "        <di:waypoint x=\"500\" y=\"258\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNShape id=\"Activity_1kyok5a_di\" bpmnElement=\"Activity_0d63gnp\">\n" +
                "        <dc:Bounds x=\"500\" y=\"218\" width=\"100\" height=\"80\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Event_09dr7i8_di\" bpmnElement=\"Event_1wbhsi9\">\n" +
                "        <dc:Bounds x=\"922\" y=\"352\" width=\"36\" height=\"36\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"919\" y=\"395\" width=\"43\" height=\"14\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Event_1ng8kt4_di\" bpmnElement=\"StartEvent_1\">\n" +
                "        <dc:Bounds x=\"412\" y=\"240\" width=\"36\" height=\"36\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"419\" y=\"283\" width=\"22\" height=\"14\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Activity_0zilrzy_di\" bpmnElement=\"Activity_0zilrzy\">\n" +
                "        <dc:Bounds x=\"660\" y=\"330\" width=\"100\" height=\"80\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "    </bpmndi:BPMNPlane>\n" +
                "  </bpmndi:BPMNDiagram>\n" +
                "</bpmn2:definitions>";
    }

}
