package com.java110.common.smartMeter.factory.zhongkong;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.smartMeter.ISmartMeterCoreRead;
import com.java110.common.smartMeter.ISmartMeterFactoryAdapt;
import com.java110.common.smartMeter.factory.tqdianbiao.TdDianBiaoUtil;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.meterMachine.MeterMachineDetailDto;
import com.java110.dto.meterMachine.MeterMachineDto;
import com.java110.dto.meterMachine.MeterMachineSpecDto;
import com.java110.intf.common.IMeterMachineDetailV1InnerServiceSMO;
import com.java110.intf.common.IMeterMachineSpecV1InnerServiceSMO;
import com.java110.intf.common.IMeterMachineV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeConfigV1InnerServiceSMO;
import com.java110.po.meterMachineDetail.MeterMachineDetailPo;
import com.java110.utils.cache.UrlCache;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 中控 充值电表
 */
@Service("zhongkongDianBiaoFactoryAdaptImpl")
public class ZhongkongDianBiaoFactoryAdaptImpl implements ISmartMeterFactoryAdapt {


    private static final String RECHARGE_URL = "http://www.zhckxt.com/api/mjkj-water/mjkj/open/wy/recharge";

    private static final String READ_URL = "http://www.zhckxt.com/api/mjkj-water/mjkj/open/wy/get/amount";

    private static final String READS_URL = "http://www.zhckxt.com/api/mjkj-water/mjkj/open/wy/get/lastNum";

    private static final String NOTIFY_URL = "/app/smartMeter/notify/zhongkongDianBiaoFactoryAdaptImpl/992020051967020024";


    @Autowired
    private IMeterMachineSpecV1InnerServiceSMO meterMachineSpecV1InnerServiceSMOImpl;

    @Autowired
    private IMeterMachineDetailV1InnerServiceSMO meterMachineDetailV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeConfigV1InnerServiceSMO feeConfigV1InnerServiceSMOImpl;

    @Autowired
    private IMeterMachineV1InnerServiceSMO meterMachineV1InnerServiceSMOImpl;

    @Autowired
    private ISmartMeterCoreRead smartMeterCoreReadImpl;


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

        List<Map<String, Object>> req = new ArrayList<>();
        List<MeterMachineDetailPo> meterMachineDetailPos = new ArrayList<>();
        String detailId = "";
        List<String> bhLists = new ArrayList<>();
        for (MeterMachineDto meterMachineDto : meterMachineDtos) {
            detailId = GenerateCodeFactory.getGeneratorId("11");
            bhLists.add(meterMachineDto.getAddress());
            MeterMachineDetailPo meterMachineDetailPo = new MeterMachineDetailPo();
            meterMachineDetailPo.setCommunityId(meterMachineDto.getCommunityId());
            meterMachineDetailPo.setDetailId(detailId);
            meterMachineDetailPo.setMachineId(meterMachineDto.getMachineId());
            meterMachineDetailPo.setDetailType(meterMachineDto.getMachineModel()); // 抄表
            meterMachineDetailPo.setCurDegrees(meterMachineDto.getCurDegrees());
            meterMachineDetailPo.setCurReadingTime(meterMachineDto.getCurReadingTime());
            meterMachineDetailPo.setPrestoreDegrees(meterMachineDto.getPrestoreDegrees());
            meterMachineDetailPo.setState(MeterMachineDetailDto.STATE_W);
            meterMachineDetailPos.add(meterMachineDetailPo);
        }

        Map reqParams = new HashMap();
        reqParams.put("callbackUrl",UrlCache.getOwnerUrl() + NOTIFY_URL);
        reqParams.put("bhList",bhLists);

        String response = WyRequestUtils.executeReads(READS_URL, reqParams, HttpMethod.POST);

        JSONObject paramOut = JSONObject.parseObject(response);

        if (paramOut.getIntValue("code") != 200) {
            return new ResultVo(ResultVo.CODE_ERROR, paramOut.getString("msg"));
        }



        if (meterMachineDetailPos.size() > 0) {
            meterMachineDetailV1InnerServiceSMOImpl.saveMeterMachineDetails(meterMachineDetailPos);
        }

        return new ResultVo(ResultVo.CODE_OK, "请求已发送，等待电表反馈数据");
    }

    @Override
    public ResultVo notifyReadData(String readData) {

        JSONObject data = JSONObject.parseObject(readData);

        MeterMachineDto meterMachineDto = new MeterMachineDto();
        meterMachineDto.setAddress(data.getString("bh"));
        List<MeterMachineDto> meterMachineDtos = meterMachineV1InnerServiceSMOImpl.queryMeterMachines(meterMachineDto);
        Assert.listOnlyOne(meterMachineDtos,"表不存在");

        MeterMachineDetailDto meterMachineDetailDto = new MeterMachineDetailDto();
        meterMachineDetailDto.setCommunityId(meterMachineDtos.get(0).getCommunityId());
        meterMachineDetailDto.setMachineId(meterMachineDtos.get(0).getMachineId());
        meterMachineDetailDto.setState(MeterMachineDetailDto.STATE_W);
        List<MeterMachineDetailDto> meterMachineDetailDtos = meterMachineDetailV1InnerServiceSMOImpl.queryMeterMachineDetails(meterMachineDetailDto);
        if (meterMachineDetailDtos == null || meterMachineDetailDtos.size() < 1) {
            return new ResultVo(ResultVo.CODE_ERROR, "没有数据");
        }

        String batchId = smartMeterCoreReadImpl.generatorBatch(meterMachineDetailDtos.get(0).getCommunityId());
        doBusiness(data, batchId,meterMachineDetailDtos);

        return new ResultVo(ResultVo.CODE_OK, "成功");
    }


    private void doBusiness(JSONObject contentObject, String batchId, List<MeterMachineDetailDto> meterMachineDetailDtos) {

        double degree = contentObject.getDoubleValue("bqds");

        smartMeterCoreReadImpl.saveMeterAndCreateFee(meterMachineDetailDtos.get(0), degree + "", batchId);
    }
}
