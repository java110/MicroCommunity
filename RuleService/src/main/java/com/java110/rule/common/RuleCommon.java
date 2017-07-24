package com.java110.rule.common;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.java110.common.constant.RuleDomain;
import com.java110.common.log.LoggerEngine;
import com.java110.common.util.RuleUtil;
import com.java110.common.util.SpringBeanInvoker;
import com.java110.entity.rule.ContractRootRule;
import com.java110.entity.rule.Rule;
import com.java110.entity.rule.RuleCondCfg;
import com.java110.rule.dao.IRuleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by wuxw on 2017/7/23.
 */
@Service("ruleCommon")
public class RuleCommon extends LoggerEngine{


    private String ruleCode;
    private String ruleMsg;
    private String ruleCondRtn;//配置规则条件返回值 0成功 1失败

    @Autowired
    IRuleDao ruleDaoImpl;


    Map<String,String> ruleNodeMap;

    Map<String,String> dataStackMap = null;

    Map<String,List<Map<String,String>>> ruleDbMap;



    public RuleCommon(Map<String, String> dataStackMap) {
        this.dataStackMap = dataStackMap;
    }

    public String ruleCommonCondMed(String ruleId, ContractRootRule contractRootRule, JSONObject reqJson)  throws Exception {
        long allStartTime = System.currentTimeMillis();
        java.sql.Timestamp startTime = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());

        String condReturnXml = "";
        Map<String,Rule> ruleMap = ruleDaoImpl.getRuleMap();
        Rule rule = ruleMap.get(ruleId);

        Map<String,String> map = new HashMap<String,String>();

        List<TreeMap<String,String>> paramList = new ArrayList<TreeMap<String,String>>();

        map = ruleCommonCondMed(ruleId,paramList,reqJson);

        condReturnXml = ruleCommonCondMed(map,paramList,rule,contractRootRule.getTcpCont().getTransactionId());
        //记录耗时明细
        recordCostTime(reqJson,
                System.currentTimeMillis()-allStartTime,
                "校验规则条件判断耗时[ruleId:"+ruleId+"]",
                2,
                startTime,
                new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
        return condReturnXml;
    }

    public String ruleCommonCondMed(Map<String,String> map,List<TreeMap<String,String>> paramList,Rule rule,String transactionID)  throws Exception {
        String retCode = map.get("retCode");
        String retMsg = map.get("retMsg");
        if(!StringUtils.isEmpty(retCode)){
            if(retCode.equals(RuleDomain.RULE_COND_RETURN_0000)){//校验通过
                return retCode;
            }else{//校验失败
                return RuleUtil.ruleReturnJson(transactionID,retCode,retMsg,rule.getRule_type());
            }
        }else{
            //如果调用存过的参数为空，直接返回成功
            if(paramList.size() == 0 || !rule.getRule_type().contains(RuleDomain.RULE_TYPE_STORED_PROCEDURE)){
                return RuleDomain.RULE_COND_RETURN_0000;
            }else{
                return ruleAction(transactionID,rule,paramList);
            }
        }
    }


