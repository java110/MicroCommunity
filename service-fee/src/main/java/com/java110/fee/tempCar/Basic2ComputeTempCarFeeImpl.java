package com.java110.fee.tempCar;

import com.java110.core.factory.TempCarFeeFactory;
import com.java110.dto.fee.TempCarFeeResult;
import com.java110.dto.machine.CarInoutDto;
import com.java110.dto.tempCarFeeConfig.TempCarFeeConfigAttrDto;
import com.java110.dto.tempCarFeeConfig.TempCarFeeConfigDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * 标准临时车费用计算
 */

@Component(value = "6700012005")
public class Basic2ComputeTempCarFeeImpl extends BaseComputeTempCarFee {

    //5600012010	免费时间(分钟)
//5600012011	超过免费(分钟)
//5600012012	超过免费收费
//5600012013	首段最高时间(分钟)
//5600012014	首段最高收费
//5600012015	超过首段(分钟)
//5600012016	超过首段收费
//5600012017	最高时间(分钟)
//5600012018	最高收费
    public static final String SPEC_CD_5600012010 = "5600012010";
    public static final String SPEC_CD_5600012011 = "5600012011";
    public static final String SPEC_CD_5600012012 = "5600012012";
    public static final String SPEC_CD_5600012013 = "5600012013";
    public static final String SPEC_CD_5600012014 = "5600012014";
    public static final String SPEC_CD_5600012015 = "5600012015";
    public static final String SPEC_CD_5600012016 = "5600012016";
    public static final String SPEC_CD_5600012017 = "5600012017";
    public static final String SPEC_CD_5600012018 = "5600012018";

    @Override
    public TempCarFeeResult doCompute(CarInoutDto carInoutDto, TempCarFeeConfigDto tempCarFeeConfigDto,
                                      List<TempCarFeeConfigAttrDto> tempCarFeeConfigAttrDtos) {


        //获取停车时间
        long min = TempCarFeeFactory.getTempCarCeilMin(carInoutDto);

        //免费分钟
        int freeMin = TempCarFeeFactory.getAttrValueInt(tempCarFeeConfigAttrDtos, SPEC_CD_5600012010);

        //最大收费
        double maxFeeMoney = TempCarFeeFactory.getAttrValueDouble(tempCarFeeConfigAttrDtos, SPEC_CD_5600012018);
        //判断 时间是否超过24小时
        double baseMoney = 0.0;

        double curMoney = 0.0;

        //免费时间中
        if (min <= freeMin && !TempCarFeeFactory.judgeFinishPayTempCarFee(carInoutDto)) {
            return new TempCarFeeResult(carInoutDto.getCarNum(), 0.0, maxFeeMoney, baseMoney);
        }

        BigDecimal minDeci = new BigDecimal(min);

        //处理超过 一天的数据
        if (min > 24 * 60) {
            BigDecimal dayDeci = minDeci.divide(new BigDecimal(24 * 60), 0, BigDecimal.ROUND_DOWN);
            baseMoney = dayDeci.multiply(new BigDecimal(maxFeeMoney)).doubleValue();

            minDeci = minDeci.subtract(dayDeci.multiply(new BigDecimal(24 * 60))).setScale(0, BigDecimal.ROUND_DOWN);
            min = minDeci.intValue();
        }

        // 特殊情况  好几天后刚好 min为0
        if (min == 0) {
            return new TempCarFeeResult(carInoutDto.getCarNum(), 0.0, maxFeeMoney, baseMoney);
        }

        int firstMin = TempCarFeeFactory.getAttrValueInt(tempCarFeeConfigAttrDtos, SPEC_CD_5600012011);
        double firstMoney = TempCarFeeFactory.getAttrValueDouble(tempCarFeeConfigAttrDtos, SPEC_CD_5600012012);

        //首段最高收费
        double firstMaxMoney = TempCarFeeFactory.getAttrValueInt(tempCarFeeConfigAttrDtos, SPEC_CD_5600012014);
        int firstMaxMin = TempCarFeeFactory.getAttrValueInt(tempCarFeeConfigAttrDtos, SPEC_CD_5600012013);


        minDeci = new BigDecimal(min);
        if (min > firstMaxMin) {
            curMoney = firstMaxMoney;
        } else {
            curMoney = minDeci.divide(new BigDecimal(firstMin), 0, BigDecimal.ROUND_UP).multiply(new BigDecimal(firstMoney)).doubleValue();
            if (curMoney > firstMaxMoney) {
                curMoney = firstMaxMoney;
            }
            return new TempCarFeeResult(carInoutDto.getCarNum(), curMoney, maxFeeMoney, baseMoney);
        }

        int secondMin = TempCarFeeFactory.getAttrValueInt(tempCarFeeConfigAttrDtos, SPEC_CD_5600012015);
        double secondMoney = TempCarFeeFactory.getAttrValueDouble(tempCarFeeConfigAttrDtos, SPEC_CD_5600012016);

        //首段最高收费
        double secondMaxMoney = TempCarFeeFactory.getAttrValueInt(tempCarFeeConfigAttrDtos, SPEC_CD_5600012018);
        int secondMaxMin = TempCarFeeFactory.getAttrValueInt(tempCarFeeConfigAttrDtos, SPEC_CD_5600012017);
        if (min > secondMaxMin) {
            //curMoney = new BigDecimal(curMoney).add(new BigDecimal(secondMaxMoney)).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
            curMoney = secondMaxMoney;
            return new TempCarFeeResult(carInoutDto.getCarNum(), curMoney, maxFeeMoney, baseMoney);
        } else {
            minDeci = new BigDecimal(min).subtract(new BigDecimal(firstMaxMin));
            curMoney = new BigDecimal(curMoney).add(
                    minDeci.divide(new BigDecimal(secondMin), 0, BigDecimal.ROUND_UP).multiply(new BigDecimal(secondMoney))
            ).doubleValue();

            if (curMoney > secondMaxMoney) {
                curMoney = secondMaxMoney;
            }
            return new TempCarFeeResult(carInoutDto.getCarNum(), curMoney, maxFeeMoney, baseMoney);
        }
    }
}
