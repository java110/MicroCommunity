package com.java110.user.bmo.rentingAppointment.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.user.IRentingAppointmentInnerServiceSMO;
import com.java110.po.rentingAppointment.RentingAppointmentPo;
import com.java110.user.bmo.rentingAppointment.IUpdateRentingAppointmentBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateRentingAppointmentBMOImpl")
public class UpdateRentingAppointmentBMOImpl implements IUpdateRentingAppointmentBMO {

    @Autowired
    private IRentingAppointmentInnerServiceSMO rentingAppointmentInnerServiceSMOImpl;

    /**
     * @param rentingAppointmentPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(RentingAppointmentPo rentingAppointmentPo) {

        int flag = rentingAppointmentInnerServiceSMOImpl.updateRentingAppointment(rentingAppointmentPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
