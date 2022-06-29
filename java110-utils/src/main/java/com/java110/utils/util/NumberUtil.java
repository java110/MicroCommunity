package com.java110.utils.util;

public class NumberUtil {

    public static boolean isDouble(String var) {
        try {
            Double.parseDouble(var);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public static double getDouble(String var) {
        try {
            return Double.parseDouble(var);
        } catch (Exception e) {
            return 0;
        }
    }
}
