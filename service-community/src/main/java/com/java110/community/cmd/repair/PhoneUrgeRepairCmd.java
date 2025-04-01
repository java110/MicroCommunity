package com.java110.community.cmd.repair;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.privilege.BasePrivilegeDto;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.dto.repairEvent.RepairEventDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.community.IRepairEventV1InnerServiceSMO;
import com.java110.intf.community.IRepairInnerServiceSMO;
import com.java110.intf.community.IRepairUserInnerServiceSMO;
import com.java110.intf.order.IPrivilegeInnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.repairEvent.RepairEventPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

/**
 * 用户催单
 */
@Java110Cmd(serviceCode = "repair.phoneUrgeRepair")
public class PhoneUrgeRepairCmd extends Cmd {

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMOImpl;

    @Autowired
    private IRepairEventV1InnerServiceSMO repairEventV1InnerServiceSMOImpl;

    @Autowired
    private IPrivilegeInnerServiceSMO privilegeInnerServiceSMOImpl;

    @Autowired
    private IRepairUserInnerServiceSMO repairUserInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "repairId", "未包含工单ID");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        String userId = CmdContextUtils.getUserId(context);

        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户未登录");
        RepairDto repairDto = new RepairDto();
        repairDto.setRepairId(reqJson.getString("repairId"));
        repairDto.setTel(userDtos.get(0).getTel());
        List<RepairDto> repairDtos = repairInnerServiceSMOImpl.queryRepairs(repairDto);
        Assert.listOnlyOne(repairDtos, "报修不存在");


        RepairEventPo repairEventPo = new RepairEventPo();
        repairEventPo.setEventId(GenerateCodeFactory.getGeneratorId("11"));

        repairEventPo.setRepairId(repairDtos.get(0).getRepairId());
        repairEventPo.setRemark("用户着急催单了，请尽快处理！");
        repairEventPo.setEventType(RepairEventDto.EVENT_TYPE_URGE);
        repairEventPo.setCommunityId(repairDtos.get(0).getCommunityId());
        queryStaff(repairEventPo, repairDtos.get(0));


        repairEventV1InnerServiceSMOImpl.saveRepairEvent(repairEventPo);
    }

    private void queryStaff(RepairEventPo repairEventPo, RepairDto repairDto) {

        if (RepairDto.STATE_WAIT.equals(repairDto.getState())) {
            BasePrivilegeDto basePrivilegeDto = new BasePrivilegeDto();
            basePrivilegeDto.setResource("/wechatRepairRegistration");
            //basePrivilegeDto.setStoreId(communityMemberDtos.get(0).getMemberId());
            basePrivilegeDto.setCommunityId(repairDto.getCommunityId());
            List<UserDto> userDtos = privilegeInnerServiceSMOImpl.queryPrivilegeUsers(basePrivilegeDto);
            if (ListUtil.isNull(userDtos)) {
                repairEventPo.setStaffId("-1");
                repairEventPo.setStaffName("未找到派单人员");
                return;
            }
            UserDto userDto = new UserDto();
            userDto.setUserId(userDtos.get(0).getUserId());
            List<UserDto> tmpUserDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
            if (ListUtil.isNull(tmpUserDtos)) {
                repairEventPo.setStaffId("-1");
                repairEventPo.setStaffName("未找到派单人员");
                return;
            }
            repairEventPo.setStaffId(tmpUserDtos.get(0).getUserId());
            repairEventPo.setStaffName("派单人员-" + tmpUserDtos.get(0).getName());
            return;
        }

        RepairUserDto repairUserDto = new RepairUserDto();
        repairUserDto.setRepairId(repairDto.getRepairId());
        repairUserDto.setState(RepairUserDto.STATE_DOING);
        List<RepairUserDto> repairUserDtos = repairUserInnerServiceSMOImpl.queryRepairUsers(repairUserDto);

        if(ListUtil.isNull(repairUserDtos)){
            repairEventPo.setStaffId("-1");
            repairEventPo.setStaffName("未找到报修师傅");
            return;
        }

        repairEventPo.setStaffId(repairUserDtos.get(0).getStaffId());
        repairEventPo.setStaffName(repairUserDtos.get(0).getStaffName());
    }
}
