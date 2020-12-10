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
package com.java110.job.adapt.ximoIot;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.dto.RoomDto;
import com.java110.dto.communityLocation.CommunityLocationDto;
import com.java110.dto.file.FileDto;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.machine.MachineDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.entity.order.Business;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.common.IMachineInnerServiceSMO;
import com.java110.intf.community.ICommunityLocationInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.job.adapt.ximoIot.asyn.IXimoMachineAsyn;
import com.java110.po.machine.MachinePo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

/**
 * HC iot 设备同步适配器
 *
 * @desc add by 吴学文 18:58
 */
@Component(value = "ximoAddMachineTransactionIotAdapt")
public class XimoAddMachineTransactionIotAdapt extends DatabusAdaptImpl {

    @Autowired
    private IXimoMachineAsyn ximoMachineAsynImpl;
    @Autowired
    IMachineInnerServiceSMO machineInnerServiceSMOImpl;

    @Autowired
    private ICommunityLocationInnerServiceSMO communityLocationInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    /**
     * accessToken={access_token}
     * &extCommunityUuid=01000
     * &extCommunityId=1
     * &devSn=111111111
     * &name=设备名称
     * &positionType=0
     * &positionUuid=1
     *
     * @param business   当前处理业务
     * @param businesses 所有业务信息
     */
    @Override
    public void execute(Business business, List<Business> businesses) {
        JSONObject data = business.getData();
        if (data.containsKey(MachinePo.class.getSimpleName())) {
            Object bObj = data.get(MachinePo.class.getSimpleName());
            JSONArray businessMachines = null;
            if (bObj instanceof JSONObject) {
                businessMachines = new JSONArray();
                businessMachines.add(bObj);
            } else if (bObj instanceof List) {
                businessMachines = JSONArray.parseArray(JSONObject.toJSONString(bObj));
            } else {
                businessMachines = (JSONArray) bObj;
            }
            //JSONObject businessMachine = data.getJSONObject("businessMachine");
            for (int bMachineIndex = 0; bMachineIndex < businessMachines.size(); bMachineIndex++) {
                JSONObject businessMachine = businessMachines.getJSONObject(bMachineIndex);
                doSendMachine(business, businessMachine);
            }
        }
    }

    private void doSendMachine(Business business, JSONObject businessMachine) {

        MachinePo machinePo = BeanConvertUtil.covertBean(businessMachine, MachinePo.class);

        MachineDto machineDto = new MachineDto();
        machineDto.setMachineCode(machinePo.getMachineCode());
        machineDto.setCommunityId(machinePo.getCommunityId());
        List<MachineDto> machineDtos = machineInnerServiceSMOImpl.queryMachines(machineDto);

        Assert.listOnlyOne(machineDtos, "未找到设备");

        if (!"9999".equals(machineDtos.get(0).getMachineTypeCd())) {
            return;
        }

        List<MultiValueMap<String, Object>> ownerDtos = getOwners(machinePo);

        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();

        postParameters.add("extCommunityUuid", machinePo.getCommunityId());
        postParameters.add("devSn", machinePo.getMachineCode());
        postParameters.add("uuid", machineDtos.get(0).getMachineId());
        postParameters.add("name", machinePo.getMachineName());
        postParameters.add("positionType", "0");
        postParameters.add("positionUuid", machinePo.getCommunityId());
        ximoMachineAsynImpl.send(postParameters, ownerDtos);
    }

    private List<MultiValueMap<String, Object>> getOwners(MachinePo machinePo) {
        //拿到小区ID
        String communityId = machinePo.getCommunityId();

        List<MultiValueMap<String, Object>> ownerDtos = new ArrayList<>();

        List<OwnerDto> owners = null;
        //根据小区ID查询现有设备
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(communityId);
        String locationTypeCd = machinePo.getLocationTypeCd();
        CommunityLocationDto communityLocationDto = new CommunityLocationDto();
        communityLocationDto.setLocationId(locationTypeCd);
        communityLocationDto.setCommunityId(machinePo.getCommunityId());
        List<CommunityLocationDto> communityLocationDtos = communityLocationInnerServiceSMOImpl.queryCommunityLocations(communityLocationDto);

        if (communityLocationDtos == null || communityLocationDtos.size() < 1) {
            return ownerDtos;
        }
        communityLocationDto = communityLocationDtos.get(0);

        if ("1000".contains(communityLocationDto.getLocationType())) {//查询整个小区的业主
            owners = ownerInnerServiceSMOImpl.queryOwnerMembers(ownerDto);
        } else if ("2000".equals(communityLocationDto.getLocationType())) {//2000 单元门 ，则这个单元下的业主同步
            //先根据单元门ID 查询 房屋
            RoomDto roomDto = new RoomDto();
            roomDto.setUnitId(machinePo.getLocationObjId());
            roomDto.setCommunityId(machinePo.getCommunityId());
            List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
            if (roomDtos == null || roomDtos.size() == 0) { // 单元下没有房屋
                return ownerDtos;
            }
            ownerDto.setRoomIds(getRoomIds(roomDtos));
            owners = ownerInnerServiceSMOImpl.queryOwnerMembers(ownerDto);
        } else if ("3000".equals(communityLocationDto.getLocationType())) {// 3000 房屋门
            ownerDto.setRoomId(machinePo.getLocationObjId());
            owners = ownerInnerServiceSMOImpl.queryOwnerMembers(ownerDto);
        }

        for (OwnerDto tOwnerDto : owners) {
            FileRelDto fileRelDto = new FileRelDto();
            fileRelDto.setObjId(tOwnerDto.getMemberId());
            fileRelDto.setRelTypeCd("10000");
            List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
            if (fileRelDtos == null || fileRelDtos.size() != 1) {
                continue;
            }
            FileDto fileDto = new FileDto();
            fileDto.setFileId(fileRelDtos.get(0).getFileSaveName());
            fileDto.setFileSaveName(fileRelDtos.get(0).getFileSaveName());
            fileDto.setCommunityId(tOwnerDto.getCommunityId());
            List<FileDto> fileDtos = fileInnerServiceSMOImpl.queryFiles(fileDto);
            if (fileDtos == null || fileDtos.size() != 1) {
                continue;
            }
            MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();

            postParameters.add("extCommunityUuid", tOwnerDto.getCommunityId());
            postParameters.add("addAuthorizationDevSn", machinePo.getMachineCode());
            postParameters.add("uuid", tOwnerDto.getMemberId());
            postParameters.add("name", tOwnerDto.getName());
            postParameters.add("faceFileBase64Array", fileDtos.get(0).getContext());

            ownerDtos.add(postParameters);
        }

        return ownerDtos;
    }

    private String[] getRoomIds(List<RoomDto> roomDtos) {
        List<String> roomIds = new ArrayList<String>();
        for (RoomDto roomDto : roomDtos) {
            roomIds.add(roomDto.getRoomId());
        }
        return roomIds.toArray(new String[roomIds.size()]);
    }
}
