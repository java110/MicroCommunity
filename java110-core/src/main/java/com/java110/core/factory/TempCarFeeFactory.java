package com.java110.core.factory;


import com.java110.dto.machine.CarInoutDto;
import com.java110.dto.tempCarFeeConfig.TempCarFeeConfigAttrDto;
import com.java110.utils.util.DateUtil;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * 临时车 费用 工厂类
 */
public class TempCarFeeFactory {

    /**
     * 判断 用户是支付完成
     *
     * @param carInoutDto
     * @return
     */
    public static boolean judgeFinishPayTempCarFee(CarInoutDto carInoutDto) {

        //不是支付完成 状态
        if (!CarInoutDto.STATE_PAY.equals(carInoutDto.getState())) {
            return false;
        }

        //支付时间是否超过15分钟
        Date payTime = null;
        try {
            payTime = DateUtil.getDateFromString(carInoutDto.getPayTime(), DateUtil.DATE_FORMATE_STRING_A);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date nowTime = DateUtil.getCurrentDate();
        //支付完成超过15分钟
        if (nowTime.getTime() - payTime.getTime() > 15 * 60 * 1000) {
            return false;
        }
        return true;
    }

    /**
     * 判断 用户是支付完成
     *
     * @param carInoutDto
     * @return
     */
    public static long getTempCarCeilMin(CarInoutDto carInoutDto) {

        //支付时间是否超过15分钟
        Date payTime = null;
        double min = 0.0;
        try {
            //不是支付完成 状态
            if (CarInoutDto.STATE_PAY.equals(carInoutDto.getState()) || CarInoutDto.STATE_REPAY.equals(carInoutDto.getState())) {

                try {
                    payTime = DateUtil.getDateFromString(carInoutDto.getPayTime(), DateUtil.DATE_FORMATE_STRING_A);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                payTime = DateUtil.getDateFromString(carInoutDto.getInTime(), DateUtil.DATE_FORMATE_STRING_A);
            }
            Date nowTime = DateUtil.getCurrentDate();
            //支付完成超过15分钟
            min = (nowTime.getTime() - payTime.getTime()) / (60 * 1000* 1.00);

            return new Double(Math.ceil(min)).longValue();
            //return (nowTime.getTime() - payTime.getTime()) / (60 * 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 判断 用户是支付完成
     *
     * @param carInoutDto
     * @return
     */
    public static long getTempCarMin(CarInoutDto carInoutDto) {

        //支付时间是否超过15分钟
        Date payTime = null;
        double min = 0.0;
        try {
            //不是支付完成 状态
            if (CarInoutDto.STATE_PAY.equals(carInoutDto.getState())) {

                try {
                    payTime = DateUtil.getDateFromString(carInoutDto.getPayTime(), DateUtil.DATE_FORMATE_STRING_A);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                payTime = DateUtil.getDateFromString(carInoutDto.getInTime(), DateUtil.DATE_FORMATE_STRING_A);
            }
            Date nowTime = DateUtil.getCurrentDate();
            //支付完成超过15分钟

            return (nowTime.getTime() - payTime.getTime()) / (60 * 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static double getAttrValueDouble(List<TempCarFeeConfigAttrDto> tempCarFeeConfigAttrDtos, String specCd) {

        for (TempCarFeeConfigAttrDto tempCarFeeConfigAttrDto : tempCarFeeConfigAttrDtos) {
            if (tempCarFeeConfigAttrDto.getSpecCd().equals(specCd)) {
                return Double.parseDouble(tempCarFeeConfigAttrDto.getValue());
            }
        }

        return 0.0;
    }


    public static String getAttrValueString(List<TempCarFeeConfigAttrDto> tempCarFeeConfigAttrDtos, String specCd) {
        for (TempCarFeeConfigAttrDto tempCarFeeConfigAttrDto : tempCarFeeConfigAttrDtos) {
            if (tempCarFeeConfigAttrDto.getSpecCd().equals(specCd)) {
                return tempCarFeeConfigAttrDto.getValue();
            }
        }
        return "";
    }

    public static int getAttrValueInt(List<TempCarFeeConfigAttrDto> tempCarFeeConfigAttrDtos, String specCd) {
        for (TempCarFeeConfigAttrDto tempCarFeeConfigAttrDto : tempCarFeeConfigAttrDtos) {
            if (tempCarFeeConfigAttrDto.getSpecCd().equals(specCd)) {
                return Integer.parseInt(tempCarFeeConfigAttrDto.getValue());
            }
        }
        return 0;
    }
}
