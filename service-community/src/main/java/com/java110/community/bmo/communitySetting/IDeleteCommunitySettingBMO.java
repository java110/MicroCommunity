package com.java110.community.bmo.communitySetting;
import com.java110.po.community.CommunitySettingPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteCommunitySettingBMO {


    /**
     * 修改小区相关设置
     * add by wuxw
     * @param communitySettingPo
     * @return
     */
    ResponseEntity<String> delete(CommunitySettingPo communitySettingPo);


}
