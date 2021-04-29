package com.java110.api.listener.basePrivilege;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.utils.constant.ServiceCodeBasePrivilegeConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.vo.api.basePrivilege.ApiBasePrivilegeDataVo;
import com.java110.vo.api.basePrivilege.ApiBasePrivilegeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询小区侦听类
 */
@Java110Listener("listBasePrivilegesListener")
public class ListBasePrivilegesListener extends AbstractServiceApiListener {

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeBasePrivilegeConstant.LIST_BASEPRIVILEGES;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IMenuInnerServiceSMO getMenuInnerServiceSMOImpl() {
        return menuInnerServiceSMOImpl;
    }

    public void setMenuInnerServiceSMOImpl(IMenuInnerServiceSMO menuInnerServiceSMOImpl) {
        this.menuInnerServiceSMOImpl = menuInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        BasePrivilegeDto basePrivilegeDto = BeanConvertUtil.covertBean(reqJson, BasePrivilegeDto.class);

        int count = menuInnerServiceSMOImpl.queryBasePrivilegesCount(basePrivilegeDto);

        List<ApiBasePrivilegeDataVo> basePrivileges = null;

        if (count > 0) {
            basePrivileges = BeanConvertUtil.covertBeanList(menuInnerServiceSMOImpl.queryBasePrivileges(basePrivilegeDto), ApiBasePrivilegeDataVo.class);
        } else {
            basePrivileges = new ArrayList<>();
        }

        ApiBasePrivilegeVo apiBasePrivilegeVo = new ApiBasePrivilegeVo();

        apiBasePrivilegeVo.setTotal(count);
        apiBasePrivilegeVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiBasePrivilegeVo.setBasePrivileges(basePrivileges);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiBasePrivilegeVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
