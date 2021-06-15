package com.java110.acct.bmo.shopVipAccountDetail.impl;

import com.java110.acct.bmo.shopVipAccountDetail.IDeleteShopVipAccountDetailBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.acct.IShopVipAccountDetailInnerServiceSMO;
import com.java110.po.shopVipAccountDetail.ShopVipAccountDetailPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteShopVipAccountDetailBMOImpl")
public class DeleteShopVipAccountDetailBMOImpl implements IDeleteShopVipAccountDetailBMO {

    @Autowired
    private IShopVipAccountDetailInnerServiceSMO shopVipAccountDetailInnerServiceSMOImpl;

    /**
     * @param shopVipAccountDetailPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(ShopVipAccountDetailPo shopVipAccountDetailPo) {

        int flag = shopVipAccountDetailInnerServiceSMOImpl.deleteShopVipAccountDetail(shopVipAccountDetailPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
