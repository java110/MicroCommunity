package com.java110.api.listener.fee;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.intf.fee.ITempCarFeeConfigInnerServiceSMO;
import com.java110.dto.tempCarFeeConfig.TempCarFeeConfigDto;
import com.java110.utils.constant.ServiceCodeTempCarFeeConfigConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询小区侦听类
 */
@Java110Listener("listTempCarFeeConfigsListener")
public class ListTempCarFeeConfigsListener extends AbstractServiceApiListener {

    @Autowired
    private ITempCarFeeConfigInnerServiceSMO tempCarFeeConfigInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeTempCarFeeConfigConstant.LIST_TEMPCARFEECONFIGS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public ITempCarFeeConfigInnerServiceSMO getTempCarFeeConfigInnerServiceSMOImpl() {
        return tempCarFeeConfigInnerServiceSMOImpl;
    }

    public void setTempCarFeeConfigInnerServiceSMOImpl(ITempCarFeeConfigInnerServiceSMO tempCarFeeConfigInnerServiceSMOImpl) {
        this.tempCarFeeConfigInnerServiceSMOImpl = tempCarFeeConfigInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        TempCarFeeConfigDto tempCarFeeConfigDto = BeanConvertUtil.covertBean(reqJson, TempCarFeeConfigDto.class);

        int count = tempCarFeeConfigInnerServiceSMOImpl.queryTempCarFeeConfigsCount(tempCarFeeConfigDto);

        List<TempCarFeeConfigDto> tempCarFeeConfigDtos = null;

        if (count > 0) {
            tempCarFeeConfigDtos = tempCarFeeConfigInnerServiceSMOImpl.queryTempCarFeeConfigs(tempCarFeeConfigDto);
        } else {
            tempCarFeeConfigDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, tempCarFeeConfigDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
