package com.java110.fee.bmo.importFee;

import com.java110.po.importFee.ImportFeePo;
import org.springframework.http.ResponseEntity;
public interface ISaveImportFeeBMO {


    /**
     * 添加费用导入
     * add by wuxw
     * @param importFeePo
     * @return
     */
    ResponseEntity<String> save(ImportFeePo importFeePo);


}
