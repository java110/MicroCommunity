package com.java110.community.bmo.communitySetting;

import com.java110.po.communitySetting.CommunitySettingPo;
import org.springframework.http.ResponseEntity;
public interface ISaveCommunitySettingBMO {


    /**
     * 添加小区相关设置
     * add by wuxw
     * @param communitySettingPo
     * @return
     */
    ResponseEntity<String> save(CommunitySettingPo communitySettingPo);


}
