package com.java110.store.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.constant.ServiceCodeConstant;
import com.java110.common.constant.StatusConstant;
import com.java110.common.log.LoggerEngine;
import com.java110.common.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.DataTransactionFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.event.service.BusinessServiceDataFlowEvent;
import com.java110.event.service.BusinessServiceDataFlowListener;
import com.java110.store.dao.IStoreServiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存 用户信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveStoreInfoListener")
@Transactional
public class SaveStoreInfoListener extends LoggerEngine implements BusinessServiceDataFlowListener{

    @Autowired
    IStoreServiceDao storeServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_SAVE_STORE_INFO;
    }

    @Override
    public void soService(BusinessServiceDataFlowEvent event) {
        //这里处理业务逻辑数据
        DataFlowContext dataFlowContext = event.getDataFlowContext();
        doSaveStoreInfo(dataFlowContext);
    }

    /**
     * 保存商户信息
     * 主要保存 businessStore，businessStoreAttr，businessStorePhoto，businessStoreCerdentials信息
     * @param dataFlowContext 数据流对象
     */
    private void doSaveStoreInfo(DataFlowContext dataFlowContext){
        String businessType = dataFlowContext.getOrder().getBusinessType();
        Business business = dataFlowContext.getCurrentBusiness();
        // Instance 过程
        if(StatusConstant.REQUEST_BUSINESS_TYPE_INSTANCE.equals(businessType)){
            doSaveInstanceStoreInfo(dataFlowContext,business);
        }else if(StatusConstant.REQUEST_BUSINESS_TYPE_BUSINESS.equals(businessType)){ // Business过程
            doSaveBusinessStoreInfo(dataFlowContext,business);
        }else if(StatusConstant.REQUEST_BUSINESS_TYPE_DELETE.equals(businessType)){ //撤单过程
            doDeleteInstanceStoreInfo(dataFlowContext,business);
        }

        dataFlowContext.setResJson(DataTransactionFactory.createBusinessResponseJson(dataFlowContext,ResponseConstant.RESULT_CODE_SUCCESS,"成功",
                dataFlowContext.getParamOut()));
    }

    /**
     * 撤单
     * @param business
     */
    private void doDeleteInstanceStoreInfo(DataFlowContext dataFlowContext, Business business) {

        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");
        Map info = new HashMap();
        info.put("bId",bId);
        //商户信息
        Map storeInfo = storeServiceDaoImpl.getStoreInfo(info);
        if(storeInfo != null && !storeInfo.isEmpty()){
            info.put("bId",bId);
            info.put("storeId",storeInfo.get("store_id").toString());
            info.put("statusCd",StatusConstant.STATUS_CD_INVALID);
            storeServiceDaoImpl.updateStoreInfoInstance(info);
            dataFlowContext.addParamOut("storeId",storeInfo.get("store_id"));
        }

        //商户属性
        List<Map> storeAttrs = storeServiceDaoImpl.getStoreAttrs(info);
        if(storeAttrs != null && storeAttrs.size()>0){
            storeServiceDaoImpl.updateStoreAttrInstance(info);
        }

        //商户照片
        List<Map> storePhotos = storeServiceDaoImpl.getStorePhoto(info);
        if(storePhotos != null && storePhotos.size()>0){
            storeServiceDaoImpl.updateStorePhotoInstance(info);
        }

        //商户属性
        List<Map> storeCerdentialses = storeServiceDaoImpl.getStoreCerdentials(info);
        if(storeCerdentialses != null && storeCerdentialses.size()>0){
            storeServiceDaoImpl.updateStoreCerdentailsInstance(info);
        }


    }

    /**
     * instance过程
     * @param business
     */
    private void doSaveInstanceStoreInfo(DataFlowContext dataFlowContext, Business business) {

        JSONObject data = business.getDatas();

        Map info = new HashMap();
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_ADD);

        //商户信息
        Map businessStoreInfo = storeServiceDaoImpl.getBusinessStoreInfo(info);
        if( businessStoreInfo != null && !businessStoreInfo.isEmpty()) {
            storeServiceDaoImpl.saveStoreInfoInstance(info);
            dataFlowContext.addParamOut("storeId",businessStoreInfo.get("store_id"));
        }
        //商户属性
        List<Map> businessStoreAttrs = storeServiceDaoImpl.getBusinessStoreAttrs(info);
        if(businessStoreAttrs != null && businessStoreAttrs.size() > 0) {
            storeServiceDaoImpl.saveStoreAttrsInstance(info);
        }
        //商户照片
        List<Map> businessStorePhotos = storeServiceDaoImpl.getBusinessStorePhoto(info);
        if(businessStorePhotos != null && businessStorePhotos.size() >0){
            storeServiceDaoImpl.saveStorePhotoInstance(info);
        }
        //商户证件
        List<Map> businessStoreCerdentialses = storeServiceDaoImpl.getBusinessStoreCerdentials(info);
        if(businessStoreCerdentialses != null && businessStoreCerdentialses.size()>0){
            storeServiceDaoImpl.saveStoreCerdentialsInstance(info);
        }

    }

    /**
     * 保存数据至u_user 表中
     * @param business
     */
    private void doComplateUserInfo(DataFlowContext dataFlowContext,Business business) {
        /*String bId = business.getbId();
        Map paramIn = new HashMap();
        paramIn.put("bId",bId);
        paramIn.put("statusCd",StatusConstant.STATUS_CD_VALID);
        storeServiceDaoImpl.updateUserInfoInstance(paramIn);
        storeServiceDaoImpl.updateUserAttrInstance(paramIn);*/
    }

    /**
     * 处理商户信息
     * @param business
     */
    private void doSaveBusinessStoreInfo(DataFlowContext dataFlowContext, Business business) {

        JSONObject data = business.getDatas();

        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessStore 节点
        if(data.containsKey("businessStore")){
            JSONObject businessStore = data.getJSONObject("businessStore");
            doBusinessStore(business,businessStore);
            dataFlowContext.addParamOut("storeId",businessStore.getString("storeId"));
        }

        if(data.containsKey("businessStorePhoto")){
            JSONArray businessStorePhotos = data.getJSONArray("businessStorePhoto");
            doBusinessStorePhoto(business,businessStorePhotos);
        }

        if(data.containsKey("businessStoreCerdentials")){
            JSONArray businessStoreCerdentialses = data.getJSONArray("businessStoreCerdentials");
            doBusinessStoreCerdentials(business,businessStoreCerdentialses);
        }


        //storeServiceDaoImpl.saveUserInfoInstance(businessUser);
    }

    /**
     * 保存商户照片
     * @param business 业务对象
     * @param businessStorePhotos 商户照片
     */
    private void doBusinessStorePhoto(Business business, JSONArray businessStorePhotos) {

        for(int businessStorePhotoIndex = 0 ;businessStorePhotoIndex < businessStorePhotos.size();businessStorePhotoIndex++) {
            JSONObject businessStorePhoto = businessStorePhotos.getJSONObject(businessStorePhotoIndex);
            Assert.jsonObjectHaveKey(businessStorePhoto, "storeId", "businessStorePhoto 节点下没有包含 storeId 节点");

            if (businessStorePhoto.getLong("storePhotoId") < 0) {
                String storePhotoId = GenerateCodeFactory.getStorePhotoId();
                businessStorePhoto.put("storePhotoId", storePhotoId);
            }
            businessStorePhoto.put("bId", business.getbId());
            businessStorePhoto.put("operate", StatusConstant.OPERATE_ADD);
            //保存商户信息
            storeServiceDaoImpl.saveBusinessStorePhoto(businessStorePhoto);
        }
    }

    /**
     * 处理 businessStore 节点
     * @param business 总的数据节点
     * @param businessStore 商户节点
     */
    private void doBusinessStore(Business business,JSONObject businessStore){

        Assert.jsonObjectHaveKey(businessStore,"storeId","businessStore 节点下没有包含 storeId 节点");

        if(businessStore.getInteger("storeId") < 0){
            //刷新缓存
            flushStoreId(business.getDatas());
        }


        businessStore.put("bId",business.getbId());
        businessStore.put("operate", StatusConstant.OPERATE_ADD);
        //保存商户信息
        storeServiceDaoImpl.saveBusinessStoreInfo(businessStore);
        //保存 商户属性信息
        if(businessStore.containsKey("businessStoreAttr")){
            JSONArray businessStoreAttrs = businessStore.getJSONArray("businessStoreAttr");
            doSaveBusinessStoreAttrs(business,businessStoreAttrs);
        }
    }



    /**
     * 保存商户属性信息
     * @param business 当前业务
     * @param businessStoreAttrs 商户属性
     */
    private void doSaveBusinessStoreAttrs(Business business,JSONArray businessStoreAttrs){
        JSONObject data = business.getDatas();
        JSONObject businessStore = data.getJSONObject("businessStore");
        for(int storeAttrIndex = 0 ; storeAttrIndex < businessStoreAttrs.size();storeAttrIndex ++){
            JSONObject storeAttr = businessStoreAttrs.getJSONObject(storeAttrIndex);
            Assert.jsonObjectHaveKey(storeAttr,"attrId","businessStoreAttr 节点下没有包含 attrId 节点");

            if(storeAttr.getInteger("attrId") < 0){
                String attrId = GenerateCodeFactory.getAttrId();
                storeAttr.put("attrId",attrId);
            }

            storeAttr.put("bId",business.getbId());
            storeAttr.put("storeId",businessStore.getString("storeId"));
            storeAttr.put("operate", StatusConstant.OPERATE_ADD);

            storeServiceDaoImpl.saveBusinessStoreAttr(storeAttr);
        }
    }


    /**
     * 保存 商户证件 信息
     * @param business 当前业务
     * @param businessStoreCerdentialses 商户证件
     */
    private void doBusinessStoreCerdentials(Business business, JSONArray businessStoreCerdentialses) {
        for(int businessStoreCerdentialsIndex = 0 ; businessStoreCerdentialsIndex < businessStoreCerdentialses.size() ; businessStoreCerdentialsIndex ++) {
            JSONObject businessStoreCerdentials = businessStoreCerdentialses.getJSONObject(businessStoreCerdentialsIndex);
            Assert.jsonObjectHaveKey(businessStoreCerdentials, "storeId", "businessStorePhoto 节点下没有包含 storeId 节点");

            if (businessStoreCerdentials.getLong("storeCerdentialsId") < 0) {
                String storePhotoId = GenerateCodeFactory.getStoreCerdentialsId();
                businessStoreCerdentials.put("storeCerdentialsId", storePhotoId);
            }
            businessStoreCerdentials.put("bId", business.getbId());
            businessStoreCerdentials.put("operate", StatusConstant.OPERATE_ADD);
            //保存商户信息
            storeServiceDaoImpl.saveBusinessStoreCerdentials(businessStoreCerdentials);
        }
    }



    /**
     * 刷新 商户ID
     * @param data
     */
    private void flushStoreId(JSONObject data) {

        String storeId = GenerateCodeFactory.getStoreId();
        JSONObject businessStore = data.getJSONObject("businessStore");
        businessStore.put("storeId",storeId);
        if(data.containsKey("businessStorePhoto")) {
            JSONObject businessStorePhoto = data.getJSONObject("businessStorePhoto");
            businessStorePhoto.put("storeId", storeId);
        }
        if(data.containsKey("businessStoreCerdentials")) {
            JSONObject businessStoreCerdentials = data.getJSONObject("businessStoreCerdentials");
            businessStoreCerdentials.put("storeId", storeId);
        }
    }


    public IStoreServiceDao getStoreServiceDaoImpl() {
        return storeServiceDaoImpl;
    }

    public void setStoreServiceDaoImpl(IStoreServiceDao storeServiceDaoImpl) {
        this.storeServiceDaoImpl = storeServiceDaoImpl;
    }
}
