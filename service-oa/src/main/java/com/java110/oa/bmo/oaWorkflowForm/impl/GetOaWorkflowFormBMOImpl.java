package com.java110.oa.bmo.oaWorkflowForm.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.oaWorkflow.OaWorkflowDto;
import com.java110.dto.oaWorkflowForm.OaWorkflowFormDto;
import com.java110.dto.user.UserDto;
import com.java110.entity.audit.AuditUser;
import com.java110.intf.common.IOaWorkflowUserInnerServiceSMO;
import com.java110.intf.oa.IOaWorkflowFormInnerServiceSMO;
import com.java110.intf.oa.IOaWorkflowInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.oa.bmo.oaWorkflowForm.IGetOaWorkflowFormBMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("getOaWorkflowFormBMOImpl")
public class GetOaWorkflowFormBMOImpl implements IGetOaWorkflowFormBMO {

    @Autowired
    private IOaWorkflowFormInnerServiceSMO oaWorkflowFormInnerServiceSMOImpl;

    @Autowired
    private IOaWorkflowInnerServiceSMO oaWorkflowInnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IOaWorkflowUserInnerServiceSMO oaWorkflowUserInnerServiceSMOImpl;

    /**
     * @param oaWorkflowFormDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(OaWorkflowFormDto oaWorkflowFormDto) {


        int count = oaWorkflowFormInnerServiceSMOImpl.queryOaWorkflowFormsCount(oaWorkflowFormDto);

        List<OaWorkflowFormDto> oaWorkflowFormDtos = null;
        if (count > 0) {
            oaWorkflowFormDtos = oaWorkflowFormInnerServiceSMOImpl.queryOaWorkflowForms(oaWorkflowFormDto);
        } else {
            oaWorkflowFormDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) oaWorkflowFormDto.getRow()), count, oaWorkflowFormDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    /**
     * {"schemaVersion":1,"exporter":{"name":"form-js","version":"0.1.0"},
     * "components":[{"text":"# Text","type":"text"},{"key":"textfield2","label":"文本框","type":"textfield"},{"key":"number2","label":"Number","type":"number"},
     * {"key":"checkbox2","label":"Checkbox","type":"checkbox"},
     * {"key":"radio2","label":"Radio","type":"radio","values":[{"label":"Value","value":"value"}]},
     * {"key":"select2","label":"Select","type":"select","values":[{"label":"Value","value":"value"}]},
     * {"text":"# Text","type":"text"},{"key":"textarea1","label":"多行文本框","type":"textarea"},
     * {"key":"textdate1","label":"日期","type":"textdate"},
     * {"key":"textdatetime1","label":"时间","type":"textdatetime"},
     * {"action":"submit","key":"button1","label":"Button","type":"button"}],"type":"default"}
     *
     * @param paramIn
     * @return
     */
    @Override
    public ResponseEntity<String> queryOaWorkflowFormData(Map paramIn) {

        OaWorkflowFormDto oaWorkflowFormDto = new OaWorkflowFormDto();
        oaWorkflowFormDto.setFlowId(paramIn.get("flowId").toString());
        oaWorkflowFormDto.setStoreId(paramIn.get("storeId").toString());
        oaWorkflowFormDto.setRow(1);
        oaWorkflowFormDto.setPage(1);
        List<OaWorkflowFormDto> oaWorkflowFormDtos = oaWorkflowFormInnerServiceSMOImpl.queryOaWorkflowForms(oaWorkflowFormDto);

        Assert.listOnlyOne(oaWorkflowFormDtos, "未包含流程表单，请先设置表单");

        paramIn.put("tableName", oaWorkflowFormDtos.get(0).getTableName());

        int count = oaWorkflowFormInnerServiceSMOImpl.queryOaWorkflowFormDataCount(paramIn);

        List<Map> datas = null;
        if (count > 0) {
            datas = oaWorkflowFormInnerServiceSMOImpl.queryOaWorkflowFormDatas(paramIn);
        } else {
            datas = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (int) paramIn.get("row")), count, datas);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    /**
     * 保存表单数据
     *
     * @param reqJson
     * @return
     */
    @Override
    public ResponseEntity<String> saveOaWorkflowFormData(JSONObject reqJson) {
        OaWorkflowFormDto oaWorkflowFormDto = new OaWorkflowFormDto();
        oaWorkflowFormDto.setFlowId(reqJson.get("flowId").toString());
        oaWorkflowFormDto.setStoreId(reqJson.get("storeId").toString());
        oaWorkflowFormDto.setRow(1);
        oaWorkflowFormDto.setPage(1);
        List<OaWorkflowFormDto> oaWorkflowFormDtos = oaWorkflowFormInnerServiceSMOImpl.queryOaWorkflowForms(oaWorkflowFormDto);
        Assert.listOnlyOne(oaWorkflowFormDtos, "未包含流程表单，请先设置表单");

        //
        OaWorkflowDto oaWorkflowDto = new OaWorkflowDto();
        oaWorkflowDto.setStoreId(reqJson.getString("storeId"));
        oaWorkflowDto.setFlowId(reqJson.getString("flowId"));
        List<OaWorkflowDto> oaWorkflowDtos = oaWorkflowInnerServiceSMOImpl.queryOaWorkflows(oaWorkflowDto);
        Assert.listOnlyOne(oaWorkflowDtos, "流程不存在");

        if (!OaWorkflowDto.STATE_COMPLAINT.equals(oaWorkflowDtos.get(0).getState())) {
            throw new IllegalArgumentException(oaWorkflowDtos.get(0).getFlowName() + "流程未部署");
        }

        if (StringUtil.isEmpty(oaWorkflowDtos.get(0).getProcessDefinitionKey())) {
            throw new IllegalArgumentException(oaWorkflowDtos.get(0).getFlowName() + "流程未部署");
        }

        //查询用户名称
        UserDto userDto = new UserDto();
        userDto.setUserId(reqJson.getString("userId"));
        List<UserDto> userDtos = userInnerServiceSMOImpl.getUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户不存在");

        //保存表单数据
        reqJson.put("id", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_flowId));
        reqJson.put("state", "1001");
        reqJson.put("createUserId", reqJson.getString("userId"));
        reqJson.put("createUserName", userDtos.get(0).getUserName());
        reqJson.put("tableName", oaWorkflowFormDtos.get(0).getTableName());

