package com.java110.api.listener.fee;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.RoomDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.intf.IImportFeeDetailInnerServiceSMO;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.utils.constant.ServiceCodeFeeConfigConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.vo.api.fee.ApiFeeDataVo;
import com.java110.vo.api.fee.ApiFeeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;


/**
 * 查询小区侦听类
 */
@Java110Listener("listFeeListener")
public class ListFeeListener extends AbstractServiceApiListener {

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    private IImportFeeDetailInnerServiceSMO importFeeDetailInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeFeeConfigConstant.LIST_FEE;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IFeeConfigInnerServiceSMO getFeeConfigInnerServiceSMOImpl() {
        return feeConfigInnerServiceSMOImpl;
    }

    public void setFeeConfigInnerServiceSMOImpl(IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl) {
        this.feeConfigInnerServiceSMOImpl = feeConfigInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        FeeDto feeDto = BeanConvertUtil.covertBean(reqJson, FeeDto.class);

        int count = feeInnerServiceSMOImpl.queryFeesCount(feeDto);

        List<ApiFeeDataVo> fees = null;

        if (count > 0) {
            List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
            computeFeePrice(feeDtos);
            fees = BeanConvertUtil.covertBeanList(feeDtos, ApiFeeDataVo.class);


        } else {
            fees = new ArrayList<>();
        }

        ApiFeeVo apiFeeVo = new ApiFeeVo();

        apiFeeVo.setTotal(count);
        apiFeeVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiFeeVo.setFees(fees);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiFeeVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }

    private void computeFeePrice(List<FeeDto> feeDtos) {

        for (FeeDto feeDto : feeDtos) {

            // 轮数 * 周期 * 30 + 开始时间 = 目标 到期时间
            Map<String, Object> targetEndDateAndOweMonth = getTargetEndDateAndOweMonth(feeDto);
            Date targetEndDate = (Date) targetEndDateAndOweMonth.get("targetEndDate");
            double oweMonth = (double) targetEndDateAndOweMonth.get("oweMonth");
            //一次性费用
            if ("3333".equals(feeDto.getPayerObjType())) { //房屋相关
                computeFeePriceByRoom(feeDto, oweMonth);
            } else if ("6666".equals(feeDto.getPayerObjType())) {//车位相关
                computeFeePriceByParkingSpace(feeDto, oweMonth);
            }

            feeDto.setDeadlineTime(targetEndDate);
        }
    }

    private Map getTargetEndDateAndOweMonth(FeeDto feeDto) {
        Date targetEndDate = null;
        double oweMonth = 0.0;

        Map<String, Object> targetEndDateAndOweMonth = new HashMap<>();

        if (FeeDto.STATE_FINISH.equals(feeDto.getState())) {
            targetEndDate = feeDto.getEndTime();
            targetEndDateAndOweMonth.put("oweMonth", oweMonth);
            targetEndDateAndOweMonth.put("targetEndDate", targetEndDate);
            return targetEndDateAndOweMonth;
        }
        if (FeeDto.FEE_FLAG_ONCE.equals(feeDto.getFeeFlag())) {
            if (feeDto.getImportFeeEndTime() == null) {
                targetEndDate = feeDto.getConfigEndTime();
            } else {
                targetEndDate = feeDto.getImportFeeEndTime();
            }
            //判断当前费用是不是导入费用
            oweMonth = 1.0;

        } else {
            //当前时间
            Date billEndTime = DateUtil.getCurrentDate();
            //开始时间
            Date startDate = feeDto.getStartTime();
            //到期时间
            Date endDate = feeDto.getEndTime();
            //缴费周期
            long paymentCycle = Long.parseLong(feeDto.getPaymentCycle());
            // 当前时间 - 开始时间  = 月份
            double mulMonth = dayCompare(startDate, billEndTime);
            // 月份/ 周期 = 轮数（向上取整）
            double round = 0.0;
            if ("1200".equals(feeDto.getPaymentCd())) { // 预付费
                round = Math.floor(mulMonth / paymentCycle) + 1;
            } else { //后付费
                round = Math.floor(mulMonth / paymentCycle);
            }
            // 轮数 * 周期 * 30 + 开始时间 = 目标 到期时间
            targetEndDate = getTargetEndTime(round * paymentCycle, startDate);
            //费用 快结束了
            if (feeDto.getConfigEndTime().getTime() < targetEndDate.getTime()) {
                targetEndDate = feeDto.getConfigEndTime();
            }
            //说明没有欠费
            if (endDate.getTime() < targetEndDate.getTime()) {
                // 目标到期时间 - 到期时间 = 欠费月份
                oweMonth = dayCompare(endDate, targetEndDate);
            }
        }

        targetEndDateAndOweMonth.put("oweMonth", oweMonth);
        targetEndDateAndOweMonth.put("targetEndDate", targetEndDate);
        return targetEndDateAndOweMonth;
    }

