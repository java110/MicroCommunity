package com.java110.api.listener.resourceStore;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.userStorehouse.UserStorehouseDto;
import com.java110.intf.store.IUserStorehouseInnerServiceSMO;
import com.java110.utils.constant.ServiceCodeUserStorehouseConstant;
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
@Java110Listener("listUserStorehousesListener")
public class ListUserStorehousesListener extends AbstractServiceApiListener {

    @Autowired
    private IUserStorehouseInnerServiceSMO userStorehouseInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeUserStorehouseConstant.LIST_USERSTOREHOUSES;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IUserStorehouseInnerServiceSMO getUserStorehouseInnerServiceSMOImpl() {
        return userStorehouseInnerServiceSMOImpl;
    }

    public void setUserStorehouseInnerServiceSMOImpl(IUserStorehouseInnerServiceSMO userStorehouseInnerServiceSMOImpl) {
        this.userStorehouseInnerServiceSMOImpl = userStorehouseInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        UserStorehouseDto userStorehouseDto = BeanConvertUtil.covertBean(reqJson, UserStorehouseDto.class);

        int count = userStorehouseInnerServiceSMOImpl.queryUserStorehousesCount(userStorehouseDto);

        List<UserStorehouseDto> userStorehouseDtos = null;

        if (count > 0) {
            userStorehouseDtos = userStorehouseInnerServiceSMOImpl.queryUserStorehouses(userStorehouseDto);
        } else {
            userStorehouseDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, userStorehouseDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
