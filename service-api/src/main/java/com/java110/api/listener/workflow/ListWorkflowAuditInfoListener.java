package com.java110.api.listener.workflow;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.user.UserDto;
import com.java110.dto.workflow.WorkflowAuditInfoDto;
import com.java110.intf.common.IWorkflowInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.utils.constant.ServiceCodeWorkflowConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
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
@Java110Listener("listWorkflowAuditInfoListener")
public class ListWorkflowAuditInfoListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IWorkflowInnerServiceSMO workflowInnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeWorkflowConstant.LIST_WORKFLOW_AUDIT_INFO;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    public IWorkflowInnerServiceSMO getWorkflowInnerServiceSMOImpl() {
        return workflowInnerServiceSMOImpl;
    }

    public void setWorkflowInnerServiceSMOImpl(IWorkflowInnerServiceSMO workflowInnerServiceSMOImpl) {
        this.workflowInnerServiceSMOImpl = workflowInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中请包含小区ID");
        Assert.hasKeyAndValue(reqJson, "businessKey", "请求报文中未包含业务ID");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {


        WorkflowAuditInfoDto workflowAuditInfoDto = BeanConvertUtil.covertBean(reqJson, WorkflowAuditInfoDto.class);
        List<WorkflowAuditInfoDto> workflowAuditInfoDtos = workflowInnerServiceSMOImpl.queryWorkflowAuditHistory(workflowAuditInfoDto);
        freshUserName(workflowAuditInfoDtos);
        ResultVo resultVo = new ResultVo(ResultVo.CODE_OK, ResultVo.MSG_OK, workflowAuditInfoDtos);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }

    private void freshUserName(List<WorkflowAuditInfoDto> workflowAuditInfoDtos) {
        if (workflowAuditInfoDtos == null || workflowAuditInfoDtos.size() < 1) {
            return;
        }

        List<String> userIds = new ArrayList<>();
        for (WorkflowAuditInfoDto tmpWorkflowAuditInfoDto:workflowAuditInfoDtos ){
            userIds.add(tmpWorkflowAuditInfoDto.getUserId());
        }

        UserDto userDto = new UserDto();
        userDto.setUserIds(userIds.toArray(new String[userIds.size()]));
        List<UserDto> userDtos = userInnerServiceSMOImpl.getStaffs(userDto);


        for (WorkflowAuditInfoDto tmpWorkflowAuditInfoDto:workflowAuditInfoDtos ){
            if(StringUtil.isEmpty(tmpWorkflowAuditInfoDto.getUserId())){
                continue;
            }
            for(UserDto tmpUserDto: userDtos){
                if (tmpWorkflowAuditInfoDto.getUserId().equals(tmpUserDto.getUserId())){
                    tmpWorkflowAuditInfoDto.setUserName(tmpUserDto.getUserName());
                    tmpWorkflowAuditInfoDto.setOrgName(tmpUserDto.getOrgName());
                }
            }
        }

    }
}
