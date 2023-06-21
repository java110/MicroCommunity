package com.java110.common.cmd.machine;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.doc.annotation.*;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;

import java.text.ParseException;



@Java110CmdDoc(title = "门禁二维码核验接口",
        description = "主要用于门禁机刷二维码时调用该接口核验,<br/>" +
                "请求其他接口时 头信息中需要加 Authorization: Bearer token ，<br/>" +
                "token 是这个接口返回的内容<br/> " +
                "会话保持为2小时，请快要到2小时时，再次登录，保持会话</br>",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/machine.accessControlQrcodeVerification",
        resource = "commonDoc",
        author = "吴学文",
        serviceCode = "machine.accessControlQrcodeVerification",
        seq = 1
)

@Java110ParamsDoc(
        headers = {
                @Java110HeaderDoc(name="APP-ID",defaultValue = "通过dev账户分配应用",description = "应用APP-ID"),
                @Java110HeaderDoc(name="TRANSACTION-ID",defaultValue = "uuid",description = "交易流水号"),
                @Java110HeaderDoc(name="REQ-TIME",defaultValue = "20220917120915",description = "请求时间 YYYYMMDDhhmmss"),
                @Java110HeaderDoc(name="JAVA110-LANG",defaultValue = "zh-cn",description = "语言中文"),
                @Java110HeaderDoc(name="USER-ID",defaultValue = "-1",description = "调用用户ID 一般写-1"),
        },
        params = {
                @Java110ParamDoc(name = "qrCode", length = 30, remark = "二维码"),
                @Java110ParamDoc(name = "machineCode", length = 30, remark = "设备编码"),
        })

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
                @Java110ParamDoc(name = "data", type = "Object", remark = "有效数据"),
                @Java110ParamDoc(parentNodeName = "data",name = "userName", type = "String", remark = "用户名称"),
        }
)

@Java110ExampleDoc(
        reqBody="{'qrCode':'wuxw','machineCode':'admin'}",
        resBody="{'code':0,'msg':'成功','data':{'userName':'123123'}}"
)
/**
 * 门禁二维码核验接口
 */
@Java110Cmd(serviceCode = "machine.accessControlQrcodeVerification")
public class AccessControlQrcodeVerificationCmd extends Cmd {
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        Assert.hasKeyAndValue(reqJson, "qrCode", "未包含二维码信息");
        Assert.hasKeyAndValue(reqJson, "machineCode", "未包含设备编码");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

    }
}
