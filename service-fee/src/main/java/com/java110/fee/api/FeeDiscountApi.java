package com.java110.fee.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.dto.feeDiscount.FeeDiscountDto;
import com.java110.dto.feeDiscountRule.FeeDiscountRuleDto;
import com.java110.dto.feeDiscountRuleSpec.FeeDiscountRuleSpecDto;
import com.java110.fee.bmo.feeDiscount.IDeleteFeeDiscountBMO;
import com.java110.fee.bmo.feeDiscount.IGetFeeDiscountBMO;
import com.java110.fee.bmo.feeDiscount.ISaveFeeDiscountBMO;
import com.java110.fee.bmo.feeDiscount.IUpdateFeeDiscountBMO;
import com.java110.fee.bmo.feeDiscountRule.IGetFeeDiscountRuleBMO;
import com.java110.fee.bmo.feeDiscountRuleSpec.IGetFeeDiscountRuleSpecBMO;
import com.java110.po.feeDiscount.FeeDiscountPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/feeDiscount")
public class FeeDiscountApi {

    @Autowired
    private ISaveFeeDiscountBMO saveFeeDiscountBMOImpl;
    @Autowired
    private IUpdateFeeDiscountBMO updateFeeDiscountBMOImpl;
    @Autowired
    private IDeleteFeeDiscountBMO deleteFeeDiscountBMOImpl;

    @Autowired
    private IGetFeeDiscountBMO getFeeDiscountBMOImpl;

    @Autowired
    private IGetFeeDiscountRuleBMO getFeeDiscountRuleBMOImpl;

    @Autowired
    private IGetFeeDiscountRuleSpecBMO getFeeDiscountRuleSpecBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /feeDiscount/saveFeeDiscount
     * @path /app/feeDiscount/saveFeeDiscount
     */
    @RequestMapping(value = "/saveFeeDiscount", method = RequestMethod.POST)
    public ResponseEntity<String> saveFeeDiscount(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "discountName", "请求报文中未包含discountName");
        Assert.hasKeyAndValue(reqJson, "ruleId", "请求报文中未包含ruleId");
        Assert.hasKeyAndValue(reqJson, "discountType", "请求报文中未包含discountType");

        JSONArray feeDiscountRuleSpecs = reqJson.getJSONArray("feeDiscountRuleSpecs");
        FeeDiscountPo feeDiscountPo = BeanConvertUtil.covertBean(reqJson, FeeDiscountPo.class);
        return saveFeeDiscountBMOImpl.save(feeDiscountPo, feeDiscountRuleSpecs);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /feeDiscount/updateFeeDiscount
     * @path /app/feeDiscount/updateFeeDiscount
     */
    @RequestMapping(value = "/updateFeeDiscount", method = RequestMethod.POST)
    public ResponseEntity<String> updateFeeDiscount(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "discountName", "请求报文中未包含discountName");
        Assert.hasKeyAndValue(reqJson, "ruleId", "请求报文中未包含ruleId");
        Assert.hasKeyAndValue(reqJson, "discountType", "请求报文中未包含discountType");
        Assert.hasKeyAndValue(reqJson, "discountId", "discountId不能为空");

        JSONArray feeDiscountRuleSpecs = reqJson.getJSONArray("feeDiscountRuleSpecs");
        FeeDiscountPo feeDiscountPo = BeanConvertUtil.covertBean(reqJson, FeeDiscountPo.class);
        return updateFeeDiscountBMOImpl.update(feeDiscountPo, feeDiscountRuleSpecs);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /feeDiscount/deleteFeeDiscount
     * @path /app/feeDiscount/deleteFeeDiscount
     */
    @RequestMapping(value = "/deleteFeeDiscount", method = RequestMethod.POST)
    public ResponseEntity<String> deleteFeeDiscount(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "discountId", "discountId不能为空");


        FeeDiscountPo feeDiscountPo = BeanConvertUtil.covertBean(reqJson, FeeDiscountPo.class);
        return deleteFeeDiscountBMOImpl.delete(feeDiscountPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /feeDiscount/queryFeeDiscount
     * @path /app/feeDiscount/queryFeeDiscount
     */
    @RequestMapping(value = "/queryFeeDiscount", method = RequestMethod.GET)
    public ResponseEntity<String> queryFeeDiscount(@RequestParam(value = "communityId") String communityId,
                                                   @RequestParam(value = "discountType", required = false) String discountType,
                                                   @RequestParam(value = "page") int page,
                                                   @RequestParam(value = "row") int row) {
        FeeDiscountDto feeDiscountDto = new FeeDiscountDto();
        feeDiscountDto.setPage(page);
        feeDiscountDto.setRow(row);
        feeDiscountDto.setCommunityId(communityId);
        feeDiscountDto.setDiscountType(discountType);
        return getFeeDiscountBMOImpl.get(feeDiscountDto);
    }

    /**
     * 微信删除消息模板
     *
     * @return
     * @serviceCode /feeDiscount/queryFeeDiscountRule
     * @path /app/feeDiscount/queryFeeDiscountRule
     */
    @RequestMapping(value = "/queryFeeDiscountRule", method = RequestMethod.GET)
    public ResponseEntity<String> queryFeeDiscountRule(
            @RequestParam(value = "discountType", required = false) String discountType,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "row") int row) {
        FeeDiscountRuleDto feeDiscountRuleDto = new FeeDiscountRuleDto();
        feeDiscountRuleDto.setPage(page);
        feeDiscountRuleDto.setRow(row);
        feeDiscountRuleDto.setDiscountType(discountType);
        return getFeeDiscountRuleBMOImpl.get(feeDiscountRuleDto);
    }

    /**
     * 微信删除消息模板
     *
     * @param ruleId 小区ID
     * @return
     * @serviceCode /feeDiscount/queryFeeDiscountRuleSpec
     * @path /app/feeDiscount/queryFeeDiscountRuleSpec
     */
    @RequestMapping(value = "/queryFeeDiscountRuleSpec", method = RequestMethod.GET)
    public ResponseEntity<String> queryFeeDiscountRuleSpec(@RequestParam(value = "ruleId") String ruleId,
                                                           @RequestParam(value = "page") int page,
                                                           @RequestParam(value = "row") int row) {
        FeeDiscountRuleSpecDto feeDiscountRuleSpecDto = new FeeDiscountRuleSpecDto();
        feeDiscountRuleSpecDto.setPage(page);
        feeDiscountRuleSpecDto.setRow(row);
        feeDiscountRuleSpecDto.setRuleId(ruleId);
        return getFeeDiscountRuleSpecBMOImpl.get(feeDiscountRuleSpecDto);
    }
}
