package com.java110.user.cmd.role;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.dto.org.OrgDto;
import com.java110.dto.org.OrgStaffRelDto;
import com.java110.dto.privilegeUser.PrivilegeUserDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.intf.user.IOrgStaffRelInnerServiceSMO;
import com.java110.intf.user.IOrgV1InnerServiceSMO;
import com.java110.intf.user.IPrivilegeUserV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 查询不在这个角色 内的员工
 */
@Java110Cmd(serviceCode = "role.listStaffsNoRole")
public class ListStaffsNoRoleCmd extends Cmd {

    @Autowired
    private IPrivilegeUserV1InnerServiceSMO privilegeUserV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Autowired
    private IOrgV1InnerServiceSMO orgV1InnerServiceSMOImpl;

    @Autowired
    private IOrgStaffRelInnerServiceSMO iOrgStaffRelInnerServiceSMO;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "roleId", "未包含角色");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        String storeId = context.getReqHeaders().get("store-id");
        String userId = context.getReqHeaders().get("user-id");

        PrivilegeUserDto privilegeUserDto = new PrivilegeUserDto();
        privilegeUserDto.setpId(reqJson.getString("roleId"));
        privilegeUserDto.setStoreId(storeId);
        privilegeUserDto.setPrivilegeFlag(PrivilegeUserDto.PRIVILEGE_FLAG_GROUP);
        privilegeUserDto.setUserName(reqJson.getString("searchUserName"));
        privilegeUserDto.setPage(Integer.parseInt(reqJson.getString("page")));
        privilegeUserDto.setRow(Integer.parseInt(reqJson.getString("row")));

        // 判断是不是管理员，管理员反馈 物业 的所角色
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        userDto.setPage(1);
        userDto.setRow(1);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        Assert.listOnlyOne(userDtos, "用户不存在");
        if (!UserDto.LEVEL_CD_ADMIN.equals(userDtos.get(0).getLevelCd())) {
            //默认只查看当前归属组织架构
            BasePrivilegeDto basePrivilegeDto = new BasePrivilegeDto();
            basePrivilegeDto.setResource("/viewAllOrganization");
            basePrivilegeDto.setUserId(userId);
            List<Map> privileges = menuInnerServiceSMOImpl.checkUserHasResource(basePrivilegeDto);
            if (privileges.size() == 0) {
                //查询员工所属二级组织架构
                OrgStaffRelDto orgStaffRelDto = new OrgStaffRelDto();
                orgStaffRelDto.setStaffId(reqJson.getString("userId"));
                List<OrgStaffRelDto> orgStaffRelDtos = iOrgStaffRelInnerServiceSMO.queryOrgInfoByStaffIdsNew(orgStaffRelDto);
                if (orgStaffRelDtos.size() > 0) {
                    List<String> haveOrgList = new ArrayList<String>();
                    for (OrgStaffRelDto orgStaffRelDto1 : orgStaffRelDtos) {
                        OrgDto orgDto1 = new OrgDto();
                        orgDto1.setOrgId(orgStaffRelDto1.getOrgId());
                        List<OrgDto> orgDtoList = orgV1InnerServiceSMOImpl.queryOrgs(orgDto1);
                        findCompany(haveOrgList, orgDtoList);
                    }
                    privilegeUserDto.setOrgIds(haveOrgList.toArray(new String[haveOrgList.size()]));//当前人虽归属的二级组织信息
                }
            }

        }

        int count = privilegeUserV1InnerServiceSMOImpl.queryStaffsNoRoleCount(privilegeUserDto);

        List<UserDto> staffsDtos = null;

        if (count > 0) {
            staffsDtos = privilegeUserV1InnerServiceSMOImpl.queryStaffsNoRoleInfos(privilegeUserDto);
        } else {
            staffsDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, staffsDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    private void findCompany(List<String> haveOrgList, List<OrgDto> orgDtoList) {

        for (OrgDto orgDto : orgDtoList) {
            haveOrgList.add(orgDto.getOrgId());
            if (!"1".equals(orgDto.getOrgLevel())) {
                if ("2".equals(orgDto.getOrgLevel())) {
                    //上级别
                    OrgDto orgDto1 = new OrgDto();
                    orgDto1.setOrgId(orgDto.getParentOrgId());
                    List<OrgDto> orgDtoList1 = orgV1InnerServiceSMOImpl.queryOrgs(orgDto1);
                    for (OrgDto orgDto2 : orgDtoList1) {
                        haveOrgList.add(orgDto2.getOrgId());
                    }
                    //同级别
                    OrgDto orgDto2 = new OrgDto();
                    orgDto2.setParentOrgId(orgDto.getOrgId());
                    List<OrgDto> orgDtoList2 = orgV1InnerServiceSMOImpl.queryOrgs(orgDto2);
                    for (OrgDto orgDto3 : orgDtoList2) {
                        haveOrgList.add(orgDto3.getOrgId());
                    }
                } else {
                    OrgDto orgDto1 = new OrgDto();
                    orgDto1.setOrgId(orgDto.getParentOrgId());
                    List<OrgDto> orgDtoList1 = orgV1InnerServiceSMOImpl.queryOrgs(orgDto1);
                    findCompany(haveOrgList, orgDtoList1);

                    //下一个级别
                    if (!"2".equals(orgDto.getOrgLevel())) {
                        OrgDto orgDto3 = new OrgDto();
                        orgDto3.setParentOrgId(orgDto.getOrgId());
                        List<OrgDto> orgDtoList2 = orgV1InnerServiceSMOImpl.queryOrgs(orgDto3);
                        for (OrgDto orgDto4 : orgDtoList2) {
                            haveOrgList.add(orgDto4.getOrgId());
                        }
                    }

                }

            }
        }


    }

}
