package com.java110.fee.cmd.fee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.fee.ITempCarFeeConfigAttrV1InnerServiceSMO;
import com.java110.intf.fee.ITempCarFeeConfigV1InnerServiceSMO;
import com.java110.po.fee.PayFeeConfigPo;
import com.java110.po.tempCarFeeConfig.TempCarFeeConfigPo;
import com.java110.po.tempCarFeeConfigAttr.TempCarFeeConfigAttrPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

@Java110Cmd(serviceCode = "fee.deleteTempCarFeeConfig")
public class DeleteTempCarFeeConfigCmd extends Cmd {

    @Autowired
    private ITempCarFeeConfigV1InnerServiceSMO tempCarFeeConfigV1InnerServiceSMOImpl;

    @Autowired
    private ITempCarFeeConfigAttrV1InnerServiceSMO tempCarFeeConfigAttrV1InnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "configId", "configId不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        TempCarFeeConfigPo tempCarFeeConfigPo = BeanConvertUtil.covertBean(reqJson, TempCarFeeConfigPo.class);
        int flag = tempCarFeeConfigV1InnerServiceSMOImpl.deleteTempCarFeeConfig(tempCarFeeConfigPo);
        if (flag < 1) {
            throw new CmdException("删除临时车费失败");
        }
        JSONArray attrs = reqJson.getJSONArray("tempCarFeeConfigAttrs");
        if (attrs != null && attrs.size() > 0) {
            JSONObject attr = null;
            for (int attrIndex = 0; attrIndex < attrs.size(); attrIndex++) {
                attr = attrs.getJSONObject(attrIndex);
                attr.put("ruleId", reqJson.getString("ruleId"));
                attr.put("communityId", reqJson.getString("communityId"));
                if (!attr.containsKey("attrId") || attr.getString("attrId").startsWith("-") || StringUtil.isEmpty(attr.getString("attrId"))) {
                    continue;
                }
                TempCarFeeConfigAttrPo tempCarFeeConfigAttrPo = BeanConvertUtil.covertBean(attr, TempCarFeeConfigAttrPo.class);
                flag = tempCarFeeConfigAttrV1InnerServiceSMOImpl.deleteTempCarFeeConfigAttr(tempCarFeeConfigAttrPo);
                if (flag < 1) {
                    throw new CmdException("删除临时车费失败");
                }
            }
        }
        PayFeeConfigPo payFeeConfigPo = new PayFeeConfigPo();
        payFeeConfigPo.setConfigId(reqJson.getString("feeConfigId"));
        payFeeConfigPo.setStatusCd("1");
        int i = feeConfigInnerServiceSMOImpl.deleteFeeConfig(payFeeConfigPo);
        if (i < 1) {
            throw new CmdException("删除费用项失败");
        }
    }
}
