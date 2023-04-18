package com.java110.common.smartMeter.factory.tqdianbiao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.smartMeter.ISmartMeterCoreRead;
import com.java110.common.smartMeter.ISmartMeterFactoryAdapt;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.meterMachine.MeterMachineDto;
import com.java110.dto.meterMachine.MeterMachineDetailDto;
import com.java110.dto.meterMachine.MeterMachineSpecDto;
import com.java110.intf.common.IMeterMachineDetailV1InnerServiceSMO;
import com.java110.intf.common.IMeterMachineSpecV1InnerServiceSMO;
import com.java110.intf.common.IMeterMachineV1InnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.meterMachineDetail.MeterMachineDetailPo;
import com.java110.utils.cache.UrlCache;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 拓强智能电表 处理类 -- 5.3 单项远程单费率预付费表
 */
@Service("tqDianBiaoRemoteDanFeiLvPreFactoryAdaptImpl")
public class TqDianBiaoRemoteDanFeiLvPreFactoryAdaptImpl implements ISmartMeterFactoryAdapt {

    private static final String READ_URL = "http://api2.tqdianbiao.com/Api_v2/ele_read";

    private static final String RECHARGE_URL = "http://api2.tqdianbiao.com/Api_v2/ele_security/recharge";

    private static final String NOTIFY_URL = "/app/smartMeter/notify/tqDianBiaoRemoteDanFeiLvPreFactoryAdaptImpl/992020051967020024";
    @Autowired
    private IMeterMachineSpecV1InnerServiceSMO meterMachineSpecV1InnerServiceSMOImpl;

    @Autowired
    private IMeterMachineV1InnerServiceSMO meterMachineV1InnerServiceSMOImpl;

    @Autowired
    private IMeterMachineDetailV1InnerServiceSMO meterMachineDetailV1InnerServiceSMOImpl;


    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;


    @Autowired
    private ISmartMeterCoreRead smartMeterCoreReadImpl;


    @Override
    public ResultVo requestRecharge(MeterMachineDto meterMachineDto, double degree, double money) {


        // 请求内容，调用接口所需要的数据（电表充值，同步模式）
        List<Map<String, Object>> req = new ArrayList<>();

        MeterMachineSpecDto meterMachineSpecDto = new MeterMachineSpecDto();
        meterMachineSpecDto.setMachineId(meterMachineDto.getMachineId());
        meterMachineSpecDto.setCommunityId(meterMachineDto.getCommunityId());
        meterMachineSpecDto.setSpecId("120102");
        List<MeterMachineSpecDto> meterMachineSpecDtos = meterMachineSpecV1InnerServiceSMOImpl.queryMeterMachineSpecs(meterMachineSpecDto);
        if (meterMachineSpecDtos == null || meterMachineSpecDtos.size() < 1) {
            return new ResultVo(ResultVo.CODE_ERROR, "未配置采集器ID");
        }
        String detailId = GenerateCodeFactory.getGeneratorId("11");
        Map<String, Object> item = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        params.put("money", money + "");

        item.put("opr_id", detailId);
        item.put("time_out", 0);
        item.put("must_online", true);
        item.put("retry_times", 1);
        item.put("cid", meterMachineSpecDtos.get(0).getSpecValue());
        item.put("address", meterMachineDto.getAddress());
        item.put("params", params);
        req.add(item);


        String request_content = JSON.toJSONString(req);

        String resStr = TdDianBiaoUtil.requestAsync(RECHARGE_URL, request_content, UrlCache.getOwnerUrl() + NOTIFY_URL);

        JSONObject paramOut = JSONObject.parseObject(resStr);

        if (!"SUCCESS".equals(paramOut.getString("status"))) {
            return new ResultVo(ResultVo.CODE_ERROR, paramOut.getString("error_msg"));
        }

        JSONArray responseContent = paramOut.getJSONArray("response_content");

        for (int resIndex = 0; resIndex < responseContent.size(); resIndex++) {
            String status = responseContent.getJSONObject(resIndex).getString("status");
            if (!"SUCCESS".equals(status)) {
                return new ResultVo(ResultVo.CODE_ERROR, responseContent.getJSONObject(resIndex).getString("error_msg"));
            }
        }

        List<MeterMachineDetailPo> meterMachineDetailPos = new ArrayList<>();
        MeterMachineDetailPo meterMachineDetailPo = new MeterMachineDetailPo();
        meterMachineDetailPo.setCommunityId(meterMachineDto.getCommunityId());
        meterMachineDetailPo.setDetailId(detailId);
        meterMachineDetailPo.setMachineId(meterMachineDto.getMachineId());
        meterMachineDetailPo.setDetailType(meterMachineDto.getMachineModel()); // 抄表
        meterMachineDetailPo.setCurDegrees((Double.parseDouble(meterMachineDto.getCurDegrees()) + degree) + "");
        meterMachineDetailPo.setCurReadingTime(meterMachineDto.getCurReadingTime());
        meterMachineDetailPo.setPrestoreDegrees((Double.parseDouble(meterMachineDto.getCurDegrees()) + degree) + "");
        meterMachineDetailPo.setState(MeterMachineDetailDto.STATE_C);
        meterMachineDetailPos.add(meterMachineDetailPo);

        if (meterMachineDetailPos.size() > 0) {
            meterMachineDetailV1InnerServiceSMOImpl.saveMeterMachineDetails(meterMachineDetailPos);
        }
        return new ResultVo(ResultVo.CODE_OK, "请求已发送，等待电表反馈数据");
    }

