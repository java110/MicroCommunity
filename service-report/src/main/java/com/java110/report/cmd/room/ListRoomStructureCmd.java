package com.java110.report.cmd.room;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.RoomDto;
import com.java110.intf.report.IReportCommunityInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/*
     查询房屋结构图
 */
@Java110Cmd(serviceCode = "room.listRoomStructure")
public class ListRoomStructureCmd extends Cmd {

    @Autowired
    private IReportCommunityInnerServiceSMO reportCommunityInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "unitId", "未传入单元信息");
        Assert.hasKeyAndValue(reqJson, "communityId", "未传入房屋信息");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        RoomDto roomDto = BeanConvertUtil.covertBean(reqJson, RoomDto.class);
        List<RoomDto> roomDtos = reportCommunityInnerServiceSMOImpl.queryRoomStructures(roomDto);

        cmdDataFlowContext.setResponseEntity(ResultVo.createResponseEntity(roomDtos));
    }
}
