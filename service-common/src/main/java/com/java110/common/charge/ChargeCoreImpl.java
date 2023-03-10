package com.java110.common.charge;

import com.java110.common.smartMeter.ISmartMeterFactoryAdapt;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.account.AccountDto;
import com.java110.dto.chargeMachine.ChargeMachineDto;
import com.java110.dto.chargeMachineFactory.ChargeMachineFactoryDto;
import com.java110.dto.chargeMachineOrder.ChargeMachineOrderDto;
import com.java110.dto.chargeMachineOrder.NotifyChargeOrderDto;
import com.java110.dto.chargeMachineOrder.NotifyChargePortDto;
import com.java110.dto.chargeMachineOrderAcct.ChargeMachineOrderAcctDto;
import com.java110.dto.chargeMachinePort.ChargeMachinePortDto;
import com.java110.intf.acct.IAccountInnerServiceSMO;
import com.java110.intf.common.*;
import com.java110.po.accountDetail.AccountDetailPo;
import com.java110.po.chargeMachineOrder.ChargeMachineOrderPo;
import com.java110.po.chargeMachineOrderAcct.ChargeMachineOrderAcctPo;
import com.java110.po.chargeMachinePort.ChargeMachinePortPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 充电核心类
 */
@Service
public class ChargeCoreImpl implements IChargeCore {

    @Autowired
    private IAccountInnerServiceSMO accountInnerServiceSMOImpl;

    @Autowired
    private IChargeMachineFactoryV1InnerServiceSMO chargeMachineFactoryV1InnerServiceSMOImpl;

    @Autowired
    private IChargeMachineOrderV1InnerServiceSMO chargeMachineOrderV1InnerServiceSMOImpl;

    @Autowired
    private IChargeMachineV1InnerServiceSMO chargeMachineV1InnerServiceSMOImpl;

    @Autowired
    private IChargeMachinePortV1InnerServiceSMO chargeMachinePortV1InnerServiceSMOImpl;


    @Autowired
    private IChargeMachineOrderAcctV1InnerServiceSMO chargeMachineOrderAcctV1InnerServiceSMOImpl;

    @Override
    public ResultVo startCharge(ChargeMachineDto chargeMachineDto, ChargeMachinePortDto chargeMachinePortDto, String chargeType, double duration, String orderId) {

        ChargeMachineFactoryDto chargeMachineFactoryDto = new ChargeMachineFactoryDto();
        chargeMachineFactoryDto.setFactoryId(chargeMachineDto.getImplBean());
        List<ChargeMachineFactoryDto> chargeMachineFactoryDtos = chargeMachineFactoryV1InnerServiceSMOImpl.queryChargeMachineFactorys(chargeMachineFactoryDto);

        Assert.listOnlyOne(chargeMachineFactoryDtos, "充电桩厂家不存在");

        IChargeFactoryAdapt chargeFactoryAdapt = ApplicationContextFactory.getBean(chargeMachineFactoryDtos.get(0).getBeanImpl(), IChargeFactoryAdapt.class);
        if (chargeFactoryAdapt == null) {
            throw new CmdException("厂家接口未实现");
        }

        chargeMachinePortDto = chargeFactoryAdapt.getChargePortState(chargeMachineDto, chargeMachinePortDto);

        if (!ChargeMachinePortDto.STATE_FREE.equals(chargeMachinePortDto.getState())) {
            throw new IllegalArgumentException("充电插槽不是空闲状态");
        }

        return chargeFactoryAdapt.startCharge(chargeMachineDto, chargeMachinePortDto, chargeType, duration, orderId);
    }

    @Override
    public ResultVo stopCharge(ChargeMachineDto chargeMachineDto, ChargeMachinePortDto chargeMachinePortDto) {
        ChargeMachineFactoryDto chargeMachineFactoryDto = new ChargeMachineFactoryDto();
        chargeMachineFactoryDto.setFactoryId(chargeMachineDto.getImplBean());
        List<ChargeMachineFactoryDto> chargeMachineFactoryDtos = chargeMachineFactoryV1InnerServiceSMOImpl.queryChargeMachineFactorys(chargeMachineFactoryDto);

        Assert.listOnlyOne(chargeMachineFactoryDtos, "充电桩厂家不存在");

        IChargeFactoryAdapt chargeFactoryAdapt = ApplicationContextFactory.getBean(chargeMachineFactoryDtos.get(0).getBeanImpl(), IChargeFactoryAdapt.class);
        if (chargeFactoryAdapt == null) {
            throw new CmdException("厂家接口未实现");
        }

        return chargeFactoryAdapt.stopCharge(chargeMachineDto, chargeMachinePortDto);
    }

