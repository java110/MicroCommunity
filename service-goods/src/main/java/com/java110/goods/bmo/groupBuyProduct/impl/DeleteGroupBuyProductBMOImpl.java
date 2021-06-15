package com.java110.goods.bmo.groupBuyProduct.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.goods.bmo.groupBuyProduct.IDeleteGroupBuyProductBMO;
import com.java110.intf.goods.IGroupBuyProductInnerServiceSMO;
import com.java110.po.groupBuyProduct.GroupBuyProductPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteGroupBuyProductBMOImpl")
public class DeleteGroupBuyProductBMOImpl implements IDeleteGroupBuyProductBMO {

    @Autowired
    private IGroupBuyProductInnerServiceSMO groupBuyProductInnerServiceSMOImpl;

    /**
     * @param groupBuyProductPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(GroupBuyProductPo groupBuyProductPo) {

        int flag = groupBuyProductInnerServiceSMOImpl.deleteGroupBuyProduct(groupBuyProductPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
