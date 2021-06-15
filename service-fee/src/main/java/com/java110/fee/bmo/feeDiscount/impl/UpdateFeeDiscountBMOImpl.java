package com.java110.fee.bmo.feeDiscount.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.fee.bmo.feeDiscount.IUpdateFeeDiscountBMO;
import com.java110.intf.fee.IFeeDiscountInnerServiceSMO;
import com.java110.intf.fee.IFeeDiscountSpecInnerServiceSMO;
import com.java110.po.feeDiscount.FeeDiscountPo;
import com.java110.po.feeDiscountSpec.FeeDiscountSpecPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateFeeDiscountBMOImpl")
public class UpdateFeeDiscountBMOImpl implements IUpdateFeeDiscountBMO {

    @Autowired
    private IFeeDiscountInnerServiceSMO feeDiscountInnerServiceSMOImpl;

    @Autowired
    private IFeeDiscountSpecInnerServiceSMO feeDiscountSpecInnerServiceSMOImpl;

    /**
     * @param feeDiscountPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(FeeDiscountPo feeDiscountPo, JSONArray feeDiscountRuleSpecs) {

        int flag = feeDiscountInnerServiceSMOImpl.updateFeeDiscount(feeDiscountPo);

        if (flag < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
        }

        if (feeDiscountRuleSpecs == null || feeDiscountRuleSpecs.size() < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }
        FeeDiscountSpecPo feeDiscountSpecPo = null;
        //删除所有
        feeDiscountSpecPo = new FeeDiscountSpecPo();
        feeDiscountSpecPo.setCommunityId(feeDiscountPo.getCommunityId());
        feeDiscountSpecPo.setDiscountId(feeDiscountPo.getDiscountId());
        feeDiscountSpecInnerServiceSMOImpl.deleteFeeDiscountSpec(feeDiscountSpecPo);

        JSONObject specObj = null;
        for (int specIndex = 0; specIndex < feeDiscountRuleSpecs.size(); specIndex++) {
            specObj = feeDiscountRuleSpecs.getJSONObject(specIndex);
            feeDiscountSpecPo = new FeeDiscountSpecPo();
            feeDiscountSpecPo.setCommunityId(feeDiscountPo.getCommunityId());
            feeDiscountSpecPo.setDiscountId(feeDiscountPo.getDiscountId());
            feeDiscountSpecPo.setSpecId(specObj.getString("specId"));
            feeDiscountSpecPo.setSpecName(specObj.getString("specName"));
            feeDiscountSpecPo.setSpecValue(specObj.getString("specValue"));
            feeDiscountSpecPo.setDiscountSpecId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_specId));
            flag = feeDiscountSpecInnerServiceSMOImpl.saveFeeDiscountSpec(feeDiscountSpecPo);
            if (flag < 1) {
                throw new IllegalArgumentException("保存 折扣规格失败");
            }
        }


        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
    }

}
