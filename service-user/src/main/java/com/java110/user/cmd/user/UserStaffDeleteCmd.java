package com.java110.user.cmd.user;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.doc.annotation.*;
import com.java110.dto.privilege.PrivilegeUserDto;
import com.java110.dto.repair.RepairTypeUserDto;
import com.java110.intf.community.IRepairTypeUserInnerServiceSMO;
import com.java110.intf.community.IRepairTypeUserV1InnerServiceSMO;
import com.java110.intf.store.IStoreUserV1InnerServiceSMO;
import com.java110.intf.user.IPrivilegeUserV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.privilege.PrivilegeUserPo;
import com.java110.po.repair.RepairTypeUserPo;
import com.java110.po.store.StoreUserPo;
import com.java110.po.user.UserPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;


@Java110CmdDoc(title = "删除员工",
        description = "外部系统通过删除员工接口 删除员工 注意需要物业管理员账号登录，因为不需要传storeId 是根据管理员登录信息获取的",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/user.staff.delete",
        resource = "userDoc",
        author = "吴学文",
        serviceCode = "user.staff.delete",
        seq = 5
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "userId", length = 30, remark = "员工ID"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
        }
)

@Java110ExampleDoc(
        reqBody="{\"userId\":\"123123\"}",
        resBody="{'code':0,'msg':'成功'"
)

@Java110Cmd(serviceCode = "user.staff.delete")
public class UserStaffDeleteCmd extends Cmd {


    @Autowired
    private IRepairTypeUserInnerServiceSMO repairTypeUserInnerServiceSMOImpl;

    @Autowired
    private IStoreUserV1InnerServiceSMO storeUserV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IPrivilegeUserV1InnerServiceSMO privilegeUserV1InnerServiceSMOImpl;

    @Autowired
    private IRepairTypeUserV1InnerServiceSMO repairTypeUserV1InnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        Assert.jsonObjectHaveKey(reqJson, "storeId", "请求参数中未包含storeId 节点，请确认");
        Assert.jsonObjectHaveKey(reqJson, "userId", "请求参数中未包含userId 节点，请确认");


    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        deleteStaff(reqJson);

        //删除用户
        deleteUser(reqJson);

        //删除报修设置
        RepairTypeUserDto repairTypeUserDto = new RepairTypeUserDto();
        repairTypeUserDto.setStaffId(reqJson.getString("userId"));
        repairTypeUserDto.setStatusCd("0");
        List<RepairTypeUserDto> repairTypeUserDtoList = repairTypeUserInnerServiceSMOImpl.queryRepairTypeUsers(repairTypeUserDto);
        if (repairTypeUserDtoList != null && repairTypeUserDtoList.size() > 0) {
            for (RepairTypeUserDto repairTypeUserDto1 : repairTypeUserDtoList) {
                JSONObject typeUserJson1 = (JSONObject) JSONObject.toJSON(repairTypeUserDto1);
                deleteRepairTypeUser(typeUserJson1);
            }
        }

        //赋权
        deleteUserPrivilege(reqJson);
    }

    /**
     * 删除用户权限
     *
     * @param paramInJson
     */
    private void deleteUserPrivilege(JSONObject paramInJson) {

        PrivilegeUserDto privilegeUserDto = new PrivilegeUserDto();
        privilegeUserDto.setUserId(paramInJson.getString("userId"));
        List<PrivilegeUserDto> privilegeUserDtos = privilegeUserV1InnerServiceSMOImpl.queryPrivilegeUsers(privilegeUserDto);

        if (privilegeUserDtos == null || privilegeUserDtos.size() < 1) {
            return;
        }

        for (PrivilegeUserDto tmpPrivilegeUserDto : privilegeUserDtos) {
            PrivilegeUserPo privilegeUserPo = new PrivilegeUserPo();
            privilegeUserPo.setPuId(tmpPrivilegeUserDto.getPuId());
            int flag = privilegeUserV1InnerServiceSMOImpl.deletePrivilegeUser(privilegeUserPo);
            if (flag < 1) {
                throw new CmdException("删除员工失败");
            }
        }
    }

    /**
     * 删除商户
     *
     * @param paramInJson
     * @return
     */
    public void deleteStaff(JSONObject paramInJson) {

        JSONObject businessStoreUser = new JSONObject();
        businessStoreUser.put("storeId", paramInJson.getString("storeId"));
        businessStoreUser.put("userId", paramInJson.getString("userId"));


        StoreUserPo storeUserPo = BeanConvertUtil.covertBean(businessStoreUser, StoreUserPo.class);

        int flag = storeUserV1InnerServiceSMOImpl.deleteStoreUser(storeUserPo);

        if (flag < 1) {
            throw new CmdException("删除员工失败");
        }
    }

    /**
     * 删除商户
     *
     * @param paramInJson
     * @return
     */
    public void deleteUser(JSONObject paramInJson) {
        //校验json 格式中是否包含 name,email,levelCd,tel
        JSONObject businessStoreUser = new JSONObject();
        businessStoreUser.put("userId", paramInJson.getString("userId"));

        UserPo userPo = BeanConvertUtil.covertBean(businessStoreUser, UserPo.class);
        int flag = userV1InnerServiceSMOImpl.deleteUser(userPo);

        if (flag < 1) {
            throw new CmdException("删除员工失败");
        }
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void deleteRepairTypeUser(JSONObject paramInJson) {

        RepairTypeUserPo repairTypeUserPo = BeanConvertUtil.covertBean(paramInJson, RepairTypeUserPo.class);
        //super.update(dataFlowContext, repairTypeUserPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_REPAIR_TYPE_USER);
        int flag = repairTypeUserV1InnerServiceSMOImpl.deleteRepairTypeUser(repairTypeUserPo);
        if (flag < 1) {
            throw new CmdException("删除员工失败");
        }
    }
}
