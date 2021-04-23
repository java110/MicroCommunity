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

import java.math.BigDecimal;

/**
 * @desc add by 吴学文 17:14
 */
public class Money2ChineseUtil {
    /*
eg:640 409 329
 987 654 321  //对应的坐标

 &&& &&& &&&  //替换成对应的字符 (如果是0加上标记)
 --- --- ---  //每位修辞符常量
      *       //每隔5位数字修辞符常量

*/
    //数字转中文常量
    private final static String[] C_N = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
    //每隔5位数字修辞符常量
    private final static String[] C_F = {"", "万", "亿", "兆", "吉", "太", "拍", "艾"};
    //对应的每位修辞符常量
    private final static String[] C_S = {"", "拾", "佰", "仟"};
    private final static String[] CNY = {"元", "角", "分", "毫", "厘"};
    //"-"
    private final static String negative = "负";
    //"+"
    private final static String positive = "";

    /**
     * 将整数转换成对应的中文字符串
     *
     * @param l
     * @return StringBuffed
     */
    public static String toChineseChar(long l) {
        //定义变量保存中文字符串
        StringBuilder sb = new StringBuilder();
        //将阿拉伯数字转换为无符号的字符串数组
        char[] ch = (Math.abs(l) + "").toCharArray();
        //加上符号
        sb.append(l < 0 ? negative : positive);
        //定义一个标记,判断该数位前面是否有'0'
        boolean beforeHaveZore = false;
        //遍历中文字符串
        for (int i = 0, j = ch.length - 1; i < ch.length; i++, j--) {
            //如果首位是1且是十位就不用加'壹'了:eg 15 拾伍   150213 拾伍万零贰佰壹叁  150013 拾伍万零壹拾叁 拾亿零壹拾万/拾亿零拾万
            if (i == 0 && ch[i] - 48 == 1 && j % 4 == 1) {
                sb.append(C_S[1]);
                //判断这个数字是否为0且它前面没有0
            } else if (ch[i] != 48 && !beforeHaveZore)
                //中文常量 + 对应的每位修辞符常量 + 每隔4位数字修辞符常量
                sb.append(C_N[ch[i] - 48] + C_S[j % 4] + C_F[j % 4 == 0 ? j / 4 : 0]);
                //判断这个数字是否为0且它前面有0
            else if (ch[i] != 48 && beforeHaveZore) {
                //中文常量"零"+中文常量+对应的每位修辞符常量+每隔4位数字修辞符常量
                sb.append(C_N[0] + C_N[ch[i] - 48] + C_S[j % 4] + C_F[j % 4 == 0 ? j / 4 : 0]);
                //消出标记
                beforeHaveZore = false;
                //这个数字是0
            } else {
                //如果这个数的位置不是第5位?标记:不标记
                beforeHaveZore = beforeHaveZore || j % 4 != 0;
                //每隔5位数字修辞符常量 eg: 50000 伍万
                sb.append(C_F[j % 4 == 0 ? j / 4 : 0]);
            }
        }
        return sb.toString();
    }

    /**
     * To CNY
     *
     * @param d
     * @return
     */
    public static String toChineseCNY(Double d) {
        long l = d.longValue();
        if (d - l == 0) {
            return toChineseChar(l) + CNY[0] + "整";
        }
        String s = (d + "").split("\\.")[1];

        StringBuffer sb = new StringBuffer(s.length() * 2);
        int i = 1;
        for (char c : s.toCharArray()) {
            if (c - 48 == 0) {
                i++;
                continue;
            }
            sb.append(C_N[c - 48] + CNY[i++]);
            if (CNY.length == i) {
                break;
            }
        }
        return toChineseChar(l) + CNY[0] + sb;
    }

    public static String toChineseChar(Double d) {
        long l = d.longValue();
        if (d - l == 0) {
            return toChineseChar(l);
        }
        String s = (BigDecimal.valueOf(d).toPlainString()).split("\\.")[1];
        StringBuffer sb = new StringBuffer(s.length());
        sb.append("点");
        for (char c : s.toCharArray()) {
            sb.append(C_N[c - 48]);
        }
        return toChineseChar(l) + sb;
    }
}
