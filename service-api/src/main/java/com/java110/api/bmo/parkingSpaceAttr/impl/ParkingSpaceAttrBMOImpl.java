package com.java110.api.bmo.parkingSpaceAttr.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.parkingSpaceAttr.IParkingSpaceAttrBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.intf.community.IParkingSpaceAttrInnerServiceSMO;
import com.java110.po.parkingSpaceAttr.ParkingSpaceAttrPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("parkingSpaceAttrBMOImpl")
public class ParkingSpaceAttrBMOImpl extends ApiBaseBMO implements IParkingSpaceAttrBMO {

    @Autowired
    private IParkingSpaceAttrInnerServiceSMO parkingSpaceAttrInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addParkingSpaceAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("attrId", "-1");
        ParkingSpaceAttrPo parkingSpaceAttrPo = BeanConvertUtil.covertBean(paramInJson, ParkingSpaceAttrPo.class);
        super.insert(dataFlowContext, parkingSpaceAttrPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_PARKING_SPACE_ATTR);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateParkingSpaceAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        ParkingSpaceAttrPo parkingSpaceAttrPo = BeanConvertUtil.covertBean(paramInJson, ParkingSpaceAttrPo.class);
        super.update(dataFlowContext, parkingSpaceAttrPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_PARKING_SPACE_ATTR);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteParkingSpaceAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        ParkingSpaceAttrPo parkingSpaceAttrPo = BeanConvertUtil.covertBean(paramInJson, ParkingSpaceAttrPo.class);
        super.update(dataFlowContext, parkingSpaceAttrPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_PARKING_SPACE_ATTR);
    }

}
