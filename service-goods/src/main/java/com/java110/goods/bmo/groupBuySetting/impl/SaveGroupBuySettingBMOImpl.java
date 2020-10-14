package com.java110.goods.bmo.groupBuySetting.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.goods.bmo.groupBuySetting.ISaveGroupBuySettingBMO;
import com.java110.intf.IGroupBuySettingInnerServiceSMO;
import com.java110.po.groupBuySetting.GroupBuySettingPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveGroupBuySettingBMOImpl")
public class SaveGroupBuySettingBMOImpl implements ISaveGroupBuySettingBMO {

    @Autowired
    private IGroupBuySettingInnerServiceSMO groupBuySettingInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param groupBuySettingPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(GroupBuySettingPo groupBuySettingPo) {

        groupBuySettingPo.setSettingId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_settingId));
        int flag = groupBuySettingInnerServiceSMOImpl.saveGroupBuySetting(groupBuySettingPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