    @Override
    public ResultVo requestRead(MeterMachineDto meterMachineDto) {
        List<Map<String, Object>> req = new ArrayList<>();


        MeterMachineSpecDto meterMachineSpecDto = new MeterMachineSpecDto();
        meterMachineSpecDto.setMachineId(meterMachineDto.getMachineId());
        meterMachineSpecDto.setCommunityId(meterMachineDto.getCommunityId());
        meterMachineSpecDto.setSpecId("120102");
        List<MeterMachineSpecDto> meterMachineSpecDtos = meterMachineSpecV1InnerServiceSMOImpl.queryMeterMachineSpecs(meterMachineSpecDto);
        if (meterMachineSpecDtos == null || meterMachineSpecDtos.size() < 1) {
            return new ResultVo(ResultVo.CODE_ERROR, "未配置采集器ID");
        }
        String detailId = GenerateCodeFactory.getGeneratorId("11");
        Map<String, Object> item = new HashMap<>();
        item.put("opr_id", detailId);
        item.put("time_out", 0);
        item.put("must_online", true);
        item.put("retry_times", 1);
        item.put("cid", meterMachineSpecDtos.get(0).getSpecValue());
        item.put("address", meterMachineDto.getAddress());
        item.put("type", 3);
        req.add(item);
        List<MeterMachineDetailPo> meterMachineDetailPos = new ArrayList<>();
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

        String request_content = JSON.toJSONString(req);

        String resStr = TdDianBiaoUtil.requestAsync(READ_URL, request_content, UrlCache.getOwnerUrl() + NOTIFY_URL);

        JSONObject paramOut = JSONObject.parseObject(resStr);

        if (!"SUCCESS".equals(paramOut.getString("status"))) {
            return new ResultVo(ResultVo.CODE_ERROR, paramOut.getString("error_msg"));
        }

        JSONArray responseContent = paramOut.getJSONArray("response_content");

        for (int resIndex = 0; resIndex < responseContent.size(); resIndex++) {
            String status = responseContent.getJSONObject(resIndex).getString("status");
            if (!"SUCCESS".equals(status)) {
                return new ResultVo(ResultVo.CODE_ERROR, responseContent.getJSONObject(resIndex).getString("error_msg"));
            }
        }
        if (meterMachineDetailPos.size() > 0) {
            meterMachineDetailV1InnerServiceSMOImpl.saveMeterMachineDetails(meterMachineDetailPos);
        }
        return new ResultVo(ResultVo.CODE_OK, "提交重置");
    }

