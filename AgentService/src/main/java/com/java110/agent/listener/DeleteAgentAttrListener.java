package com.java110.agent.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.agent.dao.IAgentServiceDao;
import com.java110.common.constant.BusinessTypeConstant;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.constant.StatusConstant;
import com.java110.common.exception.ListenerExecuteException;
import com.java110.common.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 删除代理商信息 侦听
 *
 * 处理节点
 * 1、businessAgentHouse:{} 代理商住户信息节点
 * 2、businessAgentAttr:[{}] 代理商属性信息节点
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("deleteAgentAttrListener")
@Transactional
public class DeleteAgentAttrListener extends AbstractAgentBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(DeleteAgentAttrListener.class);
    @Autowired
    IAgentServiceDao agentServiceDaoImpl;

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_DELETE_AGENT_ATTR;
    }

    /**
     * 根据删除信息 查出Instance表中数据 保存至business表 （状态写DEL） 方便撤单时直接更新回去
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();

        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        if(data.containsKey("businessAgentAttr")){
            JSONArray businessAgentAttrs = data.getJSONArray("businessAgentAttr");
            doSaveBusinessAgentAttrs(business,businessAgentAttrs);
        }
    }

    /**
     * 删除 instance数据
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");

        //代理商信息
        Map info = new HashMap();
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_DEL);

        //代理商属性
        List<Map> businessAgentAttrs = agentServiceDaoImpl.getBusinessAgentAttrs(info);
        if(businessAgentAttrs != null && businessAgentAttrs.size() > 0) {
            for(Map businessAgentAttr : businessAgentAttrs) {
                flushBusinessAgentAttr(businessAgentAttr,StatusConstant.STATUS_CD_INVALID);
                agentServiceDaoImpl.updateAgentAttrInstance(businessAgentAttr);
            }
        }
    }

    /**
     * 撤单
     * 从business表中查询到DEL的数据 将instance中的数据更新回来
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");
        Map info = new HashMap();
        info.put("bId",bId);
        info.put("statusCd",StatusConstant.STATUS_CD_INVALID);

        Map delInfo = new HashMap();
        delInfo.put("bId",business.getbId());
        delInfo.put("operate",StatusConstant.OPERATE_DEL);

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

            autoSaveDelBusinessAgentAttr(business,agentAttr);
        }
    }

    public IAgentServiceDao getAgentServiceDaoImpl() {
        return agentServiceDaoImpl;
    }

    public void setAgentServiceDaoImpl(IAgentServiceDao agentServiceDaoImpl) {
        this.agentServiceDaoImpl = agentServiceDaoImpl;
    }
}
