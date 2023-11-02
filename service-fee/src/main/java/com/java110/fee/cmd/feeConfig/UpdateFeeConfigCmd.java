package com.java110.fee.cmd.feeConfig;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.payFeeRule.PayFeeRuleDto;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.fee.IPayFeeConfigV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeRuleV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeV1InnerServiceSMO;
import com.java110.po.fee.PayFeeConfigPo;
import com.java110.po.fee.PayFeePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Java110Cmd(serviceCode = "feeConfig.updateFeeConfig")
public class UpdateFeeConfigCmd extends Cmd {

    @Autowired
    private IPayFeeConfigV1InnerServiceSMO payFeeConfigV1InnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IPayFeeRuleV1InnerServiceSMO payFeeRuleV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "configId", "费用项ID不能为空");
        Assert.hasKeyAndValue(reqJson, "feeTypeCd", "必填，请选择费用类型");
        Assert.hasKeyAndValue(reqJson, "feeName", "必填，请填写收费项目");
        Assert.hasKeyAndValue(reqJson, "feeFlag", "必填，请选择费用标识");
        Assert.hasKeyAndValue(reqJson, "startTime", "必填，请选择计费起始时间");
        Assert.hasKeyAndValue(reqJson, "endTime", "必填，请选择计费终止时间");
        Assert.hasKeyAndValue(reqJson, "computingFormula", "必填，请填写附加费用");
        Assert.hasKeyAndValue(reqJson, "squarePrice", "必填，请填写计费单价");
        Assert.hasKeyAndValue(reqJson, "additionalAmount", "必填，请填写附加费用");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
        Assert.hasKeyAndValue(reqJson, "billType", "必填，请填写出账类型");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setCommunityId(reqJson.getString("communityId"));
        feeConfigDto.setConfigId(reqJson.getString("configId"));
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
        Assert.listOnlyOne(feeConfigDtos, "未找到该费用项");
        JSONObject businessFeeConfig = new JSONObject();
        businessFeeConfig.putAll(reqJson);
        businessFeeConfig.put("isDefault", feeConfigDtos.get(0).getIsDefault());
        PayFeeConfigPo payFeeConfigPo = BeanConvertUtil.covertBean(businessFeeConfig, PayFeeConfigPo.class);
        int flag = payFeeConfigV1InnerServiceSMOImpl.updatePayFeeConfig(payFeeConfigPo);
        if (flag < 1) {
            throw new CmdException("修改费用项失败");
        }
        cmdDataFlowContext.setResponseEntity(ResultVo.success());
        //todo 修改费用标识
        if (!reqJson.containsKey("feeFlag")) {
            return;
        }
        String feeFlag = reqJson.getString("feeFlag");
        //todo 说明没有修改费用项标识
        if (feeFlag.equals(feeConfigDtos.get(0).getFeeFlag())) {
            return;
        }

        // todo 检查是否为账单模式，也就是在 poy_fee_rule 中是否有数据，这里有数据不让修改
        PayFeeRuleDto payFeeRuleDto = new PayFeeRuleDto();
        payFeeRuleDto.setConfigId(feeConfigDtos.get(0).getConfigId());
        payFeeRuleDto.setCommunityId(reqJson.getString("communityId"));
        int count = payFeeRuleV1InnerServiceSMOImpl.queryPayFeeRulesCount(payFeeRuleDto);
        if (count > 0) {
            return;
        }

        PayFeePo payFeePo = new PayFeePo();
        payFeePo.setConfigId(feeConfigDtos.get(0).getConfigId());
        payFeePo.setFeeFlag(reqJson.getString("feeFlag"));

        payFeeV1InnerServiceSMOImpl.updatePayFee(payFeePo);
    }
}
