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
import com.java110.dto.org.OrgStaffRelDto;
import com.java110.entity.order.Business;
import com.java110.intf.common.IAttendanceClassesInnerServiceSMO;
import com.java110.intf.user.IOrgStaffRelInnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.job.adapt.hcIot.asyn.IIotSendAsyn;
import com.java110.po.attendanceClasses.AttendanceClassesPo;
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
@Component(value = "updateAttendanceToIotAdapt")
public class UpdateAttendanceToIotAdapt extends DatabusAdaptImpl {

    @Autowired
    private IIotSendAsyn hcOwnerAttendanceAsynImpl;


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
        JSONArray businessOwnerAttendances = new JSONArray();
        if (data.containsKey(AttendanceClassesPo.class.getSimpleName())) {
            Object bObj = data.get(AttendanceClassesPo.class.getSimpleName());
            if (bObj instanceof JSONObject) {
                businessOwnerAttendances.add(bObj);
            } else if (bObj instanceof List) {
                businessOwnerAttendances = JSONArray.parseArray(JSONObject.toJSONString(bObj));
            } else {
                businessOwnerAttendances = (JSONArray) bObj;
            }

        } else {
            if (data instanceof JSONObject) {
                businessOwnerAttendances.add(data);
            }
        }

        //JSONObject businessOwnerAttendance = data.getJSONObject("businessOwnerAttendance");
        for (int bOwnerAttendanceIndex = 0; bOwnerAttendanceIndex < businessOwnerAttendances.size(); bOwnerAttendanceIndex++) {
            JSONObject businessOwnerAttendance = businessOwnerAttendances.getJSONObject(bOwnerAttendanceIndex);
            doSendOwnerAttendance(business, businessOwnerAttendance);
        }
    }

    private void doSendOwnerAttendance(Business business, JSONObject businessOwnerAttendance) {

        AttendanceClassesPo ownerAttendancePo = BeanConvertUtil.covertBean(businessOwnerAttendance, AttendanceClassesPo.class);

        AttendanceClassesDto ownerAttendanceDto = new AttendanceClassesDto();
        ownerAttendanceDto.setClassesId(ownerAttendancePo.getClassesId());
        ownerAttendanceDto.setStoreId(ownerAttendancePo.getStoreId());
        List<AttendanceClassesDto> attendanceDtos = attendanceClassesInnerServiceSMOImpl.queryAttendanceClassess(ownerAttendanceDto);

        Assert.listOnlyOne(attendanceDtos, "未找到考勤班组");

        JSONObject postParameters = new JSONObject();
        postParameters.put("classesName", attendanceDtos.get(0).getClassesName());
        postParameters.put("timeOffset", attendanceDtos.get(0).getTimeOffset());
        postParameters.put("clockCount", attendanceDtos.get(0).getClockCount());
        postParameters.put("clockType", attendanceDtos.get(0).getClockType());
        postParameters.put("clockTypeValue", attendanceDtos.get(0).getClockTypeValue());
        postParameters.put("lateOffset", attendanceDtos.get(0).getLateOffset());
        postParameters.put("leaveOffset", attendanceDtos.get(0).getLeaveOffset());
        postParameters.put("extClassesId", attendanceDtos.get(0).getClassesId());
        postParameters.put("extCommunityId", "-1");
        postParameters.put("attrs", JSONArray.parseArray(JSONArray.toJSONString(attendanceDtos.get(0).getAttrs())));
        hcOwnerAttendanceAsynImpl.updateAttendance(postParameters);
    }
}
