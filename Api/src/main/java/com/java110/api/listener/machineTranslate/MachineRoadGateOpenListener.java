package com.java110.api.listener.machineTranslate;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.hardwareAdapation.ICarBlackWhiteInnerServiceSMO;
import com.java110.core.smo.hardwareAdapation.IMachineInnerServiceSMO;
import com.java110.dto.hardwareAdapation.CarBlackWhiteDto;
import com.java110.dto.hardwareAdapation.MachineDto;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeMachineTranslateConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import com.java110.vo.api.machine.MachineResDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

/**
 * @ClassName MachineRoadGateOpenListener
 * @Description 道闸开门侦听类
 * @Author wuxw
 * @Date 2020/1/25 21:50
 * @Version 1.0
 * add by wuxw 2020/1/25
 **/
@Java110Listener("machineRoadGateOpenListener")
public class MachineRoadGateOpenListener extends BaseMachineListener {

    private static final String MACHINE_DIRECTION_IN = "3306"; // 进入

    private static final String MACHINE_DIRECTION_OUT = "3307"; //出去

    private static final String CAR_BLACK = "1111"; // 车辆黑名单
    private static final String CAR_WHITE = "2222"; // 车辆白名单

    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;

    @Autowired
    private ICarBlackWhiteInnerServiceSMO carBlackWhiteInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        super.validateMachineHeader(event, reqJson);

        Assert.hasKeyAndValue(reqJson, "carNum", "请求报文中未包含车牌号");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        JSONObject outParam = null;
        ResponseEntity<String> responseEntity = null;
        Map<String, String> reqHeader = context.getRequestHeaders();
        String communityId = reqHeader.get("communityId");
        String machineCode = reqHeader.get("machinecode");
        HttpHeaders headers = new HttpHeaders();
        for (String key : reqHeader.keySet()) {
            if (key.toLowerCase().equals("content-length")) {
                continue;
            }
            headers.add(key, reqHeader.get(key));
        }
        //根据设备编码查询 设备信息
        MachineDto machineDto = new MachineDto();
        machineDto.setMachineCode(machineCode);
        machineDto.setCommunityId(communityId);
        List<MachineDto> machineDtos = machineInnerServiceSMOImpl.queryMachines(machineDto);
        if (machineDtos == null || machineDtos.size() < 1) {
            outParam.put("code", -1);
            outParam.put("message", "该设备【" + machineCode + "】未在该小区【" + communityId + "】注册");
            responseEntity = new ResponseEntity<>(outParam.toJSONString(), headers, HttpStatus.OK);
            context.setResponseEntity(responseEntity);
            return;
        }
        //设备方向
        String direction = machineDtos.get(0).getDirection();

        //进入
        if (MACHINE_DIRECTION_IN.equals(direction)) {
            dealCarIn(context, reqJson, machineDtos.get(0), communityId, machineCode);
        }

    }

    /**
     * 处理车辆进入
     *
     * @param reqJson
     * @param machineDto
     * @param communityId
     * @param machineCode
     */
    private void dealCarIn(DataFlowContext context, JSONObject reqJson, MachineDto machineDto, String communityId, String machineCode) {
        //车辆是否黑名单 车辆
        String carNum = reqJson.getString("carNum");
        CarBlackWhiteDto carBlackWhiteDto = new CarBlackWhiteDto();
        carBlackWhiteDto.setCommunityId(communityId);
        carBlackWhiteDto.setCarNum(carNum);
        carBlackWhiteDto.setBlackWhite(CAR_BLACK);
        int count = carBlackWhiteInnerServiceSMOImpl.queryCarBlackWhitesCount(carBlackWhiteDto);
        if (count > 0) {
            context.setResponseEntity(MachineResDataVo.getResData(MachineResDataVo.CODE_ERROR, carNum + "被加入黑名单，无法放行"));
            return;
        }



    }

    @Override
    public String getServiceCode() {
        return ServiceCodeMachineTranslateConstant.MACHINE_ROAD_GATE_OPEN;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }
}
