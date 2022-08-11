package com.java110.user.cmd.org;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.dto.org.OrgDto;
import com.java110.dto.org.OrgStaffRelDto;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.intf.user.IOrgInnerServiceSMO;
import com.java110.intf.user.IOrgStaffRelInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.org.ApiOrgDataVo;
import com.java110.vo.api.org.ApiOrgVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Java110Cmd(serviceCode = "org.listOrgs")
public class ListOrgsCmd extends Cmd {

    @Autowired
    private IOrgInnerServiceSMO orgInnerServiceSMOImpl;

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Autowired
    private IOrgStaffRelInnerServiceSMO iOrgStaffRelInnerServiceSMO;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
        if (!reqJson.containsKey("storeId")) {
            String storeId = context.getReqHeaders().get("store-id");
            reqJson.put("storeId", storeId);
        }
//        String storeId = context.getReqHeaders().get("store-id");
//        reqJson.put("storeId", storeId);
        Assert.hasKeyAndValue(reqJson, "storeId", "必填，请填写商户ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        OrgDto orgDto = BeanConvertUtil.covertBean(reqJson, OrgDto.class);
        //2级别组织信息
        if (reqJson.containsKey("orgLevel") && reqJson.getString("orgLevel").equals("2")) {
            //默认只查看当前归属组织架构
            BasePrivilegeDto basePrivilegeDto = new BasePrivilegeDto();
            basePrivilegeDto.setResource("/viewAllOrganization");
            basePrivilegeDto.setUserId(reqJson.getString("userId"));
            List<Map> privileges = menuInnerServiceSMOImpl.checkUserHasResource(basePrivilegeDto);
            if (privileges.size() == 0) {
                //查询员工所属二级组织架构
                OrgStaffRelDto orgStaffRelDto = new OrgStaffRelDto();
                orgStaffRelDto.setStaffId(reqJson.getString("userId"));
                List<OrgStaffRelDto> orgStaffRelDtos = iOrgStaffRelInnerServiceSMO.queryOrgInfoByStaffIds(orgStaffRelDto);
                if (orgStaffRelDtos.size() > 0) {
                    orgDto.setOrgId(orgStaffRelDtos.get(0).getCompanyId());//当前人虽归属的二级组织信息
                }

            }
        }

        int count = orgInnerServiceSMOImpl.queryOrgsCount(orgDto);

        List<ApiOrgDataVo> orgs = null;

        if (count > 0) {
            orgs = BeanConvertUtil.covertBeanList(orgInnerServiceSMOImpl.queryOrgs(orgDto), ApiOrgDataVo.class);
        } else {
            orgs = new ArrayList<>();
        }

        ApiOrgVo apiOrgVo = new ApiOrgVo();

        apiOrgVo.setTotal(count);
        apiOrgVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiOrgVo.setOrgs(orgs);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiOrgVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
