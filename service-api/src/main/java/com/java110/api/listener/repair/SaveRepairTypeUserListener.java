package com.java110.api.listener.repair;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.repair.IRepairTypeUserBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.repair.RepairTypeUserDto;
import com.java110.intf.community.IRepairTypeUserInnerServiceSMO;
import com.java110.utils.constant.ServiceCodeRepairTypeUserConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.util.List;

/**
 * 保存商户侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("saveRepairTypeUserListener")
public class SaveRepairTypeUserListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IRepairTypeUserBMO repairTypeUserBMOImpl;

    @Autowired
    private IRepairTypeUserInnerServiceSMO repairTypeUserInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");
        Assert.hasKeyAndValue(reqJson, "staffId", "请求报文中未包含staffId");
        Assert.hasKeyAndValue(reqJson, "staffName", "请求报文中未包含staffName");
        Assert.hasKeyAndValue(reqJson, "repairType", "请求报文中未包含repairType");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        reqJson.put("state", RepairTypeUserDto.STATE_ONLINE);
        RepairTypeUserDto repairTypeUserDto = new RepairTypeUserDto();
        repairTypeUserDto.setRepairType(reqJson.getString("repairType"));
        repairTypeUserDto.setStaffId(reqJson.getString("staffId"));
        repairTypeUserDto.setCommunityId(reqJson.getString("communityId"));
        List<RepairTypeUserDto> repairTypeUserDtos = repairTypeUserInnerServiceSMOImpl.queryRepairTypeUsers(repairTypeUserDto);
        Assert.listIsNull(repairTypeUserDtos, "维修师傅已存在，不能重复添加！");
        repairTypeUserBMOImpl.addRepairTypeUser(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeRepairTypeUserConstant.ADD_REPAIRTYPEUSER;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

}
