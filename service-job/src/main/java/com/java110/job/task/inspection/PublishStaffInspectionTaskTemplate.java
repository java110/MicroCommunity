package com.java110.job.task.inspection;

import com.alibaba.fastjson.JSON;
import com.java110.core.factory.WechatFactory;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.inspection.InspectionTaskDetailDto;
import com.java110.dto.inspection.InspectionTaskDto;
import com.java110.dto.wechat.SmallWeChatDto;
import com.java110.dto.wechat.SmallWechatAttrDto;
import com.java110.dto.user.StaffAppAuthDto;
import com.java110.dto.task.TaskDto;
import com.java110.dto.wechat.Content;
import com.java110.dto.wechat.Data;
import com.java110.dto.wechat.PropertyFeeTemplateMessage;
import com.java110.intf.community.*;
import com.java110.intf.store.ISmallWeChatInnerServiceSMO;
import com.java110.intf.store.ISmallWechatAttrInnerServiceSMO;
import com.java110.intf.user.IStaffAppAuthInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.job.quartz.TaskSystemQuartz;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Component
public class PublishStaffInspectionTaskTemplate extends TaskSystemQuartz {

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
    private ISmallWeChatInnerServiceSMO smallWeChatInnerServiceSMOImpl;

    @Autowired
    private ISmallWechatAttrInnerServiceSMO smallWechatAttrInnerServiceSMOImpl;

    @Autowired
    private IStaffAppAuthInnerServiceSMO staffAppAuthInnerServiceSMO;

    @Autowired
    private RestTemplate outRestTemplate;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMO;


    //模板信息推送地址
    private static String sendMsgUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

    @Override
    protected void process(TaskDto taskDto) throws Exception {
        logger.debug("开始执行微信模板信息推送" + taskDto.toString());

        // 获取小区
        List<CommunityDto> communityDtos = getAllCommunity();

        for (CommunityDto communityDto : communityDtos) {
            try {
                publishStaffInspectionTask(taskDto, communityDto);
            } catch (Exception e) {
                logger.error("推送消息失败", e);
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
        inspectionTaskDetailDto.setQrCodeTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_B));
        inspectionTaskDetailDto.setSendFlag(InspectionTaskDetailDto.SEND_FLAG_N);
        List<InspectionTaskDetailDto> inspectionTaskDetailDtos = inspectionTaskDetailInnerServiceSMOImpl.queryInspectionTaskDetails(inspectionTaskDetailDto);

        for (InspectionTaskDetailDto inspectionTaskDetailDto1 : inspectionTaskDetailDtos) {
            dealInspectionTaskDetail(inspectionTaskDetailDto1, taskDto, communityDto);
        }
    }

    /**
     * 处理巡检计划
     *
     * @param inspectionTaskDetailDto
     * @param taskDto
     * @param communityDto
     */
    private void dealInspectionTaskDetail(InspectionTaskDetailDto inspectionTaskDetailDto, TaskDto taskDto, CommunityDto communityDto) {

        String startTime = inspectionTaskDetailDto.getPointStartTime();

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
        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setWeChatType("1100");
        smallWeChatDto.setObjType(SmallWeChatDto.OBJ_TYPE_COMMUNITY);
        smallWeChatDto.setObjId(communityDto.getCommunityId());
        List<SmallWeChatDto> smallWeChatDtos = smallWeChatInnerServiceSMOImpl.querySmallWeChats(smallWeChatDto);
        if (smallWeChatDto == null || smallWeChatDtos.size() <= 0) {
            logger.info("未配置微信公众号信息,定时任务执行结束");
            return;
        }
        SmallWeChatDto weChatDto = smallWeChatDtos.get(0);
        SmallWechatAttrDto smallWechatAttrDto = new SmallWechatAttrDto();
        smallWechatAttrDto.setCommunityId(communityDto.getCommunityId());
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
        staffAppAuthDto.setStaffId(inspectionTaskDetailDto.getPlanUserId());
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
        data.setFirst(new Content("您有新的巡检任务，巡检信息如下："));
        data.setKeyword1(new Content(inspectionTaskDetailDto.getInspectionName()));
        data.setKeyword2(new Content(inspectionTaskDetailDto.getPlanInsTime()));
        data.setKeyword3(new Content(inspectionTaskDetailDto.getInspectionName() + "未巡检，请及时巡检"));
        data.setRemark(new Content("如有疑问请联系管理员"));

        templateMessage.setData(BeanConvertUtil.beanCovertJson(data));


        String staffWechatUrl = MappingCache.getValue(MappingConstant.URL_DOMAIN,"STAFF_WECHAT_URL");
        staffWechatUrl =  staffWechatUrl
                    + "pages/excuteOneQrCodeInspection/excuteOneQrCodeInspection?inspectionId="
                    + inspectionTaskDetailDto.getInspectionId()
                    + "&inspectionName=" + inspectionTaskDetailDto.getInspectionName()
                    + "&itemId=" + inspectionTaskDetailDto.getItemId();


        templateMessage.setUrl(staffWechatUrl);
        logger.info("发送模板消息内容:{}", JSON.toJSONString(templateMessage));
        ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(url, JSON.toJSONString(templateMessage), String.class);
        logger.info("微信模板返回内容:{}", responseEntity);


        InspectionTaskDetailDto inspectionTaskDetailPo = new InspectionTaskDetailDto();
        inspectionTaskDetailPo.setTaskDetailId(inspectionTaskDetailDto.getTaskDetailId());
        inspectionTaskDetailPo.setSendFlag(InspectionTaskDetailDto.SEND_FLAG_Y);
        inspectionTaskDetailDto.setStatusCd("0");
        inspectionTaskDetailInnerServiceSMOImpl.updateInspectionTaskDetail(inspectionTaskDetailPo);

    }

}
