package com.java110.fee.bmo.feeReceiptDetail.impl;

import com.java110.dto.fee.FeeDto;
import com.java110.dto.fee.FeeReceiptDetailDto;
import com.java110.dto.fee.FeeReceiptDto;
import com.java110.dto.payFee.PayFeeDetailDiscountDto;
import com.java110.fee.bmo.feeReceiptDetail.IGetFeeReceiptDetailBMO;
import com.java110.intf.fee.IFeeReceiptDetailInnerServiceSMO;
import com.java110.intf.fee.IPayFeeDetailDiscountInnerServiceSMO;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

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
    public ResponseEntity<String> get(FeeReceiptDetailDto feeReceiptDetailDto, String mergeFee) {


        int count = feeReceiptDetailInnerServiceSMOImpl.queryFeeReceiptDetailsCount(feeReceiptDetailDto);

        List<FeeReceiptDetailDto> feeReceiptDetailDtos = null;
        if (count > 0) {
            feeReceiptDetailDtos = feeReceiptDetailInnerServiceSMOImpl.queryFeeReceiptDetails(feeReceiptDetailDto);

        } else {
            feeReceiptDetailDtos = new ArrayList<>();
        }

        //todo 计算优惠金额
        computeDiscountFee(feeReceiptDetailDtos);

        //todo 合并一次性费用，按费用项名称和单价合并
        feeReceiptDetailDtos = mergeOnceFee(feeReceiptDetailDtos, mergeFee);

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) feeReceiptDetailDto.getRow()), count, feeReceiptDetailDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    private void computeDiscountFee(List<FeeReceiptDetailDto> feeReceiptDetailDtos) {

        if (feeReceiptDetailDtos == null || feeReceiptDetailDtos.size() < 1) {
            return;
        }

        List<String> detailIds = new ArrayList<>();
        for (FeeReceiptDetailDto feeReceiptDetailDto : feeReceiptDetailDtos) {
            detailIds.add(feeReceiptDetailDto.getDetailId());
        }

        PayFeeDetailDiscountDto payFeeDetailDiscountDto = new PayFeeDetailDiscountDto();
        payFeeDetailDiscountDto.setCommunityId(feeReceiptDetailDtos.get(0).getCommunityId());
        payFeeDetailDiscountDto.setDetailIds(detailIds.toArray(new String[detailIds.size()]));

        List<PayFeeDetailDiscountDto> payFeeDetailDiscountDtos = payFeeDetailDiscountInnerServiceSMOImpl.computeDiscountFee(payFeeDetailDiscountDto);

        for (FeeReceiptDetailDto feeReceiptDetailDto : feeReceiptDetailDtos) {
            for (PayFeeDetailDiscountDto tmpPayFeeDetailDiscountDto : payFeeDetailDiscountDtos) {
                if (!feeReceiptDetailDto.getDetailId().equals(tmpPayFeeDetailDiscountDto.getDetailId())) {
                    continue;
                }
                feeReceiptDetailDto.setDiscountPrice(tmpPayFeeDetailDiscountDto.getDiscountPrice());
            }
        }
    }


    private List<FeeReceiptDetailDto> mergeOnceFee(List<FeeReceiptDetailDto> feeReceiptDetailDtos, String mergeFee) {
        if (ListUtil.isNull(feeReceiptDetailDtos)) {
            return feeReceiptDetailDtos;
        }

        if (!"ON".equals(mergeFee)) {
            return feeReceiptDetailDtos;
        }


        //todo cycle fee
        List<FeeReceiptDetailDto> cycleFeeReceiptDetailDtos = new ArrayList<>();
        List<FeeReceiptDetailDto> onceFeeReceiptDetailDtos = new ArrayList<>();

//        for (FeeReceiptDetailDto feeReceiptDetailDto : feeReceiptDetailDtos) {
//            if (FeeDto.FEE_FLAG_ONCE.equals(feeReceiptDetailDto.getFeeFlag())) {
//                onceFeeReceiptDetailDtos.add(feeReceiptDetailDto);
//                continue;
//            }
//            cycleFeeReceiptDetailDtos.add(feeReceiptDetailDto);
//        }
        for (FeeReceiptDetailDto feeReceiptDetailDto : feeReceiptDetailDtos) {
            onceFeeReceiptDetailDtos.add(feeReceiptDetailDto);
        }
        // todo not exists once fee
        if (ListUtil.isNull(onceFeeReceiptDetailDtos)) {
            return feeReceiptDetailDtos;
        }

        //todo order by endTime asc
        Collections.sort(onceFeeReceiptDetailDtos, new Comparator<FeeReceiptDetailDto>() {
            @Override
            public int compare(FeeReceiptDetailDto receiptDto1, FeeReceiptDetailDto receiptDto2) {
                Date endTime1 = DateUtil.getDateFromStringA(receiptDto1.getEndTime());
                Date endTime2 = DateUtil.getDateFromStringA(receiptDto2.getEndTime());
                return Long.compare(endTime1.getTime(), endTime2.getTime());
            }
        });
        List<FeeReceiptDetailDto> nOnceFeeReceiptDetailDtos = new ArrayList<>();

        for (FeeReceiptDetailDto feeReceiptDetailDto : onceFeeReceiptDetailDtos) {
            doMergeOnceFee(nOnceFeeReceiptDetailDtos, feeReceiptDetailDto);
        }


        cycleFeeReceiptDetailDtos.addAll(nOnceFeeReceiptDetailDtos);

        return cycleFeeReceiptDetailDtos;

    }

    /**
     * 合并一次性费用
     *
     * @param nOnceFeeReceiptDetailDtos
     * @param feeReceiptDetailDto
     */
    private void doMergeOnceFee(List<FeeReceiptDetailDto> nOnceFeeReceiptDetailDtos, FeeReceiptDetailDto feeReceiptDetailDto) {
        if (ListUtil.isNull(nOnceFeeReceiptDetailDtos)) {
            nOnceFeeReceiptDetailDtos.add(feeReceiptDetailDto);
            return;
        }
        FeeReceiptDetailDto nOnceFeeReceiptDetailDto = null;
        for (FeeReceiptDetailDto nFeeReceiptDetailDto : nOnceFeeReceiptDetailDtos) {
            //todo 如果名称不一样
            if (!nFeeReceiptDetailDto.getFeeName().equals(feeReceiptDetailDto.getFeeName())) {
                continue;
            }

            if (!nFeeReceiptDetailDto.getSquarePrice().equals(feeReceiptDetailDto.getSquarePrice())) {
                continue;
            }

            if (!nFeeReceiptDetailDto.getPayerObjId().equals(feeReceiptDetailDto.getPayerObjId())) {
                continue;
            }


            Date endTime = DateUtil.getDateFromStringB(nFeeReceiptDetailDto.getEndTime());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(endTime);
            calendar.add(Calendar.SECOND,1);
            endTime = calendar.getTime();
            Date startTime = DateUtil.getDateFromStringB(feeReceiptDetailDto.getStartTime());
            if (endTime.getTime() != startTime.getTime()) {
                continue;
            }

            nOnceFeeReceiptDetailDto = nFeeReceiptDetailDto;
        }

        if (nOnceFeeReceiptDetailDto == null) {
            nOnceFeeReceiptDetailDtos.add(feeReceiptDetailDto);
            return;
        }

        nOnceFeeReceiptDetailDto.setEndTime(feeReceiptDetailDto.getEndTime());
        BigDecimal amount = new BigDecimal(nOnceFeeReceiptDetailDto.getAmount());
        amount = amount.add(new BigDecimal(feeReceiptDetailDto.getAmount()));
        nOnceFeeReceiptDetailDto.setAmount(amount.doubleValue() + "");
        nOnceFeeReceiptDetailDto.setCurDegrees(feeReceiptDetailDto.getCurDegrees());
    }

}