    public Map<String,String> ruleCommonCondMed (String ruleId, List<TreeMap<String,String>> list,JSONObject reqJson)  throws Exception {
        if(list == null){
            list = new ArrayList<TreeMap<String, String>>();
        }

        Map<String,String> returnMap = new HashMap<String,String>();
        dataStackMap = new HashMap<String,String>();
        Map<String,Rule> ruleMap = ruleDaoImpl.getRuleMap();
        List<RuleCondCfg> ruleCondCfgList = ruleMap.get(ruleId).getRuleCondCfgs();
        ruleNodeMap = new HashMap<String,String>();
        ruleDbMap = new HashMap<String, List<Map<String,String>>>();
        returnMap.clear();
        TreeMap<String,String> returnTreeMap = new TreeMap<String,String>();
        String ruleCommonCondStr = "";
        int i = 0;
        for(RuleCondCfg ruleCondCfg : ruleCondCfgList){
            i++;
            String ruleNode = ruleCondCfg.getRule_node();
            String ruleNodeTmp = RuleDomain.RULE_SIGN_5+ruleCondCfg.getNode_existed()+RuleDomain.RULE_SIGN_5;
            String ruleNodeValue = "";
            String ruleNodeValueTmp = ruleNodeMap.get(ruleNodeTmp)==null?"":ruleNodeMap.get(ruleNodeTmp);
            if(!StringUtils.isEmpty(ruleNode)){
                String dataFromFlag = ruleCondCfg.getData_from_flag();
                if(dataFromFlag.contains(RuleDomain.RULE_SIGN_4)){
                    for(String dataFromFlag1 : dataFromFlag.split(RuleDomain.RULE_SIGN_4)){
                        ruleNodeValue = getRuleNodeValue(dataFromFlag1,ruleCondCfg,ruleNodeValueTmp,reqJson.toJSONString());
                        if(!StringUtils.isEmpty(ruleNodeValue)){
                            break;
                        }
                    }
                }else{
                    ruleNodeValue = getRuleNodeValue(dataFromFlag,ruleCondCfg,ruleNodeValueTmp,reqJson.toJSONString());
                }

            }
            ruleNodeMap.put(RuleDomain.RULE_SIGN_5+ruleNode+RuleDomain.RULE_SIGN_5, ruleNodeValue);
            //如果堆栈节点配置不为空并且取值方式不是5，则把结果值压入堆栈
            if(!StringUtils.isEmpty(ruleCondCfg.getData_stack_flag_prefix()) && !ruleCondCfg.getData_from_flag().equals("5")){
                dataStackMap.put(ruleCondCfg.getData_stack_flag_prefix(),ruleNodeValue);
            }
            if(!StringUtils.isEmpty(ruleCondCfg.getIs_log()) && ruleCondCfg.getIs_log().equals(RuleDomain.RULE_IS_Y)){
                ruleCommonCondStr += RuleDomain.RULE_SIGN_1+ruleNodeValue;
            }
            if(!StringUtils.isEmpty(ruleCondCfg.getError_code())){
                if(((StringUtils.isEmpty(StringUtils.replace(ruleNodeValue, "''", ""))
                        && (StringUtils.isEmpty(ruleCondCfg.getIs_reverse())
                        || ruleCondCfg.getIs_reverse().equals(RuleDomain.RULE_IS_N)))
                        || (!StringUtils.isEmpty(StringUtils.replace(ruleNodeValue, "''", ""))
                        && !StringUtils.isEmpty(ruleCondCfg.getIs_reverse())
                        && ruleCondCfg.getIs_reverse().equals(RuleDomain.RULE_IS_Y)))){
                    returnMap.put("retCode", ruleCondCfg.getError_code());
                    returnMap.put("retMsg", getErrorMsg(ruleCondCfg.getError_msg()));
                    break;
                }
            }
            if(!StringUtils.isEmpty(ruleCondCfg.getProc_param_flag())){
                returnTreeMap.put(String.valueOf(i), ruleNodeValue);
            }
        }
        //记录规则日志

        String transactionID = reqJson.getJSONObject("TcpCont").getString("TransactionID");
        saveRuleCommonCondLog(transactionID,ruleId,ruleCommonCondStr,returnMap);
        if(!returnTreeMap.isEmpty()){
            list.add(returnTreeMap);
        }

        return returnMap;
    }


    /**
     * 获取值
     * @param dataFromFlag
     * @param ruleCondCfg
     * @param ruleNodeValueTmp
     * @param reqJson
     * @return
     * @throws Exception
     */
    public String getRuleNodeValue(String dataFromFlag,RuleCondCfg ruleCondCfg,String ruleNodeValueTmp,String reqJson) throws Exception{

        if(dataFromFlag.equals("2")){//dbSql取值
            //return getRuleNodeValueFromDbSQL(ruleCondCfg.getRuleNode(),getDbSQL(ruleCondCfg.getDbSql()));
            String dbSql =getDbSQL(ruleCondCfg.getDb_sql());
            try
            {
                return getRuleNodeValueFromDbSQL(ruleCondCfg.getRule_node(),dbSql);
            }catch(Exception e)
            {
                logger.error("-------------[RuleServiceSMOImpl.getRuleNodeValue]-------------rule_id is "+ruleCondCfg.getRule_id()+" ,error sql is ="+dbSql);
                throw e;
            }
        }else if(dataFromFlag.equals("3")){//常量
            return ruleCondCfg.getDefault_value();
        }else if(dataFromFlag.equals("4")){//取已有节点的值
            return ruleNodeValueTmp;
        }else if(dataFromFlag.equals("5")){//从堆栈中获取
            return getRuleNodeValueFromDataStack(ruleCondCfg.getData_stack_flag_prefix());
        }else if(dataFromFlag.equals("6")){//从XML中获取
            return getRuleNodeValueFromJson(ruleCondCfg.getJpath(),reqJson);
        }else{
            return "";
        }
    }


