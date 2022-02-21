package com.java110.api.listener.returnPayFee;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.returnPayFee.ReturnPayFeeDto;
import com.java110.intf.fee.IReturnPayFeeInnerServiceSMO;
import com.java110.utils.constant.ServiceCodeReturnPayFeeConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.api.returnPayFee.ApiReturnPayFeeDataVo;
import com.java110.vo.api.returnPayFee.ApiReturnPayFeeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询小区侦听类
 */
@Java110Listener("listReturnPayFeesListener")
public class ListReturnPayFeesListener extends AbstractServiceApiListener {

    @Autowired
    private IReturnPayFeeInnerServiceSMO returnPayFeeInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeReturnPayFeeConstant.LIST_RETURNPAYFEES;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IReturnPayFeeInnerServiceSMO getReturnPayFeeInnerServiceSMOImpl() {
        return returnPayFeeInnerServiceSMOImpl;
    }

    public void setReturnPayFeeInnerServiceSMOImpl(IReturnPayFeeInnerServiceSMO returnPayFeeInnerServiceSMOImpl) {
        this.returnPayFeeInnerServiceSMOImpl = returnPayFeeInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        ReturnPayFeeDto returnPayFeeDto = BeanConvertUtil.covertBean(reqJson, ReturnPayFeeDto.class);

        int count = returnPayFeeInnerServiceSMOImpl.queryReturnPayFeesCount(returnPayFeeDto);

        List<ReturnPayFeeDto> returnPayFeeDtos = null;

        List<ApiReturnPayFeeDataVo> returnPayFees;

        if (count > 0) {
            //注意这里处理 记得测试退费逻辑
            returnPayFeeDtos = returnPayFeeInnerServiceSMOImpl.queryReturnPayFees(returnPayFeeDto);
            //List<ReturnPayFeeDto> returnPayFeeDtoList = new ArrayList<>();
            for (ReturnPayFeeDto returnPayFee : returnPayFeeDtos) {
                //获取付款方标识
                String payerObjType = returnPayFee.getPayerObjType();
                if (!StringUtil.isEmpty(payerObjType) && payerObjType.equals("3333")) { //房屋
                    returnPayFeeDto.setReturnFeeId(returnPayFee.getReturnFeeId());
                    List<ReturnPayFeeDto> returnPayFeeList = returnPayFeeInnerServiceSMOImpl.queryRoomReturnPayFees(returnPayFeeDto);
                    if (returnPayFeeList == null || returnPayFeeList.size() < 1) {
                        continue;
                    }
                    //returnPayFeeDtoList.add(returnPayFeeList.get(0));
                    BeanConvertUtil.covertBean(returnPayFeeList.get(0), returnPayFee);
                } else if (!StringUtil.isEmpty(payerObjType) && payerObjType.equals("6666")) { //车辆
                    returnPayFeeDto.setReturnFeeId(returnPayFee.getReturnFeeId());
                    List<ReturnPayFeeDto> returnPayFeeList = returnPayFeeInnerServiceSMOImpl.queryCarReturnPayFees(returnPayFeeDto);
                    if (returnPayFeeList == null || returnPayFeeList.size() < 1) {
                        continue;
                    }
                    BeanConvertUtil.covertBean(returnPayFeeList.get(0), returnPayFee);
                    //returnPayFeeDtoList.add(returnPayFeeList.get(0));
                } else {
                    continue;
                }
            }
            returnPayFees = BeanConvertUtil.covertBeanList(returnPayFeeDtos, ApiReturnPayFeeDataVo.class);
        } else {
            returnPayFees = new ArrayList<>();
        }

        ApiReturnPayFeeVo apiReturnPayFeeVo = new ApiReturnPayFeeVo();

        apiReturnPayFeeVo.setTotal(count);
        apiReturnPayFeeVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiReturnPayFeeVo.setReturnPayFees(returnPayFees);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiReturnPayFeeVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}