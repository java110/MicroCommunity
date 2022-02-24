package com.java110.store.listener;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.entity.center.Business;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.store.dao.IStoreServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 商户 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractStoreBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener{
    private final static Logger logger = LoggerFactory.getLogger(AbstractStoreBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     * @return
     */
    public abstract IStoreServiceDao getStoreServiceDaoImpl();

    /**
     * 刷新 businessStoreInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     * @param businessStoreInfo
     */
    protected void flushBusinessStoreInfo(Map businessStoreInfo,String statusCd){
        businessStoreInfo.put("newBId",businessStoreInfo.get("b_id"));
        businessStoreInfo.put("storeId",businessStoreInfo.get("store_id"));
        businessStoreInfo.put("userId",businessStoreInfo.get("user_id"));
        businessStoreInfo.put("storeTypeCd",businessStoreInfo.get("store_type_cd"));
        businessStoreInfo.put("nearbyLandmarks",businessStoreInfo.get("nearby_landmarks"));
        businessStoreInfo.put("mapX",businessStoreInfo.get("map_x"));
        businessStoreInfo.put("mapY",businessStoreInfo.get("map_y"));
        businessStoreInfo.put("statusCd", statusCd);
    }

    /**
        刷新 businessStoreAttr 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     * @param businessStoreAttr
     * @param statusCd
     */
    protected void flushBusinessStoreAttr(Map businessStoreAttr,String statusCd){
        businessStoreAttr.put("attrId",businessStoreAttr.get("attr_id"));
        businessStoreAttr.put("specCd",businessStoreAttr.get("spec_cd"));
        businessStoreAttr.put("storeId",businessStoreAttr.get("store_id"));
        businessStoreAttr.put("newBId",businessStoreAttr.get("b_id"));
        businessStoreAttr.put("statusCd",statusCd);
    }

    /**
     * 刷新 businessStorePhoto 数据
     * @param businessStorePhoto
     * @param statusCd
     */
    protected void flushBusinessStorePhoto(Map businessStorePhoto,String statusCd){
        businessStorePhoto.put("storeId",businessStorePhoto.get("store_id"));
        businessStorePhoto.put("storePhotoId",businessStorePhoto.get("store_photo_id"));
        businessStorePhoto.put("storePhotoTypeCd",businessStorePhoto.get("store_photo_type_cd"));
        businessStorePhoto.put("newBId",businessStorePhoto.get("b_id"));
        businessStorePhoto.put("statusCd",statusCd);
    }

    /**
     * 刷新 businessStoreCerdentials 数据
     * @param businessStoreCerdentials
     * @param statusCd
     */
    protected void flushBusinessStoreCredentials(Map businessStoreCerdentials ,String statusCd){
        businessStoreCerdentials.put("storeId",businessStoreCerdentials.get("store_id"));
        businessStoreCerdentials.put("storeCerdentialsId",businessStoreCerdentials.get("store_cerdentials_id"));
        businessStoreCerdentials.put("credentialsCd",businessStoreCerdentials.get("credentials_cd"));
        businessStoreCerdentials.put("validityPeriod",businessStoreCerdentials.get("validity_period"));
        businessStoreCerdentials.put("positivePhoto",businessStoreCerdentials.get("positive_photo"));
        businessStoreCerdentials.put("negativePhoto",businessStoreCerdentials.get("negative_photo"));
        businessStoreCerdentials.put("newBId",businessStoreCerdentials.get("b_id"));
        businessStoreCerdentials.put("statusCd",statusCd);
    }

    /**
     * 刷新 businessMemberStore 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     * @param businessMemberStore
     */
    protected void flushBusinessMemberStore(Map businessMemberStore,String statusCd){
        businessMemberStore.put("newBId",businessMemberStore.get("b_id"));
        businessMemberStore.put("storeId",businessMemberStore.get("store_id"));
        businessMemberStore.put("memberStoreId",businessMemberStore.get("member_store_id"));
        businessMemberStore.put("memberId",businessMemberStore.get("member_id"));
        businessMemberStore.put("statusCd", statusCd);
    }

    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     * @param businessStore 商户信息
     */
    protected void autoSaveDelBusinessStore(Business business, JSONObject businessStore){
//自动插入DEL
        Map info = new HashMap();
        info.put("storeId",businessStore.getString("storeId"));
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        Map currentStoreInfo = getStoreServiceDaoImpl().getStoreInfo(info);
        if(currentStoreInfo == null || currentStoreInfo.isEmpty()){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"未找到需要修改数据信息，入参错误或数据有问题，请检查"+info);
        }
        currentStoreInfo.put("bId",business.getbId());
        currentStoreInfo.put("storeId",currentStoreInfo.get("store_id"));
        currentStoreInfo.put("userId",currentStoreInfo.get("user_id"));
        currentStoreInfo.put("storeTypeCd",currentStoreInfo.get("store_type_cd"));
        currentStoreInfo.put("nearbyLandmarks",currentStoreInfo.get("nearby_landmarks"));
        currentStoreInfo.put("mapX",currentStoreInfo.get("map_x"));
        currentStoreInfo.put("mapY",currentStoreInfo.get("map_y"));
        currentStoreInfo.put("operate",StatusConstant.OPERATE_DEL);
        getStoreServiceDaoImpl().saveBusinessStoreInfo(currentStoreInfo);

        for (Object key : currentStoreInfo.keySet()) {
            if (businessStore.get(key) == null) {
                businessStore.put(key.toString(), currentStoreInfo.get(key));
            }
        }
    }

    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     * @param business 当前业务
     * @param storeAttr 商户属性
     */
    protected void autoSaveDelBusinessStoreAttr(Business business, JSONObject storeAttr){
        Map info = new HashMap();
        info.put("attrId",storeAttr.getString("attrId"));
        info.put("storeId",storeAttr.getString("storeId"));
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        List<Map> currentStoreAttrs = getStoreServiceDaoImpl().getStoreAttrs(info);
        if(currentStoreAttrs == null || currentStoreAttrs.size() != 1){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"未找到需要修改数据信息，入参错误或数据有问题，请检查"+info);
        }
        Map currentStoreAttr = currentStoreAttrs.get(0);
        currentStoreAttr.put("bId",business.getbId());
        currentStoreAttr.put("attrId",currentStoreAttr.get("attr_id"));
        currentStoreAttr.put("storeId",currentStoreAttr.get("store_id"));
        currentStoreAttr.put("specCd",currentStoreAttr.get("spec_cd"));
        currentStoreAttr.put("operate",StatusConstant.OPERATE_DEL);
        getStoreServiceDaoImpl().saveBusinessStoreAttr(currentStoreAttr);
        for (Object key : currentStoreAttr.keySet()) {
            if (storeAttr.get(key) == null) {
                storeAttr.put(key.toString(), currentStoreAttr.get(key));
            }
        }
    }

    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     * @param business
     * @param businessStorePhoto 商户照片
     */
    protected void autoSaveDelBusinessStorePhoto(Business business,JSONObject businessStorePhoto){
       Map info = new HashMap();
        info.put("storePhotoId",businessStorePhoto.getString("storePhotoId"));
        info.put("storeId",businessStorePhoto.getString("storeId"));
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        List<Map> currentStorePhotos = getStoreServiceDaoImpl().getStorePhoto(info);
        if(currentStorePhotos == null || currentStorePhotos.size() != 1){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"未找到需要修改数据信息，入参错误或数据有问题，请检查"+info);
        }
        Map currentStorePhoto = currentStorePhotos.get(0);
        currentStorePhoto.put("bId",business.getbId());
        currentStorePhoto.put("storePhotoId",currentStorePhoto.get("store_photo_id"));
        currentStorePhoto.put("storeId",currentStorePhoto.get("store_id"));
        currentStorePhoto.put("storePhotoTypeCd",currentStorePhoto.get("store_photo_type_cd"));
        currentStorePhoto.put("operate",StatusConstant.OPERATE_DEL);
        getStoreServiceDaoImpl().saveBusinessStorePhoto(currentStorePhoto);
        for (Object key : currentStorePhoto.keySet()) {
            if (businessStorePhoto.get(key) == null) {
                businessStorePhoto.put(key.toString(), currentStorePhoto.get(key));
            }
        }
    }

    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     * @param business
     * @param businessStoreCerdentials 商户证件
     */
    protected void autoSaveDelBusinessStoreCerdentials(Business business,JSONObject businessStoreCerdentials){
        Map info = new HashMap();
        info.put("storeCerdentialsId",businessStoreCerdentials.getString("storeCerdentialsId"));
        info.put("storeId",businessStoreCerdentials.getString("storeId"));
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        List<Map> currentStoreCerdentailses = getStoreServiceDaoImpl().getStoreCerdentials(info);
        if(currentStoreCerdentailses == null || currentStoreCerdentailses.size() != 1){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"未找到需要修改数据信息，入参错误或数据有问题，请检查"+info);
        }
        Map currentStoreCerdentials = currentStoreCerdentailses.get(0);

        currentStoreCerdentials.put("bId",business.getbId());
        currentStoreCerdentials.put("storeCerdentialsId",currentStoreCerdentials.get("store_cerdentials_id"));
        currentStoreCerdentials.put("storeId",currentStoreCerdentials.get("store_id"));
        currentStoreCerdentials.put("credentialsCd",currentStoreCerdentials.get("credentials_cd"));
        currentStoreCerdentials.put("validityPeriod",currentStoreCerdentials.get("validity_period"));
        currentStoreCerdentials.put("positivePhoto",currentStoreCerdentials.get("positive_photo"));
        currentStoreCerdentials.put("negativePhoto",currentStoreCerdentials.get("negative_photo"));
        currentStoreCerdentials.put("operate",StatusConstant.OPERATE_DEL);
        getStoreServiceDaoImpl().saveBusinessStoreCerdentials(currentStoreCerdentials);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     * @param businessMemberStore 商户信息
     */
    protected void autoSaveDelBusinessMemberStore(Business business, JSONObject businessMemberStore){
//自动插入DEL
        Map info = new HashMap();
        info.put("memberStoreId",businessMemberStore.getString("memberStoreId"));
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        Map currentMemberStore = getStoreServiceDaoImpl().getMemberStore(info);
        if(currentMemberStore == null || currentMemberStore.isEmpty()){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"未找到需要修改数据信息，入参错误或数据有问题，请检查"+info);
        }
        currentMemberStore.put("bId",business.getbId());
        currentMemberStore.put("storeId",currentMemberStore.get("store_id"));
        currentMemberStore.put("memberStoreId",currentMemberStore.get("member_store_id"));
        currentMemberStore.put("memberId",currentMemberStore.get("member_id"));
        currentMemberStore.put("operate",StatusConstant.OPERATE_DEL);
        getStoreServiceDaoImpl().saveBusinessStoreInfo(currentMemberStore);
    }

    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     * @param businessStoreUser 商户信息
     */
    protected void autoSaveDelBusinessStoreUser(Business business, JSONObject businessStoreUser){
        //自动插入DEL
        Map info = new HashMap();
        info.put("storeId",businessStoreUser.getString("storeId"));
        info.put("userId",businessStoreUser.getString("userId"));
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        Map currentStoreUser = getStoreServiceDaoImpl().getStoreUser(info).get(0);
        if(currentStoreUser == null || currentStoreUser.isEmpty()){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"未找到需要修改数据信息，入参错误或数据有问题，请检查"+info);
        }
        currentStoreUser.put("bId",business.getbId());
        currentStoreUser.put("storeUserId", currentStoreUser.get("store_user_id"));
        currentStoreUser.put("storeId",currentStoreUser.get("store_id"));
        currentStoreUser.put("userId",currentStoreUser.get("user_id"));
        currentStoreUser.put("relCd",currentStoreUser.get("rel_cd"));
        currentStoreUser.put("operate",StatusConstant.OPERATE_DEL);
        getStoreServiceDaoImpl().saveBusinessStoreUser(currentStoreUser);
    }

    /**
     * 刷新 businessMemberStore 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     * @param businessMemberStore
     */
    protected void flushBusinessStoreUser(Map businessMemberStore,String statusCd){
        businessMemberStore.put("newBId",businessMemberStore.get("b_id"));
        businessMemberStore.put("storeUserId", businessMemberStore.get("store_user_id"));
        businessMemberStore.put("storeId",businessMemberStore.get("store_id"));
        businessMemberStore.put("userId",businessMemberStore.get("user_id"));
        businessMemberStore.put("relCd",businessMemberStore.get("rel_cd"));
        businessMemberStore.put("statusCd", statusCd);
    }



}
