package com.java110.job.task.fee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.client.RestTemplate;
import com.java110.core.factory.CallApiServiceFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.account.AccountDto;
import com.java110.dto.app.AppDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.logSystemError.LogSystemErrorDto;
import com.java110.dto.task.TaskDto;
import com.java110.intf.acct.IAccountInnerServiceSMO;
import com.java110.intf.common.ILogSystemErrorInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.job.quartz.TaskSystemQuartz;
import com.java110.po.accountDetail.AccountDetailPo;
import com.java110.po.logSystemError.LogSystemErrorPo;
import com.java110.service.smo.ISaveSystemErrorSMO;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.exception.SMOException;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName GenerateOwnerBillTemplate
 * @Description TODO  预存账户扣款缴费
 * @Author wuxw
 * @Date 2020/6/4 8:33
 * @Version 1.0
 * add by wuxw 2020/6/4
 **/
@Component
public class AccountDeductionPayFeeTemplate extends TaskSystemQuartz {


    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    @Autowired
    private ILogSystemErrorInnerServiceSMO logSystemErrorInnerServiceSMOImpl;

    @Autowired
    private ISaveSystemErrorSMO saveSystemErrorSMOImpl;

    @Autowired
    private IAccountInnerServiceSMO accountInnerServiceSMOImpl;

    @Autowired
    RestTemplate restTemplate;

    @Override
    protected void process(TaskDto taskDto) throws Exception {

        // 获取小区
        List<CommunityDto> communityDtos = getAllCommunity();

        for (CommunityDto communityDto : communityDtos) {
            doDeductionPayFeeFee(taskDto, communityDto);
        }

    }

