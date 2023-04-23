package com.java110.job.adapt.payment.receipt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.logSystemError.LogSystemErrorDto;
import com.java110.dto.machine.MachinePrinterDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.printerRule.PrinterRuleDto;
import com.java110.dto.printerRule.PrinterRuleFeeDto;
import com.java110.dto.printerRule.PrinterRuleMachineDto;
import com.java110.entity.order.Business;
import com.java110.intf.common.IMachinePrinterV1InnerServiceSMO;
import com.java110.intf.common.IPrinterRuleFeeV1InnerServiceSMO;
import com.java110.intf.common.IPrinterRuleMachineV1InnerServiceSMO;
import com.java110.intf.common.IPrinterRuleV1InnerServiceSMO;
import com.java110.intf.fee.IFeeDetailInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.fee.IFeeReceiptDetailInnerServiceSMO;
import com.java110.intf.fee.IFeeReceiptInnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.job.printer.IPrinter;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.feeReceipt.FeeReceiptPo;
import com.java110.po.feeReceiptDetail.FeeReceiptDetailPo;
import com.java110.po.logSystemError.LogSystemErrorPo;
import com.java110.service.smo.ISaveSystemErrorSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ExceptionUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 缴费收据处理
 *
 * @author fqz
 * @date 2020-12-11  18:54
 */
@Component(value = "payFeeReceiptAdapt")
public class PayFeeReceiptAdapt extends DatabusAdaptImpl {

    private static Logger logger = LoggerFactory.getLogger(PayFeeReceiptAdapt.class);

    @Autowired
    private ISaveSystemErrorSMO saveSystemErrorSMOImpl;

    @Autowired
    private IFeeReceiptInnerServiceSMO feeReceiptInnerServiceSMOImpl;

    @Autowired
    private IFeeReceiptDetailInnerServiceSMO feeReceiptDetailInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;


    @Autowired
    private IPrinterRuleFeeV1InnerServiceSMO printerRuleFeeV1InnerServiceSMOImpl;

    @Autowired
    private IPrinterRuleV1InnerServiceSMO printerRuleV1InnerServiceSMOImpl;

    @Autowired
    private IPrinterRuleMachineV1InnerServiceSMO printerRuleMachineV1InnerServiceSMOImpl;

    @Autowired
    private IMachinePrinterV1InnerServiceSMO machinePrinterV1InnerServiceSMOImpl;

    //模板信息推送地址
    private static String sendMsgUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

