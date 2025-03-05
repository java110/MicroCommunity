package com.java110.user.cmd.user;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.Environment;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.attendance.AttendanceClassesStaffDto;
import com.java110.dto.classes.ScheduleClassesStaffDto;
import com.java110.dto.data.DataPrivilegeStaffDto;
import com.java110.dto.inspection.InspectionPlanStaffDto;
import com.java110.dto.maintainance.MaintainancePlanStaffDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.privilege.PrivilegeUserDto;
import com.java110.dto.repair.RepairTypeUserDto;
import com.java110.dto.store.StoreUserDto;
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
import com.java110.po.owner.OwnerAppUserPo;
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

@Java110Cmd(serviceCode = "user.deleteSystemUser")
public class DeleteSystemUserCmd extends Cmd {


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

    @Autowired
    private IOwnerAppUserV1InnerServiceSMO ownerAppUserV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        super.validateAdmin(context);
        Environment.isDevEnv();
        Assert.hasKeyAndValue(reqJson, "staffId", "未包含用户ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        // 删除员工信息
        deleteStaff(reqJson);

        //todo 删除用户
        deleteUser(reqJson);

        // 解绑业主
        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setUserId(reqJson.getString("staffId"));
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserV1InnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);
        if (ListUtil.isNull(ownerAppUserDtos)) {
            return;
        }
        OwnerAppUserPo ownerAppUserPo = null;
        for (OwnerAppUserDto tmpOwnerAppUserDto : ownerAppUserDtos) {
            ownerAppUserPo = BeanConvertUtil.covertBean(tmpOwnerAppUserDto, OwnerAppUserPo.class);
            ownerAppUserV1InnerServiceSMOImpl.deleteOwnerAppUser(ownerAppUserPo);
        }
    }


    /**
     * 删除商户
     *
     * @param paramInJson
     * @return
     */
    public void deleteUser(JSONObject paramInJson) {
        UserPo userPo = new UserPo();
        userPo.setUserId(paramInJson.getString("staffId"));
        int flag = userV1InnerServiceSMOImpl.deleteUser(userPo);

        if (flag < 1) {
            throw new CmdException("删除员工失败");
        }
    }

    /**
     * 删除商户
     *
     * @param reqJson
     * @return
     */
    public void deleteStaff(JSONObject reqJson) {

        StoreUserDto storeUserDto = new StoreUserDto();
        storeUserDto.setUserId(reqJson.getString("staffId"));
        List<StoreUserDto> storeUserDtos = storeUserV1InnerServiceSMOImpl.queryStoreUsers(storeUserDto);
        if (ListUtil.isNull(storeUserDtos)) {
            return;
        }
        StoreUserPo storeUserPo = null;
        for (StoreUserDto tmpStoreUserDto : storeUserDtos) {
            storeUserPo = new StoreUserPo();
            storeUserPo.setStoreUserId(tmpStoreUserDto.getStoreUserId());
            storeUserPo.setUserId(tmpStoreUserDto.getUserId());
            storeUserPo.setStoreId(tmpStoreUserDto.getStoreId());
            storeUserV1InnerServiceSMOImpl.deleteStoreUser(storeUserPo);
        }

        //todo 删除报修设置
        RepairTypeUserDto repairTypeUserDto = new RepairTypeUserDto();
        repairTypeUserDto.setStaffId(reqJson.getString("staffId"));
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
     *
     * @param reqJson
     */
    private void deleteStaffDataPri(JSONObject reqJson) {

        DataPrivilegeStaffDto dataPrivilegeStaffDto = new DataPrivilegeStaffDto();
        dataPrivilegeStaffDto.setStaffId(reqJson.getString("staffId"));
        List<DataPrivilegeStaffDto> dataPrivilegeStaffDtos
                = dataPrivilegeStaffV1InnerServiceSMOImpl.queryDataPrivilegeStaffs(dataPrivilegeStaffDto);
        if (ListUtil.isNull(dataPrivilegeStaffDtos)) {
            return;
        }
        for (DataPrivilegeStaffDto tmpDataPrivilegeStaffDto : dataPrivilegeStaffDtos) {

            DataPrivilegeStaffPo dataPrivilegeStaffPo = new DataPrivilegeStaffPo();
            dataPrivilegeStaffPo.setDpsId(tmpDataPrivilegeStaffDto.getDpsId());
            dataPrivilegeStaffV1InnerServiceSMOImpl.deleteDataPrivilegeStaff(dataPrivilegeStaffPo);
        }
    }

    private void deleteStaffAppAuth(JSONObject reqJson) {

        StaffAppAuthDto staffAppAuthDto = new StaffAppAuthDto();
        staffAppAuthDto.setStaffId(reqJson.getString("staffId"));
        List<StaffAppAuthDto> staffAppAuthDtos = staffAppAuthInnerServiceSMOImpl.queryStaffAppAuths(staffAppAuthDto);
        if (ListUtil.isNull(staffAppAuthDtos)) {
            return;
        }
        for (StaffAppAuthDto tmpStaffAppAuthDto : staffAppAuthDtos) {
            StaffAppAuthPo staffAppAuthPo = new StaffAppAuthPo();
            staffAppAuthPo.setAuId(tmpStaffAppAuthDto.getAuId());
            staffAppAuthInnerServiceSMOImpl.deleteStaffAppAuth(staffAppAuthPo);
        }
    }

    private void deleteInspectionPlanStaff(JSONObject reqJson) {

        InspectionPlanStaffDto inspectionPlanStaffDto = new InspectionPlanStaffDto();
        inspectionPlanStaffDto.setStaffId(reqJson.getString("staffId"));
        List<InspectionPlanStaffDto> inspectionPlanStaffDtos
                = inspectionPlanStaffV1InnerServiceSMOImpl.queryInspectionPlanStaffs(inspectionPlanStaffDto);
        if (ListUtil.isNull(inspectionPlanStaffDtos)) {
            return;
        }

        for (InspectionPlanStaffDto tmpInspectionPlanStaffDto : inspectionPlanStaffDtos) {
            InspectionPlanStaffPo inspectionPlanStaffPo = new InspectionPlanStaffPo();
            inspectionPlanStaffPo.setIpStaffId(tmpInspectionPlanStaffDto.getIpStaffId());
            inspectionPlanStaffV1InnerServiceSMOImpl.deleteInspectionPlanStaff(inspectionPlanStaffPo);
        }
    }

    /**
     * 删除保养员工
     *
     * @param reqJson
     */
    private void deleteMaintainanceStaff(JSONObject reqJson) {
        MaintainancePlanStaffDto maintainancePlanStaffDto = new MaintainancePlanStaffDto();
        maintainancePlanStaffDto.setStaffId(reqJson.getString("staffId"));
        List<MaintainancePlanStaffDto> maintainancePlanStaffDtos
                = maintainancePlanStaffV1InnerServiceSMOImpl.queryMaintainancePlanStaffs(maintainancePlanStaffDto);
        if (ListUtil.isNull(maintainancePlanStaffDtos)) {
            return;
        }

        for (MaintainancePlanStaffDto tmpMaintainancePlanStaffDto : maintainancePlanStaffDtos) {
            MaintainancePlanStaffPo maintainancePlanStaffPo = new MaintainancePlanStaffPo();
            maintainancePlanStaffPo.setMpsId(tmpMaintainancePlanStaffDto.getMpsId());
            maintainancePlanStaffV1InnerServiceSMOImpl.deleteMaintainancePlanStaff(maintainancePlanStaffPo);
        }
    }

    private void deleteAttendanceClassesStaff(JSONObject reqJson) {

        AttendanceClassesStaffDto attendanceClassesStaffDto = new AttendanceClassesStaffDto();
        attendanceClassesStaffDto.setStaffId(reqJson.getString("staffId"));
        List<AttendanceClassesStaffDto> attendanceClassesStaffDtos
                = attendanceClassesStaffV1InnerServiceSMOImpl.queryAttendanceClassesStaffs(attendanceClassesStaffDto);
        if (ListUtil.isNull(attendanceClassesStaffDtos)) {
            return;
        }
        for (AttendanceClassesStaffDto tmpAttendanceClassesStaffDto : attendanceClassesStaffDtos) {
            AttendanceClassesStaffPo attendanceClassesStaffPo = new AttendanceClassesStaffPo();
            attendanceClassesStaffPo.setCsId(tmpAttendanceClassesStaffDto.getCsId());
            attendanceClassesStaffV1InnerServiceSMOImpl.deleteAttendanceClassesStaff(attendanceClassesStaffPo);
        }
    }

    /**
     * 删除考勤员工
     *
     * @param reqJson
     */
    private void deleteScheduleClassesStaff(JSONObject reqJson) {
        ScheduleClassesStaffDto scheduleClassesStaffDto = new ScheduleClassesStaffDto();
        scheduleClassesStaffDto.setStaffId(reqJson.getString("staffId"));
        List<ScheduleClassesStaffDto> scheduleClassesStaffDtos
                = scheduleClassesStaffV1InnerServiceSMOImpl.queryScheduleClassesStaffs(scheduleClassesStaffDto);
        if (ListUtil.isNull(scheduleClassesStaffDtos)) {
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
        privilegeUserDto.setUserId(paramInJson.getString("staffId"));
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
