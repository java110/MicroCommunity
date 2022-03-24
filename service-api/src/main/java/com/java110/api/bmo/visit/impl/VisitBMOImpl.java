package com.java110.api.bmo.visit.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.visit.IVisitBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.po.owner.VisitPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.stereotype.Service;

/**
 * @ClassName VisitBMOImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/10 0:06
 * @Version 1.0
 * add by wuxw 2020/3/10
 **/
@Service("visitBMOImpl")
public class VisitBMOImpl extends ApiBaseBMO implements IVisitBMO {

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteVisit(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        VisitPo visitPo = BeanConvertUtil.covertBean(paramInJson, VisitPo.class);
        visitPo.setStatusCd("1");
        super.delete(dataFlowContext, visitPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_VISIT);

    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addVisit(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        JSONObject businessVisit = new JSONObject();
        businessVisit.putAll(paramInJson);

        VisitPo visitPo = BeanConvertUtil.covertBean(businessVisit, VisitPo.class);
        super.insert(dataFlowContext, visitPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_VISIT);
    }

    /**
     * 添加访客登记信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateVisit(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        VisitPo visitPo = BeanConvertUtil.covertBean(paramInJson, VisitPo.class);
        super.update(dataFlowContext, visitPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_VISIT);
    }
}
