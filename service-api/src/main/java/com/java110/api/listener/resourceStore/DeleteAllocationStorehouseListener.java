package com.java110.api.listener.resourceStore;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.allocationStorehouse.IAllocationStorehouseBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.allocationStorehouseApply.AllocationStorehouseApplyDto;
import com.java110.intf.store.IAllocationStorehouseApplyInnerServiceSMO;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.ServiceCodeAllocationStorehouseConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("deleteAllocationStorehouseListener")
public class DeleteAllocationStorehouseListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IAllocationStorehouseBMO allocationStorehouseBMOImpl;

    @Autowired
    private IAllocationStorehouseApplyInnerServiceSMO allocationStorehouseApplyInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "applyId", "调拨编号不能为空");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        //获取申请id
        String applyId = reqJson.getString("applyId");
        AllocationStorehouseApplyDto allocationStorehouseApplyDto = new AllocationStorehouseApplyDto();
        allocationStorehouseApplyDto.setApplyId(applyId);
        //查询调拨申请信息
        List<AllocationStorehouseApplyDto> allocationStorehouseApplyDtos = allocationStorehouseApplyInnerServiceSMOImpl.queryAllocationStorehouseApplys(allocationStorehouseApplyDto);
        Assert.listOnlyOne(allocationStorehouseApplyDtos, "查询调拨申请表错误！");
        //获取状态
        String state = allocationStorehouseApplyDtos.get(0).getState();
        if (!StringUtil.isEmpty(state) && state.equals("1200")) { //1200表示调拨申请状态
            allocationStorehouseBMOImpl.deleteAllocationStorehouse(reqJson, context);
        } else {
            ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_BUSINESS_VERIFICATION, "您的调拨订单已经状态已改变，无法进行取消操作！！");
            context.setResponseEntity(responseEntity);
            return;
        }
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeAllocationStorehouseConstant.DELETE_ALLOCATIONSTOREHOUSE;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

}
