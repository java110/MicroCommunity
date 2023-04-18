package com.java110.job.adapt.payment.integral;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.RoomDto;
import com.java110.dto.account.AccountDto;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.integral.IntegralRuleConfigDto;
import com.java110.dto.integral.IntegralRuleFeeDto;
import com.java110.dto.logSystemError.LogSystemErrorDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.entity.order.Business;
import com.java110.intf.acct.*;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.community.IRoomV1InnerServiceSMO;
import com.java110.intf.fee.IFeeAttrInnerServiceSMO;
import com.java110.intf.fee.IFeeDetailInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.po.account.AccountPo;
import com.java110.po.accountDetail.AccountDetailPo;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.integralGiftDetail.IntegralGiftDetailPo;
import com.java110.po.logSystemError.LogSystemErrorPo;
import com.java110.service.smo.ISaveSystemErrorSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.lock.DistributedLock;
import com.java110.utils.util.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 缴费 赠送 积分
 *
 * @author fqz
 * @date 2020-12-11  18:54
 */
@Component(value = "payFeeGiftIntegralAdapt")
public class PayFeeGiftIntegralAdapt extends DatabusAdaptImpl {

    private static Logger logger = LoggerFactory.getLogger(PayFeeGiftIntegralAdapt.class);

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMO;

    @Autowired
    private ISaveSystemErrorSMO saveSystemErrorSMOImpl;

    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;


    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

    @Autowired
    private IIntegralRuleFeeV1InnerServiceSMO integralRuleFeeV1InnerServiceSMOImpl;

    @Autowired
    private IIntegralRuleConfigV1InnerServiceSMO integralRuleConfigV1InnerServiceSMOImpl;

    @Autowired
    private IAccountInnerServiceSMO accountInnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerInnerServiceSMOImpl;


    @Autowired
    private IIntegralGiftDetailV1InnerServiceSMO integralGiftDetailV1InnerServiceSMOImpl;


    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    //模板信息推送地址
    private static String sendMsgUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

    @Override
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

            if(businessPayFeeDetail.containsKey("receivedAmount")
                    && businessPayFeeDetail.getDoubleValue("receivedAmount")<0){
                return ;
            }

            IntegralRuleFeeDto integralRuleFeeDto = new IntegralRuleFeeDto();
            integralRuleFeeDto.setFeeConfigId(feeDtos.get(0).getConfigId());
            integralRuleFeeDto.setCurTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            integralRuleFeeDto.setCommunityId(payFeeDetailPo.getCommunityId());
            integralRuleFeeDto.setCycle(payFeeDetailPo.getCycles());
            List<IntegralRuleFeeDto> integralRuleFeeDtos = integralRuleFeeV1InnerServiceSMOImpl.queryIntegralRuleFees(integralRuleFeeDto);

            if (integralRuleFeeDtos == null || integralRuleFeeDtos.size() < 1) {
                return;
            }

            List<String> ruleIds = new ArrayList<>();
            for (IntegralRuleFeeDto tmpCouponRuleFeeDto : integralRuleFeeDtos) {
                ruleIds.add(tmpCouponRuleFeeDto.getRuleId());
            }

            IntegralRuleConfigDto integralRuleConfigDto = new IntegralRuleConfigDto();
            integralRuleConfigDto.setRuleIds(ruleIds.toArray(new String[ruleIds.size()]));
            List<IntegralRuleConfigDto> integralRuleConfigDtos = integralRuleConfigV1InnerServiceSMOImpl.queryIntegralRuleConfigs(integralRuleConfigDto);

            if (integralRuleConfigDtos == null || integralRuleConfigDtos.size() < 1) {
                return;
            }

            // 计算赠送 积分公式
            long quantity = computeIntegralQuantity(integralRuleConfigDtos, feeDtos.get(0), payFeeDetailPo.getReceivedAmount());

