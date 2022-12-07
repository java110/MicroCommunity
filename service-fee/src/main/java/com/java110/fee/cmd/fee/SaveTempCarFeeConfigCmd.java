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
import com.java110.dto.fee.FeeDto;
import com.java110.dto.parking.ParkingAreaDto;
import com.java110.intf.community.IParkingAreaInnerServiceSMO;
import com.java110.intf.fee.IPayFeeConfigV1InnerServiceSMO;
import com.java110.intf.fee.ITempCarFeeConfigAttrV1InnerServiceSMO;
import com.java110.intf.fee.ITempCarFeeConfigV1InnerServiceSMO;
import com.java110.po.fee.PayFeeConfigPo;
import com.java110.po.tempCarFeeConfig.TempCarFeeConfigPo;
import com.java110.po.tempCarFeeConfigAttr.TempCarFeeConfigAttrPo;
import com.java110.utils.constant.FeeConfigConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Java110Cmd(serviceCode = "fee.saveTempCarFeeConfig")
public class SaveTempCarFeeConfigCmd extends Cmd {

    @Autowired
    private IParkingAreaInnerServiceSMO parkingAreaInnerServiceSMOImpl;

    @Autowired
    private IPayFeeConfigV1InnerServiceSMO payFeeConfigV1InnerServiceSMOImpl;

    @Autowired
    private ITempCarFeeConfigV1InnerServiceSMO tempCarFeeConfigV1InnerServiceSMOImpl;

    @Autowired
    private ITempCarFeeConfigAttrV1InnerServiceSMO tempCarFeeConfigAttrV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "feeName", "请求报文中未包含feeName");
        Assert.hasKeyAndValue(reqJson, "paId", "请求报文中未包含paId");
        Assert.hasKeyAndValue(reqJson, "carType", "请求报文中未包含carType");
        Assert.hasKeyAndValue(reqJson, "ruleId", "请求报文中未包含ruleId");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含startTime");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含endTime");
        //查询停车场编号
        ParkingAreaDto parkingAreaDto = new ParkingAreaDto();
        parkingAreaDto.setPaId(reqJson.getString("paId"));
        parkingAreaDto.setCommunityId(reqJson.getString("communityId"));
        List<ParkingAreaDto> parkingAreaDtos = parkingAreaInnerServiceSMOImpl.queryParkingAreas(parkingAreaDto);
        Assert.listOnlyOne(parkingAreaDtos, "停车场不存在");
        reqJson.put("areaNum", parkingAreaDtos.get(0).getNum());
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        reqJson.put("feeConfigId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_configId));
        reqJson.put("configId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_configId));
        TempCarFeeConfigPo tempCarFeeConfigPo = BeanConvertUtil.covertBean(reqJson, TempCarFeeConfigPo.class);
        //处理房屋属性
        dealAttr(reqJson, context);
        int flag = tempCarFeeConfigV1InnerServiceSMOImpl.saveTempCarFeeConfig(tempCarFeeConfigPo);
        if (flag < 1) {
            throw new CmdException("保存临时收费失败");
        }
        //补费用项数据
        PayFeeConfigPo payFeeConfigPo = new PayFeeConfigPo();
        payFeeConfigPo.setAdditionalAmount("0");
        payFeeConfigPo.setBillType(FeeConfigDto.BILL_TYPE_YEAR);
        payFeeConfigPo.setCommunityId(reqJson.getString("communityId"));
        payFeeConfigPo.setComputingFormula(FeeConfigDto.COMPUTING_FORMULA_DYNAMIC);
        payFeeConfigPo.setComputingFormulaText("");
        payFeeConfigPo.setConfigId(reqJson.getString("feeConfigId"));
        payFeeConfigPo.setEndTime(reqJson.getString("endTime"));
        payFeeConfigPo.setStartTime(reqJson.getString("startTime"));
        payFeeConfigPo.setFeeFlag(FeeDto.FEE_FLAG_ONCE);
        payFeeConfigPo.setFeeName(reqJson.getString("feeName"));
        payFeeConfigPo.setFeeTypeCd(FeeConfigDto.FEE_TYPE_CD_PARKING);
        payFeeConfigPo.setIsDefault(FeeConfigDto.DEFAULT_FEE_CONFIG);
        payFeeConfigPo.setPaymentCd(FeeConfigDto.PAYMENT_CD_PRE);
        payFeeConfigPo.setPaymentCycle("1");
        payFeeConfigPo.setSquarePrice("0");
        payFeeConfigPo.setDeductFrom(FeeConfigDto.DEDUCT_FROM_N);
        payFeeConfigPo.setDecimalPlace("2");
        payFeeConfigPo.setScale("1");
        payFeeConfigPo.setUnits("元");
        payFeeConfigPo.setPayOnline("Y");
        flag = payFeeConfigV1InnerServiceSMOImpl.savePayFeeConfig(payFeeConfigPo);
        if (flag < 1) {
            throw new CmdException("保存临时收费失败");
        }
    }

    private void dealAttr(JSONObject reqJson, ICmdDataFlowContext context) {
        if (!reqJson.containsKey("attrs")) {
            return;
        }
        JSONArray attrs = reqJson.getJSONArray("attrs");
        if (attrs == null || attrs.size() < 1) {
            return;
        }
        JSONObject attr = null;
        int flag = 0;
        for (int attrIndex = 0; attrIndex < attrs.size(); attrIndex++) {
            attr = attrs.getJSONObject(attrIndex);
            attr.put("configId", reqJson.getString("configId"));
            attr.put("communityId", reqJson.getString("communityId"));
            attr.put("attrId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
            TempCarFeeConfigAttrPo tempCarFeeConfigAttrPo = BeanConvertUtil.covertBean(attr, TempCarFeeConfigAttrPo.class);
            flag = tempCarFeeConfigAttrV1InnerServiceSMOImpl.saveTempCarFeeConfigAttr(tempCarFeeConfigAttrPo);
            if (flag < 1) {
                throw new CmdException("保存临时收费失败");
            }
        }
    }
}
