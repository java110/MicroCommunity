package com.java110.fee.cmd.fee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.account.AccountDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.couponUser.CouponUserDto;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.feeDiscount.ComputeDiscountDto;
import com.java110.intf.acct.IAccountDetailInnerServiceSMO;
import com.java110.intf.acct.IAccountInnerServiceSMO;
import com.java110.intf.acct.ICouponUserV1InnerServiceSMO;
import com.java110.intf.community.ICommunityV1InnerServiceSMO;
import com.java110.intf.community.IRepairUserInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.*;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.po.account.AccountPo;
import com.java110.po.accountDetail.AccountDetailPo;
import com.java110.po.feeAccountDetail.FeeAccountDetailPo;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 类表述：删除
 * 服务编码：feePrintPage.deleteFeePrintPage
 * 请求路劲：/app/feePrintPage.DeleteFeePrintPage
 * add by 吴学文 at 2021-09-16 22:26:04 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "fee.payFeePre")
public class PayFeePreCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(PayFeePreCmd.class);

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IFeeDiscountInnerServiceSMO feeDiscountInnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Autowired
    private IRepairUserInnerServiceSMO repairUserInnerServiceSMO;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IApplyRoomDiscountInnerServiceSMO applyRoomDiscountInnerServiceSMOImpl;

    @Autowired
    private IFeeDetailInnerServiceSMO iFeeDetailInnerServiceSMO;

    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    @Autowired
    private ICouponUserV1InnerServiceSMO couponUserV1InnerServiceSMOImpl;

    @Autowired
    private IAccountInnerServiceSMO accountInnerServiceSMOImpl;

    @Autowired
    private ICommunityV1InnerServiceSMO communityV1InnerServiceSMOImpl;

    @Autowired
    private IAccountDetailInnerServiceSMO accountDetailInnerServiceSMOImpl;

    @Autowired
    private IFeeAccountDetailServiceSMO feeAccountDetailServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(reqJson, "cycles", "请求报文中未包含cycles节点");
        Assert.jsonObjectHaveKey(reqJson, "receivedAmount", "请求报文中未包含receivedAmount节点");
        Assert.jsonObjectHaveKey(reqJson, "feeId", "请求报文中未包含feeId节点");
        Assert.jsonObjectHaveKey(reqJson, "appId", "请求报文中未包含appId节点");

        Assert.hasLength(reqJson.getString("communityId"), "小区ID不能为空");
        Assert.hasLength(reqJson.getString("cycles"), "周期不能为空");
        Assert.hasLength(reqJson.getString("receivedAmount"), "实收金额不能为空");
        Assert.hasLength(reqJson.getString("feeId"), "费用ID不能为空");
        Assert.hasLength(reqJson.getString("appId"), "appId不能为空");


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

        if (feeConfigDtos != null && feeConfigDtos.size() == 1) {
            try {
                Date configEndTime = DateUtil.getDateFromString(feeConfigDtos.get(0).getEndTime(), DateUtil.DATE_FORMATE_STRING_A);
                configEndTime = DateUtil.stepDay(configEndTime, 5);

                Date newDate = DateUtil.stepMonth(endTime, reqJson.getInteger("cycles"));

                if (newDate.getTime() > configEndTime.getTime()) {
                    throw new IllegalArgumentException("缴费周期超过 缴费结束时间");
                }

            } catch (Exception e) {
                logger.error("比较费用日期失败", e);
            }
        }

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        logger.debug("ServiceDataFlowEvent : {}", event);

        String appId = cmdDataFlowContext.getReqHeaders().get("app-id");
        reqJson.put("appId", appId);

        FeeDto feeDto = new FeeDto();
        feeDto.setFeeId(reqJson.getString("feeId"));
        feeDto.setCommunityId(reqJson.getString("communityId"));
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
        if (feeDtos == null || feeDtos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "查询费用信息失败，未查到数据或查到多条数据");
        }
        feeDto = feeDtos.get(0);
        reqJson.put("feeTypeCd", feeDto.getFeeTypeCd());
        reqJson.put("feeId", feeDto.getFeeId());
        Map feePriceAll = computeFeeSMOImpl.getFeePrice(feeDto);
        BigDecimal receivableAmount = new BigDecimal(feePriceAll.get("feePrice").toString());
        BigDecimal cycles = new BigDecimal(Double.parseDouble(reqJson.getString("cycles")));
        double tmpReceivableAmount = cycles.multiply(receivableAmount).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
        JSONObject paramOut = new JSONObject();
        paramOut.put("receivableAmount", tmpReceivableAmount);
        paramOut.put("oId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_oId));

        //实收金额
        BigDecimal tmpReceivedAmout = new BigDecimal(tmpReceivableAmount);

        //判断是否有折扣情况
        double discountPrice = judgeDiscount(reqJson);
        tmpReceivedAmout = tmpReceivedAmout.subtract(new BigDecimal(discountPrice)).setScale(2, BigDecimal.ROUND_HALF_EVEN);
        //2.0 考虑账户抵消
        double accountPrice = judgeAccount(reqJson);
        tmpReceivedAmout = tmpReceivedAmout.subtract(new BigDecimal(accountPrice)).setScale(2, BigDecimal.ROUND_HALF_EVEN);

        //3.0 考虑优惠卷
        double couponPrice = checkCouponUser(reqJson);
        tmpReceivedAmout = tmpReceivedAmout.subtract(new BigDecimal(couponPrice)).setScale(2, BigDecimal.ROUND_HALF_EVEN);


        double receivedAmount = tmpReceivedAmout.doubleValue();
        //所有 优惠折扣计算完后，如果总金额小于等于0，则返回总扣款为0
        if (receivedAmount <= 0) {
            receivedAmount = 0.0;
        }
        paramOut.put("receivedAmount", receivedAmount);

        String feeName = getObjName(feeDto);
        paramOut.put("feeName", feeName);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(paramOut.toJSONString(), HttpStatus.OK);
        reqJson.putAll(paramOut);
        CommonCache.setValue("payFeePre" + paramOut.getString("oId"), reqJson.toJSONString(), 24 * 60 * 60);
        cmdDataFlowContext.setResponseEntity(responseEntity);
    }

    private String getObjName(FeeDto feeDto) {
        //查询小区名称
        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(feeDto.getCommunityId());
        List<CommunityDto> communityDtos = communityV1InnerServiceSMOImpl.queryCommunitys(communityDto);

        Assert.listOnlyOne(communityDtos, "小区不存在");

        List<FeeAttrDto> feeAttrDtos = feeDto.getFeeAttrDtos();
        if (feeAttrDtos == null || feeAttrDtos.size() < 1) {
            return communityDtos.get(0).getName() + "-" + feeDto.getFeeName();
        }

        for (FeeAttrDto feeAttrDto : feeAttrDtos) {
            if (FeeAttrDto.SPEC_CD_PAY_OBJECT_NAME.equals(feeAttrDto.getSpecCd())) {
                return communityDtos.get(0).getName() + "-" + feeAttrDto.getValue() + "-" + feeDto.getFeeName();
            }
        }

        return communityDtos.get(0).getName() + "-" + feeDto.getFeeName();
    }

    /**
     * 考虑账户抵消
     *
     * @param reqJson
     */
    private double judgeAccount(JSONObject reqJson) {
        if (!reqJson.containsKey("deductionAmount")) {
            reqJson.put("deductionAmount", 0.0);
            return 0.0;
        }

        double deductionAmount = reqJson.getDouble("deductionAmount");
        if (deductionAmount <= 0) {
            reqJson.put("deductionAmount", 0.0);
            return 0.0;
        }

        if (!reqJson.containsKey("selectUserAccount")) {
            reqJson.put("deductionAmount", 0.0);
            return 0.0;
        }

        JSONArray selectUserAccount = reqJson.getJSONArray("selectUserAccount");
        if (selectUserAccount == null || selectUserAccount.size() < 1) {
            reqJson.put("deductionAmount", 0.0);
            return 0.0;
        }
        List<String> acctIds = new ArrayList<>();
        for (int userAccountIndex = 0; userAccountIndex < selectUserAccount.size(); userAccountIndex++) {
            acctIds.add(selectUserAccount.getJSONObject(userAccountIndex).getString("acctId"));
        }

        AccountDto accountDto = new AccountDto();
        accountDto.setAcctIds(acctIds.toArray(new String[acctIds.size()]));
        List<AccountDto> accountDtos = accountInnerServiceSMOImpl.queryAccounts(accountDto);

        if (accountDtos == null || accountDtos.size() < 1) {
            reqJson.put("deductionAmount", 0.0);
            return 0.0;
        }

        BigDecimal money = new BigDecimal(0);
        BigDecimal totalAccountAmount = new BigDecimal(0);
        for (AccountDto tmpAccountDto : accountDtos) {
            if (!StringUtil.isEmpty(tmpAccountDto.getAcctType()) && tmpAccountDto.getAcctType().equals("2004")) { //积分账户
                String maximum = "";
                String deduction = "";
                for (int index = 0; index < selectUserAccount.size(); index++) {
                    JSONObject param = selectUserAccount.getJSONObject(index);
                    if (!StringUtil.isEmpty(param.getString("acctType")) && param.getString("acctType").equals("2004")) { //积分账户
                        maximum = param.getString("maximumNumber");
                        deduction = param.getString("deductionProportion");
                    }
                }
                //账户金额
                BigDecimal amount = new BigDecimal(tmpAccountDto.getAmount());
                //获取最大抵扣积分
                BigDecimal maximumNumber = new BigDecimal(maximum);
                //获取积分抵扣比例
                BigDecimal deductionProportion = new BigDecimal(deduction);
                int flag = amount.compareTo(maximumNumber);
                BigDecimal redepositAmount = new BigDecimal("0.00");
                BigDecimal integralAmount = new BigDecimal("0.00");
                if (flag == 1) { //账户积分大于最大使用积分，就用最大使用积分抵扣
                    redepositAmount = maximumNumber;
                    integralAmount = amount.subtract(maximumNumber);
                }
                if (flag > -1) { //账户积分大于等于最大使用积分，就用最大使用积分抵扣
                    redepositAmount = maximumNumber;
                    integralAmount = amount.subtract(maximumNumber);
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
                //更新账户信息
//                AccountPo accountPo = new AccountPo();
//                accountPo.setAcctId(tmpAccountDto.getAcctId());
//                accountPo.setAmount(integralAmount.toString());
//                accountInnerServiceSMOImpl.updateAccount(accountPo);
                //生成账户详情
//                AccountDetailPo accountDetailPo = new AccountDetailPo();
//                accountDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
//                accountDetailPo.setAcctId(tmpAccountDto.getAcctId());
//                accountDetailPo.setDetailType("2002"); //1001 转入 2002 转出
//                accountDetailPo.setRelAcctId("-1");
//                accountDetailPo.setAmount(redepositAmount.toString());
//                accountDetailPo.setObjType("6006"); //6006 个人 7007 商户
//                accountDetailPo.setObjId(tmpAccountDto.getObjId());
//                accountDetailPo.setOrderId("-1");
//                accountDetailPo.setbId("-1");
//                accountDetailPo.setRemark("手机端积分抵扣");
//                accountDetailPo.setCreateTime(new Date());
//                accountDetailInnerServiceSMOImpl.saveAccountDetails(accountDetailPo);
                //计算积分换算的金额
                BigDecimal divide = redepositAmount.divide(deductionProportion);
                BigDecimal dedAmount = new BigDecimal(deductionAmount);
                //计算实付金额
                int flag2 = divide.compareTo(dedAmount);
                BigDecimal subtract = new BigDecimal("0.00");
                //生成抵扣明细记录
                FeeAccountDetailPo feeAccountDetailPo = new FeeAccountDetailPo();
                if (flag2 == -1) { //积分换算金额小于应付金额
                    //subtract = dedAmount.subtract(divide);
                    BigDecimal multiply = divide.multiply(deductionProportion);
                    feeAccountDetailPo.setAmount(multiply.toString()); //积分抵扣金额
                } else if (flag < 1) { //积分换算金额小于等于应付金额
                    //subtract = dedAmount.subtract(divide);
                    BigDecimal multiply = divide.multiply(deductionProportion);
                    feeAccountDetailPo.setAmount(multiply.toString()); //积分抵扣金额
                } else {
                    BigDecimal multiply = dedAmount.multiply(deductionProportion);
                    feeAccountDetailPo.setAmount(multiply.toString()); //积分抵扣金额
                }
                reqJson.put("receivedMoney", divide);
//                feeAccountDetailPo.setFadId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_fadId));
//                feeAccountDetailPo.setDetailId(accountDetailPo.getDetailId());
//                feeAccountDetailPo.setCommunityId(reqJson.getString("communityId"));
//                feeAccountDetailPo.setState("1003"); //1001 无抵扣 1002 现金账户抵扣 1003 积分账户抵扣 1004 优惠券抵扣
//                feeAccountDetailServiceSMOImpl.saveFeeAccountDetail(feeAccountDetailPo);
                money = divide;
            } else if (!StringUtil.isEmpty(tmpAccountDto.getAcctType()) && tmpAccountDto.getAcctType().equals("2003")) { //现金账户
                //账户金额
                BigDecimal amount = new BigDecimal(tmpAccountDto.getAmount());
                //获取应收金额
                BigDecimal dedAmount = new BigDecimal("0.00");
                if (reqJson.containsKey("receivedMoney") && !StringUtil.isEmpty(reqJson.getString("receivedMoney"))) {
                    dedAmount = new BigDecimal(reqJson.getString("receivedMoney"));
                } else {
                    dedAmount = new BigDecimal(reqJson.getString("deductionAmount"));
                }
                int flag = amount.compareTo(dedAmount);
                BigDecimal redepositAmount = new BigDecimal("0.00");
                BigDecimal integralAmount = new BigDecimal("0.00");
                if (flag == 1) { //现金账户大于应收金额，就用应收金额抵扣
                    redepositAmount = dedAmount;
                    integralAmount = amount.subtract(dedAmount);
                }
                if (flag > -1) { //现金账户大于等于应收金额，就用应收金额抵扣
                    redepositAmount = dedAmount;
                    integralAmount = amount.subtract(dedAmount);
                }
                if (flag == -1) { //现金账户小于实收金额，就用现金账户抵扣
                    redepositAmount = amount;
                }
                if (flag < 1) { //现金账户小于等于应收金额，就用现金账户抵扣
                    redepositAmount = amount;
                }
                if (flag == 0) { //现金账户等于应收金额
                    redepositAmount = amount;
                }
                //更新账户信息
//                AccountPo accountPo = new AccountPo();
//                accountPo.setAcctId(tmpAccountDto.getAcctId());
//                accountPo.setAmount(integralAmount.toString());
//                accountInnerServiceSMOImpl.updateAccount(accountPo);
                //生成账户详情
//                AccountDetailPo accountDetailPo = new AccountDetailPo();
//                accountDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
//                accountDetailPo.setAcctId(tmpAccountDto.getAcctId());
//                accountDetailPo.setDetailType("2002"); //1001 转入 2002 转出
//                accountDetailPo.setRelAcctId("-1");
//                accountDetailPo.setAmount(redepositAmount.toString());
//                accountDetailPo.setObjType("6006"); //6006 个人 7007 商户
//                accountDetailPo.setObjId(tmpAccountDto.getObjId());
//                accountDetailPo.setOrderId("-1");
//                accountDetailPo.setbId("-1");
//                accountDetailPo.setRemark("手机端现金账户抵扣");
//                accountDetailPo.setCreateTime(new Date());
//                accountDetailInnerServiceSMOImpl.saveAccountDetails(accountDetailPo);
                //生成抵扣明细记录
//                FeeAccountDetailPo feeAccountDetailPo = new FeeAccountDetailPo();
//                feeAccountDetailPo.setFadId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_fadId));
//                feeAccountDetailPo.setDetailId(accountDetailPo.getDetailId());
//                feeAccountDetailPo.setCommunityId(reqJson.getString("communityId"));
//                feeAccountDetailPo.setState("1002"); //1001 无抵扣 1002 现金账户抵扣 1003 积分账户抵扣 1004 优惠券抵扣
//                feeAccountDetailPo.setAmount(redepositAmount.toString()); //积分抵扣金额
//                feeAccountDetailServiceSMOImpl.saveFeeAccountDetail(feeAccountDetailPo);
                money = money.add(redepositAmount);
                /*int flag2 = money.compareTo(amount);
                if (flag2 == 1) { //剩余金额大于现金账户
                    money = money.subtract(amount);
                }
                if (flag2 > -1) { //剩余金额大于等于现金账户
                    money = money.subtract(amount);
                }
                if (flag2 == -1) { //剩余金额小于现金账户
                    money = new BigDecimal(0);
                }
                if (flag2 < 1) { //剩余金额小于等于现金账户
                    money = new BigDecimal(0);
                }
                if (flag2 == 0) { //剩余金额等于现金账户
                    money = new BigDecimal(0);
                }*/
            }
//            totalAccountAmount = totalAccountAmount.add(new BigDecimal(tmpAccountDto.getAmount()));
        }

       /* double tmpDeductionAmount = totalAccountAmount.subtract(new BigDecimal(deductionAmount)).doubleValue();
        if (tmpDeductionAmount < 0) {
            reqJson.put("deductionAmount", totalAccountAmount.doubleValue());
            reqJson.put("selectUserAccount", BeanConvertUtil.beanCovertJSONArray(accountDtos));
            return totalAccountAmount.doubleValue();
        }
        reqJson.put("deductionAmount", deductionAmount);
        reqJson.put("selectUserAccount", BeanConvertUtil.beanCovertJSONArray(accountDtos));
        return deductionAmount;*/
        reqJson.put("deductionAmount", money.doubleValue());
        reqJson.put("selectUserAccount", BeanConvertUtil.beanCovertJSONArray(accountDtos));
        return money.doubleValue();
    }

    private double checkCouponUser(JSONObject paramObj) {
        JSONArray couponList = paramObj.getJSONArray("couponList");
        BigDecimal couponPrice = new BigDecimal(0.0);
        List<String> couponIds = new ArrayList<String>();

        if (couponList == null || couponList.size() < 1) {
            paramObj.put("couponPrice", couponPrice.doubleValue());
            paramObj.put("couponUserDtos", new JSONArray()); //这里考虑空
            return couponPrice.doubleValue();
        }
        for (int couponIndex = 0; couponIndex < couponList.size(); couponIndex++) {
            couponIds.add(couponList.getJSONObject(couponIndex).getString("couponId"));
        }
        CouponUserDto couponUserDto = new CouponUserDto();
        couponUserDto.setCouponIds(couponIds.toArray(new String[couponIds.size()]));
        List<CouponUserDto> couponUserDtos = couponUserV1InnerServiceSMOImpl.queryCouponUsers(couponUserDto);
        if (couponUserDtos == null || couponUserDtos.size() < 1) {
            paramObj.put("couponPrice", couponPrice.doubleValue());
            return couponPrice.doubleValue();
        }
        for (CouponUserDto couponUser : couponUserDtos) {
            //不计算已过期购物券金额
            if (couponUser.getEndTime().compareTo(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_B)) >= 0) {
                couponPrice = couponPrice.add(new BigDecimal(Double.parseDouble(couponUser.getActualPrice())));
            }
        }
        paramObj.put("couponPrice", couponPrice.doubleValue());
        paramObj.put("couponUserDtos", BeanConvertUtil.beanCovertJSONArray(couponUserDtos));
        return couponPrice.doubleValue();
    }


    private double judgeDiscount(JSONObject paramObj) {
        FeeDetailDto feeDetailDto = new FeeDetailDto();
        feeDetailDto.setCommunityId(paramObj.getString("communityId"));
        feeDetailDto.setFeeId(paramObj.getString("feeId"));
        feeDetailDto.setCycles(paramObj.getString("cycles"));
        feeDetailDto.setPayerObjId(paramObj.getString("payerObjId"));
        feeDetailDto.setPayerObjType(paramObj.getString("payerObjType"));
        String endTime = paramObj.getString("endTime");  //获取缴费到期时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            feeDetailDto.setStartTime(simpleDateFormat.parse(endTime));
        } catch (ParseException e) {
            throw new CmdException(e.getLocalizedMessage());
        }

        feeDetailDto.setRow(20);
        feeDetailDto.setPage(1);
        List<ComputeDiscountDto> computeDiscountDtos = feeDiscountInnerServiceSMOImpl.computeDiscount(feeDetailDto);

        if (computeDiscountDtos == null || computeDiscountDtos.size() < 1) {
            paramObj.put("discountPrice", 0.0);
            return 0.0;
        }
        BigDecimal discountPrice = new BigDecimal(0);
        for (ComputeDiscountDto computeDiscountDto : computeDiscountDtos) {
            discountPrice = discountPrice.add(new BigDecimal(computeDiscountDto.getDiscountPrice()));
        }
        paramObj.put("discountPrice", discountPrice.doubleValue());
        paramObj.put("computeDiscountDtos", BeanConvertUtil.beanCovertJSONArray(computeDiscountDtos));
        return discountPrice.doubleValue();
    }
}
