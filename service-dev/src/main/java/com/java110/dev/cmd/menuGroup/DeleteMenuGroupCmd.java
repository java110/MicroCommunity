package com.java110.dev.cmd.menuGroup;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.menu.MenuGroupDto;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Java110Cmd(serviceCode = "menuGroup.deleteMenuGroup")
public class DeleteMenuGroupCmd extends Cmd{
    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        Assert.hasKeyAndValue(reqJson, "gId", "组Id不能为空");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        ResponseEntity<String> responseEntity = null;

        MenuGroupDto menuGroupDto = BeanConvertUtil.covertBean(reqJson, MenuGroupDto.class);


        int saveFlag = menuInnerServiceSMOImpl.deleteMenuGroup(menuGroupDto);

        responseEntity = new ResponseEntity<String>(saveFlag > 0 ? "成功" : "失败", saveFlag > 0 ? HttpStatus.OK : HttpStatus.BAD_REQUEST);

        context.setResponseEntity(responseEntity);
    }
}
