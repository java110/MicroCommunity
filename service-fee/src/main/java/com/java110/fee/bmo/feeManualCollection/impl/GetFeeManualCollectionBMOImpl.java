package com.java110.fee.bmo.feeManualCollection.impl;

import com.java110.dto.fee.FeeManualCollectionDto;
import com.java110.dto.fee.FeeManualCollectionDetailDto;
import com.java110.fee.bmo.feeManualCollection.IGetFeeManualCollectionBMO;
import com.java110.intf.fee.IFeeManualCollectionDetailInnerServiceSMO;
import com.java110.intf.fee.IFeeManualCollectionInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getFeeManualCollectionBMOImpl")
public class GetFeeManualCollectionBMOImpl implements IGetFeeManualCollectionBMO {

    @Autowired
    private IFeeManualCollectionInnerServiceSMO feeManualCollectionInnerServiceSMOImpl;

    @Autowired
    private IFeeManualCollectionDetailInnerServiceSMO feeManualCollectionDetailInnerServiceSMOImpl;

    /**
     * @param feeManualCollectionDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(FeeManualCollectionDto feeManualCollectionDto) {


        int count = feeManualCollectionInnerServiceSMOImpl.queryFeeManualCollectionsCount(feeManualCollectionDto);

        List<FeeManualCollectionDto> feeManualCollectionDtos = null;
        if (count > 0) {
            feeManualCollectionDtos = feeManualCollectionInnerServiceSMOImpl.queryFeeManualCollections(feeManualCollectionDto);
            freshTotalFee(feeManualCollectionDtos);
        } else {
            feeManualCollectionDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) feeManualCollectionDto.getRow()), count, feeManualCollectionDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    private void freshTotalFee(List<FeeManualCollectionDto> feeManualCollectionDtos) {

        for (FeeManualCollectionDto feeManualCollectionDto : feeManualCollectionDtos) {
            FeeManualCollectionDetailDto feeManualCollectionDetailDto = new FeeManualCollectionDetailDto();
            feeManualCollectionDetailDto.setCollectionId(feeManualCollectionDto.getCollectionId());
            feeManualCollectionDetailDto.setCommunityId(feeManualCollectionDto.getCommunityId());
            double totalFee = feeManualCollectionDetailInnerServiceSMOImpl.queryFeeManualCollectionDetailTotalFee(feeManualCollectionDetailDto);
            feeManualCollectionDto.setTotalFee(totalFee + "");
        }
    }

}
