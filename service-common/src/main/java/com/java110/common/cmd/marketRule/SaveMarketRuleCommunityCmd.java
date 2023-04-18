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
package com.java110.common.cmd.marketRule;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.market.MarketRuleCommunityDto;
import com.java110.intf.common.IMarketRuleCommunityV1InnerServiceSMO;
import com.java110.intf.community.ICommunityV1InnerServiceSMO;
import com.java110.po.marketRuleCommunity.MarketRuleCommunityPo;
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
 * 服务编码：marketRuleCommunity.saveMarketRuleCommunity
 * 请求路劲：/app/marketRuleCommunity.SaveMarketRuleCommunity
 * add by 吴学文 at 2022-10-26 20:49:45 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "marketRule.saveMarketRuleCommunity")
public class SaveMarketRuleCommunityCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveMarketRuleCommunityCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IMarketRuleCommunityV1InnerServiceSMO marketRuleCommunityV1InnerServiceSMOImpl;

    @Autowired
    private ICommunityV1InnerServiceSMO communityV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "ruleId", "请求报文中未包含ruleId");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");


        MarketRuleCommunityDto marketRuleCommunityDto = new MarketRuleCommunityDto();
        marketRuleCommunityDto.setCommunityId(reqJson.getString("communityId"));
        marketRuleCommunityDto.setRuleId(reqJson.getString("ruleId"));
        List<MarketRuleCommunityDto> marketRuleCommunityDtos = marketRuleCommunityV1InnerServiceSMOImpl.queryMarketRuleCommunitys(marketRuleCommunityDto);

        if(marketRuleCommunityDtos != null && marketRuleCommunityDtos.size() > 0){
            throw new CmdException("小区已经授权");
        }

    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(reqJson.getString("communityId"));
        List<CommunityDto> communityDtos = communityV1InnerServiceSMOImpl.queryCommunitys(communityDto);

        Assert.listOnlyOne(communityDtos,"小区不存在");

        MarketRuleCommunityPo marketRuleCommunityPo = BeanConvertUtil.covertBean(reqJson, MarketRuleCommunityPo.class);
        marketRuleCommunityPo.setRcId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        marketRuleCommunityPo.setCommunityName(communityDtos.get(0).getName());
        int flag = marketRuleCommunityV1InnerServiceSMOImpl.saveMarketRuleCommunity(marketRuleCommunityPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
