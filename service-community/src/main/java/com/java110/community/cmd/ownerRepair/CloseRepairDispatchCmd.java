package com.java110.community.cmd.ownerRepair;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.DataFlowContext;
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
import com.java110.utils.constant.StateConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

@Java110Cmd(serviceCode = "ownerRepair.closeRepairDispatch")
public class CloseRepairDispatchCmd extends Cmd {

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMOImpl;

    @Autowired
    private IRepairUserInnerServiceSMO repairUserInnerServiceSMOImpl;

    @Autowired
    private IRepairUserV1InnerServiceSMO repairUserV1InnerServiceSMOImpl;


    @Autowired
    private IRepairPoolV1InnerServiceSMO repairPoolV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        Assert.hasKeyAndValue(reqJson, "state", "未包含处理信息");
        Assert.hasKeyAndValue(reqJson, "context", "未包含处理内容");
        Assert.hasKeyAndValue(reqJson, "repairId", "未包含报修单信息");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
        Assert.hasKeyAndValue(reqJson, "staffId", "未包含员工ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        modifyBusinessRepairUser(reqJson);

        modifyBusinessRepair(reqJson);
    }

    public void modifyBusinessRepairUser(JSONObject paramInJson) {

        RepairUserDto repairUserDto = new RepairUserDto();
        repairUserDto.setRepairId(paramInJson.getString("repairId"));
        repairUserDto.setUserId(paramInJson.getString("staffId"));
        List<RepairUserDto> repairUserDtos = repairUserInnerServiceSMOImpl.queryRepairUsers(repairUserDto);
        Assert.isOne(repairUserDtos, "查询到多条数据，repairId=" + repairUserDto.getRepairId() + " userId = " + repairUserDto.getUserId());
        JSONObject businessObj = new JSONObject();
        businessObj.putAll(BeanConvertUtil.beanCovertMap(repairUserDtos.get(0)));
        businessObj.put("state", paramInJson.getString("state"));
        businessObj.put("context", paramInJson.getString("context"));

        RepairUserPo repairUserPo = BeanConvertUtil.covertBean(businessObj, RepairUserPo.class);
        int flag = repairUserV1InnerServiceSMOImpl.updateRepairUserNew(repairUserPo);
        if (flag < 1) {
            throw new CmdException("修改用户失败");
        }
    }

    public void modifyBusinessRepair(JSONObject paramInJson) {
        //查询报修单
        RepairDto repairDto = new RepairDto();
        repairDto.setRepairId(paramInJson.getString("repairId"));

        List<RepairDto> repairDtos = repairInnerServiceSMOImpl.queryRepairs(repairDto);

        Assert.isOne(repairDtos, "查询到多条数据，repairId=" + repairDto.getRepairId());


        JSONObject businessOwnerRepair = new JSONObject();
        businessOwnerRepair.putAll(BeanConvertUtil.beanCovertMap(repairDtos.get(0)));
        businessOwnerRepair.put("state", "10002".equals(paramInJson.getString("state")) ? StateConstant.REPAIR_DISPATCH_FINISH : StateConstant.REPAIR_NO_DISPATCH);

        RepairPoolPo repairPoolPo = BeanConvertUtil.covertBean(businessOwnerRepair, RepairPoolPo.class);
        int flag = repairPoolV1InnerServiceSMOImpl.updateRepairPoolNew(repairPoolPo);
        if (flag < 1) {
            throw new CmdException("修改失败");
        }
    }
}
