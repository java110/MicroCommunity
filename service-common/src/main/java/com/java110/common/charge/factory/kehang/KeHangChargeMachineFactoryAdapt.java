package com.java110.common.charge.factory.kehang;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.charge.IChargeFactoryAdapt;
import com.java110.dto.chargeMachine.ChargeMachineDto;
import com.java110.dto.chargeMachine.NotifyChargeOrderDto;
import com.java110.dto.chargeMachine.NotifyChargePortDto;
import com.java110.dto.chargeMachine.ChargeMachinePortDto;
import com.java110.vo.ResultVo;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 科航充电桩
 */
@Service("keHangChargeMachineFactory")
public class KeHangChargeMachineFactoryAdapt implements IChargeFactoryAdapt {
    @Override
    public ResultVo startCharge(ChargeMachineDto chargeMachineDto, ChargeMachinePortDto chargeMachinePortDto, String chargeType, double duration, String orderId) {
        JSONObject body = new JSONObject();
        body.put("version", "2.0");
        body.put("time", duration * 60);
        body.put("equipCd", chargeMachineDto.getMachineCode());
        body.put("port", chargeMachinePortDto.getPortCode());
        body.put("chargeId", orderId);
        String paramOut = null;
        try {
            paramOut = KeHangChargeUtils.execute("net.equip.charge.slow.run", body.toJSONString(), HttpMethod.GET);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        JSONObject paramObj = JSONObject.parseObject(paramOut);
        if (paramObj.getIntValue("code") != 1) {
            throw new IllegalArgumentException(paramObj.getString("msg"));
        }

        return new ResultVo(ResultVo.CODE_OK, paramObj.getString("msg"));
    }

    @Override
    public ResultVo stopCharge(ChargeMachineDto chargeMachineDto, ChargeMachinePortDto chargeMachinePortDto) {
        JSONObject body = new JSONObject();
        body.put("equipCd", chargeMachineDto.getMachineCode());
        body.put("port", chargeMachinePortDto.getPortCode());
        String paramOut = null;
        try {
            paramOut = KeHangChargeUtils.execute("net.equip.charge.slow.run.stop", body.toJSONString(), HttpMethod.GET);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        JSONObject paramObj = JSONObject.parseObject(paramOut);
        if (paramObj.getIntValue("code") != 1) {
            throw new IllegalArgumentException(paramObj.getString("msg"));
        }

        return new ResultVo(ResultVo.CODE_OK, paramObj.getString("msg"));
    }

    @Override
    public ChargeMachinePortDto getChargePortState(ChargeMachineDto chargeMachineDto, ChargeMachinePortDto chargeMachinePortDto) {
        JSONObject body = new JSONObject();
        body.put("equipCd", chargeMachineDto.getMachineCode());
        String paramOut = null;
        try {
            paramOut = KeHangChargeUtils.execute("net.equip.charge.slow.port.query", body.toJSONString(), HttpMethod.GET);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        JSONObject paramObj = JSONObject.parseObject(paramOut);
        if (paramObj.getIntValue("code") != 1) {
            throw new IllegalArgumentException(paramObj.getString("msg"));
        }

        JSONArray ports = paramObj.getJSONArray("ports");
        String state = ChargeMachinePortDto.STATE_BREAKDOWN;
        JSONObject port = null;
        for(int portIndex = 0;portIndex < ports.size() ;portIndex++){
            port = ports.getJSONObject(portIndex);
            if(chargeMachinePortDto.getPortCode().equals(port.getString("port"))){
                if("1".equals(port.getString("status"))){
                    state = ChargeMachinePortDto.STATE_FREE;
                } else if ("2".equals(port.getString("status"))) {
                    state = ChargeMachinePortDto.STATE_WORKING;
                }else{
                    state = ChargeMachinePortDto.STATE_BREAKDOWN;
                }
            }
        }

        chargeMachinePortDto.setState(state);
        return chargeMachinePortDto;
    }

    @Override
    public void queryChargeMachineState(ChargeMachineDto chargeMachineDto) {
        JSONObject body = new JSONObject();
        body.put("equipCd", chargeMachineDto.getMachineCode());
        String paramOut = null;
        try {
            paramOut = KeHangChargeUtils.execute("net.equip.online.query", body.toJSONString(), HttpMethod.GET);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        JSONObject paramObj = JSONObject.parseObject(paramOut);
        if (paramObj.getIntValue("code") != 1) {
            throw new IllegalArgumentException(paramObj.getString("msg"));
        }

        JSONObject data = paramObj.getJSONObject("data");

        if(data.getBoolean("online")){
            chargeMachineDto.setState(ChargeMachineDto.STATE_ONLINE);
            chargeMachineDto.setStateName("在线");
            return ;
        }

        chargeMachineDto.setState(ChargeMachineDto.STATE_OFFLINE);
        chargeMachineDto.setStateName("离线");
    }

    @Override
    public void workHeartbeat(ChargeMachineDto chargeMachineDto, String bodyParam) {

    }

    @Override
    public List<NotifyChargePortDto> getChargeHeartBeatParam(NotifyChargeOrderDto notifyChargeOrderDto) {
        return null;
    }
}
