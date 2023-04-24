package com.java110.fee.bmo.feeDiscount.impl;

import com.java110.dto.fee.FeeDiscountDto;
import com.java110.dto.fee.FeeDiscountSpecDto;
import com.java110.dto.payFeeDetailDiscount.PayFeeDetailDiscountDto;
import com.java110.fee.bmo.feeDiscount.IGetFeeDiscountBMO;
import com.java110.intf.fee.IFeeDiscountInnerServiceSMO;
import com.java110.intf.fee.IFeeDiscountSpecInnerServiceSMO;
import com.java110.intf.fee.IPayFeeDetailDiscountInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getFeeDiscountBMOImpl")
public class GetFeeDiscountBMOImpl implements IGetFeeDiscountBMO {

    @Autowired
    private IFeeDiscountInnerServiceSMO feeDiscountInnerServiceSMOImpl;

    @Autowired
    private IFeeDiscountSpecInnerServiceSMO feeDiscountSpecInnerServiceSMOImpl;

    @Autowired
    private IPayFeeDetailDiscountInnerServiceSMO payFeeDetailDiscountInnerServiceSMOImpl;

    /**
     * @param feeDiscountDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(FeeDiscountDto feeDiscountDto) {


        int count = feeDiscountInnerServiceSMOImpl.queryFeeDiscountsCount(feeDiscountDto);

        List<FeeDiscountDto> feeDiscountDtos = null;
        if (count > 0) {
            feeDiscountDtos = feeDiscountInnerServiceSMOImpl.queryFeeDiscounts(feeDiscountDto);
            freshDiscountSpec(feeDiscountDtos);
        } else {
            feeDiscountDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) feeDiscountDto.getRow()), count, feeDiscountDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    @Override
    public ResponseEntity<String> getFeeDetailDiscount(PayFeeDetailDiscountDto payFeeDetailDiscountDto) {

        int count = payFeeDetailDiscountInnerServiceSMOImpl.queryPayFeeDetailDiscountsCount(payFeeDetailDiscountDto);

        List<PayFeeDetailDiscountDto> feeDetailDiscountDtos = null;
        if (count > 0) {
            feeDetailDiscountDtos = payFeeDetailDiscountInnerServiceSMOImpl.queryPayFeeDetailDiscounts(payFeeDetailDiscountDto);
            freshDiscountDetailSpec(feeDetailDiscountDtos);
        } else {
            feeDetailDiscountDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) payFeeDetailDiscountDto.getRow()), count, feeDetailDiscountDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    private void freshDiscountDetailSpec(List<PayFeeDetailDiscountDto> feeDetailDiscountDtos) {

        if (feeDetailDiscountDtos == null || feeDetailDiscountDtos.size() < 1) {
            return;
        }

        List<String> discountIds = new ArrayList<>();
        for (PayFeeDetailDiscountDto feeDetailDiscountDto : feeDetailDiscountDtos) {
            discountIds.add(feeDetailDiscountDto.getDiscountId());
        }

        FeeDiscountSpecDto tmpFeeDiscountSpecDto = new FeeDiscountSpecDto();

        tmpFeeDiscountSpecDto.setDiscountIds(discountIds.toArray(new String[discountIds.size()]));
        tmpFeeDiscountSpecDto.setCommunityId(feeDetailDiscountDtos.get(0).getCommunityId());

        List<FeeDiscountSpecDto> feeDiscountSpecDtos = feeDiscountSpecInnerServiceSMOImpl.queryFeeDiscountSpecs(tmpFeeDiscountSpecDto);

        if (feeDiscountSpecDtos == null || feeDiscountSpecDtos.size() < 1) {
            return;
        }
        List<FeeDiscountSpecDto> tmpSpecs = null;
        for (PayFeeDetailDiscountDto feeDetailDiscountDto : feeDetailDiscountDtos) {
            tmpSpecs = new ArrayList<>();
            for (FeeDiscountSpecDto feeDiscountSpecDto : feeDiscountSpecDtos) {
                if (feeDetailDiscountDto.getDiscountId().equals(feeDiscountSpecDto.getDiscountId())) {
                    tmpSpecs.add(feeDiscountSpecDto);

                }
            }
            feeDetailDiscountDto.setFeeDiscountSpecs(tmpSpecs);
        }
    }

    private void freshDiscountSpec(List<FeeDiscountDto> feeDiscountDtos) {

        if (feeDiscountDtos == null || feeDiscountDtos.size() < 1) {
            return;
        }

        List<String> discountIds = new ArrayList<>();
        for (FeeDiscountDto feeDiscountDto : feeDiscountDtos) {
            discountIds.add(feeDiscountDto.getDiscountId());
        }

        FeeDiscountSpecDto tmpFeeDiscountSpecDto = new FeeDiscountSpecDto();

        tmpFeeDiscountSpecDto.setDiscountIds(discountIds.toArray(new String[discountIds.size()]));
        tmpFeeDiscountSpecDto.setCommunityId(feeDiscountDtos.get(0).getCommunityId());

        List<FeeDiscountSpecDto> feeDiscountSpecDtos = feeDiscountSpecInnerServiceSMOImpl.queryFeeDiscountSpecs(tmpFeeDiscountSpecDto);

        if (feeDiscountSpecDtos == null || feeDiscountSpecDtos.size() < 1) {
            return;
        }
        List<FeeDiscountSpecDto> tmpSpecs = null;
        for (FeeDiscountDto feeDiscountDto : feeDiscountDtos) {
            tmpSpecs = new ArrayList<>();
            for (FeeDiscountSpecDto feeDiscountSpecDto : feeDiscountSpecDtos) {
                if (feeDiscountDto.getDiscountId().equals(feeDiscountSpecDto.getDiscountId())) {
                    tmpSpecs.add(feeDiscountSpecDto);

                }
            }
            feeDiscountDto.setFeeDiscountSpecs(tmpSpecs);
        }
    }

}
