package com.java110.api.listener.fee;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.fee.IFeeConfigInnerServiceSMO;
import com.java110.core.smo.fee.IFeeInnerServiceSMO;
import com.java110.core.smo.community.IParkingSpaceInnerServiceSMO;
import com.java110.core.smo.community.IRoomInnerServiceSMO;
import com.java110.dto.RoomDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeFeeConfigConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.fee.ApiFeeDataVo;
import com.java110.vo.api.fee.ApiFeeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


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
            if ("3333".equals(feeDto.getPayerObjType())) { //房屋相关
                computeFeePriceByRoom(feeDto);
            } else if ("6666".equals(feeDto.getPayerObjType())) {//车位相关
                computeFeePriceByParkingSpace(feeDto);
            }
        }
    }

    private void computeFeePriceByParkingSpace(FeeDto feeDto) {

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
        } else {
            feePrice = -1.00;
        }

        feeDto.setFeePrice(feePrice);


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
