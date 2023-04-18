package com.java110.community.bmo.communitySetting;
import com.java110.dto.community.CommunitySettingDto;
import org.springframework.http.ResponseEntity;
public interface IGetCommunitySettingBMO {


    /**
     * 查询小区相关设置
     * add by wuxw
     * @param  communitySettingDto
     * @return
     */
    ResponseEntity<String> get(CommunitySettingDto communitySettingDto);


}
