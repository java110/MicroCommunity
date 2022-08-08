package com.java110.community.cmd.ownerRepair;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.intf.community.IRepairInnerServiceSMO;
import com.java110.intf.community.IRepairPoolV1InnerServiceSMO;
import com.java110.intf.community.IRepairUserInnerServiceSMO;
import com.java110.intf.community.IRepairUserV1InnerServiceSMO;
import com.java110.po.owner.RepairPoolPo;
import com.java110.po.owner.RepairUserPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Java110Cmd(serviceCode = "ownerRepair.repairStart")
public class RepairStartCmd extends Cmd {

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMOImpl;

    @Autowired
    private IRepairUserInnerServiceSMO repairUserInnerServiceSMOImpl;

    @Autowired
    private IRepairPoolV1InnerServiceSMO repairPoolV1InnerServiceSMOImpl;

    @Autowired
    private IRepairUserV1InnerServiceSMO repairUserV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "repairId", "未包含报修单信息");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        RepairDto repairDto = new RepairDto();
        repairDto.setRepairId(reqJson.getString("repairId"));
        //查询报修信息
        List<RepairDto> repairDtos = repairInnerServiceSMOImpl.queryRepairs(repairDto);
        Assert.listOnlyOne(repairDtos, "查询报修信息错误！");
        String state = repairDtos.get(0).getState();
        int flag = 0;
        if (!StringUtil.isEmpty(state) && state.equals(RepairDto.STATE_STOP)) { //状态是暂停状态
            RepairPoolPo repairPoolPo = new RepairPoolPo();
            repairPoolPo.setRepairId(reqJson.getString("repairId"));
            repairPoolPo.setState(RepairDto.STATE_TAKING); //状态变为接单状态
            flag = repairPoolV1InnerServiceSMOImpl.updateRepairPoolNew(repairPoolPo);
            if(flag < 1){
                throw new CmdException("修改工单失败");
            }
            RepairUserDto repairUserDto = new RepairUserDto();
            repairUserDto.setRepairId(reqJson.getString("repairId"));
            repairUserDto.setState(RepairUserDto.STATE_STOP); //暂停状态
            //查询报修派单
            List<RepairUserDto> repairUserDtos = repairUserInnerServiceSMOImpl.queryRepairUsers(repairUserDto);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (repairUserDtos != null && repairUserDtos.size() > 0) {
                for (RepairUserDto repairUser : repairUserDtos) {
                    if (repairUser.getEndTime() == null) {
                        RepairUserPo repairUserPo = new RepairUserPo();
                        repairUserPo.setRuId(repairUser.getRuId());
                        repairUserPo.setEndTime(simpleDateFormat.format(new Date()));
                        //修改暂停报修状态
                        flag = repairUserV1InnerServiceSMOImpl.updateRepairUserNew(repairUserPo);
                        if(flag < 1){
                            throw new CmdException("修改工单失败");
                        }
                    }
                }
            } else {
                throw new IllegalArgumentException("启动报修单错误！");
            }
        }
    }
}
