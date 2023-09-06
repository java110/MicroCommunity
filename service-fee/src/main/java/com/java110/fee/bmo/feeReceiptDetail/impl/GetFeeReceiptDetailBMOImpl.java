package com.java110.fee.bmo.feeReceiptDetail.impl;

import com.java110.dto.fee.FeeReceiptDetailDto;
import com.java110.dto.payFee.PayFeeDetailDiscountDto;
import com.java110.fee.bmo.feeReceiptDetail.IGetFeeReceiptDetailBMO;
import com.java110.intf.fee.IFeeReceiptDetailInnerServiceSMO;
import com.java110.intf.fee.IPayFeeDetailDiscountInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getFeeReceiptDetailBMOImpl")
public class GetFeeReceiptDetailBMOImpl implements IGetFeeReceiptDetailBMO {

    @Autowired
    private IFeeReceiptDetailInnerServiceSMO feeReceiptDetailInnerServiceSMOImpl;

    @Autowired
    private IPayFeeDetailDiscountInnerServiceSMO payFeeDetailDiscountInnerServiceSMOImpl;

    /**
     * @param feeReceiptDetailDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(FeeReceiptDetailDto feeReceiptDetailDto) {


        int count = feeReceiptDetailInnerServiceSMOImpl.queryFeeReceiptDetailsCount(feeReceiptDetailDto);

        List<FeeReceiptDetailDto> feeReceiptDetailDtos = null;
        if (count > 0) {
            feeReceiptDetailDtos = feeReceiptDetailInnerServiceSMOImpl.queryFeeReceiptDetails(feeReceiptDetailDto);

        } else {
            feeReceiptDetailDtos = new ArrayList<>();
        }

        //todo 计算优惠金额
        computeDiscountFee(feeReceiptDetailDtos);

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) feeReceiptDetailDto.getRow()), count, feeReceiptDetailDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    private void computeDiscountFee(List<FeeReceiptDetailDto> feeReceiptDetailDtos) {

        if(feeReceiptDetailDtos == null || feeReceiptDetailDtos.size() < 1){
            return ;
        }

        List<String> detailIds = new ArrayList<>();
        for(FeeReceiptDetailDto feeReceiptDetailDto:feeReceiptDetailDtos){
            detailIds.add(feeReceiptDetailDto.getDetailId());
        }

        PayFeeDetailDiscountDto payFeeDetailDiscountDto = new PayFeeDetailDiscountDto();
        payFeeDetailDiscountDto.setCommunityId(feeReceiptDetailDtos.get(0).getCommunityId());
        payFeeDetailDiscountDto.setDetailIds(detailIds.toArray(new String[detailIds.size()]));

        List<PayFeeDetailDiscountDto> payFeeDetailDiscountDtos = payFeeDetailDiscountInnerServiceSMOImpl.computeDiscountFee(payFeeDetailDiscountDto);

        for(FeeReceiptDetailDto feeReceiptDetailDto:feeReceiptDetailDtos){
            for(PayFeeDetailDiscountDto tmpPayFeeDetailDiscountDto:payFeeDetailDiscountDtos){
                if(!feeReceiptDetailDto.getDetailId().equals(tmpPayFeeDetailDiscountDto.getDetailId())){
                    continue;
                }
                feeReceiptDetailDto.setDiscountPrice(tmpPayFeeDetailDiscountDto.getDiscountPrice());
            }
        }
    }

}