            if (quantity <= 0) {
                return;
            }
            //赠送优惠券
            giftIntegral(integralRuleConfigDtos, feeDtos.get(0), payFeeDetailPo, quantity);
        } catch (Exception e) {
            LogSystemErrorPo logSystemErrorPo = new LogSystemErrorPo();
            logSystemErrorPo.setErrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_errId));
            logSystemErrorPo.setErrType(LogSystemErrorDto.ERR_TYPE_COUPON);
            logSystemErrorPo.setMsg(ExceptionUtil.getStackTrace(e));
            saveSystemErrorSMOImpl.saveLog(logSystemErrorPo);
            logger.error("通知异常", e);
        }
    }

    private long computeIntegralQuantity(List<IntegralRuleConfigDto> integralRuleConfigDtos, FeeDto feeDto, String receivedAmount) {
        JSONObject reqJson = new JSONObject();
        reqJson.put("amount", receivedAmount);
        reqJson.put("area", "1");
        if (FeeDto.PAYER_OBJ_TYPE_ROOM.equals(feeDto.getPayerObjType())) {
            RoomDto roomDto = new RoomDto();
            roomDto.setRoomId(feeDto.getPayerObjId());
            List<RoomDto> roomDtos = roomV1InnerServiceSMOImpl.queryRooms(roomDto);
            Assert.listOnlyOne(roomDtos, "房屋不存在");
            reqJson.put("area", roomDtos.get(0).getBuiltUpArea());
        }

        long quantity = 0;
        for (IntegralRuleConfigDto integralRuleConfigDto : integralRuleConfigDtos) {
            quantity += computeFeeSMOImpl.computeOneIntegralQuantity(integralRuleConfigDto, reqJson);
        }
        return quantity;
    }

    /**
     * 赠送积分
     *
     * @param integralRuleConfigDtos
     * @param feeDto
     * @param payFeeDetailPo
     */
    private void giftIntegral(List<IntegralRuleConfigDto> integralRuleConfigDtos, FeeDto feeDto, PayFeeDetailPo payFeeDetailPo, long quantity) {


        String ownerId = FeeAttrDto.getFeeAttrValue(feeDto, FeeAttrDto.SPEC_CD_OWNER_ID);

        if (StringUtil.isEmpty(ownerId)) {
            return;
        }

        //向积分账户中充值积分
        AccountDto accountDto = new AccountDto();
        accountDto.setObjId(ownerId);
        accountDto.setAcctType(AccountDto.ACCT_TYPE_INTEGRAL);
        List<AccountDto> accountDtos = accountInnerServiceSMOImpl.queryAccounts(accountDto);

        if (accountDtos == null || accountDtos.size() < 1) {
            accountDtos = addAccountDto(accountDto,payFeeDetailPo);
        }

        AccountDetailPo accountDetailPo = new AccountDetailPo();
        accountDetailPo.setAcctId(accountDtos.get(0).getAcctId());
        accountDetailPo.setObjId(accountDtos.get(0).getObjId());
        accountDetailPo.setObjType(accountDtos.get(0).getObjType());
        accountDetailPo.setAmount(quantity + "");
        int flag = accountInnerServiceSMOImpl.prestoreAccount(accountDetailPo);

        if (flag < 1) {
            throw new CmdException("扣款失败");
        }

        for (IntegralRuleConfigDto integralRuleConfigDto : integralRuleConfigDtos) {
            try {
                doGiftIntegral(integralRuleConfigDto, feeDto,accountDtos.get(0));
            } catch (Exception e) {
                LogSystemErrorPo logSystemErrorPo = new LogSystemErrorPo();
                logSystemErrorPo.setErrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_errId));
                logSystemErrorPo.setErrType(LogSystemErrorDto.ERR_TYPE_INTEGRAL);
                logSystemErrorPo.setMsg(ExceptionUtil.getStackTrace(e));
                saveSystemErrorSMOImpl.saveLog(logSystemErrorPo);
                logger.error("通知异常", e);
            }
        }

    }

    private List<AccountDto> addAccountDto(AccountDto accountDto, PayFeeDetailPo payFeeDetailPo) {
        if (StringUtil.isEmpty(accountDto.getObjId())) {
            return new ArrayList<>();
        }
        //开始锁代码
        String requestId = DistributedLock.getLockUUID();
        String key = this.getClass().getSimpleName() + "AddCountDto" + accountDto.getObjId();
        try {
            DistributedLock.waitGetDistributedLock(key, requestId);

            AccountPo accountPo = new AccountPo();
            accountPo.setAmount("0");
            accountPo.setAcctId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_acctId));
            accountPo.setObjId(accountDto.getObjId());
            accountPo.setObjType(AccountDto.OBJ_TYPE_PERSON);
            accountPo.setAcctType(AccountDto.ACCT_TYPE_INTEGRAL);
            OwnerDto tmpOwnerDto = new OwnerDto();
            tmpOwnerDto.setMemberId(accountDto.getObjId());
            tmpOwnerDto.setCommunityId(payFeeDetailPo.getCommunityId());
            List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(tmpOwnerDto);
            Assert.listOnlyOne(ownerDtos, "业主不存在");
            accountPo.setAcctName(ownerDtos.get(0).getName());
            accountPo.setPartId(ownerDtos.get(0).getCommunityId());
            accountPo.setLink(ownerDtos.get(0).getLink());
            accountInnerServiceSMOImpl.saveAccount(accountPo);
            List<AccountDto> accountDtos = accountInnerServiceSMOImpl.queryAccounts(accountDto);
            return accountDtos;
        } finally {
            DistributedLock.releaseDistributedLock(requestId, key);
        }
    }

    private void doGiftIntegral(IntegralRuleConfigDto integralRuleConfigDto, FeeDto feeDto,AccountDto accountDto) {

        //先加明细
        IntegralGiftDetailPo integralGiftDetailPo = new IntegralGiftDetailPo();
        integralGiftDetailPo.setCommunityId(integralRuleConfigDto.getCommunityId());
        integralGiftDetailPo.setAcctId(accountDto.getAcctId());
        integralGiftDetailPo.setAcctName(accountDto.getAcctName());
        integralGiftDetailPo.setAcctDetailId("-1");
        integralGiftDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId("11"));
        integralGiftDetailPo.setConfigId(integralRuleConfigDto.getConfigId());
        integralGiftDetailPo.setConfigName(integralRuleConfigDto.getConfigName());
        integralGiftDetailPo.setRuleId(integralRuleConfigDto.getRuleId());
        integralGiftDetailPo.setRuleName(integralRuleConfigDto.getRuleName());
        integralGiftDetailPo.setQuantity(integralRuleConfigDto.getQuantity());
        integralGiftDetailPo.setCreateUserId(FeeAttrDto.getFeeAttrValue(feeDto, FeeAttrDto.SPEC_CD_OWNER_ID));
        integralGiftDetailPo.setUserName(FeeAttrDto.getFeeAttrValue(feeDto, FeeAttrDto.SPEC_CD_OWNER_NAME));
        integralGiftDetailPo.setTel(FeeAttrDto.getFeeAttrValue(feeDto, FeeAttrDto.SPEC_CD_OWNER_LINK));
        integralGiftDetailV1InnerServiceSMOImpl.saveIntegralGiftDetail(integralGiftDetailPo);

    }


}
