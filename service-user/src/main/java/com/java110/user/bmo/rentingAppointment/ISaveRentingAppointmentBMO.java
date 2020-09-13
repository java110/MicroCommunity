package com.java110.user.bmo.rentingAppointment;

import com.java110.po.rentingAppointment.RentingAppointmentPo;
import org.springframework.http.ResponseEntity;
public interface ISaveRentingAppointmentBMO {


    /**
     * 添加租赁预约
     * add by wuxw
     * @param rentingAppointmentPo
     * @return
     */
    ResponseEntity<String> save(RentingAppointmentPo rentingAppointmentPo);


}
