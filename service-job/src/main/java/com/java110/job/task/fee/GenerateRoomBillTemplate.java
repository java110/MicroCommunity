package com.java110.job.task.fee;

import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.fee.IFeeConfigInnerServiceSMO;
import com.java110.core.smo.fee.IFeeDetailInnerServiceSMO;
import com.java110.core.smo.fee.IFeeInnerServiceSMO;
import com.java110.core.smo.room.IRoomInnerServiceSMO;
import com.java110.dto.RoomDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.fee.BillDto;
import com.java110.dto.fee.BillOweFeeDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDetailDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.task.TaskDto;
import com.java110.job.quartz.TaskSystemQuartz;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.TaskTemplateException;
import com.java110.utils.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @ClassName GenerateOwnerBillTemplate
 * @Description TODO  房屋费用账单生成
 * @Author wuxw
 * @Date 2020/6/4 8:33
 * @Version 1.0
 * add by wuxw 2020/6/4
 **/
@Component
public class GenerateRoomBillTemplate extends TaskSystemQuartz {
    private static final String TASK_ATTR_BILL_TYPE = "10002"; // 出账类型

    private static final String TASK_ATTR_VALUE_MONTH = "002"; //按月出账

    private static final String TASK_ATTR_VALUE_DAY = "003"; //按日出账

    private static final String TASK_ATTR_VALUE_YEAR = "001"; //按年出账

    private static final String TASK_ATTR_VALUE_ONCE_MONTH = "005"; //一次性按月出账

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;


    @Override
    protected void process(TaskDto taskDto) throws Exception {

        // 获取小区
        List<CommunityDto> communityDtos = getAllCommunity();

        for (CommunityDto communityDto : communityDtos) {
            GenerateBill(taskDto, communityDto);
        }

    }

    /**
     * 根据小区生成账单
     *
     * @param communityDto
     */
    private void GenerateBill(TaskDto taskDto, CommunityDto communityDto) {

        //查询费用项
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setCommunityId(communityDto.getCommunityId());
        feeConfigDto.setBillType(getCurTaskAttr(taskDto, TASK_ATTR_BILL_TYPE).getValue());
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);

        for (FeeConfigDto tmpFeeConfigDto : feeConfigDtos) {
            try {
                GenerateBillByFeeConfig(taskDto, tmpFeeConfigDto);
            } catch (Exception e) {
                logger.error("费用出账失败" + tmpFeeConfigDto.getConfigId(), e);
            }
        }
    }

    /**
     * 按费用项来出账
     *
     * @param taskDto
     * @param feeConfigDto
     */
    private void GenerateBillByFeeConfig(TaskDto taskDto, FeeConfigDto feeConfigDto) throws Exception {

        //当前费用项是否
        BillDto billDto = new BillDto();
        billDto.setCurBill("T");
        billDto.setConfigId(feeConfigDto.getConfigId());
        billDto.setCommunityId(feeConfigDto.getCommunityId());
        List<BillDto> billDtos = feeInnerServiceSMOImpl.queryBills(billDto);

        //Assert.listOnlyOne(billDtos, "当前存在多个有效账单" + feeConfigDto.getConfigId());
        if (billDtos != null && billDtos.size() > 1) {
            throw new TaskTemplateException(ResponseConstant.RESULT_CODE_ERROR, "当前存在多个有效账单");
        }

        Date startTime = billDtos == null ? getDefaultStartTime(feeConfigDto.getBillType()) : DateUtil.getDateFromString(billDto.getBillTime(), DateUtil.DATE_FORMATE_STRING_A);

        FeeDto feeDto = new FeeDto();
        feeDto.setConfigId(feeConfigDto.getConfigId());
        feeDto.setCommunityId(feeConfigDto.getCommunityId());
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

        //没有关联费用不做出账
        if (feeDto == null || feeDtos.size() < 1) {
            return;
        }

        for (FeeDto tmpFeeDto : feeDtos) {
            generateFee(startTime, tmpFeeDto);
        }
    }

    /**
     * 生成 费用
     *
     * @param feeDto
     */
    private void generateFee(Date startTime, FeeDto feeDto) {
        Date billEndTime = DateUtil.getCurrentDate();
        if ("2009001".equals(feeDto.getState())) { //判断是否缴费结束
            FeeDetailDto feeDetailDto = new FeeDetailDto();
            feeDetailDto.setCommunityId(feeDto.getCommunityId());
            feeDetailDto.setFeeId(feeDto.getFeeId());
            List<FeeDetailDto> feeDetailDtos = feeDetailInnerServiceSMOImpl.queryFeeDetails(feeDetailDto);

            if (feeDetailDtos == null || feeDetailDtos.size() < 1) {
                return;//这种数据有问题 不做出账处理
            }

            Date detailTime = feeDetailDtos.get(0).getCreateTime();
            if (detailTime.getTime() < startTime.getTime()) {
                //说明上次出账已经出国 无需再出
                return;
            }
        }

        if (feeDto.getEndTime().getTime() > billEndTime.getTime()) { //当期没有欠费
            return;
        }

        computeFeePriceByRoom(feeDto);

        if (feeDto.getFeePrice() <= 0) {
            return ;//这个没有欠费可算
        }

        BillOweFeeDto billOweFeeDto = new BillOweFeeDto();
        billOweFeeDto.setOweId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_oweId));
        billOweFeeDto.setFeeId(feeDto.getFeeId());
        billOweFeeDto.setBillId("-1");
        //billOweFeeDto.setAmountOwed();

    }

    private Date getDefaultStartTime(String billType) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        if (billType.equals(TASK_ATTR_VALUE_MONTH)) {
            calendar.add(Calendar.MONTH, -1);
            return calendar.getTime();
        }

        if (billType.equals(TASK_ATTR_VALUE_DAY)) {
            calendar.add(Calendar.DATE, -1);
            return calendar.getTime();
        }

        if (billType.equals(TASK_ATTR_VALUE_DAY)) {
            calendar.add(Calendar.DATE, -1);
            return calendar.getTime();
        }

        if (billType.equals(TASK_ATTR_VALUE_YEAR)) {
            calendar.add(Calendar.YEAR, -1);
            return calendar.getTime();
        }

        return calendar.getTime();

    }

    /**
     * 根据房屋来算单价
     *
     * @param feeDto
     */
    private void computeFeePriceByRoom(FeeDto feeDto) {
        RoomDto roomDto = new RoomDto();
        roomDto.setCommunityId(feeDto.getCommunityId());
        roomDto.setRoomId(feeDto.getPayerObjId());
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);

        if (roomDtos == null || roomDtos.size() < 1) { //数据有问题
            return;
        }

        String computingFormula = feeDto.getComputingFormula();
        double feePrice = 0.00;
        if ("1001".equals(computingFormula)) { //面积*单价+附加费
            BigDecimal squarePrice = new BigDecimal(Double.parseDouble(feeDto.getSquarePrice()));
            BigDecimal builtUpArea = new BigDecimal(Double.parseDouble(roomDtos.get(0).getBuiltUpArea()));
            BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
            feePrice = squarePrice.multiply(builtUpArea).add(additionalAmount).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
        } else if ("2002".equals(computingFormula)) { // 固定费用
            BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
            feePrice = additionalAmount.setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
        } else {
            feePrice = -1.00;
        }

        feeDto.setFeePrice(feePrice);
    }
}
