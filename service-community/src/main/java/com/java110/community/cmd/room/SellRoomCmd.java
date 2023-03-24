package com.java110.community.cmd.room;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.doc.annotation.*;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.community.IRoomV1InnerServiceSMO;
import com.java110.intf.community.IUnitInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelV1InnerServiceSMO;
import com.java110.po.owner.OwnerRoomRelPo;
import com.java110.po.room.RoomPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
@Java110CmdDoc(title = "业主房屋关系绑定",
        description = "对应后台 业主入驻房屋功能",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/room.sellRoom",
        resource = "communityDoc",
        author = "吴学文",
        serviceCode = "room.sellRoom",
        seq = 18
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "communityId", length = 30, remark = "小区ID"),
        @Java110ParamDoc(name = "roomId", length = 30, remark = "房屋ID"),
        @Java110ParamDoc(name = "state", length = 12, remark = "状态 2001\t已入住\t\n" +
                "2003\t已交房\t\n" +
                "2005\t已装修\t\n" +
                "2004\t未入住\t\n" +
                "2008\t空闲\n" +
                "2009\t装修中\t"),
        @Java110ParamDoc(name = "ownerId", length = 30, remark = "业主ID"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
        }
)

@Java110ExampleDoc(
        reqBody="{\n" +
                "\t\"ownerId\": 121231,\n" +
                "\t\"state\": \"2001\",\n" +
                "\t\"roomId\": \"123123\",\n" +
                "\t\"communityId\": \"2022121921870161\"\n" +
                "}",
        resBody="{\"code\":0,\"msg\":\"成功\"}"
)
@Java110Cmd(serviceCode = "room.sellRoom")
public class SellRoomCmd extends Cmd {

    @Autowired
    private IUnitInnerServiceSMO unitInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelV1InnerServiceSMO ownerRoomRelV1InnerServiceSMOImpl;

    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(reqJson, "ownerId", "请求报文中未包含ownerId节点");
        Assert.jsonObjectHaveKey(reqJson, "roomId", "请求报文中未包含roomId节点");
        Assert.jsonObjectHaveKey(reqJson, "state", "请求报文中未包含state节点");
      //  Assert.jsonObjectHaveKey(reqJson, "storeId", "请求报文中未包含storeId节点");

        Assert.hasLength(reqJson.getString("communityId"), "小区ID不能为空");
        Assert.hasLength(reqJson.getString("ownerId"), "ownerId不能为空");
        Assert.hasLength(reqJson.getString("roomId"), "roomId不能为空");
        Assert.hasLength(reqJson.getString("state"), "state不能为空");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        if (!reqJson.containsKey("startTime")) {
            reqJson.put("startTime", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        }
        if (!reqJson.containsKey("endTime")) {
            reqJson.put("endTime", "2037-01-01 00:00:00");
        }
        //添加单元信息
        sellRoom(reqJson);

        //更新房屋信息为售出
        updateShellRoom(reqJson);
    }

    /**
     * 售卖房屋信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void sellRoom(JSONObject paramInJson) {

        JSONObject businessUnit = new JSONObject();
        businessUnit.putAll(paramInJson);
        businessUnit.put("relId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_relId));
        businessUnit.put("userId", "-1");
        OwnerRoomRelPo ownerRoomRelPo = BeanConvertUtil.covertBean(businessUnit, OwnerRoomRelPo.class);
        int flag = ownerRoomRelV1InnerServiceSMOImpl.saveOwnerRoomRel(ownerRoomRelPo);

        if (flag < 1) {
            throw new CmdException("添加业主房屋关系");
        }

    }

    /**
     * 修改小区楼信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void updateShellRoom(JSONObject paramInJson) {
        JSONObject businessUnit = new JSONObject();
        businessUnit.putAll(paramInJson);
        businessUnit.put("userId", "-1");
        RoomPo roomPo = BeanConvertUtil.covertBean(businessUnit, RoomPo.class);

        int flag = roomV1InnerServiceSMOImpl.updateRoom(roomPo);
        if (flag < 1) {
            throw new CmdException("添加业主房屋关系");
        }
    }
}
