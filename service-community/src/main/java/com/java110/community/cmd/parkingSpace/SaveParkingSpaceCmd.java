package com.java110.community.cmd.parkingSpace;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.intf.community.IParkingSpaceV1InnerServiceSMO;
import com.java110.po.parking.ParkingSpacePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Java110Cmd(serviceCode = "parkingSpace.saveParkingSpace")
public class SaveParkingSpaceCmd extends Cmd {

    @Autowired
    private IParkingSpaceV1InnerServiceSMO parkingSpaceV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "未包含小区ID");
        Assert.jsonObjectHaveKey(reqJson, "num", "请求报文中未包含age");
        Assert.jsonObjectHaveKey(reqJson, "area", "请求报文中未包含name");
        Assert.jsonObjectHaveKey(reqJson, "userId", "请求报文中未包含userId");
        Assert.jsonObjectHaveKey(reqJson, "paId", "请求报文中未包含停车场信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        JSONObject businessParkingSpace = new JSONObject();
        businessParkingSpace.putAll(reqJson);
        businessParkingSpace.put("state", "F");
        businessParkingSpace.put("psId", GenerateCodeFactory.getPsId(GenerateCodeFactory.CODE_PREFIX_psId));
        businessParkingSpace.put("bId", "-1");
        businessParkingSpace.put("createTime", new Date());
        ParkingSpacePo parkingSpacePo = BeanConvertUtil.covertBean(businessParkingSpace, ParkingSpacePo.class);

        int flag = parkingSpaceV1InnerServiceSMOImpl.saveParkingSpace(parkingSpacePo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        if(!ParkingSpaceDto.TYPE_CD_SON_MOTHER.equals(parkingSpacePo.getParkingType())){
            return ;
        }

        //如果是子母车位 创建子车位
        parkingSpacePo.setPsId(GenerateCodeFactory.getPsId(GenerateCodeFactory.CODE_PREFIX_psId));
        parkingSpacePo.setNum(parkingSpacePo.getNum()+ParkingSpaceDto.NUM_MOTHER);
        flag = parkingSpaceV1InnerServiceSMOImpl.saveParkingSpace(parkingSpacePo);
        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

    }
}
