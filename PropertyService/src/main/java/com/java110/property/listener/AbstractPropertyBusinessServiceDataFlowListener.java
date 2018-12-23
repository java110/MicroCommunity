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
     * 刷新 businessPropertyInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     * @param businessPropertyHouse
     */
    protected void flushBusinessPropertyHouse(Map businessPropertyHouse,String statusCd){
        businessPropertyHouse.put("newBId",businessPropertyHouse.get("b_id"));
        businessPropertyHouse.put("propertyId",businessPropertyHouse.get("property_id"));
        businessPropertyHouse.put("houseId",businessPropertyHouse.get("house_id"));
        businessPropertyHouse.put("houseNum",businessPropertyHouse.get("house_num"));
        businessPropertyHouse.put("houseName",businessPropertyHouse.get("house_name"));
        businessPropertyHouse.put("housePhone",businessPropertyHouse.get("house_phone"));
        businessPropertyHouse.put("houseArea",businessPropertyHouse.get("house_area"));
        businessPropertyHouse.put("feeTypeCd",businessPropertyHouse.get("fee_type_cd"));
        businessPropertyHouse.put("feePrice",businessPropertyHouse.get("fee_price"));
        businessPropertyHouse.put("statusCd", statusCd);
    }

    /**
     刷新 businessPropertyAttr 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     * @param businessPropertyHouesAttr
     * @param statusCd
     */
    protected void flushBusinessPropertyHouseAttr(Map businessPropertyHouesAttr,String statusCd){
        businessPropertyHouesAttr.put("attrId",businessPropertyHouesAttr.get("attr_id"));
        businessPropertyHouesAttr.put("specCd",businessPropertyHouesAttr.get("spec_cd"));
        businessPropertyHouesAttr.put("houseId",businessPropertyHouesAttr.get("house_id"));
        businessPropertyHouesAttr.put("newBId",businessPropertyHouesAttr.get("b_id"));
        businessPropertyHouesAttr.put("statusCd",statusCd);
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
     * 刷新 businessPropertyPhoto 数据
     * @param businessPropertyFee
     * @param statusCd
     */
    protected void flushBusinessPropertyFee(Map businessPropertyFee,String statusCd){
        businessPropertyFee.put("propertyId",businessPropertyFee.get("property_id"));
        businessPropertyFee.put("feeId",businessPropertyFee.get("fee_id"));
        businessPropertyFee.put("propertyId",businessPropertyFee.get("property_id"));
        businessPropertyFee.put("feeTypeCd",businessPropertyFee.get("fee_type_cd"));

        businessPropertyFee.put("feeMoney",businessPropertyFee.get("fee_money"));
        businessPropertyFee.put("feeTime",businessPropertyFee.get("fee_time"));
        businessPropertyFee.put("startTime",businessPropertyFee.get("start_time"));
        businessPropertyFee.put("endTime",businessPropertyFee.get("end_time"));
        businessPropertyFee.put("newBId",businessPropertyFee.get("b_id"));
        businessPropertyFee.put("statusCd",statusCd);
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
        //这个不允许修改，这里直接写入查出来的
        propertyAttr.put("specCd",currentPropertyAttr.get("spec_cd"));
    }

    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     * @param businessPropertyHouse 物业信息
     */
    protected void autoSaveDelBusinessPropertyHouse(Business business, JSONObject businessPropertyHouse){
//自动插入DEL
        Map info = new HashMap();
        info.put("houseId",businessPropertyHouse.getString("houseId"));
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        Map currentPropertyHouse = getPropertyServiceDaoImpl().getPropertyHouse(info);
        if(currentPropertyHouse == null || currentPropertyHouse.isEmpty()){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"未找到需要修改数据信息，入参错误或数据有问题，请检查"+info);
        }
        currentPropertyHouse.put("bId",business.getbId());
        currentPropertyHouse.put("propertyId",currentPropertyHouse.get("property_id"));
        currentPropertyHouse.put("houseId",currentPropertyHouse.get("house_id"));
        currentPropertyHouse.put("houseNum",currentPropertyHouse.get("house_num"));
        currentPropertyHouse.put("houseName",currentPropertyHouse.get("house_name"));
        currentPropertyHouse.put("housePhone",currentPropertyHouse.get("house_phone"));
        currentPropertyHouse.put("houseArea",currentPropertyHouse.get("house_area"));
        currentPropertyHouse.put("feeTypeCd",currentPropertyHouse.get("fee_type_cd"));
        currentPropertyHouse.put("feePrice",currentPropertyHouse.get("fee_price"));
        currentPropertyHouse.put("operate",StatusConstant.OPERATE_DEL);
        getPropertyServiceDaoImpl().saveBusinessPropertyHouse(currentPropertyHouse);
    }

    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     * @param business 当前业务
     * @param propertyHouseAttr 物业属性
     */
    protected void autoSaveDelBusinessPropertyHouseAttr(Business business, JSONObject propertyHouseAttr){
        Map info = new HashMap();
        info.put("attrId",propertyHouseAttr.getString("attrId"));
        info.put("houseId",propertyHouseAttr.getString("houseId"));
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        List<Map> currentPropertyHouseAttrs = getPropertyServiceDaoImpl().getPropertyHouseAttrs(info);
        if(currentPropertyHouseAttrs == null || currentPropertyHouseAttrs.size() != 1){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"未找到需要修改数据信息，入参错误或数据有问题，请检查"+info);
        }
        Map currentPropertyHouseAttr = currentPropertyHouseAttrs.get(0);
        currentPropertyHouseAttr.put("bId",business.getbId());
        currentPropertyHouseAttr.put("attrId",currentPropertyHouseAttr.get("attr_id"));
        currentPropertyHouseAttr.put("houseId",currentPropertyHouseAttr.get("house_id"));
        currentPropertyHouseAttr.put("specCd",currentPropertyHouseAttr.get("spec_cd"));
        currentPropertyHouseAttr.put("operate",StatusConstant.OPERATE_DEL);
        getPropertyServiceDaoImpl().saveBusinessPropertyHouseAttr(currentPropertyHouseAttr);
        //这个不允许修改，这里直接写入查出来的
        propertyHouseAttr.put("specCd",currentPropertyHouseAttr.get("spec_cd"));
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
     * @param businessPropertyFee 物业费用
     */
    protected void autoSaveDelBusinessPropertyFee(Business business,JSONObject businessPropertyFee){
        Map info = new HashMap();
        info.put("feeId",businessPropertyFee.getString("feeId"));
        info.put("propertyId",businessPropertyFee.getString("propertyId"));
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        List<Map> currentPropertyFees = getPropertyServiceDaoImpl().getPropertyFee(info);
        if(currentPropertyFees == null || currentPropertyFees.size() != 1){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"未找到需要修改数据信息，入参错误或数据有问题，请检查"+info);
        }
        Map currentPropertyFee = currentPropertyFees.get(0);
        currentPropertyFee.put("bId",business.getbId());
        currentPropertyFee.put("feeId",currentPropertyFee.get("fee_id"));
        currentPropertyFee.put("propertyId",currentPropertyFee.get("property_id"));
        currentPropertyFee.put("feeTypeCd",currentPropertyFee.get("fee_type_cd"));

        currentPropertyFee.put("feeMoney",currentPropertyFee.get("fee_money"));
        currentPropertyFee.put("feeTime",currentPropertyFee.get("fee_time"));
        currentPropertyFee.put("startTime",currentPropertyFee.get("start_time"));
        currentPropertyFee.put("endTime",currentPropertyFee.get("end_time"));
        currentPropertyFee.put("operate",StatusConstant.OPERATE_DEL);
        getPropertyServiceDaoImpl().saveBusinessPropertyFee(currentPropertyFee);

        businessPropertyFee.put("feeTypeCd",currentPropertyFee.get("fee_type_cd"));
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

        //证件类型不让修改
        businessPropertyCerdentials.put("credentialsCd",currentPropertyCerdentials.get("credentials_cd"));
    }



}
