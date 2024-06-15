package com.java110.acct.integral.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.integral.IComputeGiftIntegral;
import com.java110.dto.integral.GiftIntegralDto;
import com.java110.dto.integral.IntegralRuleConfigDto;
import com.java110.dto.integral.IntegralRuleFeeDto;
import com.java110.intf.acct.IIntegralRuleConfigV1InnerServiceSMO;
import com.java110.intf.acct.IIntegralRuleFeeV1InnerServiceSMO;
import com.java110.intf.job.IMallInnerServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 计算赠送积分
 */
@Service
public class ComputeGiftIntegralImpl implements IComputeGiftIntegral {
    private static final String MALL_DOMAIN = "MALL";

    @Autowired
    private IMallInnerServiceSMO mallInnerServiceSMOImpl;

    @Autowired
    private IIntegralRuleFeeV1InnerServiceSMO integralRuleFeeV1InnerServiceSMOImpl;


    @Autowired
    private IIntegralRuleConfigV1InnerServiceSMO integralRuleConfigV1InnerServiceSMOImpl;


    @Override
    public GiftIntegralDto gift(double payMoney, int month, String communityId) {

        String mallSwitch = MappingCache.getValue(MALL_DOMAIN, "MALL_SWITCH");

        if (!"ON".equals(mallSwitch)) {
            return new GiftIntegralDto(0, 0, communityId);
        }

        IntegralRuleFeeDto integralRuleFeeDto = new IntegralRuleFeeDto();
        integralRuleFeeDto.setCurTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        integralRuleFeeDto.setCommunityId(communityId);
        List<IntegralRuleFeeDto> integralRuleFeeDtos = integralRuleFeeV1InnerServiceSMOImpl.queryIntegralRuleFees(integralRuleFeeDto);

        if (ListUtil.isNull(integralRuleFeeDtos)) {
            return new GiftIntegralDto(0, 0, communityId);
        }

        List<String> ruleIds = new ArrayList<>();
        for (IntegralRuleFeeDto tmpCouponRuleFeeDto : integralRuleFeeDtos) {
            ruleIds.add(tmpCouponRuleFeeDto.getRuleId());
        }

        IntegralRuleConfigDto integralRuleConfigDto = new IntegralRuleConfigDto();
        integralRuleConfigDto.setRuleIds(ruleIds.toArray(new String[ruleIds.size()]));
        List<IntegralRuleConfigDto> integralRuleConfigDtos = integralRuleConfigV1InnerServiceSMOImpl.queryIntegralRuleConfigs(integralRuleConfigDto);

        if (ListUtil.isNull(integralRuleConfigDtos)) {
            return new GiftIntegralDto(0, 0, communityId);
        }

        int quantity = computeOneIntegralQuantity(integralRuleConfigDtos.get(0), payMoney, month);

        if (quantity <= 0) {
            return new GiftIntegralDto(0, 0, communityId);
        }

        double money = mallInnerServiceSMOImpl.computeIntegralMoney(quantity);


        return new GiftIntegralDto(quantity, money, communityId,
                integralRuleFeeDtos.get(0).getRuleId(),
                integralRuleConfigDtos.get(0).getRuleName(),
                integralRuleConfigDtos.get(0).getConfigId(),
                integralRuleConfigDtos.get(0).getConfigName()
                );
    }

    public int computeOneIntegralQuantity(IntegralRuleConfigDto integralRuleConfigDto, double payMoney, int month) {
        String computingFormula = integralRuleConfigDto.getComputingFormula();
        int amount = 0;
        if (IntegralRuleConfigDto.COMPUTING_FORMULA_MONEY.equals(computingFormula)) { // 满金额送积分
            if (payMoney >= Double.parseDouble(integralRuleConfigDto.getSquarePrice())) {
                amount = Integer.parseInt(integralRuleConfigDto.getAdditionalAmount());
            }
        } else if (IntegralRuleConfigDto.COMPUTING_FORMULA_MONTH.equals(computingFormula)) { // 满月送积分
            if (month >= Double.parseDouble(integralRuleConfigDto.getSquarePrice())) {
                amount = Integer.parseInt(integralRuleConfigDto.getAdditionalAmount());
            }
        }
        return amount;
    }
}
