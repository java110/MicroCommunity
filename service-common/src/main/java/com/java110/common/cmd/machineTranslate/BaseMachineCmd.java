package com.java110.common.cmd.machineTranslate;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.utils.StringUtils;
import com.java110.core.context.DataFlowContext;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.machine.MachineDto;
import com.java110.intf.common.IMachineInnerServiceSMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public abstract class BaseMachineCmd extends Cmd {

    /**
     * 校验头部信息
     *
     * @param event
     * @param reqJson
     */
    protected void validateMachineHeader(CmdEvent event, JSONObject reqJson) {
        ICmdDataFlowContext context = event.getCmdDataFlowContext();
        Map<String, String> reqHeader = context.getReqHeaders();
        Assert.hasKeyAndValue(reqHeader, "machinecode", "请求报文中未包含设备编码");
//        Assert.hasKeyAndValue(reqHeader, "communityId", "请求报文中未包含小区信息");
    }

    /**
     * 校验报文内容
     *
     * @param event
     * @param context
     * @param reqJson
     */
    protected boolean validateMachineBody(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson,
                                          IMachineInnerServiceSMO machineInnerServiceSMOImpl) {

        ResponseEntity<String> responseEntity = null;
        ResultVo resultVo = null;
        Map<String, String> reqHeader = context.getReqHeaders();
        HttpHeaders headers = new HttpHeaders();
        String communityId = reqJson.containsKey("communityId") ? reqJson.getString("communityId") : reqHeader.get("communityId");
        if (StringUtil.isEmpty(communityId)) {
            resultVo = new ResultVo(ResultVo.CODE_MACHINE_ERROR, "请求头中未包含小区编码");
            responseEntity = new ResponseEntity<>(resultVo.toString(), headers, HttpStatus.OK);
            context.setResponseEntity(responseEntity);
            return false;
        }
        for (String key : reqHeader.keySet()) {
            if (key.toLowerCase().equals("content-length")) {
                continue;
            }
            headers.add(key, reqHeader.get(key));
        }

        if (!reqHeader.containsKey("machinecode") || StringUtils.isEmpty(reqHeader.get("machinecode"))) {
            resultVo = new ResultVo(ResultVo.CODE_MACHINE_ERROR, "请求头中未包含设备编码");
            responseEntity = new ResponseEntity<>(resultVo.toString(), headers, HttpStatus.OK);
            context.setResponseEntity(responseEntity);
            return false;
        }
        //检查设备是否合法

        if("-1".equals(reqHeader.get("machinecode"))){
            reqJson.put("machineCode", reqHeader.get("machinecode"));
            reqJson.put("machineId", reqHeader.get("machinecode"));
            reqJson.put("communityId", communityId);
            return true;
        }
        //检查设备是否合法
        MachineDto machineDto = new MachineDto();
        machineDto.setMachineCode(reqHeader.get("machinecode"));
        machineDto.setCommunityId(communityId);
        List<MachineDto> machineDtos = machineInnerServiceSMOImpl.queryMachines(machineDto);
        if (machineDtos == null || machineDtos.size() < 1) {
            resultVo = new ResultVo(ResultVo.CODE_MACHINE_ERROR, "该设备【" + reqHeader.get("machinecode") + "】未在该小区【" + communityId + "】注册");
            responseEntity = new ResponseEntity<>(resultVo.toString(), headers, HttpStatus.OK);
            context.setResponseEntity(responseEntity);
            return false;
        }

        if ("1600".equals(machineDtos.get(0).getState())) { //设备禁用状态
            resultVo = new ResultVo(ResultVo.CODE_MACHINE_ERROR, "该设备【" + reqHeader.get("machinecode") + "】禁用状态");
            responseEntity = new ResponseEntity<>(resultVo.toString(), headers, HttpStatus.OK);
            context.setResponseEntity(responseEntity);
            return false;
        }
        reqJson.put("machineCode", machineDtos.get(0).getMachineCode());
        reqJson.put("machineId", machineDtos.get(0).getMachineId());
        reqJson.put("communityId", communityId);
        return true;
    }

    protected HttpHeaders getHeader(ICmdDataFlowContext context) {
        Map<String, String> reqHeader = context.getReqHeaders();

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
