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
package com.java110.common.cmd.marketGoods;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.market.MarketGoodsItemDto;
import com.java110.dto.market.MarketRuleCommunityDto;
import com.java110.dto.market.MarketRuleObjDto;
import com.java110.dto.market.MarketRuleWayDto;
import com.java110.intf.common.*;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 类表述：查询
 * 服务编码：marketGoods.listMarketGoods
 * 请求路劲：/app/marketGoods.ListMarketGoods
 * add by 吴学文 at 2022-10-25 10:58:15 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "marketGoods.listCommunityMarketGoods")
public class ListCommunityMarketGoodsCmd extends Cmd {

  private static Logger logger = LoggerFactory.getLogger(ListCommunityMarketGoodsCmd.class);
    @Autowired
    private IMarketGoodsV1InnerServiceSMO marketGoodsV1InnerServiceSMOImpl;

    @Autowired
    private IMarketRuleCommunityV1InnerServiceSMO marketRuleCommunityV1InnerServiceSMOImpl;

    @Autowired
    private IMarketTextV1InnerServiceSMO marketTextV1InnerServiceSMOImpl;

    @Autowired
    private IMarketRuleObjV1InnerServiceSMO marketRuleObjV1InnerServiceSMOImpl;

    @Autowired
    private IMarketRuleWayV1InnerServiceSMO marketRuleWayV1InnerServiceSMOImpl;

    @Autowired
    private IMarketGoodsItemV1InnerServiceSMO marketGoodsItemV1InnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson,"communityId","未包含小区");
        Assert.hasKeyAndValue(reqJson,"objType","未包含类型");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        MarketRuleCommunityDto marketRuleCommunityDto = new MarketRuleCommunityDto();
        marketRuleCommunityDto.setCommunityId(reqJson.getString("communityId"));
        List<MarketRuleCommunityDto> marketRuleCommunityDtos = marketRuleCommunityV1InnerServiceSMOImpl.queryMarketRuleCommunitys(marketRuleCommunityDto);

        if(marketRuleCommunityDtos == null || marketRuleCommunityDtos.size()<1){
            return;
        }

        List<String> ruleIds = new ArrayList<>();
        for(MarketRuleCommunityDto tmpMarketRuleCommunityDto: marketRuleCommunityDtos){
            ruleIds.add(tmpMarketRuleCommunityDto.getRuleId());
        }

        MarketRuleObjDto marketRuleObjDto = new MarketRuleObjDto();
        marketRuleObjDto.setRuleIds(ruleIds.toArray(new String[ruleIds.size()]));
        marketRuleObjDto.setObjType(reqJson.getString("objType"));
        long count = marketRuleObjV1InnerServiceSMOImpl.queryMarketRuleObjsCount(marketRuleObjDto);

        if(count <1){
            return ;
        }


        MarketRuleWayDto marketRuleWayDto = new MarketRuleWayDto();
        marketRuleWayDto.setRuleIds(ruleIds.toArray(new String[ruleIds.size()]));
        marketRuleWayDto.setWayType(MarketRuleWayDto.WAY_TYPE_GOODS);
        List<MarketRuleWayDto> marketRuleWayDtos = marketRuleWayV1InnerServiceSMOImpl.queryMarketRuleWays(marketRuleWayDto);

        if(marketRuleWayDtos == null || marketRuleWayDtos.size()<1){
            return;
        }

        MarketGoodsItemDto  marketGoodsItemDto = new MarketGoodsItemDto();
        marketGoodsItemDto.setGoodsId(marketRuleWayDtos.get(0).getWayObjId());

        List<MarketGoodsItemDto> marketGoodsItemDtos = marketGoodsItemV1InnerServiceSMOImpl.queryMarketGoodsItems(marketGoodsItemDto);

        ResultVo resultVo = new ResultVo(marketGoodsItemDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        cmdDataFlowContext.setResponseEntity(responseEntity);
    }
}
