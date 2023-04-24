package com.java110.fee.bmo.feeCollectionDetail;
import com.java110.dto.fee.FeeCollectionDetailDto;
import org.springframework.http.ResponseEntity;
public interface IGetFeeCollectionDetailBMO {


    /**
     * 查询催缴单
     * add by wuxw
     * @param  feeCollectionDetailDto
     * @return
     */
    ResponseEntity<String> get(FeeCollectionDetailDto feeCollectionDetailDto);


}
