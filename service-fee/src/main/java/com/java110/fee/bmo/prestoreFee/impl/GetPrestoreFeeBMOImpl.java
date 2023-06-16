package com.java110.fee.bmo.prestoreFee.impl;

import com.java110.fee.bmo.prestoreFee.IGetPrestoreFeeBMO;
import com.java110.intf.fee.IPrestoreFeeInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.java110.dto.prestoreFee.PrestoreFeeDto;

import java.util.ArrayList;
import java.util.List;

@Service("getPrestoreFeeBMOImpl")
public class GetPrestoreFeeBMOImpl implements IGetPrestoreFeeBMO {

    @Autowired
    private IPrestoreFeeInnerServiceSMO prestoreFeeInnerServiceSMOImpl;

    /**
     *
     *
     * @param  prestoreFeeDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(PrestoreFeeDto prestoreFeeDto) {


        int count = prestoreFeeInnerServiceSMOImpl.queryPrestoreFeesCount(prestoreFeeDto);

        List<PrestoreFeeDto> prestoreFeeDtos = null;
        if (count > 0) {
            prestoreFeeDtos = prestoreFeeInnerServiceSMOImpl.queryPrestoreFees(prestoreFeeDto);
        } else {
            prestoreFeeDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) prestoreFeeDto.getRow()), count, prestoreFeeDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
