package com.java110.goods.bmo.groupBuyProduct.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.goods.bmo.groupBuyProduct.IUpdateGroupBuyProductBMO;
import com.java110.intf.IGroupBuyProductInnerServiceSMO;
import com.java110.po.groupBuyProduct.GroupBuyProductPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateGroupBuyProductBMOImpl")
public class UpdateGroupBuyProductBMOImpl implements IUpdateGroupBuyProductBMO {

    @Autowired
    private IGroupBuyProductInnerServiceSMO groupBuyProductInnerServiceSMOImpl;

    /**
     * @param groupBuyProductPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(GroupBuyProductPo groupBuyProductPo) {

        int flag = groupBuyProductInnerServiceSMOImpl.updateGroupBuyProduct(groupBuyProductPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
