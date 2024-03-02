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
import com.java110.dto.app.AppDto;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.fee.FeeReceiptDetailDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.dto.user.UserDto;
import com.java110.fee.bmo.fee.IFinishFeeNotify;
import com.java110.fee.smo.impl.FeeReceiptInnerServiceSMOImpl;
import com.java110.intf.acct.IAccountInnerServiceSMO;
import com.java110.intf.community.*;
import com.java110.intf.fee.*;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.account.AccountDetailPo;
import com.java110.po.car.OwnerCarPo;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.fee.FeeReceiptPo;
import com.java110.po.fee.FeeReceiptDetailPo;
import com.java110.po.owner.RepairPoolPo;
import com.java110.po.owner.RepairUserPo;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

@Java110Cmd(serviceCode = "fee.payOweFee")
public class PayOweFeeCmd extends Cmd {
    private static Logger logger = LoggerFactory.getLogger(PayOweFeeCmd.class);
    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;
    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IFeeReceiptInnerServiceSMO feeReceiptInnerServiceSMOImpl;

    @Autowired
    private IFeeReceiptDetailInnerServiceSMO feeReceiptDetailInnerServiceSMOImpl;


    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeDetailV1InnerServiceSMO payFeeDetailV1InnerServiceSMOImpl;


    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IFinishFeeNotify finishFeeNotifyImpl;

    @Autowired
    private IAccountInnerServiceSMO accountInnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        Assert.hasKey(reqJson, "fees", "请求报文中未包含费用信息");

        JSONArray fees = reqJson.getJSONArray("fees");

        JSONObject feeObject = null;

