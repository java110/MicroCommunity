package com.java110.user.cmd.user;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.doc.annotation.*;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.org.OrgDto;
import com.java110.dto.org.OrgStaffRelDto;
import com.java110.dto.privilege.BasePrivilegeDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.intf.store.IOrgStaffRelV1InnerServiceSMO;
import com.java110.intf.user.IOrgStaffRelInnerServiceSMO;
import com.java110.intf.user.IOrgV1InnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.*;
import com.java110.vo.api.staff.ApiStaffDataVo;
import com.java110.vo.api.staff.ApiStaffVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Java110Cmd(serviceCode = "user.queryAdminPropertyStaff")
public class QueryAdminPropertyStaffCmd extends Cmd {

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IOrgV1InnerServiceSMO orgV1InnerServiceSMOImpl;

    @Autowired
    private IOrgStaffRelV1InnerServiceSMO orgStaffRelV1InnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Autowired
    private IOrgStaffRelInnerServiceSMO iOrgStaffRelInnerServiceSMO;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validateAdmin(context);
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        UserDto userDto = BeanConvertUtil.covertBean(reqJson, UserDto.class);
        userDto.setStoreId("");

        int count = userInnerServiceSMOImpl.getStaffCount(userDto);
        List<ApiStaffDataVo> staffs = new ArrayList<>();
        if (count > 0) {
            List<ApiStaffDataVo> staffList = BeanConvertUtil.covertBeanList(userInnerServiceSMOImpl.getStaffs(userDto), ApiStaffDataVo.class);
            for (ApiStaffDataVo apiStaffDataVo : staffList) {
                FileRelDto fileRelDto = new FileRelDto();
                fileRelDto.setObjId(apiStaffDataVo.getUserId());
                fileRelDto.setRelTypeCd("12000"); //员工图片
                List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
                if (ListUtil.isNotNull(fileRelDtos)) {
                    List<String> urls = new ArrayList<>();
                    for (FileRelDto fileRel : fileRelDtos) {
                        urls.add(fileRel.getFileRealName());
                    }
                    apiStaffDataVo.setUrls(urls);
                }
                staffs.add(apiStaffDataVo);
            }
            refreshInitials(staffs);
            refreshOrgs(staffs);
        } else {
            staffs = new ArrayList<>();
        }
        ApiStaffVo apiStaffVo = new ApiStaffVo();
        apiStaffVo.setTotal(count);
        apiStaffVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiStaffVo.setStaffs(staffs);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiStaffVo), HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }

    private void refreshOrgs(List<ApiStaffDataVo> staffs) {
        if (ListUtil.isNull(staffs)) {
            return;
        }
        List<String> staffIds = new ArrayList<>();
        for (ApiStaffDataVo apiStaffDataVo : staffs) {
            staffIds.add(apiStaffDataVo.getUserId());
        }
        OrgDto orgDto = new OrgDto();
        orgDto.setStoreId(staffs.get(0).getStoreId());
        List<OrgDto> orgDtos = orgV1InnerServiceSMOImpl.queryOrgs(orgDto);
        if (ListUtil.isNull(orgDtos)) {
            return;
        }
        OrgStaffRelDto orgStaffRelDto = new OrgStaffRelDto();
        orgStaffRelDto.setStaffIds(staffIds.toArray(new String[staffIds.size()]));
        orgStaffRelDto.setStoreId(staffs.get(0).getStoreId());
        List<OrgStaffRelDto> orgStaffRels = orgStaffRelV1InnerServiceSMOImpl.queryOrgStaffRels(orgStaffRelDto);
        if (ListUtil.isNull(orgStaffRels)) {
            return;
        }
        for (ApiStaffDataVo apiStaffDataVo : staffs) {
            for (OrgStaffRelDto tmpOrgStaffRelDto : orgStaffRels) {
                if (!apiStaffDataVo.getUserId().equals(tmpOrgStaffRelDto.getStaffId())) {
                    continue;
                }
                OrgDto org = new OrgDto();
                org.setOrgId(tmpOrgStaffRelDto.getOrgId());
                List<OrgDto> orgs = orgV1InnerServiceSMOImpl.queryOrgs(org);
                if (ListUtil.isNull(orgs)) {
                    continue;
                }
                apiStaffDataVo.setOrgId(tmpOrgStaffRelDto.getOrgId());
                apiStaffDataVo.setParentTwoOrgId(orgs.get(0).getParentOrgId());
            }
        }
        for (ApiStaffDataVo apiStaffDataVo : staffs) {
            if (StringUtil.isEmpty(apiStaffDataVo.getOrgId())) {
                continue;
            }
            apiStaffDataVo.setParentOrgId(apiStaffDataVo.getOrgId());
            findParents(apiStaffDataVo, orgDtos, null, 0);
        }
    }

    private void findParents(ApiStaffDataVo apiStaffDataVo, List<OrgDto> orgDtos, OrgDto curOrgDto, int orgDeep) {
        for (OrgDto orgDto : orgDtos) {
            curOrgDto = orgDto;
            if (!apiStaffDataVo.getParentOrgId().equals(orgDto.getOrgId())) { // 他自己跳过
                continue;
            }
            //如果到一级 就结束
            if (OrgDto.ORG_LEVEL_STORE.equals(apiStaffDataVo.getOrgLevel())) {
                continue;
            }
            apiStaffDataVo.setParentOrgId(orgDto.getParentOrgId());
            if (StringUtil.isEmpty(apiStaffDataVo.getOrgName())) {
                apiStaffDataVo.setOrgName(orgDto.getOrgName());
                continue;
            }
            apiStaffDataVo.setOrgName(orgDto.getOrgName() + " / " + apiStaffDataVo.getOrgName());
            apiStaffDataVo.setOrgLevel(orgDto.getOrgLevel());
        }
        if (curOrgDto != null && OrgDto.ORG_LEVEL_STORE.equals(curOrgDto.getOrgLevel())) {
            return;
        }
        if (curOrgDto != null && curOrgDto.getParentOrgId().equals(curOrgDto.getOrgId())) {
            return;
        }
        if (curOrgDto != null && "-1".equals(curOrgDto.getParentOrgId())) {
            return;
        }
        orgDeep += 1;
        if (orgDeep > 20) {
            return;
        }
        findParents(apiStaffDataVo, orgDtos, curOrgDto, orgDeep);
    }

    /**
     * 刷入首字母
     *
     * @param staffs
     */
    private void refreshInitials(List<ApiStaffDataVo> staffs) {
        for (ApiStaffDataVo staffDataVo : staffs) {
            if (StringUtil.isEmpty(staffDataVo.getName())) {
                continue;
            }
            staffDataVo.setInitials(PinYinUtil.getFirstSpell(staffDataVo.getName()).toUpperCase().charAt(0) + "");
        }
    }
}
