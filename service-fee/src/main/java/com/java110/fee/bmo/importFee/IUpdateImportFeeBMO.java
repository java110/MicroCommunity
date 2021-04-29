package com.java110.fee.bmo.importFee;
import com.java110.po.importFee.ImportFeePo;
import org.springframework.http.ResponseEntity;

public interface IUpdateImportFeeBMO {


    /**
     * 修改费用导入
     * add by wuxw
     * @param importFeePo
     * @return
     */
    ResponseEntity<String> update(ImportFeePo importFeePo);


}
