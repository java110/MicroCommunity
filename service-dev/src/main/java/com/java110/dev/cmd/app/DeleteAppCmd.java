package com.java110.dev.cmd.app;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.app.AppDto;
import com.java110.intf.community.IAppInnerServiceSMO;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Java110Cmd(serviceCode = "app.deleteApp")
public class DeleteAppCmd extends Cmd {

    @Autowired
    private IAppInnerServiceSMO appInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");
        Assert.hasKeyAndValue(reqJson, "appId", "应用Id不能为空");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        AppDto appDto = BeanConvertUtil.covertBean(reqJson, AppDto.class);
        appDto.setStatusCd(StatusConstant.STATUS_CD_INVALID);
        int count = appInnerServiceSMOImpl.deleteApp(appDto);
        if (count < 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "删除数据失败");
        }
        ResponseEntity<String> responseEntity = new ResponseEntity<String>("", HttpStatus.OK);
        cmdDataFlowContext.setResponseEntity(responseEntity);
    }
}
