package com.java110.user.cmd.user;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.org.OrgDto;
import com.java110.dto.org.OrgStaffRelDto;
import com.java110.dto.roleCommunity.RoleCommunityDto;
import com.java110.intf.store.IOrgStaffRelV1InnerServiceSMO;
import com.java110.intf.user.IOrgV1InnerServiceSMO;
import com.java110.intf.user.IRoleCommunityV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import com.java110.vo.api.community.ApiCommunityDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "user.listStaffCommunitys")
public class ListStaffCommunitysCmd extends Cmd {

    @Autowired
    private IRoleCommunityV1InnerServiceSMO roleCommunityV1InnerServiceSMOImpl;

    @Autowired
    private IOrgV1InnerServiceSMO orgV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "staffId", "未包含 员工信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        String storeId = context.getReqHeaders().get("store-id");
        Assert.hasLength(storeId, "未包含商户信息");

        RoleCommunityDto orgCommunityDto = BeanConvertUtil.covertBean(reqJson, RoleCommunityDto.class);
        orgCommunityDto.setStaffId(reqJson.getString("staffId"));
        int count = roleCommunityV1InnerServiceSMOImpl.queryRoleCommunitysCount(orgCommunityDto);
        List<ApiCommunityDataVo> communitys = null;
        if (count > 0) {
            List<RoleCommunityDto> roleCommunityDtos = roleCommunityV1InnerServiceSMOImpl.queryRoleCommunitys(orgCommunityDto);
            communitys = BeanConvertUtil.covertBeanList(roleCommunityDtos, ApiCommunityDataVo.class);
            for (RoleCommunityDto tmpOrgCommunityDto : roleCommunityDtos) {
                for (ApiCommunityDataVo tmpApiCommunityDataVo : communitys) {
                    if (tmpOrgCommunityDto.getCommunityId().equals(tmpApiCommunityDataVo.getCommunityId())) {
                        tmpApiCommunityDataVo.setName(tmpOrgCommunityDto.getCommunityName());
                    }
                }
            }
        } else {
            communitys = new ArrayList<>();
        }
        ResultVo resultVo = new ResultVo(1, communitys.size(), communitys);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}
