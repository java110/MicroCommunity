package com.java110.api.bmo.returnPayFee.impl;

import com.alibaba.fastjson.JSONObject;

import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.returnPayFee.IReturnPayFeeBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.file.IFileInnerServiceSMO;
import com.java110.core.smo.file.IFileRelInnerServiceSMO;
import com.java110.core.smo.returnPayFee.IReturnPayFeeInnerServiceSMO;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.returnPayFee.ReturnPayFeeDto;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("returnPayFeeBMOImpl")
public class ReturnPayFeeBMOImpl extends ApiBaseBMO implements IReturnPayFeeBMO {

    @Autowired
    private IReturnPayFeeInnerServiceSMO returnPayFeeInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject addReturnPayFee(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_RETURN_PAY_FEE);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessReturnPayFee = new JSONObject();
        businessReturnPayFee.putAll(paramInJson);
        businessReturnPayFee.put("returnFeeId", "-1");
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessReturnPayFee", businessReturnPayFee);
        return business;
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject updateReturnPayFee(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        ReturnPayFeeDto returnPayFeeDto = new ReturnPayFeeDto();
        returnPayFeeDto.setReturnFeeId(paramInJson.getString("returnFeeId"));
        List<ReturnPayFeeDto> returnPayFeeDtos = returnPayFeeInnerServiceSMOImpl.queryReturnPayFees(returnPayFeeDto);

        Assert.listOnlyOne(returnPayFeeDtos, "未找到需要修改的活动 或多条数据");


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_RETURN_PAY_FEE);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessReturnPayFee = new JSONObject();
        businessReturnPayFee.putAll(BeanConvertUtil.beanCovertMap(returnPayFeeDtos.get(0)));
        businessReturnPayFee.putAll(paramInJson);
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessReturnPayFee", businessReturnPayFee);
        return business;
    }

    public JSONObject addFeeDetail(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_DETAIL);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessReturnPayFee = new JSONObject();
        businessReturnPayFee.putAll(paramInJson);
        businessReturnPayFee.put("detailId", "-1");
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessFeeDetail", businessReturnPayFee);
        return business;
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public JSONObject deleteReturnPayFee(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_DELETE_RETURN_PAY_FEE);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessReturnPayFee = new JSONObject();
        businessReturnPayFee.putAll(paramInJson);
        //计算 应收金额
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessReturnPayFee", businessReturnPayFee);
        return business;
    }

}
