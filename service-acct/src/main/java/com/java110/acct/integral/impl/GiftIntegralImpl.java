package com.java110.acct.integral.impl;

import com.java110.acct.integral.IGiftIntegral;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.integral.GiftIntegralDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.acct.IIntegralGiftDetailV1InnerServiceSMO;
import com.java110.intf.job.IMallInnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.integral.IntegralGiftDetailPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GiftIntegralImpl implements IGiftIntegral {

    private static final String MALL_DOMAIN = "MALL";

    @Autowired
    private IIntegralGiftDetailV1InnerServiceSMO integralGiftDetailV1InnerServiceSMOImpl;



    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IMallInnerServiceSMO mallInnerServiceSMOImpl;

    @Override
    public void gift(GiftIntegralDto giftIntegralDto) {
        String mallSwitch = MappingCache.getValue(MALL_DOMAIN, "MALL_SWITCH");

        if (!"ON".equals(mallSwitch)) {
            return ;
        }

        //todo 没有用户就不送了
        if(StringUtil.isEmpty(giftIntegralDto.getUserId())){
            return ;
        }

        UserDto userDto = new UserDto();
        userDto.setUserId(giftIntegralDto.getUserId());
        List<UserDto> userDtos= userV1InnerServiceSMOImpl.queryUsers(userDto);
        if(ListUtil.isNull(userDtos)){
            return ;
        }

        //调用商城送用户积分 通过手机号赠
        giftIntegralDto.setLink(userDtos.get(0).getTel());
        giftIntegralDto.setRemark("物业缴费赠送积分");

        mallInnerServiceSMOImpl.sendUserIntegral(giftIntegralDto);


        //先加明细
        IntegralGiftDetailPo integralGiftDetailPo = new IntegralGiftDetailPo();
        integralGiftDetailPo.setCommunityId(giftIntegralDto.getCommunityId());
        integralGiftDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId("11"));
        integralGiftDetailPo.setConfigId(giftIntegralDto.getConfigId());
        integralGiftDetailPo.setConfigName(giftIntegralDto.getConfigName());
        integralGiftDetailPo.setRuleId(giftIntegralDto.getRuleId());
        integralGiftDetailPo.setRuleName(giftIntegralDto.getRuleName());
        integralGiftDetailPo.setQuantity(giftIntegralDto.getIntegral()+"");
        integralGiftDetailPo.setCreateUserId(giftIntegralDto.getUserId());
        integralGiftDetailPo.setUserName(userDtos.get(0).getName());
        integralGiftDetailPo.setTel(userDtos.get(0).getTel());
        integralGiftDetailV1InnerServiceSMOImpl.saveIntegralGiftDetail(integralGiftDetailPo);
    }
}
