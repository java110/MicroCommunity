package com.java110.community.listener.community;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.ICommunityServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.po.community.CommunityAttrPo;
import com.java110.po.community.CommunityPhotoPo;
import com.java110.po.community.CommunityPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 修改小区信息 侦听
 * <p>
 * 处理节点
 * 1、businessCommunity:{} 小区基本信息节点
 * 2、businessCommunityAttr:[{}] 小区属性信息节点
 * 3、businessCommunityPhoto:[{}] 小区照片信息节点
 * 4、businessCommunityCerdentials:[{}] 小区证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E4%BF%AE%E6%94%B9%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateCommunityInfoListener")
@Transactional
public class UpdateCommunityInfoListener extends AbstractCommunityBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(UpdateCommunityInfoListener.class);
    @Autowired
    ICommunityServiceDao communityServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_COMMUNITY_INFO;
    }

    /**
     * business过程
     *
     * @param dataFlowContext
     * @param business
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {

        JSONObject data = business.getDatas();

        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessCommunity 节点
        if (data.containsKey(CommunityPo.class.getSimpleName())) {
           // JSONObject businessCommunity = data.getJSONObject(CommunityPo.class.getSimpleName());
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
     * business to instance 过程
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
            flushBusinessCommunityInfo(businessCommunityInfo, StatusConstant.STATUS_CD_VALID);
            communityServiceDaoImpl.updateCommunityInfoInstance(businessCommunityInfo);
            dataFlowContext.addParamOut("communityId", businessCommunityInfo.get("community_id"));
        }
        //小区属性
        List<Map> businessCommunityAttrs = communityServiceDaoImpl.getBusinessCommunityAttrs(info);
        if (businessCommunityAttrs != null && businessCommunityAttrs.size() > 0) {
            for (Map businessCommunityAttr : businessCommunityAttrs) {
                flushBusinessCommunityAttr(businessCommunityAttr, StatusConstant.STATUS_CD_VALID);
                communityServiceDaoImpl.updateCommunityAttrInstance(businessCommunityAttr);
            }
        }
        //小区照片
        List<Map> businessCommunityPhotos = communityServiceDaoImpl.getBusinessCommunityPhoto(info);
        if (businessCommunityPhotos != null && businessCommunityPhotos.size() > 0) {
            for (Map businessCommunityPhoto : businessCommunityPhotos) {
                flushBusinessCommunityPhoto(businessCommunityPhoto, StatusConstant.STATUS_CD_VALID);
                communityServiceDaoImpl.updateCommunityPhotoInstance(businessCommunityPhoto);
            }
        }

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
        Map delInfo = new HashMap();
        delInfo.put("bId", business.getbId());
        delInfo.put("operate", StatusConstant.OPERATE_DEL);
        //小区信息
        Map communityInfo = communityServiceDaoImpl.getCommunityInfo(info);
        if (communityInfo != null && !communityInfo.isEmpty()) {

            //小区信息
            Map businessCommunityInfo = communityServiceDaoImpl.getBusinessCommunityInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessCommunityInfo == null || businessCommunityInfo.isEmpty()) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（community），程序内部异常,请检查！ " + delInfo);
            }

            flushBusinessCommunityInfo(businessCommunityInfo, StatusConstant.STATUS_CD_VALID);
            communityServiceDaoImpl.updateCommunityInfoInstance(businessCommunityInfo);
            dataFlowContext.addParamOut("communityId", communityInfo.get("community_id"));
        }

        //小区属性
        List<Map> communityAttrs = communityServiceDaoImpl.getCommunityAttrs(info);
        if (communityAttrs != null && communityAttrs.size() > 0) {

            List<Map> businessCommunityAttrs = communityServiceDaoImpl.getBusinessCommunityAttrs(delInfo);
            //除非程序出错了，这里不会为空
            if (businessCommunityAttrs == null || businessCommunityAttrs.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败(community_attr)，程序内部异常,请检查！ " + delInfo);
            }
            for (Map businessCommunityAttr : businessCommunityAttrs) {
                flushBusinessCommunityAttr(businessCommunityAttr, StatusConstant.STATUS_CD_VALID);
                communityServiceDaoImpl.updateCommunityAttrInstance(businessCommunityAttr);
            }
        }

        //小区照片
        List<Map> communityPhotos = communityServiceDaoImpl.getCommunityPhoto(info);
        if (communityPhotos != null && communityPhotos.size() > 0) {
            List<Map> businessCommunityPhotos = communityServiceDaoImpl.getBusinessCommunityPhoto(delInfo);
            //除非程序出错了，这里不会为空
            if (businessCommunityPhotos == null || businessCommunityPhotos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败(community_photo)，程序内部异常,请检查！ " + delInfo);
            }
            for (Map businessCommunityPhoto : businessCommunityPhotos) {
                flushBusinessCommunityPhoto(businessCommunityPhoto, StatusConstant.STATUS_CD_VALID);
                communityServiceDaoImpl.updateCommunityPhotoInstance(businessCommunityPhoto);
            }
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
                throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "communityPhotoId 错误，不能自动生成（必须已经存在的communityPhotoId）" + businessCommunityPhoto);
            }

            //自动保存DEL信息
            autoSaveDelBusinessCommunityPhoto(business, businessCommunityPhoto);

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
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "communityId 错误，不能自动生成（必须已经存在的communityId）" + businessCommunity);
        }
        //自动保存DEL
        autoSaveDelBusinessCommunity(business, businessCommunity);

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
                throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "attrId 错误，不能自动生成（必须已经存在的attrId）" + communityAttr);
            }
            //自动保存DEL数据
            autoSaveDelBusinessCommunityAttr(business, communityAttr);

            communityAttr.put("bId", business.getbId());
            communityAttr.put("communityId", communityAttr.getString("communityId"));
            communityAttr.put("operate", StatusConstant.OPERATE_ADD);

            communityServiceDaoImpl.saveBusinessCommunityAttr(communityAttr);
        }
    }


    public ICommunityServiceDao getCommunityServiceDaoImpl() {
        return communityServiceDaoImpl;
    }

    public void setCommunityServiceDaoImpl(ICommunityServiceDao communityServiceDaoImpl) {
        this.communityServiceDaoImpl = communityServiceDaoImpl;
    }


}
