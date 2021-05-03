package com.java110.acct.bmo.systemGoldSetting;
import com.java110.po.systemGoldSetting.SystemGoldSettingPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateSystemGoldSettingBMO {


    /**
     * 修改金币设置
     * add by wuxw
     * @param systemGoldSettingPo
     * @return
     */
    ResponseEntity<String> update(SystemGoldSettingPo systemGoldSettingPo);


}
