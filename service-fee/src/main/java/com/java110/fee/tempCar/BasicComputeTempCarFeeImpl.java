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

@Component(value = "6700012001")
public class BasicComputeTempCarFeeImpl extends BaseComputeTempCarFee {

    //5600012001	免费时间(分钟)
    //5600012002	首段时长(分钟)
    //5600012003	首段时长收费
    //5600012004	超过首段(分钟)
    //5600012005	超过首段收费
    //5600012006	停车超过(分钟)
    //5600012007	每多少分钟
    //5600012008	停车超过收费
    //5600012009	最高收费
    public static final String SPEC_CD_5600012001 = "5600012001";
    public static final String SPEC_CD_5600012002 = "5600012002";
    public static final String SPEC_CD_5600012003 = "5600012003";
    public static final String SPEC_CD_5600012004 = "5600012004";
    public static final String SPEC_CD_5600012005 = "5600012005";
    public static final String SPEC_CD_5600012006 = "5600012006";
    public static final String SPEC_CD_5600012007 = "5600012007";
    public static final String SPEC_CD_5600012008 = "5600012008";
    public static final String SPEC_CD_5600012009 = "5600012009";

    @Override
    public TempCarFeeResult doCompute(CarInoutDto carInoutDto, TempCarFeeConfigDto tempCarFeeConfigDto,
                                      List<TempCarFeeConfigAttrDto> tempCarFeeConfigAttrDtos) {


        //获取停车时间
        long min = TempCarFeeFactory.getTempCarCeilMin(carInoutDto);

        int freeMin = TempCarFeeFactory.getAttrValueInt(tempCarFeeConfigAttrDtos, SPEC_CD_5600012001);

        //最大收费
        double maxFeeMoney = TempCarFeeFactory.getAttrValueDouble(tempCarFeeConfigAttrDtos, SPEC_CD_5600012009);
        //判断 时间是否超过24小时
        double baseMoney = 0.0;

        //免费时间中
        if (min <= freeMin && !TempCarFeeFactory.judgeFinishPayTempCarFee(carInoutDto)) {
            return new TempCarFeeResult(carInoutDto.getCarNum(), 0.0, maxFeeMoney, baseMoney);
        }

        BigDecimal minDeci = new BigDecimal(min);

        //处理超过 一天的数据
        if (min > 24 * 60) {
            BigDecimal dayDeci = minDeci.divide(new BigDecimal(24 * 60),0, BigDecimal.ROUND_DOWN);
            baseMoney = dayDeci.multiply(new BigDecimal(maxFeeMoney)).doubleValue();

            minDeci = minDeci.subtract(dayDeci.multiply(new BigDecimal(24 * 60))).setScale(0, BigDecimal.ROUND_DOWN);
            min = minDeci.intValue();
        }

        // 特殊情况  好几天后刚好 min为0
        if (min == 0) {
            return new TempCarFeeResult(carInoutDto.getCarNum(), 0.0, maxFeeMoney, baseMoney);
        }

        int firstMin = TempCarFeeFactory.getAttrValueInt(tempCarFeeConfigAttrDtos, SPEC_CD_5600012002);
        double firstMoney = TempCarFeeFactory.getAttrValueDouble(tempCarFeeConfigAttrDtos, SPEC_CD_5600012003);
        //在首段时长(分钟)中
        if (min < firstMin) {
            return new TempCarFeeResult(carInoutDto.getCarNum(), firstMoney, maxFeeMoney, baseMoney);
        }

        //超过手段
        int afterMin = TempCarFeeFactory.getAttrValueInt(tempCarFeeConfigAttrDtos, SPEC_CD_5600012004);
        double afterByMoney = TempCarFeeFactory.getAttrValueDouble(tempCarFeeConfigAttrDtos, SPEC_CD_5600012005);

        //超过多少分钟
        int maxMin = TempCarFeeFactory.getAttrValueInt(tempCarFeeConfigAttrDtos, SPEC_CD_5600012006);


        //判断是否超过最大 停车时间
        if (min < maxMin) { //最大停车时间内
            //超过的时间
            BigDecimal afterFirstMin = minDeci.subtract(new BigDecimal(firstMin));
            //时间差 除以 没多少分钟 向上取整
            double money = afterFirstMin.divide(new BigDecimal(afterMin),0, BigDecimal.ROUND_UP)
                    .multiply(new BigDecimal(afterByMoney))
                    .setScale(2, BigDecimal.ROUND_HALF_UP).add(new BigDecimal(firstMoney)).doubleValue();
            return new TempCarFeeResult(carInoutDto.getCarNum(), money, maxFeeMoney, baseMoney);
        }

        //最大停车时间
        BigDecimal maxMinDeci = new BigDecimal(maxMin);
        int afterMaxMin = TempCarFeeFactory.getAttrValueInt(tempCarFeeConfigAttrDtos, SPEC_CD_5600012007);
        double afterMaxByMoney = TempCarFeeFactory.getAttrValueDouble(tempCarFeeConfigAttrDtos, SPEC_CD_5600012008);
        //最大时间减去 首段时间
        BigDecimal afterFirstMin = maxMinDeci.subtract(new BigDecimal(firstMin));
        //时间差 除以 没多少分钟 向上取整
        BigDecimal firstToMaxMoney = afterFirstMin.divide(new BigDecimal(afterMin),0, BigDecimal.ROUND_UP)
                .multiply(new BigDecimal(afterByMoney))
                .setScale(2, BigDecimal.ROUND_HALF_UP).add(new BigDecimal(firstMoney));

        double money = minDeci.subtract(maxMinDeci)
                .divide(new BigDecimal(afterMaxMin),0,BigDecimal.ROUND_UP)
                .multiply(new BigDecimal(afterMaxByMoney))
                .setScale(2, BigDecimal.ROUND_HALF_UP)
                .add(firstToMaxMoney).doubleValue();

        return new TempCarFeeResult(carInoutDto.getCarNum(), money, maxFeeMoney, baseMoney);
    }

}