    public final static String ALI_SMS_DOMAIN = "ALI_SMS";

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
            feeDto = feeDtos.get(0);
            //查询业主信息
            OwnerDto ownerDto = computeFeeSMOImpl.getFeeOwnerDto(feeDto);
            // if received amount lt zero
            if (businessPayFeeDetail.containsKey("receivedAmount")
                    && businessPayFeeDetail.getDoubleValue("receivedAmount") < 0) {
                return;
            }
            //添加收据和收据详情
            FeeReceiptPo feeReceiptPo = new FeeReceiptPo();
            FeeReceiptDetailPo feeReceiptDetailPo = new FeeReceiptDetailPo();
            feeReceiptDetailPo.setAmount(businessPayFeeDetail.getString("receivedAmount"));
            feeReceiptDetailPo.setCommunityId(feeDto.getCommunityId());
            feeReceiptDetailPo.setCycle(businessPayFeeDetail.getString("cycles"));
            feeReceiptDetailPo.setDetailId(businessPayFeeDetail.getString("detailId"));
            feeReceiptDetailPo.setEndTime(businessPayFeeDetail.getString("endTime"));
            feeReceiptDetailPo.setFeeId(feeDto.getFeeId());
            feeReceiptDetailPo.setFeeName(StringUtil.isEmpty(feeDto.getImportFeeName()) ? feeDto.getFeeName() : feeDto.getImportFeeName());
            feeReceiptDetailPo.setStartTime(businessPayFeeDetail.getString("startTime"));
            feeReceiptDetailPo.setReceiptId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_receiptId));
            feeReceiptDetailPo.setCreateTime(payFeeDetailPo.getCreateTime());
            //处理 小数点后 0
            feeDto.setSquarePrice(Double.parseDouble(feeDto.getSquarePrice()) + "");
            feeDto.setAdditionalAmount(Double.parseDouble(feeDto.getAdditionalAmount()) + "");
            computeFeeSMOImpl.freshFeeReceiptDetail(feeDto, feeReceiptDetailPo);
            feeReceiptPo.setAmount(feeReceiptDetailPo.getAmount());
            feeReceiptPo.setCommunityId(feeReceiptDetailPo.getCommunityId());
            feeReceiptPo.setReceiptId(feeReceiptDetailPo.getReceiptId());
            feeReceiptPo.setObjType(feeDto.getPayerObjType());
            feeReceiptPo.setObjId(feeDto.getPayerObjId());
            feeReceiptPo.setObjName(computeFeeSMOImpl.getFeeObjName(feeDto));
            feeReceiptPo.setPayObjId(ownerDto.getOwnerId());
            feeReceiptPo.setPayObjName(ownerDto.getName());
            feeReceiptPo.setCreateTime(payFeeDetailPo.getCreateTime());
            //这里只是写入 收据表，暂不考虑 事务一致性问题，就算写入失败 也只是影响 收据打印，如果 贵公司对 收据要求 比较高，不能有失败的情况 请加入事务管理
            feeReceiptDetailInnerServiceSMOImpl.saveFeeReceiptDetail(feeReceiptDetailPo);
            feeReceiptInnerServiceSMOImpl.saveFeeReceipt(feeReceiptPo);

            // 是否配置了自动打印功能 如果配置了自动打印功能 自动打印
            autoPrintReceipt(businessPayFeeDetail.getString("detailId"), feeDto.getCommunityId());
        } catch (Exception e) {
            LogSystemErrorPo logSystemErrorPo = new LogSystemErrorPo();
            logSystemErrorPo.setErrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_errId));
            logSystemErrorPo.setErrType(LogSystemErrorDto.ERR_TYPE_NOTICE);
            logSystemErrorPo.setMsg(ExceptionUtil.getStackTrace(e));
            saveSystemErrorSMOImpl.saveLog(logSystemErrorPo);
            logger.error("通知异常", e);
        }
    }



    private void autoPrintReceipt(String detailId, String communityId) {

        FeeDetailDto feeDetailDto = new FeeDetailDto();
        feeDetailDto.setDetailId(detailId);
        feeDetailDto.setCommunityId(communityId);
        List<FeeDetailDto> feeDetailDtos = feeDetailInnerServiceSMOImpl.queryFeeDetails(feeDetailDto);

        if (feeDetailDtos == null || feeDetailDtos.size() < 1) {
            return;
        }

        PrinterRuleFeeDto printerRuleFeeDto = new PrinterRuleFeeDto();
        printerRuleFeeDto.setCommunityId(communityId);
        printerRuleFeeDto.setFeeId(feeDetailDtos.get(0).getConfigId());
        List<PrinterRuleFeeDto> printerRuleFeeDtos = printerRuleFeeV1InnerServiceSMOImpl.queryPrinterRuleFees(printerRuleFeeDto);

        if (printerRuleFeeDtos == null || printerRuleFeeDtos.size() < 1) {
            return;
        }

        PrinterRuleDto printerRuleDto = new PrinterRuleDto();
        printerRuleDto.setRuleId(printerRuleFeeDtos.get(0).getRuleId());
        printerRuleDto.setCommunityId(communityId);
        printerRuleDto.setState(PrinterRuleDto.STATE_NORMAL);
        int count = printerRuleV1InnerServiceSMOImpl.queryPrinterRulesCount(printerRuleDto);

        if (count < 1) {
            return;
        }

        PrinterRuleMachineDto printerRuleMachineDto = new PrinterRuleMachineDto();
        printerRuleMachineDto.setCommunityId(communityId);
        printerRuleMachineDto.setRuleId(printerRuleFeeDtos.get(0).getRuleId());
        List<PrinterRuleMachineDto> printerRuleMachineDtos = printerRuleMachineV1InnerServiceSMOImpl.queryPrinterRuleMachines(printerRuleMachineDto);
        if (printerRuleMachineDtos == null || printerRuleMachineDtos.size() < 1) {
            return;
        }

        for (PrinterRuleMachineDto tmpPrinterRuleMachineDto : printerRuleMachineDtos) {
            try {
                doPrint(tmpPrinterRuleMachineDto, detailId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    private void doPrint(PrinterRuleMachineDto tmpPrinterRuleMachineDto, String detailId) {
        MachinePrinterDto machinePrinterDto = new MachinePrinterDto();
        machinePrinterDto.setCommunityId(tmpPrinterRuleMachineDto.getCommunityId());
        machinePrinterDto.setMachineId(tmpPrinterRuleMachineDto.getMachineId());
        List<MachinePrinterDto> machinePrinterDtos = machinePrinterV1InnerServiceSMOImpl.queryMachinePrinters(machinePrinterDto);

        Assert.listOnlyOne(machinePrinterDtos, "云打印机不存在");

        IPrinter printer = ApplicationContextFactory.getBean(machinePrinterDtos.get(0).getImplBean(), IPrinter.class);

        if (printer == null) {
            throw new CmdException("打印机异常，未包含适配器");
        }

        printer.printPayFeeDetail(new String[]{detailId}, tmpPrinterRuleMachineDto.getCommunityId(), Integer.parseInt(tmpPrinterRuleMachineDto.getQuantity()), machinePrinterDtos.get(0),"");

    }


}
