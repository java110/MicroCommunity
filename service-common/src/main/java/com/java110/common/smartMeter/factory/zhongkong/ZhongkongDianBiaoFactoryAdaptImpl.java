package com.java110.common.smartMeter.factory.zhongkong;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.smartMeter.ISmartMeterFactoryAdapt;
import com.java110.dto.meterMachine.MeterMachineDto;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

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



    @Override
    public ResultVo requestRecharge(MeterMachineDto meterMachineDto, double degree, double money) {
        Map<String, String> reqMap = new HashMap<>();
        reqMap.put("bh", meterMachineDto.getAddress());
        reqMap.put("amount", money + "");
        String response = WyRequestUtils.execute(RECHARGE_URL,reqMap, HttpMethod.POST);
        JSONObject resJson = JSONObject.parseObject(response);
        if(resJson.getIntValue("code") != 200){
            return new ResultVo(ResultVo.CODE_ERROR,resJson.getString("msg"));
        }
        return new ResultVo(ResultVo.CODE_OK,resJson.getString("msg"));
    }

    @Override
    public ResultVo requestRead(MeterMachineDto meterMachineDto) {

        String response = WyRequestUtils.execute(READ_URL+"?bh="+meterMachineDto.getAddress(),null, HttpMethod.GET);
        JSONObject resJson = JSONObject.parseObject(response);
        if(resJson.getIntValue("code") != 200){
            return new ResultVo(ResultVo.CODE_ERROR,resJson.getString("msg"));
        }
      
        return new ResultVo(ResultVo.CODE_OK,resJson.getString("msg"),resJson.getString("data"));
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
