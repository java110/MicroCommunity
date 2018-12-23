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
     * 刷新 businessAgentInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     * @param businessAgentHouse
     */
    protected void flushBusinessAgentHouse(Map businessAgentHouse,String statusCd){
        businessAgentHouse.put("newBId",businessAgentHouse.get("b_id"));
        businessAgentHouse.put("agentId",businessAgentHouse.get("agent_id"));
        businessAgentHouse.put("houseId",businessAgentHouse.get("house_id"));
        businessAgentHouse.put("houseNum",businessAgentHouse.get("house_num"));
        businessAgentHouse.put("houseName",businessAgentHouse.get("house_name"));
        businessAgentHouse.put("housePhone",businessAgentHouse.get("house_phone"));
        businessAgentHouse.put("houseArea",businessAgentHouse.get("house_area"));
        businessAgentHouse.put("feeTypeCd",businessAgentHouse.get("fee_type_cd"));
        businessAgentHouse.put("feePrice",businessAgentHouse.get("fee_price"));
        businessAgentHouse.put("statusCd", statusCd);
    }

    /**
     刷新 businessAgentAttr 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     * @param businessAgentHouesAttr
     * @param statusCd
     */
    protected void flushBusinessAgentHouseAttr(Map businessAgentHouesAttr,String statusCd){
        businessAgentHouesAttr.put("attrId",businessAgentHouesAttr.get("attr_id"));
        businessAgentHouesAttr.put("specCd",businessAgentHouesAttr.get("spec_cd"));
        businessAgentHouesAttr.put("houseId",businessAgentHouesAttr.get("house_id"));
        businessAgentHouesAttr.put("newBId",businessAgentHouesAttr.get("b_id"));
        businessAgentHouesAttr.put("statusCd",statusCd);
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
     * 刷新 businessAgentPhoto 数据
     * @param businessAgentUser
     * @param statusCd
     */
    protected void flushBusinessAgentUser(Map businessAgentUser,String statusCd){
        businessAgentUser.put("agentId",businessAgentUser.get("agent_id"));
        businessAgentUser.put("agentUserId",businessAgentUser.get("agent_user_id"));
        businessAgentUser.put("userId",businessAgentUser.get("user_id"));
        businessAgentUser.put("relCd",businessAgentUser.get("rel_cd"));
        businessAgentUser.put("newBId",businessAgentUser.get("b_id"));
        businessAgentUser.put("statusCd",statusCd);
    }


    /**
     * 刷新 businessAgentPhoto 数据
     * @param businessAgentFee
     * @param statusCd
     */
    protected void flushBusinessAgentFee(Map businessAgentFee,String statusCd){
        businessAgentFee.put("agentId",businessAgentFee.get("agent_id"));
        businessAgentFee.put("feeId",businessAgentFee.get("fee_id"));
        businessAgentFee.put("agentId",businessAgentFee.get("agent_id"));
        businessAgentFee.put("feeTypeCd",businessAgentFee.get("fee_type_cd"));

        businessAgentFee.put("feeMoney",businessAgentFee.get("fee_money"));
        businessAgentFee.put("feeTime",businessAgentFee.get("fee_time"));
        businessAgentFee.put("startTime",businessAgentFee.get("start_time"));
        businessAgentFee.put("endTime",businessAgentFee.get("end_time"));
        businessAgentFee.put("newBId",businessAgentFee.get("b_id"));
        businessAgentFee.put("statusCd",statusCd);
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
        //这个不允许修改，这里直接写入查出来的
        agentAttr.put("specCd",currentAgentAttr.get("spec_cd"));
    }

    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     * @param businessAgentHouse 代理商信息
     */
    protected void autoSaveDelBusinessAgentHouse(Business business, JSONObject businessAgentHouse){
//自动插入DEL
        Map info = new HashMap();
        info.put("houseId",businessAgentHouse.getString("houseId"));
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        Map currentAgentHouse = getAgentServiceDaoImpl().getAgentHouse(info);
        if(currentAgentHouse == null || currentAgentHouse.isEmpty()){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"未找到需要修改数据信息，入参错误或数据有问题，请检查"+info);
        }
        currentAgentHouse.put("bId",business.getbId());
        currentAgentHouse.put("agentId",currentAgentHouse.get("agent_id"));
        currentAgentHouse.put("houseId",currentAgentHouse.get("house_id"));
        currentAgentHouse.put("houseNum",currentAgentHouse.get("house_num"));
        currentAgentHouse.put("houseName",currentAgentHouse.get("house_name"));
        currentAgentHouse.put("housePhone",currentAgentHouse.get("house_phone"));
        currentAgentHouse.put("houseArea",currentAgentHouse.get("house_area"));
        currentAgentHouse.put("feeTypeCd",currentAgentHouse.get("fee_type_cd"));
        currentAgentHouse.put("feePrice",currentAgentHouse.get("fee_price"));
        currentAgentHouse.put("operate",StatusConstant.OPERATE_DEL);
        getAgentServiceDaoImpl().saveBusinessAgentHouse(currentAgentHouse);
    }

    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     * @param business 当前业务
     * @param agentHouseAttr 代理商属性
     */
    protected void autoSaveDelBusinessAgentHouseAttr(Business business, JSONObject agentHouseAttr){
        Map info = new HashMap();
        info.put("attrId",agentHouseAttr.getString("attrId"));
        info.put("houseId",agentHouseAttr.getString("houseId"));
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        List<Map> currentAgentHouseAttrs = getAgentServiceDaoImpl().getAgentHouseAttrs(info);
        if(currentAgentHouseAttrs == null || currentAgentHouseAttrs.size() != 1){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"未找到需要修改数据信息，入参错误或数据有问题，请检查"+info);
        }
        Map currentAgentHouseAttr = currentAgentHouseAttrs.get(0);
        currentAgentHouseAttr.put("bId",business.getbId());
        currentAgentHouseAttr.put("attrId",currentAgentHouseAttr.get("attr_id"));
        currentAgentHouseAttr.put("houseId",currentAgentHouseAttr.get("house_id"));
        currentAgentHouseAttr.put("specCd",currentAgentHouseAttr.get("spec_cd"));
        currentAgentHouseAttr.put("operate",StatusConstant.OPERATE_DEL);
        getAgentServiceDaoImpl().saveBusinessAgentHouseAttr(currentAgentHouseAttr);
        //这个不允许修改，这里直接写入查出来的
        agentHouseAttr.put("specCd",currentAgentHouseAttr.get("spec_cd"));
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
     * @param businessAgentUser 代理商用户
     */
    protected void autoSaveDelBusinessAgentUser(Business business,JSONObject businessAgentUser){
        Map info = new HashMap();
        info.put("agentUserId",businessAgentUser.getString("agentUserId"));
        info.put("agentId",businessAgentUser.getString("agentId"));
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        List<Map> currentAgentUsers = getAgentServiceDaoImpl().getAgentUser(info);
        if(currentAgentUsers == null || currentAgentUsers.size() != 1){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"未找到需要修改数据信息，入参错误或数据有问题，请检查"+info);
        }
        Map currentAgentUser = currentAgentUsers.get(0);
        currentAgentUser.put("bId",business.getbId());
        currentAgentUser.put("agentUserId",currentAgentUser.get("agent_user_id"));
        currentAgentUser.put("agentId",currentAgentUser.get("agent_id"));
        currentAgentUser.put("userId",currentAgentUser.get("user_id"));
        currentAgentUser.put("relCd",currentAgentUser.get("rel_cd"));
        currentAgentUser.put("operate",StatusConstant.OPERATE_DEL);
        getAgentServiceDaoImpl().saveBusinessAgentUser(currentAgentUser);
    }

    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     * @param business
     * @param businessAgentFee 代理商费用
     */
    protected void autoSaveDelBusinessAgentFee(Business business,JSONObject businessAgentFee){
        Map info = new HashMap();
        info.put("feeId",businessAgentFee.getString("feeId"));
        info.put("agentId",businessAgentFee.getString("agentId"));
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        List<Map> currentAgentFees = getAgentServiceDaoImpl().getAgentFee(info);
        if(currentAgentFees == null || currentAgentFees.size() != 1){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"未找到需要修改数据信息，入参错误或数据有问题，请检查"+info);
        }
        Map currentAgentFee = currentAgentFees.get(0);
        currentAgentFee.put("bId",business.getbId());
        currentAgentFee.put("feeId",currentAgentFee.get("fee_id"));
        currentAgentFee.put("agentId",currentAgentFee.get("agent_id"));
        currentAgentFee.put("feeTypeCd",currentAgentFee.get("fee_type_cd"));

        currentAgentFee.put("feeMoney",currentAgentFee.get("fee_money"));
        currentAgentFee.put("feeTime",currentAgentFee.get("fee_time"));
        currentAgentFee.put("startTime",currentAgentFee.get("start_time"));
        currentAgentFee.put("endTime",currentAgentFee.get("end_time"));
        currentAgentFee.put("operate",StatusConstant.OPERATE_DEL);
        getAgentServiceDaoImpl().saveBusinessAgentFee(currentAgentFee);

        businessAgentFee.put("feeTypeCd",currentAgentFee.get("fee_type_cd"));
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

        //证件类型不让修改
        businessAgentCerdentials.put("credentialsCd",currentAgentCerdentials.get("credentials_cd"));
    }



}
