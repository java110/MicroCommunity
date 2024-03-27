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
import com.java110.dto.account.AccountDetailDto;
import com.java110.dto.fee.*;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.dto.user.UserDto;
import com.java110.fee.smo.impl.FeeReceiptInnerServiceSMOImpl;
import com.java110.intf.acct.IAccountInnerServiceSMO;
import com.java110.intf.community.*;
import com.java110.intf.fee.*;
import com.java110.intf.fee.IFeeAccountDetailServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.account.AccountDetailPo;
import com.java110.po.fee.FeeAttrPo;
import com.java110.po.room.ApplyRoomDiscountPo;
import com.java110.po.car.OwnerCarPo;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.fee.FeeAccountDetailPo;
import com.java110.po.owner.RepairPoolPo;
import com.java110.po.owner.RepairUserPo;
import com.java110.po.payFee.PayFeeDetailDiscountPo;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.constant.FeeFlagTypeConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.lock.DistributedLock;
import com.java110.utils.util.*;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 前台 现金或者转账收费 缴费处理类
 * <p>
 * 假如 缴费 后要处理一些逻辑建议用databus
 * 这个类已经很复杂 ，最好不要加新逻辑
 */
@Java110Cmd(serviceCode = "fee.payFee")
public class PayFeeCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(PayFeeCmd.class);

    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeDetailV1InnerServiceSMO payFeeDetailNewV1InnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IRepairUserInnerServiceSMO repairUserInnerServiceSMO;

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMO;

    @Autowired
    private IApplyRoomDiscountInnerServiceSMO applyRoomDiscountInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IAccountInnerServiceSMO accountInnerServiceSMOImpl;

    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    @Autowired
    private IPayFeeDetailDiscountNewV1InnerServiceSMO payFeeDetailDiscountNewV1InnerServiceSMOImpl;

    @Autowired
    private IRepairPoolV1InnerServiceSMO repairPoolNewV1InnerServiceSMOImpl;

    @Autowired
    private IRepairUserV1InnerServiceSMO repairUserNewV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerCarNewV1InnerServiceSMO ownerCarNewV1InnerServiceSMOImpl;

    @Autowired
    private IFeeAccountDetailServiceSMO feeAccountDetailServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private FeeReceiptInnerServiceSMOImpl feeReceiptInnerServiceSMOImpl;

    @Autowired
    private IFeeDiscountInnerServiceSMO feeDiscountInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(reqJson, "cycles", "请求报文中未包含cycles节点");
        Assert.jsonObjectHaveKey(reqJson, "receivedAmount", "请求报文中未包含receivedAmount节点");
        Assert.jsonObjectHaveKey(reqJson, "feeId", "请求报文中未包含feeId节点");
        Assert.hasLength(reqJson.getString("communityId"), "小区ID不能为空");
        Assert.hasLength(reqJson.getString("cycles"), "周期不能为空");
        Assert.hasLength(reqJson.getString("receivedAmount"), "实收金额不能为空");
        Assert.hasLength(reqJson.getString("feeId"), "费用ID不能为空");
        //判断是否 费用状态为缴费结束
        FeeDto feeDto = new FeeDto();
        feeDto.setFeeId(reqJson.getString("feeId"));
        feeDto.setCommunityId(reqJson.getString("communityId"));
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

        Assert.listOnlyOne(feeDtos, "传入费用ID错误");

        feeDto = feeDtos.get(0);

        if (FeeDto.STATE_FINISH.equals(feeDto.getState())) {
            throw new IllegalArgumentException("收费已经结束，不能再缴费");
        }

        Date endTime = feeDto.getEndTime();

        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setConfigId(feeDto.getConfigId());
        feeConfigDto.setCommunityId(reqJson.getString("communityId"));
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);

        if (feeConfigDtos == null || feeConfigDtos.size() != 1) {
            throw new IllegalArgumentException("费用项不存在");
        }
        //一次性费用 和间接性费用
        Date maxEndTime = feeDtos.get(0).getDeadlineTime();
        //周期性费用
        if (maxEndTime == null || FeeDto.FEE_FLAG_CYCLE.equals(feeConfigDtos.get(0).getFeeFlag())) {
            maxEndTime = DateUtil.getDateFromStringA(feeConfigDtos.get(0).getEndTime());
        }
