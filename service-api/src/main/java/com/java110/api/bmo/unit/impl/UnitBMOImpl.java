package com.java110.api.bmo.unit.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.unit.IUnitBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.po.unit.UnitPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.stereotype.Service;

/**
 * @ClassName UnitBMOImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/9 23:54
 * @Version 1.0
 * add by wuxw 2020/3/9
 **/
@Service("unitBMOImpl")
public class UnitBMOImpl extends ApiBaseBMO implements IUnitBMO {
    /**
     * 修改小区楼信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void editUnit(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        JSONObject businessUnit = new JSONObject();
        businessUnit.put("unitId", paramInJson.getString("unitId"));
        UnitPo unitPo = BeanConvertUtil.covertBean(businessUnit, UnitPo.class);

        super.delete(dataFlowContext, unitPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_UNIT_INFO);
    }

    /**
     * 添加小区楼信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addUnit(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        JSONObject businessUnit = new JSONObject();
        businessUnit.put("floorId", paramInJson.getString("floorId"));
        businessUnit.put("layerCount", paramInJson.getString("layerCount"));
        businessUnit.put("unitId", !paramInJson.containsKey("unitId") ? GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_unitId)
                : paramInJson.getString("unitId"));
        businessUnit.put("unitNum", paramInJson.getString("unitNum"));
        businessUnit.put("lift", paramInJson.getString("lift"));
        businessUnit.put("remark", paramInJson.getString("remark"));
        businessUnit.put("unitArea", paramInJson.getString("unitArea"));
        businessUnit.put("userId", dataFlowContext.getRequestCurrentHeaders().get(CommonConstant.HTTP_USER_ID));
        UnitPo unitPo = BeanConvertUtil.covertBean(businessUnit, UnitPo.class);

        super.insert(dataFlowContext, unitPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_UNIT_INFO);
    }

    /**
     * 修改小区楼信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void editUpdateUnit(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        JSONObject businessUnit = new JSONObject();
        businessUnit.put("floorId", paramInJson.getString("floorId"));
        businessUnit.put("layerCount", paramInJson.getString("layerCount"));
        businessUnit.put("unitId", paramInJson.getString("unitId"));
        businessUnit.put("unitNum", paramInJson.getString("unitNum"));
        businessUnit.put("lift", paramInJson.getString("lift"));
        businessUnit.put("remark", paramInJson.getString("remark"));
        businessUnit.put("unitArea", paramInJson.getString("unitArea"));
        businessUnit.put("userId", dataFlowContext.getRequestCurrentHeaders().get(CommonConstant.HTTP_USER_ID));
        UnitPo unitPo = BeanConvertUtil.covertBean(businessUnit, UnitPo.class);

        super.update(dataFlowContext, unitPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_UNIT_INFO);
    }
}
