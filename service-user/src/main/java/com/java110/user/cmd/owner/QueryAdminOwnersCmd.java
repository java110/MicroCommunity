package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.doc.annotation.*;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.privilege.BasePrivilegeDto;
import com.java110.dto.room.RoomDto;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IStaffCommunityV1InnerServiceSMO;
import com.java110.user.bmo.owner.IQueryOwnerStatisticsBMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Java110Cmd(serviceCode = "owner.queryAdminOwners")
public class QueryAdminOwnersCmd extends Cmd {

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;


    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;


    @Autowired
    private IQueryOwnerStatisticsBMO queryOwnerStatisticsBMOImpl;

    @Autowired
    private IStaffCommunityV1InnerServiceSMO staffCommunityV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        super.validateAdmin(cmdDataFlowContext);
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {


        //todo 根据房屋查询时 先用 房屋信息查询 业主ID
        freshRoomId(reqJson);
        OwnerDto ownerDto = BeanConvertUtil.covertBean(reqJson, OwnerDto.class);

        String staffId = CmdContextUtils.getUserId(cmdDataFlowContext);

        List<String> communityIds = staffCommunityV1InnerServiceSMOImpl.queryStaffCommunityIds(staffId);

        if (!ListUtil.isNull(communityIds)) {
            ownerDto.setCommunityIds(communityIds.toArray(new String[communityIds.size()]));
        }

        int row = reqJson.getInteger("row");
        //查询总记录数
        int total = ownerInnerServiceSMOImpl.queryOwnersCount(ownerDto);
        List<OwnerDto> ownerDtos = null;
        if (total > 0) {
            ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);
            // 查询统计数据
            ownerDtos = queryOwnerStatisticsBMOImpl.queryAdminData(ownerDtos);
        } else {
            ownerDtos = new ArrayList<>();
        }
        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity((int) Math.ceil((double) total / (double) row), total, ownerDtos);
        cmdDataFlowContext.setResponseEntity(responseEntity);
    }

    private void freshRoomId(JSONObject reqJson) {
        if (!reqJson.containsKey("roomName")) {
            return;
        }
        String roomName = reqJson.getString("roomName");
        if (StringUtil.isEmpty(roomName)) {
            return;
        }
        if (!roomName.contains("-")) {
            throw new IllegalArgumentException("房屋格式错误,请写入如 楼栋-单元-房屋 格式");
        }
        String[] params = roomName.split("-", 3);
        if (params.length != 3) {
            throw new IllegalArgumentException("房屋格式错误,请写入如 楼栋-单元-房屋 格式");
        }
        RoomDto roomDto = new RoomDto();
        roomDto.setFloorNum(params[0]);
        roomDto.setUnitNum(params[1]);
        roomDto.setRoomNum(params[2]);
        roomDto.setCommunityId(reqJson.getString("communityId"));
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
        Assert.listOnlyOne(roomDtos, "未查询到房屋下业主信息");
        reqJson.put("roomId", roomDtos.get(0).getRoomId());
    }


}
