package com.java110.goods.bmo.groupBuyProduct.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.goods.bmo.groupBuyProduct.ISaveGroupBuyProductBMO;
import com.java110.intf.IGroupBuyProductInnerServiceSMO;
import com.java110.po.groupBuyProduct.GroupBuyProductPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveGroupBuyProductBMOImpl")
public class SaveGroupBuyProductBMOImpl implements ISaveGroupBuyProductBMO {

    @Autowired
    private IGroupBuyProductInnerServiceSMO groupBuyProductInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param groupBuyProductPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(GroupBuyProductPo groupBuyProductPo) {

        groupBuyProductPo.setGroupId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_groupId));
        int flag = groupBuyProductInnerServiceSMOImpl.saveGroupBuyProduct(groupBuyProductPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
