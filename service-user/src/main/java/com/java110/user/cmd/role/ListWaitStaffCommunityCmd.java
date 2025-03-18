package com.java110.user.cmd.role;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.PageDto;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.privilege.RoleCommunityDto;
import com.java110.dto.staffCommunity.StaffCommunityDto;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.user.IRoleCommunityV1InnerServiceSMO;
import com.java110.intf.user.IStaffCommunityV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import com.java110.vo.api.community.ApiCommunityDataVo;
import com.java110.vo.api.community.ApiCommunityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "role.listWaitStaffCommunity")
public class ListWaitStaffCommunityCmd extends Cmd {

    @Autowired
    private IStaffCommunityV1InnerServiceSMO staffCommunityV1InnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);

        Assert.hasKeyAndValue(reqJson, "staffId", "必填，请填写角色");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        String storeId = CmdContextUtils.getStoreId(context);
        StaffCommunityDto staffCommunityDto = new StaffCommunityDto();
        staffCommunityDto.setStaffId(reqJson.getString("staffId"));
        staffCommunityDto.setStoreId(storeId);
        staffCommunityDto.setPage(PageDto.DEFAULT_PAGE);
        List<StaffCommunityDto> staffCommunityDtos = staffCommunityV1InnerServiceSMOImpl.queryStaffCommunitys(staffCommunityDto);
        List<String> communityIds = new ArrayList<>();
        for (StaffCommunityDto tmpStaffCommunityDto : staffCommunityDtos) {
            communityIds.add(tmpStaffCommunityDto.getCommunityId());
        }
        CommunityDto communityDto = new CommunityDto();
        communityDto.setStoreId(storeId);
        if (!ListUtil.isNull(communityIds)) {
            communityDto.setNotInCommunityId(communityIds.toArray(new String[communityIds.size()]));
        }
        communityDto.setAuditStatusCd("1100");
        communityDto.setMemberId(storeId);
        communityDto.setPage(reqJson.getIntValue("page"));
        communityDto.setRow(reqJson.getIntValue("row"));
        int count = communityInnerServiceSMOImpl.queryCommunitysCount(communityDto);

        List<CommunityDto> communitys = null;

        if (count > 0) {
            communitys = communityInnerServiceSMOImpl.queryCommunitys(communityDto);
        } else {
            communitys = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, communitys);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
