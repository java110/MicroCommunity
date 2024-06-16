package com.java110.acct.integral.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.integral.IComputeDeductionIntegral;
import com.java110.dto.MallDataDto;
import com.java110.dto.integral.DeductionIntegralDto;
import com.java110.dto.integral.GiftIntegralDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.job.IMallInnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComputeDeductionIntegralImpl implements IComputeDeductionIntegral {

    private static final String MALL_DOMAIN = "MALL";

    @Autowired
    private IMallInnerServiceSMO mallInnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Override
    public DeductionIntegralDto deduction(String userId, String orderId, String communityId) {

        String mallSwitch = MappingCache.getValue(MALL_DOMAIN, "MALL_SWITCH");

        if (!"ON".equals(mallSwitch)) {
            return new DeductionIntegralDto(0, 0);
        }

        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        if (ListUtil.isNull(userDtos)) {
            return new DeductionIntegralDto(0, 0);
        }


        JSONObject reqJson = new JSONObject();
        reqJson.put("link", userDtos.get(0).getTel());
        ResultVo resultVo = mallInnerServiceSMOImpl.postMallData(new MallDataDto("queryAppUserIntegralBmoImpl", reqJson));

        if (resultVo.getCode() != ResultVo.CODE_OK) {
            return new DeductionIntegralDto(0, 0);
        }


        JSONObject data = reqJson.getJSONObject("data");


        DeductionIntegralDto deductionIntegralDto = new DeductionIntegralDto(data.getIntValue("integral"), data.getDoubleValue("integralMoney"));
        deductionIntegralDto.setLink(userDtos.get(0).getTel());
        deductionIntegralDto.setCommunityId(communityId);
        CommonCache.setValue("integral_deduction_" + orderId, JSONObject.toJSONString(deductionIntegralDto), CommonCache.PAY_DEFAULT_EXPIRE_TIME);
        return deductionIntegralDto;
    }
}
