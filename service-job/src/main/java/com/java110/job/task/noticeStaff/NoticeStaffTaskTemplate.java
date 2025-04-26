package com.java110.job.task.noticeStaff;

import com.java110.dto.community.CommunityDto;
import com.java110.dto.community.CommunityMemberDto;
import com.java110.dto.inspection.InspectionTaskDetailDto;
import com.java110.dto.inspection.InspectionTaskDto;
import com.java110.dto.notice.NoticeStaffDto;
import com.java110.dto.privilege.BasePrivilegeDto;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.dto.task.TaskDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.community.*;
import com.java110.intf.order.IPrivilegeInnerServiceSMO;
import com.java110.intf.store.ISmallWeChatInnerServiceSMO;
import com.java110.intf.store.ISmallWechatAttrInnerServiceSMO;
import com.java110.intf.user.IStaffAppAuthInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.job.msgNotify.IMsgNotify;
import com.java110.job.msgNotify.MsgNotifyFactory;
import com.java110.job.quartz.TaskSystemQuartz;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * 通知员工工单处理
 */
@Component
public class NoticeStaffTaskTemplate extends TaskSystemQuartz {

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IInspectionPlanInnerServiceSMO inspectionPlanInnerServiceSMOImpl;

    @Autowired
    private IInspectionPlanStaffInnerServiceSMO inspectionPlanStaffInnerServiceSMOImpl;

    @Autowired
    private IInspectionRoutePointRelInnerServiceSMO inspectionRoutePointRelInnerServiceSMOImpl;


    @Autowired
    private IInspectionTaskInnerServiceSMO inspectionTaskInnerServiceSMOImpl;

    @Autowired
    private IInspectionTaskDetailInnerServiceSMO inspectionTaskDetailInnerServiceSMOImpl;

    @Autowired
    private IPrivilegeInnerServiceSMO privilegeInnerServiceSMO;

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMOImpl;

    @Autowired
    private IRepairUserInnerServiceSMO repairUserInnerServiceSMOImpl;


    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMO;


    @Override
    protected void process(TaskDto taskDto) throws Exception {
        logger.debug("开始执行微信模板信息推送" + taskDto.toString());

        // 获取小区
        List<CommunityDto> communityDtos = getAllCommunity();

        for (CommunityDto communityDto : communityDtos) {
            try {
                //todo 巡检 任务生成提醒
                publishStaffInspectionTask(taskDto, communityDto);
            } catch (Exception e) {
                logger.error("巡检 任务生成提醒", e);
            }
            try {
                //todo 巡检情况通知
                notifyTodayInspection(communityDto);
            } catch (Exception e) {
                logger.error("巡检情况通知", e);
            }

            try {
                //todo 报修快超时通知
                notifyRepairTimeout(communityDto);
            } catch (Exception e) {
                logger.error("巡检情况通知", e);
            }
        }
    }

