package com.java110.api.bmo.returnPayFee.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.returnPayFee.IReturnPayFeeBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.returnPayFee.ReturnPayFeeDto;
import com.java110.intf.fee.IFeeDetailInnerServiceSMO;
import com.java110.intf.fee.IReturnPayFeeInnerServiceSMO;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.fee.ReturnPayFeePo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("returnPayFeeBMOImpl")
public class ReturnPayFeeBMOImpl extends ApiBaseBMO implements IReturnPayFeeBMO {

    @Autowired
    private IReturnPayFeeInnerServiceSMO returnPayFeeInnerServiceSMOImpl;
    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addReturnPayFee(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        paramInJson.put("returnFeeId", "-1");
        ReturnPayFeePo returnPayFeePo = BeanConvertUtil.covertBean(paramInJson, ReturnPayFeePo.class);
        super.insert(dataFlowContext, returnPayFeePo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_RETURN_PAY_FEE);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateReturnPayFee(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        ReturnPayFeeDto returnPayFeeDto = new ReturnPayFeeDto();
        returnPayFeeDto.setReturnFeeId(paramInJson.getString("returnFeeId"));
        List<ReturnPayFeeDto> returnPayFeeDtos = returnPayFeeInnerServiceSMOImpl.queryReturnPayFees(returnPayFeeDto);

        Assert.listOnlyOne(returnPayFeeDtos, "未找到需要修改的活动 或多条数据");


        JSONObject businessReturnPayFee = new JSONObject();
        businessReturnPayFee.putAll(BeanConvertUtil.beanCovertMap(returnPayFeeDtos.get(0)));
        businessReturnPayFee.putAll(paramInJson);
        ReturnPayFeePo returnPayFeePo = BeanConvertUtil.covertBean(businessReturnPayFee, ReturnPayFeePo.class);
        super.update(dataFlowContext, returnPayFeePo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_RETURN_PAY_FEE);
    }

    public void updateFeeDetail(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        FeeDetailDto feeDetailDto = new FeeDetailDto();
        feeDetailDto.setDetailId(paramInJson.getString("detailId"));
        List<FeeDetailDto> feeDetailDtos = feeDetailInnerServiceSMOImpl.queryFeeDetails(feeDetailDto);
        Assert.listOnlyOne(feeDetailDtos, "未找到需要修改的活动 或多条数据");

        JSONObject businessReturnPayFee = new JSONObject();
        businessReturnPayFee.putAll(BeanConvertUtil.beanCovertMap(feeDetailDtos.get(0)));
        businessReturnPayFee.putAll(paramInJson);
        PayFeeDetailPo returnPayFeePo = BeanConvertUtil.covertBean(businessReturnPayFee, PayFeeDetailPo.class);
        super.update(dataFlowContext, returnPayFeePo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_FEE_DETAIL);
    }


    public void addFeeDetail(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        JSONObject businessReturnPayFee = new JSONObject();
        businessReturnPayFee.putAll(paramInJson);
        businessReturnPayFee.put("detailId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        PayFeeDetailPo returnPayFeePo = BeanConvertUtil.covertBean(businessReturnPayFee, PayFeeDetailPo.class);
        super.insert(dataFlowContext, returnPayFeePo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_DETAIL);
        paramInJson.put("newDetailId", businessReturnPayFee.getString("detailId"));
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteReturnPayFee(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        ReturnPayFeePo returnPayFeePo = BeanConvertUtil.covertBean(paramInJson, ReturnPayFeePo.class);
        super.insert(dataFlowContext, returnPayFeePo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_RETURN_PAY_FEE);
    }

}
