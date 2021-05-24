package com.java110.api.listener.resourceStore;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.intf.store.IAllocationStorehouseApplyInnerServiceSMO;
import com.java110.dto.allocationStorehouseApply.AllocationStorehouseApplyDto;
import com.java110.utils.constant.ServiceCodeAllocationStorehouseApplyConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 查询小区侦听类
 */
@Java110Listener("listAllocationStorehouseApplysListener")
public class ListAllocationStorehouseApplysListener extends AbstractServiceApiListener {

    @Autowired
    private IAllocationStorehouseApplyInnerServiceSMO allocationStorehouseApplyInnerServiceSMOImpl;

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeAllocationStorehouseApplyConstant.LIST_ALLOCATIONSTOREHOUSEAPPLYS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IAllocationStorehouseApplyInnerServiceSMO getAllocationStorehouseApplyInnerServiceSMOImpl() {
        return allocationStorehouseApplyInnerServiceSMOImpl;
    }

    public void setAllocationStorehouseApplyInnerServiceSMOImpl(IAllocationStorehouseApplyInnerServiceSMO allocationStorehouseApplyInnerServiceSMOImpl) {
        this.allocationStorehouseApplyInnerServiceSMOImpl = allocationStorehouseApplyInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        AllocationStorehouseApplyDto allocationStorehouseApplyDto = BeanConvertUtil.covertBean(reqJson, AllocationStorehouseApplyDto.class);
        //调拨记录（调拨记录所有权限查看所有数据）
        String userId = reqJson.getString("userId");
        BasePrivilegeDto basePrivilegeDto = new BasePrivilegeDto();
        basePrivilegeDto.setResource("/viewlistAllocationStorehouses");
        basePrivilegeDto.setUserId(userId);
        List<Map> privileges = menuInnerServiceSMOImpl.checkUserHasResource(basePrivilegeDto);
        if (privileges.size()==0) {
            allocationStorehouseApplyDto.setStartUserId(userId);
        }
        int count = allocationStorehouseApplyInnerServiceSMOImpl.queryAllocationStorehouseApplysCount(allocationStorehouseApplyDto);

        List<AllocationStorehouseApplyDto> allocationStorehouseApplyDtos = null;

        if (count > 0) {
            allocationStorehouseApplyDtos = allocationStorehouseApplyInnerServiceSMOImpl.queryAllocationStorehouseApplys(allocationStorehouseApplyDto);
        } else {
            allocationStorehouseApplyDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, allocationStorehouseApplyDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
