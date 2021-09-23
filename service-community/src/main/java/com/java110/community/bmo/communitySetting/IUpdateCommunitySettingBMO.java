package com.java110.community.bmo.communitySetting;
import com.java110.po.communitySetting.CommunitySettingPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateCommunitySettingBMO {


    /**
     * 修改小区相关设置
     * add by wuxw
     * @param communitySettingPo
     * @return
     */
    ResponseEntity<String> update(CommunitySettingPo communitySettingPo);


}
