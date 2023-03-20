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
package com.java110.common.cmd.machine;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.RoomDto;
import com.java110.dto.attendanceClasses.AttendanceClassesDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.community.CommunityLocationDto;
import com.java110.dto.machine.MachineDto;
import com.java110.dto.unit.FloorAndUnitDto;
import com.java110.intf.common.IAttendanceClassesV1InnerServiceSMO;
import com.java110.intf.common.IMachineInnerServiceSMO;
import com.java110.intf.community.*;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.api.machine.ApiMachineDataVo;
import com.java110.vo.api.machine.ApiMachineVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * 类表述：查询
 * 服务编码：machine.listMachine
 * 请求路劲：/app/machine.ListMachine
 * add by 吴学文 at 2021-11-09 15:42:41 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "machine.listMachines")
public class ListMachinesCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(ListMachinesCmd.class);
    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IFloorInnerServiceSMO floorInnerServiceSMOImpl;

    @Autowired
    private IUnitInnerServiceSMO unitInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private ICommunityLocationInnerServiceSMO communityLocationInnerServiceSMOImpl;

    @Autowired
    private IAttendanceClassesV1InnerServiceSMO attendanceClassesV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validatePageInfo(reqJson);

        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含小区信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        MachineDto machineDto = BeanConvertUtil.covertBean(reqJson, MachineDto.class);

        if (reqJson.containsKey("machineTypeCds") && !StringUtil.isEmpty(reqJson.getString("machineTypeCds"))) {
            machineDto.setMachineTypeCds(reqJson.getString("machineTypeCds").split(","));
        }

        int count = machineInnerServiceSMOImpl.queryMachinesCount(machineDto);

        List<ApiMachineDataVo> machines = null;

        if (count > 0) {
            List<MachineDto> machineDtos = machineInnerServiceSMOImpl.queryMachines(machineDto);
            // 刷新 位置信息
            //refreshMachines(machineDtos);
            refreshMachineLocation(machineDtos);
            machines = BeanConvertUtil.covertBeanList(machineDtos, ApiMachineDataVo.class);
        } else {
            machines = new ArrayList<>();
        }

        ApiMachineVo apiMachineVo = new ApiMachineVo();

        apiMachineVo.setTotal(count);
        apiMachineVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiMachineVo.setMachines(machines);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiMachineVo), HttpStatus.OK);

        cmdDataFlowContext.setResponseEntity(responseEntity);

    }

    private void refreshMachineLocation(List<MachineDto> machines) {
        for (MachineDto machineDto : machines) {
            freshMachineStateName(machineDto);
            getMachineLocation(machineDto);
        }
    }

    private void freshMachineStateName(MachineDto machineDto) {
        String heartbeatTime = machineDto.getHeartbeatTime();
        try {
            if (StringUtil.isEmpty(heartbeatTime)) {
                machineDto.setStateName("设备离线");
            } else {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(DateUtil.getDateFromString(heartbeatTime, DateUtil.DATE_FORMATE_STRING_A));
                calendar.add(Calendar.MINUTE, 2);
                if (calendar.getTime().getTime() <= DateUtil.getCurrentDate().getTime()) {
                    machineDto.setStateName("设备离线");
                } else {
                    machineDto.setStateName("设备在线");
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            machineDto.setStateName("设备离线");
        }
    }


    private void getMachineLocation(MachineDto machineDto) {

        if (MachineDto.MACHINE_TYPE_ATTENDANCE.equals(machineDto.getMachineTypeCd())) {
            AttendanceClassesDto attendanceClassesDto = new AttendanceClassesDto();
            attendanceClassesDto.setClassesId(machineDto.getLocationObjId());
            List<AttendanceClassesDto> attendanceClassesDtos = attendanceClassesV1InnerServiceSMOImpl.queryAttendanceClassess(attendanceClassesDto);
            if (attendanceClassesDtos == null || attendanceClassesDtos.size() < 1) {
                machineDto.setLocationType(machineDto.getLocationTypeCd());
                return;
            }

            machineDto.setLocationType(attendanceClassesDtos.get(0).getClassesId());
            machineDto.setLocationObjName(attendanceClassesDtos.get(0).getClassesName());
            return;
        }

        CommunityLocationDto communityLocationDto = new CommunityLocationDto();
        communityLocationDto.setCommunityId(machineDto.getCommunityId());
        communityLocationDto.setLocationId(machineDto.getLocationTypeCd());
        List<CommunityLocationDto> communityLocationDtos = communityLocationInnerServiceSMOImpl.queryCommunityLocations(communityLocationDto);

        if (communityLocationDtos == null || communityLocationDtos.size() < 1) {
            machineDto.setLocationType(machineDto.getLocationTypeCd());
            return;
        }

        machineDto.setLocationType(communityLocationDtos.get(0).getLocationType());
        machineDto.setLocationObjName(communityLocationDtos.get(0).getLocationName());
    }

}
