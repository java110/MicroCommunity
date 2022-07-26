package com.java110.user.cmd.org;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.org.OrgDto;
import com.java110.dto.org.OrgTreeDto;
import com.java110.intf.user.IOrgV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "org.listOrgTree")
public class ListOrgTreeCmd extends Cmd {

    @Autowired
    private IOrgV1InnerServiceSMO orgV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        String storeId = context.getReqHeaders().get("store-id");
        if (StringUtil.isEmpty(storeId)) {
            storeId = reqJson.getString("storeId");
        }

        Assert.hasLength(storeId, "未包含商户信息");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        String storeId = context.getReqHeaders().get("store-id");
        if (StringUtil.isEmpty(storeId)) {
            storeId = reqJson.getString("storeId");
        }
        Assert.hasLength(storeId, "未包含商户信息");


        OrgDto orgDto = new OrgDto();
        orgDto.setStoreId(storeId);
        List<OrgDto> orgDtos = orgV1InnerServiceSMOImpl.queryOrgs(orgDto);

        OrgTreeDto storeOrgTreeDto = null;
        for (OrgDto tmpOrgDto : orgDtos) {
            if (OrgDto.ORG_LEVEL_STORE.equals(tmpOrgDto.getOrgLevel())) {
                storeOrgTreeDto = new OrgTreeDto(tmpOrgDto.getOrgId(), tmpOrgDto.getOrgName(), tmpOrgDto.getParentOrgId(), tmpOrgDto.getOrgName());
            }
        }

        if (storeOrgTreeDto == null) {
            return;
        }

        findChilds(storeOrgTreeDto, orgDtos);
        context.setResponseEntity(ResultVo.createResponseEntity(storeOrgTreeDto));

    }

    private void findChilds(OrgTreeDto parentOrgDto, List<OrgDto> orgDtos) {

        List<OrgTreeDto> childs = new ArrayList<>();
        OrgTreeDto child = null;
        for (OrgDto orgDto : orgDtos) {
            if (parentOrgDto.getId().equals(orgDto.getOrgId())) { // 他自己跳过
                continue;
            }
            if (orgDto.getParentOrgId().equals(parentOrgDto.getId())) {
                child = new OrgTreeDto(orgDto.getOrgId(), orgDto.getOrgName(), orgDto.getParentOrgId(), parentOrgDto.getAllOrgName() + " / " + orgDto.getOrgName());
                childs.add(child);
            }
        }

        if (childs.size() < 1) {
            return;
        }

        parentOrgDto.setChildren(childs);

        for (OrgTreeDto orgTreeDto : childs) {
            findChilds(orgTreeDto, orgDtos);
        }
    }
}
