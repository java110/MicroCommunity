package com.java110.fee.bmo.feePrintSpec.impl;

import com.java110.dto.fee.FeePrintSpecDto;
import com.java110.fee.bmo.feePrintSpec.IGetFeePrintSpecBMO;
import com.java110.intf.fee.IFeePrintSpecInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getFeePrintSpecBMOImpl")
public class GetFeePrintSpecBMOImpl implements IGetFeePrintSpecBMO {

    @Autowired
    private IFeePrintSpecInnerServiceSMO feePrintSpecInnerServiceSMOImpl;

    /**
     * @param feePrintSpecDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(FeePrintSpecDto feePrintSpecDto) {


        int count = feePrintSpecInnerServiceSMOImpl.queryFeePrintSpecsCount(feePrintSpecDto);

        List<FeePrintSpecDto> feePrintSpecDtos = null;
        if (count > 0) {
            feePrintSpecDtos = feePrintSpecInnerServiceSMOImpl.queryFeePrintSpecs(feePrintSpecDto);
        } else {
            feePrintSpecDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) feePrintSpecDto.getRow()), count, feePrintSpecDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
