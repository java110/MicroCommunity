package com.java110.api.listener.fee;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeFeeConfigConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.feeConfig.ApiFeeConfigDataVo;
import com.java110.vo.api.feeConfig.ApiFeeConfigVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询小区侦听类
 */
@Java110Listener("listFeeConfigsListener")
public class ListFeeConfigsListener extends AbstractServiceApiListener {

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeFeeConfigConstant.LIST_FEECONFIGS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IFeeConfigInnerServiceSMO getFeeConfigInnerServiceSMOImpl() {
        return feeConfigInnerServiceSMOImpl;
    }

    public void setFeeConfigInnerServiceSMOImpl(IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl) {
        this.feeConfigInnerServiceSMOImpl = feeConfigInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        FeeConfigDto feeConfigDto = BeanConvertUtil.covertBean(reqJson, FeeConfigDto.class);

        int count = feeConfigInnerServiceSMOImpl.queryFeeConfigsCount(feeConfigDto);

        List<ApiFeeConfigDataVo> feeConfigs = null;

        if (count > 0) {
            feeConfigs = BeanConvertUtil.covertBeanList(feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto), ApiFeeConfigDataVo.class);
        } else {
            feeConfigs = new ArrayList<>();
        }

        ApiFeeConfigVo apiFeeConfigVo = new ApiFeeConfigVo();

        apiFeeConfigVo.setTotal(count);
        apiFeeConfigVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiFeeConfigVo.setFeeConfigs(feeConfigs);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiFeeConfigVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
