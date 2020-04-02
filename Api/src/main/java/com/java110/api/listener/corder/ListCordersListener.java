package com.java110.api.listener.corder;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.corder.ICordersInnerServiceSMO;
import com.java110.dto.corder.CorderDto;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeActivitiesConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.corder.ApiCorderDataVo;
import com.java110.vo.api.corder.ApiCorderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Java110Listener("listCordersListener")
public class ListCordersListener extends AbstractServiceApiListener {
    @Autowired
    private ICordersInnerServiceSMO cordersInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        CorderDto corderDto = BeanConvertUtil.covertBean(reqJson, CorderDto.class);

        int count = cordersInnerServiceSMOImpl.queryCordersCount(corderDto);

        List<ApiCorderDataVo> corderVos = null;

        if (count > 0) {
            corderVos = BeanConvertUtil.covertBeanList(cordersInnerServiceSMOImpl.queryCorders(corderDto), ApiCorderDataVo.class);
        } else {
            corderVos = new ArrayList<>();
        }


        ApiCorderVo corderVo = new  ApiCorderVo();

        corderVo.setTotal(count);
        corderVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        corderVo.setCorderDataVos(corderVos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(corderVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeActivitiesConstant.LIST_CORDERS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }
}
