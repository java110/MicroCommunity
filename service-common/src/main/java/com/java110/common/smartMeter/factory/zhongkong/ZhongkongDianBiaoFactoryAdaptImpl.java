package com.java110.common.smartMeter.factory.zhongkong;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.smartMeter.ISmartMeterFactoryAdapt;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.meterMachine.MeterMachineDto;
import com.java110.intf.fee.IPayFeeConfigV1InnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 中控 充值电表
 */
@Service("zhongkongDianBiaoFactoryAdaptImpl")
public class ZhongkongDianBiaoFactoryAdaptImpl implements ISmartMeterFactoryAdapt {

    private static final String RECHARGE_URL = "http://watergateway.dev.mj.ink:9900/mjkj-water/mjkj/open/wy/recharge";

    private static final String READ_URL = "http://watergateway.dev.mj.ink:9900/mjkj-water/mjkj/open/wy/get/amount";

    @Autowired
    private IPayFeeConfigV1InnerServiceSMO feeConfigV1InnerServiceSMOImpl;


    @Override
    public ResultVo requestRecharge(MeterMachineDto meterMachineDto, double degree, double money) {
        Map<String, String> reqMap = new HashMap<>();
        reqMap.put("bh", meterMachineDto.getAddress());
        reqMap.put("amount", money + "");
        String response = WyRequestUtils.execute(RECHARGE_URL, reqMap, HttpMethod.POST);
        JSONObject resJson = JSONObject.parseObject(response);
        if (resJson.getIntValue("code") != 200) {
            return new ResultVo(ResultVo.CODE_ERROR, resJson.getString("msg"));
        }
        return new ResultVo(ResultVo.CODE_OK, resJson.getString("msg"));
    }

    @Override
    public ResultVo requestRead(MeterMachineDto meterMachineDto) {

        String response = WyRequestUtils.execute(READ_URL + "?bh=" + meterMachineDto.getAddress(), null, HttpMethod.GET);
        JSONObject resJson = JSONObject.parseObject(response);
        if (resJson.getIntValue("code") != 200) {
            return new ResultVo(ResultVo.CODE_ERROR, resJson.getString("msg"));
        }

        double money = resJson.getDoubleValue("data");

        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setConfigId(meterMachineDto.getFeeConfigId());
        feeConfigDto.setCommunityId(meterMachineDto.getCommunityId());
        feeConfigDto.setComputingFormula("6006");
        List<FeeConfigDto> feeConfigDtos = feeConfigV1InnerServiceSMOImpl.queryPayFeeConfigs(feeConfigDto);

        if (feeConfigDtos == null || feeConfigDtos.size() < 1) {
            return new ResultVo(ResultVo.CODE_OK, resJson.getString("msg"), -1);
        }

        if (Double.parseDouble(feeConfigDtos.get(0).getSquarePrice()) == 0) {
            return new ResultVo(ResultVo.CODE_OK, resJson.getString("msg"), -1);
        }

        BigDecimal degreeDec = new BigDecimal(money).subtract(new BigDecimal(feeConfigDtos.get(0).getAdditionalAmount()));
        degreeDec = degreeDec.divide(new BigDecimal(Double.parseDouble(feeConfigDtos.get(0).getSquarePrice())), 2, BigDecimal.ROUND_HALF_UP);

        return new ResultVo(ResultVo.CODE_OK, resJson.getString("msg"), degreeDec.doubleValue());
    }

    @Override
    public ResultVo requestReads(List<MeterMachineDto> meterMachineDtos) {
        return null;
    }

    @Override
    public ResultVo notifyReadData(String readData) {
        return null;
    }
}
