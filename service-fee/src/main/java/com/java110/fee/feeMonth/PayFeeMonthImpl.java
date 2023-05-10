package com.java110.fee.feeMonth;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.payFeeDetailMonth.PayFeeDetailMonthDto;
import com.java110.dto.payFeeDetailMonth.PayFeeMonthOwnerDto;
import com.java110.dto.report.ReportRoomDto;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.fee.IPayFeeConfigV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeDetailMonthInnerServiceSMO;
import com.java110.po.payFeeDetailMonth.PayFeeDetailMonthPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * 费用离散为月 实现类
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
    private IPayFeeMonthHelp payFeeMonthHelp;

    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    public static final int DEFAULT_DEAL_COUNT = 200;


    /**
     * 生成单个费用 并 离散到月
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

    public void doGeneratorOrRefreshFeeMonth(FeeDto feeDto, String communityId) {

        //todo 计算每月单价
        Double feePrice = payFeeMonthHelp.getMonthFeePrice(feeDto);

        // 准备离散的基础数据
        PayFeeMonthOwnerDto payFeeMonthOwnerDto = payFeeMonthHelp.generatorOwnerRoom(feeDto);


        //todo 检查费用是否离散过，如果没有离散过，先离散 start_time 到 end_time 的数据
        ifHasNoMonthData(feeDto,payFeeMonthOwnerDto,feePrice);

    }




    /**
     * 如果费用没有month 数据，则直接离散 start_time 到end_time 数据
     * @param feeDto
     */
    private void ifHasNoMonthData(FeeDto feeDto,PayFeeMonthOwnerDto payFeeMonthOwnerDto,Double feePrice) {

        //todo 分析建账时间 和开始时间
        Date startTime = feeDto.getStartTime();
        if(startTime == null){
            throw new IllegalArgumentException("数据错误，未包含开始时间");
        }

        Calendar calendar = Calendar.getInstance();
        int startYear = calendar.get(Calendar.YEAR);
        int startMonth = calendar.get(Calendar.MONTH)+1;

        PayFeeDetailMonthDto payFeeDetailMonthDto = new PayFeeDetailMonthDto();
        payFeeDetailMonthDto.setCommunityId(feeDto.getCommunityId());
        payFeeDetailMonthDto.setFeeId(feeDto.getFeeId());
        payFeeDetailMonthDto.setDetailYear(startYear+"");
        payFeeDetailMonthDto.setDetailMonth(startMonth+"");
        int count = payFeeDetailMonthInnerServiceSMOImpl.queryPayFeeDetailMonthsCount(payFeeDetailMonthDto);

        // todo 说明这个费用已经第一次离散过
        if(count > 0){
            return ;
        }

        double maxMonth = Math.ceil(computeFeeSMOImpl.dayCompare(startTime, feeDto.getEndTime()));

        if (maxMonth < 1) {
            return;
        }

        PayFeeDetailMonthPo tmpPayFeeDetailMonthPo;
        String detailId = "";
        String discountAmount = "";
        String receivedAmount = "";
        List<PayFeeDetailMonthPo> payFeeDetailMonthPos = new ArrayList<>();
        for (int month = 0; month < maxMonth; month++) {
            calendar.setTime(startTime);
            calendar.add(Calendar.MONTH, month);
            tmpPayFeeDetailMonthPo = new PayFeeDetailMonthPo();
            tmpPayFeeDetailMonthPo.setFeeId(feeDto.getFeeId());
            tmpPayFeeDetailMonthPo.setCommunityId(feeDto.getCommunityId());
            tmpPayFeeDetailMonthPo.setDetailId(detailId);
            tmpPayFeeDetailMonthPo.setDetailMonth((calendar.get(Calendar.MONTH) + 1) + "");
            tmpPayFeeDetailMonthPo.setDetailYear(calendar.get(Calendar.YEAR) + "");
            tmpPayFeeDetailMonthPo.setDiscountAmount(discountAmount);
            tmpPayFeeDetailMonthPo.setMonthId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_monthId));
            tmpPayFeeDetailMonthPo.setReceivableAmount(feePrice + "");
            tmpPayFeeDetailMonthPo.setReceivedAmount(receivedAmount);
            tmpPayFeeDetailMonthPo.setRemark("程序计算生成");
            tmpPayFeeDetailMonthPo.setObjName(payFeeMonthOwnerDto.getObjName());
            tmpPayFeeDetailMonthPo.setObjId(payFeeMonthOwnerDto.getObjId());
            tmpPayFeeDetailMonthPo.setOwnerId(payFeeMonthOwnerDto.getOwnerId());
            tmpPayFeeDetailMonthPo.setOwnerName(payFeeMonthOwnerDto.getOwnerName());
            payFeeDetailMonthPos.add(tmpPayFeeDetailMonthPo);
        }
        payFeeDetailMonthInnerServiceSMOImpl.savePayFeeDetailMonths(payFeeDetailMonthPos);
    }

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
            feeDto.setPage(pageIndex * max);
            feeDto.setRow(max);
            List<FeeDto> tmpFeeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
            // 离散费用
            doTmpFeeDtoMonths(communityId, tmpFeeDtos);
        }

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
