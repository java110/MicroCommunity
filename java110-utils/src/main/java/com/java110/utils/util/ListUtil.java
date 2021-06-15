package com.java110.utils.util;

import java.util.List;

/**
 * list 工具类
 *
 * add by wuxw 2020-10-16
 */
public class ListUtil {

    /**
     * 判断 List 是否为空
     *
     * @param values list 数据
     * @return 空为true 有值为false
     */
    public  static boolean isNull(List values) {

        if (values == null) {
            return true;
        }

        if (values.size() < 1) {
            return true;
        }

        return false;
    }

    /**
     * 判断 list 是否只有一个结果集
     *
     * @param values list 数据
     * @return 只有一个值 为true 否则为false
     */
    public  static boolean hasOne(List values) {

        if (isNull(values)) {
            return false;
        }

        if (values.size() > 1) {
            return false;
        }

        return true;

    }
}
