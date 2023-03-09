package com.java110.common.charge;

import com.java110.common.smartMeter.ISmartMeterFactoryAdapt;
import com.java110.dto.chargeMachine.ChargeMachineDto;
import com.java110.dto.chargeMachineFactory.ChargeMachineFactoryDto;
import com.java110.dto.chargeMachineOrder.ChargeMachineOrderDto;
import com.java110.dto.chargeMachineOrder.NotifyChargeOrderDto;
import com.java110.dto.chargeMachineOrder.NotifyChargePortDto;
import com.java110.dto.chargeMachinePort.ChargeMachinePortDto;
import com.java110.intf.common.IChargeMachineFactoryV1InnerServiceSMO;
import com.java110.intf.common.IChargeMachineOrderV1InnerServiceSMO;
import com.java110.intf.common.IChargeMachinePortV1InnerServiceSMO;
import com.java110.intf.common.IChargeMachineV1InnerServiceSMO;
import com.java110.po.chargeMachineOrder.ChargeMachineOrderPo;
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

import java.util.List;

/**
 * 充电核心类
 */
@Service
public class ChargeCoreImpl implements IChargeCore {

    @Autowired
    private IChargeMachineFactoryV1InnerServiceSMO chargeMachineFactoryV1InnerServiceSMOImpl;

    @Autowired
    private IChargeMachineOrderV1InnerServiceSMO chargeMachineOrderV1InnerServiceSMOImpl;

    @Autowired
    private IChargeMachineV1InnerServiceSMO chargeMachineV1InnerServiceSMOImpl;

    @Autowired
    private IChargeMachinePortV1InnerServiceSMO chargeMachinePortV1InnerServiceSMOImpl;

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
            doDealChargePort(notifyChargePortDto, notifyChargeOrderDto, chargeMachineDtos.get(0));
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
    private void doDealChargePort(NotifyChargePortDto notifyChargePortDto, NotifyChargeOrderDto notifyChargeOrderDto, ChargeMachineDto chargeMachineDto) {

        // todo 1.0 查询上报时间是否已经 扣款，如果扣款过，那么更新 充电电量 后返回

        // todo 2.0 检查账户是否余额充足，如果余额不足，则 调用停止充电 将充电订单 修改成充电完成，并且修改备注

        // todo 3.0 账户扣款


    }
}
