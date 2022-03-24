package com.java110.store.bmo.purchase.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Transactional;
import com.java110.dto.purchaseApply.PurchaseApplyDto;
import com.java110.intf.common.IPurchaseApplyUserInnerServiceSMO;
import com.java110.intf.store.IPurchaseApplyInnerServiceSMO;
import com.java110.po.purchase.PurchaseApplyPo;
import com.java110.store.bmo.purchase.IPurchaseApplyBMO;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("purchaseApplyBMOImpl")
public class PurchaseApplyBMOImpl implements IPurchaseApplyBMO {

    @Autowired
    private IPurchaseApplyInnerServiceSMO purchaseApplyInnerServiceSMOImpl;

    @Autowired
    private IPurchaseApplyUserInnerServiceSMO purchaseApplyUserInnerServiceSMOImpl;

    @Override
    @Java110Transactional
    public ResponseEntity<String> apply(PurchaseApplyPo purchaseApplyPo, JSONObject reqJson) {
        int saveFlag = purchaseApplyInnerServiceSMOImpl.savePurchaseApply(purchaseApplyPo);
        if (saveFlag < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "采购申请失败");
        }
        PurchaseApplyDto purchaseApplyDto = BeanConvertUtil.covertBean(purchaseApplyPo, PurchaseApplyDto.class);
        purchaseApplyDto.setCurrentUserId(purchaseApplyPo.getUserId());
        if(!purchaseApplyPo.getWarehousingWay().equals(PurchaseApplyDto.WAREHOUSING_TYPE_DIRECT) && !purchaseApplyPo.getWarehousingWay().equals(PurchaseApplyDto.WAREHOUSING_TYPE_URGENT)){
            purchaseApplyDto.setNextStaffId(reqJson.getString("staffId"));
            purchaseApplyUserInnerServiceSMOImpl.startProcess(purchaseApplyDto);
        }
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "采购申请成功");
    }
}
