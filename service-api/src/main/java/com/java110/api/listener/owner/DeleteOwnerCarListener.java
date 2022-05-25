package com.java110.api.listener.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.parkingSpace.IParkingSpaceBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.po.car.OwnerCarPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.util.List;

/**
 * 保存小区侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("deleteOwnerCarListener")
public class DeleteOwnerCarListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceBMO parkingSpaceBMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "carId", "carId不能为空");
        Assert.hasKeyAndValue(reqJson, "memberId", "memberId不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        FeeDto feeDto = new FeeDto();
        feeDto.setPayerObjId(reqJson.getString("carId"));
        feeDto.setCommunityId(reqJson.getString("communityId"));
        feeDto.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_CAR);
        List<FeeDto> feeDtoList = feeInnerServiceSMOImpl.queryFees(feeDto);

        for (FeeDto tmpFeeDto : feeDtoList) {
            if (!FeeDto.STATE_FINISH.equals(tmpFeeDto.getState())) {
                throw new IllegalArgumentException("存在 未结束费用 不能删除");
            }
        }

        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setMemberId(reqJson.getString("memberId"));
        ownerCarDto.setCommunityId(reqJson.getString("communityId"));

        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);

        Assert.listOnlyOne(ownerCarDtos, "当前未找到需要删除车辆");
        reqJson.put("psId", ownerCarDtos.get(0).getPsId());

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        if (reqJson.containsKey("psId") && !StringUtil.isEmpty(reqJson.getString("psId"))) {
            ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
            parkingSpaceDto.setPsId(reqJson.getString("psId"));
            List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);
            Assert.listOnlyOne(parkingSpaceDtos, "查询车位错误！");
            if (!StringUtil.isEmpty(parkingSpaceDtos.get(0).getParkingType()) && parkingSpaceDtos.get(0).getParkingType().equals("2")
                    && !StringUtil.isEmpty(reqJson.getString("carTypeCd")) && reqJson.getString("carTypeCd").equals("1001")) { //子母车位
                OwnerCarDto ownerCarDto = new OwnerCarDto();
                ownerCarDto.setCarId(reqJson.getString("carId"));
                ownerCarDto.setPsId(reqJson.getString("psId"));
                ownerCarDto.setOwnerId(reqJson.getString("ownerId"));
                ownerCarDto.setCarTypeCd("1002"); //成员车辆
                List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
                if (ownerCarDtos != null && ownerCarDtos.size() > 0) {
                    throw new IllegalArgumentException("该车位下含有子车辆，请先删除子车辆后再进行操作！");
                }
            }
        }
        OwnerCarPo ownerCarPo = new OwnerCarPo();
        ownerCarPo.setCommunityId(reqJson.getString("communityId"));
        ownerCarPo.setCarId(reqJson.getString("carId"));
        ownerCarPo.setMemberId(reqJson.getString("memberId"));
        super.delete(context, ownerCarPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_OWNER_CAR);

        if (StringUtil.isEmpty(reqJson.getString("psId")) || "-1".equals(reqJson.getString("psId"))) {
            return;
        }
        //释放车位
        if (reqJson.getString("carId").equals(reqJson.getString("memberId"))) {
            reqJson.put("carNumType", ParkingSpaceDto.STATE_FREE);//修改为空闲
            parkingSpaceBMOImpl.modifySellParkingSpaceState(reqJson, context);
        }
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_DELETE_OWNER_CAR;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

}
