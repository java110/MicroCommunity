package com.java110.acct.cmd.integral;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.doc.annotation.*;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.integral.IntegralRuleConfigDto;
import com.java110.dto.integral.IntegralRuleFeeDto;
import com.java110.intf.acct.IIntegralRuleConfigV1InnerServiceSMO;
import com.java110.intf.acct.IIntegralRuleFeeV1InnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Java110CmdDoc(title = "缴费赠送积分",
        description = "缴费时计算费用 是否有积分",
        httpMethod = "get",
        url = "http://{ip}:{port}/app/integral.computePayFeeIntegral",
        resource = "acctDoc",
        author = "吴学文",
        serviceCode = "integral.computePayFeeIntegral"
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "feeId", length = 30, remark = "费用ID"),
        @Java110ParamDoc(name = "cycle", length = 30, remark = "缴费周期"),
        @Java110ParamDoc(name = "communityId", length = 30, remark = "小区ID"),
        @Java110ParamDoc(name = "amount", length = 30, remark = "缴费金额"),
        @Java110ParamDoc(name = "area", length = 30, remark = "缴费面积"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
                @Java110ParamDoc(name = "data", type = "int", remark = "赠送积分数"),
        }
)

@Java110ExampleDoc(
        reqBody="{'feeId':'123123','cycle':'1','communityId':'123123','amount':'123123','area':'123123'}",
        resBody="{'code':0,'msg':'成功','data':1000}"
)
@Java110Cmd(serviceCode = "integral.computePayFeeIntegral")
public class ComputePayFeeIntegralCmd extends Cmd {

    @Autowired
    private IIntegralRuleFeeV1InnerServiceSMO integralRuleFeeV1InnerServiceSMOImpl;

    @Autowired
    private IIntegralRuleConfigV1InnerServiceSMO integralRuleConfigV1InnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        Assert.hasKeyAndValue(reqJson,"feeId","未包含费用");
        Assert.hasKeyAndValue(reqJson,"communityId","未包含小区");
        Assert.hasKeyAndValue(reqJson,"cycles","未包含缴费周期");
        Assert.hasKeyAndValue(reqJson,"amount","未包含缴费周期");
        Assert.hasKeyAndValue(reqJson,"area","未包含缴费周期");


    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        FeeDto feeDto = new FeeDto();
        feeDto.setFeeId(reqJson.getString("feeId"));
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

        Assert.listOnlyOne(feeDtos,"费用不存在");

        IntegralRuleFeeDto integralRuleFeeDto = new IntegralRuleFeeDto();
        integralRuleFeeDto.setFeeConfigId(feeDtos.get(0).getConfigId());
        integralRuleFeeDto.setCurTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        integralRuleFeeDto.setCommunityId(reqJson.getString("communityId"));
        integralRuleFeeDto.setCycle(reqJson.getString("cycles"));
        List<IntegralRuleFeeDto> integralRuleFeeDtos = integralRuleFeeV1InnerServiceSMOImpl.queryIntegralRuleFees(integralRuleFeeDto);

        if(integralRuleFeeDtos == null || integralRuleFeeDtos.size()<1){
            context.setResponseEntity(ResultVo.createResponseEntity(0));
            return ;
        }

        List<String> ruleIds = new ArrayList<>();
        for(IntegralRuleFeeDto tmpCouponRuleFeeDto: integralRuleFeeDtos){
            ruleIds.add(tmpCouponRuleFeeDto.getRuleId());
        }

        IntegralRuleConfigDto integralRuleConfigDto = new IntegralRuleConfigDto();
        integralRuleConfigDto.setRuleIds(ruleIds.toArray(new String[ruleIds.size()]));
        List<IntegralRuleConfigDto> integralRuleConfigDtos = integralRuleConfigV1InnerServiceSMOImpl.queryIntegralRuleConfigs(integralRuleConfigDto);

        if(integralRuleConfigDtos == null || integralRuleConfigDtos.size() < 1){
            context.setResponseEntity(ResultVo.createResponseEntity(0));
            return ;
        }

        // 计算赠送 积分公式
        long quantity = computeIntegralQuantity(integralRuleConfigDtos,reqJson);
        context.setResponseEntity(ResultVo.createResponseEntity(quantity));
    }

    private long computeIntegralQuantity(List<IntegralRuleConfigDto> integralRuleConfigDtos,JSONObject reqJson) {

        long quantity = 0;
        for(IntegralRuleConfigDto integralRuleConfigDto:integralRuleConfigDtos){
            quantity += computeFeeSMOImpl.computeOneIntegralQuantity(integralRuleConfigDto,reqJson);
        }
        return quantity;
    }


}
