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

        BigDecimal feeTotalPrice = new BigDecimal(price);

        if(DOWN.equals(scale)) {
            feeTotalPrice = feeTotalPrice.setScale(decimalPlace, BigDecimal.ROUND_DOWN);
        }else if(UP.equals(scale)){
            feeTotalPrice = feeTotalPrice.setScale(decimalPlace, BigDecimal.ROUND_UP);
        }else{
            feeTotalPrice = feeTotalPrice.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        }

        return feeTotalPrice.doubleValue();
    }


}
