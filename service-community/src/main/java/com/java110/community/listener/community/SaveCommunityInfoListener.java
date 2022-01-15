package com.java110.community.listener.community;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.ICommunityServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.community.CommunityAttrPo;
import com.java110.po.community.CommunityPhotoPo;
import com.java110.po.community.CommunityPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存 用户信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveCommunityInfoListener")
@Transactional
public class SaveCommunityInfoListener extends AbstractCommunityBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(SaveCommunityInfoListener.class);

    @Autowired
    ICommunityServiceDao communityServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_COMMUNITY_INFO;
    }

    /**
     * 保存小区信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessCommunity 节点
        if (data.containsKey(CommunityPo.class.getSimpleName())) {
            JSONObject businessCommunity = data.getJSONArray(CommunityPo.class.getSimpleName()).getJSONObject(0);
            doBusinessCommunity(business, businessCommunity);
            dataFlowContext.addParamOut("communityId", businessCommunity.getString("communityId"));
        }

        if (data.containsKey(CommunityAttrPo.class.getSimpleName())) {
            JSONArray businessCommunityAttrs = data.getJSONArray(CommunityAttrPo.class.getSimpleName());
            doSaveBusinessCommunityAttrs(business, businessCommunityAttrs);
        }

        if (data.containsKey(CommunityPhotoPo.class.getSimpleName())) {
            JSONArray businessCommunityPhotos = data.getJSONArray(CommunityPhotoPo.class.getSimpleName());
            doBusinessCommunityPhoto(business, businessCommunityPhotos);
        }


    }

    /**
     * business 数据转移到 instance
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();

        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_ADD);

        //小区信息
        Map businessCommunityInfo = communityServiceDaoImpl.getBusinessCommunityInfo(info);

        if (businessCommunityInfo != null && !businessCommunityInfo.isEmpty()) {
            reFresh(info, businessCommunityInfo);
            communityServiceDaoImpl.saveCommunityInfoInstance(businessCommunityInfo);
            dataFlowContext.addParamOut("communityId", businessCommunityInfo.get("community_id"));
        }
        //小区属性
        List<Map> businessCommunityAttrs = communityServiceDaoImpl.getBusinessCommunityAttrs(info);
        if (businessCommunityAttrs != null && businessCommunityAttrs.size() > 0) {
            reFresh(info, businessCommunityAttrs.get(0));
            communityServiceDaoImpl.saveCommunityAttrsInstance(info);
        }
        //小区照片
        List<Map> businessCommunityPhotos = communityServiceDaoImpl.getBusinessCommunityPhoto(info);
        if (businessCommunityPhotos != null && businessCommunityPhotos.size() > 0) {
            reFresh(info, businessCommunityPhotos.get(0));
            communityServiceDaoImpl.saveCommunityPhotoInstance(info);
        }

    }

    /**
     * 刷 communityId
     *
     * @param info         查询对象
     * @param businessInfo 小区ID
     */
    private void reFresh(Map info, Map businessInfo) {

        if (info.containsKey("communityId")) {
            return;
        }

        if (!businessInfo.containsKey("community_id")) {
            return;
        }

        info.put("communityId", businessInfo.get("community_id"));
    }

    /**
     * 撤单
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");
        Map info = new HashMap();
        info.put("bId", bId);
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        Map paramIn = new HashMap();
        paramIn.put("bId", bId);
        paramIn.put("statusCd", StatusConstant.STATUS_CD_INVALID);
        //小区信息
        Map communityInfo = communityServiceDaoImpl.getCommunityInfo(info);
        if (communityInfo != null && !communityInfo.isEmpty()) {
            paramIn.put("communityId", communityInfo.get("community_id").toString());
            communityServiceDaoImpl.updateCommunityInfoInstance(paramIn);
            dataFlowContext.addParamOut("communityId", communityInfo.get("community_id"));
        }

        //小区属性
        List<Map> communityAttrs = communityServiceDaoImpl.getCommunityAttrs(info);
        if (communityAttrs != null && communityAttrs.size() > 0) {
            communityServiceDaoImpl.updateCommunityAttrInstance(paramIn);
        }

        //小区照片
        List<Map> communityPhotos = communityServiceDaoImpl.getCommunityPhoto(info);
        if (communityPhotos != null && communityPhotos.size() > 0) {
            communityServiceDaoImpl.updateCommunityPhotoInstance(paramIn);
        }


    }

    /**
     * 保存小区照片
     *
     * @param business                业务对象
     * @param businessCommunityPhotos 小区照片
     */
    private void doBusinessCommunityPhoto(Business business, JSONArray businessCommunityPhotos) {

        for (int businessCommunityPhotoIndex = 0; businessCommunityPhotoIndex < businessCommunityPhotos.size(); businessCommunityPhotoIndex++) {
            JSONObject businessCommunityPhoto = businessCommunityPhotos.getJSONObject(businessCommunityPhotoIndex);
            Assert.jsonObjectHaveKey(businessCommunityPhoto, "communityId", "businessCommunityPhoto 节点下没有包含 communityId 节点");

            if (businessCommunityPhoto.getString("communityPhotoId").startsWith("-")) {
                String communityPhotoId = GenerateCodeFactory.getCommunityPhotoId();
                businessCommunityPhoto.put("communityPhotoId", communityPhotoId);
            }
            businessCommunityPhoto.put("bId", business.getbId());
            businessCommunityPhoto.put("operate", StatusConstant.OPERATE_ADD);
            //保存小区信息
            communityServiceDaoImpl.saveBusinessCommunityPhoto(businessCommunityPhoto);
        }
    }

    /**
     * 处理 businessCommunity 节点
     *
     * @param business          总的数据节点
     * @param businessCommunity 小区节点
     */
    private void doBusinessCommunity(Business business, JSONObject businessCommunity) {

        Assert.jsonObjectHaveKey(businessCommunity, "communityId", "businessCommunity 节点下没有包含 communityId 节点");

        if (businessCommunity.getString("communityId").startsWith("-")) {
            //刷新缓存
            flushCommunityId(business.getDatas());
        }

        businessCommunity.put("bId", business.getbId());
        businessCommunity.put("operate", StatusConstant.OPERATE_ADD);
        //保存小区信息
        communityServiceDaoImpl.saveBusinessCommunityInfo(businessCommunity);

    }


    /**
     * 保存小区属性信息
     *
     * @param business               当前业务
     * @param businessCommunityAttrs 小区属性
     */
    private void doSaveBusinessCommunityAttrs(Business business, JSONArray businessCommunityAttrs) {
        JSONObject data = business.getDatas();
        for (int communityAttrIndex = 0; communityAttrIndex < businessCommunityAttrs.size(); communityAttrIndex++) {
            JSONObject communityAttr = businessCommunityAttrs.getJSONObject(communityAttrIndex);
            Assert.jsonObjectHaveKey(communityAttr, "attrId", "businessCommunityAttr 节点下没有包含 attrId 节点");

            if (communityAttr.getString("attrId").startsWith("-")) {
                String attrId = GenerateCodeFactory.getAttrId();
                communityAttr.put("attrId", attrId);
            }

            communityAttr.put("bId", business.getbId());
            //communityAttr.put("communityId", businessCommunity.getString("communityId"));
            communityAttr.put("operate", StatusConstant.OPERATE_ADD);

            communityServiceDaoImpl.saveBusinessCommunityAttr(communityAttr);
        }
    }


    /**
     * 刷新 小区ID
     *
     * @param data
     */
    private void flushCommunityId(JSONObject data) {

        String communityId = GenerateCodeFactory.getCommunityId();
        JSONObject businessCommunity = data.getJSONObject("businessCommunity");
        businessCommunity.put("communityId", communityId);
        //刷小区属性
        if (data.containsKey("businessCommunityAttr")) {
            JSONArray businessCommunityAttrs = data.getJSONArray("businessCommunityAttr");
            for (int businessCommunityAttrIndex = 0; businessCommunityAttrIndex < businessCommunityAttrs.size(); businessCommunityAttrIndex++) {
                JSONObject businessCommunityAttr = businessCommunityAttrs.getJSONObject(businessCommunityAttrIndex);
                businessCommunityAttr.put("communityId", communityId);
            }
        }
        //刷 是小区照片 的 communityId
        if (data.containsKey("businessCommunityPhoto")) {
            JSONArray businessCommunityPhotos = data.getJSONArray("businessCommunityPhoto");
            for (int businessCommunityPhotoIndex = 0; businessCommunityPhotoIndex < businessCommunityPhotos.size(); businessCommunityPhotoIndex++) {
                JSONObject businessCommunityPhoto = businessCommunityPhotos.getJSONObject(businessCommunityPhotoIndex);
                businessCommunityPhoto.put("communityId", communityId);
            }
        }

    }


    public ICommunityServiceDao getCommunityServiceDaoImpl() {
        return communityServiceDaoImpl;
    }

    public void setCommunityServiceDaoImpl(ICommunityServiceDao communityServiceDaoImpl) {
        this.communityServiceDaoImpl = communityServiceDaoImpl;
    }
}
