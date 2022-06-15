package com.java110.fee.cmd.feeConfig;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.fee.IPayFeeConfigV1InnerServiceSMO;
import com.java110.po.fee.PayFeeConfigPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

@Java110Cmd(serviceCode = "feeConfig.saveFeeConfig")
public class SaveFeeConfigCmd extends Cmd {

    @Autowired
    private IPayFeeConfigV1InnerServiceSMO payFeeConfigV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "feeTypeCd", "必填，请选择费用类型");
        Assert.hasKeyAndValue(reqJson, "feeName", "必填，请填写收费项目");
        Assert.hasKeyAndValue(reqJson, "feeFlag", "必填，请选择费用标识");
        Assert.hasKeyAndValue(reqJson, "startTime", "必填，请选择计费起始时间");
        Assert.hasKeyAndValue(reqJson, "endTime", "必填，请选择计费终止时间");
        Assert.hasKeyAndValue(reqJson, "computingFormula", "必填，请填写附加费用");
        Assert.hasKeyAndValue(reqJson, "squarePrice", "必填，请填写计费单价");
        Assert.hasKeyAndValue(reqJson, "additionalAmount", "必填，请填写附加费用");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
        Assert.hasKeyAndValue(reqJson, "billType", "未包含出账类型");
        Assert.hasKeyAndValue(reqJson, "paymentCd", "付费类型不能为空");
        Assert.hasKeyAndValue(reqJson, "paymentCycle", "缴费周期不能为空");


    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        reqJson.put("configId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_configId));
        reqJson.put("isDefault", "F");
        PayFeeConfigPo payFeeConfigPo = BeanConvertUtil.covertBean(reqJson, PayFeeConfigPo.class);

        int flag = payFeeConfigV1InnerServiceSMOImpl.savePayFeeConfig(payFeeConfigPo);

        if (flag < 1) {
            throw new CmdException("保存费用项失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
