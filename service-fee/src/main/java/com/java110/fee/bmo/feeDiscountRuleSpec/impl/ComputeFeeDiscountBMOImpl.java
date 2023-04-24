package com.java110.fee.bmo.feeDiscountRuleSpec.impl;

import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.fee.ComputeDiscountDto;
import com.java110.fee.bmo.feeDiscountRuleSpec.IComputeFeeDiscountBMO;
import com.java110.fee.smo.impl.FeeDiscountInnerServiceSMOImpl;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service("computeFeeDiscountBMOImpl")
public class ComputeFeeDiscountBMOImpl implements IComputeFeeDiscountBMO {

    @Autowired
    private FeeDiscountInnerServiceSMOImpl feeDiscountInnerServiceSMOImpl;

    /**
     * 计算折扣
     *
     * @param feeId
     * @param communityId
     * @param cycles
     * @param page
     * @param row
     * @return
     */
    @Override
    public ResponseEntity<String> compute(String feeId, String communityId, double cycles, String payerObjId, String payerObjType, String endTime, int page, int row) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        FeeDetailDto feeDetailDto = new FeeDetailDto();
        feeDetailDto.setCommunityId(communityId);
        feeDetailDto.setFeeId(feeId);
        feeDetailDto.setCycles(cycles + "");
        feeDetailDto.setPayerObjId(payerObjId);
        feeDetailDto.setPayerObjType(payerObjType);
        //缴费开始时间为上月到期时间
        feeDetailDto.setStartTime(simpleDateFormat.parse(endTime));
        feeDetailDto.setRow(row);
        feeDetailDto.setPage(page);
        List<ComputeDiscountDto> computeDiscountDtos = feeDiscountInnerServiceSMOImpl.computeDiscount(feeDetailDto);
        return ResultVo.createResponseEntity(computeDiscountDtos);
    }
}
