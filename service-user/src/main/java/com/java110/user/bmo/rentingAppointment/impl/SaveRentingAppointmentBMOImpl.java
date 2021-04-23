package com.java110.user.bmo.rentingAppointment.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.rentingAppointment.RentingAppointmentDto;
import com.java110.intf.user.IRentingAppointmentInnerServiceSMO;
import com.java110.po.rentingAppointment.RentingAppointmentPo;
import com.java110.user.bmo.rentingAppointment.ISaveRentingAppointmentBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveRentingAppointmentBMOImpl")
public class SaveRentingAppointmentBMOImpl implements ISaveRentingAppointmentBMO {

    @Autowired
    private IRentingAppointmentInnerServiceSMO rentingAppointmentInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param rentingAppointmentPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(RentingAppointmentPo rentingAppointmentPo) {

        rentingAppointmentPo.setAppointmentId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_appointmentId));
        rentingAppointmentPo.setState(RentingAppointmentDto.STATE_SUBMIT);
        int flag = rentingAppointmentInnerServiceSMOImpl.saveRentingAppointment(rentingAppointmentPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
