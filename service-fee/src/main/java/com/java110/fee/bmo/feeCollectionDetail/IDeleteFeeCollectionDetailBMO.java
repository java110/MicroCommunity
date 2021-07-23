package com.java110.fee.bmo.feeCollectionDetail;
import com.java110.po.feeCollectionDetail.FeeCollectionDetailPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteFeeCollectionDetailBMO {


    /**
     * 修改催缴单
     * add by wuxw
     * @param feeCollectionDetailPo
     * @return
     */
    ResponseEntity<String> delete(FeeCollectionDetailPo feeCollectionDetailPo);


}
