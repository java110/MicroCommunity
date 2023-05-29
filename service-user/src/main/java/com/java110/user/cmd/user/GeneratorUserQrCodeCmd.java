package com.java110.user.cmd.user;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.doc.annotation.*;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;


@Java110CmdDoc(title = "生成用户二维码",
        description = "生成用户的唯一二维码信息",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/user.generatorUserQrCode",
        resource = "userDoc",
        author = "吴学文",
        serviceCode = "user.generatorUserQrCode",
        seq = 10
)


@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
                @Java110ParamDoc(name = "data", type = "String", length = 250, defaultValue = "二维码", remark = "二维码"),
        }
)

@Java110ExampleDoc(
        reqBody = "{}",
        resBody = "{'code':0,'msg':'成功','data':'sdsfsdf'}"
)

@Java110Cmd(serviceCode = "user.generatorUserQrCode")
public class GeneratorUserQrCodeCmd extends Cmd {

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        String userId = context.getReqHeaders().get("user-id");

        String qrCode = userV1InnerServiceSMOImpl.generatorUserIdQrCode(userId);

        context.setResponseEntity(ResultVo.createResponseEntity(qrCode));
    }
}
