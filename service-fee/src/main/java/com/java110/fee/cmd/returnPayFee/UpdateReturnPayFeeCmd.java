/*
 * Copyright 2017-2020 吴学文 and java110 team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.java110.fee.cmd.returnPayFee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.RoomDto;
import com.java110.dto.account.AccountDto;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.fee.FeeDiscountDto;
import com.java110.dto.fee.FeeDiscountRuleDto;
import com.java110.dto.fee.FeeDiscountSpecDto;
import com.java110.dto.fee.FeeReceiptDetailDto;
import com.java110.dto.onlinePay.OnlinePayDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.payFeeConfigDiscount.PayFeeConfigDiscountDto;
import com.java110.dto.payFeeDetailDiscount.PayFeeDetailDiscountDto;
import com.java110.dto.returnPayFee.ReturnPayFeeDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.acct.IAccountDetailInnerServiceSMO;
import com.java110.intf.acct.IAccountInnerServiceSMO;
import com.java110.intf.acct.IOnlinePayV1InnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.*;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.account.AccountPo;
import com.java110.po.accountDetail.AccountDetailPo;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.feeReceipt.FeeReceiptPo;
import com.java110.po.feeReceiptDetail.FeeReceiptDetailPo;
import com.java110.po.onlinePay.OnlinePayPo;
import com.java110.po.payFeeDetailDiscount.PayFeeDetailDiscountPo;
import com.java110.po.returnPayFee.ReturnPayFeePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 类表述：退费审核接口
 * 服务编码：returnPayFee.updateReturnPayFee
 * 请求路劲：/app/returnPayFee.UpdateReturnPayFee
 * add by 吴学文 at 2022-02-21 12:20:03 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "returnPayFee.updateReturnPayFee")
public class UpdateReturnPayFeeCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(UpdateReturnPayFeeCmd.class);

    @Autowired
    private IReturnPayFeeV1InnerServiceSMO returnPayFeeV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeConfigDiscountInnerServiceSMO payFeeConfigDiscountInnerServiceSMOImpl;

    @Autowired
    private IFeeDiscountInnerServiceSMO feeDiscountInnerServiceSMOImpl;

    @Autowired
    private IFeeDiscountRuleInnerServiceSMO feeDiscountRuleInnerServiceSMOImpl;

    @Autowired
    private IFeeDiscountSpecInnerServiceSMO feeDiscountSpecInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

    @Autowired
    private IPayFeeDetailV1InnerServiceSMO payFeeDetailV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeDetailDiscountInnerServiceSMO payFeeDetailDiscountInnerServiceSMOImpl;

    @Autowired
    private IReturnPayFeeInnerServiceSMO returnPayFeeInnerServiceSMOImpl;

    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeDetailDiscountNewV1InnerServiceSMO payFeeDetailDiscountNewV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IAccountInnerServiceSMO accountInnerServiceSMOImpl;

    @Autowired
    private IAccountDetailInnerServiceSMO accountDetailInnerServiceSMOImpl;

    @Autowired
    private IOnlinePayV1InnerServiceSMO onlinePayV1InnerServiceSMOImpl;

    @Autowired
    private IFeeReceiptInnerServiceSMO feeReceiptInnerServiceSMOImpl;

    @Autowired
    private IFeeReceiptDetailInnerServiceSMO feeReceiptDetailInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;


    private static final String SPEC_RATE = "89002020980015"; //赠送月份

    private static final String SPEC_MONTH = "89002020980014"; //月份

    public static final String CODE_PREFIX_ID = "10";

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "returnFeeId", "returnFeeId不能为空");
        Assert.hasKeyAndValue(reqJson, "state", "state不能为空");
        Assert.hasKeyAndValue(reqJson, "feeId", "feeId不能为空");
        if (reqJson.containsKey("cycles")) {
            String cycles = reqJson.getString("cycles");
            if (!cycles.startsWith("-")) {
                throw new IllegalArgumentException("退费周期必须负数");// 这里必须传入负数，否则费用自动相加不会退费
            }
        }
        FeeDetailDto feeDetailDto = new FeeDetailDto();
        feeDetailDto.setDetailId(reqJson.getString("detailId"));
        feeDetailDto.setFeeId(reqJson.getString("feeId"));
        feeDetailDto.setCommunityId(reqJson.getString("communityId"));
        List<FeeDetailDto> feeDetailDtos = feeDetailInnerServiceSMOImpl.queryFeeDetails(feeDetailDto);
        Assert.listOnlyOne(feeDetailDtos, "不存在缴费记录");
        reqJson.put("feeDetailDto", feeDetailDtos.get(0));
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        String userId = cmdDataFlowContext.getReqHeaders().get("user-id");

        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户不存在");
        FeeDetailDto feeDetailDto = (FeeDetailDto) reqJson.get("feeDetailDto");

        ReturnPayFeeDto returnPayFeeDto = new ReturnPayFeeDto();
        returnPayFeeDto.setReturnFeeId(reqJson.getString("returnFeeId"));
        List<ReturnPayFeeDto> returnPayFeeDtos = returnPayFeeInnerServiceSMOImpl.queryReturnPayFees(returnPayFeeDto);
        Assert.listOnlyOne(returnPayFeeDtos, "未找到需要修改的活动 或多条数据");

        // todo 修改退款状态
        updateReturnPayFee(reqJson, userDtos.get(0), returnPayFeeDtos.get(0));

        //退费审核通过
        if ("1100".equals(reqJson.getString("state"))) {
            //判断退费周期是否为负数如果不是 抛出异常
            String cycles = reqJson.getString("cycles");
            reqJson.put("state", "1300");
            reqJson.put("startTime", DateUtil.getFormatTimeString(feeDetailDto.getStartTime(), DateUtil.DATE_FORMATE_STRING_A));
            reqJson.put("endTime", DateUtil.getFormatTimeString(feeDetailDto.getEndTime(), DateUtil.DATE_FORMATE_STRING_A));
            reqJson.put("payOrderId", feeDetailDto.getPayOrderId());
            // todo  添加退费明细
            addFeeDetail(reqJson, returnPayFeeDtos.get(0));
            reqJson.put("state", "1100");
            String receivableAmount = (String) reqJson.get("receivableAmount");
            String receivedAmount = (String) reqJson.get("receivedAmount");
            reqJson.put("cycles", unum(cycles));
            reqJson.put("receivableAmount", unum(receivableAmount));
            reqJson.put("receivedAmount", unum(receivedAmount));
            reqJson.put("createTime", reqJson.get("payTime"));
            // todo 修改 缴费记录
            updateFeeDetail(reqJson);
            //修改pay_fee 费用到期时间  以及如果是押金则修改状态为结束收费
            FeeDto feeDto = new FeeDto();
            feeDto.setFeeId((String) reqJson.get("feeId"));
            List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
            Assert.listOnlyOne(feeDtos, "费用不存在");
            FeeDto feeDto1 = feeDtos.get(0);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            reqJson.put("endTime", DateUtil.getFormatTimeString(feeDetailDto.getStartTime(), DateUtil.DATE_FORMATE_STRING_A));
            reqJson.put("amount", feeDto1.getAmount());
            reqJson.put("feeTypeCd", feeDto1.getFeeTypeCd());
            reqJson.put("communityId", feeDto1.getCommunityId());
            reqJson.put("payerObjId", feeDto1.getPayerObjId());
            reqJson.put("incomeObjId", feeDto1.getIncomeObjId());
            reqJson.put("startTime", sdf.format(feeDto1.getStartTime()));
            reqJson.put("userId", feeDto1.getUserId());
            reqJson.put("feeFlag", feeDto1.getFeeFlag());
            reqJson.put("statusCd", feeDto1.getStatusCd());
            reqJson.put("state", feeDto1.getState());
            reqJson.put("configId", feeDto1.getConfigId());
            reqJson.put("payerObjType", feeDto1.getPayerObjType());
            reqJson.put("feeId", feeDto1.getFeeId());
            if ("888800010006".equals(feeDto1.getFeeTypeCds())) {
                reqJson.put("state", "2009001");
            } else {
                reqJson.put("state", "2008001");
            }
            //todo 费用退回去
            updateFee(reqJson);
            reqJson.put("feeName", feeDto1.getFeeName());
//            dealFeeReceipt(reqJson);
            //检查是否有优惠
            PayFeeDetailDiscountDto payFeeDetailDiscountDto = new PayFeeDetailDiscountDto();
            payFeeDetailDiscountDto.setCommunityId(feeDto1.getCommunityId());
            payFeeDetailDiscountDto.setDetailId(reqJson.getString("detailId"));
            List<PayFeeDetailDiscountDto> payFeeDetailDiscountDtos = payFeeDetailDiscountInnerServiceSMOImpl.queryPayFeeDetailDiscounts(payFeeDetailDiscountDto);
            if (payFeeDetailDiscountDtos != null && payFeeDetailDiscountDtos.size() > 0) {
                JSONObject discountJson = new JSONObject();
                discountJson.put("discountId", payFeeDetailDiscountDtos.get(0).getDiscountId());
                discountJson.put("discountPrice", unum(payFeeDetailDiscountDtos.get(0).getDiscountPrice()));
                addPayFeeDetailDiscountTwo(reqJson, discountJson);
            }
            //todo 判读是否有赠送规则优惠
            returnCoupon(reqJson, feeDtos);
            //todo 检查是否现金账户抵扣
            returnAccount(reqJson);
            //todo 提交线上退费
            returnOnlinePayMoney(feeDetailDto);
        }
        //不通过
        if ("1200".equals(reqJson.getString("state"))) {
            reqJson.put("state", "1200");
            updateFeeDetail(reqJson);
            reqJson.put("state", "1200");
            String cycles = (String) reqJson.get("cycles");
            String receivableAmount = (String) reqJson.get("receivableAmount");
            String receivedAmount = (String) reqJson.get("receivedAmount");
            reqJson.put("cycles", unum(cycles));
            reqJson.put("receivableAmount", unum(receivableAmount));
            reqJson.put("receivedAmount", unum(receivedAmount));
            reqJson.put("createTime", reqJson.get("payTime"));
            updateFeeDetail(reqJson);
        }
    }

    private void returnAccount(JSONObject reqJson) {
        String feeAccountDetailDtoList = reqJson.getString("feeAccountDetailDtoList");
        JSONArray feeAccountDetails = JSONArray.parseArray(feeAccountDetailDtoList);
        if (feeAccountDetails == null || feeAccountDetails.size() < 1) {
            return;
        }
        String ownerId = "";
        if (FeeDto.PAYER_OBJ_TYPE_ROOM.equals(reqJson.getString("payerObjType"))) { //房屋
            OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
            ownerRoomRelDto.setRoomId(reqJson.getString("payerObjId"));
            List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);
            Assert.listOnlyOne(ownerRoomRelDtos, "查询业主房屋关系表错误！");
            ownerId = ownerRoomRelDtos.get(0).getOwnerId();
        } else if (FeeDto.PAYER_OBJ_TYPE_CAR.equals(reqJson.getString("payerObjType"))) { //车辆
            OwnerCarDto ownerCarDto = new OwnerCarDto();
            ownerCarDto.setMemberId(reqJson.getString("payerObjId"));
            List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
            Assert.listOnlyOne(ownerCarDtos, "查询业主车辆错误！");
            ownerId = ownerCarDtos.get(0).getOwnerId();
        }
        for (int index = 0; index < feeAccountDetails.size(); index++) {
            JSONObject param = feeAccountDetails.getJSONObject(index);
            String state = param.getString("state");
            if (!"1002".equals(param.getString("state"))) { //1001 无抵扣 1002 现金账户抵扣 1003 积分账户抵扣 1004 优惠券抵扣
                continue;
            }
            AccountDto accountDto = new AccountDto();
            accountDto.setObjId(ownerId);
            accountDto.setAcctType(AccountDto.ACCT_TYPE_CASH); //2003  现金账户
            List<AccountDto> accountDtos = accountInnerServiceSMOImpl.queryAccounts(accountDto);
            Assert.listOnlyOne(accountDtos, "查询业主现金账户错误！");
            BigDecimal amount = new BigDecimal(accountDtos.get(0).getAmount());
            BigDecimal money = new BigDecimal(param.getString("amount"));
            BigDecimal newAmount = amount.add(money);
            AccountPo accountPo = new AccountPo();
            accountPo.setAcctId(accountDtos.get(0).getAcctId());
            accountPo.setAmount(String.valueOf(newAmount));
            int flag = accountInnerServiceSMOImpl.updateAccount(accountPo);
            if (flag < 1) {
                throw new IllegalArgumentException("更新业主现金账户失败！");
            }
            AccountDetailPo accountDetailPo = new AccountDetailPo();
            accountDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
            accountDetailPo.setAcctId(accountDtos.get(0).getAcctId());
            accountDetailPo.setDetailType("1001"); //1001 转入 2002 转出
            accountDetailPo.setRelAcctId("-1");
            accountDetailPo.setAmount(param.getString("amount"));
            accountDetailPo.setObjType("6006"); //6006 个人 7007 商户
            accountDetailPo.setObjId(ownerId);
            accountDetailPo.setOrderId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_orderId));
            accountDetailPo.setbId("-1");
            accountDetailPo.setRemark("现金账户退费");
            accountDetailPo.setCreateTime(new Date());
            int i = accountDetailInnerServiceSMOImpl.saveAccountDetails(accountDetailPo);
            if (i < 1) {
                throw new IllegalArgumentException("保存业主现金账户明细失败！");
            }
        }
    }

    private void returnCoupon(JSONObject reqJson, List<FeeDto> feeDtos) {
        PayFeeConfigDiscountDto payFeeConfigDiscountDto = new PayFeeConfigDiscountDto();
        payFeeConfigDiscountDto.setConfigId(reqJson.getString("configId"));
        List<PayFeeConfigDiscountDto> payFeeConfigDiscountDtos = payFeeConfigDiscountInnerServiceSMOImpl.queryPayFeeConfigDiscounts(payFeeConfigDiscountDto);
        if (payFeeConfigDiscountDtos == null || payFeeConfigDiscountDtos.size() < 1) {
            return;
        }
        for (PayFeeConfigDiscountDto payFeeConfigDiscount : payFeeConfigDiscountDtos) {
            FeeDiscountDto feeDiscountDto = new FeeDiscountDto();
            feeDiscountDto.setDiscountId(payFeeConfigDiscount.getDiscountId());
            List<FeeDiscountDto> feeDiscountDtos = feeDiscountInnerServiceSMOImpl.queryFeeDiscounts(feeDiscountDto);
            Assert.listOnlyOne(feeDiscountDtos, "查询打折优惠表错误");
            FeeDiscountRuleDto feeDiscountRuleDto = new FeeDiscountRuleDto();
            feeDiscountRuleDto.setRuleId(feeDiscountDtos.get(0).getRuleId());
            List<FeeDiscountRuleDto> feeDiscountRuleDtos = feeDiscountRuleInnerServiceSMOImpl.queryFeeDiscountRules(feeDiscountRuleDto);
            Assert.listOnlyOne(feeDiscountRuleDtos, "查询规则表错误");
            //获取实现方式
            String beanImpl = feeDiscountRuleDtos.get(0).getBeanImpl();
            if (!"reductionMonthFeeRule".equals(beanImpl)) { //赠送规则
                continue;
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            //获取缴费开始时间
            Date startTime = DateUtil.getDateFromStringA(reqJson.getString("startTime"));
            FeeDiscountSpecDto feeDiscountSpecDto = new FeeDiscountSpecDto();
            feeDiscountSpecDto.setDiscountId(payFeeConfigDiscount.getDiscountId());
            feeDiscountSpecDto.setSpecId(SPEC_RATE); //赠送规则
            //查询打折规格
            List<FeeDiscountSpecDto> feeDiscountSpecDtos = feeDiscountSpecInnerServiceSMOImpl.queryFeeDiscountSpecs(feeDiscountSpecDto);
            Assert.listOnlyOne(feeDiscountSpecDtos, "查询打折规格表错误！");
            //获取赠送月份
            String specValue = feeDiscountSpecDtos.get(0).getSpecValue();
            BigDecimal value = new BigDecimal(specValue);
            FeeDiscountSpecDto feeDiscountSpec = new FeeDiscountSpecDto();
            feeDiscountSpec.setDiscountId(payFeeConfigDiscount.getDiscountId());
            feeDiscountSpec.setSpecId(SPEC_MONTH); //月份
            List<FeeDiscountSpecDto> feeDiscountSpecs = feeDiscountSpecInnerServiceSMOImpl.queryFeeDiscountSpecs(feeDiscountSpec);
            Assert.listOnlyOne(feeDiscountSpecs, "查询打折规格表错误！");
            //获取月份
            BigDecimal discountMonth = new BigDecimal(feeDiscountSpecs.get(0).getSpecValue());
            //获取周期
            BigDecimal cycle = new BigDecimal(reqJson.getString("cycles"));
            int flag = discountMonth.compareTo(cycle);
            if (flag == 1) { //月份discountMonth大于周期cycle，无法享受赠送规则
                continue;
            }

            int monthNum = cycle.add(value).intValue();
            //获取费用开始时间
            Date endTime = feeDtos.get(0).getEndTime();

            cal.setTime(endTime);
            cal.add(Calendar.MONTH, -monthNum);
            PayFeePo payFeePo = new PayFeePo();
            payFeePo.setFeeId(feeDtos.get(0).getFeeId());
            payFeePo.setEndTime(simpleDateFormat.format(cal.getTime()));
            feeInnerServiceSMOImpl.updateFee(payFeePo);

        }

    }

    private double unum(String value) {
        double dValue = Double.parseDouble(value);
        return dValue * -1;
    }

    /**
     * 添加活动信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void updateReturnPayFee(JSONObject paramInJson, UserDto userDto, ReturnPayFeeDto returnPayFeeDto) {

        JSONObject businessReturnPayFee = new JSONObject();
        businessReturnPayFee.putAll(BeanConvertUtil.beanCovertMap(returnPayFeeDto));
        businessReturnPayFee.putAll(paramInJson);
        ReturnPayFeePo returnPayFeePo = BeanConvertUtil.covertBean(businessReturnPayFee, ReturnPayFeePo.class);
        returnPayFeePo.setAuditPersonId(userDto.getUserId());
        returnPayFeePo.setAuditPersonName(userDto.getName());
        int flag = returnPayFeeV1InnerServiceSMOImpl.updateReturnPayFee(returnPayFeePo);
        if (flag < 1) {
            throw new CmdException("更新数据失败");
        }
    }

    public void updateFeeDetail(JSONObject paramInJson) {
        FeeDetailDto feeDetailDto = new FeeDetailDto();
        feeDetailDto.setDetailId(paramInJson.getString("detailId"));
        List<FeeDetailDto> feeDetailDtos = feeDetailInnerServiceSMOImpl.queryFeeDetails(feeDetailDto);
        Assert.listOnlyOne(feeDetailDtos, "未找到需要修改的活动 或多条数据");
        JSONObject businessReturnPayFee = new JSONObject();
        businessReturnPayFee.putAll(BeanConvertUtil.beanCovertMap(feeDetailDtos.get(0)));
        businessReturnPayFee.putAll(paramInJson);
        PayFeeDetailPo returnPayFeePo = BeanConvertUtil.covertBean(businessReturnPayFee, PayFeeDetailPo.class);
        int flag = payFeeDetailV1InnerServiceSMOImpl.updatePayFeeDetailNew(returnPayFeePo);
        if (flag < 1) {
            throw new CmdException("更新数据失败");
        }

        // todo 将收据删除
        FeeReceiptDetailDto feeReceiptDetailDto = new FeeReceiptDetailDto();
        feeReceiptDetailDto.setDetailId(returnPayFeePo.getDetailId());
        feeReceiptDetailDto.setCommunityId(returnPayFeePo.getCommunityId());
        int count = feeReceiptDetailInnerServiceSMOImpl.queryFeeReceiptDetailsCount(feeReceiptDetailDto);
        if (count != 1) {
            return;
        }
        FeeReceiptDetailPo feeReceiptDetailPo = new FeeReceiptDetailPo();
        feeReceiptDetailPo.setDetailId(returnPayFeePo.getDetailId());
        feeReceiptDetailPo.setCommunityId(returnPayFeePo.getCommunityId());
        feeReceiptDetailInnerServiceSMOImpl.deleteFeeReceiptDetail(feeReceiptDetailPo);
    }

    /**
     * 添加退费单
     *
     * @param paramInJson
     * @param returnPayFeeDto
     */
    public void addFeeDetail(JSONObject paramInJson, ReturnPayFeeDto returnPayFeeDto) {
        JSONObject businessReturnPayFee = new JSONObject();
        businessReturnPayFee.putAll(paramInJson);
        businessReturnPayFee.put("detailId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        PayFeeDetailPo returnPayFeePo = BeanConvertUtil.covertBean(businessReturnPayFee, PayFeeDetailPo.class);
        returnPayFeePo.setCashierId(returnPayFeeDto.getApplyPersonId());
        returnPayFeePo.setCashierName(returnPayFeeDto.getApplyPersonName());
        int flag = payFeeDetailV1InnerServiceSMOImpl.savePayFeeDetailNew(returnPayFeePo);
        if (flag < 1) {
            throw new CmdException("更新数据失败");
        }
        paramInJson.put("newDetailId", businessReturnPayFee.getString("detailId"));
    }

    public void updateFee(JSONObject paramInJson) {
        PayFeePo payFeePo = BeanConvertUtil.covertBean(paramInJson, PayFeePo.class);
        int flag = payFeeV1InnerServiceSMOImpl.updatePayFee(payFeePo);
        if (flag < 1) {
            throw new CmdException("更新数据失败");
        }
    }

    //收据相关操作
    public void dealFeeReceipt(JSONObject paramInJson) {
        //添加收据
        FeeReceiptPo feeReceiptPo = new FeeReceiptPo();
        feeReceiptPo.setReceiptId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_receiptId));
        feeReceiptPo.setCommunityId(paramInJson.getString("communityId"));
        feeReceiptPo.setObjType(paramInJson.getString("payerObjType")); //收据对象 3333 房屋 6666 车位车辆
        feeReceiptPo.setObjId(paramInJson.getString("payerObjId")); //对象ID
        feeReceiptPo.setObjName(paramInJson.getString("payerObjName")); //对象名称
        double receivedAmount = unum(paramInJson.getString("receivedAmount"));
        feeReceiptPo.setAmount(String.valueOf(receivedAmount)); //总金额
        feeReceiptPo.setRemark("退费收据");
        String payObjId = "";
        String payObjName = "";
        String roomArea = "";
        if (!StringUtil.isEmpty(paramInJson.getString("payerObjType")) && paramInJson.getString("payerObjType").equals("3333")) { //房屋
            OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
            ownerRoomRelDto.setRoomId(paramInJson.getString("payerObjId"));
            List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);
            Assert.listOnlyOne(ownerRoomRelDtos, "查询房屋错误！");
            OwnerDto ownerDto = new OwnerDto();
            ownerDto.setOwnerId(ownerRoomRelDtos.get(0).getOwnerId());
            ownerDto.setOwnerTypeCd("1001"); //1001 业主本人 1002 家庭成员
            List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);
            Assert.listOnlyOne(ownerDtos, "查询业主信息错误！");
            payObjId = ownerDtos.get(0).getOwnerId();
            payObjName = ownerDtos.get(0).getName();
        } else if (!StringUtil.isEmpty(paramInJson.getString("payerObjType")) && paramInJson.getString("payerObjType").equals("6666")) { //车辆
            OwnerCarDto ownerCarDto = new OwnerCarDto();
            ownerCarDto.setCarId(paramInJson.getString("payerObjId"));
            List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
            Assert.listOnlyOne(ownerCarDtos, "查询业主车辆错误！");
            OwnerDto ownerDto = new OwnerDto();
            ownerDto.setOwnerId(ownerCarDtos.get(0).getOwnerId());
            ownerDto.setOwnerTypeCd("1001"); //1001 业主本人 1002 家庭成员
            List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);
            Assert.listOnlyOne(ownerDtos, "查询业主信息错误！");
            payObjId = ownerDtos.get(0).getOwnerId();
            payObjName = ownerDtos.get(0).getName();
        }
        feeReceiptPo.setPayObjId(payObjId); //付费人id
        feeReceiptPo.setPayObjName(payObjName); //付费人姓名
        int i = feeReceiptInnerServiceSMOImpl.saveFeeReceipt(feeReceiptPo);
        if (i < 1) {
            throw new CmdException("添加收据失败");
        }
        //添加收据详情
        FeeReceiptDetailPo feeReceiptDetailPo = new FeeReceiptDetailPo();
        feeReceiptDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        feeReceiptDetailPo.setReceiptId(feeReceiptPo.getReceiptId()); //收据id
        feeReceiptDetailPo.setFeeId(paramInJson.getString("feeId")); //费用id
        feeReceiptDetailPo.setFeeName(paramInJson.getString("feeName"));
        if (!StringUtil.isEmpty(paramInJson.getString("payerObjType")) && paramInJson.getString("payerObjType").equals("3333")) { //房屋
            RoomDto roomDto = new RoomDto();
            roomDto.setRoomId(paramInJson.getString("payerObjId"));
            List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
            Assert.listOnlyOne(roomDtos, "查询房屋错误！");
            roomArea = roomDtos.get(0).getRoomArea();
        }
        feeReceiptDetailPo.setArea(roomArea); //面积/用量
        feeReceiptDetailPo.setStartTime(paramInJson.getString("startTime"));
        feeReceiptDetailPo.setEndTime(paramInJson.getString("endTime"));
        feeReceiptDetailPo.setAmount(feeReceiptPo.getAmount());
        feeReceiptDetailPo.setCycle(paramInJson.getString("cycles"));
        feeReceiptDetailPo.setCommunityId(paramInJson.getString("communityId"));
        int flag = feeReceiptDetailInnerServiceSMOImpl.saveFeeReceiptDetail(feeReceiptDetailPo);
        if (flag < 1) {
            throw new CmdException("添加收据详情失败");
        }
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void addPayFeeDetailDiscountTwo(JSONObject paramInJson, JSONObject discountJson) {
        JSONObject businessFee = new JSONObject();
        businessFee.put("detailDiscountId", GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        businessFee.put("discountPrice", discountJson.getString("discountPrice"));
        businessFee.put("discountId", discountJson.getString("discountId"));
        businessFee.put("detailId", paramInJson.containsKey("newDetailId") ? paramInJson.getString("newDetailId") : paramInJson.getString("detailId"));
        businessFee.put("communityId", paramInJson.getString("communityId"));
        businessFee.put("feeId", paramInJson.getString("feeId"));
        PayFeeDetailDiscountPo payFeeDetailDiscountPo = BeanConvertUtil.covertBean(businessFee, PayFeeDetailDiscountPo.class);
        int flag = payFeeDetailDiscountNewV1InnerServiceSMOImpl.savePayFeeDetailDiscountNew(payFeeDetailDiscountPo);
        if (flag < 1) {
            throw new CmdException("更新数据失败");
        }
    }

    /**
     * 发起退款
     * 这里发起退款后 通过databus 触发 ReturnPayFeeMoneyAdapt 这个类 退款
     *
     * @param feeDetailDto
     */
    private void returnOnlinePayMoney(FeeDetailDto feeDetailDto) {
        if (StringUtil.isEmpty(feeDetailDto.getPayOrderId())) {
            return;
        }
        OnlinePayDto onlinePayDto = new OnlinePayDto();
        onlinePayDto.setOrderId(feeDetailDto.getPayOrderId());
        List<OnlinePayDto> onlinePayDtos = onlinePayV1InnerServiceSMOImpl.queryOnlinePays(onlinePayDto);
        if (onlinePayDtos == null || onlinePayDtos.size() < 1) {
            return;
        }
        OnlinePayPo onlinePayPo = new OnlinePayPo();
        onlinePayPo.setOrderId(onlinePayDtos.get(0).getOrderId());
        onlinePayPo.setPayId(onlinePayDtos.get(0).getPayId());
        onlinePayPo.setState(OnlinePayDto.STATE_WT);
        onlinePayPo.setRefundFee(feeDetailDto.getReceivedAmount());
        onlinePayV1InnerServiceSMOImpl.updateOnlinePay(onlinePayPo);
    }
}
