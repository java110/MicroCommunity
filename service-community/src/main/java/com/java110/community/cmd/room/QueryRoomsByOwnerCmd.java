package com.java110.community.cmd.room;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.bmo.room.IQueryRoomStatisticsBMO;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.doc.annotation.*;
import com.java110.dto.RoomDto;
import com.java110.intf.community.IFloorInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.api.ApiRoomDataVo;
import com.java110.vo.api.ApiRoomVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Java110CmdDoc(title = "查询业主房屋",
        description = "查询业主房屋信息",
        httpMethod = "get",
        url = "http://{ip}:{port}/app/room.queryRoomsByOwner",
        resource = "communityDoc",
        author = "吴学文",
        serviceCode = "room.queryRoomsByOwner",
        seq = 17
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "page", type = "int",length = 11, remark = "页数"),
        @Java110ParamDoc(name = "row", type = "int",length = 11, remark = "行数"),
        @Java110ParamDoc(name = "communityId", length = 30, remark = "小区ID"),
        @Java110ParamDoc(name = "ownerId", length = 30, remark = "业主ID"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "records", type = "int", length = 11,  remark = "总页数"),
                @Java110ParamDoc(name = "total", type = "int", length = 11, remark = "总数据"),
                @Java110ParamDoc(name = "rooms", type = "Object", remark = "有效数据"),
                @Java110ParamDoc(parentNodeName = "rooms",name = "roomName", type = "String", remark = "房屋名称"),
                @Java110ParamDoc(parentNodeName = "rooms",name = "roomId", type = "String", remark = "房屋编号"),
        }
)

@Java110ExampleDoc(
        reqBody="http://{ip}:{port}/app/room.queryRoomsByOwner?ownerId=123123&page=1&row=10&communityId=2022081539020475",
        resBody="{\"page\":0,\"records\":1,\"rooms\":[{\"apartment\":\"10101\",\"apartmentName\":\"一室一厅\",\"builtUpArea\":\"11.00\",\"endTime\":\"2037-01-01 00:00:00\",\"feeCoefficient\":\"1.00\",\"floorId\":\"732022081690440002\",\"floorNum\":\"D\",\"idCard\":\"\",\"layer\":\"1\",\"link\":\"18909711447\",\"ownerId\":\"772022082070860017\",\"ownerName\":\"张杰\",\"remark\":\"11\",\"roomArea\":\"11.00\",\"roomAttrDto\":[{\"attrId\":\"112022082081600012\",\"listShow\":\"Y\",\"page\":-1,\"records\":0,\"roomId\":\"752022082030880010\",\"row\":0,\"specCd\":\"9035007248\",\"specName\":\"精装修\",\"statusCd\":\"0\",\"total\":0,\"value\":\"20\",\"valueName\":\"20\"}],\"roomId\":\"752022082030880010\",\"roomName\":\"D-1-1001\",\"roomNum\":\"1001\",\"roomRent\":\"0.00\",\"roomSubType\":\"110\",\"roomSubTypeName\":\"住宅\",\"roomType\":\"1010301\",\"section\":\"1\",\"startTime\":\"2022-09-03 18:50:53\",\"state\":\"2001\",\"stateName\":\"已入住\",\"unitId\":\"742022082058950007\",\"unitNum\":\"1\"}],\"rows\":0,\"total\":2}"
)
@Java110Cmd(serviceCode = "room.queryRoomsByOwner")
public class QueryRoomsByOwnerCmd extends Cmd {

    @Autowired
    private IFloorInnerServiceSMO floorInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;


    @Autowired
    private IQueryRoomStatisticsBMO queryRoomStatisticsBMOImpl;



    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "communityId", "请求中未包含communityId信息");
        //Assert.jsonObjectHaveKey(reqJson, "ownerId", "请求中未包含ownerId信息");

        Assert.hasLength(reqJson.getString("communityId"), "小区ID不能为空");
        //Assert.hasLength(reqJson.getString("ownerId"), "业主ID不能为空");

        String ownerId = reqJson.getString("ownerId");

        String ownerNameLike = reqJson.getString("ownerNameLike");

        if(StringUtil.isEmpty(ownerId) && StringUtil.isEmpty(ownerNameLike)){
            throw new IllegalArgumentException("未包含业主信息");
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        RoomDto roomDto = BeanConvertUtil.covertBean(reqJson, RoomDto.class);

        ApiRoomVo apiRoomVo = new ApiRoomVo();
        List<RoomDto> roomDtoList = roomInnerServiceSMOImpl.queryRoomsByOwner(roomDto);
        roomDtoList = queryRoomStatisticsBMOImpl.queryRoomOweFee(roomDtoList);

        apiRoomVo.setTotal(roomDtoList.size());
        apiRoomVo.setRooms(BeanConvertUtil.covertBeanList(roomDtoList, ApiRoomDataVo.class));
        apiRoomVo.setRecords(1);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiRoomVo), HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }
}
