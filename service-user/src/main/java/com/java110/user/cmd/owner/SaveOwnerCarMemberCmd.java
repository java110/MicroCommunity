package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerCarV1InnerServiceSMO;
import com.java110.po.car.OwnerCarPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Java110Cmd(serviceCode = "owner.saveOwnerCarMember")
public class SaveOwnerCarMemberCmd extends Cmd {

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarV1InnerServiceSMO ownerCarV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "未包含小区ID");
        Assert.jsonObjectHaveKey(reqJson, "carId", "请求报文中未包含carId");
        Assert.jsonObjectHaveKey(reqJson, "carNum", "请求报文中未包含carNum");
        Assert.jsonObjectHaveKey(reqJson, "carBrand", "请求报文中未包含carBrand");
        Assert.jsonObjectHaveKey(reqJson, "carType", "请求报文中未包含carType");
        Assert.jsonObjectHaveKey(reqJson, "carColor", "未包含carColor");

        //校验车牌号是否存在
        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCommunityId(reqJson.getString("communityId"));
        ownerCarDto.setCarNum(reqJson.getString("carNum"));
        ownerCarDto.setCarTypeCds(new String[]{OwnerCarDto.CAR_TYPE_PRIMARY, OwnerCarDto.CAR_TYPE_MEMBER}); // 临时车除外
        int count = ownerCarInnerServiceSMOImpl.queryOwnerCarsCount(ownerCarDto);

        if (count > 0) {
            throw new IllegalArgumentException("车辆已存在");
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        //校验车牌号是否存在
        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCommunityId(reqJson.getString("communityId"));
        ownerCarDto.setCarTypeCd("1001"); //业主车辆
        ownerCarDto.setCarId(reqJson.getString("carId"));
        ownerCarDto.setStatusCd("0");
        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
        if (ownerCarDtos.isEmpty() || ownerCarDtos.size() == 0) {
            throw new IllegalArgumentException("主车辆不存在");
        }

        JSONObject tmpOwnerCar = JSONObject.parseObject(JSONObject.toJSONString(ownerCarDtos.get(0)));
        tmpOwnerCar.putAll(reqJson);
        tmpOwnerCar.put("startTime", DateUtil.getFormatTimeString(ownerCarDtos.get(0).getStartTime(), DateUtil.DATE_FORMATE_STRING_A));
        tmpOwnerCar.put("endTime", DateUtil.getFormatTimeString(ownerCarDtos.get(0).getEndTime(), DateUtil.DATE_FORMATE_STRING_A));
        tmpOwnerCar.put("memberId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_carId));

        OwnerCarPo ownerCarPo = BeanConvertUtil.covertBean(tmpOwnerCar, OwnerCarPo.class);
        ownerCarPo.setState(OwnerCarDto.STATE_NORMAL);
        ownerCarPo.setCarTypeCd(OwnerCarDto.CAR_TYPE_MEMBER);
        int flag = ownerCarV1InnerServiceSMOImpl.saveOwnerCar(ownerCarPo);
        if (flag < 1) {
            throw new CmdException("保存车辆属性失败");
        }
    }
}
