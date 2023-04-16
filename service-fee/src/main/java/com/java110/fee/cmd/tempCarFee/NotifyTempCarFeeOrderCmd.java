package com.java110.fee.cmd.tempCarFee;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.intf.fee.ITempCarFeeCreateOrderV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 类表述：通知
 * 服务编码：tempCarFee.notifyTempCarFeeOrder
 * 请求路劲：/app/tempCarFee.notifyTempCarFeeOrder
 * add by 吴学文 at 2021-09-16 22:26:04 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "tempCarFee.notifyTempCarFeeOrder")
public class NotifyTempCarFeeOrderCmd extends Cmd {

    @Autowired
    private ITempCarFeeCreateOrderV1InnerServiceSMO tempCarFeeCreateOrderV1InnerServiceSMOImpl;
    //{"amount":20.0,"payType":"2","orderId":"19c4321c-b5d5-405f-b2ff-20e86a2e7f3e",
    // "payTime":"2021-10-17 17:29:54","paId":"102021101160020175","carNum":"青A88888","oId":"102021101724760012"}
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {

    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        cmdDataFlowContext.setResponseEntity(tempCarFeeCreateOrderV1InnerServiceSMOImpl.notifyOrder(reqJson));
    }
}