    /**
     * 根据小区生成账单
     *
     * @param communityDto
     */
    private void doDeductionPayFeeFee(TaskDto taskDto, CommunityDto communityDto) {

        //查询费用项
        AccountDto accountDto = new AccountDto();
        accountDto.setPartId(communityDto.getCommunityId());
        accountDto.setHasMoney("1");
        accountDto.setAcctType(AccountDto.ACCT_TYPE_CASH);
        List<AccountDto> accountDtos = accountInnerServiceSMOImpl.queryAccounts(accountDto);

        if (accountDtos == null || accountDtos.size() < 1) {
            return;
        }

        for (AccountDto tmpAccountDto : accountDtos) {
            try {
                doPayFee(tmpAccountDto);
            } catch (Exception e) {
                LogSystemErrorPo logSystemErrorPo = new LogSystemErrorPo();
                logSystemErrorPo.setErrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_errId));
                logSystemErrorPo.setErrType(LogSystemErrorDto.ERR_TYPE_JOB);
                logSystemErrorPo.setMsg(ExceptionUtil.getStackTrace(e));
                saveSystemErrorSMOImpl.saveLog(logSystemErrorPo);
                logger.error("生成费用失败", e);
            }
        }

    }

    private void doPayFee(AccountDto tmpAccountDto) {

        // 查询业主的费用
        FeeDto feeDto = new FeeDto();
        feeDto.setCommunityId(tmpAccountDto.getPartId());
        feeDto.setOwnerId(tmpAccountDto.getObjId());
        feeDto.setState(FeeDto.STATE_DOING);
        feeDto.setDeductFrom("Y");
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
        if (feeDtos == null || feeDtos.size() < 1) {
            return;
        }
        List<FeeDto> tmpFeeDtos = new ArrayList<>();
        for (FeeDto tmpFeeDto : feeDtos) {
            computeFeeSMOImpl.computeEveryOweFee(tmpFeeDto);//计算欠费金额
            //如果金额为0 就排除
            if (tmpFeeDto.getFeePrice() > 0 && tmpFeeDto.getEndTime().getTime() <= DateUtil.getCurrentDate().getTime()) {
                tmpFeeDtos.add(tmpFeeDto);
            }
        }

        if (tmpFeeDtos.size() < 1) {
            return;
        }
        int flag = 0;
        computeFeeSMOImpl.freshFeeObjName(tmpFeeDtos);
        for (FeeDto tmpFeeDto : tmpFeeDtos) {
            try {
                if (tmpFeeDto.getFeePrice() > Double.parseDouble(tmpAccountDto.getAmount())) {
                    continue; // 余额不足
                }
                //先做扣款
                AccountDetailPo accountDetailPo = new AccountDetailPo();
                accountDetailPo.setAcctId(tmpAccountDto.getAcctId());
                accountDetailPo.setAmount(tmpFeeDto.getFeePrice() + "");
                accountDetailPo.setObjId(tmpAccountDto.getObjId());
                accountDetailPo.setObjType(tmpAccountDto.getObjType());
                accountDetailPo.setRemark("缴费扣款,费用对象："
                        + tmpFeeDto.getPayerObjName()
                        + ",费用：" + tmpFeeDto.getFeeName()
                        + ",时间：" + DateUtil.getFormatTimeString(tmpFeeDto.getEndTime(), DateUtil.DATE_FORMATE_STRING_A)
                        + "至" + DateUtil.getFormatTimeString(tmpFeeDto.getDeadlineTime(), DateUtil.DATE_FORMATE_STRING_A));
                flag = accountInnerServiceSMOImpl.withholdAccount(accountDetailPo);
                if (flag < 1) {
                    continue;//扣款失败
                }
                JSONObject param = new JSONObject();
                param.put("communityId", tmpAccountDto.getPartId());
                JSONArray fees = new JSONArray();
                JSONObject fee = new JSONObject();
                fee.put("feeId", tmpFeeDto.getFeeId());
                fee.put("startTime", DateUtil.getFormatTimeString(tmpFeeDto.getEndTime(), DateUtil.DATE_FORMATE_STRING_A));
                fee.put("endTime", DateUtil.getFormatTimeString(tmpFeeDto.getDeadlineTime(), DateUtil.DATE_FORMATE_STRING_A));
                fee.put("receivedAmount", tmpFeeDto.getFeePrice());
                fee.put("primeRate", "1");
                fee.put("remark", "定时账户扣款缴费");
                fees.add(fee);
                param.put("fees", fees);
                param.put("remark", "定时账户扣款缴费");
                try {
                    CallApiServiceFactory.postForApi(AppDto.JOB_APP_ID, param, ServiceCodeConstant.SERVICE_CODE_PAY_OWE_FEE, JSONObject.class);
                } catch (SMOException e) {
                    logger.error("缴费失败", e);
                    accountDetailPo = new AccountDetailPo();
                    accountDetailPo.setAcctId(tmpAccountDto.getAcctId());
                    accountDetailPo.setAmount(tmpFeeDto.getFeePrice() + "");
                    accountDetailPo.setObjId(tmpAccountDto.getObjId());
                    accountDetailPo.setObjType(tmpAccountDto.getObjType());
                    accountDetailPo.setRemark("缴费失败，费用预存");
                    flag = accountInnerServiceSMOImpl.prestoreAccount(accountDetailPo);
                    if (flag < 1) {
                        throw new IllegalArgumentException("费用回退失败，严重，请手工处理 " + tmpAccountDto.getAcctId() + "金额：" + tmpFeeDto.getFeePrice());
                    }
                }
            } catch (Exception e) {
                logger.error("缴费失败", e);
                LogSystemErrorPo logSystemErrorPo = new LogSystemErrorPo();
                logSystemErrorPo.setErrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_errId));
                logSystemErrorPo.setErrType(LogSystemErrorDto.ERR_TYPE_ACCOUNT);
                logSystemErrorPo.setMsg(ExceptionUtil.getStackTrace(e));
                saveSystemErrorSMOImpl.saveLog(logSystemErrorPo);
                logger.error("生成费用失败", e);
            }
        }


        //feeId: _item,
        //                                startTime: _oweFeeItem.endTime,
        //                                endTime: _oweFeeItem.deadlineTime,
        //                                receivedAmount: _oweFeeItem.feePrice,
        //                                primeRate:$that.owePayFeeOrderInfo.primeRate

    }

}
