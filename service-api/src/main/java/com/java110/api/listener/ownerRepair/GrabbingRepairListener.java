package com.java110.api.listener.ownerRepair;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ownerRepair.IOwnerRepairBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.smo.community.IRepairInnerServiceSMO;
import com.java110.core.smo.community.IRepairUserInnerServiceSMO;
import com.java110.core.smo.fee.IFeeConfigInnerServiceSMO;
import com.java110.core.smo.community.IRepairTypeUserInnerServiceSMO;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.repair.RepairTypeUserDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.po.owner.RepairUserPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ServiceCodeRepairDispatchStepConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * 完成报修
 * add by wuxw 2019-06-30
 */
@Java110Listener("grabbingRepairListener")
public class GrabbingRepairListener extends AbstractServiceApiPlusListener {


    private static Logger logger = LoggerFactory.getLogger(GrabbingRepairListener.class);

    @Autowired
    private IOwnerRepairBMO ownerRepairBMOImpl;

    @Autowired
    private IRepairUserInnerServiceSMO repairUserInnerServiceSMOImpl;

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMOImpl;

    @Autowired
    private IRepairTypeUserInnerServiceSMO repairTypeUserInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "repairId", "未包含报修单信息");
        //Assert.hasKeyAndValue(reqJson, "context", "未包含派单内容");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
        Assert.hasKeyAndValue(reqJson, "userId", "未包含用户ID");
        Assert.hasKeyAndValue(reqJson, "userName", "未包含用户名称");
        RepairDto repairDto = new RepairDto();
        repairDto.setRepairId(reqJson.getString("repairId"));
        repairDto.setCommunityId(reqJson.getString("communityId"));
        List<RepairDto> repairDtos = repairInnerServiceSMOImpl.queryRepairs(repairDto);

        Assert.listOnlyOne(repairDtos, "未找到工单信息或找到多条");

        String repairType = repairDtos.get(0).getRepairType();
        RepairTypeUserDto repairTypeUserDto = new RepairTypeUserDto();
        repairTypeUserDto.setCommunityId(reqJson.getString("communityId"));
        repairTypeUserDto.setRepairType(repairType);
        repairTypeUserDto.setStaffId(reqJson.getString("userId"));
        int count = repairTypeUserInnerServiceSMOImpl.queryRepairTypeUsersCount(repairTypeUserDto);
        if (count < 1) {
            throw new IllegalArgumentException("您没有权限强该类型报修单");
        }

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        RepairUserDto repairUserDto = new RepairUserDto();
        repairUserDto.setRepairId(reqJson.getString("repairId"));
        repairUserDto.setCommunityId(reqJson.getString("communityId"));
        repairUserDto.setRepairEvent(RepairUserDto.REPAIR_EVENT_START_USER);
        List<RepairUserDto> repairUserDtos = repairUserInnerServiceSMOImpl.queryRepairUsers(repairUserDto);
        Assert.listOnlyOne(repairUserDtos, "未找到开始节点或找到多条");

        String userId = reqJson.getString("userId");
        String userName = reqJson.getString("userName");
        RepairUserPo repairUserPo = new RepairUserPo();
        repairUserPo.setRuId("-1");
        repairUserPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        repairUserPo.setState(RepairUserDto.STATE_DOING);
        repairUserPo.setRepairId(reqJson.getString("repairId"));
        repairUserPo.setStaffId(userId);
        repairUserPo.setStaffName(userName);
        repairUserPo.setPreStaffId(repairUserDtos.get(0).getStaffId());
        repairUserPo.setPreStaffName(repairUserDtos.get(0).getStaffName());
        repairUserPo.setRepairEvent(RepairUserDto.REPAIR_EVENT_AUDIT_USER);
        repairUserPo.setContext("");
        repairUserPo.setCommunityId(reqJson.getString("communityId"));
        super.insert(context, repairUserPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_REPAIR_USER);
        ownerRepairBMOImpl.modifyBusinessRepairDispatch(reqJson, context, RepairDto.STATE_TAKING);


        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_OK, ResultVo.MSG_OK);

        context.setResponseEntity(responseEntity);

    }

    @Override
    public String getServiceCode() {
        return ServiceCodeRepairDispatchStepConstant.BINDING_GRABBING_REPAIR;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
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
}
