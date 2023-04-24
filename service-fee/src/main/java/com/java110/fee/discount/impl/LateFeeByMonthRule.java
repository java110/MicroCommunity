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
package com.java110.fee.discount.impl;

import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.fee.ComputeDiscountDto;
import com.java110.dto.fee.FeeDiscountDto;
import com.java110.dto.fee.FeeDiscountSpecDto;
import com.java110.fee.discount.IComputeDiscount;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.utils.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @desc add by 吴学文 12:43
 */
@Component(value = "lateFeeByMonthRule")
public class LateFeeByMonthRule implements IComputeDiscount {
    /**
     * 89002020980001	102020001	月份
     * 89002020980002	102020001	打折率
     */
    private static final String SPEC_RATE = "89002020980006"; // 打折率


    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    @Override
    public ComputeDiscountDto compute(FeeDiscountDto feeDiscountDto) {
        List<FeeDiscountSpecDto> feeDiscountSpecDtos = feeDiscountDto.getFeeDiscountSpecs();
        if (feeDiscountSpecDtos.size() < 1) {
            return null;
        }
        double rate = 0.0;
        for (FeeDiscountSpecDto feeDiscountSpecDto : feeDiscountSpecDtos) {
            if (SPEC_RATE.equals(feeDiscountSpecDto.getSpecId())) {
                rate = Double.parseDouble(feeDiscountSpecDto.getSpecValue());
            }
        }

        FeeDto feeDto = new FeeDto();
        feeDto.setCommunityId(feeDiscountDto.getCommunityId());
        feeDto.setFeeId(feeDiscountDto.getFeeId());
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

        Date curTime = DateUtil.getCurrentDate();

        Date endTime = feeDtos.get(0).getEndTime();

        if (endTime.getTime() > curTime.getTime()) {
            ComputeDiscountDto computeDiscountDto = new ComputeDiscountDto();
            computeDiscountDto.setDiscountId(feeDiscountDto.getDiscountId());
            computeDiscountDto.setDiscountType(FeeDiscountDto.DISCOUNT_TYPE_V);
            computeDiscountDto.setRuleId(feeDiscountDto.getRuleId());
            computeDiscountDto.setRuleName(feeDiscountDto.getRuleName());
            computeDiscountDto.setDiscountName(feeDiscountDto.getDiscountName());
            computeDiscountDto.setDiscountPrice(0.0);
            computeDiscountDto.setFeeDiscountSpecs(feeDiscountSpecDtos);
            return computeDiscountDto;
        }
        //查询费用
        Double month = computeFeeSMOImpl.dayCompare(endTime, curTime);

        Map feePriceAll = computeFeeSMOImpl.getFeePrice(feeDtos.get(0));

        BigDecimal priceDec = new BigDecimal(feePriceAll.get("feePrice").toString());

        BigDecimal monthDec = new BigDecimal(month);

        double discountPrice = priceDec.multiply(new BigDecimal(rate)).multiply(monthDec).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();

        ComputeDiscountDto computeDiscountDto = new ComputeDiscountDto();
        computeDiscountDto.setDiscountId(feeDiscountDto.getDiscountId());
        computeDiscountDto.setDiscountType(FeeDiscountDto.DISCOUNT_TYPE_V);
        computeDiscountDto.setRuleId(feeDiscountDto.getRuleId());
        computeDiscountDto.setRuleName(feeDiscountDto.getRuleName());
        computeDiscountDto.setDiscountName(feeDiscountDto.getDiscountName());
        computeDiscountDto.setDiscountPrice(discountPrice * -1);
        computeDiscountDto.setFeeDiscountSpecs(feeDiscountSpecDtos);
        return computeDiscountDto;
    }
}
