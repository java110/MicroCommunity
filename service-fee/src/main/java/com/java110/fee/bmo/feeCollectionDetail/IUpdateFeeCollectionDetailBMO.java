package com.java110.fee.bmo.feeCollectionDetail;
import com.java110.po.fee.FeeCollectionDetailPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateFeeCollectionDetailBMO {


    /**
     * 修改催缴单
     * add by wuxw
     * @param feeCollectionDetailPo
     * @return
     */
    ResponseEntity<String> update(FeeCollectionDetailPo feeCollectionDetailPo);


}
