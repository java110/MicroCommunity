package com.java110.user.bmo.rentingPoolFlow.impl;

import com.java110.dto.rentingPool.RentingPoolFlowDto;
import com.java110.intf.user.IRentingPoolFlowInnerServiceSMO;
import com.java110.user.bmo.rentingPoolFlow.IGetRentingPoolFlowBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getRentingPoolFlowBMOImpl")
public class GetRentingPoolFlowBMOImpl implements IGetRentingPoolFlowBMO {

    @Autowired
    private IRentingPoolFlowInnerServiceSMO rentingPoolFlowInnerServiceSMOImpl;

    /**
     * @param rentingPoolFlowDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(RentingPoolFlowDto rentingPoolFlowDto) {


        int count = rentingPoolFlowInnerServiceSMOImpl.queryRentingPoolFlowsCount(rentingPoolFlowDto);

        List<RentingPoolFlowDto> rentingPoolFlowDtos = null;
        if (count > 0) {
            rentingPoolFlowDtos = rentingPoolFlowInnerServiceSMOImpl.queryRentingPoolFlows(rentingPoolFlowDto);
        } else {
            rentingPoolFlowDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) rentingPoolFlowDto.getRow()), count, rentingPoolFlowDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