    @Override
    public ResultVo requestReads(List<MeterMachineDto> meterMachineDtos) {
        List<Map<String, Object>> req = new ArrayList<>();
        List<MeterMachineDetailPo> meterMachineDetailPos = new ArrayList<>();
        String detailId = "";
        for (MeterMachineDto meterMachineDto : meterMachineDtos) {

            MeterMachineSpecDto meterMachineSpecDto = new MeterMachineSpecDto();
            meterMachineSpecDto.setMachineId(meterMachineDto.getMachineId());
            meterMachineSpecDto.setCommunityId(meterMachineDto.getCommunityId());
            meterMachineSpecDto.setSpecId("120102");
            List<MeterMachineSpecDto> meterMachineSpecDtos = meterMachineSpecV1InnerServiceSMOImpl.queryMeterMachineSpecs(meterMachineSpecDto);
            if (meterMachineSpecDtos == null || meterMachineSpecDtos.size() < 1) {
                continue;
            }
            detailId = GenerateCodeFactory.getGeneratorId("11");
            Map<String, Object> item = new HashMap<>();
            item.put("opr_id", detailId);
            item.put("time_out", 0);
            item.put("must_online", true);
            item.put("retry_times", 1);
            item.put("cid", meterMachineSpecDtos.get(0).getSpecValue());
            item.put("address", meterMachineDto.getAddress());
            item.put("type", 3);
            req.add(item);

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

        String request_content = JSON.toJSONString(req);

        String resStr = TdDianBiaoUtil.requestAsync(READ_URL, request_content, UrlCache.getOwnerUrl() + NOTIFY_URL);

        JSONObject paramOut = JSONObject.parseObject(resStr);

        if (!"SUCCESS".equals(paramOut.getString("status"))) {
            return new ResultVo(ResultVo.CODE_ERROR, paramOut.getString("error_msg"));
        }

        JSONArray responseContent = JSONArray.parseArray(paramOut.getString("response_content"));

        for (int resIndex = 0; resIndex < responseContent.size(); resIndex++) {
            String status = responseContent.getJSONObject(resIndex).getString("status");
            if (!"SUCCESS".equals(status)) {
                return new ResultVo(ResultVo.CODE_ERROR, responseContent.getJSONObject(resIndex).getString("error_msg"));
            }
        }

        if (meterMachineDetailPos.size() > 0) {
            meterMachineDetailV1InnerServiceSMOImpl.saveMeterMachineDetails(meterMachineDetailPos);
        }

        return new ResultVo(ResultVo.CODE_OK, "请求已发送，等待电表反馈数据");
    }

    @Override
    public ResultVo notifyReadData(String readData) {
        JSONObject data = JSONObject.parseObject(readData);

        String response_content = data.getString("response_content");
        String timestamp = data.getString("timestamp");
        String sign = data.getString("sign");
// 验签
        if (!TdDianBiaoUtil.checkSign(response_content, timestamp, sign)) {
            System.out.println("sign check failed");
            return new ResultVo(ResultVo.CODE_ERROR, "鉴权失败");
        }

        JSONArray contentArray = JSON.parseArray(response_content);

        if (contentArray == null || contentArray.size() < 1) {
            return new ResultVo(ResultVo.CODE_ERROR, "没有数据");
        }

        MeterMachineDetailDto meterMachineDetailDto = new MeterMachineDetailDto();
        meterMachineDetailDto.setDetailId(contentArray.getJSONObject(0).getString("opr_id"));
        meterMachineDetailDto.setState(MeterMachineDetailDto.STATE_W);
        List<MeterMachineDetailDto> meterMachineDetailDtos = meterMachineDetailV1InnerServiceSMOImpl.queryMeterMachineDetails(meterMachineDetailDto);
        if (meterMachineDetailDtos == null || meterMachineDetailDtos.size() < 1) {
            return new ResultVo(ResultVo.CODE_ERROR, "没有数据");
        }

        String batchId = smartMeterCoreReadImpl.generatorBatch(meterMachineDetailDtos.get(0).getCommunityId());


        for (int i = 0; i < contentArray.size(); ++i) {
            try {
                JSONObject contentObject = contentArray.getJSONObject(i);
                doBusiness(contentObject, batchId);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        return new ResultVo(ResultVo.CODE_OK, "成功");
    }

    private void doBusiness(JSONObject contentObject, String batchId) {
        MeterMachineDetailDto meterMachineDetailDto;
        meterMachineDetailDto = new MeterMachineDetailDto();
        meterMachineDetailDto.setDetailId(contentObject.getString("opr_id"));
        meterMachineDetailDto.setState(MeterMachineDetailDto.STATE_W);
        List<MeterMachineDetailDto> meterMachineDetailDtos = meterMachineDetailV1InnerServiceSMOImpl.queryMeterMachineDetails(meterMachineDetailDto);
        if (meterMachineDetailDtos == null || meterMachineDetailDtos.size() < 1) {
            return;
        }

        if (!"SUCCESS".equals(contentObject.getString("status"))) {
            return;
        }

        double degree = contentObject.getJSONArray("data").getJSONObject(0).getJSONArray("value").getDouble(0);

        smartMeterCoreReadImpl.saveMeterAndCreateFee(meterMachineDetailDtos.get(0), degree + "", batchId);
    }

}

