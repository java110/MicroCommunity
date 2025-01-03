package com.java110.user.cmd.user;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.doc.annotation.*;
import com.java110.dto.attendance.AttendanceClassesStaffDto;
import com.java110.dto.classes.ScheduleClassesStaffDto;
import com.java110.dto.data.DataPrivilegeStaffDto;
import com.java110.dto.inspection.InspectionPlanStaffDto;
import com.java110.dto.maintainance.MaintainancePlanStaffDto;
import com.java110.dto.privilege.PrivilegeUserDto;
import com.java110.dto.repair.RepairTypeUserDto;
import com.java110.dto.user.StaffAppAuthDto;
import com.java110.intf.community.IInspectionPlanStaffV1InnerServiceSMO;
import com.java110.intf.community.IMaintainancePlanStaffV1InnerServiceSMO;
import com.java110.intf.community.IRepairTypeUserInnerServiceSMO;
import com.java110.intf.community.IRepairTypeUserV1InnerServiceSMO;
import com.java110.intf.store.IScheduleClassesStaffV1InnerServiceSMO;
import com.java110.intf.store.IStoreUserV1InnerServiceSMO;
import com.java110.intf.user.*;
import com.java110.po.attendance.AttendanceClassesStaffPo;
import com.java110.po.classes.ScheduleClassesStaffPo;
import com.java110.po.inspection.InspectionPlanStaffPo;
import com.java110.po.maintainance.MaintainancePlanStaffPo;
import com.java110.po.privilege.DataPrivilegeStaffPo;
import com.java110.po.privilege.PrivilegeUserPo;
import com.java110.po.repair.RepairTypeUserPo;
import com.java110.po.store.StoreUserPo;
import com.java110.po.user.StaffAppAuthPo;
import com.java110.po.user.UserPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
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
        reqBody = "{\"userId\":\"123123\"}",
        resBody = "{'code':0,'msg':'成功'"
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

    @Autowired
    private IScheduleClassesStaffV1InnerServiceSMO scheduleClassesStaffV1InnerServiceSMOImpl;

    @Autowired
    private IAttendanceClassesStaffV1InnerServiceSMO attendanceClassesStaffV1InnerServiceSMOImpl;

    @Autowired
    private IInspectionPlanStaffV1InnerServiceSMO inspectionPlanStaffV1InnerServiceSMOImpl;

    @Autowired
    private IStaffAppAuthInnerServiceSMO staffAppAuthInnerServiceSMOImpl;

    @Autowired
    private IDataPrivilegeStaffV1InnerServiceSMO dataPrivilegeStaffV1InnerServiceSMOImpl;

    @Autowired
    private IMaintainancePlanStaffV1InnerServiceSMO maintainancePlanStaffV1InnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        Assert.jsonObjectHaveKey(reqJson, "storeId", "请求参数中未包含storeId 节点，请确认");
        Assert.jsonObjectHaveKey(reqJson, "userId", "请求参数中未包含userId 节点，请确认");


    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        //todo 删除员工信息
        deleteStaff(reqJson);

        //todo 删除用户
        deleteUser(reqJson);

        //todo 删除报修设置
        RepairTypeUserDto repairTypeUserDto = new RepairTypeUserDto();
        repairTypeUserDto.setStaffId(reqJson.getString("userId"));
        repairTypeUserDto.setStatusCd("0");
        List<RepairTypeUserDto> repairTypeUserDtoList = repairTypeUserInnerServiceSMOImpl.queryRepairTypeUsers(repairTypeUserDto);
        if (!ListUtil.isNull(repairTypeUserDtoList)) {
            for (RepairTypeUserDto tmpRepairTypeUserDto : repairTypeUserDtoList) {
                JSONObject typeUserJson1 = (JSONObject) JSONObject.toJSON(tmpRepairTypeUserDto);
                deleteRepairTypeUser(typeUserJson1);
            }
        }
        //todo 删除员工排班数据
        deleteScheduleClassesStaff(reqJson);

        //todo 删除员工考勤组
        deleteAttendanceClassesStaff(reqJson);

        // todo 删除巡检员工
        deleteInspectionPlanStaff(reqJson);

        //todo 赋权
        deleteUserPrivilege(reqJson);

        // todo 删除员工认证
        deleteStaffAppAuth(reqJson);

        // todo 删除数据权限
        deleteStaffDataPri(reqJson);

        //todo 删除保养员工
        deleteMaintainanceStaff(reqJson);
    }



    /**
     * 删除数据权限
     * @param reqJson
     */
    private void deleteStaffDataPri(JSONObject reqJson) {

        DataPrivilegeStaffDto dataPrivilegeStaffDto = new DataPrivilegeStaffDto();
        dataPrivilegeStaffDto.setStaffId(reqJson.getString("userId"));
        List<DataPrivilegeStaffDto> dataPrivilegeStaffDtos
                = dataPrivilegeStaffV1InnerServiceSMOImpl.queryDataPrivilegeStaffs(dataPrivilegeStaffDto);
        if(ListUtil.isNull(dataPrivilegeStaffDtos)){
            return;
        }
        for(DataPrivilegeStaffDto tmpDataPrivilegeStaffDto:dataPrivilegeStaffDtos){

            DataPrivilegeStaffPo dataPrivilegeStaffPo = new DataPrivilegeStaffPo();
            dataPrivilegeStaffPo.setDpsId(tmpDataPrivilegeStaffDto.getDpsId());
            dataPrivilegeStaffV1InnerServiceSMOImpl.deleteDataPrivilegeStaff(dataPrivilegeStaffPo);
        }
    }

    private void deleteStaffAppAuth(JSONObject reqJson) {

        StaffAppAuthDto staffAppAuthDto = new StaffAppAuthDto();
        staffAppAuthDto.setStaffId(reqJson.getString("userId"));
        List<StaffAppAuthDto> staffAppAuthDtos = staffAppAuthInnerServiceSMOImpl.queryStaffAppAuths(staffAppAuthDto);
        if(ListUtil.isNull(staffAppAuthDtos)){
            return;
        }
        for(StaffAppAuthDto tmpStaffAppAuthDto:staffAppAuthDtos){
            StaffAppAuthPo staffAppAuthPo = new StaffAppAuthPo();
            staffAppAuthPo.setAuId(tmpStaffAppAuthDto.getAuId());
            staffAppAuthInnerServiceSMOImpl.deleteStaffAppAuth(staffAppAuthPo);
        }
    }

    private void deleteInspectionPlanStaff(JSONObject reqJson) {

        InspectionPlanStaffDto inspectionPlanStaffDto = new InspectionPlanStaffDto();
        inspectionPlanStaffDto.setStaffId(reqJson.getString("userId"));
        List<InspectionPlanStaffDto> inspectionPlanStaffDtos
                = inspectionPlanStaffV1InnerServiceSMOImpl.queryInspectionPlanStaffs(inspectionPlanStaffDto);
        if(ListUtil.isNull(inspectionPlanStaffDtos)){
            return;
        }

        for(InspectionPlanStaffDto tmpInspectionPlanStaffDto:inspectionPlanStaffDtos){
            InspectionPlanStaffPo inspectionPlanStaffPo = new InspectionPlanStaffPo();
            inspectionPlanStaffPo.setIpStaffId(tmpInspectionPlanStaffDto.getIpStaffId());
            inspectionPlanStaffV1InnerServiceSMOImpl.deleteInspectionPlanStaff(inspectionPlanStaffPo);
        }
    }

    /**
     * 删除保养员工
     * @param reqJson
     */
    private void deleteMaintainanceStaff(JSONObject reqJson) {
        MaintainancePlanStaffDto maintainancePlanStaffDto = new MaintainancePlanStaffDto();
        maintainancePlanStaffDto.setStaffId(reqJson.getString("userId"));
        List<MaintainancePlanStaffDto> maintainancePlanStaffDtos
                = maintainancePlanStaffV1InnerServiceSMOImpl.queryMaintainancePlanStaffs(maintainancePlanStaffDto);
        if(ListUtil.isNull(maintainancePlanStaffDtos)){
            return;
        }

        for(MaintainancePlanStaffDto tmpMaintainancePlanStaffDto:maintainancePlanStaffDtos){
            MaintainancePlanStaffPo maintainancePlanStaffPo = new MaintainancePlanStaffPo();
            maintainancePlanStaffPo.setMpsId(tmpMaintainancePlanStaffDto.getMpsId());
            maintainancePlanStaffV1InnerServiceSMOImpl.deleteMaintainancePlanStaff(maintainancePlanStaffPo);
        }
    }

    private void deleteAttendanceClassesStaff(JSONObject reqJson) {

        AttendanceClassesStaffDto attendanceClassesStaffDto = new AttendanceClassesStaffDto();
        attendanceClassesStaffDto.setStaffId(reqJson.getString("userId"));
        List<AttendanceClassesStaffDto> attendanceClassesStaffDtos
                = attendanceClassesStaffV1InnerServiceSMOImpl.queryAttendanceClassesStaffs(attendanceClassesStaffDto);
        if(ListUtil.isNull(attendanceClassesStaffDtos)){
            return;
        }
        for(AttendanceClassesStaffDto tmpAttendanceClassesStaffDto:attendanceClassesStaffDtos){
            AttendanceClassesStaffPo attendanceClassesStaffPo = new AttendanceClassesStaffPo();
            attendanceClassesStaffPo.setCsId(tmpAttendanceClassesStaffDto.getCsId());
            attendanceClassesStaffV1InnerServiceSMOImpl.deleteAttendanceClassesStaff(attendanceClassesStaffPo);
        }
    }

    /**
     * 删除考勤员工
     * @param reqJson
     */
    private void deleteScheduleClassesStaff(JSONObject reqJson) {
        ScheduleClassesStaffDto scheduleClassesStaffDto = new ScheduleClassesStaffDto();
        scheduleClassesStaffDto.setStaffId(reqJson.getString("userId"));
        List<ScheduleClassesStaffDto> scheduleClassesStaffDtos
                = scheduleClassesStaffV1InnerServiceSMOImpl.queryScheduleClassesStaffs(scheduleClassesStaffDto);
        if(ListUtil.isNull(scheduleClassesStaffDtos)){
            return;
        }
        for (ScheduleClassesStaffDto tmpScheduleClassesStaffDto : scheduleClassesStaffDtos) {
            ScheduleClassesStaffPo scheduleClassesStaffPo = new ScheduleClassesStaffPo();
            scheduleClassesStaffPo.setScsId(tmpScheduleClassesStaffDto.getScsId());
            scheduleClassesStaffV1InnerServiceSMOImpl.deleteScheduleClassesStaff(scheduleClassesStaffPo);
        }

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

        if (ListUtil.isNull(privilegeUserDtos)) {
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
