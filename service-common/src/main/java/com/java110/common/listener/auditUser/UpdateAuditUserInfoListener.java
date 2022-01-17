package com.java110.common.listener.auditUser;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IAuditUserServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.po.audit.AuditUserPo;
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
 * 修改审核人员信息 侦听
 * <p>
 * 处理节点
 * 1、businessAuditUser:{} 审核人员基本信息节点
 * 2、businessAuditUserAttr:[{}] 审核人员属性信息节点
 * 3、businessAuditUserPhoto:[{}] 审核人员照片信息节点
 * 4、businessAuditUserCerdentials:[{}] 审核人员证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E4%BF%AE%E6%94%B9%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateAuditUserInfoListener")
@Transactional
public class UpdateAuditUserInfoListener extends AbstractAuditUserBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(UpdateAuditUserInfoListener.class);
    @Autowired
    private IAuditUserServiceDao auditUserServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_AUDIT_USER;
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


        //处理 businessAuditUser 节点
        if (data.containsKey(AuditUserPo.class.getSimpleName())) {
            Object _obj = data.get(AuditUserPo.class.getSimpleName());
            JSONArray businessAuditUsers = null;
            if (_obj instanceof JSONObject) {
                businessAuditUsers = new JSONArray();
                businessAuditUsers.add(_obj);
            } else {
                businessAuditUsers = (JSONArray) _obj;
            }
            //JSONObject businessAuditUser = data.getJSONObject("businessAuditUser");
            for (int _auditUserIndex = 0; _auditUserIndex < businessAuditUsers.size(); _auditUserIndex++) {
                JSONObject businessAuditUser = businessAuditUsers.getJSONObject(_auditUserIndex);
                doBusinessAuditUser(business, businessAuditUser);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("auditUserId", businessAuditUser.getString("auditUserId"));
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

        //审核人员信息
        List<Map> businessAuditUserInfos = auditUserServiceDaoImpl.getBusinessAuditUserInfo(info);
        if (businessAuditUserInfos != null && businessAuditUserInfos.size() > 0) {
            for (int _auditUserIndex = 0; _auditUserIndex < businessAuditUserInfos.size(); _auditUserIndex++) {
                Map businessAuditUserInfo = businessAuditUserInfos.get(_auditUserIndex);
                flushBusinessAuditUserInfo(businessAuditUserInfo, StatusConstant.STATUS_CD_VALID);
                auditUserServiceDaoImpl.updateAuditUserInfoInstance(businessAuditUserInfo);
                if (businessAuditUserInfo.size() == 1) {
                    dataFlowContext.addParamOut("auditUserId", businessAuditUserInfo.get("audit_user_id"));
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
        //审核人员信息
        List<Map> auditUserInfo = auditUserServiceDaoImpl.getAuditUserInfo(info);
        if (auditUserInfo != null && auditUserInfo.size() > 0) {

            //审核人员信息
            List<Map> businessAuditUserInfos = auditUserServiceDaoImpl.getBusinessAuditUserInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessAuditUserInfos == null || businessAuditUserInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（auditUser），程序内部异常,请检查！ " + delInfo);
            }
            for (int _auditUserIndex = 0; _auditUserIndex < businessAuditUserInfos.size(); _auditUserIndex++) {
                Map businessAuditUserInfo = businessAuditUserInfos.get(_auditUserIndex);
                flushBusinessAuditUserInfo(businessAuditUserInfo, StatusConstant.STATUS_CD_VALID);
                auditUserServiceDaoImpl.updateAuditUserInfoInstance(businessAuditUserInfo);
            }
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
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "auditUserId 错误，不能自动生成（必须已经存在的auditUserId）" + businessAuditUser);
        }
        //自动保存DEL
        autoSaveDelBusinessAuditUser(business, businessAuditUser);

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
