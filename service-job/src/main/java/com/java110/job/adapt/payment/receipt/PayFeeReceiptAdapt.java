package com.java110.job.adapt.payment.receipt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.java110.core.factory.CommunitySettingFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.log.LogSystemErrorDto;
import com.java110.dto.machine.MachinePrinterDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.printerRule.PrinterRuleDto;
import com.java110.dto.printerRule.PrinterRuleFeeDto;
import com.java110.dto.printerRule.PrinterRuleMachineDto;
import com.java110.dto.system.Business;
import com.java110.intf.common.IMachinePrinterV1InnerServiceSMO;
import com.java110.intf.common.IPrinterRuleFeeV1InnerServiceSMO;
import com.java110.intf.common.IPrinterRuleMachineV1InnerServiceSMO;
import com.java110.intf.common.IPrinterRuleV1InnerServiceSMO;
import com.java110.intf.fee.*;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.job.printer.IPrinter;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.fee.FeeReceiptPo;
import com.java110.po.fee.FeeReceiptDetailPo;
import com.java110.po.log.LogSystemErrorPo;
import com.java110.service.smo.ISaveSystemErrorSMO;
import com.java110.utils.cache.CommonCache;
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
    private IPrinterRuleFeeV1InnerServiceSMO printerRuleFeeV1InnerServiceSMOImpl;

    @Autowired
    private IPrinterRuleV1InnerServiceSMO printerRuleV1InnerServiceSMOImpl;

    @Autowired
    private IPrinterRuleMachineV1InnerServiceSMO printerRuleMachineV1InnerServiceSMOImpl;

    @Autowired
    private IMachinePrinterV1InnerServiceSMO machinePrinterV1InnerServiceSMOImpl;

    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;


    @Autowired
    private IGeneratorFeeReceiptInnerServiceSMO generatorFeeReceiptInnerServiceSMOImpl;


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
        //查询缴费明细
        PayFeeDetailPo payFeeDetailPo = BeanConvertUtil.covertBean(businessPayFeeDetail, PayFeeDetailPo.class);
        generatorFeeReceiptInnerServiceSMOImpl.generator(payFeeDetailPo);

        //todo 是否配置了自动打印功能 如果配置了自动打印功能 自动打印
        autoPrintReceipt(payFeeDetailPo.getDetailId(), payFeeDetailPo.getCommunityId());
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

        printer.printPayFeeDetail(new String[]{detailId}, tmpPrinterRuleMachineDto.getCommunityId(), Integer.parseInt(tmpPrinterRuleMachineDto.getQuantity()), machinePrinterDtos.get(0), "");

    }



}
