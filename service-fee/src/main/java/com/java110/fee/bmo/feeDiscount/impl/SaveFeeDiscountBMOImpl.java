package com.java110.fee.bmo.feeDiscount.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.fee.bmo.feeDiscount.ISaveFeeDiscountBMO;
import com.java110.intf.fee.IFeeDiscountInnerServiceSMO;
import com.java110.intf.fee.IFeeDiscountSpecInnerServiceSMO;
import com.java110.po.feeDiscount.FeeDiscountPo;
import com.java110.po.feeDiscountSpec.FeeDiscountSpecPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveFeeDiscountBMOImpl")
public class SaveFeeDiscountBMOImpl implements ISaveFeeDiscountBMO {

    @Autowired
    private IFeeDiscountInnerServiceSMO feeDiscountInnerServiceSMOImpl;

    @Autowired
    private IFeeDiscountSpecInnerServiceSMO feeDiscountSpecInnerServiceSMOImpl;

    /**
     * 添加小区信息
     * <p>
     * {
     * "discountId": "",
     * "discountName": "物业费减免",
     * "discountType": "1001",
     * "ruleId": "102020002",
     * "discountDesc": "",
     * "feeDiscountRuleSpecs": [{
     * "page": -1,
     * "records": 0,
     * "remark": "必填，请填写减免金额（元）",
     * "row": 0,
     * "ruleId": "102020002",
     * "specId": "89002020980004",
     * "specName": "减免金额",
     * "statusCd": "0",
     * "total": 0,
     * "specValue": "100"
     * }, {
     * "page": -1,
     * "records": 0,
     * "remark": "必填，请填写至少缴费月份",
     * "row": 0,
     * "ruleId": "102020002",
     * "specId": "89002020980003",
     * "specName": "月份",
     * "statusCd": "0",
     * "total": 0,
     * "specValue": "12"
     * }],
     * "communityId": "7020181217000001"
     * }
     *
     * @param feeDiscountPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(FeeDiscountPo feeDiscountPo, JSONArray feeDiscountRuleSpecs) {

        feeDiscountPo.setDiscountId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_discountId));
        int flag = feeDiscountInnerServiceSMOImpl.saveFeeDiscount(feeDiscountPo);

        if (flag < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
        }

        if (feeDiscountRuleSpecs == null || feeDiscountRuleSpecs.size() < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }
        JSONObject specObj = null;
        for (int specIndex = 0; specIndex < feeDiscountRuleSpecs.size(); specIndex++) {
            specObj = feeDiscountRuleSpecs.getJSONObject(specIndex);
            FeeDiscountSpecPo feeDiscountSpecPo = new FeeDiscountSpecPo();
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
