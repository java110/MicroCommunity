package com.java110.fee.bmo.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.repair.RepairDto;
import com.java110.fee.bmo.IPayOweFee;
import com.java110.fee.listener.fee.UpdateFeeInfoListener;
import com.java110.intf.community.IRepairInnerServiceSMO;
import com.java110.intf.fee.IFeeAttrInnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.fee.IFeeDetailInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.owner.RepairPoolPo;
import com.java110.utils.constant.FeeFlagTypeConstant;
import com.java110.utils.constant.FeeStateConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.lock.DistributedLock;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 欠费缴费实现类
 */
@Service
public class PayOweFeeImpl implements IPayOweFee {

    private static Logger logger = LoggerFactory.getLogger(UpdateFeeInfoListener.class);


    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMOImpl;

    /**
     * 欠费缴费
     *
     * @param reqJson 缴费报文 {"communityId":"7020181217000001","fees":[{"feeId":"902020073149140091","feePrice":90},{"feeId":"902020072844020741","feePrice":1500}]}
     * @return
     */
    @Override
    @Java110Transactional
    public ResponseEntity<String> pay(JSONObject reqJson) {

        //小区ID
        String communityId = reqJson.getString("communityId");

        JSONArray fees = reqJson.getJSONArray("fees");

        JSONObject feeObj = null;

        for (int feeIndex = 0; feeIndex < fees.size(); feeIndex++) {
            feeObj = fees.getJSONObject(feeIndex);
            Assert.hasKeyAndValue(feeObj, "feeId", "未包含费用项ID");
            Assert.hasKeyAndValue(feeObj, "feeId", "未包含费用项ID");

            feeObj.put("communityId", communityId);
            doPayOweFee(feeObj);
        }
        return ResultVo.success();
    }

    private void doPayOweFee(JSONObject feeObj) {
        //开启全局锁
        String requestId = DistributedLock.getLockUUID();
        String key = this.getClass().getSimpleName() + feeObj.get("feeId");
        try {
            DistributedLock.waitGetDistributedLock(key, requestId);

            addFeeDetail(feeObj);

            modifyFee(feeObj);

            //判断是否有派单属性ID
            FeeAttrDto feeAttrDto = new FeeAttrDto();
            feeAttrDto.setCommunityId(feeObj.getString("communityId"));
            feeAttrDto.setFeeId(feeObj.getString("feeId"));
            feeAttrDto.setSpecCd(FeeAttrDto.SPEC_CD_REPAIR);
            List<FeeAttrDto> feeAttrDtos = feeAttrInnerServiceSMOImpl.queryFeeAttrs(feeAttrDto);
            //修改 派单状态
            if (feeAttrDtos != null && feeAttrDtos.size() > 0) {
                RepairPoolPo repairPoolPo = new RepairPoolPo();
                repairPoolPo.setRepairId(feeAttrDtos.get(0).getValue());
                repairPoolPo.setCommunityId(feeObj.getString("communityId"));
                repairPoolPo.setState(RepairDto.STATE_APPRAISE);
                int saved = repairInnerServiceSMOImpl.updateRepair(repairPoolPo);
                if (saved < 1) {
                    throw new IllegalArgumentException("缴费后修改报修单失败");
                }
            }
        } catch (Exception e) {
            logger.error("缴费失败", e);
            throw new IllegalArgumentException("缴费失败" + e);
        } finally {
            DistributedLock.releaseDistributedLock(requestId, key);
        }
    }

    /**
     * @param feeObj
     */
    private void modifyFee(JSONObject feeObj) throws ParseException {

        PayFeePo payFeePo = new PayFeePo();
        FeeDto feeInfo = (FeeDto) feeObj.get("feeInfo");
        Date endTime = feeInfo.getEndTime();
        Calendar endCalender = Calendar.getInstance();
        endCalender.setTime(endTime);
        int hours = 0;
        hours = new Double(Double.parseDouble(feeObj.getString("tmpCycles")) * DateUtil.getCurrentMonthDay() * 24).intValue();
        endCalender.add(Calendar.HOUR, hours);
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setConfigId(feeInfo.getConfigId());
        feeConfigDto.setCommunityId(feeInfo.getCommunityId());
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);

        Assert.listOnlyOne(feeConfigDtos, "未找到费用配置");
        payFeePo.setEndTime(DateUtil.getFormatTimeString(endCalender.getTime(), DateUtil.DATE_FORMATE_STRING_A));
        // 一次性收费类型，缴费后，则设置费用状态为收费结束、设置结束日期为费用项终止日期
        if (FeeFlagTypeConstant.ONETIME.equals(feeConfigDtos.get(0).getFeeFlag())) {
            payFeePo.setState(FeeStateConstant.END);
            payFeePo.setEndTime(feeConfigDtos.get(0).getEndTime());
        }
        // 周期性收费、缴费后，到期日期在费用项终止日期后，则设置缴费状态结束，设置结束日期为费用项终止日期
        if (FeeFlagTypeConstant.CYCLE.equals(feeConfigDtos.get(0).getFeeFlag())) {
            if ((feeInfo.getEndTime()).after(DateUtil.getDateFromString(feeConfigDtos.get(0).getEndTime(), DateUtil.DATE_FORMATE_STRING_A))) {
                payFeePo.setState(FeeStateConstant.END);
                payFeePo.setEndTime(feeConfigDtos.get(0).getEndTime());
            }
        }
        payFeePo.setFeeId(feeObj.getString("feeId"));
        payFeePo.setEndTime(DateUtil.getFormatTimeString(feeInfo.getEndTime(), DateUtil.DATE_FORMATE_STRING_A));
        payFeePo.setCommunityId(feeObj.getString("communityId"));
        int saveFlag = feeInnerServiceSMOImpl.updateFee(payFeePo);
        if (saveFlag < 1) {
            throw new IllegalArgumentException("缴费失败" + payFeePo.toString());
        }
    }

    /**
     * 添加 费用缴费明细
     *
     * @param paramInJson
     */
    private void addFeeDetail(JSONObject paramInJson) {

        PayFeeDetailPo payFeeDetailPo = new PayFeeDetailPo();
        payFeeDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        payFeeDetailPo.setPrimeRate("1.00");
        //计算 应收金额
        FeeDto feeDto = new FeeDto();
        feeDto.setFeeId(paramInJson.getString("feeId"));
        feeDto.setCommunityId(paramInJson.getString("communityId"));
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

        if (feeDtos == null || feeDtos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "查询费用信息失败，未查到数据或查到多条数据");
        }

        feeDto = feeDtos.get(0);
        paramInJson.put("feeInfo", feeDto);

        BigDecimal feePrice = new BigDecimal("0.00");

        BigDecimal receivedAmount = new BigDecimal(Double.parseDouble(paramInJson.getString("receivedAmount")));
        BigDecimal cycles = receivedAmount.divide(feePrice, 2, BigDecimal.ROUND_HALF_EVEN);
        paramInJson.put("tmpCycles", cycles);
        payFeeDetailPo.setCommunityId(paramInJson.getString("communityId"));
        payFeeDetailPo.setCycles(cycles.doubleValue() + "");
        payFeeDetailPo.setReceivableAmount(receivedAmount.doubleValue() + "");
        payFeeDetailPo.setReceivedAmount(receivedAmount.doubleValue() + "");

        int saveFeeDetail = feeDetailInnerServiceSMOImpl.saveFeeDetail(payFeeDetailPo);

        if (saveFeeDetail < 1) {
            throw new IllegalArgumentException("保存费用详情失败" + payFeeDetailPo.toString());
        }
    }
}
