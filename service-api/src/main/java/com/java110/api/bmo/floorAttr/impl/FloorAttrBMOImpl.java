package com.java110.api.bmo.floorAttr.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.floorAttr.IFloorAttrBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.intf.community.IFloorAttrInnerServiceSMO;
import com.java110.po.floorAttr.FloorAttrPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("floorAttrBMOImpl")
public class FloorAttrBMOImpl extends ApiBaseBMO implements IFloorAttrBMO {

    @Autowired
    private IFloorAttrInnerServiceSMO floorAttrInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addFloorAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("attrId", "-1");
        FloorAttrPo floorAttrPo = BeanConvertUtil.covertBean(paramInJson, FloorAttrPo.class);
        super.insert(dataFlowContext, floorAttrPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FLOOR_ATTR_INFO);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateFloorAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        FloorAttrPo floorAttrPo = BeanConvertUtil.covertBean(paramInJson, FloorAttrPo.class);
        super.update(dataFlowContext, floorAttrPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_FLOOR_ATTR_INFO);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteFloorAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        FloorAttrPo floorAttrPo = BeanConvertUtil.covertBean(paramInJson, FloorAttrPo.class);
        super.update(dataFlowContext, floorAttrPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_FLOOR_ATTR_INFO);
    }

}
