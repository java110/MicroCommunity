package com.java110.api.listener.ownerRepair;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.user.UserDto;
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

/**
 * 查询小区侦听类
 */
@Java110Listener("listStaffRepairsListener")
public class ListStaffRepairsListener extends AbstractServiceApiListener {

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMO;

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
        //查询用户信息
        List<UserDto> users = userInnerServiceSMO.getUsers(userDto);
        Assert.listOnlyOne(users, "数据异常未找到用户信息");
        if (!users.get(0).getLevelCd().equals("00")) {  //当前用户不是管理员时放入staffId的值
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
