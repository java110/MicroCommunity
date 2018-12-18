package com.java110.property.listener;

import com.alibaba.fastjson.JSONObject;
import com.java110.property.dao.IPropertyServiceDao;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.constant.StatusConstant;
import com.java110.common.exception.ListenerExecuteException;
import com.java110.entity.center.Business;
import com.java110.event.service.AbstractBusinessServiceDataFlowListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 物业 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractPropertyBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener{


    /**
     * 获取 DAO工具类
     * @return
     */
    public abstract IPropertyServiceDao getPropertyServiceDaoImpl();

    /**
     * 刷新 businessPropertyInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     * @param businessPropertyInfo
     */
    protected void flushBusinessPropertyInfo(Map businessPropertyInfo,String statusCd){
        businessPropertyInfo.put("newBId",businessPropertyInfo.get("b_id"));
        businessPropertyInfo.put("propertyId",businessPropertyInfo.get("property_id"));
        businessPropertyInfo.put("userId",businessPropertyInfo.get("user_id"));
        businessPropertyInfo.put("propertyTypeCd",businessPropertyInfo.get("property_type_cd"));
        businessPropertyInfo.put("nearbyLandmarks",businessPropertyInfo.get("nearby_landmarks"));
        businessPropertyInfo.put("mapX",businessPropertyInfo.get("map_x"));
        businessPropertyInfo.put("mapY",businessPropertyInfo.get("map_y"));
        businessPropertyInfo.put("statusCd", statusCd);
    }

    /**
        刷新 businessPropertyAttr 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     * @param businessPropertyAttr
     * @param statusCd
     */
    protected void flushBusinessPropertyAttr(Map businessPropertyAttr,String statusCd){
        businessPropertyAttr.put("attrId",businessPropertyAttr.get("attr_id"));
        businessPropertyAttr.put("specCd",businessPropertyAttr.get("spec_cd"));
        businessPropertyAttr.put("propertyId",businessPropertyAttr.get("property_id"));
        businessPropertyAttr.put("newBId",businessPropertyAttr.get("b_id"));
        businessPropertyAttr.put("statusCd",statusCd);
    }

    /**
     * 刷新 businessPropertyPhoto 数据
     * @param businessPropertyPhoto
     * @param statusCd
     */
    protected void flushBusinessPropertyPhoto(Map businessPropertyPhoto,String statusCd){
        businessPropertyPhoto.put("propertyId",businessPropertyPhoto.get("property_id"));
        businessPropertyPhoto.put("propertyPhotoId",businessPropertyPhoto.get("property_photo_id"));
        businessPropertyPhoto.put("propertyPhotoTypeCd",businessPropertyPhoto.get("property_photo_type_cd"));
        businessPropertyPhoto.put("newBId",businessPropertyPhoto.get("b_id"));
        businessPropertyPhoto.put("statusCd",statusCd);
    }

    /**
     * 刷新 businessPropertyCerdentials 数据
     * @param businessPropertyCerdentials
     * @param statusCd
     */
    protected void flushBusinessPropertyCredentials(Map businessPropertyCerdentials ,String statusCd){
        businessPropertyCerdentials.put("propertyId",businessPropertyCerdentials.get("property_id"));
        businessPropertyCerdentials.put("propertyCerdentialsId",businessPropertyCerdentials.get("property_cerdentials_id"));
        businessPropertyCerdentials.put("credentialsCd",businessPropertyCerdentials.get("credentials_cd"));
        businessPropertyCerdentials.put("validityPeriod",businessPropertyCerdentials.get("validity_period"));
        businessPropertyCerdentials.put("positivePhoto",businessPropertyCerdentials.get("positive_photo"));
        businessPropertyCerdentials.put("negativePhoto",businessPropertyCerdentials.get("negative_photo"));
        businessPropertyCerdentials.put("newBId",businessPropertyCerdentials.get("b_id"));
        businessPropertyCerdentials.put("statusCd",statusCd);
    }

    /**
     * 刷新 businessMemberProperty 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     * @param businessMemberProperty
     */
    protected void flushBusinessMemberProperty(Map businessMemberProperty,String statusCd){
        businessMemberProperty.put("newBId",businessMemberProperty.get("b_id"));
        businessMemberProperty.put("propertyId",businessMemberProperty.get("property_id"));
        businessMemberProperty.put("memberPropertyId",businessMemberProperty.get("member_property_id"));
        businessMemberProperty.put("memberId",businessMemberProperty.get("member_id"));
        businessMemberProperty.put("statusCd", statusCd);
    }

    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     * @param businessProperty 物业信息
     */
    protected void autoSaveDelBusinessProperty(Business business, JSONObject businessProperty){
//自动插入DEL
        Map info = new HashMap();
        info.put("propertyId",businessProperty.getString("propertyId"));
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        Map currentPropertyInfo = getPropertyServiceDaoImpl().getPropertyInfo(info);
        if(currentPropertyInfo == null || currentPropertyInfo.isEmpty()){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"未找到需要修改数据信息，入参错误或数据有问题，请检查"+info);
        }
        currentPropertyInfo.put("bId",business.getbId());
        currentPropertyInfo.put("propertyId",currentPropertyInfo.get("property_id"));
        currentPropertyInfo.put("userId",currentPropertyInfo.get("user_id"));
        currentPropertyInfo.put("propertyTypeCd",currentPropertyInfo.get("property_type_cd"));
        currentPropertyInfo.put("nearbyLandmarks",currentPropertyInfo.get("nearby_landmarks"));
        currentPropertyInfo.put("mapX",currentPropertyInfo.get("map_x"));
        currentPropertyInfo.put("mapY",currentPropertyInfo.get("map_y"));
        currentPropertyInfo.put("operate",StatusConstant.OPERATE_DEL);
        getPropertyServiceDaoImpl().saveBusinessPropertyInfo(currentPropertyInfo);
    }

    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     * @param business 当前业务
     * @param propertyAttr 物业属性
     */
    protected void autoSaveDelBusinessPropertyAttr(Business business, JSONObject propertyAttr){
        Map info = new HashMap();
        info.put("attrId",propertyAttr.getString("attrId"));
        info.put("propertyId",propertyAttr.getString("propertyId"));
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        List<Map> currentPropertyAttrs = getPropertyServiceDaoImpl().getPropertyAttrs(info);
        if(currentPropertyAttrs == null || currentPropertyAttrs.size() != 1){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"未找到需要修改数据信息，入参错误或数据有问题，请检查"+info);
        }
        Map currentPropertyAttr = currentPropertyAttrs.get(0);
        currentPropertyAttr.put("bId",business.getbId());
        currentPropertyAttr.put("attrId",currentPropertyAttr.get("attr_id"));
        currentPropertyAttr.put("propertyId",currentPropertyAttr.get("property_id"));
        currentPropertyAttr.put("specCd",currentPropertyAttr.get("spec_cd"));
        currentPropertyAttr.put("operate",StatusConstant.OPERATE_DEL);
        getPropertyServiceDaoImpl().saveBusinessPropertyAttr(currentPropertyAttr);
    }

    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     * @param business
     * @param businessPropertyPhoto 物业照片
     */
    protected void autoSaveDelBusinessPropertyPhoto(Business business,JSONObject businessPropertyPhoto){
       Map info = new HashMap();
        info.put("propertyPhotoId",businessPropertyPhoto.getString("propertyPhotoId"));
        info.put("propertyId",businessPropertyPhoto.getString("propertyId"));
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        List<Map> currentPropertyPhotos = getPropertyServiceDaoImpl().getPropertyPhoto(info);
        if(currentPropertyPhotos == null || currentPropertyPhotos.size() != 1){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"未找到需要修改数据信息，入参错误或数据有问题，请检查"+info);
        }
        Map currentPropertyPhoto = currentPropertyPhotos.get(0);
        currentPropertyPhoto.put("bId",business.getbId());
        currentPropertyPhoto.put("propertyPhotoId",currentPropertyPhoto.get("property_photo_id"));
        currentPropertyPhoto.put("propertyId",currentPropertyPhoto.get("property_id"));
        currentPropertyPhoto.put("propertyPhotoTypeCd",currentPropertyPhoto.get("property_photo_type_cd"));
        currentPropertyPhoto.put("operate",StatusConstant.OPERATE_DEL);
        getPropertyServiceDaoImpl().saveBusinessPropertyPhoto(currentPropertyPhoto);
    }

    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     * @param business
     * @param businessPropertyCerdentials 物业证件
     */
    protected void autoSaveDelBusinessPropertyCerdentials(Business business,JSONObject businessPropertyCerdentials){
        Map info = new HashMap();
        info.put("propertyCerdentialsId",businessPropertyCerdentials.getString("propertyCerdentialsId"));
        info.put("propertyId",businessPropertyCerdentials.getString("propertyId"));
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        List<Map> currentPropertyCerdentailses = getPropertyServiceDaoImpl().getPropertyCerdentials(info);
        if(currentPropertyCerdentailses == null || currentPropertyCerdentailses.size() != 1){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"未找到需要修改数据信息，入参错误或数据有问题，请检查"+info);
        }
        Map currentPropertyCerdentials = currentPropertyCerdentailses.get(0);

        currentPropertyCerdentials.put("bId",business.getbId());
        currentPropertyCerdentials.put("propertyCerdentialsId",currentPropertyCerdentials.get("property_cerdentials_id"));
        currentPropertyCerdentials.put("propertyId",currentPropertyCerdentials.get("property_id"));
        currentPropertyCerdentials.put("credentialsCd",currentPropertyCerdentials.get("credentials_cd"));
        currentPropertyCerdentials.put("validityPeriod",currentPropertyCerdentials.get("validity_period"));
        currentPropertyCerdentials.put("positivePhoto",currentPropertyCerdentials.get("positive_photo"));
        currentPropertyCerdentials.put("negativePhoto",currentPropertyCerdentials.get("negative_photo"));
        currentPropertyCerdentials.put("operate",StatusConstant.OPERATE_DEL);
        getPropertyServiceDaoImpl().saveBusinessPropertyCerdentials(currentPropertyCerdentials);
    }



}
