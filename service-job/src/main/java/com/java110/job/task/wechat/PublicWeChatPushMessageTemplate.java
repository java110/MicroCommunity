package com.java110.job.task.wechat;

import com.alibaba.fastjson.JSON;
import com.java110.core.factory.WechatFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.fee.BillOweFeeDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.dto.smallWechatAttr.SmallWechatAttrDto;
import com.java110.dto.task.TaskDto;
import com.java110.entity.wechat.Content;
import com.java110.entity.wechat.Data;
import com.java110.entity.wechat.Miniprogram;
import com.java110.entity.wechat.PropertyFeeTemplateMessage;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.store.ISmallWeChatInnerServiceSMO;
import com.java110.intf.store.ISmallWechatAttrInnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserInnerServiceSMO;
import com.java110.job.quartz.TaskSystemQuartz;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.cache.UrlCache;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @program: MicroCommunity
 * @description: 微信公众号主动推送信息
 * @author: zcc
 * @create: 2020-06-15 13:35
 **/
@Component
public class PublicWeChatPushMessageTemplate extends TaskSystemQuartz {

    private static Logger logger = LoggerFactory.getLogger(PublicWeChatPushMessageTemplate.class);

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private ISmallWeChatInnerServiceSMO smallWeChatInnerServiceSMOImpl;

    @Autowired
    private ISmallWechatAttrInnerServiceSMO smallWechatAttrInnerServiceSMOImpl;

    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl;

    @Autowired
    private RestTemplate outRestTemplate;

    //模板信息推送地址
    private static String sendMsgUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

    //模板id
    private static String DEFAULT_TEMPLATE_ID = "ZF4j_ug2XW-UGwW1F-Gi4M1-51lpiu-PM89Oa6oZv6w";


    @Override
    protected void process(TaskDto taskDto) {
        logger.debug("开始执行微信模板信息推送" + taskDto.toString());

        // 获取小区
        List<CommunityDto> communityDtos = getAllCommunity();

        for (CommunityDto communityDto : communityDtos) {
            try {
                publishMsg(taskDto, communityDto);
            } catch (Exception e) {
                logger.error("推送消息失败", e);
            }
        }
    }

    private void publishMsg(TaskDto taskDto, CommunityDto communityDto) throws Exception {

//
//        String templateId = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.KEY_PROPERTY_FEE_TEMPLATE_ID);
//
//        templateId = StringUtil.isEmpty(templateId) ? DEFAULT_TEMPLATE_ID : templateId;

        //查询公众号配置
        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setWeChatType("1100");
        smallWeChatDto.setObjType(SmallWeChatDto.OBJ_TYPE_COMMUNITY);
        smallWeChatDto.setObjId(communityDto.getCommunityId());
        List<SmallWeChatDto> smallWeChatDtos = smallWeChatInnerServiceSMOImpl.querySmallWeChats(smallWeChatDto);

        if (smallWeChatDto == null || smallWeChatDtos.size() <= 0) {
            logger.error("未配置微信公众号信息,定时任务执行结束");
            return;
        }
        SmallWeChatDto weChatDto = smallWeChatDtos.get(0);


        SmallWechatAttrDto smallWechatAttrDto = new SmallWechatAttrDto();
        smallWechatAttrDto.setCommunityId(communityDto.getCommunityId());
        smallWechatAttrDto.setWechatId(weChatDto.getWeChatId());
        smallWechatAttrDto.setSpecCd(SmallWechatAttrDto.SPEC_CD_OWE_FEE_TEMPLATE);
        List<SmallWechatAttrDto> smallWechatAttrDtos = smallWechatAttrInnerServiceSMOImpl.querySmallWechatAttrs(smallWechatAttrDto);

        if (smallWechatAttrDtos == null || smallWechatAttrDtos.size() <= 0) {
            logger.error("未配置微信公众号消息模板");
            return;
        }

        String templateId = smallWechatAttrDtos.get(0).getValue();

        String accessToken = WechatFactory.getAccessToken(weChatDto.getAppId(), weChatDto.getAppSecret());

        if (StringUtil.isEmpty(accessToken)) {
            logger.error("推送微信模板,获取accessToken失败:{}", accessToken);
            return;
        }

        //根据小区id查询业主与公众号绑定信息
        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setCommunityId(weChatDto.getObjId());
        ownerAppUserDto.setAppType(OwnerAppUserDto.APP_TYPE_WECHAT);
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserInnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);

        if (ownerAppUserDtos.size() <= 0 || ownerAppUserDtos == null) {
            logger.error("未查询到业主与微信公众号绑定关系");
            return;
        }

