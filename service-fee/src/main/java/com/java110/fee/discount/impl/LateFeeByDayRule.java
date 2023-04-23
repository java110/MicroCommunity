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
@Component(value = "lateFeeByDayRule")
public class LateFeeByDayRule implements IComputeDiscount {
    /**
     * 89002020980001	102020001	月份
     * 89002020980002	102020001	打折率
     */
    private static final String SPEC_RATE = "89002020980005"; // 打折率


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
        int day = DateUtil.daysBetween(curTime, endTime);

        if (day < 1) {
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

        Map feePriceAll = computeFeeSMOImpl.getFeePrice(feeDtos.get(0));

        BigDecimal priceDec = new BigDecimal(feePriceAll.get("feePrice").toString());

        BigDecimal dayDec = new BigDecimal(day);

        double discountPrice = priceDec.divide(new BigDecimal(30), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(rate)).multiply(dayDec).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();

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

    public static void main(String[] args) throws Exception {
        int day = 316;
        double money = 0.0;
        for (int i = 1; i< day+1;i++){
            money += ((124.51/30 * (i-1) +money)  * 0.003);
            System.out.println("第"+i+"天 违约金 = "+money + ",计算公式为：(124.51/30 * ("+i+"-1) + " + money +") * 0.003");
        }

        System.out.println(money);
    }
}
