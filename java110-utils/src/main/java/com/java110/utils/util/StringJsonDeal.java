package com.java110.utils.util;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.log.LoggerEngine;

import java.text.SimpleDateFormat;

/**
 * JSON 格式字符串解析
 * Created by wuxw on 2017/7/23.
 */
public class StringJsonDeal extends LoggerEngine{


    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHHmmss");

    private JSONObject jsonObject = null;

    public StringJsonDeal(String jsonString){
        try {
            jsonObject = JSONObject.parseObject(jsonString);
        }catch (Exception e){
            e.printStackTrace();
        }
    }




    public StringJsonDeal(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }




    /**
     * 对逻辑字符串进行分析和运算
     *
     * @param jsonString
     * @param keyIdentifyLogicInfo
     * @return
     * @throws
     */
    public static boolean logicCalculus(String jsonString, String keyIdentifyLogicInfo) throws Exception {
        final String PART_STRING_DBL_AMPERSAND = "&&"; //表示字符串：&&
        final String PART_STRING_DBL_VERTICAL_BAR = "||"; //表示字符串：||
        final String PART_STRING_L_BIG_BRACKET = "{"; //表示字符串：{
        final String PART_STRING_R_BIG_BRACKET = "}"; //表示字符串：}
        final String PRVT_TRUE_STR = "1";
        final String PRVT_FALSE_STR = "0";
        final String PRVT_OR_BLANK_STR = " || ";
        String keyIdentifyLogicStr = keyIdentifyLogicInfo; //动态变化的关键判断逻辑信息
        while (keyIdentifyLogicStr.contains(PART_STRING_L_BIG_BRACKET)
                && keyIdentifyLogicStr.contains(PART_STRING_R_BIG_BRACKET)
                || keyIdentifyLogicStr.contains(PART_STRING_DBL_AMPERSAND)
                || keyIdentifyLogicStr.contains(PART_STRING_DBL_VERTICAL_BAR)
                || !PRVT_TRUE_STR.equals(keyIdentifyLogicStr) && !PRVT_FALSE_STR.equals(keyIdentifyLogicStr)) {
            int leftBigBracketPosition = 0; //最内左大括号的位置
            int rightBigBracketPosition = 0; //最内右大括号的位置
            String curProcessStr = keyIdentifyLogicStr; //当前要处理的字符串片断
            if (keyIdentifyLogicStr.contains(PART_STRING_L_BIG_BRACKET)
                    && keyIdentifyLogicStr.contains(PART_STRING_R_BIG_BRACKET)) {
                leftBigBracketPosition = keyIdentifyLogicStr.lastIndexOf(PART_STRING_L_BIG_BRACKET);
                rightBigBracketPosition = keyIdentifyLogicStr
                        .indexOf(PART_STRING_R_BIG_BRACKET, leftBigBracketPosition);
                curProcessStr = keyIdentifyLogicStr.substring(leftBigBracketPosition + 1, rightBigBracketPosition);
            }
            String rsltLogicStr = "";
            if (curProcessStr.contains(PART_STRING_DBL_VERTICAL_BAR)) {
                String[] orLogicStrs = curProcessStr.split("\\|\\|"); //“或”逻辑信息
                int orLogicStrNum = orLogicStrs.length;
                String curOrLogicStr = "";
                String curAndLogicStr = "";
                String orLogicStr = "";
                String andLogicStr = "";
                boolean hasTrue = false;
                for (int i = 0; i < orLogicStrNum; i++) {
                    curOrLogicStr = orLogicStrs[i];
                    if (null != curOrLogicStr) {
                        curOrLogicStr = curOrLogicStr.trim();
                    }
                    if (null == curOrLogicStr || "".equals(curOrLogicStr)) {
                        throw new Exception("字段配置错误，两个||之间不能没有信息");
                    }
                    orLogicStr = PRVT_FALSE_STR;
                    if (curOrLogicStr.contains(PART_STRING_DBL_AMPERSAND)) {
                        String[] andLogicStrs = curOrLogicStr.split(PART_STRING_DBL_AMPERSAND); //“与”逻辑信息
                        int andLogicStrNum = andLogicStrs.length;
                        andLogicStr = PRVT_TRUE_STR;
                        for (int j = 0; j < andLogicStrNum; j++) {
                            curAndLogicStr = andLogicStrs[j];
                            if (null != curAndLogicStr) {
                                curAndLogicStr = curAndLogicStr.trim();
                            }
                            if (null == curAndLogicStr || "".equals(curAndLogicStr)) {
                                throw new Exception("字段配置错误，两个&&之间不能没有信息");
                            }
                            if (!jsonString.contains(curAndLogicStr) || PRVT_FALSE_STR.equals(curAndLogicStr)) {
                                andLogicStr = PRVT_FALSE_STR;
                                break;
                            }
                        }
                        if ("".equals(rsltLogicStr)) {
                            rsltLogicStr = andLogicStr;
                        } else {
                            rsltLogicStr += PRVT_OR_BLANK_STR + andLogicStr;
                        }
                    } else {
                        if (jsonString.contains(curOrLogicStr) || PRVT_TRUE_STR.equals(curOrLogicStr)) {
                            if (!hasTrue) {
                                orLogicStr = PRVT_TRUE_STR;
                                if ("".equals(rsltLogicStr)) {
                                    rsltLogicStr = orLogicStr;
                                } else {
                                    rsltLogicStr += PRVT_OR_BLANK_STR + orLogicStr;
                                }
                                hasTrue = true;
                            }
                        }
                    }
                }
                if (!hasTrue) {
                    if ("".equals(rsltLogicStr)) {
                        rsltLogicStr = orLogicStr;
                    } else {
                        rsltLogicStr += PRVT_OR_BLANK_STR + orLogicStr;
                    }
                }
            } else if (curProcessStr.contains(PART_STRING_DBL_AMPERSAND)) {
                String[] andLogicStrs = curProcessStr.split(PART_STRING_DBL_AMPERSAND); //“与”逻辑信息
                int andLogicStrNum = andLogicStrs.length;
                String curAndLogicStr = "";
                String andLogicStr = PRVT_TRUE_STR;
                for (int j = 0; j < andLogicStrNum; j++) {
                    curAndLogicStr = andLogicStrs[j];
                    if (null != curAndLogicStr) {
                        curAndLogicStr = curAndLogicStr.trim();
                    }
                    if (null == curAndLogicStr || "".equals(curAndLogicStr)) {
                        throw new Exception("字段配置错误，两个&&之间不能没有信息");
                    }
                    if (!jsonString.contains(curAndLogicStr) || PRVT_FALSE_STR.equals(curAndLogicStr)) {
                        andLogicStr = PRVT_FALSE_STR;
                        break;
                    }
                }
                rsltLogicStr = andLogicStr;
            } else {
                String andLogicStr = PRVT_TRUE_STR;
                if (!jsonString.contains(curProcessStr) || PRVT_FALSE_STR.equals(curProcessStr)) {
                    andLogicStr = PRVT_FALSE_STR;
                }
                rsltLogicStr = andLogicStr;
            }
            logger.debug("替换前判断逻辑字符串（keyIdentifyLogicStr） ：{}", keyIdentifyLogicStr);
            logger.debug("当前用于替换的逻辑字符串（rsltLogicStr） ：{}", rsltLogicStr);
            if (0 != rightBigBracketPosition) {
                keyIdentifyLogicStr = keyIdentifyLogicStr.substring(0, leftBigBracketPosition) + rsltLogicStr
                        + keyIdentifyLogicStr.substring(rightBigBracketPosition + 1);
            } else {
                keyIdentifyLogicStr = rsltLogicStr;
            }
            logger.debug("替换后判断逻辑字符串（keyIdentifyLogicStr） ：{}", keyIdentifyLogicStr);
        }
        if (PRVT_TRUE_STR.equals(keyIdentifyLogicStr)) {
            return true;
        }
        return false;
    }
}