        List<String> memberIdList = new ArrayList<>(ownerAppUserDtos.size());
        for (OwnerAppUserDto appUserDto : ownerAppUserDtos) {
            if (StringUtil.isEmpty(appUserDto.getMemberId()) || "-1".equals(appUserDto.getMemberId())) {
                continue;
            }
            memberIdList.add(appUserDto.getMemberId());
        }

        String[] memberIds = memberIdList.toArray(new String[memberIdList.size()]);
        //查询欠费信息
        BillOweFeeDto billOweFeeDto = new BillOweFeeDto();
        billOweFeeDto.setCommunityId(weChatDto.getObjId());
        billOweFeeDto.setOwnerIds(memberIds);
        billOweFeeDto.setState("1000");
        billOweFeeDto.setCurBill("T");
        List<BillOweFeeDto> billOweFeeDtos = feeInnerServiceSMOImpl.queryBillOweFees(billOweFeeDto);

        String sendTemplate = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.SEND_TEMPLATE_URL);
        if (StringUtil.isEmpty(sendTemplate)) {
            sendTemplate = sendMsgUrl;
        }
        String url = sendTemplate + accessToken;

        String oweRoomUrl = UrlCache.getOwnerUrl()+MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.OWE_FEE_PAGE);
        String oweCarUrl = UrlCache.getOwnerUrl()+MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.OWE_CAR_FEE_PAGE);
        Miniprogram miniprogram = null;
        if (oweRoomUrl.contains("@@")) {
            miniprogram = new Miniprogram();
            miniprogram.setAppid(oweRoomUrl.split("@@")[0]);
        }
        //车辆费用
        if (oweCarUrl.contains("@@")) {
            miniprogram = new Miniprogram();
            miniprogram.setAppid(oweCarUrl.split("@@")[0]);
        }

        String oweUrl = "";
        for (BillOweFeeDto fee : billOweFeeDtos) {
            oweUrl = FeeDto.PAYER_OBJ_TYPE_ROOM.equals(fee.getPayerObjType()) ? oweRoomUrl : oweCarUrl;
            for (OwnerAppUserDto appUserDto : ownerAppUserDtos) {
                try {
                    if (fee.getOwnerId().equals(appUserDto.getMemberId())) {
                        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(fee.getFeeEndTime());
                        Calendar now = Calendar.getInstance();
                        now.setTime(date);
//                    int year = now.get(Calendar.YEAR);
//                    int month = now.get(Calendar.MONTH);
                        Data data = new Data();
                        PropertyFeeTemplateMessage templateMessage = new PropertyFeeTemplateMessage();
                        templateMessage.setTemplate_id(templateId);
                        templateMessage.setTouser(appUserDto.getOpenId());
                        /*data.setFirst(new Content("物业费缴费提醒"));*/
                        data.setFirst(new Content(fee.getFeeTypeName() + "提醒"));
                        data.setKeyword1(new Content(fee.getPayerObjName()));
                        data.setKeyword2(new Content(fee.getBillAmountOwed()));
                        data.setKeyword3(new Content(DateUtil.dateTimeToDate(fee.getFeeEndTime()) + "至" + DateUtil.dateTimeToDate(fee.getDeadlineTime())));
                        data.setRemark(new Content("请您及时缴费,如有疑问请联系相关物业人员"));
                        if (!StringUtil.isEmpty(oweUrl)) {
                            if (miniprogram == null) {
                                templateMessage.setUrl(oweUrl + fee.getPayObjId() + "&wAppId=" + weChatDto.getAppId());
                            } else {
                                miniprogram.setPagepath(oweUrl.split("@@")[1] + fee.getPayObjId() + "&wAppId=" + weChatDto.getAppId());
                                templateMessage.setMiniprogram(miniprogram);
                            }
                        }
                        templateMessage.setData(data);
                        logger.info("发送模板消息内容:{}", JSON.toJSONString(templateMessage));
                        ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(url, JSON.toJSONString(templateMessage), String.class);
                        logger.info("微信模板返回内容:{}", responseEntity);
                    }
                } catch (Exception e) {
                    logger.error("推送账单异常", e);
                }
            }

            BillOweFeeDto tmpBillOweFeeDto = new BillOweFeeDto();
            tmpBillOweFeeDto.setFeeId(fee.getFeeId());
            tmpBillOweFeeDto.setCommunityId(fee.getCommunityId());
            tmpBillOweFeeDto.setBillId(fee.getBillId());
            tmpBillOweFeeDto.setState(BillOweFeeDto.STATE_SEND_OWNER);
            feeInnerServiceSMOImpl.updateBillOweFees(tmpBillOweFeeDto);

        }
    }
}
