package com.java110.fee.bmo;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.fee.FeeAttrDto;
import org.springframework.http.ResponseEntity;

public interface IQueryFeeByAttr {

    /**
     * 查询费用
     * @param feeAttrDto
     * @return
     */
    ResponseEntity<String> query(FeeAttrDto feeAttrDto);
}
