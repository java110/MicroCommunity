package com.java110.common.listener.auditUser;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IAuditUserServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.audit.AuditUserPo;
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
 * 保存 审核人员信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveAuditUserInfoListener")
@Transactional
public class SaveAuditUserInfoListener extends AbstractAuditUserBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveAuditUserInfoListener.class);

    @Autowired
    private IAuditUserServiceDao auditUserServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_AUDIT_USER;
    }

    /**
     * 保存审核人员信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessAuditUser 节点
        if (data.containsKey(AuditUserPo.class.getSimpleName())) {
            Object bObj = data.get(AuditUserPo.class.getSimpleName());
            JSONArray businessAuditUsers = null;
            if (bObj instanceof JSONObject) {
                businessAuditUsers = new JSONArray();
                businessAuditUsers.add(bObj);
            } else {
                businessAuditUsers = (JSONArray) bObj;
            }
            //JSONObject businessAuditUser = data.getJSONObject("businessAuditUser");
            for (int bAuditUserIndex = 0; bAuditUserIndex < businessAuditUsers.size(); bAuditUserIndex++) {
                JSONObject businessAuditUser = businessAuditUsers.getJSONObject(bAuditUserIndex);
                doBusinessAuditUser(business, businessAuditUser);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("auditUserId", businessAuditUser.getString("auditUserId"));
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

        //审核人员信息
        List<Map> businessAuditUserInfo = auditUserServiceDaoImpl.getBusinessAuditUserInfo(info);
        if (businessAuditUserInfo != null && businessAuditUserInfo.size() > 0) {
            reFreshShareColumn(info, businessAuditUserInfo.get(0));
            auditUserServiceDaoImpl.saveAuditUserInfoInstance(info);
            if (businessAuditUserInfo.size() == 1) {
                dataFlowContext.addParamOut("auditUserId", businessAuditUserInfo.get(0).get("audit_user_id"));
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
        //审核人员信息
        List<Map> auditUserInfo = auditUserServiceDaoImpl.getAuditUserInfo(info);
        if (auditUserInfo != null && auditUserInfo.size() > 0) {
            reFreshShareColumn(paramIn, auditUserInfo.get(0));
            auditUserServiceDaoImpl.updateAuditUserInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessAuditUser 节点
     *
     * @param business          总的数据节点
     * @param businessAuditUser 审核人员节点
     */
    private void doBusinessAuditUser(Business business, JSONObject businessAuditUser) {

        Assert.jsonObjectHaveKey(businessAuditUser, "auditUserId", "businessAuditUser 节点下没有包含 auditUserId 节点");

        if (businessAuditUser.getString("auditUserId").startsWith("-")) {
            //刷新缓存
            //flushAuditUserId(business.getDatas());

            businessAuditUser.put("auditUserId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_auditUserId));

        }

        businessAuditUser.put("bId", business.getbId());
        businessAuditUser.put("operate", StatusConstant.OPERATE_ADD);
        //保存审核人员信息
        auditUserServiceDaoImpl.saveBusinessAuditUserInfo(businessAuditUser);

    }

    public IAuditUserServiceDao getAuditUserServiceDaoImpl() {
        return auditUserServiceDaoImpl;
    }

    public void setAuditUserServiceDaoImpl(IAuditUserServiceDao auditUserServiceDaoImpl) {
        this.auditUserServiceDaoImpl = auditUserServiceDaoImpl;
    }
}
