package com.java110.fee.bmo.feePrintSpec;

import com.java110.dto.feePrintPage.FeePrintSpecDto;
import org.springframework.http.ResponseEntity;

public interface IGetFeePrintSpecBMO {


    /**
     * 查询打印说明
     * add by wuxw
     *
     * @param feePrintSpecDto
     * @return
     */
    ResponseEntity<String> get(FeePrintSpecDto feePrintSpecDto);


}
