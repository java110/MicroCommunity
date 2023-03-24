package com.java110.user.cmd.user;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.doc.annotation.*;
import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.org.OrgDto;
import com.java110.dto.org.OrgStaffRelDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.intf.store.IOrgStaffRelV1InnerServiceSMO;
import com.java110.intf.user.IOrgStaffRelInnerServiceSMO;
import com.java110.intf.user.IOrgV1InnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.PinYinUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.api.staff.ApiStaffDataVo;
import com.java110.vo.api.staff.ApiStaffVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Java110CmdDoc(title = "查询员工",
        description = "根据员工 名称 手机号等查询员工信息",
        httpMethod = "get",
        url = "http://{ip}:{port}/app/query.staff.infos",
        resource = "userDoc",
        author = "吴学文",
        serviceCode = "query.staff.infos",
        seq = 6
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "orgId", length = 30, remark = "组织ID"),
        @Java110ParamDoc(name = "staffName", length = 64, remark = "员工名称"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
                @Java110ParamDoc(name = "staffs", type = "Object", remark = "有效数据"),
                @Java110ParamDoc(parentNodeName = "staffs",name = "userId", type = "String", remark = "员工ID"),
                @Java110ParamDoc(parentNodeName = "staffs",name = "name", type = "String", remark = "员工名称"),
                @Java110ParamDoc(parentNodeName = "staffs",name = "orgName", type = "String", remark = "组织"),
                @Java110ParamDoc(parentNodeName = "staffs",name = "orgId", type = "String", remark = "组织ID"),
                @Java110ParamDoc(parentNodeName = "staffs",name = "relCdName", type = "String", remark = "岗位"),
                @Java110ParamDoc(parentNodeName = "staffs",name = "tel", type = "String", remark = "手机号"),
                @Java110ParamDoc(parentNodeName = "staffs",name = "address", type = "String", remark = "地址"),
        }
)

