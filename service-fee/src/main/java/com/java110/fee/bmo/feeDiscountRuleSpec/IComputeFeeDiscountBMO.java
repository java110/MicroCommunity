package com.java110.fee.bmo.feeDiscountRuleSpec;
import com.java110.dto.feeDiscountRuleSpec.FeeDiscountRuleSpecDto;
import org.springframework.http.ResponseEntity;

public interface IComputeFeeDiscountBMO {


    /**
     * 查询折扣规则配置
     * add by wuxw
     * @return
     */
    ResponseEntity<String> compute(String feeId,String communityId,double cycles,int page,int row );


}
