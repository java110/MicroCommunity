package com.java110.goods.bmo.groupBuyProductSpec;
import com.java110.dto.groupBuyProductSpec.GroupBuyProductSpecDto;
import org.springframework.http.ResponseEntity;
public interface IGetGroupBuyProductSpecBMO {


    /**
     * 查询拼团产品规格
     * add by wuxw
     * @param  groupBuyProductSpecDto
     * @return
     */
    ResponseEntity<String> get(GroupBuyProductSpecDto groupBuyProductSpecDto);


}
