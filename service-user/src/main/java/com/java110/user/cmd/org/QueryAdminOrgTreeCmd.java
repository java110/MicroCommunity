package com.java110.user.cmd.org;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.org.OrgDto;
import com.java110.dto.org.OrgTreeDto;
import com.java110.dto.store.StoreDto;
import com.java110.dto.unit.UnitDto;
import com.java110.intf.user.IOrgV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "org.queryAdminOrgTree")
public class QueryAdminOrgTreeCmd extends Cmd {

    @Autowired
    private IOrgV1InnerServiceSMO orgV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        super.validateAdmin(context);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        OrgDto orgDto = new OrgDto();
        orgDto.setStoreTypeCd(StoreDto.STORE_TYPE_PROPERTY);
        List<OrgDto> orgDtos = orgV1InnerServiceSMOImpl.queryOrgs(orgDto);
        JSONArray orgs = new JSONArray();

        if(ListUtil.isNull(orgDtos)){
            context.setResponseEntity(ResultVo.createResponseEntity(orgs));
            return;
        }

        JSONObject orgInfo = null;
        for (OrgDto tmpOrgDto : orgDtos) {
            if (OrgDto.ORG_LEVEL_STORE.equals(tmpOrgDto.getOrgLevel())) {
                orgInfo = new JSONObject();
                orgInfo.put("id",  tmpOrgDto.getOrgId());
                orgInfo.put("orgId", tmpOrgDto.getOrgId());
                orgInfo.put("text", tmpOrgDto.getOrgName());
                orgInfo.put("icon", "/img/org.png");
                orgs.add(orgInfo);
            }
        }

        if(ListUtil.isNull(orgs)){
            context.setResponseEntity(ResultVo.createResponseEntity(orgs));
            return;
        }

        for(int orgIndex = 0 ;orgIndex < orgs.size();orgIndex++){
            orgInfo = orgs.getJSONObject(orgIndex);
            findChilds(orgInfo, orgDtos);

        }
        context.setResponseEntity(ResultVo.createResponseEntity(orgs));

    }

    private void findChilds(JSONObject curOrg, List<OrgDto> orgDtos) {

        JSONArray childs = new JSONArray();
        JSONObject child = null;
        for (OrgDto orgDto : orgDtos) {
            if (curOrg.getString("id").equals(orgDto.getOrgId())) { // 他自己跳过
                continue;
            }
            if (orgDto.getParentOrgId().equals(curOrg.getString("id"))) {//二级
                child = new JSONObject();
                child.put("id",  orgDto.getOrgId());
                child.put("orgId", orgDto.getOrgId());
                child.put("text", orgDto.getOrgName());
                child.put("icon", "/img/org.png");
                childs.add(child);
            }
        }

        if (ListUtil.isNull(childs)) {
            return;
        }

        curOrg.put("children",childs);

        for(int orgIndex = 0 ;orgIndex < childs.size();orgIndex++){
            child = childs.getJSONObject(orgIndex);
            findChilds(child, orgDtos);

        }
    }
}
