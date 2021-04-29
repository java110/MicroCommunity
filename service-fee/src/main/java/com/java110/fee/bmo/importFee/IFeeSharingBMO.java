package com.java110.fee.bmo.importFee;
import com.alibaba.fastjson.JSONObject;
import com.java110.dto.importFee.ImportFeeDto;
import org.springframework.http.ResponseEntity;

public interface IFeeSharingBMO {


    /**
     * 查询费用公摊
     * add by wuxw
     * @param  reqJson
     * @return
     */
    ResponseEntity<String> share(JSONObject reqJson);


}
