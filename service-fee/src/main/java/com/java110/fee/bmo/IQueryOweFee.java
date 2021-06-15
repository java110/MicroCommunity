package com.java110.fee.bmo;

import com.java110.dto.fee.FeeDto;
import org.springframework.http.ResponseEntity;

public interface IQueryOweFee {

    /**
     * 查询费用
     *
     * @param feeDto
     * @return
     */
    ResponseEntity<String> query(FeeDto feeDto);

    /**
     * 查询费用
     *
     * @param feeDto
     * @return
     */
    ResponseEntity<String> queryAllOwneFee(FeeDto feeDto);

    /**
     * 查询费用作用对象 接口
     *
     * @param feeDto
     * @return
     */
    ResponseEntity<String> listFeeObj(FeeDto feeDto);

    /**
     * 查询所有房屋的欠费
     * @param feeDto
     * @return
     */
    ResponseEntity<String> querys(FeeDto feeDto);
}
