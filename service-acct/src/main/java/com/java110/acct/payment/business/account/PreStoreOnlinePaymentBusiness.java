package com.java110.acct.payment.business.account;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.payment.IPaymentBusiness;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.doc.annotation.*;
import com.java110.dto.account.AccountDto;
import com.java110.dto.payment.PaymentOrderDto;
import com.java110.intf.acct.IAccountInnerServiceSMO;
import com.java110.intf.community.ICommunitySpacePersonTimeV1InnerServiceSMO;
import com.java110.intf.community.ICommunitySpacePersonV1InnerServiceSMO;
import com.java110.intf.community.ICommunitySpaceV1InnerServiceSMO;
import com.java110.po.accountDetail.AccountDetailPo;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Java110CmdDoc(title = "账户预存",
        description = "手机端账户预存",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/payment.unifiedPayment",
        resource = "acctDoc",
        author = "吴学文",
        serviceCode = "payment.unifiedPayment.preStoreOnline",
        seq = 1
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "business", length = 64, remark = "支付场景， preStoreOnline"),
        @Java110ParamDoc(name = "payAdapt", length = 64, remark = "支付适配器，非必填"),
        @Java110ParamDoc(name = "tradeType", length = 64, remark = "支付类型 NATIVE JSAPI APP"),
        @Java110ParamDoc(name = "communityId", length = 30, remark = "小区ID"),
        @Java110ParamDoc(name = "tradeType", length = 30, remark = "支付类型 NATIVE JSAPI APP"),
        @Java110ParamDoc(name = "acctId", length = 30, remark = "账户ID"),
        @Java110ParamDoc(name = "receivedAmount", length = 30, remark = "充值金额")

})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 100 成功不需要唤起支付窗口，直接支付成功，可能从账户等做了扣款，其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
        }
)

@Java110ExampleDoc(
        reqBody = "{       \"business\":\"preStoreOnline\",\"communityId\":\"123123\",\n" +
                "         personName:\"张三\",\n" +
                "         acctId:\"18909711111\",\n" +
                "         receivedAmount:\"100\",\n" +
                "         communityId:\"123123\"" +
                " }",
        resBody = "{'code':0,'msg':'成功'}"
)

/**
 * 场地预约
 */
@Service("preStoreOnline")
public class PreStoreOnlinePaymentBusiness implements IPaymentBusiness {

    @Autowired
    private ICommunitySpaceV1InnerServiceSMO communitySpaceV1InnerServiceSMOImpl;

    public static final String CODE_PREFIX_ID = "10";


    @Autowired
    private ICommunitySpacePersonV1InnerServiceSMO communitySpacePersonV1InnerServiceSMOImpl;

    @Autowired
    private ICommunitySpacePersonTimeV1InnerServiceSMO communitySpacePersonTimeV1InnerServiceSMOImpl;

    @Autowired
    private IAccountInnerServiceSMO accountInnerServiceSMOImpl;

    /**
     * @param context
     * @param reqJson{ personName:"",
     *                 personTel:"",
     *                 appointmentTime:"",
     *                 payWay:"",
     *                 communityId:"",
     *                 spaces:[{spaceId:'123',openTimes:[{hours:1},{hours:2}]}]
     *                 }
     * @return
     */
    @Override
    public PaymentOrderDto unified(ICmdDataFlowContext context, JSONObject reqJson) {

        //Assert.hasKeyAndValue(reqJson, "spaceId", "请求报文中未包含spaceId");
        Assert.hasKeyAndValue(reqJson, "acctId", "请求报文中未包含acctId");
        Assert.hasKeyAndValue(reqJson, "receivedAmount", "请求报文中未包含receivedAmount");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");

        AccountDto accountDto = new AccountDto();
        accountDto.setAcctId(reqJson.getString("acctId"));
        List<AccountDto> accountDtos = accountInnerServiceSMOImpl.queryAccounts(accountDto);

        Assert.listOnlyOne(accountDtos, "账户不存在");

        PaymentOrderDto paymentOrderDto = new PaymentOrderDto();
        paymentOrderDto.setOrderId(GenerateCodeFactory.getOId());
        paymentOrderDto.setMoney(reqJson.getDoubleValue("receivedAmount"));
        paymentOrderDto.setName("账户充值");

        reqJson.put("receivableAmount", reqJson.getDoubleValue("receivedAmount"));
        reqJson.put("receivedAmount", reqJson.getDoubleValue("receivedAmount"));
        return paymentOrderDto;
    }

    @Override
    public void notifyPayment(PaymentOrderDto paymentOrderDto, JSONObject reqJson) {

        String acctId = reqJson.getString("acctId");
        String receivedAmount = reqJson.getString("receivedAmount");
        //查询 业主是否有账户
        AccountDto accountDto = new AccountDto();
        accountDto.setAcctId(acctId);
        List<AccountDto> accountDtos = accountInnerServiceSMOImpl.queryAccounts(accountDto);

        Assert.listOnlyOne(accountDtos, "账户不存在");

        AccountDetailPo accountDetailPo = new AccountDetailPo();
        accountDetailPo.setRemark("线上充值");
        accountDetailPo.setAmount(receivedAmount);
        accountDetailPo.setOrderId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_orderId));
        accountDetailPo.setAcctId(accountDto.getAcctId());
        accountDetailPo.setObjId(accountDtos.get(0).getObjId());
        accountDetailPo.setObjType(accountDtos.get(0).getObjType());
        accountInnerServiceSMOImpl.prestoreAccount(accountDetailPo);
    }
}
