package com.java110.common.listener.auditMessage;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IAuditMessageServiceDao;
import com.java110.entity.center.Business;
import com.java110.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 审核原因 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractAuditMessageBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractAuditMessageBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IAuditMessageServiceDao getAuditMessageServiceDaoImpl();

    /**
     * 刷新 businessAuditMessageInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessAuditMessageInfo
     */
    protected void flushBusinessAuditMessageInfo(Map businessAuditMessageInfo, String statusCd) {
        businessAuditMessageInfo.put("newBId", businessAuditMessageInfo.get("b_id"));
        businessAuditMessageInfo.put("auditOrderType", businessAuditMessageInfo.get("audit_order_type"));
        businessAuditMessageInfo.put("operate", businessAuditMessageInfo.get("operate"));
        businessAuditMessageInfo.put("auditMessageId", businessAuditMessageInfo.get("audit_message_id"));
        businessAuditMessageInfo.put("auditOrderId", businessAuditMessageInfo.get("audit_order_id"));
        businessAuditMessageInfo.put("state", businessAuditMessageInfo.get("state"));
        businessAuditMessageInfo.put("storeId", businessAuditMessageInfo.get("store_id"));
        businessAuditMessageInfo.put("userName", businessAuditMessageInfo.get("user_name"));
        businessAuditMessageInfo.put("message", businessAuditMessageInfo.get("message"));
        businessAuditMessageInfo.put("userId", businessAuditMessageInfo.get("user_id"));
        businessAuditMessageInfo.remove("bId");
        businessAuditMessageInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessAuditMessage 审核原因信息
     */
    protected void autoSaveDelBusinessAuditMessage(Business business, JSONObject businessAuditMessage) {
//自动插入DEL
        Map info = new HashMap();
        info.put("auditMessageId", businessAuditMessage.getString("auditMessageId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentAuditMessageInfos = getAuditMessageServiceDaoImpl().getAuditMessageInfo(info);
        if (currentAuditMessageInfos == null || currentAuditMessageInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentAuditMessageInfo = currentAuditMessageInfos.get(0);

        currentAuditMessageInfo.put("bId", business.getbId());

        currentAuditMessageInfo.put("auditOrderType", currentAuditMessageInfo.get("audit_order_type"));
        currentAuditMessageInfo.put("operate", currentAuditMessageInfo.get("operate"));
        currentAuditMessageInfo.put("auditMessageId", currentAuditMessageInfo.get("audit_message_id"));
        currentAuditMessageInfo.put("auditOrderId", currentAuditMessageInfo.get("audit_order_id"));
        currentAuditMessageInfo.put("state", currentAuditMessageInfo.get("state"));
        currentAuditMessageInfo.put("storeId", currentAuditMessageInfo.get("store_id"));
        currentAuditMessageInfo.put("userName", currentAuditMessageInfo.get("user_name"));
        currentAuditMessageInfo.put("message", currentAuditMessageInfo.get("message"));
        currentAuditMessageInfo.put("userId", currentAuditMessageInfo.get("user_id"));


        currentAuditMessageInfo.put("operate", StatusConstant.OPERATE_DEL);
        getAuditMessageServiceDaoImpl().saveBusinessAuditMessageInfo(currentAuditMessageInfo);
    }


}
