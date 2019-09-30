package com.java110.rule.rest;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.RuleDomain;
import com.java110.utils.log.LoggerEngine;
import com.java110.utils.util.CodeMapUtil;
import com.java110.utils.util.RuleUtil;
import com.java110.core.base.controller.BaseController;
import com.java110.rule.smo.IRuleServiceSMO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 规则服务
 * Created by wuxw on 2017/7/22.
 */
@RestController
public class RuleServiceRest extends BaseController {
    private final static Logger logger = LoggerFactory.getLogger(RuleServiceRest.class);


    @Autowired
    IRuleServiceSMO ruleServiceSMOImpl;

    /**
     * 规则校验方法
     * 请求报文协议：
     *
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
     *
     * @param validateInfo
     * @return
     */
    @RequestMapping("/ruleService/validate")
    @ResponseBody
    public String validate(@RequestParam("validateInfo") String validateInfo){
        String resultInfo = "";
        logger.debug("----------[RuleServiceRest.validate]-----------请求报文：" + validateInfo);
        try{
            resultInfo = ruleServiceSMOImpl.validate(validateInfo);
        }catch (Exception e){
            LoggerEngine.error("----------[RuleServiceRest.validate]-----------出现异常，请求报文为["+validateInfo+"]" ,e);
            try {
                //XStream转换出错，默认返回成功，后台记录错误日志
                JSONObject jsonObject = JSONObject.parseObject(validateInfo);
                String transactionID = jsonObject.containsKey("TcpCont") ?
                        (jsonObject.getJSONObject("TcpCont").containsKey("TransactionID")?jsonObject.getJSONObject("TcpCont").getString("TransactionID"):"-1")
                        :"-1";
                String error =  e.toString();
                //记录错误日志
                //recordRuleLog(transactionID,error,"LTE4G全量校验报文转换环节异常");
                //动态常量DEP_PRVNC_RULE_ERROR_RET，不配置或配置不是1，则程序异常默认返回成功，其他返回具体异常信息
                String depPrvncSaopRuleErrorRet = CodeMapUtil.getDynamicConstantValue("DEP_PRVNC_RULE_ERROR_RET");
                if(StringUtils.isEmpty(depPrvncSaopRuleErrorRet) || !depPrvncSaopRuleErrorRet.equals("1")){
                    resultInfo = RuleUtil.ruleReturnJson(transactionID,RuleDomain.RULE_COND_RETURN_0000,"成功","");
                }else{
                    resultInfo = RuleUtil.ruleReturnJson(transactionID,RuleDomain.RULE_COND_RETURN_1999,e.getMessage(),"");
                }
            } catch (Exception e1) {
                LoggerEngine.error("----------[RuleServiceRest.validate]-----------出现异常，请求报文为["+validateInfo+"]" ,e);
                resultInfo = RuleUtil.ruleReturnJson("-1",RuleDomain.RULE_COND_RETURN_1999,e.getMessage(),"");
            }
        }finally {
            //这里需要记录日志报文
            logger.debug("----------[RuleServiceRest.validate]-----------返回报文：" + resultInfo);
            return resultInfo;
        }
    }


    public IRuleServiceSMO getRuleServiceSMOImpl() {
        return ruleServiceSMOImpl;
    }

    public void setRuleServiceSMOImpl(IRuleServiceSMO ruleServiceSMOImpl) {
        this.ruleServiceSMOImpl = ruleServiceSMOImpl;
    }
}
