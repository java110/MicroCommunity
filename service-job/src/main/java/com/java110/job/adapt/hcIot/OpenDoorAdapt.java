/*
 * Copyright 2017-2020 吴学文 and java110 team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.java110.job.adapt.hcIot;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.client.RestTemplate;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.machine.MachineDto;
import com.java110.dto.machine.MachineTranslateDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.common.IMachineInnerServiceSMO;
import com.java110.intf.common.IMachineTranslateInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.List;

/**
 * 开门适配器
 * 接口协议地址： https://gitee.com/java110/MicroCommunityThings/blob/master/back/docs/api.md
 *
 * @desc add by 吴学文 15:29
 */
@Component(value = "openDoorAdapt")
public class OpenDoorAdapt extends DatabusAdaptImpl {

    @Autowired
    RestTemplate outRestTemplate;

    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IMachineTranslateInnerServiceSMO machineTranslateInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    /**
     * 开门实现方法
     *
     * @param paramIn 业务信息
     * @return
     */
    @Override
    public ResultVo openDoor(JSONObject paramIn) {

        MachineDto machineDto = new MachineDto();
        machineDto.setMachineCode(paramIn.getString("machineCode"));
        machineDto.setCommunityId(paramIn.getString("communityId"));
        List<MachineDto> machineDtos = machineInnerServiceSMOImpl.queryMachines(machineDto);

        Assert.listOnlyOne(machineDtos, "设备不存在");
        String userId = "";
        String userName = "";

        if (!"owner".equals(paramIn.getString("userRole"))) {
            UserDto userDto = new UserDto();
            userDto.setUserId(paramIn.getString("userId"));
            List<UserDto> userDtos = userInnerServiceSMOImpl.getUsers(userDto);
            Assert.listOnlyOne(userDtos, "用户不存在");
            userId = userDtos.get(0).getUserId();
            userName = userDtos.get(0).getUserName();
        } else {
            OwnerDto ownerDto = new OwnerDto();
            ownerDto.setMemberId(paramIn.getString("userId"));
            ownerDto.setCommunityId(paramIn.getString("communityId"));
            List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnerMembers(ownerDto);
            Assert.listOnlyOne(ownerDtos, "业主不存在");
            userId = ownerDtos.get(0).getMemberId();
            userName = ownerDtos.get(0).getName();
        }
        MachineTranslateDto machineTranslateDto = new MachineTranslateDto();
        machineTranslateDto.setMachineTranslateId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_machineTranslateId));
        machineTranslateDto.setCommunityId(paramIn.getString("communityId"));
        machineTranslateDto.setMachineCmd(MachineTranslateDto.CMD_OPEN_DOOR);
        machineTranslateDto.setMachineCode(machineDtos.get(0).getMachineCode());
        machineTranslateDto.setMachineId(machineDtos.get(0).getMachineId());
        machineTranslateDto.setObjId(userId);
        machineTranslateDto.setObjName(userName);
        machineTranslateDto.setTypeCd(MachineTranslateDto.TYPE_COMMUNITY);
        machineTranslateDto.setState(MachineTranslateDto.STATE_SUCCESS);
        machineTranslateDto.setRemark("同步物联网系统成功");

        try {
            JSONObject postParameters = new JSONObject();
            postParameters.put("taskId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_machineTranslateId));
            postParameters.put("machineCode", paramIn.getString("machineCode"));
            postParameters.put("extStaffId", userId);
            postParameters.put("staffName", userName);

            HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity(postParameters.toJSONString(), getHeaders(outRestTemplate));
            ResponseEntity<String> responseEntity = outRestTemplate.exchange(IotConstant.getUrl(IotConstant.OPEN_DOOR), HttpMethod.POST, httpEntity, String.class);
            if (responseEntity.getStatusCode() != HttpStatus.OK) {
                machineTranslateDto.setState(MachineTranslateDto.STATE_ERROR);
                machineTranslateDto.setRemark("开门失败");
                return new ResultVo(ResultVo.CODE_ERROR, responseEntity.getBody());
            }
            JSONObject paramOut = JSONObject.parseObject(responseEntity.getBody());
            if (!paramOut.containsKey("code") || ResultVo.CODE_OK != paramOut.getInteger("code")) {
                machineTranslateDto.setState(MachineTranslateDto.STATE_ERROR);
                machineTranslateDto.setRemark(paramOut.getString("msg"));
            } else {
                machineTranslateDto.setState(MachineTranslateDto.STATE_SUCCESS);
                machineTranslateDto.setRemark("同步物联网系统成功");
            }
            return new ResultVo(paramOut.getInteger("code"), paramOut.getString("msg"));
        } finally {
            machineTranslateDto.setbId("-1");
            machineTranslateDto.setObjBId("-1");
            machineTranslateDto.setUpdateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            machineTranslateInnerServiceSMOImpl.saveMachineTranslate(machineTranslateDto);
        }
    }

    /**
     * 开门实现方法
     *
     * @param paramIn 业务信息
     * @return
     */
    @Override
    public ResultVo closeDoor(JSONObject paramIn) {

        MachineDto machineDto = new MachineDto();
        machineDto.setMachineCode(paramIn.getString("machineCode"));
        machineDto.setCommunityId(paramIn.getString("communityId"));
        List<MachineDto> machineDtos = machineInnerServiceSMOImpl.queryMachines(machineDto);

        Assert.listOnlyOne(machineDtos, "设备不存在");
        String userId = "";
        String userName = "";

        if (!"owner".equals(paramIn.getString("userRole"))) {
            UserDto userDto = new UserDto();
            userDto.setUserId(paramIn.getString("userId"));
            List<UserDto> userDtos = userInnerServiceSMOImpl.getUsers(userDto);
            Assert.listOnlyOne(userDtos, "用户不存在");
            userId = userDtos.get(0).getUserId();
            userName = userDtos.get(0).getUserName();
        } else {
            OwnerDto ownerDto = new OwnerDto();
            ownerDto.setMemberId(paramIn.getString("userId"));
            ownerDto.setCommunityId(paramIn.getString("communityId"));
            List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnerMembers(ownerDto);
            Assert.listOnlyOne(ownerDtos, "业主不存在");
            userId = ownerDtos.get(0).getMemberId();
            userName = ownerDtos.get(0).getName();
        }
        MachineTranslateDto machineTranslateDto = new MachineTranslateDto();
        machineTranslateDto.setMachineTranslateId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_machineTranslateId));
        machineTranslateDto.setCommunityId(paramIn.getString("communityId"));
        machineTranslateDto.setMachineCmd(MachineTranslateDto.CMD_CLOSE_DOOR);
        machineTranslateDto.setMachineCode(machineDtos.get(0).getMachineCode());
        machineTranslateDto.setMachineId(machineDtos.get(0).getMachineId());
        machineTranslateDto.setObjId(userId);
        machineTranslateDto.setObjName(userName);
        machineTranslateDto.setTypeCd(MachineTranslateDto.TYPE_COMMUNITY);
        machineTranslateDto.setState(MachineTranslateDto.STATE_SUCCESS);
        machineTranslateDto.setRemark("同步物联网系统成功");

        try {
            JSONObject postParameters = new JSONObject();
            postParameters.put("taskId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_machineTranslateId));
            postParameters.put("machineCode", paramIn.getString("machineCode"));
            HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity(postParameters.toJSONString(), getHeaders(outRestTemplate));
            ResponseEntity<String> responseEntity = outRestTemplate.exchange(IotConstant.getUrl(IotConstant.CLOSE_DOOR), HttpMethod.POST, httpEntity, String.class);
            if (responseEntity.getStatusCode() != HttpStatus.OK) {
                machineTranslateDto.setState(MachineTranslateDto.STATE_ERROR);
                machineTranslateDto.setRemark("关门失败");
                return new ResultVo(ResultVo.CODE_ERROR, responseEntity.getBody());
            }
            JSONObject paramOut = JSONObject.parseObject(responseEntity.getBody());
            if (!paramOut.containsKey("code") || ResultVo.CODE_OK != paramOut.getInteger("code")) {
                machineTranslateDto.setState(MachineTranslateDto.STATE_ERROR);
                machineTranslateDto.setRemark(paramOut.getString("msg"));
            } else {
                machineTranslateDto.setState(MachineTranslateDto.STATE_SUCCESS);
                machineTranslateDto.setRemark("同步物联网系统成功");
            }
            return new ResultVo(paramOut.getInteger("code"), paramOut.getString("msg"));
        } finally {
            machineTranslateDto.setbId("-1");
            machineTranslateDto.setObjBId("-1");
            machineTranslateDto.setUpdateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            machineTranslateInnerServiceSMOImpl.saveMachineTranslate(machineTranslateDto);
        }
    }

    /**
     * 开门实现方法
     *
     * @param paramIn 业务信息
     * @return
     */
    @Override
    public ResultVo customCarInOut(JSONObject paramIn) {

        MachineDto machineDto = new MachineDto();
        machineDto.setMachineId(paramIn.getString("machineId"));
        machineDto.setCommunityId(paramIn.getString("communityId"));
        List<MachineDto> machineDtos = machineInnerServiceSMOImpl.queryMachines(machineDto);

        Assert.listOnlyOne(machineDtos, "设备不存在");

        JSONObject postParameters = new JSONObject();
        postParameters.put("taskId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_machineTranslateId));
        postParameters.put("extMachineId", paramIn.getString("machineId"));
        postParameters.put("carNum", paramIn.getString("carNum"));
        postParameters.put("type", paramIn.getString("type"));
        postParameters.put("amount", paramIn.getString("amount"));
        postParameters.put("payCharge", paramIn.getString("payCharge"));
        postParameters.put("payType", paramIn.getString("payType"));
        postParameters.put("unlicense", paramIn.getString("unlicense"));

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity(postParameters.toJSONString(), getHeaders(outRestTemplate));
        ResponseEntity<String> responseEntity = outRestTemplate.exchange(IotConstant.getUrl(IotConstant.CUSTOM_CAR_INOUT), HttpMethod.POST, httpEntity, String.class);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return new ResultVo(ResultVo.CODE_ERROR, responseEntity.getBody());
        }
        JSONObject paramOut = JSONObject.parseObject(responseEntity.getBody());

        return new ResultVo(paramOut.getInteger("code"), paramOut.getString("msg"));

    }

    @Override
    public ResultVo payVideo(MachineDto machineDto) {

        JSONObject postParameters = new JSONObject();
        postParameters.put("taskId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_machineTranslateId));
        postParameters.put("deviceId", machineDto.getMachineCode());
        postParameters.put("channelId", machineDto.getMachineVersion());
        postParameters.put("port", machineDto.getMachineIp());
        postParameters.put("mediaProtocol", "UDP");
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity(postParameters.toJSONString(), getHeaders(outRestTemplate));
        ResponseEntity<String> responseEntity = outRestTemplate.exchange(IotConstant.getUrl(IotConstant.PLAY_VIDEO), HttpMethod.POST, httpEntity, String.class);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return new ResultVo(ResultVo.CODE_ERROR, responseEntity.getBody());
        }
        JSONObject paramOut = JSONObject.parseObject(responseEntity.getBody());
        return new ResultVo(paramOut.getInteger("code"), paramOut.getString("msg"), paramOut.getJSONObject("data"));
    }

    @Override
    public ResultVo heartbeatVideo(JSONObject reqJson) {
        JSONObject postParameters = new JSONObject();
        postParameters.put("taskId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_machineTranslateId));
        postParameters.put("callIds", reqJson.getString("callIds"));
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity(postParameters.toJSONString(), getHeaders(outRestTemplate));
        ResponseEntity<String> responseEntity = outRestTemplate.exchange(IotConstant.getUrl(IotConstant.HEARTBEAT_VIDEO), HttpMethod.POST, httpEntity, String.class);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return new ResultVo(ResultVo.CODE_ERROR, responseEntity.getBody());
        }
        JSONObject paramOut = JSONObject.parseObject(responseEntity.getBody());
        return new ResultVo(paramOut.getInteger("code"), paramOut.getString("msg"));
    }

}
