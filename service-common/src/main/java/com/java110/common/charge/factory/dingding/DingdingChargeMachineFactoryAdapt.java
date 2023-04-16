package com.java110.common.charge.factory.dingding;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.charge.IChargeFactoryAdapt;
import com.java110.dto.chargeMachine.ChargeMachineDto;
import com.java110.dto.chargeMachine.NotifyChargeOrderDto;
import com.java110.dto.chargeMachine.NotifyChargePortDto;
import com.java110.dto.chargeMachine.ChargeMachinePortDto;
import com.java110.intf.common.IChargeMachineOrderV1InnerServiceSMO;
import com.java110.po.chargeMachineOrder.ChargeMachineOrderPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 叮叮充电桩 充电接口类
 * <p>
 * 叮叮 充电桩 通知处理类名为 NotifyDingDingChargeController.java
 */
@Service("dingdingChargeMachineFactory")
public class DingdingChargeMachineFactoryAdapt implements IChargeFactoryAdapt {

    private static final String QUERY_PORT_URL = DingdingChargeUtils.URL + "/equipments/ID/PORT";

    private static final String QUERY_CHARGE_STATE_URL = DingdingChargeUtils.URL + "/equipments/ID";


    //开始充电
    private static final String START_CHARGE_URL = DingdingChargeUtils.URL + "/equipments/ID/PORT/open";

    //关闭充电
    private static final String STOP_CHARGE_URL = DingdingChargeUtils.URL + "/equipments/ID/PORT/close";

    @Autowired
    private IChargeMachineOrderV1InnerServiceSMO chargeMachineOrderV1InnerServiceSMOImpl;

    @Override
    public ResultVo startCharge(ChargeMachineDto chargeMachineDto, ChargeMachinePortDto chargeMachinePortDto, String chargeType, double duration, String orderId) {
        String url = START_CHARGE_URL.replace("ID", chargeMachineDto.getMachineCode()).replace("PORT", chargeMachinePortDto.getPortCode());

        JSONObject body = new JSONObject();
        body.put("mode", 1);
        body.put("duration", duration * 60);
        body.put("energy", 0);
        body.put("chargeType", "order");
        body.put("chargeId", orderId);
        String paramOut = null;
        try {
            paramOut = DingdingChargeUtils.execute(url, body.toJSONString(), HttpMethod.POST);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        JSONObject paramObj = JSONObject.parseObject(paramOut);
        if (paramObj.getIntValue("code") != 200) {
            throw new IllegalArgumentException(paramObj.getString("msg"));
        }

        return new ResultVo(ResultVo.CODE_OK, paramObj.getString("msg"));
    }

    @Override
    public ResultVo stopCharge(ChargeMachineDto chargeMachineDto, ChargeMachinePortDto chargeMachinePortDto) {
        String url = STOP_CHARGE_URL.replace("ID", chargeMachineDto.getMachineCode()).replace("PORT", chargeMachinePortDto.getPortCode());

        JSONObject body = new JSONObject();
        String paramOut = null;
        try {
            paramOut = DingdingChargeUtils.execute(url, body.toJSONString(), HttpMethod.POST);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        JSONObject paramObj = JSONObject.parseObject(paramOut);
        if (paramObj.getIntValue("code") != 200) {
            throw new IllegalArgumentException(paramObj.getString("msg"));
        }

        return new ResultVo(ResultVo.CODE_OK, paramObj.getString("msg"));
    }

    @Override
    public ChargeMachinePortDto getChargePortState(ChargeMachineDto chargeMachineDto, ChargeMachinePortDto chargeMachinePortDto) {

        String url = QUERY_PORT_URL.replace("ID", chargeMachineDto.getMachineCode()).replace("PORT", chargeMachinePortDto.getPortCode());

        String paramOut = null;
        try {
            paramOut = DingdingChargeUtils.execute(url, "", HttpMethod.GET);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        JSONObject paramObj = JSONObject.parseObject(paramOut);
        if (paramObj.getIntValue("code") != 200) {
            throw new IllegalArgumentException(paramObj.getString("msg"));
        }

        chargeMachinePortDto.setState(paramObj.getJSONObject("data").getString("status"));
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
        String url = QUERY_CHARGE_STATE_URL.replace("ID", chargeMachineDto.getMachineCode());

        String paramOut = null;
        try {
            paramOut = DingdingChargeUtils.execute(url, "", HttpMethod.GET);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        JSONObject paramObj = JSONObject.parseObject(paramOut);
        if (paramObj.getIntValue("code") != 200) {
            throw new IllegalArgumentException(paramObj.getString("msg"));
        }

        Boolean online = paramObj.getJSONObject("data").getBoolean("online");
        if (online) {
            chargeMachineDto.setState(ChargeMachineDto.STATE_ONLINE);
            chargeMachineDto.setStateName("在线");
            return;
        }

        chargeMachineDto.setState(ChargeMachineDto.STATE_OFFLINE);
        chargeMachineDto.setStateName("离线");
    }

    @Override
    public void workHeartbeat(ChargeMachineDto chargeMachineDto, String bodyParam) {

        JSONArray params = JSONArray.parseArray(bodyParam);

        if (params == null || params.size() < 1) {
            return;
        }

        JSONObject param = null;
        for(int paramIndex = 0 ;paramIndex < params.size() ; paramIndex ++){
            doWorkHeartbeat(chargeMachineDto,params.getJSONObject(paramIndex));
        }

    }


    /**
     * 工作心跳
     * @param chargeMachineDto
     * @param param
     */
    private void doWorkHeartbeat(ChargeMachineDto chargeMachineDto, JSONObject param) {

        ChargeMachineOrderPo chargeMachineOrderPo = new ChargeMachineOrderPo();
        chargeMachineOrderPo.setOrderId(param.getString("chargeId"));

        chargeMachineOrderPo.setCommunityId(chargeMachineDto.getCommunityId());
        chargeMachineOrderPo.setEnergy(param.getString("energy"));

        chargeMachineOrderV1InnerServiceSMOImpl.updateChargeMachineOrder(chargeMachineOrderPo);
    }
}
