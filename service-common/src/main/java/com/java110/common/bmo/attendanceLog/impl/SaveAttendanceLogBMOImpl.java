package com.java110.common.bmo.attendanceLog.impl;

import com.java110.common.bmo.attendanceLog.ISaveAttendanceLogBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.store.StoreUserDto;
import com.java110.intf.common.IAttendanceLogInnerServiceSMO;
import com.java110.intf.store.IStoreInnerServiceSMO;
import com.java110.po.attendanceLog.AttendanceLogPo;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("saveAttendanceLogBMOImpl")
public class SaveAttendanceLogBMOImpl implements ISaveAttendanceLogBMO {

    @Autowired
    private IAttendanceLogInnerServiceSMO attendanceLogInnerServiceSMOImpl;

    @Autowired
    private IStoreInnerServiceSMO storeInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param attendanceLogPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(AttendanceLogPo attendanceLogPo) {

        StoreUserDto storeUserDto = new StoreUserDto();
        storeUserDto.setUserId(attendanceLogPo.getStaffId());
        List<StoreUserDto> storeUserDtos = storeInnerServiceSMOImpl.getStoreUserInfo(storeUserDto);

        Assert.listOnlyOne(storeUserDtos, "未找到商户信息");

        attendanceLogPo.setLogId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_logId));
        attendanceLogPo.setStoreId(storeUserDtos.get(0).getStoreId());
        attendanceLogPo.setStaffName(storeUserDtos.get(0).getStaffName());
        int flag = attendanceLogInnerServiceSMOImpl.saveAttendanceLog(attendanceLogPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
