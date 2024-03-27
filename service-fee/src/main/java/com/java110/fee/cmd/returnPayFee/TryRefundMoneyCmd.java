package com.java110.fee.cmd.returnPayFee;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.onlinePayRefund.OnlinePayRefundDto;
import com.java110.dto.wechat.OnlinePayDto;
import com.java110.intf.acct.IOnlinePayRefundV1InnerServiceSMO;
import com.java110.intf.acct.IOnlinePayV1InnerServiceSMO;
import com.java110.po.onlinePayRefund.OnlinePayRefundPo;
import com.java110.po.wechat.OnlinePayPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

@Java110Cmd(serviceCode = "returnPayFee.tryRefundMoney")
public class TryRefundMoneyCmd extends Cmd {

    @Autowired
    private IOnlinePayRefundV1InnerServiceSMO onlinePayRefundV1InnerServiceSMOImpl;

    @Autowired
    private IOnlinePayV1InnerServiceSMO onlinePayV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson,"detailId","未包含缴费明细");


    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        OnlinePayRefundDto onlinePayRefundDto = new OnlinePayRefundDto();
        onlinePayRefundDto.setBusiId(reqJson.getString("detailId"));
        onlinePayRefundDto.setState(OnlinePayDto.STATE_FT);
        List<OnlinePayRefundDto> onlinePayRefundDtos = onlinePayRefundV1InnerServiceSMOImpl.queryOnlinePayRefunds(onlinePayRefundDto);
        if(ListUtil.isNull(onlinePayRefundDtos)){
            throw new CmdException("未查到退款失败订单");
        }

        OnlinePayDto onlinePayDto = new OnlinePayDto();
        onlinePayDto.setPayId(onlinePayRefundDtos.get(0).getPayId());
        List<OnlinePayDto> onlinePayDtos = onlinePayV1InnerServiceSMOImpl.queryOnlinePays(onlinePayDto);
        if (ListUtil.isNull(onlinePayDtos)) {
            return;
        }

        //todo 保存 退费明细

        OnlinePayRefundPo onlinePayRefundPo = new OnlinePayRefundPo();
        onlinePayRefundPo.setRefundId(onlinePayRefundDtos.get(0).getRefundId());
        onlinePayRefundPo.setState(OnlinePayDto.STATE_WT);
        onlinePayRefundPo.setMessage("待退费");
        onlinePayRefundPo.setCommunityId(onlinePayRefundDtos.get(0).getCommunityId());
        onlinePayRefundV1InnerServiceSMOImpl.updateOnlinePayRefund(onlinePayRefundPo);

        OnlinePayPo onlinePayPo = new OnlinePayPo();
        onlinePayPo.setOrderId(onlinePayDtos.get(0).getOrderId());
        onlinePayPo.setPayId(onlinePayDtos.get(0).getPayId());
        onlinePayPo.setState(OnlinePayDto.STATE_WT);
        onlinePayPo.setRefundFee(onlinePayRefundDtos.get(0).getRefundFee());
        onlinePayV1InnerServiceSMOImpl.updateOnlinePay(onlinePayPo);

    }
}
