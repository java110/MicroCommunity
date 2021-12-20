package com.java110.job.task.fee;

import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.IComputeFeeSMO;
import com.java110.dto.RoomDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.fee.*;
import com.java110.dto.logSystemError.LogSystemErrorDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.task.TaskDto;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.fee.IFeeDetailInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.job.quartz.TaskSystemQuartz;
import com.java110.po.logSystemError.LogSystemErrorPo;
import com.java110.service.smo.ISaveSystemErrorSMO;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.TaskTemplateException;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ExceptionUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName GenerateOwnerBillTemplate
 * @Description TODO  房屋费用账单生成
 * @Author wuxw
 * @Date 2020/6/4 8:33
 * @Version 1.0
 * add by wuxw 2020/6/4
 **/
@Component
public class GenerateBillProTemplate extends TaskSystemQuartz {
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

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    @Autowired
    private ISaveSystemErrorSMO saveSystemErrorSMOImpl;


    @Override
    protected void process(TaskDto taskDto) throws Exception {

        // 获取小区
        List<CommunityDto> communityDtos = getAllCommunity();

        for (CommunityDto communityDto : communityDtos) {
            try {
                GenerateBill(taskDto, communityDto);
            }catch (Throwable e){

                LogSystemErrorPo logSystemErrorPo = new LogSystemErrorPo();
                logSystemErrorPo.setErrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_errId));
                logSystemErrorPo.setErrType(LogSystemErrorDto.ERR_TYPE_JOB);
                logSystemErrorPo.setMsg(ExceptionUtil.getStackTrace(e));
                saveSystemErrorSMOImpl.saveLog(logSystemErrorPo);
                logger.error("费用出账失败" + communityDto.getCommunityId(), e);
            }
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

