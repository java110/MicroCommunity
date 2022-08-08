package com.java110.community.cmd.ownerRepair;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.intf.community.IRepairInnerServiceSMO;
import com.java110.intf.community.IRepairPoolV1InnerServiceSMO;
import com.java110.intf.community.IRepairUserInnerServiceSMO;
import com.java110.intf.community.IRepairUserV1InnerServiceSMO;
import com.java110.po.owner.RepairPoolPo;
import com.java110.po.owner.RepairUserPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Java110Cmd(serviceCode = "ownerRepair.appraiseRepair")
public class AppraiseRepairCmd extends Cmd {

    @Autowired
    private IRepairUserInnerServiceSMO repairUserInnerServiceSMOImpl;

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMOImpl;

    @Autowired
    private IRepairUserV1InnerServiceSMO repairUserV1InnerServiceSMOImpl;

    @Autowired
    private IRepairPoolV1InnerServiceSMO repairPoolV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "repairId", "未包含报修单信息");
        Assert.hasKeyAndValue(reqJson, "context", "未包含派单内容");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        //获取报修渠道
        String repairChannel = reqJson.getString("repairChannel");
        //是否是公共区域
        String publicArea = reqJson.getString("publicArea");
        //查询维修类型
        String maintenanceType = reqJson.getString("maintenanceType");
        RepairUserDto repairUserDto = new RepairUserDto();
        repairUserDto.setRepairId(reqJson.getString("repairId"));
        repairUserDto.setCommunityId(reqJson.getString("communityId"));
        if (repairChannel.equals("Z")) { //如果是业主端报修，就查询是否有已评价状态的
            repairUserDto.setState(RepairUserDto.STATE_FINISH);
        } else if (!StringUtil.isEmpty(maintenanceType) && maintenanceType.equals("1001")) { //如果不是业主端报修，且是有偿的，就查询已支付状态的
            repairUserDto.setState(RepairUserDto.STATE_FINISH_PAY_FEE);
        } else { //其他的查询结单状态的
            repairUserDto.setState(RepairUserDto.STATE_CLOSE);
        }
        List<RepairUserDto> repairUserDtos = repairUserInnerServiceSMOImpl.queryRepairUsers(repairUserDto);
        Assert.listOnlyOne(repairUserDtos, "当前用户没有需要处理的订单");
        // 1.0 关闭自己订单
        RepairUserPo repairUserPo = new RepairUserPo();
        repairUserPo.setRuId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ruId));
        repairUserPo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        repairUserPo.setState(RepairUserDto.STATE_FINISH_VISIT);
        repairUserPo.setContext(reqJson.getString("context"));
        repairUserPo.setCommunityId(reqJson.getString("communityId"));
        repairUserPo.setCreateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        repairUserPo.setRepairId(reqJson.getString("repairId"));
        repairUserPo.setStaffId(reqJson.getString("userId"));
        repairUserPo.setStaffName(reqJson.getString("userName"));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date endTime = repairUserDtos.get(0).getEndTime();
        repairUserPo.setStartTime(format.format(endTime));
        repairUserPo.setPreStaffId(repairUserDtos.get(0).getStaffId());
        repairUserPo.setPreStaffName(repairUserDtos.get(0).getStaffName());
        repairUserPo.setPreRuId(repairUserDtos.get(0).getRuId());
        repairUserPo.setRepairEvent("auditUser");
        int flag = repairUserV1InnerServiceSMOImpl.saveRepairUserNew(repairUserPo);
        if (flag < 1) {
            throw new CmdException("保存失败");
        }
        modifyBusinessRepairDispatch(reqJson, RepairDto.STATE_COMPLATE);
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
        if (flag < 1) {
            throw new CmdException("保存失败");
        }
    }
}
