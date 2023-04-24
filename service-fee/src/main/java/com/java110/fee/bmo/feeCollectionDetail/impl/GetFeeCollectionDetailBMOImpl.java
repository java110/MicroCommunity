package com.java110.fee.bmo.feeCollectionDetail.impl;

import com.java110.dto.fee.FeeCollectionDetailDto;
import com.java110.fee.bmo.feeCollectionDetail.IGetFeeCollectionDetailBMO;
import com.java110.intf.fee.IFeeCollectionDetailInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getFeeCollectionDetailBMOImpl")
public class GetFeeCollectionDetailBMOImpl implements IGetFeeCollectionDetailBMO {

    @Autowired
    private IFeeCollectionDetailInnerServiceSMO feeCollectionDetailInnerServiceSMOImpl;

    /**
     * @param feeCollectionDetailDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(FeeCollectionDetailDto feeCollectionDetailDto) {


        int count = feeCollectionDetailInnerServiceSMOImpl.queryFeeCollectionDetailsCount(feeCollectionDetailDto);

        List<FeeCollectionDetailDto> feeCollectionDetailDtos = null;
        if (count > 0) {
            feeCollectionDetailDtos = feeCollectionDetailInnerServiceSMOImpl.queryFeeCollectionDetails(feeCollectionDetailDto);
        } else {
            feeCollectionDetailDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) feeCollectionDetailDto.getRow()), count, feeCollectionDetailDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
