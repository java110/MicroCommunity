package com.java110.fee.cmd.tempCarFee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.AbstractServiceCmdListener;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.couponUser.CouponUserDto;
import com.java110.dto.tempCarFeeConfig.TempCarPayOrderDto;
import com.java110.fee.bmo.tempCarFee.IGetTempCarFeeRules;
import com.java110.intf.acct.ICouponUserV1InnerServiceSMO;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * 类表述：删除
 * 服务编码：feePrintPage.deleteFeePrintPage
 * 请求路劲：/app/feePrintPage.DeleteFeePrintPage
 * add by 吴学文 at 2021-09-16 22:26:04 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "tempCarFee.queryTempCarFeeOrder")
public class QueryTempCarFeeOrderCmd extends AbstractServiceCmdListener {
    @Autowired
    private ICouponUserV1InnerServiceSMO couponUserV1InnerServiceSMOImpl;
    @Autowired
    private IGetTempCarFeeRules getTempCarFeeRulesImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "paId", "paId不能为空");
        Assert.hasKeyAndValue(reqJson, "carNum", "carNum不能为空");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        TempCarPayOrderDto tempCarPayOrderDto = new TempCarPayOrderDto();
        tempCarPayOrderDto.setPaId(reqJson.getString("paId"));
        tempCarPayOrderDto.setCarNum(reqJson.getString("carNum"));
        ResponseEntity<String> responseEntity = getTempCarFeeRulesImpl.getTempCarFeeOrder(tempCarPayOrderDto);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            cmdDataFlowContext.setResponseEntity(responseEntity);
            return;
        }
        JSONObject orderInfo = JSONObject.parseObject(responseEntity.getBody().toString());
        if (orderInfo.getIntValue("code") != 0) {
            cmdDataFlowContext.setResponseEntity(responseEntity);
            return;
        }

        JSONObject fee = orderInfo.getJSONObject("data");
        //double money = fee.getDouble("payCharge");
        BigDecimal money = new BigDecimal(fee.getDouble("payCharge"));
        //3.0 考虑优惠卷
        double couponPrice = checkCouponUser(reqJson);
        money = money.subtract(new BigDecimal(couponPrice)).setScale(2, BigDecimal.ROUND_HALF_EVEN);

        double receivedAmount = money.doubleValue();
        //所有 优惠折扣计算完后，如果总金额小于等于0，则返回总扣款为0
        if (receivedAmount <= 0) {
            receivedAmount = 0.0;
        }
        fee.put("receivedAmount", receivedAmount);
        fee.put("oId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_oId));
        JSONObject outParm = new JSONObject();
        outParm.put("data",fee);
        outParm.put("code","0");
        outParm.put("msg","成功");
        ResponseEntity<String> responseEntitys = new ResponseEntity<>(outParm.toJSONString(), HttpStatus.OK);
        fee.putAll(reqJson);
        CommonCache.setValue("queryTempCarFeeOrder" + fee.getString("oId"), fee.toJSONString(), 24 * 60 * 60);
        cmdDataFlowContext.setResponseEntity(responseEntitys);
    }

    private double checkCouponUser(JSONObject paramObj) {

        BigDecimal couponPrice = new BigDecimal(0.0);
        String couponIds = paramObj.getString("couponIds");
        if (couponIds == null || "".equals(couponIds)) {
            paramObj.put("couponPrice", couponPrice.doubleValue());
            paramObj.put("couponUserDtos", new JSONArray()); //这里考虑空
            return couponPrice.doubleValue();
        }

        List<String> result = Arrays.asList(couponIds.split(","));
        CouponUserDto couponUserDto = new CouponUserDto();
        couponUserDto.setCouponIds(result.toArray(new String[result.size()]));
        List<CouponUserDto> couponUserDtos = couponUserV1InnerServiceSMOImpl.queryCouponUsers(couponUserDto);
        if (couponUserDtos == null || couponUserDtos.size() < 1) {
            paramObj.put("couponPrice", couponPrice.doubleValue());
            return couponPrice.doubleValue();
        }
        for (CouponUserDto couponUser : couponUserDtos) {
            //不计算已过期购物券金额
            if (couponUser.getEndTime().compareTo(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_B)) >= 0) {
                couponPrice = couponPrice.add(new BigDecimal(Double.parseDouble(couponUser.getActualPrice())));
            }
        }
        paramObj.put("couponPrice", couponPrice.doubleValue());
        paramObj.put("couponUserDtos", BeanConvertUtil.beanCovertJSONArray(couponUserDtos));
        return couponPrice.doubleValue();
    }
}
