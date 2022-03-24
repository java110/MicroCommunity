package com.java110.community.cmd.unit;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.AbstractServiceCmdListener;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.FloorDto;
import com.java110.dto.UnitDto;
import com.java110.intf.community.IFloorInnerServiceSMO;
import com.java110.intf.community.IFloorV1InnerServiceSMO;
import com.java110.intf.community.IUnitInnerServiceSMO;
import com.java110.intf.community.IUnitV1InnerServiceSMO;
import com.java110.po.floor.FloorPo;
import com.java110.po.unit.UnitPo;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

@Java110Cmd(serviceCode = "unit.saveUnit")
public class SaveUnitCmd extends AbstractServiceCmdListener {
    @Autowired
    private IFloorInnerServiceSMO floorInnerServiceSMOImpl;

    @Autowired
    private IUnitInnerServiceSMO unitInnerServiceSMOImpl;

    @Autowired
    private IUnitV1InnerServiceSMO unitV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(reqJson, "floorId", "请求报文中未包含floorId节点");
        Assert.jsonObjectHaveKey(reqJson, "unitNum", "请求报文中未包含unitNum节点");
        Assert.jsonObjectHaveKey(reqJson, "layerCount", "请求报文中未包含layerCount节点");
        Assert.jsonObjectHaveKey(reqJson, "lift", "请求报文中未包含lift节点");

        Assert.isInteger(reqJson.getString("layerCount"), "单元总层数据不是有效数字");

        if (!"1010".equals(reqJson.getString("lift")) && !"2020".equals(reqJson.getString("lift"))) {
            throw new IllegalArgumentException("是否有电梯 传入数据错误");
        }

        FloorDto floorDto = new FloorDto();
        floorDto.setCommunityId(reqJson.getString("communityId"));
        floorDto.setFloorId(reqJson.getString("floorId"));
        //校验小区楼ID和小区是否有对应关系
        int total = floorInnerServiceSMOImpl.queryFloorsCount(floorDto);

        if (total < 1) {
            throw new IllegalArgumentException("传入小区楼ID不是该小区的楼");
        }

        //校验单元编号是否已经存在
        UnitDto unitDto = new UnitDto();
        unitDto.setCommunityId(reqJson.getString("communityId"));
        unitDto.setFloorId(reqJson.getString("floorId"));
        unitDto.setUnitNum(reqJson.getString("unitNum"));
        int count = unitInnerServiceSMOImpl.queryUnitsCount(unitDto);

        if(count > 0){
            throw new IllegalArgumentException("单元编号已经存在，请勿重复添加");
        }
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        JSONObject businessUnit = new JSONObject();
        businessUnit.put("floorId", reqJson.getString("floorId"));
        businessUnit.put("layerCount", reqJson.getString("layerCount"));
        businessUnit.put("unitId", !reqJson.containsKey("unitId") ? GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_unitId)
                : reqJson.getString("unitId"));
        businessUnit.put("unitNum", reqJson.getString("unitNum"));
        businessUnit.put("lift", reqJson.getString("lift"));
        businessUnit.put("remark", reqJson.getString("remark"));
        businessUnit.put("unitArea", reqJson.getString("unitArea"));
        businessUnit.put("userId", reqJson.getString("userId"));
        UnitPo unitPo = BeanConvertUtil.covertBean(businessUnit, UnitPo.class);
        int flag = unitV1InnerServiceSMOImpl.saveUnit(unitPo);

        if (flag < 1) {
            throw new CmdException("保存单元失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
