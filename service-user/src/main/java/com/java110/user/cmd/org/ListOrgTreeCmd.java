package com.java110.user.cmd.org;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.doc.annotation.*;
import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.dto.org.OrgDto;
import com.java110.dto.org.OrgStaffRelDto;
import com.java110.dto.org.OrgTreeDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.intf.user.IOrgStaffRelInnerServiceSMO;
import com.java110.intf.user.IOrgV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Java110CmdDoc(title = "查询组织",
        description = "主要用于添加员工时的组织ID 可以通过此接口查询， 此接口返回为树形接口，请其他系统用树形的方式展示数据，具体请参考物业系统组织架构展示方式",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/org.listOrgTree",
        resource = "userDoc",
        author = "吴学文",
        serviceCode = "org.listOrgTree",
        seq = 8
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "orgId", length = 30, remark = "组织ID，非必填"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
                @Java110ParamDoc(name = "data", type = "Object", remark = "有效数据"),
                @Java110ParamDoc(parentNodeName = "data",name = "allOrgName", type = "String", remark = "组织名称"),
                @Java110ParamDoc(parentNodeName = "data",name = "id", type = "String", remark = "组织ID"),
                @Java110ParamDoc(parentNodeName = "data",name = "children", type = "Array", remark = "子节点"),
                @Java110ParamDoc(parentNodeName = "children",name = "allOrgName", type = "String", remark = "组织名称"),
                @Java110ParamDoc(parentNodeName = "children",name = "id", type = "String", remark = "组织ID"),
        }
)

@Java110ExampleDoc(
        reqBody="http://{ip}:{port}/app/org.listOrgTree",
        resBody="{\"code\":0,\"data\":{\"allOrgName\":\"演示物业\",\"children\":[{\"allOrgName\":\"演示物业 / 软件部\",\"icon\":\"/img/org.png\",\"id\":\"102022091988250052\",\"parentId\":\"842022081548770433\",\"text\":\"软件部\"}],\"icon\":\"/img/org.png\",\"id\":\"842022081548770433\",\"parentId\":\"-1\",\"text\":\"演示物业\"},\"msg\":\"成功\",\"page\":0,\"records\":1,\"rows\":0,\"total\":1}"
)

@Java110Cmd(serviceCode = "org.listOrgTree")
public class ListOrgTreeCmd extends Cmd {

    @Autowired
    private IOrgV1InnerServiceSMO orgV1InnerServiceSMOImpl;


    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Autowired
    private IOrgStaffRelInnerServiceSMO iOrgStaffRelInnerServiceSMO;


    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;


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
        String userId = context.getReqHeaders().get("user-id");
        if (StringUtil.isEmpty(storeId)) {
            storeId = reqJson.getString("storeId");
        }
        Assert.hasLength(storeId, "未包含商户信息");


        OrgDto orgDto = new OrgDto();
        orgDto.setStoreId(storeId);

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
                    orgDto.setOrgIds(haveOrgList.toArray(new String[haveOrgList.size()]));//当前人虽归属的二级组织信息
                }
            }
        }

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
            if (orgDto.getParentOrgId().equals(parentOrgDto.getId())) {//二级
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
