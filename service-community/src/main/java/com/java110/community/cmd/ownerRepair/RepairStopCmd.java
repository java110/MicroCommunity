package com.java110.community.cmd.ownerRepair;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.community.IRepairInnerServiceSMO;
import com.java110.intf.community.IRepairPoolV1InnerServiceSMO;
import com.java110.intf.community.IRepairUserInnerServiceSMO;
import com.java110.intf.community.IRepairUserV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.owner.RepairPoolPo;
import com.java110.po.owner.RepairUserPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Java110Cmd(serviceCode = "ownerRepair.repairStop")
public class RepairStopCmd extends Cmd {

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMOImpl;

    @Autowired
    private IRepairUserInnerServiceSMO repairUserInnerServiceSMOImpl;

    @Autowired
    private IRepairPoolV1InnerServiceSMO repairPoolV1InnerServiceSMOImpl;

    @Autowired
    private IRepairUserV1InnerServiceSMO repairUserV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "repairId", "未包含报修单信息");
        Assert.hasKeyAndValue(reqJson, "remark", "未包含派单内容");
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
        //查询报修信息
        List<RepairDto> repairDtos = repairInnerServiceSMOImpl.queryRepairs(repairDto);
        Assert.listOnlyOne(repairDtos, "查询报修单错误！");
        String state = repairDtos.get(0).getState();
        int flag = 0;
        if (RepairDto.STATE_STOP.equals(state)) { // todo 暂停状态直接返回
            return;
        }
        //报修单不是暂停状态
        RepairPoolPo repairPoolPo = BeanConvertUtil.covertBean(reqJson, RepairPoolPo.class);
        repairPoolPo.setState(RepairDto.STATE_STOP); //将报修状态变为暂停状态
        //更新报修状态
        flag = repairPoolV1InnerServiceSMOImpl.updateRepairPoolNew(repairPoolPo);
        if (flag < 1) {
            throw new CmdException("修改工单失败");
        }
        RepairUserPo repairUserPo = new RepairUserPo();
        repairUserPo.setRuId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ruId)); //报修派单id
        repairUserPo.setRepairId(reqJson.getString("repairId")); //报修派单
        repairUserPo.setCommunityId(reqJson.getString("communityId")); //小区id
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        repairUserPo.setCreateTime(simpleDateFormat.format(new Date())); //创建时间
        repairUserPo.setState(RepairUserDto.STATE_STOP); //状态(暂停状态)
        repairUserPo.setContext(reqJson.getString("remark")); //报修内容
        repairUserPo.setStaffId(userDtos.get(0).getUserId()); //当前处理员工id
        repairUserPo.setStaffName(userDtos.get(0).getName()); //当前处理员工名称
        RepairUserDto repairUserDto = new RepairUserDto();
        repairUserDto.setRepairId(reqJson.getString("repairId"));
        repairUserDto.setState(RepairUserDto.STATE_DOING); //处理中状态
        //查询报修派单
        List<RepairUserDto> repairUserDtos = repairUserInnerServiceSMOImpl.queryRepairUsers(repairUserDto);
        Assert.listOnlyOne(repairUserDtos, "查询报修派单错误！");
        repairUserPo.setPreStaffId(repairUserDtos.get(0).getStaffId()); //上一节点处理员工id
        repairUserPo.setPreStaffName(repairUserDtos.get(0).getStaffName()); //上一节点处理员工名称
        repairUserPo.setStartTime(simpleDateFormat.format(new Date())); //开始时间
        repairUserPo.setEndTime(simpleDateFormat.format(new Date()));
        repairUserPo.setRepairEvent(RepairUserDto.REPAIR_EVENT_AUDIT_USER); //审核用户
        repairUserPo.setPreRuId(repairUserDtos.get(0).getRuId()); //上一节点处理id
        flag = repairUserV1InnerServiceSMOImpl.saveRepairUserNew(repairUserPo);
        if (flag < 1) {
            throw new CmdException("添加工单失败");
        }

    }
}
