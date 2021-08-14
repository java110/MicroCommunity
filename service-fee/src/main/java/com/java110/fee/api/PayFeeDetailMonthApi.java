package com.java110.fee.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.payFeeDetailMonth.PayFeeDetailMonthDto;
import com.java110.fee.bmo.payFeeDetailMonth.IDeletePayFeeDetailMonthBMO;
import com.java110.fee.bmo.payFeeDetailMonth.IGetPayFeeDetailMonthBMO;
import com.java110.fee.bmo.payFeeDetailMonth.ISavePayFeeDetailMonthBMO;
import com.java110.fee.bmo.payFeeDetailMonth.IUpdatePayFeeDetailMonthBMO;
import com.java110.po.payFeeDetailMonth.PayFeeDetailMonthPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/payFeeDetailMonth")
public class PayFeeDetailMonthApi {

    @Autowired
    private ISavePayFeeDetailMonthBMO savePayFeeDetailMonthBMOImpl;
    @Autowired
    private IUpdatePayFeeDetailMonthBMO updatePayFeeDetailMonthBMOImpl;
    @Autowired
    private IDeletePayFeeDetailMonthBMO deletePayFeeDetailMonthBMOImpl;

    @Autowired
    private IGetPayFeeDetailMonthBMO getPayFeeDetailMonthBMOImpl;

    /**
     * 微信保存消息模板
     * @serviceCode /payFeeDetailMonth/savePayFeeDetailMonth
     * @path /app/payFeeDetailMonth/savePayFeeDetailMonth
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/savePayFeeDetailMonth", method = RequestMethod.POST)
    public ResponseEntity<String> savePayFeeDetailMonth(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "detailId", "请求报文中未包含detailId");
Assert.hasKeyAndValue(reqJson, "detailYear", "请求报文中未包含detailYear");
Assert.hasKeyAndValue(reqJson, "detailMonth", "请求报文中未包含detailMonth");


        PayFeeDetailMonthPo payFeeDetailMonthPo = BeanConvertUtil.covertBean(reqJson, PayFeeDetailMonthPo.class);
        return savePayFeeDetailMonthBMOImpl.save(payFeeDetailMonthPo);
    }

    /**
     * 微信修改消息模板
     * @serviceCode /payFeeDetailMonth/updatePayFeeDetailMonth
     * @path /app/payFeeDetailMonth/updatePayFeeDetailMonth
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/updatePayFeeDetailMonth", method = RequestMethod.POST)
    public ResponseEntity<String> updatePayFeeDetailMonth(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "detailId", "请求报文中未包含detailId");
Assert.hasKeyAndValue(reqJson, "detailYear", "请求报文中未包含detailYear");
Assert.hasKeyAndValue(reqJson, "detailMonth", "请求报文中未包含detailMonth");
Assert.hasKeyAndValue(reqJson, "monthId", "monthId不能为空");


        PayFeeDetailMonthPo payFeeDetailMonthPo = BeanConvertUtil.covertBean(reqJson, PayFeeDetailMonthPo.class);
        return updatePayFeeDetailMonthBMOImpl.update(payFeeDetailMonthPo);
    }

    /**
     * 微信删除消息模板
     * @serviceCode /payFeeDetailMonth/deletePayFeeDetailMonth
     * @path /app/payFeeDetailMonth/deletePayFeeDetailMonth
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/deletePayFeeDetailMonth", method = RequestMethod.POST)
    public ResponseEntity<String> deletePayFeeDetailMonth(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "monthId", "monthId不能为空");


        PayFeeDetailMonthPo payFeeDetailMonthPo = BeanConvertUtil.covertBean(reqJson, PayFeeDetailMonthPo.class);
        return deletePayFeeDetailMonthBMOImpl.delete(payFeeDetailMonthPo);
    }

    /**
     * 微信删除消息模板
     * @serviceCode /payFeeDetailMonth/queryPayFeeDetailMonth
     * @path /app/payFeeDetailMonth/queryPayFeeDetailMonth
     * @param communityId 小区ID
     * @return
     */
    @RequestMapping(value = "/queryPayFeeDetailMonth", method = RequestMethod.GET)
    public ResponseEntity<String> queryPayFeeDetailMonth(@RequestParam(value = "communityId") String communityId,
                                                      @RequestParam(value = "page") int page,
                                                      @RequestParam(value = "row") int row) {
        PayFeeDetailMonthDto payFeeDetailMonthDto = new PayFeeDetailMonthDto();
        payFeeDetailMonthDto.setPage(page);
        payFeeDetailMonthDto.setRow(row);
        payFeeDetailMonthDto.setCommunityId(communityId);
        return getPayFeeDetailMonthBMOImpl.get(payFeeDetailMonthDto);
    }
}
