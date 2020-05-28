package com.java110.api.bmo.inspection.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.inspection.IInspectionBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.inspectionPlan.IInspectionPlanInnerServiceSMO;
import com.java110.core.smo.inspectionRoute.IInspectionRoutePointRelInnerServiceSMO;
import com.java110.dto.inspectionPlan.InspectionPlanDto;
import com.java110.dto.inspectionRoute.InspectionRoutePointRelDto;
import com.java110.po.inspection.InspectionPlanPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName InspectionBMOImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/9 22:39
 * @Version 1.0
 * add by wuxw 2020/3/9
 **/
@Service("inspectionBMOImpl")
public class InspectionBMOImpl extends ApiBaseBMO implements IInspectionBMO {


    @Autowired
    private IInspectionPlanInnerServiceSMO inspectionPlanInnerServiceSMOImpl;

    @Autowired
    private IInspectionRoutePointRelInnerServiceSMO inspectionRoutePointRelInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteInspectionPlan(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        InspectionPlanPo inspectionPlanPo = BeanConvertUtil.covertBean(paramInJson, InspectionPlanPo.class);

        super.delete(dataFlowContext, inspectionPlanPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_INSPECTION_PLAN);

    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addInspectionPlan(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        JSONObject businessInspectionPlan = new JSONObject();
        businessInspectionPlan.putAll(paramInJson);
        businessInspectionPlan.put("inspectionPlanId", "-1");
        InspectionPlanPo inspectionPlanPo = BeanConvertUtil.covertBean(businessInspectionPlan, InspectionPlanPo.class);

        super.insert(dataFlowContext, inspectionPlanPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_INSPECTION_PLAN);
    }

    /**
     * 添加巡检计划信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateInspectionPlan(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        InspectionPlanPo inspectionPlanPo = BeanConvertUtil.covertBean(paramInJson, InspectionPlanPo.class);
        super.update(dataFlowContext, inspectionPlanPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_INSPECTION_PLAN);
    }

    /**
     * 添加设备信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateInspectionPlanState(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        InspectionPlanDto inspectionPlanDto = new InspectionPlanDto();
        inspectionPlanDto.setCommunityId(paramInJson.getString("communityId"));
        inspectionPlanDto.setInspectionPlanId(paramInJson.getString("inspectionPlanId"));
        List<InspectionPlanDto> inspectionPlanDtos = inspectionPlanInnerServiceSMOImpl.queryInspectionPlans(inspectionPlanDto);

        Assert.listOnlyOne(inspectionPlanDtos, "根据计划ID查询到多条记录，请检查数据");
        JSONObject businessInspectionPlan = new JSONObject();
        businessInspectionPlan.putAll(BeanConvertUtil.beanCovertMap(inspectionPlanDtos.get(0)));
        businessInspectionPlan.put("state", paramInJson.getString("state"));
        InspectionPlanPo inspectionPlanPo = BeanConvertUtil.covertBean(businessInspectionPlan, InspectionPlanPo.class);
        super.update(dataFlowContext, inspectionPlanPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_INSPECTION_PLAN);
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject deleteInspectionPoint(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");

        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_DELETE_INSPECTION);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessInspectionPoint = new JSONObject();
        businessInspectionPoint.putAll(paramInJson);
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessInspectionPoint", businessInspectionPoint);
        return business;
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addInspectionPoint(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_INSPECTION);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessInspectionPoint = new JSONObject();
        businessInspectionPoint.putAll(paramInJson);
        businessInspectionPoint.put("inspectionId", "-1");
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessInspectionPoint", businessInspectionPoint);
        return business;
    }

    /**
     * 添加巡检点信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject updateInspectionPoint(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_INSPECTION);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessInspectionPoint = new JSONObject();
        businessInspectionPoint.putAll(paramInJson);
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessInspectionPoint", businessInspectionPoint);
        return business;
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject deleteInspectionRoute(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_DELETE_INSPECTION_ROUTE);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessInspectionRoute = new JSONObject();
        businessInspectionRoute.putAll(paramInJson);
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessInspectionRoute", businessInspectionRoute);
        return business;
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject deleteInspectionRoutePoint(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        InspectionRoutePointRelDto inspectionRoutePointRelDto = new InspectionRoutePointRelDto();
        inspectionRoutePointRelDto.setCommunityId(paramInJson.getString("communityId"));
        inspectionRoutePointRelDto.setInspectionId(paramInJson.getString("inspectionId"));
        inspectionRoutePointRelDto.setInspectionRouteId(paramInJson.getString("inspectionRouteId"));
        List<InspectionRoutePointRelDto> inspectionRoutePointRelDtos = inspectionRoutePointRelInnerServiceSMOImpl.queryInspectionRoutePointRels(inspectionRoutePointRelDto);

        Assert.listOnlyOne(inspectionRoutePointRelDtos, "未查询到（或多条）要删除的 巡检路线下的巡检点");

        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_DELETE_INSPECTION_ROUTE_POINT_REL);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessInspectionRoute = new JSONObject();
        businessInspectionRoute.putAll(BeanConvertUtil.beanCovertMap(inspectionRoutePointRelDtos.get(0)));
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessInspectionRoutePointRel", businessInspectionRoute);
        return business;
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addInspectionRoute(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_INSPECTION_ROUTE);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessInspectionRoute = new JSONObject();
        businessInspectionRoute.putAll(paramInJson);
        businessInspectionRoute.put("inspectionRouteId", "-1");
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessInspectionRoute", businessInspectionRoute);
        return business;
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addInspectionRoute(JSONObject paramInJson, DataFlowContext dataFlowContext, int index) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_INSPECTION_ROUTE_POINT_REL);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessInspectionRoute = new JSONObject();
        businessInspectionRoute.putAll(paramInJson);
        businessInspectionRoute.put("irpRelId", "-" + index);
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessInspectionRoutePointRel", businessInspectionRoute);
        return business;
    }

    /**
     * 添加巡检路线信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject updateInspectionRoute(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_INSPECTION_ROUTE);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessInspectionRoute = new JSONObject();
        businessInspectionRoute.putAll(paramInJson);
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessInspectionRoute", businessInspectionRoute);
        return business;
    }

}
