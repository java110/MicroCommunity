package com.java110.community.listener.community;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.entity.center.Business;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.community.dao.ICommunityServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 小区 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractCommunityBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(AbstractCommunityBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract ICommunityServiceDao getCommunityServiceDaoImpl();

    /**
     * 刷新 businessCommunityInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessCommunityInfo
     */
    protected void flushBusinessCommunityInfo(Map businessCommunityInfo, String statusCd) {
        businessCommunityInfo.put("newBId", businessCommunityInfo.get("b_id"));
        businessCommunityInfo.put("communityId", businessCommunityInfo.get("community_id"));
        businessCommunityInfo.put("cityCode", businessCommunityInfo.get("city_code"));
        businessCommunityInfo.put("nearbyLandmarks", businessCommunityInfo.get("nearby_landmarks"));
        businessCommunityInfo.put("mapX", businessCommunityInfo.get("map_x"));
        businessCommunityInfo.put("mapY", businessCommunityInfo.get("map_y"));
        businessCommunityInfo.put("state", businessCommunityInfo.get("state"));
        businessCommunityInfo.put("communityArea", businessCommunityInfo.get("community_area"));
        businessCommunityInfo.put("tel", businessCommunityInfo.get("tel"));
        businessCommunityInfo.put("payFeeMonth", businessCommunityInfo.get("pay_fee_month"));
        businessCommunityInfo.put("feePrice", businessCommunityInfo.get("fee_price"));

        businessCommunityInfo.put("statusCd", statusCd);
    }

    /**
     * 刷新 businessCommunityAttr 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessCommunityAttr
     * @param statusCd
     */
    protected void flushBusinessCommunityAttr(Map businessCommunityAttr, String statusCd) {
        businessCommunityAttr.put("attrId", businessCommunityAttr.get("attr_id"));
        businessCommunityAttr.put("specCd", businessCommunityAttr.get("spec_cd"));
        businessCommunityAttr.put("communityId", businessCommunityAttr.get("community_id"));
        businessCommunityAttr.put("newBId", businessCommunityAttr.get("b_id"));
        businessCommunityAttr.put("statusCd", statusCd);
    }

    /**
     * 刷新 businessCommunityPhoto 数据
     *
     * @param businessCommunityPhoto
     * @param statusCd
     */
    protected void flushBusinessCommunityPhoto(Map businessCommunityPhoto, String statusCd) {
        businessCommunityPhoto.put("communityId", businessCommunityPhoto.get("community_id"));
        businessCommunityPhoto.put("communityPhotoId", businessCommunityPhoto.get("community_photo_id"));
        businessCommunityPhoto.put("communityPhotoTypeCd", businessCommunityPhoto.get("community_photo_type_cd"));
        businessCommunityPhoto.put("newBId", businessCommunityPhoto.get("b_id"));
        businessCommunityPhoto.put("statusCd", statusCd);
    }


    /**
     * 刷新 businessCommunityMember 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessCommunityMember
     */
    protected void flushBusinessCommunityMember(Map businessCommunityMember, String statusCd) {
        businessCommunityMember.put("newBId", businessCommunityMember.get("b_id"));
        businessCommunityMember.put("communityId", businessCommunityMember.get("community_id"));
        businessCommunityMember.put("communityMemberId", businessCommunityMember.get("community_member_id"));
        businessCommunityMember.put("memberId", businessCommunityMember.get("member_id"));
        businessCommunityMember.put("memberTypeCd", businessCommunityMember.get("member_type_cd"));
        businessCommunityMember.put("auditStatusCd", businessCommunityMember.get("audit_status_cd"));
        businessCommunityMember.put("startTime", businessCommunityMember.get("start_time"));
        businessCommunityMember.put("endTime", businessCommunityMember.get("end_time"));
        businessCommunityMember.put("statusCd", statusCd);
    }

    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessCommunity 小区信息
     */
    protected void autoSaveDelBusinessCommunity(Business business, JSONObject businessCommunity) {
//自动插入DEL
        Map info = new HashMap();
        info.put("communityId", businessCommunity.getString("communityId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        Map currentCommunityInfo = getCommunityServiceDaoImpl().getCommunityInfo(info);
        if (currentCommunityInfo == null || currentCommunityInfo.isEmpty()) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }
        currentCommunityInfo.put("bId", business.getbId());
        currentCommunityInfo.put("communityId", currentCommunityInfo.get("community_id"));
        currentCommunityInfo.put("cityCode", currentCommunityInfo.get("city_code"));
        currentCommunityInfo.put("nearbyLandmarks", currentCommunityInfo.get("nearby_landmarks"));
        currentCommunityInfo.put("mapX", currentCommunityInfo.get("map_x"));
        currentCommunityInfo.put("mapY", currentCommunityInfo.get("map_y"));
        currentCommunityInfo.put("state", currentCommunityInfo.get("state"));
        currentCommunityInfo.put("communityArea", currentCommunityInfo.get("community_area"));
        currentCommunityInfo.put("tel", currentCommunityInfo.get("tel"));
        currentCommunityInfo.put("payFeeMonth", currentCommunityInfo.get("pay_fee_month"));
        currentCommunityInfo.put("feePrice", currentCommunityInfo.get("fee_price"));

        currentCommunityInfo.put("operate", StatusConstant.OPERATE_DEL);
        getCommunityServiceDaoImpl().saveBusinessCommunityInfo(currentCommunityInfo);
        for(Object key : currentCommunityInfo.keySet()) {
            if(businessCommunity.get(key) == null) {
                businessCommunity.put(key.toString(), currentCommunityInfo.get(key));
            }
        }

    }

    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param business      当前业务
     * @param communityAttr 小区属性
     */
    protected void autoSaveDelBusinessCommunityAttr(Business business, JSONObject communityAttr) {
        Map info = new HashMap();
        info.put("attrId", communityAttr.getString("attrId"));
        info.put("communityId", communityAttr.getString("communityId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentCommunityAttrs = getCommunityServiceDaoImpl().getCommunityAttrs(info);
        if (currentCommunityAttrs == null || currentCommunityAttrs.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }
        Map currentCommunityAttr = currentCommunityAttrs.get(0);
        currentCommunityAttr.put("bId", business.getbId());
        currentCommunityAttr.put("attrId", currentCommunityAttr.get("attr_id"));
        currentCommunityAttr.put("communityId", currentCommunityAttr.get("community_id"));
        currentCommunityAttr.put("specCd", currentCommunityAttr.get("spec_cd"));
        currentCommunityAttr.put("operate", StatusConstant.OPERATE_DEL);
        getCommunityServiceDaoImpl().saveBusinessCommunityAttr(currentCommunityAttr);
        for(Object key : currentCommunityAttr.keySet()) {
            if(communityAttr.get(key) == null) {
                communityAttr.put(key.toString(), currentCommunityAttr.get(key));
            }
        }
    }

    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param business
     * @param businessCommunityPhoto 小区照片
     */
    protected void autoSaveDelBusinessCommunityPhoto(Business business, JSONObject businessCommunityPhoto) {
        Map info = new HashMap();
        info.put("communityPhotoId", businessCommunityPhoto.getString("communityPhotoId"));
        info.put("communityId", businessCommunityPhoto.getString("communityId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentCommunityPhotos = getCommunityServiceDaoImpl().getCommunityPhoto(info);
        if (currentCommunityPhotos == null || currentCommunityPhotos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }
        Map currentCommunityPhoto = currentCommunityPhotos.get(0);
        currentCommunityPhoto.put("bId", business.getbId());
        currentCommunityPhoto.put("communityPhotoId", currentCommunityPhoto.get("community_photo_id"));
        currentCommunityPhoto.put("communityId", currentCommunityPhoto.get("community_id"));
        currentCommunityPhoto.put("communityPhotoTypeCd", currentCommunityPhoto.get("community_photo_type_cd"));
        currentCommunityPhoto.put("operate", StatusConstant.OPERATE_DEL);
        getCommunityServiceDaoImpl().saveBusinessCommunityPhoto(currentCommunityPhoto);
        for(Object key : currentCommunityPhoto.keySet()) {
            if(businessCommunityPhoto.get(key) == null) {
                businessCommunityPhoto.put(key.toString(), currentCommunityPhoto.get(key));
            }
        }
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessCommunityMember 小区信息
     */
    protected void autoSaveDelBusinessCommunityMember(Business business, JSONObject businessCommunityMember) {
//自动插入DEL
        Map info = new HashMap();
        info.put("communityMemberId", businessCommunityMember.getString("communityMemberId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);


        List<Map> currentCommunityMembers = getCommunityServiceDaoImpl().getCommunityMember(info);

        if (currentCommunityMembers == null || currentCommunityMembers.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentCommunityMember = currentCommunityMembers.get(0);

        currentCommunityMember.put("bId", business.getbId());
        currentCommunityMember.put("communityId", currentCommunityMember.get("community_id"));
        currentCommunityMember.put("communityMemberId", currentCommunityMember.get("community_member_id"));
        currentCommunityMember.put("memberId", currentCommunityMember.get("member_id"));
        currentCommunityMember.put("memberTypeCd", currentCommunityMember.get("member_type_cd"));
        currentCommunityMember.put("auditStatusCd", currentCommunityMember.get("audit_status_cd"));
        currentCommunityMember.put("startTime", currentCommunityMember.get("start_time"));
        currentCommunityMember.put("endTime", currentCommunityMember.get("end_time"));
        currentCommunityMember.put("operate", StatusConstant.OPERATE_DEL);
        getCommunityServiceDaoImpl().saveBusinessCommunityMember(currentCommunityMember);

        for(Object key : currentCommunityMember.keySet()) {
            if(businessCommunityMember.get(key) == null) {
                businessCommunityMember.put(key.toString(), currentCommunityMember.get(key));
            }
        }
    }
}
