package com.java110.fee.cmd.payFeeQrcode;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.payFeeQrcode.PayFeeQrcodeDto;
import com.java110.intf.fee.IPayFeeQrcodeV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

/***
 * 查询二维码配置
 */
@Java110Cmd(serviceCode = "payFeeQrcode.getQrcodeConfig")
public class GetQrcodeConfigCmd extends Cmd {

    @Autowired
    private IPayFeeQrcodeV1InnerServiceSMO payFeeQrcodeV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");
        Assert.hasKeyAndValue(reqJson, "pfqId", "未包含二维码信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        PayFeeQrcodeDto payFeeQrcodeDto = new PayFeeQrcodeDto();
        payFeeQrcodeDto.setCommunityId(reqJson.getString("communityId"));
        payFeeQrcodeDto.setPfqId(reqJson.getString("pfqId"));
        List<PayFeeQrcodeDto> payFeeQrcodeDtos = payFeeQrcodeV1InnerServiceSMOImpl.queryPayFeeQrcodes(payFeeQrcodeDto);

        Assert.listOnlyOne(payFeeQrcodeDtos, "二维码配置错误");

        context.setResponseEntity(ResultVo.createResponseEntity(payFeeQrcodeDtos.get(0)));
    }
}
