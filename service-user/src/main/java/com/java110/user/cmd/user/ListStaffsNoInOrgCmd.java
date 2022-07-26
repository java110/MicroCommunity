package com.java110.user.cmd.user;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.user.UserDto;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询不在这个这个组织中的员工
 */
@Java110Cmd(serviceCode = "user.listStaffsNoInOrg")
public class ListStaffsNoInOrgCmd extends Cmd {

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "orgId", "未包含组织信息");
        String storeId = context.getReqHeaders().get("store-id");
        Assert.hasLength(storeId, "未找到账户信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        String storeId = context.getReqHeaders().get("store-id");

        UserDto userDto = new UserDto();
        userDto.setStoreId(storeId);
        userDto.setOrgId(reqJson.getString("orgId"));
        userDto.setStaffName(reqJson.getString("staffName"));


        int count = userV1InnerServiceSMOImpl.queryStaffsNoInOrgCount(userDto);

        List<UserDto> staffsDtos = null;

        if (count > 0) {
            staffsDtos = userV1InnerServiceSMOImpl.queryStaffsNoInOrg(userDto);
        } else {
            staffsDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, staffsDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
