package com.java110.fee.bmo.importFee.impl;

import com.java110.dto.importFee.ImportFeeDto;
import com.java110.fee.bmo.importFee.IGetImportFeeBMO;
import com.java110.intf.fee.IImportFeeInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getImportFeeBMOImpl")
public class GetImportFeeBMOImpl implements IGetImportFeeBMO {

    @Autowired
    private IImportFeeInnerServiceSMO importFeeInnerServiceSMOImpl;

    /**
     * @param importFeeDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ImportFeeDto importFeeDto) {


        int count = importFeeInnerServiceSMOImpl.queryImportFeesCount(importFeeDto);

        List<ImportFeeDto> importFeeDtos = null;
        if (count > 0) {
            importFeeDtos = importFeeInnerServiceSMOImpl.queryImportFees(importFeeDto);
        } else {
            importFeeDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) importFeeDto.getRow()), count, importFeeDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
