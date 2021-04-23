package com.java110.fee.bmo.payFeeDetail;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.ResponseEntity;

public interface IImportPayFeeBMODetail {


    /**
     * 导入费用历史
     * @return
     */
    public ResponseEntity<String> importPayFeeDetail(JSONObject reqJson);
}
