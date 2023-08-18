package com.java110.fee.cmd.oweFeeCallable;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;

import java.text.ParseException;

/**
 * 登记催缴记录
 */
@Java110Cmd(serviceCode = "oweFeeCallable.writeOweFeeCallable")
public class WriteOweFeeCallableCmd extends Cmd {


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        Assert.hasKeyAndValue(reqJson,"communityId","未包含小区");
        Assert.hasKeyAndValue(reqJson,"roomId","未包含房屋");

        JSONArray feeIds = reqJson.getJSONArray("feeIds");

        if(feeIds == null || feeIds.size() < 1){
            throw new CmdException("未包含费用");
        }

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        JSONArray feeIds = reqJson.getJSONArray("feeIds");

        for(int feeIndex = 0; feeIndex < feeIds.size(); feeIndex++){
            doWriteFee(feeIds.get(feeIndex),reqJson);
        }



    }

    private void doWriteFee(Object o, JSONObject reqJson) {
    }
}
