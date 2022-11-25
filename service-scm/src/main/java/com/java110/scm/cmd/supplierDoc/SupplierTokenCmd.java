package com.java110.scm.cmd.supplierDoc;


import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.doc.annotation.*;
import com.java110.utils.exception.CmdException;

import java.text.ParseException;

@Java110CmdDoc(title = "调用第三方供应商系统获取token",
        description = "appId 和 appSecure 还有 调用地址可以到admin账户添加供应商时设置",
        httpMethod = "post",
        url = "自主设置",
        resource = "scmDoc",
        author = "吴学文",
        serviceCode = "supplier.getToken"
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "appId", length = 30, remark = "第三方系统APPID"),
        @Java110ParamDoc(name = "appSecure", length = 30, remark = "第三方系统appSecure"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
                @Java110ParamDoc(name = "data", type = "Object", remark = "有效数据"),
                @Java110ParamDoc(parentNodeName = "data",name = "accessToken", type = "String", remark = "token"),
                @Java110ParamDoc(parentNodeName = "data",name = "expiresIn", type = "String", remark = "过期时间（秒）"),
        }
)

@Java110ExampleDoc(
        reqBody="{'appId':'123123','appSecure':'123123'}",
        resBody="{'code':0,'msg':'成功','data':{'accessToken':'123123','expiresIn':7200}}"
)
@Java110Cmd(serviceCode = "supplier.getToken")
public class SupplierTokenCmd extends Cmd{
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

    }
}
