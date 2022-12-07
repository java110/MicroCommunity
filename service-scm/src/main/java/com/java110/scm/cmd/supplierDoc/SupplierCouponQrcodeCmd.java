package com.java110.scm.cmd.supplierDoc;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.doc.annotation.*;
import com.java110.utils.exception.CmdException;

import java.text.ParseException;


@Java110CmdDoc(title = "优惠券二维码",
        description = "调用第三方供应商系统获取优惠券二维码",
        httpMethod = "post",
        url = "admin 账户 供应商 自主设置",
        resource = "scmDoc",
        author = "吴学文",
        serviceCode = "supplier.supplierCouponQrcode"
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "businessKey", length = 30, remark = "业务ID"),
        @Java110ParamDoc(name = "suppilerId", length = 30, remark = "供应商ID"),
        @Java110ParamDoc(name = "couponName", length = 30, remark = "优惠券名称"),
        @Java110ParamDoc(name = "couponId", length = 30, remark = "优惠券ID"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
                @Java110ParamDoc(name = "data", type = "Object", remark = "有效数据"),
                @Java110ParamDoc(parentNodeName = "data",name = "qrCode", type = "String", remark = "二维码内容"),
                @Java110ParamDoc(parentNodeName = "data",name = "remark", type = "String", remark = "核销流程说明"),
        }
)

@Java110ExampleDoc(
        reqBody="{'businessKey':'123123','suppilerId':'123123','couponName':'123123','couponId':'123123'}",
        resBody="{'code':0,'msg':'成功','data':{'qrCode':'123123','remark':7200}}"
)
@Java110Cmd(serviceCode = "supplier.supplierCouponQrcode")
public class SupplierCouponQrcodeCmd extends Cmd{
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

    }
}
