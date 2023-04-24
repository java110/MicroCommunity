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

import com.java110.dto.fee.ComputeDiscountDto;
import com.java110.dto.fee.FeeDiscountDto;
import com.java110.dto.fee.FeeDiscountSpecDto;
import com.java110.fee.discount.IComputeDiscount;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * 减免 规则
 *
 * @desc add by 吴学文 10:27
 */

@Component(value = "reductionFeeRule")
public class ReductionFeeRule implements IComputeDiscount {


    /**
     * 89002020980001	102020001	月份
     * 89002020980002	102020001	打折率
     */
    private static final String SPEC_MONTH = "89002020980003"; //月份
    private static final String SPEC_MONEY = "89002020980004"; // 减免金额


    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;


    @Override
    public ComputeDiscountDto compute(FeeDiscountDto feeDiscountDto) {

        List<FeeDiscountSpecDto> feeDiscountSpecDtos = feeDiscountDto.getFeeDiscountSpecs();

        if (feeDiscountSpecDtos.size() < 1) {
            return null;
        }
        double month = 0.0;
        double money = 0.0;
        for (FeeDiscountSpecDto feeDiscountSpecDto : feeDiscountSpecDtos) {
            if (SPEC_MONTH.equals(feeDiscountSpecDto.getSpecId())) {
                month = Double.parseDouble(feeDiscountSpecDto.getSpecValue());
            }
            if (SPEC_MONEY.equals(feeDiscountSpecDto.getSpecId())) {
                money = Double.parseDouble(feeDiscountSpecDto.getSpecValue());
            }
        }

        if (feeDiscountDto.getCycles() < month) {
            ComputeDiscountDto computeDiscountDto = new ComputeDiscountDto();
            computeDiscountDto.setDiscountId(feeDiscountDto.getDiscountId());
            computeDiscountDto.setDiscountType(FeeDiscountDto.DISCOUNT_TYPE_D);
            computeDiscountDto.setRuleId(feeDiscountDto.getRuleId());
            computeDiscountDto.setRuleName(feeDiscountDto.getRuleName());
            computeDiscountDto.setDiscountName(feeDiscountDto.getDiscountName());
            computeDiscountDto.setDiscountPrice(0.0);
            computeDiscountDto.setFeeDiscountSpecs(feeDiscountSpecDtos);
            return computeDiscountDto;
        }

        Double cycle = feeDiscountDto.getCycles() / month;
        BigDecimal cycleDec = new BigDecimal(cycle.intValue());

        double discountPrice = cycleDec.multiply(new BigDecimal(money)).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();

        ComputeDiscountDto computeDiscountDto = new ComputeDiscountDto();
        computeDiscountDto.setDiscountId(feeDiscountDto.getDiscountId());
        computeDiscountDto.setDiscountType(FeeDiscountDto.DISCOUNT_TYPE_D);
        computeDiscountDto.setRuleId(feeDiscountDto.getRuleId());
        computeDiscountDto.setRuleName(feeDiscountDto.getRuleName());
        computeDiscountDto.setDiscountName(feeDiscountDto.getDiscountName());
        computeDiscountDto.setDiscountPrice(discountPrice);
        computeDiscountDto.setFeeDiscountSpecs(feeDiscountSpecDtos);
        return computeDiscountDto;
    }
}
