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
package com.java110.job.adapt.hcIot.staff;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.dto.attendanceClasses.AttendanceClassesDto;
import com.java110.dto.org.OrgStaffRelDto;
import com.java110.entity.order.Business;
import com.java110.intf.common.IAttendanceClassesInnerServiceSMO;
import com.java110.intf.user.IOrgStaffRelInnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.job.adapt.hcIot.asyn.IIotSendAsyn;
import com.java110.po.attendanceClasses.AttendanceClassesPo;
import com.java110.po.store.StoreUserPo;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * HC iot 考勤同步适配器
 * <p>
 * 接口协议地址： https://gitee.com/java110/MicroCommunityThings/blob/master/back/docs/api.md
 *
 * @desc add by 吴学文 18:58
 */
@Component(value = "deleteStaffToIotAdapt")
public class DeleteStaffToIotAdapt extends DatabusAdaptImpl {

    @Autowired
    private IIotSendAsyn hcStoreUserAsynImpl;


    @Autowired
    private IAttendanceClassesInnerServiceSMO attendanceClassesInnerServiceSMOImpl;

    @Autowired
    private IOrgStaffRelInnerServiceSMO orgStaffRelInnerServiceSMOImpl;


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
        if (data.containsKey(StoreUserPo.class.getSimpleName())) {
            Object bObj = data.get(StoreUserPo.class.getSimpleName());
            JSONArray businessOwnerAttendances = null;
            if (bObj instanceof JSONObject) {
                businessOwnerAttendances = new JSONArray();
                businessOwnerAttendances.add(bObj);
            } else if (bObj instanceof List) {
                businessOwnerAttendances = JSONArray.parseArray(JSONObject.toJSONString(bObj));
            } else {
                businessOwnerAttendances = (JSONArray) bObj;
            }
            //JSONObject businessOwnerAttendance = data.getJSONObject("businessOwnerAttendance");
            for (int bOwnerAttendanceIndex = 0; bOwnerAttendanceIndex < businessOwnerAttendances.size(); bOwnerAttendanceIndex++) {
                JSONObject businessOwnerAttendance = businessOwnerAttendances.getJSONObject(bOwnerAttendanceIndex);
                doSendOwnerAttendance(business, businessOwnerAttendance);
            }
        }
    }

    private void doSendOwnerAttendance(Business business, JSONObject businessStoreUser) {

        StoreUserPo storeUserPo = BeanConvertUtil.covertBean(businessStoreUser, StoreUserPo.class);

        //查询员工部门
        OrgStaffRelDto orgStaffRelDto = new OrgStaffRelDto();
        orgStaffRelDto.setStaffId(storeUserPo.getUserId());
        orgStaffRelDto.setStoreId(storeUserPo.getStoreId());
        List<OrgStaffRelDto> orgStaffRelDtos = orgStaffRelInnerServiceSMOImpl.queryOrgStaffRels(orgStaffRelDto);

        Assert.listOnlyOne(orgStaffRelDtos, "未包含员工信息");

        //查询员工部门是否参与考勤
        AttendanceClassesDto attendanceClassesDto = new AttendanceClassesDto();
        attendanceClassesDto.setClassesObjType(AttendanceClassesDto.CLASSES_OBJ_TYPE_PARTMENT);
        attendanceClassesDto.setClassesObjId(orgStaffRelDtos.get(0).getDepartmentId());
        List<AttendanceClassesDto> attendanceClassesDtos = attendanceClassesInnerServiceSMOImpl.queryAttendanceClassess(attendanceClassesDto);

        //员工部门没有考勤，不用处理
        if (attendanceClassesDtos == null || attendanceClassesDtos.size() < 1) {
            return;
        }

        for(AttendanceClassesDto tmpAttendanceClassesDto : attendanceClassesDtos ){
            JSONObject postParameters = new JSONObject();
            postParameters.put("classesName", tmpAttendanceClassesDto.getClassesName());
            postParameters.put("extClassesId", tmpAttendanceClassesDto.getClassesId());
            postParameters.put("extStaffId", orgStaffRelDtos.get(0).getStaffId());
            postParameters.put("extCommunityId", "-1");
            hcStoreUserAsynImpl.deleteAttendanceStaff(postParameters);
        }

    }
}
