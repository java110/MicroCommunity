package com.java110.job.task.attendance;

import com.alibaba.fastjson.JSON;
import com.java110.core.factory.WechatFactory;
import com.java110.dto.CommunityMemberDto;
import com.java110.dto.attendanceClasses.AttendanceClassesTaskDetailDto;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.dto.smallWechatAttr.SmallWechatAttrDto;
import com.java110.dto.staffAppAuth.StaffAppAuthDto;
import com.java110.dto.task.TaskDto;
import com.java110.entity.wechat.Content;
import com.java110.entity.wechat.Data;
import com.java110.entity.wechat.PropertyFeeTemplateMessage;
import com.java110.intf.common.IAttendanceClassesTaskDetailInnerServiceSMO;
import com.java110.intf.community.*;
import com.java110.intf.store.ISmallWeChatInnerServiceSMO;
import com.java110.intf.store.ISmallWechatAttrInnerServiceSMO;
import com.java110.intf.user.IStaffAppAuthInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.job.quartz.TaskSystemQuartz;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;
import java.util.List;

/**
 * 推送员工考勤提示
 */
@Component
public class PublishStaffAttendanceTaskTemplate extends TaskSystemQuartz {


    @Autowired
    private IAttendanceClassesTaskDetailInnerServiceSMO attendanceClassesTaskDetailInnerServiceSMOImpl;

    @Autowired
    private ISmallWeChatInnerServiceSMO smallWeChatInnerServiceSMOImpl;

    @Autowired
    private ISmallWechatAttrInnerServiceSMO smallWechatAttrInnerServiceSMOImpl;

    @Autowired
    private IStaffAppAuthInnerServiceSMO staffAppAuthInnerServiceSMO;

    @Autowired
    private RestTemplate outRestTemplate;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMO;

    @Autowired
    private ICommunityMemberV1InnerServiceSMO communityMemberV1InnerServiceSMOImpl;


    //模板信息推送地址
    private static String sendMsgUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

    @Override
    protected void process(TaskDto taskDto) throws Exception {
        logger.debug("开始执行微信模板信息推送" + taskDto.toString());

        // 获取小区
        publishStaffInspectionTask(taskDto);

    }

    /**
     * 考勤任务
     *
     * @param taskDto
     */
    private void publishStaffInspectionTask(TaskDto taskDto) {
        AttendanceClassesTaskDetailDto attendanceClassesTaskDetailDto = new AttendanceClassesTaskDetailDto();
        attendanceClassesTaskDetailDto.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        Calendar endCal = Calendar.getInstance();
        endCal.add(Calendar.MINUTE, 5);
        attendanceClassesTaskDetailDto.setEndTime(DateUtil.getFormatTimeString(endCal.getTime(), DateUtil.DATE_FORMATE_STRING_A));
        attendanceClassesTaskDetailDto.setState(AttendanceClassesTaskDetailDto.STATE_WAIT);
        List<AttendanceClassesTaskDetailDto> inspectionTaskDetailDtos = attendanceClassesTaskDetailInnerServiceSMOImpl.queryWaitSendMsgDetail(attendanceClassesTaskDetailDto);

        if(inspectionTaskDetailDtos == null || inspectionTaskDetailDtos.size() < 1){
            return;
        }
        for (AttendanceClassesTaskDetailDto tmpAttendanceClassesTaskDetailDto : inspectionTaskDetailDtos) {
            try {
                dealAttendanceClassesTaskDetail(tmpAttendanceClassesTaskDetailDto, taskDto);
            } catch (Exception e) {
                logger.error("推送考勤提示失败", e);
            }
        }
    }

    /**
     * 处理考勤
     *
     * @param attendanceClassesTaskDetailDto
     * @param taskDto
     */
    private void dealAttendanceClassesTaskDetail(AttendanceClassesTaskDetailDto attendanceClassesTaskDetailDto, TaskDto taskDto) {

        CommunityMemberDto communityMemberDto = new CommunityMemberDto();
        communityMemberDto.setMemberId(attendanceClassesTaskDetailDto.getStoreId());
        communityMemberDto.setState(CommunityMemberDto.STATE_NORMAL);
        List<CommunityMemberDto> communityMemberDtos = communityMemberV1InnerServiceSMOImpl.queryCommunityMembers(communityMemberDto);

        if(communityMemberDtos == null || communityMemberDtos.size() < 1){
            return ;
        }

        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setWeChatType("1100");
        smallWeChatDto.setObjType(SmallWeChatDto.OBJ_TYPE_COMMUNITY);
        smallWeChatDto.setObjId(communityMemberDtos.get(0).getCommunityId());
        List<SmallWeChatDto> smallWeChatDtos = smallWeChatInnerServiceSMOImpl.querySmallWeChats(smallWeChatDto);
        if (smallWeChatDto == null || smallWeChatDtos.size() <= 0) {
            logger.info("未配置微信公众号信息,定时任务执行结束");
            return;
        }
        SmallWeChatDto weChatDto = smallWeChatDtos.get(0);
        SmallWechatAttrDto smallWechatAttrDto = new SmallWechatAttrDto();
        smallWechatAttrDto.setCommunityId(communityMemberDtos.get(0).getCommunityId());
        smallWechatAttrDto.setWechatId(weChatDto.getWeChatId());
        smallWechatAttrDto.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_TEMPLATE);
        List<SmallWechatAttrDto> smallWechatAttrDtos = smallWechatAttrInnerServiceSMOImpl.querySmallWechatAttrs(smallWechatAttrDto);
        if (smallWechatAttrDtos == null || smallWechatAttrDtos.size() <= 0) {
            logger.info("未配置微信公众号消息模板");
            return;
        }
        String templateId = smallWechatAttrDtos.get(0).getValue();
        String accessToken = WechatFactory.getAccessToken(weChatDto.getAppId(), weChatDto.getAppSecret());
        if (StringUtil.isEmpty(accessToken)) {
            logger.info("推送微信模板,获取accessToken失败:{}", accessToken);
            return;
        }
        String url = sendMsgUrl + accessToken;
        //根据 userId 查询到openId
        StaffAppAuthDto staffAppAuthDto = new StaffAppAuthDto();
        staffAppAuthDto.setStaffId(attendanceClassesTaskDetailDto.getStaffId());
        staffAppAuthDto.setAppType("WECHAT");
        List<StaffAppAuthDto> staffAppAuthDtos = staffAppAuthInnerServiceSMO.queryStaffAppAuths(staffAppAuthDto);
        if (staffAppAuthDtos == null || staffAppAuthDtos.size() < 1) {
            return;
        }
        String openId = staffAppAuthDtos.get(0).getOpenId();
        Data data = new Data();
        PropertyFeeTemplateMessage templateMessage = new PropertyFeeTemplateMessage();
        templateMessage.setTemplate_id(templateId);
        templateMessage.setTouser(openId);
        data.setFirst(new Content("您有新的考勤任务："));
        data.setKeyword1(new Content(attendanceClassesTaskDetailDto.getStaffName()+"-"+("1001".equals(attendanceClassesTaskDetailDto.getSpecCd())?"上班考勤":"下班考勤")));
        data.setKeyword2(new Content(attendanceClassesTaskDetailDto.getValue()));
        data.setKeyword3(new Content("请到考勤机及时考勤"));
        data.setRemark(new Content("如有疑问请联系管理员"));
        templateMessage.setData(data);

        logger.info("发送模板消息内容:{}", JSON.toJSONString(templateMessage));
        ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(url, JSON.toJSONString(templateMessage), String.class);
        logger.info("微信模板返回内容:{}", responseEntity);

    }

}
