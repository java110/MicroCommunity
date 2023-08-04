package com.java110.job.task.wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.WechatFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.fee.BillOweFeeDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.wechat.SmallWeChatDto;
import com.java110.dto.wechat.SmallWechatAttrDto;
import com.java110.dto.task.TaskDto;
import com.java110.dto.wechat.Content;
import com.java110.dto.wechat.Data;
import com.java110.dto.wechat.Miniprogram;
import com.java110.dto.wechat.PropertyFeeTemplateMessage;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.store.ISmallWeChatInnerServiceSMO;
import com.java110.intf.store.ISmallWechatAttrInnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserInnerServiceSMO;
import com.java110.job.msgNotify.MsgNotifyFactory;
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


        //根据小区id查询业主与公众号绑定信息
        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setCommunityId(communityDto.getCommunityId());
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
        billOweFeeDto.setCommunityId(communityDto.getCommunityId());
        billOweFeeDto.setOwnerIds(memberIds);
        billOweFeeDto.setState("1000");
        billOweFeeDto.setCurBill("T");
        List<BillOweFeeDto> billOweFeeDtos = feeInnerServiceSMOImpl.queryBillOweFees(billOweFeeDto);

        String sendTemplate = MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.SEND_TEMPLATE_URL);
        if (StringUtil.isEmpty(sendTemplate)) {
            sendTemplate = sendMsgUrl;
        }

        String oweRoomUrl = UrlCache.getOwnerUrl() + MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.OWE_FEE_PAGE);
        String oweCarUrl = UrlCache.getOwnerUrl() + MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.OWE_CAR_FEE_PAGE);
//        Miniprogram miniprogram = null;
//        if (oweRoomUrl.contains("@@")) {
//            miniprogram = new Miniprogram();
//            miniprogram.setAppid(oweRoomUrl.split("@@")[0]);
//        }
//        //车辆费用
//        if (oweCarUrl.contains("@@")) {
//            miniprogram = new Miniprogram();
//            miniprogram.setAppid(oweCarUrl.split("@@")[0]);
//        }

        String oweUrl = "";
        JSONObject content = null;
        for (BillOweFeeDto fee : billOweFeeDtos) {
            oweUrl = FeeDto.PAYER_OBJ_TYPE_ROOM.equals(fee.getPayerObjType()) ? oweRoomUrl : oweCarUrl;
            for (OwnerAppUserDto appUserDto : ownerAppUserDtos) {
                try {
                    if (!fee.getOwnerId().equals(appUserDto.getMemberId())) {
                        continue;
                    }
                    content = new JSONObject();
                    content.put("feeTypeName", fee.getFeeTypeName());
                    content.put("payerObjName", fee.getPayerObjName());
                    content.put("billAmountOwed", fee.getBillAmountOwed());
                    content.put("date", DateUtil.dateTimeToDate(fee.getFeeEndTime()) + "~" + DateUtil.dateTimeToDate(fee.getDeadlineTime()));
                    content.put("url",oweUrl);
                    MsgNotifyFactory.sendOweFeeMsg(communityDto.getCommunityId(), appUserDto.getUserId(), content);
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
