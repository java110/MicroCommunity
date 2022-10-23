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
package com.java110.common.cmd.marketSms;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.common.IMarketSmsV1InnerServiceSMO;
import com.java110.intf.common.IMarketSmsValueV1InnerServiceSMO;
import com.java110.po.marketSms.MarketSmsPo;
import com.java110.po.marketSmsValue.MarketSmsValuePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类表述：保存
 * 服务编码：marketSms.saveMarketSms
 * 请求路劲：/app/marketSms.SaveMarketSms
 * add by 吴学文 at 2022-10-23 17:43:58 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "marketSms.saveMarketSms")
public class SaveMarketSmsCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveMarketSmsCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IMarketSmsV1InnerServiceSMO marketSmsV1InnerServiceSMOImpl;

    @Autowired
    private IMarketSmsValueV1InnerServiceSMO marketSmsValueV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "smsName", "请求报文中未包含smsName");
        Assert.hasKeyAndValue(reqJson, "smsType", "请求报文中未包含smsType");
        Assert.hasKeyAndValue(reqJson, "remark", "请求报文中未包含remark");

        if (!reqJson.containsKey("smsTypeValues")) {
            throw new CmdException("未包含配置信息");
        }

        JSONArray smsTypeValues = reqJson.getJSONArray("smsTypeValues");

        if (smsTypeValues == null || smsTypeValues.size() < 1) {
            throw new CmdException("未包含配置信息");
        }

        for (int typeIndex = 0; typeIndex < smsTypeValues.size(); typeIndex++) {
            if (StringUtil.isEmpty(smsTypeValues.getJSONObject(typeIndex).getString("smsValue"))) {
                throw new CmdException("未填写" + smsTypeValues.getJSONObject(typeIndex).getString("name"));
            }
        }

    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        MarketSmsPo marketSmsPo = BeanConvertUtil.covertBean(reqJson, MarketSmsPo.class);
        marketSmsPo.setSmsId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        int flag = marketSmsV1InnerServiceSMOImpl.saveMarketSms(marketSmsPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        JSONArray smsTypeValues = reqJson.getJSONArray("smsTypeValues");

        JSONObject value = null;

        MarketSmsValuePo marketSmsValuePo = null;
        for (int typeIndex = 0; typeIndex < smsTypeValues.size(); typeIndex++) {
            value = smsTypeValues.getJSONObject(typeIndex);
            marketSmsValuePo = BeanConvertUtil.covertBean(value, MarketSmsValuePo.class);
            marketSmsValuePo.setValueId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
            marketSmsValuePo.setSmsId(marketSmsPo.getSmsId());
            flag = marketSmsValueV1InnerServiceSMOImpl.saveMarketSmsValue(marketSmsValuePo);
            if (flag < 1) {
                throw new CmdException("保存数据失败");
            }
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