        if (StringUtil.isEmpty(feeConfigDto.getBillType())) {
            throw new IllegalArgumentException("配置错误 未拿到属性值");
        }
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);

        for (FeeConfigDto tmpFeeConfigDto : feeConfigDtos) {
            try {
                GenerateBillByFeeConfig(taskDto, tmpFeeConfigDto);
            } catch (Throwable e) {
                LogSystemErrorPo logSystemErrorPo = new LogSystemErrorPo();
                logSystemErrorPo.setErrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_errId));
                logSystemErrorPo.setErrType(LogSystemErrorDto.ERR_TYPE_JOB);
                logSystemErrorPo.setMsg(ExceptionUtil.getStackTrace(e));
                saveSystemErrorSMOImpl.saveLog(logSystemErrorPo);
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
        BillDto tmpBillDto = new BillDto();
        tmpBillDto.setCurBill("T");
        tmpBillDto.setConfigId(feeConfigDto.getConfigId());
        tmpBillDto.setCommunityId(feeConfigDto.getCommunityId());
        Date startTime = getDefaultStartTime(feeConfigDto.getBillType());
        tmpBillDto.setCurBillTime(DateUtil.getFormatTimeString(startTime, DateUtil.DATE_FORMATE_STRING_A));
        List<BillDto> billDtos = feeInnerServiceSMOImpl.queryBills(tmpBillDto);

        //Assert.listOnlyOne(billDtos, "当前存在多个有效账单" + feeConfigDto.getConfigId());
        if (billDtos != null && billDtos.size() > 0) {
            throw new TaskTemplateException(ResponseConstant.RESULT_CODE_ERROR, "已经出过账了");
        }

        String billId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_billId);
        BillDto billDto = new BillDto();
        billDto.setBillId(billId);
        billDto.setBillName(feeConfigDto.getFeeName() + "-" + DateUtil.getFormatTimeString(new Date(), DateUtil.DATE_FORMATE_STRING_H));
        billDto.setBillTime(DateUtil.getFormatTimeString(new Date(), DateUtil.DATE_FORMATE_STRING_B));
        billDto.setCommunityId(feeConfigDto.getCommunityId());
        billDto.setConfigId(feeConfigDto.getConfigId());
        billDto.setCurBill("T");
        //查询历史有效账单
        tmpBillDto = new BillDto();
        tmpBillDto.setCurBill("T");
        tmpBillDto.setConfigId(feeConfigDto.getConfigId());
        tmpBillDto.setCommunityId(feeConfigDto.getCommunityId());
        billDtos = feeInnerServiceSMOImpl.queryBills(tmpBillDto);

        startTime = (billDtos == null || billDtos.size() < 1) ? getDefaultStartTime(feeConfigDto.getBillType())
                : DateUtil.getDateFromString(billDtos.get(0).getBillTime(), DateUtil.DATE_FORMATE_STRING_A);

        FeeDto feeDto = new FeeDto();
        feeDto.setConfigId(feeConfigDto.getConfigId());
        feeDto.setCommunityId(feeConfigDto.getCommunityId());
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

        //没有关联费用不做出账
        if (feeDto == null || feeDtos.size() < 1) {
            return;
        }
        billDto.setReceivable("0");
        billDto.setReceipts("0");
        billDto.setCurReceivable("0");
        for (FeeDto tmpFeeDto : feeDtos) {
            try {
                generateFee(startTime, tmpFeeDto, billDto, feeConfigDto);
            } catch (Throwable e) {
                LogSystemErrorPo logSystemErrorPo = new LogSystemErrorPo();
                logSystemErrorPo.setErrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_errId));
                logSystemErrorPo.setErrType(LogSystemErrorDto.ERR_TYPE_JOB);
                logSystemErrorPo.setMsg(ExceptionUtil.getStackTrace(e));
                saveSystemErrorSMOImpl.saveLog(logSystemErrorPo);
                logger.error("生成费用失败", e);
            }
        }


        //生成本次账单
        Date billEndTime = DateUtil.getCurrentDate();
        billDto.setRemark(DateUtil.getFormatTimeString(startTime, DateUtil.DATE_FORMATE_STRING_A) +
                "-" + DateUtil.getFormatTimeString(billEndTime, DateUtil.DATE_FORMATE_STRING_A) + "账单数据");
        feeInnerServiceSMOImpl.insertBill(billDto);

    }

    /**
     * 生成 费用
     *
     * @param feeDto
     */
    private void generateFee(Date startTime, FeeDto feeDto, BillDto billDto, FeeConfigDto feeConfigDto) {

        if (FeeDto.STATE_FINISH.equals(feeDto.getState())) { //判断是否缴费结束
            return;
        }

        // 当前时间 - 开始时间  = 月份
        // 月份/ 周期 = 轮数（向上取整）
        // 轮数 * 周期 * 30 + 开始时间 = 目标 到期时间
        // 到期时间 >= 目标到期时间===>结束
        // 目标到期时间 - 到期时间 = 欠费月份
        // 月份 * 每月单价 = 欠费金额

        Map<String, Object> targetEndDateAndOweMonth = computeFeeSMOImpl.getTargetEndDateAndOweMonth(feeDto);
        Date targetEndDate = (Date) targetEndDateAndOweMonth.get("targetEndDate");
        double oweMonth = (double) targetEndDateAndOweMonth.get("oweMonth");

        //当前时间
        Date billEndTime = DateUtil.getCurrentDate();

        //缴费周期
        long paymentCycle = Long.parseLong(feeConfigDto.getPaymentCycle());


        //说明没有欠费
        if (oweMonth <= 0.0) {
            return;
        }
        Map feePriceAll = computeFeeSMOImpl.getFeePrice(feeDto);
        feeDto.setFeePrice(Double.parseDouble(feePriceAll.get("feePrice").toString()));
        if (feeDto.getFeePrice() <= 0) {
            return;//这个没有欠费可算
        }

        BillOweFeeDto billOweFeeDto = new BillOweFeeDto();
        billOweFeeDto.setOweId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_oweId));
        billOweFeeDto.setFeeId(feeDto.getFeeId());
        billOweFeeDto.setBillId(billDto.getBillId());
        BigDecimal curFeePrice = new BigDecimal(feeDto.getFeePrice());
        curFeePrice = curFeePrice.multiply(new BigDecimal(paymentCycle));
        billOweFeeDto.setAmountOwed(curFeePrice.doubleValue() + "");
        BigDecimal feePrice = new BigDecimal(feeDto.getFeePrice());
        feePrice = feePrice.multiply(new BigDecimal(oweMonth));

        billOweFeeDto.setBillAmountOwed(feePrice.doubleValue() + "");
        billOweFeeDto.setFeeEndTime(DateUtil.getFormatTimeString(feeDto.getEndTime(), DateUtil.DATE_FORMATE_STRING_A));
        billOweFeeDto.setCommunityId(feeDto.getCommunityId());
        billOweFeeDto.setPayerObjType(feeDto.getPayerObjType());
        billOweFeeDto.setState("1000");
        billOweFeeDto.setDeadlineTime(DateUtil.getFormatTimeString(targetEndDate, DateUtil.DATE_FORMATE_STRING_A));
        if (FeeDto.PAYER_OBJ_TYPE_ROOM.equals(feeDto.getPayerObjType())) {
            getRoomInfo(billOweFeeDto, feeDto);
        } else {
            getParkingSpaceInfo(billOweFeeDto, feeDto);
        }

        feeInnerServiceSMOImpl.insertBillOweFees(billOweFeeDto);

        double recFee = StringUtil.isEmpty(billDto.getReceivable()) ? 0.0 : Double.parseDouble(billDto.getReceivable());

        BigDecimal recFeeBig = new BigDecimal(recFee);
        BigDecimal newRecFee = recFeeBig.add(feePrice);

        //应收
        billDto.setReceivable(newRecFee.doubleValue() + "");

        //当期应收
        double curRecFee = StringUtil.isEmpty(billDto.getCurReceivable()) ? 0.0 : Double.parseDouble(billDto.getCurReceivable());
        BigDecimal curRecFeeBig = new BigDecimal(curRecFee);
        BigDecimal curNewRecFee = curRecFeeBig.add(curFeePrice);
        billDto.setCurReceivable(curNewRecFee.doubleValue() + "");

        FeeDetailDto feeDetailDto = new FeeDetailDto();
        feeDetailDto.setFeeId(feeDto.getFeeId());
        feeDetailDto.setCommunityId(feeDto.getCommunityId());
        feeDetailDto.setStartTime(startTime);
        feeDetailDto.setEndTime(billEndTime);
        List<FeeDetailDto> feeDetailDtos = feeDetailInnerServiceSMOImpl.queryFeeDetails(feeDetailDto);

        double curReceiptFee = StringUtil.isEmpty(billDto.getReceipts()) ? 0.0 : Double.parseDouble(billDto.getReceipts());
        BigDecimal curReceipts = new BigDecimal(curReceiptFee);
        if (feeDetailDtos != null && feeDetailDtos.size() > 0) {
            for (FeeDetailDto tmpFeeDetailDto : feeDetailDtos) {
                BigDecimal recAmount = new BigDecimal(Double.parseDouble(tmpFeeDetailDto.getReceivedAmount()));
                curReceipts = recAmount.add(curReceipts);
            }
        }
        billDto.setReceipts(curReceipts.doubleValue() + "");
    }

    /**
     * 查询车位信息
     *
     * @param billOweFeeDto
     * @param feeDto
     */
    private void getParkingSpaceInfo(BillOweFeeDto billOweFeeDto, FeeDto feeDto) {


        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setWithOwner(true);
        ownerCarDto.setCarId(feeDto.getPayerObjId());
        ownerCarDto.setCommunityId(feeDto.getCommunityId());

        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);


        if (ownerCarDtos == null || ownerCarDtos.size() < 1) {
            //房屋可能被删除了
            billOweFeeDto.setOwnerId("1");
            billOweFeeDto.setOwnerName("未知");
            billOweFeeDto.setOwnerTel("19999999999");
            return;
        }

        billOweFeeDto.setPayerObjName(ownerCarDtos.get(0).getCarNum());
        billOweFeeDto.setOwnerId(ownerCarDtos.get(0).getOwnerId());
        billOweFeeDto.setOwnerName(ownerCarDtos.get(0).getOwnerName());
        billOweFeeDto.setOwnerTel(ownerCarDtos.get(0).getLink());
    }

    /**
     * 查询房屋信息
     *
     * @param billOweFeeDto
     * @param feeDto
     */
    private void getRoomInfo(BillOweFeeDto billOweFeeDto, FeeDto feeDto) {
        RoomDto roomDto = new RoomDto();
        roomDto.setRoomId(feeDto.getPayerObjId());
        roomDto.setCommunityId(feeDto.getCommunityId());
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);

        if (roomDtos == null || roomDtos.size() < 1) {
            //房屋可能被删除了
            billOweFeeDto.setOweId("1");
            billOweFeeDto.setOwnerId("-1");
            billOweFeeDto.setOwnerName("未知");
            billOweFeeDto.setOwnerTel("19999999999");
            billOweFeeDto.setPayerObjName("未知");
            return;
        }

        RoomDto tmpRoomDto = roomDtos.get(0);
        if (RoomDto.ROOM_TYPE_ROOM.equals(tmpRoomDto.getRoomType())) {
            billOweFeeDto.setPayerObjName(tmpRoomDto.getFloorNum() + "栋" + tmpRoomDto.getUnitNum() + "单元" + tmpRoomDto.getRoomNum() + "室");
        } else {
            billOweFeeDto.setPayerObjName(tmpRoomDto.getFloorNum() + "栋" + tmpRoomDto.getRoomNum() + "室");
        }

        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setByOwnerInfo(true);
        ownerRoomRelDto.setRoomId(tmpRoomDto.getRoomId());
        ownerRoomRelDto.setCommunityId(feeDto.getCommunityId());
        List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);

        if (ownerRoomRelDtos == null || ownerRoomRelDtos.size() < 1) {
            //房屋可能被删除了
            billOweFeeDto.setOweId("1");
            billOweFeeDto.setOwnerId("-1");
            billOweFeeDto.setOwnerName("未知");
            billOweFeeDto.setOwnerTel("19999999999");
            return;
        }

        billOweFeeDto.setOwnerId(ownerRoomRelDtos.get(0).getOwnerId());
        billOweFeeDto.setOwnerName(ownerRoomRelDtos.get(0).getOwnerName());
        billOweFeeDto.setOwnerTel(ownerRoomRelDtos.get(0).getLink());

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


}
