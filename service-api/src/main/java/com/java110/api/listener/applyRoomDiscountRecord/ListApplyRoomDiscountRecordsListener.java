package com.java110.api.listener.applyRoomDiscountRecord;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.applyRoomDiscountRecord.ApplyRoomDiscountRecordDto;
import com.java110.intf.community.IApplyRoomDiscountRecordInnerServiceSMO;
import com.java110.utils.constant.ServiceCodeApplyRoomDiscountRecordConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
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
@Java110Listener("listApplyRoomDiscountRecordsListener")
public class ListApplyRoomDiscountRecordsListener extends AbstractServiceApiListener {

    @Autowired
    private IApplyRoomDiscountRecordInnerServiceSMO applyRoomDiscountRecordInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeApplyRoomDiscountRecordConstant.LIST_APPLYROOMDISCOUNTRECORDS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IApplyRoomDiscountRecordInnerServiceSMO getApplyRoomDiscountRecordInnerServiceSMOImpl() {
        return applyRoomDiscountRecordInnerServiceSMOImpl;
    }

    public void setApplyRoomDiscountRecordInnerServiceSMOImpl(IApplyRoomDiscountRecordInnerServiceSMO applyRoomDiscountRecordInnerServiceSMOImpl) {
        this.applyRoomDiscountRecordInnerServiceSMOImpl = applyRoomDiscountRecordInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        ApplyRoomDiscountRecordDto applyRoomDiscountRecordDto = BeanConvertUtil.covertBean(reqJson, ApplyRoomDiscountRecordDto.class);

        int count = applyRoomDiscountRecordInnerServiceSMOImpl.queryApplyRoomDiscountRecordsCount(applyRoomDiscountRecordDto);

        List<ApplyRoomDiscountRecordDto> applyRoomDiscountRecordDtos = null;

        if (count > 0) {
            applyRoomDiscountRecordDtos = applyRoomDiscountRecordInnerServiceSMOImpl.queryApplyRoomDiscountRecords(applyRoomDiscountRecordDto);
        } else {
            applyRoomDiscountRecordDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, applyRoomDiscountRecordDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
