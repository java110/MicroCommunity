package com.java110.acct.integral.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.integral.IDeductionIntegral;
import com.java110.dto.integral.DeductionIntegralDto;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.util.StringUtil;
import org.springframework.stereotype.Service;

@Service
public class DeductionIntegralImpl implements IDeductionIntegral {
    @Override
    public int deduction(String orderId) {

        String deductionIntegralDtoStr = CommonCache.getAndRemoveValue("integral_deduction_" + orderId);

        if (StringUtil.isEmpty(deductionIntegralDtoStr)) {
            return 0;

        }
        DeductionIntegralDto deductionIntegralDto = JSONObject.parseObject(deductionIntegralDtoStr, DeductionIntegralDto.class);
        if (deductionIntegralDto == null) {
            return 0;
        }
        return 0;
    }
}
