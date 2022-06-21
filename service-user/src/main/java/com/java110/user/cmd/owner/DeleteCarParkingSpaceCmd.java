package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IParkingSpaceV1InnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerCarV1InnerServiceSMO;
import com.java110.po.car.OwnerCarPo;
import com.java110.po.parking.ParkingSpacePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Java110Cmd(serviceCode = "owner.deleteCarParkingSpace")
public class DeleteCarParkingSpaceCmd extends Cmd {

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;


    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceV1InnerServiceSMO parkingSpaceV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerCarV1InnerServiceSMO ownerCarV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "未包含小区ID");
//        Assert.jsonObjectHaveKey(reqJson, "carId", "请求报文中未包含carId");
        Assert.hasLength(reqJson.getString("communityId"), "小区ID不能为空");

        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCarId(reqJson.getString("carId"));
        ownerCarDto.setCommunityId(reqJson.getString("communityId"));
        ownerCarDto.setStatusCd("0");
        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
        if (ownerCarDtos != null && ownerCarDtos.size() > 1) {
            throw new IllegalArgumentException("有多个车辆绑定此车位，请先删除车辆！");
        }

        String state = ownerCarDtos.get(0).getState();

        if (StringUtil.isEmpty(state) || state.equals(OwnerCarDto.STATE_FINISH)) {
            throw new IllegalArgumentException("车位已经释放无需释放");
        }

//        if (ownerCarDtos.get(0).getEndTime().getTime() > DateUtil.getCurrentDate().getTime()) {
//            throw new IllegalArgumentException("车位租用还未结束不能释放");
//        }
        reqJson.put("ownerCarDto", ownerCarDtos.get(0));
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        OwnerCarDto ownerCarDto = (OwnerCarDto) reqJson.get("ownerCarDto");
        FeeDto feeDto = new FeeDto();
        feeDto.setPayerObjId(reqJson.getString("carId"));
        feeDto.setCommunityId(reqJson.getString("communityId"));
        feeDto.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_CAR);
        feeDto.setState(FeeDto.STATE_FINISH);
        List<FeeDto> feeDtoList = feeInnerServiceSMOImpl.queryFees(feeDto);
        boolean oweFee = false;
        if (feeDtoList == null || feeDtoList.size() < 1) {
            oweFee = true;
        }
        for (FeeDto tmpFeeDto : feeDtoList) {

            if (tmpFeeDto.getEndTime().getTime() <= ownerCarDto.getEndTime().getTime()) {
                oweFee = true;
                break;
            } else {
                throw new IllegalArgumentException("费用未开始！");
            }
        }

        if (OwnerCarDto.STATE_FINISH.equals(ownerCarDto.getState())) {
            oweFee = false;
        }


        OwnerCarPo ownerCarPo = new OwnerCarPo();
//        ownerCarPo.setPsId("-1");
        ownerCarPo.setCarId(reqJson.getString("carId"));
        ownerCarPo.setMemberId(reqJson.getString("memberId"));
        ownerCarPo.setCommunityId(reqJson.getString("communityId"));
        if (oweFee) {
            ownerCarPo.setState(OwnerCarDto.STATE_FINISH);
        }
        int flag = ownerCarV1InnerServiceSMOImpl.updateOwnerCar(ownerCarPo);
        if (flag < 1) {
            throw new IllegalArgumentException("修改车辆出错");
        }
        reqJson.put("carNumType", ParkingSpaceDto.STATE_FREE);
        reqJson.put("psId", ownerCarDto.getPsId());
        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setCommunityId(reqJson.getString("communityId"));
        parkingSpaceDto.setPsId(reqJson.getString("psId"));
        List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);

        if (parkingSpaceDtos == null || parkingSpaceDtos.size() != 1) {
            //throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "未查询到停车位信息" + JSONObject.toJSONString(parkingSpaceDto));
            return;
        }

        parkingSpaceDto = parkingSpaceDtos.get(0);

        JSONObject businessParkingSpace = new JSONObject();

        businessParkingSpace.putAll(BeanConvertUtil.beanCovertMap(parkingSpaceDto));
        businessParkingSpace.put("state", reqJson.getString("carNumType"));
        ParkingSpacePo parkingSpacePo = BeanConvertUtil.covertBean(businessParkingSpace, ParkingSpacePo.class);
        flag = parkingSpaceV1InnerServiceSMOImpl.updateParkingSpace(parkingSpacePo);
        if (flag < 1) {
            throw new IllegalArgumentException("修改车辆出错");
        }
    }
}
