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
 * 1、businessAgentPhoto:{} 代理商照片节点
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateAgentPhotoListener")
@Transactional
public class UpdateAgentPhotoListener extends AbstractAgentBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(UpdateAgentPhotoListener.class);
    @Autowired
    IAgentServiceDao agentServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return ServiceCodeConstant.SERVICE_CODE_UPDATE_AGENT_PHOTO;
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

        if(data.containsKey("businessAgentPhoto")){
            JSONArray businessAgentPhotos = data.getJSONArray("businessAgentPhoto");
            doBusinessAgentPhoto(business,businessAgentPhotos);
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

        //代理商照片
        List<Map> businessAgentPhotos = agentServiceDaoImpl.getBusinessAgentPhoto(info);
        if(businessAgentPhotos != null && businessAgentPhotos.size() >0){
            for(Map businessAgentPhoto : businessAgentPhotos) {
                flushBusinessAgentPhoto(businessAgentPhoto,StatusConstant.STATUS_CD_VALID);
                agentServiceDaoImpl.updateAgentPhotoInstance(businessAgentPhoto);
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

        //代理商照片
        List<Map> agentPhotos = agentServiceDaoImpl.getAgentPhoto(info);
        if(agentPhotos != null && agentPhotos.size()>0){
            List<Map> businessAgentPhotos = agentServiceDaoImpl.getBusinessAgentPhoto(delInfo);
            //除非程序出错了，这里不会为空
            if(businessAgentPhotos == null || businessAgentPhotos.size() ==0 ){
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR,"撤单失败(agent_photo)，程序内部异常,请检查！ "+delInfo);
            }
            for(Map businessAgentPhoto : businessAgentPhotos) {
                flushBusinessAgentPhoto(businessAgentPhoto,StatusConstant.STATUS_CD_VALID);
                agentServiceDaoImpl.updateAgentPhotoInstance(businessAgentPhoto);
            }
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
                throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"agentPhotoId 错误，不能自动生成（必须已经存在的agentPhotoId）"+businessAgentPhoto);
            }

            //自动保存DEL信息
            autoSaveDelBusinessAgentPhoto(business,businessAgentPhoto);

            businessAgentPhoto.put("bId", business.getbId());
            businessAgentPhoto.put("operate", StatusConstant.OPERATE_ADD);
            //保存代理商信息
            agentServiceDaoImpl.saveBusinessAgentPhoto(businessAgentPhoto);
        }
    }


    public IAgentServiceDao getAgentServiceDaoImpl() {
        return agentServiceDaoImpl;
    }

    public void setAgentServiceDaoImpl(IAgentServiceDao agentServiceDaoImpl) {
        this.agentServiceDaoImpl = agentServiceDaoImpl;
    }



}
