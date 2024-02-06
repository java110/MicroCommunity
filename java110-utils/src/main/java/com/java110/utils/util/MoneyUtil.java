package com.java110.utils.util;

import java.math.BigDecimal;

public class MoneyUtil {

    /**
     *  <option value="1">{{vc.i18n('四舍五入','editFeeConfig')}}</option>
     <option value="3">{{vc.i18n('向上进位','editFeeConfig')}}</option>
     <option value="4">{{vc.i18n('向下进位','editFeeConfig')}}</option>
     */

    public static final String HALF_UP = "1";
    public static final String UP = "3";
    public static final String DOWN = "4";

    /**
     * 四舍五入
     * @param price
     * @param scale
     * @param decimalPlace
     * @return
     */
    public static double computePriceScale(double price,String scale,int decimalPlace){

        //todo 解决 群里反馈 进度丢失问题
        //todo 发现了个BUG
        // MoneyUtil.computePriceScale
        // 计算金额四舍五入时，精度丢失问题，
        // new BigDecimal(String ）
        // 就OK了，  double  会出问题。
        // 例如444.195   四舍五入变成了 44.19
        BigDecimal feeTotalPrice = new BigDecimal(price+"");

        if(DOWN.equals(scale)) {
            feeTotalPrice = feeTotalPrice.setScale(decimalPlace, BigDecimal.ROUND_DOWN);
        }else if(UP.equals(scale)){
            feeTotalPrice = feeTotalPrice.setScale(decimalPlace, BigDecimal.ROUND_UP);
        }else{
            feeTotalPrice = feeTotalPrice.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        }

        return feeTotalPrice.doubleValue();
    }

    /**
     * 四舍五入
     * @param price
     * @return
     */
    public static double computePriceScale(double price){
        return computePriceScale(price,HALF_UP,2);
    }


}
