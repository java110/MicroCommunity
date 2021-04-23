package com.java110.common.bmo.attendanceClassesTaskDetail.impl;

import com.java110.common.bmo.attendanceClassesTaskDetail.ISaveAttendanceClassesTaskDetailBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.common.IAttendanceClassesTaskDetailInnerServiceSMO;
import com.java110.po.attendanceClassesTaskDetail.AttendanceClassesTaskDetailPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveAttendanceClassesTaskDetailBMOImpl")
public class SaveAttendanceClassesTaskDetailBMOImpl implements ISaveAttendanceClassesTaskDetailBMO {

    @Autowired
    private IAttendanceClassesTaskDetailInnerServiceSMO attendanceClassesTaskDetailInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param attendanceClassesTaskDetailPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(AttendanceClassesTaskDetailPo attendanceClassesTaskDetailPo) {

        attendanceClassesTaskDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        int flag = attendanceClassesTaskDetailInnerServiceSMOImpl.saveAttendanceClassesTaskDetail(attendanceClassesTaskDetailPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
