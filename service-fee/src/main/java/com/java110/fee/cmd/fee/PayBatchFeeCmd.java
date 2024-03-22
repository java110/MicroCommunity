package com.java110.fee.cmd.fee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.factory.Java110TransactionalFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.account.AccountDto;
import com.java110.dto.fee.*;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.dto.user.UserDto;
import com.java110.fee.bmo.fee.IFinishFeeNotify;
import com.java110.intf.acct.IAccountInnerServiceSMO;
import com.java110.intf.community.*;
import com.java110.intf.fee.*;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.car.OwnerCarPo;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.owner.RepairPoolPo;
import com.java110.po.owner.RepairUserPo;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.constant.FeeFlagTypeConstant;
import com.java110.utils.constant.FeeConfigConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.lock.DistributedLock;
import com.java110.utils.util.*;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

@Java110Cmd(serviceCode = "fee.payBatchFee")
public class PayBatchFeeCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(PayBatchFeeCmd.class);


    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;
    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;
    @Autowired
    private IPayFeeDetailV1InnerServiceSMO payFeeDetailNewV1InnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IFeeReceiptInnerServiceSMO feeReceiptInnerServiceSMOImpl;

    @Autowired
    private IFinishFeeNotify finishFeeNotifyImpl;

    @Autowired
    private IAccountInnerServiceSMO accountInnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId节点");

        if (!reqJson.containsKey("fees")) {
            throw new CmdException("未包含费用");
        }

        JSONArray fees = reqJson.getJSONArray("fees");

        if (ListUtil.isNull(fees)) {
            throw new CmdException("未包含费用");
        }

        //todo 查询用户信息
        String userId = cmdDataFlowContext.getReqHeaders().get("user-id");
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        Assert.listOnlyOne(userDtos, "用户未登录");


        JSONObject paramInObj = null;
        List<PayFeeDataDto> feeDataDtos = new ArrayList<>();
        PayFeeDataDto payFeeDataDto = null;
        for (int feeIndex = 0; feeIndex < fees.size(); feeIndex++) {
            paramInObj = fees.getJSONObject(feeIndex);
            Assert.hasKeyAndValue(paramInObj, "feeId", "未包含费用ID");
            Assert.hasKeyAndValue(paramInObj, "tempCycle", "未包含缴费周期类型");
            Assert.hasKeyAndValue(paramInObj, "primeRate", "未包含缴费缴费方式");
            Assert.hasKeyAndValue(paramInObj, "receivedAmount", "未包含实收金额");
            //todo 判断是否 费用状态为缴费结束
            FeeDto feeDto = new FeeDto();
            feeDto.setFeeId(paramInObj.getString("feeId"));
            feeDto.setCommunityId(reqJson.getString("communityId"));
            List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
            Assert.listOnlyOne(feeDtos, "传入费用ID错误");
            if (FeeDto.STATE_FINISH.equals(feeDtos.get(0).getState())) {
                throw new IllegalArgumentException("收费已经结束，不能再缴费");
            }
            Date endTime = feeDtos.get(0).getEndTime();
            FeeConfigDto feeConfigDto = new FeeConfigDto();
            feeConfigDto.setConfigId(feeDtos.get(0).getConfigId());
            feeConfigDto.setCommunityId(paramInObj.getString("communityId"));
            List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
            if (ListUtil.isNull(feeConfigDtos)) {
                throw new IllegalArgumentException("费用项不存在");
            }
            Date maxEndTime = feeDtos.get(0).getDeadlineTime();
            //周期性费用
            if (maxEndTime == null || FeeDto.FEE_FLAG_CYCLE.equals(feeConfigDtos.get(0).getFeeFlag())) {
                maxEndTime = DateUtil.getDateFromStringA(feeConfigDtos.get(0).getEndTime());
            }

            if (maxEndTime != null && endTime != null && !FeeDto.FEE_FLAG_ONCE.equals(feeDtos.get(0).getFeeFlag())) {
                Date newDate = DateUtil.stepMonth(endTime, paramInObj.getDouble("cycles").intValue());
                if (newDate.getTime() > maxEndTime.getTime()) {
                    throw new IllegalArgumentException("缴费周期超过 缴费结束时间,请用按结束时间方式缴费");
                }
            }
            //todo 封装数据
            payFeeDataDto = new PayFeeDataDto();
            payFeeDataDto.setFeeDto(feeDtos.get(0));
            payFeeDataDto.setFeeConfigDto(feeConfigDtos.get(0));
            payFeeDataDto.setCommunityId(reqJson.getString("communityId"));
            payFeeDataDto.setTempCycle(paramInObj.getString("tempCycle"));
            payFeeDataDto.setCustEndTime(paramInObj.getString("custEndTime"));
            payFeeDataDto.setPrimeRate(paramInObj.getString("primeRate"));
            payFeeDataDto.setRemark(paramInObj.getString("remark"));
            payFeeDataDto.setReceivedAmount(paramInObj.getString("receivedAmount"));
            payFeeDataDto.setCashierId(userDtos.get(0).getUserId());
            payFeeDataDto.setCashierName(userDtos.get(0).getName());
            payFeeDataDto.setFeeId(paramInObj.getString("feeId"));
            payFeeDataDto.setCycles(paramInObj.getString("cycles"));
            feeDataDtos.add(payFeeDataDto);
        }

        //todo 账户抵扣
        ifHasAccount(reqJson, feeDataDtos);

        reqJson.put("feeDataDtos", feeDataDtos);


    }


    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        //todo 封装好的参数
        List<PayFeeDataDto> feeDataDtos = (List<PayFeeDataDto>) reqJson.get("feeDataDtos");
        String communityId = reqJson.getString("communityId");
        String payOrderId = reqJson.getString("payOrderId");

        //todo 生成收据编号
        String receiptCode = feeReceiptInnerServiceSMOImpl.generatorReceiptCode(communityId);
        JSONArray details = new JSONArray();
        //获取订单ID
        String oId = Java110TransactionalFactory.getOId();

        for (PayFeeDataDto payFeeDataDto : feeDataDtos) {
            payFeeDataDto.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
            payFeeDataDto.setPayOrderId(oId);
            // todo 如果 扫码枪支付 输入支付订单ID
            if (!StringUtil.isEmpty(payOrderId)) {
                payFeeDataDto.setPayOrderId(payOrderId);
            }

            String requestId = DistributedLock.getLockUUID();
            String key = this.getClass().getSimpleName() + payFeeDataDto.getFeeId();
            DistributedLock.waitGetDistributedLock(key, requestId);
            try {
                doPayFee(payFeeDataDto, receiptCode);
            } finally {
                DistributedLock.releaseDistributedLock(key, requestId);
            }
            details.add(payFeeDataDto.getDetailId());
        }

        JSONObject data = new JSONObject();
        data.put("details", details);

        cmdDataFlowContext.setResponseEntity(ResultVo.createResponseEntity(data));
    }

    private void doPayFee(PayFeeDataDto payFeeDataDto, String receiptCode) {


        PayFeeDetailPo payFeeDetailPo = payFeeDataDto;
        payFeeDetailPo.setOpenInvoice("N");
        payFeeDetailPo.setState(FeeDetailDto.STATE_NORMAL);
        payFeeDetailPo.setStartTime(DateUtil.getFormatTimeStringA(payFeeDataDto.getFeeDto().getEndTime()));

        //todo 计算 结束时间 应收  缴费周期
        /**
         *         payFeeDetailPo.setCycles(cycles.doubleValue() + "");
         *         payFeeDetailPo.setEndTime(DateUtil.getFormatTimeStringA(targetEndTime));
         *         payFeeDetailPo.setReceivableAmount(receivableAmount);
         *         payFeeDetailPo.setPayableAmount(receivableAmount);
         */
        computeEndTimeCycleAmount(payFeeDataDto, payFeeDetailPo);
        payFeeDetailPo.setAcctAmount(payFeeDataDto.getAcctAmount());

        //todo 缓存收据编号
        CommonCache.setValue(payFeeDetailPo.getDetailId() + CommonCache.RECEIPT_CODE, receiptCode, CommonCache.DEFAULT_EXPIRETIME_TWO_MIN);
        int flag = payFeeDetailNewV1InnerServiceSMOImpl.savePayFeeDetailNew(payFeeDetailPo);
        if (flag < 1) {
            throw new CmdException("缴费失败");
        }

        PayFeePo payFeePo = new PayFeePo();
        payFeePo.setFeeId(payFeeDataDto.getFeeId());
        payFeePo.setEndTime(payFeeDetailPo.getEndTime());
        payFeePo.setState(FeeDto.STATE_FINISH);

        Date maxTime = payFeeDataDto.getFeeDto().getDeadlineTime();

        if (maxTime == null) {
            maxTime = DateUtil.getDateFromStringA(payFeeDataDto.getFeeConfigDto().getEndTime());
        }
        //todo 不是一次性费用
        if (!FeeDto.FEE_FLAG_ONCE.equals(payFeeDataDto.getFeeDto().getFeeFlag()) && maxTime != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(maxTime);
            calendar.add(Calendar.DAY_OF_MONTH, -5);
            maxTime = calendar.getTime();
            if (maxTime.after(DateUtil.getDateFromStringA(payFeeDetailPo.getEndTime()))) {
                payFeePo.setState(FeeDto.STATE_DOING);
            }
        }

        flag = payFeeV1InnerServiceSMOImpl.updatePayFee(payFeePo);
        if (flag < 1) {
            throw new CmdException("缴费失败");
        }

        //todo 账户扣款
        finishFeeNotifyImpl.withholdAccount(payFeeDataDto, payFeeDataDto.getFeeId(), payFeeDataDto.getCommunityId());

        //todo 修改车辆
        finishFeeNotifyImpl.updateCarEndTime(payFeeDataDto.getFeeId(), payFeeDataDto.getCommunityId());


        //todo 修改报修单
        finishFeeNotifyImpl.updateRepair(payFeeDataDto.getFeeId(), payFeeDataDto.getCommunityId(), payFeeDetailPo.getReceivedAmount());

    }

    /**
     * 计算 结束时间 缴费周期 金额
     * payFeeDetailPo.setCycles(cycles.doubleValue() + "");
     * payFeeDetailPo.setEndTime(DateUtil.getFormatTimeStringA(targetEndTime));
     * payFeeDetailPo.setReceivableAmount(receivableAmount);
     *
     * @param payFeeDataDto
     * @param payFeeDetailPo
     */
    private void computeEndTimeCycleAmount(PayFeeDataDto payFeeDataDto, PayFeeDetailPo payFeeDetailPo) {
        Date targetEndTime = null;
        BigDecimal cycles = null;
        String receivableAmount = "";
        Map feePriceAll = computeFeeSMOImpl.getFeePrice(payFeeDataDto.getFeeDto());
        BigDecimal feePrice = new BigDecimal(feePriceAll.get("feePrice").toString());
        if (PayFeeDataDto.TEMP_CYCLE_CUSTOM_AMOUNT.equals(payFeeDataDto.getTempCycle())) { //todo  自定义金额交费
            Date endTime = payFeeDataDto.getFeeDto().getEndTime();
            Calendar endCalender = Calendar.getInstance();
            endCalender.setTime(endTime);
            BigDecimal receivedAmount = new BigDecimal(Double.parseDouble(payFeeDataDto.getReceivedAmount()));
            //todo 考虑账户金额
            if (payFeeDataDto.getAccountAmount() > 0) {
                receivedAmount = receivedAmount.add(new BigDecimal(payFeeDataDto.getAccountAmount()));
            }
            cycles = receivedAmount.divide(feePrice, 4, BigDecimal.ROUND_HALF_EVEN);
            endCalender = getTargetEndTime(endCalender, cycles.doubleValue());
            targetEndTime = endCalender.getTime();
            receivableAmount = payFeeDataDto.getReceivedAmount();
            //处理 可能还存在 实收手工减免的情况
        } else if (PayFeeDataDto.TEMP_CYCLE_CUSTOM_END_TIME.equals(payFeeDataDto.getTempCycle())) { //todo 这里按缴费结束时间缴费
            String custEndTime = payFeeDataDto.getCustEndTime();
            Date endDates = DateUtil.getDateFromStringB(custEndTime);
            Calendar c = Calendar.getInstance();
            c.setTime(endDates);
            c.add(Calendar.DAY_OF_MONTH, 1);
            endDates = c.getTime();//这是明天
            targetEndTime = endDates;
            BigDecimal receivedAmount1 = new BigDecimal(Double.parseDouble(payFeeDataDto.getReceivedAmount()));
            cycles = receivedAmount1.divide(feePrice, 4, BigDecimal.ROUND_HALF_EVEN);
            receivableAmount = payFeeDataDto.getReceivedAmount();

        } else { //自定义周期
            targetEndTime = computeFeeSMOImpl.getFeeEndTimeByCycles(payFeeDataDto.getFeeDto(), payFeeDataDto.getCycles());//根据缴费周期计算 结束时间
            cycles = new BigDecimal(Double.parseDouble(payFeeDataDto.getCycles()));
            double tmpReceivableAmount = cycles.multiply(feePrice).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
            receivableAmount = tmpReceivableAmount + "";
            //出租递增问题处理
            if (FeeConfigDto.COMPUTING_FORMULA_RANT_RATE.equals(payFeeDataDto.getFeeDto().getComputingFormula())) {
                computeFeeSMOImpl.dealRentRateCycle(payFeeDataDto.getFeeDto(), cycles.doubleValue());
                if (payFeeDataDto.getFeeDto().getOweFee() > 0) {
                    receivableAmount = payFeeDataDto.getFeeDto().getAmountOwed();

                }
            }
        }

        payFeeDetailPo.setCycles(cycles.doubleValue() + "");
        payFeeDetailPo.setEndTime(DateUtil.getFormatTimeStringA(targetEndTime));
        payFeeDetailPo.setReceivableAmount(receivableAmount);
        payFeeDetailPo.setPayableAmount(receivableAmount);

    }


    private static Calendar getTargetEndTime(Calendar endCalender, Double cycles) {
        if (StringUtil.isInteger(cycles.toString())) {
            endCalender.add(Calendar.MONTH, new Double(cycles).intValue());
            return endCalender;
        }
        if (cycles >= 1) {
            endCalender.add(Calendar.MONTH, new Double(Math.floor(cycles)).intValue());
            cycles = cycles - Math.floor(cycles);
        }
        int futureDay = endCalender.getActualMaximum(Calendar.DAY_OF_MONTH);
        int hours = new Double(cycles * futureDay * 24).intValue();
        endCalender.add(Calendar.HOUR, hours);
        return endCalender;
    }


    private void ifHasAccount(JSONObject reqJson, List<PayFeeDataDto> feeDataDtos) {
        if (!reqJson.containsKey("accountAmount")) {
            return;
        }
        double accountAmount = reqJson.getDouble("accountAmount");
        if (accountAmount <= 0) {
            return;
        }

        Assert.hasKeyAndValue(reqJson, "acctId", "未包含账户ID");
        String acctId = reqJson.getString("acctId");
        //todo 校验账户金额是否充足
        AccountDto accountDto = new AccountDto();
        accountDto.setAcctId(acctId);
        List<AccountDto> accountDtos = accountInnerServiceSMOImpl.queryAccounts(accountDto);

        Assert.listOnlyOne(accountDtos, "账户不存在");

        if (Double.parseDouble(accountDtos.get(0).getAmount()) < accountAmount) {
            throw new CmdException("账户余额不足");
        }

        BigDecimal accountAmountDec = null;
        double receivedAmount = 0.0;
        BigDecimal receivedAmountDec = null;
        for (PayFeeDataDto payFeeDataDto : feeDataDtos) {
            if (accountAmount == 0) {
                continue;
            }
            accountAmountDec = new BigDecimal(accountAmount);
            receivedAmount = Double.parseDouble(payFeeDataDto.getReceivedAmount());
            receivedAmountDec = new BigDecimal(receivedAmount);
            if (receivedAmount >= accountAmount) {
                receivedAmountDec = receivedAmountDec.subtract(accountAmountDec).setScale(2, BigDecimal.ROUND_HALF_UP);
                payFeeDataDto.setReceivedAmount(receivedAmountDec.doubleValue() + "");
                payFeeDataDto.setAccountAmount(accountAmount);
                payFeeDataDto.setAcctId(acctId);
                accountAmount = 0.00;
                continue;
            }

            payFeeDataDto.setReceivedAmount("0");
            payFeeDataDto.setAccountAmount(receivedAmount);
            payFeeDataDto.setAcctId(acctId);

            accountAmountDec = accountAmountDec.subtract(receivedAmountDec).setScale(2, BigDecimal.ROUND_HALF_UP);
            accountAmount = accountAmountDec.doubleValue();
        }


    }
}

