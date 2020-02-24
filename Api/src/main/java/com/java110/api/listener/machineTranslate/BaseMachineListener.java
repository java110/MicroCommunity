package com.java110.api.listener.machineTranslate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.utils.StringUtils;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.hardwareAdapation.IMachineInnerServiceSMO;
import com.java110.core.smo.hardwareAdapation.IMachineTranslateInnerServiceSMO;
import com.java110.dto.hardwareAdapation.MachineDto;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.util.Assert;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

/**
 * 设备侦听 父类
 */
public abstract class BaseMachineListener extends AbstractServiceApiListener {

    /**
     * 校验头部信息
     *
     * @param event
     * @param reqJson
     */
    protected void validateMachineHeader(ServiceDataFlowEvent event, JSONObject reqJson) {
        DataFlowContext context = event.getDataFlowContext();
        Map<String, String> reqHeader = context.getRequestHeaders();
        Assert.hasKeyAndValue(reqHeader, "machinecode", "请求报文中未包含设备编码");
        Assert.hasKeyAndValue(reqHeader, "communityId", "请求报文中未包含小区信息");
    }

    /**
     * 校验报文内容
     *
     * @param event
     * @param context
     * @param reqJson
     */
    protected boolean validateMachineBody(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson,
                                          IMachineInnerServiceSMO machineInnerServiceSMOImpl) {
        JSONObject outParam = null;
        ResponseEntity<String> responseEntity = null;

        outParam = new JSONObject();
        outParam.put("code", 0);
        outParam.put("message", "success");
        JSONArray data = null;
        Map<String, String> reqHeader = context.getRequestHeaders();
        HttpHeaders headers = new HttpHeaders();
        if (reqHeader == null || !reqHeader.containsKey("communityId") || StringUtils.isEmpty(reqHeader.get("communityId"))) {
            outParam.put("code", -1);
            outParam.put("message", "请求地址中未包含小区信息");
            responseEntity = new ResponseEntity<>(outParam.toJSONString(), headers, HttpStatus.OK);
            context.setResponseEntity(responseEntity);
            return false;
        }
        for (String key : reqHeader.keySet()) {
            if (key.toLowerCase().equals("content-length")) {
                continue;
            }
            headers.add(key, reqHeader.get(key));
        }

        String communityId = reqHeader.get("communityId");

        if (!reqHeader.containsKey("machinecode") || StringUtils.isEmpty(reqHeader.get("machinecode"))) {
            outParam.put("code", -1);
            outParam.put("message", "请求头中未包含设备编码");
            responseEntity = new ResponseEntity<>(outParam.toJSONString(), headers, HttpStatus.OK);
            context.setResponseEntity(responseEntity);
            return false;
        }

        //检查设备是否合法
        //检查设备是否合法
        MachineDto machineDto = new MachineDto();
        machineDto.setMachineCode(reqHeader.get("machinecode"));
        machineDto.setCommunityId(communityId);
        List<MachineDto> machineDtos = machineInnerServiceSMOImpl.queryMachines(machineDto);
        if (machineDtos == null || machineDtos.size() < 1) {
            outParam.put("code", -1);
            outParam.put("message", "该设备【" + reqHeader.get("machinecode") + "】未在该小区【" + communityId + "】注册");
            responseEntity = new ResponseEntity<>(outParam.toJSONString(), headers, HttpStatus.OK);
            context.setResponseEntity(responseEntity);
            return false;
        }

        if("1600".equals(machineDtos.get(0).getState())){ //设备禁用状态
            outParam.put("code", -1);
            outParam.put("message", "该设备【" + reqHeader.get("machinecode") + "】禁用状态");
            responseEntity = new ResponseEntity<>(outParam.toJSONString(), headers, HttpStatus.OK);
            context.setResponseEntity(responseEntity);
            return false;
        }


        reqJson.put("machineCode", machineDtos.get(0).getMachineCode());
        reqJson.put("machineId", machineDtos.get(0).getMachineId());
        reqJson.put("communityId", communityId);
        return true;
    }

    protected HttpHeaders getHeader(DataFlowContext context) {
        Map<String, String> reqHeader = context.getRequestHeaders();

        HttpHeaders headers = new HttpHeaders();

        for (String key : reqHeader.keySet()) {
            if (key.toLowerCase().equals("content-length")) {
                continue;
            }
            headers.add(key, reqHeader.get(key));
        }
        return headers;
    }
}
