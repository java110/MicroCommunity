package com.java110.community.cmd.floor;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.doc.annotation.*;
import com.java110.dto.UnitDto;
import com.java110.intf.community.IFloorInnerServiceSMO;
import com.java110.intf.community.IFloorV1InnerServiceSMO;
import com.java110.intf.community.IUnitV1InnerServiceSMO;
import com.java110.po.floor.FloorPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;


@Java110CmdDoc(title = "删除楼栋",
        description = "用于外系统删除楼栋信息功能",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/floor.deleteFloor",
        resource = "communityDoc",
        author = "吴学文",
        serviceCode = "floor.deleteFloor",
        seq = 7
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "communityId", length = 30, remark = "小区ID"),
        @Java110ParamDoc(name = "floorId",length = 30, remark = "楼栋ID"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
        }
)

@Java110ExampleDoc(
        reqBody="{\"floorId\":\"123123\",\"communityId\":\"2022081539020475\"}",
        resBody="{'code':0,'msg':'成功'}"
)

@Java110Cmd(serviceCode = "floor.deleteFloor")
public class DeleteFloorCmd extends Cmd {
    @Autowired
    private IFloorInnerServiceSMO floorInnerServiceSMOImpl;

    @Autowired
    private IFloorV1InnerServiceSMO floorV1InnerServiceSMOImpl;

    @Autowired
    private IUnitV1InnerServiceSMO unitV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "floorId", "请求报文中未包含floorId");
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId");

        UnitDto unitDto = new UnitDto();
        unitDto.setFloorId(reqJson.getString("floorId"));
        unitDto.setCommunityId(reqJson.getString("communityId"));
//        unitDto.setRoomUnit(UnitDto.ROOM_UNIT_Y);
        int count = unitV1InnerServiceSMOImpl.queryUnitsCount(unitDto);
        if (count > 0) {
            throw new IllegalArgumentException("请先删除单元 再删除楼栋");
        }
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        FloorPo floorPo = BeanConvertUtil.covertBean(reqJson, FloorPo.class);
        int flag = floorV1InnerServiceSMOImpl.deleteFloor(floorPo);

        if (flag < 1) {
            throw new CmdException("删除楼栋失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
