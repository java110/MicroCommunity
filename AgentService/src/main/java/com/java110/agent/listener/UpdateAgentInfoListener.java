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
 * 1、businessAgentInfo:{} 代理商基本信息节点
 * 2、businessAgentAttr:[{}] 代理商属性信息节点
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateAgentInfoListener")
@Transactional
public class UpdateAgentInfoListener extends AbstractAgentBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(UpdateAgentInfoListener.class);
    @Autowired
    IAgentServiceDao agentServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return ServiceCodeConstant.SERVICE_CODE_UPDATE_AGENT_INFO;
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

        //处理 businessAgent 节点
        if(data.containsKey("businessAgent")){
            JSONObject businessAgent = data.getJSONObject("businessAgent");
            doBusinessAgent(business,businessAgent);
            dataFlowContext.addParamOut("agentId",businessAgent.getString("agentId"));
        }

        if(data.containsKey("businessAgentAttr")){
            JSONArray businessAgentAttrs = data.getJSONArray("businessAgentAttr");
            doSaveBusinessAgentAttrs(business,businessAgentAttrs);
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

        //代理商信息
        Map businessAgentInfo = agentServiceDaoImpl.getBusinessAgentInfo(info);
        if( businessAgentInfo != null && !businessAgentInfo.isEmpty()) {
            flushBusinessAgentInfo(businessAgentInfo,StatusConstant.STATUS_CD_VALID);
            agentServiceDaoImpl.updateAgentInfoInstance(businessAgentInfo);
            dataFlowContext.addParamOut("agentId",businessAgentInfo.get("agent_id"));
        }
        //代理商属性
        List<Map> businessAgentAttrs = agentServiceDaoImpl.getBusinessAgentAttrs(info);
        if(businessAgentAttrs != null && businessAgentAttrs.size() > 0) {
            for(Map businessAgentAttr : businessAgentAttrs) {
                flushBusinessAgentAttr(businessAgentAttr,StatusConstant.STATUS_CD_VALID);
                agentServiceDaoImpl.updateAgentAttrInstance(businessAgentAttr);
            }
        }
        //代理商照片
        List<Map> businessAgentPhotos = agentServiceDaoImpl.getBusinessAgentPhoto(info);
        if(businessAgentPhotos != null && businessAgentPhotos.size() >0){
            for(Map businessAgentPhoto : businessAgentPhotos) {
                flushBusinessAgentPhoto(businessAgentPhoto,StatusConstant.STATUS_CD_VALID);
                agentServiceDaoImpl.updateAgentPhotoInstance(businessAgentPhoto);
            }
        }
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
        //代理商信息
        Map agentInfo = agentServiceDaoImpl.getAgentInfo(info);
        if(agentInfo != null && !agentInfo.isEmpty()){

            //代理商信息
            Map businessAgentInfo = agentServiceDaoImpl.getBusinessAgentInfo(delInfo);
            //除非程序出错了，这里不会为空
            if(businessAgentInfo == null || businessAgentInfo.isEmpty()){
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR,"撤单失败（agent），程序内部异常,请检查！ "+delInfo);
            }

            flushBusinessAgentInfo(businessAgentInfo,StatusConstant.STATUS_CD_VALID);
            agentServiceDaoImpl.updateAgentInfoInstance(businessAgentInfo);
            dataFlowContext.addParamOut("agentId",agentInfo.get("agent_id"));
        }

        //代理商属性
        List<Map> agentAttrs = agentServiceDaoImpl.getAgentAttrs(info);
        if(agentAttrs != null && agentAttrs.size()>0){

            List<Map> businessAgentAttrs = agentServiceDaoImpl.getBusinessAgentAttrs(delInfo);
            //除非程序出错了，这里不会为空
            if(businessAgentAttrs == null || businessAgentAttrs.size() ==0 ){
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR,"撤单失败(agent_attr)，程序内部异常,请检查！ "+delInfo);
            }
            for(Map businessAgentAttr : businessAgentAttrs) {
                flushBusinessAgentAttr(businessAgentAttr,StatusConstant.STATUS_CD_VALID);
                agentServiceDaoImpl.updateAgentAttrInstance(businessAgentAttr);
            }
        }
    }

    /**
     * 处理 businessAgent 节点
     * @param business 总的数据节点
     * @param businessAgent 代理商节点
     */
    private void doBusinessAgent(Business business,JSONObject businessAgent){

        Assert.jsonObjectHaveKey(businessAgent,"agentId","businessAgent 节点下没有包含 agentId 节点");

        if(businessAgent.getString("agentId").startsWith("-")){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"agentId 错误，不能自动生成（必须已经存在的agentId）"+businessAgent);
        }
        //自动保存DEL
        autoSaveDelBusinessAgent(business,businessAgent);

        businessAgent.put("bId",business.getbId());
        businessAgent.put("operate", StatusConstant.OPERATE_ADD);
        //保存代理商信息
        agentServiceDaoImpl.saveBusinessAgentInfo(businessAgent);

    }



    /**
     * 保存代理商属性信息
     * @param business 当前业务
     * @param businessAgentAttrs 代理商属性
     */
    private void doSaveBusinessAgentAttrs(Business business,JSONArray businessAgentAttrs){
        JSONObject data = business.getDatas();


        for(int agentAttrIndex = 0 ; agentAttrIndex < businessAgentAttrs.size();agentAttrIndex ++){
            JSONObject agentAttr = businessAgentAttrs.getJSONObject(agentAttrIndex);
            Assert.jsonObjectHaveKey(agentAttr,"attrId","businessAgentAttr 节点下没有包含 attrId 节点");
            if(agentAttr.getString("attrId").startsWith("-")){
                throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"attrId 错误，不能自动生成（必须已经存在的attrId）"+agentAttr);
            }
            //自动保存DEL数据
            autoSaveDelBusinessAgentAttr(business,agentAttr);

            agentAttr.put("bId",business.getbId());
            agentAttr.put("agentId",agentAttr.getString("agentId"));
            agentAttr.put("operate", StatusConstant.OPERATE_ADD);

            agentServiceDaoImpl.saveBusinessAgentAttr(agentAttr);
        }
    }


    public IAgentServiceDao getAgentServiceDaoImpl() {
        return agentServiceDaoImpl;
    }

    public void setAgentServiceDaoImpl(IAgentServiceDao agentServiceDaoImpl) {
        this.agentServiceDaoImpl = agentServiceDaoImpl;
    }



}
