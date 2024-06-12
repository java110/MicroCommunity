package com.java110.acct.integral.impl;

import com.java110.acct.integral.IGiftIntegral;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.integral.GiftIntegralDto;
import com.java110.intf.acct.IIntegralGiftDetailV1InnerServiceSMO;
import com.java110.po.integral.IntegralGiftDetailPo;
import com.java110.utils.cache.MappingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GiftIntegralImpl implements IGiftIntegral {

    private static final String MALL_DOMAIN = "MALL";

    @Autowired
    private IIntegralGiftDetailV1InnerServiceSMO integralGiftDetailV1InnerServiceSMOImp;

    @Override
    public void gift(GiftIntegralDto giftIntegralDto) {
        String mallSwitch = MappingCache.getValue(MALL_DOMAIN, "MALL_SWITCH");

        if (!"ON".equals(mallSwitch)) {
            return ;
        }



        //先加明细
//        IntegralGiftDetailPo integralGiftDetailPo = new IntegralGiftDetailPo();
//        integralGiftDetailPo.setCommunityId(giftIntegralDto.get());
//        integralGiftDetailPo.setAcctId(accountDto.getAcctId());
//        integralGiftDetailPo.setAcctName(accountDto.getAcctName());
//        integralGiftDetailPo.setAcctDetailId("-1");
//        integralGiftDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId("11"));
//        integralGiftDetailPo.setConfigId(integralRuleConfigDto.getConfigId());
//        integralGiftDetailPo.setConfigName(integralRuleConfigDto.getConfigName());
//        integralGiftDetailPo.setRuleId(integralRuleConfigDto.getRuleId());
//        integralGiftDetailPo.setRuleName(integralRuleConfigDto.getRuleName());
//        integralGiftDetailPo.setQuantity(integralRuleConfigDto.getQuantity());
//        integralGiftDetailPo.setCreateUserId(FeeAttrDto.getFeeAttrValue(feeDto, FeeAttrDto.SPEC_CD_OWNER_ID));
//        integralGiftDetailPo.setUserName(FeeAttrDto.getFeeAttrValue(feeDto, FeeAttrDto.SPEC_CD_OWNER_NAME));
//        integralGiftDetailPo.setTel(FeeAttrDto.getFeeAttrValue(feeDto, FeeAttrDto.SPEC_CD_OWNER_LINK));
//        integralGiftDetailV1InnerServiceSMOImpl.saveIntegralGiftDetail(integralGiftDetailPo);
    }
}
