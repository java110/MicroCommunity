package com.java110.common.listener.auditMessage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IAuditMessageServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存 审核原因信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveAuditMessageInfoListener")
@Transactional
public class SaveAuditMessageInfoListener extends AbstractAuditMessageBusinessServiceDataFlowListener{

    private static Logger logger = LoggerFactory.getLogger(SaveAuditMessageInfoListener.class);

    @Autowired
    private IAuditMessageServiceDao auditMessageServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_AUDIT_MESSAGE;
    }

    /**
     * 保存审核原因信息 business 表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessAuditMessage 节点
        if(data.containsKey("businessAuditMessage")){
            Object bObj = data.get("businessAuditMessage");
            JSONArray businessAuditMessages = null;
            if(bObj instanceof JSONObject){
                businessAuditMessages = new JSONArray();
                businessAuditMessages.add(bObj);
            }else {
                businessAuditMessages = (JSONArray)bObj;
            }
            //JSONObject businessAuditMessage = data.getJSONObject("businessAuditMessage");
            for (int bAuditMessageIndex = 0; bAuditMessageIndex < businessAuditMessages.size();bAuditMessageIndex++) {
                JSONObject businessAuditMessage = businessAuditMessages.getJSONObject(bAuditMessageIndex);
                doBusinessAuditMessage(business, businessAuditMessage);
                if(bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("auditMessageId", businessAuditMessage.getString("auditMessageId"));
                }
            }
        }
    }

    /**
     * business 数据转移到 instance
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();

        Map info = new HashMap();
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_ADD);

        //审核原因信息
        List<Map> businessAuditMessageInfo = auditMessageServiceDaoImpl.getBusinessAuditMessageInfo(info);
        if( businessAuditMessageInfo != null && businessAuditMessageInfo.size() >0) {
            reFreshShareColumn(info, businessAuditMessageInfo.get(0));
            auditMessageServiceDaoImpl.saveAuditMessageInfoInstance(info);
            if(businessAuditMessageInfo.size() == 1) {
                dataFlowContext.addParamOut("auditMessageId", businessAuditMessageInfo.get(0).get("audit_message_id"));
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

        if (info.containsKey("storeId")) {
            return;
        }

        if (!businessInfo.containsKey("store_id")) {
            return;
        }

        info.put("storeId", businessInfo.get("store_id"));
    }
    /**
     * 撤单
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");
        Map info = new HashMap();
        info.put("bId",bId);
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        Map paramIn = new HashMap();
        paramIn.put("bId",bId);
        paramIn.put("statusCd",StatusConstant.STATUS_CD_INVALID);
        //审核原因信息
        List<Map> auditMessageInfo = auditMessageServiceDaoImpl.getAuditMessageInfo(info);
        if(auditMessageInfo != null && auditMessageInfo.size() > 0){
            reFreshShareColumn(paramIn, auditMessageInfo.get(0));
            auditMessageServiceDaoImpl.updateAuditMessageInfoInstance(paramIn);
        }
    }



    /**
     * 处理 businessAuditMessage 节点
     * @param business 总的数据节点
     * @param businessAuditMessage 审核原因节点
     */
    private void doBusinessAuditMessage(Business business,JSONObject businessAuditMessage){

        Assert.jsonObjectHaveKey(businessAuditMessage,"auditMessageId","businessAuditMessage 节点下没有包含 auditMessageId 节点");

        if(businessAuditMessage.getString("auditMessageId").startsWith("-")){
            //刷新缓存
            //flushAuditMessageId(business.getDatas());

            businessAuditMessage.put("auditMessageId",GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_auditMessageId));

        }

        businessAuditMessage.put("bId",business.getbId());
        businessAuditMessage.put("operate", StatusConstant.OPERATE_ADD);
        //保存审核原因信息
        auditMessageServiceDaoImpl.saveBusinessAuditMessageInfo(businessAuditMessage);

    }

    public IAuditMessageServiceDao getAuditMessageServiceDaoImpl() {
        return auditMessageServiceDaoImpl;
    }

    public void setAuditMessageServiceDaoImpl(IAuditMessageServiceDao auditMessageServiceDaoImpl) {
        this.auditMessageServiceDaoImpl = auditMessageServiceDaoImpl;
    }
}
