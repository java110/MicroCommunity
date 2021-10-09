package com.java110.community.bmo.communitySetting.impl;

import com.java110.community.bmo.communitySetting.ISaveCommunitySettingBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.CommunitySettingFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.community.ICommunitySettingInnerServiceSMO;
import com.java110.po.communitySetting.CommunitySettingPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service("saveCommunitySettingBMOImpl")
public class SaveCommunitySettingBMOImpl implements ISaveCommunitySettingBMO {

    @Autowired
    private ICommunitySettingInnerServiceSMO communitySettingInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param communitySettingPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(CommunitySettingPo communitySettingPo) {

        communitySettingPo.setCsId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_csId));
        int flag = communitySettingInnerServiceSMOImpl.saveCommunitySetting(communitySettingPo);

        if (flag < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
        }

        //将结果写入缓存
        CommunitySettingFactory.getCommunitySettingFromDb(communitySettingPo.getCommunityId(), communitySettingPo.getSettingKey());


        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
    }

}
