package com.java110.acct.bmo.shopVipAccountDetail.impl;

import com.java110.acct.bmo.shopVipAccountDetail.ISaveShopVipAccountDetailBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.acct.IShopVipAccountDetailInnerServiceSMO;
import com.java110.po.shopVipAccountDetail.ShopVipAccountDetailPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveShopVipAccountDetailBMOImpl")
public class SaveShopVipAccountDetailBMOImpl implements ISaveShopVipAccountDetailBMO {

    @Autowired
    private IShopVipAccountDetailInnerServiceSMO shopVipAccountDetailInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param shopVipAccountDetailPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(ShopVipAccountDetailPo shopVipAccountDetailPo) {

        shopVipAccountDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        int flag = shopVipAccountDetailInnerServiceSMOImpl.saveShopVipAccountDetail(shopVipAccountDetailPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
