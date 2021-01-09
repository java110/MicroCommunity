package com.java110.api.listener.fee;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.tempCarFeeConfigAttr.TempCarFeeConfigAttrDto;
import com.java110.intf.fee.ITempCarFeeConfigAttrInnerServiceSMO;
import com.java110.utils.constant.ServiceCodeTempCarFeeConfigAttrConstant;
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
@Java110Listener("listTempCarFeeConfigAttrsListener")
public class ListTempCarFeeConfigAttrsListener extends AbstractServiceApiListener {

    @Autowired
    private ITempCarFeeConfigAttrInnerServiceSMO tempCarFeeConfigAttrInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeTempCarFeeConfigAttrConstant.LIST_TEMPCARFEECONFIGATTRS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public ITempCarFeeConfigAttrInnerServiceSMO getTempCarFeeConfigAttrInnerServiceSMOImpl() {
        return tempCarFeeConfigAttrInnerServiceSMOImpl;
    }

    public void setTempCarFeeConfigAttrInnerServiceSMOImpl(ITempCarFeeConfigAttrInnerServiceSMO tempCarFeeConfigAttrInnerServiceSMOImpl) {
        this.tempCarFeeConfigAttrInnerServiceSMOImpl = tempCarFeeConfigAttrInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        TempCarFeeConfigAttrDto tempCarFeeConfigAttrDto = BeanConvertUtil.covertBean(reqJson, TempCarFeeConfigAttrDto.class);

        int count = tempCarFeeConfigAttrInnerServiceSMOImpl.queryTempCarFeeConfigAttrsCount(tempCarFeeConfigAttrDto);

        List<TempCarFeeConfigAttrDto> tempCarFeeConfigAttrDtos = null;

        if (count > 0) {
            tempCarFeeConfigAttrDtos = tempCarFeeConfigAttrInnerServiceSMOImpl.queryTempCarFeeConfigAttrs(tempCarFeeConfigAttrDto);
        } else {
            tempCarFeeConfigAttrDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, tempCarFeeConfigAttrDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
