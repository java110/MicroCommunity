package com.java110.api.listener.fee;


import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.constant.ServiceCodeConstant;
import com.java110.common.exception.ListenerExecuteException;
import com.java110.common.util.Assert;
import com.java110.common.util.BeanConvertUtil;
import com.java110.common.util.DateUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.fee.IFeeDetailInnerServiceSMO;
import com.java110.dto.FeeDetailDto;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.vo.api.ApiFeeDetailDataVo;
import com.java110.vo.api.ApiFeeDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 小区楼数据层侦听类
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@Java110Listener("queryFeeDetail")
public class QueryFeeDetailListener extends AbstractServiceApiDataFlowListener {

    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_QUERY_FEE_DETAIL;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    /**
     * 业务层数据处理
     *
     * @param event 时间对象
     */
    @Override
    public void soService(ServiceDataFlowEvent event) {
        DataFlowContext dataFlowContext = event.getDataFlowContext();
        //获取请求数据
        JSONObject reqJson = dataFlowContext.getReqJson();
        validateFeeConfigData(reqJson);

        //查询总记录数
        ApiFeeDetailVo apiFeeDetailVo = new ApiFeeDetailVo();
        FeeDetailDto feeDetailDto = BeanConvertUtil.covertBean(reqJson, FeeDetailDto.class);

//        try {
//            if (reqJson.containsKey("startTime")) {
//                feeDetailDto.setStartTime(DateUtil.getDateFromString(reqJson.getString("startTime"), DateUtil.DATE_FORMATE_STRING_B));
//            }
//
//            if (reqJson.containsKey("endTime")) {
//                feeDetailDto.setStartTime(DateUtil.getDateFromString(reqJson.getString("endTime"), DateUtil.DATE_FORMATE_STRING_B));
//            }
//        } catch (Exception e) {
//            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, e.getMessage() + "传入开始时间或结束时间格式错误 c");
//        }


        int total = feeDetailInnerServiceSMOImpl.queryFeeDetailsCount(feeDetailDto);
        apiFeeDetailVo.setTotal(total);
        if (total > 0) {
            List<FeeDetailDto> feeDetailDtos = feeDetailInnerServiceSMOImpl.queryFeeDetails(BeanConvertUtil.covertBean(reqJson, FeeDetailDto.class));
            List<ApiFeeDetailDataVo> feeDetails = BeanConvertUtil.covertBeanList(feeDetailDtos, ApiFeeDetailDataVo.class);

            //reFreshCreateTime(feeDetails, feeDetailDtos);

            apiFeeDetailVo.setFeeDetails(feeDetails);
        }
        int row = reqJson.getInteger("row");
        apiFeeDetailVo.setRecords((int) Math.ceil((double) total / (double) row));
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiFeeDetailVo), HttpStatus.OK);

        dataFlowContext.setResponseEntity(responseEntity);
    }

    /**
     * 刷新 创建时间
     *
     * @param feeDetails    返回对象
     * @param feeDetailDtos 数据传输对象
     */
    private void reFreshCreateTime(List<ApiFeeDetailDataVo> feeDetails, List<FeeDetailDto> feeDetailDtos) {
        for (ApiFeeDetailDataVo feeDetailDataVo : feeDetails) {
            for (FeeDetailDto feeDetailDto : feeDetailDtos) {
                if (feeDetailDataVo.getDetailId().equals(feeDetailDto.getDetailId())) {
                    feeDetailDataVo.setCreateTime(DateUtil.getFormatTimeString(feeDetailDto.getCreateTime(), DateUtil.DATE_FORMATE_STRING_A));
                }
            }
        }
    }

    /**
     * 校验查询条件是否满足条件
     *
     * @param reqJson 包含查询条件
     */
    private void validateFeeConfigData(JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求中未包含communityId信息");
        Assert.jsonObjectHaveKey(reqJson, "feeId", "请求中未包含feeId信息");


    }

    @Override
    public int getOrder() {
        return super.DEFAULT_ORDER;
    }

    public IFeeDetailInnerServiceSMO getFeeDetailInnerServiceSMOImpl() {
        return feeDetailInnerServiceSMOImpl;
    }

    public void setFeeDetailInnerServiceSMOImpl(IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl) {
        this.feeDetailInnerServiceSMOImpl = feeDetailInnerServiceSMOImpl;
    }
}
