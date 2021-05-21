package com.java110.utils.util;

import com.java110.utils.log.LoggerEngine;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by wuxw on 2017/7/23.
 */
public class RuleUtil extends LoggerEngine{



    /**
     * 根据传入的json从缓存中获取规则分组下的规则编码集合
     * @param reqJson
     * @return   如果获取不到则会返回new ArrayList(),不会为null
     */
    public static List<Object> getRuleIdsInGroupByJson(String reqJson,List<Object> saopRuleGroupInfoList)
    {
        //获取缓存中规则分组集合
        //List saopRuleGroupInfoList = RuleUtil.getRuleGroupRelaList();

        //当前分组下的规则编码集合
        List<Object> ruleIdsInCurGroupList = new ArrayList<Object>();

        try
        {
            int saopRuleGroupInfoListSize = saopRuleGroupInfoList.size();
            if(saopRuleGroupInfoListSize > 0)
            {
                for(int i = 0; i < saopRuleGroupInfoListSize; i++)
                {
                    Map ruleGroupMap = (Map)saopRuleGroupInfoList.get(i);

                    //获取集团报文中的关键信息片断，用于报文分析时定位当前转换场景
                    String keyIdentifyLogicInfo = "";
                    if(null != ruleGroupMap.get("KEY_IDENTIFY_LOGIC_STR"))
                    {
                        keyIdentifyLogicInfo = String.valueOf(ruleGroupMap.get("KEY_IDENTIFY_LOGIC_STR"));
                    }

                    if(!StringUtil.isEmpty(keyIdentifyLogicInfo))
                    {
                        //对逻辑字符串进行分析和运算
                        boolean logicMatchRtn = StringJsonDeal.logicCalculus(reqJson,keyIdentifyLogicInfo);

                        if(logicMatchRtn)
                        {
                            if(null != ruleGroupMap.get("ruleIdList"))
                            {
                                ruleIdsInCurGroupList = (List)ruleGroupMap.get("ruleIdList");
                            }
                            break;
                        }
                    }
                    else
                    {
                        if(null != ruleGroupMap.get("ruleIdList"))
                        {
                            ruleIdsInCurGroupList = (List)ruleGroupMap.get("ruleIdList");
                        }
                        break;
                    }
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            if(logger.isErrorEnabled())
            {
                logger.error("CodeMapUtil.getRuleIdsInGroupByXml执行出错，错误信息：" + e.toString());
            }
        }
        return ruleIdsInCurGroupList;
    }

    /**
     * 拼装业务规则校验返回报文
     * @param
     * @return
     */
    public static String ruleReturnJson(String transactionId,String ruleCode,String ruleDesc,String ruleType){
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        StringBuffer returnJson = new StringBuffer("{")
                .append("\"ContractRoot\":{")
                .append("\"TcpCont\":{")
                .append("\"TransactionID\":\"").append(transactionId).append("\",")
                .append("\"RspTime\":\"").append(df.format(new Date())).append("\",")
                .append("\"RspType\":\"").append(ruleType).append("\",")
                .append("\"RspCode\":").append(ruleCode).append("\",")
                .append("\"RspDesc\":").append(ruleDesc).append("\"")
                .append("}")
                .append("}");
        return returnJson.toString();
    }



}
