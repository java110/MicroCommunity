package com.java110.user.bmo.rentingConfig.impl;

import com.java110.dto.rentingPool.RentingConfigDto;
import com.java110.intf.user.IRentingConfigInnerServiceSMO;
import com.java110.user.bmo.rentingConfig.IGetRentingConfigBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getRentingConfigBMOImpl")
public class GetRentingConfigBMOImpl implements IGetRentingConfigBMO {

    @Autowired
    private IRentingConfigInnerServiceSMO rentingConfigInnerServiceSMOImpl;

    /**
     * @param rentingConfigDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(RentingConfigDto rentingConfigDto) {


        int count = rentingConfigInnerServiceSMOImpl.queryRentingConfigsCount(rentingConfigDto);

        List<RentingConfigDto> rentingConfigDtos = null;
        if (count > 0) {
            rentingConfigDtos = rentingConfigInnerServiceSMOImpl.queryRentingConfigs(rentingConfigDto);
        } else {
            rentingConfigDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) rentingConfigDto.getRow()), count, rentingConfigDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
