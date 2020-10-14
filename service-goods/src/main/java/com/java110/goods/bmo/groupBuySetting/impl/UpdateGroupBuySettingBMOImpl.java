package com.java110.goods.bmo.groupBuySetting.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.goods.bmo.groupBuySetting.IUpdateGroupBuySettingBMO;
import com.java110.intf.IGroupBuySettingInnerServiceSMO;
import com.java110.po.groupBuySetting.GroupBuySettingPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateGroupBuySettingBMOImpl")
public class UpdateGroupBuySettingBMOImpl implements IUpdateGroupBuySettingBMO {

    @Autowired
    private IGroupBuySettingInnerServiceSMO groupBuySettingInnerServiceSMOImpl;

    /**
     * @param groupBuySettingPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(GroupBuySettingPo groupBuySettingPo) {

        int flag = groupBuySettingInnerServiceSMOImpl.updateGroupBuySetting(groupBuySettingPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
