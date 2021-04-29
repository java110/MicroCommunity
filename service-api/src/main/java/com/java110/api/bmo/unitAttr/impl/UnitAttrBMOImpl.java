package com.java110.api.bmo.unitAttr.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.unitAttr.IUnitAttrBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.intf.community.IUnitAttrInnerServiceSMO;
import com.java110.po.unitAttr.UnitAttrPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("unitAttrBMOImpl")
public class UnitAttrBMOImpl extends ApiBaseBMO implements IUnitAttrBMO {

    @Autowired
    private IUnitAttrInnerServiceSMO unitAttrInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addUnitAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("attrId", "-1");
        UnitAttrPo unitAttrPo = BeanConvertUtil.covertBean(paramInJson, UnitAttrPo.class);
        super.insert(dataFlowContext, unitAttrPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_UNIT_ATTR_INFO);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateUnitAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        UnitAttrPo unitAttrPo = BeanConvertUtil.covertBean(paramInJson, UnitAttrPo.class);
        super.update(dataFlowContext, unitAttrPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_UNIT_ATTR_INFO);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteUnitAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        UnitAttrPo unitAttrPo = BeanConvertUtil.covertBean(paramInJson, UnitAttrPo.class);
        super.update(dataFlowContext, unitAttrPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_UNIT_ATTR_INFO);
    }

}