    public String getDbSQL(String dbSql)  throws Exception {
        String exeDbSql = "";
        String[] sqlArray = dbSql.split(RuleDomain.RULE_SIGN_5);
        if (null != sqlArray && sqlArray.length > 1) {
            String sqlStr = null;
            int sqlLength = sqlArray.length;
            if (0 == sqlLength % 2) {
                if (!dbSql.endsWith(RuleDomain.RULE_SIGN_5)) {
                    return "SQL语句配置错误，参数两边的#没有成对出现：SQL=" + dbSql;
                }
            }
            for (int j = 0; j < sqlLength; j++) {
                sqlStr = sqlArray[j];
                if (0 != j % 2) {
                    sqlStr = ruleNodeMap.get(RuleDomain.RULE_SIGN_5+sqlStr+RuleDomain.RULE_SIGN_5)==null?"":ruleNodeMap.get(RuleDomain.RULE_SIGN_5+sqlStr+RuleDomain.RULE_SIGN_5);
                    if(!sqlStr.contains(RuleDomain.RULE_SIGN_3)){
                        sqlStr = "'" + sqlStr + "'";
                    }
                } else {
                    sqlStr = getDbSQL(sqlStr);
                }
                exeDbSql += sqlStr;
            }
            return exeDbSql;
        }
        return dbSql;
    }


    /**
     * 根据sql获取值
     * @param ruleNode
     * @param dbSql
     * @return
     * @throws Exception
     */
    public String getRuleNodeValueFromDbSQL(String ruleNode,String dbSql)  throws Exception {
        String columnStr = "";
        String retVal = "";
        String dbSqlStr = dbSql;
        if(dbSql.startsWith(RuleDomain.RULE_SIGN_6)){
            String dbSqlTmp = dbSql.split(RuleDomain.RULE_SIGN_6)[1];
            if(dbSqlTmp.contains(RuleDomain.RULE_SIGN_8)){
                String ruleNodeTmp = dbSqlTmp.split("\\"+RuleDomain.RULE_SIGN_8)[0];
                String columnStrTmp = dbSqlTmp.split("\\"+RuleDomain.RULE_SIGN_8)[1];
                List<Map<String,String>> dbList = ruleDbMap.get(ruleNodeTmp);
                if(dbList != null && dbList.size() > 0){
                    for(Map<String,String> dbMap : dbList){
                        retVal += RuleDomain.RULE_SIGN_4 + RuleDomain.RULE_SIGN_3 + String.valueOf(dbMap.get(columnStrTmp.toUpperCase())) + RuleDomain.RULE_SIGN_3;
                    }
                    if(retVal.length() > 0){
                        retVal = retVal.substring(1);
                    }
                }
            }
        }else{
            if(dbSql.contains(RuleDomain.RULE_SIGN_7)){
                if(dbSql.split(RuleDomain.RULE_SIGN_7).length > 1){
                    columnStr = dbSql.split(RuleDomain.RULE_SIGN_7)[1].toUpperCase();
                }
                dbSqlStr = dbSql.split(RuleDomain.RULE_SIGN_7)[0];
            }
            List<Map<String,String>> dbList = ruleDaoImpl.executeSql(dbSqlStr);
            if(dbList != null && dbList.size() > 0){
                for(Map<String,String> dbMap : dbList){
                    if(StringUtils.isEmpty(columnStr)){
                        Object[] retAttr = dbMap.values().toArray();
                        if(retAttr.length > 0){
                            if(retAttr[0]!=null){
                                retVal += RuleDomain.RULE_SIGN_4 + RuleDomain.RULE_SIGN_3 + retAttr[0].toString() + RuleDomain.RULE_SIGN_3;

                            }
                        }
                    }else{
                        retVal += RuleDomain.RULE_SIGN_4 + RuleDomain.RULE_SIGN_3 + String.valueOf(dbMap.get(columnStr)) + RuleDomain.RULE_SIGN_3;
                    }
                }
                if(retVal.length() > 0){
                    retVal = retVal.substring(1);
                }
                ruleDbMap.put(ruleNode, dbList);
            }
        }
        return retVal;
    }


