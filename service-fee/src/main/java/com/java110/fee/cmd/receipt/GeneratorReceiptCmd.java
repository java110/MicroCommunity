package com.java110.fee.cmd.receipt;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.fee.FeeReceiptDetailDto;
import com.java110.intf.fee.*;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

/**
 * 手工生成收据
 */
@Java110Cmd(serviceCode = "receipt.generatorReceipt")
public class GeneratorReceiptCmd extends Cmd {

    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

    @Autowired
    private IFeeReceiptDetailInnerServiceSMO feeReceiptDetailInnerServiceSMOImpl;

    @Autowired
    private IGeneratorFeeReceiptInnerServiceSMO generatorFeeReceiptInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
        Assert.hasKeyAndValue(reqJson, "detailId", "未包含收据ID");


        //todo 查询收费明细是否存在
        FeeDetailDto feeDetailDto = new FeeDetailDto();
        feeDetailDto.setDetailId(reqJson.getString("detailId"));
        feeDetailDto.setCommunityId(reqJson.getString("communityId"));
        List<FeeDetailDto> feeDetailDtos = feeDetailInnerServiceSMOImpl.queryFeeDetails(feeDetailDto);
        Assert.listOnlyOne(feeDetailDtos, "缴费明细不存在");


        FeeReceiptDetailDto feeReceiptDetailDto = new FeeReceiptDetailDto();
        feeReceiptDetailDto.setDetailId(reqJson.getString("detailId"));
        feeReceiptDetailDto.setCommunityId(reqJson.getString("communityId"));
        List<FeeReceiptDetailDto> feeReceiptDetailDtos = feeReceiptDetailInnerServiceSMOImpl.queryFeeReceiptDetails(feeReceiptDetailDto);

        if (feeReceiptDetailDtos != null && feeReceiptDetailDtos.size() > 0) {
            throw new CmdException("收据已存在");
        }


        reqJson.put("feeDetailDto", feeDetailDtos.get(0));


    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        FeeDetailDto feeDetailDto = (FeeDetailDto) reqJson.get("feeDetailDto");


        PayFeeDetailPo payFeeDetailPo = BeanConvertUtil.covertBean(feeDetailDto, PayFeeDetailPo.class);

        String receiptCode = reqJson.getString("receiptCode");
        if (!StringUtil.isEmpty(receiptCode)) {
            CommonCache.setValue(payFeeDetailPo.getDetailId() + CommonCache.RECEIPT_CODE, receiptCode);
        }

        //todo 手工打印
        generatorFeeReceiptInnerServiceSMOImpl.generator(payFeeDetailPo);

        if (!StringUtil.isEmpty(receiptCode)) {
            CommonCache.removeValue(payFeeDetailPo.getDetailId() + CommonCache.RECEIPT_CODE);
        }

        context.setResponseEntity(ResultVo.success());
    }
}
