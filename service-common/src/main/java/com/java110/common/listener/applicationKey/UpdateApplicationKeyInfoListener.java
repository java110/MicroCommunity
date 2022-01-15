package com.java110.common.listener.applicationKey;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IApplicationKeyServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.po.applicationKey.ApplicationKeyPo;
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
 * 修改钥匙申请信息 侦听
 * <p>
 * 处理节点
 * 1、businessApplicationKey:{} 钥匙申请基本信息节点
 * 2、businessApplicationKeyAttr:[{}] 钥匙申请属性信息节点
 * 3、businessApplicationKeyPhoto:[{}] 钥匙申请照片信息节点
 * 4、businessApplicationKeyCerdentials:[{}] 钥匙申请证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E4%BF%AE%E6%94%B9%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateApplicationKeyInfoListener")
@Transactional
public class UpdateApplicationKeyInfoListener extends AbstractApplicationKeyBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(UpdateApplicationKeyInfoListener.class);
    @Autowired
    private IApplicationKeyServiceDao applicationKeyServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_APPLICATION_KEY;
    }

    /**
     * business过程
     *
     * @param dataFlowContext 上下文对象
     * @param business        业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {

        JSONObject data = business.getDatas();

        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");


        //处理 businessApplicationKey 节点
        if (data.containsKey(ApplicationKeyPo.class.getSimpleName())) {
            Object _obj = data.get(ApplicationKeyPo.class.getSimpleName());
            JSONArray businessApplicationKeys = null;
            if (_obj instanceof JSONObject) {
                businessApplicationKeys = new JSONArray();
                businessApplicationKeys.add(_obj);
            } else {
                businessApplicationKeys = (JSONArray) _obj;
            }
            //JSONObject businessApplicationKey = data.getJSONObject("businessApplicationKey");
            for (int _applicationKeyIndex = 0; _applicationKeyIndex < businessApplicationKeys.size(); _applicationKeyIndex++) {
                JSONObject businessApplicationKey = businessApplicationKeys.getJSONObject(_applicationKeyIndex);
                doBusinessApplicationKey(business, businessApplicationKey);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("applicationKeyId", businessApplicationKey.getString("applicationKeyId"));
                }
            }

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

        //钥匙申请信息
        List<Map> businessApplicationKeyInfos = applicationKeyServiceDaoImpl.getBusinessApplicationKeyInfo(info);
        if (businessApplicationKeyInfos != null && businessApplicationKeyInfos.size() > 0) {
            for (int _applicationKeyIndex = 0; _applicationKeyIndex < businessApplicationKeyInfos.size(); _applicationKeyIndex++) {
                Map businessApplicationKeyInfo = businessApplicationKeyInfos.get(_applicationKeyIndex);
                flushBusinessApplicationKeyInfo(businessApplicationKeyInfo, StatusConstant.STATUS_CD_VALID);
                applicationKeyServiceDaoImpl.updateApplicationKeyInfoInstance(businessApplicationKeyInfo);
                if (businessApplicationKeyInfo.size() == 1) {
                    dataFlowContext.addParamOut("applicationKeyId", businessApplicationKeyInfo.get("application_key_id"));
                }
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
        //钥匙申请信息
        List<Map> applicationKeyInfo = applicationKeyServiceDaoImpl.getApplicationKeyInfo(info);
        if (applicationKeyInfo != null && applicationKeyInfo.size() > 0) {

            //钥匙申请信息
            List<Map> businessApplicationKeyInfos = applicationKeyServiceDaoImpl.getBusinessApplicationKeyInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessApplicationKeyInfos == null || businessApplicationKeyInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（applicationKey），程序内部异常,请检查！ " + delInfo);
            }
            for (int _applicationKeyIndex = 0; _applicationKeyIndex < businessApplicationKeyInfos.size(); _applicationKeyIndex++) {
                Map businessApplicationKeyInfo = businessApplicationKeyInfos.get(_applicationKeyIndex);
                flushBusinessApplicationKeyInfo(businessApplicationKeyInfo, StatusConstant.STATUS_CD_VALID);
                applicationKeyServiceDaoImpl.updateApplicationKeyInfoInstance(businessApplicationKeyInfo);
            }
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
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "applicationKeyId 错误，不能自动生成（必须已经存在的applicationKeyId）" + businessApplicationKey);
        }
        //自动保存DEL
        autoSaveDelBusinessApplicationKey(business, businessApplicationKey);

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
