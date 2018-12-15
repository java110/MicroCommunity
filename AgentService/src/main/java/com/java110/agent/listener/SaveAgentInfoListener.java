package com.java110.agent.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.constant.ServiceCodeConstant;
import com.java110.common.constant.StatusConstant;
import com.java110.common.exception.ListenerExecuteException;
import com.java110.common.util.Assert;
import com.java110.common.util.DateUtil;
import com.java110.common.util.StringUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.agent.dao.IAgentServiceDao;
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
@Java110Listener("saveAgentInfoListener")
@Transactional
public class SaveAgentInfoListener extends AbstractAgentBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(SaveAgentInfoListener.class);

    @Autowired
    IAgentServiceDao agentServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_SAVE_STORE_INFO;
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

        if(data.containsKey("businessAgentPhoto")){
            JSONArray businessAgentPhotos = data.getJSONArray("businessAgentPhoto");
            doBusinessAgentPhoto(business,businessAgentPhotos);
        }

        if(data.containsKey("businessAgentCerdentials")){
            JSONArray businessAgentCerdentialses = data.getJSONArray("businessAgentCerdentials");
            doBusinessAgentCerdentials(business,businessAgentCerdentialses);
        }
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

        //代理商信息
        Map businessAgentInfo = agentServiceDaoImpl.getBusinessAgentInfo(info);
        if( businessAgentInfo != null && !businessAgentInfo.isEmpty()) {
            agentServiceDaoImpl.saveAgentInfoInstance(info);
            dataFlowContext.addParamOut("agentId",businessAgentInfo.get("agent_id"));
        }
        //代理商属性
        List<Map> businessAgentAttrs = agentServiceDaoImpl.getBusinessAgentAttrs(info);
        if(businessAgentAttrs != null && businessAgentAttrs.size() > 0) {
            agentServiceDaoImpl.saveAgentAttrsInstance(info);
        }
        //代理商照片
        List<Map> businessAgentPhotos = agentServiceDaoImpl.getBusinessAgentPhoto(info);
        if(businessAgentPhotos != null && businessAgentPhotos.size() >0){
            agentServiceDaoImpl.saveAgentPhotoInstance(info);
        }
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
        //代理商信息
        Map agentInfo = agentServiceDaoImpl.getAgentInfo(info);
        if(agentInfo != null && !agentInfo.isEmpty()){
            paramIn.put("agentId",agentInfo.get("agent_id").toString());
            agentServiceDaoImpl.updateAgentInfoInstance(paramIn);
            dataFlowContext.addParamOut("agentId",agentInfo.get("agent_id"));
        }

        //代理商属性
        List<Map> agentAttrs = agentServiceDaoImpl.getAgentAttrs(info);
        if(agentAttrs != null && agentAttrs.size()>0){
            agentServiceDaoImpl.updateAgentAttrInstance(paramIn);
        }

        //代理商照片
        List<Map> agentPhotos = agentServiceDaoImpl.getAgentPhoto(info);
        if(agentPhotos != null && agentPhotos.size()>0){
            agentServiceDaoImpl.updateAgentPhotoInstance(paramIn);
        }

        //代理商属性
        List<Map> agentCerdentialses = agentServiceDaoImpl.getAgentCerdentials(info);
        if(agentCerdentialses != null && agentCerdentialses.size()>0){
            agentServiceDaoImpl.updateAgentCerdentailsInstance(paramIn);
        }
    }

    /**
     * 保存代理商照片
     * @param business 业务对象
     * @param businessAgentPhotos 代理商照片
     */
    private void doBusinessAgentPhoto(Business business, JSONArray businessAgentPhotos) {

        for(int businessAgentPhotoIndex = 0 ;businessAgentPhotoIndex < businessAgentPhotos.size();businessAgentPhotoIndex++) {
            JSONObject businessAgentPhoto = businessAgentPhotos.getJSONObject(businessAgentPhotoIndex);
            Assert.jsonObjectHaveKey(businessAgentPhoto, "agentId", "businessAgentPhoto 节点下没有包含 agentId 节点");

            if (businessAgentPhoto.getString("agentPhotoId").startsWith("-")) {
                String agentPhotoId = GenerateCodeFactory.getAgentPhotoId();
                businessAgentPhoto.put("agentPhotoId", agentPhotoId);
            }
            businessAgentPhoto.put("bId", business.getbId());
            businessAgentPhoto.put("operate", StatusConstant.OPERATE_ADD);
            //保存代理商信息
            agentServiceDaoImpl.saveBusinessAgentPhoto(businessAgentPhoto);
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
            //刷新缓存
            flushAgentId(business.getDatas());
        }

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
        JSONObject businessAgent = data.getJSONObject("businessAgent");
        for(int agentAttrIndex = 0 ; agentAttrIndex < businessAgentAttrs.size();agentAttrIndex ++){
            JSONObject agentAttr = businessAgentAttrs.getJSONObject(agentAttrIndex);
            Assert.jsonObjectHaveKey(agentAttr,"attrId","businessAgentAttr 节点下没有包含 attrId 节点");

            if(agentAttr.getString("attrId").startsWith("-")){
                String attrId = GenerateCodeFactory.getAttrId();
                agentAttr.put("attrId",attrId);
            }

            agentAttr.put("bId",business.getbId());
            agentAttr.put("agentId",businessAgent.getString("agentId"));
            agentAttr.put("operate", StatusConstant.OPERATE_ADD);

            agentServiceDaoImpl.saveBusinessAgentAttr(agentAttr);
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
                String agentPhotoId = GenerateCodeFactory.getAgentCerdentialsId();
                businessAgentCerdentials.put("agentCerdentialsId", agentPhotoId);
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



    /**
     * 刷新 代理商ID
     * @param data
     */
    private void flushAgentId(JSONObject data) {

        String agentId = GenerateCodeFactory.getAgentId();
        JSONObject businessAgent = data.getJSONObject("businessAgent");
        businessAgent.put("agentId",agentId);
        //刷代理商属性
        if(data.containsKey("businessAgentAttr")) {
            JSONArray businessAgentAttrs = data.getJSONArray("businessAgentAttr");
            for(int businessAgentAttrIndex = 0;businessAgentAttrIndex < businessAgentAttrs.size();businessAgentAttrIndex++) {
                JSONObject businessAgentAttr = businessAgentAttrs.getJSONObject(businessAgentAttrIndex);
                businessAgentAttr.put("agentId", agentId);
            }
        }
        //刷 是代理商照片 的 agentId
        if(data.containsKey("businessAgentPhoto")) {
            JSONArray businessAgentPhotos = data.getJSONArray("businessAgentPhoto");
            for(int businessAgentPhotoIndex = 0;businessAgentPhotoIndex < businessAgentPhotos.size();businessAgentPhotoIndex++) {
                JSONObject businessAgentPhoto = businessAgentPhotos.getJSONObject(businessAgentPhotoIndex);
                businessAgentPhoto.put("agentId", agentId);
            }
        }
        //刷 代理商证件 的agentId
        if(data.containsKey("businessAgentCerdentials")) {
            JSONArray businessAgentCerdentialses = data.getJSONArray("businessAgentCerdentials");
            for(int businessAgentCerdentialsIndex = 0;businessAgentCerdentialsIndex < businessAgentCerdentialses.size();businessAgentCerdentialsIndex++) {
                JSONObject businessAgentCerdentials = businessAgentCerdentialses.getJSONObject(businessAgentCerdentialsIndex);
                businessAgentCerdentials.put("agentId", agentId);
            }
        }
    }


    public IAgentServiceDao getAgentServiceDaoImpl() {
        return agentServiceDaoImpl;
    }

    public void setAgentServiceDaoImpl(IAgentServiceDao agentServiceDaoImpl) {
        this.agentServiceDaoImpl = agentServiceDaoImpl;
    }
}
