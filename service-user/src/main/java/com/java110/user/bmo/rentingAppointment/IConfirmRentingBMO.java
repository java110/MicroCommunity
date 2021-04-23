package com.java110.user.bmo.rentingAppointment;
import com.java110.po.rentingAppointment.RentingAppointmentPo;
import org.springframework.http.ResponseEntity;

public interface IConfirmRentingBMO {


    /**
     * 确认出租
     * add by wuxw
     * @param rentingAppointmentPo
     * @return
     */
    ResponseEntity<String> confirm(RentingAppointmentPo rentingAppointmentPo);


}
