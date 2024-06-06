package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.IotDataDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.job.IIotInnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IOwnerCarV1InnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.po.car.OwnerCarPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

/**
 * 业主自己修改车牌号
 */
@Java110Cmd(serviceCode = "owner.appEditSelfCarNum")
public class AppEditSelfCarNumCmd extends Cmd {

    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IIotInnerServiceSMO iotInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarV1InnerServiceSMO ownerCarV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "carId", "未包含carId");
        Assert.hasKeyAndValue(reqJson, "carNum", "未包含carNum");
        Assert.hasKeyAndValue(reqJson, "newCarNum", "未包含newCarNum");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含communityId");

        String userId = context.getReqHeaders().get("user-id");

        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setUserId(userId);
        ownerAppUserDto.setCommunityId(reqJson.getString("communityId"));
        ownerAppUserDto.setMemberId(reqJson.getString("memberId"));
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserInnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);

        if (ListUtil.isNull(ownerAppUserDtos)) {
            throw new CmdException("未绑定业主");
        }

        String memberId = "";
        for (OwnerAppUserDto tmpOwnerAppUserDto : ownerAppUserDtos) {
            if ("-1".equals(tmpOwnerAppUserDto.getMemberId())) {
                continue;
            }
            memberId = tmpOwnerAppUserDto.getMemberId();
        }

        if (StringUtil.isEmpty(memberId)) {
            throw new CmdException("未绑定业主");
        }

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(reqJson.getString("communityId"));
        ownerDto.setMemberId(memberId);
        List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);

        Assert.listOnlyOne(ownerDtos, "业主不存在");

        reqJson.put("ownerId", ownerDtos.get(0).getOwnerId());
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCarId(reqJson.getString("carId"));
        ownerCarDto.setOwnerId(reqJson.getString("ownerId"));
        ownerCarDto.setCarNum(reqJson.getString("carNum"));

        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);

        if (ListUtil.isNull(ownerCarDtos)) {
            throw new CmdException("车辆不存在");
        }

        //todo 校验车辆是否场外
        if (hasInParkingArea(ownerCarDtos.get(0).getCarNum(), ownerCarDtos.get(0).getAreaNum(), ownerCarDtos.get(0).getCommunityId())) {
            throw new CmdException(ownerCarDtos.get(0).getCarNum() + "车在场，请先出场");
        }
        //todo 校验新车辆是否场外
        if (hasInParkingArea(reqJson.getString("newCarNum"), ownerCarDtos.get(0).getAreaNum(), ownerCarDtos.get(0).getCommunityId())) {
            throw new CmdException(ownerCarDtos.get(0).getCarNum() + "车在场，请先出场");
        }

        OwnerCarPo ownerCarPo = new OwnerCarPo();
        ownerCarPo.setMemberId(ownerCarDtos.get(0).getMemberId());
        ownerCarPo.setCarId(ownerCarDtos.get(0).getCarId());
        ownerCarPo.setCarNum(reqJson.getString("newCarNum"));
        ownerCarPo.setCommunityId(reqJson.getString("communityId"));


        ownerCarV1InnerServiceSMOImpl.updateOwnerCar(ownerCarPo);

    }

    private boolean hasInParkingArea(String carNum, String areaNum, String communityId) {

        JSONObject paramIn = new JSONObject();
        paramIn.put("communityId", communityId);
        paramIn.put("page", 1);
        paramIn.put("row", 1);
        paramIn.put("paNum", areaNum);

        ResultVo resultVo = iotInnerServiceSMOImpl.postIotData(new IotDataDto("listCarInParkingAreaBmoImpl", paramIn));

        if (resultVo.getCode() != ResultVo.CODE_OK) {
            return true;
        }

        JSONArray data = (JSONArray) resultVo.getData();

        if (ListUtil.isNull(data)) {
            return false;
        }

        return true;
    }
}
