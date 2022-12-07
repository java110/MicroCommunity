package com.java110.fee.cmd.fee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.tempCarFeeConfig.TempCarFeeConfigDto;
import com.java110.intf.fee.*;
import com.java110.po.fee.PayFeeConfigPo;
import com.java110.po.tempCarFeeConfig.TempCarFeeConfigPo;
import com.java110.po.tempCarFeeConfigAttr.TempCarFeeConfigAttrPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Java110Cmd(serviceCode = "fee.updateTempCarFeeConfig")
public class UpdateTempCarFeeConfigCmd extends Cmd {

    @Autowired
    private ITempCarFeeConfigInnerServiceSMO tempCarFeeConfigInnerServiceSMOImpl;

    @Autowired
    private ITempCarFeeConfigV1InnerServiceSMO tempCarFeeConfigV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeConfigV1InnerServiceSMO payFeeConfigV1InnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private ITempCarFeeConfigAttrV1InnerServiceSMO tempCarFeeConfigAttrV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "configId", "configId不能为空");
        Assert.hasKeyAndValue(reqJson, "feeName", "请求报文中未包含feeName");
        Assert.hasKeyAndValue(reqJson, "paId", "请求报文中未包含paId");
        Assert.hasKeyAndValue(reqJson, "carType", "请求报文中未包含carType");
        Assert.hasKeyAndValue(reqJson, "ruleId", "请求报文中未包含ruleId");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含startTime");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含endTime");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setConfigId(reqJson.getString("feeConfigId"));
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
        Assert.listOnlyOne(feeConfigDtos, "查询费用项错误！");
        TempCarFeeConfigDto tempCarFeeConfigDto = new TempCarFeeConfigDto();
        tempCarFeeConfigDto.setConfigId(reqJson.getString("configId"));
        tempCarFeeConfigDto.setCommunityId(reqJson.getString("communityId"));
        List<TempCarFeeConfigDto> tempCarFeeConfigDtos = tempCarFeeConfigInnerServiceSMOImpl.queryTempCarFeeConfigs(tempCarFeeConfigDto);
        Assert.listOnlyOne(tempCarFeeConfigDtos, "临时车收费标准不存在");
        TempCarFeeConfigPo tempCarFeeConfigPo = BeanConvertUtil.covertBean(reqJson, TempCarFeeConfigPo.class);
        updateAttr(reqJson);
        int flag = tempCarFeeConfigV1InnerServiceSMOImpl.updateTempCarFeeConfig(tempCarFeeConfigPo);
        if (flag < 1) {
            throw new CmdException("修改临时车费用配置失败");
        }
        //补费用项数据
        PayFeeConfigPo payFeeConfigPo = new PayFeeConfigPo();
        payFeeConfigPo.setCommunityId(reqJson.getString("communityId"));
        payFeeConfigPo.setConfigId(tempCarFeeConfigDtos.get(0).getFeeConfigId());
        payFeeConfigPo.setEndTime(reqJson.getString("endTime"));
        payFeeConfigPo.setStartTime(reqJson.getString("startTime"));
        payFeeConfigPo.setFeeName(reqJson.getString("feeName"));
        updateFeeConfig(BeanConvertUtil.beanCovertJson(payFeeConfigPo), context);
    }

    private void updateAttr(JSONObject reqJson) {
        int flag;
        JSONArray attrs = reqJson.getJSONArray("attrs");
        if (attrs == null || attrs.size() < 1) {
            return;
        }
        JSONObject attr = null;
        for (int attrIndex = 0; attrIndex < attrs.size(); attrIndex++) {
            attr = attrs.getJSONObject(attrIndex);
            attr.put("ruleId", reqJson.getString("ruleId"));
            attr.put("communityId", reqJson.getString("communityId"));
            if (!attr.containsKey("attrId") || attr.getString("attrId").startsWith("-") || StringUtil.isEmpty(attr.getString("attrId"))) {
                attr.put("attrId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
                TempCarFeeConfigAttrPo tempCarFeeConfigAttrPo = BeanConvertUtil.covertBean(attr, TempCarFeeConfigAttrPo.class);
                flag = tempCarFeeConfigAttrV1InnerServiceSMOImpl.saveTempCarFeeConfigAttr(tempCarFeeConfigAttrPo);
                if (flag < 1) {
                    throw new CmdException("修改属性失败");
                }
                continue;
            }
            TempCarFeeConfigAttrPo tempCarFeeConfigAttrPo = BeanConvertUtil.covertBean(attr, TempCarFeeConfigAttrPo.class);
            flag = tempCarFeeConfigAttrV1InnerServiceSMOImpl.updateTempCarFeeConfigAttr(tempCarFeeConfigAttrPo);
            if (flag < 1) {
                throw new CmdException("修改属性失败");
            }
        }
    }

    public void updateFeeConfig(JSONObject paramInJson, ICmdDataFlowContext dataFlowContext) {
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setCommunityId(paramInJson.getString("communityId"));
        feeConfigDto.setConfigId(paramInJson.getString("configId"));
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
        Assert.listOnlyOne(feeConfigDtos, "未找到该费用项");
        JSONObject businessFeeConfig = new JSONObject();
        businessFeeConfig.putAll(paramInJson);
        businessFeeConfig.put("isDefault", feeConfigDtos.get(0).getIsDefault());
        PayFeeConfigPo payFeeConfigPo = BeanConvertUtil.covertBean(businessFeeConfig, PayFeeConfigPo.class);
        int flag = payFeeConfigV1InnerServiceSMOImpl.updatePayFeeConfig(payFeeConfigPo);
        if (flag < 1) {
            throw new CmdException("修改费用项失败");
        }
    }
}
