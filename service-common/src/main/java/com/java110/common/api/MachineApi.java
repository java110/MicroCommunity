package com.java110.common.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.machine.IMachineHeartbeatBMO;
import com.java110.common.bmo.machine.IMachineOpenDoorBMO;
import com.java110.common.bmo.machine.ISaveMachineRecordBMO;
import com.java110.common.bmo.machine.IUpdateMachineTransactionStateBMO;
import com.java110.common.bmo.machineTranslateError.IGetMachineTranslateErrorBMO;
import com.java110.dto.machine.MachineDto;
import com.java110.dto.machine.MachineRecordDto;
import com.java110.dto.machine.MachineTranslateDto;
import com.java110.dto.machine.MachineTranslateErrorDto;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 设备相关接口类
 * <p>
 * add by 吴学文 2020-12-28
 */
@RestController
@RequestMapping(value = "/machine")
public class MachineApi {

    private static Logger logger = LoggerFactory.getLogger(MachineApi.class);

    private static final String USER_ROLE_OWNER = "owner";

    @Autowired
    private IMachineOpenDoorBMO machineOpenDoorBMOImpl;

    @Autowired
    private IGetMachineTranslateErrorBMO getMachineTranslateErrorBMOImpl;

    @Autowired
    private ISaveMachineRecordBMO saveMachineRecordBMOImpl;

    @Autowired
    private IUpdateMachineTransactionStateBMO updateMachineTransactionStateBMOImpl;

    @Autowired
    private IMachineHeartbeatBMO machineHeartbeatBMOImpl;

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
        Assert.hasKeyAndValue(reqJson, "userId", "请求报文中未包含用户信息");
        return machineOpenDoorBMOImpl.openDoor(reqJson);
    }

    /**
     * 设备开门功能
     *
     * @param reqJson
     * @return
     * @serviceCode /machine/closeDoor
     * @path /app/machine/closeDoor
     */
    @RequestMapping(value = "/closeDoor", method = RequestMethod.POST)
    public ResponseEntity<String> closeDoor(@RequestBody JSONObject reqJson,
                                           @RequestHeader(value = "user-id", required = false) String userId) {
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含小区信息");
        Assert.hasKeyAndValue(reqJson, "machineCode", "请求报文中未包含设备信息");
        Assert.hasKeyAndValue(reqJson, "userRole", "请求报文中未包含用户角色");
        if (!USER_ROLE_OWNER.equals(reqJson.getString("userRole"))) { //这种为 员工的情况呢
            reqJson.put("userId", userId);
        }
        Assert.hasKeyAndValue(reqJson, "userId", "请求报文中未包含用户信息");
        return machineOpenDoorBMOImpl.closeDoor(reqJson);
    }



    /**
     * 设备二维码
     *
     * @param reqJson
     * @return
     * @serviceCode /machine/getQRcode
     * @path /app/machine/getQRcode
     */
    @RequestMapping(value = "/getQRcode", method = RequestMethod.POST)
    public ResponseEntity<String> getQRcode(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含小区信息");
        Assert.hasKeyAndValue(reqJson, "userId", "请求报文中未包含用户信息");
        Assert.hasKeyAndValue(reqJson, "machineCode", "请求报文中未包含设备信息");

        return machineOpenDoorBMOImpl.getQRcode(reqJson);
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
        //Assert.hasKeyAndValue(reqJson, "photo", "未包含抓拍照片");
        Assert.hasKeyAndValue(reqJson, "dateTime", "未包含开门时间");
        Assert.hasKeyAndValue(reqJson, "extCommunityId", "未包含小区信息");
        Assert.hasKeyAndValue(reqJson, "recordTypeCd", "未包含记录类型");
        MachineRecordDto machineRecordDto = BeanConvertUtil.covertBean(reqJson, MachineRecordDto.class);
        machineRecordDto.setCommunityId(reqJson.getString("extCommunityId"));
        machineRecordDto.setName(reqJson.getString("userName"));
        if (reqJson.containsKey("idNumber")) {
            machineRecordDto.setIdCard(reqJson.getString("idNumber"));
        } else {
            machineRecordDto.setIdCard("-1");
        }
        if (reqJson.containsKey("tel")) {
            machineRecordDto.setTel(reqJson.getString("tel"));
        } else {
            machineRecordDto.setTel("-1");
        }
        return saveMachineRecordBMOImpl.saveRecord(machineRecordDto);
    }

    /**
     * 物联网系统指令执行情况
     *
     * @param reqJson
     * @return
     * @serviceCode /machine/cmdResult
     * @path /app/machine/cmdResult
     */
    @RequestMapping(value = "/cmdResult", method = RequestMethod.POST)
    public ResponseEntity<String> cmdResult(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "taskId", "未包含任务信息");
        Assert.hasKeyAndValue(reqJson, "code", "未包含结果编码");
        Assert.hasKeyAndValue(reqJson, "msg", "未包含结果说明");
        MachineTranslateDto machineRecordDto = new MachineTranslateDto();
        machineRecordDto.setMachineTranslateId(reqJson.getString("taskId"));
        machineRecordDto.setState(reqJson.getIntValue("code") == 0
                ? MachineTranslateDto.STATE_SUCCESS : MachineTranslateDto.STATE_ERROR);
        machineRecordDto.setRemark(reqJson.getString("msg"));
        return updateMachineTransactionStateBMOImpl.update(machineRecordDto);
    }

    /**
     * 物联网系统设备心跳
     *
     * @param reqJson
     * @return
     * @serviceCode /machine/heartbeat
     * @path /app/machine/heartbeat
     */
    @RequestMapping(value = "/heartbeat", method = RequestMethod.POST)
    public ResponseEntity<String> heartbeat(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "taskId", "未包含任务信息");
        Assert.hasKeyAndValue(reqJson, "machineCode", "未包含设备编码");
        Assert.hasKeyAndValue(reqJson, "heartbeatTime", "未包含心跳时间");
        Assert.hasKeyAndValue(reqJson, "extCommunityId", "未包含小区ID");
        MachineDto machineDto = new MachineDto();
        machineDto.setMachineCode(reqJson.getString("machineCode"));
        machineDto.setCommunityId(reqJson.getString("extCommunityId"));
        //machineDto.setHeartbeatTime(reqJson.getString("heartbeatTime"));
        machineDto.setHeartbeatTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));//这里自己生成 不获取传递时间 因为可能时钟不一致 导致前台状态显示不正常

        return machineHeartbeatBMOImpl.heartbeat(machineDto);
    }

}