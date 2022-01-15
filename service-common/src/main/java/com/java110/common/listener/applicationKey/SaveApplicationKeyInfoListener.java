package com.java110.common.listener.applicationKey;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IApplicationKeyServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.applicationKey.ApplicationKeyPo;
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
 * 保存 钥匙申请信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveApplicationKeyInfoListener")
@Transactional
public class SaveApplicationKeyInfoListener extends AbstractApplicationKeyBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveApplicationKeyInfoListener.class);

    @Autowired
    private IApplicationKeyServiceDao applicationKeyServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_APPLICATION_KEY;
    }

    /**
     * 保存钥匙申请信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessApplicationKey 节点
        if (data.containsKey(ApplicationKeyPo.class.getSimpleName())) {
            Object bObj = data.get(ApplicationKeyPo.class.getSimpleName());
            JSONArray businessApplicationKeys = null;
            if (bObj instanceof JSONObject) {
                businessApplicationKeys = new JSONArray();
                businessApplicationKeys.add(bObj);
            } else {
                businessApplicationKeys = (JSONArray) bObj;
            }
            //JSONObject businessApplicationKey = data.getJSONObject("businessApplicationKey");
            for (int bApplicationKeyIndex = 0; bApplicationKeyIndex < businessApplicationKeys.size(); bApplicationKeyIndex++) {
                JSONObject businessApplicationKey = businessApplicationKeys.getJSONObject(bApplicationKeyIndex);
                doBusinessApplicationKey(business, businessApplicationKey);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("applicationKeyId", businessApplicationKey.getString("applicationKeyId"));
                }
            }
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

        //钥匙申请信息
        List<Map> businessApplicationKeyInfo = applicationKeyServiceDaoImpl.getBusinessApplicationKeyInfo(info);
        if (businessApplicationKeyInfo != null && businessApplicationKeyInfo.size() > 0) {
            reFreshShareColumn(info, businessApplicationKeyInfo.get(0));
            applicationKeyServiceDaoImpl.saveApplicationKeyInfoInstance(info);
            if (businessApplicationKeyInfo.size() == 1) {
                dataFlowContext.addParamOut("applicationKeyId", businessApplicationKeyInfo.get(0).get("application_key_id"));
            }
        }
    }


    /**
     * 刷 分片字段
     *
     * @param info         查询对象
     * @param businessInfo 小区ID
     */
    private void reFreshShareColumn(Map info, Map businessInfo) {

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
        //钥匙申请信息
        List<Map> applicationKeyInfo = applicationKeyServiceDaoImpl.getApplicationKeyInfo(info);
        if (applicationKeyInfo != null && applicationKeyInfo.size() > 0) {
            reFreshShareColumn(paramIn, applicationKeyInfo.get(0));
            applicationKeyServiceDaoImpl.updateApplicationKeyInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessApplicationKey 节点
     *
     * @param business               总的数据节点
     * @param businessApplicationKey 钥匙申请节点
     */
    private void doBusinessApplicationKey(Business business, JSONObject businessApplicationKey) {

        Assert.jsonObjectHaveKey(businessApplicationKey, "applicationKeyId", "businessApplicationKey 节点下没有包含 applicationKeyId 节点");

        if (businessApplicationKey.getString("applicationKeyId").startsWith("-")) {
            //刷新缓存
            //flushApplicationKeyId(business.getDatas());

            businessApplicationKey.put("applicationKeyId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_applicationKeyId));

        }

        businessApplicationKey.put("bId", business.getbId());
        businessApplicationKey.put("operate", StatusConstant.OPERATE_ADD);
        //保存钥匙申请信息
        applicationKeyServiceDaoImpl.saveBusinessApplicationKeyInfo(businessApplicationKey);

    }

    public IApplicationKeyServiceDao getApplicationKeyServiceDaoImpl() {
        return applicationKeyServiceDaoImpl;
    }

    public void setApplicationKeyServiceDaoImpl(IApplicationKeyServiceDao applicationKeyServiceDaoImpl) {
        this.applicationKeyServiceDaoImpl = applicationKeyServiceDaoImpl;
    }
}
