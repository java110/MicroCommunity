package com.java110.community.cmd.parkingSpace;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.intf.community.IParkingSpaceV1InnerServiceSMO;
import com.java110.po.parking.ParkingSpacePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "parkingSpace.deleteParkingSpace")
public class DeleteParkingSpaceCmd extends Cmd {

    @Autowired
    private IParkingSpaceV1InnerServiceSMO parkingSpaceV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "psId", "请求报文中未包含psId");
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId");
        if (!ParkingSpaceDto.STATE_FREE.equals(reqJson.getString("state"))) {
            throw new CmdException("车位不是空闲，不能做删除");
        }
        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setPsId(reqJson.getString("psId"));
        parkingSpaceDto.setTypeCd(ParkingSpaceDto.TYPE_CD_SON_MOTHER);
        parkingSpaceDto.setParkingType("2"); //1：普通车位  2：子母车位  3：豪华车位
        parkingSpaceDto.setCommunityId(reqJson.getString("communityId"));
        List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceV1InnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);
        //不是子母车位
        if (parkingSpaceDtos == null || parkingSpaceDtos.size() < 1) {
            return;
        }
        List<String> nums = new ArrayList<>();
        nums.add(parkingSpaceDtos.get(0).getNum());
        if (parkingSpaceDtos.get(0).getNum().endsWith(ParkingSpaceDto.NUM_MOTHER)) {
            nums.add(parkingSpaceDtos.get(0).getNum().replace(ParkingSpaceDto.NUM_MOTHER, ""));
        } else {
            nums.add(parkingSpaceDtos.get(0).getNum() + ParkingSpaceDto.NUM_MOTHER);
        }
        parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setNums(nums.toArray(new String[nums.size()]));
        parkingSpaceDto.setCommunityId(reqJson.getString("communityId"));
        parkingSpaceDto.setStates(new String[]{ParkingSpaceDto.STATE_HIRE, ParkingSpaceDto.STATE_SELL});
        parkingSpaceDto.setParkingType("2"); //1：普通车位  2：子母车位  3：豪华车位
        int flag = parkingSpaceV1InnerServiceSMOImpl.queryParkingSpacesCount(parkingSpaceDto);
        if (flag > 0) {
            throw new CmdException("子母车位非空闲");
        }
        reqJson.put("sonMotherNums", nums);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        //非 子母车位
        if (!reqJson.containsKey("sonMotherNums")) {
            deleteParkingSpace(reqJson);
            return;
        }
        List<String> nums = reqJson.getObject("sonMotherNums", List.class);
        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setNums(nums.toArray(new String[nums.size()]));
        parkingSpaceDto.setCommunityId(reqJson.getString("communityId"));
        List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceV1InnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);
        JSONObject paramIn = null;
        for (ParkingSpaceDto tmpParkingSpaceDto : parkingSpaceDtos) {
            paramIn = new JSONObject();
            paramIn.put("psId", tmpParkingSpaceDto.getPsId());
            deleteParkingSpace(reqJson);
        }
    }

    /**
     * 添加小区楼信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void deleteParkingSpace(JSONObject paramInJson) {
        JSONObject businessParkingSpace = new JSONObject();
        businessParkingSpace.put("psId", paramInJson.getString("psId"));
        ParkingSpacePo parkingSpacePo = BeanConvertUtil.covertBean(businessParkingSpace, ParkingSpacePo.class);
        int flag = parkingSpaceV1InnerServiceSMOImpl.deleteParkingSpace(parkingSpacePo);

        if (flag < 1) {
            throw new CmdException("删除车位失败");
        }
    }
}
