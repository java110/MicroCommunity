package com.java110.api.listener.storeAttr;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.storeAttr.IStoreAttrInnerServiceSMO;
import com.java110.dto.store.StoreAttrDto;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeStoreAttrConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.storeAttr.ApiStoreAttrDataVo;
import com.java110.vo.api.storeAttr.ApiStoreAttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询小区侦听类
 */
@Java110Listener("listStoreAttrsListener")
public class ListStoreAttrsListener extends AbstractServiceApiListener {

    @Autowired
    private IStoreAttrInnerServiceSMO storeAttrInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeStoreAttrConstant.LIST_STOREATTRS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IStoreAttrInnerServiceSMO getStoreAttrInnerServiceSMOImpl() {
        return storeAttrInnerServiceSMOImpl;
    }

    public void setStoreAttrInnerServiceSMOImpl(IStoreAttrInnerServiceSMO storeAttrInnerServiceSMOImpl) {
        this.storeAttrInnerServiceSMOImpl = storeAttrInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        StoreAttrDto storeAttrDto = BeanConvertUtil.covertBean(reqJson, StoreAttrDto.class);

        int count = storeAttrInnerServiceSMOImpl.queryStoreAttrsCount(storeAttrDto);

        List<ApiStoreAttrDataVo> storeAttrs = null;

        if (count > 0) {
            storeAttrs = BeanConvertUtil.covertBeanList(storeAttrInnerServiceSMOImpl.queryStoreAttrs(storeAttrDto), ApiStoreAttrDataVo.class);
        } else {
            storeAttrs = new ArrayList<>();
        }

        ApiStoreAttrVo apiStoreAttrVo = new ApiStoreAttrVo();

        apiStoreAttrVo.setTotal(count);
        apiStoreAttrVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiStoreAttrVo.setStoreAttrs(storeAttrs);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiStoreAttrVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
