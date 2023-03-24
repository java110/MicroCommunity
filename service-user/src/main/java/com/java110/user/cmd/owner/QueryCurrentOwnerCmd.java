package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.doc.annotation.*;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.owner.OwnerAttrDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.user.IOwnerAppUserInnerServiceSMO;
import com.java110.intf.user.IOwnerAttrInnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

@Java110CmdDoc(title = "查询登录业主信息",
        description = "主要用业主手机端查询业主信息",
        httpMethod = "get",
        url = "http://{ip}:{port}/app/owner.queryCurrentOwner",
        resource = "userDoc",
        author = "吴学文",
        serviceCode = "room.queryRooms",
        seq = 13
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "communityId", length = 30, remark = "小区ID"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "records", type = "int", length = 11, remark = "总页数"),
                @Java110ParamDoc(name = "total", type = "int", length = 11, remark = "总数据"),
                @Java110ParamDoc(name = "data", type = "Object", remark = "有效数据"),
                @Java110ParamDoc(parentNodeName = "data", name = "ownerId", type = "String", remark = "业主ID"),
                @Java110ParamDoc(parentNodeName = "data", name = "ownerName", type = "String", remark = "业主名称"),
        }
)

@Java110ExampleDoc(
        reqBody = "http://{ip}:{port}/app/owner.queryCurrentOwner?communityId=2022081539020475",
        resBody = "{\"page\":0,\"records\":1,\"data\":{\"apartment\":\"10101\",\"apartmentName\":\"一室一厅\",\"builtUpArea\":\"11.00\",\"endTime\":\"2037-01-01 00:00:00\",\"feeCoefficient\":\"1.00\",\"floorId\":\"732022081690440002\",\"floorNum\":\"D\",\"idCard\":\"\",\"layer\":\"1\",\"link\":\"18909711447\",\"ownerId\":\"772022082070860017\",\"ownerName\":\"张杰\",\"remark\":\"11\",\"roomArea\":\"11.00\",\"roomAttrDto\":[{\"attrId\":\"112022082081600012\",\"listShow\":\"Y\",\"page\":-1,\"records\":0,\"roomId\":\"752022082030880010\",\"row\":0,\"specCd\":\"9035007248\",\"specName\":\"精装修\",\"statusCd\":\"0\",\"total\":0,\"value\":\"20\",\"valueName\":\"20\"}],\"roomId\":\"752022082030880010\",\"roomName\":\"D-1-1001\",\"roomNum\":\"1001\",\"roomRent\":\"0.00\",\"roomSubType\":\"110\",\"roomSubTypeName\":\"住宅\",\"roomType\":\"1010301\",\"section\":\"1\",\"startTime\":\"2022-09-03 18:50:53\",\"state\":\"2001\",\"stateName\":\"已入住\",\"unitId\":\"742022082058950007\",\"unitNum\":\"1\"}"
)
@Java110Cmd(serviceCode = "owner.queryCurrentOwner")
public class QueryCurrentOwnerCmd extends Cmd {

    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerAttrInnerServiceSMO ownerAttrInnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        String userId = context.getReqHeaders().get("user-id");

        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setUserId(userId);
        ownerAppUserDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserInnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);

        if (ownerAppUserDtos == null || ownerAppUserDtos.size() < 1) {
            throw new CmdException("未绑定业主");
        }

        String memberId = ownerAppUserDtos.get(0).getMemberId();

        if ("-1".equals(memberId)) {
            throw new CmdException("未绑定业主");
        }

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(reqJson.getString("communityId"));
        ownerDto.setMemberId(memberId);
        List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);

        Assert.listOnlyOne(ownerDtos, "业主不存在");

        OwnerAttrDto ownerAttrDto = new OwnerAttrDto();
        ownerAttrDto.setMemberId(ownerDtos.get(0).getMemberId());
        ownerAttrDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerAttrDto> ownerAttrDtos = ownerAttrInnerServiceSMOImpl.queryOwnerAttrs(ownerAttrDto);
        ownerDtos.get(0).setOwnerAttrDtos(ownerAttrDtos);

        context.setResponseEntity(ResultVo.createResponseEntity(ownerDtos.get(0)));

    }
}
