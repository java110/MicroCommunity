package com.java110.agent.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.agent.dao.IAgentServiceDao;
import com.java110.common.constant.BusinessTypeConstant;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.constant.StatusConstant;
import com.java110.common.exception.ListenerExecuteException;
import com.java110.common.util.Assert;
import com.java110.common.util.DateUtil;
import com.java110.common.util.StringUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存 用户信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveAgentCerdentialsListener")
@Transactional
public class SaveAgentCerdentialsListener extends AbstractAgentBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(SaveAgentCerdentialsListener.class);

    @Autowired
    IAgentServiceDao agentServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_AGENT_CERDENTIALS;
    }

    /**
     * 保存代理商信息 business 表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        if(!data.containsKey("businessAgentCerdentials")){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"没有businessAgentCerdentials节点");
        }
        JSONArray businessAgentCerdentialses = data.getJSONArray("businessAgentCerdentials");
        doBusinessAgentCerdentials(business,businessAgentCerdentialses);
    }

    /**
     * business 数据转移到 instance
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();

        Map info = new HashMap();
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_ADD);

        //代理商证件
        List<Map> businessAgentCerdentialses = agentServiceDaoImpl.getBusinessAgentCerdentials(info);
        if(businessAgentCerdentialses != null && businessAgentCerdentialses.size()>0){
            agentServiceDaoImpl.saveAgentCerdentialsInstance(info);
        }
    }

    /**
     * 撤单
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");
        Map info = new HashMap();
        info.put("bId",bId);
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        Map paramIn = new HashMap();
        paramIn.put("bId",bId);
        paramIn.put("statusCd",StatusConstant.STATUS_CD_INVALID);
        //代理商属性
        List<Map> agentCerdentialses = agentServiceDaoImpl.getAgentCerdentials(info);
        if(agentCerdentialses != null && agentCerdentialses.size()>0){
            agentServiceDaoImpl.updateAgentCerdentailsInstance(paramIn);
        }
    }


    /**
     * 保存 代理商证件 信息
     * @param business 当前业务
     * @param businessAgentCerdentialses 代理商证件
     */
    private void doBusinessAgentCerdentials(Business business, JSONArray businessAgentCerdentialses) {
        for(int businessAgentCerdentialsIndex = 0 ; businessAgentCerdentialsIndex < businessAgentCerdentialses.size() ; businessAgentCerdentialsIndex ++) {
            JSONObject businessAgentCerdentials = businessAgentCerdentialses.getJSONObject(businessAgentCerdentialsIndex);
            Assert.jsonObjectHaveKey(businessAgentCerdentials, "agentId", "businessAgentPhoto 节点下没有包含 agentId 节点");

            if (businessAgentCerdentials.getString("agentCerdentialsId").startsWith("-")) {
                String agentAgentId = GenerateCodeFactory.getAgentCerdentialsId();
                businessAgentCerdentials.put("agentCerdentialsId", agentAgentId);
            }
            Date validityPeriod = null;
            try {
                if(StringUtil.isNullOrNone(businessAgentCerdentials.getString("validityPeriod"))){
                    validityPeriod = DateUtil.getLastDate();
                }else {
                    validityPeriod = DateUtil.getDateFromString(businessAgentCerdentials.getString("validityPeriod"), DateUtil.DATE_FORMATE_STRING_B);
                }
            } catch (ParseException e) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"传入参数 validityPeriod 格式不正确，请填写 "+DateUtil.DATE_FORMATE_STRING_B +" 格式，"+businessAgentCerdentials);
            }
            businessAgentCerdentials.put("validityPeriod",validityPeriod);
            businessAgentCerdentials.put("bId", business.getbId());
            businessAgentCerdentials.put("operate", StatusConstant.OPERATE_ADD);
            //保存代理商信息
            agentServiceDaoImpl.saveBusinessAgentCerdentials(businessAgentCerdentials);
        }
    }


    public IAgentServiceDao getAgentServiceDaoImpl() {
        return agentServiceDaoImpl;
    }

    public void setAgentServiceDaoImpl(IAgentServiceDao agentServiceDaoImpl) {
        this.agentServiceDaoImpl = agentServiceDaoImpl;
    }
}
