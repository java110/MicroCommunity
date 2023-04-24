package com.java110.fee.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.dto.fee.FeeDiscountDto;
import com.java110.dto.fee.FeeDiscountRuleDto;
import com.java110.dto.fee.FeeDiscountRuleSpecDto;
import com.java110.dto.payFeeDetailDiscount.PayFeeDetailDiscountDto;
import com.java110.fee.bmo.feeDiscount.IDeleteFeeDiscountBMO;
import com.java110.fee.bmo.feeDiscount.IGetFeeDiscountBMO;
import com.java110.fee.bmo.feeDiscount.ISaveFeeDiscountBMO;
import com.java110.fee.bmo.feeDiscount.IUpdateFeeDiscountBMO;
import com.java110.fee.bmo.feeDiscountRule.IGetFeeDiscountRuleBMO;
import com.java110.fee.bmo.feeDiscountRuleSpec.IComputeFeeDiscountBMO;
import com.java110.fee.bmo.feeDiscountRuleSpec.IGetFeeDiscountRuleSpecBMO;
import com.java110.po.feeDiscount.FeeDiscountPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;


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

    @Autowired
    private IComputeFeeDiscountBMO computeFeeDiscountBMOImpl;

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
                                                   @RequestParam(value = "discountName", required = false) String discountName,
                                                   @RequestParam(value = "discountId", required = false) String discountId,
                                                   @RequestParam(value = "ruleName", required = false) String ruleName,
                                                   @RequestParam(value = "page") int page,
                                                   @RequestParam(value = "row") int row) {
        FeeDiscountDto feeDiscountDto = new FeeDiscountDto();
        feeDiscountDto.setPage(page);
        feeDiscountDto.setRow(row);
        feeDiscountDto.setCommunityId(communityId);
        feeDiscountDto.setDiscountType(discountType);
        feeDiscountDto.setDiscountName(discountName);
        feeDiscountDto.setRuleName(ruleName);
        feeDiscountDto.setDiscountId(discountId);
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

    /**
     * 计算费用折扣
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /feeDiscount/computeFeeDiscount
     * @path /app/feeDiscount/computeFeeDiscount
     */
    @RequestMapping(value = "/computeFeeDiscount", method = RequestMethod.GET)
    public ResponseEntity<String> computeFeeDiscount(@RequestParam(value = "feeId") String feeId,
                                                     @RequestParam(value = "communityId") String communityId,
                                                     @RequestParam(value = "cycles") double cycles,
                                                     @RequestParam(value = "payerObjId") String payerObjId,
                                                     @RequestParam(value = "payerObjType") String payerObjType,
                                                     @RequestParam(value = "endTime") String endTime,
                                                     @RequestParam(value = "page") int page,
                                                     @RequestParam(value = "row") int row) throws ParseException {
        return computeFeeDiscountBMOImpl.compute(feeId, communityId, cycles, payerObjId, payerObjType, endTime, page, row);
    }


    /**
     * 查询 缴费优惠
     *
     * @return
     * @serviceCode /feeDiscount/queryFeeDetailDiscount
     * @path /app/feeDiscount/queryFeeDetailDiscount
     */
    @RequestMapping(value = "/queryFeeDetailDiscount", method = RequestMethod.GET)
    public ResponseEntity<String> queryFeeDetailDiscount(
            @RequestParam(value = "detailId") String detailId,
            @RequestParam(value = "communityId") String communityId,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "row") int row) {
        PayFeeDetailDiscountDto payFeeDetailDiscountDto = new PayFeeDetailDiscountDto();
        payFeeDetailDiscountDto.setPage(page);
        payFeeDetailDiscountDto.setRow(row);
        payFeeDetailDiscountDto.setDetailId(detailId);
        payFeeDetailDiscountDto.setCommunityId(communityId);
        return getFeeDiscountBMOImpl.getFeeDetailDiscount(payFeeDetailDiscountDto);
    }
}
