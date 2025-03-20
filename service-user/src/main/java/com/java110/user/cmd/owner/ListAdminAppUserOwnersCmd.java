package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.privilege.BasePrivilegeDto;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserInnerServiceSMO;
import com.java110.intf.user.IStaffCommunityV1InnerServiceSMO;
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

@Java110Cmd(serviceCode = "owner.listAdminAppUserOwners")
public class ListAdminAppUserOwnersCmd extends Cmd {

    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl;

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Autowired
    private IStaffCommunityV1InnerServiceSMO staffCommunityV1InnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        super.validatePageInfo(reqJson);
        super.validateAdmin(cmdDataFlowContext);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        //获取当前用户id
        OwnerAppUserDto ownerAppUserDto = BeanConvertUtil.covertBean(reqJson, OwnerAppUserDto.class);

        String staffId = CmdContextUtils.getUserId(cmdDataFlowContext);

        List<String> communityIds = staffCommunityV1InnerServiceSMOImpl.queryStaffCommunityIds(staffId);

        if (!ListUtil.isNull(communityIds)) {
            ownerAppUserDto.setCommunityIds(communityIds.toArray(new String[communityIds.size()]));
        }

        ownerAppUserDto.setUserId(reqJson.getString("systemUserId"));
        int count = ownerAppUserInnerServiceSMOImpl.queryOwnerAppUsersCount(ownerAppUserDto);
        int row = reqJson.getIntValue("row");

        List<OwnerAppUserDto> ownerAppUserDtos = null;
        if (count > 0) {
            ownerAppUserDtos = ownerAppUserInnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);
        } else {
            ownerAppUserDtos = new ArrayList<>();
        }

        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity((int) Math.ceil((double) count / (double) row), count, ownerAppUserDtos);
        cmdDataFlowContext.setResponseEntity(responseEntity);
    }

    /**
     * 脱敏处理
     *
     * @return
     */
    public List<Map> getPrivilegeOwnerList(String resource, String userId) {
        BasePrivilegeDto basePrivilegeDto = new BasePrivilegeDto();
        basePrivilegeDto.setResource(resource);
        basePrivilegeDto.setUserId(userId);
        List<Map> privileges = menuInnerServiceSMOImpl.checkUserHasResource(basePrivilegeDto);
        return privileges;
    }
}
