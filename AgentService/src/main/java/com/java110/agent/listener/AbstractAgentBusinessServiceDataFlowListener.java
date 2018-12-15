package com.java110.agent.listener;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.constant.StatusConstant;
import com.java110.common.exception.ListenerExecuteException;
import com.java110.entity.center.Business;
import com.java110.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.agent.dao.IAgentServiceDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 代理商 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractAgentBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener{


    /**
     * 获取 DAO工具类
     * @return
     */
    public abstract IAgentServiceDao getAgentServiceDaoImpl();

    /**
     * 刷新 businessAgentInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     * @param businessAgentInfo
     */
    protected void flushBusinessAgentInfo(Map businessAgentInfo,String statusCd){
        businessAgentInfo.put("newBId",businessAgentInfo.get("b_id"));
        businessAgentInfo.put("agentId",businessAgentInfo.get("agent_id"));
        businessAgentInfo.put("userId",businessAgentInfo.get("user_id"));
        businessAgentInfo.put("agentTypeCd",businessAgentInfo.get("agent_type_cd"));
        businessAgentInfo.put("nearbyLandmarks",businessAgentInfo.get("nearby_landmarks"));
        businessAgentInfo.put("mapX",businessAgentInfo.get("map_x"));
        businessAgentInfo.put("mapY",businessAgentInfo.get("map_y"));
        businessAgentInfo.put("statusCd", statusCd);
    }

    /**
        刷新 businessAgentAttr 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     * @param businessAgentAttr
     * @param statusCd
     */
    protected void flushBusinessAgentAttr(Map businessAgentAttr,String statusCd){
        businessAgentAttr.put("attrId",businessAgentAttr.get("attr_id"));
        businessAgentAttr.put("specCd",businessAgentAttr.get("spec_cd"));
        businessAgentAttr.put("agentId",businessAgentAttr.get("agent_id"));
        businessAgentAttr.put("newBId",businessAgentAttr.get("b_id"));
        businessAgentAttr.put("statusCd",statusCd);
    }

    /**
     * 刷新 businessAgentPhoto 数据
     * @param businessAgentPhoto
     * @param statusCd
     */
    protected void flushBusinessAgentPhoto(Map businessAgentPhoto,String statusCd){
        businessAgentPhoto.put("agentId",businessAgentPhoto.get("agent_id"));
        businessAgentPhoto.put("agentPhotoId",businessAgentPhoto.get("agent_photo_id"));
        businessAgentPhoto.put("agentPhotoTypeCd",businessAgentPhoto.get("agent_photo_type_cd"));
        businessAgentPhoto.put("newBId",businessAgentPhoto.get("b_id"));
        businessAgentPhoto.put("statusCd",statusCd);
    }

    /**
     * 刷新 businessAgentCerdentials 数据
     * @param businessAgentCerdentials
     * @param statusCd
     */
    protected void flushBusinessAgentCredentials(Map businessAgentCerdentials ,String statusCd){
        businessAgentCerdentials.put("agentId",businessAgentCerdentials.get("agent_id"));
        businessAgentCerdentials.put("agentCerdentialsId",businessAgentCerdentials.get("agent_cerdentials_id"));
        businessAgentCerdentials.put("credentialsCd",businessAgentCerdentials.get("credentials_cd"));
        businessAgentCerdentials.put("validityPeriod",businessAgentCerdentials.get("validity_period"));
        businessAgentCerdentials.put("positivePhoto",businessAgentCerdentials.get("positive_photo"));
        businessAgentCerdentials.put("negativePhoto",businessAgentCerdentials.get("negative_photo"));
        businessAgentCerdentials.put("newBId",businessAgentCerdentials.get("b_id"));
        businessAgentCerdentials.put("statusCd",statusCd);
    }

    /**
     * 刷新 businessMemberAgent 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     * @param businessMemberAgent
     */
    protected void flushBusinessMemberAgent(Map businessMemberAgent,String statusCd){
        businessMemberAgent.put("newBId",businessMemberAgent.get("b_id"));
        businessMemberAgent.put("agentId",businessMemberAgent.get("agent_id"));
        businessMemberAgent.put("memberAgentId",businessMemberAgent.get("member_agent_id"));
        businessMemberAgent.put("memberId",businessMemberAgent.get("member_id"));
        businessMemberAgent.put("statusCd", statusCd);
    }

    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     * @param businessAgent 代理商信息
     */
    protected void autoSaveDelBusinessAgent(Business business, JSONObject businessAgent){
//自动插入DEL
        Map info = new HashMap();
        info.put("agentId",businessAgent.getString("agentId"));
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        Map currentAgentInfo = getAgentServiceDaoImpl().getAgentInfo(info);
        if(currentAgentInfo == null || currentAgentInfo.isEmpty()){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"未找到需要修改数据信息，入参错误或数据有问题，请检查"+info);
        }
        currentAgentInfo.put("bId",business.getbId());
        currentAgentInfo.put("agentId",currentAgentInfo.get("agent_id"));
        currentAgentInfo.put("userId",currentAgentInfo.get("user_id"));
        currentAgentInfo.put("agentTypeCd",currentAgentInfo.get("agent_type_cd"));
        currentAgentInfo.put("nearbyLandmarks",currentAgentInfo.get("nearby_landmarks"));
        currentAgentInfo.put("mapX",currentAgentInfo.get("map_x"));
        currentAgentInfo.put("mapY",currentAgentInfo.get("map_y"));
        currentAgentInfo.put("operate",StatusConstant.OPERATE_DEL);
        getAgentServiceDaoImpl().saveBusinessAgentInfo(currentAgentInfo);
    }

    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     * @param business 当前业务
     * @param agentAttr 代理商属性
     */
    protected void autoSaveDelBusinessAgentAttr(Business business, JSONObject agentAttr){
        Map info = new HashMap();
        info.put("attrId",agentAttr.getString("attrId"));
        info.put("agentId",agentAttr.getString("agentId"));
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        List<Map> currentAgentAttrs = getAgentServiceDaoImpl().getAgentAttrs(info);
        if(currentAgentAttrs == null || currentAgentAttrs.size() != 1){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"未找到需要修改数据信息，入参错误或数据有问题，请检查"+info);
        }
        Map currentAgentAttr = currentAgentAttrs.get(0);
        currentAgentAttr.put("bId",business.getbId());
        currentAgentAttr.put("attrId",currentAgentAttr.get("attr_id"));
        currentAgentAttr.put("agentId",currentAgentAttr.get("agent_id"));
        currentAgentAttr.put("specCd",currentAgentAttr.get("spec_cd"));
        currentAgentAttr.put("operate",StatusConstant.OPERATE_DEL);
        getAgentServiceDaoImpl().saveBusinessAgentAttr(currentAgentAttr);
    }

    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     * @param business
     * @param businessAgentPhoto 代理商照片
     */
    protected void autoSaveDelBusinessAgentPhoto(Business business,JSONObject businessAgentPhoto){
       Map info = new HashMap();
        info.put("agentPhotoId",businessAgentPhoto.getString("agentPhotoId"));
        info.put("agentId",businessAgentPhoto.getString("agentId"));
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        List<Map> currentAgentPhotos = getAgentServiceDaoImpl().getAgentPhoto(info);
        if(currentAgentPhotos == null || currentAgentPhotos.size() != 1){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"未找到需要修改数据信息，入参错误或数据有问题，请检查"+info);
        }
        Map currentAgentPhoto = currentAgentPhotos.get(0);
        currentAgentPhoto.put("bId",business.getbId());
        currentAgentPhoto.put("agentPhotoId",currentAgentPhoto.get("agent_photo_id"));
        currentAgentPhoto.put("agentId",currentAgentPhoto.get("agent_id"));
        currentAgentPhoto.put("agentPhotoTypeCd",currentAgentPhoto.get("agent_photo_type_cd"));
        currentAgentPhoto.put("operate",StatusConstant.OPERATE_DEL);
        getAgentServiceDaoImpl().saveBusinessAgentPhoto(currentAgentPhoto);
    }

    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     * @param business
     * @param businessAgentCerdentials 代理商证件
     */
    protected void autoSaveDelBusinessAgentCerdentials(Business business,JSONObject businessAgentCerdentials){
        Map info = new HashMap();
        info.put("agentCerdentialsId",businessAgentCerdentials.getString("agentCerdentialsId"));
        info.put("agentId",businessAgentCerdentials.getString("agentId"));
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        List<Map> currentAgentCerdentailses = getAgentServiceDaoImpl().getAgentCerdentials(info);
        if(currentAgentCerdentailses == null || currentAgentCerdentailses.size() != 1){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"未找到需要修改数据信息，入参错误或数据有问题，请检查"+info);
        }
        Map currentAgentCerdentials = currentAgentCerdentailses.get(0);

        currentAgentCerdentials.put("bId",business.getbId());
        currentAgentCerdentials.put("agentCerdentialsId",currentAgentCerdentials.get("agent_cerdentials_id"));
        currentAgentCerdentials.put("agentId",currentAgentCerdentials.get("agent_id"));
        currentAgentCerdentials.put("credentialsCd",currentAgentCerdentials.get("credentials_cd"));
        currentAgentCerdentials.put("validityPeriod",currentAgentCerdentials.get("validity_period"));
        currentAgentCerdentials.put("positivePhoto",currentAgentCerdentials.get("positive_photo"));
        currentAgentCerdentials.put("negativePhoto",currentAgentCerdentials.get("negative_photo"));
        currentAgentCerdentials.put("operate",StatusConstant.OPERATE_DEL);
        getAgentServiceDaoImpl().saveBusinessAgentCerdentials(currentAgentCerdentials);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     * @param businessMemberAgent 代理商信息
     */
    protected void autoSaveDelBusinessMemberAgent(Business business, JSONObject businessMemberAgent){
//自动插入DEL
        Map info = new HashMap();
        info.put("memberAgentId",businessMemberAgent.getString("memberAgentId"));
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        Map currentMemberAgent = getAgentServiceDaoImpl().getMemberAgent(info);
        if(currentMemberAgent == null || currentMemberAgent.isEmpty()){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"未找到需要修改数据信息，入参错误或数据有问题，请检查"+info);
        }
        currentMemberAgent.put("bId",business.getbId());
        currentMemberAgent.put("agentId",currentMemberAgent.get("agent_id"));
        currentMemberAgent.put("memberAgentId",currentMemberAgent.get("member_agent_id"));
        currentMemberAgent.put("memberId",currentMemberAgent.get("member_id"));
        currentMemberAgent.put("operate",StatusConstant.OPERATE_DEL);
        getAgentServiceDaoImpl().saveBusinessAgentInfo(currentMemberAgent);
    }
}
