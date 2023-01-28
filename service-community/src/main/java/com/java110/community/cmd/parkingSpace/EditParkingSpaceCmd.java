package com.java110.community.cmd.parkingSpace;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IParkingSpaceV1InnerServiceSMO;
import com.java110.po.parking.ParkingSpacePo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

@Java110Cmd(serviceCode = "parkingSpace.editParkingSpace")
public class EditParkingSpaceCmd extends Cmd {

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceV1InnerServiceSMO parkingSpaceV1InnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "未包含小区ID");
        Assert.jsonObjectHaveKey(reqJson, "psId", "未包含停车位ID");
        Assert.jsonObjectHaveKey(reqJson, "paId", "未包含停车场信息");
        Assert.jsonObjectHaveKey(reqJson, "num", "请求报文中未包含num");
        Assert.jsonObjectHaveKey(reqJson, "area", "请求报文中未包含area");


        Assert.hasLength(reqJson.getString("psId"), "停车位ID不能为空");

        if (reqJson.getString("psId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "停车位ID必须为已有ID");
        }

        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setCommunityId(reqJson.getString("communityId"));
        parkingSpaceDto.setPsId(reqJson.getString("psId"));
        List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);

        if (parkingSpaceDtos == null || parkingSpaceDtos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "未查询到停车位信息" + JSONObject.toJSONString(parkingSpaceDto));
        }

        //不修改 车位类型
        if (!reqJson.containsKey("parkingType")) {
            return;
        }

        // 不修改 车位类型
        if (parkingSpaceDtos.get(0).getParkingType().equals(reqJson.getString("parkingType"))) {
            return;
        }

        if (ParkingSpaceDto.TYPE_CD_SON_MOTHER.equals(reqJson.getString("parkingType"))
                || ParkingSpaceDto.TYPE_CD_SON_MOTHER.equals(parkingSpaceDtos.get(0).getParkingType())
        ) {
            throw  new CmdException("子母车位不能修改为其他车位，其他车位也不能修改为子母车位！");
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setCommunityId(reqJson.getString("communityId"));
        parkingSpaceDto.setPsId(reqJson.getString("psId"));
        List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);

        if (parkingSpaceDtos == null || parkingSpaceDtos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "未查询到停车位信息" + JSONObject.toJSONString(parkingSpaceDto));
        }

        parkingSpaceDto = parkingSpaceDtos.get(0);

        JSONObject businessParkingSpace = new JSONObject();

        businessParkingSpace.putAll(reqJson);
        businessParkingSpace.put("state", parkingSpaceDto.getState());
        ParkingSpacePo parkingSpacePo = BeanConvertUtil.covertBean(businessParkingSpace, ParkingSpacePo.class);
        //parkingSpaceInnerServiceSMOImpl.updateParkingSpace(parkingSpacePo);
        int flag = parkingSpaceV1InnerServiceSMOImpl.updateParkingSpace(parkingSpacePo);

        if (flag < 1) {
            throw new CmdException("修改车位失败");
        }

    }

}
