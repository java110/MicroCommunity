package com.java110.common.rule;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.util.DateUtil;
import com.java110.common.util.SequenceUtil;

/**
 * 创建校验单 请求报文协议
 *
 * 工具类，提供于第三方系统调用组装请求报文
 * Created by wuxw on 2017/7/24.
 */
public class RuleProtocol {

    /**
     * 创建头部
     * @param transactionId
     * @param ruleType
     * @param serviceCode
     * @return
     */
    public static JSONObject createTcpCont(String transactionId,String ruleType,String serviceCode){
        JSONObject tcpCont = new JSONObject();

        tcpCont.put("RuleType",ruleType);

        tcpCont.put("ServiceCode",serviceCode);

        tcpCont.put("TransactionID",transactionId);

        tcpCont.put("ReqTime", DateUtil.getyyyyMMddhhmmssDateString());

        return tcpCont;
    }

    /**
     * 创建头部
     * @param
     * @param ruleType
     * @param serviceCode
     * @return
     */
    public static JSONObject createTcpCont(String ruleType,String serviceCode){
        return createTcpCont(SequenceUtil.getTransactionId(),ruleType,serviceCode);
    }


    /**
     * 创建 业务部分数据
     * @param svcContParam
     * @return
     */
    public static JSONObject createSvcCont(JSONObject svcContParam){
        return svcContParam;
    }

    /**
     * transactionId 自己生成
     * 创建json校验请求报文
     * @param ruleType
     * @param serviceCode
     * @param svcContParam
     * @return
     */
    public static String createValidateJson(String ruleType,String serviceCode,JSONObject svcContParam){
        return createValidateJson(SequenceUtil.getTransactionId(),ruleType,serviceCode,svcContParam);
    }

    public static String createValidateJson(String transactionId,String ruleType,String serviceCode,JSONObject svcContParam){
        JSONObject validateJson = JSONObject.parseObject("{\"ContractRoot\":{}}");

        JSONObject contractRoot = validateJson.getJSONObject("ContractRoot");

        contractRoot.put("TcpCont", createTcpCont(transactionId,ruleType,serviceCode));
        contractRoot.put("SvcCont", createSvcCont(svcContParam));

        return validateJson.toJSONString();
    }
}
