package com.java110.rule.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.java110.utils.constant.RuleDomain;
import com.java110.utils.util.CodeMapUtil;
import com.java110.utils.util.RuleUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.entity.rule.ContractRootRule;
import com.java110.entity.rule.Rule;
import com.java110.entity.rule.RuleEntrance;
import com.java110.entity.rule.TcpContRule;
import com.java110.rule.common.RuleCommon;
import com.java110.rule.dao.IRuleDao;
import com.java110.rule.smo.IRuleServiceSMO;
import com.java110.rule.thread.RuleCommonThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 规则服务实现类
 * Created by wuxw on 2017/7/23.
 */
@Service("ruleServiceSMOImpl")
public class RuleServiceSMOImpl extends BaseServiceSMO implements IRuleServiceSMO {
    private final static Logger logger = LoggerFactory.getLogger(RuleServiceSMOImpl.class);


    public static DateFormat df = new SimpleDateFormat("yyyyMMddHHmmSSS");



    @Autowired
    RuleCommon ruleCommon;

    @Autowired
    IRuleDao ruleDaoImpl;




    /**
     * 校验方法
     *
     * 请求协议：
     * {
     "ContractRoot": {
     "TcpCont": { -- 请求头部
     "RuleType":"RULE0001", -- 用来区分走那个规则组
     "ServiceCode": "SVC0001", -- 用来判断走那些规则
     "TransactionID": "1001000101201603081234567890", -- 交易流水
     "ReqTime": "20130817200202123",--请求时间
     },
     "SvcCont": { -- 请求体 这里面的结构可以随便定
     "AccNbr": "",
     "ProductNbr":"",
     "RegionCode":"863000"
     }
     }
     }
     * @param validateInfo
     * @return
     * @throws Exception
     */
    @Override
    public String validate(String validateInfo) throws Exception {

        JSONObject validateInfoJson = JSONObject.parseObject(validateInfo);
        ContractRootRule contractRootRule = new ContractRootRule();

        try {
            TcpContRule reqTcpCont = JSONObject.parseObject(validateInfoJson.getJSONObject("TcpCont").toJSONString(), TcpContRule.class);
            JSONObject reqSvcCont = validateInfoJson.getJSONObject("SvcCont");
            contractRootRule.setTcpCont(reqTcpCont);
            contractRootRule.setSvcCont(reqSvcCont);
        }catch (Exception e){
            //说明报文不是按协议来的，我们也需要支撑，所以这里补充数据，以免后续流程异常，流程继续往下走
            TcpContRule reqTcpCont = new TcpContRule();
            reqTcpCont.setTransactionId("-1");
            reqTcpCont.setServiceCode(RuleDomain.RULE_SERVICE_CODE_DEFAULT);
            reqTcpCont.setRuleType(RuleDomain.RULE_TYPE_DEFAULT);
            reqTcpCont.setReqTime(df.format(new Date()));
            contractRootRule.setTcpCont(reqTcpCont);
            contractRootRule.setSvcCont(validateInfoJson);
        }

        return  ruleCommon(contractRootRule,validateInfoJson);
    }




    /**
     * 业务规则校验实体转换
     * @param reqJson
     * @return
     */
    public String ruleCommon(ContractRootRule contractRootRule,JSONObject reqJson) throws Exception{
            java.sql.Timestamp startTime = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
            long allStartTime = System.currentTimeMillis();

            //调用规则校验
            String ruleResult =  ruleEntrance(contractRootRule,reqJson);

            //记录耗时明细
            ruleCommon.recordCostTime(reqJson,
                    System.currentTimeMillis()-allStartTime,
                    "LTE4G全量规则校验总耗时",
                    1,
                    startTime,
                    new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));