    /**
     * 报修超时通知
     *
     * @param communityDto
     */
    private void notifyRepairTimeout(CommunityDto communityDto) {

        RepairDto repairDto = new RepairDto();
        repairDto.setCommunityId(communityDto.getCommunityId());
        repairDto.setStatess(new String[]{RepairDto.STATE_WAIT, RepairDto.STATE_TAKING});
        List<RepairDto> repairDtos = repairInnerServiceSMOImpl.queryRepairs(repairDto);
        if (ListUtil.isNull(repairDtos)) {
            return;
        }

        RepairUserDto repairUserDto;
        List<RepairUserDto> repairUserDtos;
        IMsgNotify msgNotify = MsgNotifyFactory.getMsgNotify(MsgNotifyFactory.NOTIFY_WAY_WECHAT);
        for (RepairDto tRepairDto : repairDtos) {
            if (!NoticeTaskUtil.isRepairAboutTimeout(tRepairDto)) {
                continue;
            }
            repairUserDto = new RepairUserDto();
            repairUserDto.setRepairId(tRepairDto.getRepairId());
            repairUserDto.setState(RepairUserDto.STATE_DOING);
            repairUserDtos = repairUserInnerServiceSMOImpl.queryRepairUsers(repairUserDto);
            if (ListUtil.isNull(repairUserDtos)) {
                continue;
            }
            try {
                msgNotify.sendStaffMsg(
                        new NoticeStaffDto(communityDto.getCommunityId(),
                                repairUserDtos.get(0).getStaffId(),
                                tRepairDto.getRepairName() + "提交报修单快超时了",
                                "系统管理员"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //todo 提示已经超时的工单
        for (RepairDto tRepairDto : repairDtos) {
            if (!NoticeTaskUtil.isRepairTimeout(tRepairDto)) {
                continue;
            }
            repairUserDto = new RepairUserDto();
            repairUserDto.setRepairId(tRepairDto.getRepairId());
            repairUserDto.setState(RepairUserDto.STATE_DOING);
            repairUserDtos = repairUserInnerServiceSMOImpl.queryRepairUsers(repairUserDto);
            if (ListUtil.isNull(repairUserDtos)) {
                continue;
            }
            try {
                msgNotify.sendStaffMsg(
                        new NoticeStaffDto(communityDto.getCommunityId(),
                                repairUserDtos.get(0).getStaffId(),
                                tRepairDto.getRepairName() + "提交报修单已超时",
                                "系统管理员"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 今日巡检情况通知
     *
     * @param communityDto
     */
    private void notifyTodayInspection(CommunityDto communityDto) {

        if (!NoticeTaskUtil.isNotifyTodayInspection(communityDto)) {
            return;
        }

        CommunityMemberDto communityMemberDto = new CommunityMemberDto();
        communityMemberDto.setCommunityId(communityDto.getCommunityId());
        communityMemberDto.setAuditStatusCd(CommunityMemberDto.AUDIT_STATUS_NORMAL);
        communityMemberDto.setMemberTypeCd(CommunityMemberDto.MEMBER_TYPE_PROPERTY);
        List<CommunityMemberDto> communityMemberDtos = communityInnerServiceSMOImpl.getCommunityMembers(communityMemberDto);
        Assert.listOnlyOne(communityMemberDtos, "小区没有物业公司");

        BasePrivilegeDto basePrivilegeDto = new BasePrivilegeDto();
        basePrivilegeDto.setResource("/todayInspectionNotifyStaff");
        basePrivilegeDto.setStoreId(communityMemberDtos.get(0).getMemberId());
        basePrivilegeDto.setCommunityId(communityDto.getCommunityId());
        List<UserDto> userDtos = privilegeInnerServiceSMO.queryPrivilegeUsers(basePrivilegeDto);
        if (ListUtil.isNull(userDtos)) {
            return;
        }

        IMsgNotify msgNotify = MsgNotifyFactory.getMsgNotify(MsgNotifyFactory.NOTIFY_WAY_WECHAT);
        String wechatUrl = MappingCache.getValue(MappingConstant.URL_DOMAIN, "STAFF_WECHAT_URL");
        if (wechatUrl.endsWith("/")) {
            wechatUrl += "#/pages/inspection/inspectionTodayReport";
        } else {
            wechatUrl += "/#/pages/inspection/inspectionTodayReport";
        }
        for (UserDto userDto : userDtos) {
            //根据 userId 查询到openId
            try {
                msgNotify.sendStaffMsg(
                        new NoticeStaffDto(communityDto.getCommunityId(),
                                userDto.getUserId(),
                                communityDto.getName() + "今日巡检情况",
                                "系统管理员",
                                wechatUrl));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 巡检任务
     *
     * @param taskDto
     * @param communityDto
     */
    private void publishStaffInspectionTask(TaskDto taskDto, CommunityDto communityDto) {

        InspectionTaskDetailDto inspectionTaskDetailDto = new InspectionTaskDetailDto();
        inspectionTaskDetailDto.setCommunityId(communityDto.getCommunityId());
        inspectionTaskDetailDto.setState(InspectionTaskDto.STATE_NO_START);
        inspectionTaskDetailDto.setQrCodeTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        inspectionTaskDetailDto.setSendFlag(InspectionTaskDetailDto.SEND_FLAG_N);
        List<InspectionTaskDetailDto> inspectionTaskDetailDtos = inspectionTaskDetailInnerServiceSMOImpl.queryInspectionTaskDetails(inspectionTaskDetailDto);
        for (InspectionTaskDetailDto tInspectionTaskDetailDto : inspectionTaskDetailDtos) {
            String startTime = tInspectionTaskDetailDto.getPointStartTime();
            startTime = DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_B) + " " + startTime + ":00";
            Date startDate = null;
            try {
                startDate = DateUtil.getDateFromString(startTime, DateUtil.DATE_FORMATE_STRING_A);
            } catch (ParseException e) {
                e.printStackTrace();
                startDate = new Date();
            }

            //还没有到时间
            if (startDate.getTime() > DateUtil.getCurrentDate().getTime()) {
                return;
            }

            IMsgNotify msgNotify = MsgNotifyFactory.getMsgNotify(MsgNotifyFactory.NOTIFY_WAY_WECHAT);
            String staffWechatUrl = MappingCache.getValue(MappingConstant.URL_DOMAIN, "STAFF_WECHAT_URL");
            staffWechatUrl = staffWechatUrl
                    + "pages/excuteOneQrCodeInspection/excuteOneQrCodeInspection?inspectionId="
                    + tInspectionTaskDetailDto.getInspectionId()
                    + "&inspectionName=" + tInspectionTaskDetailDto.getInspectionName()
                    + "&itemId=" + tInspectionTaskDetailDto.getItemId();
            try {
                msgNotify.sendStaffMsg(
                        new NoticeStaffDto(communityDto.getCommunityId(),
                                tInspectionTaskDetailDto.getPlanUserId(),
                                tInspectionTaskDetailDto.getInspectionName() + "未巡检，请及时巡检",
                                "系统管理员",
                                staffWechatUrl)
                );
            } catch (Exception e) {
                e.printStackTrace();
            }


            InspectionTaskDetailDto inspectionTaskDetailPo = new InspectionTaskDetailDto();
            inspectionTaskDetailPo.setTaskDetailId(tInspectionTaskDetailDto.getTaskDetailId());
            inspectionTaskDetailPo.setSendFlag(InspectionTaskDetailDto.SEND_FLAG_Y);
            inspectionTaskDetailPo.setStatusCd("0");
            inspectionTaskDetailInnerServiceSMOImpl.updateInspectionTaskDetail(inspectionTaskDetailPo);
        }

    }

}
