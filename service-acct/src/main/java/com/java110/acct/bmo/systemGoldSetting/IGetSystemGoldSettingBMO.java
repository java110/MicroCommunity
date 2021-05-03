package com.java110.acct.bmo.systemGoldSetting;
import com.java110.dto.systemGoldSetting.SystemGoldSettingDto;
import org.springframework.http.ResponseEntity;
public interface IGetSystemGoldSettingBMO {


    /**
     * 查询金币设置
     * add by wuxw
     * @param  systemGoldSettingDto
     * @return
     */
    ResponseEntity<String> get(SystemGoldSettingDto systemGoldSettingDto);


}
