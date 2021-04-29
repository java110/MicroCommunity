package com.java110.fee.bmo.importFeeDetail;
import com.java110.dto.importFee.ImportFeeDetailDto;
import org.springframework.http.ResponseEntity;
public interface IGetImportFeeDetailBMO {


    /**
     * 查询费用导入明细
     * add by wuxw
     * @param  importFeeDetailDto
     * @return
     */
    ResponseEntity<String> get(ImportFeeDetailDto importFeeDetailDto);


}
