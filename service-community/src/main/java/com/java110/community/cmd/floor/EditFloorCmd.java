package com.java110.community.cmd.floor;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.AbstractServiceCmdListener;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.FloorDto;
import com.java110.intf.community.IFloorInnerServiceSMO;
import com.java110.intf.community.IFloorV1InnerServiceSMO;
import com.java110.po.floor.FloorPo;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

@Java110Cmd(serviceCode = "floor.editFloor")
public class EditFloorCmd extends AbstractServiceCmdListener {
    @Autowired
    private IFloorInnerServiceSMO floorInnerServiceSMOImpl;

    @Autowired
    private IFloorV1InnerServiceSMO floorV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "floorId", "请求报文中未包含floorId");
        Assert.jsonObjectHaveKey(reqJson, "name", "请求报文中未包含name");
        Assert.jsonObjectHaveKey(reqJson, "userId", "请求报文中未包含userId");
        Assert.jsonObjectHaveKey(reqJson, "floorNum", "请求报文中未包含floorNum");
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        JSONObject businessFloor = new JSONObject();
        businessFloor.put("floorId", reqJson.getString("floorId"));
        businessFloor.put("name", reqJson.getString("floorName"));
        businessFloor.put("remark", reqJson.getString("remark"));
        businessFloor.put("userId", reqJson.getString("userId"));
        businessFloor.put("floorNum", reqJson.getString("floorNum"));
        businessFloor.put("communityId", reqJson.getString("communityId"));
        businessFloor.put("floorArea", reqJson.getString("floorArea"));

        FloorPo floorPo = BeanConvertUtil.covertBean(businessFloor, FloorPo.class);
        int flag = floorV1InnerServiceSMOImpl.updateFloor(floorPo);

        if (flag < 1) {
            throw new CmdException("修改楼栋失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
