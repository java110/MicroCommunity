package com.java110.user.bmo.rentingPoolFlow;
import com.java110.dto.rentingPool.RentingPoolFlowDto;
import org.springframework.http.ResponseEntity;
public interface IGetRentingPoolFlowBMO {


    /**
     * 查询出租流程
     * add by wuxw
     * @param  rentingPoolFlowDto
     * @return
     */
    ResponseEntity<String> get(RentingPoolFlowDto rentingPoolFlowDto);


}
