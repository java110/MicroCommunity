package com.java110.community.cmd.ownerRepair;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.DataFlowContext;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.community.IRepairInnerServiceSMO;
import com.java110.intf.community.IRepairPoolV1InnerServiceSMO;
import com.java110.intf.community.IRepairUserInnerServiceSMO;
import com.java110.intf.community.IRepairUserV1InnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.po.owner.RepairPoolPo;
import com.java110.po.owner.RepairUserPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.List;

@Java110Cmd(serviceCode = "ownerRepair.repairForceFinish")
public class RepairForceFinishCmd extends Cmd {

    @Autowired
    private IRepairUserInnerServiceSMO repairUserInnerServiceSMOImpl;

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMO;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMO;

    @Autowired
    private IRepairPoolV1InnerServiceSMO repairPoolV1InnerServiceSMOImpl;

    @Autowired
    private IRepairUserV1InnerServiceSMO repairUserV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "repairId", "未包含报修单信息");
        Assert.hasKeyAndValue(reqJson, "context", "未包含派单内容");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        String userId = context.getReqHeaders().get("user-id");
        String userName = context.getReqHeaders().get("user-name");

        int flag = 0;

        RepairDto repairDto = new RepairDto();
        repairDto.setRepairId(reqJson.getString("repairId"));
        repairDto.setCommunityId(reqJson.getString("communityId"));
        repairDto.setState(RepairDto.STATE_TAKING);
        List<RepairDto> repairDtos = repairInnerServiceSMOImpl.queryRepairs(repairDto);
        Assert.listOnlyOne(repairDtos, "当前没有需要处理订单");
        if (repairDtos != null && repairDtos.size() != 1) {
            ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_BUSINESS_VERIFICATION, "非常抱歉当前不能退单！");
            context.setResponseEntity(responseEntity);
            return;
        }
        //查询正在处理 工单的师傅
        RepairUserDto repairUserDto = new RepairUserDto();
        repairUserDto.setRepairId(reqJson.getString("repairId"));
        repairUserDto.setCommunityId(reqJson.getString("communityId"));
        repairUserDto.setState(RepairUserDto.STATE_DOING);
        List<RepairUserDto> repairUserDtos = repairUserInnerServiceSMOImpl.queryRepairUsers(repairUserDto);

        if (repairUserDtos != null && repairUserDtos.size() > 0) {
            // 1.0 关闭自己订单
            RepairUserPo repairUserPo = new RepairUserPo();
            repairUserPo.setRuId(repairUserDtos.get(0).getRuId());
            repairUserPo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            repairUserPo.setState(RepairUserDto.STATE_CLOSE);
            repairUserPo.setContext(userName + " 强制回单");
            repairUserPo.setCommunityId(reqJson.getString("communityId"));
            flag = repairUserV1InnerServiceSMOImpl.updateRepairUserNew(repairUserPo);
            if(flag < 1){
                throw new CmdException("修改工单失败");
            }

            repairUserPo = new RepairUserPo();
            repairUserPo.setRuId("-1");
            repairUserPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            repairUserPo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            repairUserPo.setState(RepairUserDto.STATE_CLOSE);
            repairUserPo.setRepairId(reqJson.getString("repairId"));
            repairUserPo.setPreStaffId(repairUserDtos.get(0).getStaffId());
            repairUserPo.setPreStaffName(repairUserDtos.get(0).getStaffName());
            repairUserPo.setStaffId(userId);
            repairUserPo.setStaffName(userName);
            repairUserPo.setPreRuId(repairUserDtos.get(0).getRuId());
            repairUserPo.setRepairEvent(RepairUserDto.REPAIR_EVENT_AUDIT_USER);
            repairUserPo.setContext(reqJson.getString("context"));
            repairUserPo.setCommunityId(reqJson.getString("communityId"));
            flag = repairUserV1InnerServiceSMOImpl.saveRepairUserNew(repairUserPo);
            if(flag < 1){
                throw new CmdException("修改工单失败");
            }
        }

        reqJson.put("state", RepairDto.STATE_RETURN_VISIT);
        modifyBusinessRepairDispatch(reqJson, RepairDto.STATE_RETURN_VISIT);

        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_OK, ResultVo.MSG_OK);
        context.setResponseEntity(responseEntity);
    }

    public void modifyBusinessRepairDispatch(JSONObject paramInJson, String state) {
        //查询报修单
        RepairDto repairDto = new RepairDto();
        repairDto.setRepairId(paramInJson.getString("repairId"));
        List<RepairDto> repairDtos = repairInnerServiceSMOImpl.queryRepairs(repairDto);
        Assert.isOne(repairDtos, "查询到多条数据，repairId=" + repairDto.getRepairId());
        JSONObject businessOwnerRepair = new JSONObject();
        businessOwnerRepair.putAll(BeanConvertUtil.beanCovertMap(repairDtos.get(0)));
        businessOwnerRepair.put("state", state);
        //计算 应收金额
        RepairPoolPo repairPoolPo = BeanConvertUtil.covertBean(businessOwnerRepair, RepairPoolPo.class);
        int flag = repairPoolV1InnerServiceSMOImpl.updateRepairPoolNew(repairPoolPo);
        if(flag < 1){
            throw new CmdException("修改工单失败");
        }
    }
}
