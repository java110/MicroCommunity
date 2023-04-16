package com.java110.common.charge.factory.common;


import com.alibaba.fastjson.JSONArray;
import com.java110.common.charge.IChargeFactoryAdapt;
import com.java110.dto.chargeMachine.ChargeMachineDto;
import com.java110.dto.chargeMachine.NotifyChargeOrderDto;
import com.java110.dto.chargeMachine.NotifyChargePortDto;
import com.java110.dto.chargeMachine.ChargeMachinePortDto;
import com.java110.vo.ResultVo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 通用 充电接口类
 *
 * 叮叮 充电桩 通知处理类名为 NotifyCommonChargeController.java
 */
@Service("commonChargeMachineFactory")
public class CommonChargeMachineFactoryAdapt implements IChargeFactoryAdapt {

    private static final String QUERY_PORT_URL =  "/equipments/ID/PORT";

    //开始充电
    private static final String START_CHARGE_URL =  "/equipments/ID/PORT/open";

    //关闭充电
    private static final String STOP_CHARGE_URL = "/equipments/ID/PORT/close";

    @Override
    public ResultVo startCharge(ChargeMachineDto chargeMachineDto, ChargeMachinePortDto chargeMachinePortDto, String chargeType, double duration, String orderId) {


        return new ResultVo(ResultVo.CODE_OK, "开始充电成功");
    }

    @Override
    public ResultVo stopCharge(ChargeMachineDto chargeMachineDto, ChargeMachinePortDto chargeMachinePortDto) {
        return new ResultVo(ResultVo.CODE_OK, "结束充电成功");

    }

    @Override
    public ChargeMachinePortDto getChargePortState(ChargeMachineDto chargeMachineDto, ChargeMachinePortDto chargeMachinePortDto) {

        chargeMachinePortDto.setState(ChargeMachinePortDto.STATE_FREE);
        return chargeMachinePortDto;
    }

    @Override
    public List<NotifyChargePortDto> getChargeHeartBeatParam(NotifyChargeOrderDto notifyChargeOrderDto) {

        JSONArray jsonArray = JSONArray.parseArray(notifyChargeOrderDto.getBodyParam());
        List<NotifyChargePortDto> ports = new ArrayList<>();

        if (jsonArray == null || jsonArray.size() < 1) {
            return ports;
        }
        NotifyChargePortDto port = null;
        for (int portIndex = 0; portIndex < jsonArray.size(); portIndex++) {
            port = new NotifyChargePortDto();
            port.setPortCode(jsonArray.getJSONObject(portIndex).getString("port"));
            port.setMachineCode(notifyChargeOrderDto.getMachineCode());
            port.setOrderId(jsonArray.getJSONObject(portIndex).getString("chargeId"));
            port.setEnergy(jsonArray.getJSONObject(portIndex).getString("energy"));
            long time = jsonArray.getJSONObject(portIndex).getLongValue("powerTime");
            port.setPowerTime(new Date(time));
            ports.add(port);
        }

        return ports;
    }

    @Override
    public void queryChargeMachineState(ChargeMachineDto chargeMachineDto) {
        chargeMachineDto.setState(ChargeMachineDto.STATE_ONLINE);
        chargeMachineDto.setStateName("在线");
    }

    @Override
    public void workHeartbeat(ChargeMachineDto chargeMachineDto, String bodyParam) {

    }
}
