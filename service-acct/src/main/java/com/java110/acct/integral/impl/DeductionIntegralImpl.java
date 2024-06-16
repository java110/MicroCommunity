package com.java110.acct.integral.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.integral.IDeductionIntegral;
import com.java110.dto.integral.DeductionIntegralDto;
import com.java110.intf.job.IMallInnerServiceSMO;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeductionIntegralImpl implements IDeductionIntegral {

    @Autowired
    private IMallInnerServiceSMO mallInnerServiceSMOImpl;


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


        ResultVo resultVo = mallInnerServiceSMOImpl.userIntegralToCommunity(deductionIntegralDto);

        if(resultVo.getCode() != ResultVo.CODE_OK){
            throw new IllegalArgumentException(resultVo.getMsg());
        }

        return 1;
    }
}
