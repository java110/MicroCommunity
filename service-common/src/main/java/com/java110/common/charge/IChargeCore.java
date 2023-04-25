package com.java110.common.charge;

import com.java110.dto.chargeMachine.ChargeMachineDto;
import com.java110.dto.chargeMachine.NotifyChargeOrderDto;
import com.java110.dto.chargeMachine.ChargeMachinePortDto;
import com.java110.vo.ResultVo;

import java.util.List;

/**
 * 充电适配器
 */
public interface IChargeCore {

    public static final String CHARGE_TYPE_HOURS = "hours";
    public static final String CHARGE_TYPE_ENERGY = "energy";

    /**
     * 开始充电接口
     * @param chargeMachineDto
     * @param chargeType
     * @param duration
     * @return
     */
    ResultVo startCharge(ChargeMachineDto chargeMachineDto, ChargeMachinePortDto chargeMachinePortDto, String chargeType, double duration,String orderId);


    /**
     * 结束充电接口
     * @param chargeMachineDto
     * @return
     */
    ResultVo stopCharge(ChargeMachineDto chargeMachineDto, ChargeMachinePortDto chargeMachinePortDto);

    /**
     * 查询充电端口状态
     * @param chargeMachineDto
     * @param chargeMachinePortDto
     * @return
     */
    ChargeMachinePortDto getChargePortState(ChargeMachineDto chargeMachineDto, ChargeMachinePortDto chargeMachinePortDto);

    ResultVo finishCharge(NotifyChargeOrderDto notifyChargeOrderDto);

    ResultVo workHeartbeat(NotifyChargeOrderDto notifyChargeOrderDto);


    /**
     * 查询设备状态
     * @param chargeMachineDtos
     */
    void queryChargeMachineState(List<ChargeMachineDto> chargeMachineDtos);

     boolean ifMonthCard(String personTel, String communityId);
}
