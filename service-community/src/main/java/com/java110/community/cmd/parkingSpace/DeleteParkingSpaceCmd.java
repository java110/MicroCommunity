package com.java110.community.cmd.parkingSpace;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.DataFlowContext;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.community.IParkingSpaceV1InnerServiceSMO;
import com.java110.po.parking.ParkingSpacePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;

@Java110Cmd(serviceCode = "parkingSpace.deleteParkingSpace")
public class DeleteParkingSpaceCmd extends Cmd {

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceV1InnerServiceSMO parkingSpaceV1InnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        Assert.jsonObjectHaveKey(reqJson, "psId", "请求报文中未包含psId");
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        if (reqJson.containsKey("state") && !StringUtil.isEmpty("state") && reqJson.getString("state").equals("F")) {
            deleteParkingSpace(reqJson);
        } else if (reqJson.containsKey("state") && !StringUtil.isEmpty("state") && reqJson.getString("state").equals("S")) {
            throw new IllegalArgumentException("车位已出售，不能删除！");
        } else if (reqJson.containsKey("state") && !StringUtil.isEmpty("state") && reqJson.getString("state").equals("H")) {
            throw new IllegalArgumentException("车位已出租，不能删除！");
        } else {
            throw new IllegalArgumentException("未知车位！");
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

        int flag =  parkingSpaceV1InnerServiceSMOImpl.deleteParkingSpace(parkingSpacePo);

        if(flag < 1){
            throw new CmdException("删除车位失败");
        }
    }

}
