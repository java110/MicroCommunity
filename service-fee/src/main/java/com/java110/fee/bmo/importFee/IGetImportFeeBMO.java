package com.java110.fee.bmo.importFee;
import com.java110.dto.importFee.ImportFeeDto;
import org.springframework.http.ResponseEntity;
public interface IGetImportFeeBMO {


    /**
     * 查询费用导入
     * add by wuxw
     * @param  importFeeDto
     * @return
     */
    ResponseEntity<String> get(ImportFeeDto importFeeDto);


}
