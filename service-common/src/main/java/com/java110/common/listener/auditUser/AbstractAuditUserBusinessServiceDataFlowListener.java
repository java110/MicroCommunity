package com.java110.common.listener.auditUser;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IAuditUserServiceDao;
import com.java110.entity.center.Business;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 审核人员 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractAuditUserBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractAuditUserBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IAuditUserServiceDao getAuditUserServiceDaoImpl();

    /**
     * 刷新 businessAuditUserInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessAuditUserInfo
     */
    protected void flushBusinessAuditUserInfo(Map businessAuditUserInfo, String statusCd) {
        businessAuditUserInfo.put("newBId", businessAuditUserInfo.get("b_id"));
        businessAuditUserInfo.put("objCode", businessAuditUserInfo.get("obj_code"));
        businessAuditUserInfo.put("auditUserId", businessAuditUserInfo.get("audit_user_id"));
        businessAuditUserInfo.put("operate", businessAuditUserInfo.get("operate"));
        businessAuditUserInfo.put("storeId", businessAuditUserInfo.get("store_id"));
        businessAuditUserInfo.put("userName", businessAuditUserInfo.get("user_name"));
        businessAuditUserInfo.put("userId", businessAuditUserInfo.get("user_id"));
        businessAuditUserInfo.put("auditLink", businessAuditUserInfo.get("audit_link"));
        businessAuditUserInfo.remove("bId");
        businessAuditUserInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessAuditUser 审核人员信息
     */
    protected void autoSaveDelBusinessAuditUser(Business business, JSONObject businessAuditUser) {
//自动插入DEL
        Map info = new HashMap();
        info.put("auditUserId", businessAuditUser.getString("auditUserId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentAuditUserInfos = getAuditUserServiceDaoImpl().getAuditUserInfo(info);
        if (currentAuditUserInfos == null || currentAuditUserInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentAuditUserInfo = currentAuditUserInfos.get(0);

        currentAuditUserInfo.put("bId", business.getbId());

        currentAuditUserInfo.put("objCode", currentAuditUserInfo.get("obj_code"));
        currentAuditUserInfo.put("auditUserId", currentAuditUserInfo.get("audit_user_id"));
        currentAuditUserInfo.put("operate", currentAuditUserInfo.get("operate"));
        currentAuditUserInfo.put("storeId", currentAuditUserInfo.get("store_id"));
        currentAuditUserInfo.put("userName", currentAuditUserInfo.get("user_name"));
        currentAuditUserInfo.put("userId", currentAuditUserInfo.get("user_id"));
        currentAuditUserInfo.put("auditLink", currentAuditUserInfo.get("audit_link"));


        currentAuditUserInfo.put("operate", StatusConstant.OPERATE_DEL);
        getAuditUserServiceDaoImpl().saveBusinessAuditUserInfo(currentAuditUserInfo);

        for(Object key : currentAuditUserInfo.keySet()) {
            if(businessAuditUser.get(key) == null) {
                businessAuditUser.put(key.toString(), currentAuditUserInfo.get(key));
            }
        }
    }


}
