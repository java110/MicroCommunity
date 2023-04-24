package com.java110.user.bmo.rentingPoolAttr.impl;

import com.java110.dto.rentingPool.RentingPoolAttrDto;
import com.java110.intf.user.IRentingPoolAttrInnerServiceSMO;
import com.java110.user.bmo.rentingPoolAttr.IGetRentingPoolAttrBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getRentingPoolAttrBMOImpl")
public class GetRentingPoolAttrBMOImpl implements IGetRentingPoolAttrBMO {

    @Autowired
    private IRentingPoolAttrInnerServiceSMO rentingPoolAttrInnerServiceSMOImpl;

    /**
     * @param rentingPoolAttrDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(RentingPoolAttrDto rentingPoolAttrDto) {


        int count = rentingPoolAttrInnerServiceSMOImpl.queryRentingPoolAttrsCount(rentingPoolAttrDto);

        List<RentingPoolAttrDto> rentingPoolAttrDtos = null;
        if (count > 0) {
            rentingPoolAttrDtos = rentingPoolAttrInnerServiceSMOImpl.queryRentingPoolAttrs(rentingPoolAttrDto);
        } else {
            rentingPoolAttrDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) rentingPoolAttrDto.getRow()), count, rentingPoolAttrDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
