package com.java110.api.bmo.parkingAreaAttr.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.parkingAreaAttr.IParkingAreaAttrBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.intf.community.IParkingAreaAttrInnerServiceSMO;
import com.java110.po.parkingAreaAttr.ParkingAreaAttrPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("parkingAreaAttrBMOImpl")
public class ParkingAreaAttrBMOImpl extends ApiBaseBMO implements IParkingAreaAttrBMO {

    @Autowired
    private IParkingAreaAttrInnerServiceSMO parkingAreaAttrInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addParkingAreaAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("attrId", "-1");
        ParkingAreaAttrPo parkingAreaAttrPo = BeanConvertUtil.covertBean(paramInJson, ParkingAreaAttrPo.class);
        super.insert(dataFlowContext, parkingAreaAttrPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_PARKING_AREA_ATTR);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateParkingAreaAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        ParkingAreaAttrPo parkingAreaAttrPo = BeanConvertUtil.covertBean(paramInJson, ParkingAreaAttrPo.class);
        super.update(dataFlowContext, parkingAreaAttrPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_PARKING_AREA_ATTR);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteParkingAreaAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        ParkingAreaAttrPo parkingAreaAttrPo = BeanConvertUtil.covertBean(paramInJson, ParkingAreaAttrPo.class);
        super.update(dataFlowContext, parkingAreaAttrPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_PARKING_AREA_ATTR);
    }

}
