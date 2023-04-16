package com.java110.community.cmd.floor;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.UnitDto;
import com.java110.dto.data.DataPrivilegeStaffDto;
import com.java110.intf.community.IDataPrivilegeUnitV1InnerServiceSMO;
import com.java110.intf.community.IFloorInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.ApiUnitVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Java110Cmd(serviceCode = "floor.queryFloorAndUnits")
public class QueryFloorAndUnitsCmd extends Cmd {

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

        //查询 员工是否有配置 授权单元
        String staffId = cmdDataFlowContext.getReqHeaders().get("user-id");
        DataPrivilegeStaffDto dataPrivilegeStaffDto = new DataPrivilegeStaffDto();
        dataPrivilegeStaffDto.setStaffId(staffId);
        String[] unitIds = dataPrivilegeUnitV1InnerServiceSMOImpl.queryDataPrivilegeUnitsByStaff(dataPrivilegeStaffDto);

        if(unitIds != null && unitIds.length>0){
            unitDto.setUnitIds(unitIds);
        }

        List<UnitDto> unitDtoList = floorInnerServiceSMOImpl.queryFloorAndUnits(unitDto);

        List<ApiUnitVo> apiUnitVos = BeanConvertUtil.covertBeanList(unitDtoList, ApiUnitVo.class);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiUnitVos), HttpStatus.OK);
        cmdDataFlowContext.setResponseEntity(responseEntity);
    }
}
