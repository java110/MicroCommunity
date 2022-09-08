package com.java110.common.bmo.attendanceClassesTask.impl;

import com.java110.common.bmo.attendanceClassesTask.ISaveAttendanceClassesTaskBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.dto.attendanceClasses.AttendanceClassesDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.common.IAttendanceClassesInnerServiceSMO;
import com.java110.intf.common.IAttendanceClassesTaskDetailInnerServiceSMO;
import com.java110.intf.common.IAttendanceClassesTaskInnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.attendanceClassesTask.AttendanceClassesTaskPo;
import com.java110.po.attendanceClassesTaskDetail.AttendanceClassesTaskDetailPo;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("saveAttendanceClassesTaskBMOImpl")
public class SaveAttendanceClassesTaskBMOImpl implements ISaveAttendanceClassesTaskBMO {

    @Autowired
    private IAttendanceClassesTaskInnerServiceSMO attendanceClassesTaskInnerServiceSMOImpl;

    @Autowired
    private IAttendanceClassesTaskDetailInnerServiceSMO attendanceClassesTaskDetailInnerServiceSMOImpl;
    @Autowired
    private IAttendanceClassesInnerServiceSMO attendanceClassesInnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param attendanceClassesTaskPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(AttendanceClassesTaskPo attendanceClassesTaskPo,
                                       List<AttendanceClassesTaskDetailPo> attendanceClassesTaskDetailPos) {

        //attendanceClassesTaskPo.setTaskId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_taskId));
        //查询班组是否存在
        AttendanceClassesDto attendanceClassesDto = new AttendanceClassesDto();
        attendanceClassesDto.setClassesId(attendanceClassesTaskPo.getClassId());
        List<AttendanceClassesDto> attendanceClassesDtos = attendanceClassesInnerServiceSMOImpl.queryAttendanceClassess(attendanceClassesDto);

        Assert.listOnlyOne(attendanceClassesDtos, "班组不存在");

        attendanceClassesTaskPo.setStoreId(attendanceClassesDtos.get(0).getStoreId());

        //查询员工信息
        UserDto userDto = new UserDto();
        userDto.setUserId(attendanceClassesTaskPo.getStaffId());
        userDto.setPage(1);
        userDto.setRow(1);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        Assert.listOnlyOne(userDtos,"未包含员工");

        attendanceClassesTaskPo.setStaffName(userDtos.get(0).getName());

        int flag = attendanceClassesTaskInnerServiceSMOImpl.saveAttendanceClassesTask(attendanceClassesTaskPo);

        for (AttendanceClassesTaskDetailPo attendanceClassesTaskDetailPo : attendanceClassesTaskDetailPos) {
            attendanceClassesTaskDetailPo.setStoreId(attendanceClassesDtos.get(0).getStoreId());
            attendanceClassesTaskDetailInnerServiceSMOImpl.saveAttendanceClassesTaskDetail(attendanceClassesTaskDetailPo);
        }

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
