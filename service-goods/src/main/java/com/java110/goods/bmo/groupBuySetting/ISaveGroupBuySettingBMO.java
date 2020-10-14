package com.java110.goods.bmo.groupBuySetting;

import com.java110.po.groupBuySetting.GroupBuySettingPo;
import org.springframework.http.ResponseEntity;

public interface ISaveGroupBuySettingBMO {


    /**
     * 添加拼团设置
     * add by wuxw
     *
     * @param groupBuySettingPo
     * @return
     */
    ResponseEntity<String> save(GroupBuySettingPo groupBuySettingPo);


}