            return ruleResult;
        }


    /**
     * 业务规则入口
     * @param contractRootRule
     * @return
     */
    public String ruleEntrance(ContractRootRule contractRootRule,JSONObject reqJson) throws Exception{
        long ruleEntranceStartTime = System.currentTimeMillis();
        java.sql.Timestamp ruleEntranceStart = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
        String transactionId = contractRootRule.getTcpCont().getTransactionId();

        String roamFlag = RuleDomain.RULE_COND_RETURN_0000;
            String depPrvncSaopRuleType = CodeMapUtil.getDynamicConstantValue("DEP_PRVNC_RULE_TYPE");
            //获取表 Rule_Entrance 数据
            Map<String,RuleEntrance> ruleEntranceMap = ruleDaoImpl.getRuleEntranceMap();
            int ruleCount = 0;
            this.ruleCommon.init();

            //当前分组下的规则编码集合
            List ruleIdsInCurGroupList = RuleUtil.getRuleIdsInGroupByJson(reqJson.toJSONString(),ruleDaoImpl.getRuleGroupRelaList());

            //耗时明细信息集合
            List costTimeMapList = new ArrayList();

            List<String> ruleList = new ArrayList<String>();
            //记录耗时明细-耗时明细信息
            Map ruleEntranceDetailMap = new HashMap();
            ruleEntranceDetailMap.put("TRANSACTION_ID", contractRootRule.getTcpCont().getTransactionId());
            ruleEntranceDetailMap.put("COST_TIME", System.currentTimeMillis()-ruleEntranceStartTime);
            ruleEntranceDetailMap.put("REMARK", "LTE4G全量校验规则对象转换耗时");
            ruleEntranceDetailMap.put("DO_WHAT", "LTE4G全量校验规则对象转换耗时");
            ruleEntranceDetailMap.put("CLASS_FLAG", 2);
            ruleEntranceDetailMap.put("START_TIME", ruleEntranceStart);
            ruleEntranceDetailMap.put("FINISH_TIME", new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
            costTimeMapList.add(ruleEntranceDetailMap);
            for (RuleEntrance ruleEntrance : ruleEntranceMap.values()) {
                //add begin by dongchao 对业务规则校验增加分组逻辑 2015/6/6
                if(!ruleIdsInCurGroupList.contains(ruleEntrance.getRule_id()))
                {
                    continue;
                }
                long allStartTime = System.currentTimeMillis();
                java.sql.Timestamp startTime = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());

                String ruleCondition = ruleEntrance.getRule_condition();

                //BO_ACTION_TYPE入口检查
                if(!StringUtils.isEmpty(ruleCondition)){
                    if(!ruleCondition(ruleEntrance,reqJson)){
                        continue;
                    }
                }

                //记录耗时明细-耗时明细信息
                Map detailMap = new HashMap();
                detailMap.put("TRANSACTION_ID", contractRootRule.getTcpCont().getTransactionId());
                detailMap.put("COST_TIME", System.currentTimeMillis()-allStartTime);
                detailMap.put("REMARK", "LTE4G全量校验规则入口判断耗时[ruleId:"+ruleEntrance.getRule_id()+"]");
                detailMap.put("DO_WHAT", "LTE4G全量校验规则入口判断耗时[ruleId:"+ruleEntrance.getRule_id()+"]");
                detailMap.put("CLASS_FLAG", 2);
                detailMap.put("START_TIME", startTime);
                detailMap.put("FINISH_TIME", new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
                costTimeMapList.add(detailMap);

                //调用规则条件
                if(StringUtils.isEmpty(depPrvncSaopRuleType) || !depPrvncSaopRuleType.equals("1")){//不配置或配置不是1 为多线程并发调用模式
                    ruleCount ++;
                    ruleList.add(ruleEntrance.getRule_id());
                }else{//配置1为单线程串行调用模式
                    Rule rule = ruleDaoImpl.getRuleMap().get(ruleEntrance.getRule_id());
                    String url = rule.getRule_url();
                    String entranceRetrunXml = "";
                    if(!StringUtils.isEmpty(url)){
                        if(rule.getRule_type().contains(RuleDomain.RULE_TYPE_COND_CFG)){
                            entranceRetrunXml = ruleCommon.ruleCommonCondMed(ruleEntrance.getRule_id(),contractRootRule,reqJson);
                        }else{
                            entranceRetrunXml = ruleCommon.ruleCond(ruleEntrance.getRule_id(),contractRootRule);
                        }

                    }

                    if(!StringUtils.isEmpty(entranceRetrunXml) && !entranceRetrunXml.equals(RuleDomain.RULE_COND_RETURN_0000)){
                         //校验失败
                         //校验失败返回
                         return entranceRetrunXml;
                    }
                }
            }

            if((StringUtils.isEmpty(depPrvncSaopRuleType) || !depPrvncSaopRuleType.equals("1")) && ruleCount > 0){//不配置或配置不是1 为多线程并发调用模式
                RuleCommonThread ruleCommonThread = new RuleCommonThread(ruleCommon,"",contractRootRule,reqJson,ruleCount,ruleList);
                roamFlag = ruleCommonThread.getRuleRst();
                //关闭线程池
                ruleCommonThread.shutdownThreadPool();
            }

            //校验成功返回
            if(!roamFlag.equals(RuleDomain.RULE_COND_RETURN_0000)){
                //国漫免预存返回报文-特殊
                return RuleUtil.ruleReturnJson(transactionId,RuleDomain.RULE_COND_RETURN_1999,roamFlag,"");
            }else{
                String serviceCode = contractRootRule.getTcpCont().getServiceCode();
                return RuleUtil.ruleReturnJson(transactionId,RuleDomain.RULE_COND_RETURN_0000,"成功","");

            }

    }

    /**
     * 业务规则入口检查[Rule_condition]
     * @param ruleEntrance,reqJson
     * @return
     */
    public boolean ruleCondition(RuleEntrance ruleEntrance,JSONObject reqJson){
        try {

            String ruleCondition = ruleEntrance.getRule_condition();

            JSONPath.eval(reqJson,ruleCondition);
            //ruleCondition 入口检查
            if(!StringUtils.isEmpty(JSONPath.eval(reqJson,ruleCondition))){
                return true;
            }

        }catch (Exception e) {
            logger.error("----------[RuleServiceSMOImpl.ruleCondition]-----------条件判断异常：规则条件为 "
                    +JSONObject.toJSONString(ruleEntrance)+",请求报文为 "+reqJson);
        }
        return false;
    }


    public RuleCommon getRuleCommon() {
        return ruleCommon;
    }

    public void setRuleCommon(RuleCommon ruleCommon) {
        this.ruleCommon = ruleCommon;
    }

    public IRuleDao getRuleDaoImpl() {
        return ruleDaoImpl;
    }

    public void setRuleDaoImpl(IRuleDao ruleDaoImpl) {
        this.ruleDaoImpl = ruleDaoImpl;
    }
}
