package com.java110.utils.util;

import java.util.Map;

public class FlowUtil {

    /**
     * el表达式判断
     *
     * @param expression
     * @param vars
     * @return
     */
    public static boolean isCondition(String expression, Map<String, Object> vars) {
        if (expression == null || expression == "") {
            return false;
        }

        //分割表达式
        String[] exprArr = expression.split("[{}$&]");
        for (String expr : exprArr) {
            //是否包含键message
            if (expr.contains("flag")) {
                if (!vars.containsKey("flag")) {
                    continue;
                }
                if (expr.contains("==")) {
                    String[] primes = expr.split("==");
                    String valExpr = primes[1].trim();
                    if (valExpr.startsWith("'")) {
                        valExpr = valExpr.substring(1);
                    }
                    if (valExpr.endsWith("'")) {
                        valExpr = valExpr.substring(0, valExpr.length() - 1);
                    }
                    if (primes.length == 2 && valExpr.equals(vars.get("flag"))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
