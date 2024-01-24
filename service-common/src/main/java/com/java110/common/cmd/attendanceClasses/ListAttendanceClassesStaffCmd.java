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
package com.java110.common.cmd.attendanceClasses;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.file.FileRelDto;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.user.IAttendanceClassesStaffV1InnerServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import com.java110.dto.attendance.AttendanceClassesStaffDto;

import java.util.List;
import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 类表述：查询
 * 服务编码：attendanceClassesStaff.listAttendanceClassesStaff
 * 请求路劲：/app/attendanceClassesStaff.ListAttendanceClassesStaff
 * add by 吴学文 at 2023-02-21 23:48:34 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "attendanceClasses.listAttendanceClassesStaff")
public class ListAttendanceClassesStaffCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(ListAttendanceClassesStaffCmd.class);
    @Autowired
    private IAttendanceClassesStaffV1InnerServiceSMO attendanceClassesStaffV1InnerServiceSMOImpl;


    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validatePageInfo(reqJson);

        String storeId = cmdDataFlowContext.getReqHeaders().get("store-id");
        Assert.hasLength(storeId, "未包含商户信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        String storeId = cmdDataFlowContext.getReqHeaders().get("store-id");

        AttendanceClassesStaffDto attendanceClassesStaffDto = BeanConvertUtil.covertBean(reqJson, AttendanceClassesStaffDto.class);
        attendanceClassesStaffDto.setStoreId(storeId);
        int count = attendanceClassesStaffV1InnerServiceSMOImpl.queryAttendanceClassesStaffsCount(attendanceClassesStaffDto);

        List<AttendanceClassesStaffDto> attendanceClassesStaffDtos = null;

        if (count > 0) {
            attendanceClassesStaffDtos = attendanceClassesStaffV1InnerServiceSMOImpl.queryAttendanceClassesStaffs(attendanceClassesStaffDto);
            refreshPhoto(attendanceClassesStaffDtos);
        } else {
            attendanceClassesStaffDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, attendanceClassesStaffDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        cmdDataFlowContext.setResponseEntity(responseEntity);
    }

    private void refreshPhoto(List<AttendanceClassesStaffDto> attendanceClassesStaffDtos) {
        if (ListUtil.isNull(attendanceClassesStaffDtos)) {
            return;
        }

        List<String> csId = new ArrayList<>();
        for (AttendanceClassesStaffDto attendanceClassesStaffDto : attendanceClassesStaffDtos) {
            csId.add(attendanceClassesStaffDto.getCsId());
        }

        FileRelDto fileRelDto = new FileRelDto();
        fileRelDto.setObjIds(csId.toArray(new String[csId.size()]));
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);

        if (fileRelDtos == null || fileRelDtos.size() < 1) {
            return;
        }
        String imgUrl = MappingCache.getValue(MappingConstant.FILE_DOMAIN, "IMG_PATH");
        for (AttendanceClassesStaffDto attendanceClassesStaffDto : attendanceClassesStaffDtos) {
            for (FileRelDto tmpFileRelDto : fileRelDtos) {
                if (!attendanceClassesStaffDto.getCsId().equals(tmpFileRelDto.getObjId())) {
                    continue;
                }
                if (tmpFileRelDto.getFileSaveName().startsWith("http")) {
                    attendanceClassesStaffDto.setPersonFace(tmpFileRelDto.getFileSaveName());
                } else {
                    attendanceClassesStaffDto.setPersonFace(imgUrl + tmpFileRelDto.getFileSaveName());
                }
            }
        }
    }
}
