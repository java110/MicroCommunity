package com.java110.api.bmo.auditUser.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.auditUser.IAuditUserBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.po.audit.AuditUserPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.stereotype.Service;

/**
 * @ClassName AuditUserBMOImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/9 20:49
 * @Version 1.0
 * add by wuxw 2020/3/9
 **/
@Service("auditUserBMOImpl")
public class AuditUserBMOImpl extends ApiBaseBMO implements IAuditUserBMO {


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteAuditUser(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        AuditUserPo auditUserPo = BeanConvertUtil.covertBean(paramInJson, AuditUserPo.class);
        super.delete(dataFlowContext, auditUserPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_AUDIT_USER);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addAuditUser(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("auditUserId", "-1");

        AuditUserPo auditUserPo = BeanConvertUtil.covertBean(paramInJson, AuditUserPo.class);

        super.insert(dataFlowContext, auditUserPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_AUDIT_USER);
    }
}
