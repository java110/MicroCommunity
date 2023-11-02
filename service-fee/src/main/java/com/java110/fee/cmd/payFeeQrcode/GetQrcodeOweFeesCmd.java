package com.java110.fee.cmd.payFeeQrcode;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.CommunitySettingFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeDto;
import com.java110.fee.bmo.impl.QueryOweFeeImpl;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.MoneyUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询 二维码 欠费费用
 */
@Java110Cmd(serviceCode = "payFeeQrcode.getQrcodeOweFees")
public class GetQrcodeOweFeesCmd extends Cmd {

    private final static Logger logger = LoggerFactory.getLogger(GetQrcodeOweFeesCmd.class);
    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;


    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    //域
    public static final String DOMAIN_COMMON = "DOMAIN.COMMON";

    //键
    public static final String TOTAL_FEE_PRICE = "TOTAL_FEE_PRICE";

    //键
    public static final String RECEIVED_AMOUNT_SWITCH = "RECEIVED_AMOUNT_SWITCH";

    //禁用电脑端提交收费按钮
    public static final String OFFLINE_PAY_FEE_SWITCH = "OFFLINE_PAY_FEE_SWITCH";

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");
        Assert.hasKeyAndValue(reqJson, "ownerId", "未包含业主");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        FeeDto feeDto = new FeeDto();
        feeDto.setOwnerId(reqJson.getString("ownerId"));
        feeDto.setCommunityId(reqJson.getString("communityId"));
        feeDto.setArrearsEndTime(DateUtil.getCurrentDate());
        feeDto.setState(FeeDto.STATE_DOING);
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

        if (feeDtos == null || feeDtos.size() < 1) {
            feeDtos = new ArrayList<>();
            context.setResponseEntity(ResultVo.createResponseEntity(feeDtos));
            return;
        }

        String val = CommunitySettingFactory.getValue(feeDtos.get(0).getCommunityId(), TOTAL_FEE_PRICE);
        if (StringUtil.isEmpty(val)) {
            val = MappingCache.getValue(DOMAIN_COMMON, TOTAL_FEE_PRICE);
        }
        List<FeeDto> tmpFeeDtos = new ArrayList<>();
        for (FeeDto tmpFeeDto : feeDtos) {
            try {
                computeFeeSMOImpl.computeEveryOweFee(tmpFeeDto);//计算欠费金额
                //如果金额为0 就排除
                tmpFeeDto.setFeeTotalPrice(
                        MoneyUtil.computePriceScale(
                                tmpFeeDto.getFeeTotalPrice(),
                                tmpFeeDto.getScale(),
                                Integer.parseInt(tmpFeeDto.getDecimalPlace())
                        )
                );

                tmpFeeDto.setVal(val);
                //todo 这里考虑负数金额，有些可能要红冲
                if (tmpFeeDto.getFeePrice() !=0) {
                    String ownerName = FeeAttrDto.getFeeAttrValue(tmpFeeDto,FeeAttrDto.SPEC_CD_OWNER_NAME);
                    ownerName = StringUtil.maskName(ownerName);
                    tmpFeeDto.setFeeAttrDtos(null); //todo 这里删掉 以免信息泄露带来的风险
                    tmpFeeDto.setOwnerName(ownerName);
                    tmpFeeDtos.add(tmpFeeDto);
                }
            } catch (Exception e) {
                logger.error("可能费用资料有问题导致算费失败", e);
            }
        }


        context.setResponseEntity(ResultVo.createResponseEntity(feeDtos));
    }
}
