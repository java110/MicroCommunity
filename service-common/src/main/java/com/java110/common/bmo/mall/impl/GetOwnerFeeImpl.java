package com.java110.common.bmo.mall.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.mall.IMallCommonApiBmo;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.log.LoggerFactory;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.reportFee.ReportOweFeeDto;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.report.IReportOweFeeInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.MoneyUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询业主费用
 */
@Service("getOwnerFeeImpl")
public class GetOwnerFeeImpl implements IMallCommonApiBmo {
    private final static Logger logger = LoggerFactory.getLogger(GetOwnerFeeImpl.class);

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IReportOweFeeInnerServiceSMO reportOweFeeInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;


    @Override
    public void validate(ICmdDataFlowContext context, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "tel", "未包含手机号");
    }

    @Override
    public void doCmd(ICmdDataFlowContext context, JSONObject reqJson) {
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setLink(reqJson.getString("tel"));
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);
        if (ListUtil.isNull(ownerDtos)) {
            throw new CmdException("业主不存在");
        }

        List<String> ownerIds = new ArrayList<>();
        for (OwnerDto tmpOwnerDto : ownerDtos) {
            ownerIds.add(tmpOwnerDto.getOwnerId());
        }

        ReportOweFeeDto reportOweFeeDto = new ReportOweFeeDto();
        reportOweFeeDto.setOwnerIds(ownerIds.toArray(new String[ownerIds.size()]));
        List<ReportOweFeeDto> reportOweFeeDtos = reportOweFeeInnerServiceSMOImpl.queryOwnerOweFee(reportOweFeeDto);
        if (ListUtil.isNull(reportOweFeeDtos)) {
            throw new CmdException("未包含费用");
        }
        // todo 这里取一个房屋欠费即可
        String roomId = reportOweFeeDtos.get(0).getPayerObjId();
        String communityId = reportOweFeeDtos.get(0).getCommunityId();

        FeeDto feeDto = new FeeDto();
        feeDto.setState(FeeDto.STATE_DOING);
        feeDto.setPayerObjId(roomId);
        feeDto.setCommunityId(communityId);
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
        if (ListUtil.isNull(feeDtos)) {
            throw new CmdException("未包含费用");
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

                if (tmpFeeDto.getFeeTotalPrice() != 0) {
                    tmpFeeDtos.add(tmpFeeDto);
                }
            } catch (Exception e) {
                logger.error("可能费用资料有问题导致算费失败", e);
            }
        }

        context.setResponseEntity(ResultVo.createResponseEntity(tmpFeeDtos));
    }
}
