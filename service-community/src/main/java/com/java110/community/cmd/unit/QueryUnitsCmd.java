package com.java110.community.cmd.unit;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.doc.annotation.*;
import com.java110.dto.UnitDto;
import com.java110.dto.data.DataPrivilegeStaffDto;
import com.java110.intf.community.IDataPrivilegeUnitV1InnerServiceSMO;
import com.java110.intf.community.IFloorInnerServiceSMO;
import com.java110.intf.community.IUnitInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.ApiUnitVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;


@Java110CmdDoc(title = "查询单元",
        description = "用于外系统查询单元信息功能",
        httpMethod = "get",
        url = "http://{ip}:{port}/app/unit.queryUnits",
        resource = "communityDoc",
        author = "吴学文",
        serviceCode = "unit.queryUnits",
        seq = 12
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "communityId", length = 30, remark = "小区ID"),
        @Java110ParamDoc(name = "floorId", length = 30, remark = "楼栋ID"),
        @Java110ParamDoc(name = "page", type = "int",length = 11, remark = "页数"),
        @Java110ParamDoc(name = "row", type = "int",length = 11, remark = "行数"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "unitId", type = "String", length = 30,  remark = "单元ID"),
                @Java110ParamDoc(name = "unitNum",  type = "String", length = 250,  remark = "单元编号"),
                @Java110ParamDoc(name = "seq",  type = "int", length = 11,  remark = "排序"),
        }
)

@Java110ExampleDoc(
        reqBody="http://{ip}:{port}/app/unit.queryUnits?page=1&row=10&communityId=123123&floorId=123",
        resBody="{'unitId':'123123','unitNum':'123123','seq':1}"
)

@Java110Cmd(serviceCode = "unit.queryUnits")
public class QueryUnitsCmd extends Cmd {
    @Autowired
    private IUnitInnerServiceSMO unitInnerServiceSMOImpl;

    @Autowired
    private IFloorInnerServiceSMO floorInnerServiceSMOImpl;

    @Autowired
    private IDataPrivilegeUnitV1InnerServiceSMO dataPrivilegeUnitV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求中未包含communityId信息");
        //Assert.jsonObjectHaveKey(reqJson, "floorId", "请求中未包含floorId信息");
        //校验小区楼ID和小区是否有对应关系
//        int total = floorInnerServiceSMOImpl.queryFloorsCount(BeanConvertUtil.covertBean(reqJson, FloorDto.class));
//
//        if (total < 1) {
//            throw new IllegalArgumentException("传入小区楼ID不是该小区的楼");
//        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        UnitDto unitDto = BeanConvertUtil.covertBean(reqJson, UnitDto.class);
        unitDto.setUserId("");

        String staffId = cmdDataFlowContext.getReqHeaders().get("user-id");
        DataPrivilegeStaffDto dataPrivilegeStaffDto = new DataPrivilegeStaffDto();
        dataPrivilegeStaffDto.setStaffId(staffId);
        String[] unitIds = dataPrivilegeUnitV1InnerServiceSMOImpl.queryDataPrivilegeUnitsByStaff(dataPrivilegeStaffDto);

        if(unitIds != null && unitIds.length>0){
            unitDto.setUnitIds(unitIds);
        }

        List<UnitDto> unitDtoList = unitInnerServiceSMOImpl.queryUnits(unitDto);

        List<ApiUnitVo> apiUnitVos = BeanConvertUtil.covertBeanList(unitDtoList, ApiUnitVo.class);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiUnitVos), HttpStatus.OK);
        cmdDataFlowContext.setResponseEntity(responseEntity);
    }
}