@Java110ExampleDoc(
        reqBody="http://{ip}:{port}/app/query.staff.infos?page=1&row=10&orgId=102022091983800014&staffName=",
        resBody="{\"page\":0,\"records\":1,\"rows\":0,\"staffs\":[{\"address\":\"changzhi\",\"age\":0,\"email\":\"123@126.com\",\"initials\":\"H\",\"levelCd\":\"01\",\"name\":\"韩余\",\"orgId\":\"842022090369280030\",\"orgLevel\":\"1\",\"orgName\":\"晋邻通智慧社区 / 安保部\",\"parentOrgId\":\"-1\",\"parentTwoOrgId\":\"-1\",\"relCd\":\"1000\",\"relCdName\":\"普通员工\",\"relId\":\"842022091914570040\",\"sex\":\"1\",\"storeId\":\"102022090382820025\",\"tel\":\"15033304901\",\"urls\":[\"img/20220919/5d258a09-0cea-407b-9da5-93da98a93760.jpg\"],\"userId\":\"302022091908980026\",\"userName\":\"韩余\"},{\"address\":\"长治\",\"age\":0,\"email\":\"sxjltwy@163.com\",\"initials\":\"H\",\"levelCd\":\"01\",\"name\":\"韩俞\",\"orgId\":\"842022090369280030\",\"orgLevel\":\"1\",\"orgName\":\"晋邻通智慧社区 / 安保部\",\"parentOrgId\":\"-1\",\"parentTwoOrgId\":\"-1\",\"relCd\":\"4000\",\"relCdName\":\"部门组长\",\"relId\":\"842022091910820017\",\"sex\":\"0\",\"storeId\":\"102022090382820025\",\"tel\":\"17633305226\",\"urls\":[\"img/20220906/98ff9c83-606e-4f1a-8f40-fbcdbb00c784.jpg\"],\"userId\":\"302022090609300172\",\"userName\":\"韩俞\"},{\"address\":\"长治\",\"age\":0,\"email\":\"sxjltwy@163.com\",\"initials\":\"L\",\"levelCd\":\"01\",\"name\":\"李瑶\",\"orgId\":\"842022090369280030\",\"orgLevel\":\"1\",\"orgName\":\"晋邻通智慧社区 / 安保部\",\"parentOrgId\":\"-1\",\"parentTwoOrgId\":\"-1\",\"relCd\":\"1000\",\"relCdName\":\"普通员工\",\"relId\":\"842022091967800024\",\"sex\":\"1\",\"storeId\":\"102022090382820025\",\"tel\":\"13032266195\",\"urls\":[\"img/20220916/b7acab38-e72e-4e20-8804-9a1d5fc5d18f.jpg\"],\"userId\":\"302022090656600168\",\"userName\":\"李瑶\"},{\"address\":\"长治\",\"age\":0,\"email\":\"sxjltwy@163.com\",\"initials\":\"W\",\"levelCd\":\"01\",\"name\":\"王勇\",\"orgId\":\"842022090369280030\",\"orgLevel\":\"1\",\"orgName\":\"晋邻通智慧社区 / 安保部\",\"parentOrgId\":\"-1\",\"parentTwoOrgId\":\"-1\",\"relCd\":\"8000\",\"relCdName\":\"总公司总经理\",\"relId\":\"842022091936110033\",\"sex\":\"0\",\"storeId\":\"102022090382820025\",\"tel\":\"15034504949\",\"urls\":[\"img/20220906/1dff150d-4f59-40b3-a9be-d437026e1b00.jpg\"],\"userId\":\"302022090645120164\",\"userName\":\"王勇\"}],\"total\":1}"
)
@Java110Cmd(serviceCode = "query.staff.infos")
public class QueryStaffInfosCmd extends Cmd {

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
        Assert.hasKeyAndValue(reqJson, "page", "请求报文中未包含page节点");
        Assert.hasKeyAndValue(reqJson, "row", "请求报文中未包含rows节点");
        if (!reqJson.containsKey("storeId")) {
            String storeId = context.getReqHeaders().get("store-id");
            reqJson.put("storeId", storeId);
        }
        Assert.hasKeyAndValue(reqJson, "storeId", "请求报文中未包含storeId节点");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        UserDto userDto = BeanConvertUtil.covertBean(reqJson, UserDto.class);
        String userId = context.getReqHeaders().get("user-id");
        // 判断是不是管理员，管理员反馈 物业 的所角色
        UserDto userDto1 = new UserDto();
        userDto1.setUserId(userId);
        userDto1.setPage(1);
        userDto1.setRow(1);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto1);
        Assert.listOnlyOne(userDtos, "用户不存在");
        if (!UserDto.LEVEL_CD_ADMIN.equals(userDtos.get(0).getLevelCd())) {
            //默认只查看当前归属组织架构
            BasePrivilegeDto basePrivilegeDto = new BasePrivilegeDto();
            basePrivilegeDto.setResource("/viewAllOrganization");
            basePrivilegeDto.setUserId(reqJson.getString("userId"));
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
                    userDto.setOrgIds(haveOrgList.toArray(new String[haveOrgList.size()]));//当前人虽归属的二级组织信息
                }
            }
        }
        int count = userInnerServiceSMOImpl.getStaffCount(userDto);
        List<ApiStaffDataVo> staffs = new ArrayList<>();
        if (count > 0) {
            List<ApiStaffDataVo> staffList = BeanConvertUtil.covertBeanList(userInnerServiceSMOImpl.getStaffs(userDto), ApiStaffDataVo.class);
            for (ApiStaffDataVo apiStaffDataVo : staffList) {
                FileRelDto fileRelDto = new FileRelDto();
                fileRelDto.setObjId(apiStaffDataVo.getUserId());
                fileRelDto.setRelTypeCd("12000"); //员工图片
                List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
                if (fileRelDtos != null && fileRelDtos.size() > 0) {
                    List<String> urls = new ArrayList<>();
                    for (FileRelDto fileRel : fileRelDtos) {
                        urls.add(fileRel.getFileRealName());
                    }
                    apiStaffDataVo.setUrls(urls);
                }
                staffs.add(apiStaffDataVo);
            }
            refreshInitials(staffs);
            refreshOrgs(staffs, reqJson.getString("storeId"));
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

    private void refreshOrgs(List<ApiStaffDataVo> staffs, String storeId) {
        if (staffs == null || staffs.size() < 1) {
            return;
        }
        List<String> staffIds = new ArrayList<>();
        for (ApiStaffDataVo apiStaffDataVo : staffs) {
            staffIds.add(apiStaffDataVo.getUserId());
        }
        OrgDto orgDto = new OrgDto();
        orgDto.setStoreId(storeId);
        List<OrgDto> orgDtos = orgV1InnerServiceSMOImpl.queryOrgs(orgDto);
        if (orgDtos == null || orgDtos.size() < 1) {
            return;
        }
        OrgStaffRelDto orgStaffRelDto = new OrgStaffRelDto();
        orgStaffRelDto.setStaffIds(staffIds.toArray(new String[staffIds.size()]));
        orgStaffRelDto.setStoreId(storeId);
        List<OrgStaffRelDto> orgStaffRels = orgStaffRelV1InnerServiceSMOImpl.queryOrgStaffRels(orgStaffRelDto);
        if (orgStaffRels == null || orgStaffRels.size() < 1) {
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
                if(orgs == null || orgs.size() < 1){
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
