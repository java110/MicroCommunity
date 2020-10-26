package com.java110.goods.bmo.groupBuyProduct.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.goods.bmo.groupBuyProduct.IUpdateGroupBuyProductBMO;
import com.java110.intf.goods.IGroupBuyProductInnerServiceSMO;
import com.java110.intf.goods.IGroupBuyProductSpecInnerServiceSMO;
import com.java110.po.groupBuyProduct.GroupBuyProductPo;
import com.java110.po.groupBuyProductSpec.GroupBuyProductSpecPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("updateGroupBuyProductBMOImpl")
public class UpdateGroupBuyProductBMOImpl implements IUpdateGroupBuyProductBMO {

    @Autowired
    private IGroupBuyProductInnerServiceSMO groupBuyProductInnerServiceSMOImpl;

    @Autowired
    private IGroupBuyProductSpecInnerServiceSMO groupBuyProductSpecInnerServiceSMOImpl;

    /**
     * @param groupBuyProductPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(GroupBuyProductPo groupBuyProductPo, List<GroupBuyProductSpecPo> groupBuyProductSpecPos) {

        int flag = groupBuyProductInnerServiceSMOImpl.updateGroupBuyProduct(groupBuyProductPo);

        if (flag < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
        }

        for (GroupBuyProductSpecPo groupBuyProductSpecPo : groupBuyProductSpecPos) {
            flag = groupBuyProductSpecInnerServiceSMOImpl.updateGroupBuyProductSpec(groupBuyProductSpecPo);
            if (flag < 1) {
                return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
            }
        }
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");

    }

}
