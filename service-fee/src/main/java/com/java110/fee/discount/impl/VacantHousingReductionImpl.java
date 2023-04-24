package com.java110.fee.discount.impl;

import com.java110.dto.fee.ComputeDiscountDto;
import com.java110.dto.fee.FeeDiscountDto;
import com.java110.dto.fee.FeeDiscountSpecDto;
import com.java110.fee.discount.IComputeDiscount;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * 空置房减免规则
 *
 * @author fqz
 * @date 2021-01-04 17:00
 */
@Component(value = "roomReductionManageRule")
public class VacantHousingReductionImpl implements IComputeDiscount {

    /**
     * 89002020980010	102020006	月份
     * 89002020980011	102020006	减免金额
     */

    private static final String SPEC_MONTH = "89002020980010"; //月份
    private static final String SPEC_MONEY = "89002020980011"; // 减免金额

    /**
     * @param feeDiscountDto 费用优惠
     * @return
     */
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
        //判断缴费周期是否小于空置房减免最低缴费月份
        if (feeDiscountDto.getCycles() < month) {
            ComputeDiscountDto computeDiscountDto = new ComputeDiscountDto();
            computeDiscountDto.setDiscountId(feeDiscountDto.getDiscountId());
            computeDiscountDto.setDiscountType(FeeDiscountDto.DISCOUNT_TYPE_DV);
            computeDiscountDto.setRuleId(feeDiscountDto.getRuleId());
            computeDiscountDto.setRuleName(feeDiscountDto.getRuleName());
            computeDiscountDto.setDiscountName(feeDiscountDto.getDiscountName());
            computeDiscountDto.setDiscountPrice(0.0);
            computeDiscountDto.setFeeDiscountSpecs(feeDiscountSpecDtos);
            return computeDiscountDto;
        }
        Double cycle = month / month;
        BigDecimal cycleDec = new BigDecimal(cycle.intValue());
        double discountPrice = cycleDec.multiply(new BigDecimal(money)).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
        ComputeDiscountDto computeDiscountDto = new ComputeDiscountDto();
        computeDiscountDto.setDiscountId(feeDiscountDto.getDiscountId());
        computeDiscountDto.setDiscountType(FeeDiscountDto.DISCOUNT_TYPE_DV);
        computeDiscountDto.setRuleId(feeDiscountDto.getRuleId());
        computeDiscountDto.setRuleName(feeDiscountDto.getRuleName());
        computeDiscountDto.setDiscountName(feeDiscountDto.getDiscountName());
        computeDiscountDto.setDiscountPrice(discountPrice);
        computeDiscountDto.setFeeDiscountSpecs(feeDiscountSpecDtos);
        return computeDiscountDto;
    }
}
