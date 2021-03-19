package com.java110.fee.bmo.importFeeDetail.impl;

import com.java110.dto.importFee.ImportFeeDetailDto;
import com.java110.fee.bmo.importFeeDetail.IGetImportFeeDetailBMO;
import com.java110.intf.fee.IImportFeeDetailInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getImportFeeDetailBMOImpl")
public class GetImportFeeDetailBMOImpl implements IGetImportFeeDetailBMO {

    @Autowired
    private IImportFeeDetailInnerServiceSMO importFeeDetailInnerServiceSMOImpl;

    /**
     * @param importFeeDetailDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ImportFeeDetailDto importFeeDetailDto) {


        int count = importFeeDetailInnerServiceSMOImpl.queryImportFeeDetailsCount(importFeeDetailDto);

        List<ImportFeeDetailDto> importFeeDetailDtos = null;
        if (count > 0) {
            importFeeDetailDtos = importFeeDetailInnerServiceSMOImpl.queryImportFeeDetails(importFeeDetailDto);
        } else {
            importFeeDetailDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) importFeeDetailDto.getRow()), count, importFeeDetailDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
