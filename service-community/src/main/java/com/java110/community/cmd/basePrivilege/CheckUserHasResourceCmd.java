package com.java110.community.cmd.basePrivilege;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@Java110Cmd(serviceCode = "basePrivilege.CheckUserHasResourceListener")
public class CheckUserHasResourceCmd extends Cmd {
    private final static Logger logger = LoggerFactory.getLogger(CheckUserHasResourceCmd.class);

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        logger.debug("请求信息：{}", reqJson);
        ResponseEntity<String> responseEntity = null;
        BasePrivilegeDto basePrivilegeDto = new BasePrivilegeDto();
        basePrivilegeDto.setResource(reqJson.getString("resource"));
        basePrivilegeDto.setUserId(reqJson.getString("userId"));
        List<Map> privileges = menuInnerServiceSMOImpl.checkUserHasResource(basePrivilegeDto);

        JSONObject data = new JSONObject();
        data.put("privileges", privileges);
        responseEntity = new ResponseEntity<String>(data.toJSONString(), HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }
}
