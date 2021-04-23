package com.java110.goods.bmo.groupBuySetting.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.goods.bmo.groupBuySetting.IDeleteGroupBuySettingBMO;
import com.java110.intf.goods.IGroupBuySettingInnerServiceSMO;
import com.java110.po.groupBuySetting.GroupBuySettingPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteGroupBuySettingBMOImpl")
public class DeleteGroupBuySettingBMOImpl implements IDeleteGroupBuySettingBMO {

    @Autowired
    private IGroupBuySettingInnerServiceSMO groupBuySettingInnerServiceSMOImpl;

    /**
     * @param groupBuySettingPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(GroupBuySettingPo groupBuySettingPo) {

        int flag = groupBuySettingInnerServiceSMOImpl.deleteGroupBuySetting(groupBuySettingPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
