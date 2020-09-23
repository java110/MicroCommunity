package com.java110.api.listener.fee;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.core.smo.IComputeFeeSMO;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


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
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;


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
            freshFeeAttrs(fees, feeDtos);


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

    private void freshFeeAttrs(List<ApiFeeDataVo> fees, List<FeeDto> feeDtos) {

        for (ApiFeeDataVo apiFeeDataVo : fees) {
            for (FeeDto feeDto : feeDtos) {
                if (apiFeeDataVo.getFeeId().equals(feeDto.getFeeId())) {
                    apiFeeDataVo.setFeeAttrs(feeDto.getFeeAttrDtos());
                }
            }
        }
    }

    private void computeFeePrice(List<FeeDto> feeDtos) {

        for (FeeDto feeDto : feeDtos) {

            // 轮数 * 周期 * 30 + 开始时间 = 目标 到期时间
            Map<String, Object> targetEndDateAndOweMonth = computeFeeSMOImpl.getTargetEndDateAndOweMonth(feeDto);
            Date targetEndDate = (Date) targetEndDateAndOweMonth.get("targetEndDate");
            double oweMonth = (double) targetEndDateAndOweMonth.get("oweMonth");
            //一次性费用
            if (FeeDto.PAYER_OBJ_TYPE_ROOM.equals(feeDto.getPayerObjType())) { //房屋相关
                computeFeePriceByRoom(feeDto, oweMonth);
            } else if (FeeDto.PAYER_OBJ_TYPE_CAR.equals(feeDto.getPayerObjType())) {//车位相关
                computeFeePriceByCar(feeDto, oweMonth);
            }

            feeDto.setDeadlineTime(targetEndDate);
        }
    }


    private void computeFeePriceByCar(FeeDto feeDto, double oweMonth) {
        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCommunityId(feeDto.getCommunityId());
        ownerCarDto.setCarId(feeDto.getPayerObjId());
        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
        if (ownerCarDtos == null || ownerCarDtos.size() < 1) { //数据有问题
            return;
        }
        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setCommunityId(feeDto.getCommunityId());
        parkingSpaceDto.setPsId(ownerCarDtos.get(0).getPsId());
        List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);
        if (parkingSpaceDtos == null || parkingSpaceDtos.size() < 1) { //数据有问题
            return;
        }
        String computingFormula = feeDto.getComputingFormula();
        double feePrice = 0.00;
        feePrice = computeFeeSMOImpl.getFeePrice(feeDto);
        feeDto.setFeePrice(feePrice);
        BigDecimal curFeePrice = new BigDecimal(feeDto.getFeePrice());
        curFeePrice = curFeePrice.multiply(new BigDecimal(oweMonth));
        feeDto.setAmountOwed(curFeePrice.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "");
        //动态费用
        if ("4004".equals(computingFormula)) {
            feeDto.setAmountOwed(feeDto.getFeePrice() + "");
            feeDto.setDeadlineTime(DateUtil.getCurrentDate());
        }
    }

    /**
     * 根据房屋来算单价
     *
     * @param feeDto
     */
    private void computeFeePriceByRoom(FeeDto feeDto, double oweMonth) {
        String computingFormula = feeDto.getComputingFormula();
        double feePrice = 0.00;
        feePrice = computeFeeSMOImpl.getFeePrice(feeDto);
        feeDto.setFeePrice(feePrice);
        BigDecimal curFeePrice = new BigDecimal(feeDto.getFeePrice());
        curFeePrice = curFeePrice.multiply(new BigDecimal(oweMonth));
        feeDto.setAmountOwed(curFeePrice.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "");
        //动态费用
        if ("4004".equals(computingFormula)) {
            feeDto.setAmountOwed(feeDto.getFeePrice() + "");
            feeDto.setDeadlineTime(DateUtil.getCurrentDate());
        }
    }

}