//        Date maxEndTime = null;
//        if (!StringUtil.isEmpty(feeDto.getFeeFlag()) && feeDto.getFeeFlag().equals(FeeDto.FEE_FLAG_CYCLE)) { //周期性费用
//            maxEndTime = DateUtil.getDateFromStringA(feeConfigDtos.get(0).getEndTime());
//        } else { //一次性费用 和间接性费用
//            maxEndTime = feeDtos.get(0).getDeadlineTime();
//        }

        if (maxEndTime != null && endTime != null && !FeeDto.FEE_FLAG_ONCE.equals(feeDtos.get(0).getFeeFlag())) {
            Date newDate = DateUtil.stepMonth(endTime, reqJson.getDouble("cycles").intValue());
            if (newDate.getTime() > maxEndTime.getTime()) {
                throw new IllegalArgumentException("缴费周期超过 缴费结束时间,请用按结束时间方式缴费");
            }
        }

        JSONArray selectUserAccount = reqJson.getJSONArray("selectUserAccount");
        for (int paramIndex = 0; paramIndex < selectUserAccount.size(); paramIndex++) {
            JSONObject param = selectUserAccount.getJSONObject(paramIndex);
            String maximumNumber = param.getString("maximumNumber");

        }



        //todo 是否按缴费时间段缴费
        validateIfPayFeeStartEndDate(reqJson, feeConfigDtos.get(0));

        //todo 校验 优惠
        JSONArray selectDiscounts = reqJson.getJSONArray("selectDiscount");
        if(!ListUtil.isNull(selectDiscounts)) {
            for (int discountIndex = 0; discountIndex < selectDiscounts.size(); discountIndex++) {
                JSONObject param = selectDiscounts.getJSONObject(discountIndex);
                Assert.hasKeyAndValue(param, "discountId", "未包含优惠ID");
                Assert.hasKeyAndValue(param, "discountPrice", "未包含优惠金额");
            }
        }
    }


    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject paramObj) throws CmdException {
        logger.debug("paramObj : {}", paramObj);
        String payOrderId = paramObj.getString("payOrderId");

        String userId = cmdDataFlowContext.getReqHeaders().get("user-id");
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        Assert.listOnlyOne(userDtos, "用户未登录");

        String cycles = paramObj.getString("cycles");
        Date endTime = null;

        //todo 生成收据编号
        String receiptCode = feeReceiptInnerServiceSMOImpl.generatorReceiptCode(paramObj.getString("communityId"));

        PayFeePo payFeePo = null;
        String requestId = DistributedLock.getLockUUID();
        String key = this.getClass().getSimpleName() + paramObj.get("feeId");
        try {
            DistributedLock.waitGetDistributedLock(key, requestId);
            //todo 封装 缴费记录报文
            JSONObject feeDetail = addFeeDetail(paramObj);
            FeeDto feeInfo = (FeeDto) paramObj.get("feeInfo");
            endTime = feeInfo.getEndTime();
            feeDetail.put("payableAmount", feeDetail.getString("receivableAmount"));
            //todo 封装 修改费用时间报文
            JSONObject fee = modifyFee(paramObj);
            payFeePo = BeanConvertUtil.covertBean(fee, PayFeePo.class);
            PayFeeDetailPo payFeeDetailPo = BeanConvertUtil.covertBean(feeDetail, PayFeeDetailPo.class);
            payFeeDetailPo.setReceivableAmount(feeDetail.getString("totalFeePrice"));
            //todo 缓存收据编号
            CommonCache.setValue(payFeeDetailPo.getDetailId() + CommonCache.RECEIPT_CODE, receiptCode, CommonCache.DEFAULT_EXPIRETIME_TWO_MIN);

            //todo 判断是否有折扣规则
            hasDiscount(paramObj, payFeePo, payFeeDetailPo,feeInfo);

            // todo 处理用户账户
            dealUserAccount(paramObj, payFeeDetailPo);


            String oId = Java110TransactionalFactory.getOId();
            if (StringUtil.isEmpty(oId)) {
                oId = payFeeDetailPo.getDetailId();
            }

            payFeeDetailPo.setPayOrderId(oId);
            // todo 如果 扫码枪支付 输入支付订单ID
            if (!StringUtil.isEmpty(payOrderId)) {
                payFeeDetailPo.setPayOrderId(payOrderId);
            }
            payFeeDetailPo.setCashierId(userDtos.get(0).getUserId());
            payFeeDetailPo.setCashierName(userDtos.get(0).getName());
            payFeeDetailPo.setOpenInvoice("N");
            if (!StringUtil.isEmpty(paramObj.getString("cashAmount")) && !StringUtil.isEmpty(paramObj.getString("integralAmount"))) {
                BigDecimal cashAmount = new BigDecimal(paramObj.getString("cashAmount")).setScale(2, BigDecimal.ROUND_HALF_UP);
                BigDecimal integralAmount = new BigDecimal(paramObj.getString("integralAmount")).setScale(2, BigDecimal.ROUND_HALF_UP);
                if (!StringUtil.isEmpty(payFeeDetailPo.getRemark())) {
                    payFeeDetailPo.setRemark(payFeeDetailPo.getRemark() + "，现金账户抵扣" + cashAmount + "元，积分账户抵扣" + integralAmount + "元");
                } else {
                    payFeeDetailPo.setRemark("现金账户抵扣" + cashAmount + "元，积分账户抵扣" + integralAmount + "元");
                }
            }
            if (!StringUtil.isEmpty(paramObj.getString("cashAmount")) && StringUtil.isEmpty(paramObj.getString("integralAmount"))) {
                BigDecimal cashAmount = new BigDecimal(paramObj.getString("cashAmount")).setScale(2, BigDecimal.ROUND_HALF_UP);
                if (!StringUtil.isEmpty(payFeeDetailPo.getRemark())) {
                    payFeeDetailPo.setRemark(payFeeDetailPo.getRemark() + "，现金账户抵扣" + cashAmount + "元");
                } else {
                    payFeeDetailPo.setRemark("现金账户抵扣" + cashAmount + "元");
                }
            }
            if (StringUtil.isEmpty(paramObj.getString("cashAmount")) && !StringUtil.isEmpty(paramObj.getString("integralAmount"))) {
                BigDecimal integralAmount = new BigDecimal(paramObj.getString("integralAmount")).setScale(2, BigDecimal.ROUND_HALF_UP);
                if (!StringUtil.isEmpty(payFeeDetailPo.getRemark())) {
                    payFeeDetailPo.setRemark(payFeeDetailPo.getRemark() + "，积分账户抵扣" + integralAmount + "元");
                } else {
                    payFeeDetailPo.setRemark("积分账户抵扣" + integralAmount + "元");
                }
            }
            int flag = payFeeDetailNewV1InnerServiceSMOImpl.savePayFeeDetailNew(payFeeDetailPo);
            if (flag < 1) {
                throw new CmdException("缴费失败");
            }

            flag = payFeeV1InnerServiceSMOImpl.updatePayFee(payFeePo);
            if (flag < 1) {
                throw new CmdException("缴费失败");
            }
            // todo 如果是按 自定义时间段缴费，这里补一条缴费记录 和 欠费费用
            ifCustomStartEndTimePayFee(cycles, endTime, payFeeDetailPo, payFeePo, paramObj);
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            DistributedLock.releaseDistributedLock(key, requestId);
        }
        //todo 账户处理
        dealAccount(paramObj);


        //todo 为停车费单独处理
        updateCarEndTime(paramObj);

        //todo 处理报修单
        doDealRepairOrder(paramObj);


        //修改折扣申请状态，空置房折扣只能用一次
        String selectDiscount = paramObj.getString("selectDiscount");
        JSONArray params = JSONArray.parseArray(selectDiscount);
        for (int index = 0; index < params.size(); index++) {
            JSONObject param = params.getJSONObject(index);
            if (!StringUtil.isEmpty(param.getString("ardId"))) {
                ApplyRoomDiscountPo applyRoomDiscountPo = new ApplyRoomDiscountPo();
                //空置房优惠不可用
                applyRoomDiscountPo.setInUse("1");
                applyRoomDiscountPo.setArdId(param.getString("ardId"));
                applyRoomDiscountInnerServiceSMOImpl.updateApplyRoomDiscount(applyRoomDiscountPo);
            }
        }


        //根据明细ID 查询收据信息
        FeeReceiptDetailDto feeReceiptDetailDto = new FeeReceiptDetailDto();
        feeReceiptDetailDto.setDetailId(paramObj.getString("detailId"));

        cmdDataFlowContext.setResponseEntity(ResultVo.createResponseEntity(feeReceiptDetailDto));
    }


    private void dealUserAccount(JSONObject paramObj, PayFeeDetailPo payFeeDetailPo) {
        //判断选择的账号
        JSONArray jsonArray = paramObj.getJSONArray("selectUserAccount");
        if (ListUtil.isNull(jsonArray)) {
            return;
        }

        BigDecimal integralSum = new BigDecimal(0);
        BigDecimal cashSum = new BigDecimal(0);

        for (int columnIndex = 0; columnIndex < jsonArray.size(); columnIndex++) {
            JSONObject param = jsonArray.getJSONObject(columnIndex);
            //账户金额
            BigDecimal amount = new BigDecimal(param.getString("amount"));
            if (AccountDto.ACCT_TYPE_INTEGRAL.equals(param.getString("acctType"))) { //积分账户
                //获取最大抵扣积分
                BigDecimal maximumNumber = new BigDecimal(param.getString("maximumNumber"));
                //获取积分抵扣
                BigDecimal deductionProportion = new BigDecimal(param.getString("deductionProportion"));
                int flag = amount.compareTo(maximumNumber);
                BigDecimal redepositAmount = new BigDecimal("0.00");
                if (flag == 1) { //账户积分大于最大使用积分，就用最大使用积分抵扣
                    redepositAmount = maximumNumber;
                }
                if (flag > -1) { //账户积分大于等于最大使用积分，就用最大使用积分抵扣
                    redepositAmount = maximumNumber;
                }
                if (flag == -1) { //账户积分小于最大使用积分，就用账户积分抵扣
                    redepositAmount = amount;
                }
                if (flag < 1) { //账户积分小于等于最大使用积分，就用账户积分抵扣
                    redepositAmount = amount;
                }
                if (flag == 0) { //账户积分等于最大使用积分
                    redepositAmount = amount;
                }
                //计算积分换算的金额
                BigDecimal divide = redepositAmount.divide(deductionProportion);
                BigDecimal receivedAmount = new BigDecimal(payFeeDetailPo.getReceivedAmount());
                //计算实付金额
                int flag2 = divide.compareTo(receivedAmount);
                BigDecimal subtract = new BigDecimal("0.00");
                //生成抵扣明细记录
                if (flag2 == -1) { //积分换算金额小于实付金额
                    subtract = receivedAmount.subtract(divide);
                } else if (flag < 1) { //积分换算金额小于等于实付金额
                    subtract = receivedAmount.subtract(divide);
                }
//                integralSum = integralSum.add(subtract);
                payFeeDetailPo.setReceivedAmount(subtract.toString());
                integralSum = integralSum.add(divide);
                // todo 如果积分大于0
                if (integralSum.doubleValue() > 0) {
                    FeeAccountDetailPo feeAccountDetailPo = new FeeAccountDetailPo();
                    feeAccountDetailPo.setFadId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_fadId));
                    feeAccountDetailPo.setDetailId(payFeeDetailPo.getDetailId());
                    feeAccountDetailPo.setCommunityId(payFeeDetailPo.getCommunityId());
                    feeAccountDetailPo.setAmount(integralSum.doubleValue() + "");
                    feeAccountDetailPo.setState("1003"); //1001 无抵扣 1002 现金账户抵扣 1003 积分账户抵扣 1004 优惠券抵扣
                    feeAccountDetailServiceSMOImpl.saveFeeAccountDetail(feeAccountDetailPo);
                }
            } else if (AccountDto.ACCT_TYPE_CASH.equals(param.getString("acctType"))) { //现金账户
                //实收金额
                BigDecimal receivedAmount = new BigDecimal(payFeeDetailPo.getReceivedAmount());
                int flag = amount.compareTo(receivedAmount);
                BigDecimal redepositAmount = new BigDecimal(0.00);
                if (flag == 1) { //现金账户大于实收金额，就用实收金额
                    redepositAmount = receivedAmount;
                }
                if (flag > -1) { //现金账户大于等于实收金额，就用实收金额
                    redepositAmount = receivedAmount;
                }
                if (flag == -1) { //现金账户小于实收金额，就用现金账户
                    redepositAmount = amount;
                }
                if (flag < 1) { //现金账户小于等于实收金额，就用现金账户
                    redepositAmount = amount;
                }
                if (flag == 0) { //现金账户等于实收金额
                    redepositAmount = amount;
                }
                cashSum = cashSum.add(redepositAmount);

            }

            // todo 如果积分大于0
            if (integralSum.doubleValue() > 0) {
                FeeAccountDetailPo feeAccountDetailPo = new FeeAccountDetailPo();
                feeAccountDetailPo.setFadId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_fadId));
                feeAccountDetailPo.setDetailId(payFeeDetailPo.getDetailId());
                feeAccountDetailPo.setCommunityId(payFeeDetailPo.getCommunityId());
                feeAccountDetailPo.setAmount(integralSum.doubleValue() + "");
                feeAccountDetailPo.setState("1003"); //1001 无抵扣 1002 现金账户抵扣 1003 积分账户抵扣 1004 优惠券抵扣
                feeAccountDetailServiceSMOImpl.saveFeeAccountDetail(feeAccountDetailPo);
            }
            if (cashSum.doubleValue() > 0) {
                //生成抵扣明细记录
                FeeAccountDetailPo feeAccountDetailPo = new FeeAccountDetailPo();
                feeAccountDetailPo.setFadId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_fadId));
                feeAccountDetailPo.setDetailId(payFeeDetailPo.getDetailId());
                feeAccountDetailPo.setCommunityId(payFeeDetailPo.getCommunityId());
                feeAccountDetailPo.setState("1002"); //1001 无抵扣 1002 现金账户抵扣 1003 积分账户抵扣 1004 优惠券抵扣
                feeAccountDetailPo.setAmount(cashSum.doubleValue() + ""); //现金抵扣金额
                feeAccountDetailServiceSMOImpl.saveFeeAccountDetail(feeAccountDetailPo);
                BigDecimal receivedAmountDec = new BigDecimal(payFeeDetailPo.getReceivedAmount());
                receivedAmountDec = receivedAmountDec.subtract(cashSum);
                payFeeDetailPo.setReceivedAmount(receivedAmountDec.doubleValue() + "");
                payFeeDetailPo.setAcctAmount(cashSum.doubleValue() + "");
            }
        }
    }

    /**
     * 改造赠送逻辑 if 嵌套有点多 优化
     *
     * @param paramObj
     * @param payFeePo
     * @param payFeeDetailPo
     * @throws ParseException
     */
    private void hasDiscount(JSONObject paramObj, PayFeePo payFeePo, PayFeeDetailPo payFeeDetailPo,FeeDto feeDto) throws ParseException {
        if (!paramObj.containsKey("selectDiscount")) {
            return;
        }
        JSONArray selectDiscounts = paramObj.getJSONArray("selectDiscount");

        if (ListUtil.isNull(selectDiscounts)) {
            return;
        }
        Map feePriceMap = computeFeeSMOImpl.getFeePrice(feeDto);
        for (int index = 0; index < selectDiscounts.size(); index++) {
            JSONObject paramJson = selectDiscounts.getJSONObject(index);
            if (!"102020008".equals(paramJson.getString("ruleId"))) { //赠送规则
                continue;
            }
            JSONArray feeDiscountSpecs = paramJson.getJSONArray("feeDiscountSpecs");
            if (ListUtil.isNull(feeDiscountSpecs)) {
                continue;
            }
            for (int specIndex = 0; specIndex < feeDiscountSpecs.size(); specIndex++) {
                JSONObject paramIn = feeDiscountSpecs.getJSONObject(specIndex);
                if (!"89002020980015".equals(paramIn.getString("specId"))) { //赠送月份
                    continue;
                }
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String specValue = paramIn.getString("specValue");
                //获取费用结束时间(也就是下次费用开始时间)
                Date endTime = df.parse(payFeeDetailPo.getEndTime());
                Calendar cal = Calendar.getInstance();
                cal.setTime(endTime);
                cal.add(Calendar.MONTH, Integer.parseInt(specValue));
                payFeeDetailPo.setEndTime(df.format(cal.getTime()));
                payFeePo.setEndTime(df.format(cal.getTime()));

                BigDecimal value = new BigDecimal(payFeeDetailPo.getGiftAmount());
                value = value.add(new BigDecimal(specValue).multiply(new BigDecimal(feePriceMap.get("feePrice").toString())));
                payFeeDetailPo.setGiftAmount(value.doubleValue() + "");
            }
        }

        for (int discountIndex = 0; discountIndex < selectDiscounts.size(); discountIndex++) {
            JSONObject param = selectDiscounts.getJSONObject(discountIndex);
            addPayFeeDetailDiscount(paramObj, param, payFeeDetailPo);
        }

    }

    /**
     * 处理报修单
     *
     * @param paramObj
     */
    private void doDealRepairOrder(JSONObject paramObj) {
        int flag;//判断是否有派单属性ID
        FeeAttrDto feeAttrDto = new FeeAttrDto();
        feeAttrDto.setCommunityId(paramObj.getString("communityId"));
        feeAttrDto.setFeeId(paramObj.getString("feeId"));
        feeAttrDto.setSpecCd(FeeAttrDto.SPEC_CD_REPAIR);
        List<FeeAttrDto> feeAttrDtos = feeAttrInnerServiceSMOImpl.queryFeeAttrs(feeAttrDto);
        //修改 派单状态
        if (feeAttrDtos != null && feeAttrDtos.size() > 0) {
            RepairDto repairDto = new RepairDto();
            repairDto.setRepairId(feeAttrDtos.get(0).getValue());
            //查询报修记录
            List<RepairDto> repairDtos = repairInnerServiceSMO.queryRepairs(repairDto);
            Assert.listOnlyOne(repairDtos, "报修信息错误！");
            //获取报修渠道
            String repairChannel = repairDtos.get(0).getRepairChannel();
            RepairPoolPo repairPoolPo = new RepairPoolPo();
            repairPoolPo.setRepairId(feeAttrDtos.get(0).getValue());
            repairPoolPo.setCommunityId(paramObj.getString("communityId"));
            if (repairChannel.equals("Z")) { //如果是业主自主报修，状态就变成待评价
                repairPoolPo.setState(RepairDto.STATE_APPRAISE);
            } else { //如果是员工代客报修或电话报修，状态就变成待回访
                repairPoolPo.setState(RepairDto.STATE_RETURN_VISIT);
            }
            flag = repairPoolNewV1InnerServiceSMOImpl.updateRepairPoolNew(repairPoolPo);
            if (flag < 1) {
                throw new CmdException("更新微信派单池信息失败");
            }

            repairDto = new RepairDto();
            repairDto.setRepairId(feeAttrDtos.get(0).getValue());
            //查询报修记录
            repairDtos = repairInnerServiceSMO.queryRepairs(repairDto);

            Assert.listOnlyOne(repairDtos, "报修信息错误！");
            //获取报修渠道
            repairChannel = repairDtos.get(0).getRepairChannel();
            RepairUserDto repairUserDto = new RepairUserDto();
            repairUserDto.setRepairId(feeAttrDtos.get(0).getValue());
            repairUserDto.setState(RepairUserDto.STATE_PAY_FEE);
            //查询待支付状态的记录
            List<RepairUserDto> repairUserDtoList = repairUserInnerServiceSMO.queryRepairUsers(repairUserDto);
            Assert.listOnlyOne(repairUserDtoList, "信息错误！");
            RepairUserPo repairUserPo = new RepairUserPo();
            repairUserPo.setRuId(repairUserDtoList.get(0).getRuId());
            if ("Z".equals(repairChannel)) {  //如果业主是自主报修，状态就变成已支付，并新增一条待评价状态
                repairUserPo.setState(RepairUserDto.STATE_FINISH_PAY_FEE);
                //如果是待评价状态，就更新结束时间
                repairUserPo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
                repairUserPo.setContext("已支付" + paramObj.getString("feePrice") + "元");
                //新增待评价状态
                RepairUserPo repairUser = new RepairUserPo();
                repairUser.setRuId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ruId));
                repairUser.setStartTime(repairUserPo.getEndTime());
                repairUser.setState(RepairUserDto.STATE_EVALUATE);
                repairUser.setContext("待评价");
                repairUser.setCommunityId(paramObj.getString("communityId"));
                repairUser.setCreateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
                repairUser.setRepairId(repairUserDtoList.get(0).getRepairId());
                repairUser.setStaffId(repairUserDtoList.get(0).getStaffId());
                repairUser.setStaffName(repairUserDtoList.get(0).getStaffName());
                repairUser.setPreStaffId(repairUserDtoList.get(0).getStaffId());
                repairUser.setPreStaffName(repairUserDtoList.get(0).getStaffName());
                repairUser.setPreRuId(repairUserDtoList.get(0).getRuId());
                repairUser.setRepairEvent("auditUser");
                flag = repairUserNewV1InnerServiceSMOImpl.saveRepairUserNew(repairUser);
                if (flag < 1) {
                    throw new CmdException("更新微信派单池信息失败");
                }
            } else {  //如果是员工代客报修或电话报修，状态就变成已支付
                repairUserPo.setState(RepairUserDto.STATE_FINISH_PAY_FEE);
                //如果是已支付状态，就更新结束时间
                repairUserPo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
                repairUserPo.setContext("已支付" + paramObj.getString("feePrice") + "元");
            }
            flag = repairUserNewV1InnerServiceSMOImpl.updateRepairUserNew(repairUserPo);
            if (flag < 1) {
                throw new CmdException("更新微信派单池信息失败");
            }
        }
    }

    /**
     * 处理停车费
     *
     * @param paramObj
     */
    private void updateCarEndTime(JSONObject paramObj) {
        int flag;
        FeeDto feeDto = new FeeDto();
        feeDto.setFeeId(paramObj.getString("feeId"));
        feeDto.setCommunityId(paramObj.getString("communityId"));
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

        if (feeDtos == null || feeDtos.size() < 1) {
            return;
        }
        if (!FeeDto.PAYER_OBJ_TYPE_CAR.equals(feeDtos.get(0).getPayerObjType())) {
            return;
        }
        Date feeEndTime = feeDtos.get(0).getEndTime();
        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCommunityId(paramObj.getString("communityId"));
        ownerCarDto.setCarId(feeDtos.get(0).getPayerObjId());
        ownerCarDto.setCarTypeCd("1001"); //业主车辆
        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);

        if (ownerCarDtos == null || ownerCarDtos.size() < 1) {
            return;
        }
        //获取车位id
        String psId = ownerCarDtos.get(0).getPsId();
        //获取车辆状态(1001 正常状态，2002 欠费状态  3003 车位释放)
        boolean sonMotherParking = false;
        String num = "";
        String carState = ownerCarDtos.get(0).getState();
        if (!StringUtil.isEmpty(psId) && "3003".equals(carState)) {
            ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
            parkingSpaceDto.setPsId(psId);
            List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);
            Assert.listOnlyOne(parkingSpaceDtos, "查询车位信息错误！");
            //获取车位状态(出售 S，出租 H ，空闲 F)
            String state = parkingSpaceDtos.get(0).getState();
            if (!StringUtil.isEmpty(state) && !state.equals("F")) {
                throw new IllegalArgumentException("车位已被使用，不能再缴费！");
            }

            if (ParkingSpaceDto.TYPE_CD_SON_MOTHER.equals(parkingSpaceDtos.get(0).getTypeCd())
                    && !parkingSpaceDtos.get(0).getTypeCd().contains(ParkingSpaceDto.NUM_MOTHER)
            ) {
                sonMotherParking = true;
                num = parkingSpaceDtos.get(0).getNum();
            }
        }

        // todo  字母车位，子车位缴费 母车位延期
        if (sonMotherParking) {
            queryMotherOwnerCars(num, ownerCarDtos, psId);
        }


        Calendar endTimeCalendar = null;
        //车位费用续租
        for (OwnerCarDto tmpOwnerCarDto : ownerCarDtos) {
            //后付费 或者信用期车辆 加一个月
            if (FeeConfigDto.PAYMENT_CD_AFTER.equals(feeDtos.get(0).getPaymentCd())
                    || OwnerCarDto.CAR_TYPE_CREDIT.equals(tmpOwnerCarDto.getCarType())) {
                endTimeCalendar = Calendar.getInstance();
                endTimeCalendar.setTime(feeEndTime);
                endTimeCalendar.add(Calendar.MONTH, 1);
                feeEndTime = endTimeCalendar.getTime();
            }
            if (tmpOwnerCarDto.getEndTime().getTime() >= feeEndTime.getTime()) {
                continue;
            }
            OwnerCarPo ownerCarPo = new OwnerCarPo();
            ownerCarPo.setMemberId(tmpOwnerCarDto.getMemberId());
            ownerCarPo.setEndTime(DateUtil.getFormatTimeString(feeEndTime, DateUtil.DATE_FORMATE_STRING_A));
            flag = ownerCarNewV1InnerServiceSMOImpl.updateOwnerCarNew(ownerCarPo);
            if (flag < 1) {
                throw new CmdException("缴费失败");
            }
        }


    }

    /**
     * 子母车位延期 母车位也需要延期
     *
     * @param num
     * @param ownerCarDtos
     */
    private void queryMotherOwnerCars(String num, List<OwnerCarDto> ownerCarDtos, String paId) {

        String sonMotherSwitch = CommonCache.getValue("SON_MOTHER_PARKING_AUTO_FEE");

        if (!"ON".equals(sonMotherSwitch)) {
            return;
        }

        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setNum(num + ParkingSpaceDto.NUM_MOTHER);
        parkingSpaceDto.setPaId(paId);
        List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);
        Assert.listOnlyOne(parkingSpaceDtos, "查询车位信息错误！");

        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCommunityId(parkingSpaceDtos.get(0).getCommunityId());
        ownerCarDto.setPsId(parkingSpaceDtos.get(0).getPsId());
        ownerCarDto.setCarTypeCd("1001"); //业主车辆
        List<OwnerCarDto> tmpOwnerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);

        if (ownerCarDtos == null) {
            ownerCarDtos = new ArrayList<>();
        }

        ownerCarDtos.addAll(tmpOwnerCarDtos);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void addPayFeeDetailDiscount(JSONObject paramInJson, JSONObject discountJson, PayFeeDetailPo payFeeDetailPo) {
        JSONObject businessFee = new JSONObject();
        businessFee.put("detailDiscountId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailDiscountId));
        businessFee.put("discountPrice", discountJson.getString("discountPrice"));
        businessFee.put("discountId", discountJson.getString("discountId"));
        businessFee.put("detailId", paramInJson.getString("detailId"));
        businessFee.put("communityId", paramInJson.getString("communityId"));
        businessFee.put("feeId", paramInJson.getString("feeId"));
        PayFeeDetailDiscountPo payFeeDetailDiscount = BeanConvertUtil.covertBean(businessFee, PayFeeDetailDiscountPo.class);
        //businessFee.putAll(feeMap);
        int fage = payFeeDetailDiscountNewV1InnerServiceSMOImpl.savePayFeeDetailDiscountNew(payFeeDetailDiscount);

        if (fage < 1) {
            throw new CmdException("更新费用信息失败");
        }

        FeeDiscountDto feeDiscountDto = new FeeDiscountDto();
        feeDiscountDto.setDiscountId(discountJson.getString("discountId"));
        List<FeeDiscountDto> feeDiscountDtos = feeDiscountInnerServiceSMOImpl.queryFeeDiscounts(feeDiscountDto);

        if (ListUtil.isNull(feeDiscountDtos)) {
            return;
        }

        //todo 打折或者空置房打折
        if (FeeDiscountRuleDto.DISCOUNT_SMALL_TYPE_DISCOUNT.equals(feeDiscountDtos.get(0).getDiscountSmallType())
                || FeeDiscountRuleDto.DISCOUNT_SMALL_TYPE_APPLY_DISCOUNT.equals(feeDiscountDtos.get(0).getDiscountSmallType())
        ) {
            BigDecimal value = new BigDecimal(payFeeDetailPo.getDiscountAmount());
            value = value.add(new BigDecimal(discountJson.getString("discountPrice")));
            payFeeDetailPo.setDiscountAmount(value.doubleValue() + "");
        } else if (FeeDiscountRuleDto.DISCOUNT_SMALL_TYPE_DEDUCTION.equals(feeDiscountDtos.get(0).getDiscountSmallType())
                || FeeDiscountRuleDto.DISCOUNT_SMALL_TYPE_APPLY_DEDUCTION.equals(feeDiscountDtos.get(0).getDiscountSmallType())) {
            BigDecimal value = new BigDecimal(payFeeDetailPo.getDeductionAmount());
            value = value.add(new BigDecimal(discountJson.getString("discountPrice")));
            payFeeDetailPo.setDeductionAmount(value.doubleValue() + "");
        } else if (FeeDiscountRuleDto.DISCOUNT_SMALL_TYPE_LATE.equals(feeDiscountDtos.get(0).getDiscountSmallType())) {
            BigDecimal value = new BigDecimal(payFeeDetailPo.getLateAmount());
            value = value.add(new BigDecimal(discountJson.getString("discountPrice")));
            payFeeDetailPo.setLateAmount(value.doubleValue() + "");
        }
    }

    /**
     * 添加费用明细信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public JSONObject addFeeDetail(JSONObject paramInJson) {
        JSONObject businessFeeDetail = new JSONObject();
        businessFeeDetail.putAll(paramInJson);
        businessFeeDetail.put("detailId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        paramInJson.put("detailId", businessFeeDetail.getString("detailId"));
        //支付方式
        businessFeeDetail.put("primeRate", paramInJson.getString("primeRate"));
        //计算 应收金额
        FeeDto feeDto = new FeeDto();
        feeDto.setFeeId(paramInJson.getString("feeId"));
        feeDto.setCommunityId(paramInJson.getString("communityId"));
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
        if (ListUtil.isNull(feeDtos)) {
            throw new CmdException("查询费用信息失败，未查到数据或查到多条数据");
        }
        if (!businessFeeDetail.containsKey("state") || StringUtil.isEmpty(businessFeeDetail.getString("state"))) {
            businessFeeDetail.put("state", "1400");
        }
        feeDto = feeDtos.get(0);
        businessFeeDetail.put("startTime", DateUtil.getFormatTimeStringA(feeDto.getEndTime()));
        int hours = 0;
        Date targetEndTime = null;
        BigDecimal cycles = null;
        Map feePriceAll = computeFeeSMOImpl.getFeePrice(feeDto);
        BigDecimal feePrice = new BigDecimal(feePriceAll.get("feePrice").toString());
        if ("-101".equals(paramInJson.getString("cycles"))) { // 自定义金额交费
            Date endTime = feeDto.getEndTime();
            Calendar endCalender = Calendar.getInstance();
            endCalender.setTime(endTime);
            BigDecimal receivedAmount = new BigDecimal(Double.parseDouble(paramInJson.getString("receivedAmount")));
            cycles = receivedAmount.divide(feePrice, 4, BigDecimal.ROUND_HALF_EVEN);
            endCalender = getTargetEndTime(endCalender, cycles.doubleValue());
            targetEndTime = endCalender.getTime();
            paramInJson.put("tmpCycles", cycles.doubleValue());
            businessFeeDetail.put("cycles", cycles.doubleValue());
            //处理 可能还存在 实收手工减免的情况
            if (paramInJson.containsKey("receivableAmount") && !StringUtil.isEmpty(paramInJson.getString("receivableAmount"))) {
                businessFeeDetail.put("receivableAmount", paramInJson.getString("receivableAmount"));
            } else {
                businessFeeDetail.put("receivableAmount", receivedAmount.doubleValue());
            }
            //todo 如果应收小于实收，将应收刷为 实收
            if (businessFeeDetail.getDoubleValue("receivableAmount") < receivedAmount.doubleValue()) {
                businessFeeDetail.put("receivableAmount", receivedAmount.doubleValue());
            }
        } else if ("-103".equals(paramInJson.getString("cycles"))) { //这里按缴费结束时间缴费
            String custEndTime = paramInJson.getString("custEndTime");
            Date endDates = DateUtil.getDateFromStringB(custEndTime);
            Calendar c = Calendar.getInstance();
            c.setTime(endDates);
            c.add(Calendar.DAY_OF_MONTH, 1);
            endDates = c.getTime();//这是明天
            targetEndTime = endDates;
            BigDecimal receivedAmount1 = new BigDecimal(Double.parseDouble(paramInJson.getString("receivedAmount")));
            cycles = receivedAmount1.divide(feePrice, 4, BigDecimal.ROUND_HALF_EVEN);
            paramInJson.put("tmpCycles", cycles.doubleValue());
            businessFeeDetail.put("cycles", cycles.doubleValue());
            BigDecimal receivedAmount = new BigDecimal(Double.parseDouble(paramInJson.getString("receivedAmount")));
            //处理 可能还存在 实收手工减免的情况
            if (paramInJson.containsKey("receivableAmount") && !StringUtil.isEmpty(paramInJson.getString("receivableAmount"))) {
                businessFeeDetail.put("receivableAmount", paramInJson.getString("receivableAmount"));
            } else {
                businessFeeDetail.put("receivableAmount", receivedAmount.doubleValue());
            }
            //todo 如果应收小于实收，将应收刷为 实收
            if (businessFeeDetail.getDoubleValue("receivableAmount") < receivedAmount.doubleValue()) {
                businessFeeDetail.put("receivableAmount", receivedAmount.doubleValue());
            }
        } else if ("-105".equals(paramInJson.getString("cycles"))) { //这里按缴费结束时间缴费
            String customEndTime = paramInJson.getString("customEndTime");
            Date endDates = DateUtil.getDateFromStringB(customEndTime);
            Calendar c = Calendar.getInstance();
            c.setTime(endDates);
            c.add(Calendar.DAY_OF_MONTH, 1);
            endDates = c.getTime();//这是明天
            targetEndTime = endDates;
            BigDecimal receivedAmount1 = new BigDecimal(Double.parseDouble(paramInJson.getString("receivedAmount")));
            cycles = receivedAmount1.divide(feePrice, 4, BigDecimal.ROUND_HALF_EVEN);
            paramInJson.put("tmpCycles", cycles.doubleValue());
            businessFeeDetail.put("cycles", cycles.doubleValue());
            BigDecimal receivedAmount = new BigDecimal(Double.parseDouble(paramInJson.getString("receivedAmount")));
            //处理 可能还存在 实收手工减免的情况
            if (paramInJson.containsKey("receivableAmount") && !StringUtil.isEmpty(paramInJson.getString("receivableAmount"))) {
                businessFeeDetail.put("receivableAmount", paramInJson.getString("receivableAmount"));
            } else {
                businessFeeDetail.put("receivableAmount", receivedAmount.doubleValue());
            }
            //todo 如果应收小于实收，将应收刷为 实收
            if (businessFeeDetail.getDoubleValue("receivableAmount") < receivedAmount.doubleValue()) {
                businessFeeDetail.put("receivableAmount", receivedAmount.doubleValue());
            }
            //todo 改写开始时间
            businessFeeDetail.put("startTime", paramInJson.getString("customStartTime"));
        } else { //自定义周期
            targetEndTime = computeFeeSMOImpl.getFeeEndTimeByCycles(feeDto, paramInJson.getString("cycles"));//根据缴费周期计算 结束时间
            cycles = new BigDecimal(Double.parseDouble(paramInJson.getString("cycles")));
            double tmpReceivableAmount = cycles.multiply(feePrice).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
            businessFeeDetail.put("receivableAmount", tmpReceivableAmount);
            //出租递增问题处理
            if (FeeConfigDto.COMPUTING_FORMULA_RANT_RATE.equals(feeDto.getComputingFormula())) {
                computeFeeSMOImpl.dealRentRateCycle(feeDto, cycles.doubleValue());
                if (feeDto.getOweFee() > 0) {
                    businessFeeDetail.put("receivableAmount", feeDto.getAmountOwed());
                }
            }
        }

        businessFeeDetail.put("endTime", DateUtil.getFormatTimeStringA(targetEndTime));
        paramInJson.put("feeInfo", feeDto);

        return businessFeeDetail;
    }

    public JSONObject modifyFee(JSONObject paramInJson) {

        JSONObject businessFee = new JSONObject();
        FeeDto feeInfo = (FeeDto) paramInJson.get("feeInfo");
        Date endTime = feeInfo.getEndTime();
        Calendar endCalender = Calendar.getInstance();
        endCalender.setTime(endTime);
        int hours = 0;
        //-101自定义金额 -102自定义周期 -103 自定义结束时间
        if ("-101".equals(paramInJson.getString("cycles"))) {
            endCalender = getTargetEndTime(endCalender, Double.parseDouble(paramInJson.getString("tmpCycles")));
        } else if ("-103".equals(paramInJson.getString("cycles"))) {
            String custEndTime = paramInJson.getString("custEndTime");
            Date endDates = DateUtil.getDateFromStringB(custEndTime);
            Calendar c = Calendar.getInstance();
            c.setTime(endDates);
            c.add(Calendar.DAY_OF_MONTH, 1);
            endDates = c.getTime();//这是明天
            endCalender.setTime(endDates);
        } else if ("-105".equals(paramInJson.getString("cycles"))) {
            String customEndTime = paramInJson.getString("customEndTime");
            Date endDates = DateUtil.getDateFromStringB(customEndTime);
            Calendar c = Calendar.getInstance();
            c.setTime(endDates);
            c.add(Calendar.DAY_OF_MONTH, 1);
            endDates = c.getTime();//这是明天
            endCalender.setTime(endDates);
        } else {
            endCalender.add(Calendar.MONTH, Integer.parseInt(paramInJson.getString("cycles")));
        }
        if (FeeDto.FEE_FLAG_ONCE.equals(feeInfo.getFeeFlag())) {
            if (feeInfo.getDeadlineTime() != null) {
                endCalender.setTime(feeInfo.getDeadlineTime());
            } else if (!StringUtil.isEmpty(feeInfo.getCurDegrees())) {
                endCalender.setTime(feeInfo.getCurReadingTime());
            } else if (feeInfo.getImportFeeEndTime() == null) {
                endCalender.setTime(feeInfo.getConfigEndTime());
            } else {
                endCalender.setTime(feeInfo.getImportFeeEndTime());
            }
            //businessFee.put("state",FeeDto.STATE_FINISH);
            feeInfo.setState(FeeDto.STATE_FINISH);
        }
        feeInfo.setEndTime(endCalender.getTime());
        Date maxEndTime = feeInfo.getDeadlineTime();
        if (FeeDto.FEE_FLAG_CYCLE.equals(feeInfo.getFeeFlag())) {
            maxEndTime = feeInfo.getConfigEndTime();
        }

        if (FeeDto.FEE_FLAG_CYCLE_ONCE.equals(feeInfo.getFeeFlag())) {
            maxEndTime = feeInfo.getMaxEndTime();
        }

        //如果间歇性费用没有设置结束时间 则取费用项的
        if (maxEndTime == null) {
            maxEndTime = feeInfo.getConfigEndTime();
        }

        //判断 结束时间 是否大于 费用项 结束时间，这里 容错一下，如果 费用结束时间大于 费用项结束时间 30天 走报错 属于多缴费
        if (maxEndTime != null) {
            if (feeInfo.getEndTime().getTime() - maxEndTime.getTime() > 30 * 24 * 60 * 60 * 1000L) {
                throw new IllegalArgumentException("缴费超过了 费用项结束时间");
            }
        }
        Map feeMap = BeanConvertUtil.beanCovertMap(feeInfo);
        feeMap.put("startTime", DateUtil.getFormatTimeString(feeInfo.getStartTime(), DateUtil.DATE_FORMATE_STRING_A));
        feeMap.put("endTime", DateUtil.getFormatTimeString(feeInfo.getEndTime(), DateUtil.DATE_FORMATE_STRING_A));
        feeMap.put("cycles", paramInJson.getString("cycles"));
        feeMap.put("configEndTime", feeInfo.getConfigEndTime());
        businessFee.putAll(feeMap);
        //为停车费单独处理
        paramInJson.put("carFeeEndTime", feeInfo.getEndTime());
        paramInJson.put("carPayerObjType", feeInfo.getPayerObjType());
        paramInJson.put("carPayerObjId", feeInfo.getPayerObjId());

        // 周期性收费、缴费后，到期日期在费用项终止日期后，则设置缴费状态结束，设置结束日期为费用项终止日期
        if (!FeeFlagTypeConstant.ONETIME.equals(feeInfo.getFeeFlag())) {
            //这里 容错五天时间
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(maxEndTime);
            calendar.add(Calendar.DAY_OF_MONTH, -5);
            maxEndTime = calendar.getTime();
            if (feeInfo.getEndTime().after(maxEndTime)) {
                businessFee.put("state", FeeDto.STATE_FINISH);
                businessFee.put("endTime", maxEndTime);
            }
        }
        return businessFee;
    }


    public void dealAccount(JSONObject paramObj) {
        //判断选择的账号
        JSONArray jsonArray = paramObj.getJSONArray("selectUserAccount");
        if (jsonArray == null || jsonArray.size() < 1) {
            return;
        }
        List<AccountDto> accountDtos = new ArrayList<>();
        for (int columnIndex = 0; columnIndex < jsonArray.size(); columnIndex++) {
            //应收款 totalFeePrice
            BigDecimal totalFeePrice = new BigDecimal(paramObj.getString("totalFeePrice")); //应收款
            //实收款 receivedAmount
            BigDecimal receivedAmount = new BigDecimal(paramObj.getString("receivedAmount")); //实收款（扣款金额）
            BigDecimal redepositAmount = new BigDecimal("0.00");//抵扣金额
            JSONObject param = jsonArray.getJSONObject(columnIndex);
            if (!StringUtil.isEmpty(param.getString("acctType")) && param.getString("acctType").equals("2004")) { //积分账户
                //获取抵扣比例
                BigDecimal deductionProportion = new BigDecimal(param.getString("deductionProportion"));
                //计算实收款所扣的积分
                BigDecimal multiply = receivedAmount.multiply(deductionProportion);
                receivedAmount = multiply;
            }
            //账户金额
            BigDecimal amount = new BigDecimal(param.getString("amount"));
            int flag = amount.compareTo(receivedAmount);
            if (flag == -1) { //账户金额小于实收款
                redepositAmount = amount;//抵扣金额
            } else {
                redepositAmount = receivedAmount;//抵扣金额
            }
            if ("2004".equals(param.getString("acctType"))) {
                //获取最大抵扣积分
                BigDecimal maximumNumber = new BigDecimal(param.getString("maximumNumber"));
                //获取积分抵扣
                BigDecimal deductionProportion = new BigDecimal(param.getString("deductionProportion"));
                int flag2 = amount.compareTo(maximumNumber);
                if (flag2 == 1) { //账户积分大于最大使用积分，就用最大使用积分抵扣
                    int flag3 = maximumNumber.compareTo(receivedAmount);
                    if (flag3 == 1) { //如果最大使用积分大于实收金额抵扣积分，就把实收金额抵扣积分赋值给抵扣积分
                        redepositAmount = receivedAmount;
                    } else if (flag3 > -1) {//如果最大使用积分大于等于实收金额抵扣积分，就把实收金额抵扣积分赋值给抵扣积分
                        redepositAmount = receivedAmount;
                    } else {
                        redepositAmount = maximumNumber;
                    }
                }
                if (flag2 > -1) { //账户积分大于等于最大使用积分，就用最大使用积分抵扣
                    int flag3 = maximumNumber.compareTo(receivedAmount);
                    if (flag3 == 1) { //如果最大使用积分大于实收金额抵扣积分，就把实收金额抵扣积分赋值给抵扣积分
                        redepositAmount = receivedAmount;
                    } else if (flag3 > -1) {//如果最大使用积分大于等于实收金额抵扣积分，就把实收金额抵扣积分赋值给抵扣积分
                        redepositAmount = receivedAmount;
                    } else {
                        redepositAmount = maximumNumber;
                    }
                }
                if (flag2 == -1) { //账户积分小于最大使用积分，就用账户积分抵扣
                    int flag3 = amount.compareTo(receivedAmount);
                    if (flag3 == 1) { //如果积分账户大于实收金额抵扣积分，就把实收金额抵扣积分赋值给抵扣积分
                        redepositAmount = receivedAmount;
                    } else if (flag3 > -1) {//如果积分账户大于等于实收金额抵扣积分，就把实收金额抵扣积分赋值给抵扣积分
                        redepositAmount = receivedAmount;
                    } else {
                        redepositAmount = amount;
                    }
                }
                if (flag2 < 1) { //账户积分小于等于最大使用积分，就用账户积分抵扣
                    int flag3 = amount.compareTo(receivedAmount);
                    if (flag3 == 1) { //如果积分账户大于实收金额抵扣积分，就把实收金额抵扣积分赋值给抵扣积分
                        redepositAmount = receivedAmount;
                    } else if (flag3 > -1) {//如果积分账户大于等于实收金额抵扣积分，就把实收金额抵扣积分赋值给抵扣积分
                        redepositAmount = receivedAmount;
                    } else {
                        redepositAmount = amount;
                    }
                }
                if (flag2 == 0) { //账户积分等于最大使用积分
                    int flag3 = amount.compareTo(receivedAmount);
                    if (flag3 == 1) { //如果积分账户大于实收金额抵扣积分，就把实收金额抵扣积分赋值给抵扣积分
                        redepositAmount = receivedAmount;
                    } else if (flag3 > -1) {//如果积分账户大于等于实收金额抵扣积分，就把实收金额抵扣积分赋值给抵扣积分
                        redepositAmount = receivedAmount;
                    } else {
                        redepositAmount = amount;
                    }
                }
                //计算积分换算的金额
                BigDecimal divide = redepositAmount.divide(deductionProportion);
                BigDecimal divide1 = receivedAmount.divide(deductionProportion);
                //计算还需支付的金额
                BigDecimal subtract = divide1.subtract(divide);
                paramObj.put("receivedAmount", subtract);
            }
            String acctId = param.getString("acctId");
            if (StringUtil.isEmpty(acctId)) {
                throw new IllegalArgumentException("账户id为空！");
            }
            AccountDto accountDto = new AccountDto();
            accountDto.setAcctId(acctId);
            //查询账户金额
            accountDtos = accountInnerServiceSMOImpl.queryAccounts(accountDto);
            Assert.listOnlyOne(accountDtos, "查询账户金额错误！");
            if (accountDtos != null && accountDtos.size() > 0) {
                AccountDto accountDto1 = accountDtos.get(0);
                BigDecimal accountDto1Amount = new BigDecimal(accountDto1.getAmount());
                if (accountDto1Amount.compareTo(redepositAmount) == -1) {
                    throw new UnsupportedOperationException("账户金额抵扣不足，请您确认账户金额！");
                }
            }
            AccountDetailPo accountDetailPo = new AccountDetailPo();
            accountDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
            int flag1 = redepositAmount.compareTo(BigDecimal.ZERO);
            if (flag1 == 1) {
                accountDetailPo.setAmount(redepositAmount + "");
                accountDetailPo.setDetailType(AccountDetailDto.DETAIL_TYPE_OUT);
                accountDetailPo.setRemark("前台缴费扣款");
            }
            accountDetailPo.setOrderId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_orderId));
            accountDetailPo.setObjType(accountDtos.get(0).getObjType());
            accountDetailPo.setObjId(accountDtos.get(0).getObjId());
            accountDetailPo.setAcctId(accountDtos.get(0).getAcctId());
            accountDetailPo.setRelAcctId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));

            flag = accountInnerServiceSMOImpl.withholdAccount(accountDetailPo);

            if (flag < 1) {
                throw new CmdException("扣款失败");
            }
        }
    }


    /**
     * 校验是否按缴费时间段缴费
     *
     * @param reqJson
     * @param feeConfigDto
     */
    private void validateIfPayFeeStartEndDate(JSONObject reqJson, FeeConfigDto feeConfigDto) {
        if (!"-105".equals(reqJson.getString("cycles"))) {
            return;
        }
        // todo 自己是间接性费用
        if (FeeDto.FEE_FLAG_CYCLE_ONCE.equals(feeConfigDto.getFeeFlag())) {
            return;
        }

        FeeConfigDto tmpFeeConfigDto = new FeeConfigDto();
        tmpFeeConfigDto.setFeeNameEq(feeConfigDto.getFeeName() + "欠费");
        tmpFeeConfigDto.setFeeFlag(FeeDto.FEE_FLAG_CYCLE_ONCE);
        tmpFeeConfigDto.setComputingFormula(feeConfigDto.getComputingFormula());
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(tmpFeeConfigDto);

        Assert.listOnlyOne(feeConfigDtos, "按自定义时间段缴费时，费用必须为间接性费用，或者存在名称为 " + feeConfigDto.getFeeName() + "欠费 的间接性费用，它的公式计算必须要和" + feeConfigDto.getFeeName() + "一致");
    }


    /**
     * 自定义时间段 缴费
     *
     * @param cycles
     * @param endTime
     * @param payFeeDetailPo
     * @param payFeePo
     */
    private void ifCustomStartEndTimePayFee(String cycles, Date endTime, PayFeeDetailPo payFeeDetailPo, PayFeePo payFeePo, JSONObject reqJson) {
        if (!"-105".equals(cycles)) {
            return;
        }

        //todo 如果是同一天不创建
        if (DateUtil.getFormatTimeStringB(endTime).equals(reqJson.getString("customStartTime"))) {
            return;
        }

        FeeDto feeInfo = (FeeDto) reqJson.get("feeInfo");
        String payObjNameRemark = "房屋";
        if (FeeDto.PAYER_OBJ_TYPE_CAR.equals(feeInfo.getPayerObjType())) {
            payObjNameRemark = "车辆";
        } else if (FeeDto.PAYER_OBJ_TYPE_CONTRACT.equals(feeInfo.getPayerObjType())) {
            payObjNameRemark = "合同";
        }

        //todo 补充一条 缴费记录数据
        PayFeeDetailPo tmpPayFeeDetailPo = BeanConvertUtil.covertBean(payFeeDetailPo, PayFeeDetailPo.class);
        tmpPayFeeDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        tmpPayFeeDetailPo.setCycles("0");
        tmpPayFeeDetailPo.setReceivableAmount("0");
        tmpPayFeeDetailPo.setReceivedAmount("0");
        tmpPayFeeDetailPo.setPayableAmount("0");
        tmpPayFeeDetailPo.setStartTime(DateUtil.getFormatTimeStringB(endTime));
        tmpPayFeeDetailPo.setEndTime(reqJson.getString("customStartTime"));
        tmpPayFeeDetailPo.setState(FeeDetailDto.STATE_OWE);
        tmpPayFeeDetailPo.setOpenInvoice("N");
        tmpPayFeeDetailPo.setRemark("按缴费时间段缴费,这部分费用按欠费的方式重新生成，请在" + payObjNameRemark + "上查看");
        if (!StringUtil.isEmpty(reqJson.getString("cashAmount")) && !StringUtil.isEmpty(reqJson.getString("integralAmount"))) {
            BigDecimal cashAmount = new BigDecimal(reqJson.getString("cashAmount")).setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal integralAmount = new BigDecimal(reqJson.getString("integralAmount")).setScale(2, BigDecimal.ROUND_HALF_UP);
            if (!StringUtil.isEmpty(tmpPayFeeDetailPo.getRemark())) {
                tmpPayFeeDetailPo.setRemark(tmpPayFeeDetailPo.getRemark() + "，现金账户抵扣" + cashAmount + "元，积分账户抵扣" + integralAmount + "元");
            } else {
                tmpPayFeeDetailPo.setRemark("现金账户抵扣" + cashAmount + "元，积分账户抵扣" + integralAmount + "元");
            }
        }
        if (!StringUtil.isEmpty(reqJson.getString("cashAmount")) && StringUtil.isEmpty(reqJson.getString("integralAmount"))) {
            BigDecimal cashAmount = new BigDecimal(reqJson.getString("cashAmount")).setScale(2, BigDecimal.ROUND_HALF_UP);
            if (!StringUtil.isEmpty(tmpPayFeeDetailPo.getRemark())) {
                tmpPayFeeDetailPo.setRemark(tmpPayFeeDetailPo.getRemark() + "，现金账户抵扣" + cashAmount + "元");
            } else {
                tmpPayFeeDetailPo.setRemark("现金账户抵扣" + cashAmount + "元");
            }
        }
        if (StringUtil.isEmpty(reqJson.getString("cashAmount")) && !StringUtil.isEmpty(reqJson.getString("integralAmount"))) {
            BigDecimal integralAmount = new BigDecimal(reqJson.getString("integralAmount")).setScale(2, BigDecimal.ROUND_HALF_UP);
            if (!StringUtil.isEmpty(tmpPayFeeDetailPo.getRemark())) {
                tmpPayFeeDetailPo.setRemark(tmpPayFeeDetailPo.getRemark() + "，积分账户抵扣" + integralAmount + "元");
            } else {
                tmpPayFeeDetailPo.setRemark("积分账户抵扣" + integralAmount + "元");
            }
        }
        int flag = payFeeDetailNewV1InnerServiceSMOImpl.savePayFeeDetailNew(tmpPayFeeDetailPo);

        if (flag < 1) {
            throw new CmdException("生成欠费失败");
        }

        //todo 生成费用
        PayFeePo tmpPayFeePo = BeanConvertUtil.covertBean(feeInfo, PayFeePo.class);
        tmpPayFeePo.setFeeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
        tmpPayFeePo.setEndTime(DateUtil.getFormatTimeStringB(endTime));
        tmpPayFeePo.setState(FeeDto.STATE_DOING);

        // todo 处理configId
        doChangeConfigId(tmpPayFeePo, feeInfo);


        flag = payFeeV1InnerServiceSMOImpl.savePayFee(tmpPayFeePo);
        if (flag < 1) {
            throw new CmdException("生成欠费失败");
        }

        //todo 补充 费用属性
        FeeAttrDto feeAttrDto = new FeeAttrDto();
        feeAttrDto.setFeeId(payFeePo.getFeeId());
        feeAttrDto.setCommunityId(payFeePo.getCommunityId());
        List<FeeAttrDto> feeAttrDtos = feeAttrInnerServiceSMOImpl.queryFeeAttrs(feeAttrDto);

        if (feeAttrDtos == null || feeAttrDtos.size() < 1) {
            return;
        }

        List<FeeAttrPo> tmpFeeAttrPos = new ArrayList<>();
        FeeAttrPo tmpFeeAttrPo = null;
        boolean hasDeadLineTime = false;
        for (FeeAttrDto tmpFeeAttrDto : feeAttrDtos) {
            tmpFeeAttrDto.setFeeId(tmpPayFeePo.getFeeId());
            tmpFeeAttrDto.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId, true));

            if (FeeAttrDto.SPEC_CD_ONCE_FEE_DEADLINE_TIME.equals(tmpFeeAttrDto.getSpecCd())) {
                tmpFeeAttrDto.setValue(reqJson.getString("customStartTime"));
                hasDeadLineTime = true;
            }
            tmpFeeAttrPo = BeanConvertUtil.covertBean(tmpFeeAttrDto, FeeAttrPo.class);
            tmpFeeAttrPos.add(tmpFeeAttrPo);
        }
        //todo 没有结束时间时
        if (!hasDeadLineTime) {
            tmpFeeAttrPo = new FeeAttrPo();
            tmpFeeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId, true));
            tmpFeeAttrPo.setFeeId(tmpPayFeePo.getFeeId());
            tmpFeeAttrPo.setCommunityId(tmpFeeAttrPo.getCommunityId());
            tmpFeeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_ONCE_FEE_DEADLINE_TIME);
            tmpFeeAttrPo.setValue(reqJson.getString("customStartTime"));
            tmpFeeAttrPos.add(tmpFeeAttrPo);
        }

        feeAttrInnerServiceSMOImpl.saveFeeAttrs(tmpFeeAttrPos);

    }

    /**
     * 处理费用项ID
     *
     * @param tmpPayFeePo
     * @param feeInfo
     */
    private void doChangeConfigId(PayFeePo tmpPayFeePo, FeeDto feeInfo) {
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setConfigId(feeInfo.getConfigId());
        feeConfigDto.setCommunityId(feeInfo.getCommunityId());
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
        Assert.listOnlyOne(feeConfigDtos, "费用项不存在");
        if (FeeDto.FEE_FLAG_CYCLE_ONCE.equals(feeConfigDtos.get(0).getFeeFlag())) {
            return;
        }


        FeeConfigDto tmpFeeConfigDto = new FeeConfigDto();
        tmpFeeConfigDto.setFeeNameEq(feeConfigDtos.get(0).getFeeName() + "欠费");
        tmpFeeConfigDto.setFeeFlag(FeeDto.FEE_FLAG_CYCLE_ONCE);
        tmpFeeConfigDto.setComputingFormula(feeConfigDto.getComputingFormula());
        //todo 校验的时候校验过了 所以这里不可能为空
        feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(tmpFeeConfigDto);

        tmpPayFeePo.setConfigId(feeConfigDtos.get(0).getConfigId());
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

}
