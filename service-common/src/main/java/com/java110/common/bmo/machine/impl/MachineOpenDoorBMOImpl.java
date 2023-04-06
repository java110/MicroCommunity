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
package com.java110.common.bmo.machine.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.machine.IMachineOpenDoorBMO;
import com.java110.dto.machine.MachineTranslateDto;
import com.java110.dto.owner.OwnerAttrDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.common.IMachineTranslateInnerServiceSMO;
import com.java110.intf.job.IDataBusInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 设备开门功能
 *
 * @desc add by 吴学文 14:15
 */
@Service
public class MachineOpenDoorBMOImpl implements IMachineOpenDoorBMO {


    @Autowired
    IDataBusInnerServiceSMO dataBusInnerServiceSMOImpl;
    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IMachineTranslateInnerServiceSMO machineTranslateInnerServiceSMOImpl;

    /**
     * 开门功能
     *
     * @param reqJson 请求报文信息
     * @return
     */
    @Override
    public ResponseEntity<String> openDoor(JSONObject reqJson) {
        //如果是业主 限制开门次数
        if ("owner".equals(reqJson.getString("userRole"))) {
            OwnerDto ownerDto = new OwnerDto();
            ownerDto.setMemberId(reqJson.getString("userId"));
            ownerDto.setCommunityId(reqJson.getString("communityId"));
            List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnerMembers(ownerDto);
            if (ownerDtos == null || ownerDtos.size() < 1) {
                return ResultVo.error("没有权限开门");
            }

            if (!hasOpenDoorPri(ownerDtos.get(0), reqJson.getString("machineCode"))) {
                return ResultVo.error("今日开门次数已用完，请联系物业客服人员");
            }
        }
        ResultVo resultVo = dataBusInnerServiceSMOImpl.openDoor(reqJson);
        return ResultVo.createResponseEntity(resultVo);
    }

    /**
     * 开门功能
     *
     * @param reqJson 请求报文信息
     * @return
     */
    @Override
    public ResponseEntity<String> closeDoor(JSONObject reqJson) {
        //如果是业主 限制开门次数
        if ("owner".equals(reqJson.getString("userRole"))) {
            OwnerDto ownerDto = new OwnerDto();
            ownerDto.setMemberId(reqJson.getString("userId"));
            ownerDto.setCommunityId(reqJson.getString("communityId"));
            List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);
            if (ownerDtos == null || ownerDtos.size() < 1) {
                return ResultVo.error("没有权限开门");
            }

            if (!hasOpenDoorPri(ownerDtos.get(0), reqJson.getString("machineCode"))) {
                return ResultVo.error("今日开门次数已用完，请联系物业客服人员");
            }
        }
        ResultVo resultVo = dataBusInnerServiceSMOImpl.closeDoor(reqJson);
        return ResultVo.createResponseEntity(resultVo);
    }



    private boolean hasOpenDoorPri(OwnerDto ownerDto, String machineCode) {

        List<OwnerAttrDto> ownerAttrDtos = ownerDto.getOwnerAttrDtos();

        if (ownerAttrDtos == null || ownerAttrDtos.size() < 1) {
            return true;
        }
        long openDoorCount = -1;
        for (OwnerAttrDto ownerAttrDto : ownerAttrDtos) {
            if (OwnerAttrDto.SPEC_CD_MACHINE_OPEN_COUNT.equals(ownerAttrDto.getSpecCd())) {
                openDoorCount = Long.parseLong(ownerAttrDto.getValue());
            }
        }

        if (openDoorCount == -1) { //说明没有配置 不限制
            return true;
        }

        MachineTranslateDto machineTranslateDto = new MachineTranslateDto();
        machineTranslateDto.setCommunityId(ownerDto.getCommunityId());
        machineTranslateDto.setMachineCode(machineCode);
        machineTranslateDto.setObjBId(ownerDto.getMemberId());
        machineTranslateDto.setMachineCmd(MachineTranslateDto.CMD_OPEN_DOOR);
        machineTranslateDto.setIsNow("Y");
        long count = machineTranslateInnerServiceSMOImpl.queryMachineTranslatesCount(machineTranslateDto);

        if (openDoorCount > count) {
            return true;
        }

        return false;
    }

    /**
     * 重启设备
     *
     * @param reqJson 请求报文信息
     * @return
     */
    @Override
    public ResponseEntity<String> restartMachine(JSONObject reqJson) {
        ResultVo resultVo = dataBusInnerServiceSMOImpl.restartMachine(reqJson);
        return ResultVo.createResponseEntity(resultVo);
    }

    @Override
    public ResponseEntity<String> resendIot(JSONObject reqJson) {
        ResultVo resultVo = dataBusInnerServiceSMOImpl.resendIot(reqJson);
        return ResultVo.createResponseEntity(resultVo);
    }

    @Override
    public ResponseEntity<String> getQRcode(JSONObject reqJson) {
        //如果是业主 限制开门次数

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setMemberId(reqJson.getString("userId"));
        ownerDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);
        if (ownerDtos == null || ownerDtos.size() < 1) {
            return ResultVo.error("没有权限开门");
        }

        ResultVo resultVo = dataBusInnerServiceSMOImpl.getQRcode(reqJson);
        return ResultVo.createResponseEntity(resultVo.getCode(), resultVo.getMsg(), resultVo.getData());
    }

    @Override
    public ResponseEntity<String> customCarInOut(JSONObject reqJson) {
        ResultVo resultVo = dataBusInnerServiceSMOImpl.customCarInOut(reqJson);
        return ResultVo.createResponseEntity(resultVo);
    }

    @Override
    public ResponseEntity<String> getManualOpenDoorLogs(JSONObject reqJson) {
        ResultVo resultVo = dataBusInnerServiceSMOImpl.getManualOpenDoorLogs(reqJson);
        return ResultVo.createResponseEntity(resultVo);
    }
}