    @Override
    public ChargeMachinePortDto getChargePortState(ChargeMachineDto chargeMachineDto, ChargeMachinePortDto chargeMachinePortDto) {
        ChargeMachineFactoryDto chargeMachineFactoryDto = new ChargeMachineFactoryDto();
        chargeMachineFactoryDto.setFactoryId(chargeMachineDto.getImplBean());
        List<ChargeMachineFactoryDto> chargeMachineFactoryDtos = chargeMachineFactoryV1InnerServiceSMOImpl.queryChargeMachineFactorys(chargeMachineFactoryDto);

        Assert.listOnlyOne(chargeMachineFactoryDtos, "充电桩厂家不存在");

        IChargeFactoryAdapt chargeFactoryAdapt = ApplicationContextFactory.getBean(chargeMachineFactoryDtos.get(0).getBeanImpl(), IChargeFactoryAdapt.class);
        if (chargeFactoryAdapt == null) {
            throw new CmdException("厂家接口未实现");
        }

        return chargeFactoryAdapt.getChargePortState(chargeMachineDto, chargeMachinePortDto);
    }

    @Override
    public ResponseEntity<String> finishCharge(NotifyChargeOrderDto notifyChargeOrderDto) {

        // todo 生成 充电订单
        ChargeMachineOrderPo chargeMachineOrderPo = new ChargeMachineOrderPo();
        chargeMachineOrderPo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        chargeMachineOrderPo.setState(ChargeMachineOrderDto.STATE_FINISH);
        chargeMachineOrderPo.setOrderId(notifyChargeOrderDto.getOrderId());
        chargeMachineOrderV1InnerServiceSMOImpl.updateChargeMachineOrder(chargeMachineOrderPo);

        ChargeMachineDto chargeMachineDto = new ChargeMachineDto();
        chargeMachineDto.setMachineCode(notifyChargeOrderDto.getMachineCode());
        List<ChargeMachineDto> chargeMachineDtos = chargeMachineV1InnerServiceSMOImpl.queryChargeMachines(chargeMachineDto);

        Assert.listOnlyOne(chargeMachineDtos, "充电桩 不存在");

        // todo 插槽是否空闲

        ChargeMachinePortDto chargeMachinePortDto = new ChargeMachinePortDto();
        chargeMachinePortDto.setMachineId(chargeMachineDtos.get(0).getMachineId());
        chargeMachinePortDto.setPortCode(notifyChargeOrderDto.getPortCode());
        chargeMachinePortDto.setState(ChargeMachinePortDto.STATE_FREE);
        List<ChargeMachinePortDto> chargeMachinePortDtos = chargeMachinePortV1InnerServiceSMOImpl.queryChargeMachinePorts(chargeMachinePortDto);
        Assert.listOnlyOne(chargeMachinePortDtos, "插槽忙线");

        ChargeMachinePortPo chargeMachinePortPo = new ChargeMachinePortPo();
        chargeMachinePortPo.setPortId(chargeMachinePortDtos.get(0).getPortId());
        chargeMachinePortPo.setState(ChargeMachinePortDto.STATE_FREE);
        chargeMachinePortV1InnerServiceSMOImpl.updateChargeMachinePort(chargeMachinePortPo);

        return new ResponseEntity<>("{\n" +
                "\"code\" : 200,\n" +
                "\"msg\" : \"success\"\n" +
                "}", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> heartbeat(NotifyChargeOrderDto notifyChargeOrderDto) {
        return new ResponseEntity<>("{\n" +
                "\"code\" : 200,\n" +
                "\"msg\" : \"success\"\n" +
                "}", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> chargeHeartBeat(NotifyChargeOrderDto notifyChargeOrderDto) {
        ChargeMachineDto chargeMachineDto = new ChargeMachineDto();
        chargeMachineDto.setMachineCode(notifyChargeOrderDto.getMachineCode());
        List<ChargeMachineDto> chargeMachineDtos = chargeMachineV1InnerServiceSMOImpl.queryChargeMachines(chargeMachineDto);

        Assert.listOnlyOne(chargeMachineDtos, "充电桩 不存在");

        ChargeMachineFactoryDto chargeMachineFactoryDto = new ChargeMachineFactoryDto();
        chargeMachineFactoryDto.setFactoryId(chargeMachineDtos.get(0).getImplBean());
        List<ChargeMachineFactoryDto> chargeMachineFactoryDtos = chargeMachineFactoryV1InnerServiceSMOImpl.queryChargeMachineFactorys(chargeMachineFactoryDto);

        Assert.listOnlyOne(chargeMachineFactoryDtos, "充电桩厂家不存在");

        IChargeFactoryAdapt chargeFactoryAdapt = ApplicationContextFactory.getBean(chargeMachineFactoryDtos.get(0).getBeanImpl(), IChargeFactoryAdapt.class);
        if (chargeFactoryAdapt == null) {
            throw new CmdException("厂家接口未实现");
        }

        List<NotifyChargePortDto> portDtos = chargeFactoryAdapt.getChargeHeartBeatParam(notifyChargeOrderDto);

        for (NotifyChargePortDto notifyChargePortDto : portDtos) {
            doDealChargePort(notifyChargePortDto, notifyChargeOrderDto, chargeMachineDtos.get(0), chargeFactoryAdapt);
        }

        return new ResponseEntity<>("{\n" +
                "\"code\" : 200,\n" +
                "\"msg\" : \"success\"\n" +
                "}", HttpStatus.OK);
    }

    /**
     * 处理充电 扣款问题
     *
     * @param notifyChargePortDto
     * @param notifyChargeOrderDto
     */
    private void doDealChargePort(NotifyChargePortDto notifyChargePortDto,
                                  NotifyChargeOrderDto notifyChargeOrderDto,
                                  ChargeMachineDto chargeMachineDto,
                                  IChargeFactoryAdapt chargeFactoryAdapt) {

        String preEnergy = "0";
        ChargeMachineOrderDto chargeMachineOrderDto = new ChargeMachineOrderDto();
        chargeMachineOrderDto.setOrderId(notifyChargePortDto.getOrderId());
        List<ChargeMachineOrderDto> orderDtos = chargeMachineOrderV1InnerServiceSMOImpl.queryChargeMachineOrders(chargeMachineOrderDto);

        Assert.listOnlyOne(orderDtos, "订单不存在");
        String state = ChargeMachineOrderDto.STATE_DOING;
        if (preEnergy.equals(notifyChargePortDto.getEnergy())) {
            state = ChargeMachineOrderDto.STATE_FINISH;
        }
        updateOrderState(notifyChargePortDto, state);

        //todo 主动调用关闭
        if (preEnergy.equals(notifyChargePortDto.getEnergy())) {
            customStopCharge(notifyChargePortDto, chargeMachineDto, chargeFactoryAdapt, orderDtos);
        }

        // todo 1.0 查询上报时间是否已经 扣款，如果扣款过，那么更新 充电电量 后返回

        String powerTime = DateUtil.getFormatTimeString(notifyChargePortDto.getPowerTime(), DateUtil.DATE_FORMATE_STRING_A);

        ChargeMachineOrderAcctDto chargeMachineOrderAcctDto = new ChargeMachineOrderAcctDto();
        chargeMachineOrderAcctDto.setOrderId(notifyChargeOrderDto.getOrderId());
        chargeMachineOrderAcctDto.setPowerTime(powerTime);
        List<ChargeMachineOrderAcctDto> chargeMachineOrderAcctDtos = chargeMachineOrderAcctV1InnerServiceSMOImpl.queryChargeMachineOrderAccts(chargeMachineOrderAcctDto);

        if (chargeMachineOrderAcctDtos != null && chargeMachineOrderAcctDtos.size() > 0) {
            return;
        }

        // todo 2.0 检查账户是否余额充足，如果余额不足，则 调用停止充电 将充电订单 修改成充电完成，并且修改备注

        double price = Double.parseDouble(chargeMachineDto.getDurationPrice());

        AccountDto accountDto = new AccountDto();
        accountDto.setAcctId(orderDtos.get(0).getAcctDetailId());
        accountDto.setAcctType(AccountDto.ACCT_TYPE_CASH);
        List<AccountDto> accountDtos = accountInnerServiceSMOImpl.queryAccounts(accountDto);

        if (accountDtos == null || accountDtos.size() < 1) {
            throw new CmdException("请先充值，账户金额不足");
        }

        //todo 账户金额不足，无法支付小时费用,停止充电
        if (Double.parseDouble(accountDtos.get(0).getAmount()) < price) {
            customStopCharge(notifyChargePortDto, chargeMachineDto, chargeFactoryAdapt, orderDtos);
            updateOrderState(notifyChargePortDto, ChargeMachineOrderDto.STATE_FINISH);
            return;
        }


        // todo 3.0 账户扣款
        AccountDetailPo accountDetailPo = new AccountDetailPo();
        accountDetailPo.setAcctId(accountDtos.get(0).getAcctId());
        accountDetailPo.setObjId(accountDtos.get(0).getObjId());
        accountDetailPo.setObjType(accountDtos.get(0).getObjType());
        accountDetailPo.setAmount(price + "");
        accountDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        accountInnerServiceSMOImpl.withholdAccount(accountDetailPo);

        ChargeMachineOrderAcctPo chargeMachineOrderAcctPo = new ChargeMachineOrderAcctPo();
        chargeMachineOrderAcctPo.setAcctDetailId(accountDetailPo.getDetailId());
        chargeMachineOrderAcctPo.setAmount(price + "");
        chargeMachineOrderAcctPo.setCmoaId(GenerateCodeFactory.getGeneratorId("11"));
        chargeMachineOrderAcctPo.setOrderId(notifyChargePortDto.getOrderId());
        chargeMachineOrderAcctPo.setAcctId(accountDtos.get(0).getAcctId());
        chargeMachineOrderAcctPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        chargeMachineOrderAcctPo.setEndTime(DateUtil.getAddHoursStringA(DateUtil.getCurrentDate(), 1));
        chargeMachineOrderAcctPo.setRemark("一小时定时扣款");
        chargeMachineOrderAcctPo.setCommunityId(orderDtos.get(0).getCommunityId());
        chargeMachineOrderAcctPo.setEnergy(notifyChargePortDto.getEnergy());

        chargeMachineOrderAcctV1InnerServiceSMOImpl.saveChargeMachineOrderAcct(chargeMachineOrderAcctPo);

    }

    private void updateOrderState(NotifyChargePortDto notifyChargePortDto, String state) {
        ChargeMachineOrderPo chargeMachineOrderPo = new ChargeMachineOrderPo();
        chargeMachineOrderPo.setOrderId(notifyChargePortDto.getOrderId());
        chargeMachineOrderPo.setEnergy(notifyChargePortDto.getEnergy());
        chargeMachineOrderPo.setState(state);

        chargeMachineOrderV1InnerServiceSMOImpl.updateChargeMachineOrder(chargeMachineOrderPo);
    }

    private void customStopCharge(NotifyChargePortDto notifyChargePortDto, ChargeMachineDto chargeMachineDto, IChargeFactoryAdapt chargeFactoryAdapt, List<ChargeMachineOrderDto> orderDtos) {
        ChargeMachinePortDto chargeMachinePortDto = new ChargeMachinePortDto();
        chargeMachinePortDto.setMachineId(orderDtos.get(0).getMachineId());
        chargeMachinePortDto.setPortCode(notifyChargePortDto.getPortCode());
        List<ChargeMachinePortDto> chargeMachinePortDtos = chargeMachinePortV1InnerServiceSMOImpl.queryChargeMachinePorts(chargeMachinePortDto);
        if (chargeMachinePortDtos != null && chargeMachinePortDtos.size() > 0) {
            chargeFactoryAdapt.stopCharge(chargeMachineDto, chargeMachinePortDtos.get(0));
        }
    }
}