        int flag = oaWorkflowFormInnerServiceSMOImpl.saveOaWorkflowFormData(reqJson);
        if (flag < 1) {
            throw new IllegalArgumentException("保存失败");
        }

        reqJson.put("processDefinitionKey", oaWorkflowDtos.get(0).getProcessDefinitionKey());
        oaWorkflowUserInnerServiceSMOImpl.startProcess(reqJson);

        return ResultVo.success();
    }

    /**
     * 查询工作流待办
     *
     * @param paramIn
     * @return
     */
    @Override
    public ResponseEntity<String> queryOaWorkflowUserTaskFormData(JSONObject paramIn) {

        OaWorkflowDto oaWorkflowDto = new OaWorkflowDto();
        oaWorkflowDto.setStoreId(paramIn.getString("storeId"));
        oaWorkflowDto.setFlowId(paramIn.getString("flowId"));
        List<OaWorkflowDto> oaWorkflowDtos = oaWorkflowInnerServiceSMOImpl.queryOaWorkflows(oaWorkflowDto);
        Assert.listOnlyOne(oaWorkflowDtos, "流程不存在");

        OaWorkflowFormDto oaWorkflowFormDto = new OaWorkflowFormDto();
        oaWorkflowFormDto.setFlowId(paramIn.get("flowId").toString());
        oaWorkflowFormDto.setStoreId(paramIn.get("storeId").toString());
        oaWorkflowFormDto.setRow(1);
        oaWorkflowFormDto.setPage(1);
        List<OaWorkflowFormDto> oaWorkflowFormDtos = oaWorkflowFormInnerServiceSMOImpl.queryOaWorkflowForms(oaWorkflowFormDto);
        Assert.listOnlyOne(oaWorkflowFormDtos, "未包含流程表单，请先设置表单");

        AuditUser auditUser = new AuditUser();
        auditUser.setProcessDefinitionKey(oaWorkflowDtos.get(0).getProcessDefinitionKey());
        auditUser.setFlowId(paramIn.getString("flowId"));
        auditUser.setUserId(paramIn.getString("userId"));
        auditUser.setStoreId(paramIn.getString("storeId"));
        auditUser.setPage(paramIn.getInteger("page"));
        auditUser.setRow(paramIn.getInteger("row"));

        long count = oaWorkflowUserInnerServiceSMOImpl.getUserTaskCount(auditUser);

        List<JSONObject> datas = null;

        if (count > 0) {
            datas = oaWorkflowUserInnerServiceSMOImpl.getUserTasks(auditUser);
            //刷新 表单数据
            freshFormData(datas, paramIn, oaWorkflowFormDtos.get(0));
        } else {
            datas = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) paramIn.getInteger("row")), count, datas);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
        return responseEntity;
    }

    /**
     * 查询工作流待办
     *
     * @param paramIn
     * @return
     */
    @Override
    public ResponseEntity<String> queryOaWorkflowUserHisTaskFormData(JSONObject paramIn) {

        OaWorkflowDto oaWorkflowDto = new OaWorkflowDto();
        oaWorkflowDto.setStoreId(paramIn.getString("storeId"));
        oaWorkflowDto.setFlowId(paramIn.getString("flowId"));
        List<OaWorkflowDto> oaWorkflowDtos = oaWorkflowInnerServiceSMOImpl.queryOaWorkflows(oaWorkflowDto);
        Assert.listOnlyOne(oaWorkflowDtos, "流程不存在");

        OaWorkflowFormDto oaWorkflowFormDto = new OaWorkflowFormDto();
        oaWorkflowFormDto.setFlowId(paramIn.get("flowId").toString());
        oaWorkflowFormDto.setStoreId(paramIn.get("storeId").toString());
        oaWorkflowFormDto.setRow(1);
        oaWorkflowFormDto.setPage(1);
        List<OaWorkflowFormDto> oaWorkflowFormDtos = oaWorkflowFormInnerServiceSMOImpl.queryOaWorkflowForms(oaWorkflowFormDto);
        Assert.listOnlyOne(oaWorkflowFormDtos, "未包含流程表单，请先设置表单");

        AuditUser auditUser = new AuditUser();
        auditUser.setProcessDefinitionKey(oaWorkflowDtos.get(0).getProcessDefinitionKey());
        auditUser.setFlowId(paramIn.getString("flowId"));
        auditUser.setUserId(paramIn.getString("userId"));
        auditUser.setStoreId(paramIn.getString("storeId"));
        auditUser.setPage(paramIn.getInteger("page"));
        auditUser.setRow(paramIn.getInteger("row"));

        long count = oaWorkflowUserInnerServiceSMOImpl.getUserHistoryTaskCount(auditUser);

        List<JSONObject> datas = null;

        if (count > 0) {
            datas = oaWorkflowUserInnerServiceSMOImpl.getUserHistoryTasks(auditUser);
            //刷新 表单数据
            freshFormData(datas, paramIn, oaWorkflowFormDtos.get(0));
        } else {
            datas = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) paramIn.getInteger("row")), count, datas);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> auditOaWorkflow(JSONObject reqJson) {
        OaWorkflowDto oaWorkflowDto = new OaWorkflowDto();
        oaWorkflowDto.setStoreId(reqJson.getString("storeId"));
        oaWorkflowDto.setFlowId(reqJson.getString("flowId"));
        List<OaWorkflowDto> oaWorkflowDtos = oaWorkflowInnerServiceSMOImpl.queryOaWorkflows(oaWorkflowDto);
        Assert.listOnlyOne(oaWorkflowDtos, "流程不存在");

        OaWorkflowFormDto oaWorkflowFormDto = new OaWorkflowFormDto();
        oaWorkflowFormDto.setFlowId(reqJson.get("flowId").toString());
        oaWorkflowFormDto.setStoreId(reqJson.get("storeId").toString());
        oaWorkflowFormDto.setRow(1);
        oaWorkflowFormDto.setPage(1);
        List<OaWorkflowFormDto> oaWorkflowFormDtos = oaWorkflowFormInnerServiceSMOImpl.queryOaWorkflowForms(oaWorkflowFormDto);
        Assert.listOnlyOne(oaWorkflowFormDtos, "未包含流程表单，请先设置表单");

        //业务办理
        if ("1100".equals(reqJson.getString("auditCode"))) {
            reqJson.put("nextUserId", reqJson.getString("staffId"));
            boolean isLastTask = oaWorkflowUserInnerServiceSMOImpl.completeTask(reqJson);
            if (isLastTask) {
                reqJson.put("state", "1005");
            } else {
                reqJson.put("state", "1002");
            }
            reqJson.put("tableName", oaWorkflowFormDtos.get(0).getTableName());
            oaWorkflowFormInnerServiceSMOImpl.updateOaWorkflowFormData(reqJson);
        } else if ("1300".equals(reqJson.getString("auditCode"))) {
            reqJson.put("nextUserId", reqJson.getString("staffId"));
            oaWorkflowUserInnerServiceSMOImpl.changeTaskToOtherUser(reqJson);
            reqJson.put("state", "1004");
            reqJson.put("tableName", oaWorkflowFormDtos.get(0).getTableName());
            oaWorkflowFormInnerServiceSMOImpl.updateOaWorkflowFormData(reqJson);
        } else if ("1200".equals(reqJson.getString("auditCode"))) {
            reqJson.put("curUserId", reqJson.getString("curUserId"));
            oaWorkflowUserInnerServiceSMOImpl.goBackTask(reqJson);
            reqJson.put("state", "1003");
            reqJson.put("tableName", oaWorkflowFormDtos.get(0).getTableName());
            oaWorkflowFormInnerServiceSMOImpl.updateOaWorkflowFormData(reqJson);
        } else {
            throw new IllegalArgumentException("不支持的类型");
        }


        return ResultVo.success();
    }

    @Override
    public ResponseEntity<String> getNextTask(JSONObject reqJson) {
        List<JSONObject> tasks = oaWorkflowUserInnerServiceSMOImpl.nextAllNodeTaskList(reqJson);
        return ResultVo.createResponseEntity(tasks);
    }

    /**
     * 刷入表单数据
     *
     * @param datas
     */
    private void freshFormData(List<JSONObject> datas, JSONObject paramIn, OaWorkflowFormDto oaWorkflowFormDto) {
        List<String> ids = new ArrayList<>();
        for (JSONObject data : datas) {
            ids.add(data.getString("id"));
        }
        if (ids.size() < 1) {
            return;
        }

        Map paramMap = new HashMap();
        paramMap.put("storeId", paramIn.getString("storeId"));
        paramMap.put("ids", ids.toArray(new String[ids.size()]));
        paramMap.put("tableName", oaWorkflowFormDto.getTableName());
        paramMap.put("page", 1);
        paramMap.put("row", ids.size());
        List<Map> formDatas = oaWorkflowFormInnerServiceSMOImpl.queryOaWorkflowFormDatas(paramMap);

        for (JSONObject data : datas) {
            for (Map form : formDatas) {
                if (data.getString("id").equals(form.get("id"))) {
                    data.putAll(form);
                }
            }
        }
    }

}
