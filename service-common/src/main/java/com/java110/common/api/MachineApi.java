package com.java110.common.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.machine.IMachineOpenDoorBMO;
import com.java110.common.bmo.machineRecord.ISaveMachineRecordBMO;
import com.java110.common.bmo.machineTranslateError.IGetMachineTranslateErrorBMO;
import com.java110.dto.machine.MachineRecordDto;
import com.java110.dto.machineTranslateError.MachineTranslateErrorDto;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/machine")
public class MachineApi {

    private static final String USER_ROLE_OWNER = "owner";
    @Autowired
    private IMachineOpenDoorBMO machineOpenDoorBMOImpl;

    @Autowired
    private IGetMachineTranslateErrorBMO getMachineTranslateErrorBMOImpl;

    @Autowired
    private ISaveMachineRecordBMO saveMachineRecordBMOImpl;

    /**
     * 设备开门功能
     *
     * @param reqJson
     * @return
     * @serviceCode /machine/openDoor
     * @path /app/machine/openDoor
     */
    @RequestMapping(value = "/openDoor", method = RequestMethod.POST)
    public ResponseEntity<String> openDoor(@RequestBody JSONObject reqJson,
                                           @RequestHeader(value = "user-id", required = false) String userId) {
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含小区信息");
        Assert.hasKeyAndValue(reqJson, "machineCode", "请求报文中未包含设备信息");
        Assert.hasKeyAndValue(reqJson, "userRole", "请求报文中未包含用户角色");
        if (!USER_ROLE_OWNER.equals(reqJson.getString("userRole"))) { //这种为 员工的情况呢
            reqJson.put("userId", userId);
        }
        Assert.hasKeyAndValue(reqJson, "userId", "请求报文中未包含设备信息");
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

    /**
     * 重新送物联网系统
     *
     * @param reqJson
     * @return
     * @serviceCode /machine/openDoorLog
     * @path /app/machine/openDoorLog
     */
    @RequestMapping(value = "/openDoorLog", method = RequestMethod.POST)
    public ResponseEntity<String> openDoorLog(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "userId", "未包含用户信息");
        Assert.hasKeyAndValue(reqJson, "userName", "未包含用户名称");
        Assert.hasKeyAndValue(reqJson, "machineCode", "未包含设备编码");
        Assert.hasKeyAndValue(reqJson, "openTypeCd", "未包含开门方式");
        Assert.hasKeyAndValue(reqJson, "similar", "未包含开门相似度");
        Assert.hasKeyAndValue(reqJson, "photo", "未包含抓拍照片");
        Assert.hasKeyAndValue(reqJson, "dateTime", "未包含开门时间");
        Assert.hasKeyAndValue(reqJson, "extCommunityId", "未包含小区信息");
        Assert.hasKeyAndValue(reqJson, "recordTypeCd", "未包含记录类型");
        MachineRecordDto machineRecordDto = BeanConvertUtil.covertBean(reqJson, MachineRecordDto.class);
        machineRecordDto.setCommunityId(reqJson.getString("extCommunityId"));
        machineRecordDto.setName(reqJson.getString("userName"));
        return saveMachineRecordBMOImpl.saveRecord(machineRecordDto);
    }

}