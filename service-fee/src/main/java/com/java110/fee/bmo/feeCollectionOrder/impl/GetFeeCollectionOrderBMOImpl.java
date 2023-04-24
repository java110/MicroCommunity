package com.java110.fee.bmo.feeCollectionOrder.impl;

import com.java110.dto.fee.FeeCollectionOrderDto;
import com.java110.fee.bmo.feeCollectionOrder.IGetFeeCollectionOrderBMO;
import com.java110.intf.fee.IFeeCollectionOrderInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getFeeCollectionOrderBMOImpl")
public class GetFeeCollectionOrderBMOImpl implements IGetFeeCollectionOrderBMO {

    @Autowired
    private IFeeCollectionOrderInnerServiceSMO feeCollectionOrderInnerServiceSMOImpl;

    /**
     * @param feeCollectionOrderDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(FeeCollectionOrderDto feeCollectionOrderDto) {


        int count = feeCollectionOrderInnerServiceSMOImpl.queryFeeCollectionOrdersCount(feeCollectionOrderDto);

        List<FeeCollectionOrderDto> feeCollectionOrderDtos = null;
        if (count > 0) {
            feeCollectionOrderDtos = feeCollectionOrderInnerServiceSMOImpl.queryFeeCollectionOrders(feeCollectionOrderDto);
        } else {
            feeCollectionOrderDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) feeCollectionOrderDto.getRow()), count, feeCollectionOrderDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
