package com.java110.acct.cmd.coupon;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.doc.annotation.*;
import com.java110.dto.couponPool.CouponRuleCppsDto;
import com.java110.dto.couponPool.CouponRuleFeeDto;
import com.java110.dto.fee.FeeDto;
import com.java110.intf.acct.ICouponRuleCppsV1InnerServiceSMO;
import com.java110.intf.acct.ICouponRuleFeeV1InnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Java110CmdDoc(title = "根据费用计算优惠券",
        description = "缴费时计算费用 是否有优惠券",
        httpMethod = "get",
        url = "http://{ip}:{port}/app/coupon.computePayFeeCoupon",
        resource = "acctDoc",
        author = "吴学文",
        serviceCode = "coupon.computePayFeeCoupon"
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "feeId", length = 30, remark = "费用ID"),
        @Java110ParamDoc(name = "cycle", length = 30, remark = "缴费周期"),
        @Java110ParamDoc(name = "communityId", length = 30, remark = "小区ID"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
                @Java110ParamDoc(name = "data", type = "Array", remark = "有效数据"),
                @Java110ParamDoc(parentNodeName = "data",name = "userId", type = "String", remark = "用户ID"),
                @Java110ParamDoc(parentNodeName = "data",name = "token", type = "String", remark = "临时票据"),
        }
)

@Java110ExampleDoc(
        reqBody="{'feeId':'123123','cycle':'1','communityId':'123123'}",
        resBody="{'code':0,'msg':'成功','data':{'userId':'123123','token':'123213'}}"
)
@Java110Cmd(serviceCode = "coupon.computePayFeeCoupon")
public class ComputePayFeeCouponCmd extends Cmd {

    @Autowired
    private ICouponRuleFeeV1InnerServiceSMO couponRuleFeeV1InnerServiceSMOImpl;

    @Autowired
    private ICouponRuleCppsV1InnerServiceSMO couponRuleCppsV1InnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        Assert.hasKeyAndValue(reqJson,"feeId","未包含费用");
        Assert.hasKeyAndValue(reqJson,"communityId","未包含小区");
        Assert.hasKeyAndValue(reqJson,"cycles","未包含缴费周期");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        FeeDto feeDto = new FeeDto();
        feeDto.setFeeId(reqJson.getString("feeId"));
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

        Assert.listOnlyOne(feeDtos,"费用不存在");

        CouponRuleFeeDto couponRuleFeeDto = new CouponRuleFeeDto();
        couponRuleFeeDto.setFeeConfigId(feeDtos.get(0).getConfigId());
        couponRuleFeeDto.setCurTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        couponRuleFeeDto.setCommunityId(reqJson.getString("communityId"));
        couponRuleFeeDto.setCycle(reqJson.getString("cycles"));
        List<CouponRuleFeeDto> couponRuleFeeDtos = couponRuleFeeV1InnerServiceSMOImpl.queryCouponRuleFees(couponRuleFeeDto);

        if(couponRuleFeeDtos == null || couponRuleFeeDtos.size()<1){
            context.setResponseEntity(ResultVo.createResponseEntity(new JSONArray()));
            return ;
        }

        List<String> ruleIds = new ArrayList<>();
        for(CouponRuleFeeDto tmpCouponRuleFeeDto: couponRuleFeeDtos){
            ruleIds.add(tmpCouponRuleFeeDto.getRuleId());
        }

        CouponRuleCppsDto couponRuleCppsDto = new CouponRuleCppsDto();
        couponRuleCppsDto.setRuleIds(ruleIds.toArray(new String[ruleIds.size()]));
        List<CouponRuleCppsDto> couponRuleCppsDtos = couponRuleCppsV1InnerServiceSMOImpl.queryCouponRuleCppss(couponRuleCppsDto);

        if(couponRuleCppsDtos == null || couponRuleCppsDtos.size() < 1){
            context.setResponseEntity(ResultVo.createResponseEntity(new JSONArray()));
            return ;
        }
        context.setResponseEntity(ResultVo.createResponseEntity(couponRuleCppsDtos));
    }
}
