package com.java110.fee.bmo.prestoreFee;
import com.java110.po.prestoreFee.PrestoreFeePo;
import org.springframework.http.ResponseEntity;

public interface IUpdatePrestoreFeeBMO {


    /**
     * 修改预存费用
     * add by wuxw
     * @param prestoreFeePo
     * @return
     */
    ResponseEntity<String> update(PrestoreFeePo prestoreFeePo);


}
