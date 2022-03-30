package com.java110.store.bmo.collection.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Transactional;
import com.java110.dto.purchaseApply.PurchaseApplyDto;
import com.java110.intf.common.IGoodCollectionUserInnerServiceSMO;
import com.java110.intf.store.IPurchaseApplyInnerServiceSMO;
import com.java110.po.purchase.PurchaseApplyPo;
import com.java110.store.bmo.collection.IGoodsCollectionBMO;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service("goodsCollectionBMOImpl")
public class GoodsCollectionBMOImpl implements IGoodsCollectionBMO {

    @Autowired
    private IPurchaseApplyInnerServiceSMO purchaseApplyInnerServiceSMOImpl;


    @Autowired
    private IGoodCollectionUserInnerServiceSMO goodCollectionUserInnerServiceSMOImpl;


    @Override
    @Java110Transactional
    public ResponseEntity<String> collection(PurchaseApplyPo purchaseApplyPo, JSONObject reqJson) {

        int saveFlag = purchaseApplyInnerServiceSMOImpl.savePurchaseApply(purchaseApplyPo);

        if (saveFlag < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "物品领用申请失败");
        }

        PurchaseApplyDto purchaseApplyDto = BeanConvertUtil.covertBean(purchaseApplyPo, PurchaseApplyDto.class);
        purchaseApplyDto.setCurrentUserId(purchaseApplyPo.getUserId());
        purchaseApplyDto.setNextStaffId(reqJson.getString("staffId"));
        if (!PurchaseApplyDto.WAREHOUSING_TYPE_DIRECT.equals(purchaseApplyPo.getWarehousingWay())) {
            goodCollectionUserInnerServiceSMOImpl.startProcess(purchaseApplyDto);
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "物品领用成功");
    }
}
