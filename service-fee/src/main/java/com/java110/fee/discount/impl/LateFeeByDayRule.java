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
import java.util.Calendar;
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

    private static final String SPEC_DAY = "89002020980018"; // 延迟天数
    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    @Override
    public ComputeDiscountDto compute(FeeDiscountDto feeDiscountDto) {
        List<FeeDiscountSpecDto> feeDiscountSpecDtos = feeDiscountDto.getFeeDiscountSpecs();
        if (feeDiscountSpecDtos.isEmpty()) {
            return null;
        }
        double rate = 0.0;
        int delayDay = 1;
        for (FeeDiscountSpecDto feeDiscountSpecDto : feeDiscountSpecDtos) {
            if (SPEC_RATE.equals(feeDiscountSpecDto.getSpecId())) {
                rate = Double.parseDouble(feeDiscountSpecDto.getSpecValue());
            }
            if (SPEC_DAY.equals(feeDiscountSpecDto.getSpecId())) {
                delayDay = Integer.parseInt(feeDiscountSpecDto.getSpecValue());
            }
        }

        FeeDto feeDto = new FeeDto();
        feeDto.setCommunityId(feeDiscountDto.getCommunityId());
        feeDto.setFeeId(feeDiscountDto.getFeeId());
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

        // Date curTime = DateUtil.getCurrentDate();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, delayDay * -1);
        Date curTime = calendar.getTime();
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

        double discountPrice = 0.0;
        //todo 一次性计算滞纳金
        if (FeeDto.FEE_FLAG_ONCE.equals(feeDtos.get(0).getFeeFlag())) {
            discountPrice = priceDec.multiply(new BigDecimal(rate)).multiply(dayDec).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
        } else {//todo 计算 滞纳金，
            discountPrice = computeLateDiscountPrice(priceDec, feeDtos.get(0), curTime,rate);
        }

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

    private double computeLateDiscountPrice(BigDecimal priceDec, FeeDto feeDto,Date curTime,double rate) {

        //todo 欠费最大结束时间
        Date targetEndDate = computeFeeSMOImpl.getDeadlineTime(feeDto);
        //Date targetEndDate = DateUtil.getDateFromStringB("2023-11-30");

        //todo 周期开始时间
        Date targetStartDate = getTargetStartDate(feeDto);

        double allMonth = DateUtil.dayCompare(feeDto.getEndTime(), targetEndDate);

        int paymentCycle = Integer.parseInt(feeDto.getPaymentCycle());

        double maxCycle = Math.ceil(allMonth / paymentCycle);

        int lateDay = 0;
        BigDecimal discountPriceDec = new BigDecimal("0.00");
        BigDecimal curCycleDiscountPriceDec = null;
        for (int cycleIndex = 0; cycleIndex < maxCycle; cycleIndex++) {
            //todo 计算 违约天数
            lateDay = DateUtil.daysBetween(curTime, targetStartDate);
            curCycleDiscountPriceDec = priceDec.multiply(new BigDecimal(paymentCycle)).multiply(new BigDecimal(rate)).multiply(new BigDecimal(lateDay)).setScale(2, BigDecimal.ROUND_HALF_EVEN);
            System.out.println(DateUtil.getFormatTimeStringB(targetStartDate)+">"+lateDay+":"+curCycleDiscountPriceDec.doubleValue());
            discountPriceDec = discountPriceDec.add(curCycleDiscountPriceDec);
            targetStartDate = DateUtil.stepMonth(targetStartDate,paymentCycle);
        }

        return discountPriceDec.doubleValue();
    }

    private static Date getTargetStartDate(FeeDto feeDto) {


        Calendar startCal = Calendar.getInstance();
        startCal.setTime(feeDto.getStartTime());
        Date targetStartTime = feeDto.getStartTime();

        while (startCal.getTime().before(feeDto.getEndTime())) {
            targetStartTime = startCal.getTime();
            startCal.add(Calendar.MONTH, Integer.parseInt(feeDto.getPaymentCycle()));
        }
        return targetStartTime;
    }

    public static void main(String[] args) throws Exception {
        BigDecimal priceDec = new BigDecimal("89.43");
        FeeDto feeDto = new FeeDto();
        feeDto.setStartTime(DateUtil.getDateFromStringB("2022-01-01"));
        feeDto.setEndTime(DateUtil.getDateFromStringB("2022-01-01"));
        feeDto.setPaymentCycle("1");
//        double discountPrice = computeLateDiscountPrice(priceDec, feeDto, DateUtil.getDateFromStringB("2023-11-07"),0.0005);
//        System.out.println("discountPrice = "+discountPrice);
    }
}
