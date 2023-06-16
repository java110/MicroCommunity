package com.java110.acct.bmo.systemGoldSetting;
import com.java110.po.system.SystemGoldSettingPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteSystemGoldSettingBMO {


    /**
     * 修改金币设置
     * add by wuxw
     * @param systemGoldSettingPo
     * @return
     */
    ResponseEntity<String> delete(SystemGoldSettingPo systemGoldSettingPo);


}
