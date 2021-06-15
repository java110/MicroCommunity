package com.java110.goods.bmo.groupBuySetting;
import com.java110.dto.groupBuySetting.GroupBuySettingDto;
import org.springframework.http.ResponseEntity;
public interface IGetGroupBuySettingBMO {


    /**
     * 查询拼团设置
     * add by wuxw
     * @param  groupBuySettingDto
     * @return
     */
    ResponseEntity<String> get(GroupBuySettingDto groupBuySettingDto);


}
