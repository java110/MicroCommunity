package com.java110.store.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.store.StoreAttrPo;
import com.java110.po.store.StoreCerdentialPo;
import com.java110.po.store.StorePhotoPo;
import com.java110.po.store.StorePo;
import com.java110.store.dao.IStoreServiceDao;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存 用户信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveStoreInfoListener")
@Transactional
public class SaveStoreInfoListener extends AbstractStoreBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(SaveStoreInfoListener.class);

    @Autowired
    IStoreServiceDao storeServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_STORE_INFO;
    }

    /**
     * 保存商户信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessStore 节点
        if (data.containsKey(StorePo.class.getSimpleName())) {
            JSONObject businessStore = data.getJSONObject(StorePo.class.getSimpleName());
            doBusinessStore(business, businessStore);
            dataFlowContext.addParamOut("storeId", businessStore.getString("storeId"));
        }

        if (data.containsKey(StoreAttrPo.class.getSimpleName())) {
            JSONArray businessStoreAttrs = data.getJSONArray(StoreAttrPo.class.getSimpleName());
            doSaveBusinessStoreAttrs(business, businessStoreAttrs);
        }

        if (data.containsKey(StorePhotoPo.class.getSimpleName())) {
            JSONArray businessStorePhotos = data.getJSONArray(StorePhotoPo.class.getSimpleName());
            doBusinessStorePhoto(business, businessStorePhotos);
        }

        if (data.containsKey(StoreCerdentialPo.class.getSimpleName())) {
            JSONArray businessStoreCerdentialses = data.getJSONArray(StoreCerdentialPo.class.getSimpleName());
            doBusinessStoreCerdentials(business, businessStoreCerdentialses);
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

        //商户信息
        Map businessStoreInfo = storeServiceDaoImpl.getBusinessStoreInfo(info);
        if (businessStoreInfo != null && !businessStoreInfo.isEmpty()) {
            storeServiceDaoImpl.saveStoreInfoInstance(info);
            dataFlowContext.addParamOut("storeId", businessStoreInfo.get("store_id"));
        }
        //商户属性
        List<Map> businessStoreAttrs = storeServiceDaoImpl.getBusinessStoreAttrs(info);
        if (businessStoreAttrs != null && businessStoreAttrs.size() > 0) {
            storeServiceDaoImpl.saveStoreAttrsInstance(info);
        }
        //商户照片
        List<Map> businessStorePhotos = storeServiceDaoImpl.getBusinessStorePhoto(info);
        if (businessStorePhotos != null && businessStorePhotos.size() > 0) {
            storeServiceDaoImpl.saveStorePhotoInstance(info);
        }
        //商户证件
        List<Map> businessStoreCerdentialses = storeServiceDaoImpl.getBusinessStoreCerdentials(info);
        if (businessStoreCerdentialses != null && businessStoreCerdentialses.size() > 0) {
            storeServiceDaoImpl.saveStoreCerdentialsInstance(info);
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
        Map paramIn = new HashMap();
        paramIn.put("bId", bId);
        paramIn.put("statusCd", StatusConstant.STATUS_CD_INVALID);
        //商户信息
        Map storeInfo = storeServiceDaoImpl.getStoreInfo(info);
        if (storeInfo != null && !storeInfo.isEmpty()) {
            paramIn.put("storeId", storeInfo.get("store_id").toString());
            storeServiceDaoImpl.updateStoreInfoInstance(paramIn);
            dataFlowContext.addParamOut("storeId", storeInfo.get("store_id"));
        }

        //商户属性
        List<Map> storeAttrs = storeServiceDaoImpl.getStoreAttrs(info);
        if (storeAttrs != null && storeAttrs.size() > 0) {
            storeServiceDaoImpl.updateStoreAttrInstance(paramIn);
        }

        //商户照片
        List<Map> storePhotos = storeServiceDaoImpl.getStorePhoto(info);
        if (storePhotos != null && storePhotos.size() > 0) {
            storeServiceDaoImpl.updateStorePhotoInstance(paramIn);
        }

        //商户属性
        List<Map> storeCerdentialses = storeServiceDaoImpl.getStoreCerdentials(info);
        if (storeCerdentialses != null && storeCerdentialses.size() > 0) {
            storeServiceDaoImpl.updateStoreCerdentailsInstance(paramIn);
        }
    }

    /**
     * 保存商户照片
     *
     * @param business            业务对象
     * @param businessStorePhotos 商户照片
     */
    private void doBusinessStorePhoto(Business business, JSONArray businessStorePhotos) {

        for (int businessStorePhotoIndex = 0; businessStorePhotoIndex < businessStorePhotos.size(); businessStorePhotoIndex++) {
            JSONObject businessStorePhoto = businessStorePhotos.getJSONObject(businessStorePhotoIndex);
            Assert.jsonObjectHaveKey(businessStorePhoto, "storeId", "businessStorePhoto 节点下没有包含 storeId 节点");

            if (businessStorePhoto.getString("storePhotoId").startsWith("-")) {
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
     *
     * @param business      总的数据节点
     * @param businessStore 商户节点
     */
    private void doBusinessStore(Business business, JSONObject businessStore) {

        Assert.jsonObjectHaveKey(businessStore, "storeId", "businessStore 节点下没有包含 storeId 节点");

        if (businessStore.getString("storeId").startsWith("-")) {
            //刷新缓存
            flushStoreId(business.getDatas());
        }

        businessStore.put("bId", business.getbId());
        businessStore.put("operate", StatusConstant.OPERATE_ADD);
        //保存商户信息
        storeServiceDaoImpl.saveBusinessStoreInfo(businessStore);

    }


    /**
     * 保存商户属性信息
     *
     * @param business           当前业务
     * @param businessStoreAttrs 商户属性
     */
    private void doSaveBusinessStoreAttrs(Business business, JSONArray businessStoreAttrs) {
        JSONObject data = business.getDatas();
        JSONObject businessStore = data.getJSONObject(StorePo.class.getSimpleName());
        for (int storeAttrIndex = 0; storeAttrIndex < businessStoreAttrs.size(); storeAttrIndex++) {
            JSONObject storeAttr = businessStoreAttrs.getJSONObject(storeAttrIndex);
            Assert.jsonObjectHaveKey(storeAttr, "attrId", "businessStoreAttr 节点下没有包含 attrId 节点");

            if (storeAttr.getString("attrId").startsWith("-")) {
                String attrId = GenerateCodeFactory.getAttrId();
                storeAttr.put("attrId", attrId);
            }

            storeAttr.put("bId", business.getbId());
            storeAttr.put("storeId", businessStore.getString("storeId"));
            storeAttr.put("operate", StatusConstant.OPERATE_ADD);

            storeServiceDaoImpl.saveBusinessStoreAttr(storeAttr);
        }
    }


    /**
     * 保存 商户证件 信息
     *
     * @param business                   当前业务
     * @param businessStoreCerdentialses 商户证件
     */
    private void doBusinessStoreCerdentials(Business business, JSONArray businessStoreCerdentialses) {
        for (int businessStoreCerdentialsIndex = 0; businessStoreCerdentialsIndex < businessStoreCerdentialses.size(); businessStoreCerdentialsIndex++) {
            JSONObject businessStoreCerdentials = businessStoreCerdentialses.getJSONObject(businessStoreCerdentialsIndex);
            Assert.jsonObjectHaveKey(businessStoreCerdentials, "storeId", "businessStorePhoto 节点下没有包含 storeId 节点");

            if (businessStoreCerdentials.getString("storeCerdentialsId").startsWith("-")) {
                String storePhotoId = GenerateCodeFactory.getStoreCerdentialsId();
                businessStoreCerdentials.put("storeCerdentialsId", storePhotoId);
            }
            Date validityPeriod = null;
            try {
                if (StringUtil.isNullOrNone(businessStoreCerdentials.getString("validityPeriod"))) {
                    validityPeriod = DateUtil.getLastDate();
                } else {
                    validityPeriod = DateUtil.getDateFromString(businessStoreCerdentials.getString("validityPeriod"), DateUtil.DATE_FORMATE_STRING_B);
                }
            } catch (ParseException e) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "传入参数 validityPeriod 格式不正确，请填写 " + DateUtil.DATE_FORMATE_STRING_B + " 格式，" + businessStoreCerdentials);
            }
            businessStoreCerdentials.put("validityPeriod", validityPeriod);
            businessStoreCerdentials.put("bId", business.getbId());
            businessStoreCerdentials.put("operate", StatusConstant.OPERATE_ADD);
            //保存商户信息
            storeServiceDaoImpl.saveBusinessStoreCerdentials(businessStoreCerdentials);
        }
    }


    /**
     * 刷新 商户ID
     *
     * @param data
     */
    private void flushStoreId(JSONObject data) {

        String storeId = GenerateCodeFactory.getStoreId();
        JSONObject businessStore = data.getJSONObject(StorePo.class.getSimpleName());
        businessStore.put("storeId", storeId);
        //刷商户属性
        if (data.containsKey(StoreAttrPo.class.getSimpleName())) {
            JSONArray businessStoreAttrs = data.getJSONArray(StoreAttrPo.class.getSimpleName());
            for (int businessStoreAttrIndex = 0; businessStoreAttrIndex < businessStoreAttrs.size(); businessStoreAttrIndex++) {
                JSONObject businessStoreAttr = businessStoreAttrs.getJSONObject(businessStoreAttrIndex);
                businessStoreAttr.put("storeId", storeId);
            }
        }
        //刷 是商户照片 的 storeId
        if (data.containsKey(StorePhotoPo.class.getSimpleName())) {
            JSONArray businessStorePhotos = data.getJSONArray(StorePhotoPo.class.getSimpleName());
            for (int businessStorePhotoIndex = 0; businessStorePhotoIndex < businessStorePhotos.size(); businessStorePhotoIndex++) {
                JSONObject businessStorePhoto = businessStorePhotos.getJSONObject(businessStorePhotoIndex);
                businessStorePhoto.put("storeId", storeId);
            }
        }
        //刷 商户证件 的storeId
        if (data.containsKey(StoreCerdentialPo.class.getSimpleName())) {
            JSONArray businessStoreCerdentialses = data.getJSONArray(StoreCerdentialPo.class.getSimpleName());
            for (int businessStoreCerdentialsIndex = 0; businessStoreCerdentialsIndex < businessStoreCerdentialses.size(); businessStoreCerdentialsIndex++) {
                JSONObject businessStoreCerdentials = businessStoreCerdentialses.getJSONObject(businessStoreCerdentialsIndex);
                businessStoreCerdentials.put("storeId", storeId);
            }
        }
    }


    public IStoreServiceDao getStoreServiceDaoImpl() {
        return storeServiceDaoImpl;
    }

    public void setStoreServiceDaoImpl(IStoreServiceDao storeServiceDaoImpl) {
        this.storeServiceDaoImpl = storeServiceDaoImpl;
    }
}
