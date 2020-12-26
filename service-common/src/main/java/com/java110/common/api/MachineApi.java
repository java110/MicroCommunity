package com.java110.common.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.machine.IMachineOpenDoorBMO;
import com.java110.common.bmo.machineTranslateError.IGetMachineTranslateErrorBMO;
import com.java110.dto.machineTranslateError.MachineTranslateErrorDto;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/machine")
public class MachineApi {

    @Autowired
    private IMachineOpenDoorBMO machineOpenDoorBMOImpl;

    @Autowired
    private IGetMachineTranslateErrorBMO getMachineTranslateErrorBMOImpl;

    /**
     * 设备开门功能
     *
     * @param reqJson
     * @return
     * @serviceCode /machine/openDoor
     * @path /app/machine/openDoor
     */
    @RequestMapping(value = "/openDoor", method = RequestMethod.POST)
    public ResponseEntity<String> openDoor(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含小区信息");
        Assert.hasKeyAndValue(reqJson, "machineCode", "请求报文中未包含设备信息");
        return machineOpenDoorBMOImpl.openDoor(reqJson);
    }

    /**
     * 设备开门功能
     *
     * @param reqJson
     * @return
     * @serviceCode /machine/restartMachine
     * @path /app/machine/restartMachine
     */
    @RequestMapping(value = "/restartMachine", method = RequestMethod.POST)
    public ResponseEntity<String> restartMachine(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含小区信息");
        Assert.hasKeyAndValue(reqJson, "machineCode", "请求报文中未包含设备信息");
        return machineOpenDoorBMOImpl.restartMachine(reqJson);
    }


    /**
     * 重新送物联网系统
     *
     * @param reqJson
     * @return
     * @serviceCode /machine/resendIot
     * @path /app/machine/resendIot
     */
    @RequestMapping(value = "/resendIot", method = RequestMethod.POST)
    public ResponseEntity<String> resendIot(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "machineTranslateId", "未包含同步ID");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
        return machineOpenDoorBMOImpl.resendIot(reqJson);
    }

    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /machine/queryMachineTranslateError
     * @path /app/machine/queryMachineTranslateError
     */
    @RequestMapping(value = "/queryMachineTranslateError", method = RequestMethod.GET)
    public ResponseEntity<String> queryMachineTranslateError(@RequestParam(value = "communityId") String communityId,
                                                             @RequestParam(value = "page") int page,
                                                             @RequestParam(value = "row") int row) {
        MachineTranslateErrorDto machineTranslateErrorDto = new MachineTranslateErrorDto();
        machineTranslateErrorDto.setPage(page);
        machineTranslateErrorDto.setRow(row);
        machineTranslateErrorDto.setCommunityId(communityId);
        return getMachineTranslateErrorBMOImpl.get(machineTranslateErrorDto);
    }

}