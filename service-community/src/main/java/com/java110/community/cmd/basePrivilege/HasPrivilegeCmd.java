package com.java110.community.cmd.basePrivilege;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.basePrivilege.HasPrivilegeDto;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Java110Cmd(serviceCode = "check.user.hasPrivilege")
public class HasPrivilegeCmd extends Cmd {


    private final static Logger logger = LoggerFactory.getLogger(HasPrivilegeCmd.class);

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "userId", "请求报文中未包含userId节点");
        Assert.hasKeyAndValue(reqJson, "pId", "请求报文中未包含pId节点");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        ResponseEntity<String> responseEntity = null;

        //根据名称查询用户信息
        HasPrivilegeDto hasPrivilegeDto = new HasPrivilegeDto();
        hasPrivilegeDto.setUserId(reqJson.getString("userId"));
        hasPrivilegeDto.setpId(reqJson.getString("pId"));
        List<HasPrivilegeDto> privilegeDtos = menuInnerServiceSMOImpl.hasPrivilege(hasPrivilegeDto);

        if (privilegeDtos == null || privilegeDtos.size() < 1) {
            context.setResponseEntity(ResultVo.error("没有权限操作", HttpStatus.UNAUTHORIZED));
            return;
        }

        responseEntity = new ResponseEntity<String>("成功", HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }
}
