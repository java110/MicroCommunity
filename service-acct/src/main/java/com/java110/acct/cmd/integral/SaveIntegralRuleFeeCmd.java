/*
 * Copyright 2017-2020 吴学文 and java110 team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.java110.acct.cmd.integral;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.integral.IntegralRuleFeeDto;
import com.java110.intf.acct.IIntegralRuleFeeV1InnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.po.integralRuleFee.IntegralRuleFeePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 类表述：保存
 * 服务编码：integralRuleFee.saveIntegralRuleFee
 * 请求路劲：/app/integralRuleFee.SaveIntegralRuleFee
 * add by 吴学文 at 2022-12-11 22:15:21 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "integral.saveIntegralRuleFee")
public class SaveIntegralRuleFeeCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveIntegralRuleFeeCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IIntegralRuleFeeV1InnerServiceSMO integralRuleFeeV1InnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "ruleId", "请求报文中未包含ruleId");
        Assert.hasKeyAndValue(reqJson, "feeConfigId", "请求报文中未包含feeConfigId");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "payStartTime", "请求报文中未包含payStartTime");
        Assert.hasKeyAndValue(reqJson, "payEndTime", "请求报文中未包含payEndTime");
        Assert.hasKeyAndValue(reqJson, "payMonth", "请求报文中未包含payMonth");
        IntegralRuleFeeDto integralRuleFeeDto = new IntegralRuleFeeDto();
        integralRuleFeeDto.setRuleId(reqJson.getString("ruleId"));
        integralRuleFeeDto.setFeeConfigId(reqJson.getString("feeConfigId"));
        long count = integralRuleFeeV1InnerServiceSMOImpl.queryIntegralRuleFeesCount(integralRuleFeeDto);

        if(count > 0){
            throw new CmdException("费用项已经关联");
        }
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setConfigId(reqJson.getString("feeConfigId"));
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);

        Assert.listOnlyOne(feeConfigDtos,"费用项不存在");
        IntegralRuleFeePo integralRuleFeePo = BeanConvertUtil.covertBean(reqJson, IntegralRuleFeePo.class);
        integralRuleFeePo.setIrfId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        integralRuleFeePo.setFeeConfigName(feeConfigDtos.get(0).getFeeName());
        int flag = integralRuleFeeV1InnerServiceSMOImpl.saveIntegralRuleFee(integralRuleFeePo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
