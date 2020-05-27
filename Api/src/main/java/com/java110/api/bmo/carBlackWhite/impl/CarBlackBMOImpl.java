package com.java110.api.bmo.carBlackWhite.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.carBlackWhite.ICarBlackWhiteBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.po.car.CarBlackWhitePo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.stereotype.Service;

/**
 * @ClassName ApiBaseBMOImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/9 20:58
 * @Version 1.0
 * add by wuxw 2020/3/9
 **/
@Service("carBlackBMOImpl")
public class CarBlackBMOImpl extends ApiBaseBMO implements ICarBlackWhiteBMO {


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteCarBlackWhite(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        CarBlackWhitePo carBlackWhitePo = BeanConvertUtil.covertBean(paramInJson, CarBlackWhitePo.class);
        super.delete(dataFlowContext, carBlackWhitePo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_CAR_BLACK_WHITE);
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addCarBlackWhite(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        paramInJson.put("bwId", "-1");

        CarBlackWhitePo carBlackWhitePo = BeanConvertUtil.covertBean(paramInJson, CarBlackWhitePo.class);

        super.insert(dataFlowContext, carBlackWhitePo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_CAR_BLACK_WHITE);
    }


    /**
     * 添加黑白名单信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateCarBlackWhite(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        CarBlackWhitePo carBlackWhitePo = BeanConvertUtil.covertBean(paramInJson,CarBlackWhitePo.class);

        super.update(dataFlowContext,carBlackWhitePo,BusinessTypeConstant.BUSINESS_TYPE_UPDATE_CAR_BLACK_WHITE);
    }


}
