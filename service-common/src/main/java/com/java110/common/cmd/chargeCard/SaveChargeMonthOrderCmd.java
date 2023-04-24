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
package com.java110.common.cmd.chargeCard;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.chargeMonthCard.ChargeMonthCardDto;
import com.java110.dto.chargeMonthOrder.ChargeMonthOrderDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.acct.IIntegralConfigV1InnerServiceSMO;
import com.java110.intf.common.IChargeMonthCardV1InnerServiceSMO;
import com.java110.intf.common.IChargeMonthOrderV1InnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.chargeMonthOrder.ChargeMonthOrderPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * 类表述：保存
 * 服务编码：chargeMonthOrder.saveChargeMonthOrder
 * 请求路劲：/app/chargeMonthOrder.SaveChargeMonthOrder
 * add by 吴学文 at 2023-04-24 10:21:05 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "chargeCard.saveChargeMonthOrder")
public class SaveChargeMonthOrderCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveChargeMonthOrderCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IChargeMonthOrderV1InnerServiceSMO chargeMonthOrderV1InnerServiceSMOImpl;

    @Autowired
    private IChargeMonthCardV1InnerServiceSMO chargeMonthCardV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "cardId", "请求报文中未包含cardId");
        Assert.hasKeyAndValue(reqJson, "personTel", "请求报文中未包含personTel");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "primeRate", "请求报文中未包含primeRate");
        Assert.hasKeyAndValue(reqJson, "receivedAmount", "请求报文中未包含receivedAmount");

        //校验月卡是否 存在
        ChargeMonthCardDto chargeMonthCardDto = new ChargeMonthCardDto();
        chargeMonthCardDto.setCardId(reqJson.getString("cardId"));
        chargeMonthCardDto.setCommunityId(reqJson.getString("communityId"));
        List<ChargeMonthCardDto> chargeMonthCardDtos = chargeMonthCardV1InnerServiceSMOImpl.queryChargeMonthCards(chargeMonthCardDto);

        Assert.listOnlyOne(chargeMonthCardDtos, "月卡不存在");
        reqJson.put("receivableAmount", chargeMonthCardDtos.get(0).getCardPrice());
        reqJson.put("cardMonth", chargeMonthCardDtos.get(0).getCardMonth());
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        ChargeMonthOrderDto chargeMonthOrderDto = new ChargeMonthOrderDto();
        chargeMonthOrderDto.setPersonTel(reqJson.getString("personTel"));
        chargeMonthOrderDto.setQueryTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        List<ChargeMonthOrderDto> chargeMonthOrderDtos = chargeMonthOrderV1InnerServiceSMOImpl.queryChargeMonthOrders(chargeMonthOrderDto);
        Date startTime = null;
        if (chargeMonthOrderDtos == null || chargeMonthOrderDtos.size() < 1) {
            startTime = DateUtil.getCurrentDate();
        } else {
            startTime = DateUtil.getDateFromStringA(chargeMonthOrderDtos.get(0).getEndTime());
        }

        String endDate = DateUtil.getAddMonthStringA(startTime, reqJson.getIntValue("cardMonth"));

        ChargeMonthOrderPo chargeMonthOrderPo = BeanConvertUtil.covertBean(reqJson, ChargeMonthOrderPo.class);
        chargeMonthOrderPo.setOrderId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        chargeMonthOrderPo.setStartTime(DateUtil.getFormatTimeString(startTime, DateUtil.DATE_FORMATE_STRING_A));
        chargeMonthOrderPo.setEndTime(endDate);

        chargeMonthOrderPo.setPersonName(getPersonName(reqJson));
        int flag = chargeMonthOrderV1InnerServiceSMOImpl.saveChargeMonthOrder(chargeMonthOrderPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }

    private String getPersonName(JSONObject reqJson) {

        // todo 业主用 手机号查询
        OwnerDto tmpOwnerDto = new OwnerDto();
        tmpOwnerDto.setLink(reqJson.getString("personTel"));
        tmpOwnerDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(tmpOwnerDto);
        if (ownerDtos != null && ownerDtos.size() > 0) {
            return ownerDtos.get(0).getName();
        }

        //todo 非业主是游客
        UserDto userDto = new UserDto();
        userDto.setTel(reqJson.getString("personTel"));
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        if (userDtos != null && userDtos.size() > 0) {
            return userDtos.get(0).getName();
        }
        throw new CmdException("业主不存在");
    }
}
