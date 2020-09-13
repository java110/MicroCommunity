package com.java110.user.bmo.rentingAppointment.impl;

import com.java110.dto.rentingAppointment.RentingAppointmentDto;
import com.java110.intf.IRentingAppointmentInnerServiceSMO;
import com.java110.user.bmo.rentingAppointment.IGetRentingAppointmentBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getRentingAppointmentBMOImpl")
public class GetRentingAppointmentBMOImpl implements IGetRentingAppointmentBMO {

    @Autowired
    private IRentingAppointmentInnerServiceSMO rentingAppointmentInnerServiceSMOImpl;

    /**
     * @param rentingAppointmentDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(RentingAppointmentDto rentingAppointmentDto) {


        int count = rentingAppointmentInnerServiceSMOImpl.queryRentingAppointmentsCount(rentingAppointmentDto);

        List<RentingAppointmentDto> rentingAppointmentDtos = null;
        if (count > 0) {
            rentingAppointmentDtos = rentingAppointmentInnerServiceSMOImpl.queryRentingAppointments(rentingAppointmentDto);
        } else {
            rentingAppointmentDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) rentingAppointmentDto.getRow()), count, rentingAppointmentDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
