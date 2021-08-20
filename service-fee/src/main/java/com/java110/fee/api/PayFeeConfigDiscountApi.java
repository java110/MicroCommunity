package com.java110.fee.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.payFeeConfigDiscount.PayFeeConfigDiscountDto;
import com.java110.fee.bmo.payFeeConfigDiscount.IDeletePayFeeConfigDiscountBMO;
import com.java110.fee.bmo.payFeeConfigDiscount.IGetPayFeeConfigDiscountBMO;
import com.java110.fee.bmo.payFeeConfigDiscount.ISavePayFeeConfigDiscountBMO;
import com.java110.fee.bmo.payFeeConfigDiscount.IUpdatePayFeeConfigDiscountBMO;
import com.java110.po.payFeeConfigDiscount.PayFeeConfigDiscountPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/payFeeConfigDiscount")
public class PayFeeConfigDiscountApi {

    @Autowired
    private ISavePayFeeConfigDiscountBMO savePayFeeConfigDiscountBMOImpl;

    @Autowired
    private IUpdatePayFeeConfigDiscountBMO updatePayFeeConfigDiscountBMOImpl;

    @Autowired
    private IDeletePayFeeConfigDiscountBMO deletePayFeeConfigDiscountBMOImpl;

    @Autowired
    private IGetPayFeeConfigDiscountBMO getPayFeeConfigDiscountBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /payFeeConfigDiscount/savePayFeeConfigDiscount
     * @path /app/payFeeConfigDiscount/savePayFeeConfigDiscount
     */
    @RequestMapping(value = "/savePayFeeConfigDiscount", method = RequestMethod.POST)
    public ResponseEntity<String> savePayFeeConfigDiscount(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "configId", "请求报文中未包含configId");
        Assert.hasKeyAndValue(reqJson, "discountId", "请求报文中未包含discountId");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含startTime");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含endTime");

        PayFeeConfigDiscountPo payFeeConfigDiscountPo = BeanConvertUtil.covertBean(reqJson, PayFeeConfigDiscountPo.class);
        String paymaxEndTime = reqJson.getString("payMaxEndTime");
        if (StringUtil.isEmpty(paymaxEndTime)) {
            //如果优惠最大时间为空，就默认为2037-12-31 00:00:00
            payFeeConfigDiscountPo.setPayMaxEndTime("2037-12-31 00:00:00");
        }
        return savePayFeeConfigDiscountBMOImpl.save(payFeeConfigDiscountPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /payFeeConfigDiscount/updatePayFeeConfigDiscount
     * @path /app/payFeeConfigDiscount/updatePayFeeConfigDiscount
     */
    @RequestMapping(value = "/updatePayFeeConfigDiscount", method = RequestMethod.POST)
    public ResponseEntity<String> updatePayFeeConfigDiscount(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "configId", "请求报文中未包含configId");
        Assert.hasKeyAndValue(reqJson, "discountId", "请求报文中未包含discountId");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "configDiscountId", "configDiscountId不能为空");


        PayFeeConfigDiscountPo payFeeConfigDiscountPo = BeanConvertUtil.covertBean(reqJson, PayFeeConfigDiscountPo.class);
        return updatePayFeeConfigDiscountBMOImpl.update(payFeeConfigDiscountPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /payFeeConfigDiscount/deletePayFeeConfigDiscount
     * @path /app/payFeeConfigDiscount/deletePayFeeConfigDiscount
     */
    @RequestMapping(value = "/deletePayFeeConfigDiscount", method = RequestMethod.POST)
    public ResponseEntity<String> deletePayFeeConfigDiscount(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "configDiscountId", "configDiscountId不能为空");


        PayFeeConfigDiscountPo payFeeConfigDiscountPo = BeanConvertUtil.covertBean(reqJson, PayFeeConfigDiscountPo.class);
        return deletePayFeeConfigDiscountBMOImpl.delete(payFeeConfigDiscountPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /payFeeConfigDiscount/queryPayFeeConfigDiscount
     * @path /app/payFeeConfigDiscount/queryPayFeeConfigDiscount
     */
    @RequestMapping(value = "/queryPayFeeConfigDiscount", method = RequestMethod.GET)
    public ResponseEntity<String> queryPayFeeConfigDiscount(@RequestParam(value = "communityId") String communityId,
                                                            @RequestParam(value = "configId", required = false) String configId,
                                                            @RequestParam(value = "page") int page,
                                                            @RequestParam(value = "row") int row) {
        PayFeeConfigDiscountDto payFeeConfigDiscountDto = new PayFeeConfigDiscountDto();
        payFeeConfigDiscountDto.setPage(page);
        payFeeConfigDiscountDto.setRow(row);
        payFeeConfigDiscountDto.setConfigId(configId);
        payFeeConfigDiscountDto.setCommunityId(communityId);
        return getPayFeeConfigDiscountBMOImpl.get(payFeeConfigDiscountDto);
    }
}
