package com.java110.api.bmo.storehouse.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.storehouse.IStorehouseBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.dto.storehouse.StorehouseDto;
import com.java110.intf.store.IStorehouseInnerServiceSMO;
import com.java110.po.storehouse.StorehousePo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("storehouseBMOImpl")
public class StorehouseBMOImpl extends ApiBaseBMO implements IStorehouseBMO {

    @Autowired
    private IStorehouseInnerServiceSMO storehouseInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addStorehouse(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("shId", "-1");
        StorehousePo storehousePo = BeanConvertUtil.covertBean(paramInJson, StorehousePo.class);
        if (StorehouseDto.SH_TYPE_GROUP.equals(storehousePo.getShType())) {
            storehousePo.setShObjId(storehousePo.getStoreId());
        } else {
            storehousePo.setShObjId(paramInJson.getString("communityId"));
        }
        super.insert(dataFlowContext, storehousePo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_STOREHOUSE);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateStorehouse(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        StorehousePo storehousePo = BeanConvertUtil.covertBean(paramInJson, StorehousePo.class);
        super.update(dataFlowContext, storehousePo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_STOREHOUSE);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteStorehouse(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        StorehousePo storehousePo = BeanConvertUtil.covertBean(paramInJson, StorehousePo.class);
        super.update(dataFlowContext, storehousePo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_STOREHOUSE);
    }

}
