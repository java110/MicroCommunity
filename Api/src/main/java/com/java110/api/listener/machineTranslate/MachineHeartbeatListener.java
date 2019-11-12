package com.java110.api.listener.machineTranslate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.utils.StringUtils;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.hardwareAdapation.IMachineInnerServiceSMO;
import com.java110.core.smo.hardwareAdapation.IMachineTranslateInnerServiceSMO;
import com.java110.dto.hardwareAdapation.MachineDto;
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
 * 调用地址
 * http://api.demo.winqi.cn/api/machineTranslate.machineHeartbeart?app_id=992019111002270001&communityId=7020181217000001&transaction_id=-1&req_time=20181113225612&user_id=-1
 * 硬件心跳检测类
 */
@Java110Listener("machineHeartbeatListener")
public class MachineHeartbeatListener extends AbstractServiceApiListener {

    @Autowired
    private IMachineTranslateInnerServiceSMO machineTranslateInnerServiceSMOImpl;

    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;

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
        //Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含小区信息");
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
        if (reqHeader == null || !reqHeader.containsKey("communityId") || StringUtils.isEmpty(reqHeader.get("communityId"))) {
            outParam.put("code", -1);
            outParam.put("message", "请求地址中未包含小区信息");
            responseEntity = new ResponseEntity<>(outParam.toJSONString(), headers, HttpStatus.OK);
            context.setResponseEntity(responseEntity);
            return;
        }
        for (String key : reqHeader.keySet()) {
            if(key.toLowerCase().equals("Content-Length")){
                continue;
            }
            headers.add(key, reqHeader.get(key));
        }

        String communityId = reqHeader.get("communityId");

        //检查设备是否合法
        MachineDto machineDto = new MachineDto();
        machineDto.setMachineCode(reqJson.getString("machineCode"));
        machineDto.setCommunityId(communityId);
        int machineCount = machineInnerServiceSMOImpl.queryMachinesCount(machineDto);
        if (machineCount < 1) {
            outParam.put("code", -1);
            outParam.put("message", "该设备【" + reqJson.getString("machineCode") + "】未在该小区【" + communityId + "】注册");
            responseEntity = new ResponseEntity<>(outParam.toJSONString(), headers, HttpStatus.OK);
            context.setResponseEntity(responseEntity);
            return;
        }


        //查询删除的业主信息
        MachineTranslateDto machineTranslateDto = new MachineTranslateDto();
        machineTranslateDto.setMachineCode(reqJson.getString("machineCode"));
        machineTranslateDto.setCommunityId(communityId);
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

        //查询待同步的业主数据
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

    public IMachineInnerServiceSMO getMachineInnerServiceSMOImpl() {
        return machineInnerServiceSMOImpl;
    }

    public void setMachineInnerServiceSMOImpl(IMachineInnerServiceSMO machineInnerServiceSMOImpl) {
        this.machineInnerServiceSMOImpl = machineInnerServiceSMOImpl;
    }
}
