package com.java110.acct.cmd.couponProperty;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.coupon.ICouponAdapt;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.doc.annotation.*;
import com.java110.dto.couponPool.CouponPropertyUserDto;
import com.java110.dto.couponPool.CouponQrCodeDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.acct.ICouponPropertyUserV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;



@Java110CmdDoc(title = "优惠券核销码",
        description = "供业主端生成优惠券核销码",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/couponProperty.generatorCouponQrcode",
        resource = "acctDoc",
        author = "吴学文",
        serviceCode = "couponProperty.generatorCouponQrcode"
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "couponId", length = 30, remark = "优惠券ID"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
                @Java110ParamDoc(name = "data", type = "Object", remark = "有效数据"),
                @Java110ParamDoc(parentNodeName = "data", name = "qrCode", type = "String", remark = "二维码信息"),
                @Java110ParamDoc(parentNodeName = "data", name = "remark", type = "String", remark = "优惠券核销说明"),
        }
)

@Java110ExampleDoc(
        reqBody = "{'couponId':'123123'}",
        resBody = "{'code':0,'msg':'成功','data':{'qrCode':'123123','remark':'nihao'}}"
)

@Java110Cmd(serviceCode = "couponProperty.generatorCouponQrcode")
public class GeneratorCouponQrcodeCmd extends Cmd {

    @Autowired
    private ICouponPropertyUserV1InnerServiceSMO couponPropertyUserV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "couponId", "未包含优惠券ID");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        String userId = context.getReqHeaders().get("user-id");

        //校验优惠券是否存在
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        Assert.listOnlyOne(userDtos,"用户不存在");

        CouponPropertyUserDto couponPropertyUserDto = new CouponPropertyUserDto();
        couponPropertyUserDto.setCouponId(reqJson.getString("couponId"));
        couponPropertyUserDto.setTel(userDtos.get(0).getTel());
        couponPropertyUserDto.setState(CouponPropertyUserDto.STATE_WAIT);

        List<CouponPropertyUserDto> couponPropertyUserDtos = couponPropertyUserV1InnerServiceSMOImpl.queryCouponPropertyUsers(couponPropertyUserDto);

        if (couponPropertyUserDtos == null || couponPropertyUserDtos.size() < 1) {
            throw new CmdException("优惠券不存在");
        }

        if (!"Y".equals(couponPropertyUserDtos.get(0).getIsExpire())) {
            throw new CmdException("优惠券已过期");
        }


        String toType = couponPropertyUserDtos.get(0).getToType();

        ICouponAdapt couponAdapt = ApplicationContextFactory.getBean(ICouponAdapt.COUPON_PRE+toType,ICouponAdapt.class);

        if(couponAdapt == null){
            throw new CmdException("优惠券不支持生成二维码");
        }

        CouponQrCodeDto couponQrCodeDto = couponAdapt.generatorQrcode(couponPropertyUserDtos.get(0));

        context.setResponseEntity(ResultVo.createResponseEntity(couponQrCodeDto));
    }
}
