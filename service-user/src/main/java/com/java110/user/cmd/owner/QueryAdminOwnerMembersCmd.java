package com.java110.user.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.privilege.BasePrivilegeDto;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
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

@Java110Cmd(serviceCode = "owner.queryAdminOwnerMembers")
public class QueryAdminOwnerMembersCmd extends Cmd {

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "ownerId", "请求中未包含ownerId信息");
        super.validatePageInfo(reqJson);
        super.validateAdmin(context);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        String userId = context.getReqHeaders().get("user-id");
        OwnerDto ownerDto = BeanConvertUtil.covertBean(reqJson, OwnerDto.class);
        ownerDto.setOwnerTypeCd(OwnerDto.OWNER_TYPE_CD_MEMBER);
        int row = reqJson.getInteger("row");
        //查询总记录数
        int total = ownerInnerServiceSMOImpl.queryOwnersMemberCount(ownerDto);

        List<OwnerDto> ownerDtos = null;
        if (total > 0) {
            ownerDtos = ownerInnerServiceSMOImpl.queryOwnerMembers(ownerDto);
        } else {
            ownerDtos = new ArrayList<>();
        }
        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity((int) Math.ceil((double) total / (double) row), total, ownerDtos);
        context.setResponseEntity(responseEntity);
    }
}