    private void computeFeePriceByParkingSpace(FeeDto feeDto, double oweMonth) {

        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setCommunityId(feeDto.getCommunityId());
        parkingSpaceDto.setPsId(feeDto.getPayerObjId());
        List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);

        if (parkingSpaceDtos == null || parkingSpaceDtos.size() < 1) { //数据有问题
            return;
        }

        String computingFormula = feeDto.getComputingFormula();
        double feePrice = 0.00;
        if ("1001".equals(computingFormula)) { //面积*单价+附加费
            BigDecimal squarePrice = new BigDecimal(Double.parseDouble(feeDto.getSquarePrice()));
            BigDecimal builtUpArea = new BigDecimal(Double.parseDouble(parkingSpaceDtos.get(0).getArea()));
            BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
            feePrice = squarePrice.multiply(builtUpArea).add(additionalAmount).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
        } else if ("2002".equals(computingFormula)) { // 固定费用

            BigDecimal additionalAmount = new BigDecimal(Double.parseDouble(feeDto.getAdditionalAmount()));
            feePrice = additionalAmount.setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
        } else if ("4004".equals(computingFormula)) {
            feePrice = Double.parseDouble(feeDto.getAmount());
        } else {
            feePrice = -1.00;
        }

        feeDto.setFeePrice(feePrice);

        BigDecimal curFeePrice = new BigDecimal(feeDto.getFeePrice());
        curFeePrice = curFeePrice.multiply(new BigDecimal(oweMonth));
        feeDto.setAmountOwed(curFeePrice.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "");


    }

    /**
     * 根据房屋来算单价
     *
     * @param feeDto
     */
    private void computeFeePriceByRoom(FeeDto feeDto, double oweMonth) {
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
        } else if ("4004".equals(computingFormula)) {
            feePrice = Double.parseDouble(feeDto.getAmount());
        } else {
            feePrice = -1.00;
        }

        feeDto.setFeePrice(feePrice);
        BigDecimal curFeePrice = new BigDecimal(feeDto.getFeePrice());
        curFeePrice = curFeePrice.multiply(new BigDecimal(oweMonth));
        feeDto.setAmountOwed(curFeePrice.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "");
    }

    /**
     * 计算2个日期之间相差的  以年、月、日为单位，各自计算结果是多少
     * 比如：2011-02-02 到  2017-03-02
     * 以年为单位相差为：6年
     * 以月为单位相差为：73个月
     * 以日为单位相差为：2220天
     *
     * @param fromDate
     * @param toDate
     * @return
     */
    public static double dayCompare(Date fromDate, Date toDate) {
        Calendar from = Calendar.getInstance();
        from.setTime(fromDate);
        Calendar to = Calendar.getInstance();
        to.setTime(toDate);
        int result = to.get(Calendar.MONTH) - from.get(Calendar.MONTH);
        int month = (to.get(Calendar.YEAR) - from.get(Calendar.YEAR)) * 12;

        result = result + month;
        Calendar newFrom = Calendar.getInstance();
        newFrom.setTime(fromDate);
        newFrom.add(Calendar.MONTH, result);

        long t1 = newFrom.getTimeInMillis();
        long t2 = to.getTimeInMillis();
        long days = (t2 - t1) / (24 * 60 * 60 * 1000);

        BigDecimal tmpDays = new BigDecimal(days);
        BigDecimal monthDay = new BigDecimal(30);

        return tmpDays.divide(monthDay, 2, RoundingMode.HALF_UP).doubleValue() + result;
    }

    private Date getTargetEndTime(double v, Date startDate) {
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(startDate);
        endDate.add(Calendar.MONTH, (int) v);
        return endDate.getTime();
    }
}
