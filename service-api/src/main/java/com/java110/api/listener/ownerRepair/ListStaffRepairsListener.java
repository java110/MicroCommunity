package com.java110.api.listener.ownerRepair;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.intf.community.IRepairInnerServiceSMO;
import com.java110.dto.repair.RepairDto;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.utils.constant.ServiceCodeOwnerRepairConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 查询小区侦听类
 */
@Java110Listener("listStaffRepairsListener")
public class ListStaffRepairsListener extends AbstractServiceApiListener {

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMOImpl;

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeOwnerRepairConstant.LIST_STAFF_REPAIRS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

    public IRepairInnerServiceSMO getRepairInnerServiceSMOImpl() {
        return repairInnerServiceSMOImpl;
    }

    public void setRepairInnerServiceSMOImpl(IRepairInnerServiceSMO repairInnerServiceSMOImpl) {
        this.repairInnerServiceSMOImpl = repairInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "请求中未包含小区ID");
        Assert.hasKeyAndValue(reqJson, "userId", "请求中未包含员工信息");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        RepairDto ownerRepairDto = BeanConvertUtil.covertBean(reqJson, RepairDto.class);
        //获取用户id
        String userId = reqJson.getString("userId");
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        //报修待办查看所有记录权限
        BasePrivilegeDto basePrivilegeDto = new BasePrivilegeDto();
        basePrivilegeDto.setResource("/viewListStaffRepairs");
        basePrivilegeDto.setUserId(userId);
        List<Map> privileges = menuInnerServiceSMOImpl.checkUserHasResource(basePrivilegeDto);
        if (privileges.size()==0) {
            ownerRepairDto.setStaffId(reqJson.getString("userId"));
        }
        int count = repairInnerServiceSMOImpl.queryStaffRepairsCount(ownerRepairDto);
        List<RepairDto> ownerRepairs = null;
        if (count > 0) {
            ownerRepairs = repairInnerServiceSMOImpl.queryStaffRepairs(ownerRepairDto);
        } else {
            ownerRepairs = new ArrayList<>();
        }
        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, ownerRepairs);
        context.setResponseEntity(responseEntity);
    }
}
