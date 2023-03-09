package com.java110.common.cmd.chargeMachine;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.charge.IChargeCore;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.account.AccountDto;
import com.java110.dto.accountDetail.AccountDetailDto;
import com.java110.dto.chargeMachine.ChargeMachineDto;
import com.java110.dto.chargeMachineOrder.ChargeMachineOrderDto;
import com.java110.dto.chargeMachinePort.ChargeMachinePortDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.acct.IAccountInnerServiceSMO;
import com.java110.intf.common.IChargeMachineOrderV1InnerServiceSMO;
import com.java110.intf.common.IChargeMachinePortV1InnerServiceSMO;
import com.java110.intf.common.IChargeMachineV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.chargeMachineOrder.ChargeMachineOrderPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

/**
 * 充电桩开始充电 处理类
 * <p>
 * add by wuxw 2023-03-09
 */
@Java110Cmd(serviceCode = "chargeMachine.startCharge")
public class StartChargeCmd extends Cmd {

    @Autowired
    private IChargeMachineV1InnerServiceSMO chargeMachineV1InnerServiceSMOImpl;

    @Autowired
    private IChargeMachinePortV1InnerServiceSMO chargeMachinePortV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IAccountInnerServiceSMO accountInnerServiceSMOImpl;

    @Autowired
    private IChargeCore chargeCoreImpl;

    @Autowired
    private IChargeMachineOrderV1InnerServiceSMO chargeMachineOrderV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
        Assert.hasKeyAndValue(reqJson, "machineId", "未包含充电桩");
        Assert.hasKeyAndValue(reqJson, "portId", "未包含插槽");
        Assert.hasKeyAndValue(reqJson, "duration", "未包含充电时长"); // 999 为按量充电

        String userId = context.getReqHeaders().get("user-id");
        Assert.hasLength(userId, "用户不存在");

        //查询充电桩设备信息

        ChargeMachineDto chargeMachineDto = new ChargeMachineDto();
        chargeMachineDto.setCommunityId(reqJson.getString("communityId"));
        chargeMachineDto.setMachineId(reqJson.getString("machineId"));
        List<ChargeMachineDto> chargeMachineDtos = chargeMachineV1InnerServiceSMOImpl.queryChargeMachines(chargeMachineDto);

        Assert.listOnlyOne(chargeMachineDtos, "充电桩 不存在");

        // todo 插槽是否空闲

        ChargeMachinePortDto chargeMachinePortDto = new ChargeMachinePortDto();
        chargeMachinePortDto.setMachineId(reqJson.getString("machineId"));
        chargeMachinePortDto.setPortId(reqJson.getString("portId"));
        chargeMachinePortDto.setState(ChargeMachinePortDto.STATE_FREE);
        List<ChargeMachinePortDto> chargeMachinePortDtos = chargeMachinePortV1InnerServiceSMOImpl.queryChargeMachinePorts(chargeMachinePortDto);
        Assert.listOnlyOne(chargeMachinePortDtos, "插槽忙线");


        double duration = reqJson.getDoubleValue("duration");
        if (duration == 999) {
            duration = 10;
        }

        BigDecimal payMoneyDec = new BigDecimal(Double.parseDouble(chargeMachineDtos.get(0).getDurationPrice()));

        double payMoney = payMoneyDec.multiply(new BigDecimal(duration)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();


        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户不存在");

        AccountDto accountDto = new AccountDto();
        accountDto.setLink(userDtos.get(0).getTel());
        accountDto.setAcctType(AccountDto.ACCT_TYPE_CASH);
        List<AccountDto> accountDtos = accountInnerServiceSMOImpl.queryAccounts(accountDto);

        if (accountDtos == null || accountDtos.size() < 1) {
            throw new CmdException("请先充值，账户金额不足");
        }

        if (Double.parseDouble(accountDtos.get(0).getAmount()) < payMoney) {
            throw new CmdException("账户金额不足，无法支付" + duration + "小时费用,请先充值");
        }

        reqJson.put("acctId", accountDtos.get(0).getAcctId());


    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        String userId = context.getReqHeaders().get("user-id");
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户不存在");
        ChargeMachineDto chargeMachineDto = new ChargeMachineDto();
        chargeMachineDto.setCommunityId(reqJson.getString("communityId"));
        chargeMachineDto.setMachineId(reqJson.getString("machineId"));
        List<ChargeMachineDto> chargeMachineDtos = chargeMachineV1InnerServiceSMOImpl.queryChargeMachines(chargeMachineDto);
        double duration = reqJson.getDoubleValue("duration");
        String durationType = IChargeCore.CHARGE_TYPE_HOURS;
        double durationHours = duration;
        if (duration == 999) {
            durationType = IChargeCore.CHARGE_TYPE_ENERGY;
            durationHours = 10;
        }

        ChargeMachinePortDto chargeMachinePortDto = new ChargeMachinePortDto();
        chargeMachinePortDto.setMachineId(reqJson.getString("machineId"));
        chargeMachinePortDto.setPortId(reqJson.getString("portId"));
        chargeMachinePortDto.setState(ChargeMachinePortDto.STATE_FREE);
        List<ChargeMachinePortDto> chargeMachinePortDtos = chargeMachinePortV1InnerServiceSMOImpl.queryChargeMachinePorts(chargeMachinePortDto);

        String orderId = GenerateCodeFactory.getGeneratorId("11");
        //调用充电桩充电
        ResultVo resultVo = chargeCoreImpl.startCharge(chargeMachineDtos.get(0), chargeMachinePortDtos.get(0), durationType, durationHours, orderId);

        if (resultVo.getCode() != ResultVo.CODE_OK) {
            context.setResponseEntity(ResultVo.createResponseEntity(resultVo));
            return;
        }

        // todo 生成 充电订单
        ChargeMachineOrderPo chargeMachineOrderPo = new ChargeMachineOrderPo();
        chargeMachineOrderPo.setAmount("0");
        chargeMachineOrderPo.setOrderId(orderId);
        chargeMachineOrderPo.setPortId(chargeMachinePortDtos.get(0).getPortId());
        chargeMachineOrderPo.setPersonName(userDtos.get(0).getName());
        chargeMachineOrderPo.setMachineId(chargeMachineDtos.get(0).getMachineId());
        chargeMachineOrderPo.setAcctDetailId(reqJson.getString("acctId"));
        chargeMachineOrderPo.setPersonId(userId);
        chargeMachineOrderPo.setChargeHours(reqJson.getString("duration"));
        chargeMachineOrderPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        Calendar calendar = Calendar.getInstance();
        if (duration == 999) {
            calendar.add(Calendar.HOUR, 10);
        } else {
            calendar.add(Calendar.HOUR, reqJson.getIntValue("duration"));
        }

        chargeMachineOrderPo.setEndTime(DateUtil.getFormatTimeString(calendar.getTime(), DateUtil.DATE_FORMATE_STRING_A));
        chargeMachineOrderPo.setState(ChargeMachineOrderDto.STATE_DOING);
        chargeMachineOrderPo.setPersonTel(userDtos.get(0).getTel());
        chargeMachineOrderPo.setCommunityId(chargeMachineDtos.get(0).getCommunityId());
        chargeMachineOrderPo.setEnergy("1");
        int flag = chargeMachineOrderV1InnerServiceSMOImpl.saveChargeMachineOrder(chargeMachineOrderPo);

        if (flag < 1) {
            chargeCoreImpl.stopCharge(chargeMachineDtos.get(0), chargeMachinePortDtos.get(0));
            throw new CmdException("充电失败");
        }
        context.setResponseEntity(ResultVo.createResponseEntity(resultVo));
    }
}
