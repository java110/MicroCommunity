package com.java110.user.bmo.rentingConfig;

import com.java110.dto.rentingPool.RentingConfigDto;
import org.springframework.http.ResponseEntity;

public interface IGetRentingConfigBMO {


    /**
     * 查询房屋出租配置
     * add by wuxw
     *
     * @param rentingConfigDto
     * @return
     */
    ResponseEntity<String> get(RentingConfigDto rentingConfigDto);


}
