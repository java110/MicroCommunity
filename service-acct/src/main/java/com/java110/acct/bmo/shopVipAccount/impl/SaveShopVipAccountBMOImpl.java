package com.java110.acct.bmo.shopVipAccount.impl;

import com.java110.acct.bmo.shopVipAccount.ISaveShopVipAccountBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.acct.IShopVipAccountInnerServiceSMO;
import com.java110.po.shopVipAccount.ShopVipAccountPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveShopVipAccountBMOImpl")
public class SaveShopVipAccountBMOImpl implements ISaveShopVipAccountBMO {

    @Autowired
    private IShopVipAccountInnerServiceSMO shopVipAccountInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param shopVipAccountPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(ShopVipAccountPo shopVipAccountPo) {

        shopVipAccountPo.setVipAcctId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_vipAcctId));
        int flag = shopVipAccountInnerServiceSMOImpl.saveShopVipAccount(shopVipAccountPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
