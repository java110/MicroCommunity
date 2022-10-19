package com.java110.job.cmd.export;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;

import java.text.ParseException;

/**
 * 数据导出处理类
 */
@Java110Cmd(serviceCode = "export.exportData")
public class ExportDataCmd extends Cmd {
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "communityId", "请求中未包含小区");
        Assert.hasKeyAndValue(reqJson, "pagePath", "请求中未包含页面");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {



    }
}
