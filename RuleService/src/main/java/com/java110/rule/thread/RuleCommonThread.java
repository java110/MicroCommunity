package com.java110.rule.thread;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.RuleDomain;
import com.java110.utils.log.LoggerEngine;
import com.java110.utils.util.CodeMapUtil;
import com.java110.entity.rule.ContractRootRule;
import com.java110.entity.rule.Rule;
import com.java110.rule.common.RuleCommon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by wuxw on 2017/7/23.
 */
public class RuleCommonThread extends LoggerEngine{

    private final static Logger logger = LoggerFactory.getLogger(RuleCommonThread.class);

    private RuleCommon ruleCommon;

    private String ruleId;
    private JSONObject reqJson;
    private ContractRootRule contractRootRule;
    private int threadNum;
    private ExecutorService rulethreadPool;

    private CompletionService<String> ruleCompletionService;

    public RuleCommonThread(RuleCommon ruleCommon, String ruleId,
                            ContractRootRule contractRootRule,JSONObject reqJson,int threadNum,List<String> ruleList){
//		super();
        this.ruleCommon = ruleCommon;
        this.ruleId = ruleId;
        this.contractRootRule = contractRootRule;
        this.reqJson = reqJson;
        this.threadNum = threadNum;
//		this.start();
        this.ruleCurThread(ruleList);
    }

    //启动校验线程
    public void ruleCurThread(List<String> ruleList){
        rulethreadPool = Executors.newFixedThreadPool(threadNum);
        ruleCompletionService = new ExecutorCompletionService<String>(rulethreadPool);
        //往线程池里面加入线程对象
        for(final String ruleIdStr : ruleList){
            ruleCompletionService.submit(new Callable<String>(){
                public String call() throws Exception{
                    try {
                        Map<String,Rule> ruleMap = ruleCommon.getRuleMap();
                        Rule rule = ruleMap.get(ruleIdStr);
                        String url = rule.getRule_url();
                        String condRtn = "";
                        if(!StringUtils.isEmpty(url)){
                            if(rule.getRule_type().contains(RuleDomain.RULE_TYPE_COND_CFG)){
                                condRtn = ruleCommon.ruleCommonCondMed(ruleIdStr,contractRootRule,reqJson);
                            }else{
                                condRtn = ruleCommon.ruleCond(ruleIdStr,contractRootRule);
                            }
                        }
                        if(!StringUtils.isEmpty(condRtn)){
                            return condRtn;
                        }else{
                            //默认返回成功
                            return RuleDomain.RULE_COND_RETURN_0000;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        //动态常量DEP_PRVNC_RULE_ERROR_RET，不配置或配置不是1，则程序异常默认返回成功，其他返回具体异常信息
                        String depPrvncSaopRuleErrorRet = CodeMapUtil.getDynamicConstantValue("DEP_PRVNC_RULE_ERROR_RET");
                        if(StringUtils.isEmpty(depPrvncSaopRuleErrorRet) || !depPrvncSaopRuleErrorRet.equals("1")){
                            return RuleDomain.RULE_COND_RETURN_0000;
                        }else{
                            return "LTE4G全量校验规则条件判断环节异常[ruleId:"+ruleIdStr+"]:"+e.getMessage();
                        }
                    }
                }
            });
        }
    }

    //获取执行结果
    public String getRuleRst(){
        String retStr = RuleDomain.RULE_COND_RETURN_0000;
        for(int i=0; i<this.threadNum; i++){
            //检索并移除表示下一个已完成任务的 Future，如果目前不存在这样的任务，则等待。
            try {
                String ruleRst = ruleCompletionService.take().get();
                if(RuleDomain.RULE_COND_RETURN_0000.equals(ruleRst)){//校验成功

                }else if(RuleDomain.RULE_COND_RETURN_0003.equals(ruleRst)
                        || RuleDomain.RULE_COND_RETURN_0004.equals(ruleRst)){//国漫预存款校验
                    retStr = ruleRst;
                }else{//校验失败
                    return ruleRst;
                }
            } catch (Exception e) {
                e.printStackTrace();
                //动态常量DEP_PRVNC_RULE_ERROR_RET，不配置或配置不是1，则程序异常默认返回成功，其他返回具体异常信息
                String depPrvncSaopRuleErrorRet = CodeMapUtil.getDynamicConstantValue("DEP_PRVNC_RULE_ERROR_RET");
                if(StringUtils.isEmpty(depPrvncSaopRuleErrorRet) || !depPrvncSaopRuleErrorRet.equals("1")){

                }else{
                    return "LTE4G全量校验规则条件判断环节异常:"+e.getMessage();
                }
            }
        }
        return retStr;
    }

    //关闭线程池
    public void shutdownThreadPool(){
        rulethreadPool.shutdown();
    }

    public void run() {
        Rule rule = null;
        try{
            Map<String,Rule> ruleMap = ruleCommon.getRuleMap();
            rule = ruleMap.get(ruleId);
            String url = rule.getRule_url();
            String condRtn = "";
            if(!StringUtils.isEmpty(url)){
                if(rule.getRule_type().contains(RuleDomain.RULE_TYPE_COND_CFG)){
                    condRtn = this.ruleCommon.ruleCommonCondMed(ruleId,contractRootRule,this.reqJson);
                }else{
                    condRtn = this.ruleCommon.ruleCond(ruleId,contractRootRule);
                }
            }

            if(!StringUtils.isEmpty(condRtn)){
                if(condRtn.equals(RuleDomain.RULE_COND_RETURN_0000)){//成功
                    this.ruleCommon.setRuleCondRtn(this.ruleCommon.getRuleCondRtn()+"0,");
                }else{//失败
                    this.ruleCommon.setRuleCode(RuleDomain.RULE_COND_RETURN_1999);
                    this.ruleCommon.setRuleMsg(condRtn);
                    this.ruleCommon.setRuleCondRtn(this.ruleCommon.getRuleCondRtn()+"1,");

                }
            }else{
                this.ruleCommon.setRuleCondRtn(this.ruleCommon.getRuleCondRtn()+"0,");//默认成功
            }
        } catch (Exception e) {
            logger.error("校验异常",e);
            this.ruleCommon.setRuleCondRtn(this.ruleCommon.getRuleCondRtn()+"0,");//默认成功
            /*try {
                //记录错误日志
                this.ruleCommon.recordRuleLog(rule,e.getMessage(),"LTE4G全量校验规则条件判断环节异常[ruleId:"+ruleId+"]");

            } catch (Exception e1) {
                e1.printStackTrace();
            }*/
        }
    }
}
