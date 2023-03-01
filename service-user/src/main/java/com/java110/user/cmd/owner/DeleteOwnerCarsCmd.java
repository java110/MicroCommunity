package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
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

@Java110Cmd(serviceCode = "owner.deleteOwnerCars")
public class DeleteOwnerCarsCmd extends Cmd {

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceV1InnerServiceSMO parkingSpaceV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerCarV1InnerServiceSMO ownerCarV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");

        Assert.hasKeyAndValue(reqJson, "carId", "carId不能为空");
        Assert.hasKeyAndValue(reqJson, "memberId", "memberId不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        FeeDto feeDto = new FeeDto();
        feeDto.setPayerObjId(reqJson.getString("memberId"));
        feeDto.setCommunityId(reqJson.getString("communityId"));
        feeDto.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_CAR);
        List<FeeDto> feeDtoList = feeInnerServiceSMOImpl.queryFees(feeDto);

        for (FeeDto tmpFeeDto : feeDtoList) {
            if (!FeeDto.STATE_FINISH.equals(tmpFeeDto.getState())) {
                throw new IllegalArgumentException("存在 未结束费用 不能删除");
            }
        }

        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCarId(reqJson.getString("carId"));
        ownerCarDto.setMemberId(reqJson.getString("memberId"));
        ownerCarDto.setCommunityId(reqJson.getString("communityId"));

        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);

        Assert.listOnlyOne(ownerCarDtos, "当前未找到需要删除车辆");
        reqJson.put("psId", ownerCarDtos.get(0).getPsId());
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        if (reqJson.containsKey("psId") && !StringUtil.isEmpty(reqJson.getString("psId"))) {
            ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
            parkingSpaceDto.setPsId(reqJson.getString("psId"));
            List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);
            if (parkingSpaceDtos != null && parkingSpaceDtos.size()> 0 &&
                    "2".equals(parkingSpaceDtos.get(0).getParkingType())
                    && "1001".equals(reqJson.getString("carTypeCd"))) { //子母车位
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
        int flag = ownerCarV1InnerServiceSMOImpl.deleteOwnerCar(ownerCarPo);
        if (flag < 1) {
            throw new IllegalArgumentException("删除车辆出错");
        }
        if (StringUtil.isEmpty(reqJson.getString("psId")) || "-1".equals(reqJson.getString("psId"))) {
            return;
        }
        //释放车位
        if (reqJson.getString("carId").equals(reqJson.getString("memberId"))) {
            reqJson.put("carNumType", ParkingSpaceDto.STATE_FREE);//修改为空闲
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
}
