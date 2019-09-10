package com.java110.api.listener.visit;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.common.constant.ServiceCodeConstant;
import com.java110.common.util.BeanConvertUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.visit.IVisitInnerServiceSMO;
import com.java110.dto.visit.VisitDto;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.vo.api.visit.ApiVisitDataVo;
import com.java110.vo.api.visit.ApiVisitVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.java110.common.constant.CommonConstant;
import com.java110.common.constant.BusinessTypeConstant;
import com.java110.common.constant.ServiceCodeVisitConstant;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询小区侦听类
 */
@Java110Listener("listVisitsListener")
public class ListVisitsListener extends AbstractServiceApiListener {

    @Autowired
    private IVisitInnerServiceSMO visitInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeVisitConstant.LIST_VISITS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IVisitInnerServiceSMO getVisitInnerServiceSMOImpl() {
        return visitInnerServiceSMOImpl;
    }

    public void setVisitInnerServiceSMOImpl(IVisitInnerServiceSMO visitInnerServiceSMOImpl) {
        this.visitInnerServiceSMOImpl = visitInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        VisitDto visitDto = BeanConvertUtil.covertBean(reqJson, VisitDto.class);

        int count = visitInnerServiceSMOImpl.queryVisitsCount(visitDto);

        List<ApiVisitDataVo> visits = null;

        if (count > 0) {
            visits = BeanConvertUtil.covertBeanList(visitInnerServiceSMOImpl.queryVisits(visitDto), ApiVisitDataVo.class);
        } else {
            visits = new ArrayList<>();
        }

        ApiVisitVo apiVisitVo = new ApiVisitVo();

        apiVisitVo.setTotal(count);
        apiVisitVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiVisitVo.setVisits(visits);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiVisitVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