    //从堆栈中获取值
    public String getRuleNodeValueFromDataStack(String dataStackFlagPrefix)  throws Exception {
        String retVal = "";
        if(!StringUtils.isEmpty(dataStackFlagPrefix) && !StringUtils.isEmpty(dataStackMap)){
            retVal = dataStackMap.get(dataStackFlagPrefix);
            if(retVal == null){
                retVal = "";
            }
        }else{
            return "";
        }
        return retVal;
    }

    //从json中获取值
    public String getRuleNodeValueFromJson(String jPath,String reqJson)  throws Exception {
        String retVal = "";
        if (!StringUtils.isEmpty(jPath)) {
            if(jPath.contains(RuleDomain.RULE_SIGN_5)){
                String[] sooNodeXpathArray = jPath.split(RuleDomain.RULE_SIGN_5);
                if (null != sooNodeXpathArray) {
                    String sql3 = null;
                    int sooNodeXpathArrayLength = sooNodeXpathArray.length;
                    if (0 == sooNodeXpathArrayLength % 2) {
                        if (!jPath.endsWith(RuleDomain.RULE_SIGN_5)) {
                            return "规则配置错误，配置sooNodeXpath的#没有成对出现";
                        }
                    }
                    String sooNodeParam = null;
                    String sooNodevalue = null;
                    for (int j = 0; j < sooNodeXpathArrayLength; j++) {
                        sooNodeParam = RuleDomain.PART_STRING_ORIGINAL_VALUE;
                        sooNodeParam = sooNodeXpathArray[j];
                        if (0 != j % 2) {
                            sooNodevalue = ruleNodeMap.get(RuleDomain.RULE_SIGN_5+sooNodeParam+RuleDomain.RULE_SIGN_5)==null?"":ruleNodeMap.get(RuleDomain.RULE_SIGN_5+sooNodeParam+RuleDomain.RULE_SIGN_5);
                            sooNodevalue = sooNodevalue.contains("'")?sooNodevalue:"'"+sooNodevalue+"'";
                            jPath = StringUtils.replace(jPath, RuleDomain.RULE_SIGN_5+sooNodeParam+RuleDomain.RULE_SIGN_5, sooNodevalue);
                        }
                    }
                }
            }
            retVal = this.getNodeValueFromJson(jPath, reqJson);
        }
        return retVal;
    }


