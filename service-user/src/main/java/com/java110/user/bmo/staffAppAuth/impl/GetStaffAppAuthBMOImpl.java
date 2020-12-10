package com.java110.user.bmo.staffAppAuth.impl;

import com.java110.dto.staffAppAuth.StaffAppAuthDto;
import com.java110.intf.user.IStaffAppAuthInnerServiceSMO;
import com.java110.user.bmo.staffAppAuth.IGetStaffAppAuthBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getStaffAppAuthBMOImpl")
public class GetStaffAppAuthBMOImpl implements IGetStaffAppAuthBMO {

    @Autowired
    private IStaffAppAuthInnerServiceSMO staffAppAuthInnerServiceSMOImpl;

    /**
     * @param staffAppAuthDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(StaffAppAuthDto staffAppAuthDto) {


        int count = staffAppAuthInnerServiceSMOImpl.queryStaffAppAuthsCount(staffAppAuthDto);

        List<StaffAppAuthDto> staffAppAuthDtos = null;
        if (count > 0) {
            staffAppAuthDtos = staffAppAuthInnerServiceSMOImpl.queryStaffAppAuths(staffAppAuthDto);
        } else {
            staffAppAuthDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) staffAppAuthDto.getRow()), count, staffAppAuthDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
