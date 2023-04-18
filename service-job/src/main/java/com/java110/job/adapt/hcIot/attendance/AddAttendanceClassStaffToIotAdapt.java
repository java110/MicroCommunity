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
package com.java110.job.adapt.hcIot.attendance;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.dto.attendanceClasses.AttendanceClassesDto;
import com.java110.dto.attendanceClasses.AttendanceClassesStaffDto;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.machine.MachineDto;
import com.java110.entity.order.Business;
import com.java110.intf.common.IAttendanceClassesInnerServiceSMO;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.common.IMachineV1InnerServiceSMO;
import com.java110.intf.user.IAttendanceClassesStaffV1InnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.job.adapt.hcIot.asyn.IIotSendAsyn;
import com.java110.po.attendanceClassesStaff.AttendanceClassesStaffPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ImageUtils;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * HC iot 车辆同步适配器
 * <p>
 * 接口协议地址： https://gitee.com/java110/MicroCommunityThings/blob/master/back/docs/api.md
 *
 * @desc add by 吴学文 18:58
 */
@Component(value = "addAttendanceClassStaffToIotAdapt")
public class AddAttendanceClassStaffToIotAdapt extends DatabusAdaptImpl {

    @Autowired
    private IIotSendAsyn hcStoreUserAsynImpl;

    @Autowired
    private IAttendanceClassesStaffV1InnerServiceSMO attendanceClassesStaffV1InnerServiceSMOImpl;

    @Autowired
    private IAttendanceClassesInnerServiceSMO attendanceClassesInnerServiceSMOImpl;

    @Autowired
    private IMachineV1InnerServiceSMO machineV1InnerServiceSMOImpl;


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
        JSONArray businessStoreUsers = new JSONArray();
        if (data.containsKey(AttendanceClassesStaffPo.class.getSimpleName())) {
            Object bObj = data.get(AttendanceClassesStaffPo.class.getSimpleName());
            if (bObj instanceof JSONObject) {

                businessStoreUsers.add(bObj);
            } else if (bObj instanceof List) {
                businessStoreUsers = JSONArray.parseArray(JSONObject.toJSONString(bObj));
            } else {
                businessStoreUsers = (JSONArray) bObj;
            }
        } else {
            if (data instanceof JSONObject) {
                businessStoreUsers.add(data);
            }
        }
        //JSONObject businessStoreUser = data.getJSONObject("businessStoreUser");
        for (int bStoreUserIndex = 0; bStoreUserIndex < businessStoreUsers.size(); bStoreUserIndex++) {
            JSONObject businessStoreUser = businessStoreUsers.getJSONObject(bStoreUserIndex);
            doSendStoreUser(business, businessStoreUser);
        }
    }

    private void doSendStoreUser(Business business, JSONObject businessStoreUser) {

        AttendanceClassesStaffPo attendanceClassesStaffPo = BeanConvertUtil.covertBean(businessStoreUser, AttendanceClassesStaffPo.class);

        AttendanceClassesStaffDto attendanceClassesStaffDto = new AttendanceClassesStaffDto();
        attendanceClassesStaffDto.setCsId(attendanceClassesStaffPo.getCsId());
        attendanceClassesStaffDto.setStoreId(attendanceClassesStaffPo.getStoreId());
        List<AttendanceClassesStaffDto> attendanceClassesStaffs = attendanceClassesStaffV1InnerServiceSMOImpl.queryAttendanceClassesStaffs(attendanceClassesStaffDto);
        if (attendanceClassesStaffs == null || attendanceClassesStaffs.size() < 1) {
            return;
        }


        //查询员工部门是否参与考勤
        AttendanceClassesDto attendanceClassesDto = new AttendanceClassesDto();
        attendanceClassesDto.setClassesId(attendanceClassesStaffs.get(0).getClassesId());
        attendanceClassesDto.setStoreId(attendanceClassesStaffs.get(0).getStoreId());
        List<AttendanceClassesDto> attendanceClassesDtos = attendanceClassesInnerServiceSMOImpl.queryAttendanceClassess(attendanceClassesDto);

        //员工部门没有考勤，不用处理
        if (attendanceClassesDtos == null || attendanceClassesDtos.size() < 1) {
            return;
        }

        MachineDto machineDto = new MachineDto();
        machineDto.setLocationObjId(attendanceClassesDtos.get(0).getClassesId());
        machineDto.setMachineTypeCd(MachineDto.MACHINE_TYPE_ATTENDANCE);
        List<MachineDto> machineDtos = machineV1InnerServiceSMOImpl.queryMachines(machineDto);
        //员工部门没有考勤，不用处理
        if (machineDtos == null || machineDtos.size() < 1) {
            return;
        }
        String img = getStaffPhoto(attendanceClassesStaffs.get(0));

        JSONObject storeUserObj = null;
        List<JSONObject> storeUserObjs = new ArrayList<>();
        for (MachineDto tmpMachineDto : machineDtos) {

            storeUserObj = new JSONObject();
            storeUserObj.put("extClassesId", attendanceClassesDtos.get(0).getClassesId());
            storeUserObj.put("extStaffId", attendanceClassesStaffs.get(0).getStaffId());
            storeUserObj.put("staffName", attendanceClassesStaffs.get(0).getStaffName());
            storeUserObj.put("departmentId", attendanceClassesDtos.get(0).getClassesId());
            storeUserObj.put("departmentName", attendanceClassesDtos.get(0).getClassesName());
            storeUserObj.put("machineCode", tmpMachineDto.getMachineCode());
            storeUserObj.put("extMachineId", tmpMachineDto.getMachineId());
            storeUserObj.put("extCommunityId", tmpMachineDto.getCommunityId());
            storeUserObj.put("faceBase64", img);
            storeUserObjs.add(storeUserObj);
        }
        JSONObject postParameters = new JSONObject();
        postParameters.put("classesName", attendanceClassesDtos.get(0).getClassesName());
        postParameters.put("extClassesId", attendanceClassesDtos.get(0).getClassesId());
        postParameters.put("extCommunityId", "-1");
        hcStoreUserAsynImpl.addAttendanceStaff(postParameters, storeUserObjs);

    }

    private String getStaffPhoto(AttendanceClassesStaffDto attendanceClassesStaffDto) {

        FileRelDto fileRelDto = new FileRelDto();
        fileRelDto.setObjId(attendanceClassesStaffDto.getCsId());
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
        if (fileRelDtos == null || fileRelDtos.size() != 1) {
            return "";
        }
        String fileName = fileRelDtos.get(0).getFileSaveName();


        if(StringUtil.isEmpty(fileName)){
            return fileName;
        }
        String imgUrl = MappingCache.getValue(MappingConstant.FILE_DOMAIN,"IMG_PATH");
        if(!fileName.startsWith("http")){
            fileName = imgUrl +fileName;
        }

        String faceBase64 = ImageUtils.getBase64ByImgUrl(fileName);

        return faceBase64;
    }
}
