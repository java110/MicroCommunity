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
package com.java110.utils.util;

/**
 * @desc add by 吴学文 17:14
 */
public class Money2ChineseUtil {
    // 大写数字
    private static final String[] NUMBERS = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
    // 整数部分的单位
    private static final String[] IUNIT = {"元", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "万", "拾", "佰", "仟"};
    // 小数部分的单位
    private static final String[] DUNIT = {"角", "分", "厘"};


    /**
     * 转换为大写的中文金额
     *
     * @param price 字符串类型的 金额数字
     * @return
     */
    public static String toChineseChar(double price) {
        // 判断输入的金额字符串是否符合要求
        String str = price + "";
        if (StringUtil.isEmpty(str) || !str.matches("(-)?[\\d]*(.)?[\\d]*")) {
            return "抱歉，请输入数字！";
        }

        if ("0".equals(str) || "0.00".equals(str) || "0.0".equals(str)) {
            return "零元";
        }

        // 判断金额数字中是否存在负号"-"
        boolean flag = false;
        if (str.startsWith("-")) {
            // 标志位，标志此金额数字为负数
            flag = true;
            str = str.replaceAll("-", "");
        }

        // 去掉金额数字中的逗号","
        str = str.replaceAll(",", "");
        String integerStr;//整数部分数字
        String decimalStr;//小数部分数字

        // 初始化：分离整数部分和小数部分
        if (str.indexOf(".") > 0) {
            integerStr = str.substring(0, str.indexOf("."));
            decimalStr = str.substring(str.indexOf(".") + 1);
        } else if (str.indexOf(".") == 0) {
            integerStr = "";
            decimalStr = str.substring(1);
        } else {
            integerStr = str;
            decimalStr = "";
        }

        // beyond超出计算能力，直接返回
        if (integerStr.length() > IUNIT.length) {
            return "超出计算能力！";
        }

        // 整数部分数字
        int[] integers = toIntArray(integerStr);
        // 判断整数部分是否存在输入012的情况
        if (integers.length > 1 && integers[0] == 0) {
            return "抱歉，输入数字不符合要求！";
        }
        // 设置万单位
        boolean isWan = isWan5(integerStr);
        // 小数部分数字
        int[] decimals = toIntArray(decimalStr);
        // 返回最终的大写金额
        String result = getChineseInteger(integers, isWan) + getChineseDecimal(decimals);
        if (flag) {
            // 如果是负数，加上"负"
            return "负" + result;
        } else {
            return result;
        }
    }

    /**
     * 将字符串转为int数组
     *
     * @param number 数字
     * @return
     */
    private static int[] toIntArray(String number) {
        int[] array = new int[number.length()];
        for (int i = 0; i < number.length(); i++) {
            array[i] = Integer.parseInt(number.substring(i, i + 1));
        }
        return array;
    }

    /**
     * 将整数部分转为大写的金额
     *
     * @param integers 整数部分数字
     * @param isWan    整数部分是否已经是达到【万】
     * @return
     */
    public static String getChineseInteger(int[] integers, boolean isWan) {
        StringBuffer chineseInteger = new StringBuffer("");
        int length = integers.length;
        if (length == 1 && integers[0] == 0) {
            return "";
        }
        for (int i = 0; i < length; i++) {
            String key = "";
            if (integers[i] == 0) {
                if ((length - i) == 13)//万（亿）
                    key = IUNIT[4];
                else if ((length - i) == 9) {//亿
                    key = IUNIT[8];
                } else if ((length - i) == 5 && isWan) {//万
                    key = IUNIT[4];
                } else if ((length - i) == 1) {//元
                    key = IUNIT[0];
                }
                if ((length - i) > 1 && integers[i + 1] != 0) {
                    key += NUMBERS[0];
                }
            }
            chineseInteger.append(integers[i] == 0 ? key : (NUMBERS[integers[i]] + IUNIT[length - i - 1]));
        }
        return chineseInteger.toString();
    }

    /**
     * 将小数部分转为大写的金额
     *
     * @param decimals 小数部分的数字
     * @return
     */
    private static String getChineseDecimal(int[] decimals) {
        StringBuffer chineseDecimal = new StringBuffer("");
        for (int i = 0; i < decimals.length; i++) {
            if (i == 3) {
                break;
            }
            chineseDecimal.append(decimals[i] == 0 ? "" : (NUMBERS[decimals[i]] + DUNIT[i]));
        }
        return chineseDecimal.toString();
    }

    /**
     * 判断当前整数部分是否已经是达到【万】
     *
     * @param integerStr 整数部分数字
     * @return
     */
    private static boolean isWan5(String integerStr) {
        int length = integerStr.length();
        if (length > 4) {
            String subInteger = "";
            if (length > 8) {
                subInteger = integerStr.substring(length - 8, length - 4);
            } else {
                subInteger = integerStr.substring(0, length - 4);
            }
            return Integer.parseInt(subInteger) > 0;
        } else {
            return false;
        }
    }


    // Test
    public static void main(String[] args) {
        double number = 3462.98;
        System.out.println(number + ": " + toChineseChar(number));


    }
}