    /**
     * 通过jpath 获取值
     * @param jPath
     * @param reqJson
     * @return
     */
    private String getNodeValueFromJson(String jPath,String reqJson){
        String nodeValue = "";
        JSONObject reqJsonObject = null;
        if (!StringUtils.isEmpty(reqJson)) {
            try {
                reqJsonObject = JSONObject.parseObject(reqJson);
            } catch (Exception e) {
                return "给出的报文格式存在错误，无法转换为JSON对象，具体原因为：" + e;
            }
            //报文中SOO对象的根节点
            if (null != reqJsonObject) {
                if (null != jPath && !"".equals(jPath)) {
                    String[] array1 = jPath.split(RuleDomain.RULE_SIGN_9);
                    int length1 = array1.length;
                    if (0 < length1) {
                        String sooNodeJpathTmp = null;
                        for (int i = 0; i < length1; i++) {
                            sooNodeJpathTmp = array1[i];
                            if (null != sooNodeJpathTmp && !"".equals(sooNodeJpathTmp)) {
                                if(!sooNodeJpathTmp.startsWith("$.")){
                                    sooNodeJpathTmp = "$."+sooNodeJpathTmp;
                                }
                                Object valueObj = JSONPath.eval(reqJsonObject,sooNodeJpathTmp);
                                if (null != valueObj && valueObj instanceof String) {
                                    nodeValue = nodeValue + ",'" +valueObj + "'";
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return nodeValue.length()>0?nodeValue.substring(1):"";
    }



    /**
     * 业务规则校验,调用存储过程
     * @param
     * @return
     */
    public String ruleAction(String transactionId, Rule rule,List<TreeMap<String,String>> paramList) throws Exception{
        long allStartTime = System.currentTimeMillis();
        java.sql.Timestamp startTime = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());

        String procParam = "";
        String ruleCode = RuleDomain.RULE_COND_RETURN_0000;
        String ruleDesc = "";
        for(TreeMap<String,String> paramMap : paramList){
            procParam = "";
            for(String paramStr : paramMap.values()){
                if(StringUtils.isEmpty(paramStr)){
                    paramStr = "''";
                }
                procParam +=  paramStr + RuleDomain.RULE_SIGN_1;
            }
            procParam = procParam.substring(0,procParam.length() - 2);
            String actionRtn = ruleDaoImpl.executeProc(transactionId,procParam, rule.getRule_url());
            if(!actionRtn.equals(ruleCode)){//校验不通过
                ruleCode = RuleDomain.RULE_COND_RETURN_0002;
                ruleDesc = actionRtn;
                break;
            }else{
                ruleCode = actionRtn;
            }
        }

        //记录耗时明细
        recordCostTime(null,
                System.currentTimeMillis()-allStartTime,
                "LTE4G全量校验规则逻辑校验耗时[ruleId:"+rule.getRule_id()+"]",
                2,
                startTime,
                new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));

        if(ruleCode.equals(RuleDomain.RULE_COND_RETURN_0000)){//校验通过
            return ruleCode;
        }else{//校验失败
            return RuleUtil.ruleReturnJson(transactionId,rule.getRule_code(),ruleDesc,"");
        }

    }

    /**
     * 业务规则条件
     * @param contractRootRule
     * @return
     */
    public String ruleCond(String ruleId,ContractRootRule contractRootRule) throws Exception{

        long allStartTime = System.currentTimeMillis();
        java.sql.Timestamp startTime = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());

        String condReturnXml = "";
        Map<String,Rule> ruleMap = ruleDaoImpl.getRuleMap();
        Rule rule = ruleMap.get(ruleId);
        if(!StringUtils.isEmpty(rule.getRule_url())){

            List<TreeMap<String,String>> paramList = new ArrayList<TreeMap<String,String>>();
            Class[] parameterTypes = {ContractRootRule.class,List.class};
            Object[] args = {contractRootRule,paramList};

            Object ruleService = SpringBeanInvoker.getBean("ruleService");
            Method method = ruleService.getClass().getMethod(rule.getRule_url(), parameterTypes);
            String condRtn =  (String)method.invoke(ruleService, args);
            //记录耗时明细
            recordCostTime(null,
                    System.currentTimeMillis()-allStartTime,
                    "LTE4G全量校验规则条件判断耗时[ruleId:"+ruleId+"]",
                    2,
                    startTime,
                    new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));

            if(!StringUtils.isEmpty(condRtn)){
                if(condRtn.equals(RuleDomain.RULE_COND_RETURN_0000)){//校验通过
                    condReturnXml = condRtn;
                }else if(condRtn.equals(RuleDomain.RULE_COND_RETURN_0001)){//继续转存过校验
                    //如果调用存过的参数为空，直接返回成功
                    if(paramList.size() == 0 || !rule.getRule_type().contains(RuleDomain.RULE_TYPE_STORED_PROCEDURE)){
                        return RuleDomain.RULE_COND_RETURN_0000;
                    }
                    condReturnXml = ruleAction(contractRootRule.getTcpCont().getTransactionId(),rule,paramList);
                }else{//校验失败
                    condReturnXml = RuleUtil.ruleReturnJson(contractRootRule.getTcpCont().getTransactionId(),rule.getRule_code(),condRtn,rule.getRule_level());
                }
            }
        }

        return condReturnXml;

    }


    public String getErrorMsg(String errorMsg)  throws Exception {
        String exeErrorMsg = "";
        String[] errorMsgArray = errorMsg.split(RuleDomain.RULE_SIGN_5);
        if (null != errorMsgArray && errorMsgArray.length > 1) {
            String errorMsg1 = null;
            int errorMsgLength = errorMsgArray.length;
            if (0 == errorMsgLength % 2) {
                if (!errorMsg.endsWith(RuleDomain.RULE_SIGN_5)) {
                    return "规则提示语配置错误，配置的#没有成对出现：errorMsg=" + errorMsg;
                }
            }
            for (int j = 0; j < errorMsgLength; j++) {
                errorMsg1 = errorMsgArray[j];
                if (0 != j % 2) {
                    errorMsg1 = ruleNodeMap.get(RuleDomain.RULE_SIGN_5+errorMsg1+RuleDomain.RULE_SIGN_5)==null?"":ruleNodeMap.get(RuleDomain.RULE_SIGN_5+errorMsg1+RuleDomain.RULE_SIGN_5);
                } else {
                    errorMsg1 = getErrorMsg(errorMsg1);
                }
                exeErrorMsg += errorMsg1;
            }
            return exeErrorMsg;
        }
        return errorMsg;
    }

    /**
     * 为了多线程时可以用到这里我们中转一下
     * @return
     * @throws Exception
     */
    public Map<String,Rule> getRuleMap() throws Exception{
       return ruleDaoImpl.getRuleMap();
    }

    public void saveRuleCommonCondLog(String transactionID,String ruleID,String ruleNodeValue,Map<String,String> returnMap)  throws Exception {
       /* String retCode = returnMap.get("retCode");
        String retMsg = returnMap.get("retMsg");
        String retVal;
        if(StringUtils.isBlank(retCode) || retCode.equals(RuleDomain.RULE_COND_RETURN_0000)){
            retVal = RuleDomain.RULE_COND_RETURN_0000;
        }else{
            retVal = retMsg;
        }
        ruleNodeValue = StringUtils.isBlank(ruleNodeValue)?"":ruleNodeValue.substring(2);
        Map<String,String> ruleCondMap = new HashMap<String,String>();
        ruleCondMap.put("transactionID", transactionID);
        ruleCondMap.put("ruleID", ruleID);
        ruleCondMap.put("ruleNodeValue", ruleNodeValue);
        ruleCondMap.put("retVal", retVal);
        this.getRuleManageBmo().saveRuleCommonCondLog(ruleCondMap);*/
    }


    /**
     * 记录耗时明细
     * @param
     * @return
     */
    @SuppressWarnings("unchecked")
    public void recordCostTime(JSONObject reqJson,long costTime, String costDesc,int classFlag ,java.sql.Timestamp startTime,java.sql.Timestamp endTime){
       /* try {
            //判断是否开启了日志记录监控
            if(PCrmDomain.WRITE_LOG_SWITCH_DCV.ON.value.equals(CodeMapUtil.getDynamicConstantValue(PCrmDomain.WRITE_COST_TIME_SWITCH_DCN))){
                //这里统计最终的耗时，然后记录到监控表中
                Map detailMap = new HashMap();
                detailMap.put("TRANSACTION_ID", transactionId);
                detailMap.put("COST_TIME", costTime);
                detailMap.put("REMARK", costDesc);
                detailMap.put("DO_WHAT", costDesc);
                detailMap.put("CLASS_FLAG", classFlag);
                detailMap.put("START_TIME", startTime);
                detailMap.put("FINISH_TIME", endTime);

                this.getCustManagerSearchBMO().insertCostTimeDetail(detailMap);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }*/
    }

    public void init(){
        this.setRuleCode("");
        this.setRuleMsg("");
        this.setRuleCondRtn("");
    }


    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    public String getRuleMsg() {
        return ruleMsg;
    }

    public void setRuleMsg(String ruleMsg) {
        this.ruleMsg = ruleMsg;
    }

    public String getRuleCondRtn() {
        return ruleCondRtn;
    }

    public void setRuleCondRtn(String ruleCondRtn) {
        this.ruleCondRtn = ruleCondRtn;
    }

    public IRuleDao getRuleDaoImpl() {
        return ruleDaoImpl;
    }

    public void setRuleDaoImpl(IRuleDao ruleDaoImpl) {
        this.ruleDaoImpl = ruleDaoImpl;
    }
}
