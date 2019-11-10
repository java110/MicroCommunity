package com.java110.api.listener.machineTranslate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.hardwareAdapation.IMachineTranslateInnerServiceSMO;
import com.java110.dto.hardwareAdapation.MachineTranslateDto;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeMachineTranslateConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 硬件心跳检测类
 */
@Java110Listener("machineHeartbeatListener")
public class MachineHeartbeatListener extends AbstractServiceApiListener {

    @Autowired
    private IMachineTranslateInnerServiceSMO machineTranslateInnerServiceSMOImpl;

    /**
     * {
     * "machineCode":"test-MachineCode",
     * "devGroup":"default",
     * "name":"dev1",
     * "authCode":"ab2324f12ca2312b213133bfac",
     * "ip":"192.168.100.33",
     * "mac":"00:00:00:00","remarks":"test",
     * "faceNum":0,
     * "lastOnTime":15328329,
     * "statCode":1,
     * "deviceType":1,
     * "versionCode":114
     * }
     *
     * @param event   事件对象
     * @param reqJson 请求报文数据
     */
    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "machineCode", "请求报文中未包含设备编码");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含小区信息");
        Assert.hasKeyAndValue(reqJson, "authCode", "请求报文中未包含设备鉴权码");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        JSONObject outParam = null;
        ResponseEntity<String> responseEntity = null;

        outParam = new JSONObject();
        outParam.put("code", 0);
        outParam.put("message", "success");
        JSONArray data = null;
        Map<String, String> reqHeader = context.getRequestHeaders();
        HttpHeaders headers = new HttpHeaders();
        if (reqHeader != null && !reqHeader.isEmpty()) {
            for (String key : reqHeader.keySet()) {
                headers.add(key, reqHeader.get(key));
            }
        }

        //查询设备是否同步
        MachineTranslateDto machineTranslateDto = new MachineTranslateDto();
        machineTranslateDto.setMachineCode(reqJson.getString("machineCode"));
        machineTranslateDto.setStatusCd(StatusConstant.STATUS_CD_INVALID);
        List<MachineTranslateDto> machineTranslateDtos = machineTranslateInnerServiceSMOImpl.queryMachineTranslates(machineTranslateDto);
        //如果有失效数据，则告诉设备删除
        if (machineTranslateDtos != null && machineTranslateDtos.size() >= 0) {
            data = new JSONArray();
            for (MachineTranslateDto tmpM : machineTranslateDtos) {
                JSONObject tmpData = new JSONObject();
                tmpData.put("taskcmd", 102);
                tmpData.put("taskId", UUID.randomUUID().toString().replace("-", ""));
                tmpData.put("taskinfo", tmpM.getObjId());
                data.add(tmpData);
            }
        }

        machineTranslateDto.setStatusCd(StatusConstant.STATUS_CD_VALID);
        machineTranslateDto.setState("10000");
        //鉴权码先不做判断，后期判断
        machineTranslateDtos = machineTranslateInnerServiceSMOImpl.queryMachineTranslates(machineTranslateDto);
        if (machineTranslateDtos == null || machineTranslateDtos.size() == 0) {

            outParam.put("data", data);
            responseEntity = new ResponseEntity<>(outParam.toJSONString(), headers, HttpStatus.OK);
            context.setResponseEntity(responseEntity);
            return;
        }

        if (data == null) {
            data = new JSONArray();
        }
        JSONObject tmpData = new JSONObject();
        for (MachineTranslateDto tmpMachineTranslate : machineTranslateDtos) {
            tmpData.put("taskcmd", 101);
            tmpData.put("taskId", UUID.randomUUID().toString().replace("-", ""));
            tmpData.put("taskinfo", tmpMachineTranslate.getObjId());
            data.add(tmpData);
        }

        //将 设备 待同步 改为同步中
        MachineTranslateDto tmpMtDto = new MachineTranslateDto();
        tmpMtDto.setMachineCode(machineTranslateDto.getMachineCode());
        tmpMtDto.setCommunityId(machineTranslateDto.getCommunityId());
        tmpMtDto.setState("30000");
        machineTranslateInnerServiceSMOImpl.updateMachineTranslateState(tmpMtDto);
        outParam.put("data", data);

        responseEntity = new ResponseEntity<>(outParam.toJSONString(), headers, HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeMachineTranslateConstant.MACHINE_HEARTBEAT;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    public IMachineTranslateInnerServiceSMO getMachineTranslateInnerServiceSMOImpl() {
        return machineTranslateInnerServiceSMOImpl;
    }

    public void setMachineTranslateInnerServiceSMOImpl(IMachineTranslateInnerServiceSMO machineTranslateInnerServiceSMOImpl) {
        this.machineTranslateInnerServiceSMOImpl = machineTranslateInnerServiceSMOImpl;
    }
}
