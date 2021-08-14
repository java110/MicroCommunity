package com.java110.job.adapt.payment.fee.asyn.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.RoomDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.reportFeeYearCollection.ReportFeeYearCollectionDto;
import com.java110.entity.order.Business;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.report.IReportFeeYearCollectionInnerServiceSMO;
import com.java110.job.adapt.payment.fee.asyn.IPayFeeDetailToMonth;
import com.java110.po.fee.PayFeeDetailPo;
import com.java110.po.feeReceipt.FeeReceiptPo;
import com.java110.po.feeReceiptDetail.FeeReceiptDetailPo;
import com.java110.po.reportFeeYearCollection.ReportFeeYearCollectionPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

@Service
public class PayFeeDetailToMonthIImpl implements IPayFeeDetailToMonth {


    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;


    @Autowired
    private IReportFeeYearCollectionInnerServiceSMO reportFeeYearCollectionInnerServiceSMOImpl;

    @Override
    @Async
    public void doPayFeeDetail(Business business, JSONObject businessPayFeeDetail) {
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

        ReportFeeYearCollectionDto reportFeeYearCollectionDto = new ReportFeeYearCollectionDto();
        reportFeeYearCollectionDto.setCommunityId(feeDto.getCommunityId());
        reportFeeYearCollectionDto.setConfigId(feeDto.getConfigId());
        reportFeeYearCollectionDto.setObjId(feeDto.getPayerObjId());
        reportFeeYearCollectionDto.setObjType(feeDto.getPayerObjType());
        List<ReportFeeYearCollectionDto> statistics = BeanConvertUtil.covertBeanList(
                reportFeeYearCollectionInnerServiceSMOImpl.getReportFeeYearCollectionInfo(BeanConvertUtil.beanCovertMap(reportFeeYearCollectionDto)),
                ReportFeeYearCollectionDto.class);

        ReportFeeYearCollectionPo reportFeeYearCollectionPo = new ReportFeeYearCollectionPo();
        if (ListUtil.isNull(statistics)) {
            reportFeeYearCollectionPo.setBuiltUpArea(reportRoomDto.getBuiltUpArea());
            reportFeeYearCollectionPo.setCollectionId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_collectionId));
            reportFeeYearCollectionPo.setCommunityId(feeDto.getCommunityId());
            reportFeeYearCollectionPo.setConfigId(feeDto.getConfigId());
            reportFeeYearCollectionPo.setFeeId(feeDto.getFeeId());
            reportFeeYearCollectionPo.setObjId(feeDto.getPayerObjId());
            reportFeeYearCollectionPo.setObjType(feeDto.getPayerObjType());
            reportFeeYearCollectionPo.setFeeName(StringUtil.isEmpty(feeDto.getImportFeeName()) ? feeDto.getFeeName() : feeDto.getImportFeeName());
            if (RoomDto.ROOM_TYPE_ROOM.equals(reportRoomDto.getRoomType())) {
                reportFeeYearCollectionPo.setObjName(reportRoomDto.getFloorNum() + "-" + reportRoomDto.getUnitNum() + "-" + reportRoomDto.getRoomNum());
            } else {
                reportFeeYearCollectionPo.setObjName(reportRoomDto.getFloorNum() + "-" + reportRoomDto.getRoomNum());
            }
            reportFeeYearCollectionPo.setOwnerId(ownerDto.getOwnerId());
            reportFeeYearCollectionPo.setOwnerName(ownerDto.getName());
            reportFeeYearCollectionPo.setOwnerLink(ownerDto.getLink());

            //reportFeeYearCollectionPo.setUpdateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            reportFeeYearCollectionInnerServiceSMOImpl.saveReportFeeYearCollectionInfo(BeanConvertUtil.beanCovertMap(reportFeeYearCollectionPo));
        } else {
            BeanConvertUtil.covertBean(statistics.get(0), reportFeeYearCollectionPo);
        }

        //计算费用项开始时间 起始 时间至现在的年份
        Calendar configStartTime = Calendar.getInstance();
        configStartTime.setTime(tmpReportFeeDto.getConfigStartTime());
        int startYear = configStartTime.get(Calendar.YEAR);
        //结束年
        Calendar configEndTime = Calendar.getInstance();
        configStartTime.setTime(tmpReportFeeDto.getConfigEndTime());
        int endYear = configEndTime.get(Calendar.YEAR);

        //当前年
        int curYear = Calendar.getInstance().get(Calendar.YEAR)+1;

        double feePrice = computeFeeSMOImpl.getReportFeePrice(tmpReportFeeDto, reportRoomDto, null);
        tmpReportFeeDto.setFeePrice(feePrice);

        if (endYear > curYear) {
            endYear = curYear;
        }

        for (int year = startYear; year <= endYear; year++) {
            computeYearFee(year, tmpReportFeeDto, reportFeeYearCollectionPo);
        }
    }
}
