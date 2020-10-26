package com.java110.goods.bmo.groupBuy.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.goods.bmo.groupBuy.ISaveGroupBuyBMO;
import com.java110.intf.goods.IGroupBuyInnerServiceSMO;
import com.java110.po.groupBuy.GroupBuyPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveGroupBuyBMOImpl")
public class SaveGroupBuyBMOImpl implements ISaveGroupBuyBMO {

    @Autowired
    private IGroupBuyInnerServiceSMO groupBuyInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param groupBuyPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(GroupBuyPo groupBuyPo) {

        groupBuyPo.setBuyId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_buyId));
        int flag = groupBuyInnerServiceSMOImpl.saveGroupBuy(groupBuyPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
