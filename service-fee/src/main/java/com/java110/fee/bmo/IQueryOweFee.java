package com.java110.fee.bmo;

import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeDto;
import org.springframework.http.ResponseEntity;

public interface IQueryOweFee {

    /**
     * 查询费用
     * @param feeDto
     * @return
     */
    ResponseEntity<String> query(FeeDto feeDto);

    /**
     * 查询费用
     * @param feeDto
     * @return
     */
    ResponseEntity<String> queryAllOwneFee(FeeDto feeDto);
}