        for (int feeIndex = 0; feeIndex < fees.size(); feeIndex++) {
            feeObject = fees.getJSONObject(feeIndex);
            Assert.hasKeyAndValue(feeObject, "feeId", "未包含费用信息");
            Assert.hasKeyAndValue(feeObject, "startTime", "未包含开始时间");
            Assert.hasKeyAndValue(feeObject, "endTime", "未包含结束时间");
            Assert.hasKeyAndValue(feeObject, "receivedAmount", "未包含实收金额");

            //计算 应收金额
            FeeDto feeDto = new FeeDto();
            feeDto.setFeeId(feeObject.getString("feeId"));
            feeDto.setCommunityId(feeObject.getString("communityId"));
            Date pageEndTime = null;
            List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
            if (feeDtos == null || feeDtos.size() != 1) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "查询费用信息失败，未查到数据或查到多条数据");
            }
            feeDto = feeDtos.get(0);
            feeObject.put("feeDto", feeDto);
            if (!FeeDto.FEE_FLAG_CYCLE.equals(feeDto.getFeeFlag())) {
                continue;
            }

            pageEndTime = DateUtil.getDateFromStringB(feeObject.getString("endTime"));
            if (pageEndTime.getTime() <= feeDto.getEndTime().getTime()) {
                throw new IllegalArgumentException("可能存在重复缴费，请刷新页面重新缴费");
            }
        }

        //todo 从账户中扣款
        ifHasAccount(reqJson, fees);
    }


    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext dataFlowContext, JSONObject paramObj) throws CmdException, ParseException {
        logger.info("======欠费缴费返回======：" + JSONArray.toJSONString(paramObj));
        String userId = dataFlowContext.getReqHeaders().get("user-id");
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        Assert.listOnlyOne(userDtos, "用户未登录");

        String payOrderId = paramObj.getString("payOrderId");

        JSONArray fees = paramObj.getJSONArray("fees");
        JSONObject feeObj = null;
        String appId = dataFlowContext.getReqHeaders().get("app-id");


        //todo 生成收据编号
        String receiptCode = feeReceiptInnerServiceSMOImpl.generatorReceiptCode(paramObj.getString("communityId"));
        //todo 根据明细ID 查询收据信息
        JSONArray details = new JSONArray();

        for (int feeIndex = 0; feeIndex < fees.size(); feeIndex++) {
            feeObj = fees.getJSONObject(feeIndex);
            feeObj.put("communityId", paramObj.getString("communityId"));
            if (paramObj.containsKey("oId")) {
                feeObj.put("oId", paramObj.getString("oId"));
            }
            String remark = paramObj.getString("remark");
            feeObj.put("remark", remark);
            if (!feeObj.containsKey("primeRate") && AppDto.OWNER_WECHAT_PAY.equals(appId)) {  //微信公众号支付
                feeObj.put("primeRate", "5");
                feeObj.put("remark", "线上公众号支付");
            }
            if (!feeObj.containsKey("primeRate")) {
                feeObj.put("primeRate", "6");
            }
            logger.info("======支付方式======：" + appId + "+======+" + feeObj.containsKey("primeRate") + "======:" + JSONArray.toJSONString(dataFlowContext));
            if (AppDto.OWNER_WECHAT_PAY.equals(appId)
                    && FeeDetailDto.PRIME_REATE_WECHAT.equals(feeObj.getString("primeRate"))) {  //微信支付（欠费缴费无法区分小程序还是微信公众号）
                feeObj.put("remark", "线上公众号支付");
            } else if (AppDto.OWNER_WECHAT_PAY.equals(appId)
                    && FeeDetailDto.PRIME_REATE_WECHAT_APP.equals(feeObj.getString("primeRate"))) {
                feeObj.put("remark", "线上小程序支付");
            }
            feeObj.put("state", "1400");
            // todo 添加交费明细
            addOweFeeDetail(feeObj, details, userDto, receiptCode, payOrderId);
            modifyOweFee(feeObj, dataFlowContext);

            //todo 账户扣款
            finishFeeNotifyImpl.withholdAccount(feeObj, feeObj.getString("feeId"), feeObj.getString("communityId"));

            //todo 修改车辆
            finishFeeNotifyImpl.updateCarEndTime(feeObj.getString("feeId"), feeObj.getString("communityId"));


            //todo 修改报修单
            finishFeeNotifyImpl.updateRepair(feeObj.getString("feeId"), feeObj.getString("communityId"), feeObj.getString("receivedAmount"));
        }


        JSONObject data = new JSONObject();
        data.put("details", details);

        dataFlowContext.setResponseEntity(ResultVo.createResponseEntity(data));
    }


    /**
     * 修改费用信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void modifyOweFee(JSONObject paramInJson, ICmdDataFlowContext dataFlowContext) {

        JSONObject businessFee = new JSONObject();
        FeeDto feeInfo = (FeeDto) paramInJson.get("feeInfo");
        Map feeMap = BeanConvertUtil.beanCovertMap(feeInfo);
        feeMap.put("startTime", DateUtil.getFormatTimeString(feeInfo.getStartTime(), DateUtil.DATE_FORMATE_STRING_A));
        feeMap.put("endTime", paramInJson.getString("endTime"));
        feeMap.put("cycles", paramInJson.getString("cycles"));
        feeMap.put("configEndTime", feeInfo.getConfigEndTime());
        if (FeeDto.FEE_FLAG_ONCE.equals(feeInfo.getFeeFlag())) { //缴费结束
            feeMap.put("state", FeeDto.STATE_FINISH);
        }
        Date maxEndTime = feeInfo.getConfigEndTime();
        if (!FeeDto.FEE_FLAG_CYCLE.equals(feeInfo.getFeeFlag())) {
            maxEndTime = feeInfo.getDeadlineTime();
        }
        if (maxEndTime != null) { //这里数据问题的情况下
            Date endTime = DateUtil.getDateFromStringA(paramInJson.getString("endTime"));
            if (endTime.getTime() >= maxEndTime.getTime()) {
                feeMap.put("state", FeeDto.STATE_FINISH);
            }
        }

        businessFee.putAll(feeMap);
        PayFeePo payFeePo = BeanConvertUtil.covertBean(businessFee, PayFeePo.class);
        int flag = payFeeV1InnerServiceSMOImpl.updatePayFee(payFeePo);
        if (flag < 1) {
            throw new CmdException("修改失败");
        }
    }

    public void addOweFeeDetail(JSONObject paramInJson,
                                JSONArray detailIds,
                                UserDto userDto,
                                String receiptCode,
                                String payOrderId) {
        JSONObject businessFeeDetail = new JSONObject();
        businessFeeDetail.putAll(paramInJson);
        businessFeeDetail.put("detailId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        businessFeeDetail.put("primeRate", paramInJson.getString("primeRate"));
        FeeDto feeDto = (FeeDto) paramInJson.get("feeDto");


        businessFeeDetail.put("startTime", paramInJson.getString("startTime"));
        BigDecimal cycles = null;
        Map feePriceAll = computeFeeSMOImpl.getFeePrice(feeDto);
        BigDecimal feePrice = new BigDecimal(feePriceAll.get("feePrice").toString());
        Date endTime = feeDto.getEndTime();
        Calendar endCalender = Calendar.getInstance();
        endCalender.setTime(endTime);
        BigDecimal receivedAmount = new BigDecimal(Double.parseDouble(paramInJson.getString("receivedAmount")));
        cycles = receivedAmount.divide(feePrice, 4, BigDecimal.ROUND_HALF_EVEN);
        businessFeeDetail.put("cycles", receivedAmount.divide(feePrice, 2, BigDecimal.ROUND_HALF_UP).doubleValue());

        if (!paramInJson.containsKey("receivableAmount") || StringUtil.isEmpty(paramInJson.getString("receivableAmount"))) {
            paramInJson.put("receivableAmount", paramInJson.getString("receivedAmount"));
        }

        businessFeeDetail.put("receivableAmount", paramInJson.getString("receivableAmount"));
        businessFeeDetail.put("receivedAmount", paramInJson.getString("receivedAmount"));
        businessFeeDetail.put("payableAmount", paramInJson.getString("receivedAmount"));
        businessFeeDetail.put("endTime", paramInJson.getString("endTime"));
        paramInJson.put("feeInfo", feeDto);
        paramInJson.put("cycles", cycles.doubleValue());
        PayFeeDetailPo payFeeDetailPo = BeanConvertUtil.covertBean(businessFeeDetail, PayFeeDetailPo.class);
        if (paramInJson.containsKey("oId")) {
            payFeeDetailPo.setPayOrderId(paramInJson.getString("oId"));
        } else {
            String oId = Java110TransactionalFactory.getOId();
            if (StringUtil.isEmpty(oId)) {
                oId = payFeeDetailPo.getDetailId();
            }
            payFeeDetailPo.setPayOrderId(oId);

        }

        // todo 如果 扫码枪支付 输入支付订单ID
        if (!StringUtil.isEmpty(payOrderId)) {
            payFeeDetailPo.setPayOrderId(payOrderId);
        }
        if (paramInJson.containsKey("accountAmount")) {
            payFeeDetailPo.setAcctAmount(paramInJson.getString("accountAmount"));
        }

        payFeeDetailPo.setCashierId(userDto.getUserId());
        payFeeDetailPo.setCashierName(userDto.getName());
        payFeeDetailPo.setOpenInvoice("N");
        //todo 缓存收据编号
        CommonCache.setValue(payFeeDetailPo.getDetailId() + CommonCache.RECEIPT_CODE, receiptCode, CommonCache.DEFAULT_EXPIRETIME_TWO_MIN);
        int flag = payFeeDetailV1InnerServiceSMOImpl.savePayFeeDetailNew(payFeeDetailPo);

        if (flag < 1) {
            throw new CmdException("保存明细失败");
        }

        paramInJson.put("detailId", businessFeeDetail.getString("detailId"));
        detailIds.add(businessFeeDetail.getString("detailId"));
    }


    private void ifHasAccount(JSONObject reqJson, JSONArray fees) {
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

        // todo 从费用实际缴费中扣款
        JSONObject feeObject = null;
        double receivedAmount = 0.0;
        BigDecimal receivedAmountDec = null;
        for (int feeIndex = 0; feeIndex < fees.size(); feeIndex++) {
            if (accountAmount == 0) {
                continue;
            }
            accountAmountDec = new BigDecimal(accountAmount);
            feeObject = fees.getJSONObject(feeIndex);
            receivedAmount = feeObject.getDouble("receivedAmount");
            receivedAmountDec = new BigDecimal(receivedAmount);
            if (receivedAmount >= accountAmount) {
                receivedAmountDec = receivedAmountDec.subtract(accountAmountDec).setScale(2, BigDecimal.ROUND_HALF_UP);
                feeObject.put("receivedAmount", receivedAmountDec.doubleValue());
                feeObject.put("accountAmount", accountAmount);
                feeObject.put("acctId", acctId);
                accountAmount = 0.00;
                continue;
            }

            feeObject.put("receivedAmount", "0");
            feeObject.put("accountAmount", receivedAmount);
            feeObject.put("acctId", acctId);

            accountAmountDec = accountAmountDec.subtract(receivedAmountDec).setScale(2, BigDecimal.ROUND_HALF_UP);
            accountAmount = accountAmountDec.doubleValue();
        }


    }
}
