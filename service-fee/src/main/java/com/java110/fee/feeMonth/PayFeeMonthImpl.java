package com.java110.fee.feeMonth;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.payFeeDetailMonth.PayFeeDetailMonthDto;
import com.java110.dto.payFeeDetailMonth.PayFeeMonthOwnerDto;
import com.java110.intf.fee.*;
import com.java110.po.payFeeDetailMonth.PayFeeDetailMonthPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * 费用离散为月 实现类
 * V2
 * <p>
 * case 1 定时任务 调用
 */
@Service
public class PayFeeMonthImpl implements IPayFeeMonth {
    private static Logger logger = LoggerFactory.getLogger(PayFeeMonthImpl.class);

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IPayFeeConfigV1InnerServiceSMO payFeeConfigV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeDetailMonthInnerServiceSMO payFeeDetailMonthInnerServiceSMOImpl;

    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

    @Autowired
    private IPayFeeMonthHelp payFeeMonthHelp;

    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    public static final int DEFAULT_DEAL_COUNT = 200;

    /**
     * 1.0 定时任务离散 小区数据 离散为 月数据
     *
     * @param communityId
     */
    @Async
    @Override
    public void doGeneratorOrRefreshAllFeeMonth(String communityId) {


        FeeDto feeDto = new FeeDto();
        feeDto.setCommunityId(communityId);
        int count = feeInnerServiceSMOImpl.queryFeesCount(feeDto);

        int page = 1;
        int max = 15;
        if (count < DEFAULT_DEAL_COUNT) {
            page = 1;
            max = count;
        } else {
            page = (int) Math.ceil((double) count / (double) DEFAULT_DEAL_COUNT);
            max = DEFAULT_DEAL_COUNT;
        }

        //todo  每次按200条处理
        for (int pageIndex = 0; pageIndex < page; pageIndex++) {
            feeDto.setPage(pageIndex + 1);
            feeDto.setRow(max);
            List<FeeDto> tmpFeeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
            // 离散费用
            doTmpFeeDtoMonths(communityId, tmpFeeDtos);
        }


    }

    /**
     * 2.0 物业缴费时离散 报表数据
     *
     * @param feeId
     * @param detailId
     * @param communityId
     */
    @Async
    @Override
    public void payFeeDetailRefreshFeeMonth(String feeId, String detailId, String communityId) {
        // todo 查询费用
        FeeDto feeDto = new FeeDto();
        feeDto.setCommunityId(communityId);
        feeDto.setFeeId(feeId);
        List<FeeDto> tmpFeeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

        Assert.listOnlyOne(tmpFeeDtos, "费用不存在");
        feeDto = tmpFeeDtos.get(0);
        //todo 查询 缴费明细
        FeeDetailDto feeDetailDto = new FeeDetailDto();
        feeDetailDto.setCommunityId(feeDto.getCommunityId());
        feeDetailDto.setFeeId(feeDto.getFeeId());
        feeDetailDto.setDetailId(detailId);
        List<FeeDetailDto> feeDetailDtos = feeDetailInnerServiceSMOImpl.queryFeeDetails(feeDetailDto);

        Assert.listOnlyOne(feeDetailDtos, "缴费记录不存在");

        //todo 计算每月单价
        Double feePrice = payFeeMonthHelp.getMonthFeePrice(feeDto);

        // todo 准备离散的基础数据
        PayFeeMonthOwnerDto payFeeMonthOwnerDto = payFeeMonthHelp.generatorOwnerRoom(feeDto);

        // todo 删除缴费时间范围内的数据
        doDeletePayFeeDetailInMonth(feeDto, feeDetailDtos.get(0));

        // todo 生成一段时间内的数据
        maxMonthDateToDeadlineTimeData(feeDto, payFeeMonthOwnerDto, feePrice);
    }

    /**
     * 生成单个费用 并 离散到月
     *
     * @param feeId
     * @param communityId
     */
    @Override
    public void doGeneratorOrRefreshFeeMonth(String feeId, String communityId) {

        // todo 查询费用
        FeeDto feeDto = new FeeDto();
        feeDto.setCommunityId(communityId);
        feeDto.setFeeId(feeId);
        List<FeeDto> tmpFeeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

        Assert.listOnlyOne(tmpFeeDtos, "费用不存在");

        doGeneratorOrRefreshFeeMonth(tmpFeeDtos.get(0), communityId);
    }




    /**
     * 删除月费用
     *
     * @param feeId
     * @param communityId
     */
    @Override
    public void deleteFeeMonth(String feeId, String communityId) {

        PayFeeDetailMonthPo payFeeDetailMonthPo = new PayFeeDetailMonthPo();
        payFeeDetailMonthPo.setFeeId(feeId);
        payFeeDetailMonthPo.setCommunityId(communityId);
        payFeeDetailMonthInnerServiceSMOImpl.deletePayFeeDetailMonth(payFeeDetailMonthPo);
    }

    private void doGeneratorOrRefreshFeeMonth(FeeDto feeDto, String communityId) {

        // todo 计算每月单价
        Double feePrice = payFeeMonthHelp.getMonthFeePrice(feeDto);

        // todo 准备离散的基础数据
        PayFeeMonthOwnerDto payFeeMonthOwnerDto = payFeeMonthHelp.generatorOwnerRoom(feeDto);

        //todo 离散start_time 或者 pay_fee_detail_month 最大月份 到  deadlineTime 的数据
        maxMonthDateToDeadlineTimeData(feeDto, payFeeMonthOwnerDto, feePrice);


    }

    /**
     * 离散最大 离散月到 deadlineTime 的数据
     * <p>
     * 核心方法处理
     *
     * @param feeDto
     * @param payFeeMonthOwnerDto
     * @param feePrice
     */
    private void maxMonthDateToDeadlineTimeData(FeeDto feeDto, PayFeeMonthOwnerDto payFeeMonthOwnerDto, Double feePrice) {

        //todo 处理已经交过费的记录处理
        payFeeMonthHelp.waitDispersedFeeDetail(feeDto, payFeeMonthOwnerDto);


        //todo 处理 endTime 到 deadlineTime 的费用
        Date deadlineTime = computeFeeSMOImpl.getDeadlineTime(feeDto);
        payFeeMonthHelp.waitDispersedOweFee(feeDto,payFeeMonthOwnerDto,feePrice,deadlineTime);

    }


    /**
     * 删除缴费范围内的数据
     *
     * @param feeDto
     * @param feeDetailDto
     */
    private void doDeletePayFeeDetailInMonth(FeeDto feeDto, FeeDetailDto feeDetailDto) {

        PayFeeDetailMonthPo payFeeDetailMonthPo = new PayFeeDetailMonthPo();
        payFeeDetailMonthPo.setFeeId(feeDto.getFeeId());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(feeDetailDto.getStartTime());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        payFeeDetailMonthPo.setCurMonthTime(DateUtil.getFormatTimeStringB(calendar.getTime()));
        payFeeDetailMonthPo.setCurMonthEndTime(DateUtil.getFormatTimeStringB(feeDetailDto.getEndTime()));
        payFeeDetailMonthInnerServiceSMOImpl.deletePayFeeDetailMonth(payFeeDetailMonthPo);
    }

    private void doTmpFeeDtoMonths(String communityId, List<FeeDto> tmpFeeDtos) {
        for (FeeDto tmpFeeDto : tmpFeeDtos) {
            try {
                doGeneratorOrRefreshFeeMonth(tmpFeeDto, communityId);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("生成费用报表失败" + JSONObject.toJSONString(tmpFeeDto), e);
            }
        }
    }
}
