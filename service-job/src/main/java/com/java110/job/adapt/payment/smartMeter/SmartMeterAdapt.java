package com.java110.job.adapt.payment.smartMeter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.log.LogSystemErrorDto;
import com.java110.dto.meter.MeterMachineDto;
import com.java110.dto.system.Business;
import com.java110.intf.common.IMeterMachineV1InnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.fee.IFeeDetailInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.job.adapt.hcIotNew.http.ISendIot;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.log.LogSystemErrorPo;
import com.java110.service.smo.ISaveSystemErrorSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ExceptionUtil;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 智能水电表充值
 */
@Component(value = "smartMeterAdapt")
public class SmartMeterAdapt extends DatabusAdaptImpl {
    private static Logger logger = LoggerFactory.getLogger(SmartMeterAdapt.class);

    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IMeterMachineV1InnerServiceSMO meterMachineV1InnerServiceSMOImpl;

    @Autowired
    private ISaveSystemErrorSMO saveSystemErrorSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private ISendIot sendIotImpl;

    public void execute(Business business, List<Business> businesses) {
        JSONObject data = business.getData();

        if (data != null) {
            logger.debug("请求日志:{}", data);
        }
        JSONArray businessPayFeeDetails = null;
        if (data == null) {
            FeeDetailDto feeDetailDto = new FeeDetailDto();
            feeDetailDto.setbId(business.getbId());
            List<FeeDetailDto> feeDetailDtos = feeDetailInnerServiceSMOImpl.queryFeeDetails(feeDetailDto);
            Assert.listOnlyOne(feeDetailDtos, "未查询到缴费记录");
            businessPayFeeDetails = JSONArray.parseArray(JSONArray.toJSONString(feeDetailDtos, SerializerFeature.WriteDateUseDateFormat));
        } else if (data.containsKey(PayFeeDetailPo.class.getSimpleName())) {
            Object bObj = data.get(PayFeeDetailPo.class.getSimpleName());
            if (bObj instanceof JSONObject) {
                businessPayFeeDetails = new JSONArray();
                businessPayFeeDetails.add(bObj);
            } else if (bObj instanceof Map) {
                businessPayFeeDetails = new JSONArray();
                businessPayFeeDetails.add(JSONObject.parseObject(JSONObject.toJSONString(bObj)));
            } else if (bObj instanceof List) {
                businessPayFeeDetails = JSONArray.parseArray(JSONObject.toJSONString(bObj));
            } else {
                businessPayFeeDetails = (JSONArray) bObj;
            }
        } else {
            if (data instanceof JSONObject) {
                businessPayFeeDetails = new JSONArray();
                businessPayFeeDetails.add(data);
            }
        }

        if (businessPayFeeDetails == null) {
            return;
        }
        for (int bPayFeeDetailIndex = 0; bPayFeeDetailIndex < businessPayFeeDetails.size(); bPayFeeDetailIndex++) {
            JSONObject businessPayFeeDetail = businessPayFeeDetails.getJSONObject(bPayFeeDetailIndex);
            doPayFeeDetail(business, businessPayFeeDetail);
        }
    }

    private void doPayFeeDetail(Business business, JSONObject businessPayFeeDetail) {
        try {
            //查询缴费明细
            PayFeeDetailPo payFeeDetailPo = BeanConvertUtil.covertBean(businessPayFeeDetail, PayFeeDetailPo.class);
            FeeDto feeDto = new FeeDto();
            feeDto.setFeeId(payFeeDetailPo.getFeeId());
            feeDto.setCommunityId(payFeeDetailPo.getCommunityId());
            List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

            Assert.listOnlyOne(feeDtos, "未查询到费用信息");

            // todo 水费 电费 煤气费 才能送
            if (!FeeConfigDto.FEE_TYPE_CD_METER.equals(feeDtos.get(0).getFeeTypeCd())
                    && !FeeConfigDto.FEE_TYPE_CD_WATER.equals(feeDtos.get(0).getFeeTypeCd())
                    && !FeeConfigDto.FEE_TYPE_CD_GAS.equals(feeDtos.get(0).getFeeTypeCd())
            ) {
                return;
            }

            //todo 公式必须要用量乘以单机

            FeeConfigDto feeConfigDto = new FeeConfigDto();
            feeConfigDto.setConfigId(feeDtos.get(0).getConfigId());
            feeConfigDto.setComputingFormula("6006");
            List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);

            if (ListUtil.isNull(feeConfigDtos)) {
                return;
            }


            JSONObject reqJson = new JSONObject();
            reqJson.put("roomId", feeDtos.get(0).getPayerObjId());
            reqJson.put("communityId", feeDtos.get(0).getCommunityId());
            reqJson.put("receivedAmount", payFeeDetailPo.getReceivedAmount());
            reqJson.put("feeTypeCd",feeDtos.get(0).getFeeTypeCd());

            reqJson.put("iotApiCode", "preStoreMeterBmoImpl");

            ResultVo resultVo = sendIotImpl.post("/iot/api/common.openCommonApi", reqJson);

            if (resultVo.getCode() != ResultVo.CODE_OK) {
                throw new CmdException(resultVo.getMsg());
            }


        } catch (Exception e) {
            LogSystemErrorPo logSystemErrorPo = new LogSystemErrorPo();
            logSystemErrorPo.setErrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_errId));
            logSystemErrorPo.setErrType(LogSystemErrorDto.ERR_TYPE_NOTICE);
            logSystemErrorPo.setMsg(ExceptionUtil.getStackTrace(e));
            saveSystemErrorSMOImpl.saveLog(logSystemErrorPo);
            logger.error("通知异常", e);
        }
    }
}
