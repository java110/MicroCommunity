package com.java110.agent.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.constant.ServiceCodeConstant;
import com.java110.common.constant.StatusConstant;
import com.java110.common.exception.ListenerExecuteException;
import com.java110.common.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.agent.dao.IAgentServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 修改代理商信息 侦听
 *
 * 处理节点
 * 4、businessAgentCerdentials:[{}] 代理商证件信息节点
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateAgentCerdentialsListener")
@Transactional
public class UpdateAgentCerdentialsListener extends AbstractAgentBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(UpdateAgentCerdentialsListener.class);
    @Autowired
    IAgentServiceDao agentServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return ServiceCodeConstant.SERVICE_CODE_UPDATE_AGENT_CERDENTIALS;
    }

    /**
     * business过程
     * @param dataFlowContext
     * @param business
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {

        JSONObject data = business.getDatas();

        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        if(data.containsKey("businessAgentCerdentials")){
            JSONArray businessAgentCerdentialses = data.getJSONArray("businessAgentCerdentials");
            doBusinessAgentCerdentials(business,businessAgentCerdentialses);
        }
    }


    /**
     * business to instance 过程
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
            for(Map businessAgentCerdentials : businessAgentCerdentialses) {
                flushBusinessAgentCredentials(businessAgentCerdentials,StatusConstant.STATUS_CD_VALID);
                agentServiceDaoImpl.updateAgentCerdentailsInstance(businessAgentCerdentials);
            }
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
        Map delInfo = new HashMap();
        delInfo.put("bId",business.getbId());
        delInfo.put("operate",StatusConstant.OPERATE_DEL);
        //代理商属性
        List<Map> agentCerdentialses = agentServiceDaoImpl.getAgentCerdentials(info);
        if(agentCerdentialses != null && agentCerdentialses.size()>0){
            List<Map> businessAgentCerdentialses = agentServiceDaoImpl.getBusinessAgentCerdentials(delInfo);
            //除非程序出错了，这里不会为空
            if(businessAgentCerdentialses == null || businessAgentCerdentialses.size() ==0 ){
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR,"撤单失败(agent_cerdentials)，程序内部异常,请检查！ "+delInfo);
            }
            for(Map businessAgentCerdentials : businessAgentCerdentialses) {
                flushBusinessAgentCredentials(businessAgentCerdentials,StatusConstant.STATUS_CD_VALID);
                agentServiceDaoImpl.updateAgentCerdentailsInstance(businessAgentCerdentials);
            }
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
                throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"agentPhotoId 错误，不能自动生成（必须已经存在的agentCerdentialsId）"+businessAgentCerdentials);
            }

            autoSaveDelBusinessAgentCerdentials(business,businessAgentCerdentials);

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
