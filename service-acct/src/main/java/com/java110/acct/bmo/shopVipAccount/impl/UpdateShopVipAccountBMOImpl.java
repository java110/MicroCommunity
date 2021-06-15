package com.java110.acct.bmo.shopVipAccount.impl;

import com.java110.acct.bmo.shopVipAccount.IUpdateShopVipAccountBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.acct.IShopVipAccountInnerServiceSMO;
import com.java110.po.shopVipAccount.ShopVipAccountPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateShopVipAccountBMOImpl")
public class UpdateShopVipAccountBMOImpl implements IUpdateShopVipAccountBMO {

    @Autowired
    private IShopVipAccountInnerServiceSMO shopVipAccountInnerServiceSMOImpl;

    /**
     * @param shopVipAccountPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(ShopVipAccountPo shopVipAccountPo) {

        int flag = shopVipAccountInnerServiceSMOImpl.updateShopVipAccount(shopVipAccountPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
