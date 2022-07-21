package com.java110.dev.cmd.menu;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.menu.MenuDto;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Java110Cmd(serviceCode = "menu.updateMenu")
public class UpdateMenuCmd extends Cmd {

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "mId", "菜单ID不能为空");
        Assert.hasKeyAndValue(reqJson, "name", "必填，请填写菜单名称");
        Assert.hasKeyAndValue(reqJson, "url", "必填，请菜单菜单地址");
        Assert.hasKeyAndValue(reqJson, "seq", "必填，请填写序列");
        Assert.hasKeyAndValue(reqJson, "isShow", "必填，请选择是否显示菜单");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        MenuDto menuDto = BeanConvertUtil.covertBean(reqJson, MenuDto.class);


        int count = menuInnerServiceSMOImpl.updateMenu(menuDto);

        if (count < 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "修改数据失败");
        }


        context.setResponseEntity(ResultVo.success());
    }
}
