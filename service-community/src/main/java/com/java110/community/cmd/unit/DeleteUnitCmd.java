package com.java110.community.cmd.unit;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.FloorDto;
import com.java110.dto.RoomDto;
import com.java110.dto.UnitDto;
import com.java110.intf.community.IFloorInnerServiceSMO;
import com.java110.intf.community.IRoomV1InnerServiceSMO;
import com.java110.intf.community.IUnitInnerServiceSMO;
import com.java110.intf.community.IUnitV1InnerServiceSMO;
import com.java110.po.unit.UnitPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

@Java110Cmd(serviceCode = "unit.deleteUnit")
public class DeleteUnitCmd extends Cmd {
    @Autowired
    private IFloorInnerServiceSMO floorInnerServiceSMOImpl;

    @Autowired
    private IUnitInnerServiceSMO unitInnerServiceSMOImpl;

    @Autowired
    private IUnitV1InnerServiceSMO unitV1InnerServiceSMOImpl;

    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(reqJson, "floorId", "请求报文中未包含floorId节点");
        Assert.jsonObjectHaveKey(reqJson, "unitId", "请求报文中未包含unitId节点");

        FloorDto floorDto = new FloorDto();
        floorDto.setCommunityId(reqJson.getString("communityId"));
        floorDto.setFloorId(reqJson.getString("floorId"));
        //校验小区楼ID和小区是否有对应关系
        int total = floorInnerServiceSMOImpl.queryFloorsCount(floorDto);

        if (total < 1) {
            throw new IllegalArgumentException("传入小区楼ID不是该小区的楼");
        }

        //校验 小区楼ID 和单元ID是否有关系
        UnitDto unitDto = new UnitDto();
        unitDto.setFloorId(reqJson.getString("floorId"));
        unitDto.setUnitId(reqJson.getString("unitId"));
        total = unitInnerServiceSMOImpl.queryUnitsCount(unitDto);
        if (total < 1) {
            throw new IllegalArgumentException("传入单元不是该小区的楼的单元");
        }

        RoomDto roomDto = new RoomDto();
        roomDto.setUnitId(reqJson.getString("unitId"));
        roomDto.setCommunityId(reqJson.getString("communityId"));
        int count = roomV1InnerServiceSMOImpl.queryRoomsCount(roomDto);
        if(count > 0){
            throw new IllegalArgumentException("单元下存在房屋 请先删除房屋");

        }
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        JSONObject businessUnit = new JSONObject();
        businessUnit.put("unitId", reqJson.getString("unitId"));
        UnitPo unitPo = BeanConvertUtil.covertBean(businessUnit, UnitPo.class);
        int flag = unitV1InnerServiceSMOImpl.deleteUnit(unitPo);

        if (flag < 1) {
            throw new CmdException("删除单元失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
