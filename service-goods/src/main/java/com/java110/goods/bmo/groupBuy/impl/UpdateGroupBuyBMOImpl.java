package com.java110.goods.bmo.groupBuy.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.goods.bmo.groupBuy.IUpdateGroupBuyBMO;
import com.java110.intf.goods.IGroupBuyInnerServiceSMO;
import com.java110.po.groupBuy.GroupBuyPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateGroupBuyBMOImpl")
public class UpdateGroupBuyBMOImpl implements IUpdateGroupBuyBMO {

    @Autowired
    private IGroupBuyInnerServiceSMO groupBuyInnerServiceSMOImpl;

    /**
     * @param groupBuyPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(GroupBuyPo groupBuyPo) {

        int flag = groupBuyInnerServiceSMOImpl.updateGroupBuy(groupBuyPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
