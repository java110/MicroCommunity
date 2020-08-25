package com.java110.user.bmo.rentingPool.impl;

import com.java110.dto.rentingPool.RentingPoolDto;
import com.java110.intf.user.IRentingPoolInnerServiceSMO;
import com.java110.user.bmo.rentingPool.IGetRentingPoolBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getRentingPoolBMOImpl")
public class GetRentingPoolBMOImpl implements IGetRentingPoolBMO {

    @Autowired
    private IRentingPoolInnerServiceSMO rentingPoolInnerServiceSMOImpl;

    /**
     * @param rentingPoolDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(RentingPoolDto rentingPoolDto) {


        int count = rentingPoolInnerServiceSMOImpl.queryRentingPoolsCount(rentingPoolDto);

        List<RentingPoolDto> rentingPoolDtos = null;
        if (count > 0) {
            rentingPoolDtos = rentingPoolInnerServiceSMOImpl.queryRentingPools(rentingPoolDto);
        } else {
            rentingPoolDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) rentingPoolDto.getRow()), count, rentingPoolDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
