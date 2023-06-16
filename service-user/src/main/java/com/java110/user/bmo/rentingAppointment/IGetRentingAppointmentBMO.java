package com.java110.user.bmo.rentingAppointment;
import com.java110.dto.renting.RentingAppointmentDto;
import org.springframework.http.ResponseEntity;
public interface IGetRentingAppointmentBMO {


    /**
     * 查询租赁预约
     * add by wuxw
     * @param  rentingAppointmentDto
     * @return
     */
    ResponseEntity<String> get(RentingAppointmentDto rentingAppointmentDto);


}
