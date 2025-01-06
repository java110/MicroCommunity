package com.java110.common.bmo.mall.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.mall.IMallCommonApiBmo;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.community.CommunityMemberDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.reportFee.ReportOweFeeDto;
import com.java110.dto.store.StoreDto;
import com.java110.intf.community.ICommunityMemberV1InnerServiceSMO;
import com.java110.intf.community.ICommunityV1InnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.report.IReportOweFeeInnerServiceSMO;
import com.java110.intf.store.IStoreV1InnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.cache.UrlCache;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.*;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    @Autowired
    private ICommunityV1InnerServiceSMO communityV1InnerServiceSMOImpl;

    @Autowired
    private ICommunityMemberV1InnerServiceSMO communityMemberV1InnerServiceSMOImpl;

    @Autowired
    private IStoreV1InnerServiceSMO storeV1InnerServiceSMOImpl;


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
        String ownerName = reportOweFeeDtos.get(0).getOwnerName();
        String roomName = reportOweFeeDtos.get(0).getPayerObjName();

        FeeDto feeDto = new FeeDto();
        feeDto.setState(FeeDto.STATE_DOING);
        feeDto.setPayerObjId(roomId);
        feeDto.setCommunityId(communityId);
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
        if (ListUtil.isNull(feeDtos)) {
            throw new CmdException("未包含费用");
        }
        List<FeeDto> tmpFeeDtos = new ArrayList<>();
        List<String> feeIds = new ArrayList<>();
        BigDecimal oweMoney = new BigDecimal("0");
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
                    feeIds.add(tmpFeeDto.getFeeId());
                    oweMoney = oweMoney.add(new BigDecimal(tmpFeeDto.getFeeTotalPrice()+""));
                }
            } catch (Exception e) {
                logger.error("可能费用资料有问题导致算费失败", e);
            }
        }
        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(communityId);
        List<CommunityDto> communityDtos = communityV1InnerServiceSMOImpl.queryCommunitys(communityDto);
        String communityName = "";
        if (ListUtil.isNull(communityDtos)) {
            throw new CmdException("未查询到小区");
        }
        CommunityMemberDto communityMemberDto = new CommunityMemberDto();
        communityMemberDto.setCommunityId(communityId);
        communityMemberDto.setMemberTypeCd(CommunityMemberDto.MEMBER_TYPE_PROPERTY);

        List<CommunityMemberDto> communityMemberDtos = communityMemberV1InnerServiceSMOImpl.queryCommunityMembers(communityMemberDto);
        if (ListUtil.isNull(communityMemberDtos)) {
            throw new CmdException("未查询到物业公司");
        }
        StoreDto storeDto = new StoreDto();
        storeDto.setStoreId(communityMemberDtos.get(0).getMemberId());
        List<StoreDto> storeDtos = storeV1InnerServiceSMOImpl.queryStores(storeDto);
        if (ListUtil.isNull(storeDtos)) {
            throw new CmdException("未查询到物业公司");
        }

        String ownerUrl = UrlCache.getOwnerUrl();

        JSONObject data = new JSONObject();
        data.put("communityId", communityId);
        data.put("communityName", communityDtos.get(0).getName());
        data.put("storeId", storeDtos.get(0).getStoreId());
        data.put("storeName", storeDtos.get(0).getName());
        data.put("communityTel", communityDtos.get(0).getTel());
        data.put("ownerName", ownerName);
        data.put("roomName",roomName);
        data.put("qrCode", ownerUrl + "x");
        data.put("fees", tmpFeeDtos);
        data.put("oweMoney",oweMoney.doubleValue());
        String token = GenerateCodeFactory.getUUID();
        JSONObject payData = new JSONObject();
        payData.put("communityId", communityId);
        payData.put("roomId", roomId);
        payData.put("business", "oweFee");
        payData.put("storeId", storeDtos.get(0).getStoreId());
        payData.put("createUserId", storeDtos.get(0).getStoreId());
        reqJson.put("money", oweMoney.doubleValue());
        reqJson.put("feeIds", feeIds);

        // redis 中 保存 请求参数
        CommonCache.setValue("nativeQrcodePayment_" + token, payData.toJSONString(), CommonCache.PAY_DEFAULT_EXPIRE_TIME);
        data.put("qrCode", UrlCache.getOwnerUrl() + "/#/pages/fee/qrCodeCashier?qrToken=" + token);

        context.setResponseEntity(ResultVo.createResponseEntity(data));
    }
}
